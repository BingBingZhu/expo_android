package com.expo.module.heart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.BaseAdapterItemClickListener;
import com.expo.base.ExpoApp;
import com.expo.base.utils.LogUtils;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.MessagesContract;
import com.expo.entity.CommonInfo;
import com.expo.entity.Message;
import com.expo.entity.User;
import com.expo.module.heart.adapter.MessageAdapter;
import com.expo.module.heart.message.MessageTypeAppointment;
import com.expo.module.heart.message.MessageType;
import com.expo.module.heart.message.MessageTypeSystem;
import com.expo.module.heart.message.MessageTypeTourist;
import com.expo.module.login.LoginActivity;
import com.expo.module.webview.WebActivity;
import com.expo.utils.Constants;
import com.expo.utils.LocalBroadcastUtil;
import com.expo.widget.decorations.SpaceDecoration;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.List;

import butterknife.BindView;

/*
 * 各消息类型的消息列表，用同一个页面使用不同的列表项布局
 */
public class MessagesActivity extends BaseActivity<MessagesContract.Presenter>
        implements MessagesContract.View, SwipeMenuCreator, SwipeMenuItemClickListener {

    @BindView(R.id.swipe_menu_recycler_view)
    SwipeMenuRecyclerView mSwipeMenuRecyclerView;
    @BindView(R.id.message_top_line)
    View topLine;

    MessageAdapter mAdapter;
    MessageType mMessageType;
    String type;

    private BaseAdapterItemClickListener<Message> mListener = (view, position, message) -> {
//            mMessageType.itemClick(MessagesActivity.this, message);
        if (message.getType().equals("1")) {

        } else if (message.getType().equals("4")) {
            User user = ExpoApp.getApplication().getUser();
            if (user == null) {
                LoginActivity.startActivity( getContext() );
                return;
            }
            if (message == null) {
                ToastHelper.showShort( R.string.message_get_error );
            }
            String url = mPresenter.loadCommonInfo( CommonInfo.VISITOR_SERVICE_DETAILS );
            WebActivity.startActivity( getContext(),
                    TextUtils.isEmpty( url ) ? url : (url + String.format( "?Uid=%s&Ukey=%s&id=%s", user.getUid(), user.getUkey(), message.getLinkId() )),
                    getString( R.string.title_detail ), BaseActivity.TITLE_COLOR_STYLE_WHITE );
        } else if (message.getType().equals("5")) {

        }
        message.setRead(true);
        mPresenter.setMessageRead(message);
        mAdapter.notifyDataSetChanged();
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_message;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        type = getIntent().getStringExtra(Constants.EXTRAS.EXTRAS);
        initMessageType(type);
        setTitle(0, mMessageType.getTitle());
        initRecyclerView(type);
        mPresenter.getMessage(type);
        LocalBroadcastUtil.registerReceiver(getContext(), receiver, Constants.Action.ACTION_RECEIVE_MESSAGE);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mPresenter.getMessage(type);
        }
    };

    private void initMessageType(String type) {
        if (StringUtils.equals("1", type)) {
            mMessageType = new MessageTypeSystem();
        } else if (StringUtils.equals("4", type)) {
            mMessageType = new MessageTypeTourist();
        } else if (StringUtils.equals("5", type)) {
            mMessageType = new MessageTypeAppointment();
        }
    }

    private void initRecyclerView(String type) {
        mSwipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        int leftRight = (int) getResources().getDimension(R.dimen.dms_30);
        int v;
        if (type.equals("1"))
            mSwipeMenuRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));        // item分割线
        else if (type.equals("4")){
            v = (int) getResources().getDimension(R.dimen.dms_30);
            mSwipeMenuRecyclerView.addItemDecoration(new SpaceDecoration(leftRight, v, leftRight, v, 0));
        }else if (type.equals("5")){
            v = (int) getResources().getDimension(R.dimen.dms_16);
            mSwipeMenuRecyclerView.setBackgroundResource(R.color.colorAccent);
            topLine.setVisibility(View.GONE);
            mSwipeMenuRecyclerView.addItemDecoration(new SpaceDecoration(leftRight, v, leftRight, v, 0));
        }
        mSwipeMenuRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            Drawable drawable = getResources().getDrawable(R.mipmap.shape_new_msg_flag);
            @Override
            public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDrawOver(c, parent, state);
                for (int i = 0; i < parent.getChildCount(); i++){
                    View v = parent.getChildAt(i);
                    if (!messages.get(i).isRead()){
                        drawable.setBounds(v.getRight()-drawable.getIntrinsicWidth(),v.getTop(),
                                v.getRight(),v.getTop()+drawable.getIntrinsicHeight());
                        drawable.draw(c);
                    }
                }
            }
        });        // item分割线

        mSwipeMenuRecyclerView.setSwipeMenuCreator(this);
        mSwipeMenuRecyclerView.setSwipeMenuItemClickListener(this);
        mAdapter = new MessageAdapter(this);
        mAdapter.setLayoutId(mMessageType.getItemRes());
        mAdapter.setListener(mListener);
        mSwipeMenuRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    /**
     * 启动消息列表页
     *
     * @param context
     * @param type    消息类型@see{Message} type
     */
    public static void startActivity(@NonNull Context context, @NonNull String type) {
        Intent in = new Intent(context, MessagesActivity.class);
        in.putExtra(Constants.EXTRAS.EXTRAS, type);
        context.startActivity(in);
    }

    @Override
    public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
        int width = SizeUtils.dp2px(60);
        int height = ViewGroup.LayoutParams.MATCH_PARENT;

        SwipeMenuItem deleteItem = new SwipeMenuItem(this)
                .setBackground(R.drawable.bg_gradient_y_fc2637_fc515e)
                .setImage(R.mipmap.delete)
                .setWidth(width)
                .setHeight(height);
        swipeRightMenu.addMenuItem(deleteItem); // 在Item右侧添加一个菜单。
    }

    /**
     * 删除菜单
     *
     * @param menuBridge
     */
    @Override
    public void onItemClick(SwipeMenuBridge menuBridge) {
        menuBridge.closeMenu();
        int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
        mPresenter.delMessage(mAdapter.mData.get(adapterPosition).getId(), adapterPosition);
    }

    private List<Message> messages;

    @Override
    public void freshMessageList(List<Message> list) {
        messages = list;
        mAdapter.setData(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void delMessage(int position) {
        mAdapter.mData.remove(position);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastUtil.unregisterReceiver(getContext(), receiver);
    }
}
