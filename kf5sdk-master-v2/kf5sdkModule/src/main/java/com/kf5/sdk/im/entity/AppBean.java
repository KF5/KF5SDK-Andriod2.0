package com.kf5.sdk.im.entity;

/**
 * author:chosen
 * date:2016/11/2 16:04
 * email:812219713@qq.com
 */

public class AppBean {

    public int sourceId;

    public String actionName;

    public AppBean(int sourceId, String actionName) {
        this.sourceId = sourceId;
        this.actionName = actionName;
    }

    public AppBean() {
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
}
