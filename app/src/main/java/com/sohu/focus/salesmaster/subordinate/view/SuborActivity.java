package com.sohu.focus.salesmaster.subordinate.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseActivity;
import com.sohu.focus.salesmaster.base.BaseFragment;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.dynamics.view.UserDynamicsFragment;
import com.sohu.focus.salesmaster.goal.view.UserGoalFragment;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.http.api.GetUserInvestApi;
import com.sohu.focus.salesmaster.invest.model.UserInvestInfoModel;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestListener;
import com.sohu.focus.salesmaster.kernal.utils.ScreenUtil;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;
import com.sohu.focus.salesmaster.project.view.UserProjectFragment;
import com.sohu.focus.salesmaster.sheets.view.SuborSheetFragment;
import com.sohu.focus.salesmaster.uiframe.PagerSlidingTabStrip;
import com.sohu.focus.salesmaster.uiframe.statusbar.StatusBarHelper;
import com.sohu.focus.salesmaster.uiframe.statusbar.SystemStatusManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 下属页面
 * Created by yuanminjia on 2017/10/31.
 */

public class SuborActivity extends BaseActivity {

    @BindView(R.id.sub_pager)
    ViewPager viewPager;
    @BindView(R.id.sub_tabs)
    PagerSlidingTabStrip tabs;
    @BindView(R.id.sub_name)
    TextView name;
    private List<BaseFragment> mFragments = new ArrayList<>();
    private MyPagerAdapter mAdapter;

    private String mUserName;
    private String mUserId;
    private int mAreaCode;
    private int mRole;
    private boolean mLoadInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subor);
        ButterKnife.bind(this);
        if (StatusBarHelper.statusBarLightMode(this) != StatusBarHelper.OTHER) {
            if (new SystemStatusManager(this).setTranslucentStatus(R.color.white) == SystemStatusManager.STATUS_STRATEGY_ABOVE_KITKAT) {
                int statusHeight = ScreenUtil.getStatusBarHeight(this);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewPager.getLayoutParams();
                layoutParams.topMargin = statusHeight;
                viewPager.setLayoutParams(layoutParams);
            }
        }
        initDefaultData();
    }

    public void initDefaultData() {
        mLoadInfo = getIntent().getBooleanExtra(SalesConstants.EXTRA_LOAD_INFO, false);
        mUserId = getIntent().getStringExtra(SalesConstants.EXTRA_ID);
        mUserName = getIntent().getStringExtra(SalesConstants.EXTRA_NAME);
        mAreaCode = getIntent().getIntExtra(SalesConstants.EXTRA_AREA, -1);
        mRole = getIntent().getIntExtra(SalesConstants.EXTRA_ROLE, 1);
        if (!mLoadInfo) {
            initView();
        } else {
            loadInfo();
        }
    }

    private void loadInfo() {
        GetUserInvestApi api = new GetUserInvestApi(mUserId);
        HttpEngine.getInstance().doCacheRequest(api, new HttpRequestListener<UserInvestInfoModel>() {
            @Override
            public void onSuccess(UserInvestInfoModel result, String method) {
                if (result != null && result.getData() != null) {
                    mUserName = result.getData().getPersonName();
                    mRole = result.getData().getPersonRole();
                }
                initView();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onFailed(UserInvestInfoModel result, String method) {
                if (result != null) ToastUtil.toast(result.getMsg());
            }
        });
    }

    @OnClick(R.id.sub_back)
    void close() {
        finish();
    }

    public void initView() {
        BaseFragment sheet = new SuborSheetFragment();
        BaseFragment goal = new UserGoalFragment();
        BaseFragment subFragment = new UserSubFragment();
        BaseFragment dynamic = new UserDynamicsFragment();
        BaseFragment project = new UserProjectFragment();

        Bundle bundle = new Bundle();
        bundle.putString(SalesConstants.EXTRA_ID, mUserId);
        bundle.putInt(SalesConstants.EXTRA_AREA, mAreaCode);
        bundle.putInt(SalesConstants.EXTRA_ROLE, mRole);
        bundle.putBoolean(SalesConstants.EXTRA_IS_SUB, true);

        goal.setArguments(bundle);
        subFragment.setArguments(bundle);
        dynamic.setArguments(bundle);
        project.setArguments(bundle);
        sheet.setArguments(bundle);

        mFragments.add(sheet);
        mFragments.add(dynamic);
        mFragments.add(goal);
        mFragments.add(project);
        mFragments.add(subFragment);

        name.setText(mUserName);

        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(3);

        tabs.setViewPager(viewPager);
        tabs.setDividerColorResource(R.color.transparent);
        tabs.setIndicatorHeight(8);
        tabs.setIndicatorColorResource(R.color.home_icon_selected);
        tabs.setShouldExpand(true);
        tabs.setUnderlineColorResource(R.color.transparent);
        tabs.setTabPaddingLeftRight(0);
        tabs.setTextSize(getResources().getDimensionPixelSize(R.dimen.text_middle_x));
        tabs.setSelectedTabTextColorResource(R.color.home_icon_selected);
        tabs.setUnselectedTabTextColorResource(R.color.standard_text_gray);
        tabs.setSelectedBold(true);

    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"报表", "动态", "目标", "项目", "下属"};

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
