package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "vr_info")
public class VrInfo implements Parcelable {

    /*
    attrids (string, optional): 标签s属性 通过;分割标签名称 用于判断一个资源同时存在于多个标签中 ,
caption (string, optional): 全景标题 ,
captionen (string, optional): 全景标题英文 ,
createtime (string, optional),
extattr (string, optional): 扩展属性 如视频长度单位秒 ,
id (integer, optional),
lat (string, optional): 纬度 ,
linkpanresid (string, optional): 关联的全景图片/视频的id 不选默认为空字符串 ,
linkwikiid (string, optional): 关联的百科id 不选默认为空字符串 ,
lon (string, optional): 经度 ,
pic (string, optional): 缩略图 ,
remark (string, optional): 备注信息 ,
remarken (string, optional): 备注信息英文 ,
topkind (string, optional): 一级分类 1 世园实景 2 文化世园 3 在线导游 ,
type (string, optional): 分类 0 视频 1 图片 2 vr ,
updatetime (string, optional),
url (string, optional): 资源地址
    * */

    @DatabaseField(columnName = "_id", generatedId = true, allowGeneratedIdInsert = true)
    public int id;
    @DatabaseField(columnName = "caption")
    @SerializedName("caption")
    public String caption;
    @DatabaseField(columnName = "caption_en")
    @SerializedName("captionen")
    public String captionEn;
    @DatabaseField(columnName = "top_kind")
    @SerializedName("topkind")
    public String topKind;
    @DatabaseField(columnName = "attr_ids")
    @SerializedName("attrids")
    public String attrIds;
    @DatabaseField(columnName = "type")
    @SerializedName("type")
    public String type;
    @DatabaseField(columnName = "ext_attr")
    @SerializedName("extattr")
    public String extAttr;
    @DatabaseField(columnName = "link_wiki_id")
    @SerializedName("linkwikiid")
    public String linkWikiId;
    @DatabaseField(columnName = "link_pan_res_id")
    @SerializedName("linkpanresid")
    public String linkPanResId;
    @DatabaseField(columnName = "remark")
    @SerializedName("remark")
    public String remark;
    @DatabaseField(columnName = "remark_en")
    @SerializedName("remarken")
    public String remarkEn;
    @DatabaseField(columnName = "pic")
    @SerializedName("pic")
    public String pic;
    @DatabaseField(columnName = "lon")
    @SerializedName("lon")
    public String lon;
    @DatabaseField(columnName = "lat")
    @SerializedName("lat")
    public String lat;
    @DatabaseField(columnName = "url")
    @SerializedName("url")
    public String url;
    @DatabaseField(columnName = "create_time")
    @SerializedName("createtime")
    public String createTime;
    @DatabaseField(columnName = "update_time")
    @SerializedName("updatetime")
    public String updateTime;

    protected VrInfo() {

    }

    protected VrInfo(Parcel in) {
        id = in.readInt();
        caption = in.readString();
        captionEn = in.readString();
        topKind = in.readString();
        attrIds = in.readString();
        type = in.readString();
        extAttr = in.readString();
        linkWikiId = in.readString();
        linkPanResId = in.readString();
        remark = in.readString();
        remarkEn = in.readString();
        pic = in.readString();
        lon = in.readString();
        lat = in.readString();
        url = in.readString();
        createTime = in.readString();
        updateTime = in.readString();
    }

    public static final Creator<VrInfo> CREATOR = new Creator<VrInfo>() {
        @Override
        public VrInfo createFromParcel(Parcel in) {
            return new VrInfo(in);
        }

        @Override
        public VrInfo[] newArray(int size) {
            return new VrInfo[size];
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
        dest.writeString(captionEn);
        dest.writeString(topKind);
        dest.writeString(attrIds);
        dest.writeString(type);
        dest.writeString(extAttr);
        dest.writeString(linkWikiId);
        dest.writeString(linkPanResId);
        dest.writeString(remark);
        dest.writeString(remarkEn);
        dest.writeString(pic);
        dest.writeString(lon);
        dest.writeString(lat);
        dest.writeString(url);
        dest.writeString(createTime);
        dest.writeString(updateTime);
    }
}
