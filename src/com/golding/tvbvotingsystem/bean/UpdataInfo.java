package com.golding.tvbvotingsystem.bean;

/**
 * Created by Jallen on 2017/12/22 0022 14:43.
 */

public class UpdataInfo {
    private String update;
    private String apkVersion;
    private String apkUrl;
    private String sysyemVersion;
    private String sysyemUrl;
    private String description;

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getApkVersion() {
        return apkVersion;
    }

    public void setApkVersion(String apkVersion) {
        this.apkVersion = apkVersion;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getSysyemVersion() {
        return sysyemVersion;
    }

    public void setSysyemVersion(String sysyemVersion) {
        this.sysyemVersion = sysyemVersion;
    }

    public String getSysyemUrl() {
        return sysyemUrl;
    }

    public void setSysyemUrl(String sysyemUrl) {
        this.sysyemUrl = sysyemUrl;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
