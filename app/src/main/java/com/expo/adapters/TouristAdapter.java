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
import com.expo.widget.CompletedView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class TouristAdapter extends RecyclerView.Adapter<TouristAdapter.ViewHolder> {

    private Context mContext;
    private List<TouristType> mTourists;

    private View.OnClickListener onUseClickListener;
    private View.OnClickListener onDownloadClickListener;

    public TouristAdapter(Context context, List<TouristType> tourists) {
        this.mContext = context;
        this.mTourists = tourists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_tourist_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.setOnUseClickListener(onUseClickListener);
        holder.setOnDownloadClickListener(onDownloadClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TouristType tourist = mTourists.get(position);
        holder.imgPic.setImageURI(Constants.URL.FILE_BASE_URL + tourist.getPicUrl());
        holder.tvUse.setVisibility(tourist.getDownState() == DownloadManager.DOWNLOAD_FINISH ? View.VISIBLE : View.GONE);
        holder.tvUse.setText(tourist.isUsed() ? "已使用" : "使用");
        holder.tvUse.setSelected(tourist.isUsed());
        holder.imgSelected.setVisibility(tourist.isUsed() ? View.VISIBLE : View.GONE);
        holder.tvName.setText(tourist.getCaption());
        holder.tvSex.setText(tourist.getSex());
        holder.tvAge.setText(tourist.getAge());
        holder.tvRemark.setText(tourist.getRemark());
        String downState = "未下载";
        switch (tourist.getDownState()) {
            case DownloadManager.DOWNLOAD_IDLE:
                downState = "未下载";
                holder.completedView.setCenterPic(R.drawable.ico_tourist_download);
                break;
            case DownloadManager.DOWNLOAD_WAITING:
                downState = "等待中";
                holder.completedView.setCenterPic(R.drawable.ico_tourist_downloading);
                break;
            case DownloadManager.DOWNLOAD_STARTED:
                downState = "下载中";
                holder.completedView.setCenterPic(R.drawable.ico_tourist_downloading);
                break;
            case DownloadManager.DOWNLOAD_ERROR:
                downState = "下载失败";
                holder.completedView.setCenterPic(R.drawable.ico_tourist_down_del);
                break;
            case DownloadManager.DOWNLOAD_STOPPED:
                downState = "已暂停";
                holder.completedView.setCenterPic(R.drawable.ico_tourist_downstop);
                break;
            case DownloadManager.DOWNLOAD_FINISH:
                downState = "已完成";
                holder.completedView.setCenterPic(R.drawable.ico_tourist_downsuccess);
                break;
        }
        holder.tvOperatingBtn.setText(downState);
        holder.completedView.setProgress((int) tourist.getCurrPosition());

    }

    @Override
    public int getItemCount() {
        return mTourists.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private View downloadView;
        private ImageView imgSelected;
        private SimpleDraweeView imgPic;
        private TextView tvUse;
        private TextView tvName;
        private TextView tvSex;
        private TextView tvAge;
        private TextView tvRemark;
        private TextView tvOperatingBtn;
        private CompletedView completedView;

        public ViewHolder(View v) {
            super(v);
            downloadView = v.findViewById(R.id.tourist_item_down_progress);
            imgSelected = v.findViewById(R.id.tourist_item_selected_img);
            imgPic = v.findViewById(R.id.tourist_item_img);
            tvUse = v.findViewById(R.id.tourist_item_use);
            tvName = v.findViewById(R.id.tourist_item_name);
            tvSex = v.findViewById(R.id.tourist_item_sex);
            tvAge = v.findViewById(R.id.tourist_item_age);
            tvRemark = v.findViewById(R.id.tourist_item_remark);
            tvOperatingBtn = v.findViewById(R.id.tourist_item_operating_btn);
            completedView = v.findViewById(R.id.tourist_item_completed_view);
            tvUse.setTag(this);
            downloadView.setTag(this);
        }

        public void setOnUseClickListener(View.OnClickListener clickListener) {
            if (clickListener != null) {
                this.tvUse.setOnClickListener(clickListener);
                this.downloadView.setOnClickListener(clickListener);
            }
        }

        public void setOnDownloadClickListener(View.OnClickListener clickListener) {
            if (clickListener != null) {
                this.downloadView.setOnClickListener(clickListener);
            }
        }
    }

    public void setUseOnClickListener(View.OnClickListener onClickListener) {
        this.onUseClickListener = onClickListener;
    }

    public void setDownloadOnClickListener(View.OnClickListener onClickListener) {
        this.onDownloadClickListener = onClickListener;
    }
}
