package com.expo.contract.presenter;

import com.expo.R;
import com.expo.contract.FindContract;
import com.expo.contract.SceneTabContract;
import com.expo.module.main.find.FindTab;

import java.util.ArrayList;
import java.util.List;

public class SceneTabPresenterImpl extends SceneTabContract.Presenter {
    public SceneTabPresenterImpl(SceneTabContract.View view) {
        super(view);
    }

    @Override
    public void loadTabs() {
        List<FindTab> data = new ArrayList<>();
        data.add(new FindTab("", R.string.expo_scene_tab_space));
        data.add(new FindTab("1", R.string.expo_scene_tab_scenery));
        data.add(new FindTab("2", R.string.expo_scene_tab_culture));
        data.add(new FindTab("3", R.string.expo_scene_tab_knowledge));
        data.add(new FindTab("4", R.string.expo_scene_tab_green));
        mView.setTabData(data);
    }
}
