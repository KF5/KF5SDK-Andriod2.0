package com.kf5.sdk.ticket.mvp.usecase;

import com.kf5.sdk.ticket.mvp.model.TicketModelManager;

/**
 * author:chosen
 * date:2016/10/20 13:53
 * email:812219713@qq.com
 */

public class TicketUseCaseManager {

    public static TicketListCase provideTicketListCase() {
        return new TicketListCase(TicketModelManager.provideTicketListModel());
    }

    public static TicketDetailCase provideTicketDetailCase() {
        return new TicketDetailCase(TicketModelManager.provideTicketDetailModel());
    }

    public static TicketAttributeCase provideTicketAttributeCase() {
        return new TicketAttributeCase(TicketModelManager.provideTicketAttributeModel());
    }

    public static TicketFeedBackCase provideTicketFeedBackCase() {
        return new TicketFeedBackCase(TicketModelManager.provideTicketFeedBackModel());
    }

    public static RatingCase provideRatingCase() {
        return new RatingCase(TicketModelManager.provideRatingModel());
    }

}
