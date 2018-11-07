package com.expo.adapters;

import android.os.Parcelable;

public interface DownloadData extends Parcelable {

    Integer getStatus();
    Long getId();
    String getLocalPath();
    String getResUrl();
    Long getCurrPosition();

    void setStatus(int downloadState);
    void setLocalPath(String path);
    void setCurrPosition(long position);
}
