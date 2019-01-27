package com.expo.contract.presenter;

import com.expo.contract.ListContract;
import com.expo.db.QueryParams;
import com.expo.entity.Encyclopedias;
import com.expo.entity.VrInfo;

import java.util.List;

public class ListPresenterImpl extends ListContract.Presenter {

    private static final int PER_PAGE_COUNT = 10;

    public ListPresenterImpl(ListContract.View view) {
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
                .add( "orderBy", "idx", true ) );
        mView.addEncysToList( data );
    }

    @Override
    public void loadVrsByType(int vrType, Long tabId, int page) {
        List<VrInfo> data = mDao.query( VrInfo.class, new QueryParams()
                .add( "eq", "top_kind", vrType )
                .add("and")
                .add("like", "attr_ids", "%;"+tabId+";%")
                .add( "limit", page * PER_PAGE_COUNT, PER_PAGE_COUNT ) );
        mView.addVrsToList( data );
    }
}
