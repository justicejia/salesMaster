package com.sohu.focus.salesmaster.dynamics.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.widget.FrameLayout;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseActivity;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.base.SingleFragmentActivity;
import com.sohu.focus.salesmaster.comment.view.UnreadCommentsFragment;
import com.sohu.focus.salesmaster.kernal.utils.ScreenUtil;
import com.sohu.focus.salesmaster.uiframe.statusbar.StatusBarHelper;
import com.sohu.focus.salesmaster.uiframe.statusbar.SystemStatusManager;

import static com.sohu.focus.salesmaster.uiframe.statusbar.SystemStatusManager.STATUS_STRATEGY_ABOVE_KITKAT;

/**
 * Created by luckyzhangx on 14/03/2018.
 */

public class DynamicDetailActivity extends SingleFragmentActivity {


    public static void showDynamicDetail(Context context, String dynamicId) {
        Intent intent = new Intent(context, DynamicDetailActivity.class);
        intent.putExtra(SalesConstants.EXTRA_DYNAMIC_ID, dynamicId);
        ContextCompat.startActivity(context, intent, null);
    }

    @Override
    protected Fragment onGetFragment() {
        return new DynamicDetailFragment();
    }

    @Nullable
    @Override
    protected Bundle onGetArguments() {
        Bundle args = new Bundle();
        args.putString(SalesConstants.EXTRA_DYNAMIC_ID,
                getIntent().getStringExtra(SalesConstants.EXTRA_DYNAMIC_ID));
        return args;
    }
}
