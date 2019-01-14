package com.expo.contract.presenter;

import android.content.Context;

import com.blankj.utilcode.util.AppUtils;
import com.expo.upapp.UpdateAppManager;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.expo.contract.HomeContract;
import com.expo.db.QueryParams;
import com.expo.entity.AppInfo;
import com.expo.entity.CommonInfo;
import com.expo.entity.Encyclopedias;
import com.expo.entity.Message;
import com.expo.entity.TopLineInfo;
import com.expo.entity.Venue;
import com.expo.module.heart.HeartBeatService;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.BaseResponse;
import com.expo.network.response.VersionInfoResp;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

import java.util.Collections;
import java.util.List;

public class HomePresenterImpl extends HomeContract.Presenter {
    public HomePresenterImpl(HomeContract.View view) {
        super( view );
    }

    @Override
    public void setMessageCount() {
        new Message().sendMessageCount( null );
    }

    @Override
    public void setTopLine() {
        mView.showTopLine( mDao.query( TopLineInfo.class, null ) );
    }

    @Override
    public void setVenue() {
        QueryParams params = new QueryParams()
                .add( "eq", "enable", 1 )
                .add( "and" )
                .add( "eq", "recommend", 1 )
                .add( "and" )
                .add( "eq", "type_name", "场馆" )
                .add( "orderBy", "idx", true );
        mView.showVenue( mDao.query( Encyclopedias.class, params ) );
    }

    @Override
    public void setExhibit() {
        //从数据库获取
        QueryParams params = new QueryParams()
                .add( "eq", "enable", 1 )
                .add( "and" )
                .add( "eq", "recommend", 1 )
                .add( "and" )
                .add( "eq", "type_name", "展览" )
                .add( "orderBy", "idx", true );
        mView.showExhibit( mDao.query( Encyclopedias.class, params ) );
    }

    @Override
    public void setExhibitGarden() {
        //从数据库获取
        QueryParams params = new QueryParams()
                .add( "eq", "enable", 1 )
                .add( "and" )
                .add( "eq", "recommend", 1 )
                .add( "and" )
                .add( "eq", "type_name", "展园" )
                .add( "orderBy", "idx", true );
        mView.showExhibitGarden( mDao.query( Encyclopedias.class, params ) );
    }

    public void startHeartService(Context context) {
        HeartBeatService.startService( context );
    }

    @Override
    public void stopHeartService(Context context) {
        HeartBeatService.stopService( context );
    }

    @Override
    public String loadCommonInfo(String type) {
        CommonInfo info = mDao.unique( CommonInfo.class, new QueryParams().add( "eq", "type", type ) );
        if (info != null) {
            return info.getLinkUrl();
        }
        return null;
    }

    @Override
    public void appRun(String jgId) {
        Map<String, Object> params = Http.getBaseParams();
        params.put( "jgid", jgId);
        Observable<BaseResponse> observable = Http.getServer().userlogAppRun( Http.buildRequestBody( params ) );
        Http.request( new ResponseCallback<BaseResponse>() {
            @Override
            protected void onResponse(BaseResponse rsp) {
            }

        }, observable );
    }

    @Override
    public void checkUpdate() {
        Map<String, Object> params = Http.getBaseParams();
        RequestBody requestBody = Http.buildRequestBody( params );
        Observable<VersionInfoResp> observable = Http.getServer().getAppUpdateInfo( requestBody );
        Http.request( new ResponseCallback<VersionInfoResp>() {
            @Override
            protected void onResponse(VersionInfoResp rsp) {
                AppInfo appInfo = UpdateAppManager.getInstance().isHaveUpdate(AppUtils.getAppVersionName(), rsp.Objlst);
                if (null != appInfo) {
                    mView.appUpdate( appInfo );
                }
            }
        }, observable );
    }

    @Override
    public void update(Context context, AppInfo appInfo) {
        UpdateAppManager.getInstance( context ).showNoticeDialog( appInfo, false );
    }

    @Override
    public Float getDistance(long id, LatLng latLng) {
        QueryParams queryParams = new QueryParams()
                .add("eq", "wiki_id", id);
        List<Venue> venues = mDao.query(Venue.class, queryParams);
        if (venues == null || venues.size() == 0) return 999999F;
        Venue venue = venues.get(0);
        Float distance = AMapUtils.calculateLineDistance(latLng, new LatLng(venue.getLat(), venue.getLng()));
        return distance;
    }

    @Override
    public void sortVenue(List<Encyclopedias> list) {
        Collections.sort(list, (o1, o2) -> o1.getDistance().compareTo(o2.getDistance()));
    }
}
