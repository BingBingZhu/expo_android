package com.expo.module.main;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.BaseEventMessage;
import com.expo.base.BaseFragment;
import com.expo.base.ExpoApp;
import com.expo.contract.MineContract;
import com.expo.entity.CommonInfo;
import com.expo.entity.User;
import com.expo.module.badge.BadgeActivity;
import com.expo.module.contacts.ContactsActivity;
import com.expo.module.heart.MessageKindActivity;
import com.expo.module.mine.FeedbackActivity;
import com.expo.module.mine.SettingActivity;
import com.expo.module.mine.UserInfoActivity;
import com.expo.module.track.TrackActivity;
import com.expo.module.webview.WebActivity;
import com.expo.utils.CommUtils;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.expo.utils.LocalBroadcastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.raphets.roundimageview.RoundImageView;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 我的页
 */
public class MineFragment extends BaseFragment<MineContract.Presenter> implements MineContract.View {

    @BindView(R.id.mine_img)
    RoundImageView mImageView;
    @BindView(R.id.mine_name)
    TextView mTvMineName;
    @BindView(R.id.mine_integral)
    TextView mTvIntegral;
    @BindView(R.id.mine_badge)
    TextView mTvBadge;

    private int score;
    private int totScores;

    @Override
    public int getContentView() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mPresenter.loadUser();
        EventBus.getDefault().register( this );
        LocalBroadcastUtil.registerReceiver(getContext(), receiver, Constants.Action.ACTION_REDUCE_USER_POINTS);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister( this );
        LocalBroadcastUtil.unregisterReceiver(getContext(), receiver);
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void freshUser(User user) {
        if (user == null) return;
        if (!StringUtils.isEmpty( user.getPhotoUrl() ))
            CommUtils.setImgPic( getContext(), user.getPhotoUrl(), mImageView );
        mTvMineName.setText( user.getNick() );
        totScores = user.getIntTotscores();
        score = user.getIntScore();
        mTvIntegral.setText( String.format( getResources().getString( R.string.the_current_integral ), score ) );
    }

    @OnClick({R.id.mine_edit_info, R.id.mine_img, R.id.mine_name, R.id.item_mine_bespeak,
            R.id.item_mine_comment_report, R.id.item_mine_message, R.id.item_mine_track,
            R.id.item_mine_setting, R.id.mine_integral, R.id.mine_badge, R.id.item_mine_contacts})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_edit_info:
            case R.id.mine_img:
            case R.id.mine_name:
                UserInfoActivity.startActivity( getContext() );
                break;
            case R.id.item_mine_bespeak://预约
                String url = mPresenter.loadCommonInfo( CommonInfo.MY_BESPEAK );
                WebActivity.startActivity( getContext(), TextUtils.isEmpty( url ) ? Constants.URL.HTML_404 :
                        url + "?Uid=" + ExpoApp.getApplication().getUser().getUid() + "&Ukey=" + ExpoApp.getApplication().getUser().getUkey()
                                + "&lan=" + LanguageUtil.chooseTest( "zh", "en" ), getString( R.string.home_func_item_my_bespeak ), BaseActivity.TITLE_COLOR_STYLE_WHITE );
                break;
            case R.id.item_mine_comment_report://反馈
                FeedbackActivity.startActivity( getContext() );
                break;
            case R.id.item_mine_message://消息
                MessageKindActivity.startActivity( getContext() );
                break;
            case R.id.item_mine_track://足迹
                TrackActivity.startActivity( getContext() );
                break;
            case R.id.item_mine_setting://设置
                SettingActivity.startActivity( getContext() );
                break;
            case R.id.mine_integral:    // 积分
                url = mPresenter.loadCommonInfo( CommonInfo.SCORE );
                if (StringUtils.isEmpty( url ))
                    return;
                else
                    WebActivity.startActivity( getContext(), url + "?Uid=" + ExpoApp.getApplication().getUser().getUid() + "&Ukey=" + ExpoApp.getApplication().getUser().getUkey()
                            + "&integral=" + score
                            + "&lan=" + LanguageUtil.chooseTest( "zh", "en" ), getString( R.string.score ), false );
                break;
            case R.id.mine_badge:       // 徽章
                BadgeActivity.startActivity( getContext(), totScores );
                break;
            case R.id.item_mine_contacts:       // 预约联系人
                ContactsActivity.startActivity( getActivity(), false, 0 );
                break;
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            score-=intent.getIntExtra(Constants.EXTRAS.EXTRA_USER_POINTS, 0);
            mTvIntegral.setText( String.format( getResources().getString( R.string.the_current_integral ), score ) );
            mPresenter.loadUser();
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventBusMsgOnMainThread(BaseEventMessage baseEventMessage) {
        switch (baseEventMessage.id) {
            case Constants.EventBusMessageId.EVENTBUS_ID_FRESH_USER:
                mPresenter.loadUser();
                break;
        }
    }


}
