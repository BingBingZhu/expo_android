package com.expo.contract.presenter;

import com.blankj.utilcode.util.TimeUtils;
import com.expo.R;
import com.expo.base.utils.DateUtils;
import com.expo.contract.ExpoActivityContract;
import com.expo.db.QueryParams;
import com.expo.entity.Encyclopedias;
import com.expo.entity.ExpoActivityInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ExpoActivityPresenterImpl extends ExpoActivityContract.Presenter {
    public ExpoActivityPresenterImpl(ExpoActivityContract.View view) {
        super(view);
    }

    @Override
    public void loadDate(long time) {
        mView.freshDate(getDateList(time));
    }

    @Override
    public List<Long> getMonthList() {
        List<Long> list = new ArrayList<>();
        list.add(0L);
        list.add(0L);
        list.add(TimeUtils.string2Millis("2019-01", new SimpleDateFormat("yyyy-MM")));
        list.add(TimeUtils.string2Millis("2019-02", new SimpleDateFormat("yyyy-MM")));
        list.add(TimeUtils.string2Millis("2019-03", new SimpleDateFormat("yyyy-MM")));
        list.add(TimeUtils.string2Millis("2019-04", new SimpleDateFormat("yyyy-MM")));
        list.add(TimeUtils.string2Millis("2019-05", new SimpleDateFormat("yyyy-MM")));
        list.add(TimeUtils.string2Millis("2019-06", new SimpleDateFormat("yyyy-MM")));
        list.add(TimeUtils.string2Millis("2019-07", new SimpleDateFormat("yyyy-MM")));
        list.add(0L);
        list.add(0L);
        return list;
    }

    @Override
    public void loadActivityInfo(long time, int type) {
        QueryParams params = new QueryParams()
                .add("lt", "start_time", time)
                .add("and")
                .add("gt", "end_time", time);
        if (type == 1) {
            List<Integer> list = new ArrayList<>();
            list.add(1);
            list.add(3);
            list.add(5);
            list.add(7);
            params.add("and");
            params.add("in", "time_interval", list);
        } else if (type == 2) {
            List<Integer> list = new ArrayList<>();
            list.add(2);
            list.add(3);
            list.add(6);
            list.add(7);
            params.add("and");
            params.add("in", "time_interval", list);
        } else if (type == 3) {
            List<Integer> list = new ArrayList<>();
            list.add(4);
            list.add(5);
            list.add(6);
            list.add(7);
            params.add("and");
            params.add("orderBy", "time_interval", list);
        }
        params.add("ordey", "recommended_idx");
        mView.freshActivityInfo(mDao.query(ExpoActivityInfo.class, params));
    }

    private List<Long> getDateList(long time) {
        String date = TimeUtils.millis2String(time, new SimpleDateFormat("yyyy-MM"));
        time = TimeUtils.string2Millis(date, new SimpleDateFormat("yyyy-MM"));
        int days = DateUtils.getMonthDays(time);
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            list.add(time + i * 24L * 3600L * 1000L);
        }
        return list;
    }

}
