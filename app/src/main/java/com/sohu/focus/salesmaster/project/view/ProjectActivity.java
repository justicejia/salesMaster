package com.sohu.focus.salesmaster.project.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseActivity;
import com.sohu.focus.salesmaster.base.BaseFragment;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.kernal.utils.ScreenUtil;
import com.sohu.focus.salesmaster.uiframe.PagerSlidingTabStrip;
import com.sohu.focus.salesmaster.uiframe.statusbar.StatusBarHelper;
import com.sohu.focus.salesmaster.uiframe.statusbar.SystemStatusManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 项目详情页
 * Created by yuanminjia on 2017/10/30.
 */

public class ProjectActivity extends BaseActivity {

    @BindView(R.id.project_title)
    TextView title;
    @BindView(R.id.project_pager)
    ViewPager viewPager;
    @BindView(R.id.project_tabs)
    PagerSlidingTabStrip tabs;

    @OnClick(R.id.project_back)
    void back() {
        finish();
    }

    List<BaseFragment> mFragments = new ArrayList<>();
    String mProjectId;
    String mProjectName;

    MyPagerAdapter mPagerAdapter;


    public static void showDynamics(Context context, String projectID, String projectName) {
        Intent intent = new Intent(context, ProjectActivity.class);
        intent.putExtra(SalesConstants.EXTRA_PROJECT_ID, projectID);
        intent.putExtra(SalesConstants.EXTRA_PROJECT, projectName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        ButterKnife.bind(this);
        if (StatusBarHelper.statusBarLightMode(this) != StatusBarHelper.OTHER) {
            if (new SystemStatusManager(this).setTranslucentStatus(R.color.white) == SystemStatusManager.STATUS_STRATEGY_ABOVE_KITKAT) {
                int statusHeight = ScreenUtil.getStatusBarHeight(this);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewPager.getLayoutParams();
                layoutParams.topMargin = statusHeight;
                viewPager.setLayoutParams(layoutParams);
            }
        }
        mProjectName = getIntent().getStringExtra(SalesConstants.EXTRA_PROJECT);
        mProjectId = getIntent().getStringExtra(SalesConstants.EXTRA_PROJECT_ID);
        initView();
    }

    void initView() {
        title.setText(mProjectName);

        BaseFragment dataFragment = new ProjectDataFragment();
        BaseFragment kpiFragment = new ProjectKpiFragment();
        BaseFragment dynamicFragment = new ProjectDynamicFragment();
        BaseFragment infoFragment = new ProjectInfoFragment();
        BaseFragment clientFragment = new ProjectClientFragment();
        BaseFragment fileFragment = new ProjectFileFragment();
        BaseFragment investFragment = new ProjectInvestFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SalesConstants.EXTRA_PROJECT_ID, mProjectId);
        dataFragment.setArguments(bundle);
        kpiFragment.setArguments(bundle);
        dynamicFragment.setArguments(bundle);
        infoFragment.setArguments(bundle);
        clientFragment.setArguments(bundle);
        fileFragment.setArguments(bundle);
        investFragment.setArguments(bundle);
        mFragments.add(dataFragment);
        mFragments.add(kpiFragment);
        mFragments.add(dynamicFragment);
        mFragments.add(investFragment);
        mFragments.add(infoFragment);
        mFragments.add(clientFragment);
        mFragments.add(fileFragment);

        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);
        viewPager.setCurrentItem(0);

        tabs.setViewPager(viewPager);
        tabs.setDividerColorResource(R.color.transparent);
        tabs.setIndicatorHeight(8);
        tabs.setIndicatorColorResource(R.color.home_icon_selected);
        tabs.setShouldExpand(true);
        tabs.setUnderlineColorResource(R.color.transparent);
        tabs.setTextSize(getResources().getDimensionPixelSize(R.dimen.text_middle_x));
        tabs.setSelectedTabTextColorResource(R.color.home_icon_selected);
        tabs.setUnselectedTabTextColorResource(R.color.standard_text_gray);
        tabs.setSelectedBold(true);
    }


    private class MyPagerAdapter extends FragmentStatePagerAdapter {

        private final String[] titles = {"数据", "目标", "动态", "投放", "信息", "客户", "文件"};

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public Fragment getItem(int position) {
            return CommonUtils.notEmpty(mFragments) ? mFragments.get(position) : null;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mFragments.get(viewPager.getCurrentItem()).onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
