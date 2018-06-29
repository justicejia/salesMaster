package com.sohu.focus.salesmaster.kernal.utils;


import android.content.Context;
import android.text.format.DateFormat;

import com.sohu.focus.salesmaster.kernal.BaseApplication;
import com.sohu.focus.salesmaster.kernal.log.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * date utils
 * Created by qiangzhao on 2016/12/17.
 */

public class DateUtils {

    public static final int DAY = 24 * 60 * 60 * 1000;

    public static final int HOUR = 60 * 60 * 1000;

    public static final int MINUTES = 60 * 1000;

    public static final int SECOND = 1000;

    /**
     * 根据传入的时间戳字符串转化为自定义的日期格式（例如 yyyy/MM/dd HH:mm）
     *
     * @param milliseconds 时间戳
     * @param pattern      格式
     * @return 时间字符串
     */
    public static String stampStrToCustomDate(String milliseconds, String pattern) {
        long time = Long.parseLong(milliseconds);
        Date date = new Date(time);
        SimpleDateFormat dateFormat2 = new SimpleDateFormat(pattern);
        return dateFormat2.format(date);
    }

    /**
     * 获取当前时间
     *
     * @param milliseconds 时间戳
     * @return 字符串数组
     */
    public static String[] getPublishTime(long milliseconds) {
        Date date = new Date(milliseconds);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日");
        String day = dateFormat.format(date);
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH时");
        String hour = dateFormat2.format(date);
        SimpleDateFormat dateFormat3 = new SimpleDateFormat("mm分");
        String minute = dateFormat3.format(date);

        return new String[]{day, hour, minute};
    }

    /**
     * 获取当前时间
     *
     * @param milliseconds 时间戳
     * @return 字符串数组
     */
    public static String[] getBirthday(long milliseconds) {
        Date date = new Date(milliseconds);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年");
        String year = dateFormat.format(date);
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM月");
        String month = dateFormat2.format(date);
        SimpleDateFormat dateFormat3 = new SimpleDateFormat("dd日");
        String day = dateFormat3.format(date);

        return new String[]{year, month, day};
    }

    /**
     * 获取当前时间
     *
     * @param milliseconds 时间戳
     * @return 字符串数组
     */
    public static String getTime(long milliseconds) {
        Date date = new Date(milliseconds);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        return dateFormat.format(date);
    }

    public static long getTimeMillisByTimeStr(String time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
        long s = 0;
        try {
            s = df.parse(time).getTime();
        } catch (ParseException e) {
            Logger.ZQ().e("时间转换失败！" + CommonUtils.getDataNotNull(time));
            s = System.currentTimeMillis();
        }
        return s;
    }

