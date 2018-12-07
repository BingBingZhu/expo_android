package com.expo.module.main.find.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.blankj.utilcode.util.StringUtils;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.contract.FindDetailContract;
import com.expo.contract.presenter.FindDetailPresenterImpl;
import com.expo.entity.Find;
import com.expo.utils.Constants;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.raphets.roundimageview.RoundImageView;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static java.lang.Thread.sleep;

public class FindDetailActivity extends BaseActivity<FindDetailContract.Presenter> implements FindDetailContract.View {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.find_detail_head)
    RoundImageView mRvHead;
    @BindView(R.id.find_detail_name)
    TextView mTvName;
    @BindView(R.id.find_detail_content)
    TextView mTvContent;
    @BindView(R.id.find_detail_position)
    TextView mTvPosition;
    @BindView(R.id.find_detail_enjoy)
    TextView mTvEnjoy;
    @BindView(R.id.find_detail_views)
    TextView mTvViews;

    VideoView mVideo;
    ImageView mVideoControl;

    Find mFind;
    CommonAdapter mAdapter;
    List<String> mList;

    Handler mHandler = new Handler();

    @Override
    protected int getContentView() {
        return R.layout.activity_find_detail;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

        mRootView.setTopPadding();
        mFind = getIntent().getParcelableExtra(Constants.EXTRAS.EXTRAS);
        mList = mFind.getUrlList();
        setFindInfo();
        mPresenter.addViews(mFind.id + "");

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecycler.setLayoutManager(manager);
        new PagerSnapHelper().attachToRecyclerView(mRecycler);
        if (mFind.type == 1)
            mRecycler.setAdapter(mAdapter = new CommonAdapter(this, R.layout.item_find_detail_video, mList) {
                @Override
                protected void convert(ViewHolder holder, Object o, int position) {
                    mVideo = ((VideoView) holder.getView(R.id.item_find_video));
                    mVideoControl = ((ImageView) holder.getView(R.id.item_find_video_control));
                    mVideo.setVideoURI(Uri.parse(mFind.url1));
                    holder.setOnClickListener(R.id.item_find_video, v -> videoChangeState());
                    holder.setOnClickListener(R.id.item_find_video_control, v -> videoStart());
                    mVideo.setOnCompletionListener(mp -> videoComplete());
                    mTvPosition.setText((position + 1) + "/" + mFind.getResCount());
                    videoStart();
                }

            });
        else
            mRecycler.setAdapter(mAdapter = new CommonAdapter(this, R.layout.item_find_detail, mList) {
                @Override
                protected void convert(ViewHolder holder, Object o, int position) {
                    Picasso.with(FindDetailActivity.this).load(mList.get(position)).placeholder(R.drawable.image_default).error(R.drawable.image_default).into((ImageView) holder.getView(R.id.item_find_img));
                    mTvPosition.setText((position + 1) + "/" + mList.size());
                }

            });
    }

    private void setFindInfo() {
        if (!StringUtils.isEmpty(mFind.upic))
            Picasso.with(FindDetailActivity.this).load(mFind.upic).placeholder(R.drawable.image_default).error(R.drawable.image_default).into(mRvHead);
        mTvName.setText(mFind.caption);
        mTvContent.setText(mFind.remark);
        mTvEnjoy.setText(mFind.enjoys);
        mTvViews.setText(mFind.views);

    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(@NonNull Context context, Find find) {
        Intent in = new Intent(context, FindDetailActivity.class);
        in.putExtra(Constants.EXTRAS.EXTRAS, find);
        context.startActivity(in);
    }

    @OnClick(R.id.find_detail_close)
    public void clickClose(View view) {
        finish();
    }

    private void videoChangeState() {
        if (mVideo.isPlaying()) {
            videoPause();
        } else {
            videoStart();
        }
    }

    private void videoComplete() {
        if (mVideo != null) {
            Picasso.with(this).load(R.mipmap.video_restart).into(mVideoControl);
            mVideoControl.setVisibility(View.VISIBLE);
        }
    }

    private void videoPause() {
        if (mVideo != null) {
            mVideo.pause();
            Picasso.with(this).load(R.mipmap.video_start).into(mVideoControl);
            mVideoControl.setVisibility(View.VISIBLE);
        }
    }

    private void videoStart() {
        if (mVideo != null) {
            mVideo.start();
            mVideoControl.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.find_detail_enjoy_click)
    public void clickEnjoy(View view) {
        mPresenter.addEnjoy(mFind.id + "");
    }

    @OnClick(R.id.find_detail_views_click)
    public void clickViews(View view) {

    }

}
