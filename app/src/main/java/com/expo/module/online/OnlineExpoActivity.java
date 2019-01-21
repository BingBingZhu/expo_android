package com.expo.module.online;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.utils.LogUtils;
import com.expo.contract.OnlineHomeContract;
import com.expo.entity.RollData;
import com.expo.widget.decorations.SpaceDecoration;
import com.facebook.drawee.view.SimpleDraweeView;
import com.expo.widget.laminatedbanner.LaminatedBannerView;
import com.expo.widget.laminatedbanner.holder.LaminatedHolderCreator;
import com.expo.widget.laminatedbanner.holder.LaminatedViewHolder;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class OnlineExpoActivity extends BaseActivity<OnlineHomeContract.Presenter> implements OnlineHomeContract.View {

    @BindView(R.id.online_expo_recycler_culture)
    RecyclerView mRvCulture;
    @BindView(R.id.online_expo_recycler_guide)
    RecyclerView mRvGuide;
    @BindView(R.id.banner)
    LaminatedBannerView mBanner;


    CommonAdapter mAdapterCulture;
    CommonAdapter mAdapterGuide;

    List mCultureList;
    List mGuideList;

    private static final  float EPSINON = 0.00001F;

    @Override
    protected int getContentView() {
        return R.layout.activity_online_expo;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
//        initCultureView();
//        initGuideView();
        mPresenter.loadRollData();
    }

    private void initCultureView() {
        mCultureList = new ArrayList();
        mAdapterCulture = new CommonAdapter(this, R.layout.item_online_culture, mGuideList) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {

            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }
        };

        mRvCulture.setLayoutManager(new LinearLayoutManager(this));
        mRvCulture.addItemDecoration(new SpaceDecoration(getResources().getDimensionPixelSize(R.dimen.dms_58)), getResources().getDimensionPixelSize(R.dimen.dms_30));
        mRvCulture.setAdapter(mAdapterCulture);
    }

    private void initGuideView() {
        mGuideList = new ArrayList();
        mAdapterGuide = new CommonAdapter(this, R.layout.item_online_guide, mGuideList) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {

            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }
        };

        mRvGuide.setLayoutManager(new GridLayoutManager(this, 2));
        mRvGuide.addItemDecoration(new SpaceDecoration(getResources().getDimensionPixelSize(R.dimen.dms_14)), getResources().getDimensionPixelSize(R.dimen.dms_30));
        mRvGuide.setAdapter(mAdapterGuide);
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, OnlineExpoActivity.class));
    }

    @Override
    public void loadRollDataRes(List<RollData> rollDataList) {
        mBanner.setIndicatorVisible(false);
        mBanner.addPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (!(positionOffset < EPSINON  &&  positionOffset >  -EPSINON)) {
                    LogUtils.e("aaaaaaaaaaaaaa", "position: " + position + "    "
                            + "positionOffset: " + positionOffset + "    "
                            + "positionOffsetPixels: " + positionOffsetPixels);
//                    float fraction = positionOffset;
//                    if (position == 0) {
//                        img1.getBackground().setAlpha((int) ((1 - fraction) * 255));
//                        img2.getBackground().setAlpha((int) (fraction * 255));
//                        img3.getBackground().setAlpha(0);
//                    }else if (position == rollDataList.size() - 1) {
//                        img2.getBackground().setAlpha((int) ((1 - fraction) * 255));
//                        img3.getBackground().setAlpha((int) (fraction * 255));
//                        img1.getBackground().setAlpha(0);
//                    }else{
//
//                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        // 设置数据
        mBanner.setPages(rollDataList, (LaminatedHolderCreator<BannerViewHolder>) () -> new BannerViewHolder());
    }

    public static class BannerViewHolder implements LaminatedViewHolder<RollData> {
        private SimpleDraweeView imageView;
        private TextView tvName;
        private TextView tvCount;

        @Override
        public View createView(Context context) {
            // 返回页面布局
            View view = LayoutInflater.from(context).inflate(R.layout.layout_banner_item, null);
            imageView = view.findViewById(R.id.banner_image);
            tvName = view.findViewById(R.id.banner_name);
            tvCount = view.findViewById(R.id.banner_count);
            return view;
        }

        @Override
        public void onBind(Context context, int position, RollData data) {
            // 数据绑定
            imageView.setImageURI(data.getUrl());
            tvName.setText(data.getName());
            tvCount.setText(data.getCount() + "次");
        }

    }
}
