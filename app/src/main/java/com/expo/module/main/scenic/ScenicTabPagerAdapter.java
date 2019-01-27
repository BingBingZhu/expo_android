package com.expo.module.main.scenic;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.expo.module.main.encyclopedia.EncyclopediaFragment;
import com.expo.module.main.find.FindListFragment;
import com.expo.module.map.ParkMapFragment;

import java.util.List;

public class ScenicTabPagerAdapter extends FragmentStatePagerAdapter {


    public ScenicTabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        if (position == 0) {
            fragment = new ParkMapFragment();
        } else
            fragment = new EncyclopediaFragment();
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

}
