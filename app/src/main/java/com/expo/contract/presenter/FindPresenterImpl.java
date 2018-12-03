package com.expo.contract.presenter;

import com.expo.adapters.DataTypeAdapter;
import com.expo.adapters.Tab;
import com.expo.contract.EncyclopediasContract;
import com.expo.contract.FindContract;
import com.expo.db.QueryParams;
import com.expo.entity.DataType;

import java.util.List;

public class FindPresenterImpl extends FindContract.Presenter {
    public FindPresenterImpl(FindContract.View view) {
        super( view );
    }

    @Override
    public void loadTabs() {
        List<DataType> encyclopediaTypes = mDao.query( DataType.class, new QueryParams()
                .add( "eq", "kind", 3 )
                .add( "and" )
                .add( "eq", "enabled", 1 )
                .add( "orderBy", "sort", "asc" ) );
        List<Tab> data = DataTypeAdapter.convertToTabList( encyclopediaTypes );
        mView.setTabData( data );
    }
}
