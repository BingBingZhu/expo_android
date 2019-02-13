package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

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

    @DatabaseField(columnName = "_id", id = true)
    @SerializedName("id")
    private Long id;
    @DatabaseField(columnName = "caption")
    @SerializedName("caption")
    private String caption;
    @DatabaseField(columnName = "caption_en")
    @SerializedName("captionen")
    private String captionEn;
    @DatabaseField(columnName = "top_kind")
    @SerializedName("topkind")
    private String topKind;
    @DatabaseField(columnName = "attr_ids")
    @SerializedName("attrids")
    private String attrIds;
    @DatabaseField(columnName = "type")
    @SerializedName("type")
    private String type;
    @DatabaseField(columnName = "ext_attr")
    @SerializedName("extattr")
    private String extAttr;
    @DatabaseField(columnName = "link_wiki_id")
    @SerializedName("linkwikiid")
    private String linkWikiId;
    @DatabaseField(columnName = "link_pan_res_id")
    @SerializedName("linkpanresid")
    private String linkPanResId;
    @DatabaseField(columnName = "remark")
    @SerializedName("remark")
    private String remark;
    @DatabaseField(columnName = "remark_en")
    @SerializedName("remarken")
    private String remarkEn;
    @DatabaseField(columnName = "pic")
    @SerializedName("pic")
    private String pic;
    @DatabaseField(columnName = "lon")
    @SerializedName("lon")
    private String lon;
    @DatabaseField(columnName = "lat")
    @SerializedName("lat")
    private String lat;
    @DatabaseField(columnName = "url")
    @SerializedName("url")
    private String url;
    @DatabaseField(columnName = "create_time")
    @SerializedName("createtime")
    private String createTime;
    @DatabaseField(columnName = "update_time")
    @SerializedName("updatetime")
    private String updateTime;
    @DatabaseField(columnName = "is_recommended")
    @SerializedName("isrecommended")
    private Integer isRecommended;
    @DatabaseField(columnName = "recommended_idx")
    @SerializedName("recommendedidx")
    private Integer recommendedIdx;
    @DatabaseField(columnName = "view_count")
    private long viewCount;

    protected VrInfo() {

    }

    protected VrInfo(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
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
        if (in.readByte() == 0) {
            isRecommended = null;
        } else {
            isRecommended = in.readInt();
        }
        if (in.readByte() == 0) {
            recommendedIdx = null;
        } else {
            recommendedIdx = in.readInt();
        }
        viewCount = in.readLong();
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCaptionEn() {
        return captionEn;
    }

    public void setCaptionEn(String captionEn) {
        this.captionEn = captionEn;
    }

    public String getTopKind() {
        return topKind;
    }

    public void setTopKind(String topKind) {
        this.topKind = topKind;
    }

    public String getAttrIds() {
        return attrIds;
    }

    public void setAttrIds(String attrIds) {
        this.attrIds = attrIds;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExtAttr() {
        if (null == extAttr || TextUtils.isEmpty(extAttr))
            extAttr = "0";
        return extAttr;
    }

    public void setExtAttr(String extAttr) {
        this.extAttr = extAttr;
    }

    public String getLinkWikiId() {
        return linkWikiId;
    }

    public void setLinkWikiId(String linkWikiId) {
        this.linkWikiId = linkWikiId;
    }

    public String getLinkPanResId() {
        return linkPanResId;
    }

    public void setLinkPanResId(String linkPanResId) {
        this.linkPanResId = linkPanResId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemarkEn() {
        return remarkEn;
    }

    public void setRemarkEn(String remarkEn) {
        this.remarkEn = remarkEn;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getUrl() {
        return url;
    }

    public String[] getUrlArray() {
        if (TextUtils.isEmpty(url))
            return null;
        return url.split(";");
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsRecommended() {
        return isRecommended;
    }

    public void setIsRecommended(Integer isRecommended) {
        this.isRecommended = isRecommended;
    }

    public Integer getRecommendedIdx() {
        return recommendedIdx;
    }

    public void setRecommendedIdx(Integer recommendedIdx) {
        this.recommendedIdx = recommendedIdx;
    }

    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
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
        if (isRecommended == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(isRecommended);
        }
        if (recommendedIdx == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(recommendedIdx);
        }
        dest.writeLong(viewCount);
    }
}
