package com.expo.contract.presenter;

import com.expo.contract.FindListContract;
import com.expo.contract.ListContract;
import com.expo.db.QueryParams;
import com.expo.entity.Encyclopedias;
import com.expo.entity.Find;

import java.util.ArrayList;
import java.util.List;

public class FindListPresenterImpl extends FindListContract.Presenter {

    private static final int PER_PAGE_COUNT = 10;

    public FindListPresenterImpl(FindListContract.View view) {
        super(view);
    }

    @Override
    public void fresh(Long tabId, int page) {
        mView.addFindList(getFindList(), true);
    }

    @Override
    public void load(Long tabId, int page) {
        mView.addFindList(getFindList(), false);
    }

    private List<Find> getFindList() {
        List<Find> list = new ArrayList<>();
        List<String> picUrl = new ArrayList<>();

        picUrl.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544683816&di=c68ce4df8fe87eceacfbecf86295d4ac&imgtype=jpg&er=1&src=http%3A%2F%2Fwww.deyu.ln.cn%2Fimages%2Fnewtoltwmnuw2zzomnxw2%2Ftrim%2Fb22dae8cc945f30faa3710611c5d2c97594160%2Ftrim.jpg");
        picUrl.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544089267338&di=7e1e6a88632a1cef68bf01350dce5544&imgtype=0&src=http%3A%2F%2Fpic34.photophoto.cn%2F20150122%2F0005018338807223_b.jpg");
        picUrl.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2990368894,2130556537&fm=26&gp=0.jpg");
        picUrl.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544684034&di=d58b1dd4f62b7412312e85ef74676d42&imgtype=jpg&er=1&src=http%3A%2F%2Fpic33.photophoto.cn%2F20141031%2F0005018343702145_b.jpg");
        picUrl.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544089325236&di=1232d76ab50443cb1555e23010e7e636&imgtype=0&src=http%3A%2F%2Fpic21.photophoto.cn%2F20111111%2F0005018625204850_b.jpg");
        picUrl.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544089325236&di=4f003d2950c23d134ae021c28b18637c&imgtype=0&src=http%3A%2F%2Fpic25.photophoto.cn%2F20121104%2F0005018389733737_b.jpg");
        picUrl.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544684062&di=0f1860cb298ae4f6753ad31181b4e087&imgtype=jpg&er=1&src=http%3A%2F%2Fpic35.nipic.com%2F20131107%2F7487939_014644427000_2.jpg");

        for (int i = 0; i < 10; i++) {
            Find find = new Find();
            find.picUrl = picUrl;
            find.name = "小红薯";
            find.like = "544";
            find.scans = "9999";
            find.content = "走走停停，街拍别有一番风趣体验！走走停停街拍别有一番风趣体验！走走停停，街拍别有一番风趣体验，走走停停体验！";
            find.head = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544089387109&di=af5f761405cb61be1e35672e53214227&imgtype=0&src=http%3A%2F%2Fs11.sinaimg.cn%2Fmw690%2F5c85a56bgx6D4b2S8DU5a%26690";
            list.add(find);
        }
        return list;
    }

}
