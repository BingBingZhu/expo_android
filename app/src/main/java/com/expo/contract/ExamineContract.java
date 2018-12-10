package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.Find;

import java.util.List;

public interface ExamineContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void getMySocietyList(boolean isPass);


    }

    interface View extends IView {
        void freshFind(List<Find> list);
    }
}
