package com.expo.module.distinguish;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.ExpoApp;
import com.expo.base.utils.FileUtils;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.DistinguishContract;
import com.expo.entity.Plant;
import com.expo.network.response.GetBaiduDisting_Rsb;
import com.expo.utils.CameraManager;
import com.expo.utils.Constants;
import com.expo.utils.ICameraManager;
import com.expo.widget.ViewfinderView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 识花界面
 */
public class DistinguishActivity extends BaseActivity<DistinguishContract.Presenter> implements DistinguishContract.View
        , View.OnClickListener/*, CompoundButton.OnCheckedChangeListener*/, Camera.PictureCallback {

    //    @BindView(R.id.cl_distinguish_root)
//    View mRoot;
    @BindView(R.id.sv_distinguish_preview)
    TextureView mDvPreview;
    @BindView(R.id.vfv_distinguish_viewfinder)
    ViewfinderView mViewfinder;

    private String mCropFilePath;
    private boolean recoveryScan = true;
    private ICameraManager cameraManager;
    private Dialog dialog;

    @Override
    protected int getContentView() {
        return R.layout.activity_distinguish;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        cameraManager = new CameraManager();
        cameraManager.setDisplayView( mDvPreview );                                     //设置相机预画面览显示视图
        cameraManager.startPreview();
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (recoveryScan) {                                                                         //需要启动扫描界面
            mViewfinder.startScanAnimation();
        } else {
            recoveryScan = true;
        }
    }

    @Override
    protected void onPause() {
        mViewfinder.stopScanAnimation();                                                            //暂停扫描界面
        super.onPause();
    }

    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent( context, DistinguishActivity.class );
        context.startActivity( in );
    }

    @Override
    @OnClick({R.id.iv_distinguish_back, R.id.iv_distinguish_take_picture})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_distinguish_back:
                onBackPressed();
                break;
            case R.id.iv_distinguish_take_picture:
                cameraManager.tackPicture( this );                             //拍照
                mViewfinder.stopScanAnimation();
                break;
            case R.id.distinguish_result_continue:
                // 继续识别
                dialog.dismiss();
//                cameraManager.setImmediatelyPreview(true);
                restartPreview();
                break;
//            case R.id.tv_distinguish_pic:   // 相册
////                cameraManager.setImmediatelyPreview(false);
//                Intent in = new Intent(Intent.ACTION_PICK);
//                in.setType("image/*");
//                startActivityForResult(in, Constants.RequestCode.REQ_SELECT_IMAGE);
//                break;
        }
    }

    /**
     * 相机重新开始预览
     */
    private void restartPreview() {
        cameraManager.startPreview();
        //扫描线重新开始动画扫描
        mViewfinder.startScanAnimation();
    }

    /* 裁剪图片 */
    private void crop(Uri uri) {
        File file = FileUtils.createFile( "image", Constants.Config.CROP_SAVE_PATH );
        if (file != null) {
            mCropFilePath = file.getPath();
            Intent in = new Intent( "com.android.camera.action.CROP" );
            in.setDataAndType( uri, "image/*" );
            in.putExtra( "crop", true );
            in.putExtra( "aspectX", Constants.Config.USERINFO_CROP_IMAGE_ASPECT_X );
            in.putExtra( "aspectY", Constants.Config.USERINFO_CROP_IMAGE_ASPECT_Y );
            in.putExtra( "outputX", Constants.Config.USERINFO_CROP_IMAGE_OUTPUT_X );
            in.putExtra( "outputY", Constants.Config.USERINFO_CROP_IMAGE_OUTPUT_Y );
            in.putExtra( MediaStore.EXTRA_OUTPUT, Uri.fromFile( file ) );
            startActivityForResult( in, Constants.RequestCode.REQ_CROP );
        } else {
            ToastHelper.showShort( "编辑图片失败" );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == Constants.RequestCode.REQ_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            //相册选图后裁剪
            crop( data.getData() );
        } else if (Constants.RequestCode.REQ_CROP == requestCode && resultCode == RESULT_OK) {
            Bitmap bmp = BitmapFactory.decodeFile( mCropFilePath );
            //将裁剪后的图片显示在扫描框内
            mViewfinder.setDistinguishBitmap( bmp );
            recoveryScan = false;
            //裁剪后识别
//            distinguishPlant(bmp);    // 植物所
            mPresenter.distinguishPlant( mCropFilePath );     // 百度云
        } else if (resultCode == RESULT_CANCELED) {
//            cameraManager.setImmediatelyPreview(true);
        }
    }

