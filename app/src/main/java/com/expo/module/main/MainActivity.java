package com.expo.module.main;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.utils.PrefsHelper;
import com.expo.base.utils.StatusBarUtils;
import com.expo.module.main.encyclopedia.EncyclopediaFragment;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;

import butterknife.BindView;

/*
 * 主页
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.container)
    FrameLayout mContainer;
    @BindView(R.id.tab_host)
    FragmentTabHost mTabHostView;

    private String[] tabTags = {"home", "encyclopedias", "panorama", "mine"};
    private int[] tabTitles = {R.string.main_home, R.string.main_encyclopedias, R.string.main_panorama, R.string.main_mine};
    private int mImages[] = {
            R.drawable.selector_tab_home,
            R.drawable.selector_tab_panorama,
            R.drawable.selector_tab_encyclopedia,
            R.drawable.selector_tab_mine};
    private Class[] fragments = new Class[]{
            HomeFragment.class,
            EncyclopediaFragment.class,
            PanoramaFragment.class,
            MineFragment.class};

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setDoubleTapToExit( true );
        mTabHostView.setup( this, getSupportFragmentManager(), R.id.container );
        mTabHostView.getTabWidget().setDividerDrawable( null ); // 去掉分割线
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTabHostView.getTabWidget().setElevation( 5f );
        }
        for (int i = 0; i < tabTags.length; i++) {
            TabHost.TabSpec tabSpec = mTabHostView.newTabSpec( tabTags[i] ).setIndicator( getView( i ) );
            mTabHostView.addTab( tabSpec, fragments[i], null );
        }
        StatusBarUtils.cancelStatusBarFullTransparent( MainActivity.this );
    }

    @Override
    protected boolean hasPresenter() {
        return false;
    }


    // 获得tab视图
    private View getView(int index) {
        View view = getLayoutInflater().inflate( R.layout.layout_tab, null );
        ImageView tabImage = view.findViewById( R.id.tab_img );
        TextView tbaTv = view.findViewById( R.id.tab_tv );
        tabImage.setImageResource( mImages[index] );
        tbaTv.setText( tabTitles[index] );
        return view;
    }

    public static void startActivity(Context context) {
        Intent in = new Intent( context, MainActivity.class );
        if (PrefsHelper.getString( Constants.Prefs.KEY_LANGUAGE_CHOOSE, null ) != null) {
            LanguageUtil.changeAppLanguage( context, PrefsHelper.getString( Constants.Prefs.KEY_LANGUAGE_CHOOSE, null ) );
            in.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        }
        context.startActivity( in );
    }

    public static void startActivityFromSplash(Context context) {
        Intent in = new Intent( context, MainActivity.class );
        if (PrefsHelper.getString( Constants.Prefs.KEY_LANGUAGE_CHOOSE, null ) != null) {
            LanguageUtil.changeAppLanguage( context, PrefsHelper.getString( Constants.Prefs.KEY_LANGUAGE_CHOOSE, null ) );
            in.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        }
        context.startActivity( in );
    }
}
