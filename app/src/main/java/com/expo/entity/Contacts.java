package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "contacts")
public class Contacts implements Parcelable {
    @DatabaseField(columnName = "_id", dataType = DataType.INTEGER, generatedId = true)
    public int id;
    @DatabaseField(columnName = "ids")
    public String ids;
    @DatabaseField(columnName = "type")
    public String type;
    @DatabaseField(columnName = "type_name")
    public String typeName;
    @DatabaseField(columnName = "bg_vr_card")
    public String name;
    @DatabaseField(columnName = "u_id")
    public String uid;

    public Contacts() {
    }

    protected Contacts(Parcel in) {
        id = in.readInt();
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
        dest.writeInt(id);
        dest.writeString(ids);
        dest.writeString(type);
        dest.writeString(typeName);
        dest.writeString(name);
        dest.writeString(uid);
    }
}
