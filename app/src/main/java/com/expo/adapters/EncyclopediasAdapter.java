package com.expo.adapters;

import android.annotation.SuppressLint;
import android.os.Parcel;

import com.expo.entity.Encyclopedias;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ParcelCreator")
public class EncyclopediasAdapter implements ListItemData {

    private Encyclopedias mSceneAndEncy;
    public EncyclopediasAdapter(Encyclopedias dataType) {
        this.mSceneAndEncy = dataType;
    }

    public EncyclopediasAdapter(Parcel in){
        if (in.readByte() == 0) {
            mSceneAndEncy = null;
        } else {
            mSceneAndEncy = in.readParcelable( Encyclopedias.class.getClassLoader() );
        }
    }

    @Override
    public String getCaption() {
        return mSceneAndEncy.caption;
    }

    @Override
    public String getPicUrl() {
        return mSceneAndEncy.picUrl;
    }

    @Override
    public Integer getRecommend() {
        return mSceneAndEncy.recommend;
    }

    @Override
    public String getRemark() {
        return mSceneAndEncy.remark;
    }

    @Override
    public String getEnCaption() {
        return mSceneAndEncy.getCaptionEn();
    }

    @Override
    public String getEnRemark() {
        return mSceneAndEncy.getRemarkEn();
    }

    public static List<ListItemData> convertToTabList(List<Encyclopedias> types) {
        if (types == null)
            return null;
        List<ListItemData> listItemData = new ArrayList<>();
        for (Encyclopedias encyclopedias : types) {
            listItemData.add( new EncyclopediasAdapter( encyclopedias ) );
        }
        return listItemData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mSceneAndEncy == null) {
            dest.writeByte( (byte) 0 );
        } else {
            dest.writeByte( (byte) 1 );
            dest.writeParcelable( mSceneAndEncy, flags );
        }
    }

    public static final Creator<EncyclopediasAdapter> CREATOR = new Creator<EncyclopediasAdapter>() {
        @Override
        public EncyclopediasAdapter createFromParcel(Parcel in) {
            return new EncyclopediasAdapter( in );
        }

        @Override
        public EncyclopediasAdapter[] newArray(int size) {
            return new EncyclopediasAdapter[size];
        }
    };
}
