package com.expo.contract.presenter;

import com.expo.contract.OnlineHomeContract;
import com.expo.entity.RollData;

import java.util.ArrayList;
import java.util.List;

public class OnlineHomePresenterImpl extends OnlineHomeContract.Presenter {
    public OnlineHomePresenterImpl(OnlineHomeContract.View view) {
        super(view);
    }

    @Override
    public void loadRollData() {
        List<RollData> rollDataList = new ArrayList<>();
        for (int i= 0; i < 5; i++){
            rollDataList.add(new RollData("https://goss.vcg.com/creative/vcg/800/new/VCG41N1054353314.jpg", "中国馆生存手册上学活着的什么我也不知道，反正是假的数据", 123+i));
        }
        mView.loadRollDataRes(rollDataList);
    }
}
