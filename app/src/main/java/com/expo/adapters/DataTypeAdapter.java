package com.expo.adapters;

import android.os.Parcel;


import com.expo.entity.DataType;

import java.util.ArrayList;
import java.util.List;

public class DataTypeAdapter implements Tab {
    private DataType mDataType;

    public DataTypeAdapter(DataType dataType) {
        this.mDataType = dataType;
    }

    private DataTypeAdapter(Parcel in) {
        if (in.readByte() == 0) {
            mDataType = null;
        } else {
            mDataType = in.readParcelable( DataType.class.getClassLoader() );
        }
    }

    @Override
    public String getTab() {
        String typeName = mDataType.getName();
        if (typeName.length() > 6){
            typeName = typeName.substring(0, 6);
        }
        return typeName;
    }

    @Override
    public String getEnTab() {
        return mDataType.getEnName();
    }

    @Override
    public Long getId() {
        return Long.valueOf( mDataType.getTypeId() );
    }

    @Override
    public Object getData() {
        return mDataType;
    }

    public static List<Tab> convertToTabList(List<DataType> types) {
        if (types == null)
            return null;
        List<Tab> tabs = new ArrayList<>();
        for (DataType type : types) {
            tabs.add( new DataTypeAdapter( type ) );
        }
        return tabs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mDataType == null) {
            dest.writeByte( (byte) 0 );
        } else {
            dest.writeByte( (byte) 1 );
            dest.writeParcelable( mDataType, flags );
        }
    }

    public static final Creator<DataTypeAdapter> CREATOR = new Creator<DataTypeAdapter>() {
        @Override
        public DataTypeAdapter createFromParcel(Parcel in) {
            return new DataTypeAdapter( in );
        }

        @Override
        public DataTypeAdapter[] newArray(int size) {
            return new DataTypeAdapter[size];
        }
    };
}
