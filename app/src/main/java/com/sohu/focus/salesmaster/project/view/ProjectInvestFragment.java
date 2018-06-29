package com.sohu.focus.salesmaster.project.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseFragment;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.http.api.GetProjectInvestApi;
import com.sohu.focus.salesmaster.invest.view.AddInvestActivity;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestListener;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;
import com.sohu.focus.salesmaster.project.adapter.ProjectInvestHolder;
import com.sohu.focus.salesmaster.project.model.ProjectInvestModel;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.EasyRecyclerView;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.decoration.DividerDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 项目投放页
 */
public class ProjectInvestFragment extends BaseFragment {

    private final static int REQUEST_ADD_INVEST = 1;
    private static final String TAG = "ProjectInvestFragment";

    @BindView(R.id.proj_invest_target_money)
    TextView targetMoney;
    @BindView(R.id.proj_invest_target_unit)
    TextView targetUnit;
    @BindView(R.id.proj_invest_complete_money)
    TextView completeMoney;
    @BindView(R.id.proj_invest_complete_unit)
    TextView completeUnit;
    @BindView(R.id.proj_invest_rate_money)
    TextView rateMoney;
    @BindView(R.id.proj_invest_rate_unit)
    TextView rateUnit;

    @BindView(R.id.project_invest_goal_accumulate)
    TextView accumulate;
    @BindView(R.id.project_invest_goal_year)
    TextView year;
    @BindView(R.id.project_invest_goal_list)
    EasyRecyclerView recyclerView;

    private GetProjectInvestApi api;
    private RecyclerArrayAdapter<ProjectInvestModel.DataBean.HistoryBean> mAdapter;

    @OnClick(R.id.project_invest_goal_add)
    void onClickAdd() {
        Intent intent = new Intent(getContext(), AddInvestActivity.class);
        intent.putExtra(SalesConstants.EXTRA_PROJECT_ID, mProjectId);
        startActivityForResult(intent, REQUEST_ADD_INVEST);
    }

    private String mProjectId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_invest, container, false);
        ButterKnife.bind(this, view);
        mProjectId = getArguments().getString(SalesConstants.EXTRA_PROJECT_ID);
        initView();
        loadData();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HttpEngine.getInstance().cancel(TAG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                loadData();
            }
        }
    }

    void initView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerDecoration(ContextCompat.getColor(getContext(), R.color.standard_line),
                getContext().getResources().getDimensionPixelOffset(R.dimen.margin_half)));
        mAdapter = new RecyclerArrayAdapter<ProjectInvestModel.DataBean.HistoryBean>(getContext()) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new ProjectInvestHolder(parent);
            }
        };
        recyclerView.setAdapter(mAdapter);
    }

    void loadData() {
        api = new GetProjectInvestApi(mProjectId);
        api.setTag(TAG);
        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<ProjectInvestModel>() {
            @Override
            public void onSuccess(ProjectInvestModel result, String method) {
                if (result != null) {
                    loadViewData(result.getData());
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onFailed(ProjectInvestModel result, String method) {
                if (result != null)
                    ToastUtil.toast(result.getMsg());
            }
        });
    }

    void loadViewData(ProjectInvestModel.DataBean dataBean) {
        year.setText(dataBean.getYear() + "年投放目标");
        accumulate.setText("累计投放：" + CommonUtils.getDataNotNull(dataBean.getAdMoneyTotal(), "0万"));
        mAdapter.addAll(dataBean.getHistory());

        String targetStr = dataBean.getTargetMoney();
        String completeStr = dataBean.getAdMoney();
        String rateStr = dataBean.getFinishRate();

        if (notEmpty(targetStr)) {
            if (targetStr.contains("万")) {
                targetMoney.setText(getShowString(targetStr.substring(0, targetStr.indexOf("万"))));
                targetUnit.setText(" 万");
            } else {
                targetMoney.setText(targetStr);
            }
        } else {
            targetMoney.setText(targetStr);
        }

        if (notEmpty(completeStr)) {
            if (completeStr.contains("万")) {
                completeMoney.setText(getShowString(completeStr.substring(0, completeStr.indexOf("万"))));
                completeUnit.setText(" 万");
            } else {
                completeMoney.setText(completeStr);
            }
        } else {
            completeMoney.setText(completeStr);
        }

        if (notEmpty(rateStr)) {
            if (rateStr.contains("%")) {
                rateMoney.setText(rateStr.substring(0, rateStr.indexOf("%")));
                rateUnit.setText(" %");
            } else {
                rateMoney.setText(rateStr);
            }
        } else {
            rateMoney.setText(rateStr);
        }
        mAdapter.clear();
        mAdapter.addAll(dataBean.getHistory());

    }

    private String getShowString(String money) {
        if (money.length() < 5) {
            return money;
        }
        String result;
        //小数
        if (money.contains(".")) {
            int numInt = Integer.parseInt(money.substring(0, money.indexOf(".")));
            String dotStr = money.substring(money.indexOf(".") + 1, money.length());
            int dotFirstInt = Integer.parseInt(money.substring(money.indexOf(".") + 1, money.indexOf(".") + 2));
            //大于1000万，取整数
            if (numInt >= 1000) {
                if (dotFirstInt >= 5) {
                    numInt++;
                }
                result = numInt + "";
            }
            //小于1000万，取一位小数
            else {
                if (dotStr.length() > 1) {
                    if (Integer.parseInt(dotStr.substring(1, 2)) >= 5) {
                        dotFirstInt++;
                    }
                    result = numInt + "." + dotFirstInt;
                } else {
                    result = money;
                }
            }
        } else {
            result = money.substring(0, 3) + "..";
        }
        return result;
    }


    boolean notEmpty(String content) {
        return CommonUtils.notEmpty(content) && !content.equals("--");
    }
}
