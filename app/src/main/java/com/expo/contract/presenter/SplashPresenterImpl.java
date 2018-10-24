package com.expo.contract.presenter;

import android.text.TextUtils;

import com.expo.base.utils.FileUtils;
import com.expo.base.utils.PrefsHelper;
import com.expo.contract.SplashContract;
import com.expo.entity.ActualScene;
import com.expo.entity.CommonInfo;
import com.expo.entity.DataType;
import com.expo.entity.Encyclopedias;
import com.expo.entity.Subject;
import com.expo.entity.User;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.AllTypeResp;
import com.expo.network.response.BaseResponse;
import com.expo.network.response.CommonInfoResp;
import com.expo.network.response.EncyclopediasResp;
import com.expo.network.response.SpotsResp;
import com.expo.network.response.SubjectResp;
import com.expo.network.response.UpdateTimeResp;
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
            }

            @Override
            public void onComplete() {
                notifyLoadComplete();
            }
        }, observable);
        if (isRequest)
            loadCompleteCount.addAndGet(1);
    }

    /*
     * 加载攻略数据
     */
    private void loadEncyclopedias() {
        loadCompleteCount.addAndGet(1);
        Map<String, Object> params = Http.getBaseParams();
        params.put("Pageidx", 0);
        params.put("Count", 1000000);
        Observable<EncyclopediasResp> observable = Http.getServer().loadEncyclopedias(Http.buildRequestBody(params));
        Http.request(new ResponseCallback<EncyclopediasResp>() {
            @Override
            protected void onResponse(EncyclopediasResp rsp) {
                PrefsHelper.setString(Constants.Prefs.KEY_ENCYCLOPEDIAS_UPDATE_TIME, rsp.updateTime);
                mDao.clear(Encyclopedias.class);
                mDao.saveOrUpdateAll(rsp.encyclopedias);
            }

            @Override
            public void onComplete() {
                notifyLoadComplete();
            }
        }, observable);
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
        if (isRequest)
            loadCompleteCount.addAndGet(1);
    }


    /*
     * 加载主题数据
     */
    private void loadSubjects(RequestBody emptyBody) {
        loadCompleteCount.addAndGet(1);
        Observable<SubjectResp> observable = Http.getServer().loadSubjects(emptyBody);
        Http.request(new ResponseCallback<SubjectResp>() {
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
    }


    /*
     * 加载常用信息
     */
    private void loadCommonInfo(RequestBody emptyBody) {
        loadCompleteCount.addAndGet(1);
        Observable<CommonInfoResp> observable = Http.getServer().loadCommonInfos(emptyBody);
        Http.request(new ResponseCallback<CommonInfoResp>() {
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
    }

    /*
     * 请求景点景观相关内容数据
     */
    private void requestSpot(String dataUrl, RequestBody body, String updateKey, Class clz) {
        loadCompleteCount.addAndGet(1);
        Observable<SpotsResp> observable = Http.getServer().loadSpots(dataUrl, body);
        Http.request(new ResponseCallback<SpotsResp>() {
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
