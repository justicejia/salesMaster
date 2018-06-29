package com.sohu.focus.salesmaster.uiframe;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.sohu.focus.salesmaster.R;

/**
 * Created by luckyzhangx on 01/02/2018.
 */

public class Switch extends View {

    private GestureDetector.OnGestureListener gestureListener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (option == OptionEnum.left) {
                option = OptionEnum.right;
            } else {
                option = OptionEnum.left;
            }
            if (optionListener != null) {
                optionListener.onOptionSelected(option);
            }
            invalidate();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    };

    private GestureDetectorCompat gestureDetector;

    public enum OptionEnum {
        left, right
    }

    public interface OptionListener {
        void onOptionSelected(OptionEnum option);
    }

    private OptionListener optionListener;

    public void setOptionListener(OptionListener optionListener) {
        this.optionListener = optionListener;
    }

    public void setOption(OptionEnum option) {
        this.option = option;
        if (optionListener != null) {
            optionListener.onOptionSelected(option);
        }
        invalidate();
    }

    //    自定义属性
    private int color = 0xff799ff3;
    private float textSize = 32;
    private float padding = 16;
    private float round = 10;
    private float strokeWidth = 2;

    private Paint strokePaint;
    private Paint bgPaint;
    private Paint textPaint;

    private String leftText = "", rightText = "";

    private OptionEnum option = OptionEnum.left;


    //    fill 是为了将中间的圆角填充为直角
    RectF leftRect, leftRectFill, rightRect, rightRectFill;

    RectF strokeRect;

    private void initRects() {
        float offset = strokeWidth / 2;

        leftRect = new RectF(0 + offset, 0 + offset, getMeasuredWidth() / 2, getMeasuredHeight() - offset);
        leftRectFill = new RectF(getMeasuredWidth() / 2 - padding, 0 + offset, getMeasuredWidth() / 2,
                getMeasuredHeight() - offset);
        rightRect = new RectF(getMeasuredWidth() / 2, 0 + offset, getMeasuredWidth() - offset, getMeasuredHeight() - offset);
        rightRectFill = new RectF(getMeasuredWidth() / 2, 0 + offset, getMeasuredWidth() / 2 + padding,
                getMeasuredHeight() - offset);

        strokeRect = new RectF(0 + offset, 0 + offset,
                getMeasuredWidth() - offset
                , getMeasuredHeight() - offset);
    }

    private void initPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        strokePaint = new Paint(paint);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(color);
        strokePaint.setStrokeWidth(strokeWidth);

        bgPaint = new Paint(paint);

        textPaint = new Paint(paint);
        textPaint.setTextSize(textSize);
    }


    public Switch(Context context) {
        this(context, null);
    }

    public Switch(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Switch(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initPaint();
        gestureDetector = new GestureDetectorCompat(getContext(), gestureListener);

        TypedArray values = getContext().obtainStyledAttributes(attrs, R.styleable.Switch);

        color = values.getColor(R.styleable.Switch_highlight_color, Color.BLUE);
        textSize = values.getDimension(R.styleable.Switch_textSize, 16);
        padding = values.getDimension(R.styleable.Switch_padding, 16);
        round = values.getDimension(R.styleable.Switch_round, 16);
        strokeWidth = values.getDimension(R.styleable.Switch_strokeWidth, 16);

        leftText = values.getString(R.styleable.Switch_leftText);
        rightText = values.getString(R.styleable.Switch_rightText);

        values.recycle();

        initPaint();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float width = 4 * padding + textPaint.measureText(leftText + rightText);
        Paint.FontMetricsInt metricsInt = textPaint.getFontMetricsInt();
//        float height = 2 * padding + metricsInt.descent - metricsInt.ascent;
        float height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension((int) width, (int) height);

        initRects();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        bgPaint.setColor(Color.TRANSPARENT);
        if (option.equals(OptionEnum.left)) {
            bgPaint.setColor(color);
            textPaint.setColor(Color.WHITE);
        } else {
            bgPaint.setColor(Color.TRANSPARENT);
            textPaint.setColor(color);
        }

        canvas.drawRoundRect(leftRect, round, round, bgPaint);
        canvas.drawRect(leftRectFill, bgPaint);
        drawTextVerticalCenter(canvas, leftText, textPaint, padding, getMeasuredHeight() / 2);

        if (option.equals(OptionEnum.right)) {
            bgPaint.setColor(color);
            textPaint.setColor(Color.WHITE);
        } else {
            bgPaint.setColor(Color.TRANSPARENT);
            textPaint.setColor(color);
        }

        canvas.drawRoundRect(rightRect, round, round, bgPaint);
        canvas.drawRect(rightRectFill, bgPaint);
        drawTextVerticalCenter(canvas, rightText, textPaint,
                padding + getMeasuredWidth() / 2, getMeasuredHeight() / 2);

        canvas.drawRoundRect(strokeRect, round, round, strokePaint);
    }

    private void drawTextVerticalCenter(@NonNull Canvas canvas, String text, @NonNull Paint
            paint, float left, float center) {
        Paint.FontMetrics metrics = paint.getFontMetrics();
        float baseline = center -
                (((metrics.top - metrics.bottom) / 2) + metrics.bottom);

        canvas.drawText(text, left, baseline, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event) || gestureDetector.onTouchEvent(event);
    }
}
