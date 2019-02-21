package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.expo.network.Http;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

@DatabaseTable(tableName = "qad")
public class QAd implements Parcelable {

    @DatabaseField(columnName = "_id", id = true)
    @SerializedName("id")
    private Long id;

    @DatabaseField(columnName = "question_type")
    @SerializedName("questiontype")
    private String questionType;

    @DatabaseField(columnName = "question_types")
    @SerializedName("questiontypes")
    private String questionTypes;

    @SerializedName("qatlist")
    public List<QAt> qatlist;

    public QAd() {
    }

    protected QAd(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        questionType = in.readString();
        questionTypes = in.readString();
    }

    public static final Creator<QAd> CREATOR = new Creator<QAd>() {
        @Override
        public QAd createFromParcel(Parcel in) {
            return new QAd(in);
        }

        @Override
        public QAd[] newArray(int size) {
            return new QAd[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestiontype() {
        return questionType;
    }

    public void setQuestiontype(String questionType) {
        this.questionType = questionType;
    }

    public String getQuestiontypes() {
        return questionTypes;
    }

    public void setQuestiontypes(String questionTypes) {
        this.questionTypes = questionTypes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(id);
        }
        parcel.writeString(questionType);
        parcel.writeString(questionTypes);
    }
}
