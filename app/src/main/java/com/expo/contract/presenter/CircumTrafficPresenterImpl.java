package com.expo.contract.presenter;

import com.expo.contract.CircumTrafficContract;
import com.expo.db.QueryParams;
import com.expo.entity.Venue;
import com.expo.entity.VenuesType;

import java.util.List;

public class CircumTrafficPresenterImpl extends CircumTrafficContract.Presenter {

    public CircumTrafficPresenterImpl(CircumTrafficContract.View view) {
        super(view);
    }

    @Override
    public void loadTrafficData() {
        VenuesType park = mDao.unique(VenuesType.class, new QueryParams()
                .add("eq", "type_name", "停车场"));
        VenuesType bus = mDao.unique(VenuesType.class, new QueryParams()
                .add("eq", "type_name", "公交站"));

        List<Venue> venuePark = mDao.query(Venue.class, new QueryParams()
                .add("eq", "is_enable", 1).add("and")
                .add("eq", "type", park.getId()));
        List<Venue> venueBus = mDao.query(Venue.class, new QueryParams()
                .add("eq", "is_enable", 1).add("and")
                .add("eq", "type", bus.getId()));
        mView.loadTrafficDataRes(venuePark, venueBus);
    }
}
