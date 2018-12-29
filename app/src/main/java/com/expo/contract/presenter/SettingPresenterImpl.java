package com.expo.contract.presenter;

import android.content.Context;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.StringUtils;
import com.expo.R;
import com.expo.base.utils.ToastHelper;
import com.expo.upapp.UpdateAppManager;
import com.expo.contract.SettingContract;
import com.expo.db.QueryParams;
import com.expo.entity.AppInfo;
import com.expo.entity.CommonInfo;
import com.expo.entity.User;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.BaseResponse;
import com.expo.network.response.VersionInfoResp;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

public class SettingPresenterImpl extends SettingContract.Presenter {
    public SettingPresenterImpl(SettingContract.View view) {
        super( view );
    }

    @Override
    public void clearCache() {

    }

    @Override
    public void checkUpdate() {
        mView.showLoadingView();
        Map<String, Object> params = Http.getBaseParams();
        RequestBody requestBody = Http.buildRequestBody( params );
        Observable<VersionInfoResp> observable = Http.getServer().getAppUpdateInfo( requestBody );
        Http.request( new ResponseCallback<VersionInfoResp>() {
            @Override
            protected void onResponse(VersionInfoResp rsp) {
                AppInfo appInfo = UpdateAppManager.getInstance().isHaveUpdate(AppUtils.getAppVersionName(), rsp.Objlst);
                if (null == appInfo) {
                    ToastHelper.showShort( R.string.latest_app_version );
                } else {
                    mView.appUpdate( appInfo );
                }
            }

            @Override
            public void onComplete() {
                mView.hideLoadingView();
                super.onComplete();
            }
        }, observable );
    }

    @Override
    public void update(Context context, AppInfo appInfo) {
        //调用下载功能
        UpdateAppManager.getInstance( context ).showNoticeDialog( appInfo, false );
    }

    @Override
    public void logout() {
        User user = mDao.unique( User.class, null );
        mView.showLoadingView();
        Map<String, Object> params = Http.getBaseParams();
        params.put( "Uid", user.getUid() );
        params.put( "Ukey", user.getUkey() );
        RequestBody requestBody = Http.buildRequestBody( params );
        Observable<BaseResponse> observable = Http.getServer().userlogout( requestBody );
        Http.request( new ResponseCallback<BaseResponse>() {
            @Override
            protected void onResponse(BaseResponse rsp) {
            }

            @Override
            public void onComplete() {
                mView.hideLoadingView();
                user.deleteOnDb( mDao );
                mView.logout();
            }
        }, observable );
    }

    @Override
    public void clickPolicy(String type) {
        CommonInfo commonInfo = mDao.unique( CommonInfo.class, new QueryParams().add( "eq", "type", type ) );
        mView.returnCommonInfo( commonInfo );
    }
}
