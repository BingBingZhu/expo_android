package com.expo.module.main.find;

import android.os.Bundle;

import com.expo.R;
import com.expo.base.BaseFragment;
import com.expo.contract.FindContract;
import com.expo.entity.CommonInfo;
import com.expo.widget.X5WebView;

import java.util.List;

import butterknife.BindView;

public class FindFragment2 extends BaseFragment<FindContract.Presenter> implements FindContract.View {

    @BindView(R.id.common_x5)
    X5WebView x5WebView;

    @Override
    public int getContentView() {
        return R.layout.fragment_find_2;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        x5WebView.loadUrl(mPresenter.loadCommonInfo(CommonInfo.PORTAL_WEBSITE_INTEGRATION));
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    @Override
    public void setTabData(List<FindTab> tabs) {

    }

    @Override
    public boolean isNeedPaddingTop() {
        return false;
    }
}
