package com.sohu.focus.salesmaster.kernal.log.core;


import com.sohu.focus.salesmaster.kernal.log.Logger;

/**
 * Created by zhaoqiang on 2017/6/14.
 */

public class TimeCounter {

    String tag;

    public TimeCounter(String tag) {
        this.tag = tag == null ? "" : tag;
    }

    public String getTag() {
        return tag;
    }

    long beginDate;
    long endDate;

    public void begin() {
        beginDate = System.currentTimeMillis();
    }

    public void end() {
        endDate = System.currentTimeMillis();
        long time = getHowLong();
        Logger.ZQ().d("TimeCounter", String.format("%s   cost: %s ms", getTag(), time));
    }

    public long getHowLong() {
        return endDate - beginDate;
    }
}
