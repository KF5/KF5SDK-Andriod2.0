package com.kf5.sdk.im.entity;

import com.google.gson.annotations.SerializedName;

/**
 * author:chosen
 * date:2016/10/25 17:50
 * email:812219713@qq.com
 */

public class Upload {

    private static final String ID = "id";

    private static final String HEIGHT = "height";

    private static final String WIDTH = "width";

    private static final String NAME = "name";

    private static final String TYPE = "type";

    private static final String URL = "url";

    private static final String SIZE = "size";

    private static final String CREATED = "created";

    @SerializedName(ID)
    private int id;
    @SerializedName(NAME)
    private String name;
    @SerializedName(SIZE)
    private int size;
    @SerializedName(CREATED)
    private int created;
    @SerializedName(TYPE)
    private String type;
    @SerializedName(URL)
    private String url;

    private String localPath;

    @SerializedName(WIDTH)
    private int width;
    @SerializedName(HEIGHT)
    private int height;

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
