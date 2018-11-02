package com.expo.module.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.donkingliang.imageselector.utils.ImageSelector;
import com.donkingliang.imageselector.utils.ImageSelectorUtils;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.BaseAdapterItemClickListener;
import com.expo.contract.SeekHelpContract;
import com.expo.module.service.adapter.SeekHelpAdapter;
import com.expo.utils.Constants;
import com.expo.widget.decorations.SpaceDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.expo.utils.Constants.EXTRAS.EXTRAS;

/*
 * 游客求助，0:医疗救助、1:人员走失、2:寻物启事、3:治安举报、4:问询咨询通用页面
 */
public class SeekHelpActivity extends BaseActivity<SeekHelpContract.Presenter> implements SeekHelpContract.View {

    @BindView(R.id.seek_help_image_selector)
    RecyclerView mRecycler;
    @BindView(R.id.seek_help_text3)
    EditText mEtEdit;

    ArrayList<String> mImageList;
    SeekHelpAdapter mAdapter;

    BaseAdapterItemClickListener<Integer> mClickListener = new BaseAdapterItemClickListener<Integer>() {
        @Override
        public void itemClick(View view, int position, Integer o) {
            ImageSelector.builder()
                    .useCamera(true) // 设置是否使用拍照
                    .setSingle(false)  //设置是否单选
                    .setMaxSelectCount(3) // 图片的最大选择数量，小于等于0时，不限数量。
                    .setSelected(mImageList) // 把已选的图片传入默认选中。
                    .setViewImage(true) //是否点击放大图片查看,，默认为true
                    .start(SeekHelpActivity.this, Constants.RequestCode.REQUEST111); // 打开相册
        }
    };

    BaseAdapterItemClickListener<Integer> mDeleteListener = new BaseAdapterItemClickListener<Integer>() {
        @Override
        public void itemClick(View view, int position, Integer o) {
            mImageList.remove(position);
            mAdapter.refresh(mImageList);
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_seek_help;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(0, getIntent().getStringExtra(Constants.EXTRAS.EXTRA_TITLE));
        initRecyclerView();
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    private void initRecyclerView() {
        mAdapter = new SeekHelpAdapter(this);
        mRecycler.setLayoutManager(new GridLayoutManager(this, 4));
        mRecycler.addItemDecoration(new SpaceDecoration((int) getResources().getDimension(R.dimen.dms_10)));
        mRecycler.setAdapter(mAdapter);

        mImageList = new ArrayList<>();
        mAdapter.setClickListener(mClickListener);
        mAdapter.setDeleteListener(mDeleteListener);

    }

    /**
     * 启动游客服务求助界面
     *
     * @param context
     * @param type    求助类型
     */
    public static void startActivity(@NonNull Context context, int type) {
        Intent in = new Intent(context, SeekHelpActivity.class);
        in.putExtra(EXTRAS, type);
        context.startActivity(in);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)

            if (requestCode == Constants.RequestCode.REQUEST111 && data != null) {
                mImageList.clear();
                mImageList.addAll(data.getStringArrayListExtra(
                        ImageSelectorUtils.SELECT_RESULT));
                mAdapter.refresh(mImageList);
            }
    }

    @OnClick(R.id.seek_help_submit)
    public void submit(View view) {

    }

    @OnClick(R.id.seek_help_navigation)
    public void navigation(View view) {

    }

    @OnClick(R.id.seek_help_text4)
    public void location(View view) {
        startActivity(new Intent(this, LocationDescribeActivity.class));
    }

    @OnClick(R.id.seek_help_phone)
    public void phone(View view) {

    }

}
