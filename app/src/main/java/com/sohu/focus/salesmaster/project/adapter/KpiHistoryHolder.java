package com.sohu.focus.salesmaster.project.adapter;

import android.view.ViewGroup;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.project.model.ProjKpiModel;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by luckyzhangx on 02/02/2018.
 */

public class KpiHistoryHolder extends BaseViewHolder<ProjKpiModel.KpiHistory> {

    TextView time;
    TextView desc;
    TextView rate;

    public KpiHistoryHolder(ViewGroup parentView) {
        super(parentView, R.layout.holder_kpi_history);
        bindView();
    }

    private void bindView() {
        time = (TextView) $(R.id.kpi_history_time);
        desc = (TextView) $(R.id.kpi_history_desc);
        rate = (TextView) $(R.id.kpi_history_percent);
    }

    @Override
    public void setData(ProjKpiModel.KpiHistory data) {
        time.setText(data.getMonth());
        desc.setText("目标：" + data.getKpiTarget() + "  完成：" + data.getKpiFinish());
        rate.setText("完成率：" + data.getKpiFinishRate());
    }
}
