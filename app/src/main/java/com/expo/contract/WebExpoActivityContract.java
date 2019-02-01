package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.Encyclopedias;
import com.expo.entity.ExpoActivityInfo;
import com.expo.entity.Schedule;
import com.expo.entity.Venue;

import java.util.List;

public interface WebExpoActivityContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super( view );
        }

        public abstract ExpoActivityInfo loadEncyclopediaById(long id);

        public abstract Venue loadSceneByWikiId(long id);

        public abstract Schedule loadScheduleByWikiId(long id);

        public abstract String toJson(Object obj);
        public abstract void scoreChange(String type, String wikiId);

        public abstract String loadCommonInfo(String type);

        public abstract String getRecommendAndTodayExpoActivitys(Long time, long id);
    }

    interface View extends IView {

        void getActualSceneDataByIdRes(Venue venue);

        void addScore();
    }
}
