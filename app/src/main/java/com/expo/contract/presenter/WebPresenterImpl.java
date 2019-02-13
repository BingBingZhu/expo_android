package com.expo.contract.presenter;

import android.location.Location;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.expo.contract.WebContract;
import com.expo.db.QueryParams;
import com.expo.entity.CommonInfo;
import com.expo.entity.Coupon;
import com.expo.entity.Park;
import com.expo.entity.User;
import com.expo.entity.Venue;
import com.expo.entity.VenuesType;
import com.expo.map.MapUtils;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.BaseResponse;
import com.expo.network.response.RichTextRsp;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

public class WebPresenterImpl extends WebContract.Presenter {
    public WebPresenterImpl(WebContract.View view) {
        super(view);
    }

    @Override
    public void getUrlById(int urlId) {
        mView.showLoadingView();
        Map<String, Object> params = Http.getBaseParams();
        params.put( "id", urlId );
        RequestBody requestBody = Http.buildRequestBody( params );
        Observable<RichTextRsp> observable = Http.getServer().getRichText( requestBody );
        Http.request( new ResponseCallback<RichTextRsp>() {
            @Override
            protected void onResponse(RichTextRsp rsp) {
                mView.returnRichText( rsp.richText );
            }

            @Override
            public void onComplete() {
                mView.hideLoadingView();
                super.onComplete();
            }
        }, observable );
    }

    @Override
    public void logout() {
        User user = mDao.unique(User.class, null);
        mView.showLoadingView();
        Map<String, Object> params = Http.getBaseParams();
        params.put("Uid", user.getUid());
        params.put("Ukey", user.getUkey());
        RequestBody requestBody = Http.buildRequestBody(params);
        Observable<BaseResponse> observable = Http.getServer().userlogout(requestBody);
        Http.request(new ResponseCallback<BaseResponse>() {
            @Override
            protected void onResponse(BaseResponse rsp) {
                user.deleteOnDb(mDao);
                mView.logoutResp();
            }

            @Override
            public void onComplete() {
                mView.hideLoadingView();
                super.onComplete();
            }
        }, observable);
    }

    @Override
    public void setUsedCoupon(Coupon coupon) {
        mView.showLoadingView();
        Map<String, Object> params = Http.getBaseParams();
        params.put("UserCouponId", coupon.couponId);
        params.put("vrcode", coupon.vrCode);
        RequestBody requestBody = Http.buildRequestBody(params);
        Observable<BaseResponse> observable = Http.getServer().setUsedCoupon(requestBody);
        Http.request(new ResponseCallback<BaseResponse>() {
            @Override
            protected void onResponse(BaseResponse rsp) {
                mView.useCoupon();
            }

            @Override
            public void onComplete() {
                mView.hideLoadingView();
                super.onComplete();
            }
        }, observable);
    }

    @Override
    public boolean checkInPark(Location location) {
        Park park = mDao.unique( Park.class, null );
        if (park == null) return false;
        List<double[]> bounds = park.getElectronicFenceList();
        return MapUtils.ptInPolygon( location.getLatitude(), location.getLongitude(), bounds );
    }

//    @Override
//    public Venue getNearbyServiceCenter(Location location) {
//        VenuesType venuesType = mDao.unique( VenuesType.class, new QueryParams()
//                .add( "eq", "is_enable", 1 )
//                .add( "and" )
//                .add( "like", "type_name", "%\u670d\u52a1%" ) );//服务
//        if (venuesType != null) {
//            List<Venue> venues = mDao.query( Venue.class, new QueryParams()
//                    .add( "eq", "type", String.valueOf( venuesType.getId() ) ) );
//            if (venues != null) {
//                if (venues.size() > 1) {
//                    float minDistance = Float.MAX_VALUE;
//                    Venue result = null;
//                    LatLng loc = new LatLng( location.getLatitude(), location.getLongitude() );
//                    for (Venue venue : venues) {
//                        float distance = AMapUtils.calculateLineDistance( loc, new LatLng( venue.getLat(), venue.getLng() ) );
//                        if (distance < minDistance) {
//                            minDistance = distance;
//                            result = venue;
//                        }
//                    }
//                    return result;
//                } else {
//                    return venues.get( 0 );
//                }
//            }
//        }
//        return null;
//    }
}
