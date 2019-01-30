package com.expo.contract.presenter;

import com.expo.adapters.VenueTypeAdapter;
import com.expo.adapters.Tab;
import com.expo.contract.VenueContract;
import com.expo.db.QueryParams;
import com.expo.entity.VenuesType;

import java.util.List;

public class VenuePresenterImpl extends VenueContract.Presenter {
    public VenuePresenterImpl(VenueContract.View view) {
        super(view);
    }

    @Override
    public void loadTabs() {
        List<VenuesType> venuesTypes = mDao.query(VenuesType.class, new QueryParams()
                .add("eq", "is_enable", 1).add("and")
                .add("eq", "show_in_scenicspot", 1).add("and")
                .add("orderBy", "idx", true));
        List<Tab> data = VenueTypeAdapter.convertToTabList(venuesTypes);
        mView.setTabData(data);
    }
}
