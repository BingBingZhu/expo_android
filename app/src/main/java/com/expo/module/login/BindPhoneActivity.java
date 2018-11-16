package com.expo.module.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.BindPhoneContract;
import com.expo.entity.User;
import com.expo.module.main.MainActivity;
import com.expo.network.response.VerifyCodeLoginResp;
import com.expo.utils.Constants;
import com.expo.utils.LocalBroadcastUtil;
import com.sahooz.library.Country;
import com.sahooz.library.PhoneNumberLibUtil;

import butterknife.BindView;

/*
 * 第三方登录后绑定手机号
 */
public class BindPhoneActivity extends BaseActivity<BindPhoneContract.Presenter> implements View.OnClickListener, BindPhoneContract.View {

    @BindView(R.id.login_phone_number_et)
    EditText mEtPhoneNum;
    @BindView(R.id.login_phone_number_code)
    TextView mTvPhoneCode;
    @BindView(R.id.login_ver_code_et)
    EditText mEtVerCode;
    @BindView(R.id.login_get_ver_code)
    TextView mTvGetCode;
    @BindView(R.id.login_login_btn)
    Button mBtnLogin;

//    private boolean getCodeEnable = true;  // 是否允许获取验证码按钮可用
    private boolean mCountDownOver = true;
    private String mMobile;
    private TimeCount timeCount;    //验证码获取计时器
    private int mCode = 86;    //手机号区域码
    private String mPlatform;

    @Override
    protected int getContentView() {
        return R.layout.activity_bind;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        //不具有返回上级页面功能时可双击返回键退出
        setTitle(0, R.string.title_bind_ac);

        setDoubleTapToExit(true);
        mEtPhoneNum.addTextChangedListener(textWatcher);
        mEtVerCode.addTextChangedListener(textWatcher);
        mTvPhoneCode.setOnClickListener(this);
        mTvGetCode.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        mPlatform = getIntent().getStringExtra(Constants.EXTRAS.EXTRAS);
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mEtPhoneNum.isFocused()) {     //手机号输入框获取焦点
                mMobile = s.toString();
                setCodeBtnIsEnable(mMobile);
//                if (s.length() != 11) {
//                    mTvGetCode.setEnabled(false);
//                    return;
//                }
//                if (s.toString().matches(Constants.Exps.PHONE)) {
//                    if (getCodeEnable)
//                        mTvGetCode.setEnabled(true);
//                } else {
//                    mTvGetCode.setEnabled(false);
//                    ToastHelper.showShort("请输入正确的手机号");
//                }
            } else {      // 验证码输入框获取焦点
                if (s.length() != 4) {       // 长度非4位进行错误处理
                    mBtnLogin.setEnabled(false);
                } else {
                    mBtnLogin.setEnabled(true);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };

    @Override
    protected void onDestroy() {
        Country.destroy();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_phone_number_code:
                startActivityForResult(new Intent(getApplicationContext(), NationalSmsCodeActivity.class), 111);
                break;
            case R.id.login_get_ver_code:
                if (StringUtils.isEmpty(mEtPhoneNum.getText().toString())) {
                    ToastHelper.showShort(R.string.input_correct_phone);
                    return;
                }
                // 验证手机号
                if (!PhoneNumberLibUtil.checkPhoneNumber(this, mCode, Long.valueOf(mEtPhoneNum.getText().toString()))) {
                    ToastHelper.showShort(R.string.input_correct_phone);
                    return;
                }
                mPresenter.getCode(mEtPhoneNum.getText().toString(), "+"+mCode);
                surplusDuration = duration;
                timeCount = new TimeCount((duration + 1) * 1000, 1000);
                timeCount.start();
                break;
            case R.id.login_login_btn:
                // 使用验证码登录
                mPresenter.requestThirdLogin(mEtPhoneNum.getText().toString(), mTvPhoneCode.getText().toString(), mEtVerCode.getText().toString(), mPlatform);
                break;
        }
    }

    /**
     * 返回验证码
     *
     * @param code
     */
    public void returnRequestVerifyCodeResult(String code) {
        mEtVerCode.requestFocus();
        mEtVerCode.setText("");
        mEtVerCode.append(code);
    }

    /**
     *
     * @param mobile
     */
    private void setCodeBtnIsEnable(String mobile) {
        if (TextUtils.isEmpty(mobile)){
            mTvGetCode.setEnabled(false);
            return;
        }
        mTvGetCode.setEnabled(mCountDownOver && PhoneNumberLibUtil.checkPhoneNumber(getContext(), mCode, Long.valueOf(mobile)));
    }

    /**
     * 返回验证码登录结果
     *
     * @param rsp
     */
    public void verifyCodeLogin(VerifyCodeLoginResp rsp) {
        Intent intent = new Intent();
        intent.putExtra(Constants.EXTRAS.EXTRA_LOGIN_STATE, true);
        LocalBroadcastUtil.sendBroadcast(BindPhoneActivity.this, intent, Constants.Action.LOGIN_CHANGE_OF_STATE_ACTION);
        // 登录成功
        MainActivity.startActivity(this);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK) {
            Country country = Country.fromJson(data.getStringExtra("country"));
            mTvPhoneCode.setText("+" + country.code);
            mCode = country.code;
        }
    }

    public void showShort(int resId) {
        ToastHelper.showShort(resId);
    }

    private int duration = 60;
    private int surplusDuration;

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            mCountDownOver = true;
            setCodeBtnIsEnable(mMobile);
            mTvGetCode.setText("获取验证码");
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            if (surplusDuration > 0) {
                surplusDuration--;
            }
            mTvGetCode.setText(surplusDuration + "秒");
            mCountDownOver = false;
            setCodeBtnIsEnable(mMobile);
        }
    }


    /**
     * 启动第三方登录绑定手机号操作页
     *
     * @param context
     * @param platform 第三方登录获得的可用用户信息
     */
    public static void startActivity(Context context, String platform) {
        Intent in = new Intent(context, BindPhoneActivity.class);
        in.putExtra(Constants.EXTRAS.EXTRAS, platform);
        context.startActivity(in);
    }
}
