package com.expo.module.main;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.StringUtils;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.BaseEventMessage;
import com.expo.base.BaseFragment;
import com.expo.base.ExpoApp;
import com.expo.base.utils.StatusBarUtils;
import com.expo.contract.HomeContract;
import com.expo.entity.AppInfo;
import com.expo.entity.CommonInfo;
import com.expo.entity.Encyclopedias;
import com.expo.entity.ExpoActivityInfo;
import com.expo.entity.RouteInfo;
import com.expo.entity.TopLineInfo;
import com.expo.entity.VrInfo;
import com.expo.map.LocationManager;
import com.expo.module.activity.ExpoActivityActivity;
import com.expo.module.ar.ArActivity;
import com.expo.module.circum.CircumHomeActivity;
import com.expo.module.heart.MessageKindActivity;
import com.expo.module.main.adapter.HomeTopLineAdapter;
import com.expo.module.main.encyclopedia.EncyclopediaSearchActivity;
import com.expo.module.online.OnlineExpoActivity;
import com.expo.module.online.detail.VRDetailActivity;
import com.expo.module.online.detail.VRImageActivity;
import com.expo.module.routes.RouteDetailActivity;
import com.expo.module.routes.RoutesActivity;
import com.expo.module.service.TouristServiceActivity;
import com.expo.module.webview.WebActivity;
import com.expo.module.webview.WebExpoActivityActivity;
import com.expo.module.webview.WebTemplateActivity;
import com.expo.utils.CommUtils;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.expo.widget.LimitScrollerView;
import com.expo.widget.MyScrollView;
import com.expo.widget.decorations.SpaceDecoration;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/*
 * 首页
 */
public class HomeFragment extends BaseFragment<HomeContract.Presenter> implements HomeContract.View {

    @BindView(R.id.home_scroll)
    MyScrollView mSvScroll;
    @BindView(R.id.home_title)
    View mHtTitle;
    @BindView(R.id.title_home_msg_note)
    View mMsgNote;
    @BindView(R.id.home_ad)
    View mHomeAd;
    @BindView(R.id.home_venue)
    View mHomeVenue;
    @BindView(R.id.home_hot_activity)
    View mHomeActivity;
    //    @BindView(R.id.home_exhibit)
//    View mHomeExhibit;
//    @BindView(R.id.home_exhibit_garden)
//    View mHomeExhibitGarden;
    @BindView(R.id.home_ad_scroll)
    LimitScrollerView mLsvScroll;
    @BindView(R.id.home_venue_layout)
    LinearLayout mLlVenue;
    @BindView(R.id.hot_activity_layout)
    LinearLayout mLlActivity;
    //    @BindView(R.id.recycler_exhibit)
//    RecyclerView mRvExhibit;
//    @BindView(R.id.recycler_exhibit_garden)
//    RecyclerView mRvExhibitGarden;
    @BindView(R.id.home_tab1)
    View mTabView1;
    @BindView(R.id.home_tab2)
    View mTabView2;

    // 推荐活动

    @BindView(R.id.home_hot_activity_firsh)
    ImageView mActivityFirsh;
    // 推荐游园路线
    @BindView(R.id.home_route)
    View mRoute;
    @BindView(R.id.home_route_top_left)
    View mRouteLeft;
    @BindView(R.id.home_route_img1)
    ImageView mIvRouteImg1;
    @BindView(R.id.home_route_text1)
    TextView mTvRouteText1;
    @BindView(R.id.home_route_img2)
    ImageView mIvRouteImg2;
    @BindView(R.id.hot_route_layout2)
    LinearLayout mLlRoute;
    @BindView(R.id.home_ranking)
    View mRankingView;
    // 推荐游园路线
    @BindView(R.id.home_ranking_recycler)
    RecyclerView mRvRanking;
    // 推荐游园路线
    @BindView(R.id.home_food_layout)
    LinearLayout mLlFood;
    //达人
    @BindView(R.id.home_daren_layout)
    LinearLayout mLlDaren;
    // 世园会三大特色酒店
    @BindView(R.id.home_hotel)
    View mHotelView;
    @BindView(R.id.home_hotel_layout)
    LinearLayout mLlHotel;

