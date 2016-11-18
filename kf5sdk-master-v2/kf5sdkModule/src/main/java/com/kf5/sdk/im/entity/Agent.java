package com.kf5.sdk.im.entity;

import com.google.gson.annotations.SerializedName;

/**
 * author:chosen
 * date:2016/11/3 17:03
 * email:812219713@qq.com
 */

public class Agent {

    @SerializedName("enabled")
    private int enable;
    @SerializedName("company_id")
    private int companyId;
    @SerializedName("phone")
    private String phone;
    @SerializedName("max_serve")
    private int maxServer;
    @SerializedName("status")
    private String status;
    @SerializedName("photo")
    private String photo;
    @SerializedName("password")
    private String password;
    @SerializedName("id")
    private int id;
    @SerializedName("landline")
    private String landLine;
    @SerializedName("display_name")
    private String displayName;
    @SerializedName("created")
    private long created;
    @SerializedName("email")
    private String email;
    @SerializedName("name")
    private String name;
    @SerializedName("role")
    private String role;
    @SerializedName("qq")
    private String qq;

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getMaxServer() {
        return maxServer;
    }

    public void setMaxServer(int maxServer) {
        this.maxServer = maxServer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLandLine() {
        return landLine;
    }

    public void setLandLine(String landLine) {
        this.landLine = landLine;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }
}
