package com.expo.module.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.expo.R;
import com.expo.adapters.ServiceHistoryAdapter;
import com.expo.base.BaseActivity;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.ServiceHistoryContract;
import com.expo.entity.VisitorService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class ServiceHistoryActivity extends BaseActivity<ServiceHistoryContract.Presenter> implements ServiceHistoryContract.View {

    @BindView(R.id.ptr_view)
    PtrClassicFrameLayout mPtrView;
    @BindView(R.id.service_history_list)
    RecyclerView mRecyclerView;

    private int page = 0;
    private List<VisitorService> visitorServices;
    private ServiceHistoryAdapter mAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_service_history;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(0, "我的游客服务");
        visitorServices = new ArrayList<>();
        initLoadMore();
        mPtrView.autoRefresh();
    }

    private void initLoadMore() {
        mPtrView.setMode(PtrFrameLayout.Mode.BOTH);
        mPtrView.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                page++;
                mPresenter.loadMoreData(page);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 0;
                mPresenter.loadAllData();
            }
        });
    }

    @Override
    public void addDataToList(List<VisitorService> data){
        // 加载到列表
        if (null == data || data.size() == 0) {
            if (page > 0) {
                page--;
                ToastHelper.showShort(R.string.no_more_data_available);
            }
        } else {
            if (page == 0){
                visitorServices.clear();
            }
            visitorServices.addAll(data);
            mAdapter = new ServiceHistoryAdapter(getContext(), visitorServices);
            mRecyclerView.setAdapter(mAdapter);
        }
        mPtrView.refreshComplete();
    }

    @Override
    public void loadDataRes(boolean b) {
        if (!b){
            ToastHelper.showShort("刷新失败，请检查网络设置");
        }
        mPtrView.refreshComplete();
        page = 0;
        mPresenter.loadMoreData(page);
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent( context, ServiceHistoryActivity.class );
        context.startActivity( in );
    }
}
