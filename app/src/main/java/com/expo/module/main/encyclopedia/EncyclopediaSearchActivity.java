package com.expo.module.main.encyclopedia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.expo.R;
import com.expo.adapters.EncyAndSceneListAdapter;
import com.expo.adapters.ListItemData;
import com.expo.base.BaseActivity;
import com.expo.base.utils.SearchRecordUtil;
import com.expo.base.utils.StatusBarUtils;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.EncyclopediaSearchContract;
import com.expo.widget.SimpleRecyclerView;
import com.expo.widget.decorations.SpaceDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 百科搜索页
 */
public class EncyclopediaSearchActivity extends BaseActivity<EncyclopediaSearchContract.Presenter>
        implements EncyclopediaSearchContract.View, TextView.OnEditorActionListener {

    @BindView(R.id.search_history)
    View mHistoryView;
    @BindView(R.id.search_history_list)
    ListView mHistoryList;
    @BindView(R.id.search_result)
    SimpleRecyclerView mResultView;
    @BindView(R.id.serach_content)
    EditText mSearchContent;
    @BindView(R.id.search_history_empty)
    TextView mHistoryEmptyTv;
    @BindView(R.id.search_result_empty)
    TextView mSearchEmptyTv;
    @BindView(R.id.root_view)
    View mRootView;

    private String mSearchContentStr;
    private String[] mHistory;
    private SearchRecordUtil mSearchRecordUtil;
    private ArrayAdapter mHistoryAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_encyclopedia_search;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mSearchContent.setOnEditorActionListener(this);
        mResultView.addItemDecoration(new SpaceDecoration(0, getResources().getDimensionPixelSize(R.dimen.dms_4)));
        mRootView.setPadding(mRootView.getPaddingLeft(),
                mRootView.getPaddingTop() + StatusBarUtils.getStatusBarHeight(getContext()),
                mRootView.getPaddingRight(), mRootView.getPaddingBottom());
//        setTitle( 1, R.string.search );
//        mResultView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));        // item分割线
//        int marginV = getResources().getDimensionPixelSize(R.dimen.dms_18);
//        mResultView.addItemDecoration(new SpaceDecoration(0, marginV, 0, 0, 0));
        loadHistory();
    }

    private void loadHistory() {
        mSearchRecordUtil = new SearchRecordUtil();
        mHistory = mSearchRecordUtil.loadHistory();
        if (null == mHistory || mHistory.length == 0) {
            mHistoryEmptyTv.setVisibility(View.VISIBLE);
        }
        mHistoryAdapter = new ArrayAdapter<String>(getContext(), R.layout.layout_history_item, mHistory);
        mHistoryList.setDivider(getResources().getDrawable(R.drawable.bg_f5_r10));
        mHistoryList.setDividerHeight(getResources().getDimensionPixelSize(R.dimen.dms_2));
        mHistoryList.setAdapter(mHistoryAdapter);
        mHistoryList.setOnItemClickListener((parent, view, position, id) -> {
            mSearchContentStr = mHistory[position];
            search(mHistory[position]);
        });
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent(context, EncyclopediaSearchActivity.class);
        context.startActivity(in);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            mSearchContentStr = mSearchContent.getText().toString().trim();
            if (mSearchContentStr.isEmpty()) {
                ToastHelper.showShort(R.string.please_enter_keywords);
                return true;
            }
            search(mSearchContentStr);
            return true;
        }
        return false;
    }

    private void search(String searchContent) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        showLoadingView();
        mSearchContent.setText(searchContent);
        mSearchContent.clearFocus();
        mSearchRecordUtil.saveToHistory(searchContent);
        mPresenter.searchEncy(searchContent);
    }

    @Override
    public void getSearchResult(List<ListItemData> listItemDatas) {
        hideLoadingView();
        mHistoryView.setVisibility(View.GONE);
        mResultView.setAdapter(new EncyAndSceneListAdapter(getContext(), listItemDatas));
        if (null == listItemDatas || listItemDatas.size() == 0) {
            mSearchEmptyTv.setText("未找到含有“" + mSearchContentStr + "”");
            mSearchEmptyTv.setVisibility(View.VISIBLE);
            return;
        }
        mSearchEmptyTv.setVisibility(View.GONE);
        mResultView.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.search_history_clear, R.id.search_cancle})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_history_clear:
                mSearchRecordUtil.clearHistory();
                mHistory = mSearchRecordUtil.loadHistory();
                mHistoryAdapter = new ArrayAdapter<String>(getContext(), R.layout.layout_history_item, mHistory);
                mHistoryList.setAdapter(mHistoryAdapter);
                break;
            case R.id.search_cancle:
                onBackPressed();
                break;
        }
    }
}
