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
//                System.out.println(res);
//                OkHttpClient client = new OkHttpClient();
//                //构建FormBody，传入要提交的参数
//                FormBody formBody = new FormBody.Builder()
//                        .add("access_token", getAuth())
//                        .add("image", imgBase64)
//                        .build();
//                final Request request = new Request.Builder()
//                        .url("https://aip.baidubce.com/rest/2.0/image-classify/v2/dish")
//                        .post(formBody)
//                        .build();
//                Call call = client.newCall(request);
//                call.enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        ToastHelper.showLong("Post Parameter 失败");
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        String responseStr = response.body().string();
//                        System.out.print(responseStr);
////                        ToastHelper.showLong(responseStr);
//                    }
//                });
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

    /**
     * 获取权限token
     *
     * @return 返回示例：
     * {
     * "access_token": "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567",
     * "expires_in": 2592000
     * }
     */
    public static String getAuth() {
        // 官网获取的 API Key 更新为你注册的
        String clientId = "7pbSbZYAATD3S6IgcuFYV7i4";
        // 官网获取的 Secret Key 更新为你注册的
        String clientSecret = "4wYeGPE31VaofZQouRmR9HMt2ZVGsRAK";
        return getAuth(clientId, clientSecret);
    }

    /**
     * 获取API访问token
     * 该token有一定的有效期，需要自行管理，当失效时需重新获取.
     *
     * @param ak - 百度云官网获取的 API Key
     * @param sk - 百度云官网获取的 Securet Key
     * @return assess_token 示例：
     * "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567"
     */
    public static String getAuth(String ak, String sk) {
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + ak
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + sk;
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.err.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            /**
             * 返回结果示例
             */
            System.err.println("result:" + result);
            JSONObject jsonObject = new JSONObject(result);
            String access_token = jsonObject.getString("access_token");
            return access_token;
        } catch (Exception e) {
            System.err.printf("获取token失败！");
            e.printStackTrace(System.err);
        }
        return null;
    }
}
