package com.kf5.sdk.im.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.kf5.sdk.system.entity.Field;

/**
 * author:chosen
 * date:2017/12/7 14:55
 * email:812219713@qq.com
 */

public class SelectAgentGroupItem implements Parcelable {

    @SerializedName(Field.TITLE)
    private String title;
    @SerializedName(Field.DESCRIPTION)
    private String description;
    @SerializedName(Field.KEY)
    private int key;

    public SelectAgentGroupItem() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeInt(this.key);
    }

    protected SelectAgentGroupItem(Parcel in) {
        this.title = in.readString();
        this.description = in.readString();
        this.key = in.readInt();
    }

    public static final Creator<SelectAgentGroupItem> CREATOR = new Creator<SelectAgentGroupItem>() {
        @Override
        public SelectAgentGroupItem createFromParcel(Parcel source) {
            return new SelectAgentGroupItem(source);
        }

        @Override
        public SelectAgentGroupItem[] newArray(int size) {
            return new SelectAgentGroupItem[size];
        }
    };
}
