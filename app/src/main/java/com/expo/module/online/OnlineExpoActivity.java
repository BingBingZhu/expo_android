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
import android.widget.TextView;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.contract.OnlineHomeContract;
import com.expo.entity.VrInfo;
import com.expo.utils.LanguageUtil;
import com.expo.widget.RecycleViewDivider;
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

    @Override
    protected int getContentView() {
        return R.layout.activity_online_expo;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mPresenter.loadData();
        mPresenter.loadVrHot();
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, OnlineExpoActivity.class));
    }

    @Override
    public void loadLiveDataRes(List<VrInfo> liveVrs) {     // 世园实景
        mBanner.setIndicatorVisible(false);
        mBanner.setBannerPageClickListener(new LaminatedBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int position) {

            }
        });
        mBanner.addPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        // 设置数据
        mBanner.setPages(liveVrs, (LaminatedHolderCreator<BannerViewHolder>) () -> new BannerViewHolder());
    }

    @Override
    public void loadCultureDataRes(List<VrInfo> cultureVrs) {       // 文化世园
        mAdapterCulture = new CommonAdapter(this, R.layout.item_online_culture, cultureVrs) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {

            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return cultureVrs.size();
            }
        };

        mRvCulture.setLayoutManager(new LinearLayoutManager(this));
        mRvCulture.addItemDecoration(new SpaceDecoration(getResources().getDimensionPixelSize(R.dimen.dms_58)), 0);
        mRvCulture.setAdapter(mAdapterCulture);
    }

    @Override
    public void loadTourDataRes(List<VrInfo> tourVrs) {     // 在线导游
        mAdapterGuide = new CommonAdapter(this, R.layout.item_online_guide, tourVrs) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {

            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return tourVrs.size();
            }
        };

        mRvGuide.setLayoutManager(new GridLayoutManager(this, 2));
        mRvGuide.addItemDecoration(new RecycleViewDivider(
                getContext(), LinearLayoutManager.VERTICAL, 2, getResources().getColor(R.color.white_f5)));
        mRvGuide.setAdapter(mAdapterGuide);
    }

    public static class BannerViewHolder implements LaminatedViewHolder<VrInfo> {
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
        public void onBind(Context context, int position, VrInfo data) {
            // 数据绑定
            imageView.setImageURI(data.getUrl());
            tvName.setText(LanguageUtil.chooseTest(data.getCaption(), data.getCaptionEn()));
            tvCount.setText(data.getViewCount() + "次");
        }

    }
}
