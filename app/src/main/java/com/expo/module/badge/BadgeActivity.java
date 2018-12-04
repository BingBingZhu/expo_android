package com.expo.module.badge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.contract.BadgeContract;
import com.expo.entity.Badge;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;

import java.util.List;

import butterknife.BindView;

public class BadgeActivity extends BaseActivity<BadgeContract.Presenter> implements BadgeContract.View {

    @BindView(R.id.my_badge_name)
    TextView tvMyBadgeName;
    @BindView(R.id.my_badge_ico)
    ImageView imgMyBadgeIco;

    @BindView(R.id.badge_pic_lv1)
    ImageView imgLv1;
    @BindView(R.id.badge_pic_lv2)
    ImageView imgLv2;
    @BindView(R.id.badge_pic_lv3)
    ImageView imgLv3;
    @BindView(R.id.badge_pic_lv4)
    ImageView imgLv4;
    @BindView(R.id.badge_pic_lv5)
    ImageView imgLv5;
    @BindView(R.id.badge_score_lv1)
    TextView tvScoreLv1;
    @BindView(R.id.badge_score_lv2)
    TextView tvScoreLv2;
    @BindView(R.id.badge_score_lv3)
    TextView tvScoreLv3;
    @BindView(R.id.badge_score_lv4)
    TextView tvScoreLv4;
    @BindView(R.id.badge_score_lv5)
    TextView tvScoreLv5;
    @BindView(R.id.badge_name_lv1)
    TextView tvNameLv1;
    @BindView(R.id.badge_name_lv2)
    TextView tvNameLv2;
    @BindView(R.id.badge_name_lv3)
    TextView tvNameLv3;
    @BindView(R.id.badge_name_lv4)
    TextView tvNameLv4;
    @BindView(R.id.badge_name_lv5)
    TextView tvNameLv5;

    @Override
    protected int getContentView() {
        return R.layout.activity_badge;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(1, "世园知识徽章");
        mPresenter.loadBadgeData();
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(Context context, int score){
        Intent intent = new Intent(context, BadgeActivity.class);
        intent.putExtra(Constants.EXTRAS.EXTRA_USER_SCORE, score);
        context.startActivity(intent);
    }

    @Override
    public void loadBadgeDataRes(List<Badge> badges) {
        boolean[] isExists = new boolean[5];
        int score = getIntent().getIntExtra(Constants.EXTRAS.EXTRA_USER_SCORE, 0);
        int index = -1;
        for ( int i = 0; i < badges.size(); i++ ){
            isExists[i] = true;
            if (score >= badges.get(i).getScore()){
                index = i;
            }
        }
        Badge b;
        if (isExists[0]) {
            b = badges.get(0);
            tvNameLv1.setText(LanguageUtil.chooseTest(b.getCaption(), b.getCaptionEn()));
            tvScoreLv1.setText(b.getScore() + "积分");
        }
        if (isExists[1]) {
            b = badges.get(1);
            tvNameLv2.setText(LanguageUtil.chooseTest(b.getCaption(), b.getCaptionEn()));
            tvScoreLv2.setText(b.getScore() + "积分");
        }
        if (isExists[2]) {
            b = badges.get(2);
            tvNameLv3.setText(LanguageUtil.chooseTest(b.getCaption(), b.getCaptionEn()));
            tvScoreLv3.setText(b.getScore() + "积分");
        }
        if (isExists[3]) {
            b = badges.get(3);
            tvNameLv4.setText(LanguageUtil.chooseTest(b.getCaption(), b.getCaptionEn()));
            tvScoreLv4.setText(b.getScore() + "积分");
        }
        if (isExists[4]) {
            b = badges.get(4);
            tvNameLv5.setText(LanguageUtil.chooseTest(b.getCaption(), b.getCaptionEn()));
            tvScoreLv5.setText(b.getScore() + "积分");
        }
        if (index < 0){
            tvMyBadgeName.setVisibility(View.GONE);
            imgMyBadgeIco.setVisibility(View.GONE);
            return;
        }
        b = badges.get(index);
        tvMyBadgeName.setText(LanguageUtil.chooseTest(b.getCaption(), b.getCaptionEn()));
        switch (index){
            case 0:
                imgMyBadgeIco.setImageResource(R.mipmap.ico_badge_lv1);
                break;
            case 1:
                imgMyBadgeIco.setImageResource(R.mipmap.ico_badge_lv2);
                break;
            case 2:
                imgMyBadgeIco.setImageResource(R.mipmap.ico_badge_lv3);
                break;
            case 3:
                imgMyBadgeIco.setImageResource(R.mipmap.ico_badge_lv4);
                break;
            case 4:
                imgMyBadgeIco.setImageResource(R.mipmap.ico_badge_lv5);
                break;
        }
    }
}
