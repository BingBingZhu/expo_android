package com.expo.contract.presenter;

import com.expo.base.ExpoApp;
import com.expo.base.utils.LogUtils;
import com.expo.contract.MineContract;
import com.expo.db.QueryParams;
import com.expo.entity.CommonInfo;
import com.expo.entity.User;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;

import io.reactivex.Observable;
import okhttp3.RequestBody;

public class MinePresenterImpl extends MineContract.Presenter {
    public MinePresenterImpl(MineContract.View view) {
        super( view );
    }

    @Override
    public void loadUser() {
        RequestBody requestBody = Http.buildRequestBody( Http.getBaseParams() );
        Observable<User> verifyCodeLoginObservable = Http.getServer().loadUserInfo( requestBody );
        Http.request( new ResponseCallback<User>() {
            @Override
            protected void onResponse(User rsp) {
                rsp.setUid( ExpoApp.getApplication().getUser().getUid() );
                rsp.setUkey( ExpoApp.getApplication().getUser().getUkey() );
                LogUtils.e( "========user", "id:" + ExpoApp.getApplication().getUser().getUid() + " key:" + ExpoApp.getApplication().getUser().getUkey() );
                rsp.saveOnDb( mDao );
                mView.freshUser( rsp );
            }

            @Override
            public void onComplete() {
                super.onComplete();
                mView.freshUser( mDao.unique( User.class, null ) );
            }
        }, verifyCodeLoginObservable );
    }

    @Override
    public String loadCommonInfo(String type) {
        CommonInfo commonInfo = mDao.unique( CommonInfo.class, new QueryParams().add( "eq", "type", type ) );
        if (commonInfo != null)
            return commonInfo.getLinkUrl();
        return null;
    }
}
