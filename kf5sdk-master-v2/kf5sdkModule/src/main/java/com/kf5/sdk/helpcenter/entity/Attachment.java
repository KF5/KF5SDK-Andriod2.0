package com.kf5.sdk.helpcenter.entity;

import com.google.gson.annotations.SerializedName;
import com.kf5.sdk.system.entity.Field;

/**
 * author:chosen
 * date:2016/10/19 16:39
 * email:812219713@qq.com
 */

public class Attachment {

    @SerializedName(Field.ID)
    private int id;
    @SerializedName(Field.NAME_TAG)
    private String name;
    @SerializedName(Field.SIZE)
    private String size;
    @SerializedName(Field.CONTENT_URL)
    private String content_url;
    @SerializedName(Field.TOKEN)
    private String token;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getContent_url() {
        return content_url;
    }

    public void setContent_url(String content_url) {
        this.content_url = content_url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
