package com.sohu.focus.salesmaster.me.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseActivity;
import com.sohu.focus.salesmaster.base.BaseFragment;
import com.sohu.focus.salesmaster.base.BaseWebViewActivity;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.dynamics.view.UserDynamicsFragment;
import com.sohu.focus.salesmaster.kernal.utils.EnvironmentManager;
import com.sohu.focus.salesmaster.kernal.utils.ScreenUtil;
import com.sohu.focus.salesmaster.goal.view.UserGoalFragment;
import com.sohu.focus.salesmaster.login.AccountManager;
import com.sohu.focus.salesmaster.subordinate.view.UserSubFragment;
import com.sohu.focus.salesmaster.uiframe.CommonDialog;
import com.sohu.focus.salesmaster.uiframe.PagerSlidingTabStrip;
import com.sohu.focus.salesmaster.uiframe.statusbar.StatusBarHelper;
import com.sohu.focus.salesmaster.uiframe.statusbar.SystemStatusManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 个人中心
 * Created by yuanminjia on 2017/10/30.
 */

public class MeActivity extends BaseActivity {

    @BindView(R.id.me_pager)
    ViewPager viewPager;
    @BindView(R.id.me_tabs)
    PagerSlidingTabStrip tabs;
    private List<BaseFragment> mFragments = new ArrayList<>();
    private MyPagerAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        ButterKnife.bind(this);
        if (StatusBarHelper.statusBarLightMode(this) != StatusBarHelper.OTHER) {
            if (new SystemStatusManager(this).setTranslucentStatus(R.color.white) == SystemStatusManager.STATUS_STRATEGY_ABOVE_KITKAT) {
                int statusHeight = ScreenUtil.getStatusBarHeight(this);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewPager.getLayoutParams();
                layoutParams.topMargin = statusHeight;
                viewPager.setLayoutParams(layoutParams);
            }
        }
        initView();
    }

    @OnClick(R.id.me_back)
    void close() {
        finish();
    }

    @OnClick(R.id.me_logout)
    void logout() {
        CommonDialog dialog = new CommonDialog.CommonDialogBuilder(this)
                .content("确定要退出登录?")
                .leftBtnText("取消")
                .rightBtnText("确定")
                .rightBtnListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AccountManager.INSTANCE.logOut();
                        BaseWebViewActivity.naviToWebWithFlag(EnvironmentManager.getLoginUrl(), MeActivity.this);
                        finish();
                    }
                }).create();
        dialog.show(getSupportFragmentManager(), "MeActivity");
    }

    public void initView() {
        BaseFragment kpi = new UserGoalFragment();
        BaseFragment sub = new UserSubFragment();
        BaseFragment dynamic = new UserDynamicsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SalesConstants.EXTRA_ID, AccountManager.INSTANCE.getUserId());

        kpi.setArguments(bundle);
        sub.setArguments(bundle);
        dynamic.setArguments(bundle);
        mFragments.add(kpi);
        mFragments.add(dynamic);
        mFragments.add(sub);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(3);

        tabs.setViewPager(viewPager);
        tabs.setDividerColorResource(R.color.transparent);
        tabs.setIndicatorHeight(8);
        tabs.setUnderlineColorResource(R.color.transparent);
        tabs.setIndicatorColor(getResources().getColor(R.color.home_icon_selected));
        tabs.setTabPaddingLeftRight(0);
        tabs.setSelectedBold(true);
        tabs.setTextBold(false);
        tabs.setTextSize(getResources().getDimensionPixelSize(R.dimen.text_middle_x));
        tabs.setSelectedTabTextColor(getResources().getColor(R.color.home_icon_selected));
        tabs.setUnselectedTabTextColorResource(R.color.standard_text_gray);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"我的目标", "我的动态", "我的下属"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

    }
}
