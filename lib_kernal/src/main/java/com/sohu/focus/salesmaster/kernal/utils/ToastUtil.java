package com.sohu.focus.salesmaster.kernal.utils;

import android.os.Handler;
import android.widget.Toast;

import com.sohu.focus.salesmaster.kernal.BaseApplication;
import com.sohu.focus.salesmaster.kernal.log.Logger;


/**
 * Toast工具类
 */
public class ToastUtil {
    protected static Toast toast = null;
    private static String oldMsg = "";
    private static long oneTime = 0;
    private static long twoTime = 0;

    public static void toast(String msg) {
        makeToast(msg, Toast.LENGTH_SHORT);
    }

    public static void longToast(String msg) {
        makeToast(msg, Toast.LENGTH_LONG);
    }

    private static void makeToast(String msg, int time) {
        try {
            if (toast == null) {
                toast = Toast.makeText(BaseApplication.getApplication(),
                        msg, time);
                toast.setText(msg);
                oldMsg = msg;
                oneTime = System.currentTimeMillis();
                show();
            } else {
                twoTime = System.currentTimeMillis();
                if (msg.equals(oldMsg)) {
                    if (twoTime - oneTime > time) {
                        toast.setText(msg);
                        show();
                    }
                } else {
                    oldMsg = msg;
                    toast.setText(msg);
                    show();
                }
            }
            oneTime = twoTime;
        } catch (Exception e) {
            Logger.ZQ().e("make toast failed  : " + e.getMessage());
        }
    }

    private static void show() {
        Handler handler = new Handler(BaseApplication.getApplication().getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                toast.show();
            }
        });
    }


}
