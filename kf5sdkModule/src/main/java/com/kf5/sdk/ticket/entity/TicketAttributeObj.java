package com.kf5.sdk.ticket.entity;

import java.util.List;

/**
 * author:chosen
 * date:2017/3/7 14:09
 * email:812219713@qq.com
 */

public class TicketAttributeObj {

    private List<UserField> ticket_field;

    public List<UserField> getTicket_field() {
        return ticket_field;
    }

    public void setTicket_field(List<UserField> ticket_field) {
        this.ticket_field = ticket_field;
    }
}
