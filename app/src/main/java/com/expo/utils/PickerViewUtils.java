package com.expo.utils;

import android.content.Context;
import android.graphics.Color;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.expo.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PickerViewUtils {

    /**
     * @param context
     * @param time     yyyy-MM-dd 模式
     * @param listener
     * @return
     */
    public static TimePickerView showTimePickView(Context context, String time, OnTimeSelectListener listener) {
        Calendar selectDate = Calendar.getInstance();
        if (StringUtils.isEmpty(time))
            selectDate.setTime(TimeUtils.getNowDate());
        else
            selectDate.setTime(TimeUtils.string2Date(time, new SimpleDateFormat(Constants.TimeFormat.TYPE_SIMPLE)));
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(TimeUtils.string2Date("1900-01-01", new SimpleDateFormat(Constants.TimeFormat.TYPE_SIMPLE)));
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(TimeUtils.getNowDate());
        return new TimePickerBuilder(context, listener)
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText(context.getResources().getString(R.string.cancel))//取消按钮文字
                .setSubmitText(context.getResources().getString(R.string.ok))//确认按钮文字
                .setContentTextSize(18)//滚轮文字大小
                .setTitleSize(20)//标题文字大小
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setSubmitColor(context.getResources().getColor(R.color.colorAccent))//确定按钮文字颜色
                .setCancelColor(context.getResources().getColor(R.color.gray_99))//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setTextColorOut(context.getResources().getColor(R.color.gray_99))
                .setTextColorCenter(context.getResources().getColor(R.color.colorAccent))
                .setDate(selectDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .build();
    }
}
