package com.expo.adapters;

import android.os.Parcelable;

public interface ListItemData extends Parcelable {
    Long getId();

    String getCaption();

    String getPicUrl();

    Integer getRecommend();

    String getRemark();

    String getEnCaption();

    String getEnRemark();

}
