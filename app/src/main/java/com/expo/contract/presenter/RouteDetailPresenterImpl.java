package com.expo.contract.presenter;

import android.text.TextUtils;

import com.expo.contract.RouteDetailContract;
import com.expo.db.QueryParams;
import com.expo.entity.Encyclopedias;
import com.expo.entity.RouteInfo;
import com.expo.entity.Venue;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;

import java.util.Arrays;

public class RouteDetailPresenterImpl extends RouteDetailContract.Presenter {

    public RouteDetailPresenterImpl(RouteDetailContract.View view) {
        super( view );
    }

    @Override
    public void getRouteDetail(Long id) {
        QueryParams params = new QueryParams();
        params.add( "eq", "_id", id );
        mView.showRouteDetail( mDao.unique( RouteInfo.class, params ) );
    }

    @Override
    public void getRouteDetailFromency(String id) {
        Encyclopedias encyclopedias = mDao.queryById( Encyclopedias.class, id );
        mView.showRemarkDetail( LanguageUtil.chooseTest( encyclopedias.remark, encyclopedias.remarkEn ) );
    }

    @Override
    public void getVenuesList(String ids) {
        ids = ids.replace( "[", "" ).replace( "]", "" );
        String[] idArr = ids.split( "," );
        QueryParams params = new QueryParams();
        params.add( "in", "_id", Arrays.asList( idArr ) );
        params.add( "and" );
        params.add( "eq", "is_enable", "1" );
        mView.showVenuesList( mDao.query( Venue.class, params ) );
    }

    @Override
    public void loadRemarkFormEncyclopedia(Venue venue) {
        if (venue == null || TextUtils.isEmpty( venue.getWikiId() ) || !venue.getWikiId().matches( Constants.Exps.NUMBER )) {
            return;
        }
        Encyclopedias encyclopedias = mDao.queryById( Encyclopedias.class, new QueryParams()
                .add( "eq", "_id", Long.parseLong( venue.getWikiId() ) ) );
        if (encyclopedias == null) {
            return;
        }
        venue.setRemark( encyclopedias.getRemark() );
        venue.setEnRemark( encyclopedias.getRemarkEn() );
        venue.setPicUrl( encyclopedias.getPicUrl() );
    }

}
