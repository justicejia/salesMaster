package com.sohu.focus.salesmaster.main.adapter;

import android.view.ViewGroup;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.project.model.ProjectModelSet;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by yuanminjia on 2017/11/7.
 */

public class HomeProjectHolder extends BaseViewHolder<ProjectModelSet.DataBean.ListBean> {
    TextView projectName;
    TextView projectManager;
    TextView projectScore;

    public HomeProjectHolder(ViewGroup itemView) {
        super(itemView, R.layout.holder_home_project);
        projectName = $(R.id.home_project_name);
        projectManager = $(R.id.home_project_manager);
        projectScore = $(R.id.project_score);
    }

    @Override
    public void setData(ProjectModelSet.DataBean.ListBean data) {
        if (data == null) return;
        projectName.setText(data.getProjectName());
        String operatingName = "";
        String salesName = "";
        StringBuilder manager = new StringBuilder();
        if (data.getUserList() != null && data.getUserList().size() > 0) {
            for (ProjectModelSet.DataBean.ListBean.UserListBean user : data.getUserList()) {
                if (user.getSalesUserRole() == SalesConstants.USER_ROLE_SALES) {
                    salesName = user.getPersonName();
                } else if (user.getSalesUserRole() == SalesConstants.USER_ROLE_OPERATING) {
                    operatingName = user.getPersonName();
                }
            }
        }
        if (CommonUtils.notEmpty(salesName)) {
            manager.append("销售：").append(salesName).append(" ");
        }
        if (CommonUtils.notEmpty(operatingName)) {
            manager.append("运营：").append(operatingName);
        }
        projectManager.setText(manager.toString());
        switch (data.getCurrentShowStatus()) {
            case 0:
                projectScore.setText(data.getScore());
                break;
            case 1:
                projectScore.setText(data.getAdMoney());
                break;
            case 2:
                projectScore.setText(data.getIsCalledDistinct());
        }
    }
}
