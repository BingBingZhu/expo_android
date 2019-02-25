package com.expo.module.routes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.RouteDetailContract;
import com.expo.entity.RouteInfo;
import com.expo.entity.Venue;
import com.expo.media.MediaPlayerManager;
import com.expo.module.webview.WebTemplateActivity;
import com.expo.utils.CommUtils;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.expo.widget.decorations.SpaceDecoration;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 路线详情页
 */
public class RouteDetailActivity extends BaseActivity<RouteDetailContract.Presenter> implements RouteDetailContract.View {

    @BindView(R.id.item_route_img)
    SimpleDraweeView mIvImg;
    @BindView(R.id.item_route_name)
    TextView mTvName;
    @BindView(R.id.item_route_hot)
    TextView mTvHot;
    @BindView(R.id.route_detail_time)
    TextView mTvTime;
    @BindView(R.id.route_info)
    TextView mTvInfo;
    @BindView(R.id.recycler)
    RecyclerView mRvRecycler;
    @BindView(R.id.route_progress)
    SeekBar mPbProgress;
    @BindView(R.id.player_curr_time)
    TextView mTvCurrTime;
    @BindView(R.id.player_total_time)
    TextView mTvTotalTime;
    @BindView(R.id.route_horn)
    ImageView mBtPlayer;

    RouteInfo mInfo;
    CommonAdapter mAdapter;
    List<Venue> mList;

    MediaPlayerManager.MediaPlayerManagerListener mListener = new MediaPlayerManager.MediaPlayerManagerListener() {
        @Override
        public void start() {
            mBtPlayer.setImageResource(R.mipmap.player_playing);
        }

        @Override
        public void setProgress(int seek) {
            mHandler.post(() -> {
                mPbProgress.setProgress(seek);
                mTvCurrTime.setText(farmatTime(seek));
            });
        }

        @Override
        public void setDuration(int duration) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mPbProgress.setMax(duration);
                    mTvTotalTime.setText(farmatTime(duration));
                }
            });
        }

        @Override
        public void error(String error) {

        }

        @Override
        public void complete() {
            mPbProgress.setProgress(mPbProgress.getMax());
            mBtPlayer.setImageResource(R.mipmap.player_playing);
        }

        @Override
        public void paused() {
            mBtPlayer.setImageResource(R.mipmap.player_paused);
        }
    };

    private String farmatTime(int duration) {
        int second = duration / 1000 % 60;
        int minute = duration / 1000 / 60;
        return (minute > 9 ? String.valueOf(minute) : ("0" + minute)) + ":" + (second > 9 ? String.valueOf(second) : ("0" + second));
    }

    SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            MediaPlayerManager.getInstence().setSeek(seekBar.getProgress());
            mBtPlayer.setImageResource(R.mipmap.player_playing);
        }
    };

    Handler mHandler = new Handler();

    @Override
    protected int getContentView() {
        return R.layout.activity_route_detail;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(1, getIntent().getStringExtra(Constants.EXTRAS.EXTRA_ROUTE_NAME));
        initRecyclerView();

        mPbProgress.setOnSeekBarChangeListener(mSeekListener);
        MediaPlayerManager.getInstence().setListener(mListener);

        mPresenter.getRouteDetail(getIntent().getLongExtra(Constants.EXTRAS.EXTRA_ID, 0));
        mPresenter.getVenuesList(mInfo.idsList);
        MediaPlayerManager.getInstence().setDataSource(getContext(), LanguageUtil.chooseTest(CommUtils.getFullUrl(mInfo.voiceUrl), CommUtils.getFullUrl(mInfo.voiceUrlEn)));
//        MediaPlayerManager.getInstence().setDataSource(getContext(), "http://audio.xmcdn.com/group15/M06/1D/EA/wKgDZVV47ZLD3fFXABohc_qeKmc390.m4a");
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    /**
     * 启动查看路线详情
     *
     * @param context
     * @param routeId
     */
    public static void startActivity(@NonNull Context context, @NonNull Long routeId, String routeName) {
        Intent in = new Intent(context, RouteDetailActivity.class);
        in.putExtra(Constants.EXTRAS.EXTRA_ID, routeId);
        in.putExtra(Constants.EXTRAS.EXTRA_ROUTE_NAME, routeName);
        context.startActivity(in);
    }

    private void initRecyclerView() {
        mRvRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRvRecycler.addItemDecoration(new SpaceDecoration((int) getResources().getDimension(R.dimen.dms_30)));
        mList = new ArrayList<>();
        mRvRecycler.setAdapter(mAdapter = new CommonAdapter<Venue>(this, R.layout.item_route_detail, mList) {
            @Override
            protected void convert(ViewHolder holder, Venue venue, int position) {
                if (venue.getTypeName().contains(Constants.CHString.SCENIC_SPOT) || venue.getTypeName().contains(Constants.CHString.SCENIC_VENUE)) {//场馆、景点
                    mPresenter.loadRemarkFormEncyclopedia(venue);
                    ((SimpleDraweeView) holder.getView(R.id.item_route_img)).setImageURI(CommUtils.getFullUrl(venue.getPicUrl()));
                } else {
                    ((SimpleDraweeView) holder.getView(R.id.item_route_img)).setImageResource(R.mipmap.ico_car_def_img);
                }
                holder.setText(R.id.item_route_name, LanguageUtil.chooseTest(venue.getCaption(), venue.getEnCaption()));
                holder.setText(R.id.item_route_info, LanguageUtil.chooseTest(venue.getRemark(), venue.getEnRemark()));
            }
        });
        mAdapter.setOnItemClickListener(onItemClickListener);
    }

    private CommonAdapter.OnItemClickListener onItemClickListener = new MultiItemTypeAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
            Venue venue = (Venue) mAdapter.getDatas().get(position);
            if ((venue.getTypeName().contains(Constants.CHString.SCENIC_SPOT) || venue.getTypeName().contains(Constants.CHString.SCENIC_VENUE)) && !TextUtils.isEmpty(venue.getWikiId()) && venue.getWikiId().matches(Constants.Exps.NUMBER)) {
                WebTemplateActivity.startActivity(getContext(), Long.parseLong(venue.getWikiId()));
            } else {
                ToastHelper.showShort(R.string.no_introduction);
            }
        }

        @Override
        public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
            return false;
        }
    };

    @SuppressLint("StringFormatMatches")
    @Override
    public void showRouteDetail(RouteInfo info) {
        mInfo = info;
        if (mInfo == null) return;
        mIvImg.setImageURI(CommUtils.getFullUrl(info.picUrl));
        mTvName.setText(LanguageUtil.chooseTest(info.caption, info.captionen));
        mTvHot.setText(getResources().getString(R.string.hot) + info.hotCount);
        mTvTime.setText(String.format(getString(R.string.expected_travel_time, info.playTime)));
        if (StringUtils.isEmpty(LanguageUtil.chooseTest(info.remark, info.remarken)))
            mPresenter.getRouteDetailFromency(info.wikiId);
        else
            mTvInfo.setText(LanguageUtil.chooseTest(info.remark, info.remarken));
    }

    @Override
    public void showRemarkDetail(String remark) {
        mTvInfo.setText(remark);
    }

    @Override
    public void showVenuesList(List<Venue> list) {
        mList.clear();
        if (list != null) {
            mList.addAll(list);
        }
        mAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.route_horn)
    public void clickHorn(View view) {
        if (mInfo == null) return;
        MediaPlayerManager.getInstence().start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaPlayerManager.getInstence().onDestory();
    }
}
