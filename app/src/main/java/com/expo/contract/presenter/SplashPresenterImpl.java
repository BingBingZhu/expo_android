package com.expo.contract.presenter;

import android.text.TextUtils;

import com.expo.base.utils.FileUtils;
import com.expo.base.utils.PrefsHelper;
import com.expo.contract.SplashContract;
import com.expo.entity.ActualScene;
import com.expo.entity.CommonInfo;
import com.expo.entity.DataType;
import com.expo.entity.Encyclopedias;
import com.expo.entity.Park;
import com.expo.entity.RouteInfo;
import com.expo.entity.Subject;
import com.expo.entity.TopLineInfo;
import com.expo.entity.TouristType;
import com.expo.entity.User;
import com.expo.entity.VenuesInfo;
import com.expo.entity.VenuesType;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.AllTypeResp;
import com.expo.network.response.BaseResponse;
import com.expo.network.response.CommonInfoResp;
import com.expo.network.response.EncyclopediasResp;
import com.expo.network.response.ParkResp;
import com.expo.network.response.RouteInfoResp;
import com.expo.network.response.SpotsResp;
import com.expo.network.response.SubjectResp;
import com.expo.network.response.TopLineResp;
import com.expo.network.response.TouristTypeResp;
import com.expo.network.response.UpdateTimeResp;
import com.expo.network.response.VenuesInfoResp;
import com.expo.network.response.VenuesTypeResp;
import com.expo.utils.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
        loadAllTypes(emptyBody);
        copyAMapStyleToSDCard();
        loadRouteInfo(emptyBody);
        loadVenuesInfo();
        loadTopLine(emptyBody);
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
                if (!TextUtils.isEmpty(rsp.actualScene)) {
                    updateTime = PrefsHelper.getString(Constants.Prefs.KEY_ACTUAL_SCENE_UPDATE_TIME, null);
                    if (!rsp.actualScene.equals(updateTime)) {
                        requestSpot(Constants.URL.ACTUAL_SCENES, emptyBody, Constants.Prefs.KEY_ACTUAL_SCENE_UPDATE_TIME, ActualScene.class);
                    }
                }
                if (!TextUtils.isEmpty(rsp.subject)) {
                    updateTime = PrefsHelper.getString(Constants.Prefs.KEY_SUBJECT_UPDATE_TIME, null);
                    if (!rsp.subject.equals(updateTime)) {
                        loadSubjects(emptyBody);
                    }
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
                if (!TextUtils.isEmpty(rsp.scenicSpotType)) {
                    updateTime = PrefsHelper.getString(Constants.Prefs.KEY_SCENIC_SPOT_TYPE_UPDATE_TIME, null);
                    if (!rsp.scenicSpotType.equals(updateTime)) {
                        loadVenuesTypeList();
                    }
                }
                if (!TextUtils.isEmpty(rsp.parkList)) {
                    updateTime = PrefsHelper.getString(Constants.Prefs.KEY_PARK_UPDATE_TIME, null);
                    if (!rsp.parkList.equals(updateTime)) {
                        loadParksList();
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
                for (TouristType touristType : rsp.touristTypes) {
                    TouristType tt = mDao.queryById(TouristType.class, touristType.getId());
                    if (tt != null) {
                        touristType.setUsed(tt.isUsed());
                    }
                }
                mDao.clear(TouristType.class);
                mDao.saveOrUpdateAll(rsp.touristTypes);
                PrefsHelper.setString(Constants.Prefs.KEY_TOURIST_TYPE_UPDATE_TIME, rsp.updateTime);
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

    @Override
    public void appRun(String uId, String uKey) {
        Map<String, Object> params = Http.getBaseParams();
        params.put("Uid", uId);
        params.put("Ukey", uKey);
        Observable<BaseResponse> observable = Http.getServer().userlogAppRun(Http.buildRequestBody(params));
        Http.request(new ResponseCallback<BaseResponse>() {
            @Override
            protected void onResponse(BaseResponse rsp) {
            }

        }, observable);
    }

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
     * 加载主题数据
     */
    private void loadSubjects(RequestBody emptyBody) {
        Observable<SubjectResp> observable = Http.getServer().loadSubjects(emptyBody);
        isRequest = Http.request(new ResponseCallback<SubjectResp>() {
            @Override
            protected void onResponse(SubjectResp rsp) {
                PrefsHelper.setString(Constants.Prefs.KEY_SUBJECT_UPDATE_TIME, rsp.updateTime);
                mDao.clear(Subject.class);
                mDao.saveOrUpdateAll(rsp.subjects);
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
        Observable<SpotsResp> observable = Http.getServer().loadSpots(dataUrl, body);
        isRequest = Http.request(new ResponseCallback<SpotsResp>() {
            @Override
            protected void onResponse(SpotsResp rsp) {
                PrefsHelper.setString(updateKey, rsp.updateTime);
                mDao.clear(clz);
                if (clz == ActualScene.class) {
                    mDao.saveOrUpdateAll(rsp.actualScenes);
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

    /*
     * 获取场馆（设施）列表
     */
    private void loadVenuesInfo() {
        Map<String, Object> params = Http.getBaseParams();
        params.put("ParkId", "1");
        Observable<VenuesInfoResp> observable = Http.getServer().getVenuesList(Http.buildRequestBody(params));
        isRequest = Http.request(new ResponseCallback<VenuesInfoResp>() {
            @Override
            protected void onResponse(VenuesInfoResp rsp) {
                mDao.clear(VenuesInfo.class);
                mDao.saveOrUpdateAll(rsp.venuesList);
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
                mDao.clear(TopLineInfo.class);
                mDao.saveOrUpdateAll(rsp.topLine);
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
