package com.sohu.focus.salesmaster.base;

/**
 * Created by yuanminjia on 2017/11/1.
 */

public class SalesConstants {

    public static final int APP_ID = 51007;

    /*request_code*/
    public static final int REQUEST_FOR_SELECT_PHOTO = 100;
    public static final int REQUEST_FOR_VIEW_PHOTO = 101;
    public static final int REQUEST_FOR_TAKE_PHOTO = 102;
    public static final int REQUEST_FOR_PROGRESS = 103;
    public static final int REQUEST_FOR_PROJECT = 104;
    public static final int REQUEST_ADD_DYNAMICS = 105;
    public static final int REQUEST_ADD_COMPETITOR = 106;
    public static final int REQUEST_SELECT_CLIENT = 107;
    public static final int REQUEST_ADD_CLIENT = 108;
    public static final int REQUEST_ADD_INVEST = 109;

    /*Preference*/
    public static final String USER_INFO = "user_info";
    public static final String USER_ID = "user_id";
    public static final String USER_VIEW_ROLE = "user_view_role";
    public static final String USER_CITY = "user_city";
    public static final String USER_ROLE = "user_role";

    /*传参*/
    public static final String EXTRA_LOAD_INFO = "EXTRA_LOAD_INFO";
    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_NAME = "extra_name";
    public static final String EXTRA_ROLE = "extra_role";
    public static final String EXTRA_AREA = "extra_area";
    public static final String EXTRA_PROGRESS = "extra_progress";
    public static final String EXTRA_PROGRESS_ID = "extra_progress_id";
    public static final String EXTRA_PROGRESS_NEED_CUSTOMER = "extra_progress_customer";
    public static final String EXTRA_PROJECT = "extra_project";
    public static final String EXTRA_PROJECT_ID = "extra_project_id";
    public static final String EXTRA_PROJECT_CLIENT_NUM = "extra_project_client_num";
    public static final String EXTRA_GALLERY_OPTION = "extra_gallery_option";
    public static final String EXTRA_DYNAMIC_ID = "extra_dynamic_id";
    public static final String EXTRA_IS_SUB = "extra_is_sub";
    public static final String EXTRA_SELECT_CLIENT_IDS = "extra_select_client_ids";
    public static final String EXTRA_SELECT_CLIENT_NAMES = "extra_select_client_names";
    public static final String EXTRA_COMPETITORS = "extra_competitors";
    public static final String EXTRA_PHOTO_PATHS = "photo_path";
    public static final String EXTRA_PHOTO_POSITION = "photo_current_position";
    public static final String EXTRA_INVEST_MONEY = "extra_invest_money";
    public static final String EXTRA_INVEST_TIME = "extra_invest_time";
    public static final String EXTRA_WEB_URL = "web_url";
    public static final String EXTRA_WEB_MORE_URL = "web_more_url";
    public static final String EXTRA_WEB_TITLE = "web_title";
    public static final String EXTRA_NEED_COOKIE = "extra_need_cookie";

    /*dialog传参数*/
    public static final String EXTRA_UPGRADE_DATA = "extra_upgrade_data";


    /*权限申请requestcode*/
    public static final int REQUEST_PERMISSION_CAMERA = 200;
    public static final int REQUEST_PERMISSION_STORAGE = 201;

    /*用户角色*/
    public static final int USER_ROLE_OPERATING = 2; //运营
    public static final int USER_ROLE_SALES = 1;  //销售

    /**
     * 项目排序方式
     */
    public static final int SORT_SCORE = 0;
    public static final int SORT_AD = 1;
    public static final int SORT_CALL = 2;

}
