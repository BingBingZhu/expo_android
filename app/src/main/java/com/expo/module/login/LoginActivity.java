package com.expo.module.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.StringUtils;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.utils.PrefsHelper;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.LoginContract;
import com.expo.entity.CommonInfo;
import com.expo.module.main.MainActivity;
import com.expo.module.webview.WebActivity;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.expo.utils.LocalBroadcastUtil;
import com.sahooz.library.Country;
import com.sahooz.library.PhoneNumberLibUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/*
 * 登录页
 */
public class LoginActivity extends BaseActivity<LoginContract.Presenter> implements View.OnClickListener, LoginContract.View {

    @BindView(R.id.login_phone_number_code)
    TextView mTvPhoneCode;
    @BindView(R.id.login_phone_number_et)
    EditText mEtPhoneView;
    @BindView(R.id.login_ver_code_et)
    EditText mEtCodeView;
    @BindView(R.id.login_get_ver_code)
    TextView mGetCodeView;
    @BindView(R.id.login_login_btn)
    TextView mLoginView;
    @BindView(R.id.login_protocol_tv)
    TextView mProtocolView;    // 服务协议
    @BindView(R.id.login_wechat)
    ImageView mWechatView;
    @BindView(R.id.login_qq)
    ImageView mQQView;
    @BindView(R.id.login_microblog)
    ImageView mSinaView;     // 微博登录

