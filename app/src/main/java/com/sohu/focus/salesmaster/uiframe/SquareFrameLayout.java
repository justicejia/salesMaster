package com.sohu.focus.salesmaster.uiframe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by luckyzhangx on 02/01/2018.
 */

public class SquareFrameLayout extends FrameLayout {
    private static final String TAG = "SquareFrameLayout";

    public SquareFrameLayout(@NonNull Context context) {
        super(context);
    }

    public SquareFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int measureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        super.onMeasure(measureSpec, measureSpec);
    }
}
