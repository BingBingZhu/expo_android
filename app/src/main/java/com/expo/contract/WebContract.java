package com.expo.contract;

import android.location.Location;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.Coupon;
import com.expo.entity.RichText;
import com.expo.entity.Venue;

public interface WebContract {
    abstract class Presenter extends IPresenter<View>{

        public Presenter(View view) {
            super(view);
        }

        public abstract void getUrlById(int rulId);

        public abstract void logout();

        public abstract void setUsedCoupon(Coupon coupon);

        public abstract boolean checkInPark(Location location);

        public abstract String loadPlantJson(int type);

//        public abstract Venue getNearbyServiceCenter(Location location);
    }

    interface View extends IView{

        void returnRichText(RichText richText);

        void logoutResp();

        void useCoupon();
    }
}
