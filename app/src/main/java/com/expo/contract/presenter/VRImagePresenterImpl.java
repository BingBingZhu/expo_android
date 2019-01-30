package com.expo.contract.presenter;

import com.blankj.utilcode.util.StringUtils;
import com.expo.contract.VRImageContract;
import com.expo.db.QueryParams;
import com.expo.entity.VrInfo;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.BaseResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

public class VRImagePresenterImpl extends VRImageContract.Presenter {
    public VRImagePresenterImpl(VRImageContract.View view) {
        super(view);
    }

    public void loadVrRecommend(Long id) {
        // list改为从数据库中查找
        QueryParams params = new QueryParams()
                .add("orderBy", "recommended_idx", true);
        List<VrInfo> list = mDao.query(VrInfo.class, params);
        if (list == null) return;
        // list去掉传递过来的vrinfo，为了将vrinfo放在第一位
        for (int i = 0; i < list.size(); i++) {
            if (id == list.get(i).getId()) {
                VrInfo vrInfo = list.get(i);
                list.remove(i);
                list.set(0, vrInfo);
            }
        }

        mView.freshVrList(list);
    }

}
