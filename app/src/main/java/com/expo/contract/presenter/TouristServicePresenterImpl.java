package com.expo.contract.presenter;

import android.location.Location;
import android.text.TextUtils;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.expo.contract.TouristServiceContract;
import com.expo.db.QueryParams;
import com.expo.entity.CommonInfo;
import com.expo.entity.Park;
import com.expo.entity.Venue;
import com.expo.entity.VenuesType;
import com.expo.map.MapUtils;

import java.util.LinkedList;
import java.util.List;

public class TouristServicePresenterImpl extends TouristServiceContract.Presenter {
    public TouristServicePresenterImpl(TouristServiceContract.View view) {
        super( view );
    }

    @Override
    public String loadCommonUrlByType(String type) {
        CommonInfo info = mDao.unique( CommonInfo.class, new QueryParams().add( "eq", "type", type ) );
        if (info != null) {
            return info.getLinkUrl();
        }
        return null;
    }

    @Override
    public boolean checkInPark(Location location) {
        Park park = mDao.unique( Park.class, null );
        if (park == null) return false;
        List<double[]> bounds = park.getElectronicFenceList();
        return MapUtils.ptInPolygon( location.getLatitude(), location.getLongitude(), bounds );
    }

    @Override
    public String getParkTelePhone() {
        Park park = mDao.unique(Park.class, new QueryParams().add("eq", "_id", 1));
        if (null == park){
            return "";
        }else {
            return TextUtils.isEmpty(park.getTelephoneNumber()) ? "" : park.getTelephoneNumber();
        }
    }

//    @Override
//    public List<Venue> getNearbyServiceCenter(Location location) {
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
//                    venues.remove(result);
//                    venues.add(0, result);
//                    return venues;
//                }
//            }
//        }
//        return null;
//    }

}
