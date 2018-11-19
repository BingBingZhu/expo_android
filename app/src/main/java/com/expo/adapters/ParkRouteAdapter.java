package com.expo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.expo.R;
import com.expo.entity.RouteInfo;
import com.expo.entity.VenuesType;
import com.expo.utils.LanguageUtil;

import java.util.List;

public class ParkRouteAdapter extends RecyclerView.Adapter<ParkRouteAdapter.ViewHolder> {

    private Context mContext;
    private List<RouteInfo> mRouteInfos;
    private VenuesType mVenuesType;

    private View.OnClickListener onItemClickListener;

    public ParkRouteAdapter(Context mContext, List<RouteInfo> routeInfos, VenuesType venuesType){
        this.mContext = mContext;
        this.mRouteInfos = routeInfos;
        this.mVenuesType = venuesType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_park_as_item, parent, false);
        ParkRouteAdapter.ViewHolder holder = new ParkRouteAdapter.ViewHolder(view);
        holder.setOnItemClickListener(onItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RouteInfo routeInfo = mRouteInfos.get(position);
        holder.imgIco.setImageBitmap(mVenuesType.getLstBitmap());
        holder.imgIsVoice.setEnabled(!TextUtils.isEmpty(routeInfo.voiceUrl));
        holder.tvName.setText(LanguageUtil.chooseTest(routeInfo.caption, routeInfo.captionen));
        holder.tvDistance.setText(""/*getDistance(routeInfo.getLat(), routeInfo.getLng())*/);
    }

    @Override
    public int getItemCount() {
        return mRouteInfos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgIco;
        ImageView imgIsVoice;
        TextView tvName;
        TextView tvDistance;

        public ViewHolder(View v) {
            super(v);
            imgIco = v.findViewById(R.id.park_as_item_ico);
            imgIsVoice = v.findViewById(R.id.park_as_item_isvoice);
            tvName = v.findViewById(R.id.park_as_item_name);
            tvDistance = v.findViewById(R.id.park_as_item_distance);
            v.setTag(this);
        }

        public void setOnItemClickListener(View.OnClickListener clickListener) {
            if (clickListener != null) {
                this.itemView.setOnClickListener(clickListener);
            }
        }
    }

    public void setOnItemClickListener(View.OnClickListener clickListener){
        this.onItemClickListener = clickListener;
    }
}
