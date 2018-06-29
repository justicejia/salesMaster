package com.sohu.focus.salesmaster.uiframe;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by qiangzhao on 2017/3/29.
 */

public class FocusBaseDialog extends DialogFragment {

    @Override
    public void show(FragmentManager manager, String tag) {
        if (isVisible() || isAdded())
            return;
        try {
            super.show(manager, tag);
        } catch (Exception e) {
        }

    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        if (isVisible() || isAdded())
            return -1;
        return super.show(transaction, tag);
    }

    final protected boolean isAlive() {
        return (isVisible() || isAdded());
    }

    @Override
    public void dismiss() {
        dismissAllowingStateLoss();
    }
}
