package com.expo.contract.presenter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.expo.R;
import com.expo.base.ExpoApp;
import com.expo.contract.WebTemplateContract;
import com.expo.db.QueryParams;
import com.expo.entity.CommonInfo;
import com.expo.entity.Message;
import com.expo.entity.Venue;
import com.expo.entity.Encyclopedias;
import com.expo.module.heart.MessageKindActivity;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.BaseResponse;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.expo.utils.LocalBroadcastUtil;
import com.expo.utils.NotificationUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.reactivex.Observable;
import okhttp3.RequestBody;

import static android.content.Context.NOTIFICATION_SERVICE;

public class WebTemplatePresenterImpl extends WebTemplateContract.Presenter {

    public WebTemplatePresenterImpl(WebTemplateContract.View view) {
        super(view);
    }

    @Override
    public Encyclopedias loadEncyclopediaById(long id) {
        return mDao.queryById(Encyclopedias.class, id);
    }

    @Override
    public Venue loadSceneByWikiId(long id) {
        return mDao.unique(Venue.class, new QueryParams()
                .add("eq", "wiki_id", String.valueOf(id)));
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
                NotificationUtil.getInstance().show("新增积分通知", "获取到新的积分，可在积分详情查看", null);
            }
        }, observable);
    }

    @Override
    public List<Encyclopedias> loadNeayByVenues(Venue as) {
        List<Venue> venues = mDao.query( Venue.class, new QueryParams()
                .add( "notNull", "wiki_id" )
                .add( "and" )
                .add( "ne", "wiki_id", "" ) );
        List<String> ids = new ArrayList<>();
        Map<Float, Venue> venueMap = new HashMap<>();
        LatLng latLng = new LatLng( as.getLat(), as.getLng() );
        for (Venue venue : venues) {
            if (venue.getId().equals( as.getId() ))
                continue;
            Float distance = AMapUtils.calculateLineDistance( latLng, new LatLng( venue.getLat(), venue.getLng() ) );
            venueMap.put( distance, venue );
        }
        if (!venueMap.isEmpty()) {
            Map<String, Float> distances = new HashMap<>();
            if (venueMap.size() > 3) {
                List<Float> sortKeys = new ArrayList<>( venueMap.keySet() );
                Collections.sort( sortKeys );
                int i = 0;
                do {
                    String wikiId = venueMap.get( sortKeys.get( i ) ).getWikiId();
                    i++;
                    if (!TextUtils.isEmpty( wikiId )) {
                        ids.add( wikiId );
                        distances.put( wikiId, sortKeys.get( i ) );
                    }
                } while (i <= venueMap.size() && ids.size() < 3);
            } else {
                for (Venue venue : venueMap.values()) {
                    ids.add( venue.getWikiId() );
                }
            }
            List<Encyclopedias> encyclopedias = mDao.query( Encyclopedias.class, new QueryParams().add( "in", "_id", ids ) );
            for (Encyclopedias e : encyclopedias) {
                e.setDistance( distances.get( String.valueOf( e.getId() ) ) );
            }
            return encyclopedias;
        }
        return null;
    }

    @Override
    public List<Encyclopedias> loadRandomData(Long typeId, Long currId) {
        List<Encyclopedias> encyclopedias = mDao.query( Encyclopedias.class, new QueryParams()
                .add( "eq", "type_id", typeId )
                .add( "and" )
                .add( "ne", "_id", currId ) );
        List<Encyclopedias> result = new ArrayList<>();
        if (encyclopedias != null) {
            if (encyclopedias.size() > 3) {
                Random random = new Random();
                for (int i = 0; i < 3; i++) {
                    result.add( encyclopedias.remove( (int) (random.nextFloat() * encyclopedias.size()) ) );
                }
                return result;
            } else {
                return encyclopedias;
            }
        }
        return null;
    }

    @Override
    public String loadCommonInfo(String type) {
        CommonInfo info = mDao.unique( CommonInfo.class, new QueryParams()
                .add( "eq", "type", type ) );
        if (info != null)
            return info.getLinkUrl();
        return null;
    }
}
