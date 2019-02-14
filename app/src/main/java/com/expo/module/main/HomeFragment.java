package com.expo.module.main;

import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
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
import com.blankj.utilcode.util.TimeUtils;
import com.expo.R;
import com.expo.base.BaseEventMessage;
import com.expo.base.BaseFragment;
import com.expo.base.ExpoApp;
import com.expo.base.utils.StatusBarUtils;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.HomeContract;
import com.expo.entity.AppInfo;
import com.expo.entity.Circum;
import com.expo.entity.CommonInfo;
import com.expo.entity.Encyclopedias;
import com.expo.entity.ExpoActivityInfo;
import com.expo.entity.RouteInfo;
import com.expo.entity.Schedule;
import com.expo.entity.ScheduleVenue;
import com.expo.entity.VrInfo;
import com.expo.map.LocationManager;
import com.expo.module.activity.ExpoActivityActivity;
import com.expo.module.ar.ArActivity;
import com.expo.module.circum.CircumHomeActivity;
import com.expo.module.circum.CircumListActivity;
import com.expo.module.freewifi.FreeWiFiActivity;
import com.expo.module.heart.MessageKindActivity;
import com.expo.module.main.adapter.HomeTopLineAdapter;
import com.expo.module.main.encyclopedia.EncyclopediaSearchActivity;
import com.expo.module.map.AMapServicesUtil;
import com.expo.module.map.PlayMapActivity;
import com.expo.module.online.OnlineExpoActivity;
import com.expo.module.routes.RouteDetailActivity;
import com.expo.module.routes.RoutesActivity;
import com.expo.module.service.TouristServiceActivity;
import com.expo.module.webview.WebActivity;
import com.expo.module.webview.WebExpoActivityActivity;
import com.expo.module.webview.WebTemplateActivity;
import com.expo.network.Http;
import com.expo.services.TrackRecordService;
import com.expo.utils.CommUtils;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static com.expo.base.BaseActivity.TITLE_COLOR_STYLE_WHITE;

/*
 * 首页
 */
public class HomeFragment extends BaseFragment<HomeContract.Presenter> implements HomeContract.View, View.OnClickListener {

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
    @BindView(R.id.service_qa)
    View mServiceQA;//服务

    private View mFindView;//游玩
    private View mActivitiesView;//活动
    private View mRouteView;//路线
    private View mScienceView;//人气景点
    private View mEatAndHotelView;//吃&住
    private View mPeripheryView;//周边
    private View mSelectedTabView;//

    private List<ExpoActivityInfo> mListTopLine;
    private String mOutsideFoodConfig;
    private HomeTopLineAdapter mAdapterTopLine;
    private GridLayoutFactory mGridLayoutFactory;
    boolean isLocation = false;
    boolean hasTabView2Top;
    private int topHeight = 0;
    private Location mLocation;
    private List<Circum> mCircum;

    LimitScrollerView.OnItemClickListener mTopLineListener = obj -> {
        ExpoActivityInfo expoActivityInfo = (ExpoActivityInfo) obj;
//        WebActivity.startActivity(getContext(),
//                LanguageUtil.chooseTest(topLine.linkH5Url, topLine.linkH5UrlEn),
//                getString(R.string.expo_headline));
        WebExpoActivityActivity.startActivity(getContext(), expoActivityInfo.getId());
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
                topHeight = mHtTitle.getBottom() + mTabView1.getHeight();
            }
            if (mEatAndHotelView != null && mEatAndHotelView.getTop() - scrollY - 15 <= topHeight) {
                setSelectedTab(R.id.home_tab1_3);
            } else if (mPeripheryView != null && mPeripheryView.getTop() - scrollY - 15 <= topHeight) {
                setSelectedTab(R.id.home_tab1_4);
            } else if (mServiceQA != null && mServiceQA.getTop() - scrollY - 15 <= topHeight) {
                setSelectedTab(R.id.home_tab1_2);
            } else if (mFindView != null && mFindView.getTop() - scrollY - 15 <= topHeight) {
                setSelectedTab(R.id.home_tab1_1);
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
        return R.layout.fragment_home;
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
        mPresenter.appRun();
        mPresenter.startHeartService(getContext());

        LocationManager.getInstance().registerLocationListener(mOnLocationChangeListener);//定位

        initLayout();
        mTabView2.findViewById(R.id.home_tab1_1).setOnClickListener(this);
        mTabView2.findViewById(R.id.home_tab1_2).setOnClickListener(this);
        mTabView2.findViewById(R.id.home_tab1_3).setOnClickListener(this);
        mTabView2.findViewById(R.id.home_tab1_4).setOnClickListener(this);
//        mTabView2.findViewById(R.id.home_tab1_science).setOnClickListener(this);
    }

