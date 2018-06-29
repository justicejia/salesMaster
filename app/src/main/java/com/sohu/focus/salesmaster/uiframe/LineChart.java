package com.sohu.focus.salesmaster.uiframe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.kernal.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目详情折线图
 * Created by yuanminjia on 2017/12/29.
 */

public class LineChart extends View {

    private Paint mLinePaint;
    private Paint mBackGroundPaint;
    private Paint mCurveXTextPaint;
    private Paint mCurveYTextPaint;
    private Paint mCurvePaint;
    private Paint mDashLinePaint;
    private Paint mPointPaint;
    private Paint mInnerPointPaint;
    private Paint mSelectedTextPaint;
    private Paint mHintBgPaint;
    private Paint mHintTextPaint;

    private static final float TEXT_LEFT = 80;  //左侧文字占据的宽度
    private static final float TEXT_BOTTOM = 50; //底部文字占据的高度
    private static final float TEXT_TOP = 100;  //顶部文字占据的高度
    private static final float CHART_PADDING_LEFT = 40; //图表左边距
    private static final float CHART_PADDING_RIGHT = 60; //图表右边距

    //整个View的尺寸
    private float mTotalHeight;
    private float mTotalWidth;
    //图表区的尺寸
    private float mChartHeight;
    private float mChartWidth;
    //坐标原点
    private float mOriginX;
    private float mOriginY;
    //path
    private Path mBackPath;
    private Path mLinePath;
    private Path mHintPath;

    //折线图各数据点在View中的坐标
    private List<Float> mXData = new ArrayList<>();
    private List<Float> mYData = new ArrayList<>();
    //各点的原始数据
    private List<String> mYDataText = new ArrayList<>();
    //curve纵坐标
    private List<Float> mYCurveData = new ArrayList<>();
    //curve文字
    private List<String> mYCurvesText = new ArrayList<>();
    private List<String> mXCurvesText = new ArrayList<>();
    //提示框
    private int mSelectedIndex = -1;
    private RectF mRecf;

    public LineChart(Context context) {
        super(context);
        init();
    }

    public LineChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //折线
        mLinePaint = new Paint();
        mLinePaint.reset();
        mLinePaint.setColor(ContextCompat.getColor(getContext(), R.color.home_icon_selected));
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(6);
        mLinePaint.setStyle(Paint.Style.STROKE);

        //背景
        mBackGroundPaint = new Paint();
        mBackGroundPaint.setColor(ContextCompat.getColor(getContext(), R.color.line_chart_bg));
        mBackGroundPaint.setStyle(Paint.Style.FILL);
        LinearGradient linearGradient = new LinearGradient(0, 300, 0, 700,
                ContextCompat.getColor(getContext(), R.color.line_chart_bg_top),
                ContextCompat.getColor(getContext(), R.color.line_chart_bg_bottom),
                Shader.TileMode.CLAMP);
        mBackGroundPaint.setShader(linearGradient);

