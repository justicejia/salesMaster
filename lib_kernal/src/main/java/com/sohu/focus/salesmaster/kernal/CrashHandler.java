package com.sohu.focus.salesmaster.kernal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.sohu.focus.salesmaster.kernal.log.Logger;
import com.sohu.focus.salesmaster.kernal.log.core.LogLevel;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 捕获崩溃日志
 * Created by zhaoqiang on 2017/9/4.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";

    private static final CrashHandler INSTANCE = new CrashHandler();
    private Thread.UncaughtExceptionHandler defaultHandler;// 系统默认的UncaughtException处理类
    private Map<String, String> infosMap = new HashMap<>(); // 用来存储设备信息和异常信息

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     */
    public void init() {
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();// 获取系统默认的UncaughtException处理器
        Thread.setDefaultUncaughtExceptionHandler(this);// 设置当前CrashHandler为程序的默认处理器
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && defaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            defaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Logger.ZQ().e(TAG, "exception : ", e);
                e.printStackTrace();
            }
            // 杀死进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return 如果处理了该异常信息, 返回true;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        ToastUtil.toast("正在保存崩溃日志，即将退出！");
        collectDeviceInfo(BaseApplication.getApplication());// 收集设备参数信息
        saveCrashInfoToFile(ex);// 保存日志文件

        return true;
    }

    /**
     * 收集设备信息
     *
     * @param context
     */
    public void collectDeviceInfo(Context context) {
        // 使用包管理器获取信息
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "" : pi.versionName;// 版本名;若versionName==null，则="null"；否则=versionName
                String versionCode = pi.versionCode + "";// 版本号
                infosMap.put("versionName", versionName);
                infosMap.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Logger.ZQ().e(TAG, "an NameNotFoundException occured when collect package info");
            e.printStackTrace();
        }

        // 使用反射获取获取系统的硬件信息
        Field[] fields = Build.class.getDeclaredFields();// 获得某个类的所有申明的字段，即包括public、private和proteced，
        for (Field field : fields) {
            field.setAccessible(true);// 暴力反射 ,获取私有的信息;类中的成员变量为private,故必须进行此操作
            try {
                infosMap.put(field.getName(), field.get(null).toString());
                Logger.ZQ().d(TAG, field.getName() + " : " + field.get(null));
            } catch (IllegalArgumentException e) {
                Logger.ZQ().e(TAG, "an IllegalArgumentException occured when collect reflect field info", e);
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                Logger.ZQ().e(TAG, "an IllegalAccessException occured when collect reflect field info", e);
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称
     */
    @SuppressLint("CommitPrefEdits")
    private void saveCrashInfoToFile(Throwable ex) {
        // 字符串流
        StringBuilder stringBuffer = new StringBuilder();

        // 获得设备信息
        for (Map.Entry<String, String> entry : infosMap.entrySet()) {// 遍历map中的值
            String key = entry.getKey();
            String value = entry.getValue();
            stringBuffer.append(key).append("=").append(value).append("\n");
        }

        // 获得错误信息
        Writer writer = new StringWriter();// 这个writer下面还会用到，所以需要它的实例
        PrintWriter printWriter = new PrintWriter(writer);// 输出错误栈信息需要用到PrintWriter
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {// 循环，把所有的cause都输出到printWriter中
            cause.printStackTrace(printWriter);
            cause = ex.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        stringBuffer.append(result);
        BaseApplication.globalFilePrinter.println(LogLevel.ERROR, TAG, stringBuffer.toString());
    }

}
