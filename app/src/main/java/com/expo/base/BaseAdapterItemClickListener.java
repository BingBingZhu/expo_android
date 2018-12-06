package com.expo.base;

import android.view.View;

public interface BaseAdapterItemClickListener<T> {

    void itemClick(View view, int position, T t);
}
