package com.sohu.focus.salesmaster.uiframe;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.sohu.focus.salesmaster.R;

import butterknife.ButterKnife;

/**
 * base dialog fragment
 * Created by zhezhang207431 on 2016/12/23.
 */

public abstract class BaseDialogFragment extends FocusBaseDialog {
    protected String mScreenType = "1";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        initDefaultData();
        initDialogStyle();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCancelable(getDialogFragmentCancelable());
        getDialog().setCanceledOnTouchOutside(getDialogFragmentCancelable());
        this.setCancelable(getDialogFragmentCancelable());
        View contentView = inflater.inflate(getDialogLayoutId(), container, false);
        ButterKnife.bind(this, contentView);
        //初始化dialog在屏幕上的显示模式
        initScreenMode();
        findView(contentView);
        //初始化布局控件
        initView();
        //界面加载数据
        loadData();
        return contentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadWhenOverBound();
    }

    /**
     * 当dialog长宽需要到达边界时加载此操作
     */
    protected void loadWhenOverBound() {
        if (getDialogOverBound()) {
            Dialog dialog = getDialog();
            if (null != dialog) {
                if ("2".equals(mScreenType))
                    dialog.getWindow().setLayout(-2, -1);
                else
                    dialog.getWindow().setLayout(-1, -2);
            }
        }
    }

    protected void initDialogStyle() {
        if (getDialogOverBound()) {
            setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BottomDialog);
        } else {
            setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AlertDialogFragmentTheme);
        }
    }

    /**
     * 设置屏幕参数
     */
    protected void initScreenMode() {
        if (getDialogOverBound()) {
            // 设置宽度为屏宽、靠近屏幕底部。
            Window window = getDialog().getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.BOTTOM;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(wlp);
        }
    }

    protected void findView(View contentView) {

    }

    /**
     * 在页面初始化之前获取传参
     */
    protected abstract void initDefaultData();

    /**
     * 弹窗是否能取消
     */
    protected abstract boolean getDialogFragmentCancelable();

    /**
     * 获取布局ID
     */
    protected abstract int getDialogLayoutId();

    /**
     * 初始化界面控件
     */
    protected abstract void initView();

    /**
     * 为布局控件渲染数据
     */
    protected abstract void loadData();

    /**
     * 弹窗是否需要铺满垂直或者水平方向的边界
     */
    protected abstract boolean getDialogOverBound();
}
