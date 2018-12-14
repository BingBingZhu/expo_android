package com.expo.contract.presenter;

import com.expo.base.ExpoApp;
import com.expo.contract.ServiceHistoryContract;
import com.expo.db.QueryParams;
import com.expo.entity.CommonInfo;
import com.expo.entity.VisitorService;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.VisitorServiceResp;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public class ServiceHistoryPresenterImpl extends ServiceHistoryContract.Presenter {

    private static final int PER_PAGE_COUNT = 5;

    public ServiceHistoryPresenterImpl(ServiceHistoryContract.View view) {
        super( view );
    }

    @Override
    public void loadMoreData(int page) {
        List<VisitorService> data = mDao.query( VisitorService.class, new QueryParams()
                .add( "eq", "user_id", ExpoApp.getApplication().getUser().getUid() )
                .add( "limit", page * PER_PAGE_COUNT, PER_PAGE_COUNT )
                .add( "orderBy", "update_time", "desc" ) );
        mView.addDataToList( data );
    }

    @Override
    public void loadAllData() {
        Map<String, Object> params = Http.getBaseParams();
        Observable<VisitorServiceResp> verifyCodeLoginObservable = Http.getServer().findVisitorServiceList( Http.buildRequestBody( params ) );
        Http.request( new ResponseCallback<VisitorServiceResp>() {
            @Override
            protected void onResponse(VisitorServiceResp rsp) {
                mDao.clear( VisitorService.class );
                mDao.saveOrUpdateAll( rsp.vsList );
                mView.loadDataRes( true );
            }

            @Override
            public void onError(Throwable e) {
                super.onError( e );
                mView.loadDataRes( false );
            }
        }, verifyCodeLoginObservable );
    }


    @Override
    public String loadCommonInfo(String type) {
        CommonInfo info = mDao.unique( CommonInfo.class, new QueryParams()
                .add( "eq", "type", type ) );
        if (info != null) {
            return info.getLinkUrl();
        }
        return null;
    }
}
