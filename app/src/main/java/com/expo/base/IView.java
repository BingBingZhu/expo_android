package com.expo.base;

import android.content.Context;

public interface IView {

    void showLoadingView();

    void hideLoadingView();

    Context getContext();

}
