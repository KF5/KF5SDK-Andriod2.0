package com.kf5.sdk.ticket.entity;

import com.google.gson.annotations.SerializedName;
import com.kf5.sdk.helpcenter.entity.Attachment;
import com.kf5.sdk.system.entity.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * author:chosen
 * date:2016/10/20 16:21
 * email:812219713@qq.com
 */

public class Comment {

    @SerializedName(Field.ID)
    private int id;
    @SerializedName(Field.CONTENT)
    private String content;
    @SerializedName(Field.HTML_CONTENT)
    private String htmlContent;
    @SerializedName(Field.PUBLIC)
    private boolean isPublic;
    @SerializedName(Field.AUTHOR_ID)
    private int authorId;
    @SerializedName(Field.CREATED_AT)
    private long createdAt;
    @SerializedName(Field.AUTHOR_NAME)
    private String authorName;
    @SerializedName(Field.ATTACHMENTS)
    private List<Attachment> mAttachmentList = new ArrayList<>();

    private MessageStatus mMessageStatus;

    public MessageStatus getMessageStatus() {
        return mMessageStatus;
    }

    public void setMessageStatus(MessageStatus messageStatus) {
        mMessageStatus = messageStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public List<Attachment> getAttachmentList() {
        return mAttachmentList;
    }

    public void setAttachmentList(List<Attachment> attachmentList) {
        mAttachmentList = attachmentList;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
