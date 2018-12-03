package com.expo.map;

import com.amap.api.maps.model.LatLng;
import com.expo.entity.Venue;
import com.expo.entity.VenuesType;

/**
 * Created by yiyi.qi on 16/10/10.
 */

public class RegionItem implements ClusterItem {
    private LatLng mLatLng;
    private String mTitle;
    public VenuesType venuesType;
    public Venue actualScene;

    public RegionItem(LatLng latLng, String title) {
        mLatLng = latLng;
        mTitle = title;
    }

    @Override
    public LatLng getPosition() {
        // TODO Auto-generated method stub
        return mLatLng;
    }

    public String getTitle() {
        return mTitle;
    }

}
