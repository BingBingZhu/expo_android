package com.expo.module.online.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.utils.LogUtils;
import com.expo.contract.VRImageContract;
import com.expo.entity.VrInfo;
import com.expo.module.online.detail.widget.VRImageView;
import com.expo.utils.CommUtils;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.expo.widget.AppBarView;
import com.expo.widget.decorations.SpaceDecoration;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class VRImageActivity extends BaseActivity<VRImageContract.Presenter> implements VRImageContract.View {
    @BindView(R.id.vr_image_title)
    AppBarView mAppBarView;
    @BindView(R.id.vr_image_recycler)
    RecyclerView mRvRecycler;
    @BindView(R.id.vr_image_frame)
    FrameLayout mFrame;
    @BindView(R.id.vr_image_show)
    TextView mTvShow;

    CommonAdapter mAdapter;

    List<VrInfo> mData;
    View mSelectView;

    VRImageView mVRView;

    @Override
    protected int getContentView() {
        return R.layout.activity_vr_image;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mAppBarView.setBackgroundResource(R.color.white);
        mAppBarView.setTitle("全景图片");
        initTitleRightTextView();

        Long id = getIntent().getLongExtra(Constants.EXTRAS.EXTRAS, 0);

        mVRView = new VRImageView(this);
        mData = new ArrayList<>();
        mRvRecycler.setAdapter(mAdapter = new CommonAdapter<VrInfo>(this, R.layout.item_vr_image, mData) {
            @Override
            protected void convert(ViewHolder holder, VrInfo o, int position) {
                Picasso.with(VRImageActivity.this).load(CommUtils.getFullUrl(mData.get(position).getPic())).into((ImageView) holder.getView(R.id.item_vr_img));
                holder.itemView.setOnClickListener(v -> {
                    if (mSelectView != null)
                        mSelectView.setSelected(false);
                    mSelectView = v;
                    mSelectView.setSelected(true);
                    mVRView.setVrInfo(mData.get(position));
                    mAppBarView.setTitle(LanguageUtil.chooseTest(mData.get(position).getCaption(), mData.get(position).getCaptionEn()));
                });
            }

        });
        mRvRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRvRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        mRvRecycler.addItemDecoration(new SpaceDecoration((int) getResources().getDimension(R.dimen.dms_36)));
        mPresenter.loadVrRecommend(id);
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    @Override
    protected void onDestroy() {
        mVRView.onDestroy();
        mVRView = null;
        super.onDestroy();
    }

    @OnClick(R.id.title_back)
    public void onClick(View v){
        finish();
    }

    @OnClick(R.id.vr_image_show)
    public void clickImageShow(View view) {
        view.setSelected(!view.isSelected());
        if (view.isSelected()) {
            mTvShow.setText(R.string.hide);
            mRvRecycler.setVisibility(View.VISIBLE);
            mAppBarView.setVisibility(View.VISIBLE);
        } else {
            mTvShow.setText(R.string.show);
            mRvRecycler.setVisibility(View.GONE);
            mAppBarView.setVisibility(View.GONE);
        }
    }

    public static void startActivity(Context context, Long id) {
        Intent intent = new Intent(context, VRImageActivity.class);
        intent.putExtra(Constants.EXTRAS.EXTRAS, id);
        context.startActivity(intent);
    }

    public void initTitleRightTextView() {
        ImageView shareView = new ImageView(this);
        mAppBarView.setRightView(shareView);
        shareView.setImageResource(R.mipmap.share_icon);
        shareView.setOnClickListener(v -> {
//            ShareUtil.showShare(VRImageActivity.this,
//                    LanguageUtil.chooseTest(mEncyclopedias.caption, mEncyclopedias.captionEn),
//                    LanguageUtil.chooseTest(mEncyclopedias.remark, mEncyclopedias.remarkEn),
//                    CommUtils.getFullUrl(mEncyclopedias.picUrl),
//                    mUrl + "&data_type=0",
//                    mPlatformActionListener);
            LogUtils.d("Tag", "share");
        });
    }

    @Override
    public void freshVrList(List<VrInfo> list) {
        if (list != null) ;
        mData.clear();
        mData.addAll(list);
        mAdapter.notifyDataSetChanged();

        mFrame.addView(mVRView.getVrVideoView());
        mVRView.setVrInfo(list.get(0));
        mVRView.mFullScreen.setVisibility(View.GONE);
        mTvShow.setSelected(true);
        mRvRecycler.setVisibility(View.VISIBLE);
    }
}
