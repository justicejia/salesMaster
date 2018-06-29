package com.sohu.focus.salesmaster.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.sohu.focus.salesmaster.uiframe.BaseProgressDialog;

/**
 * Created by yuanminjia on 2017/10/27.
 */

public class BaseActivity extends FragmentActivity {

    private BaseProgressDialog mProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgress = new BaseProgressDialog(this);
    }

    public void showProgress() {
        if (mProgress != null && !mProgress.isShowing()) {
            mProgress.show();
        }
    }

    public void dismissProgress() {
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }
}
