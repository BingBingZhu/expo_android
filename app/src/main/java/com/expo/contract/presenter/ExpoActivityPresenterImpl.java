package com.expo.contract.presenter;

import com.blankj.utilcode.util.TimeUtils;
import com.expo.base.utils.DateUtils;
import com.expo.contract.ExpoActivityContract;

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
    public void goNextMonth(long time, int month) {
        mView.freshDate(getDateList(DateUtils.getDate(time, 0, month, 0)));
    }

    @Override
    public void loadActivity(String date, int time) {

    }

    private List<Long> getDateList(long time) {
        String date = TimeUtils.millis2String(time, new SimpleDateFormat("yyyy-MM"));
        time = TimeUtils.string2Millis(date, new SimpleDateFormat("yyyy-MM"));
        int days = DateUtils.getMonthDays(time);
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            list.add(time + i * 24 * 3600 * 1000);
        }
        return list;
    }

}
