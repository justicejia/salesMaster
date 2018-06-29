package com.sohu.focus.salesmaster.kernal.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sohu.focus.salesmaster.kernal.BaseApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * preference manager
 * Created by xiufengwang on 2016/12/16.
 */

public class PreferenceManager {

    private static final String USER_NAME = "focus_live_preference";

    private static PreferenceManager mPreferenceManager;
    private Context mContext;
    private SharedPreferences mPreference;

    private PreferenceManager() {

    }

    private PreferenceManager(Context context) {
        this.mContext = context;
        mPreference = context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized  PreferenceManager getInstance() {
        if (mPreferenceManager == null) {
            mPreferenceManager = new PreferenceManager(BaseApplication.getApplication());
        }
        return mPreferenceManager;
    }


//    public static synchronized PreferenceManager getInstance(Context context) {
//
//        if (mPreferenceManager == null) {
//            mPreferenceManager = new PreferenceManager(context);
//        }
//        return mPreferenceManager;
//
//    }


    public void clearData() {
        mPreference.edit().clear().apply();
    }


    public void saveData(String key, String value) {
        mPreference.edit().putString(key, value).apply();
    }


    public void saveData(String key, int value) {
        mPreference.edit().putInt(key, value).apply();
    }


    public void saveData(String key, long value) {
        mPreference.edit().putLong(key, value).apply();
    }


    public void saveData(String key, boolean value) {
        mPreference.edit().putBoolean(key, value).apply();
    }


    public String getStringData(String key, String defaultValue) {
        return mPreference.getString(key, defaultValue);
    }


    public boolean getBoolData(String key, boolean defaultValue) {
        return mPreference.getBoolean(key, defaultValue);
    }


    public int getIntData(String key, int defaultValue) {
        return mPreference.getInt(key, defaultValue);
    }


    public long getLongData(String key, long defaultValue) {
        return mPreference.getLong(key, defaultValue);
    }

    public void removeData(String key){
        if(containsKey(key)){
            mPreference.edit().remove(key).apply();
        }
    }


    public boolean containsKey(String key) {

        return mPreference.contains(key);
    }

    /**
     * 针对复杂类型对象存储<对象>
     *
     * @param key
     * @param object
     */
    public void setObject(String key, Object object){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream ous = null;
        try {
            ous = new ObjectOutputStream(baos);
            ous.writeObject(object);
            String objectVal = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            mPreference.edit().putString(key, objectVal).apply();
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

    /**
     * 获取存储的复杂对象<对象>
     * @param key
     * @param clazz
     */
    @SuppressWarnings("unchecked")
    public <T> T getObject(String key, Class<T> clazz){
        if (mPreference.contains(key)) {
            String objectVal = mPreference.getString(key, null);
            byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(bais);
                return (T)ois.readObject();
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

    /**
     * 获取列表数据（通过json转化）
     */
    public <T> T getListObject(String key, TypeReference<T> valueType) {
        String jsonArray = getStringData(key, "");
        return CommonUtils.jsonArrayToList(jsonArray, valueType);
    }

    /**
     * 保存列表数据（通过json转化）
     */
    public <T> void saveListObject(String key, List<T> list) {
        String jsonArray = CommonUtils.objectToJsonString(list);
        saveData(key, jsonArray);
    }
}
