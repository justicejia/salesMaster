package com.sohu.focus.salesmaster.uiframe.easyrecyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.sohu.focus.salesmaster.uiframe.HackyViewPager;

/**
 * Created by zhaoqiang on 2017/9/21.
 */

public class FixedRecyclerView extends RecyclerView {
    boolean isNeedToEatEvent = false;
    private int lastX = -1;
    private int lastY = -1;

    private boolean isNeedInterceptHorizontal = false;

    public FixedRecyclerView(Context context) {
        this(context, null);
    }

    public FixedRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FixedRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setNeedInterceptHorizontal(boolean flag) {
        this.isNeedInterceptHorizontal = flag;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(!isNeedInterceptHorizontal) {
            return super.dispatchTouchEvent(ev);
        }
        int x = (int) ev.getRawX();
        int y = (int) ev.getRawY();
        int dealtX = 0;
        int dealtY = 0;

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dealtX = 0;
                dealtY = 0;
                // 保证子View能够接收到Action_move事件
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                dealtX += Math.abs(x - lastX);
                dealtY += Math.abs(y - lastY);
                // 这里是够拦截的判断依据是左右滑动，读者可根据自己的逻辑进行是否拦截
                if (dealtX >= dealtY) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    isNeedToEatEvent = true;
                }
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                isNeedToEatEvent = false;
                getParent().requestDisallowInterceptTouchEvent(false);
                break;

        }
        HackyViewPager.setLocked(isNeedToEatEvent);
        return super.dispatchTouchEvent(ev);
    }
}
