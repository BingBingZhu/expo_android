package com.expo.module.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.ExpoActivityContract;
import com.expo.entity.ExpoActivityInfo;
import com.expo.module.webview.WebActivity;
import com.expo.utils.CommUtils;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ExpoActivityActivity extends BaseActivity<ExpoActivityContract.Presenter> implements ExpoActivityContract.View {

    @BindView(R.id.expo_activity_month_recycler)
    RecyclerView mRvMonthRecycler;
    @BindView(R.id.expo_activity_date_recycler)
    RecyclerView mRvDateRecycler;
    @BindView(R.id.expo_activity_recycler)
    RecyclerView mRvActivityRecycler;

    View mSelectMonthView;
    View mSelectDateView;
    View mSelectTimeView;

    CommonAdapter mMonthAdapter;
    CommonAdapter mDateAdapter;
    CommonAdapter mActivityAdapter;

    List<Long> mMonthList;
    List<Long> mDateList;
    List<ExpoActivityInfo> mActivityAllList;
    List<ExpoActivityInfo> mActivityShowList;

    long mSelectMonth;
    long mSelectTime;

    int mMonthScrollX;

    int mTimeType;//区分全天、上午、下午、晚上，分别为0、1、2、3
    boolean isInit;

    @Override
    protected int getContentView() {
        return R.layout.activity_expo_activity;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(0, R.string.title_expo_activity);
        initMonthRecycler();
        initDateRecycler();
        initActivityRecycler();
        mTimeType = 0;
        mPresenter.loadActivityInfo(mSelectTime, mTimeType);
    }

    private void initMonthRecycler() {
        mMonthList = mPresenter.getMonthList();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvMonthRecycler.setLayoutManager(manager);
        mRvMonthRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mMonthScrollX += dx;
            }
        });
        int nowPosition = 2;
        mSelectMonth = TimeUtils.string2Millis(TimeUtils.millis2String(TimeUtils.getNowMills(), new SimpleDateFormat("yyyy-MM")), new SimpleDateFormat("yyyy-MM"));
        for (int i = 0; i < mMonthList.size(); i++) {
            if (mSelectMonth == mMonthList.get(i)) {
                mRvMonthRecycler.smoothScrollBy(ScreenUtils.getScreenWidth() * Math.max(0, i - 2) / 5, 0);
                nowPosition = i;
                break;
            }
        }
        mSelectMonth = mMonthList.get(nowPosition);
        mRvMonthRecycler.setAdapter(mMonthAdapter = new CommonAdapter(this, R.layout.item_expo_activity_month, mMonthList) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                holder.itemView.getLayoutParams().width = ScreenUtils.getScreenWidth() / 5;
                if (mMonthList.get(position) == 0L) {
                    holder.itemView.setSelected(false);
                    return;
                }
                TimeUtils.getValueByCalendarField(mMonthList.get(position), Calendar.MONTH);
                if (mSelectMonth == mMonthList.get(position)) {
                    holder.itemView.setSelected(true);
                    mSelectMonthView = holder.itemView;
                } else {
                    holder.itemView.setSelected(false);
                }
                holder.setText(R.id.month, getString(getResources().getIdentifier("month_" + (TimeUtils.getValueByCalendarField(mMonthList.get(position), Calendar.MONTH) + 1), "string", AppUtils.getAppPackageName())));
                holder.itemView.setOnClickListener(v -> {
                    if (mSelectMonthView != null) {
                        mSelectMonthView.setSelected(false);
                    }

                    mSelectMonthView = v;
                    mSelectMonthView.setSelected(true);

                    mRvMonthRecycler.smoothScrollBy(ScreenUtils.getScreenWidth() * Math.max(0, position - 2) / 5 - mMonthScrollX, 0);

                    mPresenter.loadDate(mMonthList.get(position));
                });
            }

        });
    }

    private void initDateRecycler() {
        isInit = true;
        mDateList = new ArrayList();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvDateRecycler.setLayoutManager(manager);
        mSelectTime = TimeUtils.string2Millis(TimeUtils.millis2String(TimeUtils.getNowMills(), new SimpleDateFormat("yyyy-MM-dd")), new SimpleDateFormat("yyyy-MM-dd"));
        mRvDateRecycler.setAdapter(mDateAdapter = new CommonAdapter(this, R.layout.item_expo_activity_date, mDateList) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                if (mSelectTime == mDateList.get(position)) {
                    if (mSelectDateView != null)
                        mSelectDateView.setSelected(false);
                    mSelectTime = mDateList.get(position);
                    mSelectDateView = holder.itemView;
                    holder.itemView.setSelected(true);
                } else {
                    holder.itemView.setSelected(false);
                }
                holder.itemView.getLayoutParams().width = ScreenUtils.getScreenWidth() / 5;
                holder.setText(R.id.item_date_day, String.valueOf(position + 1));
                holder.setText(R.id.item_date_week, getResources().getString(
                        getResources().getIdentifier("expo_activity_" + TimeUtils.getUSWeek(mDateList.get(position)).toLowerCase(), "string", AppUtils.getAppPackageName())));
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
        int position = 2;

        for (int i = 0; i < mMonthList.size(); i++) {
            if (mSelectMonth == mMonthList.get(i)) {
                mRvMonthRecycler.smoothScrollBy(ScreenUtils.getScreenWidth() * Math.max(0, i - 2) / 5, 0);
                position = i;
                break;
            }
        }
        mPresenter.loadDate(mMonthList.get(position));
    }

    private void initActivityRecycler() {
        mActivityShowList = new ArrayList<>();
        mRvActivityRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRvActivityRecycler.setAdapter(mActivityAdapter = new CommonAdapter(this, R.layout.item_expo_activity_activity, mActivityShowList) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                ExpoActivityInfo info = mActivityShowList.get(position);
                Picasso.with(getContext()).load(CommUtils.getFullUrl("url")).into((ImageView) holder.getView(R.id.item_activity_img));
                holder.setText(R.id.expo_activity_name, LanguageUtil.chooseTest(info.getCaption(), info.getCaptionEn()));
                List<String> times = Arrays.asList(info.getTimes().split("/"));
                for (int i = times.size() - 1; i >= 0; i--) {
                    String start = times.get(i).split("-")[0];
                    Long time = TimeUtils.string2Millis(start, new SimpleDateFormat("mm:ss"));
                    if (mTimeType == 1 && time > Constants.TimeType.MORNING) {
                        times.remove(i);
                    }
                    if (mTimeType == 2 && (time < Constants.TimeType.MORNING || time > Constants.TimeType.AFTERNOON)) {
                        times.remove(i);
                    }
                    if (mTimeType == 3 && time < Constants.TimeType.AFTERNOON) {
                        times.remove(i);
                    }
                }

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < times.size(); i++) {
                    if (i == 1 || i == 3)
                        sb.append("\t");
                    else if (i == 2)
                        sb.append("\n");
                    sb.append(times.get(i));
                }
                holder.setText(R.id.expo_activity_time, sb.toString());
                holder.itemView.setOnClickListener(v -> WebActivity.startActivity(ExpoActivityActivity.this, LanguageUtil.chooseTest(info.getLinkH5Url(), info.getLinkH5UrLen()), LanguageUtil.chooseTest(info.getCaption(), info.getCaptionEn())));
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
        mDateAdapter.notifyDataSetChanged();

        for (int i = 0; i < mDateList.size(); i++) {
            if (mSelectTime == mDateList.get(i)) {
                mRvDateRecycler.smoothScrollBy(ScreenUtils.getScreenWidth() * Math.max(0, i - 2) / 5, 0);
                return;
            }
        }
        mRvDateRecycler.smoothScrollToPosition(0);
    }

    @Override
    public void freshActivityInfo(List<ExpoActivityInfo> list) {
        mActivityShowList.clear();
        if (list != null)
            mActivityShowList.addAll(list);
        mActivityAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.expand_activity_morning, R.id.expand_activity_noon, R.id.expand_activity_afternoon})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.expand_activity_morning:
                mTimeType = 1;
                break;
            case R.id.expand_activity_noon:
                mTimeType = 2;
                break;
            case R.id.expand_activity_afternoon:
                mTimeType = 3;
                break;
        }
        selectDataView(view);
        mPresenter.loadActivityInfo(mSelectTime, mTimeType);
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
