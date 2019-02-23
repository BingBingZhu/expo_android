package com.expo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.expo.R;
import com.expo.entity.TouristType;
import com.expo.module.download.DownloadManager;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.expo.widget.CompletedView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class TouristAdapter extends RecyclerView.Adapter<TouristAdapter.ViewHolder> {

    private Context mContext;
    private List<TouristType> mTourists;

    private View.OnClickListener onUseClickListener;

    public TouristAdapter(Context context, List<TouristType> tourists) {
        this.mContext = context;
        if (null == tourists)
            this.mTourists = new ArrayList<>();
        else
            this.mTourists = tourists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_tourist_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.setOnUseClickListener(onUseClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TouristType tourist = mTourists.get(position);
        if (null == tourist.getPicUrl() || tourist.getPicUrl().isEmpty())
            holder.imgPic.setImageResource(R.mipmap.ico_default_tour);
        else
            holder.imgPic.setImageURI(Constants.URL.FILE_BASE_URL + tourist.getPicUrl());
        holder.tvUse.setSelected(tourist.isUsed());
        holder.tvName.setText(LanguageUtil.chooseTest(tourist.getCaption(), tourist.getCaptionEN()));
        holder.tvSex.setText(mContext.getString(R.string.sex_is) +
                (tourist.getSex().equals("0") ? mContext.getString(R.string.boy) : mContext.getString(R.string.girl)));
        holder.tvAge.setText(String.format(mContext.getString(R.string.load_age_info), tourist.getAge()));

        holder.tvRemark.setText(tourist.getRemark());
        String downState = "下载";
        switch (tourist.getDownState()) {
            case DownloadManager.DOWNLOAD_IDLE:
                downState = "下载";
                holder.tvFileSize.setText(Math.round(tourist.getModelFileSize() / tourist.getModelFileSize()) + "M");
                holder.tvFileSize.setVisibility(View.VISIBLE);
                holder.completedView.setVisibility(View.GONE);
                break;
            case DownloadManager.DOWNLOAD_WAITING:
                downState = mContext.getString(R.string.waiting);
                holder.completedView.setVisibility(View.VISIBLE);
                break;
            case DownloadManager.DOWNLOAD_STARTED:
                downState = mContext.getString(R.string.be_downloading);
                holder.completedView.setVisibility(View.VISIBLE);
                break;
            case DownloadManager.DOWNLOAD_ERROR:
                downState = mContext.getString(R.string.download_failed);
                holder.completedView.setVisibility(View.VISIBLE);
                break;
            case DownloadManager.DOWNLOAD_STOPPED:
                downState = mContext.getString(R.string.paused);
                holder.completedView.setVisibility(View.VISIBLE);
                break;
            case DownloadManager.DOWNLOAD_FINISH:
                downState = "立即使用";
                holder.tvFileSize.setVisibility(View.GONE);
                holder.completedView.setVisibility(View.GONE);
                break;
        }
        int progress = (int) Math.round((double) tourist.getCurrPosition() / (double) tourist.getModelFileSize() * 100);
        holder.completedView.setProgress(progress);
        holder.tvUse.setText(downState);
        if (tourist.getDownState() == DownloadManager.DOWNLOAD_FINISH && !tourist.isUsed()) {
            holder.tvUse.setText(downState);
        } else if (tourist.getDownState() == DownloadManager.DOWNLOAD_FINISH && tourist.isUsed()) {
            holder.tvUse.setText("指路中");
        }
    }

    @Override
    public int getItemCount() {
        return mTourists.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private View downloadView;
        //        private ImageView imgSelected;        // 是否选中使用
        private SimpleDraweeView imgPic;
        private TextView tvUse;
        private TextView tvName;
        private TextView tvSex;
        private TextView tvAge;
        private TextView tvRemark;
        //        private TextView tvOperatingBtn;      // 立即下载按钮、、 下载状态
        private CompletedView completedView;
        private TextView tvFileSize;

        public ViewHolder(View v) {
            super(v);
            downloadView = v.findViewById(R.id.tourist_item_down_progress);
//            imgSelected = v.findViewById(R.id.tourist_item_selected_img);
            imgPic = v.findViewById(R.id.tourist_item_img);
            tvUse = v.findViewById(R.id.tourist_item_use);
            tvName = v.findViewById(R.id.tourist_item_name);
            tvSex = v.findViewById(R.id.tourist_item_sex);
            tvAge = v.findViewById(R.id.tourist_item_age);
            tvRemark = v.findViewById(R.id.tourist_item_remark);
//            tvOperatingBtn = v.findViewById(R.id.tourist_item_operating_btn);
            completedView = v.findViewById(R.id.tourist_item_completed_view);
            tvFileSize = v.findViewById(R.id.tourist_item_file_size);
            tvUse.setTag(this);
            downloadView.setTag(this);
        }

        public void setOnUseClickListener(View.OnClickListener clickListener) {
            if (clickListener != null) {
                this.tvUse.setOnClickListener(clickListener);
            }
        }
    }

    public void setUseOnClickListener(View.OnClickListener onClickListener) {
        this.onUseClickListener = onClickListener;
    }
}
