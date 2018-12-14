package com.expo.contract.presenter;

import com.expo.adapters.EncyclopediasAdapter;
import com.expo.contract.EncyclopediaSearchContract;
import com.expo.db.QueryParams;
import com.expo.entity.Encyclopedias;

import java.util.List;

public class EncyclopediaSearchPresenterImpl extends EncyclopediaSearchContract.Presenter {
    public EncyclopediaSearchPresenterImpl(EncyclopediaSearchContract.View view) {
        super( view );
    }

    @Override
    public void searchEncy(String searchContentStr) {
        List<Encyclopedias> encyclopedias = mDao.query( Encyclopedias.class, new QueryParams()
                .add( "like", "caption", "%" + searchContentStr + "%" )
                .add( "or" )
                .add( "like", "caption_en", "%" + searchContentStr + "%" )
                .add( "and" )
                .add( "eq", "enable", 1 )
                .add( "orderBy", "idx", true ) );
        mView.getSearchResult( EncyclopediasAdapter.convertToTabList( encyclopedias ) );
    }
}
