package com.kf5.sdk.ticket.entity;

import com.kf5.sdk.system.entity.MultiPageEntity;

import java.util.List;

/**
 * author:chosen
 * date:2017/3/6 16:04
 * email:812219713@qq.com
 */

public class TicketListObj extends MultiPageEntity {

    private List<Requester> requests;

    public List<Requester> getRequests() {
        return requests;
    }

    public void setRequests(List<Requester> requests) {
        this.requests = requests;
    }
}
