package com.expo.module.heart.message;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.expo.base.utils.BaseAdapterItemClickListener;

import butterknife.ButterKnife;

public abstract class MessageViewHolder<T> extends RecyclerView.ViewHolder {

    public MessageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public abstract void setData(T t, BaseAdapterItemClickListener listener);

}
