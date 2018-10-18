package com.expo.module.bind;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.utils.PrefsHelper;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.LoginContract;
import com.expo.main.MainActivity;
import com.expo.module.eara.PickActivity;
import com.expo.network.response.VerifyCodeLoginResp;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.expo.utils.LocalBroadcastUtil;
import com.expo.widget.AppBarView;
import com.sahooz.library.Country;
import com.sahooz.library.PhoneNumberLibUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class BindActivity extends BaseActivity<LoginContract.Presenter> implements View.OnClickListener, LoginContract.View {

    @BindView(R.id.login_back)
    AppBarView title;
    @BindView(R.id.login_phone_number_et)
    EditText etPhoneNum;
    @BindView(R.id.login_phone_number_code)
    TextView tvPhoneCode;
    @BindView(R.id.login_ver_code_et)
    EditText etVerCode;
    @BindView(R.id.login_get_ver_code)
    TextView tvGetCode;
    @BindView(R.id.login_login_btn)
    Button btnLogin;

    private boolean getCodeEnable = true;  // 是否允许获取验证码按钮可用
    private TimeCount timeCount;    //验证码获取计时器
    private int code = 86;    //手机号区域码

    @Override
    protected int getContentView() {
        return R.layout.activity_bind;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        //不具有返回上级页面功能时可双击返回键退出
        setDoubleTapToExit(true);
        tvGetCode.setEnabled(false);
        btnLogin.setEnabled(false);
        etPhoneNum.addTextChangedListener(textWatcher);
        etVerCode.addTextChangedListener(textWatcher);
        title.setOnClickListener(this);
        tvPhoneCode.setOnClickListener(this);
        tvGetCode.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
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
            if (etPhoneNum.isFocused()) {     //手机号输入框获取焦点
                if (s.length() != 11) {
                    tvGetCode.setEnabled(false);
                    return;
                }
                if (s.toString().matches(Constants.Exps.PHONE)) {
                    if (getCodeEnable)
                        tvGetCode.setEnabled(true);
                } else {
                    tvGetCode.setEnabled(false);
                    ToastHelper.showShort("请输入正确的手机号");
                }
            } else {      //验证码输入框获取焦点
                if (s.length() != 4) {       //长度非4位进行错误处理
                    btnLogin.setEnabled(false);
                } else {
                    btnLogin.setEnabled(true);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public static void startActivity(Context context) {
        Intent in = new Intent(context, BindActivity.class);
        if (PrefsHelper.getString(Constants.Prefs.KEY_LANGUAGE_CHOOSE, null) != null) {
            LanguageUtil.changeAppLanguage(context, PrefsHelper.getString(Constants.Prefs.KEY_LANGUAGE_CHOOSE, null));
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        context.startActivity(in);
    }

    @Override
    protected void onDestroy() {
        Country.destroy();
        super.onDestroy();
    }

    @OnClick(R.id.title_back)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_phone_number_code:
                startActivityForResult(new Intent(getApplicationContext(), PickActivity.class), 111);
                break;
            case R.id.title_back:
                finish();
                break;
            case R.id.login_get_ver_code:
                if (StringUtils.isEmpty(etPhoneNum.getText().toString())) {
                    ToastHelper.showShort(R.string.input_correct_phone);
                }
//                验证手机号
                if (!PhoneNumberLibUtil.checkPhoneNumber(this, code, Long.valueOf(etPhoneNum.getText().toString()))) {
                    ToastHelper.showShort(R.string.input_correct_phone);
                    return;
                }
                mPresenter.getCode(etPhoneNum.getText().toString().trim());
                duration += 60;
                surplusDuration = duration;
                timeCount = new TimeCount((duration + 1) * 1000, 1000);
                timeCount.start();
                break;
            case R.id.login_login_btn:
                // 使用验证码登录
                mPresenter.verifyCodeLogin(etPhoneNum.getText().toString().trim(),
                        etVerCode.getText().toString().trim());
                break;
        }
    }

    /**
     * 返回验证码
     *
     * @param code
     */
    @Override
    public void returnRequestVerifyCodeResult(String code) {
        etVerCode.requestFocus();
        etVerCode.setText("");
        etVerCode.append(code);
    }

    /**
     * 返回验证码登录结果
     *
     * @param rsp
     */
    @Override
    public void verifyCodeLogin(VerifyCodeLoginResp rsp) {
        Intent intent = new Intent();
        intent.putExtra(Constants.EXTRAS.EXTRA_LOGIN_STATE, true);
        LocalBroadcastUtil.sendBroadcast(BindActivity.this, intent, Constants.Action.LOGIN_CHANGE_OF_STATE_ACTION);
        // 登录成功
        MainActivity.startActivity(this);
        finish();
    }

    @Override
    public void toUserProtocol(String url) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK) {
            Country country = Country.fromJson(data.getStringExtra("country"));
            tvPhoneCode.setText("+" + country.code);
            code = country.code;
        }
    }

    @Override
    public void showShort(int resId) {
        ToastHelper.showShort(resId);
    }

    private int duration = 0;
    private int surplusDuration;

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            getCodeEnable = true;
            tvGetCode.setEnabled(true);
            tvGetCode.setText("获取验证码");
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            if (surplusDuration > 0) {
                surplusDuration--;
            }
            tvGetCode.setText(surplusDuration + "秒");
            getCodeEnable = false;
            tvGetCode.setEnabled(false);
        }
    }

}
