package com.sohu.focus.salesmaster.invest.adapter;

import android.view.ViewGroup;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.invest.model.UserInvestInfoModel;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by jiayuanmin on 2018/4/25
 * description:
 */
public class UserInvestHistoryHolder extends BaseViewHolder<UserInvestInfoModel.DataBean.HistoryBean> {

    private TextView time;
    private TextView money;

    public UserInvestHistoryHolder(ViewGroup parent) {
        super(parent, R.layout.holder_invest_user_history);
        time = $(R.id.user_invest_item_date);
        money = $(R.id.user_invest_item_money);
    }

    @Override
    public void setData(UserInvestInfoModel.DataBean.HistoryBean data) {
        time.setText(data.getMonth());
        money.setText(data.getAdMoney());
    }
}
