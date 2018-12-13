package com.expo.contract.presenter;

import com.expo.R;
import com.expo.adapters.DataTypeAdapter;
import com.expo.adapters.Tab;
import com.expo.contract.EncyclopediasContract;
import com.expo.contract.FindContract;
import com.expo.db.QueryParams;
import com.expo.entity.DataType;
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
        data.add(new FindTab(0L, R.string.find_tab_recommend));
        data.add(new FindTab(1L, R.string.find_tab_scenic));
        data.add(new FindTab(2L, R.string.find_tab_venue));
        data.add(new FindTab(3L, R.string.find_tab_food));
        data.add(new FindTab(4L, R.string.find_tab_botany));
        data.add(new FindTab(5L, R.string.find_tab_other));
        mView.setTabData(data);
    }
}
