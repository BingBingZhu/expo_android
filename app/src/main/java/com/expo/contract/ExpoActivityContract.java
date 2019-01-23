package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.Badge;

import java.util.List;

public interface ExpoActivityContract {

    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        /**
         * 根据月份获取日期数组
         */
        public abstract void loadDate(long time);

        /**
         * 1:下个月，-1上个月
         */
        public abstract void goNextMonth(long time, int month);

        /***
         * 加载列表
         * @param date 日期
         * @param time 0：全部，1：上午，2：下午，3：晚上
         */
        public abstract void loadActivity(String date, int time);


    }

    interface View extends IView {
        void freshDate(List<Long> list);

        void freshData(List list);
    }
}
