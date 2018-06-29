package com.sohu.focus.salesmaster.login;


import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.kernal.http.BaseApi;
import com.sohu.focus.salesmaster.kernal.http.cookie.CookieStore;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.kernal.utils.NetUtil;
import com.sohu.focus.salesmaster.kernal.utils.PreferenceManager;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Created by yuanminjia on 2017/11/1.
 */

public enum AccountManager {

    INSTANCE;

    public boolean isLogin() {
        HttpUrl httpUrl = HttpUrl.parse(BaseApi.BASE_URL);
        CookieStore cookieStore = NetUtil.getInstance().getCookieJar().getCookieStore();
        List<Cookie> cookies = cookieStore.getCookie(httpUrl);
        if (CommonUtils.notEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                if (cookie != null && CommonUtils.notEmpty(cookie.name())) {
                    return CommonUtils.notEmpty(cookie.value());
                }
            }
        }
        return false;
    }

    /**
     * 保存用户是否为全国属性
     *
     * @param isCountry
     */
    public void saveUserIsWholeCountry(boolean isCountry) {
        PreferenceManager.getInstance().saveData(SalesConstants.USER_CITY, isCountry);
    }

    public boolean isUserWholeCountry() {
        return PreferenceManager.getInstance().getBoolData(SalesConstants.USER_CITY, false);
    }

    public void saveUserId(String id) {
        PreferenceManager.getInstance().saveData(SalesConstants.USER_ID, id);
    }

    public void saveUserViewRoles(boolean view) {
        PreferenceManager.getInstance().saveData(SalesConstants.USER_VIEW_ROLE, view);
    }

    public boolean getUserViewRole() {
        return PreferenceManager.getInstance().getBoolData(SalesConstants.USER_VIEW_ROLE, false);
    }

    public String getUserId() {
        return PreferenceManager.getInstance().getStringData(SalesConstants.USER_ID, "");
    }

    public void saveUserRole(int role) {
        PreferenceManager.getInstance().saveData(SalesConstants.USER_ROLE, role);
    }

    public int getUserRole() {
        return PreferenceManager.getInstance().getIntData(SalesConstants.USER_ROLE, 1);
    }

    public void logOut() {
        HttpEngine.getInstance().clearCache();
        PreferenceManager.getInstance().clearData();
        NetUtil.getInstance().cleanCookie();
    }

}
