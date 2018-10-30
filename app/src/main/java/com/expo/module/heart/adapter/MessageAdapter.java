package com.expo.module.heart.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.expo.base.utils.BaseAdapterItemClickListener;
import com.expo.module.heart.message.MessageInterface;
import com.expo.module.heart.message.MessageViewHolder;

import java.util.List;

public class MessageAdapter<T> extends RecyclerView.Adapter<MessageViewHolder<T>> {

    Context mContext;
    public List<T> mData;
    MessageInterface mMessageInterface;
    BaseAdapterItemClickListener mListener;

    public MessageAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<T> data) {
        mData = data;
    }

    public void setListener(BaseAdapterItemClickListener mListener) {
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public MessageViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return mMessageInterface.getHoldView(mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder<T> holder, int position) {
        holder.setData(mData.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        if (mData == null) return 0;
        return mData.size();
    }
}
