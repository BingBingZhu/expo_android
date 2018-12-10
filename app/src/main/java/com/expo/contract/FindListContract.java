package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.Encyclopedias;
import com.expo.entity.Find;

import java.util.List;

public interface FindListContract {

    abstract class Presenter extends IPresenter<FindListContract.View> {

        public Presenter(FindListContract.View view) {
            super(view);
        }

        public abstract void getSocietyListFilter(int page, boolean fresh);

    }

    interface View extends IView {
        void addFindList(List<Find> data, boolean isFresh);
    }
}
