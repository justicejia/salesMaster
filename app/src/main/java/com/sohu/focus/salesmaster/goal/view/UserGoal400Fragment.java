package com.sohu.focus.salesmaster.goal.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseFragment;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.http.api.GetUserKpiApi;
import com.sohu.focus.salesmaster.http.model.GetUserKpi;
import com.sohu.focus.salesmaster.http.model.GetUsrKpiHistory;
import com.sohu.focus.salesmaster.http.model.GetUsrKpiHistory2;
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
import butterknife.OnClick;

/**
 * Created by luckyzhangx on 02/02/2018.
 */

public class UserGoal400Fragment extends BaseFragment implements Switch.OptionListener {

    private String mUserId;

    //    view
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
    EasyRecyclerView recyclerView;
    RecyclerArrayAdapter<ProjKpiModel.KpiHistory> mAdapter;

    @BindView(R.id.kpi_switch)
    Switch mSwitch;

    private ProjKpiModel.DataBean data;
    private String type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mUserId = getArguments().getString(SalesConstants.EXTRA_ID);

        View view = inflater.inflate(R.layout.fragment_user_kpi, container, false);
        ButterKnife.bind(this, view);
        mSwitch.setOptionListener(this);

        initRecyclerView();
        getKpiInfo();
        return view;
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerDecoration(ContextCompat.getColor(getContext(), R.color.standard_line_light), 2));


        mAdapter = new RecyclerArrayAdapter<ProjKpiModel.KpiHistory>(getContext()) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new KpiHistoryHolder(parent);
            }
        };

        mAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                GetUsrKpiHistory2 param = new GetUsrKpiHistory2(mUserId);
                param.month = (mAdapter.getItem(position)).getMonth();

                UsrKpiHistoryActivity2.show(getContext(), param);
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    private void getKpiInfo() {

        GetUserKpi model = new GetUserKpi(mUserId);
        GetUserKpiApi api = new GetUserKpiApi(model);

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
        if (data == null) return;

        type = GetUsrKpiHistory.Type.WEEK.type;

        desc1.setText("本周目标");
        desc2.setText("已完成");
        desc3.setText("完成率");
        desc4.setText("上周完成");
        desc5.setText("下周目标");

        value1.setText(data.week.thisWeekTarget);
        value2.setText(data.week.thisWeekFinish);

        String rate = data.week.thisWeekFinishRate.trim();
        if (rate.contains("%")) {
            String rateWithSpace = rate.substring(0, rate.indexOf("%")) + " %";
            SpannableString spannableString = new SpannableString(rateWithSpace);
            spannableString.setSpan(new RelativeSizeSpan(0.6f), rateWithSpace.indexOf(" "),
                    rateWithSpace.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            value3.setText(spannableString);
        } else {
            value3.setText(data.week.thisWeekFinishRate);
        }
        value4.setText(data.week.lastWeekFinish + "(" + data.week.lastWeekFinishRate + ")");
        value5.setText(data.week.nextWeekTarget);
    }

    private void showMonth() {
        if (data == null) return;

        type = GetUsrKpiHistory.Type.MONTH.type;

        desc1.setText("本月目标");
        desc2.setText("已完成");
        desc3.setText("完成率");
        desc4.setText("上月完成");
        desc5.setText("下月目标");

        value1.setText(data.month.thisMonthTarget);
        value2.setText(data.month.thisMonthFinish);
        String rate = data.month.thisMonthFinishRate.trim();
        if (rate.contains("%")) {
            String rateWithSpace = rate.substring(0, rate.indexOf("%")) + " %";
            SpannableString spannableString = new SpannableString(rateWithSpace);
            spannableString.setSpan(new RelativeSizeSpan(0.6f), rateWithSpace.indexOf(" "),
                    rateWithSpace.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            value3.setText(spannableString);
        } else {
            value3.setText(data.month.thisMonthFinishRate);
        }
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

    @OnClick(R.id.partition_1)
    public void showThisPartion() {
        GetUsrKpiHistory model = new GetUsrKpiHistory(mUserId);
        model.partition = GetUsrKpiHistory.Partition.THIS.partition;
        model.type = type;

        UsrKpiHistoryActivity.showUserKpiHistory(getContext(), model);
    }

    @OnClick(R.id.partition_2)
    public void showLastPartion() {
        GetUsrKpiHistory model = new GetUsrKpiHistory(mUserId);
        model.partition = GetUsrKpiHistory.Partition.LAST.partition;
        model.type = type;

        UsrKpiHistoryActivity.showUserKpiHistory(getContext(), model);
    }

    @OnClick(R.id.partition_3)
    public void showNextPartion() {
        GetUsrKpiHistory model = new GetUsrKpiHistory(mUserId);
        model.partition = GetUsrKpiHistory.Partition.NEXT.partition;
        model.type = type;

        UsrKpiHistoryActivity.showUserKpiHistory(getContext(), model);
    }
}
