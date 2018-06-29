package com.sohu.focus.salesmaster.kernal.utils;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;


import com.sohu.focus.salesmaster.kernal.log.Logger;

import java.lang.ref.WeakReference;

/**
 * 定时器
 * Created by zhaoqiang on 2017/6/20.
 */

public class TimerHandler extends Handler {

    public static final String TAG = TimerHandler.class.getSimpleName();

    private static final int STOP = 0x10;
    private static final int RUNNING = 0x11;
    private static final int PAUSE = 0x12;

    private static final int WHAT_START = 100;
    private static final int WHAT_POST = 101;
    private long interval;
    private WeakReference<TimerHandlerListener> listener;
    private volatile int curState = STOP;

    long lastMsgTime;

    public TimerHandler(TimerHandlerListener listener, long interval) {
        this.listener = new WeakReference<>(listener);
        this.interval = interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public void start() {
        if (curState == PAUSE) {
            resume();
        } else if (curState == STOP) {
            Logger.ZQ().d(TAG, "start");
            removeMessages(WHAT_START);
            removeMessages(WHAT_POST);
            sendEmptyMessage(WHAT_START);
            curState = RUNNING;
        }
    }

    public void startDelay(int delayTime) {
        Logger.ZQ().d(TAG, "delay");
        removeMessages(WHAT_START);
        removeMessages(WHAT_POST);
        long now = SystemClock.uptimeMillis();
        long next = now + delayTime;

        sendEmptyMessageAtTime(WHAT_START, next);
        curState = RUNNING;
    }

    public void pause() {
        if (curState != STOP) {
            Logger.ZQ().d(TAG, "pause");
            curState = PAUSE;
        }
    }

    public void resume() {
        Logger.ZQ().d(TAG, "start");
        curState = RUNNING;
    }

    public void stop() {
        if(curState != STOP) {
            curState = STOP;
            Logger.ZQ().d(TAG, "stop");
            removeMessages(WHAT_START);
            removeMessages(WHAT_POST);
        }
    }

    public void release() {
        curState = STOP;
        Logger.ZQ().d(TAG, "release");
        removeMessages(WHAT_START);
        removeMessages(WHAT_POST);
        if (listener != null)
            listener.clear();
    }

    @Override
    public void handleMessage(Message msg) {
        long now = SystemClock.uptimeMillis();
        long next = now + (interval - now % interval);
        switch (msg.what) {
            case WHAT_START:
                sendEmptyMessageAtTime(WHAT_POST, next);
                break;
            case WHAT_POST:
                if (listener != null && listener.get() != null && curState == RUNNING) {
                    if(interval >= 3000) {
                        if(now - lastMsgTime > (interval - 1000)) {
                            Logger.ZQ().v(TAG, "onTick");
                            listener.get().callBack();
                        }
                    } else {
                        Logger.ZQ().v(TAG, "onTick");
                        listener.get().callBack();
                    }
                }

                sendEmptyMessageAtTime(WHAT_POST, next);
                break;
        }
        lastMsgTime = SystemClock.uptimeMillis();
    }

    public interface TimerHandlerListener {
        void callBack();
    }
}

