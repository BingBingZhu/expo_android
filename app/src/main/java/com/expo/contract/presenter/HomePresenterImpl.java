package com.expo.contract.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.expo.R;
import com.expo.contract.HomeContract;
import com.expo.db.QueryParams;
import com.expo.entity.AppInfo;
import com.expo.entity.Circum;
import com.expo.entity.CommonInfo;
import com.expo.entity.Encyclopedias;
import com.expo.entity.ExpoActivityInfo;
import com.expo.entity.Message;
import com.expo.entity.Park;
import com.expo.entity.QAd;
import com.expo.entity.QAt;
import com.expo.entity.RouteInfo;
import com.expo.entity.ScheduleTimeInfo;
import com.expo.entity.ScheduleVenue;
import com.expo.entity.Venue;
import com.expo.entity.VrInfo;
import com.expo.map.MapUtils;
import com.expo.module.heart.HeartBeatService;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.BaseResponse;
import com.expo.network.response.CircumResp;
import com.expo.network.response.VersionInfoResp;
import com.expo.upapp.UpdateAppManager;
import com.expo.utils.Constants;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
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

        long time = TimeUtils.getNowMills();
        QueryParams params = new QueryParams()
                .add("lt", "start_time", time)
                .add("and")
                .add("gt", "end_time", time)
                .add("and")
                .add("eq", "enablestate", 1)
                .add("and")
                .add("eq", "is_recommended", 1);
        mView.showTopLine(mDao.query(ExpoActivityInfo.class, params));
    }

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
    public void appRun() {
        String jpushId = JPushInterface.getRegistrationID(mView.getContext());
        if (!TextUtils.isEmpty(jpushId)) {
            Map<String, Object> params = Http.getBaseParams();
            params.put("jgid", jpushId);
            Observable<BaseResponse> observable = Http.getServer().userlogAppRun(Http.buildRequestBody(params));
            Http.request(new ResponseCallback<BaseResponse>() {
                @Override
                protected void onResponse(BaseResponse rsp) {
                }

            }, observable);
        }
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
        QueryParams params = new QueryParams()
//                .add("gt", "start_time", TimeUtils.getNowMills())//当天之后开始的活动筛选，获取开始时间从明天或更后面开始的活动
//                .add("and")
                .add("gt", "end_time", TimeUtils.getNowMills())//筛选还没有完成的活动，结束时间在今天或之后的活动
                .add("and")
                .add("eq", "enablestate", 1)
                .add("and")
                .add("eq", "is_recommended", 1)
                .add("limit", 0, 3)
                .add("orderBy", "recommended_idx", "true");
        List tmp = mDao.query(ExpoActivityInfo.class, params);
        if (tmp == null || tmp.size() == 0) return null;
        ArrayList data = new ArrayList();
        data.add(R.string.hot_activity);
        data.add(R.mipmap.hot_activity_forest);
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
                .add("orderBy", "sort_idx", true)
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
                .add("like", "type_name", "%" + Constants.CHString.SCENIC_SPOT)
                .add("and")
                .add("eq", "enable", 1)
                .add("orderBy", "recommended_idx", true)
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
        String date = TimeUtils.getNowString(new SimpleDateFormat("yyyy-MM-dd"));
        ArrayList data = new ArrayList();
        data.add(R.string.home_bespeak_today);
        List<ScheduleVenue> tmp = mDao.query(ScheduleVenue.class, new QueryParams()
                .add("eq", "date", date)
                .add("limit", 0, 3));
        if (tmp != null) {
            for (ScheduleVenue sv : tmp) {
                List<ScheduleTimeInfo> times = mDao.query(ScheduleTimeInfo.class, new QueryParams()
                        .add("eq", "venue_id", sv.id)
                        .add("and")
                        .add("eq", "date", date)
                        .add("limit", 0, 3));
                int count = 0;
                int usedCount = 0;
                for (ScheduleTimeInfo st : times) {
                    count += st.personalCount;
                    usedCount += st.personalUsedCount;
                }
                if (count == 0) count = 1;
                sv.percent = usedCount * 100 / count;
            }
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
                .add("eq", "type_name", Constants.CHString.FOODS)
                .add("and")
                .add("eq", "enable", 0)
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
    public void loadExpoFoodsByLocation(double lat, double lng) {
        new Thread(
                () -> {
                    List<Venue> venues = mDao.query(Venue.class, new QueryParams()
                            .add("eq", "type_name", Constants.CHString.RESTAURANT)
                            .add("and")
                            .add("eq", "is_enable", 1)
                            .add("and")
                            .add("notNull", "wiki_id")
                            .add("and")
                            .add("ne", "wiki_id", ""));
                    List<String> ids = new ArrayList<>();
                    Map<Float, Venue> venueMap = new HashMap<>();
                    LatLng latLng = new LatLng(lat, lng);
                    if(null!=venues) {
                        for (Venue venue : venues) {
                            Float distance = AMapUtils.calculateLineDistance(latLng, new LatLng(venue.getLat(), venue.getLng()));
                            venueMap.put(distance, venue);
                        }
                    }
                    List<Encyclopedias> tmp = null;
                    if (!venueMap.isEmpty()) {
                        List<Float> sortKeys = new ArrayList<>(venueMap.keySet());
                        Collections.sort(sortKeys);
                        Map<String, Float> distances = new HashMap<>();
                        if (venueMap.size() > 2) {
                            int i = 0;
                            do {
                                String wikiId = venueMap.get(sortKeys.get(i)).getWikiId();
                                if (!TextUtils.isEmpty(wikiId)) {
                                    ids.add(wikiId);
                                    distances.put(wikiId, sortKeys.get(i));
                                }
                                i++;
                            } while (i <= venueMap.size() && ids.size() < 2);
                        } else {
                            for (int i=0;i<venueMap.size();i++) {
                                String wikiId = venueMap.get(sortKeys.get(i)).getWikiId();
                                if (!TextUtils.isEmpty(wikiId)) {
                                    ids.add(wikiId);
                                    distances.put(wikiId, sortKeys.get(i));
                                }
                            }
                        }
                        tmp = mDao.query(Encyclopedias.class, new QueryParams().add("in", "_id", ids));
                        for (Encyclopedias e : tmp) {
                            String key = String.valueOf(e.getId());
                            if (distances.containsKey(key))
                                e.setDistance(distances.get(key));
                        }
                    }
                    if (tmp != null) {
                        mView.setFoods(tmp);
                    }
                }
        ).start();
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
                .add("eq", "type_name", Constants.CHString.HOTEL)
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
    public List loadQA() {
        ArrayList data = new ArrayList();
        QueryParams params = new QueryParams();
        List tmp = mDao.query(QAt.class, params);
        if (tmp != null) {
            data.addAll(tmp);
        } else {
            return null;
        }
        return data;
    }

    @Override
    public boolean checkInPark(double latitude, double longitude) {
        Park park = mDao.unique(Park.class, null);
        if (park == null) return false;
        List<double[]> bounds = park.getElectronicFenceList();
        return MapUtils.ptInPolygon(latitude, longitude, bounds);
    }


    @Override
    public String loadBespeakUrlInfo() {
        return mDao.unique(CommonInfo.class, new QueryParams()
                .add("eq", "type", CommonInfo.EXPO_BESPEAK_VENUE)).getLinkUrl();
    }

    @Override
    public List<Object> loadPlants() {
        ArrayList data = new ArrayList();
        data.add(R.string.homt_title_plants);
        QueryParams params = new QueryParams()
                .add("eq", "recommend", "1")
                .add("and")
                .add("like", "type_name", "%" + Constants.CHString.PLANTS)
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

}
