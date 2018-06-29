package com.sohu.focus.salesmaster.kernal.log;


/**
 * Logger
 * 默认logcat展示所有类型的log，file存储{@link FocusLog#FILE_LOG_LEVEL}级别及以上的
 * <p>
 * Created by qiangzhao on 2016/11/10.
 */

public class Logger extends FocusLogWrapper {

    public static final String ZQ = "@qiangzhao@";
    public static final String WXF = "@xiufengwang@";
    public static final String ZZ = "@MorphuesZee@";
    public static final String GZH = "@zhanhaigao@";
    public static final String ZQH = "@qinghuazhang@";
    public static final String QJQ = "@junqingqi";
    public static final String JYM = "@jiayuanmin@";
    public static final String ZX = "@luckyzhangx@";

    public Logger(String name) {
        super(name);
    }

    public static FocusLog ZQ() {
        return getLogger(ZQ);
    }

    public static FocusLog ZZ() {
        return getLogger(ZZ);
    }

    public static FocusLog WXF() {
        return getLogger(WXF);
    }

    public static FocusLog GZH() {
        return getLogger(GZH);
    }

    public static FocusLog ZQH() {
        return getLogger(ZQH);
    }

    public static FocusLog QJQ() {
        return getLogger(QJQ);
    }

    public static FocusLog JYM() {
        return getLogger(JYM);
    }

    public static FocusLog ZX() {
        return getLogger(ZX);
    }
}
