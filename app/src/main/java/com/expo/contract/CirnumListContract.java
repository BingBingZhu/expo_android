package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.Circum;

import java.util.ArrayList;

public interface CirnumListContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void loadCircumData(int circumType, int page);

        public abstract String getDistance(double latitude, double longitude);
    }

    interface View extends IView {
        void loadCircumDataRes(ArrayList<Circum> circums);
    }
}
