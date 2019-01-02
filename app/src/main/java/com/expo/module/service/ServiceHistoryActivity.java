package com.expo.module.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.expo.R;
import com.expo.adapters.ServiceHistoryAdapter;
import com.expo.base.BaseActivity;
import com.expo.base.ExpoApp;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.ServiceHistoryContract;
import com.expo.entity.CommonInfo;
import com.expo.entity.User;
import com.expo.entity.VisitorService;
import com.expo.widget.decorations.SpaceDecoration;
import com.expo.module.webview.WebActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 游客服务记录
 */
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
        setTitle(0, R.string.my_tourist_service);
        visitorServices = new ArrayList<>();
        int marginV = getResources().getDimensionPixelSize(R.dimen.dms_30);
        mRecyclerView.addItemDecoration(new SpaceDecoration(0, marginV, 0, 0, 0));
        mAdapter = new ServiceHistoryAdapter(getContext(), visitorServices);
        mAdapter.setOnClickListener(mClickListener);
        mRecyclerView.setAdapter(mAdapter);
        initLoadMore();
        mPtrView.autoRefresh();
        setEmptyFreshListener(0, v -> {
            page = 0;
            mPresenter.loadAllData();
        });
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
    public void addDataToList(List<VisitorService> data) {
        // 加载到列表
        if (null == data || data.size() == 0) {
            if (page > 0) {
                page--;
                ToastHelper.showShort(R.string.no_more_data_available);
            }
        } else {
            if (page == 0) {
                visitorServices.clear();
            }
            visitorServices.addAll(data);
            mAdapter.notifyDataSetChanged();
        }
        if (visitorServices.size() == 0) showEmptyView();
        else hideEmptyView();
        mPtrView.refreshComplete();
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.service_history_item_more_tv:
                    RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag();
                    if (holder == null) return;
                    VisitorService vs = mAdapter.getDataByPosition(holder.getAdapterPosition());
                    User user = ExpoApp.getApplication().getUser();
                    if (vs != null && user != null) {
                        String url = mPresenter.loadCommonInfo(CommonInfo.VISITOR_SERVICE_DETAILS);
                        WebActivity.startActivity(getContext(),
                                TextUtils.isEmpty(url) ? url : (url + String.format("?Uid=%s&Ukey=%s&id=%s", user.getUid(), user.getUkey(), vs.getId())),
                                getString(R.string.title_message_tourist_ac), BaseActivity.TITLE_COLOR_STYLE_GREEN);
                    }
                    break;
            }
        }
    };

    @Override
    public void loadDataRes(boolean b) {
        if (!b) {
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
        Intent in = new Intent(context, ServiceHistoryActivity.class);
        context.startActivity(in);
    }
}
