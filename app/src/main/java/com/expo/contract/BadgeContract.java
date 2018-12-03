package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.Badge;

import java.util.List;

public interface BadgeContract {

    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void loadBadgeData();
    }

    interface View extends IView {

        void loadBadgeDataRes(List<Badge> badges);
    }
}
