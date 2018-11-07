package com.expo.module.heart.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.expo.R;
import com.expo.entity.Message;
import com.expo.base.BaseAdapterItemClickListener;
import com.expo.utils.LanguageUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class MessageKindAdapter extends RecyclerView.Adapter<MessageKindAdapter.ViewHolder> {

    Context mContext;
    public List<Message> mData;
    BaseAdapterItemClickListener<Message> mListener;

    public MessageKindAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<Message> data) {
        mData = data;
    }

    public void setListener(BaseAdapterItemClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_message_king_adapter, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = mData.get(position);

        holder.img.setImageResource(getImgRes(message.getType()));
        holder.text1.setText(LanguageUtil.chooseTest(message.getCaption(), message.getCaptionEn()));
        holder.text2.setText(LanguageUtil.chooseTest(message.getContent(), message.getContentEn()));

        holder.itemView.setOnClickListener(v -> {
            if (mListener != null)
                mListener.itemClick(v, position, message);
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    private int getImgRes(String type) {
        if (StringUtils.equals("1", type))
            return R.drawable.msg_biaoqian;
        else if (StringUtils.equals("3", type))
            return R.drawable.msg_activity;
        else if (StringUtils.equals("4", type))
            return R.drawable.msg_laba;
        else if (StringUtils.equals("5", type))
            return R.drawable.msg_xitongtuisong;
        else
            return R.drawable.msg_biaoqian;
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
