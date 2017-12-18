package com.kf5.sdk.helpcenter.entity;

import com.kf5.sdk.system.entity.MultiPageEntity;

import java.util.List;

/**
 * author:chosen
 * date:2017/3/6 14:47
 * email:812219713@qq.com
 */

public class HelpCenterItemForum extends MultiPageEntity {

    private List<HelpCenterItem> forums;

    public List<HelpCenterItem> getForums() {
        return forums;
    }

    public void setForums(List<HelpCenterItem> forums) {
        this.forums = forums;
    }
}
