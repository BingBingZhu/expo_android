package com.expo.module.heart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.SizeUtils;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.MessageKindContract;
import com.expo.entity.Message;
import com.expo.entity.MessageKindBean;
import com.expo.module.heart.adapter.MessageKindAdapter;
import com.expo.utils.Constants;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/*
 * 消息分类列表页
 */
public class MessageKindActivity extends BaseActivity<MessageKindContract.Presenter>
        implements MessageKindContract.View, SwipeItemClickListener, SwipeMenuCreator, SwipeMenuItemClickListener {

    @BindView(R.id.swipe_menu_recycler_view)
    SwipeMenuRecyclerView mSwipeMenuRecyclerView;

    MessageKindAdapter mAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_message;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(0, R.string.title_message_kind_ac);
        initSwipeMenuRecyclerView();
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent(context, MessageKindActivity.class);
        context.startActivity(in);
    }

    private void initSwipeMenuRecyclerView() {

        mSwipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSwipeMenuRecyclerView.setSwipeItemClickListener(this);
        mSwipeMenuRecyclerView.setSwipeMenuCreator(this);
        mSwipeMenuRecyclerView.setSwipeMenuItemClickListener(this);

        /////////////////////////////////////
        List<MessageKindBean> list = new ArrayList<>();
        MessageKindBean bean1 = new MessageKindBean();
        bean1.resId = R.drawable.msg_laba;
        bean1.text1 = "游客服务反馈";
        bean1.text2 = "恭喜您！您有巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉";
        MessageKindBean bean2 = new MessageKindBean();
        bean2.resId = R.drawable.msg_xitongtuisong;
        bean2.text1 = "预约提醒";
        bean2.text2 = "恭喜您！您有巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉";
        MessageKindBean bean3 = new MessageKindBean();
        bean3.resId = R.drawable.msg_biaoqian;
        bean3.text1 = "系统提醒";
        bean3.text2 = "恭喜您！您有巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉";
        MessageKindBean bean4 = new MessageKindBean();
        bean4.resId = R.drawable.msg_activity;
        bean4.text1 = "活动消息";
        bean4.text2 = "恭喜您！您有巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉";
        MessageKindBean bean5 = new MessageKindBean();
        bean5.resId = R.drawable.msg_laba;
        bean5.text1 = "活动消息";
        bean5.text2 = "恭喜您！您有巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉";
        list.add(bean1);
        list.add(bean2);
        list.add(bean3);
        list.add(bean4);
        list.add(bean5);
        mAdapter = new MessageKindAdapter(this);
        mAdapter.setData(list);
        mSwipeMenuRecyclerView.setAdapter(mAdapter);
    }

    //列表点击
    @Override
    public void onItemClick(View itemView, int position) {
        int layoutId = 0;
        int title = 0;
        switch (position % 3) {
            case 0:
                title = R.string.title_message_system_ac;
                layoutId = R.layout.item_message_system;
                break;
            case 1:
                title = R.string.title_message_tourist_ac;
                layoutId = R.layout.item_message_tourist;
                break;
            case 2:
                title = R.string.title_message_appointment_ac;
                layoutId = R.layout.item_message_appointment;
                break;
        }
        Intent intent = new Intent(this, MessagesActivity.class);
        intent.putExtra(Constants.EXTRAS.EXTRAS, layoutId);
        intent.putExtra(Constants.EXTRAS.EXTRA_TITLE, title);
        startActivity(intent);
    }

    //创建侧边栏
    @Override
    public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {

        int width = SizeUtils.dp2px(60);
        int height = ViewGroup.LayoutParams.MATCH_PARENT;

        SwipeMenuItem deleteItem = new SwipeMenuItem(this)
                .setBackground(R.drawable.bg_gradient_y_fc2637_fc515e)
                .setImage(R.drawable.delete)
                .setWidth(width)
                .setHeight(height);
        swipeRightMenu.addMenuItem(deleteItem); // 在Item右侧添加一个菜单。
    }

    //侧边栏点击
    @Override
    public void onItemClick(SwipeMenuBridge menuBridge) {
        menuBridge.closeMenu();
        int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
        mAdapter.mData.remove(adapterPosition);
        mAdapter.notifyDataSetChanged();
    }
}
