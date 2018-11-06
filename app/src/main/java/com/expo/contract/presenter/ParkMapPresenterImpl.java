package com.expo.contract.presenter;

import com.expo.adapters.DataTypeAdapter;
import com.expo.adapters.DownloadData;
import com.expo.adapters.Tab;
import com.expo.contract.ParkMapContract;
import com.expo.db.QueryParams;
import com.expo.entity.ActualScene;
import com.expo.entity.DataType;
import com.expo.entity.TouristType;
import com.expo.module.download.DownloadManager;

import java.util.List;

public class ParkMapPresenterImpl extends ParkMapContract.Presenter {
    public ParkMapPresenterImpl(ParkMapContract.View view) {
        super( view );
    }

    @Override
    public void loadTab() {
        List<DataType> dataTypes = mDao.query(DataType.class, new QueryParams()
                .add("eq", "kind", 2)
                .add("and")
                .add("eq", "enabled", 1));
        List<Tab> tabs = DataTypeAdapter.convertToTabList(dataTypes);
        mView.loadTabRes(tabs);
    }

    @Override
    public void loadFacility() {
        List<ActualScene> facilities = mDao.query(ActualScene.class, new QueryParams()
            .add("eq", "is_enable", 1));
        mView.loadFacilityRes(facilities);
    }

    @Override
    public void loadTouristType() {
        List<TouristType> touristTypes = mDao.query(TouristType.class, null);
        mView.loadTouristTypeRes(touristTypes);
    }

    @Override
    public void startDownloadTask(DownloadData tourist) {
        DownloadManager.getInstance().addTask(tourist);
    }

    @Override
    public void stopDownloadTask(DownloadData tourist) {
        DownloadManager.getInstance().removeTask( tourist );
    }

    private DownloadManager.DownloadListener mDownloadListener = new DownloadManager.DownloadListener() {
        @Override
        public void onProgressUpdate(DownloadData info) {
            mView.updateItemProgress( info );
        }

        @Override
        public void onStatusChanged(DownloadData info) {
            mView.updateItemStatus( info );
        }
    };


    @Override
    public void registerDownloadListener() {
        DownloadManager.getInstance().addDownloadUpdateListener( mDownloadListener );
    }

    @Override
    public void unregisterDownloadListener() {
        DownloadManager.getInstance().removeDownloadListener( mDownloadListener );
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
