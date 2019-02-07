package com.expo.module.main;

import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.expo.R;
import com.expo.base.BaseEventMessage;
import com.expo.base.BaseFragment;
import com.expo.base.ExpoApp;
import com.expo.base.utils.StatusBarUtils;
import com.expo.contract.HomeContract;
import com.expo.entity.AppInfo;
import com.expo.entity.Circum;
import com.expo.entity.CommonInfo;
import com.expo.entity.Encyclopedias;
import com.expo.entity.ExpoActivityInfo;
import com.expo.entity.RouteInfo;
import com.expo.entity.Schedule;
import com.expo.entity.TopLineInfo;
import com.expo.entity.VrInfo;
import com.expo.map.LocationManager;
import com.expo.module.activity.ExpoActivityActivity;
import com.expo.module.ar.ArActivity;
import com.expo.module.circum.CircumHomeActivity;
import com.expo.module.circum.CircumListActivity;
import com.expo.module.freewifi.FreeWiFiActivity;
import com.expo.module.main.adapter.HomeTopLineAdapter;
import com.expo.module.map.AMapServicesUtil;
import com.expo.module.online.OnlineExpoActivity;
import com.expo.module.routes.RouteDetailActivity;
import com.expo.module.routes.RoutesActivity;
import com.expo.module.service.TouristServiceActivity;
import com.expo.module.webview.WebActivity;
import com.expo.module.webview.WebTemplateActivity;
import com.expo.network.Http;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.expo.widget.LimitScrollerView;
import com.expo.widget.MyScrollView;
import com.expo.widget.StarBar;
import com.toolsmi.gridfactory.GridLayoutFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.expo.base.BaseActivity.TITLE_COLOR_STYLE_WHITE;

/*
 * 首页
 */
public class HomeFragment2 extends BaseFragment<HomeContract.Presenter> implements HomeContract.View, View.OnClickListener {

    @BindView(R.id.home_scroll)
    MyScrollView mSvScroll;
    @BindView(R.id.home_title)
    View mHtTitle;
    @BindView(R.id.title_home_msg_note)
    View mMsgNote;
    @BindView(R.id.home_ad)
    View mHomeAd;
    @BindView(R.id.home_ad_scroll)
    LimitScrollerView mLsvScroll;
    @BindView(R.id.home_tab1)
    View mTabView1;
    @BindView(R.id.home_tab2)
    ViewGroup mTabView2;
    @BindView(R.id.container)
    LinearLayout mContainer;

    private View mFindView;
    private View mActivitiesView;
    private View mRouteView;
    private View mScienceView;
    private View mPeripheryView;
    private View mSelectedTabView;

    private List<TopLineInfo> mListTopLine;
    private String mOutsideFoodConfig;
    private HomeTopLineAdapter mAdapterTopLine;
    private GridLayoutFactory mGridLayoutFactory;
    boolean isLocation = false;
    boolean hasTabView2Top;
    private int topHeight = 0;
    private Location mLocation;
    private List<Circum> mCircum;

    private long typeToilet;
    private long typeStation;
    private long typeGate;
    private long typeService;
    private long typeDeposit;
    private long typeMedical;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

    LimitScrollerView.OnItemClickListener mTopLineListener = obj -> {
        TopLineInfo topLine = (TopLineInfo) obj;
        WebActivity.startActivity(getContext(),
                LanguageUtil.chooseTest(topLine.linkH5Url, topLine.linkH5UrlEn),
                getString(R.string.expo_headline));
    };

