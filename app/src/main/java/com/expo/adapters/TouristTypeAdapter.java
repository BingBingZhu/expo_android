package com.expo.adapters;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.expo.entity.TouristType;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ParcelCreator")
public class TouristTypeAdapter implements DownloadData {

    private TouristType mTouristType;
    public TouristTypeAdapter(TouristType dataType) {
        this.mTouristType = dataType;
    }

    public TouristTypeAdapter(Parcel in){
        if (in.readByte() == 0) {
            mTouristType = null;
        } else {
            mTouristType = in.readParcelable( TouristType.class.getClassLoader() );
        }
    }

    @Override
    public Integer getStatus() {
        return mTouristType.getDownState();
    }

    @Override
    public Long getId() {
        return mTouristType.getId();
    }

    @Override
    public String getLocalPath() {
        return mTouristType.getLocalPath();
    }

    @Override
    public String getResUrl() {
        return mTouristType.getModelFile();
    }

    @Override
    public Long getCurrPosition() {
        return mTouristType.getCurrPosition();
    }

    @Override
    public void setStatus(int downloadState) {
        mTouristType.setDownState(downloadState);
    }

    @Override
    public void setLocalPath(String path) {
        mTouristType.setLocalPath(path);
    }

    @Override
    public void setCurrPosition(long position) {
        mTouristType.setCurrPosition(position);
    }

    public static List<DownloadData> convertToTabList(List<TouristType> types) {
        if (types == null)
            return null;
        List<DownloadData> downloadData = new ArrayList<>();
        for (TouristType type : types) {
            downloadData.add( new TouristTypeAdapter( type ) );
        }
        return downloadData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mTouristType == null) {
            dest.writeByte( (byte) 0 );
        } else {
            dest.writeByte( (byte) 1 );
            dest.writeParcelable( mTouristType, flags );
        }
    }

    public static final Parcelable.Creator<TouristTypeAdapter> CREATOR = new Parcelable.Creator<TouristTypeAdapter>() {
        @Override
        public TouristTypeAdapter createFromParcel(Parcel in) {
            return new TouristTypeAdapter( in );
        }

        @Override
        public TouristTypeAdapter[] newArray(int size) {
            return new TouristTypeAdapter[size];
        }
    };
}
