package com.expo.module.main.find;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.expo.R;
import com.expo.base.BaseFragment;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.FindListContract;
import com.expo.entity.Find;
import com.expo.module.main.find.detail.FindDetailActivity;
import com.expo.utils.Constants;
import com.expo.widget.SimpleRecyclerView;
import com.expo.widget.decorations.SpaceDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

import static android.app.Activity.RESULT_OK;

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
    private int position = 0;
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
        mAdapter = new FindListAdapter(getActivity(), mHandler, mFindList);
        mAdapter.setOnItemClickListener(onItemClickListener);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SpaceDecoration(0, getResources().getDimensionPixelSize(R.dimen.dms_4)));
        StaggeredGridLayoutManager recyclerViewLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(recyclerViewLayoutManager);

        initLoadMore();
        mPresenter.getSocietyListFilter(page, mTab.id, true);
    }

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag();
            position = holder.getAdapterPosition();
            Intent in = new Intent(getContext(), FindDetailActivity.class);
            in.putExtra(Constants.EXTRAS.EXTRAS, mFindList.get(position));
            startActivityForResult(in, Constants.RequestCode.REQ_TO_FIND_INFO);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.RequestCode.REQ_TO_FIND_INFO && resultCode == RESULT_OK) {
            if (mFindList.size() >= position - 1)
                mFindList.get(position).enjoys = data.getStringExtra(Constants.EXTRAS.EXTRA_ENJOYS);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void initLoadMore() {
        mPtrView.setMode(PtrFrameLayout.Mode.BOTH);
        mPtrView.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                page++;
                mPresenter.getSocietyListFilter(page, mTab.id, false);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 0;
                mPresenter.getSocietyListFilter(page, mTab.id, true);
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
        int start = mFindList.size();
        if (null == data || data.size() == 0) {
            if (page > 0) {
                page--;
                ToastHelper.showShort(R.string.no_more_data_available);
            }
        } else {
            mFindList.addAll(data);
        }
        int end = mFindList.size();
        if (mFindList.size() == 0) {
            mEmptyView.setVisibility(View.VISIBLE);
            mPtrView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.GONE);
            mPtrView.setVisibility(View.VISIBLE);
        }
        mAdapter.notifyItemChanged(start, end);
        mPtrView.refreshComplete();
    }

    @OnClick(R.id.layout_empty_fresh)
    public void clickFresh(View view) {
        page = 0;
        mPresenter.getSocietyListFilter(page, mTab.id, true);
    }
}
