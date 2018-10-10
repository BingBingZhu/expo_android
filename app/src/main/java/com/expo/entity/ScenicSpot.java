package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "scenic_spot")
public class ScenicSpot extends Spot {
    @DatabaseField(columnName = "sos_number")
    @SerializedName("sosnumber")
    private String sosNumber;
    @DatabaseField(columnName = "is_charge")
    @SerializedName("ischarge")
    private String isCharge;
    @DatabaseField(columnName = "ar_price")
    @SerializedName("arprice")
    private String arPrice;
    @DatabaseField(columnName = "panorama_url")
    @SerializedName("panoramaurl")
    private String panoramaUrl;
    @DatabaseField(columnName = "base_online_count")
    @SerializedName("baseonlinecount")
    private int baseOnlineCount;
    @DatabaseField(columnName = "map_url")
    @SerializedName("mapurl")
    private String mapUrl;
    @DatabaseField(columnName = "map_coordinate")
    @SerializedName("mapcoordinate")
    private String mapCoordinate;
    @DatabaseField(columnName = "guest_count")
    @SerializedName("guestcount")
    private int guestCount;
    @DatabaseField(columnName = "use_count")
    @SerializedName("usecount")
    private int useCount;
    @DatabaseField(columnName = "park_advert_url")
    @SerializedName("parkadverturl")
    private String parkAdvertUrl;

    public ScenicSpot() {
    }

    protected ScenicSpot(Parcel in) {
        super(in);
        sosNumber = in.readString();
        isCharge = in.readString();
        arPrice = in.readString();
        panoramaUrl = in.readString();
        baseOnlineCount = in.readInt();
        mapUrl = in.readString();
        mapCoordinate = in.readString();
        guestCount = in.readInt();
        useCount = in.readInt();
        parkAdvertUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(sosNumber);
        dest.writeString(isCharge);
        dest.writeString(arPrice);
        dest.writeString(panoramaUrl);
        dest.writeInt(baseOnlineCount);
        dest.writeString(mapUrl);
        dest.writeString(mapCoordinate);
        dest.writeInt(guestCount);
        dest.writeInt(useCount);
        dest.writeString(parkAdvertUrl);
    }

    public static final Parcelable.Creator<ScenicSpot> CREATOR = new Parcelable.Creator<ScenicSpot>() {
        @Override
        public ScenicSpot createFromParcel(Parcel in) {
            return new ScenicSpot(in);
        }

        @Override
        public ScenicSpot[] newArray(int size) {
            return new ScenicSpot[size];
        }
    };

    public String getSosNumber() {
        return sosNumber;
    }

    public void setSosNumber(String sosNumber) {
        this.sosNumber = sosNumber;
    }

    public String getIsCharge() {
        return isCharge;
    }

    public void setIsCharge(String isCharge) {
        this.isCharge = isCharge;
    }

    public String getArPrice() {
        return arPrice;
    }

    public void setArPrice(String arPrice) {
        this.arPrice = arPrice;
    }

    public String getPanoramaUrl() {
        return panoramaUrl;
    }

    public void setPanoramaUrl(String panoramaUrl) {
        this.panoramaUrl = panoramaUrl;
    }

    public int getBaseOnlineCount() {
        return baseOnlineCount;
    }

    public void setBaseOnlineCount(int baseOnlineCount) {
        this.baseOnlineCount = baseOnlineCount;
    }

    public String getMapUrl() {
        return mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    public String getMapCoordinate() {
        return mapCoordinate;
    }

    public void setMapCoordinate(String mapCoordinate) {
        this.mapCoordinate = mapCoordinate;
    }

    public int getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(int guestCount) {
        this.guestCount = guestCount;
    }

    public int getUseCount() {
        return useCount;
    }

    public void setUseCount(int useCount) {
        this.useCount = useCount;
    }

    public String getParkAdvertUrl() {
        return parkAdvertUrl;
    }

    public void setParkAdvertUrl(String parkAdvertUrl) {
        this.parkAdvertUrl = parkAdvertUrl;
    }
}
