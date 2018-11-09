package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.VisitorService;

public interface SeekHelpContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void addVisitorService(VisitorService visitorService);

    }

    interface View extends IView {
        void complete();

    }
}