    MyScrollView.OnScrollListener mScrollListener = new MyScrollView.OnScrollListener() {
        @Override
        public void onScroll(int scrollY) {
            //头颜色变化
            int color = Color.argb((int) Math.min(0xff,
                    Math.max(Float.valueOf(scrollY), 0.0f) / 2), 2, 205, 155);
            mHtTitle.setBackgroundColor(color);
            //tabview滚动显示控制
            if ((mTabView1.getTop() - scrollY) <= mHtTitle.getBottom()) {
                mTabView2.setVisibility(View.VISIBLE);
                if (!hasTabView2Top) {
                    hasTabView2Top = true;
                    mTabView2.setTranslationY(mHtTitle.getBottom());
                }
            } else {
                mTabView2.setVisibility(View.GONE);
            }
            //tab字颜色
            if (topHeight == 0) {
                topHeight = mHtTitle.getBottom() + mTabView1.getHeight() + 10;
            }
            if (mPeripheryView.getTop() - scrollY - 15 <= topHeight) {
                setSelectedTab(R.id.home__tab1__periphery);
            } else if (mScienceView.getTop() - scrollY - 15 <= topHeight) {
                setSelectedTab(R.id.home__tab1__science);
            } else if (mRouteView.getTop() - scrollY - 15 <= topHeight) {
                setSelectedTab(R.id.home__tab1__route);
            } else if (mActivitiesView.getTop() - scrollY - 15 <= topHeight) {
                setSelectedTab(R.id.home__tab1__activities);
            } else if (mFindView.getTop() - scrollY - 15 <= topHeight) {
                setSelectedTab(R.id.home__tab1__find);
            }
        }
    };

    private void setSelectedTab(int id) {
        if (mSelectedTabView != null) {
            mSelectedTabView.setSelected(false);
        }
        mSelectedTabView = mTabView2.findViewById(id);
        mSelectedTabView.setSelected(true);
    }


