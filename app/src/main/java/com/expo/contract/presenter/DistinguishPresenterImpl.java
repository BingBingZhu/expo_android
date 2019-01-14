package com.expo.contract.presenter;

import android.os.Handler;
import android.os.Message;

import com.baidu.aip.imageclassify.AipImageClassify;
import com.expo.contract.DistinguishContract;
import com.expo.network.Http;
import com.expo.network.response.GetBaiduDisting_Rsb;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

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
