package com.kf5.sdk.ticket.entity;

import com.kf5.sdk.system.entity.MultiPageEntity;

import java.util.List;

/**
 * author:chosen
 * date:2017/3/6 17:36
 * email:812219713@qq.com
 */

public class TicketDetailObj extends MultiPageEntity {

    Requester request;

    List<Comment> comments;

    public Requester getRequest() {
        return request;
    }

    public void setRequest(Requester request) {
        this.request = request;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
