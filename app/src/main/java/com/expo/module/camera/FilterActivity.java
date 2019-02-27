package com.expo.module.camera;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.BaseAdapterItemClickListener;
import com.expo.widget.AppBarView;
import com.expo.widget.decorations.SpaceDecoration;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageEmbossFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHighlightShadowFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSketchFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageToonFilter;

/**
 * 滤镜
 */
public class FilterActivity extends BaseActivity {

    @BindView(R.id.filter_img)
    ImageView filterImg;
    @BindView(R.id.filter_recycler)
    RecyclerView filterRecycler;

    private TextView mConfirmBtn;
    private GPUImage gpuImage;
    private String imgUrl;
    private FilterAdapter mAdapter;
    private ArrayList<Bitmap> mImagesList = new ArrayList<>();
    private ArrayList<String> mImagesTitleList = new ArrayList<>();
    private ArrayList<String> mImagesUrlList = new ArrayList<>();
    private int mPosition = 0;
    private static Handler handler=new Handler();

    @Override
    protected int getContentView() {
        return R.layout.activity_filter;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(0, "原图");
        initTitleRightTextView(true);

        imgUrl = getIntent().getStringExtra("imgUrl");
        mImagesUrlList.add(imgUrl);
        //初始化图片
        Picasso.with(this).load("file://" + imgUrl).into(filterImg);

        initRecyclerView();

        getGPUImageFromAssets1();
        getGPUImageFromAssets2();
        getGPUImageFromAssets3();
        getGPUImageFromAssets4();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateList();
            }
        },3000);
    }

    private void updateList(){
        if(mImagesList.size()>=5){
            mAdapter.refresh(mImagesList, mImagesTitleList, 0);
            for(int i=1;i<mImagesList.size();i++){
                putImg(mImagesList.get(i));
            }
        }else {
            updateList();
        }
    }

    public void initTitleRightTextView(boolean isShow) {
        if (null == mConfirmBtn) {
            mConfirmBtn = new TextView(this);
            ((AppBarView) getTitleView()).setRightView(mConfirmBtn);
        }
        mConfirmBtn.setTextAppearance(this, R.style.TextSizeWhite14);
        mConfirmBtn.setText(R.string.confirm);
        mConfirmBtn.setTextColor(getResources().getColor(R.color.color_333));
        mConfirmBtn.setGravity(Gravity.CENTER);
        mConfirmBtn.setVisibility(isShow ? View.VISIBLE : View.GONE);
        mConfirmBtn.setOnClickListener(v -> {
            EventBus.getDefault().post(mImagesUrlList.get(mPosition));
            finish();
        });
    }

    @Override
    protected boolean hasPresenter() {
        return false;
    }

    public static void startActivity(@NonNull Context context, String imgUrl) {
        Intent in = new Intent(context, FilterActivity.class);
        in.putExtra("imgUrl", imgUrl);
        context.startActivity(in);
    }

    private void initRecyclerView() {
        mAdapter = new FilterAdapter(this, "file://" + imgUrl);
        filterRecycler.setLayoutManager(new GridLayoutManager(this, 5));
        filterRecycler.addItemDecoration(new SpaceDecoration((int) getResources().getDimension(R.dimen.dms_10)));
        filterRecycler.setAdapter(mAdapter);

        mImagesList.add(null);
        mImagesTitleList.add("原图");
        mAdapter.setClickListener(mClickListener);

        mAdapter.refresh(mImagesList, mImagesTitleList, 0);
    }

    BaseAdapterItemClickListener<Integer> mClickListener = new BaseAdapterItemClickListener<Integer>() {
        @Override
        public void itemClick(View view, int position, Integer o) {
            mPosition = position;
            setTitle(0, mImagesTitleList.get(position));
            if (position == 0) {
                Picasso.with(FilterActivity.this).load("file://" + imgUrl).into(filterImg);
            } else {
                filterImg.setImageBitmap(mImagesList.get(position));
            }
            mAdapter.refresh(mImagesList, mImagesTitleList, position);
        }
    };

    //怀旧
    public void getGPUImageFromAssets1() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                try {
                    FileInputStream fis = new FileInputStream(imgUrl);
                    bitmap = BitmapFactory.decodeStream(fis);
                } catch (IOException e) {
                    Log.e("GPUImage", "Error");
                }
                gpuImage = new GPUImage(FilterActivity.this);
                gpuImage.setImage(bitmap);
                gpuImage.setFilter(new GPUImageSepiaFilter());
                bitmap = gpuImage.getBitmapWithFilterApplied();
                mImagesList.add(bitmap);
                mImagesTitleList.add("怀旧");
            }
        }).start();
    }
    //负片
    public void getGPUImageFromAssets2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                try {
                    FileInputStream fis = new FileInputStream(imgUrl);
                    bitmap = BitmapFactory.decodeStream(fis);
                } catch (IOException e) {
                    Log.e("GPUImage", "Error");
                }
                gpuImage = new GPUImage(FilterActivity.this);
                gpuImage.setImage(bitmap);
                gpuImage.setFilter(new GPUImageToonFilter());//卡通效果
                bitmap = gpuImage.getBitmapWithFilterApplied();
                mImagesList.add(bitmap);
                mImagesTitleList.add("负片");
            }
        }).start();
    }
    //灰白
    public void getGPUImageFromAssets3() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                try {
                    FileInputStream fis = new FileInputStream(imgUrl);
                    bitmap = BitmapFactory.decodeStream(fis);
                } catch (IOException e) {
                    Log.e("GPUImage", "Error");
                }
                gpuImage = new GPUImage(FilterActivity.this);
                gpuImage.setImage(bitmap);
                gpuImage.setFilter(new GPUImageSketchFilter());//素描x效果
                bitmap = gpuImage.getBitmapWithFilterApplied();
                mImagesList.add(bitmap);
                mImagesTitleList.add("灰白");
            }
        }).start();
    }
    //浮雕
    public void getGPUImageFromAssets4() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                try {
                    FileInputStream fis = new FileInputStream(imgUrl);
                    bitmap = BitmapFactory.decodeStream(fis);
                } catch (IOException e) {
                    Log.e("GPUImage", "Error");
                }
                gpuImage = new GPUImage(FilterActivity.this);
                gpuImage.setImage(bitmap);
                gpuImage.setFilter(new GPUImageEmbossFilter());
                bitmap = gpuImage.getBitmapWithFilterApplied();
                mImagesList.add(bitmap);
                mImagesTitleList.add("浮雕");
            }
        }).start();
    }

    private void putImg(Bitmap mBitmap){
        String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/expo/tmp/";
        Random random = new Random();
        String fileName = String.valueOf(random.nextInt(Integer.MAX_VALUE));
        try {
            File file = new File(dir + fileName + ".jpg");
            FileOutputStream out = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            mImagesUrlList.add(dir + fileName + ".jpg");
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
