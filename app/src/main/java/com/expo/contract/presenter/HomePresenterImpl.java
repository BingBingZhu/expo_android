package com.expo.contract.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.blankj.utilcode.util.AppUtils;
import com.expo.R;
import com.expo.contract.HomeContract;
import com.expo.db.QueryParams;
import com.expo.entity.AppInfo;
import com.expo.entity.Circum;
import com.expo.entity.CommonInfo;
import com.expo.entity.Encyclopedias;
import com.expo.entity.ExpoActivityInfo;
import com.expo.entity.Message;
import com.expo.entity.RouteInfo;
import com.expo.entity.Schedule;
import com.expo.entity.TopLineInfo;
import com.expo.entity.Venue;
import com.expo.entity.VenuesType;
import com.expo.entity.VrInfo;
import com.expo.module.heart.HeartBeatService;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.BaseResponse;
import com.expo.network.response.CircumResp;
import com.expo.network.response.VersionInfoResp;
import com.expo.upapp.UpdateAppManager;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

public class HomePresenterImpl extends HomeContract.Presenter {
    public HomePresenterImpl(HomeContract.View view) {
        super(view);
    }

    @Override
    public void setMessageCount() {
        new Message().sendMessageCount(null);
    }

    @Override
    public void setTopLine() {
        mView.showTopLine(mDao.query(TopLineInfo.class, null));
    }

    @Override
    public void setVenue() {
        QueryParams params = new QueryParams()
                .add("eq", "enable", 1)
                .add("and")
                .add("eq", "recommend", 1)
                .add("and")
                .add("eq", "type_name", "场馆")
                .add("orderBy", "idx", true);
        mView.showVenue(mDao.query(Encyclopedias.class, params));
    }

    @Override
    public void setHotActivity() {
        QueryParams params = new QueryParams()
                .add("limit", 0, 4)
                .add("orderBy", "recommended_idx", "true");
        mView.showActivity(mDao.query(ExpoActivityInfo.class, params));
    }

    @Override
    public void setRecommendRoute() {
        QueryParams params = new QueryParams()
                .add("eq", "enable", "1")
                .add("and")
                .add("eq", "type_id", "1")
                .add("limit", 0, 5);
        mView.showRoute(mDao.query(RouteInfo.class, params));
    }

    @Override
    public void setRankingScenic() {
        QueryParams params = new QueryParams()
                .add("eq", "recommend", "1")
                .add("and")
                .add("eq", "type_name", "景点")
                .add("and")
                .add("eq", "enable", 1)
                .add("orderBy", "recommended_idx", true)
                .add("limit", 0, 2);
        mView.showRankingScenic(mDao.query(Encyclopedias.class, params));
    }

    @Override
    public void setVr() {
        mView.showVr(mDao.query(VrInfo.class, new QueryParams()
                .add("eq", "is_recommended", 1)
                .add("orderBy", "recommended_idx", true)
                .add("limit", 0, 2)));
    }

    @Override
    public void setFood() {
        QueryParams params = new QueryParams()
                .add("eq", "type_name", "美食")
                .add("and")
                .add("eq", "enable", 1)
                .add("orderBy", "recommended_idx", true)
                .add("limit", 0, 5);
        mView.showFood(mDao.query(Encyclopedias.class, params));
    }

    @Override
    public void setHotel() {
        QueryParams params = new QueryParams()
                .add("eq", "type_name", "酒店")
                .add("and")
                .add("eq", "enable", 1)
                .add("orderBy", "recommended_idx", true)
                .add("limit", 0, 3);
        mView.showHotel(mDao.query(Encyclopedias.class, params));
    }

//    @Override
//    public void setExhibit() {
//        //从数据库获取
//        QueryParams params = new QueryParams()
//                .add( "eq", "enable", 1 )
//                .add( "and" )
//                .add( "eq", "recommend", 1 )
//                .add( "and" )
//                .add( "eq", "type_name", "展览" )
//                .add( "orderBy", "idx", true );
//        mView.showExhibit( mDao.query( Encyclopedias.class, params ) );
//    }

//    @Override
//    public void setExhibitGarden() {
//        //从数据库获取
//        QueryParams params = new QueryParams()
//                .add( "eq", "enable", 1 )
//                .add( "and" )
//                .add( "eq", "recommend", 1 )
//                .add( "and" )
//                .add( "eq", "type_name", "展园" )
//                .add( "orderBy", "idx", true );
//        mView.showExhibitGarden( mDao.query( Encyclopedias.class, params ) );
//    }

    public void startHeartService(Context context) {
        HeartBeatService.startService(context);
    }

