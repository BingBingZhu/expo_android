package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.expo.network.Http;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

@DatabaseTable(tableName = "contacts")
public class Contacts implements Parcelable {
    @DatabaseField(columnName = "_id", id = true)
    public String ids;
    @DatabaseField(columnName = "type")
    public String type;
    @DatabaseField(columnName = "type_name")
    public String typeName;
    @DatabaseField(columnName = "name")
    public String name;
    @DatabaseField(columnName = "u_id")
    public String uid;

    public Contacts() {
    }

    protected Contacts(Parcel in) {
        ids = in.readString();
        type = in.readString();
        typeName = in.readString();
        name = in.readString();
        uid = in.readString();
    }

    public static final Creator<Contacts> CREATOR = new Creator<Contacts>() {
        @Override
        public Contacts createFromParcel(Parcel in) {
            return new Contacts(in);
        }

        @Override
        public Contacts[] newArray(int size) {
            return new Contacts[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ids);
        dest.writeString(type);
        dest.writeString(typeName);
        dest.writeString(name);
        dest.writeString(uid);
    }
}