    public static String getTimeByFormat(long milliseconds, String format) {
        Date date = new Date(milliseconds);
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static String getIncomingTime(long milliseconds) {
        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        String time = timeStampToDate(milliseconds);
        Date date = new Date(milliseconds);
        SimpleDateFormat dateFormat;
        if (year == getYearByTimeStamp(time) && month == getMonthByTimeStamp(time)
                && day == getDayByTimeStamp(time)) {
            dateFormat = new SimpleDateFormat("今天  HH:mm");
        } else {
            dateFormat = new SimpleDateFormat("MM-dd  HH:mm");
        }
        return dateFormat.format(date);
    }

    public static String[] getUpcomingDay(long milliseconds) {
        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        String[] times = new String[5];

        String time = timeStampToDate(milliseconds);
        String dayStr = "";
        Date date = new Date(milliseconds);
        if (year == getYearByTimeStamp(time) && month == getMonthByTimeStamp(time)
                && day == getDayByTimeStamp(time)) {
            dayStr ="今天" ;
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日");
            dayStr = dateFormat.format(date);
        }
        times[0] = dayStr;

        SimpleDateFormat dateFormat = new SimpleDateFormat("HHmm");
        String minuteStr = dateFormat.format(date);
        for(int i = 0;i<4;i++) {
            times[i+1] = String.valueOf(minuteStr.charAt(i));
        }
        return times;
    }

    public static int getYearByTimeStamp(String date) {
        String year = date.substring(0, 4);
        return ParseUtil.parseInt(year, 0);
    }

    public static int getMonthByTimeStamp(String date) {
        String month = date.substring(5, 7);
        return ParseUtil.parseInt(month, 0);
    }

    public static int getDayByTimeStamp(String date) {
        String day = date.substring(8, 10);
        return ParseUtil.parseInt(day, 0);
    }

    public static String timeStampToDate(long timeStamp) {
        Date date = new Date(timeStamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

    public static String getOffsetTime(long targetTime) {
        long offsetTime = System.currentTimeMillis() - targetTime;
        if (offsetTime > 0) {
            if (offsetTime < 1 * MINUTES) {
                return offsetTime / SECOND + "秒以前";
            } else if (offsetTime < 1 * HOUR) {
                return offsetTime / MINUTES + "分钟以前";
            } else if (offsetTime < 1 * DAY) {
                return offsetTime / HOUR + "小时以前";
            } else {
                return getTimeByFormat(targetTime, "yyyy-MM-dd HH:mm");
            }
        } else {
            int[] time = getRestTime(-offsetTime);
            return time[0] + "天" + time[1] + "时" + time[2] + "分" + time[3] + "秒" + "后开始";
        }
    }

    /**
     * 根据传入Long型时间返回距今剩余多少时间
     *
     * @param time
     * @param isHaveText 设置是否需要显示剩余时间字样
     * @return
     */
    public static String getLastTime(long time, boolean isHaveText) {
        long now = Calendar.getInstance().getTimeInMillis();
        long deltime = (time - now) / 60000;
        String lastTime = "";
        if (isHaveText)
            lastTime = "剩余时间：";
        if (deltime / (60 * 24) > 0) {
            lastTime = lastTime + deltime / (60 * 24) + "天";
            long t = deltime % (60 * 24);
            if (t / 60 > 0) {
                lastTime = lastTime + t / 60 + "小时" + t % 60 + "分";
            } else {
                lastTime = lastTime + "0小时" + t % 60 + "分";
            }
        } else if (deltime / (60) > 0) {
            lastTime = lastTime + "0天" + deltime / 60 + "小时" + deltime % 60 + "分";
        } else if (deltime > 0) {
            lastTime = lastTime + "0天" + "0小时" + deltime % 60 + "分";
        } else {
            lastTime = "";
        }
        return lastTime;
    }

    /**
     * 根据传进来的long型时间返回时间数组
     *
     * @param time long型时间
     * @return 天 小时 分 秒
     */
    public static int[] getRestTime(long time) {
        int day = 0;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time > DateUtils.SECOND) {
            long l1 = time % DateUtils.DAY;
            long l2 = l1 % DateUtils.HOUR;
            second = (int) (l2 % DateUtils.MINUTES / DateUtils.SECOND);
            if (time > DateUtils.MINUTES) {
                minute = (int) (l2 / DateUtils.MINUTES);
                if (time > DateUtils.HOUR) {
                    hour = (int) (l1 / DateUtils.HOUR);
                    if (time > DateUtils.DAY) {
                        day = (int) (time / DateUtils.DAY);
                    }
                }
            }
        }
        return new int[]{day, hour, minute, second};
    }

    public static String getVodTime(String duration) {
        long time = ParseUtil.parseLong(duration, 0);
        int hour = (int) (time / (60 * 60));
        int minute = (int) (time % (60 * 60) / 60);
        int second = (int) (time % 60);
        String hourStr = hour < 10 ? "0" + hour : hour + "";
        String minuteStr = minute < 10 ? "0" + minute : minute + "";
        String secondStr = second < 10 ? "0" + second : second + "";
        return hourStr + ":" + minuteStr + ":" + secondStr;
    }

    public static String getVodTime(long time) {
        int hour = (int) (time / (60 * 60));
        int minute = (int) (time % (60 * 60) / 60);
        int second = (int) (time % 60);
        String hourStr = hour < 10 ? "0" + hour : hour + "";
        String minuteStr = minute < 10 ? "0" + minute : minute + "";
        String secondStr = second < 10 ? "0" + second : second + "";
        return hourStr + ":" + minuteStr + ":" + secondStr;
    }

    public static String getMyLiveTime(long time) {
        int hour = (int) (time / (60 * 60));
        int minute = (int) (time % (60 * 60) / 60);
        int second = (int) (time % 60);
        String hourStr = hour < 10 ? "0" + hour : hour + "";
        String minuteStr = minute < 10 ? "0" + minute : minute + "";
        String secondStr = second < 10 ? "0" + second : second + "";
        if (hourStr.equals("00")) {
            return minuteStr + ":" + secondStr;
        }
        return hourStr + ":" + minuteStr + ":" + secondStr;
    }

    /**
     * 时间格式化
     */
    public static String formattedTime(long second) {
        String hs, ms, ss, formatTime;

        long h, m, s;
        h = second / 3600;
        m = (second % 3600) / 60;
        s = (second % 3600) % 60;
        if (h < 10) {
            hs = "0" + h;
        } else {
            hs = "" + h;
        }

        if (m < 10) {
            ms = "0" + m;
        } else {
            ms = "" + m;
        }

        if (s < 10) {
            ss = "0" + s;
        } else {
            ss = "" + s;
        }
//        if (hs.equals("00")) {
//            formatTime = ms + ":" + ss;
//        } else {
        formatTime = hs + ":" + ms + ":" + ss;
//        }

        return formatTime;
    }


    /**
     * IM聊天室消息时间
     *
     * @param timeStamp 消息的时间戳
     */
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
        if(DateFormat.is24HourFormat(BaseApplication.getApplication())) {
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
     * 根据时间戳获得聊天时间显示, 当天消息显示时间(根据系统设置显示为 24 或 12 小时制), 昨天消息显示昨天, 昨天之前的消息显示日期
     */
    public static String getConversationTime(long timeStamp, Context context) {
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

    public static String getYearMonthDayTime(long timeStamp) {
        Date date = new Date(timeStamp * 1000);
        SimpleDateFormat f = new SimpleDateFormat("yyyy.M.d");
        return f.format(date);
    }

    public static String getShortlineTime(long time) {
        Date date = new Date((time));
        SimpleDateFormat f = new SimpleDateFormat("yyyy-M-d");
        return f.format(date);

    }
}
