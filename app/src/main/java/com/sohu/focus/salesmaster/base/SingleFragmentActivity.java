package com.sohu.focus.salesmaster.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.comment.view.UnreadCommentsFragment;
import com.sohu.focus.salesmaster.kernal.utils.ScreenUtil;
import com.sohu.focus.salesmaster.uiframe.statusbar.StatusBarHelper;
import com.sohu.focus.salesmaster.uiframe.statusbar.SystemStatusManager;

import static com.sohu.focus.salesmaster.uiframe.statusbar.SystemStatusManager.STATUS_STRATEGY_ABOVE_KITKAT;

/**
 * Created by yuanminjia on 2017/10/27.
 */

// 只包含一个 Fragment 的通用 Activity

public abstract class SingleFragmentActivity extends BaseActivity {

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

        Fragment fragment = onGetFragment();
        Bundle args = onGetArguments();
        if (args != null) fragment.setArguments(args);

        showFragment(fragment);
    }

    protected abstract Fragment onGetFragment();

    @Nullable
    protected abstract Bundle onGetArguments();

    protected void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, fragment);

        transaction.commit();
    }

}