    private void initLayout() {
        List<String> configs = loadConfig();
        if (configs == null || configs.isEmpty()) return;
        mGridLayoutFactory = new GridLayoutFactory(getContext(), bindViewListener);
        mGridLayoutFactory.setOnItemClickListener(itemClickListener);
        int marginTop = getResources().getDimensionPixelSize(R.dimen.dms_4);
        int padding, paddingV;
        int padding20 = getResources().getDimensionPixelSize(R.dimen.dms_20);
        View view;
        //主菜单
        List<int[]> menuData = mPresenter.loadMainMenuDate();
        if (menuData != null && !menuData.isEmpty()) {
            view = mGridLayoutFactory.getView(configs.get(0), menuData);
            padding = getResources().getDimensionPixelSize(R.dimen.dms_10);
            view.setPadding(padding, padding, padding, padding);
            ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).topMargin = marginTop;
            mContainer.addView(view, 2);
        }
        //发现
        List<Object> discover = mPresenter.loadDiscover();
        if (discover != null && !discover.isEmpty()) {
            mFindView = mGridLayoutFactory.getView(configs.get(1), discover);
            mFindView.setPadding(padding20, padding20, padding20, padding20);
            ((ViewGroup.MarginLayoutParams) mFindView.getLayoutParams()).topMargin = marginTop;
            mContainer.addView(mFindView, 6);
        }
        //活动
        List<Object> activityInfos = mPresenter.loadExpoActivities();
        if (activityInfos != null && !activityInfos.isEmpty()) {
            mActivitiesView = mGridLayoutFactory.getView(configs.get(2), activityInfos);
            mActivitiesView.setPadding(padding20, padding20, padding20, padding20);
            ((ViewGroup.MarginLayoutParams) mActivitiesView.getLayoutParams()).topMargin = marginTop;
            mContainer.addView(mActivitiesView, 7);
        }
        //推荐路线
        List<RouteInfo> routeInfos = mPresenter.loadRouteInfo();
        if (routeInfos != null && !routeInfos.isEmpty()) {
            mRouteView = mGridLayoutFactory.getView(configs.get(3), routeInfos);
            mRouteView.setPadding(padding20, padding20, padding20, padding20);
            ((ViewGroup.MarginLayoutParams) mRouteView.getLayoutParams()).topMargin = marginTop;
            mContainer.addView(mRouteView, 8);
        }
        //人气景点
        List<Object> encyclopedias = mPresenter.loadSciences();
        if (encyclopedias != null && !encyclopedias.isEmpty()) {
            mScienceView = mGridLayoutFactory.getView(configs.get(4), encyclopedias);
            padding = getResources().getDimensionPixelSize(R.dimen.dms_16);
            mScienceView.setPadding(padding, padding, padding, padding);
            ((ViewGroup.MarginLayoutParams) mScienceView.getLayoutParams()).topMargin = marginTop;
            mContainer.addView(mScienceView, 9);
        }
        //游世园
        List<Object> vrInfo = mPresenter.loadVrInfo();
        if (vrInfo != null && !vrInfo.isEmpty()) {
            view = mGridLayoutFactory.getView(configs.get(5), vrInfo);
            view.setPadding(padding20, padding20, padding20, padding20);
            ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).topMargin = marginTop;
            mContainer.addView(view, 10);
        }
        //预约
        List<Object> bespeak = mPresenter.loadBespeak();
        if (bespeak != null && !bespeak.isEmpty()) {
            view = mGridLayoutFactory.getView(configs.get(6), bespeak);
            view.setPadding(padding20, padding20, padding20, padding20);
            ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).topMargin = marginTop;
            mContainer.addView(view);
        }
        //世园美食
        List<Object> foods = mPresenter.loadExpoFoods();
        if (foods != null && !foods.isEmpty()) {
            mEatAndHotelView = mGridLayoutFactory.getView(configs.get(7), foods);
            padding = getResources().getDimensionPixelSize(R.dimen.dms_18);
            paddingV = getResources().getDimensionPixelSize(R.dimen.dms_14);
            mEatAndHotelView.setPadding(padding, paddingV, padding, paddingV);
            ((ViewGroup.MarginLayoutParams) mEatAndHotelView.getLayoutParams()).topMargin = marginTop;
            mContainer.addView(mEatAndHotelView);
        }
        //周边美食
        mOutsideFoodConfig = configs.get(8);
        mPresenter.loadOutsideFoods();
        //酒店
        List<Object> hotels = mPresenter.loadHotels();
        if (hotels != null && !hotels.isEmpty()) {
            view = mGridLayoutFactory.getView(configs.get(9), hotels);
            view.setPadding(padding20, padding20, padding20, padding20);
            ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).topMargin = marginTop;
            mContainer.addView(view);
        }
        //延庆体验
        List<Object> data = mPresenter.loadNearbyExperience();
        if (data != null && !data.isEmpty()) {
            mPeripheryView = mGridLayoutFactory.getView(configs.get(10), data);
            mPeripheryView.setPadding(padding20, padding20, padding20, padding20);
            ((ViewGroup.MarginLayoutParams) mPeripheryView.getLayoutParams()).topMargin = marginTop;
            mContainer.addView(mPeripheryView);
        }
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
                case R.layout.layout_home_title://头部
                    tv = item.findViewById(R.id.title);
                    TextView tvMore = item.findViewById(R.id.more);
                    tv.setText((Integer) obj);
                    int paddingBottom = 0;
                    if ("activities".equals(tag) || "discovery".equals(tag) || "routes".equals(tag)
                            || "play".equals(tag) || "periphery".equals(tag) || "bespeak".equals(tag)) {
                        paddingBottom = getResources().getDimensionPixelSize(R.dimen.dms_10);
                    }
                    if ("bespeak".equals(tag)) {
                        TextView tvTime = item.findViewById(R.id.describe);
                        tvTime.setText(new SimpleDateFormat("yyyy\u5e74MM\u6708dd\u65e5", Locale.getDefault()).format(new Date()));
                        tvTime.setVisibility(View.VISIBLE);
                        tvMore.setAutoLinkMask(Linkify.ALL);
                        tvMore.setCompoundDrawables(null, null, null, null);
                        tvMore.setTextColor(getResources().getColor(R.color.black_333333));
                        SpannableString content = new SpannableString(getString(R.string.enter));
                        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                        tvMore.setText(content);
                    }
                    tvMore.setTag(tag);
                    tvMore.setOnClickListener(moreClickListener);
                    if (paddingBottom != 0)
                        item.setPadding(0, 0, 0, paddingBottom);
                    break;
                case R.layout.layout_home_route_right:
                    tv = item.findViewById(R.id.title);
                    img = item.findViewById(R.id.img);
                    RouteInfo routeInfo = (RouteInfo) obj;
                    Http.loadImage(img, routeInfo.picUrl);
                    tv.setText(routeInfo.traitLabel);
                    break;
                case R.layout.layout_home_activities_forest:
                    img = item.findViewById(R.id.img);
                    img.setImageResource((Integer) obj);
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
                case R.layout.layout_home_route://推荐游园路线第一个
                    tv = item.findViewById(R.id.title);
                    img = item.findViewById(R.id.img);
                    TextView tagText = item.findViewById(R.id.tag);
                    RouteInfo info = (RouteInfo) obj;
                    tv.setText(LanguageUtil.chooseTest(info.caption, info.captionen));
                    tagText.setText(info.traitLabel);
                    Http.loadImage(img, info.picUrl);
                    break;
                case R.layout.layout_home_science:
                    tv = item.findViewById(R.id.title);
                    TextView describe = item.findViewById(R.id.describe);
                    TextView traitLabel = item.findViewById(R.id.tag);
                    img = item.findViewById(R.id.img);
                    Encyclopedias encyclopedias = (Encyclopedias) obj;
                    tv.setText(LanguageUtil.chooseTest(encyclopedias.caption, encyclopedias.captionEn));
                    traitLabel.setText(LanguageUtil.chooseTest(encyclopedias.getRecommendLang(), encyclopedias.getRecommendLangEn()));
                    describe.setText(LanguageUtil.chooseTest(encyclopedias.remark, encyclopedias.remarkEn));
                    Http.loadImage(img, encyclopedias.picUrl);
                    break;
                case R.layout.layout_home_bespeak:
                    tv = item.findViewById(R.id.title);
                    img = item.findViewById(R.id.img);
                    ProgressBar bar = item.findViewById(R.id.progress);
                    ScheduleVenue schedule = (ScheduleVenue) obj;
                    tv.setText(LanguageUtil.chooseTest(schedule.caption, schedule.captionEn));
                    bar.setMax(100);
                    bar.setProgress(schedule.percent);
                    if (schedule.percent <= 50)
                        bar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar_02cb99_ef));
                    else if (schedule.percent <= 80)
                        bar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar_ffcd00_ef));
                    else
                        bar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar_ff4848_ef));
                    img.setImageURI(Uri.parse(CommUtils.getFullUrl(schedule.pic)));
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
                    score.setText("口味：" + circum.getTaste() + "分");
                    starBar.setStarMark(circum.getAvgRating());
                    img.setImageURI(Uri.parse(circum.getPhotoUrls()));
                    distance.setText(AMapServicesUtil.calculateDistance(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()), new LatLng(circum.getLatitude(), circum.getLongitude())));
                    break;
                case R.layout.layout_home_hotel:
                    encyclopedias = (Encyclopedias) obj;
                    tv = item.findViewById(R.id.title);
                    img = item.findViewById(R.id.img);
                    tv.setText(LanguageUtil.chooseTest(encyclopedias.caption, encyclopedias.captionEn));
                    img.setImageURI(Uri.parse(CommUtils.getFullUrl(encyclopedias.picUrl)));
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
            } else if ("bespeak".equals(tag)) {//场馆预约
                ScheduleVenue sv = (ScheduleVenue) data;
                StringBuilder sb = new StringBuilder(mPresenter.loadBespeakUrlInfo())
                        .append("?time=")
                        .append(TimeUtils.getNowString(new SimpleDateFormat("yyyy-MM-dd")))
                        .append("&venid=")
                        .append(sv.id)
                        .append("&Uid=")
                        .append(ExpoApp.getApplication().getUser().getUid())
                        .append("&Ukey=")
                        .append(ExpoApp.getApplication().getUser().getUkey())
                        .append("&lan=")
                        .append(LanguageUtil.chooseTest("zh", "en"));
                WebActivity.startActivity(getContext(), sb.toString(), LanguageUtil.chooseTest(sv.caption, sv.captionEn), TITLE_COLOR_STYLE_WHITE);
            } else {
                if (data instanceof ExpoActivityInfo) {
                    ExpoActivityInfo info = (ExpoActivityInfo) data;
                    WebExpoActivityActivity.startActivity(getContext(), info.getId());
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
                ArActivity.startActivity(getContext());
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
        int widthSpec = View.MeasureSpec.makeMeasureSpec(getResources().getDisplayMetrics().widthPixels, View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(getResources().getDisplayMetrics().heightPixels, View.MeasureSpec.AT_MOST);
        mHomeAd.measure(widthSpec, heightSpec);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mHomeAd.getLayoutParams();
        lp.topMargin = -mHomeAd.getMeasuredHeight() / 2;
        mListTopLine = new ArrayList<>();
        mLsvScroll.setDataAdapter(mAdapterTopLine = new HomeTopLineAdapter(getContext()));
        mLsvScroll.setOnItemClickListener(mTopLineListener);
        mAdapterTopLine.setDatas(mListTopLine);
    }

    @OnClick({R.id.home_find_0, R.id.home_find_1, R.id.home_find_2,
            R.id.home_find_3, R.id.home_find_4, R.id.home_find_5, R.id.home_find_6})
    public void onFindClick(View v) {        // 找附近
        // 是否在园区内
        if (null == TrackRecordService.getLocation() || TrackRecordService.getLocation().getLatitude() == 0) {
            ToastHelper.showShort(R.string.trying_to_locate);
            return;
        }
        if (!mPresenter.checkInPark(TrackRecordService.getLocation().getLatitude(), TrackRecordService.getLocation().getLongitude())) {
            ToastHelper.showShort(R.string.unable_to_provide_service);
            return;
        }
        switch (v.getId()) {
            case R.id.home_find_0://卫生间
                goMapNavigation("\u536b\u751f\u95f4");
                break;
            case R.id.home_find_1://览车
                goMapNavigation("\u89c8\u8f66");
                break;
            case R.id.home_find_2://大门
                goMapNavigation("\u5927\u95e8");
                break;
            case R.id.home_find_3://服务
                goMapNavigation("\u670d\u52a1");
                break;
            case R.id.home_find_4://医疗
                goMapNavigation("\u533b\u7597");
                break;
            case R.id.home_find_5://母婴
                goMapNavigation("\u6bcd\u5a74");
                break;
            case R.id.home_find_6:
                FreeWiFiActivity.startActivity(getContext());
                break;
        }
    }

    @OnClick({R.id.ar, R.id.online_expo, R.id.service_qa, R.id.home_title_text, R.id.title_home_icon,
            R.id.title_home_msg, R.id.home_tab1_1,
            R.id.home_tab1_2, R.id.home_tab1_3, R.id.home_tab1_4})
    public void onClick(View view) {
        String url;
        switch (view.getId()) {
            case R.id.title_home_icon:
                WebActivity.startActivity(getContext(), CommUtils.getFullUrl(mPresenter.loadCommonInfo(CommonInfo.EXPO_BRIEF_INTRODUCTION)), getString(R.string.expo_intr));
                break;
            case R.id.title_home_msg:
                MessageKindActivity.startActivity(getContext());
                break;
            case R.id.home_title_text://搜索框
                EncyclopediaSearchActivity.startActivity(getContext());
                break;
            case R.id.ar://AR
                ArActivity.lunchPhotograph(getContext(), mPresenter.loadCommonInfo(CommonInfo.EXPO_AR_DOWNLOAD_PAGE));
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
            case R.id.home_tab1_1://滚动到游玩
                scrollTo(mFindView);
                break;
            case R.id.home_tab1_2://滚动到服务
                scrollTo(mServiceQA);
                break;
            case R.id.home_tab1_3://滚动到吃&住
                scrollTo(mEatAndHotelView);
                break;
            case R.id.home_tab1_4://滚动到周边
                scrollTo(mPeripheryView);
                break;
        }
    }

    //跳转地图导航到该类型最近的点
    private void goMapNavigation(String typeName) {
        PlayMapActivity.startActivity(getContext(), typeName);
    }

    private void scrollTo(View view) {
        int h = view.getTop() - topHeight + 10;
        mSvScroll.smoothScrollTo(0, h);
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    @Override
    public void showTopLine(List<ExpoActivityInfo> list) {
        if (isDataEmpty(mHomeAd, list)) return;
        mListTopLine.clear();
        mListTopLine.addAll(list);
        mLsvScroll.startScroll();
    }

    @Override
    public void setOutsideFoods(List<Circum> circums) {
        this.mCircum = circums;
        if (circums != null && mLocation != null) {
            View view = new View(getContext());
            view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
            view.setBackgroundColor(getResources().getColor(R.color.color_EEE));
            mContainer.addView(view, 17);
            view = mGridLayoutFactory.getView(mOutsideFoodConfig, circums);
            view.setBackgroundResource(R.color.white);
            int padding = getResources().getDimensionPixelSize(R.dimen.dms_16);
            int paddingV = getResources().getDimensionPixelSize(R.dimen.dms_25);
            view.setPadding(padding, padding, padding, paddingV);
            ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).topMargin = 1;
            mContainer.addView(view, 18);
        }
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

    @Override
    public void onDestroy() {
        if (null != mPresenter)
            mPresenter.stopHeartService(getContext());
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        LocationManager.getInstance().unregisterLocationListener(mOnLocationChangeListener);
    }

}
