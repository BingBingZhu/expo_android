package com.expo.contract.presenter;

import com.expo.contract.VenueListContract;
import com.expo.db.QueryParams;
import com.expo.entity.Encyclopedias;
import com.expo.entity.Venue;
import com.expo.entity.VrInfo;

import java.util.List;

public class VenueListPresenterImpl extends VenueListContract.Presenter {

    private static final int PER_PAGE_COUNT = 10;

    public VenueListPresenterImpl(VenueListContract.View view) {
        super(view);
    }

    @Override
    public void loadEncyByType(Long tabId, int page) {
        List<Venue> facilities = mDao.query(Venue.class, new QueryParams()
                .add("eq", "is_enable", 1)
                .add("and")
                .add("eq", "gis_type_id", tabId)
                .add("limit", page * PER_PAGE_COUNT, PER_PAGE_COUNT)
                .add("orderBy", "recommended_idx", false));
        mView.addEncysToList(facilities);
    }

    @Override
    public void loadVrsByType(int vrType, Long tabId, int page) {
        List<VrInfo> data = mDao.query(VrInfo.class, new QueryParams()
                .add("eq", "top_kind", vrType)
                .add("and")
                .add("like", "attr_ids", "%;" + tabId + ";%")
                .add("limit", page * PER_PAGE_COUNT, PER_PAGE_COUNT));
        mView.addVrsToList(data);
    }
}