    @Override
    public void stopHeartService(Context context) {
        HeartBeatService.stopService(context);
    }

    @Override
    public String loadCommonInfo(String type) {
        CommonInfo info = mDao.unique(CommonInfo.class, new QueryParams().add("eq", "type", type));
        if (info != null) {
            return info.getLinkUrl();
        }
        return null;
    }

    @Override
    public void appRun(String jgId) {
        Map<String, Object> params = Http.getBaseParams();
        params.put("jgid", jgId);
        Observable<BaseResponse> observable = Http.getServer().userlogAppRun(Http.buildRequestBody(params));
        Http.request(new ResponseCallback<BaseResponse>() {
            @Override
            protected void onResponse(BaseResponse rsp) {
            }

        }, observable);
    }

    @Override
    public void checkUpdate() {
        Map<String, Object> params = Http.getBaseParams();
        RequestBody requestBody = Http.buildRequestBody(params);
        Observable<VersionInfoResp> observable = Http.getServer().getAppUpdateInfo(requestBody);
        Http.request(new ResponseCallback<VersionInfoResp>() {
            @Override
            protected void onResponse(VersionInfoResp rsp) {
                AppInfo appInfo = UpdateAppManager.getInstance().isHaveUpdate(AppUtils.getAppVersionName(), rsp.Objlst);
                if (null != appInfo) {
                    mView.appUpdate(appInfo);
                }
            }
        }, observable);
    }

    @Override
    public void update(Context context, AppInfo appInfo) {
        UpdateAppManager.getInstance(context).showNoticeDialog(appInfo, false);
    }

    @Override
    public List<int[]> loadMainMenuDate() {
        List<int[]> menuData = new ArrayList<>();
        menuData.add(new int[]{R.string.home_func_item_scenic, R.mipmap.home_func_0});
        menuData.add(new int[]{R.string.home_func_item_theme_activity, R.mipmap.home_func_1});
        menuData.add(new int[]{R.string.home_func_item_theme_periphery, R.mipmap.home_func_2});
        menuData.add(new int[]{R.string.home_func_item_theme_ar, R.mipmap.home_func_3});
        menuData.add(new int[]{R.string.home_func_item_theme_smart_map, R.mipmap.home_func_4});
        menuData.add(new int[]{R.string.home_func_item_tourist_service, R.mipmap.home_func_5});
        menuData.add(new int[]{R.string.home_func_item_tickets, R.mipmap.home_func_6});
        menuData.add(new int[]{R.string.home_func_item_appointment, R.mipmap.home_func_7});
        return menuData;
    }

    @Override
    public List loadExpoActivities() {
        ArrayList data = new ArrayList();
        data.add(R.string.hot_activity);
        data.add(R.mipmap.hot_activity_0);
        QueryParams params = new QueryParams()
                .add("limit", 0, 3)
                .add("orderBy", "recommended_idx", "true");
        List tmp = mDao.query(ExpoActivityInfo.class, params);
        if (tmp != null) {
            data.addAll(tmp);
        } else {
            return null;
        }
        return data;
    }

    @Override
    public List loadRouteInfo() {
        ArrayList data = new ArrayList();
        data.add(R.string.recommend_garden_route);
        QueryParams params = new QueryParams()
                .add("eq", "enable", "1")
                .add("and")
                .add("eq", "type_id", "1")
                .add("limit", 0, 5);
        List tmp = mDao.query(RouteInfo.class, params);
        if (tmp != null) {
            data.addAll(tmp);
        } else {
            return null;
        }
        return data;
    }

    @Override
    public List<Object> loadSciences() {
        ArrayList data = new ArrayList();
        data.add(R.string.popular_cenic_spots_ranking);
        QueryParams params = new QueryParams()
                .add("eq", "recommend", "1")
                .add("and")
                .add("eq", "type_name", "景点")
                .add("and")
                .add("eq", "enable", 1)
                .add("orderBy", "recommended_idx", true)
                .add("limit", 0, 2);
        List tmp = mDao.query(Encyclopedias.class, params);
        if (tmp != null) {
            data.addAll(tmp);
        } else {
            return null;
        }
        return data;
    }

    @Override
    public List<Object> loadVrInfo() {
        ArrayList data = new ArrayList();
        data.add(R.string.daren_play_expo);
        List tmp = mDao.query(VrInfo.class, new QueryParams()
                .add("eq", "is_recommended", 1)
                .add("orderBy", "recommended_idx", true)
                .add("limit", 0, 2));
        if (tmp != null) {
            data.addAll(tmp);
        } else {
            return null;
        }
        return data;
    }

