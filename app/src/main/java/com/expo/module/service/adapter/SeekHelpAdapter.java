package com.expo.module.service.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.donkingliang.imageselector.view.SquareImageView;
import com.expo.R;
import com.expo.base.utils.BaseAdapterItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SeekHelpAdapter extends RecyclerView.Adapter<SeekHelpAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String> mImages;
    private LayoutInflater mInflater;

    private BaseAdapterItemClickListener clickListener;
    private BaseAdapterItemClickListener deleteListener;

    public SeekHelpAdapter(Context context) {
        mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public ArrayList<String> getImages() {
        return mImages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_seek_help_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null)
                clickListener.itemClick(v, position, 0);
        });
        if (position == getItemCount() - 1) {
            holder.mImageClose.setVisibility(View.GONE);
            holder.mImage.setImageResource(R.drawable.bg_add_image_dd);
        } else {
            Picasso.with(mContext).load("file://" + mImages.get(position)).into(holder.mImage);
            holder.mImageClose.setVisibility(View.VISIBLE);

            holder.mImageClose.setOnClickListener(v -> {
                if (deleteListener != null)
                    deleteListener.itemClick(v, position, 0);
            });
        }
    }

    @Override
    public int getItemCount() {
        return mImages == null ? 1 : mImages.size() + 1;
    }

    public void refresh(ArrayList<String> images) {
        mImages = images;
        notifyDataSetChanged();
    }

    public void setClickListener(BaseAdapterItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setDeleteListener(BaseAdapterItemClickListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_seek_help_img)
        SquareImageView mImage;
        @BindView(R.id.item_seek_help_close)
        ImageView mImageClose;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
