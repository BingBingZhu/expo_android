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
    @DatabaseField(columnName = "totscores")
    @SerializedName("totscores")
    private String totscores;
    @DatabaseField(columnName = "sex")
    @SerializedName("sex")
    private String sex;
    @DatabaseField(columnName = "city")
    @SerializedName("city")
    private String city;
    @DatabaseField(columnName = "email")
    @SerializedName(value = "email")
    private String email;
    @DatabaseField(columnName = "work_type")
    @SerializedName(value = "worktype")
    private String workType;
    @DatabaseField(columnName = "photo_url")
    @SerializedName(value = "PhotoUrl", alternate = {"picUrl"})
    private String photoUrl;
    @DatabaseField(columnName = "badge_caption")
    @SerializedName(value = "BadgeCaption")
    private String badgeCaption;
    @DatabaseField(columnName = "badge_caption_en")
    @SerializedName(value = "BadgeCaptionen")
    private String badgeCaptionEn;
    @DatabaseField(columnName = "badge_pic_small")
    @SerializedName(value = "BadgePicsmall")
    private String badgePicSmall;

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
        email = in.readString();
        workType = in.readString();
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
        dest.writeString(email);
        dest.writeString(workType);
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

    public int getIntTotscores() {
        if (TextUtils.isEmpty(totscores) || !totscores.matches(Constants.Exps.NUMBER))
            return 0;
        return Integer.parseInt(totscores);
    }

    public void setTotscores(String totscores) {
        this.totscores = totscores;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getBadgeCaption() {
        return badgeCaption;
    }

    public void setBadgeCaption(String badgeCaption) {
        this.badgeCaption = badgeCaption;
    }

    public String getBadgeCaptionEn() {
        return badgeCaptionEn;
    }

    public void setBadgeCaptionEn(String badgeCaptionEn) {
        this.badgeCaptionEn = badgeCaptionEn;
    }

    public String getBadgePicSmall() {
        return badgePicSmall;
    }

    public void setBadgePicSmall(String badgePicSmall) {
        this.badgePicSmall = badgePicSmall;
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

//    @Override
//    public boolean equals(Object obj) {
//        if (null == obj){
//            return false;
//        }
//        User u = (User) obj;
//        if (uid.equals(u.uid) && ukey.equals(u.ukey) && mobile.equals(u.mobile) && score.equals(u.score) &&
//                sex.equals(u.sex) && city.equals(u.city) && nick.equals(u.nick) && birthDay.equals(u.birthDay)
//                && photoUrl.equals(u.photoUrl) && favoriteCount.equals(u.favoriteCount) ){
//            return true;
//        }else{
//            return false;
//        }
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!uid.equals(user.uid)) return false;
        if (!ukey.equals(user.ukey)) return false;
        if (birthDay != null ? !birthDay.equals(user.birthDay) : user.birthDay != null)
            return false;
        if (nick != null ? !nick.equals(user.nick) : user.nick != null) return false;
        if (favoriteCount != null ? !favoriteCount.equals(user.favoriteCount) : user.favoriteCount != null)
            return false;
        if (!mobile.equals(user.mobile)) return false;
        if (score != null ? !score.equals(user.score) : user.score != null) return false;
        if (sex != null ? !sex.equals(user.sex) : user.sex != null) return false;
        if (city != null ? !city.equals(user.city) : user.city != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (workType != null ? !workType.equals(user.workType) : user.workType != null)
            return false;
        return photoUrl != null ? photoUrl.equals(user.photoUrl) : user.photoUrl == null;
    }

    @Override
    public int hashCode() {
        int result = uid.hashCode();
        result = 31 * result + ukey.hashCode();
        result = 31 * result + (birthDay != null ? birthDay.hashCode() : 0);
        result = 31 * result + (nick != null ? nick.hashCode() : 0);
        result = 31 * result + (favoriteCount != null ? favoriteCount.hashCode() : 0);
        result = 31 * result + mobile.hashCode();
        result = 31 * result + (score != null ? score.hashCode() : 0);
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (workType != null ? workType.hashCode() : 0);
        result = 31 * result + (photoUrl != null ? photoUrl.hashCode() : 0);
        return result;
    }

    public User clone() {
        User user = new User();
        user.uid = this.uid;
        user.ukey = this.ukey;
        user.birthDay = this.birthDay;
        user.nick = this.nick;
        user.favoriteCount = this.favoriteCount;
        user.mobile = this.mobile;
        user.score = this.score;
        user.sex = this.sex;
        user.city = this.city;
        user.email = this.email;
        user.workType = this.workType;
        user.photoUrl = this.photoUrl;
        return user;
    }
}
