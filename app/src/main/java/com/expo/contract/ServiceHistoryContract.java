package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.VisitorService;

import java.util.ArrayList;
import java.util.List;

public interface ServiceHistoryContract {
    abstract class Presenter extends IPresenter<View> {
        public Presenter(View view) {
            super(view);
        }

        public abstract void loadMoreData(int page);

        public abstract void loadAllData();

        public abstract String loadCommonInfo(String type);
    }

    interface View extends IView {
        void addDataToList(List<VisitorService> visitorServices);

        void loadDataRes(boolean b);
    }
}
