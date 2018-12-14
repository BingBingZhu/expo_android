package com.expo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.expo.R;
import com.expo.base.ExpoApp;
import com.expo.base.utils.ToastHelper;
import com.expo.entity.VisitorService;
import com.expo.module.webview.WebActivity;
import com.expo.utils.Constants;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class ServiceHistoryAdapter extends RecyclerView.Adapter<ServiceHistoryAdapter.ViewHolder> {

    private List<VisitorService> mData;
    private Context mContext;

    public ServiceHistoryAdapter(Context context, List<VisitorService> data) {
        this.mContext = context;
        if (null == data) {
            this.mData = new ArrayList<>();
        } else {
            this.mData = data;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_service_history_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VisitorService vs = mData.get(position);
        holder.tvCreateTime.setText(vs.getCreateTime());
        holder.img.setImageResource(getResourceId(vs.getServiceType()));
//        setImageUri(holder.simpleDraweeView1, vs.getImgUrl1());
//        setImageUri(holder.simpleDraweeView2, vs.getImgUrl2());
//        setImageUri(holder.simpleDraweeView3, vs.getImgUrl3());
        holder.tvType.setText(getTypeName(vs.getServiceType()));
        holder.tvContent.setText(vs.getSituation());
        holder.tvState.setText(getDisposeTypeName(vs.getDisposeType()));
        holder.tvMore.setOnClickListener(v -> {
            String url = "http://192.168.1.138:8080/";
            WebActivity.startActivity(mContext, TextUtils.isEmpty( url ) ? Constants.URL.HTML_404 :
                    url + "?Uid=" + ExpoApp.getApplication().getUser().getUid() + "&Ukey=" +
                            ExpoApp.getApplication().getUser().getUkey()+"&id="+vs.getId(), getTypeName(vs.getServiceType()));
        });
    }

    private void setImageUri(SimpleDraweeView view, String url) {
        if (null == url || url.isEmpty()) {
            view.setVisibility(View.INVISIBLE);
            return;
        }
        view.setImageURI(Constants.URL.FILE_BASE_URL + url);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
//        SimpleDraweeView simpleDraweeView1;
//        SimpleDraweeView simpleDraweeView2;
//        SimpleDraweeView simpleDraweeView3;
        TextView tvType;
        TextView tvContent;
        TextView tvState;
        TextView tvMore;
        TextView tvCreateTime;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCreateTime = itemView.findViewById(R.id.service_history_item_time_tv);
            img = itemView.findViewById(R.id.service_history_item_img);
//            simpleDraweeView1 = itemView.findViewById(R.id.service_history_item_simple_drawee_view1);
//            simpleDraweeView2 = itemView.findViewById(R.id.service_history_item_simple_drawee_view2);
//            simpleDraweeView3 = itemView.findViewById(R.id.service_history_item_simple_drawee_view3);
            tvType = itemView.findViewById(R.id.service_history_item_type_tv);
            tvContent = itemView.findViewById(R.id.service_history_item_content_tv);
            tvState = itemView.findViewById(R.id.service_history_item_state_tv);
            tvMore = itemView.findViewById(R.id.service_history_item_more_tv);
        }
    }



    private int getResourceId(String serviceType) {
        if (serviceType.equals("1")) {
            return R.mipmap.tourist_service_0;
        } else if (serviceType.equals("2")) {
            return R.mipmap.tourist_service_5;
        } else if (serviceType.equals("3")) {
            return R.mipmap.tourist_service_1;
        } else if (serviceType.equals("4")) {
            return R.mipmap.tourist_service_4;
        } else if (serviceType.equals("5")) {
            return R.mipmap.tourist_service_2;
        } else if (serviceType.equals("6")) {
            return R.mipmap.tourist_service_6;      // 投诉受理   暂无图标
        }
        return R.mipmap.tourist_service_4;
    }

    private String getTypeName(String serviceType) {
        if (serviceType.equals("1")) {
            return mContext.getString(R.string.item_tourist_service_text_0);
        } else if (serviceType.equals("2")) {
            return mContext.getString(R.string.item_tourist_service_text_5);
        } else if (serviceType.equals("3")) {
            return mContext.getString(R.string.item_tourist_service_text_1);
        } else if (serviceType.equals("4")) {
            return mContext.getString(R.string.item_tourist_service_text_4);
        } else if (serviceType.equals("5")) {
            return mContext.getString(R.string.item_tourist_service_text_2);
        } else if (serviceType.equals("6")) {
            return mContext.getString(R.string.item_tourist_service_text_9);
        }
        return mContext.getString(R.string.unknown_type);
    }


    private String getDisposeTypeName(String disposeType) {
        if(disposeType.equals("1"))
            return mContext.getString(R.string.waiting_process);
        if(disposeType.equals("2"))
            return mContext.getString(R.string.being_processed);
        if(disposeType.equals("3"))
            return mContext.getString(R.string.has_been_sent);
        if(disposeType.equals("4"))
            return mContext.getString(R.string.tackle_end);
        if(disposeType.equals("5"))
            return mContext.getString(R.string.closed);
        return mContext.getString(R.string.unknown_type);
    }
}
