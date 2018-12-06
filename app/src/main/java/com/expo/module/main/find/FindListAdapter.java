package com.expo.module.main.find;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.expo.R;
import com.expo.adapters.ListItemData;
import com.expo.utils.CommUtils;
import com.expo.utils.LanguageUtil;
import com.squareup.picasso.Picasso;

import org.raphets.roundimageview.RoundImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FindListAdapter extends RecyclerView.Adapter<FindListAdapter.ViewHolder> {

    private List<ListItemData> mEncyList;
    private Context mContext;

    public FindListAdapter(Context context, List<ListItemData> encyList) {
        this.mContext = context;
        if (null == encyList) {
            this.mEncyList = new ArrayList<>();
        } else {
            this.mEncyList = encyList;
        }
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
        ListItemData ency = mEncyList.get(position);
        Picasso.with(mContext).load(CommUtils.getFullUrl(ency.getPicUrl())).into(holder.img);
        if((position + 1) % 4 > 1){
            holder.img.getLayoutParams().height = (int) mContext.getResources().getDimension(R.dimen.dms_360);
        } else {
            holder.img.getLayoutParams().height = (int) mContext.getResources().getDimension(R.dimen.dms_240);
        }
        holder.content.setText(LanguageUtil.chooseTest(ency.getRemark(), ency.getEnRemark()));
        Picasso.with(mContext).load(CommUtils.getFullUrl(ency.getPicUrl())).into(holder.head);
        holder.name.setText(LanguageUtil.chooseTest(ency.getCaption(), ency.getEnCaption()));
        holder.count.setText(position + "99999");
    }

    @Override
    public int getItemCount() {
        return mEncyList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.find_list_img)
        RoundImageView img;
        @BindView(R.id.find_list_content)
        TextView content;
        @BindView(R.id.find_list_name)
        TextView name;
        @BindView(R.id.find_list_head)
        RoundImageView head;
        @BindView(R.id.find_list_count)
        TextView count;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
