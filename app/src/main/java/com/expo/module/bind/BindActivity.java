//package com.expo.module.bind;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.blankj.utilcode.util.StringUtils;
//import com.expo.R;
//import com.expo.base.BaseActivity;
//import com.expo.base.utils.PrefsHelper;
//import com.expo.base.utils.ToastHelper;
//import com.expo.contract.BindPhoneContract;
//import com.expo.main.MainActivity;
//import com.expo.module.login.NationalSmsCodeActivity;
//import com.expo.network.response.VerifyCodeLoginResp;
//import com.expo.utils.Constants;
//import com.expo.utils.LanguageUtil;
//import com.expo.utils.LocalBroadcastUtil;
//import com.sahooz.library.Country;
//import com.sahooz.library.PhoneNumberLibUtil;
//
//import butterknife.BindView;
//
//public class BindActivity extends BaseActivity<BindPhoneContract.Presenter> implements View.OnClickListener, BindPhoneContract.View {
//
//    @BindView(R.id.login_phone_number_et)
//    EditText mEtPhoneNum;
//    @BindView(R.id.login_phone_number_code)
//    TextView mTVPhoneCode;
//    @BindView(R.id.login_ver_code_et)
//    EditText etVerCode;
//    @BindView(R.id.login_get_ver_code)
//    TextView mTVGetCode;
//    @BindView(R.id.login_login_btn)
//    Button mBtnLogin;
//
//    private boolean getCodeEnable = true;  // 是否允许获取验证码按钮可用
//    private TimeCount timeCount;    //验证码获取计时器
//    private int mCode = 86;    //手机号区域码
//
//    AlertDialog mAlertDialog;
//
//    @Override
//    protected int getContentView() {
//        return R.layout.activity_bind;
//    }
//
//    @Override
//    protected void onInitView(Bundle savedInstanceState) {
//        //不具有返回上级页面功能时可双击返回键退出
//        setTitle(0, R.string.title_bind_ac);
//
//        setDoubleTapToExit(true);
//        mEtPhoneNum.addTextChangedListener(textWatcher);
//        etVerCode.addTextChangedListener(textWatcher);
//        mTVPhoneCode.setOnClickListener(this);
//        mTVGetCode.setOnClickListener(this);
//        mBtnLogin.setOnClickListener(this);
//    }
//
//    @Override
//    protected boolean hasPresenter() {
//        return true;
//    }
//
//    private TextWatcher textWatcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            if (mEtPhoneNum.isFocused()) {     //手机号输入框获取焦点
//                if (s.length() != 11) {
//                    mTVGetCode.setEnabled(false);
//                    return;
//                }
//                if (s.toString().matches(Constants.Exps.PHONE)) {
//                    if (getCodeEnable)
//                        mTVGetCode.setEnabled(true);
//                } else {
//                    mTVGetCode.setEnabled(false);
//                    ToastHelper.showShort("请输入正确的手机号");
//                }
//            } else {      //验证码输入框获取焦点
//                if (s.length() != 4) {       //长度非4位进行错误处理
//                    mBtnLogin.setEnabled(false);
//                } else {
//                    mBtnLogin.setEnabled(true);
//                }
//            }
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//
//        }
//    };
//
//    public static void startActivity(Context context) {
//        Intent in = new Intent(context, BindActivity.class);
//        if (PrefsHelper.getString(Constants.Prefs.KEY_LANGUAGE_CHOOSE, null) != null) {
//            LanguageUtil.changeAppLanguage(context, PrefsHelper.getString(Constants.Prefs.KEY_LANGUAGE_CHOOSE, null));
//            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        }
//        context.startActivity(in);
//    }
//
//    @Override
//    protected void onDestroy() {
//        Country.destroy();
//        super.onDestroy();
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.login_phone_number_code:
//                startActivityForResult(new Intent(getApplicationContext(), NationalSmsCodeActivity.class), 111);
//                break;
//            case R.id.login_get_ver_code:
//                if (StringUtils.isEmpty(mEtPhoneNum.getText().toString())) {
//                    ToastHelper.showShort(R.string.input_correct_phone);
//                    return;
//                }
////                验证手机号
//                if (!PhoneNumberLibUtil.checkPhoneNumber(this, mCode, Long.valueOf(mEtPhoneNum.getText().toString()))) {
//                    ToastHelper.showShort(R.string.input_correct_phone);
//                    return;
//                }
//                confirmPhoneNumber();
//                break;
//            case R.id.dialog_bind_phone_cancle:
//            case R.id.dialog_bind_phone_hide:
//                if (mAlertDialog != null) {
//                    mAlertDialog.dismiss();
//                }
//                break;
//            case R.id.dialog_bind_phone_ok:
//                mPresenter.getCode();
//                duration += 60;
//                surplusDuration = duration;
//                timeCount = new TimeCount((duration + 1) * 1000, 1000);
//                timeCount.start();
//                if (mAlertDialog != null) {
//                    mAlertDialog.dismiss();
//                }
//                break;
//            case R.id.login_login_btn:
//                // 使用验证码登录
//                mPresenter.bindPhone();
//                break;
//        }
//    }
//
//    /**
//     * 返回验证码
//     *
//     * @param code
//     */
//    public void returnRequestVerifyCodeResult(String code) {
//        etVerCode.requestFocus();
//        etVerCode.setText("");
//        etVerCode.append(code);
//    }
//
//    /**
//     * 返回验证码登录结果
//     *
//     * @param rsp
//     */
//    public void verifyCodeLogin(VerifyCodeLoginResp rsp) {
//        Intent intent = new Intent();
//        intent.putExtra(Constants.EXTRAS.EXTRA_LOGIN_STATE, true);
//        LocalBroadcastUtil.sendBroadcast(BindActivity.this, intent, Constants.Action.LOGIN_CHANGE_OF_STATE_ACTION);
//        // 登录成功
//        MainActivity.startActivity(this);
//        finish();
//    }
//
//    public void toUserProtocol(String url) {
//
//    }
//
//    public void confirmPhoneNumber() {
//        if (mAlertDialog == null) {
//            mAlertDialog = new AlertDialog.Builder(this, R.style.TransparentDialog).create();
//        }
//        mAlertDialog.show();
//        mAlertDialog.setContentView(getDialogView());
//    }
//
//    View mDialogView;
//
//    public View getDialogView() {
//        if (mDialogView == null) {
//            synchronized (this) {
//                if (mDialogView == null) {
//                    mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_bind_phone, null);
//                    mDialogView.findViewById(R.id.dialog_bind_phone_cancle).setOnClickListener(this);
//                    mDialogView.findViewById(R.id.dialog_bind_phone_hide).setOnClickListener(this);
//                    mDialogView.findViewById(R.id.dialog_bind_phone_ok).setOnClickListener(this);
//                }
//            }
//        }
//        ((TextView) mDialogView.findViewById(R.id.dialog_bind_phone_text)).setText(getResources().getString(R.string.send_sms_to_phone_number,
//                mTVPhoneCode.getText().toString() + " " + mEtPhoneNum.getText().toString()));
//        return mDialogView;
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 111 && resultCode == RESULT_OK) {
//            Country country = Country.fromJson(data.getStringExtra("country"));
//            mTVPhoneCode.setText("+" + country.code);
//            mCode = country.code;
//        }
//    }
//
//    public void showShort(int resId) {
//        ToastHelper.showShort(resId);
//    }
//
//    private int duration = 0;
//    private int surplusDuration;
//
//    class TimeCount extends CountDownTimer {
//        public TimeCount(long millisInFuture, long countDownInterval) {
//            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
//        }
//
//        @Override
//        public void onFinish() {// 计时完毕时触发
//            getCodeEnable = true;
//            mTVGetCode.setEnabled(true);
//            mTVGetCode.setText("获取验证码");
//        }
//
//        @Override
//        public void onTick(long millisUntilFinished) {// 计时过程显示
//            if (surplusDuration > 0) {
//                surplusDuration--;
//            }
//            mTVGetCode.setText(surplusDuration + "秒");
//            getCodeEnable = false;
//            mTVGetCode.setEnabled(false);
//        }
//    }
//
//}
