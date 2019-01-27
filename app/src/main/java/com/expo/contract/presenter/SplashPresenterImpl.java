package com.expo.contract.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.expo.base.utils.FileUtils;
import com.expo.base.utils.PrefsHelper;
import com.expo.contract.SplashContract;
import com.expo.entity.Badge;
import com.expo.entity.CommonInfo;
import com.expo.entity.DataType;
import com.expo.entity.Encyclopedias;
import com.expo.entity.ExpoActivityInfo;
import com.expo.entity.Park;
import com.expo.entity.RouteHotInfo;
import com.expo.entity.RouteInfo;
import com.expo.entity.TopLineInfo;
import com.expo.entity.TouristType;
import com.expo.entity.User;
import com.expo.entity.Venue;
import com.expo.entity.VenuesType;
import com.expo.entity.VrInfo;
import com.expo.entity.VrLableInfo;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.AllTypeResp;
import com.expo.network.response.BadgeResp;
import com.expo.network.response.CommonInfoResp;
import com.expo.network.response.EncyclopediasResp;
import com.expo.network.response.ExpoActivityInfoResp;
import com.expo.network.response.ParkResp;
import com.expo.network.response.RouteHotCountResp;
import com.expo.network.response.RouteInfoResp;
import com.expo.network.response.TopLineResp;
import com.expo.network.response.TouristTypeResp;
import com.expo.network.response.UpdateTimeResp;
import com.expo.network.response.VenueResp;
import com.expo.network.response.VenuesTypeResp;
import com.expo.network.response.VrInfoResp;
import com.expo.network.response.VrLableInfoResp;
import com.expo.utils.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import okhttp3.RequestBody;

public class SplashPresenterImpl extends SplashContract.Presenter {

    private AtomicInteger loadCompleteCount;
    private boolean isRequest;

    public SplashPresenterImpl(SplashContract.View view) {
        super(view);
    }

    @Override
    public void loadInitData() {
        loadCompleteCount = new AtomicInteger();
        RequestBody emptyBody = Http.buildRequestBody(Http.getBaseParams());
        checkUpdateDate(emptyBody);
        copyAMapStyleToSDCard();
        getRouterHotCountList();
    }

