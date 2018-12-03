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

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.baidu.speech.utils.LogUtil;
import com.expo.R;
import com.expo.entity.Venue;
import com.expo.entity.VenuesType;
import com.expo.utils.LanguageUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ParkActualSceneAdapter extends RecyclerView.Adapter<ParkActualSceneAdapter.ViewHolder> {

    private List<Venue> mVenues;
    private Context mContext;
    private VenuesType mVenuesType;
    private LatLng mLatLng;

    private View.OnClickListener onItemClickListener;

    public ParkActualSceneAdapter(Context context, List<Venue> venues, VenuesType venuesType, LatLng latLng) {
        this.mContext = context;
        if (null == venues) {
            this.mVenues = new ArrayList<>();
        } else {
            this.mVenues = venues;
        }
        this.mVenuesType = venuesType;
        this.mLatLng = latLng;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( mContext ).inflate( R.layout.layout_park_as_item, parent, false );
        ParkActualSceneAdapter.ViewHolder holder = new ParkActualSceneAdapter.ViewHolder( view );
        holder.setOnItemClickListener( onItemClickListener );
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Venue venue = mVenues.get( position );
        holder.imgIco.setImageBitmap( mVenuesType.getLstBitmap() );
        holder.imgIsVoice.setEnabled( !TextUtils.isEmpty( venue.getVoiceUrl() ) );
        holder.tvName.setText( LanguageUtil.chooseTest( venue.getCaption(), venue.getEnCaption() ) );
        holder.tvDistance.setText( getDistance( venue.getLat(), venue.getLng() ) );
    }

    @Override
    public int getItemCount() {
        return mVenues.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgIco;
        ImageView imgIsVoice;
        TextView tvName;
        TextView tvDistance;

        public ViewHolder(View v) {
            super( v );
            imgIco = v.findViewById( R.id.park_as_item_ico );
            imgIsVoice = v.findViewById( R.id.park_as_item_isvoice );
            tvName = v.findViewById( R.id.park_as_item_name );
            tvDistance = v.findViewById( R.id.park_as_item_distance );
            imgIsVoice.setTag( this );
            v.setTag( this );
        }

        public void setOnItemClickListener(View.OnClickListener clickListener) {
            if (clickListener != null) {
                this.itemView.setOnClickListener( clickListener );
            }
        }
    }

    public void setOnItemClickListener(View.OnClickListener clickListener) {
        this.onItemClickListener = clickListener;
    }

    private DecimalFormat mDecimalFormat;

    private String getDistance(Double lat, Double lng) {
        if (null == mLatLng || mLatLng.latitude == 0) {
            return mContext.getString(R.string.not_locate);
        }
        String units = "m";
        float distance = AMapUtils.calculateLineDistance( new LatLng( lat, lng ), mLatLng );
        if (distance >= 1000) {
            units = "km";
            distance = distance / 1000;
        }
        if (mDecimalFormat == null) {
            mDecimalFormat = new DecimalFormat( "#######.00" );
        }
        return mDecimalFormat.format( distance ) + units;
    }
}
