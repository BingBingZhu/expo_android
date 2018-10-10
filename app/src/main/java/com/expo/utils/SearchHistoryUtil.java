package com.expo.utils;

import android.text.TextUtils;

import com.expo.base.utils.PrefsHelper;

/**
 * 搜索记录
 */
public class SearchHistoryUtil {

    private String key;

    /**
     * @param searchType 搜索的类型，攻略/百科
     */
    public SearchHistoryUtil(String searchType) {
        this.key = searchType;
    }

    /**
     * 保存到历史记录
     *
     * @param content
     * @return
     */
    public int saveToHistory(String content) {
        String[] histories = loadHistory();
        StringBuilder sb = new StringBuilder();
        boolean isAdd = false;
        int index = -1;
        //查找是否已经存在
        if (histories != null && histories.length > 0) {
            for (int i = 0; i < histories.length; i++) {
                if (histories[i].equals( content )) {
                    index = i;
                    break;
                }
            }
        }
        if (index != -1) {
            if (index != 0) {
                //已存在，移动到前面
                for (int i = index; i >= 0; i--) {
                    if (i > 0) {
                        histories[i] = histories[i - 1];
                    } else {
                        histories[i] = content;
                    }
                }
            }
        } else {
            isAdd = true;
            sb.append( content );
        }
        if (histories != null && histories.length > 0)
            for (int i = 0; i < histories.length && i < 30; i++) {
                if (isAdd || i != 0)
                    sb.append( "," );
                sb.append( histories[i] );
            }
        PrefsHelper.setString( key, sb.toString() );
        sb.setLength( 0 );
        return index;
    }

    /**
     * 获取历史记录
     *
     * @return
     */
    public String[] loadHistory() {
        String history = PrefsHelper.getString( key, "" );
        if (!TextUtils.isEmpty( history )) {
            return history.split( "," );
        }
        return null;
    }

    /**
     * 清除历史记录
     */
    public void clearHistory() {
        PrefsHelper.setString( key, "" );
    }

}