//    //手电筒开启改变
//    @OnCheckedChanged(R.id.cb_distinguish_torch)
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        cameraManager.toggleLight(isChecked);
//    }

    //拍照回调
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Bitmap bmp = BitmapFactory.decodeByteArray( data, 0, data.length );
        WindowManager wm = (WindowManager) ExpoApp.getApplication().getSystemService( Context.WINDOW_SERVICE );
        int orientation = wm.getDefaultDisplay().getOrientation();
        if (orientation == Surface.ROTATION_0) {//图片按照屏幕方向旋转
            Matrix matrix = new Matrix();
            matrix.postRotate( 90 );
            bmp = Bitmap.createBitmap( bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true );
        }
        //将拍照的图片按照扫描框的大小位置进行裁剪
        Rect bounds = mViewfinder.getViewfinderBounds( bmp.getWidth(), bmp.getHeight() );
        bmp = Bitmap.createBitmap( bmp, bounds.left, bounds.top, bounds.width(), bounds.height() );
        File file = new File( Environment.getExternalStorageDirectory(), Constants.Config.CROP_SAVE_PATH + System.currentTimeMillis() + ".jpg" );
        file = FileUtils.createFile( file );
        FileUtils.saveBitmap( file, bmp );
//        distinguishPlant(bmp);  // 植物园
        mPresenter.distinguishPlant( file.getPath() );       // 百度云
    }

    /*
     * 识别拍照或相册选择的图片
     */
    private void distinguishPlant(Bitmap bmp) {
        FileUtils.compressToFit( bmp, 700 * 1024, (bytes) -> {
            final String imgStr = Base64.encodeToString( bytes, Base64.DEFAULT );
            runOnUiThread( () -> mPresenter.distinguishPlant( imgStr ) );
        } );
    }

//    /**
//     * 显示识别植物的结果    植物所接口
//     *
//     * @param plant
//     */
//    private void showDistinguishResult(Plant plant) {
//        dialog = new Dialog(this, R.style.BottomActionSheetDialogStyle);
//        //填充对话框的布局
//        View v = LayoutInflater.from(this).inflate(R.layout.layout_distinguish_result, null);
//        //初始化控件
//        SimpleDraweeView imageView = v.findViewById(R.id.distinguish_result_img);
//        TextView tvName = v.findViewById(R.id.distinguish_result_name);
//        TextView tvIntro = v.findViewById(R.id.distinguish_result_intro);
//        TextView btnContinue = v.findViewById(R.id.distinguish_result_continue);
//        btnContinue.setOnClickListener(this);
//        imageView.setImageURI(plant.ImageUrl);
//        tvName.setText(plant.Name+" "+plant.LatinName);
//        tvIntro.setText(plant.Family+" "+plant.Genus);
//        //将布局设置给Dialog
//        dialog.setContentView(v);
//        //获取当前Activity所在的窗体
//        Window dialogWindow = dialog.getWindow();
//        if(dialogWindow == null){
//            return;
//        }
//        //设置Dialog从窗体底部弹出
//        dialogWindow.setGravity(Gravity.BOTTOM);
//        //获得窗体的属性
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
////        lp.y = 20;//设置Dialog距离底部的距离
//        //将属性设置给窗体
//        dialogWindow.setAttributes(lp);
//        dialog.show();//显示对话框
//    }

    /**
     * 显示识别植物的结果    百度云接口
     *
     * @param plant
     */
    private void showDistinguishResult(GetBaiduDisting_Rsb plant) {
        dialog = new Dialog( this, R.style.BottomActionSheetDialogStyle );
        //填充对话框的布局
        View v = LayoutInflater.from( this ).inflate( R.layout.layout_distinguish_result, null );
        //初始化控件
        SimpleDraweeView imageView = v.findViewById( R.id.distinguish_result_img );
        TextView tvName = v.findViewById( R.id.distinguish_result_name );
        TextView tvIntro = v.findViewById( R.id.distinguish_result_intro );
        TextView btnContinue = v.findViewById( R.id.distinguish_result_continue );
        btnContinue.setOnClickListener( this );
        imageView.setImageURI( plant.baike_info.image_url );
        tvName.setText( plant.name + "   准确度:" + (int) (plant.score * 100) + "%" );
        tvIntro.setText( plant.baike_info.description );
        //将布局设置给Dialog
        dialog.setContentView( v );
        dialog.setCancelable( false );
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow == null) {
            return;
        }
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM );
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.y = 20;//设置Dialog距离底部的距离
        //将属性设置给窗体
        dialogWindow.setAttributes( lp );
        dialog.show();//显示对话框
    }

    @Override
    public void onDistinguishComplete(List<Plant> plants) {     // 植物所接口返回
        if (plants != null && !plants.isEmpty()) {  //识别结果不为空，显示识别结果
            Plant plant = plants.get( 0 );
//            showDistinguishResult(plant);
        } else {
            ToastHelper.showShort( "识别失败" );
            restartPreview();
        }
        hideLoadingView();
    }

    @Override
    public void onBaiduDistinguishComplete(GetBaiduDisting_Rsb plant) {     // 百度云接口返回
        if (null == plant) {
            ToastHelper.showShort( "识别失败" );
            restartPreview();
            return;
        } else if (plant.name.equals( "非植物" )) {
            ToastHelper.showShort( "您识别的不是植物哦" );
            restartPreview();
            return;
        }
        showDistinguishResult( plant );
    }
}
