package com.chosen.imageviewer.bean;

import java.io.Serializable;

/**
 * @author Chosen
 * @create 2018/12/17 10:34
 * @email 812219713@qq.com
 */
public class ImageInfo implements Serializable {

    private String thumbnailUrl;//缩略图
    private String originUrl;//原图或者高清图


    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getOriginUrl() {
        return originUrl;
    }

    public void setOriginUrl(String originUrl) {
        this.originUrl = originUrl;
    }

    @Override
    public String toString() {
        return "ImageInfo{"
                + "thumbnailUrl='"
                + thumbnailUrl
                + '\''
                + ", originUrl='"
                + originUrl
                + '\''
                + '}';
    }
}
