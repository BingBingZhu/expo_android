package com.expo.module.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.contract.FeedbackContract;

/*
 * 意见反馈页
 */
public class FeedbackActivity extends BaseActivity<FeedbackContract.Presenter> implements FeedbackContract.View {
    @Override
    protected int getContentView() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent( context, FeedbackActivity.class );
        context.startActivity( in );
    }
}
