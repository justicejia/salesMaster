package com.sohu.focus.salesmaster.main.view;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseActivity;
import com.sohu.focus.salesmaster.base.BaseFragment;
import com.sohu.focus.salesmaster.base.BaseWebViewActivity;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.filter.FilterPresenter;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.http.api.CheckUpdateApi;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestListener;
import com.sohu.focus.salesmaster.kernal.utils.EnvironmentManager;
import com.sohu.focus.salesmaster.kernal.utils.ScreenUtil;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;
import com.sohu.focus.salesmaster.login.AccountManager;
import com.sohu.focus.salesmaster.main.model.UpdateModel;
import com.sohu.focus.salesmaster.sheets.view.HomeSheetFragment;
import com.sohu.focus.salesmaster.uiframe.FixedFragmentTabHost;
import com.sohu.focus.salesmaster.uiframe.UpgradeDialog;
import com.sohu.focus.salesmaster.uiframe.statusbar.StatusBarHelper;
import com.sohu.focus.salesmaster.uiframe.statusbar.SystemStatusManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sohu.focus.salesmaster.uiframe.statusbar.SystemStatusManager.STATUS_STRATEGY_ABOVE_KITKAT;

public class MainActivity extends BaseActivity {
    public static final String TAG = "MainActivity";
    @BindView(R.id.tabs)
    FixedFragmentTabHost mTabHost;

    private ArrayList<BaseFragment> mFragments = new ArrayList<>();
    private String[] mTextArray = {"报表", "动态", "项目", "下属"};
    private int mImageArray[] = {R.drawable.tab_sheet, R.drawable.tab_home, R.drawable.tab_project, R.drawable.tab_sub};
    private TextView tab0Txt, tab1Txt, tab2Txt, tab3Txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (StatusBarHelper.statusBarLightMode(this) != StatusBarHelper.OTHER) {
            if (new SystemStatusManager(this).setTranslucentStatus(R.color.white) == STATUS_STRATEGY_ABOVE_KITKAT) {
                int statusHeight = ScreenUtil.getStatusBarHeight(this);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mTabHost.getLayoutParams();
                layoutParams.topMargin = statusHeight;
                mTabHost.setLayoutParams(layoutParams);
            }
        }
        if (!AccountManager.INSTANCE.isLogin()) {
            BaseWebViewActivity.naviToWeb(EnvironmentManager.getLoginUrl(), this, "");
            finish();
        } else {
            checkUpdate();
            FilterPresenter.updateFilterData();
            initView();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void initView() {
        mTabHost.setup(this, getSupportFragmentManager(), R.id.fragment_container);
        mFragments.add(new HomeSheetFragment());
        mFragments.add(new HomeDynamicsFragment());
        mFragments.add(new HomeProjectFragment());
        mFragments.add(new HomeSubFragment());

        for (int i = 0; i < mFragments.size(); i++) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextArray[i]).setIndicator(getTabItemView(i));
            mTabHost.addTab(tabSpec, mFragments.get(i).getClass(), null);
            mTabHost.getTabWidget().setDividerDrawable(null);
        }
        tab0Txt.setTextColor(getResources().getColor(R.color.home_icon_selected));
        tab1Txt.setTextColor(getResources().getColor(R.color.standard_text_light_gray));
        tab2Txt.setTextColor(getResources().getColor(R.color.standard_text_light_gray));
        tab3Txt.setTextColor(getResources().getColor(R.color.standard_text_light_gray));
    }

    private View getTabItemView(final int index) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(this);

        if (index == 0) {
            view = layoutInflater.inflate(R.layout.main_navigation_button, null);
            tab0Txt = (TextView) view.findViewById(R.id.tab_text);
            tab0Txt.setText(mTextArray[index]);
            ImageView icon1 = (ImageView) view.findViewById(R.id.tab_icon);
            icon1.setImageResource(mImageArray[index]);
            view.findViewById(R.id.tab_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeToTab(index);
                }
            });


        } else if (index == 1) {
            view = layoutInflater.inflate(R.layout.main_navigation_button, null);
            tab1Txt = (TextView) view.findViewById(R.id.tab_text);
            tab1Txt.setText(mTextArray[index]);
            ImageView icon1 = (ImageView) view.findViewById(R.id.tab_icon);
            icon1.setImageResource(mImageArray[index]);
            view.findViewById(R.id.tab_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeToTab(index);
                }
            });

        } else if (index == 2) {
            view = layoutInflater.inflate(R.layout.main_navigation_button, null);
            tab2Txt = (TextView) view.findViewById(R.id.tab_text);
            tab2Txt.setText(mTextArray[index]);
            ImageView icon2 = (ImageView) view.findViewById(R.id.tab_icon);
            icon2.setImageResource(mImageArray[index]);
            view.findViewById(R.id.tab_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeToTab(index);
                }
            });

        } else {
            view = layoutInflater.inflate(R.layout.main_navigation_button, null);
            tab3Txt = (TextView) view.findViewById(R.id.tab_text);
            tab3Txt.setText(mTextArray[index]);
            ImageView icon3 = (ImageView) view.findViewById(R.id.tab_icon);
            icon3.setImageResource(mImageArray[index]);
            view.findViewById(R.id.tab_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeToTab(index);
                }
            });
        }
        return view;
    }

    public void changeToTab(int index) {
        mTabHost.setCurrentTab(index);
        tab0Txt.setTextColor(getResources().getColor(R.color.standard_text_light_gray));
        tab1Txt.setTextColor(getResources().getColor(R.color.standard_text_light_gray));
        tab2Txt.setTextColor(getResources().getColor(R.color.standard_text_light_gray));
        tab3Txt.setTextColor(getResources().getColor(R.color.standard_text_light_gray));
        if (index == 0) {
            tab0Txt.setTextColor(getResources().getColor(R.color.home_icon_selected));
        } else if (index == 1) {
            tab1Txt.setTextColor(getResources().getColor(R.color.home_icon_selected));
        } else if (index == 2) {
            tab2Txt.setTextColor(getResources().getColor(R.color.home_icon_selected));
        } else {
            tab3Txt.setTextColor(getResources().getColor(R.color.home_icon_selected));
        }
    }

    void checkUpdate() {
        int version = 0;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (version == 0) return;
        CheckUpdateApi api = new CheckUpdateApi(String.valueOf(version));
        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<UpdateModel>() {
            @Override
            public void onSuccess(final UpdateModel result, String method) {
                if (result != null && result.getData() != null) {
                    if (result.getData().isHasNewVersion()) {
                        UpgradeDialog dialog = new UpgradeDialog();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(SalesConstants.EXTRA_UPGRADE_DATA, result);
                        dialog.setArguments(bundle);
                        dialog.show(getSupportFragmentManager(), TAG);
                    }

                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onFailed(UpdateModel result, String method) {
                if (result != null) ToastUtil.toast(result.getMsg());
            }
        });
    }
}
