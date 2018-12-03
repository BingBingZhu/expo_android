package com.expo.contract.presenter;

import com.expo.base.utils.PrefsHelper;
import com.expo.contract.BadgeContract;
import com.expo.db.QueryParams;
import com.expo.entity.Badge;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.BadgeResp;
import com.expo.utils.Constants;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;

public class BadgePresenterImpl extends BadgeContract.Presenter {
    public BadgePresenterImpl(BadgeContract.View view) {
        super(view);
    }

    @Override
    public void loadBadgeData() {
        List<Badge> badges = mDao.query(Badge.class, new QueryParams());
        if ( null != badges && !badges.isEmpty() )
            mView.loadBadgeDataRes( badges );
        else
            loadBadgeInfo();
    }

    /**
     * 加载徽章列表
     */
    private void loadBadgeInfo() {
        Observable<BadgeResp> observable = Http.getServer().getBadgeList( Http.buildRequestBody( Http.getBaseParams() ) );
        Http.request( new ResponseCallback<BadgeResp>() {
            @Override
            protected void onResponse(BadgeResp rsp) {
                PrefsHelper.setString( Constants.Prefs.KEY_BADGE_UPDATE_TIME, rsp.updatetime );
                mDao.clear( Badge.class );
                List<Badge> badges = rsp.badges;
                Collections.sort(badges);
                mDao.saveOrUpdateAll( badges );
                mView.loadBadgeDataRes( badges );
            }
        }, observable );
    }


}
