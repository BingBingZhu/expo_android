package com.expo.module.online.scene;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.expo.module.main.find.FindListFragment;
import com.expo.module.main.find.FindTab;

import java.util.List;

public class SceenTabPagerAdapter extends FragmentStatePagerAdapter {

    private List<FindTab> tabs;
    private Bundle bundle;

    public SceenTabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        SceneFragment fragment = new SceneFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("tab", tabs.get(position));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        int count = tabs == null ? 0 : tabs.size();
        return count;
    }

    public void setTabs(List<FindTab> tabs) {
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
        if (bundle == null || bundle.getBoolean("needRestore", true)) {
            super.restoreState(state, loader);
        }
    }

    public void needRestoreSave(boolean need) {
        if (bundle != null)
            bundle.putBoolean("needRestore", need);
    }
}
