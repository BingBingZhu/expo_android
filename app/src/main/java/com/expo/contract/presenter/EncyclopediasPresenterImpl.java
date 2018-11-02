package com.expo.contract.presenter;

import android.os.Parcel;

import com.expo.adapters.DataTypeAdapter;
import com.expo.adapters.Tab;
import com.expo.contract.EncyclopediasContract;
import com.expo.db.QueryParams;
import com.expo.entity.DataType;
import com.expo.entity.Subject;

import java.util.List;

public class EncyclopediasPresenterImpl extends EncyclopediasContract.Presenter {
    public EncyclopediasPresenterImpl(EncyclopediasContract.View view) {
        super( view );
    }

    @Override
    public void loadTabs() {
        List<DataType> encyclopediaTypes = mDao.query( DataType.class, new QueryParams()
                .add( "eq", "kind", 3 )
                .add( "and" )
                .add( "eq", "enabled", 1 )
                .add( "orderBy", "sort", true ) );
        List<Tab> data = DataTypeAdapter.convertToTabList( encyclopediaTypes );
        data.add(new Tab() {
            @Override
            public String getTab() {
                return "测试百科类型1";
            }

            @Override
            public Long getId() {
                return null;
            }

            @Override
            public Object getData() {
                return null;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {

            }
        });
        data.add(new Tab() {
            @Override
            public String getTab() {
                return "其他";
            }

            @Override
            public Long getId() {
                return null;
            }

            @Override
            public Object getData() {
                return null;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {

            }
        });
        mView.setTabData( data );
    }
}
