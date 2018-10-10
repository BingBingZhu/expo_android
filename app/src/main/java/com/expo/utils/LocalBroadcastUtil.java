package com.expo.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

public class LocalBroadcastUtil {

    public static void sendBroadcast(Context context, Intent intent, String... actions){
        if (null == intent)
            intent = new Intent();
        for (String action : actions){
            intent.setAction(action);
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void registerReceiver(Context context, BroadcastReceiver receiver, String... actions){
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        IntentFilter filter = new IntentFilter();
        for (String action : actions){
            filter.addAction(action);
        }
        localBroadcastManager.registerReceiver(receiver, filter);
    }

    public static void unregisterReceiver(Context context, BroadcastReceiver receiver){
        if (null != receiver)
            LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }
}
