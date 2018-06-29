package com.sohu.focus.salesmaster.utils;

import android.content.Context;
import android.text.format.DateFormat;

import com.sohu.focus.salesmaster.kernal.BaseApplication;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yuanminjia on 2017/11/9.
 */

public class DateUtils {

    public static String getImMessageTime(long timeStamp) {
        if (timeStamp == 0) {
            return "";
        }
        StringBuilder time = new StringBuilder();
        StringBuilder data = new StringBuilder();
        Calendar inputTime = Calendar.getInstance();
        inputTime.setTimeInMillis(timeStamp);
        Date currenTimeZone = inputTime.getTime();

        SimpleDateFormat hm;
        if (DateFormat.is24HourFormat(BaseApplication.getApplication())) {
            hm = new SimpleDateFormat("HH:mm");
        } else {
            //时间
            int hour = inputTime.get(Calendar.HOUR_OF_DAY);
            if (hour < 12) {
                time.append("上午 ");
            } else {
                time.append("下午 ");
            }
            hm = new SimpleDateFormat("h:mm");
        }

        time.append(hm.format(currenTimeZone));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.before(inputTime)) {
            return time.toString();
        }
        //日期
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        if (calendar.before(inputTime)) {
            data.append("昨天 ");
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
            if (calendar.before(inputTime)) {
                SimpleDateFormat md = new SimpleDateFormat("M" + "月" + "d" + "日 ");
                data.append(md.format(currenTimeZone));
            } else {
                SimpleDateFormat ymd = new SimpleDateFormat("yyyy" + "年" + "MM" + "月" + "dd" + "日 ");
                data.append(ymd.format(currenTimeZone));
            }
        }
        return data.append(time).toString();

    }

    /**
     * 根据时间戳获得时间显示, 当天消息显示时间(根据系统设置显示为 24 或 12 小时制), 昨天消息显示昨天, 昨天之前的消息显示日期
     */
    public static String getSimpleTime(long timeStamp, Context context) {
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(timeStamp);
        Date date = time.getTime();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.before(time)) {
            if (DateFormat.is24HourFormat(context)) {
                SimpleDateFormat md = new SimpleDateFormat("HH:mm");
                return md.format(date);
            } else {
                StringBuilder sb = new StringBuilder();
                SimpleDateFormat md = new SimpleDateFormat("h:mm");

                int AM_PM = time.get(Calendar.AM_PM);
                String ampm = (Calendar.AM == AM_PM ? "上午" : "下午");

                return sb.append(ampm + " " + md.format(date)).toString();
            }
        }

        calendar.add(Calendar.DAY_OF_MONTH, -1);
        if (calendar.before(time)) {
            return "昨天";
        }
//        返回日期
        SimpleDateFormat md = new SimpleDateFormat("M" + "月" + "d" + "日 ");
        return md.format(date);
    }
}
