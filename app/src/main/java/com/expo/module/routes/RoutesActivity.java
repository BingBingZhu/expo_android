package com.expo.module.routes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.BaseAdapterItemClickListener;
import com.expo.contract.RoutesContract;
import com.expo.entity.RouteInfo;
import com.expo.module.service.ServiceHistoryActivity;
import com.expo.utils.CommUtils;
import com.expo.utils.LanguageUtil;
import com.expo.widget.AppBarView;
import com.expo.widget.decorations.SpaceDecoration;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/*
 * 推荐路线列表
 */
public class RoutesActivity extends BaseActivity<RoutesContract.Presenter> implements RoutesContract.View {

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    List<RouteInfo> mData;
    CommonAdapter mAdapter;

    BaseAdapterItemClickListener<Long> mListener = new BaseAdapterItemClickListener<Long>() {

        @Override
        public void itemClick(View view, int position, Long aLong) {

            mPresenter.clickRoute(String.valueOf(aLong));
            RouteDetailActivity.startActivity(RoutesActivity.this, aLong);
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_routes;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(1, R.string.title_routest_ac);
        initTitleRightTextView(R.string.custom_route, R.color.green_02cd9b, v -> CustomRouteActivity.startActivity(getContext()));
        mData = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new SpaceDecoration((int) getResources().getDimension(R.dimen.dms_20)));
        mRecyclerView.setAdapter(mAdapter = new CommonAdapter(this, R.layout.item_route, mData) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                RouteInfo info = mData.get(position);
                Picasso.with(RoutesActivity.this).load(CommUtils.getFullUrl(info.picUrl)).placeholder(R.drawable.image_default).error(R.drawable.image_default).into((ImageView) holder.getView(R.id.item_route_img));
                holder.setText(R.id.item_route_name, LanguageUtil.chooseTest(info.caption, info.captionen));
                holder.setText(R.id.item_route_hot, getString(R.string.heat) + info.hotCount);

                holder.itemView.setOnClickListener(v -> {
                    mListener.itemClick(v, position, Long.valueOf(info.id));
                });
            }
        });

        mPresenter.getRoutesData();
        mPresenter.getRouterHotCount();
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    /**
     * 启动推荐路线列表页
     *
     * @param context
     */
    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent(context, RoutesActivity.class);
        context.startActivity(in);
    }

    @Override
    public void freshRoutes(List list) {
        if (mData == null) mData = new ArrayList();
        mData.clear();
        if (list != null) mData.addAll(list);
        mAdapter.notifyDataSetChanged();
    }
}
