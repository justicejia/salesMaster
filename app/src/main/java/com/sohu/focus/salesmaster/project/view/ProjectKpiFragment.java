package com.sohu.focus.salesmaster.project.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseFragment;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.http.api.GetProjKpiInfoApi;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestListener;
import com.sohu.focus.salesmaster.project.adapter.KpiHistoryHolder;
import com.sohu.focus.salesmaster.project.model.ProjKpiModel;
import com.sohu.focus.salesmaster.uiframe.Switch;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.EasyRecyclerView;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.decoration.DividerDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by luckyzhangx on 02/02/2018.
 */

public class ProjectKpiFragment extends BaseFragment implements Switch.OptionListener {

    private String mProjId;

    @BindView(R.id.kpi_desc1)
    TextView desc1;
    @BindView(R.id.kpi_value1)
    TextView value1;
    @BindView(R.id.kpi_desc2)
    TextView desc2;
    @BindView(R.id.kpi_value2)
    TextView value2;
    @BindView(R.id.kpi_desc3)
    TextView desc3;
    @BindView(R.id.kpi_value3)
    TextView value3;
    @BindView(R.id.kpi_desc4)
    TextView desc4;
    @BindView(R.id.kpi_value4)
    TextView value4;
    @BindView(R.id.kpi_desc5)
    TextView desc5;
    @BindView(R.id.kpi_value5)
    TextView value5;

    @BindView(R.id.kpi_history_list)
    EasyRecyclerView historyList;
    RecyclerArrayAdapter<ProjKpiModel.KpiHistory> mAdapter;

    @BindView(R.id.kpi_switch)
    Switch mSwitch;

    ProjKpiModel.DataBean data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_kpi, container, false);
        ButterKnife.bind(this, view);
        mSwitch.setOptionListener(this);

        Bundle bundle = getArguments();
        mProjId = bundle.getString(SalesConstants.EXTRA_PROJECT_ID);

        initRecyclerView();
        getKpiInfo();
        return view;
    }

    private void initRecyclerView() {
        historyList.setLayoutManager(new LinearLayoutManager(getContext()));
        historyList.addItemDecoration(new DividerDecoration(ContextCompat.getColor(getContext(),R.color.standard_line),2));
        mAdapter = new RecyclerArrayAdapter<ProjKpiModel.KpiHistory>(getContext()) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new KpiHistoryHolder(parent);
            }
        };
        historyList.setAdapterWithProgress(mAdapter);

    }

    private void getKpiInfo() {
        GetProjKpiInfoApi api = new GetProjKpiInfoApi(mProjId);
        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<ProjKpiModel>() {
            @Override
            public void onSuccess(ProjKpiModel result, String method) {
                if (result == null || result.data == null) {
                    return;
                }
                data = result.data;
                showData();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onFailed(ProjKpiModel result, String method) {

            }
        });
    }

    private void showData() {
        mAdapter.clear();
        mAdapter.addAll(data.history);
        mSwitch.setOption(Switch.OptionEnum.left);
    }

    private void showWeek() {
        desc1.setText("本周目标");
        desc2.setText("已完成");
        desc3.setText("完成率");
        desc4.setText("上周完成");
        desc5.setText("下周目标");

        value1.setText(data.week.thisWeekTarget);
        value2.setText(data.week.thisWeekFinish);
        value3.setText(data.week.thisWeekFinishRate);
        value4.setText(data.week.lastWeekFinish + "(" + data.week.lastWeekFinishRate + ")");
        value5.setText(data.week.nextWeekTarget);
    }

    private void showMonth() {
        desc1.setText("本月目标");
        desc2.setText("已完成");
        desc3.setText("完成率");
        desc4.setText("上月完成");
        desc5.setText("下月目标");

        value1.setText(data.month.thisMonthTarget);
        value2.setText(data.month.thisMonthFinish);
        value3.setText(data.month.thisMonthFinishRate);
        value4.setText(data.month.lastMonthFinish + "(" + data.month.lastMonthFinishRate + ")");
        value5.setText(data.month.nextMonthTarget);
    }

    @Override
    public void onOptionSelected(Switch.OptionEnum option) {
        if (option == Switch.OptionEnum.left) {
            showWeek();
        } else {
            showMonth();
        }
    }
}
