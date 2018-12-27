package com.expo.module.main.find.publish;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.donkingliang.imageselector.utils.ImageSelectorUtils;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.BaseAdapterItemClickListener;
import com.expo.base.ExpoApp;
import com.expo.base.utils.CheckUtils;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.FindPublishContract;
import com.expo.entity.Find;
import com.expo.map.LocationManager;
import com.expo.map.MapUtils;
import com.expo.module.camera.CameraActivity;
import com.expo.module.mine.adapter.WorkAdapter;
import com.expo.utils.CommUtils;
import com.expo.utils.Constants;
import com.expo.widget.decorations.SpaceDecoration;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.expo.utils.Constants.EXTRAS.EXTRAS;

public class FindPublishActivity extends BaseActivity<FindPublishContract.Presenter>
        implements FindPublishContract.View, View.OnClickListener {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.find_publish_edit)
    EditText mEtEdit;
    @BindView(R.id.edit_count)
    TextView mTvEditCount;
    @BindView(R.id.find_publish_type_text)
    TextView mTvTypeText;
    @BindView(R.id.find_publish_location)
    TextView mTvLocation;

    ArrayList<String> mImageList;
    FindPublishAdapter mAdapter;

    WorkAdapter mWorkAdapter;
    List<String> mWorkList = new ArrayList<String>();

    int mCameraPosition = 0;
    String mFindType;
    String mLocation = "";

    BaseAdapterItemClickListener<Integer> mClickListener = new BaseAdapterItemClickListener<Integer>() {
        @Override
        public void itemClick(View view, int position, Integer o) {
            if (position == mImageList.size())
                showImgSelectDialog();
        }
    };

    BaseAdapterItemClickListener<Integer> mDeleteListener = new BaseAdapterItemClickListener<Integer>() {
        @Override
        public void itemClick(View view, int position, Integer o) {
            removeCameraPosition(position);
            mImageList.remove(position);
            mAdapter.refresh(mImageList);
        }
    };

    AMap.OnMyLocationChangeListener mLocationChangeListener = new AMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            mLocation = ((AMapLocation)location).getPoiName();
            mTvLocation.setText(mLocation);
        }
    };

    private void initEditText() {
        String token = "<image>";
        ImageSpan span = new ImageSpan(this, R.mipmap.snow, 1);
        SpannableStringBuilder spanStr = new SpannableStringBuilder();
        spanStr.append(token);
        spanStr.append(getResources().getString(R.string.publish_edit_hint));

        spanStr.setSpan(span, 0, token.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        mEtEdit.setHint(spanStr);

        mEtEdit.setSelection(mEtEdit.getText().length());
        LocationManager.getInstance().registerLocationListener(mLocationChangeListener);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_find_publish;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(1, R.string.publish);
        initRecyclerView();
        initWorkAdapter();
        initEditText();
        mEtEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTvEditCount.setText(s.length() + "/50");
            }
        });
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    private void initRecyclerView() {
        mAdapter = new FindPublishAdapter(this);
        mRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        mRecycler.addItemDecoration(new SpaceDecoration((int) getResources().getDimension(R.dimen.dms_10)));
        mRecycler.setAdapter(mAdapter);

        mImageList = new ArrayList<>();
        mAdapter.setClickListener(mClickListener);
        mAdapter.setDeleteListener(mDeleteListener);

    }

    public static void startActivity(@NonNull Context context, int type) {
        Intent in = new Intent(context, FindPublishActivity.class);
        in.putExtra(EXTRAS, type);
        context.startActivity(in);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            if (requestCode == Constants.RequestCode.REQUEST111 && data != null) {
                resetCameraPosition();
                mImageList.addAll(data.getStringArrayListExtra(
                        ImageSelectorUtils.SELECT_RESULT));
                mAdapter.refresh(mImageList);
            } else if (requestCode == Constants.RequestCode.REQ_TO_CAMERA) {
                String path = data.getStringExtra(Constants.EXTRAS.EXTRAS);
                if ((!path.endsWith(".mp4") || (path.endsWith(".mp4") && mImageList.size() == 0)) && mImageList.size() < 9) {
                    mCameraPosition = mCameraPosition | 1 << mImageList.size();
                    mImageList.add(path);
                    mAdapter.refresh(mImageList);
                } else {
                    ToastHelper.showShort(R.string.img_limit);
                }
            }
    }

    public void initWorkAdapter() {
        mWorkAdapter = new WorkAdapter(this);
        mWorkAdapter.setSource(Constants.ContactsType.FIND_TYPE_MAP);
        mWorkList.add("1");
        mWorkList.add("2");
        mWorkList.add("3");
        mWorkList.add("4");
        mWorkList.add("5");
        mWorkAdapter.setData(mWorkList);
        // 默认不选择类别，合并代码时删除掉此处注释
//        mFindType = "3";
//        mWorkAdapter.mPosition = 2;
//        mWorkAdapter.notifyDataSetChanged();
//        mTvTypeText.setText(Constants.ContactsType.FIND_TYPE_MAP.get(mFindType));
    }

    @OnClick(R.id.find_publish_type)
    public void clickType(View view) {
        CommUtils.hideKeyBoard(this, view);
        showTypeSelectorDialog();
    }

    private void showTypeSelectorDialog() {
        DialogPlus dialog = DialogPlus.newDialog(this)
                .setContentHolder(new ListHolder())
                .setAdapter(mWorkAdapter)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        mFindType = mWorkList.get(position);
                        mTvTypeText.setText(Constants.ContactsType.FIND_TYPE_MAP.get(mFindType));
                        mWorkAdapter.mPosition = position;
                        dialog.dismiss();
                    }
                })
                .setGravity(Gravity.BOTTOM)
                .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                .setContentHeight(SizeUtils.dp2px(250))
                .create();
        dialog.show();
    }

    @OnClick(R.id.find_publish_submit)
    public void submit(View view) {
        Find find = getFindPublish();
        if (find != null) {
            find.location = mLocation;
            mPresenter.addSociety(find);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_record_explain:
                dialog.dismiss();
                if (isMp4()) {
                    ToastHelper.showShort(R.string.img_limit);
                    break;
                }
                startActivityForResult(new Intent(FindPublishActivity.this, CameraActivity.class), Constants.RequestCode.REQ_TO_CAMERA);
                break;
            case R.id.image_album:
                dialog.dismiss();
                if (isMp4()) {
                    ToastHelper.showShort(R.string.img_limit);
                    break;
                }
                goImageSelector();
                break;
            case R.id.cancle:
                dialog.dismiss();
                break;
        }
    }

    private Dialog dialog;

    private boolean isMp4() {
        if (mImageList.size() == 1) {
            return mImageList.get(0).endsWith("mp4");
        }
        return false;
    }

    private void showImgSelectDialog() {
        dialog = new Dialog(this, R.style.BottomActionSheetDialogStyle);
        //填充对话框的布局
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_image_select, null);
        //初始化控件
        TextView tvCamera1 = v.findViewById(R.id.image_record);
        TextView tvCamera2 = v.findViewById(R.id.image_record_explain);
        TextView tvPhoto = v.findViewById(R.id.image_album);
        TextView tvCancle = v.findViewById(R.id.cancle);
        tvCamera1.setOnClickListener(this);
        tvCamera2.setOnClickListener(this);
        tvPhoto.setOnClickListener(this);
        tvCancle.setOnClickListener(this);
        //将布局设置给Dialog
        dialog.setContentView(v);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow == null) {
            return;
        }
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;//设置Dialog距离底部的距离
        //将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }

    private void goImageSelector() {
        int count = getCameraImageCount();
        if (count >= 9)
            ToastHelper.showShort(R.string.today);
        else
            ImageSelector.builder()
                    .useCamera(true) // 设置是否使用拍照
                    .setSingle(false)  //设置是否单选
                    .setMaxSelectCount(9 - count) // 图片的最大选择数量，小于等于0时，不限数量。
                    .setSelected(mImageList) // 把已选的图片传入默认选中。
                    .setViewImage(true) //是否点击放大图片查看,，默认为true
                    .start(FindPublishActivity.this, Constants.RequestCode.REQUEST111); // 打开相册
    }

    private int getCameraImageCount() {
        int count = 0;
        int cameraPosition = mCameraPosition;
        while (cameraPosition > 0) {
            if ((cameraPosition | 1) == cameraPosition) {
                count++;
            }
            cameraPosition = cameraPosition >> 1;
        }
        return count;
    }

    private void resetCameraPosition() {
        for (int i = mImageList.size() - 1; i >= 0; i--) {
            if ((mCameraPosition | 1 << i) != mCameraPosition)
                mImageList.remove(i);
        }

        mCameraPosition = 0;
        for (int i = 0; i < mImageList.size(); i++) {
            mCameraPosition += 1 << i;
        }
    }

    private void removeCameraPosition(int position) {
        int right = mCameraPosition & ((1 << position) - 1);
        int left = mCameraPosition >> (position + 1);
        mCameraPosition = (left << position) + right;

    }

    @Override
    public void complete() {
        ToastHelper.showShort(getResources().getString(R.string.submit_success));
        finish();
    }

    @Override
    protected void onDestroy() {
        LocationManager.getInstance().unregisterLocationListener(mLocationChangeListener);
        super.onDestroy();
    }

    private Find getFindPublish() {
        if (CheckUtils.isEmtpy(mEtEdit.getText().toString(), R.string.check_string_empty_title, true))
            return null;
        if (mImageList.size() == 0) {
            ToastHelper.showShort(R.string.check_string_empty_image_video);
            return null;
        }
        if (StringUtils.isEmpty(mTvTypeText.getText().toString())) {
            showTypeSelectorDialog();
            return null;
        }

        Find find = new Find();
        find.caption = mEtEdit.getText().toString();
        find.subtime = TimeUtils.millis2String(TimeUtils.getNowMills(), new SimpleDateFormat(Constants.TimeFormat.TYPE_ALL));
        find.mobile = ExpoApp.getApplication().getUser().getMobile();
        find.uid = ExpoApp.getApplication().getUser().getUid();
        find.uname = ExpoApp.getApplication().getUser().getNick();
        find.upic = ExpoApp.getApplication().getUser().getPhotoUrl();
        find.kind = mFindType;
        if (mImageList.size() == 1 && mImageList.get(0).endsWith(".mp4")) find.type = 1;
        else find.type = 0;
        for (int i = 0; i < mImageList.size(); i++) {
            find.setUrl(i, mImageList.get(i));
        }

        return find;
    }

}
