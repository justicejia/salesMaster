package com.sohu.focus.salesmaster.main.adapter;

import android.view.ViewGroup;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.project.model.ProjectModelSet;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by yuanminjia on 2017/10/30.
 */

public class ProjectHolder extends BaseViewHolder<ProjectModelSet.DataBean.ListBean> {

    TextView projectName;

    public ProjectHolder(ViewGroup itemView) {
        super(itemView, R.layout.holder_project);
        projectName = $(R.id.project_item_build);
    }

    @Override
    public void setData(ProjectModelSet.DataBean.ListBean data) {
        super.setData(data);
        projectName.setText(data.getProjectName());
    }
}