    List<TopLineInfo> mListTopLine;
    List<Encyclopedias> mListVenue;
    List<ExpoActivityInfo> mListActivity;
    List<RouteInfo> mListRoute;
    List<Encyclopedias> mListFood;
    List<Encyclopedias> mListHotel;
    List<VrInfo> mListDaren;
    //    List<Encyclopedias> mListExhibit;
//    List<Encyclopedias> mListExhibitGarden;
    List<Encyclopedias> mListRanking;

    HomeTopLineAdapter mAdapterTopLine;
    //    HomeExhibitAdapter mAdapterExhibit;
//    CommonAdapter<Encyclopedias> mAdapterExhibitGarden;
    CommonAdapter<Encyclopedias> mAdapterRanking;

    boolean isLocation = false;

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
//            int color = Color.argb((int) Math.min(0xff,
////                    Math.max(Float.valueOf(scrollY), 0.0f) / 2), 2, 205, 155);
////            mHtTitle.setBackgroundColor(color);

            if (mTabView1.getTop() - StatusBarUtils.getStatusBarHeight(getContext()) < Math.abs(scrollY)) {
                mTabView2.setVisibility(View.VISIBLE);
            } else {
                mTabView2.setVisibility(View.GONE);
            }
        }
    };

    private AMap.OnMyLocationChangeListener mOnLocationChangeListener = new AMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            if (isLocation) return;
            isLocation = true;
            sort(mListVenue, new LatLng(location.getLatitude(), location.getLongitude()));
            showVenue(mListVenue);
//            sort(mListExhibit, new LatLng(location.getLatitude(), location.getLongitude()));
//            mAdapterExhibit.notifyDataSetChanged();
//            sort(mListExhibitGarden, new LatLng(location.getLatitude(), location.getLongitude()));
//            mAdapterExhibitGarden.notifyDataSetChanged();
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
//        initRecyclerExhibit();
//        initRecyclerExhibitGarden();
        initRecyclerRanking();
        EventBus.getDefault().register(this);

        int height = (int) (StatusBarUtils.getStatusBarHeight(getContext()) + getResources().getDimension(R.dimen.dms_166));
        ((ViewGroup.LayoutParams) mTabView2.getLayoutParams()).height = height;
        mTabView2.setPaddingRelative(0, StatusBarUtils.getStatusBarHeight(getContext()), 0, 0);
        mPresenter.checkUpdate();
        mPresenter.setMessageCount();
        mPresenter.setTopLine();
        mPresenter.setVenue();
        mPresenter.setHotActivity();
        mPresenter.setRecommendRoute();
        mPresenter.setRankingScenic();
        mPresenter.setVr();
        mPresenter.setFood();
        mPresenter.setHotel();
//        mPresenter.setExhibit();
//        mPresenter.setExhibitGarden();
        mPresenter.startHeartService(getContext());
        mPresenter.startHeartService(getContext());

        LocationManager.getInstance().registerLocationListener(mOnLocationChangeListener);//定位
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

//    private void initRecyclerExhibit() {
//        mListExhibit = new ArrayList<>();
//        mRvExhibit.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
////        mRvExhibit.addItemDecoration(new SpaceDecoration(0, 0, 0, 0, (int) getContent().getResources().getDimension(R.dimen.dms_20), 0));
//        new FixLinearSnapHelper().attachToRecyclerView(mRvExhibit);
//        mRvExhibit.setAdapter(mAdapterExhibit = new HomeExhibitAdapter(getContext()));
//        mAdapterExhibit.setData(mListExhibit);
//    }

