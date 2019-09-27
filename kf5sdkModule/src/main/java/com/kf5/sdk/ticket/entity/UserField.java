package com.kf5.sdk.ticket.entity;

import com.google.gson.annotations.SerializedName;
import com.kf5.sdk.system.entity.Field;

/**
 * author:chosen
 * date:2016/10/21 15:35
 * email:812219713@qq.com
 */

public class UserField {

    @SerializedName(Field.NAME)
    private String name;

    @SerializedName(Field.VALUE)
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
