package com.expo.adapters;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.expo.entity.DownloadInfo;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ParcelCreator")
public class DownloadInfoAdapter implements DownloadData {

    private DownloadInfo mDownloadInfo;
    public DownloadInfoAdapter(DownloadInfo dataType) {
        this.mDownloadInfo = dataType;
    }

    public DownloadInfoAdapter(Parcel in){
        if (in.readByte() == 0) {
            mDownloadInfo = null;
        } else {
            mDownloadInfo = in.readParcelable( DownloadInfo.class.getClassLoader() );
        }
    }

    @Override
    public Integer getStatus() {
        return mDownloadInfo.getStatus();
    }

    @Override
    public Long getId() {
        return mDownloadInfo.getId();
    }

    @Override
    public String getLocalPath() {
        return mDownloadInfo.getLocalPath();
    }

    @Override
    public String getResUrl() {
        return mDownloadInfo.getResUrl();
    }

    @Override
    public Long getCurrPosition() {
        return mDownloadInfo.getCurrPosition();
    }

    @Override
    public void setStatus(int downloadState) {
        mDownloadInfo.setStatus(downloadState);
    }

    @Override
    public void setLocalPath(String path) {
        mDownloadInfo.setLocalPath(path);
    }

    @Override
    public void setCurrPosition(long position) {
        mDownloadInfo.setCurrPosition(position);
    }

    public static List<DownloadData> convertToTabList(List<DownloadInfo> types) {
        if (types == null)
            return null;
        List<DownloadData> downloadData = new ArrayList<>();
        for (DownloadInfo type : types) {
            downloadData.add( new DownloadInfoAdapter( type ) );
        }
        return downloadData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mDownloadInfo == null) {
            dest.writeByte( (byte) 0 );
        } else {
            dest.writeByte( (byte) 1 );
            dest.writeParcelable( mDownloadInfo, flags );
        }
    }

    public static final Parcelable.Creator<DownloadInfoAdapter> CREATOR = new Parcelable.Creator<DownloadInfoAdapter>() {
        @Override
        public DownloadInfoAdapter createFromParcel(Parcel in) {
            return new DownloadInfoAdapter( in );
        }

        @Override
        public DownloadInfoAdapter[] newArray(int size) {
            return new DownloadInfoAdapter[size];
        }
    };
}
