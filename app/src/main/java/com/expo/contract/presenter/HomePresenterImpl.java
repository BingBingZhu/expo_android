package com.expo.contract.presenter;

import android.content.Context;

import com.expo.contract.HomeContract;
import com.expo.entity.Encyclopedias;
import com.expo.entity.Message;
import com.expo.entity.TopLineInfo;

import java.util.ArrayList;
import java.util.List;

import com.expo.module.heart.HeartBeatService;

public class HomePresenterImpl extends HomeContract.Presenter {
    public HomePresenterImpl(HomeContract.View view) {
        super(view);
    }

    @Override
    public void setMessageCount() {
        new Message().sendMessageCount(null);
    }

    @Override
    public void setTopLine() {
        mView.showTopLine(mDao.query(TopLineInfo.class, null));
    }

    @Override
    public void setVenue() {
        //从数据库获取
        List<Encyclopedias> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Encyclopedias encyclopedias = new Encyclopedias();
            encyclopedias.picUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1542097513&di=1e0268e10ada2e5f21d885c91127f01f&imgtype=jpg&er=1&src=http%3A%2F%2Fpic2.duowan.com%2Ftv%2F0807%2F81112579515%2F81112692772.jpg";
            encyclopedias.caption = new String[]{"中国馆", "国际馆", "生活体验馆"}[i];
            encyclopedias.captionEn = new String[]{"中国馆", "国际馆", "生活体验馆"}[i];
            list.add(encyclopedias);
        }
        mView.showVenue(list);
    }

    @Override
    public void setExhibit() {
        //从数据库获取
        List<Encyclopedias> list = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            Encyclopedias encyclopedias = new Encyclopedias();
            encyclopedias.picUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1541496680114&di=457c6a2761443037866dff39469c2bc8&imgtype=0&src=http%3A%2F%2Fpic5.nipic.com%2F20100130%2F4286795_181306438308_2.jpg";
            list.add(encyclopedias);
        }
        mView.showExhibit(list);
    }

    @Override
    public void setExhibitGarden() {
        //从数据库获取
        List<Encyclopedias> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Encyclopedias encyclopedias = new Encyclopedias();
            encyclopedias.picUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1541496858612&di=57eac455787f2c0ae523307a5160e46d&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%3D580%2Fsign%3D0b1ccbe5a2ec08fa260013af69ef3d4d%2Fd332d7160924ab186b348e6d37fae6cd7b890b1a.jpg";
            encyclopedias.caption = new String[]{"四川园", "北京园", "云南园"}[i];
            encyclopedias.captionEn = new String[]{"四川园", "北京园", "云南园"}[i];
            encyclopedias.remark = "以熊猫探索为动线的熊猫川行主题区，将熊猫野化训练的...";
            encyclopedias.remarkEn = "以熊猫探索为动线的熊猫川行主题区，将熊猫野化训练的...";
            list.add(encyclopedias);
        }
        mView.showExhibitGarden(list);
    }

    public void startHeartService(Context context) {
        HeartBeatService.startService(context);
    }

    @Override
    public void stopHeartService(Context context) {
        HeartBeatService.stopService(context);
    }
}
