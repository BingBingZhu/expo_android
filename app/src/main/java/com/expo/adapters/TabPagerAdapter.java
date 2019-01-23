package com.expo.adapters;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.expo.module.main.encyclopedia.ListFragment;
import com.expo.module.online.VrListFragment;

import java.util.List;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    private List<Tab> tabs;
    private Bundle bundle;
    private int dataType;
    private int vrType;
    public static final int TYPE_ENCYCLOPEDIA = 1;
    public static final int TYPE_VR_PANORAMA = 2;

    public TabPagerAdapter(FragmentManager fm, int type, int vrType) {
        super( fm );
        this.dataType = type;
        this.vrType = vrType;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        if (dataType == TYPE_ENCYCLOPEDIA) {
            fragment = new ListFragment();
        }else{
            fragment = new VrListFragment(vrType);
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable( "tab", tabs.get( position ) );
        fragment.setArguments( bundle );
        return fragment;
    }

    @Override
    public int getCount() {
        int count = tabs == null ? 0 : tabs.size();
        return count;
    }

    public void setTabs(List<Tab> tabs) {
        this.tabs = tabs;
        notifyDataSetChanged();
    }

    @Override
    public Parcelable saveState() {
        bundle = (Bundle) super.saveState();
        return bundle;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        bundle = (Bundle) state;
        if (bundle == null || bundle.getBoolean( "needRestore", true )) {
            super.restoreState( state, loader );
        }
    }

    public void needRestoreSave(boolean need) {
        if (bundle != null)
            bundle.putBoolean( "needRestore", need );
    }
}
