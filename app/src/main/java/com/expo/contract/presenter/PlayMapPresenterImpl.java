package com.expo.contract.presenter;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.expo.contract.PlayMapContract;
import com.expo.db.QueryParams;
import com.expo.entity.Venue;
import com.expo.entity.VenuesType;
import com.expo.network.Http;
import com.expo.services.TrackRecordService;
import com.expo.utils.Constants;

import java.util.List;

public class PlayMapPresenterImpl extends PlayMapContract.Presenter {

    public PlayMapPresenterImpl(PlayMapContract.View view) {
        super(view);
    }

    @Override
    public void queryVenueAndNearByType(String typeName) {
        VenuesType venuesType = mDao.unique(VenuesType.class, new QueryParams()
                .add("eq", "is_enable", 1)
                .add("and")
                .add("like", "type_name", "%" + typeName + "%"));
        if (venuesType != null) {
            List<Venue> venues = mDao.query(Venue.class, new QueryParams()
                    .add("eq", "type", String.valueOf(venuesType.getId())));
            if (venues != null) {
                if (venues.size() > 1) {
                    float minDistance = Float.MAX_VALUE;
                    Venue result = null;
                    LatLng loc = new LatLng(TrackRecordService.getLocation().getLatitude(), TrackRecordService.getLocation().getLongitude());
                    for (Venue venue : venues) {
                        float distance = AMapUtils.calculateLineDistance(loc, new LatLng(venue.getLat(), venue.getLng()));
                        if (distance < minDistance) {
                            minDistance = distance;
                            result = venue;
                        }
                    }
                    venues.remove(result);
                    venues.add(0, result);
                    mView.queryVenueRes(venues);
                    loadSubjectImages(venuesType);
                    return;
                }
            }
        }
        mView.queryVenueRes(null);
    }

    @Override
    public void queryVenuesByVenue(Venue venue) {
        VenuesType venuesType = mDao.unique(VenuesType.class, new QueryParams()
                .add("eq", "is_enable", 1)
                .add("and")
                .add("eq", "id", venue.getType() ));
        if (venuesType != null) {
            List<Venue> venues = mDao.query(Venue.class, new QueryParams()
                    .add("eq", "type", String.valueOf(venuesType.getId())));
            if (venues != null) {
                if (venues.size() > 1) {
                    venues.remove(venue);
                    venues.add(0, venue);
                    mView.queryVenueRes(venues);
                    loadSubjectImages(venuesType);
                    return;
                }
            }
        }
        mView.queryVenueRes(null);
    }

    private void loadSubjectImages(VenuesType venuesType) {
        Http.loadBitmap(Constants.URL.FILE_BASE_URL + venuesType.getPicMarkUrl(), mOnLoadImageCompleteListener, venuesType);
    }

    private Http.OnLoadImageCompleteListener mOnLoadImageCompleteListener = (url, bmp, obj) -> {
        if (!(obj instanceof VenuesType)) return;
        mView.updateMarkerPic(bmp);
    };
}
