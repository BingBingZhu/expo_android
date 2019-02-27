package com.expo.module.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.expo.R;
import com.expo.base.BaseAdapterItemClickListener;
import com.expo.widget.SquareFilletImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Bitmap> mImages = new ArrayList<>();
    private ArrayList<String> mTitles = new ArrayList<>();
    private int mSelectPos;
    private LayoutInflater mInflater;
    private String imgUrl;

    private BaseAdapterItemClickListener clickListener;

    public FilterAdapter(Context context,String imgUrl) {
        mContext = context;
        this.imgUrl = imgUrl;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public ArrayList<Bitmap> getImages() {
        return mImages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_filter_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null)
                clickListener.itemClick(v, position, 0);
        });

        if(position==0){
            Picasso.with(mContext).load("file://" + imgUrl).into(holder.mImage);
        }else {
            holder.mImage.setImageBitmap(mImages.get(position));
        }

        holder.mTvTitle.setText(mTitles.get(position));
        if(mSelectPos==position){
            holder.mTvTitle.setTextColor(mContext.getResources().getColor(R.color.black_333333));
        }else {
            holder.mTvTitle.setTextColor(mContext.getResources().getColor(R.color.gray_999999));
        }
    }

    @Override
    public int getItemCount() {
        return mImages == null ? 0 : mImages.size();
    }

    public void refresh(ArrayList<Bitmap> images, ArrayList<String> titles, int selectPos) {
        mImages = images;
        mTitles = titles;
        mSelectPos = selectPos;
        notifyDataSetChanged();
    }

    public void setClickListener(BaseAdapterItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_seek_help_img)
        SquareFilletImageView mImage;
        @BindView(R.id.item_seek_help_tv)
        TextView mTvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
