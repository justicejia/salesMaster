package com.sohu.focus.salesmaster.goal.adapter;

import android.view.ViewGroup;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.goal.model.SubKpiHistory;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by luckyzhangx on 05/02/2018.
 */

public class UserKpiHolder extends BaseViewHolder<SubKpiHistory> {

    TextView name, desc, rate;

    public UserKpiHolder(ViewGroup parent) {
        super(parent, R.layout.holder_kpi_history);
        bindView();
    }

    private void bindView() {
        name = $(R.id.kpi_history_time);
        desc = $(R.id.kpi_history_desc);
        rate = $(R.id.kpi_history_percent);
    }

    @Override
    public void setData(SubKpiHistory data) {
        name.setText(data.getPersonName());
        desc.setText("目标：" + data.getKpiTarget() + "  完成：" + data.getKpiFinish());
        rate.setText("完成率：" + data.getKpiFinishRate());
    }
}
