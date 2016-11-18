package com.kf5.sdk.helpcenter.entity;

import com.google.gson.annotations.SerializedName;
import com.kf5.sdk.system.entity.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * author:chosen
 * date:2016/10/19 16:38
 * email:812219713@qq.com
 */

public class Post {

    @SerializedName(Field.ID)
    private int id;
    @SerializedName(Field.FORUM_ID)
    private int forumId;
    @SerializedName(Field.TITLE)
    private String title;
    @SerializedName(Field.CONTENT)
    private String content;
    @SerializedName(Field.AUTHOR_NAME)
    private String authorName;
    @SerializedName(Field.CREATED_AT)
    private int createdAt;
    @SerializedName(Field.UPDATED_AT)
    private int updatedAt;
    @SerializedName(Field.ATTACHMENTS)
    private List<Attachment> mAttachments = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getForumId() {
        return forumId;
    }

    public void setForumId(int forumId) {
        this.forumId = forumId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public List<Attachment> getAttachments() {
        return mAttachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        mAttachments = attachments;
    }

    public int getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(int createdAt) {
        this.createdAt = createdAt;
    }

    public int getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(int updatedAt) {
        this.updatedAt = updatedAt;
    }
}
