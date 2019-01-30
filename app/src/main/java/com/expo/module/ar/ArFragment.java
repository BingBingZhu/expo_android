package com.expo.module.ar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.AppUtils;
import com.expo.R;
import com.expo.base.BaseFragment;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.ArContract;
import com.expo.entity.CommonInfo;
import com.expo.module.webview.WebActivity;
import com.expo.utils.Constants;

public class ArFragment extends BaseFragment<ArContract.Presenter> implements ArContract.View {
    @Override
    public int getContentView() {
        return R.layout.fragment_ar;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public void onClick(View v) {
        if (AppUtils.getAppInfo() == null) {
            WebActivity.startActivity(getContext(), mPresenter.loadCommonInfo(CommonInfo.EXPO_AR_DOWNLOAD_PAGE), "Logo扫一扫");
        } else {
            Intent in = new Intent("com.casvd.expo_ar.ar");
            in.putExtra("kill", true);
            int type = 0;
            switch (v.getId()) {
                case R.id.ar_photograph://拍照
                    type = 1;
                    break;
                case R.id.ar_scan_logo://扫logo
                    type = 2;
                    break;
                case R.id.ar_portal://传送门
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RequestCode.REQ_AR && resultCode == Activity.RESULT_OK && data != null) {
            ToastHelper.showShort("完成交互获取到新的积分喽！");
        }
    }
}