        //刻度文字
        mCurveXTextPaint = new Paint();
        mCurveXTextPaint.reset();
        mCurveXTextPaint.setAntiAlias(true);
        mCurveXTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.standard_text_light_gray));
        mCurveXTextPaint.setTextSize(ScreenUtil.sp2px(getContext(), 12));

        mCurveYTextPaint = new Paint();
        mCurveYTextPaint.reset();
        mCurveYTextPaint.setAntiAlias(true);
        mCurveYTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.standard_text_light_gray));
        mCurveYTextPaint.setTextSize(ScreenUtil.sp2px(getContext(), 12));
        mCurveYTextPaint.setTextAlign(Paint.Align.RIGHT);

        //标尺线
        mCurvePaint = new Paint();
        mCurvePaint.reset();
        mCurvePaint.setColor(ContextCompat.getColor(getContext(), R.color.line_chart_curve));
        mCurvePaint.setAntiAlias(true);

        //圆圈
        mPointPaint = new Paint();
        mPointPaint.reset();
        mPointPaint.setAntiAlias(true);
        mPointPaint.setColor(ContextCompat.getColor(getContext(), R.color.home_icon_selected));

        mInnerPointPaint = new Paint();
        mInnerPointPaint.reset();
        mInnerPointPaint.setAntiAlias(true);
        mInnerPointPaint.setColor(Color.WHITE);

        //虚线
        mDashLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDashLinePaint.setColor(ContextCompat.getColor(getContext(), R.color.home_icon_selected));
        mDashLinePaint.setStyle(Paint.Style.STROKE);
        mDashLinePaint.setStrokeWidth(2);
        DashPathEffect pathEffect = new DashPathEffect(new float[]{5, 5}, 1);
        mDashLinePaint.setPathEffect(pathEffect);

        //选择文字横坐标curve
        mSelectedTextPaint = new Paint();
        mSelectedTextPaint.reset();
        mSelectedTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.home_icon_selected));
        mSelectedTextPaint.setAntiAlias(true);
        mSelectedTextPaint.setTextSize(ScreenUtil.sp2px(getContext(), 12));

        //提示框
        mHintBgPaint = new Paint();
        mHintBgPaint.reset();
        mHintBgPaint.setAntiAlias(true);
        mHintBgPaint.setColor(ContextCompat.getColor(getContext(), R.color.home_icon_selected));

        mHintTextPaint = new Paint();
        mHintTextPaint.reset();
        mHintTextPaint.setAntiAlias(true);
        mHintTextPaint.setColor(Color.WHITE);
        mHintTextPaint.setTextSize(ScreenUtil.sp2px(getContext(), 12));
        mHintTextPaint.setTextAlign(Paint.Align.CENTER);

        mBackPath = new Path();
        mLinePath = new Path();
        mHintPath = new Path();


    }

    /**
     * 传入数据
     *
     * @param dataYs Y轴数据
     * @param curveY 刻度线数据
     * @param curveX X轴数据
     */
    public void setFloatData(List<String> curveX, List<Float> curveY, List<Float> dataYs) {
        clearAllData();
        for (Float i : curveY) {
            String temp = (int) (i * 100) + "%";
            mYCurvesText.add(temp);
        }
        for (Float i : dataYs) {
            mYDataText.add(String.valueOf(i));
        }
        mXCurvesText = curveX;
        mXCurvesText.remove(mXCurvesText.size() - 1);
        mXCurvesText.add("昨天");
        float firstX = mOriginX + CHART_PADDING_LEFT;
        float lastX = mTotalWidth - getPaddingRight() - CHART_PADDING_RIGHT;
        float unitY = mChartHeight / (curveY.get(curveY.size() - 1) - curveY.get(0));
        for (int i = 0; i < curveY.size(); i++) {
            mYCurveData.add(mOriginY - (curveY.get(i) - curveY.get(0)) * unitY);
        }
        float space = (lastX - firstX) / (dataYs.size() - 1);
        for (int i = 0; i < dataYs.size(); i++) {
            mYData.add(mOriginY - unitY * (dataYs.get(i) - curveY.get(0)));
            mXData.add(firstX + i * space);
        }


        drawPaths();
        invalidate();
    }

    public void setLongData(List<String> curveX, List<Long> curveY, List<Long> dataYs) {
        clearAllData();
        for (Long i : curveY) {
            mYCurvesText.add(String.valueOf(i));
        }
        for (Long i : dataYs) {
            mYDataText.add(String.valueOf(i));
        }
        mXCurvesText = curveX;
        mXCurvesText.remove(mXCurvesText.size() - 1);
        mXCurvesText.add("昨天");
        float firstX = mOriginX + CHART_PADDING_LEFT;
        float lastX = mTotalWidth - getPaddingRight() - CHART_PADDING_RIGHT;
        float unitY = mChartHeight / (curveY.get(curveY.size() - 1) - curveY.get(0));
        for (int i = 0; i < curveY.size(); i++) {
            mYCurveData.add(mOriginY - (curveY.get(i) - curveY.get(0)) * unitY);
        }
        float space = (lastX - firstX) / (dataYs.size() - 1);
        for (int i = 0; i < dataYs.size(); i++) {
            mYData.add(mOriginY - unitY * (dataYs.get(i) - curveY.get(0)));
            mXData.add(firstX + i * space);
        }
        drawPaths();
        invalidate();
    }

    private void drawPaths() {
        //折线背景
        mBackPath.reset();
        mBackPath.moveTo(mXData.get(0), mYData.get(0));
        for (int i = 1; i < mXData.size(); i++) {
            mBackPath.lineTo(mXData.get(i), mYData.get(i));
        }
        mBackPath.lineTo(mXData.get(mXData.size() - 1), mOriginY);
        mBackPath.lineTo(mXData.get(0), mOriginY);
        mBackPath.lineTo(mXData.get(0), mYData.get(0));
        mBackPath.close();

        //折线
        mLinePath.reset();
        mLinePath.moveTo(mXData.get(0), mYData.get(0));
        for (int i = 1; i < mXData.size(); i++) {
            mLinePath.lineTo(mXData.get(i), mYData.get(i));
        }
    }

    private void clearAllData() {
        mSelectedIndex = -1;
        mYData.clear();
        mXData.clear();
        mYCurvesText.clear();
        mYCurveData.clear();
        mYDataText.clear();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mTotalHeight = getMeasuredHeight();
        mTotalWidth = getMeasuredWidth();
        mChartHeight = mTotalHeight - getPaddingBottom() - getPaddingTop() - TEXT_BOTTOM - TEXT_TOP;
        mChartWidth = mTotalWidth - getPaddingLeft() - getPaddingRight() - TEXT_LEFT;
        mOriginX = getPaddingLeft() + TEXT_LEFT;
        mOriginY = mTotalHeight - getPaddingBottom() - TEXT_BOTTOM;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //背景
        canvas.drawPath(mBackPath, mBackGroundPaint);
        //折线
        canvas.drawPath(mLinePath, mLinePaint);
        //圆圈和横坐标刻度
        if (mXData.size() > 0 && mYData.size() > 0 && mXCurvesText.size() > 0) {
            for (int i = 0; i < mXData.size(); i++) {
                canvas.drawCircle(mXData.get(i), mYData.get(i), 10, mPointPaint);
                canvas.drawCircle(mXData.get(i), mYData.get(i), 7, mInnerPointPaint);
                canvas.drawText(mXCurvesText.get(i), mXData.get(i) - 42, mOriginY + 44, mCurveXTextPaint);
            }
        }
        //背景刻度线和纵坐标刻度
        if (mYCurveData.size() > 0 && mYCurvesText.size() > 0) {
            for (int i = 0; i < mYCurveData.size(); i++) {
                canvas.drawLine(mOriginX, mYCurveData.get(i),
                        mOriginX + mChartWidth,
                        mYCurveData.get(i), mCurvePaint);
                canvas.drawText(mYCurvesText.get(i) + "", 110, mYCurveData.get(i) + 10, mCurveYTextPaint);
            }
        }

        if (mSelectedIndex != -1) {
            canvas.drawPath(mHintPath, mDashLinePaint);
            canvas.drawText(mXCurvesText.get(mSelectedIndex), mXData.get(mSelectedIndex) - 42, mOriginY + 44, mSelectedTextPaint);
            canvas.drawRoundRect(mRecf, 5, 5, mHintBgPaint);
            canvas.drawText(mYDataText.get(mSelectedIndex), mXData.get(mSelectedIndex), 55, mHintTextPaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (CommonUtils.isEmpty(mYDataText) || CommonUtils.isEmpty(mXData))
            return true;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mSelectedIndex = findClosestPoint(event.getX());
            String hintText = mYDataText.get(mSelectedIndex);
            if (mSelectedIndex != -1) {
                mRecf = new RectF(mXData.get(mSelectedIndex) - hintText.length() * 17, 15, mXData.get(mSelectedIndex) + hintText.length() * 17,
                        TEXT_TOP - 30);
            }
            mHintPath.reset();
            mHintPath.moveTo(mXData.get(mSelectedIndex), TEXT_TOP / 2);
            mHintPath.lineTo(mXData.get(mSelectedIndex), mTotalHeight - TEXT_BOTTOM);
            invalidate();
        }
        return true;
    }

    int findClosestPoint(float x) {
        int index = 0;
        for (int i = 1; i < mXData.size(); i++) {
            if (Math.abs(mXData.get(i) - x) < Math.abs(mXData.get(index) - x)) {
                index = i;
            }
        }
        return index;
    }

}
