package com.expo.module.main.find.examine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.BaseAdapterItemClickListener;
import com.expo.contract.ExamineContract;
import com.expo.entity.RouteInfo;
import com.expo.module.routes.RouteDetailActivity;
import com.expo.utils.CommUtils;
import com.expo.utils.LanguageUtil;
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
public class FindExamineActivity extends BaseActivity<ExamineContract.Presenter> implements ExamineContract.View {

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    List<RouteInfo> mData;
    CommonAdapter mAdapter;

    BaseAdapterItemClickListener<Long> mListener = new BaseAdapterItemClickListener<Long>() {

        @Override
        public void itemClick(View view, int position, Long aLong) {

            mPresenter.clickRoute(String.valueOf(aLong));
            RouteDetailActivity.startActivity(FindExamineActivity.this, aLong);
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_routes;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(1, R.string.title_routest_ac);

        mData = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new SpaceDecoration((int) getResources().getDimension(R.dimen.dms_20)));
        mRecyclerView.setAdapter(mAdapter = new CommonAdapter(this, R.layout.item_examine, mData) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                RouteInfo info = mData.get(position);
                Picasso.with(FindExamineActivity.this).load(CommUtils.getFullUrl(info.picUrl)).placeholder(R.drawable.image_default).error(R.drawable.image_default).into((ImageView) holder.getView(R.id.icon_center_img));
                holder.setText(R.id.item_examine_content, LanguageUtil.chooseTest(info.caption, info.captionen));
                holder.setText(R.id.item_examine_time, "2017-1-1");
                holder.setText(R.id.item_examine_state, "审核中");
                holder.setText(R.id.item_examine_reason, "内容不符合发布规定");

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
        Intent in = new Intent(context, FindExamineActivity.class);
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
