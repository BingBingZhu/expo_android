package com.expo.module.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.ExpoActivityContract;
import com.expo.utils.CommUtils;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ExpoActivityActivity extends BaseActivity<ExpoActivityContract.Presenter> implements ExpoActivityContract.View {

    @BindView(R.id.expo_activity_date)
    TextView mTvDateTitle;
    @BindView(R.id.expo_activity_date_recycler)
    RecyclerView mRvDate;
    @BindView(R.id.expo_activity_recycler)
    RecyclerView mRvActivity;

    View mSelectDateView;
    View mSelectTimeView;

    CommonAdapter mDateAdapter;
    CommonAdapter mActivityAdapter;

    List<Long> mDateList;
    List<String> mActivityList;
    long mSelectTime;


    @Override
    protected int getContentView() {
        return R.layout.activity_expo_activity;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(0, R.string.title_expo_activity);
        initRecyclerView();
        mPresenter.loadDate(TimeUtils.getNowMills());
    }

    private void initRecyclerView() {
        mDateList = new ArrayList();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvDate.setLayoutManager(manager);
        mSelectTime = TimeUtils.string2Millis(TimeUtils.millis2String(TimeUtils.getNowMills(), new SimpleDateFormat("yyyy-MM-dd")), new SimpleDateFormat("yyyy-MM-dd"));
        mRvDate.setAdapter(mDateAdapter = new CommonAdapter(this, R.layout.item_expo_activity_date, mDateList) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                if (mSelectTime == mDateList.get(position)) {
                    if (mSelectDateView != holder.itemView) {
                        if (mSelectDateView != null)
                            mSelectDateView.setSelected(false);
                        mSelectTime = mDateList.get(position);
                        mSelectDateView = holder.itemView;
                        holder.itemView.setSelected(true);
                    }
                } else {
                    holder.itemView.setSelected(false);
                }
                holder.itemView.getLayoutParams().width = ScreenUtils.getScreenWidth() / 5;
                holder.setText(R.id.item_date_day, String.valueOf(position + 1));
                holder.setText(R.id.item_date_week, TimeUtils.getUSWeek(mDateList.get(position)).substring(0, 3));
                holder.itemView.setOnClickListener(v -> {
                    if (mSelectDateView != null)
                        mSelectDateView.setSelected(false);
                    mSelectTime = mDateList.get(position);
                    mSelectDateView = v;
                    mSelectDateView.setSelected(true);
                    selectDataView(null);
                });
            }

        });

        mActivityList = new ArrayList<>();
        mRvActivity.setLayoutManager(new LinearLayoutManager(this));
        mRvActivity.setAdapter(mActivityAdapter = new CommonAdapter(this, R.layout.item_expo_activity_activity, mActivityList) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                Picasso.with(getContext()).load(CommUtils.getFullUrl("url")).into((ImageView) holder.getView(R.id.item_activity_img));
                holder.setText(R.id.expo_activity_name, "名字");
                holder.setText(R.id.expo_activity_time, "时间");
            }
        });
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ExpoActivityActivity.class));
    }

    @Override
    public void freshDate(List<Long> list) {
        mDateList.clear();
        mDateList.addAll(list);
        mTvDateTitle.setText(TimeUtils.millis2String(mDateList.get(0), new SimpleDateFormat("yyyy年MM月")));
        mDateAdapter.notifyDataSetChanged();

        for (int i = 0; i < mDateList.size(); i++) {
            if (mSelectTime == mDateList.get(i)) {
                mRvDate.getLayoutManager().scrollToPosition(i);
                return;
            }
        }
        mRvDate.getLayoutManager().scrollToPosition(0);
    }

    @Override
    public void freshData(List list) {
        ToastHelper.showShort("刷新数据");
    }

    @OnClick({R.id.expo_activity_left, R.id.expo_activity_right, R.id.expand_activity_morning, R.id.expand_activity_afternoon, R.id.expand_activity_night})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.expo_activity_left:
                mPresenter.goNextMonth(mDateList.get(0), -1);
                break;
            case R.id.expo_activity_right:
                mPresenter.goNextMonth(mDateList.get(0), 1);
                break;
            case R.id.expand_activity_morning:
                selectDataView(view);
                break;
            case R.id.expand_activity_afternoon:
                selectDataView(view);
                break;
            case R.id.expand_activity_night:
                selectDataView(view);
                break;
        }
    }

    private void selectDataView(View view) {
        if (view == null) {
            if (mSelectTimeView != null)
                mSelectTimeView.setSelected(false);
            mSelectTimeView = null;
            return;
        }
        if (mSelectTimeView == view) {
            mSelectTimeView.setSelected(false);
            mSelectTimeView = null;
            return;//重新点击
        }
        if (mSelectTimeView != null)
            mSelectTimeView.setSelected(false);
        mSelectTimeView = view;
        mSelectTimeView.setSelected(true);
    }

}
