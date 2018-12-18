package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.blankj.utilcode.util.StringUtils;
import com.expo.R;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Find implements Parcelable {

    public String caption = "";
    public String createtime = "";
    public String enjoys = "0";
    public int id;
    public int isenable;
    public String mobile;
    public String remark = "";
    public int state;
    public String subtime = "";
    public int type;
    public String uid;
    public String uname = "";
    public String updatetime = "";
    public String upic = "";
    public String url1 = "";
    public String url2 = "";
    public String url3 = "";
    public String url4 = "";
    public String url5 = "";
    public String url6 = "";
    public String url7 = "";
    public String url8 = "";
    public String url9 = "";
    public String views = "0";
    public String kind = "0";

    @Expose()
    public int times = 0;

    public Find() {

    }

    public int getResCount() {
        for (int i = 8; i >= 0; i--) {
            if (!StringUtils.isEmpty(getUrl(i))) {
                return i + 1;
            }
        }
        return 0;
    }

    public String getUrl(int position) {
        switch (position) {
            case 0:
                return url1;
            case 1:
                return url2;
            case 2:
                return url3;
            case 3:
                return url4;
            case 4:
                return url5;
            case 5:
                return url6;
            case 6:
                return url7;
            case 7:
                return url8;
            case 8:
                return url9;
        }
        return url1;
    }

    public void setUrl(int position, String url) {
        switch (position) {
            case 0:
                url1 = url;
                break;
            case 1:
                url2 = url;
                break;
            case 2:
                url3 = url;
                break;
            case 3:
                url4 = url;
                break;
            case 4:
                url5 = url;
                break;
            case 5:
                url6 = url;
                break;
            case 6:
                url7 = url;
                break;
            case 7:
                url8 = url;
                break;
            case 8:
                url9 = url;
                break;
            default:

                break;
        }
    }

    public List<String> getUrlList() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (!StringUtils.isEmpty(getUrl(i))) {
                list.add(getUrl(i));
                continue;
            }
            break;
        }
        return list;
    }

    public int getStateRes() {
        switch (state) {
            case 0:
                return R.string.examine_not;
            case 1:
                return R.string.examine_pass;
            case 2:
                return R.string.examine_unpass;
        }
        return R.string.examine_pass;
    }

    public int getStateColor(){
        switch (state) {
            case 0:
            case 1:
                return R.color.green_02cd9b;
            case 2:
                return R.color.red_fe2121;
            default:
                return R.color.green_02cd9b;
        }
    }

    public Map toJson() {
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<String, Object>();
        return gson.fromJson(gson.toJson(this), map.getClass());

    }

    protected Find(Parcel in) {
        caption = in.readString();
        createtime = in.readString();
        enjoys = in.readString();
        id = in.readInt();
        isenable = in.readInt();
        mobile = in.readString();
        remark = in.readString();
        state = in.readInt();
        subtime = in.readString();
        type = in.readInt();
        uid = in.readString();
        uname = in.readString();
        updatetime = in.readString();
        upic = in.readString();
        url1 = in.readString();
        url2 = in.readString();
        url3 = in.readString();
        url4 = in.readString();
        url5 = in.readString();
        url6 = in.readString();
        url7 = in.readString();
        url8 = in.readString();
        url9 = in.readString();
        views = in.readString();
        kind = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(caption);
        dest.writeString(createtime);
        dest.writeString(enjoys);
        dest.writeInt(id);
        dest.writeInt(isenable);
        dest.writeString(mobile);
        dest.writeString(remark);
        dest.writeInt(state);
        dest.writeString(subtime);
        dest.writeInt(type);
        dest.writeString(uid);
        dest.writeString(uname);
        dest.writeString(updatetime);
        dest.writeString(upic);
        dest.writeString(url1);
        dest.writeString(url2);
        dest.writeString(url3);
        dest.writeString(url4);
        dest.writeString(url5);
        dest.writeString(url6);
        dest.writeString(url7);
        dest.writeString(url8);
        dest.writeString(url9);
        dest.writeString(views);
        dest.writeString(kind);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Find> CREATOR = new Creator<Find>() {
        @Override
        public Find createFromParcel(Parcel in) {
            return new Find(in);
        }

        @Override
        public Find[] newArray(int size) {
            return new Find[size];
        }
    };
}