    private int mDuration = 60;
    private int mSurplusDuration;
    private boolean mCountDownOver = true;  // 倒计时是否结束
    private TimeCount mTimeCount;    //验证码获取计时器
    private String mMobile;
    private String mSecurityCode;
    private int mCode = 86;    //手机号区域码

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        //不具有返回上级页面功能时可双击返回键退出
        setDoubleTapToExit(true);
        mEtPhoneView.addTextChangedListener(mTextWatcher);
        mEtCodeView.addTextChangedListener(mTextWatcher);
        mTvPhoneCode.setOnClickListener(this);
        mGetCodeView.setOnClickListener(this);
        mLoginView.setOnClickListener(this);
        mWechatView.setOnClickListener(this);
        mQQView.setOnClickListener(this);
        mSinaView.setOnClickListener(this);
        mProtocolView.setOnClickListener(this);
        checkPermission();
    }

    private void setCodeBtnIsEnable() {
        if (TextUtils.isEmpty(mMobile)) {
            mGetCodeView.setEnabled(false);
            return;
        }
        mGetCodeView.setEnabled(mCountDownOver && PhoneNumberLibUtil.checkPhoneNumber(getContext(), mCode, Long.valueOf(mMobile)));
    }

    private void setLoginBtnIsEnable() {
        if (TextUtils.isEmpty(mMobile)) {
            mLoginView.setEnabled(false);
            return;
        }
        mLoginView.setEnabled(null != mTimeCount && PhoneNumberLibUtil.checkPhoneNumber(getContext(), mCode, Long.valueOf(mMobile)) &&
                mSecurityCode.length() == 4);
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mEtPhoneView.isFocused()) {     // 手机号输入框获取焦点
                //  验证手机号
                mMobile = s.toString();
                setCodeBtnIsEnable();
            } else {      // 验证码输入框获取焦点
                mSecurityCode = s.toString();
            }
            setLoginBtnIsEnable();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public static void startActivity(Context context) {
        Intent in = new Intent(context, LoginActivity.class);
        if (PrefsHelper.getString(Constants.Prefs.KEY_LANGUAGE_CHOOSE, null) != null) {
            LanguageUtil.changeAppLanguage(context, PrefsHelper.getString(Constants.Prefs.KEY_LANGUAGE_CHOOSE, null));
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        context.startActivity(in);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_phone_number_code:
                startActivityForResult(new Intent(getApplicationContext(), NationalSmsCodeActivity.class), 111);
                break;
            case R.id.login_get_ver_code:
                //  验证手机号
                if (StringUtils.isEmpty(mEtPhoneView.getText().toString())) {
                    ToastHelper.showShort(R.string.input_correct_phone);
                    return;
                }
                //  验证手机号
                if (!PhoneNumberLibUtil.checkPhoneNumber(this, mCode, Long.valueOf(mEtPhoneView.getText().toString()))) {
                    ToastHelper.showShort(R.string.input_correct_phone);
                    return;
                }
                mPresenter.getCode(mEtPhoneView.getText().toString().trim(), "+" + mCode, null);
                mSurplusDuration = mDuration;
                mTimeCount = new TimeCount((mDuration + 1) * 1000, 1000);
                mTimeCount.start();
                break;
            case R.id.login_login_btn:
                // 使用验证码登录
                mPresenter.verifyCodeLogin(mEtPhoneView.getText().toString().trim(),
                        mTvPhoneCode.getText().toString(),
                        mEtCodeView.getText().toString().trim());
                break;
            case R.id.login_wechat:
                // 第三方微信登录
                mPresenter.threeLogin(Wechat.NAME);
                break;
            case R.id.login_qq:
                // 第三方QQ登录
                mPresenter.threeLogin(QQ.NAME);
                break;
            case R.id.login_microblog:
                // 新浪微博登录
                mPresenter.threeLogin(SinaWeibo.NAME);
                break;
            case R.id.login_protocol_tv:
                mPresenter.loadUserProtocol();
                break;
        }
    }

    @OnClick(R.id.to_register)
    public void clickToLogin(View view) {
        RegisterActivity.startActivity(this);
    }

    @Override
    public void toUserProtocol(CommonInfo commonInfo) {
        if (null == commonInfo || TextUtils.isEmpty(commonInfo.getLinkUrl())) {
            ToastHelper.showLong(R.string.there_is_no_user_protocol);
            return;
        }
        WebActivity.startActivity(getContext(), commonInfo.getLinkUrl(),
                LanguageUtil.isCN() ? commonInfo.getCaption() : commonInfo.getCaptionEn());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK) {
            Country country = Country.fromJson(data.getStringExtra("country"));
            mEtPhoneView.getText().clear();
            mEtCodeView.getText().clear();
            mTvPhoneCode.setText("+" + country.code);
            mCode = country.code;
        }
    }

    /**
     * 返回验证码
     *
     * @param code
     */
    @Override
    public void returnRequestVerifyCodeResult(String code) {
        mEtCodeView.requestFocus();
        mEtCodeView.setText("");
        mEtCodeView.append(code);
        setLoginBtnIsEnable();
    }

    /**
     * 返回验证码登录结果
     */
    @Override
    public void verifyCodeLogin() {
        Intent intent = new Intent();
        intent.putExtra(Constants.EXTRAS.EXTRA_LOGIN_STATE, true);
        LocalBroadcastUtil.sendBroadcast(LoginActivity.this, intent, Constants.Action.LOGIN_CHANGE_OF_STATE_ACTION);
        // 登录成功
        MainActivity.startActivity(this);
        finish();
    }

    @Override
    public void toBindPhone(String platform) {
        BindPhoneActivity.startActivity(this, platform);
    }

    @Override
    public void showShort(int resId) {
        ToastHelper.showShort(resId);
    }

    @Override
    protected void onDestroy() {
        Country.destroy();
        super.onDestroy();
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            mCountDownOver = true;
            setCodeBtnIsEnable();
            mGetCodeView.setText(getContext().getResources().getString(R.string.get_code));
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            if (mSurplusDuration > 0) {
                mSurplusDuration--;
            }
            mGetCodeView.setText(mSurplusDuration + getContext().getResources().getString(R.string.second));

            mCountDownOver = false;
            setCodeBtnIsEnable();
        }
    }

    /*
     * 检查权限并弹框提示获取权限
     */
    private void checkPermission() {
        List<String> permissionList = new ArrayList<>();
        List<String> perNameList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            perNameList.add(getString(R.string.store_permissions));
        }
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_NETWORK_STATE);
            perNameList.add(getString(R.string.change_network_permissions));
        }
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.INTERNET);
            perNameList.add(getString(R.string.network_access_permissions));
        }
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
            perNameList.add(getString(R.string.camera_permissions));
        }
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.RECORD_AUDIO);
            perNameList.add(getString(R.string.the_recording_permissions));
        }
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_WIFI_STATE);
            perNameList.add(getString(R.string.get_wifi_information_permission));
        }
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
            perNameList.add(getString(R.string.phone_privileges));
        }
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            perNameList.add(getString(R.string.location_permissions));
        }
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.CHANGE_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CHANGE_WIFI_STATE);
            perNameList.add(getString(R.string.get_wifi_state_permission));
        }
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.BLUETOOTH_ADMIN)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.BLUETOOTH_ADMIN);
            perNameList.add(getString(R.string.bluetooth_permissions));
        }
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.GET_ACCOUNTS);
            perNameList.add(getString(R.string.address_book_permissions));
        }
        if (permissionList.size() > 0) {
            //弹出权限请求提示对话框
            int size = permissionList.size();
            String[] permissions = new String[size];
            for (int i = 0; i < size; i++) {
                permissions[i] = permissionList.get(i);
            }
            PermissionUtils.permission(permissions.clone()).request();

        }
    }
}
