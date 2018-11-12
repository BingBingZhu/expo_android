package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.expo.base.BaseEventMessage;
import com.expo.db.QueryParams;
import com.expo.db.dao.BaseDao;
import com.expo.db.dao.BaseDaoImpl;
import com.expo.network.Http;
import com.expo.utils.Constants;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DatabaseTable(tableName = "message")
public class Message implements Parcelable {
    @DatabaseField(columnName = "caption")
    @SerializedName("caption")
    private String caption;
    @DatabaseField(columnName = "caption_en")
    @SerializedName("captionen")
    private String captionEn;
    @DatabaseField(columnName = "command")
    @SerializedName("cmd")
    private String command;//消息命令  logout 是通知用户强制登出消息  relogin 通知用户有人在其它手机登录   order 订单状态   pay 支付状态
    @DatabaseField(columnName = "content")
    @SerializedName("content")
    private String content;
    @DatabaseField(columnName = "content_en")
    @SerializedName("contenten")
    private String contentEn;
    @DatabaseField(columnName = "create_time")
    @SerializedName("createtime")
    private String createTime;
    @DatabaseField(columnName = "_id", allowGeneratedIdInsert = true, generatedId = true)
    @SerializedName("id")
    private Long id;
    @DatabaseField(columnName = "link_id")
    @SerializedName("linkid")
    private String linkId;
    @DatabaseField(columnName = "mobile")
    @SerializedName("mobile")
    private String mobile;
    @DatabaseField(columnName = "msg_kind")
    @SerializedName("msgkind")
    private String msgKind;//消息方式 1 站内消息  2 手机短信 3 app消息推送
    @DatabaseField(columnName = "params")
    @SerializedName("params")
    private String params;
    @DatabaseField(columnName = "type")
    @SerializedName("type")
    private String type;// 类别 1 系统消息 2 活动消息 3 命令消息（由系统主动产生） 4 游客服务反馈（由系统主动产生） 5 预约提醒（由系统主动产生）
    @DatabaseField(columnName = "uid")
    @SerializedName("uid")
    private String uid;
    @DatabaseField(columnName = "read")
    private boolean read;

    public Message() {
    }

    protected Message(Parcel in) {
        caption = in.readString();
        captionEn = in.readString();
        command = in.readString();
        content = in.readString();
        contentEn = in.readString();
        createTime = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        linkId = in.readString();
        mobile = in.readString();
        msgKind = in.readString();
        params = in.readString();
        type = in.readString();
        uid = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

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

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentEn() {
        return contentEn;
    }

    public void setContentEn(String contentEn) {
        this.contentEn = contentEn;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMsgKind() {
        return msgKind;
    }

    public void setMsgKind(String msgKind) {
        this.msgKind = msgKind;
    }

    public Map<String, Object> getParams() {
        if (TextUtils.isEmpty(params))
            return null;
        params = params.replaceAll("/'", "\"");
        return Http.getGsonInstance().fromJson(params, new TypeToken<HashMap<String, Object>>() {
        }.getType());
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString( caption );
        dest.writeString( captionEn );
        dest.writeString( command );
        dest.writeString( content );
        dest.writeString( contentEn );
        dest.writeString( createTime );
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(linkId);
        dest.writeString(mobile);
        dest.writeString(msgKind);
        dest.writeString(params);
        dest.writeString(type);
        dest.writeString(uid);
    }

    public static void saveMessage(List<Message> list) {
        if (list == null || list.size() == 0) return;
        BaseDao dao = new BaseDaoImpl();
        dao.saveOrUpdateAll(list);
        list.get(0).sendMessageCount(dao);
    }

    public void readMessage() {
        BaseDao dao = new BaseDaoImpl();
        read = true;
        dao.saveOrUpdate(this);
        sendMessageCount(dao);
    }

    public void delMessage() {
        BaseDao dao = new BaseDaoImpl();
        QueryParams params = new QueryParams()
                .add("eq", "type", type);
        List<Message> messageList = dao.query(Message.class, params);
        dao.deleteAll(messageList);
        sendMessageCount(dao);
    }

    public void sendMessageCount(BaseDao dao) {
        if (dao == null)
            dao = new BaseDaoImpl();
        QueryParams params = new QueryParams();
        params.add("eq", "read", false);
        EventBus.getDefault().post(new BaseEventMessage(Constants.EventBusMessageId.EVENTBUS_ID_HEART_MESSAGE_UNREAD_COUNT, dao.count(Message.class, params)));
    }
}
