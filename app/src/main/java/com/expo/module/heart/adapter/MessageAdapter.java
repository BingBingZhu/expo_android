package com.expo.module.heart.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.expo.R;
import com.expo.base.BaseAdapterItemClickListener;
import com.expo.entity.Message;
import com.expo.utils.CommUtils;
import com.expo.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public enum ITEM_TYPE {
        ITEM_TYPE_DATA,
        ITEM_TYPE_DATE
    }

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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TYPE_DATA.ordinal()) {
            return new ViewHolder(LayoutInflater.from(mContext).inflate(mLayoutId, parent, false));
        } else {
            return new DateViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_message_date, null));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder Iholder, int position) {
        Message message = mData.get(position);
        if (Iholder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) Iholder;
            setText(holder.mName, message.getCaption());
            setText(holder.mTime, message.getCreateTime());
            setText(holder.mContent, message.getContent());
            if (holder.itemView != null) {
                holder.itemView.setOnClickListener(v -> {
                    if (mListener != null)
                        mListener.itemClick(v, position, message);
                });
            }
            if (holder.mUnRead != null)
                if (!message.isRead()) holder.mUnRead.setVisibility(View.VISIBLE);
                else holder.mUnRead.setVisibility(View.GONE);
        } else if (Iholder instanceof DateViewHolder) {
            DateViewHolder holder = (DateViewHolder) Iholder;
            holder.tvDate.setText(message.getCreateTime());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return null == mData.get(position).getUid() || mData.get(position).getUid().isEmpty()
                ? MessageAdapter.ITEM_TYPE.ITEM_TYPE_DATE.ordinal()
                : MessageAdapter.ITEM_TYPE.ITEM_TYPE_DATA.ordinal();
    }

    @Override
    public int getItemCount() {
        if (mData == null) return 0;
        return mData.size();
    }

    private void setText(TextView textView, String content) {
        if (textView != null)
            textView.setText(content);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //        TextView mDate;
        ImageView mImg;
        TextView mName;
        TextView mTime;
        TextView mContent;
        TextView mMore;
        View mUnRead;

        public ViewHolder(View itemView) {
            super(itemView);
            mImg = itemView.findViewById(R.id.message_img);
            mName = itemView.findViewById(R.id.message_name);
            mTime = itemView.findViewById(R.id.message_time);
            mContent = itemView.findViewById(R.id.message_content);
            mMore = itemView.findViewById(R.id.message_more);
            mUnRead = itemView.findViewById(R.id.message_unread);
        }
    }

    class DateViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;

        public DateViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.message_date);
        }
    }
}
