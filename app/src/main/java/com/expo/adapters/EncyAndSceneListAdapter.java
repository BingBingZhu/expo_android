package com.expo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.expo.R;
import com.expo.base.utils.ToastHelper;
import com.expo.entity.ActualScene;
import com.expo.entity.Encyclopedias;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class EncyAndSceneListAdapter extends RecyclerView.Adapter<EncyAndSceneListAdapter.ViewHolder> {

    private List<ListItemData> mEncyList;
    private Context mContext;

    public EncyAndSceneListAdapter(Context context, List<ListItemData> encyList){
        this.mContext = context;
        if (null == encyList){
            this.mEncyList = new ArrayList<>();
        }else{
            this.mEncyList = encyList;
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_ency_item, parent, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListItemData ency = mEncyList.get(position);
//        holder.img.setBackground(mContext.getResources().getDrawable(R.drawable.ico_def));
        holder.img.setImageURI( Constants.URL.FILE_BASE_URL + ency.getPicUrl() );
        holder.tvName.setText(LanguageUtil.chooseTest(ency.getCaption(), ency.getEnCaption()) );
        holder.tvRecommend.setVisibility( ency.getRecommend() == 1 ? View.VISIBLE : View.GONE );
        holder.tvRemark.setText( LanguageUtil.chooseTest(ency.getRemark(), ency.getEnRemark()) );
        holder.root.setOnClickListener(v -> {
            ToastHelper.showShort(ency.getCaption());
        });
    }

    @Override
    public int getItemCount() {
        return mEncyList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private View root;
        private SimpleDraweeView img;
        private TextView tvName;
        private TextView tvRecommend;
        private TextView tvRemark;

        public ViewHolder(View v) {
            super(v);
            root = v.findViewById(R.id.ency_item_root);
            img = v.findViewById(R.id.ency_item_img);
            tvName = v.findViewById(R.id.ency_item_name);
            tvRecommend = v.findViewById(R.id.ency_item_recommend);
            tvRemark = v.findViewById(R.id.ency_item_remark);
        }
    }
}
