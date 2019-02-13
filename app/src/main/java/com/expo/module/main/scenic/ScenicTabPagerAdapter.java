package com.expo.module.main.scenic;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.blankj.utilcode.util.StringUtils;
import com.expo.entity.VenuesType;
import com.expo.module.main.encyclopedia.EncyclopediaFragment;
import com.expo.module.main.venue.VenueFragment;
import com.expo.module.map.ParkMapFragment;

import java.util.ArrayList;
import java.util.List;

public class ScenicTabPagerAdapter extends FragmentStatePagerAdapter {

    List<VenuesType> mList;

    public ScenicTabPagerAdapter(FragmentManager fm, List<VenuesType> list) {
        super(fm);
        mList = list;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        if (position == 0) {
            fragment = new ParkMapFragment(/*getTabType(true)*/null);
        } else
            fragment = new EncyclopediaFragment(getTabType(false));
//            fragment = new VenueFragment(getTabType(false));
        return fragment;
    }

    private List<VenuesType> getTabType(boolean showInMap) {
        List<VenuesType> list = new ArrayList<>();
        if (mList != null) {
            for (int i = 0; i < mList.size(); i++) {
                if (showInMap && StringUtils.equals("1", mList.get(i).getShowInMap())) {
                    list.add(mList.get(i));
                } else if (!showInMap && StringUtils.equals("1", mList.get(i).getShowInScenicspot())) {
                    list.add(mList.get(i));
                }
            }
        }
        return list;
    }

    @Override
    public int getCount() {
        return 2;
    }

}
