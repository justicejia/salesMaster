package com.sohu.focus.salesmaster.kernal.log.core.internal;

import android.annotation.SuppressLint;
import android.os.Build;

import com.sohu.focus.salesmaster.kernal.log.core.printer.AndroidPrinter;
import com.sohu.focus.salesmaster.kernal.log.core.printer.ConsolePrinter;
import com.sohu.focus.salesmaster.kernal.log.core.printer.Printer;


/**
 * Created by zhaoqiang on 2017/6/14.
 */

public class Platform {

    private static final Platform PLATFORM = findPlatform();

    public static Platform get() {
        return PLATFORM;
    }

    @SuppressLint("NewApi")
    String lineSeparator() {
        return System.lineSeparator();
    }

    Printer defaultPrinter() {
        return new ConsolePrinter();
    }

    public void warn(String msg) {
        System.out.println(msg);
    }

    private static Platform findPlatform() {
        try {
            Class.forName("android.os.Build");
            if (Build.VERSION.SDK_INT != 0) {
                return new Android();
            }
        } catch (ClassNotFoundException ignored) {
        }
        return new Platform();
    }

    static class Android extends Platform {
        @Override
        String lineSeparator() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                return "\n";
            }
            return System.lineSeparator();
        }

        @Override
        Printer defaultPrinter() {
            return new AndroidPrinter();
        }

        @Override
        public void warn(String msg) {
            android.util.Log.w("LogCore", msg);
        }
    }
}
