package com.expo.module.heart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.contract.MessageKindContract;

/*
 * 消息分类列表页
 */
public class MessageKindActivity extends BaseActivity<MessageKindContract.Presenter> implements MessageKindContract.View {
    @Override
    protected int getContentView() {
        return R.layout.activity_message;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent( context, MessageKindActivity.class );
        context.startActivity( in );
    }
}
