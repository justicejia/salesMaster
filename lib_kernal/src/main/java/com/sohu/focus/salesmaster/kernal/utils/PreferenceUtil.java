package com.sohu.focus.salesmaster.kernal.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;


import com.sohu.focus.salesmaster.kernal.KernalConstants;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by qiangzhao on 2016/12/22.
 */

public class PreferenceUtil {

    /**
     * 针对复杂类型对象存储<对象>
     *
     * @param key
     * @param object
     */
    public static void setObject(SharedPreferences preferences, String key, Object object){
        synchronized (preferences) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream ous = null;
            try {
                ous = new ObjectOutputStream(baos);
                ous.writeObject(object);
                String objectVal = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
                preferences.edit().putString(key, objectVal).apply();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    baos.close();
                    if (ous != null) {
                        ous.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取存储的复杂对象<对象>
     * @param key
     * @param clazz
     */
    @SuppressWarnings("unchecked")
    public static <T> T getObject(SharedPreferences preferences, String key, Class<T> clazz) {
        synchronized (preferences) {
            if (preferences.contains(key)) {
                String objectVal = preferences.getString(key, null);
                byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
                ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
                ObjectInputStream ois = null;
                try {
                    ois = new ObjectInputStream(bais);
                    T t = (T) ois.readObject();
                    return t;
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        bais.close();
                        if (ois != null) {
                            ois.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
    }

    /**
     * 记录最后一次检查更新的时间
     * @param context
     * @param dateTime
     */
    public static void setLastCheckUpdateDate(Context context, long dateTime){
        SharedPreferences preferences = context.getSharedPreferences(KernalConstants.SHAREPREFRENCE,Context.MODE_PRIVATE);
        preferences.edit().putString(KernalConstants.SHAREPREFRENCE_CHECK_UPDATE_LAST_TIME, dateTime+"").apply();
    }

    public static String getLastCheckUpdateDate(Context context){
        SharedPreferences preferences = context.getSharedPreferences(KernalConstants.SHAREPREFRENCE,Context.MODE_PRIVATE);
        return preferences.getString(KernalConstants.SHAREPREFRENCE_CHECK_UPDATE_LAST_TIME,null);
    }
}
