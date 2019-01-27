package com.expo.module.service.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.expo.R;
import com.expo.base.BaseAdapterItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TouristServiceAdapter extends RecyclerView.Adapter<TouristServiceAdapter.TouristServiceViewHolder> {

    Context mContext;
    BaseAdapterItemClickListener listener;

    public TouristServiceAdapter(Context context) {
        mContext = context;
    }

    public void setListener(BaseAdapterItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TouristServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TouristServiceViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_tourist_service_adapter, null));
    }

    @Override
    public void onBindViewHolder(@NonNull TouristServiceViewHolder holder, int position) {
        holder.mImage.setImageResource(mContext.getResources().getIdentifier("ico_tourist_service_" + position, "mipmap", AppUtils.getAppPackageName()));
        holder.mText.setText(mContext.getResources().getIdentifier("item_tourist_service_text_" + position, "string", AppUtils.getAppPackageName()));
        holder.mTextIntro.setText(mContext.getResources().getIdentifier("item_tourist_service_intro_text_" + position, "string", AppUtils.getAppPackageName()));
        holder.itemView.setOnClickListener(v -> {
            if (listener != null)
                listener.itemClick(v, position, null);
        });
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    class TouristServiceViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img)
        ImageView mImage;

        @BindView(R.id.text1)
        TextView mText;

        @BindView(R.id.text2)
        TextView mTextIntro;

        public TouristServiceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
