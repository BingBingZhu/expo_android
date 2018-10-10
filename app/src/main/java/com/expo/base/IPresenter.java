package com.expo.base;

import com.expo.db.dao.BaseDao;
import com.expo.db.dao.BaseDaoImpl;

public abstract class IPresenter<T extends IView> {
    protected T mView;
    protected BaseDao mDao;

    public IPresenter(T view) {
        this.mView = view;
        mDao = new BaseDaoImpl();
    }
}
