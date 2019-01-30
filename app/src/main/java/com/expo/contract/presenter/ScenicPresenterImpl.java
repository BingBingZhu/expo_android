package com.expo.contract.presenter;

import com.expo.contract.ScenicContract;
import com.expo.db.QueryParams;
import com.expo.entity.VenuesType;

import java.util.List;

public class ScenicPresenterImpl extends ScenicContract.Presenter {
    public ScenicPresenterImpl(ScenicContract.View view) {
        super(view);
    }

    @Override
    public List<VenuesType> getTabs() {
        return mDao.query(VenuesType.class, new QueryParams()
                .add("eq", "is_enable", 1)
                .add("and")
                .add("notIn", "type_name", "停车场")
                .add("and")
                .add("notIn", "type_name", "公交站")
                .add("orderBy", "idx", true));
//        List<Tab> data = VenueTypeAdapter.convertToTabList(venuesTypes);
//        mView.setTabData(data);
    }
}
