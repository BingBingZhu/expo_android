package com.expo.contract.presenter;

import android.content.Context;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.StringUtils;
import com.expo.R;
import com.expo.base.ExpoApp;
import com.expo.base.utils.ToastHelper;
import com.expo.base.utils.UpdateAppManager;
import com.expo.contract.HomeContract;
import com.expo.db.QueryParams;
import com.expo.entity.AppInfo;
import com.expo.entity.CommonInfo;
import com.expo.entity.Encyclopedias;
import com.expo.entity.Message;
import com.expo.entity.TopLineInfo;
import com.expo.module.heart.HeartBeatService;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.BaseResponse;
import com.expo.network.response.VersionInfoResp;

import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observable;
import okhttp3.RequestBody;

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
                if (StringUtils.equals( AppUtils.getAppVersionCode() + "", rsp.ver )) {
                    ToastHelper.showShort( R.string.latest_app_version );
                } else {
                    for (int i = 0; i < rsp.Objlst.size(); i++) {
                        if (StringUtils.equals( "android", rsp.Objlst.get( i ).platformname.toLowerCase() )) {
                            mView.appUpdate( rsp.Objlst.get( i ) );
                            return;
                        }
                    }
                }
            }
        }, observable );
    }

    @Override
    public void update(Context context, AppInfo appInfo) {
        UpdateAppManager.getInstance( context ).showNoticeDialog( appInfo, false );
    }
}
