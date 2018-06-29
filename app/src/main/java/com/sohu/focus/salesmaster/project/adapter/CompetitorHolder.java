package com.sohu.focus.salesmaster.project.adapter;

import android.view.ViewGroup;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.project.model.CompetitorItemModel;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by yuanminjia on 2018/1/12.
 */

public class CompetitorHolder extends BaseViewHolder<CompetitorItemModel> {

    private TextView company;
    private TextView price;

    public CompetitorHolder(ViewGroup itemView) {
        super(itemView, R.layout.holder_competitor);
        company = $(R.id.competitor_company);
        price = $(R.id.competitor_price);
    }

    @Override
    public void setData(CompetitorItemModel data) {
        company.setText(data.getCompany());
        price.setText(data.getPrice());

    }
}
