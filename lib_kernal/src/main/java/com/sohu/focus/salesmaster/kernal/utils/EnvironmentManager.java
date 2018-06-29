package com.sohu.focus.salesmaster.kernal.utils;


import com.sohu.focus.salesmaster.kernal.BuildConfig;

/**
 * Created by qiangzhao on 2016/12/30.
 */

public class EnvironmentManager {


    /**
     * dev开发环境，开发过程中使用
     */
    public static final int DEV_ENVIRONMENT = 2;
    /**
     * qa测试环境，开发完毕会部署到qa
     */
    public static final int QA_ENVIRONMENT = 1;
    /**
     * 正式环境
     */
    public static final int OFFICIAL_ENVIRONMENT = 0;
    /**
     * 当前环境
     */
    public static int curEnvironment = DEV_ENVIRONMENT;

    /**
     * 配置当前开发环境
     */
    public static void initEnvironment() {
        switch (BuildConfig.ENV) {
            case OFFICIAL_ENVIRONMENT:
                curEnvironment = OFFICIAL_ENVIRONMENT;
                break;
            case DEV_ENVIRONMENT:
                curEnvironment = DEV_ENVIRONMENT;
                break;
            case QA_ENVIRONMENT:
                curEnvironment = QA_ENVIRONMENT;
                break;
            default:
                curEnvironment = QA_ENVIRONMENT;

        }
    }

    public static String getSalesBaseUrl() {
        String url;
        switch (curEnvironment) {
            case DEV_ENVIRONMENT:
                url = "http://productivity.focus-dev.cn/";
                break;
            case QA_ENVIRONMENT:
                url = "http://productivity.focus-test.cn/";
                break;
            case OFFICIAL_ENVIRONMENT:
                url = "http://productivity.focus.cn/";
                break;
            default:
                url = "http://productivity.focus-test.cn/";
                break;
        }
        return url;
    }


    /**
     * 获取登录wap的url
     */
    public static String getLoginUrl() {
        String url;
        switch (curEnvironment) {
            case DEV_ENVIRONMENT:
                url = "https://sso.sohu-inc.com/login?appid=7a8499fb-63c4-4e5b-bee5-4cd369dd8769&ru=http://shengtai-audit.focus-dev.cn";
                break;
            case QA_ENVIRONMENT:
                url = "https://sso.sohu-inc.com/login?appid=8dfa1b77-46b8-409d-a74f-bfe250932471&ru=http://shengtai-audit.focus-test.cn";
                break;
            case OFFICIAL_ENVIRONMENT:
                url = "https://sso.sohu-inc.com/login?appid=7a8499fb-63c4-4e5b-bee5-4cd369dd8769&ru=http://shengtai-audit.focus.cn";
                break;
            default:
                url = "https://sso.sohu-inc.com/login?appid=8dfa1b77-46b8-409d-a74f-bfe250932471&ru=http://shengtai-audit.focus-test.cn";
                break;
        }
        return url;
    }


    public static String getBuildBaseUrl() {
        String url;
        switch (curEnvironment) {
            case DEV_ENVIRONMENT:
                url = "https://house-sv-base.focus-dev.cn";
                break;
            case QA_ENVIRONMENT:
                url = "https://house-sv-base.focus-test.cn";
                break;
            case OFFICIAL_ENVIRONMENT:
                url = "https://house-sv-base.focus.cn";
                break;
            default:
                url = "https://house-sv-base.focus-test.cn";
                break;
        }
        return url;
    }

    public static String getUserDomain() {
        String domain;
        switch (curEnvironment) {
            case DEV_ENVIRONMENT:
                domain = "http://u.focus-dev.cn/";
                break;
            case QA_ENVIRONMENT:
                domain = "http://u.focus-test.cn/";
                break;
            case OFFICIAL_ENVIRONMENT:
                domain = "http://u.focus.cn/";
                break;
            default:
                domain = "http://u.f-dev.cn/";
                break;
        }
        return domain;
    }


}
