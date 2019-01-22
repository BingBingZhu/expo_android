package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tuple")
public class Tuple implements Parcelable {
    @DatabaseField(columnName = "_id", id = true)
    @SerializedName("element")
    private Long element;
    @DatabaseField(columnName = "score")
    @SerializedName("score")
    private long score;

    public Tuple() {
    }

    protected Tuple(Parcel in) {
        if (in.readByte() == 0) {
            element = null;
        } else {
            element = in.readLong();
        }
        score = in.readLong();
    }

    public static final Creator<Tuple> CREATOR = new Creator<Tuple>() {
        @Override
        public Tuple createFromParcel(Parcel in) {
            return new Tuple(in);
        }

        @Override
        public Tuple[] newArray(int size) {
            return new Tuple[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (element == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(element);
        }
        dest.writeLong(score);
    }

    public Long getElement() {
        return element;
    }

    public void setElement(Long element) {
        this.element = element;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
}
