package com.expo.adapters;

import android.annotation.SuppressLint;
import android.os.Parcel;

import com.expo.entity.VrLableInfo;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ParcelCreator")
public class VrTabAdapter implements Tab {

    private VrLableInfo mVrLableInfo;
    public VrTabAdapter(VrLableInfo dataType) {
        this.mVrLableInfo = dataType;
    }

    public VrTabAdapter(Parcel in){
        if (in.readByte() == 0) {
            mVrLableInfo = null;
        } else {
            mVrLableInfo = in.readParcelable( VrLableInfo.class.getClassLoader() );
        }
    }

    @Override
    public String getTab() {
        return mVrLableInfo.getCaption();
    }

    @Override
    public String getEnTab() {
        return mVrLableInfo.getCaptionEn();
    }

    @Override
    public Long getId() {
        return mVrLableInfo.getLinkId();
    }

    @Override
    public VrLableInfo getData() {
        return mVrLableInfo;
    }

    public static List<Tab> convertToTabList(List<VrLableInfo> types) {
        if (types == null)
            return null;
        List<Tab> tabList = new ArrayList<>();
        for (VrLableInfo vrLableInfo : types) {
            tabList.add( new VrTabAdapter( vrLableInfo ) );
        }
        return tabList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mVrLableInfo == null) {
            dest.writeByte( (byte) 0 );
        } else {
            dest.writeByte( (byte) 1 );
            dest.writeParcelable( mVrLableInfo, flags );
        }
    }

    public static final Creator<VrTabAdapter> CREATOR = new Creator<VrTabAdapter>() {
        @Override
        public VrTabAdapter createFromParcel(Parcel in) {
            return new VrTabAdapter( in );
        }

        @Override
        public VrTabAdapter[] newArray(int size) {
            return new VrTabAdapter[size];
        }
    };
}
