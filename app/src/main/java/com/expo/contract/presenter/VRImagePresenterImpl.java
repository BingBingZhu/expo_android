package com.expo.contract.presenter;

import com.expo.contract.VRImageContract;
import com.expo.entity.VrInfo;

import java.util.ArrayList;
import java.util.List;

public class VRImagePresenterImpl extends VRImageContract.Presenter {
    public VRImagePresenterImpl(VRImageContract.View view) {
        super(view);
    }

    public void loadVrRecommend(VrInfo vrInfo) {
        List<VrInfo> list = new ArrayList<>();
        // list改为从数据库中查找
        // list去掉传递过来的vrinfo，为了将vrinfo放在第一位

        list.add(0, vrInfo);
        mView.freshVrList(list);
    }

}
