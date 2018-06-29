package com.sohu.focus.salesmaster.invest.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseWebViewActivity;
import com.sohu.focus.salesmaster.invest.model.UserInvestCommonVO;
import com.sohu.focus.salesmaster.invest.view.UserInvestMonthlyActivity;
import com.sohu.focus.salesmaster.project.view.ProjectActivity;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by jiayuanmin on 2018/3/29
 * description:
 */
public class UserInvestMonthlyHolder extends BaseViewHolder<UserInvestCommonVO> {

    private TextView name;
    private TextView money;
    private ImageView arrow;
    private String mMonth;

    public UserInvestMonthlyHolder(ViewGroup parent, String month) {
        super(parent, R.layout.holder_invest_monthly);
        name = $(R.id.name);
        money = $(R.id.money);
        arrow = $(R.id.arrow);
        mMonth = month;
    }

    @Override
    public void setData(final UserInvestCommonVO data) {
        if (data.isHasSub()) {
            arrow.setVisibility(View.VISIBLE);
            name.setText(data.personName + "：");
            money.setText(data.adMoney);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserInvestMonthlyActivity.naviToUserInvestMonth(getContext(),
                            data.personId, mMonth, data.personName);
                }
            });
        } else {
            arrow.setVisibility(View.GONE);
            name.setText(data.estateName + "：");
            money.setText(data.adMoney);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProjectActivity.showDynamics(getContext(), data.estateId, data.estateName);
                }
            });
        }
    }
}
