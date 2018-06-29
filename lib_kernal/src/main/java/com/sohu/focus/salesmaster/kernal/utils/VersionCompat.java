package com.sohu.focus.salesmaster.kernal.utils;

import android.os.Build;

/**
 * Created by jiayuanmin on 2018/5/18
 * description:
 */
public class VersionCompat {

    private VersionCompat (){}

    public static boolean isAtLeastO() {
        return Build.VERSION.SDK_INT >= 26;
    }

    public static boolean isAtLeastN() {
        return Build.VERSION.SDK_INT >= 24;
    }

    public static boolean isAtLeastM() {
        return Build.VERSION.SDK_INT >= 23;
    }

    public static boolean isAtLeastLOLLIPOP() {
        return Build.VERSION.SDK_INT >= 21;
    }

    public static boolean isAtLeastKITKAT() {
        return Build.VERSION.SDK_INT >= 19;
    }

    public static boolean isAtLeastJELLY_BEAN_MR1() {
        return Build.VERSION.SDK_INT >= 17;
    }

    public static boolean isAtLeastJELLY_BEAN() {
        return Build.VERSION.SDK_INT >= 16;
    }

    public static boolean isBelowKITKAT() {
        return Build.VERSION.SDK_INT < 19;
    }

    public static boolean isBelowM() {
        return Build.VERSION.SDK_INT < 23;
    }

    public static boolean isBelowLOLLIPOP() {
        return Build.VERSION.SDK_INT < 21;
    }

    public static boolean isBelowN() {
        return Build.VERSION.SDK_INT < 24;
    }

    public static boolean isBelowJELLY_BEAN_MR2() {
        return Build.VERSION.SDK_INT < 18;
    }

    public static boolean isBelowO() {
        return Build.VERSION.SDK_INT < 26;
    }
}
