//package com.expo.adapters;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.expo.R;
//import com.expo.entity.VrInfo;
//import com.zhy.adapter.recyclerview.CommonAdapter;
//import com.zhy.adapter.recyclerview.base.ViewHolder;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
//    private RecyclerView mRecyclerView;
//
//    private List<VrInfo> data = new ArrayList<>();
//    private Context mContext;
//
//    private View VIEW_FOOTER;
//    private View VIEW_HEADER;
//
//    //Type
//    private int TYPE_NORMAL = 1000;
//    private int TYPE_HEADER = 1001;
//    private int TYPE_FOOTER = 1002;
//
//    public MyAdapter(Context mContext, List<VrInfo> data){
//        this.data = data;
//        this.mContext = mContext;
//    }
//
//    @Override
//    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType == TYPE_FOOTER) {
//            return new MyHolder(VIEW_FOOTER);
//        } else if (viewType == TYPE_HEADER) {
//            return new MyHolder(VIEW_HEADER);
//        } else {
//            return new MyHolder(getLayout(R.layout.item_list_layout));
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(MyHolder holder, int position) {
//        if (!isHeaderView(position) && !isFooterView(position)) {
//            if (haveHeaderView()) position--;
//            TextView content = holder.itemView.findViewById(R.id.item_content);
//            TextView time = holder.itemView.findViewById(R.id.item_time);
//            content.setText(data.get(position));
//            time.setText("2016-1-1");
//        }
//    }
//
//
//    @Override
//    public int getItemCount() {
//        int count = (data == null ? 0 : data.size());
//        if (VIEW_FOOTER != null) {
//            count++;
//        }
//
//        if (VIEW_HEADER != null) {
//            count++;
//        }
//        return count;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if (isHeaderView(position)) {
//            return TYPE_HEADER;
//        } else if (isFooterView(position)) {
//            return TYPE_FOOTER;
//        } else {
//            return TYPE_NORMAL;
//        }
//    }
//
//    @Override
//    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//        try {
//            if (mRecyclerView == null && mRecyclerView != recyclerView) {
//                mRecyclerView = recyclerView;
//            }
//            ifGridLayoutManager();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private View getLayout(int layoutId) {
//        return LayoutInflater.from(mContext).inflate(layoutId, null);
//    }
//
//    public void addHeaderView(View headerView) {
//        if (haveHeaderView()) {
//            throw new IllegalStateException("hearview has already exists!");
//        } else {
////避免出现宽度自适应
//            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            headerView.setLayoutParams(params);
//            VIEW_HEADER = headerView;
//            ifGridLayoutManager();
//            notifyItemInserted(0);
//        }
//
//    }
//
//    public void addFooterView(View footerView) {
//        if (haveFooterView()) {
//            throw new IllegalStateException("footerView has already exists!");
//        } else {
//            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            footerView.setLayoutParams(params);
//            VIEW_FOOTER = footerView;
//            ifGridLayoutManager();
//            notifyItemInserted(getItemCount() - 1);
//        }
//    }
//
//    private void ifGridLayoutManager() {
//        if (mRecyclerView == null) return;
//        final RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
//        if (layoutManager instanceof GridLayoutManager) {
//            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                @Override
//                public int getSpanSize(int position) {
//                    return (isHeaderView(position) || isFooterView(position)) ?
//                            ((GridLayoutManager) layoutManager).getSpanCount() :
//                            1;
//                }
//            });
//        }
//    }
//
//    private boolean haveHeaderView() {
//        return VIEW_HEADER != null;
//    }
//
//    public boolean haveFooterView() {
//        return VIEW_FOOTER != null;
//    }
//
//    private boolean isHeaderView(int position) {
//        return haveHeaderView() && position == 0;
//    }
//
//    private boolean isFooterView(int position) {
//        return haveFooterView() && position == getItemCount() - 1;
//    }
//    public static class MyHolder extends RecyclerView.ViewHolder {
//        public MyHolder(View itemView) {
//            super(itemView);
//        }
//    }
//}
