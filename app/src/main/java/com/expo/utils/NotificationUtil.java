package com.expo.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;

import com.expo.R;
import com.expo.base.ExpoApp;
import com.expo.base.utils.PrefsHelper;

import java.io.File;
import java.net.URI;

import cn.jpush.android.api.BasicPushNotificationBuilder;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationUtil {

    private static NotificationUtil notificationUtil;

    /**
     * 获取定位控制实例
     */
    public synchronized static NotificationUtil getInstance() {
        if (notificationUtil == null) {
            notificationUtil = new NotificationUtil();
        }
        return notificationUtil;
    }

    private static int pushNum = 0;
    private static final String PUSH_CHANNEL_ID = "EXPO_PUSH_NOTIFY_ID";
    private static final String PUSH_CHANNEL_NAME = "EXPO_PUSH_NOTIFY_NAME";

    public void showNotification(String title, String content, Class<? extends Activity> activity) {
        NotificationManager notificationManager = (NotificationManager) ExpoApp.getApplication().getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(PUSH_CHANNEL_ID, PUSH_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ExpoApp.getApplication());
        Intent notificationIntent;
        if (null != activity)
            notificationIntent = new Intent(ExpoApp.getApplication().getTopActivity(), activity);
        else
            notificationIntent = new Intent();
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(ExpoApp.getApplication().getTopActivity(), 0, notificationIntent, 0);
        builder.setContentTitle(title)//设置通知栏标题
            .setContentIntent(pendingIntent) //设置通知栏点击意图
            .setContentText(content)
            .setNumber(++pushNum)
            .setTicker(content) //通知首次出现在通知栏，带上升动画效果的
            .setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
            .setSmallIcon(R.mipmap.ic_launcher)//设置通知小ICON
            .setChannelId(PUSH_CHANNEL_ID);
        Notification notification = builder.getNotification();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults = Notification.DEFAULT_LIGHTS;
        if (notificationManager != null) {
            notificationManager.notify(pushNum, notification);
            playSound();
        }
    }

    /*public void showProgressNotification(){
        NotificationManager notificationManager = (NotificationManager) ExpoApp.getApplication().getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(PUSH_CHANNEL_ID, PUSH_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ExpoApp.getApplication());
        Intent notificationIntent;
        if (null != activity)
            notificationIntent = new Intent(ExpoApp.getApplication().getTopActivity(), activity);
        else
            notificationIntent = new Intent();
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(ExpoApp.getApplication().getTopActivity(), 0, notificationIntent, 0);
        builder.setContentTitle(title)//设置通知栏标题
                .setContentIntent(pendingIntent) //设置通知栏点击意图
                .setContentText(content)
                .setNumber(++pushNum)
                .setTicker(content) //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setSmallIcon(R.mipmap.ic_launcher)//设置通知小ICON
                .setChannelId(PUSH_CHANNEL_ID);
        Notification notification = builder.getNotification();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults = Notification.DEFAULT_LIGHTS;
        if (notificationManager != null) {
            notificationManager.notify(pushNum, notification);
            playSound();
        }
    }*/

    private Uri getSoundUri() {
        int id = PrefsHelper.getInt(Constants.Prefs.KEY_RAW_SELECTOR_POSITION, 0);
        Uri uri;
        if (id == 0) {
            uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        } else {
            uri = Uri.parse("android.resource://" + ExpoApp.getApplication().getPackageName() + "/" + Constants.RawResource.resourceIds[id]);
        }
        return uri;
    }

    private void playSound() {
        Ringtone rt = RingtoneManager.getRingtone(ExpoApp.getApplication().getApplicationContext(), getSoundUri());
        rt.play();
    }
}
