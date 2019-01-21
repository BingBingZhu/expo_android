package com.expo.module.online;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.expo.R;
import com.expo.entity.RollData;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

class RecyclerLayoutAdapter extends RecyclerView.Adapter<RecyclerLayoutAdapter.ViewHolder> {

    private Context context;
    private List<RollData> rollDataList;

    public RecyclerLayoutAdapter(Context context, List<RollData> rollDataList) {
        this.context = context;
        this.rollDataList = rollDataList;
    }

    @NonNull
    @Override
    public RecyclerLayoutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_banner_item, parent, false);
        RecyclerLayoutAdapter.ViewHolder holder = new RecyclerLayoutAdapter.ViewHolder(view);
//        holder.setOnItemClickListener(onItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RollData rollData = rollDataList.get(position);
//        holder.imgIco.set
        holder.img.setImageURI(rollData.getUrl());
        holder.tvName.setText(rollData.getName());
        holder.tvCount.setText(rollData.getCount()+"次");
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView img;
        TextView tvName;
        TextView tvCount;

        public ViewHolder(View v) {
            super(v);
            img = v.findViewById(R.id.banner_image);
            tvName = v.findViewById(R.id.banner_name);
            tvCount = v.findViewById(R.id.banner_count);
            v.setTag(this);
        }

//        public void setOnItemClickListener(View.OnClickListener clickListener) {
//            if (clickListener != null) {
//                this.itemView.setOnClickListener(clickListener);
//            }
//        }
    }

    @Override
    public int getItemCount() {
        return rollDataList.size();
    }
}
