package com.kf5.sdk.im.entity;

import com.google.gson.annotations.SerializedName;

/**
 * author:chosen
 * date:2016/10/25 17:50
 * email:812219713@qq.com
 */

public class IMMessage {

    private static final String CHAT_ID = "chat_id";

    private static final String ID = "id";

    private static final String COMPANY_ID = "company_id";

    private static final String CREATED = "created";

    private static final String NAME = "name";

    private static final String ROLE = "role";

    private static final String USER_ID = "user_id";

    private static final String IS_READ = "is_read";

    private static final String TYPE = "type";

    private static final String MSG = "msg";

    private static final String REPLY_TIMEOUT = "reply_timeout";

    private static final String UPLOAD_ID = "upload_id";

    private static final String UPLOAD = "Upload";

    private static final String TIMESTAMP = "timestamp";

    private static final String RECALLED = "recalled";

    @SerializedName(ID)
    private int id; //消息id
    @SerializedName(NAME)
    private String name; // 昵称
    @SerializedName(TYPE)
    private String type; //消息类型
    @SerializedName(MSG)
    private String message; //消息内容
    @SerializedName(CHAT_ID)
    private int chatId; //聊天id
    @SerializedName(CREATED)
    private long created; //创建时间
    @SerializedName(ROLE)
    private String role; //角色
    @SerializedName(IS_READ)
    private int isRead; //是否已读
    @SerializedName(USER_ID)
    private int userId; // 用户id
    @SerializedName(UPLOAD_ID)
    private int uploadId; //附件id
    @SerializedName(UPLOAD)
    private Upload upload; //附件模型
    @SerializedName(TIMESTAMP)
    private String timeStamp;
    @SerializedName(REPLY_TIMEOUT)
    private int reply_timeout;
    @SerializedName(COMPANY_ID)
    private int companyId;
    @SerializedName(RECALLED)
    private int recalledStatus;

    private Status status; //发送状态

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    public int getUploadId() {
        return uploadId;
    }

    public void setUploadId(int uploadId) {
        this.uploadId = uploadId;
    }

    public Upload getUpload() {
        return upload;
    }

    public void setUpload(Upload upload) {
        this.upload = upload;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getReply_timeout() {
        return reply_timeout;
    }

    public void setReply_timeout(int reply_timeout) {
        this.reply_timeout = reply_timeout;
    }

    public int getRecalledStatus() {
        return recalledStatus;
    }

    public void setRecalledStatus(int recalledStatus) {
        this.recalledStatus = recalledStatus;
    }
}
