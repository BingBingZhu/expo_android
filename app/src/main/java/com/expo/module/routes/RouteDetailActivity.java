package com.expo.module.routes;

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
import com.squareup.picasso.Picasso;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.raphets.roundimageview.RoundImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 路线详情页
 */
public class RouteDetailActivity extends BaseActivity<RouteDetailContract.Presenter> implements RouteDetailContract.View {

    @BindView(R.id.item_route_img)
    RoundImageView mIvImg;
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
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mPbProgress.setProgress(seek);
                    mTvCurrTime.setText(farmatTime(seek));
                }
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
            mBtPlayer.setImageResource(R.mipmap.horn);
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
        }
    };

    Handler mHandler = new Handler();

    @Override
    protected int getContentView() {
        return R.layout.activity_route_detail;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(1, R.string.title_routest_detail);
        initRecyclerView();

        mPbProgress.setOnSeekBarChangeListener(mSeekListener);
        MediaPlayerManager.getInstence().setListener(mListener);

        mPresenter.getRouteDetail(getIntent().getLongExtra(Constants.EXTRAS.EXTRA_ID, 0));
        mPresenter.getVenuesList(mInfo.idsList);
        //LanguageUtil.chooseTest(CommUtils.getFullUrl(mInfo.voiceUrl), CommUtils.getFullUrl(mInfo.voiceUrlEn))
        MediaPlayerManager.getInstence().setDataSource(getContext(), "http://audio.xmcdn.com/group15/M06/1D/EA/wKgDZVV47ZLD3fFXABohc_qeKmc390.m4a");
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
    public static void startActivity(@NonNull Context context, @NonNull Long routeId) {
        Intent in = new Intent(context, RouteDetailActivity.class);
        in.putExtra(Constants.EXTRAS.EXTRA_ID, routeId);
        context.startActivity(in);
    }

    private void initRecyclerView() {
        mRvRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRvRecycler.addItemDecoration(new SpaceDecoration((int) getResources().getDimension(R.dimen.dms_30)));
        mList = new ArrayList<>();
        mRvRecycler.setAdapter(mAdapter = new CommonAdapter<Venue>(this, R.layout.item_route_detail, mList) {
            @Override
            protected void convert(ViewHolder holder, Venue venue, int position) {
                int picRes = 0;
                if (venue.getType() == 25 || venue.getType() == 26) {//场馆、景点
                    mPresenter.loadRemarkFormEncyclopedia(venue);
                    Picasso.with(RouteDetailActivity.this).load(CommUtils.getFullUrl(venue.getPicUrl()))
                            .placeholder(R.drawable.image_default)
                            .error(R.drawable.image_default)
                            .into((ImageView) holder.getView(R.id.item_route_img));
                } else if (venue.getType() == 27) {//美食
                    picRes = R.mipmap.ico_car_def_img;
                } else if (venue.getType() == 28) {//卫生间
                    picRes = R.mipmap.ico_toilet_def_img;
                } else if (venue.getType() == 30) {//治安
                    picRes = R.mipmap.ico_public_security_def_img;
                } else if (venue.getType() == 31) {//服务中心
                    picRes = R.mipmap.ico_car_def_img;
                } else if (venue.getType() == 32) {//导览车
                    picRes = R.mipmap.ico_car_def_img;
                }
                if (picRes != 0) {
                    Picasso.with(RouteDetailActivity.this).load(picRes)
                            .placeholder(R.drawable.image_default)
                            .error(R.drawable.image_default)
                            .into((ImageView) holder.getView(R.id.item_route_img));
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
            if ((venue.getType() == 25 || venue.getType() == 26) && !TextUtils.isEmpty(venue.getWikiId()) && venue.getWikiId().matches(Constants.Exps.NUMBER)) {
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

    @Override
    public void showRouteDetail(RouteInfo info) {
        mInfo = info;
        if (mInfo == null) return;
        Picasso.with(this).load(CommUtils.getFullUrl(info.picUrl)).placeholder(R.drawable.image_default).error(R.drawable.image_default).into(mIvImg);
        mTvName.setText(LanguageUtil.chooseTest(info.caption, info.captionen));
        mTvHot.setText(getResources().getString(R.string.hot) + info.hotCount);
        mTvTime.setText(info.updateTime);
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
