package com.expo.base.utils;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by LS on 2017/10/26.
 */

public class ActivityHelper {
    private static List<Activity> activities;

    /**
     * 添加启动的活动
     *
     * @param activity
     */
    public synchronized static void add(Activity activity) {
        if (activities == null)
            activities = new LinkedList<>();
        if (!activities.contains(activity))
            activities.add(activity);
    }

    /**
     * 移除销毁的活动
     *
     * @param activity
     */
    public synchronized static void remove(Activity activity) {
        if (activities != null && activities.contains(activity))
            activities.remove(activity);
    }

    /**
     * 关闭所有活动退出程序
     */
    public synchronized static void finishAll() {
        if (activities == null) return;
        for (int i = 0; i < activities.size(); i++) {
            activities.remove(i).finish();
        }
        activities = null;
    }
}
