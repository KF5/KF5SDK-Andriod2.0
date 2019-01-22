package com.kf5.sdk.im.keyboard.data;

/**
 * author:chosen
 * date:2016/11/1 16:04
 * email:812219713@qq.com
 */

public class EmoticonEntity {

    private long mEventType;
    private String mIconUri;
    private String mContent;

    public long getEventType() {
        return mEventType;
    }

    public void setEventType(long eventType) {
        this.mEventType = eventType;
    }

    public String getIconUri() {
        return mIconUri;
    }

    public void setIconUri(String iconUri) {
        this.mIconUri = iconUri;
    }

    public void setIconUri(int iconUri) {
        this.mIconUri = "" + iconUri;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        this.mContent = content;
    }

    public EmoticonEntity(long eventType, String iconUri, String content) {
        this.mEventType = eventType;
        this.mIconUri = iconUri;
        this.mContent = content;
    }

    public EmoticonEntity(String iconUri, String content) {
        this.mIconUri = iconUri;
        this.mContent = content;
    }

    public EmoticonEntity(String content) {
        this.mContent = content;
    }

    public EmoticonEntity() { }
}
