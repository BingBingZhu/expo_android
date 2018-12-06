package com.expo.contract.presenter;

import com.expo.contract.FindListContract;
import com.expo.contract.ListContract;
import com.expo.db.QueryParams;
import com.expo.entity.Encyclopedias;

import java.util.List;

public class FindListPresenterImpl extends FindListContract.Presenter {

    private static final int PER_PAGE_COUNT = 10;

    public FindListPresenterImpl(FindListContract.View view) {
        super(view);
    }

    @Override
    public void loadEncyByType(Long tabId, int page) {
        List<Encyclopedias> data = mDao.query( Encyclopedias.class, new QueryParams()
                .add( "eq", "type_id", tabId )
                .add( "and" )
                .add( "eq", "enable", 1 )
                .add( "limit", page * PER_PAGE_COUNT, PER_PAGE_COUNT )
                .add( "orderBy", "recommend", false )
                .add( "orderBy", "py", true ) );
        mView.addEncysToList( data );
    }
}
