package com.expo.module.heart.message;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.expo.R;
import com.expo.base.utils.BaseAdapterItemClickListener;
import com.expo.entity.Message;
import com.expo.module.heart.adapter.MessageAdapter;

import java.util.List;

import butterknife.BindView;

public class MessageService implements MessageInterface {

    MessageAdapter mAdapter;
    BaseAdapterItemClickListener mListener = new BaseAdapterItemClickListener() {
        @Override
        public void itemClick(View view, int position, Object o) {

        }
    };

    @Override
    public int getTitle() {
        return R.string.title_message_system_ac;
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
        return new HoldView(LayoutInflater.from(context).inflate(R.layout.item_message_system, null));
    }

    class HoldView extends MessageViewHolder<Message> {

        @BindView(R.id.message_system_name)
        TextView mName;
        @BindView(R.id.message_system_time)
        TextView mTime;
        @BindView(R.id.message_system_info)
        TextView mInfo;

        public HoldView(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(Message message, BaseAdapterItemClickListener listener) {
        }

    }
}
