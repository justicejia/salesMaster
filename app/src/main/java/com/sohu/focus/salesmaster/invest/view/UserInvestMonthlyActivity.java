package com.sohu.focus.salesmaster.invest.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseActivity;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.http.api.GetSuborInvestApi;
import com.sohu.focus.salesmaster.invest.adapter.UserInvestMonthlyHolder;
import com.sohu.focus.salesmaster.invest.model.SuborInvestModel;
import com.sohu.focus.salesmaster.invest.model.UserInvestCommonVO;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestListener;
import com.sohu.focus.salesmaster.kernal.utils.ScreenUtil;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.EasyRecyclerView;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.decoration.DividerDecoration;
import com.sohu.focus.salesmaster.uiframe.statusbar.StatusBarHelper;
import com.sohu.focus.salesmaster.uiframe.statusbar.SystemStatusManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sohu.focus.salesmaster.uiframe.statusbar.SystemStatusManager.STATUS_STRATEGY_ABOVE_KITKAT;

/**
 * Created by jiayuanmin on 2018/3/28
 * description: 员工每月投放详情页面，分为有下属和无下属两种类型
 */
public class UserInvestMonthlyActivity extends BaseActivity {

    private static final String TAG = UserInvestMonthlyActivity.class.getSimpleName();
    @BindView(R.id.invest_list)
    EasyRecyclerView recyclerView;
    @BindView(R.id.invest_title)
    TextView title;

    @OnClick(R.id.invest_back)
    void onClickBack() {
        this.finish();
    }

    private RecyclerArrayAdapter<UserInvestCommonVO> mAdapter;
    private String mMonth;
    private String mUserId;
    private String mUserName;

    public static void naviToUserInvestMonth(Context context, String uid, String time, String name) {
        Intent intent = new Intent(context, UserInvestMonthlyActivity.class);
        intent.putExtra(SalesConstants.EXTRA_ID, uid);
        intent.putExtra(SalesConstants.EXTRA_INVEST_TIME, time);
        intent.putExtra(SalesConstants.EXTRA_NAME, name);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_invest_monthly);
        ButterKnife.bind(this);
        if (StatusBarHelper.statusBarLightMode(this) != StatusBarHelper.OTHER) {
            if (new SystemStatusManager(this).setTranslucentStatus(R.color.white) == STATUS_STRATEGY_ABOVE_KITKAT) {
                int statusHeight = ScreenUtil.getStatusBarHeight(this);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
                layoutParams.topMargin = statusHeight;
                recyclerView.setLayoutParams(layoutParams);
            }
        }
        mUserId = getIntent().getStringExtra(SalesConstants.EXTRA_ID);
        mMonth = getIntent().getStringExtra(SalesConstants.EXTRA_INVEST_TIME);
        mUserName = getIntent().getStringExtra(SalesConstants.EXTRA_NAME);
        title.setText(mUserName + " " + mMonth);
        initView();
        getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpEngine.getInstance().cancel(TAG);
    }

    void initView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerDecoration(ContextCompat.getColor(this, R.color.standard_line_light),
                getResources().getDimensionPixelOffset(R.dimen.margin_little_x)));
        mAdapter = new RecyclerArrayAdapter<UserInvestCommonVO>(this) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new UserInvestMonthlyHolder(parent, mMonth);
            }
        };
        recyclerView.setAdapter(mAdapter);
    }

    private void getData() {
        GetSuborInvestApi api = new GetSuborInvestApi(mUserId);
        api.setTimeType(GetSuborInvestApi.INVEST_TYPE_MONTH);
        api.setYearOrMonth(mMonth);
        api.setTag(TAG);
        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<SuborInvestModel>() {
            @Override
            public void onSuccess(SuborInvestModel result, String method) {
                if (result != null && result.getData() != null) {
                    //有下属
                    if (result.getData().getType() == 1) {
                        mAdapter.addAll(result.getData().getSubordinateList());
                    } else {
                        mAdapter.addAll(result.getData().getEstateList());
                    }
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onFailed(SuborInvestModel result, String method) {
                if (result != null) {
                    ToastUtil.toast(result.getMsg());
                }
            }
        });
    }
}
