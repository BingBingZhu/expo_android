package com.expo.adapters;

import android.annotation.SuppressLint;
import android.os.Parcel;

import com.expo.entity.ActualScene;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ParcelCreator")
public class ActualSceneAdapter implements ListItemData {

    private ActualScene mSceneAndEncy;
    public ActualSceneAdapter(ActualScene dataType) {
        this.mSceneAndEncy = dataType;
    }

    public ActualSceneAdapter(Parcel in){
        if (in.readByte() == 0) {
            mSceneAndEncy = null;
        } else {
            mSceneAndEncy = in.readParcelable( ActualScene.class.getClassLoader() );
        }
    }

    @Override
    public String getCaption() {
        return mSceneAndEncy.getCaption();
    }

    @Override
    public String getPicUrl() {
        return mSceneAndEncy.getPicUrl();
    }

    @Override
    public Integer getRecommend() {
        return mSceneAndEncy.getIsRecommended();
    }

    @Override
    public String getRemark() {
        return mSceneAndEncy.getRemark();
    }

    @Override
    public String getEnCaption() {
        return mSceneAndEncy.getEnCaption();
    }

    @Override
    public String getEnRemark() {
        return mSceneAndEncy.getEnRemark();
    }

    public static List<ListItemData> convertToTabList(List<ActualScene> types) {
        if (types == null)
            return null;
        List<ListItemData> listItemData = new ArrayList<>();
        for (ActualScene sceneAndEncy : types) {
            listItemData.add( new ActualSceneAdapter( sceneAndEncy ) );
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

    public static final Creator<ActualSceneAdapter> CREATOR = new Creator<ActualSceneAdapter>() {
        @Override
        public ActualSceneAdapter createFromParcel(Parcel in) {
            return new ActualSceneAdapter( in );
        }

        @Override
        public ActualSceneAdapter[] newArray(int size) {
            return new ActualSceneAdapter[size];
        }
    };
}
