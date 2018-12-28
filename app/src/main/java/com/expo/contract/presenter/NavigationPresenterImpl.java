package com.expo.contract.presenter;

import com.expo.contract.NavigationContract;
import com.expo.db.QueryParams;
import com.expo.entity.Encyclopedias;
import com.expo.entity.TouristType;
import com.expo.entity.Venue;
import com.expo.entity.VenuesDistance;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.BaseResponse;
import com.expo.utils.LanguageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

public class NavigationPresenterImpl extends NavigationContract.Presenter {
    public NavigationPresenterImpl(NavigationContract.View view) {
        super(view);
    }

    @Override
    public Venue loadSceneById(long spotId) {
        return mDao.queryById(Venue.class, spotId);
    }

    @Override
    public List<VenuesDistance> getVenues() {
        List<Venue> venuesInfos = mDao.query(Venue.class, new QueryParams()
                .add("notNull", "wiki_id")
                .add("and")
                .add("eq", "is_enable", 1));

        List<VenuesDistance> list = new ArrayList<>();
        for (Venue v : venuesInfos) {
            VenuesDistance vd = new VenuesDistance();
            vd.id = v.getWikiId();
            try {
                vd.lat = v.getLat();
                vd.lon = v.getLng();
            } catch (Exception e) {

            }
            vd.title = LanguageUtil.chooseTest(v.getCaption(), v.getEnCaption());
            vd.content = LanguageUtil.chooseTest(v.getRemark(), v.getEnRemark());
            vd.voice = LanguageUtil.chooseTest(v.getVoiceUrl(), v.getVoiceUrlEn());
            list.add(vd);
        }
        return list;
    }

    @Override
    public Encyclopedias getEncyclopedias(String id) {
        return mDao.queryById(Encyclopedias.class, id);
    }

    @Override
    public TouristType getTourist() {
        QueryParams params = new QueryParams();
        params.add("eq", "used", true);
        return mDao.unique(TouristType.class, params);
    }

    @Override
    public void submitTouristRecord(double sx, double xy, double ex, double ey, float speed) {
        Map<String, Object> params = Http.getBaseParams();
        params.put("startpx", sx);
        params.put("startpy", xy);
        params.put("beginx", ex);
        params.put("beginy", ey);
        params.put("speed", speed);
        RequestBody requestBody = Http.buildRequestBody(params);
        Observable<BaseResponse> verifyCodeLoginObservable = Http.getServer().submitTouristRecord(requestBody);
        Http.request(new ResponseCallback<BaseResponse>() {
            @Override
            protected void onResponse(BaseResponse rsp) {
            }

        }, verifyCodeLoginObservable);
    }

}
