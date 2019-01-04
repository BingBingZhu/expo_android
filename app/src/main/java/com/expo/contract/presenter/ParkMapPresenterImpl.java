package com.expo.contract.presenter;

import android.os.Handler;
import android.os.Looper;

import com.amap.api.maps.model.LatLng;
import com.expo.R;
import com.expo.adapters.DownloadData;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.ParkMapContract;
import com.expo.db.QueryParams;
import com.expo.entity.CustomRoute;
import com.expo.entity.Venue;
import com.expo.entity.Encyclopedias;
import com.expo.entity.Park;
import com.expo.entity.RouteInfo;
import com.expo.entity.TouristType;
import com.expo.entity.VenuesType;
import com.expo.map.MapUtils;
import com.expo.module.download.DownloadManager;
import com.expo.network.Http;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;

import java.util.ArrayList;
import java.util.List;

public class ParkMapPresenterImpl extends ParkMapContract.Presenter {
    public ParkMapPresenterImpl(ParkMapContract.View view) {
        super(view);
    }

    @Override
    public void loadParkMapData(Long asId) {
        mView.showLoadingView();
        new Thread() {
            @Override
            public void run() {
                Park park = mDao.unique( Park.class, null );
                if (park != null && park.getElectronicFenceList().size() >= 3) {
                    new Handler(Looper.getMainLooper())
                            .post(() -> {
                                mView.showParkScope(park);
                            });
                }
                List<RouteInfo> routeInfos = mDao.query(RouteInfo.class, new QueryParams().add("eq", "enable", 1));
                List<Venue> facilities = mDao.query(Venue.class, new QueryParams()
                        .add("eq", "is_enable", 1));
                if (null == facilities || facilities.isEmpty()) {
                    new Handler(Looper.getMainLooper())
                            .post(() -> {
                                ToastHelper.showShort(mView.getContext().getString(R.string.no_scene));
                                mView.hideLoadingView();
                            });
                    return;
                }
                List<VenuesType> venuesTypes = mDao.query(VenuesType.class, new QueryParams()
                        .add("eq", "is_enable", 1)
                        .add("orderBy", "idx", true));
                List<TouristType> touristTypes = mDao.query(TouristType.class, new QueryParams().add("eq", "is_enable", 1));
                int tabPosition = 0;
                Venue as = mDao.queryById(Venue.class, asId);
                List<CustomRoute> customRoutes = mDao.query(CustomRoute.class, new QueryParams());
                if (asId != 0 && null != as) {
                    for (int i = 0; i < venuesTypes.size(); i++) {
                        if (venuesTypes.get(i).getId() == as.getType()) {
                            tabPosition = i;
                        }
                    }
                }
                int finalTabPosition = tabPosition;
                new Handler(Looper.getMainLooper())
                        .post(() -> {
                            mView.loadCustomRoute(customRoutes);
                            mView.loadTabRes(venuesTypes, finalTabPosition);
                            mView.loadFacilityRes(facilities, as);
                            mView.loadTouristTypeRes(touristTypes);
                            mView.loadRoute(routeInfos);
                            mView.hideLoadingView();
                            loadSubjectImages(venuesTypes);
                        });
            }
        }.start();
    }

    @Override
    public Encyclopedias getEncy(String wikiId) {
        return mDao.queryById(Encyclopedias.class, wikiId);
    }

    @Override
    public List<Venue> getActualScenes(ArrayList<Integer> ids) {
        List<Venue> venues = mDao.query(Venue.class,new QueryParams().add("in","_id",ids));
        return venues;
    }

    @Override
    public List<Venue> selectVenueByCaption(String caption) {
        VenuesType venuesType = mDao.unique(VenuesType.class, new QueryParams()
                .add("eq", "type_name", "景点")
                .add("and")
                .add("eq", "is_enable", 1));

        QueryParams queryParams = new QueryParams();
        if (LanguageUtil.isCN()){
            queryParams.add("eq", "type", venuesType.getId()).add("and")
                    .add("eq", "is_enable", 1).add("and")
                    .add("like", "caption", "%"+caption+"%");
        }else{
            queryParams.add("eq", "type", venuesType.getId()).add("and")
                    .add("eq", "is_enable", 1).add("and")
                    .add("like", "caption_en", "%"+caption+"%");
        }
        List<Venue> venues = mDao.query( Venue.class, queryParams );
        return venues;
    }

    @Override
    public boolean checkInVenue(LatLng latLng, Venue venue) {
        if (null == latLng || latLng.latitude == 0)
            return false;
        List<double[]> bounds = venue.getElectronicFenceList();
        return MapUtils.ptInPolygon( latLng.latitude, latLng.longitude, bounds );
    }

    private void loadSubjectImages(List<VenuesType> venuesTypes) {
        for (VenuesType vt : venuesTypes) {
            Http.loadBitmap(Constants.URL.FILE_BASE_URL + vt.getPicLstUrl(), mOnLoadImageCompleteListener, vt);
            Http.loadBitmap(Constants.URL.FILE_BASE_URL + vt.getPicMarkUrl(), mOnLoadImageCompleteListener, vt);
        }
    }

    private Http.OnLoadImageCompleteListener mOnLoadImageCompleteListener = (url, bmp, obj) -> {
        if (!(obj instanceof VenuesType)) return;
        VenuesType vt = (VenuesType) obj;
        if (url.endsWith(vt.getPicLstUrl())) {
            vt.setLstBitmap(bmp);
        } else if (url.endsWith(vt.getPicMarkUrl())) {
            vt.setMarkBitmap(bmp);
        }
        if (vt.picIsFinished()) {
            mView.updatePic(vt);
        }
    };

    @Override
    public void startDownloadTask(DownloadData tourist) {
        DownloadManager.getInstance().addTask(tourist);
    }

    @Override
    public void stopDownloadTask(DownloadData tourist) {
        DownloadManager.getInstance().removeTask(tourist);
    }

    private DownloadManager.DownloadListener mDownloadListener = new DownloadManager.DownloadListener() {
        @Override
        public void onProgressUpdate(DownloadData info) {
            mView.updateItemProgress(info);
        }

        @Override
        public void onStatusChanged(DownloadData info) {
            mView.updateItemStatus(info);
        }
    };


    @Override
    public void registerDownloadListener() {
        DownloadManager.getInstance().addDownloadUpdateListener(mDownloadListener);
    }

    @Override
    public void unregisterDownloadListener() {
        DownloadManager.getInstance().removeDownloadListener(mDownloadListener);
    }

    @Override
    public void setData(List<TouristType> mTouristTypes) {
        DownloadManager.getInstance().setData(mTouristTypes);
    }

    @Override
    public void saveUsed(List<TouristType> mTouristTypes) {
        mDao.saveOrUpdateAll(mTouristTypes);
    }
}
