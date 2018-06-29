package com.sohu.focus.salesmaster.project.adapter;

import android.view.ViewGroup;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.project.model.ProjectInvestModel;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by jiayuanmin on 2018/4/23
 * description:
 */
public class ProjectInvestHolder extends BaseViewHolder<ProjectInvestModel.DataBean.HistoryBean> {

    private TextView date;
    private TextView money;

    public ProjectInvestHolder(ViewGroup parent) {
        super(parent, R.layout.holder_project_invest);
        date = $(R.id.proj_invest_item_date);
        money = $(R.id.proj_invest_item_money);
    }

    @Override
    public void setData(ProjectInvestModel.DataBean.HistoryBean data) {
        date.setText(data.getMonth() + "：");
        money.setText(data.getAdMoney());
    }
}
