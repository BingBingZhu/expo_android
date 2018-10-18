package com.expo.module.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.contract.SeekHelpContract;
import com.expo.utils.Constants;

/*
 * 游客求助，0:医疗救助、1:人员走失、2:寻物启事、3:治安举报、4:问询咨询通用页面
 */
public class SeekHelpActivity extends BaseActivity<SeekHelpContract.Presenter> implements SeekHelpContract.View {
    @Override
    protected int getContentView() {
        return R.layout.activity_seek_help;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    /**
     * 启动游客服务求助界面
     *
     * @param context
     * @param type    求助类型
     */
    public static void startActivity(@NonNull Context context, int type) {
        Intent in = new Intent( context, SeekHelpActivity.class );
        in.putExtra( Constants.EXTRAS.EXTRAS, type );
        context.startActivity( in );
    }
}
