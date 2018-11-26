package com.expo.module.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.BaseEventMessage;
import com.expo.base.BaseFragment;
import com.expo.base.ExpoApp;
import com.expo.base.utils.StatusBarUtils;
import com.expo.contract.HomeContract;
import com.expo.entity.Encyclopedias;
import com.expo.entity.TopLineInfo;
import com.expo.module.distinguish.DistinguishActivity;
import com.expo.module.freewifi.FreeWiFiActivity;
import com.expo.module.heart.MessageKindActivity;
import com.expo.module.main.adapter.HomeExhibitAdapter;
import com.expo.module.main.adapter.HomeTopLineAdapter;
import com.expo.module.main.encyclopedia.EncyclopediaSearchActivity;
import com.expo.module.map.ParkMapActivity;
import com.expo.module.routes.RoutesActivity;
import com.expo.module.service.TouristServiceActivity;
import com.expo.module.webview.WebActivity;
import com.expo.module.webview.WebTemplateActivity;
import com.expo.utils.CommUtils;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.expo.widget.LimitScrollerView;
import com.expo.widget.MyScrollView;
import com.expo.widget.decorations.SpaceDecoration;
import com.expo.widget.gallery.FixLinearSnapHelper;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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
    @BindView(R.id.home_exhibit)
    View mHomeExhibit;
    @BindView(R.id.home_exhibit_garden)
    View mHomeExhibitGarden;
    @BindView(R.id.home_ad_scroll)
    LimitScrollerView mLsvScroll;
    @BindView(R.id.home_venue_layout)
    LinearLayout mLlVenue;
    @BindView(R.id.recycler_exhibit)
    RecyclerView mRvExhibit;
    @BindView(R.id.recycler_exhibit_garden)
    RecyclerView mRvExhibitGarden;

    List<TopLineInfo> mListTopLine;
    List<Encyclopedias> mListVenue;
    List<Encyclopedias> mListExhibit;
    List<Encyclopedias> mListExhibitGarden;

    HomeTopLineAdapter mAdapterTopLine;
    HomeExhibitAdapter mAdapterExhibit;
    CommonAdapter<Encyclopedias> mAdapterExhibitGarden;

    LimitScrollerView.OnItemClickListener mTopLineListener = obj -> {
        TopLineInfo topLine = (TopLineInfo) obj;
        WebActivity.startActivity( getContext(),
                LanguageUtil.chooseTest( topLine.linkH5Url, topLine.linkH5UrlEn ),
                LanguageUtil.chooseTest( topLine.caption, topLine.captionEn ) );
    };

    MyScrollView.OnScrollListener mScrollListener = new MyScrollView.OnScrollListener() {
        @Override
        public void onScroll(int scrollY) {
            int color = Color.argb( (int) Math.min( 0xff,
                    Math.max( Float.valueOf( scrollY ), 0.0f ) / 2 ), 0, 203, 153 );
            mHtTitle.setBackgroundColor( color );
        }
    };


    @Override
    public int getContentView() {
        return R.layout.fragment_home;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mSvScroll.setOnScrollListener( mScrollListener );

        mHtTitle.setPadding( 0, StatusBarUtils.getStatusBarHeight( getContext() ), 0, 0 );

        initRecyclerTop();
        initRecyclerExhibit();
        initRecyclerExhibitGarden();
        EventBus.getDefault().register( this );

        mPresenter.setMessageCount();
        mPresenter.setTopLine();
        mPresenter.setVenue();
        mPresenter.setExhibit();
        mPresenter.setExhibitGarden();
        mPresenter.startHeartService( getContext() );
    }

    @Override
    public boolean isNeedPaddingTop() {
        return false;
    }

    private void initRecyclerTop() {
        mListTopLine = new ArrayList<>();
        mLsvScroll.setDataAdapter( mAdapterTopLine = new HomeTopLineAdapter( getContext() ) );
        mLsvScroll.setOnItemClickListener( mTopLineListener );
        mAdapterTopLine.setDatas( mListTopLine );
    }

    private void initRecyclerExhibit() {
        mListExhibit = new ArrayList<>();
        mRvExhibit.setLayoutManager( new LinearLayoutManager( getContext(), RecyclerView.HORIZONTAL, false ) );
//        mRvExhibit.addItemDecoration(new SpaceDecoration(0, 0, 0, 0, (int) getContent().getResources().getDimension(R.dimen.dms_20), 0));
        new FixLinearSnapHelper().attachToRecyclerView( mRvExhibit );
        mRvExhibit.setAdapter( mAdapterExhibit = new HomeExhibitAdapter( getContext() ) );
        mAdapterExhibit.setData( mListExhibit );
    }

    private void initRecyclerExhibitGarden() {
        mListExhibitGarden = new ArrayList<>();
        mRvExhibitGarden.setLayoutManager( new LinearLayoutManager( getContext() ) );
        mRvExhibitGarden.addItemDecoration( new SpaceDecoration( 0, 0, (int) getContent().getResources().getDimension( R.dimen.dms_4 ), (int) getContent().getResources().getDimension( R.dimen.dms_20 ), 0 ) );
        mRvExhibitGarden.setAdapter( mAdapterExhibitGarden = new CommonAdapter<Encyclopedias>( getContext(), R.layout.item_home_exhibit_garden, mListExhibitGarden ) {
            @Override
            protected void convert(ViewHolder holder, Encyclopedias encyclopedias, int position) {
                Picasso.with( getContext() ).load( CommUtils.getFullUrl( encyclopedias.picUrl ) ).placeholder( R.drawable.image_default ).error( R.drawable.image_default ).into( (ImageView) holder.getView( R.id.home_exhibit_garden_img ) );
                holder.setText( R.id.home_exhibit_garden_name, LanguageUtil.chooseTest( encyclopedias.caption, encyclopedias.captionEn ) );
                holder.setText( R.id.home_exhibit_garden_content, LanguageUtil.chooseTest( encyclopedias.remark, encyclopedias.remarkEn ) );
                holder.itemView.setOnClickListener( v -> WebTemplateActivity.startActivity( mContext, encyclopedias.getId() ) );
            }
        } );
    }

    @OnClick(R.id.title_home_msg)
    public void clickTitleMsg(View view) {
        MessageKindActivity.startActivity( getContext() );
    }

