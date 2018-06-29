package com.sohu.focus.salesmaster.progress.adapter;

import android.view.ViewGroup;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.project.model.ProjectModelSet;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by yuanminjia on 2017/12/7.
 */

public class SelectProjectHolder extends BaseViewHolder<ProjectModelSet.DataBean.ListBean> {

    TextView projectName;

    public SelectProjectHolder(ViewGroup itemView) {
        super(itemView, R.layout.holder_select_project);
        projectName = $(R.id.select_project_name);
    }

    @Override
    public void setData(ProjectModelSet.DataBean.ListBean data) {
        if (data == null) return;
        projectName.setText(data.getProjectName());
    }
}
