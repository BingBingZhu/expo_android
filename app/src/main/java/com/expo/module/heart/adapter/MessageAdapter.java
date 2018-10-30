package com.expo.module.heart.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.expo.R;
import com.expo.base.utils.BaseAdapterItemClickListener;
import com.expo.entity.Message;
import com.expo.utils.CommUtils;

import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    Context mContext;
    public List<Message> mData;
    BaseAdapterItemClickListener mListener;
    int mLayoutId;

    public MessageAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<Message> data) {
        mData = data;
    }

    public void setListener(BaseAdapterItemClickListener mListener) {
        this.mListener = mListener;
    }

    public void setLayoutId(int layoutId) {
        this.mLayoutId = layoutId;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(mLayoutId, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Message message = mData.get(position);
        setText(holder.mName, message.getCaption());
        setText(holder.mTime, message.getCreateTime());
        setText(holder.mContent, message.getContent());

        if (holder.mMore != null)
            holder.mMore.setOnClickListener(v -> {
                if (mListener != null)
                    mListener.itemClick(v, position, message);
            });

        initView(holder);
    }

    @Override
    public int getItemCount() {
        if (mData == null) return 0;
        return mData.size();
    }

    private void initView(MessageAdapter.ViewHolder holder) {
        CommUtils.hideView(holder.mDate);
        if (mLayoutId == R.layout.item_message_tourist) {
            CommUtils.hideView(holder.mImg);
        }
    }

    private void setText(TextView textView, String content) {
        if (textView != null)
            textView.setText(content);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mDate;
        ImageView mImg;
        TextView mName;
        TextView mTime;
        TextView mContent;
        TextView mMore;

        public ViewHolder(View itemView) {
            super(itemView);
            mDate = itemView.findViewById(R.id.message_date);
            mImg = itemView.findViewById(R.id.message_img);
            mName = itemView.findViewById(R.id.message_name);
            mTime = itemView.findViewById(R.id.message_time);
            mContent = itemView.findViewById(R.id.message_content);
            mMore = itemView.findViewById(R.id.message_more);
        }

    }
}
