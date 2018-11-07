package com.expo.module.heart.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.expo.R;
import com.expo.entity.Message;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class MessageKindAdapter extends RecyclerView.Adapter<MessageKindAdapter.ViewHolder> {

    Context mContext;
    public List<Message> mData;

    public MessageKindAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<Message> data) {
        mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_message_king_adapter, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.img.setImageResource(mData.get(position).resId);
//        holder.text1.setText(mData.get(position).text1);
//        holder.text2.setText(mData.get(position).text2);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView img;
        TextView text1, text2;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            text1 = itemView.findViewById(R.id.text1);
            text2 = itemView.findViewById(R.id.text2);
        }
    }
}
