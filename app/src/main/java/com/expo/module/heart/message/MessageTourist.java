package com.expo.module.heart.message;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.expo.R;
import com.expo.base.utils.BaseAdapterItemClickListener;
import com.expo.module.heart.adapter.MessageAdapter;

import java.util.List;

import butterknife.BindView;

public class MessageTourist implements MessageInterface {

    MessageAdapter mAdapter;
    BaseAdapterItemClickListener mListener = new BaseAdapterItemClickListener() {
        @Override
        public void itemClick(View view, int position, Object o) {

        }
    };

    @Override
    public int getTitle() {
        return R.string.title_message_tourist_ac;
    }

    @Override
    public void setAdapter(Context context, RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mAdapter = new MessageAdapter(context));
        mAdapter.setListener(mListener);
    }

    @Override
    public void setData(List list) {
        mAdapter.setData(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public MessageViewHolder getHoldView(Context context) {
        return new HoldView(LayoutInflater.from(context).inflate(R.layout.item_message_tourist, null));
    }

    class HoldView extends MessageViewHolder {

        @BindView(R.id.message_tourist_date)
        TextView mDate;
        @BindView(R.id.message_system_name)
        TextView mName;
        @BindView(R.id.message_system_time)
        TextView mTime;
        @BindView(R.id.message_system_info)
        TextView mInfo;
        @BindView(R.id.item_tourist_info)
        TextView mTouristInfo;

        public HoldView(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(Object o, BaseAdapterItemClickListener listener) {
        }

    }
}
