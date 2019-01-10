package com.expo.module.mine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.StringUtils;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.utils.ActivityHelper;
import com.expo.base.utils.DataCleanUtil;
import com.expo.base.utils.PrefsHelper;
import com.expo.base.utils.ToastHelper;
import com.expo.map.LocationManager;
import com.expo.upapp.UpdateAppManager;
import com.expo.contract.SettingContract;
import com.expo.entity.AppInfo;
import com.expo.entity.CommonInfo;
import com.expo.module.login.LoginActivity;
import com.expo.module.prompt.PromptActivity;
import com.expo.module.webview.WebActivity;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.expo.utils.LocalBroadcastUtil;
import com.expo.widget.AppBarView;
import com.expo.widget.CustomDefaultDialog;
import com.expo.widget.MySettingView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;

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
    @BindView(R.id.setting_location_pattern)
    MySettingView mTvLocationPattern;
    @BindView(R.id.setting_guide)
    MySettingView mTvGuide;
    @BindView(R.id.setting_policy)
    MySettingView mTvPolicy;
    @BindView(R.id.logout)
    TextView mTvLogout;
    @BindView(R.id.setting_map_on_off)
    ImageView imgMapON_OFF;
    @BindView(R.id.setting_track_on_off)
    ImageView imgTrackON_OFF;
    @BindView(R.id.setting_prompt_tone)
    MySettingView mTvPromptTone;

    boolean mIsCn;//现在是否是汉语
    boolean mSelectCn;//现在是否是选择了汉语

    DialogPlus mDialogLanguage;
    DialogPlus mDialogLocation;

    // 切换语言的监听
    OnClickListener mDialogClickListener = (d, v) -> {
        switch (v.getId()) {
            case R.id.language_cn:
                changeLanguage( true, d );
                break;
            case R.id.language_en:
                changeLanguage( false, d );
                break;
            case R.id.location_mode_0:
                LocationManager.getInstance().setLocationModePotion(0);
                mTvLocationPattern.setRightText(getString(R.string.hight_accuracy));
                break;
            case R.id.location_mode_1:
                LocationManager.getInstance().setLocationModePotion(1);
                mTvLocationPattern.setRightText(getString(R.string.device_sensors));
                break;
            case R.id.location_mode_2:
                LocationManager.getInstance().setLocationModePotion(2);
                mTvLocationPattern.setRightText(getString(R.string.battery_saving));
                break;
        }
        d.dismiss();
    };

    private void changeLanguage(boolean selectCn, DialogPlus d) {
        mSelectCn = selectCn;
        if (mIsCn != mSelectCn) {
            Locale locale;
            if (mSelectCn) {
                locale = Locale.CHINA;
                PrefsHelper.setString( Constants.Prefs.KEY_LANGUAGE_CHOOSE, LanguageUtil.LANGUAGE_CN );
            } else {
                locale = Locale.ENGLISH;
                PrefsHelper.setString( Constants.Prefs.KEY_LANGUAGE_CHOOSE, LanguageUtil.LANGUAGE_EN );
            }
            LanguageUtil.changeAppLanguage( SettingActivity.this, locale );

            mIsCn = mSelectCn;
            fresh();
            ActivityHelper.reCreateAll( SettingActivity.this );
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle( 0, R.string.title_setting_ac );
        imgMapON_OFF.setImageResource(PrefsHelper.getBoolean(Constants.Prefs.KEY_MAP_ON_OFF, false) ? R.mipmap.ico_on : R.mipmap.ico_off );
        imgTrackON_OFF.setImageResource(PrefsHelper.getBoolean(Constants.Prefs.KEY_TRACK_ON_OFF, false) ? R.mipmap.ico_on : R.mipmap.ico_off );
        mTvCache.setRightText(DataCleanUtil.getCacheSize());
        mTvLanguage.setRightText( R.string.language );
        mTvUpdate.setRightText( "v" + AppUtils.getAppVersionName() );
        mTvLocationPattern.setRightText(LocationManager.getInstance().getLocationModeString(getContext()));
        mSelectCn = mIsCn = StringUtils.equals( PrefsHelper.getString( Constants.Prefs.KEY_LANGUAGE_CHOOSE, null ), LanguageUtil.LANGUAGE_CN );
    }

    @Override
    protected void onStart() {
        super.onStart();
        mTvPromptTone.setRightText(Constants.RawResource.rawName[PrefsHelper.getInt(Constants.Prefs.KEY_RAW_SELECTOR_POSITION, 0)]);
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent( context, SettingActivity.class );
        context.startActivity( in );
    }

    @OnClick(R.id.setting_language)
    public void clickLanguage(MySettingView view) {
        if (mDialogLanguage == null) {
            View dv = LayoutInflater.from( getContext() ).inflate( R.layout.dialog_language_select, null );
            int viewId = mIsCn ? R.id.language_cn : R.id.language_en;
            dv.findViewById( viewId ).performClick();

            mDialogLanguage = DialogPlus.newDialog( this )
                    .setContentHolder( new ViewHolder( dv ) )
                    .setGravity( Gravity.BOTTOM )
                    .setOnClickListener(mDialogClickListener)
                    .setExpanded( false )  // This will enable the expand feature, (similar to android L share dialog)
                    .create();
        }
        mDialogLanguage.show();
    }

    private void fresh() {
        ((AppBarView) getTitleView()).setTitle( R.string.title_setting_ac );
        mTvLanguage.fresh();
        mTvCache.fresh();
        mTvUpdate.fresh();
        mTvGuide.fresh();
        mTvPolicy.fresh();

        mTvLogout.setText( R.string.logout );
    }

    @OnClick(R.id.setting_cache)
    public void clickCache(MySettingView view) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.clear_cache_msg)
                .setPositiveButton(R.string.ok, (dialog, which) -> {

                    // 清除缓存
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            //清除Fresco的图片文件缓存
                            Fresco.getImagePipeline().clearDiskCaches();
                            //清理截图等缓存
                            DataCleanUtil.deleteCacheFiles();
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            view.setRightText(DataCleanUtil.getCacheSize());
                            ToastHelper.showLong(R.string.cache_cleared);
                        }
                    }.execute();
                    dialog.dismiss();
                }).setNegativeButton(R.string.cancel, null)
                .show();
    }

    @OnClick(R.id.setting_prompt_tone)
    public void promptTone(View v){
        PromptActivity.startActivity(getContext());
    }

    @OnClick(R.id.setting_update)
    public void clickUpdate(MySettingView view) {
        mPresenter.checkUpdate();
        LocalBroadcastUtil.registerReceiver(getContext(), receiver, Constants.Action.ACTION_DOWNLOAD_APP_SUCCESS, Constants.Action.ACTION_CANCEL_UPDATE);
    }

    @OnClick(R.id.setting_location_pattern)
    public void clickLocationMode(MySettingView view) {
//        mTvLocationPattern.setRightText();
        if (mDialogLocation == null) {
            View dv = LayoutInflater.from( getContext() ).inflate( R.layout.dialog_location_select, null );
            int viewId = 0;
            int modePotion = LocationManager.getInstance().getLocationModePotion();
            switch (modePotion){
                case 0:
                    viewId = R.id.location_mode_0;
                    break;
                case 1:
                    viewId = R.id.location_mode_1;
                    break;
                case 2:
                    viewId = R.id.location_mode_2;
                    break;
            }
            dv.findViewById( viewId ).performClick();

            mDialogLocation = DialogPlus.newDialog( this )
                    .setContentHolder( new ViewHolder( dv ) )
                    .setGravity( Gravity.BOTTOM )
                    .setOnClickListener(mDialogClickListener)
                    .setExpanded( false )  // This will enable the expand feature, (similar to android L share dialog)
                    .create();
        }
        mDialogLocation.show();
    }



    @OnClick(R.id.setting_guide)
    public void clickGuide(MySettingView view) {
        mPresenter.clickPolicy( CommonInfo.USER_GUIDE );
    }

    @OnClick(R.id.setting_policy)
    public void clickPolicy(MySettingView view) {
        mPresenter.clickPolicy( CommonInfo.USER_PROTOCOL );
    }
    @OnClick(R.id.setting_about)
    public void clickAbout(MySettingView view) {
        AboutActivity.startActivity(getContext());
    }

    @OnClick(R.id.logout)
    public void logout(View view) {
        mPresenter.logout();
    }

    @OnClick(R.id.setting_map_on_off)
    public void mapOnOFF(View v){
        PrefsHelper.setBoolean(Constants.Prefs.KEY_MAP_ON_OFF, !PrefsHelper.getBoolean(Constants.Prefs.KEY_MAP_ON_OFF, false));
        // 设置图片
        imgMapON_OFF.setImageResource(PrefsHelper.getBoolean(Constants.Prefs.KEY_MAP_ON_OFF, false) ? R.mipmap.ico_on : R.mipmap.ico_off );
    }

    @OnClick(R.id.setting_track_on_off)
    public void trackOnOFF(View v){
        if (!PrefsHelper.getBoolean(Constants.Prefs.KEY_TRACK_ON_OFF, false)) {
            CustomDefaultDialog dialog = new CustomDefaultDialog(getContext());
            dialog.setContent(R.string.track_open_hint)
                    .setOkText(R.string.open_track)
                    .setCancelText(R.string.think_again_track)
                    .setOkTextRed()
                    .setOnOKClickListener(v1 -> { settingTrack(); dialog.dismiss(); })
                    .show();
        } else
            settingTrack();
    }

    private void settingTrack(){
        PrefsHelper.setBoolean(Constants.Prefs.KEY_TRACK_ON_OFF, !PrefsHelper.getBoolean(Constants.Prefs.KEY_TRACK_ON_OFF, false));
        // 设置图片
        imgTrackON_OFF.setImageResource(PrefsHelper.getBoolean(Constants.Prefs.KEY_TRACK_ON_OFF, false) ? R.mipmap.ico_on : R.mipmap.ico_off );
        LocalBroadcastUtil.sendBroadcast(getContext(), new Intent().putExtra(Constants.EXTRAS.EXTRA_TRACK_CHANAGE,
                PrefsHelper.getBoolean(Constants.Prefs.KEY_TRACK_ON_OFF, false)), Constants.Action.ACTION_TRACK_CHANAGE);
    }

    @Override
    public void logout() {
        LoginActivity.startActivity( this );
    }

    @Override
    public void appUpdate(AppInfo info) {
        mPresenter.update( this, info );
    }

    @Override
    public void returnCommonInfo(CommonInfo commonInfo) {
        if (commonInfo.getType().equals( CommonInfo.USER_GUIDE )) {
            if (null == commonInfo || TextUtils.isEmpty( commonInfo.getLinkUrl() )) {
                ToastHelper.showLong( R.string.there_is_no_user_guidance );
                return;
            }
        } else if (commonInfo.getType().equals( CommonInfo.USER_PROTOCOL )) {
            if (null == commonInfo || TextUtils.isEmpty( commonInfo.getLinkUrl() )) {
                ToastHelper.showLong( R.string.there_is_no_user_protocol );
                return;
            }
        }
        WebActivity.startActivity( getContext(), commonInfo.getLinkUrl(),
                LanguageUtil.isCN() ? commonInfo.getCaption() : commonInfo.getCaptionEn() );
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.Action.ACTION_DOWNLOAD_APP_SUCCESS)){
                LocalBroadcastUtil.unregisterReceiver(context, receiver);
                installApp(getContext());
            }
            if (intent.getAction().equals(Constants.Action.ACTION_CANCEL_UPDATE)){
                LocalBroadcastUtil.unregisterReceiver(context, receiver);
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void installApp(Context context){
        if(!getPackageManager().canRequestPackageInstalls()){
            Uri packageURI = Uri.parse("package:"+getPackageName());
            Intent in = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,packageURI);
            startActivityForResult(in, Constants.RequestCode.REQ_INSTALL_PERMISS_CODE);
            return;
        }
        UpdateAppManager.getInstance(context).installApp();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.RequestCode.REQ_INSTALL_PERMISS_CODE){
            if(getPackageManager().canRequestPackageInstalls()) {
                UpdateAppManager.getInstance(getContext()).installApp();
            }
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastUtil.unregisterReceiver(getContext(), receiver);
        super.onDestroy();
    }
}
