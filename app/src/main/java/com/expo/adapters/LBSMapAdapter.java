package com.expo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.expo.R;
import com.expo.entity.Venue;
import com.expo.module.map.InfoWindowListener;
import com.expo.utils.LanguageUtil;
import com.facebook.drawee.view.SimpleDraweeView;

public final class LBSMapAdapter implements AMap.InfoWindowAdapter {

    private Context mContext;
    private InfoWindowListener mListener;

    public LBSMapAdapter(Context context, InfoWindowListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        //关联布局
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_map_window_item, null);
        TextView voiceRoot = v.findViewById(R.id.park_mark_dialog_voice_img);
        SimpleDraweeView pic = v.findViewById(R.id.park_mark_dialog_pic);
        TextView asName = v.findViewById(R.id.park_mark_dialog_name);
        TextView asHint = v.findViewById(R.id.park_mark_dialog_hint);   // 场馆人多提示
        TextView asInfo = v.findViewById(R.id.park_mark_dialog_info);
        ImageView asLine = v.findViewById(R.id.park_mark_dialog_line);
        ImageView dialogClose = v.findViewById(R.id.park_mark_dialog_close);
        Venue venue = (Venue) marker.getObject();
        mListener.onStopPlay(venue);
        asName.setText(LanguageUtil.chooseTest(venue.getCaption(), venue.getEnCaption()));
        mListener.onSetPic(venue, pic);
        voiceRoot.setOnClickListener(v14 -> {
            mListener.onPlayVoice(venue);
        });
        asInfo.setOnClickListener(v12 -> {
            mListener.onToInfo(venue);
            marker.hideInfoWindow();
        });
        asLine.setOnClickListener(v13 -> {
            marker.hideInfoWindow();
            mListener.onNavigation(venue);
        });
        dialogClose.setOnClickListener(v1 -> marker.hideInfoWindow());
        return v;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
