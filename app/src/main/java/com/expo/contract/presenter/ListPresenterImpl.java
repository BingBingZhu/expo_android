package com.expo.contract.presenter;

import com.expo.contract.HomeContract;
import com.expo.contract.ListContract;
import com.expo.db.QueryParams;
import com.expo.entity.Encyclopedias;

import java.util.List;

public class ListPresenterImpl extends ListContract.Presenter {

    private static final int PER_PAGE_COUNT = 10;

    public ListPresenterImpl(ListContract.View view) {
        super(view);
    }

    @Override
    public void loadEncyByType(String typeName, int page) {
        List<Encyclopedias> data = mDao.query(Encyclopedias.class, new QueryParams()
                .add( "eq", "type_name", typeName )
                .add( "limit", page * PER_PAGE_COUNT, PER_PAGE_COUNT )
                .add( "orderBy", "py", true )
                .add( "orderBy", "recommend", "desc" ));
        mView.addEncysToList(data);
    }
}
