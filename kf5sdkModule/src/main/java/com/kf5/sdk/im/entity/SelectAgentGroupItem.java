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
    @SerializedName(Field.GROUP_ID)
    private int groupId;
    @SerializedName(Field.DESCRIPTION)
    private String description;
    @SerializedName(Field.AGENT_IDS)
    private String agentIds;

    public SelectAgentGroupItem() {
    }

    public SelectAgentGroupItem(String title, int groupId, String description, String agentIds) {
        this.title = title;
        this.groupId = groupId;
        this.description = description;
        this.agentIds = agentIds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAgentIds() {
        return agentIds;
    }

    public void setAgentIds(String agentIds) {
        this.agentIds = agentIds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeInt(this.groupId);
        dest.writeString(this.description);
        dest.writeString(this.agentIds);
    }

    protected SelectAgentGroupItem(Parcel in) {
        this.title = in.readString();
        this.groupId = in.readInt();
        this.description = in.readString();
        this.agentIds = in.readString();
    }

    public static final Parcelable.Creator<SelectAgentGroupItem> CREATOR = new Parcelable.Creator<SelectAgentGroupItem>() {
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
