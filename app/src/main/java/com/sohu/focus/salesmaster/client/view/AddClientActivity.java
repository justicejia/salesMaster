package com.sohu.focus.salesmaster.client.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseActivity;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.kernal.utils.ScreenUtil;
import com.sohu.focus.salesmaster.uiframe.statusbar.StatusBarHelper;
import com.sohu.focus.salesmaster.uiframe.statusbar.SystemStatusManager;

import static com.sohu.focus.salesmaster.uiframe.statusbar.SystemStatusManager.STATUS_STRATEGY_ABOVE_KITKAT;

/**
 * 添加客户
 * Created by yuanminjia on 2018/1/3.
 */

public class AddClientActivity extends BaseActivity {

    public static void addClient(Context context, String projId) {
        Intent intent = new Intent(context, AddClientActivity.class);
        intent.putExtra(SalesConstants.EXTRA_PROJECT_ID, projId);
        context.startActivity(intent);
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment_container);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.fragment_container);
        if (StatusBarHelper.statusBarLightMode(this) != StatusBarHelper.OTHER) {
            if (new SystemStatusManager(this).setTranslucentStatus(R.color.white) == STATUS_STRATEGY_ABOVE_KITKAT) {
                int statusHeight = ScreenUtil.getStatusBarHeight(this);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) frameLayout.getLayoutParams();
                layoutParams.topMargin = statusHeight;
                frameLayout.setLayoutParams(layoutParams);
            }
        }
        String clientId = getIntent().getStringExtra(SalesConstants.EXTRA_ID);
        String projId = getIntent().getStringExtra(SalesConstants.EXTRA_PROJECT_ID);

        AddClientFragment fragment = AddClientFragment.newInstance(clientId, projId);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, fragment);

        transaction.commit();
    }
}