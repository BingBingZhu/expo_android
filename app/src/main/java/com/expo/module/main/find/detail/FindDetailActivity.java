package com.expo.module.main.find.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.blankj.utilcode.util.StringUtils;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.utils.ImageUtils;
import com.expo.base.utils.StatusBarUtils;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.FindDetailContract;
import com.expo.entity.Find;
import com.expo.utils.CommUtils;
import com.expo.utils.Constants;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class FindDetailActivity extends BaseActivity<FindDetailContract.Presenter> implements FindDetailContract.View {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.find_detail_close)
    View mImClose;
    @BindView(R.id.find_detail_head)
    SimpleDraweeView mRvHead;
    @BindView(R.id.find_detail_name)
    TextView mTvName;
    @BindView(R.id.find_detail_content)
    TextView mTvContent;
    @BindView(R.id.find_detail_position)
    TextView mTvPosition;
    @BindView(R.id.find_detail_location)
    TextView mTvLocation;
    @BindView(R.id.find_detail_enjoy)
    TextView mTvEnjoy;
    @BindView(R.id.find_detail_views)
    TextView mTvViews;

    VideoView mVideo;
    ImageView mVideoImg;
    ImageView mVideoControl;

    Find mFind;
    CommonAdapter mAdapter;
    List<String> mList;

    private Intent mIntent;

    Handler mHandler = new Handler();

    @Override
    protected int getContentView() {
        return R.layout.activity_find_detail;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-2, -2);
        params.topMargin = (int) (StatusBarUtils.getStatusBarHeight(getContext()) + getResources().getDimension(R.dimen.dms_30));
        params.leftMargin = (int) getResources().getDimension(R.dimen.dms_30);

        mImClose.setLayoutParams(params);

//        mRootView.setTopPadding();
        mFind = getIntent().getParcelableExtra(Constants.EXTRAS.EXTRAS);
        mList = mFind.getUrlList();
        setFindInfo();
        mPresenter.addViews(mFind.id + "");
//        mFind.url1 = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4 ";

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecycler.setLayoutManager(manager);
        new PagerSnapHelper().attachToRecyclerView(mRecycler);
        if (mFind.type == 1)
            mRecycler.setAdapter(mAdapter = new CommonAdapter(this, R.layout.item_find_detail_video, mList) {
                @Override
                protected void convert(ViewHolder holder, Object o, int position) {
                    mVideo = ((VideoView) holder.getView(R.id.item_find_video));
                    mVideoImg = ((ImageView) holder.getView(R.id.item_find_video_img));
                    mVideoControl = ((ImageView) holder.getView(R.id.item_find_video_control));
                    mVideo.setVideoURI( Uri.parse( CommUtils.getFullUrl( mFind.url1 ) ) );
                    holder.setOnClickListener(R.id.item_find_video, v -> videoChangeState());
                    holder.setOnClickListener(R.id.item_find_video_control, v -> videoStart());
                    mVideo.setOnCompletionListener(mp -> videoComplete());
                    mTvPosition.setVisibility(View.VISIBLE);

                    new Thread(() -> {
                        Bitmap bitmap = ImageUtils.createVideoThumbnail(CommUtils.getFullUrl(mFind.url1), MediaStore.Images.Thumbnails.MINI_KIND);
                        if (mHandler != null)
                            mHandler.post(() -> {
                                if (bitmap != null && mVideoImg != null)
                                    mVideoImg.setImageBitmap(bitmap);
                            });
                    }).start();

                    videoStart();
                }

            });
        else {
            mTvPosition.setText("1/" + mList.size());
            mRecycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    LinearLayoutManager l = (LinearLayoutManager) recyclerView.getLayoutManager();
                    mTvPosition.setText((l.findFirstVisibleItemPosition() + 1) + "/" + mList.size());
                }
            });
            mRecycler.setAdapter(mAdapter = new CommonAdapter(this, R.layout.item_find_detail, mList) {
                @Override
                protected void convert(ViewHolder holder, Object o, int position) {
                    CommUtils.setImgPic(FindDetailActivity.this, CommUtils.getFullUrl(mList.get(position)), (ImageView) holder.getView(R.id.item_find_img));
                    mTvPosition.setVisibility(View.VISIBLE);
                }

            });
        }
    }

    private void setFindInfo() {
        mIntent = new Intent();
        if (!StringUtils.isEmpty(mFind.upic))
            mRvHead.setImageURI(mFind.upic);
//            Picasso.with(FindDetailActivity.this).load(mFind.upic).placeholder(R.drawable.image_default).error(R.drawable.image_default).into(mRvHead);
        mTvName.setText(mFind.uname);
        mTvContent.setText(mFind.caption);
        mTvEnjoy.setText(mFind.enjoys);
        mTvViews.setText(mFind.views);
        mTvPosition.setVisibility(View.VISIBLE);
        if (StringUtils.isEmpty(mFind.location)) {
            mTvLocation.setVisibility(View.GONE);
        } else {
            mTvLocation.setVisibility(View.VISIBLE);
            mTvLocation.setText(mFind.location);

        }
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
            mVideoImg.setVisibility(View.VISIBLE);
        }
    }

    private void videoPause() {
        if (mVideo != null) {
            mVideoImg.setVisibility(View.GONE);
            Picasso.with(this).load(R.mipmap.video_start).into(mVideoControl);
            mVideoControl.setVisibility(View.VISIBLE);
            mVideo.pause();
        }
    }

    private void videoStart() {
        if (mVideo != null) {
            mVideoImg.setVisibility(View.GONE);
            mVideoControl.setVisibility(View.GONE);
            mVideo.start();
        }
    }

    @OnClick(R.id.find_detail_enjoy_click)
    public void clickEnjoy(View view) {
        mPresenter.addEnjoy(mFind);
    }

    @OnClick(R.id.find_detail_views_click)
    public void clickViews(View view) {

    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    public void addEnjoyRes(boolean isSelf) {
        if (isSelf){
            ToastHelper.showShort("自己不能点赞哦");
            return;
        }
        // 点赞返回
        int enjoys = Integer.parseInt(mFind.enjoys);
        enjoys++;
        mFind.enjoys = String.valueOf(enjoys);
        mTvEnjoy.setText(mFind.enjoys);
        mIntent.putExtra(Constants.EXTRAS.EXTRA_ENJOYS, mFind.enjoys);
        FindDetailActivity.this.setResult(RESULT_OK, mIntent);
    }

    @Override
    public void addViewsRes() {
        int readCount = Integer.parseInt(mFind.views);
        readCount++;
        mFind.views = String.valueOf(readCount);
        mTvViews.setText(mFind.views);
        mIntent.putExtra(Constants.EXTRAS.EXTRA_READ_COUNT, mFind.views);
        FindDetailActivity.this.setResult(RESULT_OK, mIntent);
    }
}