    private AMap.OnMyLocationChangeListener mOnLocationChangeListener = new AMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            if (isLocation) return;
            isLocation = true;
            mLocation = location;
            setOutsideFoods(mCircum);
        }
    };

    @Override
    public int getContentView() {
        return R.layout.fragment_home2;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mSvScroll.setOnScrollListener(mScrollListener);

        mHtTitle.setPadding(0, StatusBarUtils.getStatusBarHeight(getContext()), 0, 0);

        initRecyclerTop();
        EventBus.getDefault().register(this);
        mPresenter.checkUpdate();
        mPresenter.setMessageCount();
        mPresenter.setTopLine();
//        mPresenter.setVenue();
//        mPresenter.setHotActivity();
//        mPresenter.setRecommendRoute();
//        mPresenter.setRankingScenic();
//        mPresenter.setVr();
//        mPresenter.setFood();
//        mPresenter.setHotel();
        mPresenter.startHeartService(getContext());

        LocationManager.getInstance().registerLocationListener(mOnLocationChangeListener);//定位

        initLayout();
        mTabView2.findViewById(R.id.home__tab1__find).setOnClickListener(this);
        mTabView2.findViewById(R.id.home__tab1__activities).setOnClickListener(this);
        mTabView2.findViewById(R.id.home__tab1__periphery).setOnClickListener(this);
        mTabView2.findViewById(R.id.home__tab1__route).setOnClickListener(this);
        mTabView2.findViewById(R.id.home__tab1__science).setOnClickListener(this);
    }

    private void initLayout() {
        List<String> configs = loadConfig();
        if (configs == null || configs.isEmpty()) return;
        mGridLayoutFactory = new GridLayoutFactory(getContext(), bindViewListener);
        mGridLayoutFactory.setOnItemClickListener(itemClickListener);
        int marginTop = getResources().getDimensionPixelSize(R.dimen.dms_30);
        //主菜单
        List<int[]> menuData = mPresenter.loadMainMenuDate();
        View view = mGridLayoutFactory.getView(configs.get(0), menuData);
        view.setBackgroundResource(R.color.white);
        int padding = getResources().getDimensionPixelSize(R.dimen.dms_10);
        view.setPadding(padding, padding, padding, padding);
        ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).topMargin = marginTop;
        mContainer.addView(view, 2);
        //发现
        List<Object> discover = mPresenter.loadDiscover();
        mFindView = mGridLayoutFactory.getView(configs.get(1), discover);
        mFindView.setBackgroundResource(R.color.white);
        padding = getResources().getDimensionPixelSize(R.dimen.dms_20);
        mFindView.setPadding(padding, padding, padding, padding);
        ((ViewGroup.MarginLayoutParams) mFindView.getLayoutParams()).topMargin = marginTop;
        mContainer.addView(mFindView, 6);
        //活动
        List<Object> activityInfos = mPresenter.loadExpoActivities();
        mActivitiesView = mGridLayoutFactory.getView(configs.get(2), activityInfos);
        mActivitiesView.setBackgroundResource(R.color.white);
        padding = getResources().getDimensionPixelSize(R.dimen.dms_25);
        int paddingV = getResources().getDimensionPixelSize(R.dimen.dms_20);
        mActivitiesView.setPadding(padding, paddingV, padding, paddingV);
        ((ViewGroup.MarginLayoutParams) mActivitiesView.getLayoutParams()).topMargin = marginTop;
        mContainer.addView(mActivitiesView, 7);
        //推荐路线
        List<RouteInfo> routeInfos = mPresenter.loadRouteInfo();
        mRouteView = mGridLayoutFactory.getView(configs.get(3), routeInfos);
        mRouteView.setBackgroundResource(R.color.white);
        padding = getResources().getDimensionPixelSize(R.dimen.dms_26);
        mRouteView.setPadding(padding, padding, padding, padding);
        ((ViewGroup.MarginLayoutParams) mRouteView.getLayoutParams()).topMargin = marginTop;
        mContainer.addView(mRouteView, 8);
        //人气景点
        List<Object> encyclopedias = mPresenter.loadSciences();
        mScienceView = mGridLayoutFactory.getView(configs.get(4), encyclopedias);
        mScienceView.setBackgroundResource(R.color.white);
        padding = getResources().getDimensionPixelSize(R.dimen.dms_16);
        mScienceView.setPadding(padding, padding, padding, padding);
        ((ViewGroup.MarginLayoutParams) mScienceView.getLayoutParams()).topMargin = marginTop;
        mContainer.addView(mScienceView, 9);
        //游世园
        List<Object> vrInfo = mPresenter.loadVrInfo();
        view = mGridLayoutFactory.getView(configs.get(5), vrInfo);
        view.setBackgroundResource(R.color.white);
        padding = getResources().getDimensionPixelSize(R.dimen.dms_20);
        view.setPadding(padding, padding, padding, padding);
        ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).topMargin = marginTop;
        mContainer.addView(view, 10);
        //预约
        List<Object> bespeak = mPresenter.loadBespeak();
        view = mGridLayoutFactory.getView(configs.get(6), bespeak);
        view.setBackgroundResource(R.color.white);
        padding = getResources().getDimensionPixelSize(R.dimen.dms_26);
        view.setPadding(padding, padding, padding, padding);
        ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).topMargin = marginTop;
        mContainer.addView(view);
        //世园美食
        List<Object> foods = mPresenter.loadExpoFoods();
        mPeripheryView = mGridLayoutFactory.getView(configs.get(7), foods);
        mPeripheryView.setBackgroundResource(R.color.white);
        padding = getResources().getDimensionPixelSize(R.dimen.dms_8);
        int paddingTop = getResources().getDimensionPixelSize(R.dimen.dms_14);
        paddingV = getResources().getDimensionPixelSize(R.dimen.dms_10);
        mPeripheryView.setPadding(padding, paddingTop, padding, paddingV);
        ((ViewGroup.MarginLayoutParams) mPeripheryView.getLayoutParams()).topMargin = marginTop;
        mContainer.addView(mPeripheryView);
        //周边美食
        mOutsideFoodConfig = configs.get(8);
        mPresenter.loadOutsideFoods();
        //酒店
        List<Object> hotels = mPresenter.loadHotels();
        view = mGridLayoutFactory.getView(configs.get(9), hotels);
        view.setBackgroundResource(R.color.white);
        padding = getResources().getDimensionPixelSize(R.dimen.dms_25);
        paddingV = getResources().getDimensionPixelSize(R.dimen.dms_16);
        view.setPadding(padding, paddingV, padding, paddingV);
        ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).topMargin = marginTop;
        mContainer.addView(view);
        //延庆体验
        List<Object> data = mPresenter.loadNearbyExperience();
        view = mGridLayoutFactory.getView(configs.get(10), data);
        view.setBackgroundResource(R.color.white);
        padding = getResources().getDimensionPixelSize(R.dimen.dms_25);
        view.setPadding(padding, padding, padding, padding);
        ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).topMargin = marginTop;
        mContainer.addView(view);
        TextView end = new TextView(getContext());
        end.setText(R.string.list_end);
        end.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.font_24));
        end.setTextColor(getResources().getColor(R.color.gray_999999));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        padding = getResources().getDimensionPixelSize(R.dimen.dms_24);
        lp.topMargin = padding;
        lp.bottomMargin = padding;
        mContainer.addView(end, lp);
    }

    private List<String> loadConfig() {
        BufferedReader bis = null;
        try {
            bis = new BufferedReader(new InputStreamReader(getContext().getAssets().open("home_layout.config")));
            List<String> configs = new ArrayList<>();
            while (bis.ready()) {
                configs.add(bis.readLine());
            }
            return configs;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
            } catch (IOException e) {
            }
        }
        return null;
    }

    private GridLayoutFactory.OnBindViewListener bindViewListener = new GridLayoutFactory.OnBindViewListener() {

        @Override
        public void onBindView(String tag, View item, int layout, Object obj, int id) {
            switch (layout) {
                case R.layout.layout_home_discover://发现
                case R.layout.layout_home_experience://延庆必体验
                case R.layout.layout_home_menu_item://主菜单
                    int[] data = (int[]) obj;
                    TextView tv = item.findViewById(R.id.title);
                    tv.setText(data[0]);
                    if ("periphery".equals(tag) && id == 3) {
                        tv.setGravity(Gravity.LEFT);
                    }
                    ImageView img = item.findViewById(R.id.img);
                    img.setImageResource(data[1]);
                    break;
                case R.layout.layout_home_title:
                    tv = item.findViewById(R.id.title);
                    TextView tvMore = item.findViewById(R.id.more);
                    tv.setText((Integer) obj);
                    int paddingBottom = 0;
                    if ("activities".equals(tag)) {
                        paddingBottom = getResources().getDimensionPixelSize(R.dimen.dms_10);
                    } else if ("discovery".equals(tag)) {
                        paddingBottom = getResources().getDimensionPixelSize(R.dimen.dms_6);
                        tvMore.setVisibility(View.GONE);
                    } else if ("routes".equals(tag)) {
                        paddingBottom = getResources().getDimensionPixelSize(R.dimen.dms_22);
                    } else if ("play".equals(tag)) {
                        paddingBottom = getResources().getDimensionPixelSize(R.dimen.dms_10);
                    } else if ("periphery".equals(tag)) {
                        paddingBottom = getResources().getDimensionPixelSize(R.dimen.dms_20);
                    } else if ("bespeak".equals(tag)) {
                        paddingBottom = getResources().getDimensionPixelSize(R.dimen.dms_22);
                    }
                    if (tvMore.getVisibility() == View.VISIBLE) {
                        tvMore.setTag(tag);
                        tvMore.setOnClickListener(moreClickListener);
                    }
                    if (paddingBottom != 0)
                        item.setPadding(0, 0, 0, paddingBottom);
                    break;
                case R.layout.layout_home_single_img:
                    img = (ImageView) item;
                    if (obj instanceof RouteInfo) {
                        RouteInfo info = (RouteInfo) obj;
                        Http.loadImage(img, info.picUrl);
                    } else if (obj instanceof VrInfo) {
                        VrInfo info = (VrInfo) obj;
                        Http.loadImage(img, info.getPic());
                    } else {
                        img.setImageResource((Integer) obj);
                    }
                    break;
                case R.layout.layout_home_text_center:
                    tv = item.findViewById(R.id.title);
                    img = item.findViewById(R.id.img);
                    if (obj instanceof ExpoActivityInfo) {
                        ExpoActivityInfo info = (ExpoActivityInfo) obj;
                        tv.setText(LanguageUtil.chooseTest(info.getCaption(), info.getCaptionEn()));
                        Http.loadImage(img, info.getPicUrl());
                    } else if (obj instanceof RouteInfo) {
                        RouteInfo info = (RouteInfo) obj;
                        tv.setText(LanguageUtil.chooseTest(info.caption, info.captionen));
                        Http.loadImage(img, info.picUrl);
                    }
                    break;
                case R.layout.layout_home_route:
                    tv = item.findViewById(R.id.title);
                    img = item.findViewById(R.id.img);
                    RouteInfo info = (RouteInfo) obj;
                    tv.setText(LanguageUtil.chooseTest(info.caption, info.captionen) + "\n推荐最佳路线");
                    Http.loadImage(img, info.picUrl);
                    break;
                case R.layout.layout_home_science:
                    tv = item.findViewById(R.id.title);
                    TextView describe = item.findViewById(R.id.describe);
                    img = item.findViewById(R.id.img);
                    Encyclopedias encyclopedias = (Encyclopedias) obj;
                    tv.setText(LanguageUtil.chooseTest(encyclopedias.caption, encyclopedias.captionEn));
                    describe.setText(LanguageUtil.chooseTest(encyclopedias.remark, encyclopedias.remarkEn));
                    Http.loadImage(img, encyclopedias.picUrl);
                    break;
                case R.layout.layout_home_bespeak:
                    tv = item.findViewById(R.id.title);
                    img = item.findViewById(R.id.img);
                    ProgressBar bar = item.findViewById(R.id.progress);
                    Schedule schedule = (Schedule) obj;
                    tv.setText(LanguageUtil.chooseTest(schedule.getCaption(), schedule.getCaptionEn()));
                    bar.setMax(100);
                    bar.setProgress(50);
                    Http.loadImage(img, schedule.getPic());
                    break;
                case R.layout.layout_home_expo_food:
                    tv = item.findViewById(R.id.title);
                    img = item.findViewById(R.id.img);
                    encyclopedias = (Encyclopedias) obj;
                    tv.setText(LanguageUtil.chooseTest(encyclopedias.getCaption(), encyclopedias.getCaptionEn()));
                    img.setImageURI(Uri.parse(Constants.URL.FILE_BASE_URL + encyclopedias.getPicUrl()));
                    break;
                case R.layout.layout_home_outside_foods:
                    Circum circum = (Circum) obj;
                    tv = item.findViewById(R.id.title);
                    TextView score = item.findViewById(R.id.score);
                    TextView distance = item.findViewById(R.id.distance);
                    StarBar starBar = item.findViewById(R.id.rating);
                    img = item.findViewById(R.id.img);
                    tv.setText(circum.getName());
                    score.setText("评分：" + circum.getTaste() + "分");
                    starBar.setStarMark(circum.getAvgRating());
                    img.setImageURI(Uri.parse(circum.getPhotoUrls()));
                    String distanceStr = String.format("距离你：%s", AMapServicesUtil.calculateAzimuth(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()), new LatLng(circum.getLatitude(), circum.getLongitude())));
                    distance.setText(distanceStr);
                    break;
                case R.layout.layout_home_hotel:
                    encyclopedias = (Encyclopedias) obj;
                    tv = item.findViewById(R.id.title);
                    img = item.findViewById(R.id.img);
                    tv.setText(LanguageUtil.chooseTest(encyclopedias.caption, encyclopedias.captionEn));
                    Http.loadImage(img, encyclopedias.picUrl);
                    break;
            }
        }
    };

    private GridLayoutFactory.OnItemClickListener itemClickListener = new GridLayoutFactory.OnItemClickListener() {
        @Override
        public void onItemClick(String tag, View item, Object data) {
            if ("menu".equals(tag)) {
                doMenuItemClick(item, (int[]) data);
            } else if ("periphery".equals(tag)) {
                doPeripheryClick(item);
            } else {
                if (data instanceof ExpoActivityInfo) {
                    ExpoActivityInfo info = (ExpoActivityInfo) data;
                    WebActivity.startActivity(getContext(), LanguageUtil.chooseTest(info.getLinkH5Url(), info.getLinkH5UrLen()), LanguageUtil.chooseTest(info.getCaption(), info.getCaptionEn()));
                } else if (data instanceof RouteInfo) {
                    RouteInfo info = (RouteInfo) data;
                    RouteDetailActivity.startActivity(getContext(), info.id, info.caption);
                } else if (data instanceof Encyclopedias) {
                    Encyclopedias encyclopedias = (Encyclopedias) data;
                    WebTemplateActivity.startActivity(getContext(), encyclopedias.getId());
                } else if (data instanceof Schedule) {
                    lunchVenueBespeak();
                } else if (data instanceof Circum) {
                    Circum circum = (Circum) data;
                    WebActivity.startActivity(getContext(), circum.getBusinessUrl(), circum.getName());
                }
            }
        }
    };

    private void doPeripheryClick(View item) {
        switch (item.getId()) {
            case 3:
                CircumListActivity.startActivity(getContext(), 2);
                break;
            case 4:
                CircumListActivity.startActivity(getContext(), 4);
                break;
            case 7:
                CircumListActivity.startActivity(getContext(), 1);
                break;
            case 8:
                CircumListActivity.startActivity(getContext(), 3);
                break;
        }
    }

    private View.OnClickListener moreClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String tag = (String) v.getTag();
            if ("activities".equals(tag)) {
                ExpoActivityActivity.startActivity(getContext());
            } else if ("routes".equals(tag)) {
                RoutesActivity.startActivity(getContext());
            } else if ("scenic".equals(tag)) {
                ((MainActivity) getContext()).goScenicAndShowTab(getString(R.string.find_tab_scenic));
            } else if ("play".equals(tag)) {
                WebActivity.startActivity(getContext(),
                        mPresenter.loadCommonInfo(CommonInfo.PORTAL_WEBSITE_INTEGRATION),
                        getString(R.string.home_func_item_microcosmic));
            } else if ("bespeak".equals(tag)) {
                lunchVenueBespeak();
            } else if ("expo_foods".equals(tag)) {
                ((MainActivity) getContext()).goScenicAndShowTab(getString(R.string.find_tab_food));
            } else if ("hotels".equals(tag)) {
                ((MainActivity) getContext()).goScenicAndShowTab(getString(R.string.hotel));
            } else if ("periphery".equals(tag)) {
                CircumHomeActivity.startActivity(getContext());
            }
        }
    };

    //打开预约页面
    private void lunchVenueBespeak() {
        String url = mPresenter.loadCommonInfo(CommonInfo.VENUE_BESPEAK);
        WebActivity.startActivity(getContext(), TextUtils.isEmpty(url) ? Constants.URL.HTML_404 :
                url + "?Uid=" + ExpoApp.getApplication().getUser().getUid() + "&Ukey=" + ExpoApp.getApplication().getUser().getUkey()
                        + "&lan=" + LanguageUtil.chooseTest("zh", "en"), getString(R.string.home_func_item_appointment), TITLE_COLOR_STYLE_WHITE); //场馆预约
    }

    private void doMenuItemClick(View item, int[] data) {
        switch (item.getId()) {
            case 0:
                ((MainActivity) getContext()).goScenic();
                break;
            case 1:
                ExpoActivityActivity.startActivity(getContext());
                break;
            case 2:
                CircumHomeActivity.startActivity(getContext());
                break;
            case 3:
                ArActivity.lunchPhotograph(getContext(), mPresenter.loadCommonInfo(CommonInfo.EXPO_AR_DOWNLOAD_PAGE));
                break;
            case 4:
                ((MainActivity) getContext()).goScenicMap();
                break;
            case 5:
                TouristServiceActivity.startActivity(getContext()); //游客服务
                break;
            case 6:
                String url = mPresenter.loadCommonInfo(CommonInfo.BUY_TICKETS);
                WebActivity.startActivity(getContext(), TextUtils.isEmpty(url) ? Constants.URL.HTML_404 :
                        (url + "?phone=" + ExpoApp.getApplication().getUser().getMobile() + "&lang=" + LanguageUtil.chooseTest("zh", "en")), getString(R.string.buy_tickets));  //购票
                break;
            case 7:
                lunchVenueBespeak();
                break;
        }
    }

    @Override
    public boolean isNeedPaddingTop() {
        return false;
    }

    private void initRecyclerTop() {
        mListTopLine = new ArrayList<>();
        mLsvScroll.setDataAdapter(mAdapterTopLine = new HomeTopLineAdapter(getContext()));
        mLsvScroll.setOnItemClickListener(mTopLineListener);
        mAdapterTopLine.setDatas(mListTopLine);
    }

    @OnClick({R.id.ar, R.id.online_expo, R.id.service_qa, R.id.home__tab1__find,
            R.id.home__tab1__activities, R.id.home__tab1__route, R.id.home__tab1__science,
            R.id.home__tab1__periphery, R.id.home_find_0, R.id.home_find_1, R.id.home_find_2,
            R.id.home_find_3, R.id.home_find_4, R.id.home_find_5, R.id.home_find_6})
    public void onClick(View view) {
        String url;
        switch (view.getId()) {
            case R.id.ar://AR
                ArActivity.startActivity(getContext());
                break;
            case R.id.online_expo://网上世园
                OnlineExpoActivity.startActivity(getContext());
                break;
            case R.id.service_qa://问询咨询
                url = mPresenter.loadCommonInfo(CommonInfo.TOURIST_SERVICE_LOST_AND_FOUND);
                if (url != null) {
                    String title = getString(R.string.item_tourist_service_text_0);
                    WebActivity.startActivity(getContext(), TextUtils.isEmpty(url) ? Constants.URL.HTML_404 : url, title, TITLE_COLOR_STYLE_WHITE, false);
                }
                break;
            case R.id.home__tab1__find://滚动到发现
                scrollTo(mFindView);
                break;
            case R.id.home__tab1__activities://滚动到活动
                scrollTo(mActivitiesView);
                break;
            case R.id.home__tab1__route://滚动到路线
                scrollTo(mRouteView);
                break;
            case R.id.home__tab1__science://滚动到景点
                scrollTo(mScienceView);
                break;
            case R.id.home__tab1__periphery://滚动到周边
                scrollTo(mPeripheryView);
                break;
            case R.id.home_find_0:
                if (typeToilet == 0) {//卫生间
                    typeToilet = mPresenter.loadTypeId("\u536b\u751f\u95f4");
                }
                if (typeToilet != 0)
                    goMapNavigation(typeToilet);
                break;
            case R.id.home_find_1:
                if (typeStation == 0) {//车站
                    typeStation = mPresenter.loadTypeId("\u8f66\u7ad9");
                }
                if (typeStation != 0)
                    goMapNavigation(typeStation);
                break;
            case R.id.home_find_2:
                if (typeGate == 0) {//大门
                    typeGate = mPresenter.loadTypeId("\u5927\u95e8");
                }
                if (typeGate != 0)
                    goMapNavigation(typeGate);
                break;
            case R.id.home_find_3:
                if (typeDeposit == 0) {//寄存
                    typeDeposit = mPresenter.loadTypeId("\u5bc4\u5b58");
                }
                if (typeDeposit != 0)
                    goMapNavigation(typeDeposit);
                break;
            case R.id.home_find_4:
                if (typeService == 0) {//服务
                    typeService = mPresenter.loadTypeId("\u670d\u52a1");
                }
                if (typeService != 0)
                    goMapNavigation(typeService);
                break;
            case R.id.home_find_5:
                if (typeMedical == 0) {//医疗
                    typeMedical = mPresenter.loadTypeId("\u533b\u7597");
                }
                if (typeMedical != 0)
                    goMapNavigation(typeMedical);
                break;
            case R.id.home_find_6:
                FreeWiFiActivity.startActivity(getContext());
                break;
        }
    }

    //跳转地图导航到该类型最近的点
    private void goMapNavigation(long typeId) {

    }

    private void scrollTo(View view) {
        int h = view.getTop() - topHeight;
        mSvScroll.smoothScrollTo(0, h);
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    @Override
    public void showTopLine(List<TopLineInfo> list) {
        if (isDataEmpty(mHomeAd, list)) return;
        mListTopLine.clear();
        mListTopLine.addAll(list);
        mLsvScroll.startScroll();
    }

    @Override
    public void showVenue(List<Encyclopedias> list) {

    }

    @Override
    public void showActivity(List<ExpoActivityInfo> list) {

    }

    @Override
    public void showRoute(List<RouteInfo> list) {

    }

    @Override
    public void showRankingScenic(List<Encyclopedias> list) {

    }

    @Override
    public void showVr(List<VrInfo> list) {

    }

    @Override
    public void showFood(List<Encyclopedias> list) {

    }

    @Override
    public void showHotel(List<Encyclopedias> list) {

    }

    @Override
    public void setOutsideFoods(List<Circum> circums) {
        this.mCircum = circums;
        if (circums != null && mLocation != null) {
            View view = mGridLayoutFactory.getView(mOutsideFoodConfig, circums);
            view.setBackgroundResource(R.color.white);
            int padding = getResources().getDimensionPixelSize(R.dimen.dms_16);
            int paddingV = getResources().getDimensionPixelSize(R.dimen.dms_25);
            view.setPadding(padding, padding, padding, paddingV);
            ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).topMargin = 1;
            mContainer.addView(view, 16);
        }
    }

    public void sort(List<Encyclopedias> list, LatLng latLng) {
        for (int i = 0; list != null && i < list.size(); i++) {
            Encyclopedias venue = list.get(i);
            venue.setDistance(mPresenter.getDistance(venue.id, latLng));
        }
        mPresenter.sortVenue(list);
    }

    @Override
    public void appUpdate(AppInfo appInfo) {
        mPresenter.update(getContext(), appInfo);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventBusMsgOnMainThread(BaseEventMessage baseEventMessage) {
        switch (baseEventMessage.id) {
            case Constants.EventBusMessageId.EVENTBUS_ID_HEART_MESSAGE_UNREAD_COUNT:
                mMsgNote.setVisibility(((Long) baseEventMessage.t) == 0L ? View.GONE : View.VISIBLE);
                break;
        }
    }

    private boolean isDataEmpty(View view, List list) {
        boolean empty = list == null || list.size() == 0;
        if (empty) view.setVisibility(View.GONE);
        else view.setVisibility(View.VISIBLE);
        return empty;
    }

    // 推荐场馆
    private void addVenueView(int position, int width, int height) {

    }

    // 热门活动
    private void addHotActivityFirst() {

    }

    private void addHotActivity(int position) {

    }

    // 推荐游园路线
    private void addRoute1() {

    }

    private void addRoute2() {

    }

    // 推荐游园路线
    private void addRouteBottom(int position) {

    }

    // 世园美食
    private void addFood(int position) {

    }

    // 世园美食
    private void addVr(int position) {

    }

    // 世园会三大特色酒店
    private void addHotelView(int position) {

    }

    @Override
    public void onDestroy() {
        if (null != mPresenter)
            mPresenter.stopHeartService(getContext());
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        LocationManager.getInstance().unregisterLocationListener(mOnLocationChangeListener);
    }
}
