package com.sohu.focus.salesmaster.newFilter;

import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sohu.focus.salesmaster.newFilter.model.CommonFilterItemModel;
import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by jiayuanmin on 2018/5/21
 * description:
 */
public class FilterHolder extends BaseViewHolder<CommonFilterItemModel> {

    private TextView name;

    public FilterHolder(ViewGroup parent) {
        super(parent, R.layout.holder_filter);
        name = $(R.id.filter_holder_name);
    }

    @Override
    public void setData(CommonFilterItemModel data) {
        name.setText(data.getLabel());
        if (data.isSelected()) {
            name.setTextColor(ContextCompat.getColor(getContext(), R.color.home_icon_selected));
        } else {
            name.setTextColor(ContextCompat.getColor(getContext(), R.color.standard_text_light_black));
        }
    }
}
