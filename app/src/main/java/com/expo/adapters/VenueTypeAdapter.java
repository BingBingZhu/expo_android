package com.expo.adapters;

import android.os.Parcel;

import com.expo.entity.DataType;
import com.expo.entity.VenuesType;

import java.util.ArrayList;
import java.util.List;

public class VenueTypeAdapter implements Tab {
    private VenuesType mDataType;

    public VenueTypeAdapter(VenuesType dataType) {
        this.mDataType = dataType;
    }

    private VenueTypeAdapter(Parcel in) {
        if (in.readByte() == 0) {
            mDataType = null;
        } else {
            mDataType = in.readParcelable( DataType.class.getClassLoader() );
        }
    }

    @Override
    public String getTab() {
        String typeName = mDataType.getTypeName();
        if (typeName.length() > 6){
            typeName = typeName.substring(0, 6);
        }
        return typeName;
    }

    @Override
    public String getEnTab() {
        return mDataType.getTypeNameEn();
    }

    @Override
    public Long getId() {
        return Long.valueOf( mDataType.getId() );
    }

    @Override
    public Object getData() {
        return mDataType;
    }

    public static List<Tab> convertToTabList(List<VenuesType> types) {
        if (types == null)
            return null;
        List<Tab> tabs = new ArrayList<>();
        for (VenuesType type : types) {
            tabs.add( new VenueTypeAdapter( type ) );
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

    public static final Creator<VenueTypeAdapter> CREATOR = new Creator<VenueTypeAdapter>() {
        @Override
        public VenueTypeAdapter createFromParcel(Parcel in) {
            return new VenueTypeAdapter( in );
        }

        @Override
        public VenueTypeAdapter[] newArray(int size) {
            return new VenueTypeAdapter[size];
        }
    };
}
