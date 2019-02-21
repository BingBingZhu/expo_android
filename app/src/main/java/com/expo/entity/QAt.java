package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.expo.network.Http;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

@DatabaseTable(tableName = "qat")
public class QAt implements Parcelable {

    @DatabaseField(columnName = "_id", id = true)
    @SerializedName("id")
    private Long id;

    @DatabaseField(columnName = "answer")
    @SerializedName("answer")
    private String answer;

    @DatabaseField(columnName = "answers")
    @SerializedName("answers")
    private String answers;

    @DatabaseField(columnName = "create_time")
    @SerializedName("createtime")
    private String createTime;

    @DatabaseField(columnName = "qatype_id")
    @SerializedName("qatypeid")
    private String qatypeId;

    @DatabaseField(columnName = "question")
    @SerializedName("question")
    private String question;

    @DatabaseField(columnName = "questions")
    @SerializedName("questions")
    private String questions;

    @DatabaseField(columnName = "update_time")
    @SerializedName("updatetime")
    private String updateTime;

    public QAt() {
    }

    protected QAt(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        answer = in.readString();
        answers = in.readString();
        createTime = in.readString();
        qatypeId = in.readString();
        question = in.readString();
        questions = in.readString();
        updateTime = in.readString();
    }

    public static final Creator<QAt> CREATOR = new Creator<QAt>() {
        @Override
        public QAt createFromParcel(Parcel in) {
            return new QAt(in);
        }

        @Override
        public QAt[] newArray(int size) {
            return new QAt[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getQatypeId() {
        return qatypeId;
    }

    public void setQatypeId(String qatypeId) {
        this.qatypeId = qatypeId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
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
        parcel.writeString(answer);
        parcel.writeString(answers);
        parcel.writeString(createTime);
        parcel.writeString(qatypeId);
        parcel.writeString(question);
        parcel.writeString(questions);
        parcel.writeString(updateTime);
    }
}
