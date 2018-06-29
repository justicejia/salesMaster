package com.sohu.focus.salesmaster.kernal;

/**
 * Created by zhaoqiang on 2017/8/2.
 */

public class KernalConstants {

    //网络类型
    public static final int NETTYPE_WIFI = 0;
    public static final int NETTYPE_NONE = 1;
    public static final int NETTYPE_2G = 2;
    public static final int NETTYPE_3G = 3;
    public static final int NETTYPE_4G = 4;
    //网络错误提示语
    public static final String ERROR_MSG_NET_DISCONNECTED = "网络不给力，请重试";
    public static final String ERROR_MSG_NETWORK_ERROR = "服务器开小差了,请稍后再试";
    //网络成功状态吗,适应本项目
    public static final int BUSINESS_STATUS_CODE_SUCCESS = 0;
    public static final int BUILD_STATUS_CODE_SUCCESS = 0;
    //用户未登录
    public static final int BUSINESS_STATUS_USER_NOT_LOGIN = 100101;
    //用户cookie过期
    public static final int BUSINESS_STATUS_USER_TOKEN_EXPIRED = 100102;
    //检查更新
    public final static String SHAREPREFRENCE = "auto_app_info";
    public final static String SHAREPREFRENCE_CHECK_UPDATE_LAST_TIME = "check_update_last_time";
}
