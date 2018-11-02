package com.expo.contract;

import com.expo.adapters.ListItemData;
import com.expo.base.IPresenter;
import com.expo.base.IView;

import java.util.List;

public interface EncyclopediaSearchContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super( view );
        }

        public abstract void searchEncy(String searchContentStr);

    }

    interface View extends IView {
        void getSearchResult(List<ListItemData> listItemDatas);
    }
}
