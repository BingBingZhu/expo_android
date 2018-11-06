package com.expo.module.main.encyclopedia;

import android.os.Bundle;

import com.expo.R;
import com.expo.adapters.EncyAndSceneListAdapter;
import com.expo.adapters.EncyclopediasAdapter;
import com.expo.adapters.ListItemData;
import com.expo.adapters.Tab;
import com.expo.base.BaseFragment;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.ListContract;
import com.expo.entity.Encyclopedias;
import com.expo.widget.SimpleRecyclerView;
import com.expo.widget.decorations.SpaceDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class ListFragment extends BaseFragment<ListContract.Presenter> implements ListContract.View {

    private Tab mTab;
    @BindView(R.id.ptr_view)
    PtrClassicFrameLayout mPtrView;
    @BindView(R.id.recycler_view)
    SimpleRecyclerView mRecyclerView;

    private EncyAndSceneListAdapter adapter;
    private int page = 0;
    private List<ListItemData> mEncyclopediasList = null;

    @Override
    public int getContentView() {
        return R.layout.layout_fragment_list;
    }

    @Override
    public boolean isNeedPaddingTop() {
        return false;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mTab = getArguments().getParcelable("tab");
        mEncyclopediasList = new ArrayList<>();
        adapter = new EncyAndSceneListAdapter( getContext(), mEncyclopediasList );
        mRecyclerView.setAdapter( adapter );
//        int marginV = getResources().getDimensionPixelSize(R.dimen.dms_18);
//        int space = getResources().getDimensionPixelSize(R.dimen.dms_18);
//        mRecyclerView.addItemDecoration(new SpaceDecoration(0, marginV, 0, 0, 0));
        mPresenter.loadEncyByType(mTab.getTab(), page);
        initLoadMore();
    }

    private void initLoadMore() {
        mPtrView.setMode(PtrFrameLayout.Mode.LOAD_MORE);
        mPtrView.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                page++;
                mPresenter.loadEncyByType(mTab.getTab(), page);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

            }
        });
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    @Override
    public void addEncysToList(List<Encyclopedias> data) {
        // 加载到列表
        if ((null == data || data.size() == 0) && page>0){
            page--;
            ToastHelper.showShort(R.string.no_more_data_available);
        }else {
            mEncyclopediasList.addAll(EncyclopediasAdapter.convertToTabList(data));
        }
        mPtrView.refreshComplete();
    }
}
