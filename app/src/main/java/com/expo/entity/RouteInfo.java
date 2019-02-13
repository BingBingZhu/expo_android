package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "routes")
public class RouteInfo implements Parcelable {
    @DatabaseField(columnName = "_id", id = true)
    @SerializedName("id")
    public Long id;
    @DatabaseField(columnName = "caption")
    @SerializedName("caption")
    public String caption;
    @DatabaseField(columnName = "captionen")
    @SerializedName("captionen")
    public String captionen;
    @DatabaseField(columnName = "create_time")
    @SerializedName("createtime")
    public String createTime;
    @DatabaseField(columnName = "ids_list")
    @SerializedName("idslist")
    public String idsList;
    @DatabaseField(columnName = "enable")
    @SerializedName("isenable")
    public int enable;
    @DatabaseField(columnName = "lat")
    @SerializedName("lat")
    public String lat;
    @DatabaseField(columnName = "lines_list")
    @SerializedName("lineslist")
    public String linesList;
    @DatabaseField(columnName = "link_h5_url")
    @SerializedName("linkh5url")
    public String linkH5Url;
    @DatabaseField(columnName = "lon")
    @SerializedName("lon")
    public String lon;
    @DatabaseField(columnName = "par_id")
    @SerializedName("parkid")
    public String parkId;
    @DatabaseField(columnName = "parkname")
    @SerializedName("parkname")
    public String parkname;
    @DatabaseField(columnName = "pic_url")
    @SerializedName("picurl")
    public String picUrl;
    @DatabaseField(columnName = "remark")
    @SerializedName("remark")
    public String remark;
    @DatabaseField(columnName = "remarken")
    @SerializedName("remarken")
    public String remarken;
    @DatabaseField(columnName = "trait_label")
    @SerializedName("traitlabel")
    public String traitLabel;
    @DatabaseField(columnName = "type_id")
    @SerializedName("typeid")
    public String typeId;
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
    @DatabaseField(columnName = "play_time")
    @SerializedName("playtime")
    public String playTime;

    @DatabaseField(columnName = "hotCount", defaultValue = "0")
    public int hotCount;

    public RouteInfo() {

    }


    protected RouteInfo(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        caption = in.readString();
        captionen = in.readString();
        createTime = in.readString();
        idsList = in.readString();
        enable = in.readInt();
        lat = in.readString();
        linesList = in.readString();
        linkH5Url = in.readString();
        lon = in.readString();
        parkId = in.readString();
        parkname = in.readString();
        picUrl = in.readString();
        remark = in.readString();
        remarken = in.readString();
        traitLabel = in.readString();
        typeId = in.readString();
        updateTime = in.readString();
        voiceUrl = in.readString();
        voiceUrlEn = in.readString();
        wikiId = in.readString();
        playTime = in.readString();
        hotCount = in.readInt();
    }

    public static final Creator<RouteInfo> CREATOR = new Creator<RouteInfo>() {
        @Override
        public RouteInfo createFromParcel(Parcel in) {
            return new RouteInfo( in );
        }

        @Override
        public RouteInfo[] newArray(int size) {
            return new RouteInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte( (byte) 0 );
        } else {
            dest.writeByte( (byte) 1 );
            dest.writeLong( id );
        }
        dest.writeString( caption );
        dest.writeString( captionen );
        dest.writeString( createTime );
        dest.writeString( idsList );
        dest.writeInt( enable );
        dest.writeString( lat );
        dest.writeString( linesList );
        dest.writeString( linkH5Url );
        dest.writeString( lon );
        dest.writeString( parkId );
        dest.writeString( parkname );
        dest.writeString( picUrl );
        dest.writeString( remark );
        dest.writeString( remarken );
        dest.writeString( traitLabel );
        dest.writeString( typeId );
        dest.writeString( updateTime );
        dest.writeString( voiceUrl );
        dest.writeString( voiceUrlEn );
        dest.writeString( wikiId );
        dest.writeString( playTime );
        dest.writeInt( hotCount );
    }
}
