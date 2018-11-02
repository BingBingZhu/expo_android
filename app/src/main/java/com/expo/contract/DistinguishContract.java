package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.Plant;
import com.expo.network.response.GetBaiduDisting_Rsb;

import java.util.List;

public interface DistinguishContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super( view );
        }

        public abstract void distinguishPlant(String imgStr);

    }

    interface View extends IView {
        void onDistinguishComplete(List<Plant> plants);

        void onBaiduDistinguishComplete(GetBaiduDisting_Rsb plant);
    }
}