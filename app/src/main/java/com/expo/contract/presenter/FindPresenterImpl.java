package com.expo.contract.presenter;

import com.expo.R;
import com.expo.contract.FindContract;
import com.expo.db.QueryParams;
import com.expo.entity.CommonInfo;
import com.expo.module.main.find.FindTab;

import java.util.ArrayList;
import java.util.List;

public class FindPresenterImpl extends FindContract.Presenter {
    public FindPresenterImpl(FindContract.View view) {
        super(view);
    }

    @Override
    public void loadTabs() {
        List<FindTab> data = new ArrayList<>();
        data.add(new FindTab("", R.string.find_tab_recommend));
        data.add(new FindTab("1", R.string.find_tab_scenic));
        data.add(new FindTab("2", R.string.find_tab_venue));
        data.add(new FindTab("3", R.string.find_tab_food));
        data.add(new FindTab("4", R.string.find_tab_botany));
        data.add(new FindTab("5", R.string.find_tab_other));
        mView.setTabData(data);
    }

    @Override
    public String loadCommonInfo(String portalWebsiteIntegration) {
        CommonInfo commonInfo = mDao.unique( CommonInfo.class, new QueryParams().add( "eq", "type", portalWebsiteIntegration ) );
        if (commonInfo != null)
            return commonInfo.getLinkUrl();
        return null;
    }
}
