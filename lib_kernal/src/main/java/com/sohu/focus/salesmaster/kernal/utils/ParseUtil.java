package com.sohu.focus.salesmaster.kernal.utils;

import java.math.BigDecimal;

/**
 * Created by qiangzhao on 2016/8/24.
 */
public class ParseUtil {

    public static int parseInt(String value, int errorValue) {
        int i = 0;
        try {
            i = Integer.parseInt(value);
        } catch (Exception e) {
            e.printStackTrace();
            return errorValue;
        }
        return i;
    }

    public static long parseLong(String value, long errorValue) {
        long i = 0;
        try {
            i = Long.parseLong(value);
        } catch (Exception e) {
            e.printStackTrace();
            return errorValue;
        }
        return i;
    }

    public static short parseShort(String value, short errorValue) {
        short i = 0;
        try {
            i = Short.parseShort(value);
        } catch (Exception e) {
            e.printStackTrace();
            return errorValue;
        }
        return i;
    }

    public static float parseFloat(String value, float errorValue) {
        float i = 0;
        try {
            i = Float.parseFloat(value);
        } catch (Exception e) {
            e.printStackTrace();
            return errorValue;
        }
        return i;
    }

    public static double parseDouble(String value, double errorValue) {
        double i = 0;
        try {
            i = Double.parseDouble(value);
        } catch (Exception e) {
            e.printStackTrace();
            return errorValue;
        }
        return i;
    }

    public static float parseFloat(String value, int digit, float errorValue) {
        float i = 0;
        try {
            i = Float.parseFloat(value);
            BigDecimal b = new BigDecimal(i);
            i = b.setScale(digit, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().floatValue();
        } catch (Exception e) {
            e.printStackTrace();
            return errorValue;
        }
        return i;
    }

    public static double parseDouble(String value, int digit, double errorValue) {
        double i = 0;
        try {
            i = Double.parseDouble(value);
            BigDecimal b = new BigDecimal(i);
            i = b.setScale(digit, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
            return errorValue;
        }
        return i;
    }
}
