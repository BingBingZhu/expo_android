package com.expo.contract.presenter;

import com.expo.contract.NavigationContract;
import com.expo.db.QueryParams;
import com.expo.entity.Encyclopedias;
import com.expo.entity.Venue;
import com.expo.entity.VenuesDistance;
import com.expo.utils.LanguageUtil;

import java.util.ArrayList;
import java.util.List;

public class NavigationPresenterImpl extends NavigationContract.Presenter {
    public NavigationPresenterImpl(NavigationContract.View view) {
        super( view );
    }

    @Override
    public Venue loadSceneById(long spotId) {
        return mDao.queryById( Venue.class, spotId );
    }

    @Override
    public List<VenuesDistance> getVenues() {
        List<Venue> venuesInfos = mDao.query( Venue.class, new QueryParams()
                .add( "notNull", "wiki_id" )
                .add( "and" )
                .add( "eq", "is_enable", 1 ) );

        List<VenuesDistance> list = new ArrayList<>();
        for (Venue v : venuesInfos) {
            VenuesDistance vd = new VenuesDistance();
            vd.id = v.getWikiId();
            try {
                vd.lat = v.getLat();
                vd.lon = v.getLng();
            } catch (Exception e) {

            }
            vd.title = LanguageUtil.chooseTest( v.getCaption(), v.getEnCaption() );
            vd.content = LanguageUtil.chooseTest( v.getRemark(), v.getEnRemark() );
            vd.voice = new ArrayList<>();
            vd.voice.add( LanguageUtil.chooseTest( v.getVoiceUrl(), v.getVoiceUrlEn() ) );
            list.add( vd );
        }
        return list;
    }

    @Override
    public Encyclopedias getEncyclopedias(String id) {
        return mDao.queryById( Encyclopedias.class, id );
    }

}