//    private void initRecyclerExhibitGarden() {
//        mListExhibitGarden = new ArrayList<>();
//        mRvExhibitGarden.setLayoutManager(new LinearLayoutManager(getContext()));
//        mRvExhibitGarden.addItemDecoration(new SpaceDecoration(0, 0, (int) getContent().getResources().getDimension(R.dimen.dms_4), (int) getContent().getResources().getDimension(R.dimen.dms_20), 0));
//        mRvExhibitGarden.setAdapter(mAdapterExhibitGarden = new CommonAdapter<Encyclopedias>(getContext(), R.layout.item_home_exhibit_garden, mListExhibitGarden) {
//            @Override
//            protected void convert(ViewHolder holder, Encyclopedias encyclopedias, int position) {
//                Picasso.with(getContext()).load(CommUtils.getFullUrl(encyclopedias.picUrl)).placeholder(R.drawable.image_default).error(R.drawable.image_default).into((ImageView) holder.getView(R.id.home_exhibit_garden_img));
//                holder.setText(R.id.home_exhibit_garden_name, LanguageUtil.chooseTest(encyclopedias.caption, encyclopedias.captionEn));
//                holder.setText(R.id.home_exhibit_garden_content, LanguageUtil.chooseTest(encyclopedias.remark, encyclopedias.remarkEn));
//                holder.itemView.setOnClickListener(v -> WebTemplateActivity.startActivity(mContext, encyclopedias.getId()));
//            }
//
//            @Override
//            public int getItemCount() {
//                return Math.min(2, super.getItemCount());
//            }
//        });
//    }

    private void initRecyclerRanking() {
        mListRanking = new ArrayList<>();
        mRvRanking.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvRanking.addItemDecoration(new SpaceDecoration(0, (int) getContent().getResources().getDimension(R.dimen.dms_30), 0, 0, 0));
        mRvRanking.setAdapter(mAdapterRanking = new CommonAdapter<Encyclopedias>(getContext(), R.layout.item_home_ranking, mListRanking) {
            @Override
            protected void convert(ViewHolder holder, Encyclopedias encyclopedias, int position) {
                Picasso.with(getContext()).load(CommUtils.getFullUrl(encyclopedias.picUrl)).placeholder(R.drawable.image_default).error(R.drawable.image_default).into((ImageView) holder.getView(R.id.item_home_ranking_img));
                holder.setText(R.id.item_home_ranking_text1, LanguageUtil.chooseTest(encyclopedias.caption, encyclopedias.captionEn));
                holder.setText(R.id.item_home_ranking_text2, LanguageUtil.chooseTest(encyclopedias.caption, encyclopedias.captionEn));
                holder.setText(R.id.item_home_ranking_text3, LanguageUtil.chooseTest(encyclopedias.remark, encyclopedias.remarkEn));
                holder.itemView.setOnClickListener(v -> WebTemplateActivity.startActivity(mContext, encyclopedias.getId()));
//                Picasso.with(getContext()).load(R.mipmap.item_home_ranking).placeholder(R.drawable.image_default).error(R.drawable.image_default).into((ImageView) holder.getView(R.id.item_home_ranking_img));
//                holder.setText(R.id.item_home_ranking_text1, "核心亮点场馆");
//                holder.setText(R.id.item_home_ranking_text2, "植物馆");
//                holder.setText(R.id.item_home_ranking_text3, "#植物馆建筑面积约10,000平方米，建筑设计理念为“升起的...");
            }

            @Override
            public int getItemCount() {
                return mListRanking.size();
            }
        });
    }

    @OnClick(R.id.title_home_msg)
    public void clickTitleMsg(View view) {
        MessageKindActivity.startActivity(getContext());
    }

    @OnClick(R.id.home_title_text)
    public void clickTitleText(View view) {
        EncyclopediaSearchActivity.startActivity(getContext());
    }

    @OnClick({R.id.home_func_0, R.id.home_func_1, R.id.home_func_2, R.id.home_func_3, R.id.home_func_4,
            R.id.home_func_5, R.id.home_func_6, R.id.home_func_7,
            R.id.title_home_icon, R.id.home_map_img, R.id.daren_img2})
    public void clickFunc(View view) {
        String url;
        switch (view.getId()) {
            case R.id.home_func_0:
//                FreeWiFiActivity.startActivity(getContext()); //免费wifi
//                CircumHomeActivity.startActivity(getContext());     //周边服务（暂占用按钮）
                ((MainActivity) getContext()).goScenic();
                break;
            case R.id.home_func_1:
//                WebActivity.startActivity(getContext(), mPresenter.loadCommonInfo(CommonInfo.PORTAL_WEBSITE_INTEGRATION),
//                        getString(R.string.home_func_item_panorama), BaseActivity.TITLE_COLOR_STYLE_WHITE); //微观世界
                ExpoActivityActivity.startActivity(getContext());
                break;
            case R.id.home_func_2:
//                ExpoActivityActivity.startActivity(getContext());//世园活动
//                WebActivity.startActivity( getContext(), mPresenter.loadCommonInfo( CommonInfo.PANORAMA ), null, false );
                CircumHomeActivity.startActivity(getContext());
                break;
            case R.id.home_func_3:
//                ParkMapActivity.startActivity(getContext(), null);  //导航导览
//                ArActivity.startActivity(getContext());
                ArActivity.lunchPhotograph(getContext(), mPresenter.loadCommonInfo(CommonInfo.EXPO_AR_DOWNLOAD_PAGE));
                break;
            case R.id.home_func_4://智玩
//                RoutesActivity.startActivity(getContext());//游览路线
                ((MainActivity) getContext()).goScenicMap();
                break;

            case R.id.home_func_5:
                TouristServiceActivity.startActivity(getContext()); //游客服务
                break;

            case R.id.home_func_6:
                url = mPresenter.loadCommonInfo(CommonInfo.BUY_TICKETS);
                WebActivity.startActivity(getContext(), TextUtils.isEmpty(url) ? Constants.URL.HTML_404 :
                        (url + "?phone=" + ExpoApp.getApplication().getUser().getMobile() + "&lang=" + LanguageUtil.chooseTest("zh", "en")), getString(R.string.buy_tickets));  //购票
                break;
            case R.id.home_func_7:
                url = mPresenter.loadCommonInfo(CommonInfo.VENUE_BESPEAK);
//                url = "http://192.168.1.13:8888/";
                WebActivity.startActivity(getContext(), TextUtils.isEmpty(url) ? Constants.URL.HTML_404 :
                        url + "?Uid=" + ExpoApp.getApplication().getUser().getUid() + "&Ukey=" + ExpoApp.getApplication().getUser().getUkey()
                                + "&lan=" + LanguageUtil.chooseTest("zh", "en"), getString(R.string.home_func_item_appointment), BaseActivity.TITLE_COLOR_STYLE_WHITE); //场馆预约
                break;
//            case R.id.home_navigation_item:
//                ParkMapActivity.startActivity(getContext(), null);
//                break;
            case R.id.home_map_img:
            case R.id.title_home_icon:
                url = mPresenter.loadCommonInfo(CommonInfo.EXPO_BRIEF_INTRODUCTION);
                WebActivity.startActivity(getContext(), TextUtils.isEmpty(url) ? Constants.URL.HTML_404 : url, getString(R.string.beijing_world_expo_2019), 1);
                break;
            case R.id.daren_img2://AR乐拍
                ArActivity.startActivity(getContext());
                break;
            case R.id.daren_img1://网上世园
                OnlineExpoActivity.startActivity(getContext());
                break;
        }
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
        if (isDataEmpty(mHomeVenue, list)) return;
        mListVenue = list;
        mLlVenue.removeAllViews();
        int width = (ScreenUtils.getScreenWidth() - (int) getContent().getResources().getDimension(R.dimen.dms_60) - (int) getContent().getResources().getDimension(R.dimen.dms_20) * 3) / 3;
        int height = (int) (width * 0.86f);
        for (int i = 0; i < 3; i++) {
            addVenueView(i, width, height);
        }
    }

    @Override
    public void showActivity(List<ExpoActivityInfo> list) {
        if (isDataEmpty(mHomeActivity, list)) return;
        mLlActivity.removeAllViews();
        mListActivity = list;
        for (int i = 0; list != null && i < list.size() || i < 4; i++) {
            if (i == 0)
                addHotActivityFirst();
            else
                addHotActivity(i);
        }
    }

    @Override
    public void showRoute(List<RouteInfo> list) {
        if (isDataEmpty(mRoute, list)) return;
        mListRoute = list;
        for (int i = 0; list != null && i < list.size() || i < 5; i++) {
            if (i == 0) {
                addRoute1();
            } else if (i == 1) {
                addRoute2();
            } else {
                addRouteBottom(i);
            }
        }
    }

    @Override
    public void showRankingScenic(List<Encyclopedias> list) {
        if (isDataEmpty(mRankingView, list)) return;
        mListRanking.clear();
        mListRanking.addAll(list);
        mAdapterRanking.notifyDataSetChanged();
    }

    @Override
    public void showVr(List<VrInfo> list) {
        if (isDataEmpty(mLlDaren, list)) return;
        mListDaren = list;
        for (int i = 0; i < 2; i++)
            addVr(i);
    }

    @Override
    public void showFood(List<Encyclopedias> list) {
        if (isDataEmpty(mLlFood, list)) return;
        mListFood = list;
        for (int i = 0; i < 5; i++)
            addFood(i);
    }

    @Override
    public void showHotel(List<Encyclopedias> list) {
        if (isDataEmpty(mHotelView, list)) return;
        mListHotel = list;
        for (int i = 0; i < 3; i++)
            addHotelView(i);
    }

    public void sort(List<Encyclopedias> list, LatLng latLng) {
        for (int i = 0; list != null && i < list.size(); i++) {
            Encyclopedias venue = list.get(i);
            venue.setDistance(mPresenter.getDistance(venue.id, latLng));
        }
        mPresenter.sortVenue(list);
    }

