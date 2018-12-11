package com.expo.contract.presenter;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.text.TextUtils;

import com.expo.R;
import com.expo.base.ExpoApp;
import com.expo.base.utils.PrefsHelper;
import com.expo.contract.HeartBeatContract;
import com.expo.entity.Message;
import com.expo.entity.User;
import com.expo.map.LocationManager;
import com.expo.module.heart.MessageKindActivity;
import com.expo.module.login.LoginActivity;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.UserHeartBeatResp;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.expo.utils.LocalBroadcastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.Observable;

import static android.content.Context.NOTIFICATION_SERVICE;

public class HeartBeatPresenterImpl extends HeartBeatContract.Presenter {
    public HeartBeatPresenterImpl(HeartBeatContract.View view) {
        super(view);
    }

    @Override
    public void sendHeartBeat() {
        Map<String, Object> params = Http.getBaseParams();
        if (!params.containsKey("Uid") || !params.containsKey("Ukey")) {
            return;
        }
        params.put("Position", LocationManager.getInstance().getDirection());
        //获取上次获取消息的时间
        String time = PrefsHelper.getString(Constants.Prefs.KEY_LAST_MESSAGE_TIME, null);
        if (TextUtils.isEmpty(time)) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -15);
            time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(calendar.getTime());
        }
        params.put("LastMessageTime", time);
        Observable<UserHeartBeatResp> observable = Http.getServer().sendHeartBeat(Http.buildRequestBody(params));
        Http.request(new ResponseCallback<UserHeartBeatResp>() {
            @Override
            public void onResponse(UserHeartBeatResp heartBeatRsb) {
                if (heartBeatRsb.MessageList != null && !heartBeatRsb.MessageList.isEmpty()) {
                    PrefsHelper.setString(Constants.Prefs.KEY_LAST_MESSAGE_TIME, heartBeatRsb.UpdateTime);
                    List<Message> messages = heartBeatRsb.MessageList;
                    //新消息集合不为空
                    if (messages != null && !messages.isEmpty()) {
                        boolean isNewMessage = false;
                        for (Message message : messages) {
                            if (message.getType() == null) {
                                continue;
                            }
                            isNewMessage = isNewMessage || handleMessage(message);
                        }
                        if (isNewMessage)
                            messages.get(0).sendMessageCount(null);
                    }
                }
                mView.setHeartInvTime(heartBeatRsb.HeartInvTime);
            }
        }, observable);
    }

    /**
     * 进行消息处理
     *
     * @param message
     * @return true:消息中心有新消息，false不是消息中心的新消息
     */
    private boolean handleMessage(Message message) {
        if (message == null) return false;
        if ("3".equals(message.getType())) {
//            String currUkey = ExpoApp.getApplication().getUser().getUkey();
            if ("relogin".equals(message.getCommand()) || "logout".equals(message.getCommand())) {// && message.getParams() != null  && currUkey.equals(message.getParams().get("ukey"))
                mDao.clear(User.class);
                ExpoApp.getApplication().setUser(null);
                Intent intent = new Intent();
                intent.putExtra(Constants.EXTRAS.EXTRA_LOGIN_STATE, false);
                LocalBroadcastUtil.sendBroadcast(mView.getContext(), intent, Constants.Action.LOGIN_CHANGE_OF_STATE_ACTION);
                if ("relogin".equals(message.getCommand()))
                    showForceSingOutDialog(R.string.your_account_is_logged_in_on_another_device);
                else
                    showForceSingOutDialog(R.string.your_account_is_locked_you_can_not_login);
            }
            return false;
        } else {
            message.setUid(ExpoApp.getApplication().getUser().getUid());
            mDao.saveOrUpdate(message);
            checkMessage(message);
            LocalBroadcastUtil.sendBroadcast(mView.getContext(), null, Constants.Action.ACTION_RECEIVE_MESSAGE);
            return true;
        }
    }

    private void showForceSingOutDialog(int textRes) {
        new AlertDialog.Builder(ExpoApp.getApplication().getTopActivity())
                .setMessage(textRes)
                .setCancelable(false)
                .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ExpoApp.getApplication().setUser(null);
                        LoginActivity.startActivity(ExpoApp.getApplication().getTopActivity());
                    }
                })
                .show();
    }

    private void checkMessage(Message message) {
        if (isAPNS(message.getMsgKind())) {
            return;
        }
        playNotificationSound();
        if (isAppInsideMsg(message.getMsgKind())) {
            showNotificationMsg(message);
        }
        if (isAppAlert(message.getMsgKind())) {
            showAlertMsg(message);
        }
    }

    private boolean isAppInsideMsg(String msgKind) {
        if ("1".equals(msgKind.substring(1, 2)))
            return true;
        return false;
    }

    private boolean isAppAlert(String msgKind) {
        if ("1".equals(msgKind.substring(2, 3)))
            return true;
        return false;
    }

    private boolean isAPNS(String msgKind) {
        if ("1".equals(msgKind.substring(3, 4)))
            return true;
        return false;
    }

    private void showAlertMsg(Message message) {
        new AlertDialog.Builder(ExpoApp.getApplication().getTopActivity())
                .setTitle(R.string.new_message)
                .setMessage(LanguageUtil.chooseTest(message.getContent(), message.getContentEn()))
                .setNegativeButton(R.string.ok, null)
                .show();
    }

    private void showNotificationMsg(Message message) {
        NotificationManager nom = (NotificationManager) ExpoApp.getApplication().getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(ExpoApp.getApplication().getTopActivity(), MessageKindActivity.class);
        PendingIntent pi = PendingIntent.getActivity(ExpoApp.getApplication().getTopActivity(), 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification = new Notification.Builder(ExpoApp.getApplication().getTopActivity())
                .setContentTitle(LanguageUtil.chooseTest(message.getCaption(), message.getCaptionEn()))
                .setContentText(LanguageUtil.chooseTest(message.getContent(), message.getContentEn()))
                .setSmallIcon(R.mipmap.ic_launcher) // 设置图标
                .setContentIntent(pi)
                .build();
        nom.notify(notifyId++, notification);
    }

    private void playNotificationSound() {
        int id = PrefsHelper.getInt(Constants.Prefs.KEY_RAW_SELECTOR_POSITION, 0);
        Uri uri;
        if (id == 0)
            uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        else
            uri = Uri.parse("android.resource://" + ExpoApp.getApplication().getPackageName() + "/" + Constants.RawResource.resourceIds[id]);
        Ringtone rt = RingtoneManager.getRingtone(ExpoApp.getApplication().getApplicationContext(), uri);
        rt.play();
    }

    private int notifyId = 1;
}
