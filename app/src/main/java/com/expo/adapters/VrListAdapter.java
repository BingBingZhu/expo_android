package com.expo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.expo.R;
import com.expo.entity.VrInfo;
import com.expo.module.online.detail.VRDetailActivity;
import com.expo.module.online.detail.VRImageActivity;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class VrListAdapter extends RecyclerView.Adapter<VrListAdapter.ViewHolder> {

    private List<VrInfo> mVrInfos;
    private int vrType;
    private Context mContext;

    public VrListAdapter(Context context, List<VrInfo> vrInfos, int vrType) {
        this.mContext = context;
        this.vrType = vrType;
        if (null == vrInfos) {
            this.mVrInfos = new ArrayList<>();
        } else {
            this.mVrInfos = vrInfos;
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_vr_list_item, parent, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VrInfo vr = mVrInfos.get(position);
        holder.img.setImageURI(Constants.URL.FILE_BASE_URL + vr.getPic());
        holder.tvName.setText(LanguageUtil.chooseTest(vr.getCaption(), vr.getCaptionEn()));
        holder.tvViewCount.setText(vr.getViewCount() + "");
        holder.root.setOnClickListener(v -> {
            if (StringUtils.equals(vr.getType(), Constants.VrType.VR_TYPE_IMG)) {
                VRImageActivity.startActivity(mContext, vr.getId());
            } else if (StringUtils.equals(vr.getType(), Constants.VrType.VR_TYPE_VIDEO)) {
                VRDetailActivity.startActivity(mContext, vr.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVrInfos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private View root;
        private SimpleDraweeView img;
        private TextView tvName;
        private TextView tvViewCount;
        private ImageView imgIsVideo;

        public ViewHolder(View v) {
            super(v);
            root = v.findViewById(R.id.vr_list_item_root);
            img = v.findViewById(R.id.vr_list_item_img);
            tvName = v.findViewById(R.id.vr_list_item_name);
            tvViewCount = v.findViewById(R.id.vr_list_item_view_count);
        }
    }
}
