package com.expo.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.expo.module.main.encyclopedia.ListFragment;

import java.util.List;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    private List<Tab> tabs;

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        ListFragment fragment = new ListFragment();
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
}
