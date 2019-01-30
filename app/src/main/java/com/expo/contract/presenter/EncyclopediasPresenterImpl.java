package com.expo.contract.presenter;

import android.content.Context;

import com.expo.R;
import com.expo.adapters.DataTypeAdapter;
import com.expo.adapters.Tab;
import com.expo.adapters.VenueTypeAdapter;
import com.expo.contract.EncyclopediasContract;
import com.expo.db.QueryParams;
import com.expo.entity.DataType;
import com.expo.entity.Venue;
import com.expo.entity.VenuesType;

import java.util.List;

public class EncyclopediasPresenterImpl extends EncyclopediasContract.Presenter {
    public EncyclopediasPresenterImpl(EncyclopediasContract.View view) {
        super(view);
    }

    @Override
    public void loadTabs(Context context, List<VenuesType> list) {
//        // 原百科
//        List<DataType> encyclopediaTypes = mDao.query( DataType.class, new QueryParams()
//                .add( "eq", "kind", 3 )
//                .add( "and" )
//                .add( "eq", "enabled", 1 )
//                .add( "orderBy", "sort", "asc" ) );
//        List<Tab> data = DataTypeAdapter.convertToTabList( encyclopediaTypes );
//        mView.setTabData( data );

//       世园景点
        if (list == null) {
            list = mDao.query(VenuesType.class, new QueryParams()
                    .add("eq", "is_enable", 1).add("and")
                    .add("eq", "show_in_scenicspot", 1).add("and")
                    .add("orderBy", "idx", true));
        }
        list.add(0, getRecommend(context));
        List<Tab> data = VenueTypeAdapter.convertToTabList(list);
        mView.setTabData(data);
    }

    //   世园景点的推荐
    private VenuesType getRecommend(Context context) {
        VenuesType vt = new VenuesType();
        vt.setId(-1L);
        vt.setIdx(1);
        vt.setIsEnable(1);
        vt.setShowInScenicspot("1");
        vt.setTypeName("推荐");
        vt.setTypeNameEn("RECOMM");
        return vt;
    }
}
