package com.expo.contract.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.expo.R;
import com.expo.contract.CirnumListContract;
import com.expo.entity.Circum;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.CircumResp;
import com.expo.services.TrackRecordService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

public class CirnumListPresenterImpl extends CirnumListContract.Presenter {

    private DecimalFormat mDecimalFormat;

    public CirnumListPresenterImpl(CirnumListContract.View view) {
        super(view);
    }

    @Override
    public void loadCircumData(int circumType, int page) {
        Map<String, Object> params = Http.getBaseParams();
        params.put("page", page);
        params.put("type", circumType);
        RequestBody requestBody = Http.buildRequestBody( params );
        Observable<CircumResp> observable = Http.getServer().getBussinessCircleListByParams( requestBody );
        Http.request( new ResponseCallback<CircumResp>() {
            @Override
            protected void onResponse(CircumResp rsp) {
                ArrayList<Circum> circums = new ArrayList<>();
                if (null == rsp.circum || TextUtils.isEmpty(rsp.circum)){
                    mView.loadCircumDataRes(null);
                    return;
                }
                try {
                    JSONObject joData = new JSONObject(rsp.circum);
                    String data = joData.getString("data");
                    JSONObject joRecords = new JSONObject(data);
                    String dataRecord = joRecords.getString("records");
                    circums = Http.getGsonInstance().fromJson(dataRecord, new TypeToken<ArrayList<Circum>>(){}.getType());
                    Log.i("=========", ""+rsp.circum);
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    mView.loadCircumDataRes(circums);
                    mView.hideLoadingView();
                }
            }
        }, observable );
    }

    @Override
    public String getDistance(double latitude, double longitude) {
        return "12km";
//        if (null == TrackRecordService.getLocation() || TrackRecordService.getLocation().getLatitude() == 0) {
//            return "";
//        }
//        String units = "m";
//        float distance = AMapUtils.calculateLineDistance( new LatLng( latitude, longitude ),
//                new LatLng( TrackRecordService.getLocation().getLatitude(), TrackRecordService.getLocation().getLongitude() ) );
//        if (distance >= 1000) {
//            units = "km";
//            distance = distance / 1000;
//        }
//        if (mDecimalFormat == null) {
//            mDecimalFormat = new DecimalFormat( "#######.00" );
//        }
//        return mDecimalFormat.format( distance ) + units;
    }
}
