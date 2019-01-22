package com.kf5.sdk.ticket.mvp.model;

import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.ticket.api.TicketAPI;
import com.kf5.sdk.ticket.mvp.model.api.IRatingModel;

import java.util.Map;

/**
 * author:chosen
 * date:2017/1/4 18:25
 * email:812219713@qq.com
 */

public class RatingModel implements IRatingModel {
    @Override
    public void onRating(Map<String, String> map, HttpRequestCallBack httpRequestCallBack) {
        TicketAPI.getInstance().rating(map, httpRequestCallBack);
    }
}
