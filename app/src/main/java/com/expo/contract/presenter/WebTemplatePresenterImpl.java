package com.expo.contract.presenter;

import com.expo.base.utils.PrefsHelper;
import com.expo.contract.WebTemplateContract;
import com.expo.entity.ActualScene;
import com.expo.entity.Encyclopedias;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.SpotsResp;
import com.expo.utils.Constants;

import io.reactivex.Observable;
import okhttp3.RequestBody;

public class WebTemplatePresenterImpl extends WebTemplateContract.Presenter {

    private int dataType = -1;
    public final static int ENCYCLOPEDIA = 0;    // 百科
    public final static int ACTUAL_SCENE = 1;    // 景区

    public WebTemplatePresenterImpl(WebTemplateContract.View view) {
        super(view);
    }

    @Override
    public void setDataType(int intExtra) {
        this.dataType = intExtra;
    }

    @Override
    public String getDataJsonById(int id) {
        String jsonData = "";
        switch (dataType){
            case ENCYCLOPEDIA:
                jsonData = Http.getGsonInstance().toJson(mDao.queryById(Encyclopedias.class, id)) ;
                break;
            case ACTUAL_SCENE:
                jsonData = Http.getGsonInstance().toJson(mDao.queryById(ActualScene.class, id)) ;
                break;
        }
        return jsonData;
//        mView.getDataJsonByIdRes(jsonData);
    }

    @Override
    public void getDataById(long id) {
        switch (dataType){
            case ACTUAL_SCENE:
                ActualScene as = mDao.queryById(ActualScene.class, id);
                if (null == as){
                    requestSpot(Constants.URL.ACTUAL_SCENES, Http.buildRequestBody(Http.getBaseParams()),
                            Constants.Prefs.KEY_ACTUAL_SCENE_UPDATE_TIME, id);
                    return;
                }
                mView.getActualSceneDataByIdRes(as);
                break;
            default:

                break;
        }
    }

    /*
     * 请求景点景观相关内容数据
     */
    private void requestSpot(String dataUrl, RequestBody body, String updateKey, long id) {
        Observable<SpotsResp> observable = Http.getServer().loadSpots(dataUrl, body);
        Http.request(new ResponseCallback<SpotsResp>() {
            @Override
            protected void onResponse(SpotsResp rsp) {
                PrefsHelper.setString(updateKey, rsp.updateTime);
                mDao.clear(ActualScene.class);
                mDao.saveOrUpdateAll(rsp.actualScenes);
                ActualScene as = mDao.queryById(ActualScene.class, id);
                if (null != as)
                    mView.getActualSceneDataByIdRes(as);
            }
        }, observable);
    }
}
