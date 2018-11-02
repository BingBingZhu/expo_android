package com.expo.base;

import android.view.View;

public interface BaseAdapterItemClickListener<T> {

    public void itemClick(View view, int position, T t);
}
