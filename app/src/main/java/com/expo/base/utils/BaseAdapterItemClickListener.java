package com.expo.base.utils;

import android.view.View;

public interface BaseAdapterItemClickListener<T> {

    public void itemClick(View view, int position, T t);
}
