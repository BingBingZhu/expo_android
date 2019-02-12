package com.expo.contract.presenter;

import android.text.TextUtils;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.expo.base.ExpoApp;
import com.expo.contract.WebExpoActivityContract;
import com.expo.contract.WebTemplateContract;
import com.expo.db.QueryParams;
import com.expo.entity.CommonInfo;
import com.expo.entity.Encyclopedias;
import com.expo.entity.ExpoActivityInfo;
import com.expo.entity.Schedule;
import com.expo.entity.Venue;
import com.expo.module.webview.WebExpoActivityActivity;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.BaseResponse;
import com.expo.utils.LanguageUtil;
import com.expo.utils.NotificationUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.reactivex.Observable;
import okhttp3.RequestBody;

public class WebExpoActivityPresenterImpl extends WebExpoActivityContract.Presenter {

    public WebExpoActivityPresenterImpl(WebExpoActivityContract.View view) {
        super(view);
    }

    @Override
    public ExpoActivityInfo loadEncyclopediaById(long id) {
        return mDao.queryById(ExpoActivityInfo.class, id);
    }

    @Override
    public Venue loadSceneByWikiId(long id) {
        return mDao.queryById(Venue.class, id);
    }

    @Override
    public Schedule loadScheduleByWikiId(long id) {
        return mDao.unique(Schedule.class, new QueryParams()
                .add("eq", "link_id", String.valueOf(id)));
    }

    @Override
    public String toJson(Object obj) {
        return Http.getGsonInstance().toJson(obj);
    }

    @Override
    public void scoreChange(String type, String wikiId) {
        Map<String, Object> params = Http.getBaseParams();
        params.put("type", type);
        params.put("wikiid", wikiId);
        RequestBody requestBody = Http.buildRequestBody(params);
        Observable<BaseResponse> observable = Http.getServer().userScoreChange(requestBody);
        Http.request(new ResponseCallback<BaseResponse>() {
            @Override
            protected void onResponse(BaseResponse rsp) {
                mView.addScore();
                NotificationUtil.getInstance().showNotification("新增积分通知", "获取到新的积分，可在积分详情查看", null);
            }
        }, observable);
    }

    @Override
    public String loadCommonInfo(String type) {
        CommonInfo info = mDao.unique(CommonInfo.class, new QueryParams()
                .add("eq", "type", type));
        if (info != null)
            return info.getLinkUrl();
        return null;
    }

    @Override
    public String getRecommendAndTodayExpoActivitys(Long time, long id) {
        List<ExpoActivityInfo> today = mDao.query(ExpoActivityInfo.class, new QueryParams()
                .add("lt", "start_time", time)
                .add("and")
                .add("gt", "end_time", time));
//                .add("and")
//                .add("eq", "link_id", id));
        List<ExpoActivityInfo> recomment = mDao.query(ExpoActivityInfo.class, new QueryParams()
                .add("eq", "is_recommended", 1));
//                .add("and")
//                .add("eq", "link_id", id));
        String url = loadCommonInfo(CommonInfo.VENUE_BESPEAK);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("today", toJson(today));
            jsonObject.put("recommend", toJson(recomment));
            jsonObject.put("bespeak", url + "?Uid=" + ExpoApp.getApplication().getUser().getUid() + "&Ukey=" + ExpoApp.getApplication().getUser().getUkey()
                    + "&lan=" + LanguageUtil.chooseTest("zh", "en"));
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}
