package com.expo.module.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.MainThread;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.utils.LogUtils;
import com.expo.base.utils.PrefsHelper;
import com.expo.module.main.encyclopedia.EncyclopediaFragment;
import com.expo.module.main.find.FindFragment;
import com.expo.module.main.scenic.ScenicFragment;
import com.expo.services.TrackRecordService;
import com.expo.upapp.UpdateAppManager;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.expo.utils.LocalBroadcastUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

/*
 * 主页
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.container)
    FrameLayout mContainer;
    @BindView(R.id.tab_host)
    FragmentTabHost mTabHostView;

    private String[] tabTags = {"home", "scenic", "find", "mine"};//"panorama",
    private int[] tabTitles = {R.string.main_home, R.string.main_scenic, R.string.main_find, R.string.main_mine};// R.string.main_panorama,
    private int mImages[] = {
            R.drawable.selector_tab_home,
            R.drawable.selector_tab_panorama,
//            R.drawable.selector_tab_encyclopedia,
            R.drawable.selector_tab_find,
            R.drawable.selector_tab_mine};
    private Class[] fragments = new Class[]{
            HomeFragment.class,
//            PanoramaFragment.class,
            ScenicFragment.class,
//            EncyclopediaFragment.class,
            FindFragment.class,
            MineFragment.class};
    private ServiceConnection conn;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setDoubleTapToExit(true);
        mTabHostView.setup(this, getSupportFragmentManager(), R.id.container);
        mTabHostView.getTabWidget().setDividerDrawable(null); // 去掉分割线
        for (int i = 0; i < tabTags.length; i++) {
            TabHost.TabSpec tabSpec = mTabHostView.newTabSpec(tabTags[i]).setIndicator(getView(i));
            mTabHostView.addTab(tabSpec, fragments[i], null);
        }
        TrackRecordService.startService(getContext());
        LocalBroadcastUtil.registerReceiver(getContext(), receiver, Constants.Action.ACTION_DOWNLOAD_APP_SUCCESS);
//        StatusBarUtils.cancelStatusBarFullTransparent( MainActivity.this );//加上4.4系统会出现不能沉浸式，不要加
    }

    @Override
    protected boolean hasPresenter() {
        return false;
    }

    // 获得tab视图
    private View getView(int index) {
        View view = getLayoutInflater().inflate(R.layout.layout_tab, null);
        ImageView tabImage = view.findViewById(R.id.tab_img);
        TextView tbaTv = view.findViewById(R.id.tab_tv);
        tabImage.setImageResource(mImages[index]);
        tbaTv.setText(tabTitles[index]);
        return view;
    }

    public static void startActivity(Context context) {
        Intent in = new Intent(context, MainActivity.class);
        if (PrefsHelper.getString(Constants.Prefs.KEY_LANGUAGE_CHOOSE, null) != null) {
            LanguageUtil.changeAppLanguage(context, PrefsHelper.getString(Constants.Prefs.KEY_LANGUAGE_CHOOSE, null));
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        context.startActivity(in);
    }

    public static void startActivityFromSplash(Context context) {
        Intent in = new Intent(context, MainActivity.class);
        if (PrefsHelper.getString(Constants.Prefs.KEY_LANGUAGE_CHOOSE, null) != null) {
            LanguageUtil.changeAppLanguage(context, PrefsHelper.getString(Constants.Prefs.KEY_LANGUAGE_CHOOSE, null));
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        context.startActivity(in);
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.Action.ACTION_DOWNLOAD_APP_SUCCESS)) {
                LocalBroadcastUtil.unregisterReceiver(context, receiver);
                installApp(getContext());
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void installApp(Context context) {
        if (!getPackageManager().canRequestPackageInstalls()) {
            Uri packageURI = Uri.parse("package:" + getPackageName());
            Intent in = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
            startActivityForResult(in, Constants.RequestCode.REQ_INSTALL_PERMISS_CODE);
            return;
        }
        UpdateAppManager.getInstance(context).installApp();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RequestCode.REQ_INSTALL_PERMISS_CODE) {
            if (getPackageManager().canRequestPackageInstalls()) {
                UpdateAppManager.getInstance(getContext()).installApp();
            }
        } else if (requestCode == Constants.RequestCode.REQ_TO_FIND_INFO) {
            FindFragment f = (FindFragment) getFragment(2);
            if (null != f) {
                // 然后在碎片中调用重写的onActivityResult方法
                f.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    public void goScenic() {
        mTabHostView.setCurrentTabByTag("scenic");
        new Handler().postDelayed(() -> ((ScenicFragment) getFragment(1)).onClickScene(null), 300);
    }

    public void goScenicMap() {
        mTabHostView.setCurrentTabByTag("scenic");
        new Handler().postDelayed(() -> ((ScenicFragment) getFragment(1)).onClickScene(null), 300);
    }

    private Fragment getFragment(int tabId) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            String str1 = fragment.getTag();
            String str2 = String.valueOf(tabTags[tabId]);
            if (str1 != null && str1.equals(str2)) // 最开始没有检查str1是否为空，导致crash！
                return fragment;
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        TrackRecordService.stopService(getContext());
        LocalBroadcastUtil.unregisterReceiver(getContext(), receiver);
        super.onDestroy();
    }
}
