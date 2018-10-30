package com.expo.module.heart.message;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.expo.base.utils.BaseAdapterItemClickListener;
import com.expo.module.heart.adapter.MessageAdapter;

import java.util.List;

public interface MessageInterface<T> {

    int getTitle();

    void setAdapter(Context context, RecyclerView recyclerView);

    void setData(List<T> list);

    MessageViewHolder getHoldView(Context context);


}
