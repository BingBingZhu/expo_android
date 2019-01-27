package com.expo.contract.presenter;

import android.os.Handler;
import android.os.Looper;

import com.expo.base.ExpoApp;
import com.expo.base.utils.LogUtils;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.MessagesContract;
import com.expo.contract.SceneContract;
import com.expo.db.QueryParams;
import com.expo.entity.CommonInfo;
import com.expo.entity.Message;
import com.expo.network.Http;
import com.expo.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ScenePresenterImpl extends SceneContract.Presenter {
    public ScenePresenterImpl(SceneContract.View view) {
        super(view);
    }


    @Override
    public void getExpo(String type, boolean fresh, int page, int count) {

    }
}
