package com.expo.contract.presenter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;

import com.expo.base.ExpoApp;
import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.base.utils.PrefsHelper;
import com.expo.contract.HeartBeatContract;
import com.expo.entity.Message;
import com.expo.entity.User;
import com.expo.map.LocationManager;
import com.expo.module.login.LoginActivity;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.UserHeartBeatResp;
import com.expo.utils.Constants;
import com.expo.utils.LocalBroadcastUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.Observable;

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
                    if (messages != null && !messages.isEmpty()) {                                  //新消息集合不为空
                        String uid = ExpoApp.getApplication().getUser().getUid();
                        boolean isNewMessage = false;
                        for (Message message : messages) {
                            if (message.getType() == null) {
                                continue;
                            }
                            message.setUid(uid);
                            isNewMessage = isNewMessage || handleMessage(message);
                        }
                        if (isNewMessage)
                            messages.get(0).sendMessageCount(null);
                    }
                }
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
            String currUkey = ExpoApp.getApplication().getUser().getUkey();
            if ("relogin".equals(message.getCommand()) && message.getParams() != null && currUkey.equals(message.getParams().get("ukey"))) {
                mDao.clear(User.class);
                ExpoApp.getApplication().setUser(null);
                Intent intent = new Intent();
                intent.putExtra(Constants.EXTRAS.EXTRA_LOGIN_STATE, false);
                LocalBroadcastUtil.sendBroadcast(mView.getContext(), intent, Constants.Action.LOGIN_CHANGE_OF_STATE_ACTION);
                showForceSingOutDialog();
            }
            return false;
        } else {
            mDao.saveOrUpdate(message);
            LocalBroadcastUtil.sendBroadcast(mView.getContext(), null, Constants.Action.ACTION_RECEIVE_MESSAGE);
            return true;
        }
    }

    private void showForceSingOutDialog() {
        new AlertDialog.Builder(ExpoApp.getApplication().getTopActivity())
                .setMessage("您的账号在其他设备登录")
                .setCancelable(false)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ExpoApp.getApplication().setUser(null);
                        LoginActivity.startActivity(ExpoApp.getApplication().getTopActivity());
                    }
                })
                .show();
    }
}
