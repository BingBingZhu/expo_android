package com.expo.module.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.FeedbackContract;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 意见反馈页
 */
public class FeedbackActivity extends BaseActivity<FeedbackContract.Presenter> implements FeedbackContract.View {

    @BindView(R.id.feed_back_email)
    EditText mEtEmail;
    @BindView(R.id.feed_back_comment)
    EditText mEtComment;

    @Override
    protected int getContentView() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(0, R.string.title_feed_back_ac);
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent(context, FeedbackActivity.class);
        context.startActivity(in);
    }

    @OnClick(R.id.submit)
    public void submit(View view) {
        mPresenter.submit("", mEtEmail.getText().toString(), mEtComment.getText().toString());
    }

    @Override
    public void submitComplete(String string) {
//        if (!StringUtils.isEmpty(string))
//            ToastHelper.showShort(string);
        ToastHelper.showShort(R.string.toast_feed_back_success);
        finish();
    }
}
