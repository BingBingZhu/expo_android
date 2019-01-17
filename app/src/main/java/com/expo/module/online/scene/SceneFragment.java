package com.expo.module.online.scene;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.BaseFragment;
import com.expo.contract.SceneContract;
import com.expo.contract.SceneTabContract;
import com.expo.entity.Find;
import com.expo.entity.RouteInfo;
import com.expo.module.main.find.FindListAdapter;
import com.expo.module.main.find.FindTab;
import com.expo.module.main.find.detail.FindDetailActivity;
import com.expo.utils.Constants;
import com.expo.widget.SimpleRecyclerView;
import com.expo.widget.decorations.SpaceDecoration;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/*
 * 推荐路线列表
 */
public class SceneFragment extends BaseFragment<SceneContract.Presenter> implements SceneContract.View {

    private SceneTab mTab;
    @BindView(R.id.ptr_view)
    PtrClassicFrameLayout mPtrView;
    @BindView(R.id.recycler_view)
    SimpleRecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    View mEmptyView;
    @BindView(R.id.layout_empty_img)
    ImageView mEmptyImg;
    @BindView(R.id.layout_empty_text)
    TextView mEmptyText;
    @BindView(R.id.layout_empty_fresh)
    TextView mEmptyFresh;

    private CommonAdapter mAdapter;
    private int page = 0;
    private List mFindList = null;

    @Override
    public int getContentView() {
        return R.layout.fragment_scene;
    }

    @Override
    public boolean isNeedPaddingTop() {
        return false;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mTab = getArguments().getParcelable("tab");
        mFindList = new ArrayList<>();
        mAdapter = new CommonAdapter(getContext(), R.layout.item_scene, mFindList) {

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            protected void convert(ViewHolder holder, Object o, int position) {

            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SpaceDecoration(0, getResources().getDimensionPixelSize(R.dimen.dms_22)));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        initLoadMore();
        mPresenter.getExpo(mTab.id, true, 0, 10);
        Picasso.with(getContext()).load(R.mipmap.empty_img2).into(mEmptyImg);
        mEmptyText.setText(R.string.empty_content_2);
        mEmptyFresh.setText(R.string.empty_fresh_2);
        mEmptyFresh.setVisibility(View.VISIBLE);
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    @Override
    public void freshRoutes(boolean fresh, List list) {
        if (fresh)
            mFindList.clear();
        mFindList.addAll(list);
        mAdapter.notifyDataSetChanged();
        if (mFindList.size() == 0) {
            mEmptyView.setVisibility(View.VISIBLE);
            mPtrView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.GONE);
            mPtrView.setVisibility(View.VISIBLE);
        }
    }

    private void initLoadMore() {
        mPtrView.setMode(PtrFrameLayout.Mode.BOTH);
        mPtrView.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                page++;
                mPresenter.getExpo(mTab.id, false, page, 10);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 0;
                mPresenter.getExpo(mTab.id, true, page, 10);
            }
        });
    }
}