    private void checkUpdateDate(RequestBody emptyBody) {
        Observable<UpdateTimeResp> observable = Http.getServer().checkUpdateTime(emptyBody);
        isRequest = Http.request(new ResponseCallback<UpdateTimeResp>() {
            @Override
            protected void onResponse(UpdateTimeResp rsp) {
                String updateTime;
                if (!TextUtils.isEmpty(rsp.commoninformation)) {
                    updateTime = PrefsHelper.getString(Constants.Prefs.KEY_COMMON_INFO_UPDATE_TIME, null);
                    if (!rsp.commoninformation.equals(updateTime)) {
                        loadCommonInfo(emptyBody);
                    }
                }
                if (!TextUtils.isEmpty(rsp.topLine)) {
                    updateTime = PrefsHelper.getString(Constants.Prefs.KEY_TOP_LINE_UPDATE_TIME, null);
                    if (!rsp.topLine.equals(updateTime)) {
                        loadTopLine(emptyBody);
                    }
                }
                if (!TextUtils.isEmpty(rsp.allType)) {
                    updateTime = PrefsHelper.getString(Constants.Prefs.KEY_ALL_TYPE_UPDATE_TIME, null);
                    if (!rsp.allType.equals(updateTime)) {
                        loadAllTypes(emptyBody);
                    }
                }
                if (!TextUtils.isEmpty(rsp.venue)) {
                    updateTime = PrefsHelper.getString(Constants.Prefs.KEY_ACTUAL_SCENE_UPDATE_TIME, null);
                    if (!rsp.venue.equals(updateTime)) {
                        requestSpot(Constants.URL.ACTUAL_SCENES, emptyBody, Constants.Prefs.KEY_ACTUAL_SCENE_UPDATE_TIME, Venue.class);
                    }
                }
                if (!TextUtils.isEmpty(rsp.router)) {
                    updateTime = PrefsHelper.getString(Constants.Prefs.KEY_ROUTES_UPDATE_TIME, null);
                    if (!rsp.router.equals(updateTime)) {
                        loadRouteInfo(emptyBody);
                    }
                }
                if (!TextUtils.isEmpty(rsp.heartInvTime) && rsp.heartInvTime.matches(Constants.Exps.NUMBER)) {
                    PrefsHelper.setLong(Constants.Prefs.KEY_HEART_INV_TIME, Long.parseLong(rsp.heartInvTime));
                }
                if (!TextUtils.isEmpty(rsp.updateTimeInvTime) && rsp.updateTimeInvTime.matches(Constants.Exps.NUMBER)) {
                    PrefsHelper.setLong(Constants.Prefs.KEY_UPDATE_TIME_INV_TIME, Long.parseLong(rsp.updateTimeInvTime));
                }
                if (!TextUtils.isEmpty(rsp.wiki)) {
                    updateTime = PrefsHelper.getString(Constants.Prefs.KEY_ENCYCLOPEDIAS_UPDATE_TIME, null);
                    if (!rsp.wiki.equals(updateTime)) {
                        loadEncyclopedias();
                    }
                }
                if (!TextUtils.isEmpty(rsp.touristType)) {
                    updateTime = PrefsHelper.getString(Constants.Prefs.KEY_TOURIST_TYPE_UPDATE_TIME, null);
                    if (!rsp.touristType.equals(updateTime)) {
                        loadTouristTypeList();
                    }
                }
                if (!TextUtils.isEmpty(rsp.venueType)) {
                    updateTime = PrefsHelper.getString(Constants.Prefs.KEY_SCENIC_SPOT_TYPE_UPDATE_TIME, null);
                    if (!rsp.venueType.equals(updateTime)) {
                        loadVenuesTypeList();
                    }
                }
                if (!TextUtils.isEmpty(rsp.parkList)) {
                    updateTime = PrefsHelper.getString(Constants.Prefs.KEY_PARK_UPDATE_TIME, null);
                    if (!rsp.parkList.equals(updateTime)) {
                        loadParksList();
                    }
                }
                if (!TextUtils.isEmpty(rsp.badge)) {
                    updateTime = PrefsHelper.getString(Constants.Prefs.KEY_BADGE_UPDATE_TIME, null);
                    if (!rsp.badge.equals(updateTime)) {
                        loadBadgeInfo();
                    }
                }
                if (!TextUtils.isEmpty( rsp.panorama )) {
                    updateTime = PrefsHelper.getString( Constants.Prefs.KEY_VR_INFO_UPDATE_TIME, null );
                    if (!rsp.panorama.equals( updateTime )) {
                        loadVrInfo();
                    }
                }
                if (!TextUtils.isEmpty( rsp.vrLableInfo )) {
                    updateTime = PrefsHelper.getString( Constants.Prefs.KEY_VR_LABLE_INFO_UPDATE_TIME, null );
                    if (!rsp.vrLableInfo.equals( updateTime )) {
                        loadVrLableInfo();
                    }
                }
                if (!TextUtils.isEmpty(rsp.timeShowTimes)) {
                    updateTime = PrefsHelper.getString(Constants.Prefs.KEY_EXPO_ACTIVITY_UPDATE_TIME, null);
                    if (!rsp.timeShowTimes.equals(updateTime)) {
                        loadExpoActivityInfo();
                    }
                }
            }

            @Override
            public void onComplete() {
                notifyLoadComplete();
            }
        }, observable);
        addNetworkRecord();
    }

