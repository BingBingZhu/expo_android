package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.expo.adapters.DownloadData;
import com.expo.utils.CommUtils;

public class AppInfo implements DownloadData, Parcelable {
    public int id;
    public int apkfilesize;
    public String apkurl;
    public String isforce;
    public String platformname;
    public String remark;
    public String remarken;
    public int resfilesize;
    public String resurl;
    public String resver;
    public String updatetime;
    public String ver;

    public int status;
    public String localPath;
    public int currPosition;


    protected AppInfo() {

    }

    protected AppInfo(Parcel in) {
        apkfilesize = in.readInt();
        apkurl = in.readString();
        id = in.readInt();
        isforce = in.readString();
        platformname = in.readString();
        remark = in.readString();
        remarken = in.readString();
        resfilesize = in.readInt();
        resurl = in.readString();
        resver = in.readString();
        updatetime = in.readString();
        ver = in.readString();
    }

    public static final Creator<AppInfo> CREATOR = new Creator<AppInfo>() {
        @Override
        public AppInfo createFromParcel(Parcel in) {
            return new AppInfo(in);
        }

        @Override
        public AppInfo[] newArray(int size) {
            return new AppInfo[size];
        }
    };

    @Override
    public Integer getStatus() {
        return status;
    }

    @Override
    public Long getId() {
        return Long.valueOf(id);
    }

    @Override
    public String getLocalPath() {
        return localPath;
    }

    @Override
    public String getResUrl() {
        return CommUtils.getFullUrl(resurl);
    }

    @Override
    public Long getCurrPosition() {
        return null;
    }

    @Override
    public void setStatus(int downloadState) {

    }

    @Override
    public void setLocalPath(String path) {

    }

    @Override
    public void setCurrPosition(long position) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(apkfilesize);
        dest.writeString(apkurl);
        dest.writeInt(id);
        dest.writeString(isforce);
        dest.writeString(platformname);
        dest.writeString(remark);
        dest.writeString(remarken);
        dest.writeInt(resfilesize);
        dest.writeString(resurl);
        dest.writeString(resver);
        dest.writeString(updatetime);
        dest.writeString(ver);
    }
}
