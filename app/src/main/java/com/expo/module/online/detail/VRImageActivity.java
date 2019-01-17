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
import com.expo.contract.VRImageContract;
import com.expo.module.online.detail.widget.VRImageView;
import com.expo.widget.AppBarView;
import com.expo.widget.decorations.SpaceDecoration;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class VRImageActivity extends BaseActivity implements VRImageContract.View {
    @BindView(R.id.vr_image_title)
    AppBarView mAppBarView;
    @BindView(R.id.vr_image_recycler)
    RecyclerView mRvRecycler;
    @BindView(R.id.vr_image_frame)
    FrameLayout mFrame;
    @BindView(R.id.vr_image_show)
    TextView mTvShow;

    CommonAdapter mAdapter;

    List<String> mData;
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
        mAppBarView.setOnClickListener(v -> finish());

        mVRView = new VRImageView(this);
        mData = new ArrayList<>();
        mData.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1547707751782&di=ffa3185875f6d3be3c94a6811d8f7317&imgtype=0&src=http%3A%2F%2Fpic1.16pic.com%2F00%2F50%2F47%2F16pic_5047893_b.jpg");
        mData.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1745972916,2431690669&fm=26&gp=0.jpg");
        mData.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3469548459,432454939&fm=26&gp=0.jpg");
        mData.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1547724031815&di=18361a98cc18c075da6232b4052eda99&imgtype=0&src=http%3A%2F%2Fimg4.dwstatic.com%2Ftv%2F1807%2F394814443465%2F1530859743543.jpg");
        mData.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3888038808,1108989126&fm=26&gp=0.jpg");
        mData.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1548318771&di=c68decc65851ee97c28966ceed32eb1d&imgtype=jpg&er=1&src=http%3A%2F%2Fi0.hdslb.com%2Fbfs%2Farchive%2F771e9c07ddb2a292da982f7b4d1cfc3a57dd979e.jpg");
        mRvRecycler.setAdapter(mAdapter = new CommonAdapter<String>(this, R.layout.item_vr_image, mData) {
            @Override
            protected void convert(ViewHolder holder, String o, int position) {
                Picasso.with(VRImageActivity.this).load(mData.get(position)).into((ImageView) holder.getView(R.id.item_vr_img));
                holder.itemView.setOnClickListener(v -> {
                    if (mSelectView != null)
                        mSelectView.setSelected(false);
                    mSelectView = v;
                    mSelectView.setSelected(true);
                    mVRView.setUrl(mData.get(position));
                });
            }

        });
        mRvRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRvRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        mRvRecycler.addItemDecoration(new SpaceDecoration((int) getResources().getDimension(R.dimen.dms_36)));

        mFrame.addView(mVRView.getVrVideoView());
        mVRView.setUrl(mData.get(0));
        mVRView.mFullScreen.setVisibility(View.GONE);
        mTvShow.setSelected(true);
        mRvRecycler.setVisibility(View.VISIBLE);
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

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, VRImageActivity.class));
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
        });
    }
}
