package com.expo.contract.presenter;

import android.os.Handler;
import android.os.Looper;

import com.expo.contract.OnlineHomeContract;
import com.expo.db.QueryParams;
import com.expo.entity.TouristType;
import com.expo.entity.Tuple;
import com.expo.entity.VrInfo;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.PanResHotResp;
import com.expo.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;

public class OnlineHomePresenterImpl extends OnlineHomeContract.Presenter {

    private final static String TOP_KIND_LIVE = "1";
    private final static String TOP_KIND_CULTURE = "2";
    private final static String TOP_KIND_TOUR = "3";
    private static final int PER_PAGE_COUNT = 4;

    public OnlineHomePresenterImpl(OnlineHomeContract.View view) {
        super( view );
    }

    @Override
    public void loadVrHot() {
        RequestBody requestBody = Http.buildRequestBody( Http.getBaseParams() );
        Observable<PanResHotResp> panResHotRespObservable = Http.getServer().getPanResHot( requestBody );
        Http.request( new ResponseCallback<PanResHotResp>() {
            @Override
            protected void onResponse(PanResHotResp rsp) {
                List<VrInfo> vrInfos = mDao.query( VrInfo.class, null );
                if (vrInfos == null) return;
                for (Tuple t : rsp.tupleList) {
                    for (VrInfo v : vrInfos) {
                        if (v.getId() == t.getElement()) {
                            v.setViewCount( t.getScore() );
                        }
                    }
                }
                loadData();
            }
        }, panResHotRespObservable );
    }

    @Override
    public void loadData() {
        new Thread() {
            @Override
            public void run() {
                List<VrInfo> liveVrs = mDao.query( VrInfo.class, new QueryParams()
                        .add( "eq", "top_kind", TOP_KIND_LIVE )
                        .add("and")
                        .add("eq", "is_recommended", 1)
                        .add("orderBy", "recommended_idx", true));
                List<VrInfo> cultureVrs = mDao.query( VrInfo.class, new QueryParams()
                        .add( "eq", "top_kind", TOP_KIND_CULTURE )
                        .add("and")
                        .add("eq", "is_recommended", 1) );
                List<VrInfo> tourVrs = mDao.query( VrInfo.class, new QueryParams()
                        .add( "eq", "top_kind", TOP_KIND_TOUR )
                        .add("and")
                        .add("eq", "is_recommended", 1)
                        .add("orderBy", "recommended_idx", true)
                        .add( "limit", 0 * PER_PAGE_COUNT, PER_PAGE_COUNT ) );
                new Handler( Looper.getMainLooper() )
                        .post( () -> {
                            mView.loadLiveDataRes( null == liveVrs ? new ArrayList<VrInfo>() : liveVrs );
                            mView.loadCultureDataRes( null == cultureVrs ? new ArrayList<VrInfo>() : cultureVrs );
                            mView.loadTourDataRes( null == tourVrs ? new ArrayList<VrInfo>() : tourVrs );
                        } );
            }
        }.start();
    }

    @Override
    public void loadMore(int page) {
        List<VrInfo> tourVrs = mDao.query( VrInfo.class, new QueryParams()
                .add( "eq", "top_kind", TOP_KIND_TOUR )
                .add( "limit", page * PER_PAGE_COUNT, PER_PAGE_COUNT ) );
        mView.loadMoreTourDataRes( null == tourVrs ? new ArrayList<VrInfo>() : tourVrs );
    }

    @Override
    public void loadTouristType() {
        List<TouristType> touristTypes = mDao.query(TouristType.class, new QueryParams().add("eq", "is_enable", 1));
        for (TouristType touristType : touristTypes) {
            if (touristType.isUsed()) {
                mView.loadTouristTypeRes(touristType);
            }
        }

    }
}
