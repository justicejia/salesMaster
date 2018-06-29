package com.sohu.focus.salesmaster.kernal;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sohu.focus.salesmaster.kernal.http.HttpEngine;
import com.sohu.focus.salesmaster.kernal.imageloader.FocusImageLoader;
import com.sohu.focus.salesmaster.kernal.log.FocusLog;
import com.sohu.focus.salesmaster.kernal.log.core.LogConfiguration;
import com.sohu.focus.salesmaster.kernal.log.core.LogCore;
import com.sohu.focus.salesmaster.kernal.log.core.LogLevel;
import com.sohu.focus.salesmaster.kernal.log.core.flattener.ClassicFlattener;
import com.sohu.focus.salesmaster.kernal.log.core.printer.AndroidPrinter;
import com.sohu.focus.salesmaster.kernal.log.core.printer.ILifecyclePrinter;
import com.sohu.focus.salesmaster.kernal.log.core.printer.Printer;
import com.sohu.focus.salesmaster.kernal.log.core.printer.XLogPrinter;
import com.sohu.focus.salesmaster.kernal.log.core.printer.file.FilePrinter;
import com.sohu.focus.salesmaster.kernal.log.core.printer.file.naming.DateFileNameGenerator;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.kernal.utils.EnvironmentManager;
import com.sohu.focus.salesmaster.kernal.utils.NetUtil;
import com.sohu.focus.salesmaster.kernal.utils.StorageUtil;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.io.File;

import static com.sohu.focus.salesmaster.kernal.log.FocusLog.FILE_LOG_LEVEL;
import static com.sohu.focus.salesmaster.kernal.log.FocusLog.isUseXLog;
import static com.sohu.focus.salesmaster.kernal.log.core.LogConfiguration.LOG_MODE_PRINTLN;
import static com.sohu.focus.salesmaster.kernal.log.core.LogConfiguration.LOG_MODE_PRINTLN2;
import static com.sohu.focus.salesmaster.kernal.utils.StorageUtil.DEFAULT_LOG_DIR;

/**
 * Created by zhaoqiang on 2017/8/2.
 */

public class BaseApplication extends Application {

    //打印文件log的全局对象
    public static ILifecyclePrinter globalFilePrinter;
    private static BaseApplication app;
    //LeakCanary的观察对象
    private RefWatcher refWatcher;
    private int count = 0;
    /**
     * APP是否在后台
     */
    private boolean isInBackground = false;

    public static BaseApplication getApplication() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String processName = CommonUtils.getProcessName(this, android.os.Process.myPid());
        //app存在多进程，会导致多次执行oncreate
        if (processName == null || processName.equals("com.sohu.focus.salesmaster")) {
            app = this;
            initRouter();
            initLog();
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return;
            }
            refWatcher = LeakCanary.install(this);
            EnvironmentManager.initEnvironment();
            registerAppStatus();
//            if(FocusLog.isDebugging)
//                CrashHandler.getInstance().init();
            NetUtil.getInstance();
            onSubCreate();
        }
    }

    private void initLog() {
        LogConfiguration config = new LogConfiguration.Builder()
                .logLevel(BuildConfig.IS_DEBUG ? LogLevel.ALL             // Specify log level, logs below this level won't be printed, default: LogLevel.ALL
                        : FILE_LOG_LEVEL)
                .tag(FocusLog.TAG)                   // Specify TAG, default: "FocusLive"
                .mode(isUseXLog ? LOG_MODE_PRINTLN2 : LOG_MODE_PRINTLN)
                .build();
        if (!isUseXLog) {
            Printer androidPrinter = new AndroidPrinter();             // Printer that print the log using android.util.Log
            ILifecyclePrinter filePrinter = new FilePrinter                      // Printer that print the log to the file system
                    .Builder(new File(StorageUtil.getCacheDir(), DEFAULT_LOG_DIR).getPath())       // Specify the path to save log file
                    .fileNameGenerator(new DateFileNameGenerator())        // Default: ChangelessFileNameGenerator("log")
                    // .backupStrategy(new MyBackupStrategy())             // Default: FileSizeBackupStrategy(1024 * 1024)
                    .logFlattener(new ClassicFlattener())                  // Default: DefaultFlattener
                    .lowestLogLevel(FILE_LOG_LEVEL)
                    .build();
            LogCore.init(                                                 // Initialize LogCore
                    config,                                              // Specify printers, if no printer is specified, AndroidPrinter(for Android)/ConsolePrinter(for java) will be used.
                    androidPrinter,
                    filePrinter);
            globalFilePrinter = filePrinter;
        } else {
            ILifecyclePrinter xlogPrinter = new XLogPrinter
                    .Builder(new File(StorageUtil.getCacheDir(), DEFAULT_LOG_DIR).getPath())
                    .lowestLogLevel(FILE_LOG_LEVEL)
                    .build();
            globalFilePrinter = xlogPrinter;
            LogCore.init(                                                 // Initialize LogCore
                    config,                                              // Specify printers, if no printer is specified, AndroidPrinter(for Android)/ConsolePrinter(for java) will be used.
                    xlogPrinter);
            globalFilePrinter = xlogPrinter;
        }


    }

    private void initRouter() {
        if (FocusLog.isDebugging) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(app); // 尽可能早，推荐在Application中初始化
    }

    private void registerAppStatus() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            private int activityCount = 0;
            private int activityInstanceCount = 0;

            @Override
            public void onActivityStopped(Activity activity) {
                count--;
                if (count == 0) {
                    onBackGround();
                }

                activityCount--;
                if (activityCount < 1) {
                    isInBackground = true;
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (count == 0) {
                    onForeGround();
                }
                count++;

                activityCount++;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                activityInstanceCount--;
                if (activityInstanceCount < 1) {
                    isInBackground = false;
                }
            }

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activityInstanceCount++;
            }
        });
    }

    public boolean isInBackground() {
        return isInBackground;
    }

    public RefWatcher getRefWatcher(Context context) {
        BaseApplication application = (BaseApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level >= ComponentCallbacks2.TRIM_MEMORY_MODERATE) {
            FocusImageLoader.getLoader(this).clear();
        }
    }

    public void freeMemory() {
        FocusImageLoader.getLoader(this).clear();
        System.gc();
    }

    public ILifecyclePrinter getGlobalPrinter() {
        return globalFilePrinter;
    }

    protected void exit() {
        HttpEngine.cancelAll();
        if (globalFilePrinter != null)
            globalFilePrinter.close();
    }

    protected void onSubCreate() {

    }

    protected void onForeGround() {

    }

    protected void onBackGround() {

    }
}
