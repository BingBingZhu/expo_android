package com.expo.contract.presenter;

import com.expo.adapters.ActualSceneAdapter;
import com.expo.adapters.EncyclopediasAdapter;
import com.expo.adapters.ListItemData;
import com.expo.contract.EncyclopediaSearchContract;
import com.expo.db.QueryParams;
import com.expo.entity.ActualScene;
import com.expo.entity.Encyclopedias;

import java.util.ArrayList;
import java.util.List;

public class EncyclopediaSearchPresenterImpl extends EncyclopediaSearchContract.Presenter {
    public EncyclopediaSearchPresenterImpl(EncyclopediaSearchContract.View view) {
        super( view );
    }

    @Override
    public void searchEncy(String searchContentStr) {
        List<Encyclopedias> encyclopedias = mDao.query(Encyclopedias.class, new QueryParams()
                .add("like", "caption", "%"+searchContentStr+"%"));
        List<ActualScene> actualScenes = mDao.query(ActualScene.class, new QueryParams()
                .add("like", "caption", "%"+searchContentStr+"%"));
        List<ListItemData> listItemDatas = new ArrayList<>();
        listItemDatas.addAll(EncyclopediasAdapter.convertToTabList(encyclopedias));
        listItemDatas.addAll(ActualSceneAdapter.convertToTabList(actualScenes));
        mView.getSearchResult(listItemDatas);
    }
}
