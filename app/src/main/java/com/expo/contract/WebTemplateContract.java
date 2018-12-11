package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.Venue;
import com.expo.entity.Encyclopedias;

import java.util.List;

public interface WebTemplateContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super( view );
        }

        public abstract Encyclopedias loadEncyclopediaById(long id);

        public abstract Venue loadSceneByWikiId(long id);

        public abstract String toJson(Object obj);

        public abstract List<Encyclopedias> loadNeayByVenues(Venue as);

        public abstract void scoreChange(String type, String wikiId);
    }

    interface View extends IView {

        void getActualSceneDataByIdRes(Venue venue);
    }
}