    /*
     * 加载攻略数据
     */
    private void loadEncyclopedias() {
        Map<String, Object> params = Http.getBaseParams();
        params.put("Pageidx", 0);
        params.put("Count", 1000000);
        Observable<EncyclopediasResp> observable = Http.getServer().loadEncyclopedias(Http.buildRequestBody(params));
        isRequest = Http.request(new ResponseCallback<EncyclopediasResp>() {
            @Override
            protected void onResponse(EncyclopediasResp rsp) {
                PrefsHelper.setString(Constants.Prefs.KEY_ENCYCLOPEDIAS_UPDATE_TIME, rsp.updateTime);
                mDao.clear(Encyclopedias.class);
                mDao.saveOrUpdateAll(rsp.encyclopedias);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onComplete() {
                notifyLoadComplete();
            }
        }, observable);
        addNetworkRecord();
    }

    /**
     * 加载导游类型列表
     */
    private void loadTouristTypeList() {
        Map<String, Object> params = Http.getBaseParams();
        params.put("ParkID", 1);
        Observable<TouristTypeResp> observable = Http.getServer().loadTouristTypeList(Http.buildRequestBody(params));
        isRequest = Http.request(new ResponseCallback<TouristTypeResp>() {
            @Override
            protected void onResponse(TouristTypeResp rsp) {
                PrefsHelper.setString(Constants.Prefs.KEY_TOURIST_TYPE_UPDATE_TIME, rsp.updateTime);
                mDao.clear(TouristType.class);
                mDao.saveOrUpdateAll(rsp.touristTypes);
            }

            @Override
            public void onComplete() {
                notifyLoadComplete();
            }
        }, observable);
        addNetworkRecord();
    }

    /**
     * 加载徽章列表
     */
    private void loadBadgeInfo() {
        Observable<BadgeResp> observable = Http.getServer().getBadgeList(Http.buildRequestBody(Http.getBaseParams()));
        isRequest = Http.request(new ResponseCallback<BadgeResp>() {
            @Override
            protected void onResponse(BadgeResp rsp) {
                PrefsHelper.setString(Constants.Prefs.KEY_BADGE_UPDATE_TIME, rsp.updatetime);
                mDao.clear(Badge.class);
                List<Badge> badges = rsp.badges;
                Collections.sort(badges);
                mDao.saveOrUpdateAll(badges);
            }

            @Override
            public void onComplete() {
                notifyLoadComplete();
            }
        }, observable);
        addNetworkRecord();
    }

    /**
     * 获取场馆（设施）类型列表
     */
    private void loadVenuesTypeList() {
        Observable<VenuesTypeResp> observable = Http.getServer().loadVenuesTypeList(Http.buildRequestBody(Http.getBaseParams()));
        isRequest = Http.request(new ResponseCallback<VenuesTypeResp>() {
            @Override
            protected void onResponse(VenuesTypeResp rsp) {
                PrefsHelper.setString(Constants.Prefs.KEY_SCENIC_SPOT_TYPE_UPDATE_TIME, rsp.updateTime);
                mDao.clear(VenuesType.class);
                mDao.saveOrUpdateAll(rsp.venuesList);
            }

            @Override
            public void onComplete() {
                notifyLoadComplete();
            }
        }, observable);
        addNetworkRecord();
    }

    /**
     * 获取公园列表
     */
    private void loadParksList() {
        Observable<ParkResp> observable = Http.getServer().loadParksList(Http.buildRequestBody(Http.getBaseParams()));
        isRequest = Http.request(new ResponseCallback<ParkResp>() {
            @Override
            protected void onResponse(ParkResp rsp) {
                PrefsHelper.setString(Constants.Prefs.KEY_PARK_UPDATE_TIME, rsp.updateTime);
                mDao.clear(Park.class);
                mDao.saveOrUpdateAll(rsp.parkList);
            }

            @Override
            public void onComplete() {
                notifyLoadComplete();
            }
        }, observable);
        addNetworkRecord();
    }

    @Override
    public User loadUser() {
        return mDao.unique(User.class, null);
    }

//    @Override
//    public void appRun(String uId, String uKey) {
//        Map<String, Object> params = Http.getBaseParams();
//        params.put("jgid", JPushInterface.getRegistrationID(ExpoApp.getApplication()));
//        Observable<BaseResponse> observable = Http.getServer().userlogAppRun( Http.buildRequestBody( params ) );
//        Http.request( new ResponseCallback<BaseResponse>() {
//            @Override
//            protected void onResponse(BaseResponse rsp) {
//            }
//
//        }, observable );
//    }

    /*
     * 复制地图样式文件到手机储存中
     */
    public void copyAMapStyleToSDCard() {
        loadCompleteCount.addAndGet(1);
        new Thread() {
            @Override
            public void run() {
                String styleName = "style.data";
                String filePath = mView.getContext().getFilesDir().getAbsolutePath();
                File file = new File(filePath + File.separator + styleName);
                if (file.exists()) {
                    notifyLoadComplete();
                    return;
                }
                try {
                    if (file.createNewFile()) {
                        InputStream inputStream = mView.getContext().getAssets().open("map/" + styleName);
                        FileOutputStream outputStream = new FileOutputStream(file);
                        FileUtils.copy(inputStream, outputStream);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    notifyLoadComplete();
                }
            }
        }.start();
    }

    /*
     * 加载所有分类类型数据
     */
    private void loadAllTypes(RequestBody body) {
        Observable<AllTypeResp> observable = Http.getServer().loadAllTypes(body);
        isRequest = Http.request(new ResponseCallback<AllTypeResp>() {
            @Override
            protected void onResponse(AllTypeResp rsp) {
                PrefsHelper.setString(Constants.Prefs.KEY_ALL_TYPE_UPDATE_TIME, rsp.updateTime);
                mDao.clear(DataType.class);
                if (rsp.messageTypes != null && rsp.messageTypes.size() > 0) {
                    for (DataType type : rsp.messageTypes) {
                        type.setKind(1);
                        mDao.saveOrUpdate(type);
                    }
                }
                if (rsp.venueTypes != null && rsp.venueTypes.size() > 0) {
                    for (DataType type : rsp.venueTypes) {
                        type.setKind(2);
                        mDao.saveOrUpdate(type);
                    }
                }
                if (rsp.wikiTypes != null && rsp.wikiTypes.size() > 0) {
                    for (DataType type : rsp.wikiTypes) {
                        type.setKind(3);
                        mDao.saveOrUpdate(type);
                    }
                }
                if (rsp.feedbackTypes != null && rsp.feedbackTypes.size() > 0) {
                    for (DataType type : rsp.feedbackTypes) {
                        type.setKind(5);
                        mDao.saveOrUpdate(type);
                    }
                }
            }

            @Override
            public void onComplete() {
                notifyLoadComplete();
            }
        }, observable);
        addNetworkRecord();
    }

    /*
     * 加载常用信息
     */
    private void loadCommonInfo(RequestBody emptyBody) {
        Observable<CommonInfoResp> observable = Http.getServer().loadCommonInfos(emptyBody);
        isRequest = Http.request(new ResponseCallback<CommonInfoResp>() {
            @Override
            protected void onResponse(CommonInfoResp rsp) {
                PrefsHelper.setString(Constants.Prefs.KEY_COMMON_INFO_UPDATE_TIME, rsp.updateTime);
                mDao.clear(CommonInfo.class);
                mDao.saveOrUpdateAll(rsp.commonInfos);
            }

            @Override
            public void onComplete() {
                notifyLoadComplete();
            }
        }, observable);
        addNetworkRecord();
    }

    /*
     * 请求景点景观相关内容数据
     */
    private void requestSpot(String dataUrl, RequestBody body, String updateKey, Class clz) {
        Observable<VenueResp> observable = Http.getServer().loadSpots(dataUrl, body);
        isRequest = Http.request(new ResponseCallback<VenueResp>() {
            @Override
            protected void onResponse(VenueResp rsp) {
                PrefsHelper.setString(updateKey, rsp.updateTime);
                mDao.clear(clz);
                if (clz == Venue.class) {
                    mDao.saveOrUpdateAll(rsp.venues);
                }
            }

            @Override
            public void onComplete() {
                notifyLoadComplete();
            }
        }, observable);
        addNetworkRecord();
    }

    /*
     * 路线列表
     */
    private void loadRouteInfo(RequestBody emptyBody) {
        Observable<RouteInfoResp> observable = Http.getServer().loadRouteInfo(emptyBody);
        isRequest = Http.request(new ResponseCallback<RouteInfoResp>() {
            @Override
            protected void onResponse(RouteInfoResp rsp) {
                PrefsHelper.setString(Constants.Prefs.KEY_ROUTES_UPDATE_TIME, rsp.updateTime);
                mDao.clear(RouteInfo.class);
                mDao.saveOrUpdateAll(rsp.routeList);
            }

            @Override
            public void onComplete() {
                notifyLoadComplete();
            }
        }, observable);
        addNetworkRecord();
    }

    /**
     * 获取路线热度列表
     */
    private void getRouterHotCountList() {
        Map<String, Object> params = Http.getBaseParams();
        params.put("parkid", "1");
        RequestBody requestBody = Http.buildRequestBody(params);
        Observable<RouteHotCountResp> observable = Http.getServer().getRouterHotCountList(requestBody);
        isRequest = Http.request(new ResponseCallback<RouteHotCountResp>() {
            @Override
            protected void onResponse(RouteHotCountResp rsp) {
                if (rsp == null || rsp.routeHots == null || rsp.routeHots.size() == 0) return;
                for (int i = 0; i < rsp.routeHots.size(); i++) {
                    RouteHotInfo hotInfo = rsp.routeHots.get(i);
                    RouteInfo routeInfo = mDao.queryById(RouteInfo.class, hotInfo.element);
                    if (routeInfo != null) {
                        routeInfo.hotCount = hotInfo.score;
                        mDao.saveOrUpdate(routeInfo);
                    }
                }
            }

            @Override
            public void onComplete() {
                notifyLoadComplete();
            }
        }, observable);
        addNetworkRecord();
    }

    /*
     * 获取头条列表
     */
    private void loadTopLine(RequestBody emptyBody) {
        Observable<TopLineResp> observable = Http.getServer().getTopLineList(emptyBody);
        isRequest = Http.request(new ResponseCallback<TopLineResp>() {
            @Override
            protected void onResponse(TopLineResp rsp) {
                PrefsHelper.setString(Constants.Prefs.KEY_TOP_LINE_UPDATE_TIME, rsp.updateTime);
                mDao.clear(TopLineInfo.class);
                mDao.saveOrUpdateAll(rsp.topLine);
            }

            @Override
            public void onComplete() {
                notifyLoadComplete();
            }
        }, observable );
        addNetworkRecord();
    }

    /**
     * 加载全景资源
     */
    private void loadVrInfo() {
        Observable<VrInfoResp> observable = Http.getServer().getPanCamList( Http.buildRequestBody( Http.getBaseParams() ) );
        isRequest = Http.request( new ResponseCallback<VrInfoResp>() {
            @Override
            protected void onResponse(VrInfoResp rsp) {
                Log.i("-------------VrInfoResp", "-----------");
                PrefsHelper.setString( Constants.Prefs.KEY_VR_INFO_UPDATE_TIME, rsp.updateTime );
                mDao.clear( VrInfo.class );
                List<VrInfo> vrInfos = rsp.vrInfos;
                mDao.saveOrUpdateAll( vrInfos );
            }

            @Override
            public void onComplete() {
                notifyLoadComplete();
            }
        }, observable );
        addNetworkRecord();
    }

    /**
     * 加载全景标签资源
     */
    private void loadVrLableInfo() {
        Observable<VrLableInfoResp> observable = Http.getServer().getPanLableList( Http.buildRequestBody( Http.getBaseParams() ) );
        isRequest = Http.request( new ResponseCallback<VrLableInfoResp>() {
            @Override
            protected void onResponse(VrLableInfoResp rsp) {
                PrefsHelper.setString( Constants.Prefs.KEY_VR_LABLE_INFO_UPDATE_TIME, rsp.updateTime );
                mDao.clear( VrLableInfo.class );
                List<VrLableInfo> vrLableInfos = rsp.vrLableInfos;
                mDao.saveOrUpdateAll( vrLableInfos );
            }

            @Override
            public void onComplete() {
                notifyLoadComplete();
            }
        }, observable );
        addNetworkRecord();
    }

    /**
     * 加载世园活动资源
     */
    private void loadExpoActivityInfo() {
        Observable<ExpoActivityInfoResp> observable = Http.getServer().getShowTimesList_Rsb(Http.buildRequestBody(Http.getBaseParams()));
        isRequest = Http.request(new ResponseCallback<ExpoActivityInfoResp>() {
            @Override
            protected void onResponse(ExpoActivityInfoResp rsp) {
                PrefsHelper.setString(Constants.Prefs.KEY_EXPO_ACTIVITY_UPDATE_TIME, rsp.Updatetime);
                mDao.clear(ExpoActivityInfo.class);
                List<ExpoActivityInfo> expoActivityInfos = rsp.expoActivityInfos;
                mDao.saveOrUpdateAll(expoActivityInfos);
            }

            @Override
            public void onComplete() {
                notifyLoadComplete();
            }
        }, observable);
        addNetworkRecord();
    }

    private void addNetworkRecord() {
        if (isRequest)
            loadCompleteCount.addAndGet(1);
    }

    /*
     * 通知一项加载完成,跳转下一界面
     */
    private void notifyLoadComplete() {
        int value = loadCompleteCount.addAndGet(-1);
        if (value == 0) {
            mView.next();
        }
    }
}
