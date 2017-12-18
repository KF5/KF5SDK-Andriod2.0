package com.kf5.sdk.helpcenter.entity;

import com.kf5.sdk.system.entity.MultiPageEntity;

import java.util.List;

/**
 * author:chosen
 * date:2017/3/6 12:17
 * email:812219713@qq.com
 */

public  class HelpCenterItemCategory extends MultiPageEntity {

    private List<HelpCenterItem> categories;

    public List<HelpCenterItem> getCategories() {
        return categories;
    }

    public void setCategories(List<HelpCenterItem> categories) {
        this.categories = categories;
    }
}
