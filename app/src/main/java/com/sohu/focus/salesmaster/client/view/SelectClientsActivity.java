package com.sohu.focus.salesmaster.client.view;

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

import java.util.ArrayList;
import java.util.List;

import static com.sohu.focus.salesmaster.uiframe.statusbar.SystemStatusManager.STATUS_STRATEGY_ABOVE_KITKAT;

/**
 * 选择客户
 * Created by yuanminjia on 2018/1/3.
 */

public class SelectClientsActivity extends BaseActivity {
    SelectClientsFragment fragment;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectclients);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.fragment_container);

        if (StatusBarHelper.statusBarLightMode(this) != StatusBarHelper.OTHER) {
            if (new SystemStatusManager(this).setTranslucentStatus(R.color.white) == STATUS_STRATEGY_ABOVE_KITKAT) {
                int statusHeight = ScreenUtil.getStatusBarHeight(this);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) frameLayout.getLayoutParams();
                layoutParams.topMargin = statusHeight;
                frameLayout.setLayoutParams(layoutParams);
            }
        }

        String projId = getIntent().getStringExtra(SalesConstants.EXTRA_PROJECT_ID);
        ArrayList<String> selectClients = getIntent().getStringArrayListExtra
                (SalesConstants.EXTRA_SELECT_CLIENT_IDS);
        fragment = SelectClientsFragment.newInstance(projId, selectClients);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, fragment);

        ft.commit();
    }
}
