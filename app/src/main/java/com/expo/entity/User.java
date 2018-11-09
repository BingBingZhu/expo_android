package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.expo.base.ExpoApp;
import com.expo.db.dao.BaseDao;
import com.expo.db.dao.BaseDaoImpl;
import com.expo.network.response.BaseResponse;
import com.expo.utils.Constants;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "user")
public class User extends BaseResponse implements Parcelable {
    @DatabaseField(columnName = "uid", id = true)
    @SerializedName("Uid")
    private String uid;
    @DatabaseField(columnName = "ukey")
    @SerializedName("Ukey")
    private String ukey;
    @DatabaseField(columnName = "birth_day")
    @SerializedName("birthday")
    private String birthDay;
    @DatabaseField(columnName = "nick")
    @SerializedName("caption")
    private String nick;
    @DatabaseField(columnName = "favorite_count")
    @SerializedName("favoritecount")
    private String favoriteCount;
    @DatabaseField(columnName = "mobile")
    @SerializedName("mobile")
    private String mobile;
    @DatabaseField(columnName = "score")
    @SerializedName("scores")
    private String score;
    @DatabaseField(columnName = "sex")
    @SerializedName("sex")
    private String sex;
    @DatabaseField(columnName = "city")
    @SerializedName("city")
    private String city;
    @DatabaseField(columnName = "photo_url")
    @SerializedName(value = "PhotoUrl", alternate = {"picUrl"})
    private String photoUrl;

    public User() {
    }

    protected User(Parcel in) {
        uid = in.readString();
        ukey = in.readString();
        birthDay = in.readString();
        nick = in.readString();
        favoriteCount = in.readString();
        mobile = in.readString();
        score = in.readString();
        sex = in.readString();
        city = in.readString();
        photoUrl = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(ukey);
        dest.writeString(birthDay);
        dest.writeString(nick);
        dest.writeString(favoriteCount);
        dest.writeString(mobile);
        dest.writeString(score);
        dest.writeString(sex);
        dest.writeString(city);
        dest.writeString(photoUrl);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUkey() {
        return ukey;
    }

    public void setUkey(String ukey) {
        this.ukey = ukey;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(String favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getScore() {
        return score;
    }

    public int getIntScore() {
        if (TextUtils.isEmpty(score) || !score.matches(Constants.Exps.NUMBER))
            return 0;
        return Integer.parseInt(score);
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void saveOnDb(BaseDao mDao) {
        ExpoApp.getApplication().setUser(this);
        mDao.clear(User.class);
        mDao.saveOrUpdate(this);
    }

    public void deleteOnDb(BaseDao mDao) {
        ExpoApp.getApplication().setUser(null);
        mDao.clear(User.class);
    }
}
