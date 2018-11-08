package com.expo.module.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.expo.R;
import com.expo.base.BaseActivity;

import butterknife.BindView;

/*
 * 关于我们
 */
public class AboutActivity extends BaseActivity {

    @BindView(R.id.app_version_name)
    TextView mTvVersionName;
    @BindView(R.id.app_info)
    TextView mTvAppInfo;

    @Override
    protected int getContentView() {
        return R.layout.activity_about;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(0, R.string.item_mine_about);

        mTvVersionName.setText("v"+AppUtils.getAppVersionName());
        mTvAppInfo.setText(R.string.about_content_info);
    }

    @Override
    protected boolean hasPresenter() {
        return false;
    }

    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent(context, AboutActivity.class);
        context.startActivity(in);
    }
}
