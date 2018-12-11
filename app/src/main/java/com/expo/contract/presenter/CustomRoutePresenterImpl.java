package com.expo.contract.presenter;

import android.os.Handler;
import android.os.Looper;

import com.amap.api.maps.model.LatLng;
import com.expo.R;
import com.expo.base.utils.PrefsHelper;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.CustomRouteContract;
import com.expo.db.QueryParams;
import com.expo.entity.CustomRoute;
import com.expo.entity.Park;
import com.expo.entity.RouteInfo;
import com.expo.entity.TouristType;
import com.expo.entity.Venue;
import com.expo.entity.VenuesType;
import com.expo.network.Http;
import com.expo.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomRoutePresenterImpl extends CustomRouteContract.Presenter {

    private List<Venue> facilities;

    public CustomRoutePresenterImpl(CustomRouteContract.View view) {
        super(view);
    }

    @Override
    public void loadParkMapData() {
        mView.showLoadingView();
        new Thread() {
            @Override
            public void run() {
                Park park = mDao.queryById(Park.class, 1);
                if (park != null && park.getElectronicFenceList().size() >= 3) {
                    new Handler(Looper.getMainLooper())
                            .post(() -> {
                                mView.showParkScope(park);
                            });
                }
                List<VenuesType> venuesTypes = mDao.query(VenuesType.class, new QueryParams()
                        .add("eq", "type_name", "景点").add("or")
                        .add("eq", "type_name", "场馆"));
                facilities = new ArrayList<>();
                for (VenuesType vt : venuesTypes){
                    List<Venue> venues = mDao.query(Venue.class, new QueryParams()
                            .add("eq", "is_enable", 1).add("and")
                            .add("eq", "type", vt.getId()));
                    facilities.addAll(null == venues ? new ArrayList<Venue>() : venues);
                }
                Collections.sort(facilities);
                if (null == facilities || facilities.isEmpty()) {
                    new Handler(Looper.getMainLooper())
                            .post(() -> {
                                ToastHelper.showShort(mView.getContext().getString(R.string.no_scene));
                                mView.hideLoadingView();
                            });
                    return;
                }
                List<CustomRoute> customRoutes = mDao.query(CustomRoute.class, new QueryParams());
                new Handler(Looper.getMainLooper())
                        .post(() -> {
                            mView.loadFacilityRes(facilities);
                            mView.loadCustomRoute(customRoutes);
                            mView.hideLoadingView();
                            loadSubjectImages(venuesTypes);
                        });
            }
        }.start();
    }

    private void loadSubjectImages(List<VenuesType> venuesTypes) {
        for (VenuesType vt : venuesTypes) {
            Http.loadBitmap(Constants.URL.FILE_BASE_URL + vt.getPicMarkUrl(), mOnLoadImageCompleteListener, vt);
        }
    }

    private Http.OnLoadImageCompleteListener mOnLoadImageCompleteListener = (url, bmp, obj) -> {
        if (!(obj instanceof VenuesType)) return;
        VenuesType vt = (VenuesType) obj;
        if (url.endsWith(vt.getPicMarkUrl())) {
            vt.setMarkBitmap(bmp);
        }
        if (vt.mapPicIsFinished()) {
            mView.updateMarkerPic(vt);
        }
    };

    @Override
    public void clearCustomRoute() {
        for (Venue venue : facilities){
            venue.setSelected(false);
            venue.setRouteIndex(0);
        }
        mDao.saveOrUpdateAll(facilities);
        PrefsHelper.setLong(Constants.Prefs.KEY_ROUTE_INDEX, 0L);
        mDao.clear(CustomRoute.class);
    }

    @Override
    public void saveCustomRoute(List<Venue> venues, List<CustomRoute> customRoutes) {
        mDao.saveOrUpdateAll(venues);
        mDao.clear(CustomRoute.class);
        mDao.saveOrUpdateAll(customRoutes);
        ToastHelper.showLong("路线保存成功");
    }
}
