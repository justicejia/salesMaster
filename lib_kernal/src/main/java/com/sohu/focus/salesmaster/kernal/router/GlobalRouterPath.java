package com.sohu.focus.salesmaster.kernal.router;

/**
 * 全局路径
 * Created by zhaoqiang on 2017/8/24.
 */

public final class GlobalRouterPath {

    /*----------Main---------*/
    public static final class MAIN_GROUP {
        //MainActivity
        public static final String MAIN = "/main/main";
        //ChooseCityActivity
        public static final String CITY = "/main/city";
        //PublisherRapidCertificationActivity
        public static final String RAPID_CERTIFY = "/live/rapid_certify";
        //BuildDetailActivity
        public static final String BUILD_DETAIL = "/main/build_detail";
        //LoginNativeActivity
        public static final String LOGIN = "/main/login";
        //UserProfileActivity
        public static final String PROFILE = "/main/profile";
        //SplashActivity
        public static final String SPLASH = "/main/splash";
        //ShareQRCodeActivity
        public static final String SHARE_QR = "/main/share_qr";
        //FocusWebViewActivity
        public static final String WEB_VIEW = "/main/web_view";
        //FocusSearchActivity
        public static final String SEARCH = "/main/search";
    }

    /*----------Live---------*/
    public static final class LIVE_GROUP {
        //LiveHomeActivity
        public static final String MAIN = "/live/main";
        //LivePlayerActivity
        public static final String LIVE_PLAYER = "/live/live_player";
        //ForeshowPorActivity
        public static final String FORE_SHOW = "/live/fore_show";
        //PublishSettingActivity
        public static final String PUBLISH_SETTING = "/live/publish_setting";
    }

}