//    @OnClick(R.id.home_map_img)
//    public void clickMapImg(View view) {
//        ParkMapActivity.startActivity(getContext(), 0L);
//    }

    @OnClick(R.id.home_title_text)
    public void clickTitleText(View view) {
        EncyclopediaSearchActivity.startActivity( getContext() );
    }

    @OnClick({R.id.home_func_0, R.id.home_func_1, R.id.home_func_2, R.id.home_func_3, R.id.home_func_4, R.id.home_func_5, R.id.home_func_6, R.id.home_func_7, R.id.home_navigation_item})
    public void clickFunc(View view) {
        switch (view.getId()) {
            case R.id.home_func_0:
                ParkMapActivity.startActivity( getContext(), null );
                break;
            case R.id.home_func_1:
                FreeWiFiActivity.startActivity( getContext() );
                break;
            case R.id.home_func_2:
                String url = mPresenter.loadBuyTicketsUrl();
                WebActivity.startActivity( getContext(), TextUtils.isEmpty( url ) ? Constants.URL.HTML_404 : url, getString( R.string.buy_tickets ) );
                break;
            case R.id.home_func_3:
                url = mPresenter.loadVenueBespeakUrl();
                WebActivity.startActivity( getContext(), TextUtils.isEmpty( url ) ? Constants.URL.HTML_404 : url, getString( R.string.home_func_item_appointment ),BaseActivity.TITLE_COLOR_STYLE_WHITE );
//                url = "http://192.168.1.13:8080/";
//                WebActivity.startActivity( getContext(), TextUtils.isEmpty( url ) ? Constants.URL.HTML_404 :
//                        url + "?Uid=" + ExpoApp.getApplication().getUser().getUid() + "&Ukey=" + ExpoApp.getApplication().getUser().getUkey()
//                                + "&lan=" + LanguageUtil.chooseTest( "zh", "en" ), getString( R.string.home_func_item_appointment ),BaseActivity.TITLE_COLOR_STYLE_WHITE );
                break;
            case R.id.home_func_4:
                TouristServiceActivity.startActivity( getContext() );
                break;
            case R.id.home_func_5:
                RoutesActivity.startActivity( getContext() );
                break;
            case R.id.home_func_6:
                DistinguishActivity.startActivity( getContext() );
                break;
            case R.id.home_func_7:
                WebActivity.startActivity( getContext(), "http://www.changchengneiwai.com/wap/index.htm", "周边服务" );
                break;
            case R.id.home_navigation_item:
                ParkMapActivity.startActivity( getContext(), null );
                break;
        }
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    @Override
    public void showTopLine(List<TopLineInfo> list) {
        if (isDataEmpty( mHomeAd, list )) return;
        mListTopLine.clear();
        mListTopLine.addAll( list );
        mAdapterExhibit.notifyDataSetChanged();
        mLsvScroll.startScroll();
    }

    @Override
    public void showVenue(List<Encyclopedias> list) {
        if (isDataEmpty( mHomeVenue, list )) return;
        mListVenue = list;
        mLlVenue.removeAllViews();
        for (int i = 0; i < 3; i++) {
            addVenueView( i );
        }
    }

    @Override
    public void showExhibit(List<Encyclopedias> list) {
        if (isDataEmpty( mHomeExhibit, list )) return;
        mListExhibit.clear();
        mListExhibit.addAll( list );
        mAdapterExhibit.notifyDataSetChanged();
        if (mAdapterExhibit.getItemCount() > 1)
            mRvExhibit.scrollToPosition( Integer.MAX_VALUE / 2 );
    }

    @Override
    public void showExhibitGarden(List<Encyclopedias> list) {
        if (isDataEmpty( mHomeExhibitGarden, list )) return;
        mListExhibitGarden.clear();
        mListExhibitGarden.addAll( list );
        mAdapterExhibitGarden.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventBusMsgOnMainThread(BaseEventMessage baseEventMessage) {
        switch (baseEventMessage.id) {
            case Constants.EventBusMessageId.EVENTBUS_ID_HEART_MESSAGE_UNREAD_COUNT:
                mMsgNote.setVisibility( ((Long) baseEventMessage.t) == 0L ? View.GONE : View.VISIBLE );
                break;
        }
    }

    private boolean isDataEmpty(View view, List list) {
        boolean empty = list == null || list.size() == 0;
        if (empty) view.setVisibility( View.GONE );
        else view.setVisibility( View.VISIBLE );
        return empty;
    }

    private void addVenueView(int position) {
        View view = LayoutInflater.from( getContext() ).inflate( R.layout.item_home_venue, null );
        if (mListVenue.size() > position) {
            Encyclopedias encyclopedias = mListVenue.get( position );
            Picasso.with( getContext() )
                    .load( CommUtils.getFullUrl( encyclopedias.picUrl ) )
                    .placeholder( R.drawable.image_default ).error( R.drawable.image_default )
                    .resize( ScreenUtils.getScreenWidth() - (int) getContent().getResources().getDimension( R.dimen.dms_60 ) - (int) getContent().getResources().getDimension( R.dimen.dms_20 ) * (mListVenue.size() - 1), (int) getContent().getResources().getDimension( R.dimen.dms_190 ) )
                    .centerInside()
                    .into( (ImageView) view.findViewById( R.id.item_home_venue_img ) );
            ((TextView) view.findViewById( R.id.item_home_venue_text )).setText( LanguageUtil.chooseTest( encyclopedias.caption, encyclopedias.captionEn ) );
            view.setOnClickListener( v -> WebTemplateActivity.startActivity( getContext(), encyclopedias.getId() ) );
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( 0, -2 );
        params.weight = 1;
        if (position != 0) {
            params.leftMargin = (int) getContent().getResources().getDimension( R.dimen.dms_20 );
        }
        view.setLayoutParams( params );
        mLlVenue.addView( view );

    }

    @Override
    public void onDestroy() {
        if (null != mPresenter)
            mPresenter.stopHeartService( getContext() );
        super.onDestroy();
        EventBus.getDefault().unregister( this );
    }

}
