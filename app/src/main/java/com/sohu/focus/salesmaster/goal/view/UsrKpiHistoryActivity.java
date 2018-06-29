package com.sohu.focus.salesmaster.goal.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.sohu.focus.salesmaster.base.SingleFragmentActivity;
import com.sohu.focus.salesmaster.http.model.GetUsrKpiHistory;

/**
 * 与相对日期(上周，上月等)相关
 */

public class UsrKpiHistoryActivity extends SingleFragmentActivity {

    public static void showUserKpiHistory(Context context, GetUsrKpiHistory param) {
        Intent intent = new Intent(context, UsrKpiHistoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(UsrKpiHistoryFragment.POST_MODEL, param);
        intent.putExtra(UsrKpiHistoryFragment.POST_MODEL, bundle);
        context.startActivity(intent);
    }

    @Override
    protected Fragment onGetFragment() {
        return new UsrKpiHistoryFragment();
    }

    @Nullable
    @Override
    protected Bundle onGetArguments() {
        return getIntent().getBundleExtra(UsrKpiHistoryFragment.POST_MODEL);
    }

}