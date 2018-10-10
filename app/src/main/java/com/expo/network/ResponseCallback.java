package com.expo.network;

import com.expo.R;
import com.expo.base.utils.LogUtils;
import com.expo.base.utils.ToastHelper;
import com.expo.network.response.BaseResponse;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.InputMismatchException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class ResponseCallback<T extends BaseResponse> implements Observer<T> {

    private static final String TAG = "ResponseCallback";
    protected Disposable mDisposable;

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
    }

    @Override
    public void onNext(T t) {
        int resId = -1;
        switch (t.code) {
            case BaseResponse.ERROR_NO_ERROR:
                onResponse( t );
                break;
            case BaseResponse.ERROR_ACCOUNT_UNENABLE:
                resId = R.string.account_unable;
                break;
            case BaseResponse.ERROR_CODE_TIMES_LIMIT:
                resId = R.string.code_times_limit;
                break;
            case BaseResponse.ERROR_DATA_UNEXISTSD:
                resId = R.string.data_unexistsd;
                break;
            case BaseResponse.ERROR_VERIFICATION_CODE_ERROR:
                resId = R.string.code_error;
                break;
            case BaseResponse.ERROR_EXP_ERROR:
                resId = R.string.server_error;
                break;
            case BaseResponse.ERROR_EXP_VERIFICATION_CODE:
                resId = R.string.invalid_code;
                break;
            case BaseResponse.ERROR_ID_NOT_EXIST:
                resId = R.string.id_not_exist;
                break;
            case BaseResponse.ERROR_NO_REG:
                resId = R.string.no_reg;
                break;
            case BaseResponse.ERROR_NO_REQ_VERIFICATION_CODE:
                resId = R.string.no_req_code;
                break;
            case BaseResponse.ERROR_RELOGIN:
                resId = R.string.relogin;
                break;
            case BaseResponse.ERROR_REQUEST_JSON:
                String errorInfo = t.msg.trim();
                if (errorInfo.startsWith( "Mobile Error" )) {
                    resId = R.string.input_correct_phone;
                } else {
                    resId = R.string.req_json_error;
                }
                break;
            case BaseResponse.ERROR_SMS_ERROR:
                resId = R.string.data_unexistsd;
                break;
            case BaseResponse.ERROR_UID_UNLOGIN:
                resId = R.string.unlogin;
                break;
            case BaseResponse.ERROR_UKEY_ERROR:
                resId = R.string.ukey_error;
                break;
            default:
                onError( new InputMismatchException( String.format( "Code [%d] mismatched,msg:%s", t.code, t.msg ) ) );
                return;
        }
        if (resId != -1) {
            ToastHelper.showShort( resId );
        }
    }

    @Override
    public void onError(Throwable e) {
        Type superclass = this.getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException( "Missing type parameter." );
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        String type = parameterized.getActualTypeArguments()[0].toString();
        LogUtils.d( TAG, "Error----->>>>>>>>" + type, e );
        onComplete();
    }

    @Override
    public void onComplete() {
    }

    protected abstract void onResponse(T rsp);
}
