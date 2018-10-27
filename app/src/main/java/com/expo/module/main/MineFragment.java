package com.expo.module.main;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.expo.R;
import com.expo.base.BaseFragment;
import com.expo.contract.MineContract;
import com.expo.entity.User;
import com.expo.module.heart.MessageKindActivity;
import com.expo.module.mine.FeedbackActivity;
import com.expo.module.mine.SettingActivity;
import com.expo.module.mine.UserInfoActivity;
import com.expo.widget.MySettingView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/*
 * 我的页
 */
public class MineFragment extends BaseFragment<MineContract.Presenter> implements MineContract.View {

    @BindView(R.id.mine_img)
    CircleImageView mImageView;
    @BindView(R.id.mine_name)
    TextView mTvMineName;

    @Override
    public int getContentView() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mPresenter.loadUser();
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    @Override
    public void freshUser(User user) {
        if (user == null) return;
        if (!StringUtils.isEmpty(user.getPhotoUrl()))
            Picasso.with(getContext()).load(user.getPhotoUrl()).into(mImageView);
        mTvMineName.setText(user.getNick());
    }

    @OnClick(R.id.mine_edit_info)
    public void clickEditInfo(View view) {
        UserInfoActivity.startActivity(getContext());
    }

    @OnClick(R.id.item_mine_bespeak)
    public void clickBespeak(View view) {
    }

    @OnClick(R.id.item_mine_comment_report)
    public void clickComment(View view) {
        FeedbackActivity.startActivity(getContext());
    }

    @OnClick(R.id.item_mine_message)
    public void clickMessage(View view) {
        MessageKindActivity.startActivity(getContext());
    }

    @OnClick(R.id.item_mine_about)
    public void clickAbout(View view) {
    }

    @OnClick(R.id.item_mine_setting)
    public void clickSetting(View view) {
        SettingActivity.startActivity(getContext());
    }
}
