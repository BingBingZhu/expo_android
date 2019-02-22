package com.expo.contract.presenter;

import com.expo.contract.CircumTrafficContract;
import com.expo.db.QueryParams;
import com.expo.entity.Park;
import com.expo.entity.Venue;
import com.expo.entity.VenuesType;
import com.expo.network.Http;
import com.expo.utils.Constants;

import java.util.List;

public class CircumTrafficPresenterImpl extends CircumTrafficContract.Presenter {

    public CircumTrafficPresenterImpl(CircumTrafficContract.View view) {
        super(view);
    }

    @Override
    public void loadTrafficData() {
        Park mPark = mDao.unique(Park.class, null);
        if (mPark != null && mPark.getElectronicFenceList().size() >= 3) {
            mView.showParkScope(mPark);
        }

        VenuesType park = mDao.unique(VenuesType.class, new QueryParams()
                .add("eq", "type_name", "停车场"));
        VenuesType bus = mDao.unique(VenuesType.class, new QueryParams()
                .add("eq", "type_name", "公交站"));

        List<Venue> venuePark = null;
        List<Venue> venueBus = null;
        if (null != park){
            venuePark = mDao.query(Venue.class, new QueryParams()
                    .add("eq", "is_enable", 1).add("and")
                    .add("eq", "type", park.getId()));
        }
        if (null != bus) {
            venueBus = mDao.query(Venue.class, new QueryParams()
                    .add("eq", "is_enable", 1).add("and")
                    .add("eq", "type", bus.getId()));
        }

//        List<VenuesType> venuesTypes = mDao.query(VenuesType.class, new QueryParams()
//                .add("eq", "is_enable", 1)
//                .add("and")
//                .add("eq", "show_in_map", "1")
//                .add("orderBy", "idx", true));

//        mView.loadTabRes(venuesTypes, 0);
        mView.loadTrafficDataRes(venuePark, venueBus);
//        loadSubjectImages(venuesTypes);
    }

//    private void loadSubjectImages(List<VenuesType> venuesTypes) {
//        for (VenuesType vt : venuesTypes) {
//            Http.loadBitmap(Constants.URL.FILE_BASE_URL + vt.getPicMarkUrl(), mOnLoadImageCompleteListener, vt);
//        }
//    }
//
//    private Http.OnLoadImageCompleteListener mOnLoadImageCompleteListener = (url, bmp, obj) -> {
//        if (!(obj instanceof VenuesType)) return;
//        VenuesType vt = (VenuesType) obj;
//        if (url.endsWith(vt.getPicMarkUrl())) {
//            vt.setMarkBitmap(bmp);
//        }
//        mView.updatePic(vt);
//    };
}
