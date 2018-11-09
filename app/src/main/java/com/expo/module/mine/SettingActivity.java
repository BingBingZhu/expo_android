package com.expo.module.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.StringUtils;
import com.expo.R;
import com.expo.adapters.DownloadData;
import com.expo.base.BaseActivity;
import com.expo.base.utils.ActivityHelper;
import com.expo.base.utils.PrefsHelper;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.SettingContract;
import com.expo.contract.presenter.SettingPresenterImpl;
import com.expo.entity.AppInfo;
import com.expo.module.download.DownloadManager;
import com.expo.module.login.LoginActivity;
import com.expo.network.response.VersionInfoResp;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.expo.widget.AppBarView;
import com.expo.widget.MySettingView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 设置页
 */
public class SettingActivity extends BaseActivity<SettingContract.Presenter> implements SettingContract.View {

    @BindView(R.id.setting_language)
    MySettingView mTvLanguage;
    @BindView(R.id.setting_cache)
    MySettingView mTvCache;
    @BindView(R.id.setting_update)
    MySettingView mTvUpdate;
    @BindView(R.id.setting_guide)
    MySettingView mTvGuide;
    @BindView(R.id.setting_policy)
    MySettingView mTvPolicy;
    @BindView(R.id.logout)
    TextView mTvLogout;

    boolean mIsCn;//现在是否是汉语
    boolean mSelectCn;//现在是否是选择了汉语

    DialogPlus mDialogUpdate;
    DialogPlus mDialogLanguage;

    OnClickListener mLanguageClickListener = new OnClickListener() { // 切换语言的监听
        @Override
        public void onClick(DialogPlus d, View v) {
            switch (v.getId()) {
                case R.id.language_cn:
                    mSelectCn = true;
                    break;
                case R.id.language_en:
                    mSelectCn = false;
                    break;
                case R.id.ok:
                    if (mIsCn != mSelectCn) {
                        Locale locale;
                        if (mSelectCn) {
                            locale = Locale.CHINA;
                            PrefsHelper.setString(Constants.Prefs.KEY_LANGUAGE_CHOOSE, LanguageUtil.LANGUAGE_CN);
                        } else {
                            locale = Locale.ENGLISH;
                            PrefsHelper.setString(Constants.Prefs.KEY_LANGUAGE_CHOOSE, LanguageUtil.LANGUAGE_EN);
                        }
                        LanguageUtil.changeAppLanguage(SettingActivity.this, locale);

                        mIsCn = mSelectCn;
                        fresh();
                        ActivityHelper.reCreateAll(SettingActivity.this);
                    }
                case R.id.cancle:
                    d.dismiss();
                    break;
            }
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(0, R.string.title_setting_ac);
        mTvLanguage.setRightText(R.string.language);

        mSelectCn = mIsCn = StringUtils.equals(PrefsHelper.getString(Constants.Prefs.KEY_LANGUAGE_CHOOSE, null), LanguageUtil.LANGUAGE_CN);
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent(context, SettingActivity.class);
        context.startActivity(in);
    }

    @OnClick(R.id.setting_language)
    public void clickLanguage(MySettingView view) {
        if (mDialogLanguage == null) {
            View dv = LayoutInflater.from(this).inflate(R.layout.dialog_language_select, null);
            int viewId = mIsCn ? R.id.language_cn : R.id.language_en;
            dv.findViewById(viewId).performClick();

            mDialogLanguage = DialogPlus.newDialog(this)
                    .setContentHolder(new ViewHolder(dv))
                    .setGravity(Gravity.BOTTOM)
                    .setOnClickListener(mLanguageClickListener)
                    .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                    .create();
        }
        mDialogLanguage.show();
    }

    private void fresh() {
        ((AppBarView) getTitleView()).setTitle(R.string.title_setting_ac);
        mTvLanguage.fresh();
        mTvCache.fresh();
        mTvUpdate.fresh();
        mTvGuide.fresh();
        mTvPolicy.fresh();

        mTvLogout.setText(R.string.logout);
    }

    @OnClick(R.id.setting_cache)
    public void clickCache(MySettingView view) {

    }

    @OnClick(R.id.setting_update)
    public void clickUpdate(MySettingView view) {
        mPresenter.checkUpdate();
    }

    @OnClick(R.id.setting_guide)
    public void clickGuide(MySettingView view) {

    }

    @OnClick(R.id.setting_policy)
    public void clickPolicy(MySettingView view) {

    }

    @OnClick(R.id.logout)
    public void logout(View view) {
        mPresenter.logout();
    }

    @Override
    public void logout() {
        LoginActivity.startActivity(this);
    }

    @Override
    public void appUpdate(AppInfo info) {
        mPresenter.update(this, info);
    }

}
