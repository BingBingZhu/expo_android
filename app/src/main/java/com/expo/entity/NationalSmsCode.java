package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/*
 * 国家短信代号
 */
@DatabaseTable(tableName = "nation_sms_code")
public class NationalSmsCode implements Parcelable {
    @DatabaseField(columnName = "_id", generatedId = true)
    private Integer id;
    @DatabaseField(columnName = "en")
    private String en;
    @DatabaseField(columnName = "zh")
    private String zh;
    @DatabaseField(columnName = "py")
    private String py;
    @DatabaseField(columnName = "code")
    private String code;

    public NationalSmsCode() {
    }

    protected NationalSmsCode(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        en = in.readString();
        zh = in.readString();
        py = in.readString();
        code = in.readString();
    }

    public static final Creator<NationalSmsCode> CREATOR = new Creator<NationalSmsCode>() {
        @Override
        public NationalSmsCode createFromParcel(Parcel in) {
            return new NationalSmsCode( in );
        }

        @Override
        public NationalSmsCode[] newArray(int size) {
            return new NationalSmsCode[size];
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
            dest.writeInt( id );
        }
        dest.writeString( en );
        dest.writeString( zh );
        dest.writeString( py );
        dest.writeString( code );
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getZh() {
        return zh;
    }

    public void setZh(String zh) {
        this.zh = zh;
    }

    public String getPy() {
        return py;
    }

    public void setPy(String py) {
        this.py = py;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NationalSmsCode that = (NationalSmsCode) o;

        if (!id.equals( that.id )) return false;
        if (en != null ? !en.equals( that.en ) : that.en != null) return false;
        if (zh != null ? !zh.equals( that.zh ) : that.zh != null) return false;
        if (py != null ? !py.equals( that.py ) : that.py != null) return false;
        return code != null ? code.equals( that.code ) : that.code == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (en != null ? en.hashCode() : 0);
        result = 31 * result + (zh != null ? zh.hashCode() : 0);
        result = 31 * result + (py != null ? py.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        return result;
    }
}
