package com.kf5.sdk.ticket.entity;

import com.google.gson.annotations.SerializedName;
import com.kf5.sdk.system.entity.Field;

import java.io.Serializable;

/**
 * author:chosen
 * date:2016/10/20 14:04
 * email:812219713@qq.com
 */

public class Requester implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @SerializedName(Field.ID)
    private int id;

    private String url;

    @SerializedName(Field.TITLE)
    private String title;

    @SerializedName(Field.DESCRIPTION)
    private String description;

    private String type;

    @SerializedName(Field.STATUS)
    private int status;

    private String priority;

    private String rquester_id;

    private String assignee_id;

    private String organization_id;

    private String group_id;

    private String due_date;

    @SerializedName(Field.CREATED_AT)
    private int created_at;

    @SerializedName(Field.UPDATED_AT)
    private int updated_at;
    @SerializedName(Field.LAST_COMMENT_ID)
    private int last_comment_id;
    @SerializedName(Field.RATING)
    private int rating;
    @SerializedName(Field.RATING_CONTENT)
    private String ratingContent;
    @SerializedName(Field.RATING_FLAG)
    private boolean ratingFlag;
    @SerializedName(Field.RATE_LEVEL_COUNT)
    private int rateLevelCount;

    public int getRateLevelCount() {
        return rateLevelCount;
    }

    public void setRateLevelCount(int rateLevelCount) {
        this.rateLevelCount = rateLevelCount;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getRatingContent() {
        return ratingContent;
    }

    public void setRatingContent(String ratingContent) {
        this.ratingContent = ratingContent;
    }

    public boolean isRatingFlag() {
        return ratingFlag;
    }

    public void setRatingFlag(boolean ratingFlag) {
        this.ratingFlag = ratingFlag;
    }

    public int getLast_comment_id() {
        return last_comment_id;
    }

    public void setLast_comment_id(int last_comment_id) {
        this.last_comment_id = last_comment_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getRquester_id() {
        return rquester_id;
    }

    public void setRquester_id(String rquester_id) {
        this.rquester_id = rquester_id;
    }

    public String getAssignee_id() {
        return assignee_id;
    }

    public void setAssignee_id(String assignee_id) {
        this.assignee_id = assignee_id;
    }

    public String getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(String organization_id) {
        this.organization_id = organization_id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public int getCreated_at() {
        return created_at;
    }

    public void setCreated_at(int created_at) {
        this.created_at = created_at;
    }

    public int getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(int updated_at) {
        this.updated_at = updated_at;
    }
}
