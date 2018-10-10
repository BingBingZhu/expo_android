package com.expo.module.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.LoginContract;
import com.expo.main.MainActivity;
import com.expo.module.webview.WebActivity;
import com.expo.network.response.VerifyCodeLoginResp;
import com.expo.utils.Constants;
import com.expo.utils.LocalBroadcastUtil;
import com.expo.widget.AppBarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class LoginActivity extends BaseActivity<LoginContract.Presenter> implements View.OnClickListener, LoginContract.View {

    @BindView(R.id.login_back)
    AppBarView title;
    @BindView(R.id.login_gt_img)
    ImageView imgGT;
    @BindView(R.id.login_phone_number_et)
    EditText etPhoneNum;
    @BindView(R.id.login_ver_code_et)
    EditText etVerCode;
    @BindView(R.id.login_get_ver_code)
    TextView tvGetCode;
    @BindView(R.id.login_login_btn)
    Button btnLogin;
    @BindView(R.id.login_protocol_tv)
    TextView tvProtocol;    // 服务协议
    @BindView(R.id.login_wechat)
    ImageView imgWechat;
    @BindView(R.id.login_qq)
    ImageView imgQQ;
    @BindView(R.id.login_microblog)
    ImageView imgMicroblog;     // 微博登录


    private boolean getCodeEnable = true;  // 是否允许获取验证码按钮可用
    private TimeCount timeCount;    //验证码获取计时器

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        //不具有返回上级页面功能时可双击返回键退出
        setDoubleTapToExit( true );
        tvGetCode.setEnabled( false );
        btnLogin.setEnabled( false );
        setBackVisibility( false );
        etPhoneNum.addTextChangedListener( textWatcher );
        etVerCode.addTextChangedListener( textWatcher );
        title.setOnClickListener( this );
        tvGetCode.setOnClickListener( this );
        btnLogin.setOnClickListener( this );
        imgWechat.setOnClickListener( this );
        imgQQ.setOnClickListener( this );
        imgMicroblog.setOnClickListener( this );
        tvProtocol.setOnClickListener( this );
    }

    private void setBackVisibility(boolean mHomeAsBack) {
        title.setVisibility( mHomeAsBack ? View.VISIBLE : View.GONE );
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
                imgGT.setImageResource( R.drawable.ico_tuan_open );
                if (s.length() != 11) {
                    tvGetCode.setEnabled( false );
                    return;
                }
                if (s.toString().matches( Constants.Exps.PHONE )) {
                    if (getCodeEnable)
                        tvGetCode.setEnabled( true );
                } else {
                    tvGetCode.setEnabled( false );
                    ToastHelper.showShort( "请输入正确的手机号" );
                }
            } else {      //验证码输入框获取焦点
                imgGT.setImageResource( R.drawable.ico_tuan_close );
                if (s.length() != 4) {       //长度非4位进行错误处理
                    btnLogin.setEnabled( false );
                } else {
                    btnLogin.setEnabled( true );
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public static void startActivity(Context context) {
        Intent in = new Intent( context, LoginActivity.class );
        context.startActivity( in );
    }

    @Override
    protected void onResume() {
        inspectPermission();
        super.onResume();
    }

    @OnClick(R.id.title_back)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.login_get_ver_code:
                mPresenter.getCode( etPhoneNum.getText().toString().trim() );
                duration += 60;
                surplusDuration = duration;
                timeCount = new TimeCount( (duration + 1) * 1000, 1000 );
                timeCount.start();
                break;
            case R.id.login_login_btn:
                // 使用验证码登录
                mPresenter.verifyCodeLogin( etPhoneNum.getText().toString().trim(),
                        etVerCode.getText().toString().trim() );
                break;
            case R.id.login_wechat:
                // 第三方微信登录
                mPresenter.threeLogin( Wechat.NAME );
                break;
            case R.id.login_qq:
                // 第三方QQ登录
                mPresenter.threeLogin( QQ.NAME );
                break;
            case R.id.login_microblog:
                // 新浪微博登录
                mPresenter.threeLogin( SinaWeibo.NAME );
                break;
            case R.id.login_protocol_tv:
                mPresenter.loadUserProtocol();
                break;
        }
    }

    @Override
    public void toUserProtocol(String url) {
        if (url.equals( "" )) {
            ToastHelper.showLong( R.string.there_is_no_user_protocol );
            return;
        }
        WebActivity.startActivity( this, url, getString( R.string.user_protocol ) );
    }


    /**
     * 返回验证码
     *
     * @param code
     */
    @Override
    public void returnRequestVerifyCodeResult(String code) {
        etVerCode.requestFocus();
        etVerCode.setText( "" );
        etVerCode.append( code );
    }

    /**
     * 返回验证码登录结果
     *
     * @param rsp
     */
    @Override
    public void verifyCodeLogin(VerifyCodeLoginResp rsp) {
        Intent intent = new Intent();
        intent.putExtra( Constants.EXTRAS.EXTRA_LOGIN_STATE, true );
        LocalBroadcastUtil.sendBroadcast( LoginActivity.this, intent, Constants.Action.LOGIN_CHANGE_OF_STATE_ACTION );
        // 登录成功
        MainActivity.startActivity( this );
        finish();
    }

    @Override
    public void showShort(int resId) {
        ToastHelper.showShort( resId );
    }

    private int duration = 0;
    private int surplusDuration;

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super( millisInFuture, countDownInterval );// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            getCodeEnable = true;
            tvGetCode.setEnabled( true );
            tvGetCode.setText( "获取验证码" );
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            if (surplusDuration > 0) {
                surplusDuration--;
            }
            tvGetCode.setText( surplusDuration + "秒" );
            getCodeEnable = false;
            tvGetCode.setEnabled( false );
        }
    }

    private List<String> permissionList;

    private void inspectPermission() {
        permissionList = new ArrayList<>();
        List<String> perNameList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission( LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE )
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add( Manifest.permission.WRITE_EXTERNAL_STORAGE );
            perNameList.add( "存储权限" );
        }
        if (ContextCompat.checkSelfPermission( LoginActivity.this, Manifest.permission.ACCESS_NETWORK_STATE )
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add( Manifest.permission.ACCESS_NETWORK_STATE );
            perNameList.add( "修改网络" );
        }
        if (ContextCompat.checkSelfPermission( LoginActivity.this, Manifest.permission.INTERNET )
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add( Manifest.permission.INTERNET );
            perNameList.add( "网络访问" );
        }
        if (ContextCompat.checkSelfPermission( LoginActivity.this, Manifest.permission.CAMERA )
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add( Manifest.permission.CAMERA );
            perNameList.add( "相机权限" );
        }
        if (ContextCompat.checkSelfPermission( LoginActivity.this, Manifest.permission.ACCESS_WIFI_STATE )
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add( Manifest.permission.ACCESS_WIFI_STATE );
            perNameList.add( "WIFI信息" );
        }
        if (ContextCompat.checkSelfPermission( LoginActivity.this, Manifest.permission.READ_PHONE_STATE )
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add( Manifest.permission.READ_PHONE_STATE );
            perNameList.add( "电话权限" );
        }
        if (ContextCompat.checkSelfPermission( LoginActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION )
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add( Manifest.permission.ACCESS_COARSE_LOCATION );
            perNameList.add( "定位权限" );
        }
        if (ContextCompat.checkSelfPermission( LoginActivity.this, Manifest.permission.CHANGE_WIFI_STATE )
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add( Manifest.permission.CHANGE_WIFI_STATE );
            perNameList.add( "WIFI状态" );
        }
        if (ContextCompat.checkSelfPermission( LoginActivity.this, Manifest.permission.BLUETOOTH_ADMIN )
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add( Manifest.permission.BLUETOOTH_ADMIN );
            perNameList.add( "蓝牙权限" );
        }
        if (ContextCompat.checkSelfPermission( LoginActivity.this, Manifest.permission.GET_ACCOUNTS )
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add( Manifest.permission.GET_ACCOUNTS );
            perNameList.add( "通讯录" );
        }
        if (permissionList.size() > 0) {
            //弹出权限请求提示对话框
        }
    }
}
