package com.expo.module.heart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.BaseAdapterItemClickListener;
import com.expo.base.utils.LogUtils;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.MessagesContract;
import com.expo.entity.Message;
import com.expo.module.heart.adapter.MessageAdapter;
import com.expo.module.heart.message.MessageTypeAppointment;
import com.expo.module.heart.message.MessageType;
import com.expo.module.heart.message.MessageTypeSystem;
import com.expo.module.heart.message.MessageTypeTourist;
import com.expo.utils.Constants;
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
        implements MessagesContract.View, SwipeItemClickListener, SwipeMenuCreator, SwipeMenuItemClickListener {

    @BindView(R.id.swipe_menu_recycler_view)
    SwipeMenuRecyclerView mSwipeMenuRecyclerView;

    MessageAdapter mAdapter;
    MessageType mMessageType;

    BaseAdapterItemClickListener mListener = new BaseAdapterItemClickListener() {
        @Override
        public void itemClick(View view, int position, Object o) {
            mMessageType.itemClick(MessagesActivity.this, (Message) o);
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_message;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        String type = getIntent().getStringExtra(Constants.EXTRAS.EXTRAS);
        initMessageType(type);

        setTitle(0, mMessageType.getTitle());
        initRecyclerView(type);

        mPresenter.getMessage(type);
    }

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
        if (StringUtils.equals("5", type)) {
            mSwipeMenuRecyclerView.setBackgroundResource(R.color.colorAccent);
        }
        mSwipeMenuRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));        // item分割线
        mSwipeMenuRecyclerView.setSwipeItemClickListener(this);
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
    public void onItemClick(View itemView, int position) {
        mAdapter.mData.get(position).readMessage();
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

    @Override
    public void onItemClick(SwipeMenuBridge menuBridge) {
        menuBridge.closeMenu();
        int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
        mPresenter.delMessage(mAdapter.mData.get(adapterPosition).getId(), adapterPosition);
    }

    @Override
    public void freshMessageList(List<Message> list) {
        mAdapter.setData(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void delMessage(int position) {
        mAdapter.mData.remove(position);
        mAdapter.notifyDataSetChanged();
    }
}
