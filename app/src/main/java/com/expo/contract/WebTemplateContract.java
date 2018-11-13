package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.ActualScene;
import com.expo.entity.Encyclopedias;

public interface WebTemplateContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super( view );
        }

        public abstract Encyclopedias loadEncyclopediaById(long id);

        public abstract ActualScene loadSceneByWikiId(long id);

        public abstract String toJson(Object obj);
    }

    interface View extends IView {

        void getActualSceneDataByIdRes(ActualScene actualScene);
    }
}