package com.expo.module.share;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.expo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class SharePanelAdapter extends RecyclerView.Adapter<SharePanelAdapter.SharePlatformHolder> {

    private List<PlatformItem> videoTypes = new ArrayList<>();
    private List<PlatformItem> imageTypes = new ArrayList<>();
    private LayoutInflater lif;
    private View.OnClickListener onClickListener;
    private String mType;

    {
        videoTypes.add( new PlatformItem( "微信", R.mipmap.share_icon_wechat, Wechat.NAME ) );
        videoTypes.add( new PlatformItem( "QZone", R.mipmap.share_icon_qzone, QZone.NAME ) );
        videoTypes.add( new PlatformItem( "新浪微博", R.mipmap.share_icon_sina, SinaWeibo.NAME ) );
//        videoTypes.add( new PlatformItem( "删除", R.drawable.share_icon_delete, "delete" ) );

        imageTypes.add( new PlatformItem( "微信", R.mipmap.share_icon_wechat, Wechat.NAME ) );
        imageTypes.add( new PlatformItem( "朋友圈", R.mipmap.share_icon_wechat_moments, WechatMoments.NAME ) );
        imageTypes.add( new PlatformItem( "QQ", R.mipmap.share_icon_qq, QQ.NAME ) );
        imageTypes.add( new PlatformItem( "QZone", R.mipmap.share_icon_qzone, QZone.NAME ) );
        imageTypes.add( new PlatformItem( "新浪微博", R.mipmap.share_icon_sina, SinaWeibo.NAME ) );
//        imageTypes.add( new PlatformItem( "删除", R.drawable.share_icon_delete, "delete" ) );
    }

    public SharePanelAdapter(Context context, String type) {
        this.mType = type;
        lif = LayoutInflater.from( context );
    }

    @Override
    public SharePlatformHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SharePlatformHolder( lif.inflate( R.layout.layout_item_share, null ) );
    }

    @Override
    public void onBindViewHolder(SharePlatformHolder holder, int position) {
        PlatformItem platformItem = getItem( position );
        if (platformItem != null) {
            holder.name.setText( platformItem.name );
            holder.icon.setImageResource( platformItem.srcId );
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public PlatformItem getItem(int position) {
        if (mType.startsWith( "image/" )) {
            return imageTypes.get( position );
        } else if (mType.startsWith( "video/" )) {
            return videoTypes.get( position );
        }
        return null;
    }

    @Override
    public int getItemCount() {
        if (TextUtils.isEmpty( mType )) {
            return 0;
        } else if (mType.startsWith( "image/" )) {
            return imageTypes.size();
        } else if (mType.startsWith( "video/" )) {
            return videoTypes.size();
        } else {
            return 0;
        }
    }

    public void setType(String type) {
        this.mType = type;
    }

    public class SharePlatformHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.share_item_icon)
        ImageView icon;
        @BindView(R.id.share_item_name)
        TextView name;

        public SharePlatformHolder(View itemView) {
            super( itemView );
            ButterKnife.bind( this, itemView );
            this.itemView.setTag( this );
            this.itemView.setOnClickListener( onClickListener );
        }
    }
}