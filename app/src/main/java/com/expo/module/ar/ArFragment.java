package com.expo.module.ar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.expo.R;
import com.expo.base.BaseFragment;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.ArContract;
import com.expo.entity.CommonInfo;
import com.expo.module.webview.WebActivity;
import com.expo.utils.Constants;
import com.expo.widget.SwipeCardLayout;

import java.util.LinkedList;
import java.util.Queue;

import butterknife.BindView;
import butterknife.OnClick;

public class ArFragment extends BaseFragment<ArContract.Presenter> implements ArContract.View {

    @BindView(R.id.scl_layout)
    SwipeCardLayout mSwipeCardLayout;
    @BindView(R.id.vr_btn)
    TextView mBtn;

    @Override
    public int getContentView() {
        return R.layout.fragment_ar;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        initSwipeCard();
        if (AppUtils.getAppInfo("com.casvd.expo_ar") == null)
            mBtn.setText("去下载");
        else
            mBtn.setText("去体验");
    }

    @Override
    public boolean isNeedPaddingTop() {
        return false;
    }

    @SuppressLint("NewApi")
    private void initSwipeCard() {
        mSwipeCardLayout.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mSwipeCardLayout.setAdapter(new SwipeCardLayout.CardAdapter<ArFragment.CardEntity>(initData()) {
            @Override
            public View bindLayout() {
                return LayoutInflater.from(getContext()).inflate(R.layout.layout_vr_card_item, null);
            }

            @Override
            public void bindData(CardEntity data, View convertView) {
                ImageView iv_card = convertView.findViewById(R.id.vr_card_item_img);
                TextView tv_title = convertView.findViewById(R.id.vr_card_item_title);
                TextView tv_intro = convertView.findViewById(R.id.vr_card_item_intro);
                iv_card.setImageResource(data.resId);
                tv_title.setText(data.title);
                tv_intro.setText(data.intro);
            }
        });
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RequestCode.REQ_AR && resultCode == Activity.RESULT_OK && data != null) {
            ToastHelper.showShort("完成交互获取到新的积分喽！");
        }
    }

    private Queue<CardEntity> initData() {
        Queue<CardEntity> data = new LinkedList<>();
        CardEntity cardEntity1 = new CardEntity(R.mipmap.vr_test, "奇幻传送门",
                "奇幻的传送门可以让您在任何时间，任何地点“走入”世园会，不仅身临其境的感受北京世界园艺博览会，还有奇幻的体验让您流连忘返！");
        CardEntity cardEntity2 = new CardEntity(R.mipmap.vr_test, "AR乐拍",
                "行走在世园会，找到北京世园会的吉祥物们，可以和他们一起合影留念，非常有趣哦！");
        CardEntity cardEntity3 = new CardEntity(R.mipmap.vr_test, "LOGO扫一扫",
                "找到带有2019北京世界园艺博览会的图标，开这个功能对准logo，有趣的体验在等着你哦！");
        data.add(cardEntity1);
        data.add(cardEntity2);
        data.add(cardEntity3);
        return data;
    }

    class CardEntity {

        public CardEntity(int resId, String title, String intro) {
            this.resId = resId;
            this.title = title;
            this.intro = intro;
        }

        public int resId;
        public String title;
        public String intro;
    }

    @OnClick({R.id.vr_btn, R.id.vr_bottom, R.id.scl_layout})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.vr_btn:
            case R.id.vr_bottom:
            case R.id.scl_layout:
                gotoVr();
                break;
        }
    }

    private void gotoVr() {
        if (AppUtils.getAppInfo("com.casvd.expo_ar") == null) {
            WebActivity.startActivity(getContext(), mPresenter.loadCommonInfo(CommonInfo.EXPO_AR_DOWNLOAD_PAGE), "Logo扫一扫");
        } else {
            Intent in = new Intent("com.casvd.expo_ar.ar");
            in.putExtra("kill", true);
            int type = 0;
            switch ( mSwipeCardLayout.position ) {
                case 0:     // 传送门
                    type = 1;
                    break;
                case 1:     // 拍照
                    type = 2;
                    break;
                case 2:     // 扫logo
                    type = 3;
                    break;
            }
            if (type == 0) {
                ToastHelper.showShort(R.string.phone_not_support);
                return;
            }
            in.setData(Uri.parse("casvd://ar.casvd.com/" + type));
            startActivityForResult(in, Constants.RequestCode.REQ_AR);
        }
    }
}
