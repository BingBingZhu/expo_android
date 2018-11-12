package com.expo.contract.presenter;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.baidu.aip.imageclassify.AipImageClassify;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.DistinguishContract;
import com.expo.entity.Plant;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.BaseResponse;
import com.expo.network.response.GetBaiduDisting_Rsb;
import com.expo.network.response.GetDistinguishPlantList_Rsb;
import com.expo.network.response.VerificationCodeResp;
import com.expo.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DistinguishPresenterImpl extends DistinguishContract.Presenter {

    private final static String APP_ID = "14636554";
    private final static String API_KEY = "7pbSbZYAATD3S6IgcuFYV7i4";
    private final static String SECRET_KEY = "4wYeGPE31VaofZQouRmR9HMt2ZVGsRAK";

    public DistinguishPresenterImpl(DistinguishContract.View view) {
        super(view);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mView.onBaiduDistinguishComplete((GetBaiduDisting_Rsb) msg.obj);
            mView.hideLoadingView();
        }
    };

    @Override
    public void distinguishPlant(String imgBase64) {
        mView.showLoadingView();
        new Thread() {
            @Override
            public void run() {
                AipImageClassify client = new AipImageClassify(APP_ID, API_KEY, SECRET_KEY);
                HashMap<String, String> options = new HashMap<>();
                options.put("baike_num", "5");
                // 参数为本地路径
                JSONObject res = client.plantDetect(imgBase64, options);
                try {
                    String result = res.getJSONArray("result").getJSONObject(0).toString();
                    GetBaiduDisting_Rsb baiduDisting_rsb = Http.getGsonInstance().fromJson(result, GetBaiduDisting_Rsb.class);
                    handler.sendMessage(handler.obtainMessage(0, baiduDisting_rsb));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
//        Map<String, String> params = new HashMap<>();
//        params.put("img_base64", imgBase64);
//        mView.showLoadingView();
//        Observable<GetDistinguishPlantList_Rsb> observable = Http.getServer().distinguishPlant(Constants.URL.DISTINGUISH_PLANT, params);
//        Http.request(new ResponseCallback<GetDistinguishPlantList_Rsb>() {
//            @Override
//            protected void onResponse(GetDistinguishPlantList_Rsb rsp) {
//                if ("0".equals(rsp.Status)) {                                   //是否识别成功
//                    mView.onDistinguishComplete(rsp.Result);
//                } else {
//                    mView.onDistinguishComplete(null);
//                }
//            }
//        }, observable);
    }
}