//    @Override
//    public void showExhibit(List<Encyclopedias> list) {
//        if (isDataEmpty(mHomeExhibit, list)) return;
//        mListExhibit.clear();
//        mListExhibit.addAll(list);
//        mAdapterExhibit.notifyDataSetChanged();
//        if (mAdapterExhibit.getItemCount() > 1)
//            mRvExhibit.scrollToPosition(Integer.MAX_VALUE / 2);
//    }

//    @Override
//    public void showExhibitGarden(List<Encyclopedias> list) {
//        if (isDataEmpty(mHomeExhibitGarden, list)) return;
//        mListExhibitGarden.clear();
//        mListExhibitGarden.addAll(list);
//        mAdapterExhibitGarden.notifyDataSetChanged();
//    }

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
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_home_venue, null);
        if (mListVenue.size() > position) {
            Encyclopedias encyclopedias = mListVenue.get(position);
            ImageView imageView = view.findViewById(R.id.item_home_venue_img);
            imageView.getLayoutParams().width = width;
            imageView.getLayoutParams().height = height;
            Picasso.with(getContext()).load(CommUtils.getFullUrl(encyclopedias.picUrl)).placeholder(R.drawable.image_default).into(imageView);
            ((TextView) view.findViewById(R.id.item_home_venue_text)).setText(LanguageUtil.chooseTest(encyclopedias.caption, encyclopedias.captionEn));
            view.setOnClickListener(v -> WebTemplateActivity.startActivity(getContext(), encyclopedias.getId()));
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, -2);
        params.weight = 1;
        if (position != 0) {
            params.leftMargin = (int) getContent().getResources().getDimension(R.dimen.dms_20);
        }
        view.setLayoutParams(params);
        mLlVenue.addView(view);

    }

    // 热门活动
    private void addHotActivityFirst() {
        Picasso.with(getContext()).load(CommUtils.getFullUrl(mListActivity.get(0).getPicUrl())).into(mActivityFirsh);
    }

    private void addHotActivity(int position) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_home_hot_activity, null);
        if (mListActivity.size() > position) {
            ExpoActivityInfo info = mListActivity.get(position);
            ImageView imageView = view.findViewById(R.id.item_home_activity_img);
            Picasso.with(getContext()).load(CommUtils.getFullUrl(info.getPicUrl())).placeholder(R.drawable.image_default).into(imageView);
//            ((TextView) view.findViewById(R.id.item_home_activity_text)).setText(LanguageUtil.chooseTest(info.getCaption(), info.getCaptionEn()));
            view.setOnClickListener(v -> {
                WebExpoActivityActivity.startActivity(getContext(), info.getId());
            });
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, -2);
        params.weight = 1;
        if (position != 0) {
            params.leftMargin = (int) getContent().getResources().getDimension(R.dimen.dms_20);
        }
        view.setLayoutParams(params);
        mLlActivity.addView(view);

    }

    // 推荐游园路线
    private void addRoute1() {
        int width = (int) (ScreenUtils.getScreenWidth() - getContext().getResources().getDimension(R.dimen.dms_68)) * 2 / 3;
        mRouteLeft.getLayoutParams().width = width;
        mIvRouteImg1.getLayoutParams().width = width;
        if (mListRoute != null && mListRoute.size() > 0) {
            RouteInfo info = mListRoute.get(0);
            Picasso.with(getContext()).load(CommUtils.getFullUrl(info.picUrl)).into(mIvRouteImg1);
//        mTvRouteText1.setText("浪漫世园行《山水园艺轴》\n推荐最佳路线");
            mIvRouteImg1.setOnClickListener(v -> RouteDetailActivity.startActivity(getContext(), info.id, LanguageUtil.chooseTest(info.caption, info.captionen)));
        }
//        Picasso.with(getContext()).load(R.mipmap.route_top_left).into(mIvRouteImg1);
//        mTvRouteText1.setText("浪漫世园行《山水园艺轴》\n推荐最佳路线");
    }

    private void addRoute2() {
        int width = (int) (ScreenUtils.getScreenWidth() - getContext().getResources().getDimension(R.dimen.dms_76)) / 3;
        mIvRouteImg2.getLayoutParams().width = width;
        if (mListRoute != null && mListRoute.size() > 1) {
            RouteInfo info = mListRoute.get(1);
            Picasso.with(getContext()).load(CommUtils.getFullUrl(info.picUrl)).into(mIvRouteImg2);
            mIvRouteImg2.setOnClickListener(v -> RouteDetailActivity.startActivity(getContext(), info.id, LanguageUtil.chooseTest(info.caption, info.captionen)));
        }
//        Picasso.with(getContext()).load(R.mipmap.route_top_right).into(mIvRouteImg2);
    }

    // 推荐游园路线
    private void addRouteBottom(int position) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_home_hot_activity, null);
        if (mListRoute.size() > position) {
            RouteInfo info = mListRoute.get(position);
            ImageView imageView = view.findViewById(R.id.item_home_activity_img);
            Picasso.with(getContext()).load(CommUtils.getFullUrl(info.picUrl)).into(imageView);
            ((TextView) view.findViewById(R.id.item_home_activity_text)).setText(LanguageUtil.chooseTest(info.caption, info.captionen));
            view.setOnClickListener(v -> RouteDetailActivity.startActivity(getContext(), info.id, LanguageUtil.chooseTest(info.caption, info.captionen)));
        }

