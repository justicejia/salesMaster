package com.sohu.focus.salesmaster.invest.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.invest.model.UserInvestCommonVO;
import com.sohu.focus.salesmaster.invest.view.UserInvestYearlyActivity;
import com.sohu.focus.salesmaster.project.view.ProjectActivity;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by jiayuanmin on 2018/3/29
 * description:
 */
public class UserInvestYearlyHolder extends BaseViewHolder<UserInvestCommonVO> {

    private TextView name;
    private TextView detail;
    private TextView rate;
    private String year;

    public UserInvestYearlyHolder(ViewGroup itemView, String year) {
        super(itemView, R.layout.holder_invest_comp_with_sub);
        name = $(R.id.user_invest_com_name);
        detail = $(R.id.user_invest_com_detail);
        rate = $(R.id.user_invest_com_rate);
        this.year = year;
    }

    @Override
    public void setData(final UserInvestCommonVO data) {
        if (data.isHasSub()) {
            name.setText(data.personName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserInvestYearlyActivity.naviToUserInvestYear(getContext(),
                            data.personId, year, data.personName);
                }
            });
        } else {
            name.setText(data.estateName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProjectActivity.showDynamics(getContext(), data.estateId, data.estateName);
                }
            });
        }
        detail.setText("目标：" + data.targetMoney + " " + "完成：" + data.adMoney);
        rate.setText("完成率：" + data.finishRate);
    }
}
