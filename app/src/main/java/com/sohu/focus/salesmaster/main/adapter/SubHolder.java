package com.sohu.focus.salesmaster.main.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.login.AccountManager;
import com.sohu.focus.salesmaster.subordinate.model.SuborModel;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by yuanminjia on 2017/10/30.
 */

public class SubHolder extends BaseViewHolder<SuborModel.DataBean.ListBean> {
    private TextView name;
    private TextView tag;

    public SubHolder(ViewGroup itemView) {
        super(itemView, R.layout.holder_subor);
        name = $(R.id.sub_item_name);
        tag = $(R.id.sub_role_tag);
    }

    @Override
    public void setData(SuborModel.DataBean.ListBean data) {
        if (data != null) {
            name.setText(data.getPersonName());
            if (data.getSalesRole() == SalesConstants.USER_ROLE_SALES && AccountManager.INSTANCE.getUserViewRole()) {
                tag.setVisibility(View.VISIBLE);
                tag.setText(data.getCityName() + "销售");
            } else if (data.getSalesRole() == SalesConstants.USER_ROLE_OPERATING && AccountManager.INSTANCE.getUserViewRole()) {
                tag.setVisibility(View.VISIBLE);
                tag.setText(data.getCityName() + "运营");

            } else {
                tag.setVisibility(View.GONE);
            }
        }

    }
}
