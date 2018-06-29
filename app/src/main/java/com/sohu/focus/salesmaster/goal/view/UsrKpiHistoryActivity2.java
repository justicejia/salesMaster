package com.sohu.focus.salesmaster.goal.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.sohu.focus.salesmaster.base.SingleFragmentActivity;
import com.sohu.focus.salesmaster.http.model.GetUsrKpiHistory2;

/**
 * 与具体月份相关
 */

public class UsrKpiHistoryActivity2 extends SingleFragmentActivity {

    public static void show(Context context, GetUsrKpiHistory2 param) {
        Intent intent = new Intent(context, UsrKpiHistoryActivity2.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(UsrKpiHistoryFragment2.POST_MODEL, param);
        intent.putExtra(UsrKpiHistoryFragment2.POST_MODEL, bundle);
        context.startActivity(intent);
    }

    @Override
    protected Fragment onGetFragment() {
        return new UsrKpiHistoryFragment2();
    }

    @Nullable
    @Override
    protected Bundle onGetArguments() {
        return getIntent().getBundleExtra(UsrKpiHistoryFragment.POST_MODEL);
    }
}