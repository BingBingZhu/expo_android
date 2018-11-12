package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.ActualScene;

public interface WebTemplateContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void setDataType(int intExtra);
        public abstract String getDataJsonById(int id);

        public abstract void getDataById(long id);
    }

    interface View extends IView{
        void getDataJsonByIdRes(String jsonData);

        void getActualSceneDataByIdRes(ActualScene actualScene);
    }
}
