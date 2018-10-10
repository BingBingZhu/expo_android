package com.expo.entity;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

public abstract class Scene extends Spot {
    @DatabaseField(columnName = "scenic_spot_id")
    @SerializedName("parkid")
    private String scenicSpotId;
    @DatabaseField(columnName = "park_name")
    @SerializedName("parkname")
    private String scenicSpotName;
    @DatabaseField(columnName = "is_recommended")
    @SerializedName("isrecommended")
    private Integer isRecommended;
    @DatabaseField(columnName = "recommended_idx")
    @SerializedName("recommendedidx")
    private String recommendedIdx;
    @DatabaseField(columnName = "score")
    @SerializedName("score")
    private Integer score;

    public Scene() {
    }

    protected Scene(Parcel in) {
        super( in );
        scenicSpotId = in.readString();
        scenicSpotName = in.readString();
        if (in.readByte() == 0) {
            isRecommended = null;
        } else {
            isRecommended = in.readInt();
        }
        recommendedIdx = in.readString();
        if (in.readByte() == 0) {
            score = null;
        } else {
            score = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel( dest, flags );
        dest.writeString( scenicSpotId );
        dest.writeString( scenicSpotName );
        if (isRecommended == null) {
            dest.writeByte( (byte) 0 );
        } else {
            dest.writeByte( (byte) 1 );
            dest.writeInt( isRecommended );
        }
        dest.writeString( recommendedIdx );
        if (score == null) {
            dest.writeByte( (byte) 0 );
        } else {
            dest.writeByte( (byte) 1 );
            dest.writeInt( score );
        }
    }

    public String getScenicSpotId() {
        return scenicSpotId;
    }

    public void setScenicSpotId(String scenicSpotId) {
        this.scenicSpotId = scenicSpotId;
    }

    public String getScenicSpotName() {
        return scenicSpotName;
    }

    public void setScenicSpotName(String scenicSpotName) {
        this.scenicSpotName = scenicSpotName;
    }

    public Integer getIsRecommended() {
        return isRecommended;
    }

    public void setIsRecommended(Integer isRecommended) {
        this.isRecommended = isRecommended;
    }

    public String getRecommendedIdx() {
        return recommendedIdx;
    }

    public void setRecommendedIdx(String recommendedIdx) {
        this.recommendedIdx = recommendedIdx;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
