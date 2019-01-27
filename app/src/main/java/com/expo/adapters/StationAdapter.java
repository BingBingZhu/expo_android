package com.expo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.expo.R;
import com.expo.entity.Venue;
import com.expo.utils.LanguageUtil;
import com.expo.widget.SimpleHolder;
import com.expo.widget.adapter.SelectStatusAdapter;

import java.util.List;

import butterknife.BindView;

public class StationAdapter extends SelectStatusAdapter<SimpleHolder> {

    private List<Venue> mStations;

    public StationAdapter(List<Venue> stations){
        this.mStations = stations;
    }

    @Override
    public Venue getItem(int position) {
        return mStations.get(position);
    }

    @Override
    public SimpleHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_station_item, parent, false);
        Holder viewHolder = new Holder(view);
        return viewHolder;
    }

    @Override
    public void onBindCustomViewHolder(SimpleHolder holder, int position) {
        Holder holder1 = (Holder) holder;
        holder1.tv.setText(LanguageUtil.chooseTest(getItem(position).getCaption(), getItem(position).getEnCaption()));
    }

    @Override
    public int getItemCount() {
        return mStations.size();
    }

    class Holder extends SimpleHolder {

        @BindView(R.id.station_item_tv)
        TextView tv;

        public Holder(View itemView) {
            super(itemView);
        }
    }
}
