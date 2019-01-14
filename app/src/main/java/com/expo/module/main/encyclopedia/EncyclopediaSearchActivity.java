package com.expo.module.main.encyclopedia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
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
        mSearchContent.setOnEditorActionListener( this );
        mResultView.addItemDecoration( new SpaceDecoration( 0, getResources().getDimensionPixelSize( R.dimen.dms_4 ) ) );
        setTitle( 0, R.string.search );
//        mResultView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));        // item分割线
//        int marginV = getResources().getDimensionPixelSize(R.dimen.dms_18);
//        mResultView.addItemDecoration(new SpaceDecoration(0, marginV, 0, 0, 0));
        loadHistory();
    }

    private void loadHistory() {
        mSearchRecordUtil = new SearchRecordUtil();
        mHistory = mSearchRecordUtil.loadHistory();
        mHistoryAdapter = new ArrayAdapter<String>( getContext(), R.layout.layout_history_item, mHistory );
        mHistoryList.setDivider(getResources().getDrawable(android.R.drawable.divider_horizontal_bright));
        mHistoryList.setDividerHeight(getResources().getDimensionPixelSize(R.dimen.dms_2));
        mHistoryList.setAdapter( mHistoryAdapter );
        mHistoryList.setOnItemClickListener( (parent, view, position, id) -> search( mHistory[position] ) );
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent( context, EncyclopediaSearchActivity.class );
        context.startActivity( in );
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            mSearchContentStr = mSearchContent.getText().toString().trim();
            if (mSearchContentStr.isEmpty()) {
                ToastHelper.showShort( R.string.please_enter_keywords );
                return true;
            }
            search( mSearchContentStr );
            return true;
        }
        return false;
    }

    private void search(String searchContent) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService( INPUT_METHOD_SERVICE );
        inputMethodManager.hideSoftInputFromWindow( getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS );
        showLoadingView();
        mSearchContent.setText(searchContent);
        mSearchContent.clearFocus();
        mSearchRecordUtil.saveToHistory( searchContent );
        mPresenter.searchEncy( searchContent );
    }

    @Override
    public void getSearchResult(List<ListItemData> listItemDatas) {
        mResultView.setAdapter( new EncyAndSceneListAdapter( getContext(), listItemDatas ) );
        mHistoryView.setVisibility( View.GONE );
        mResultView.setVisibility( View.VISIBLE );
        hideLoadingView();
    }

    @OnClick(R.id.search_history_clear)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_history_clear:
                mSearchRecordUtil.clearHistory();
                mHistory = mSearchRecordUtil.loadHistory();
                mHistoryAdapter = new ArrayAdapter<String>( getContext(), R.layout.layout_history_item, mHistory );
                mHistoryList.setAdapter( mHistoryAdapter );
                break;
            case R.id.title_back:
                finish();
                break;
        }
    }
}
