package com.sohu.focus.salesmaster.goal.adapter;

import android.view.ViewGroup;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.goal.model.ProjKpiHistory;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by luckyzhangx on 05/02/2018.
 */

public class ProjKpiHolder extends BaseViewHolder<ProjKpiHistory> {

    TextView name, desc, rate;

    public ProjKpiHolder(ViewGroup parent) {
        super(parent, R.layout.holder_kpi_history);
        bindView();
    }

    private void bindView() {
        name = $(R.id.kpi_history_time);
        desc = $(R.id.kpi_history_desc);
        rate = $(R.id.kpi_history_percent);
    }

    @Override
    public void setData(ProjKpiHistory data) {
        name.setText(data.getProjName());
        desc.setText("目标：" + data.getKpiTarget() + "  完成：" + data.getKpiFinish());
        rate.setText("完成率：" + data.getKpiFinishRate());
    }
}
