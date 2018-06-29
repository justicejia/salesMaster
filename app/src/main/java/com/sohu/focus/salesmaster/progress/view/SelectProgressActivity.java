package com.sohu.focus.salesmaster.progress.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sohu.focus.salesmaster.http.api.GetFilterApi;
import com.sohu.focus.salesmaster.newFilter.model.FilterModel;
import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestListener;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.kernal.utils.ScreenUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.sohu.focus.salesmaster.base.BaseActivity;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;
import com.sohu.focus.salesmaster.login.AccountManager;
import com.sohu.focus.salesmaster.progress.adapter.MyListAdpater;
import com.sohu.focus.salesmaster.uiframe.statusbar.StatusBarHelper;
import com.sohu.focus.salesmaster.uiframe.statusbar.SystemStatusManager;

import java.util.ArrayList;

import static com.sohu.focus.salesmaster.uiframe.statusbar.SystemStatusManager.STATUS_STRATEGY_ABOVE_KITKAT;

/**
 * 添加进度-选择进度页
 * Created by yuanminjia on 2017/10/30.
 */

public class SelectProgressActivity extends BaseActivity {

    @BindView(R.id.select_progress_list)
    ListView listView;
    private MyListAdpater mAdapter;
    private ArrayList<FilterModel.DataBean.StateBean> mData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_progress);
        ButterKnife.bind(this);
        if (StatusBarHelper.statusBarLightMode(this) != StatusBarHelper.OTHER) {
            if (new SystemStatusManager(this).setTranslucentStatus(R.color.white) == STATUS_STRATEGY_ABOVE_KITKAT) {
                int statusHeight = ScreenUtil.getStatusBarHeight(this);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) listView.getLayoutParams();
                layoutParams.topMargin = statusHeight;
                listView.setLayoutParams(layoutParams);
            }
        }
        initView();
    }

    @OnClick(R.id.select_progress_back)
    void back() {
        finish();
    }

    public void initView() {
        mAdapter = new MyListAdpater(this, mData);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                String name = ((FilterModel.DataBean.StateBean) (mAdapter.getItem(position))).getLabel();
                int stageId = ((FilterModel.DataBean.StateBean) (mAdapter.getItem(position))).getOption();
                boolean needCustomer = ((FilterModel.DataBean.StateBean) (mAdapter.getItem(position))).isNeedCustomer();
                intent.putExtra(SalesConstants.EXTRA_PROGRESS, name);
                intent.putExtra(SalesConstants.EXTRA_PROGRESS_ID, stageId);
                intent.putExtra(SalesConstants.EXTRA_PROGRESS_NEED_CUSTOMER, needCustomer);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        listView.setAdapter(mAdapter);
        HttpEngine.getInstance().doHttpRequest(new GetFilterApi(), new HttpRequestListener<FilterModel>() {
            @Override
            public void onSuccess(FilterModel result, String method) {
                if (result != null && result.getData() != null) {
                    if (result.getData().getUserAvailableRoleList().size() > 1) {
                        AccountManager.INSTANCE.saveUserViewRoles(true);
                    } else {
                        AccountManager.INSTANCE.saveUserViewRoles(false);
                    }
                    boolean isWholeCountry = false;
                    if (CommonUtils.notEmpty(result.getData().getCityList())) {
                        for (FilterModel.DataBean.CityBean item : result.getData().getCityList()) {
                            if (item.getLabel().equals("全国")) isWholeCountry = true;
                        }
                    }
                    AccountManager.INSTANCE.saveUserId(result.getData().getUserId());
                    AccountManager.INSTANCE.saveUserIsWholeCountry(isWholeCountry);
                    AccountManager.INSTANCE.saveUserRole(result.getData().getSalesRole());
                    mData.addAll(result.getData().getState());
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onFailed(FilterModel result, String method) {
                if (result != null) {
                    ToastUtil.toast(result.getMsg());
                }
            }
        });

    }


}
