package com.expo.module.main.find;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.expo.R;
import com.expo.base.utils.ImageUtils;
import com.expo.entity.Find;
import com.expo.module.main.find.detail.FindDetailActivity;
import com.expo.utils.CommUtils;
import com.squareup.picasso.Picasso;

import org.raphets.roundimageview.RoundImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FindListAdapter extends RecyclerView.Adapter<FindListAdapter.ViewHolder> {

    private List<Find> mData;
    private Context mContext;
    private Handler mHandler;

    public FindListAdapter(Context context, Handler handler, List<Find> data) {
        this.mContext = context;
        if (null == data) {
            this.mData = new ArrayList<>();
        } else {
            this.mData = data;
        }
        this.mHandler = handler;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_find_list, parent, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Find find = mData.get(position);
        if (find.url1.endsWith(".mp4")) {
            new Thread(() -> {
                Bitmap bitmap = ImageUtils.createVideoThumbnail(CommUtils.getFullUrl(find.url1), MediaStore.Images.Thumbnails.MINI_KIND);
                if (mHandler != null)
                    mHandler.post(() -> holder.img.setImageBitmap(bitmap));
            }).start();
        } else
            Picasso.with(mContext).load(CommUtils.getFullUrl(find.url1)).into(holder.img);
        if ((position + 1) % 4 > 1) {
            holder.img.getLayoutParams().height = (int) mContext.getResources().getDimension(R.dimen.dms_468);
        } else {
            holder.img.getLayoutParams().height = (int) mContext.getResources().getDimension(R.dimen.dms_344);
        }
        holder.content.setText(find.remark);
        if (!StringUtils.isEmpty(find.upic))
            CommUtils.setImgPic(mContext, find.upic, holder.head);
        holder.name.setText(find.caption);
        holder.scans.setText(find.views);
        holder.like.setText(find.enjoys);
        if (find.type == 1) {
            holder.videoImg.setVisibility(View.VISIBLE);
        } else {
            holder.videoImg.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(v -> FindDetailActivity.startActivity(mContext, find));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.find_list_img)
        ImageView img;
        @BindView(R.id.item_find_video_control)
        ImageView videoImg;
        @BindView(R.id.find_list_content)
        TextView content;
        @BindView(R.id.find_list_name)
        TextView name;
        @BindView(R.id.find_list_head)
        RoundImageView head;
        @BindView(R.id.find_list_scans)
        TextView scans;
        @BindView(R.id.find_list_like)
        TextView like;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
