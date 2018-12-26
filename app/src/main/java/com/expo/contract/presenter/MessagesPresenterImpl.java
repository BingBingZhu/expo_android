package com.expo.contract.presenter;

import android.os.Handler;
import android.os.Looper;

import com.expo.base.ExpoApp;
import com.expo.base.utils.LogUtils;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.MessagesContract;
import com.expo.db.QueryParams;
import com.expo.entity.CommonInfo;
import com.expo.entity.Message;
import com.expo.entity.User;
import com.expo.network.Http;
import com.expo.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MessagesPresenterImpl extends MessagesContract.Presenter {
    public MessagesPresenterImpl(MessagesContract.View view) {
        super(view);
    }

    @Override
    public void getMessage(String type) {
        QueryParams params = new QueryParams()
                .add("eq", "type", type)
                .add("and")
                .add("eq", "uid", ExpoApp.getApplication().getUser().getUid())
                .add("orderBy", "create_time", "desc");
        List<Message> messages = mDao.query(Message.class, params);
        if (type.equals("4")) {      // 加入日期分组数据
            Map<String, List<Message>> map = new HashMap<>();
            List<Message> itemMessages = new ArrayList<>();
            String messageDate = "";
            for (int i = 0; i < messages.size(); i++) {
                if (messageDate.equals("") || messageDate.equals(messages.get(i).getCreateTime().split(" ")[0])) {
                    itemMessages.add(messages.get(i));
                } else {
                    map.put(messageDate, itemMessages);
                    itemMessages = new ArrayList<>();
                    itemMessages.add(messages.get(i));
                }
                if (i == messages.size() - 1) {
                    map.put(messages.get(i).getCreateTime().split(" ")[0], itemMessages);
                }
                messageDate = messages.get(i).getCreateTime().split(" ")[0];
            }
            messages = new ArrayList<>();
            Message message = new Message();
            for (String key : map.keySet()) {
                message.setCreateTime(key);
                message.setRead(true);
                messages.add(message);
                for (Message msg : map.get(key)) {
                    messages.add(msg);
                }
            }
        }
        mView.freshMessageList(messages);
    }

    @Override
    public void delMessage(long id, int position) {
        mDao.delete(Message.class, id);
        mView.delMessage(position);
    }

    @Override
    public void setMessageRead(Message message) {
        mDao.saveOrUpdate(message);
    }

    @Override
    public String loadCommonInfo(String type) {
        CommonInfo info = mDao.unique(CommonInfo.class, new QueryParams()
                .add("eq", "type", type));
        if (info != null) {
            return info.getLinkUrl();
        }
        return null;
    }

    @Override
    public void getOrderInfo(String linkId) {
        mView.showLoadingView();
        new Thread() {
            @Override
            public void run() {
                try {
                    String url = Constants.URL.BASE_URL + "Terminal/findOrderDetails";
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Map<String, Object> params = Http.getBaseParams();
                    params.put("orderid", linkId);
                    LogUtils.e("aaaaaaaaaaa", "订单id： " + linkId);
                    RequestBody requestBody = Http.buildRequestBody(params);
                    Request request = new Request.Builder()
                            .url(url)
                            .post(requestBody)
                            .build();
                    Call call = okHttpClient.newCall(request);
                    Response response = call.execute();
                    String data = response.body().string();
                    new Handler(Looper.getMainLooper())
                            .post(() -> {
                                LogUtils.e("aaaaaaaaaaa", "订单信息： " + data);
                                try {
                                    JSONObject jo = new JSONObject(data);
                                    int code = jo.getInt("Resb");
                                    mView.hideLoadingView();
                                    switch (code){
                                        case 200:
                                            mView.gotoInfoPage(data);
                                            break;
                                        case 306:
                                            ToastHelper.showShort("订单信息不存在");
                                            break;
                                        default:
                                            ToastHelper.showShort("服务端错误");
                                            break;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
