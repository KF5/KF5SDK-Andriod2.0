package com.kf5.sdk.helpcenter.entity;

import com.google.gson.annotations.SerializedName;
import com.kf5.sdk.system.entity.Field;

/**
 * author:chosen
 * date:2016/10/19 10:52
 * email:812219713@qq.com
 */

public class HelpCenterItem {

    @SerializedName(Field.ID)
    private int id;

    @SerializedName(Field.TITLE)
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
