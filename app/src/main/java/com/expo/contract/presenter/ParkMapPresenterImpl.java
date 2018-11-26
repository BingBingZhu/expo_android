package com.expo.contract.presenter;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.blankj.utilcode.util.ZipUtils;
import com.expo.R;
import com.expo.adapters.DataTypeAdapter;
import com.expo.adapters.DownloadData;
import com.expo.adapters.Tab;
import com.expo.base.utils.FileUtils;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.ParkMapContract;
import com.expo.db.QueryParams;
import com.expo.entity.ActualScene;
import com.expo.entity.DataType;
import com.expo.entity.Encyclopedias;
import com.expo.entity.Park;
import com.expo.entity.RouteInfo;
import com.expo.entity.Subject;
import com.expo.entity.TouristType;
import com.expo.entity.VenuesType;
import com.expo.module.download.DownloadManager;
import com.expo.network.Http;
import com.expo.utils.Constants;
import com.expo.utils.MD5Util;

import java.io.File;
import java.io.IOException;
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
                Park park = mDao.queryById(Park.class, 1);
                if (park != null && park.getElectronicFenceList().size() >= 3) {
                    new Handler(Looper.getMainLooper())
                            .post(() -> {
                                mView.showParkScope(park);
                            });
                }
                List<RouteInfo> routeInfos = mDao.query(RouteInfo.class, new QueryParams().add("eq", "enable", 1));
                List<ActualScene> facilities = mDao.query(ActualScene.class, new QueryParams()
                        .add("eq", "is_enable", 1));
                if (null == facilities || facilities.isEmpty()) {
                    new Handler(Looper.getMainLooper())
                            .post(() -> {
                                ToastHelper.showShort(mView.getContext().getString(R.string.no_scene));
                                mView.hideLoadingView();
                            });
                    return;
                }
                List<VenuesType> venuesTypes = mDao.query(VenuesType.class, new QueryParams().add("eq", "is_enable", 1));
                List<TouristType> touristTypes = mDao.query(TouristType.class, new QueryParams().add("eq", "is_enable", 1));
                int tabPosition = 0;
                ActualScene as = mDao.queryById(ActualScene.class, asId);
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
    public List<ActualScene> getActualScenes(ArrayList<Integer> ids) {
        List<ActualScene> actualScenes = mDao.query(ActualScene.class, new QueryParams().add("in", "_id", ids));
        return actualScenes;
    }

    @Override
    public void useTouristType(TouristType type) {
//        try {
//            FileUtils.deleteFiles(new File(Constants.NaviTip.TO_JS_NAVI_FILE_PATH));
//            ZipUtils.unzipFile(type.getLocalPath(), Constants.NaviTip.TO_JS_NAVI_FILE_PATH);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
