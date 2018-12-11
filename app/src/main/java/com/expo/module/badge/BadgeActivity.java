package com.expo.module.badge;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.contract.BadgeContract;
import com.expo.entity.Badge;
import com.expo.utils.BitmapUtils;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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

    private List<Bitmap> bitmaps;
    private List<TextView> scoreTVs;
    private List<TextView> nameTVs;
    private List<ImageView> imgs;

    @Override
    protected int getContentView() {
        return R.layout.activity_badge;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(1, "世园知识徽章");
        initBitmapsAndView();
        mPresenter.loadBadgeData();
    }

    private void initBitmapsAndView(){
        bitmaps = new ArrayList<>();
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ico_badge_lv1));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ico_badge_lv2));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ico_badge_lv3));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ico_badge_lv4));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ico_badge_lv5));
        scoreTVs = new ArrayList<>();
        scoreTVs.add(tvScoreLv1);
        scoreTVs.add(tvScoreLv2);
        scoreTVs.add(tvScoreLv3);
        scoreTVs.add(tvScoreLv4);
        scoreTVs.add(tvScoreLv5);
        nameTVs = new ArrayList<>();
        nameTVs.add(tvNameLv1);
        nameTVs.add(tvNameLv2);
        nameTVs.add(tvNameLv3);
        nameTVs.add(tvNameLv4);
        nameTVs.add(tvNameLv5);
        imgs = new ArrayList<>();
        imgs.add(imgLv1);
        imgs.add(imgLv2);
        imgs.add(imgLv3);
        imgs.add(imgLv4);
        imgs.add(imgLv5);
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
        for (int i = index+1; i < badges.size() ; i++){
            imgs.get(i).setImageBitmap(BitmapUtils.convertToBlackWhite(bitmaps.get(i), 0));
        }
        for (int i = 0; i < badges.size(); i++){
            if (isExists[i]){
                Badge b = badges.get(i);
                nameTVs.get(i).setText(LanguageUtil.chooseTest(b.getCaption(), b.getCaptionEn()));
                scoreTVs.get(i).setText(b.getScore() + "积分");
            }
        }
        if (index < 0){
            tvMyBadgeName.setVisibility(View.GONE);
            imgMyBadgeIco.setVisibility(View.GONE);
            return;
        }else{
            tvMyBadgeName.setText(LanguageUtil.chooseTest(badges.get(index).getCaption(), badges.get(index).getCaptionEn()));
            imgMyBadgeIco.setImageBitmap(bitmaps.get(index));
        }
    }
}
