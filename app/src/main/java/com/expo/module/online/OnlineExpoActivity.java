package com.expo.module.online;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.expo.base.utils.StatusBarUtils;
import com.expo.contract.OnlineHomeContract;
import com.expo.entity.VrInfo;
import com.expo.module.online.detail.VRDetailActivity;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.expo.widget.AppBarView;
import com.expo.widget.MyScrollView;
import com.expo.widget.RecycleViewDivider;
import com.expo.widget.decorations.SpaceDecoration;
import com.facebook.drawee.view.SimpleDraweeView;
import com.expo.widget.laminatedbanner.LaminatedBannerView;
import com.expo.widget.laminatedbanner.holder.LaminatedHolderCreator;
import com.expo.widget.laminatedbanner.holder.LaminatedViewHolder;
import com.hedan.textdrawablelibrary.TextViewDrawable;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class OnlineExpoActivity extends BaseActivity<OnlineHomeContract.Presenter> implements OnlineHomeContract.View {

    @BindView(R.id.online_expo_recycler_culture)
    RecyclerView mRvCulture;
    @BindView(R.id.online_expo_recycler_guide)
    RecyclerView mRvGuide;
    @BindView(R.id.banner)
    LaminatedBannerView mBanner;
    @BindView(R.id.online_expo_title)
    AppBarView mTitle;
    @BindView(R.id.online_home_scroll)
    MyScrollView mSvScroll;

    private CommonAdapter mAdapterCulture;
    private CommonAdapter mAdapterGuide;
    private List<Integer> listRepetitionFlag = new ArrayList<>();   // 元素随机抽取记录
    private List<VrInfo> mCultureVrs;
    private List<VrInfo> mRandomVrs;

    @Override
    protected int getContentView() {
        return R.layout.activity_online_expo;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mSvScroll.setOnScrollListener(mScrollListener);
        mTitle.setPadding(0, StatusBarUtils.getStatusBarHeight(getContext()), 0, 0);
        mTitle.setBackgroundColor(Color.TRANSPARENT);
        mTitle.setTitleColor(Color.TRANSPARENT);
        mPresenter.loadData();
        mPresenter.loadVrHot();
    }

    @OnClick({R.id.online_expo_item_title_scene, R.id.online_expo_item_title_culture, R.id.online_expo_item_title_guide, R.id.title_back})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.online_expo_exchange:     // 换一换
                mRandomVrs = extractRandom(mCultureVrs);
                mAdapterCulture.notifyDataSetChanged();
                break;
            case R.id.online_expo_item_title_scene:     // 世园实景
                PanoramaListActivity.startActivity(getContext(), 1);
                break;
            case R.id.online_expo_item_title_culture:      // 文化世园
                PanoramaListActivity.startActivity(getContext(), 2);
                break;
            case R.id.online_expo_item_title_guide:     // 在线导游
                PanoramaListActivity.startActivity(getContext(), 3);
                break;
            case R.id.title_back:
                finish();
                break;
        }
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
        if (liveVrs.size() > 0) {
            for (int i = 0; i < 2; i++) {
                if (liveVrs.size() < 3){
                    liveVrs.add(liveVrs.get(0));
                }
            }
        }
        mBanner.setIndicatorVisible(false);
        mBanner.setBannerPageClickListener((view, position) -> VRDetailActivity.startActivity(getContext(), liveVrs.get(position).getId(), true));
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
        this.mCultureVrs = cultureVrs;
        mRandomVrs = extractRandom(cultureVrs);
        mAdapterCulture = new CommonAdapter<VrInfo>(this, R.layout.item_online_culture, mRandomVrs) {
            @Override
            protected void convert(ViewHolder holder, VrInfo vr, int position) {
                holder.<SimpleDraweeView>getView(R.id.item_online_culture_img).setImageURI(Constants.URL.FILE_BASE_URL + vr.getUrl());
                holder.<TextView>getView(R.id.item_online_culture_name).setText(LanguageUtil.chooseTest(vr.getCaption(), vr.getCaptionEn()));
                holder.<TextViewDrawable>getView(R.id.item_online_culture_scans).setText(vr.getViewCount()+"次");
                holder.setOnClickListener(R.id.item_online_culture_root, v -> VRDetailActivity.startActivity(getContext(), vr.getId(), true));
            }
        };
        mRvCulture.setLayoutManager(new LinearLayoutManager(this));
        mRvCulture.addItemDecoration(new SpaceDecoration(getResources().getDimensionPixelSize(R.dimen.dms_58)), 0);
        mRvCulture.setAdapter(mAdapterCulture);
    }

    @Override
    public void loadTourDataRes(List<VrInfo> tourVrs) {     // 在线导游
        mAdapterGuide = new CommonAdapter<VrInfo>(this, R.layout.item_online_guide, tourVrs) {
            @Override
            protected void convert(ViewHolder holder, VrInfo vr, int position) {
                holder.<SimpleDraweeView>getView(R.id.item_online_guide_img).setImageURI(Constants.URL.FILE_BASE_URL + vr.getUrl());
//                holder.<SimpleDraweeView>getView(R.id.item_online_guide_tour).setText("");
                holder.<TextView>getView(R.id.item_online_guide_name).setText(LanguageUtil.chooseTest(vr.getCaption(), vr.getCaptionEn()));
                holder.<TextView>getView(R.id.item_online_guide_content).setText(LanguageUtil.chooseTest(vr.getRemark(), vr.getRemarkEn()));
                holder.<TextViewDrawable>getView(R.id.item_online_guide_scans).setText(vr.getViewCount()+"次");
                holder.<TextViewDrawable>getView(R.id.item_online_guide_time).setText(vr.getExtAttr()+"分钟");
                holder.setOnClickListener(R.id.item_online_guide_root, v -> VRDetailActivity.startActivity(getContext(), vr.getId(), true));
            }
        };

        mRvGuide.setLayoutManager(new GridLayoutManager(this, 2));
        mRvGuide.addItemDecoration(new RecycleViewDivider(
                getContext(), LinearLayoutManager.VERTICAL, 2, getResources().getColor(R.color.white_f5)));
        mRvGuide.setAdapter(mAdapterGuide);
    }

    /**
     * 抽取随机元素
     * @param vrInfos   总集合
     * @return  抽取后的集合
     */
    private List<VrInfo> extractRandom(List<VrInfo> vrInfos){
        int n = 4;
        List<VrInfo> listNew = new ArrayList();
        if(vrInfos.size() <= n){
            listNew.addAll(vrInfos);
        }else if(vrInfos.size() > n && vrInfos.size() < n*2){
            listRepetitionFlag.clear();
            while(listRepetitionFlag.size() < n){
                int random = (int) (Math.random() * vrInfos.size());
                if (!listRepetitionFlag.contains(random)) {
                    listRepetitionFlag.add(random);
                    listNew.add(vrInfos.get(random));
                }
            }
        }else{
            while(listRepetitionFlag.size() < n*2){
                int random = (int) (Math.random() * vrInfos.size());
                if (!listRepetitionFlag.contains(random)) {
                    listRepetitionFlag.add(random);
                    listNew.add(vrInfos.get(random));
                }
            }
            for (int i = 0 ; i < 4 ; i ++ ){
                listRepetitionFlag.remove(0);
            }
        }
        return listNew;
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
            imageView.setImageURI(Constants.URL.FILE_BASE_URL + data.getUrl());
            tvName.setText(LanguageUtil.chooseTest(data.getCaption(), data.getCaptionEn()));
            tvCount.setText(data.getViewCount() + "次");
        }

    }

    MyScrollView.OnScrollListener mScrollListener = new MyScrollView.OnScrollListener() {
        @Override
        public void onScroll(int scrollY) {
            int backgroundColor = Color.argb((int) Math.min(0xff,
                    Math.max(Float.valueOf(scrollY), 0.0f) / 2), 2, 205, 155);
            int textColor = Color.argb((int) Math.min(0xff,
                    Math.max(Float.valueOf(scrollY), 0.0f) / 2), 255, 255, 255);
            mTitle.setBackgroundColor(backgroundColor);
            mTitle.setTitleColor(textColor);
        }
    };
}
