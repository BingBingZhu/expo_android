package com.expo.contract.presenter;

import com.expo.contract.WebTemplateContract;
import com.expo.db.QueryParams;
import com.expo.entity.Venue;
import com.expo.entity.Encyclopedias;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.BaseResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

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
    public List<Encyclopedias> loadNeayByVenues(Venue as) {
        List<Venue> venues = mDao.query(Venue.class, null);
        List<Long> ids = new ArrayList<>();
        for (Venue venue : venues) {

        }
        return null;
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
            }

        }, observable);
    }
}
