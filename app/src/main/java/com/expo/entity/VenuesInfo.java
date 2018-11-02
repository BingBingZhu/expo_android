package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "venues")
public class VenuesInfo implements Parcelable {

    @DatabaseField(columnName = "id", id = true)
    @SerializedName("id")
    public int id;
    @DatabaseField(columnName = "caption")
    @SerializedName("caption")
    public String caption;
    @DatabaseField(columnName = "captionen")
    @SerializedName("captionen")
    public String captionen;
    @DatabaseField(columnName = "create_time")
    @SerializedName("createtime")
    public String createTime;
    @DatabaseField(columnName = "gis_id")
    @SerializedName("gisid")
    public String gisId;
    @DatabaseField(columnName = "gis_type_id")
    @SerializedName("gistypeid")
    public String gisTypeId;
    @DatabaseField(columnName = "is_enable")
    @SerializedName("isenable")
    public int isEnable;
    @DatabaseField(columnName = "is_recommended")
    @SerializedName("isrecommended")
    public int isRecommended;
    @DatabaseField(columnName = "lat")
    @SerializedName("lat")
    public String lat;
    @DatabaseField(columnName = "link_h5_url")
    @SerializedName("linkh5url")
    public String linkH5Url;
    @DatabaseField(columnName = "link_h5_url_en")
    @SerializedName("linkh5urlen")
    public String linkH5UrlEn;
    @DatabaseField(columnName = "lon")
    @SerializedName("lon")
    public String lon;
    @DatabaseField(columnName = "park_id")
    @SerializedName("parkid")
    public String parkId;
    @DatabaseField(columnName = "park_name")
    @SerializedName("parkname")
    public String parkName;
    @DatabaseField(columnName = "pic_url")
    @SerializedName("picurl")
    public String picUrl;
    @DatabaseField(columnName = "pic_url_en")
    @SerializedName("picurlen")
    public String picUrlEn;
    @DatabaseField(columnName = "recommended_idx")
    @SerializedName("recommendedidx")
    public String recommendedIdx;
    @DatabaseField(columnName = "remark")
    @SerializedName("remark")
    public String remark;
    @DatabaseField(columnName = "remark_en")
    @SerializedName("remarken")
    public String remarkEn;
    @DatabaseField(columnName = "score")
    @SerializedName("score")
    public int score;
    @DatabaseField(columnName = "type")
    @SerializedName("type")
    public String type;
    @DatabaseField(columnName = "update_time")
    @SerializedName("updatetime")
    public String updateTime;
    @DatabaseField(columnName = "voice_url")
    @SerializedName("voiceurl")
    public String voiceUrl;
    @DatabaseField(columnName = "voice_url_en")
    @SerializedName("voiceurlen")
    public String voiceUrlEn;
    @DatabaseField(columnName = "wiki_id")
    @SerializedName("wikiid")
    public String wikiId;

    public VenuesInfo() {

    }

    public VenuesInfo(Parcel in) {
        id = in.readInt();
        caption = in.readString();
        captionen = in.readString();
        createTime = in.readString();
        gisId = in.readString();
        gisTypeId = in.readString();
        isEnable = in.readInt();
        isRecommended = in.readInt();
        lat = in.readString();
        linkH5Url = in.readString();
        linkH5UrlEn = in.readString();
        lon = in.readString();
        parkId = in.readString();
        parkName = in.readString();
        picUrl = in.readString();
        picUrlEn = in.readString();
        recommendedIdx = in.readString();
        remark = in.readString();
        remarkEn = in.readString();
        score = in.readInt();
        type = in.readString();
        updateTime = in.readString();
        voiceUrl = in.readString();
        voiceUrlEn = in.readString();
        wikiId = in.readString();
    }

    public static final Creator<VenuesInfo> CREATOR = new Creator<VenuesInfo>() {
        @Override
        public VenuesInfo createFromParcel(Parcel in) {
            return new VenuesInfo(in);
        }

        @Override
        public VenuesInfo[] newArray(int size) {
            return new VenuesInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(caption);
        dest.writeString(captionen);
        dest.writeString(createTime);
        dest.writeString(gisId);
        dest.writeString(gisTypeId);
        dest.writeInt(isEnable);
        dest.writeInt(isRecommended);
        dest.writeString(lat);
        dest.writeString(linkH5Url);
        dest.writeString(linkH5UrlEn);
        dest.writeString(lon);
        dest.writeString(parkId);
        dest.writeString(parkName);
        dest.writeString(picUrl);
        dest.writeString(picUrlEn);
        dest.writeString(recommendedIdx);
        dest.writeString(remark);
        dest.writeString(remarkEn);
        dest.writeInt(score);
        dest.writeString(type);
        dest.writeString(updateTime);
        dest.writeString(voiceUrl);
        dest.writeString(voiceUrlEn);
        dest.writeString(wikiId);
    }
}
