package com.kf5.sdk.ticket.mvp.model;

/**
 * author:chosen
 * date:2016/10/20 13:52
 * email:812219713@qq.com
 */

public class TicketModelManager {

    public static TicketListModel provideTicketListModel() {
        return new TicketListModel();
    }

    public static TicketDetailModel provideTicketDetailModel() {
        return new TicketDetailModel();
    }

    public static TicketAttributeModel provideTicketAttributeModel() {
        return new TicketAttributeModel();
    }
    public static TicketFeedBackModel provideTicketFeedBackModel() {
        return new TicketFeedBackModel();
    }
    public static RatingModel provideRatingModel() {
        return new RatingModel();
    }

}
