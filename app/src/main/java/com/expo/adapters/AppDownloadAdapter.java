package com.expo.adapters;

import android.annotation.SuppressLint;
import android.os.Parcel;

import com.expo.entity.AppInfo;
import com.expo.entity.TouristType;
import com.expo.module.download.DownloadManager;
import com.expo.utils.CommUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ParcelCreator")
public class AppDownloadAdapter implements DownloadData {

    private AppInfo mAppInfo;

    public AppDownloadAdapter(AppInfo dataType) {
        this.mAppInfo = dataType;
    }

    public AppDownloadAdapter(Parcel in) {
        if (in.readByte() == 0) {
            mAppInfo = null;
        } else {
            mAppInfo = in.readParcelable(TouristType.class.getClassLoader());
        }
    }

    @Override
    public Integer getStatus() {
        return DownloadManager.DOWNLOAD_IDLE;
    }

    @Override
    public Long getId() {
        return mAppInfo.getId();
    }

    @Override
    public String getLocalPath() {
        return mAppInfo.getLocalPath();
    }

    @Override
    public String getResUrl() {
        return CommUtils.getFullUrl(mAppInfo.apkurl);
    }

    @Override
    public Long getCurrPosition() {
        return mAppInfo.getCurrPosition();
    }

    @Override
    public void setStatus(int downloadState) {
        mAppInfo.status = downloadState;
    }

    @Override
    public void setLocalPath(String path) {
        mAppInfo.setLocalPath(path);
    }

    @Override
    public void setCurrPosition(long position) {
        mAppInfo.setCurrPosition(position);
    }

    public static List<DownloadData> convertToTabList(List<AppInfo> types) {
        if (types == null)
            return null;
        List<DownloadData> downloadData = new ArrayList<>();
        for (AppInfo type : types) {
            downloadData.add(new AppDownloadAdapter(type));
        }
        return downloadData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mAppInfo == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeParcelable(mAppInfo, flags);
        }
    }

    public static final Creator<AppDownloadAdapter> CREATOR = new Creator<AppDownloadAdapter>() {
        @Override
        public AppDownloadAdapter createFromParcel(Parcel in) {
            return new AppDownloadAdapter(in);
        }

        @Override
        public AppDownloadAdapter[] newArray(int size) {
            return new AppDownloadAdapter[size];
        }
    };
}
