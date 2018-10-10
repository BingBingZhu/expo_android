package com.expo.widget;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class SimpleAdapter<D, T extends SimpleHolder> extends RecyclerView.Adapter<T> {

    protected List<D> mData;

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setData(List data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public void addData(List data) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.addAll( data );
        notifyDataSetChanged();
    }

    public void addData(D data) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.add( data );
        notifyDataSetChanged();
    }

    public void clear() {
        if (mData != null && !mData.isEmpty()) {
            mData.clear();
        }
    }

    public void remove(Object data) {
        if (mData == null || mData.isEmpty())
            return;
        if (mData.contains( data ))
            mData.remove( data );
    }

    public Object getItem(int position) {
        if (mData == null || mData.isEmpty())
            return null;
        if (position >= 0 && position < getItemCount())
            return mData.get( position );
        return null;
    }
}
