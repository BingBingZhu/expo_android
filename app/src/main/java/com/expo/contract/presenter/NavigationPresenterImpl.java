package com.expo.contract.presenter;

import com.expo.contract.NavigationContract;
import com.expo.db.QueryParams;
import com.expo.entity.ActualScene;
import com.expo.entity.Encyclopedias;
import com.expo.entity.VenuesDistance;
import com.expo.entity.VenuesInfo;
import com.expo.utils.LanguageUtil;

import java.util.ArrayList;
import java.util.List;

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
            vd.voice = LanguageUtil.chooseTest(v.voiceUrl, v.voiceUrl);
            list.add(vd);
        }
        return list;
    }

    @Override
    public Encyclopedias getEncyclopedias(String id) {
        return mDao.queryById(Encyclopedias.class, id);
    }

}
