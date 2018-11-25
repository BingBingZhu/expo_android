package com.expo.contract.presenter;

import com.expo.contract.WebTemplateContract;
import com.expo.db.QueryParams;
import com.expo.entity.ActualScene;
import com.expo.entity.Encyclopedias;
import com.expo.network.Http;

import java.util.ArrayList;
import java.util.List;

public class WebTemplatePresenterImpl extends WebTemplateContract.Presenter {

    public WebTemplatePresenterImpl(WebTemplateContract.View view) {
        super( view );
    }

    @Override
    public Encyclopedias loadEncyclopediaById(long id) {
        return mDao.queryById( Encyclopedias.class, id );
    }

    @Override
    public ActualScene loadSceneByWikiId(long id) {
        return mDao.unique( ActualScene.class, new QueryParams()
                .add( "eq", "wiki_id", String.valueOf( id ) ) );
    }

    @Override
    public String toJson(Object obj) {
        return Http.getGsonInstance().toJson( obj );
    }

    @Override
    public List<Encyclopedias> loadNeayByVenues(ActualScene as) {
        List<ActualScene> actualScenes = mDao.query( ActualScene.class, null );
        List<Long> ids = new ArrayList<>();
        for (ActualScene actualScene : actualScenes) {

        }
        return null;
    }
}
