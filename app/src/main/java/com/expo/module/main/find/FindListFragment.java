package com.expo.module.main.find;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.expo.R;
import com.expo.base.BaseFragment;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.FindListContract;
import com.expo.entity.Find;
import com.expo.module.main.find.publish.FindPublishActivity;
import com.expo.widget.SimpleRecyclerView;
import com.expo.widget.decorations.SpaceDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class FindListFragment extends BaseFragment<FindListContract.Presenter> implements FindListContract.View {

    private FindTab mTab;
    @BindView(R.id.ptr_view)
    PtrClassicFrameLayout mPtrView;
    @BindView(R.id.recycler_view)
    SimpleRecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    View mEmptyView;

    private FindListAdapter mAdapter;
    private int page = 0;
    private List<Find> mFindList = null;
    Handler mHandler = new Handler();

    @Override
    public int getContentView() {
        return R.layout.layout_fragment_find_list;
    }

    @Override
    public boolean isNeedPaddingTop() {
        return false;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mTab = getArguments().getParcelable("tab");
        mFindList = new ArrayList<>();
        mAdapter = new FindListAdapter(getContext(), mHandler, mFindList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SpaceDecoration(0, getResources().getDimensionPixelSize(R.dimen.dms_4)));
        StaggeredGridLayoutManager recyclerViewLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(recyclerViewLayoutManager);

        initLoadMore();
        mPresenter.getSocietyListFilter(page, true);
    }

    private void initLoadMore() {
        mPtrView.setMode(PtrFrameLayout.Mode.BOTH);
        mPtrView.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                page++;
                mPresenter.getSocietyListFilter(page, false);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 0;
                mPresenter.getSocietyListFilter(page, true);
            }
        });
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    @Override
    public void addFindList(List<Find> data, boolean isFresh) {
        if (isFresh) {
            mFindList.clear();
        }
        if (null == data || data.size() == 0) {
            if (page > 0) {
                page--;
                ToastHelper.showShort(R.string.no_more_data_available);
            }
        } else {
            mFindList.addAll(data);
        }
        if (mFindList.size() == 0) {
            mEmptyView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

        }
        mAdapter.notifyDataSetChanged();
        mPtrView.refreshComplete();
    }

    @OnClick(R.id.find_add)
    public void clickFindAdd(View view) {
        FindPublishActivity.startActivity(getContext(), 0);
    }

}