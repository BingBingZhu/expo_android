package com.expo.module.guide;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.expo.BuildConfig;
import com.expo.R;
import com.expo.base.BaseFragment;
import com.expo.base.utils.PrefsHelper;
import com.expo.module.splash.SplashActivity;
import com.expo.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 引导页
 */
public class GuideFragment extends BaseFragment {

    @BindView(R.id.guide_vp)
    ViewPager mGuidePages;
    @BindView(R.id.guide_indicator)
    LinearLayout mIndicators;
    private IndicatorPagerAdapter mGuidePagerAdapter;
    private int[] guidesImages = new int[]{R.mipmap.guide1, R.mipmap.guide2};
    private int[] guidesTitles = new int[]{R.string.guide1, R.string.guide2};

    @Override
    public int getContentView() {
        return R.layout.fragment_guide;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        List<View> views = new ArrayList<>();
        for (int i = 0; i < guidesImages.length; i++) {
            View view = getLayoutInflater().inflate( R.layout.layout_guide, null );
            ImageView img = view.findViewById( R.id.guide_page_content_img );
            TextView title = view.findViewById( R.id.guide_page_title );
            img.setImageResource( guidesImages[i] );
            title.setText( guidesTitles[i] );
            views.add( view );
        }
        mGuidePagerAdapter = new IndicatorPagerAdapter( mIndicators, mGuidePages, R.drawable.guide_indicator_point );
        mGuidePagerAdapter.setPages( views );
        mGuidePagerAdapter.notifyDataSetChanged();
    }

    @Override
    protected boolean hasPresenter() {
        return false;
    }


    @OnClick(R.id.guide_join_now)
    public void onClick(View v) {
        PrefsHelper.setString( Constants.Prefs.KEY_GUIDE_SHOWN, BuildConfig.VERSION_NAME );
        ((SplashActivity) getActivity()).next();
    }
}