//        ImageView imageView = view.findViewById(R.id.item_home_activity_img);
//        Picasso.with(getContext()).load(R.mipmap.route_bottom).placeholder(R.drawable.image_default).into(imageView);
//        ((TextView) view.findViewById(R.id.item_home_activity_text)).setText("游玩去世园");

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, -2);
        params.weight = 1;
        if (position != 2) {
            params.leftMargin = (int) getContent().getResources().getDimension(R.dimen.dms_8);
        }
        view.setLayoutParams(params);
        mLlRoute.addView(view);
    }

    // 世园美食
    private void addFood(int position) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_home_food_title, null);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (ScreenUtils.getScreenWidth() - getContext().getResources().getDimension(R.dimen.dms_60)) / 5, -1);
//        view.setLayoutParams(params);
        if (mListFood.size() > position) {
            Encyclopedias info = mListFood.get(position);
            SimpleDraweeView imageView = view.findViewById(R.id.item_home_food_title_img);
            imageView.setImageURI(CommUtils.getFullUrl(info.picUrl));
            ((TextView) view.findViewById(R.id.item_home_food_title_text)).setText(LanguageUtil.chooseTest(info.caption, info.captionEn));
            view.setOnClickListener(v -> RouteDetailActivity.startActivity(getContext(), info.id, LanguageUtil.chooseTest(info.caption, info.captionEn)));
        }

