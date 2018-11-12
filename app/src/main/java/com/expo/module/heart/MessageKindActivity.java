package com.expo.module.heart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.contract.MessageKindContract;
import com.expo.entity.Message;
import com.expo.module.heart.adapter.MessageKindAdapter;
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
        setTitle(1, R.string.title_message_kind_ac);
        initSwipeMenuRecyclerView();
        mPresenter.getMessage();
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
        mSwipeMenuRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));        // item分割线
        mAdapter = new MessageKindAdapter(this);
        mSwipeMenuRecyclerView.setAdapter(mAdapter);
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
        mPresenter.delMessage(mAdapter.mData.get(adapterPosition), adapterPosition);
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

    @Override
    public void onItemClick(View itemView, int position) {
        Message message = mAdapter.mData.get(position);
        if (!StringUtils.equals("3", message.getType())) {
            MessagesActivity.startActivity(this, mAdapter.mData.get(position).getType());
        } else {
        }
    }
}
