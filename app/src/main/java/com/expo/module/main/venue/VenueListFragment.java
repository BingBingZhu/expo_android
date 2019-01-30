package com.expo.module.main.venue;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.expo.R;
import com.expo.adapters.Tab;
import com.expo.base.BaseFragment;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.VenueListContract;
import com.expo.entity.Venue;
import com.expo.entity.VrInfo;
import com.expo.module.webview.WebActivity;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.expo.widget.SimpleRecyclerView;
import com.expo.widget.decorations.RecycleViewDivider;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class VenueListFragment extends BaseFragment<VenueListContract.Presenter> implements VenueListContract.View {

    private Tab mTab;
    @BindView(R.id.ptr_view)
    PtrClassicFrameLayout mPtrView;
    @BindView(R.id.recycler_view)
    SimpleRecyclerView mRecyclerView;

    private CommonAdapter adapter;
    private int page = 0;
    private List<Venue> mEncyclopediasList = null;

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
//        adapter = new EncyAndSceneListAdapter(getContext(), mEncyclopediasList);
        mRecyclerView.setAdapter(adapter = new CommonAdapter<Venue>(getContext(), R.layout.layout_ency_item, mEncyclopediasList) {
            @Override
            protected void convert(ViewHolder holder, Venue o, int position) {
                Venue ency = mEncyclopediasList.get(position);
                ((SimpleDraweeView) holder.getView(R.id.ency_item_img)).setImageURI(Constants.URL.FILE_BASE_URL + ency.getPicUrl());
                holder.setText(R.id.ency_item_name, LanguageUtil.chooseTest(ency.getCaption(), ency.getEnCaption()));
                holder.setVisible(R.id.ency_item_recommend, ency.getIsRecommended() == 1);
                holder.setText(R.id.ency_item_remark, LanguageUtil.chooseTest(ency.getRemark(), ency.getEnRemark()));
                holder.itemView.setOnClickListener(v -> WebActivity.startActivity(mContext,
                        LanguageUtil.chooseTest(ency.getLinkH5Url(), ency.getLinkH5UrlEn()),
                        LanguageUtil.chooseTest(ency.getCaption(), ency.getEnCaption())
                ));
            }

        });
        mRecyclerView.addItemDecoration(new RecycleViewDivider(
                getContext(), LinearLayoutManager.VERTICAL, 2, getResources().getColor(R.color.white_f5)));
        mPresenter.loadEncyByType(mTab.getId(), page);
        initLoadMore();
    }

    private void initLoadMore() {
        mPtrView.setMode(PtrFrameLayout.Mode.LOAD_MORE);
        mPtrView.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                page++;
                mPresenter.loadEncyByType(mTab.getId(), page);
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
    public void addEncysToList(List<Venue> data) {
        // 加载到列表
        if (null == data || data.size() == 0) {
            if (page > 0) {
                page--;
                ToastHelper.showShort(R.string.no_more_data_available);
//                View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_list_footer,null);
//                mPtrView.setFooterView(view);
            }
        } else {
            mEncyclopediasList.addAll(data);
        }
        adapter.notifyDataSetChanged();
        mPtrView.refreshComplete();
    }

    @Override
    public void addVrsToList(List<VrInfo> data) {
        // 未使用方法
    }
}
