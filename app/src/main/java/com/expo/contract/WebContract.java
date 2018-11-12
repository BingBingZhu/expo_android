package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.RichText;

public interface WebContract {
    abstract class Presenter extends IPresenter<View>{

        public Presenter(View view) {
            super(view);
        }

        public abstract void getUrlById(int rulId);
    }

    interface View extends IView{

        void returnRichText(RichText richText);
    }
}
