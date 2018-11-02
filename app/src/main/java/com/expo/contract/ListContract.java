package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.Encyclopedias;

import java.util.List;

public interface ListContract {

    abstract class Presenter extends IPresenter<ListContract.View> {

        public Presenter(ListContract.View view) {
            super(view);
        }

        public abstract void loadEncyByType(String typeName, int page);
    }

    interface View extends IView {
        void addEncysToList(List<Encyclopedias> data);
    }
}
