package com.expo.module.online;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.expo.R;
import com.expo.adapters.EncyAndSceneListAdapter;
import com.expo.adapters.EncyclopediasAdapter;
import com.expo.adapters.ListItemData;
import com.expo.adapters.Tab;
import com.expo.adapters.VrListAdapter;
import com.expo.base.BaseFragment;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.ListContract;
import com.expo.entity.Encyclopedias;
import com.expo.entity.VrInfo;
import com.expo.widget.RecycleViewDivider;
import com.expo.widget.SimpleRecyclerView;
import com.expo.widget.decorations.SpaceDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

@SuppressLint("ValidFragment")
public class VrListFragment extends BaseFragment<ListContract.Presenter> implements ListContract.View {

    private Tab mTab;
    @BindView(R.id.ptr_view)
    PtrClassicFrameLayout mPtrView;
    @BindView(R.id.recycler_view)
    SimpleRecyclerView mRecyclerView;

    private VrListAdapter adapter;
    private int page = 0;
    private List<VrInfo> mVrInfos = null;
    private int vrType;

    @SuppressLint("ValidFragment")
    public VrListFragment(int vrType) {
        this.vrType = vrType;
    }

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
        mVrInfos = new ArrayList<>();
        adapter = new VrListAdapter(getContext(), mVrInfos);
        mRecyclerView.setAdapter(adapter);
        int topBottom = (int) getResources().getDimension(R.dimen.dms_20);
        int leftRight = (int) getResources().getDimension(R.dimen.dms_30);
        mRecyclerView.addItemDecoration(new SpaceDecoration(leftRight, topBottom, leftRight, topBottom, topBottom ));
//        mRecyclerView.addItemDecoration( new RecycleViewDivider(
//                getContext(), LinearLayoutManager.VERTICAL, 4, getResources().getColor( R.color.white_f4 ) ) );
        mPresenter.loadVrsByType(vrType, mTab.getId(), page);
        initLoadMore();
    }

    private void initLoadMore() {
        mPtrView.setMode(PtrFrameLayout.Mode.LOAD_MORE);
        mPtrView.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                page++;
                mPresenter.loadVrsByType(vrType, mTab.getId(), page);
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
        // 未使用方法
    }

    @Override
    public void addVrsToList(List<VrInfo> data) {
        // 加载到列表
        if (null == data || data.size() == 0) {
            if (page > 0) {
                page--;
                ToastHelper.showShort(R.string.no_more_data_available);
            }
        } else {
            mVrInfos.addAll(data);
        }
        mPtrView.refreshComplete();
    }
}