//        ImageView imageView = view.findViewById(R.id.item_home_food_title_img);
//        Picasso.with(getContext()).load(R.mipmap.item_home_food_title_img).placeholder(R.drawable.image_default).into(imageView);
//        ((TextView) view.findViewById(R.id.item_home_food_title_text)).setText("涮羊肉");

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, -2);
        params.weight = 1;
        view.setLayoutParams(params);

        mLlFood.addView(view);
    }

    // 世园美食
    private void addVr(int position) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_home_daren, null);
        if (mListDaren.size() > position) {
            VrInfo vrInfo = mListDaren.get(position);
            ImageView imageView = view.findViewById(R.id.daren_left_img);
            Picasso.with(getContext()).load(CommUtils.getFullUrl(vrInfo.getUrl())).placeholder(R.drawable.image_default).into(imageView);
            SimpleDraweeView circleView = view.findViewById(R.id.daren_left_circle_img);
            circleView.setImageURI(CommUtils.getFullUrl(vrInfo.getUrl()));
            view.setOnClickListener(v -> {
                if (StringUtils.equals(vrInfo.getType(), Constants.VrType.VR_TYPE_IMG)) {
                    VRImageActivity.startActivity(getContext(), vrInfo.getId());
                } else if (StringUtils.equals(vrInfo.getType(), Constants.VrType.VR_TYPE_VIDEO)) {
                    VRDetailActivity.startActivity(getContext(), vrInfo.getId());
                }
            });
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, -2);
        params.weight = 1;
        if (position != 0) {
            params.leftMargin = (int) getContent().getResources().getDimension(R.dimen.dms_22);
        }
        view.setLayoutParams(params);
        mLlDaren.addView(view);
    }

    // 世园会三大特色酒店
    private void addHotelView(int position) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_home_hotel, null);
        if (mListHotel.size() > position) {
            Encyclopedias encyclopedias = mListHotel.get(position);
            ImageView imageView = view.findViewById(R.id.item_home_hotel_img);
            Picasso.with(getContext()).load(CommUtils.getFullUrl(encyclopedias.picUrl)).placeholder(R.drawable.image_default).into(imageView);
            ((TextView) view.findViewById(R.id.item_home_hotel_text)).setText(LanguageUtil.chooseTest(encyclopedias.caption, encyclopedias.captionEn));
            view.setOnClickListener(v -> WebTemplateActivity.startActivity(getContext(), encyclopedias.getId()));
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, -2);
        params.weight = 1;
        if (position != 0) {
            params.leftMargin = (int) getContent().getResources().getDimension(R.dimen.dms_20);
        }
        view.setLayoutParams(params);
        mLlHotel.addView(view);

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

    @OnClick({R.id.home_find_0, R.id.home_find_1, R.id.home_find_2, R.id.home_find_3,
            R.id.home_find_4, R.id.home_find_5, R.id.home_find_6})
    public void clickFind(View view) {
        ((MainActivity) getContext()).goScenicMap();
    }

    @OnClick(R.id.home_activity_more)
    public void clickActivityMore() {
        ExpoActivityActivity.startActivity(getContext());
    }

    @OnClick(R.id.home_hot_activity_firsh)
    public void clickActivityFirst() {
        if (mListActivity != null && mListActivity.size() > 1) {
            WebExpoActivityActivity.startActivity(getContext(), mListActivity.get(0).getId());
        }
    }

    @OnClick(R.id.home_route_more)
    public void clickRouteMore() {
        RoutesActivity.startActivity(getContext());
    }

    @OnClick(R.id.home_ranking_more)
    public void clickRankingMore() {
        ((MainActivity) getContext()).goScenic();
    }

    @OnClick(R.id.home_food_more)
    public void clickFoodMore() {
        ((MainActivity) getContext()).goScenic();
    }

    @OnClick(R.id.home_hotel_more)
    public void clickHotelMore() {
        ((MainActivity) getContext()).goScenic();
    }

    @OnClick({R.id.home_experience_more, R.id.home_experience_img})
    public void clickExperienceMore() {
        CircumHomeActivity.startActivity(getContext());
    }

    @OnClick(R.id.daren_img1)
    public void clickDaren1() {
        OnlineExpoActivity.startActivity(getContext());
    }

    @OnClick(R.id.home_hotel_more)
    public void clickDaren2() {
        ArActivity.lunchPhotograph(getContext(), mPresenter.loadCommonInfo(CommonInfo.EXPO_AR_DOWNLOAD_PAGE));
    }
}
