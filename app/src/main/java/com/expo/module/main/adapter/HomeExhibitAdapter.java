package com.expo.module.main.adapter;//package com.expo.widget.gallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ScreenUtils;
import com.expo.R;
import com.expo.base.BaseAdapterItemClickListener;
import com.expo.entity.Encyclopedias;
import com.squareup.picasso.Picasso;

import org.raphets.roundimageview.RoundImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhubo on 2018/1/23.
 */

public class HomeExhibitAdapter extends RecyclerView.Adapter<HomeExhibitAdapter.MyViewHolder> {

    private List<Encyclopedias> mList;
    private Context mContext;
    BaseAdapterItemClickListener mListener;

    public HomeExhibitAdapter(Context context) {
        super();
        this.mContext = context;
    }

    public void setData(List<Encyclopedias> list) {
        this.mList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_home_exhibit, null));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (getItemCount() == 0) return;
        Encyclopedias encyclopedias = mList.get(position % mList.size());
        Picasso.with(mContext).load(encyclopedias.picUrl)
                .centerCrop()
                .resize((int) (ScreenUtils.getScreenWidth() - mContext.getResources().getDimension(R.dimen.dms_200)), (int) mContext.getResources().getDimension(R.dimen.dms_360))
                .into(holder.img);

        holder.itemView.setOnClickListener(v -> {
            if (mListener != null)
                mListener.itemClick(v, position, mList.get(position % getItemCount()));
        });
    }

    public void setListener(BaseAdapterItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public int getItemCount() {
        if (mList == null || mList.size() == 0) return 0;
        if (mList.size() == 1) return 1;
        return Integer.MAX_VALUE;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_home_exhibit_img)
        RoundImageView img;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}