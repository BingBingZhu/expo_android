package com.expo.adapters;

import android.os.Parcelable;

public interface ListItemData extends Parcelable {
    String getCaption();
    String getPicUrl();
    Integer getRecommend();
    String getRemark();
}