    @Override
    public List<Object> loadBespeak() {
        ArrayList data = new ArrayList();
        data.add(R.string.home_func_item_appointment);
        List tmp = mDao.query(Schedule.class, new QueryParams()
                .add("eq", "open_state", 0)
                .add("and")
                .add("eq", "online_state", 0)
                .add("limit", 0, 3));
        if (tmp != null) {
            data.addAll(tmp);
        } else {
            return null;
        }
        return data;
    }

    @Override
    public List<Object> loadExpoFoods() {
        ArrayList data = new ArrayList();
        data.add(R.string.expo_food);
        QueryParams params = new QueryParams()
                .add("eq", "type_name", "美食")
                .add("and")
                .add("eq", "enable", 1)
                .add("orderBy", "recommended_idx", true)
                .add("limit", 0, 5);
        List tmp = mDao.query(Encyclopedias.class, params);
        if (tmp != null) {
            data.addAll(tmp);
        } else {
            return null;
        }
        return data;
    }

    @Override
    public void loadOutsideFoods() {
        Map<String, Object> params = Http.getBaseParams();
        params.put("page", 1);
        params.put("type", 1);
        RequestBody requestBody = Http.buildRequestBody(params);
        Observable<CircumResp> observable = Http.getServer().getBussinessCircleListByParams(requestBody);
        Http.request(new ResponseCallback<CircumResp>() {
            @Override
            protected void onResponse(CircumResp rsp) {
                ArrayList<Circum> circums = new ArrayList<>();
                if (null == rsp.circum || TextUtils.isEmpty(rsp.circum)) {
                    return;
                }
                try {
                    JSONObject joData = new JSONObject(rsp.circum);
                    String data = joData.getString("data");
                    JSONObject joRecords = new JSONObject(data);
                    String dataRecord = joRecords.getString("records");
                    List<Circum> tmp = Http.getGsonInstance().fromJson(dataRecord, new TypeToken<ArrayList<Circum>>() {
                    }.getType());
                    if (tmp.size() < 2) {
                        return;
                    }
                    circums.add(tmp.get(0));
                    circums.add(tmp.get(1));
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    mView.setOutsideFoods(circums);
                }
            }
        }, observable);
    }

    @Override
    public List<Object> loadHotels() {
        ArrayList data = new ArrayList();
        data.add(R.string.expo_characteristic_hotel);
        QueryParams params = new QueryParams()
                .add("eq", "type_name", "酒店")
                .add("and")
                .add("eq", "enable", 1)
                .add("orderBy", "recommended_idx", true)
                .add("limit", 0, 3);
        List tmp = mDao.query(Encyclopedias.class, params);
        if (tmp != null) {
            data.addAll(tmp);
        } else {
            return null;
        }
        return data;
    }

    @Override
    public List loadNearbyExperience() {
        List data = new ArrayList<>();
        data.add(R.string.yanqing_must_experience);
        data.add(new int[]{R.string.hotel, R.mipmap.experience1});
        data.add(new int[]{R.string.play, R.mipmap.experience2});
        data.add(new int[]{R.string.find_tab_food, R.mipmap.experience3});
        data.add(new int[]{R.string.market, R.mipmap.experience4});
        return data;
    }

    @Override
    public List<Object> loadDiscover() {
        List data = new ArrayList<>();
        data.add(R.string.discover);
        data.add(new int[]{R.string.discover_1, R.mipmap.discover_1});
        data.add(new int[]{R.string.discover_2, R.mipmap.discover_2});
        data.add(new int[]{R.string.discover_3, R.mipmap.discover_3});
        return data;
    }

    @Override
    public long loadTypeId(String typeName) {
        if (!TextUtils.isEmpty(typeName)) {
            VenuesType type = mDao.unique(VenuesType.class, new QueryParams()
                    .add("like", "type_name", "%" + typeName + "%")
                    .add("or")
                    .add("like", "type_name_en", "%" + typeName + "%"));
            if (type != null)
                return type.getId();
        }
        return 0;
    }

    @Override
    public Float getDistance(long id, LatLng latLng) {
        QueryParams queryParams = new QueryParams()
                .add("eq", "wiki_id", id);
        List<Venue> venues = mDao.query(Venue.class, queryParams);
        if (venues == null || venues.size() == 0) return 999999F;
        Venue venue = venues.get(0);
        Float distance = AMapUtils.calculateLineDistance(latLng, new LatLng(venue.getLat(), venue.getLng()));
        return distance;
    }

    @Override
    public void sortVenue(List<Encyclopedias> list) {
        Collections.sort(list, (o1, o2) -> o1.getDistance().compareTo(o2.getDistance()));
    }
}
