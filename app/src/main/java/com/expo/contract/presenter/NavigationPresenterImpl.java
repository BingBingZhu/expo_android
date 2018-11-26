package com.expo.contract.presenter;

import com.expo.base.utils.PrefsHelper;
import com.expo.contract.NavigationContract;
import com.expo.db.QueryParams;
import com.expo.entity.ActualScene;
import com.expo.entity.Encyclopedias;
import com.expo.entity.RouteInfo;
import com.expo.entity.TouristType;
import com.expo.entity.VenuesDistance;
import com.expo.entity.VenuesInfo;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.VerificationCodeResp;
import com.expo.utils.Constants;
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
    public ActualScene loadSceneById(long spotId) {
        return mDao.queryById(ActualScene.class, spotId);
    }

    @Override
    public List<VenuesDistance> getVenues() {
        List<VenuesInfo> venuesInfos = mDao.query(VenuesInfo.class, new QueryParams()
                .add("notNull", "wiki_id")
                .add("and")
                .add("eq", "is_enable", 1));

        List<VenuesDistance> list = new ArrayList<>();
        for (VenuesInfo v : venuesInfos) {
            VenuesDistance vd = new VenuesDistance();
            vd.id = v.wikiId;
            try {
                vd.lat = Double.valueOf(v.lat);
                vd.lon = Double.valueOf(v.lon);
            } catch (Exception e) {

            }
            vd.title = LanguageUtil.chooseTest(v.caption, v.captionen);
            vd.content = LanguageUtil.chooseTest(v.remark, v.remarkEn);
            vd.voice = new ArrayList<>();
            vd.voice.add(LanguageUtil.chooseTest(v.voiceUrl, v.voiceUrl));
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

}
