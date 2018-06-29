package com.sohu.focus.salesmaster.project.adapter;

import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.project.view.ProjectDataFragment;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by yuanminjia on 2018/1/11.
 */

public class ChartTopHolder extends BaseViewHolder<ProjectDataFragment.ItemModel> {
    private TextView content;

    public ChartTopHolder(ViewGroup view) {
        super(view, R.layout.holder_project_chart_top);
        content = $(R.id.content);
    }

    @Override
    public void setData(ProjectDataFragment.ItemModel data) {
        if (data != null) {
            content.setText(data.getContent());
            if (data.isSelected())
                content.setTextColor(ContextCompat.getColor(getContext(), R.color.home_icon_selected));
            else
                content.setTextColor(ContextCompat.getColor(getContext(), R.color.standard_text_light_gray));
        }
    }
}
