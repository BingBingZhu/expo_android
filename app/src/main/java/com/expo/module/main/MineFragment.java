package com.expo.module.main;

import android.annotation.SuppressLint;
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
import com.expo.module.heart.MessageKindActivity;
import com.expo.module.mine.AboutActivity;
import com.expo.module.mine.FeedbackActivity;
import com.expo.module.mine.SettingActivity;
import com.expo.module.mine.UserInfoActivity;
import com.expo.module.track.TrackActivity;
import com.expo.module.webview.WebActivity;
import com.expo.utils.CommUtils;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.squareup.picasso.Picasso;

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

    @Override
    public int getContentView() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mPresenter.loadUser();
        EventBus.getDefault().register( this );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister( this );
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
        String scores = getResources().getString(R.string.the_current_integral);
        score = user.getIntTotscores();
        mTvIntegral.setText( String.format(scores, score));
    }

    @Override
    public void returnCommonInfo(CommonInfo commonInfo) {
//        WebActivity.startActivity( getContext(), commonInfo.getLinkUrl(),
//                LanguageUtil.isCN() ? commonInfo.getCaption() : commonInfo.getCaptionEn(), BaseActivity.TITLE_COLOR_STYLE_WHITE );
//        String url = "http://192.168.1.13:8080/#/myOrder";
        String url = commonInfo.getLinkUrl();
        WebActivity.startActivity( getContext(), TextUtils.isEmpty( url ) ? Constants.URL.HTML_404 :
                url + "?Uid=" + ExpoApp.getApplication().getUser().getUid() + "&Ukey=" + ExpoApp.getApplication().getUser().getUkey()
                        + "&lan=" + LanguageUtil.chooseTest( "zh", "en" ), getString( R.string.home_func_item_appointment ), BaseActivity.TITLE_COLOR_STYLE_WHITE );
    }

    @OnClick({R.id.mine_edit_info, R.id.mine_img, R.id.mine_name, R.id.item_mine_bespeak,
            R.id.item_mine_comment_report, R.id.item_mine_message, R.id.item_mine_track,
            R.id.item_mine_about, R.id.item_mine_setting, R.id.mine_integral, R.id.mine_badge})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_edit_info:
            case R.id.mine_img:
            case R.id.mine_name:
                UserInfoActivity.startActivity( getContext() );
                break;
            case R.id.item_mine_bespeak:
                mPresenter.clickPolicy( "9" );
                break;
            case R.id.item_mine_comment_report:
                FeedbackActivity.startActivity( getContext() );
                break;
            case R.id.item_mine_message:
                MessageKindActivity.startActivity( getContext() );
                break;
            case R.id.item_mine_track:
                TrackActivity.startActivity( getContext() );
                break;
            case R.id.item_mine_about:
                AboutActivity.startActivity( getContext() );
                break;
            case R.id.item_mine_setting:
                SettingActivity.startActivity( getContext() );
                break;
            case R.id.mine_integral:    // 积分
                String url = "http://192.168.1.13:8080";
                WebActivity.startActivity(getContext(), url + "?Uid=" + ExpoApp.getApplication().getUser().getUid() + "&Ukey=" + ExpoApp.getApplication().getUser().getUkey()
                        + "&integral=" + score
                        + "&lan=" + LanguageUtil.chooseTest( "zh", "en" ), "我的积分", false);
                break;
            case R.id.mine_badge:       // 徽章
                BadgeActivity.startActivity(getContext(), score);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventBusMsgOnMainThread(BaseEventMessage baseEventMessage) {
        switch (baseEventMessage.id) {
            case Constants.EventBusMessageId.EVENTBUS_ID_FRESH_USER:
                mPresenter.loadUser();
                break;
        }
    }
}
