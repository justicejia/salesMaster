package com.sohu.focus.salesmaster.http.model;

import com.sohu.focus.salesmaster.base.SalesConstants;

import java.io.Serializable;

/**
 * Created by yuanminjia on 2017/12/13.
 */
public class UpdateParams implements Serializable {
    private int appId = SalesConstants.APP_ID;
    private int appType;
    private String appVersion;

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public int getAppType() {
        return appType;
    }

    public void setAppType(int appType) {
        this.appType = appType;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appViersion) {
        this.appVersion = appViersion;
    }
}
