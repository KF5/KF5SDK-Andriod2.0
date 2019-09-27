package com.kf5.sdk.ticket.entity;

/**
 * author:chosen
 * date:2016/10/20 14:07
 * email:812219713@qq.com
 */

public class Message {

    private String id;

    private String lastCommentId;

    private boolean isRead;

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastCommentId() {
        return lastCommentId;
    }

    public void setLastCommentId(String lastCommentId) {
        this.lastCommentId = lastCommentId;
    }
}
