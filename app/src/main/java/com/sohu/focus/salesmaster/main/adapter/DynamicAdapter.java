package com.sohu.focus.salesmaster.main.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.sohu.focus.salesmaster.comment.UpdateComment;
import com.sohu.focus.salesmaster.dynamics.model.DynamicCommentBean;
import com.sohu.focus.salesmaster.dynamics.model.DynamicSetModel;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.List;

public class DynamicAdapter extends RecyclerArrayAdapter<DynamicSetModel.DataBean.ListBean>
        implements UpdateComment {

    public DynamicAdapter(Context context) {
        super(context);
    }

    public DynamicAdapter(Context context, DynamicSetModel.DataBean.ListBean[] objects) {
        super(context, objects);
    }

    public DynamicAdapter(Context context, List<DynamicSetModel.DataBean.ListBean> objects) {
        super(context, objects);
    }

    DynamicHolder.DelCallBack delCallBack;
    DynamicHolder.ShowProjectDetailCallBack showProjectDetailCallBack;

    public DynamicHolder.DelCallBack getDelCallBack() {
        return delCallBack;
    }

    public void setDelCallBack(DynamicHolder.DelCallBack delCallBack) {
        this.delCallBack = delCallBack;
    }

    public DynamicHolder.ShowProjectDetailCallBack getShowProjectDetailCallBack() {
        return showProjectDetailCallBack;
    }

    public void setShowProjectDetailCallBack(DynamicHolder.ShowProjectDetailCallBack showProjectDetailCallBack) {
        this.showProjectDetailCallBack = showProjectDetailCallBack;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        DynamicHolder holder = new DynamicHolder(parent);
        holder.setDelCallBack(delCallBack);
        holder.setShowProjectDetailCallBack(showProjectDetailCallBack);
        return holder;
    }

    @Override
    public boolean delComment(String dynamicId, String commentId) {
        for (DynamicSetModel.DataBean.ListBean dynamic : getAllData()) {
            if (dynamic.getSalesProjectID().equals(dynamicId)) {
                for (DynamicCommentBean comment : dynamic.getCommentList()) {
                    if (comment.getCommentId().equals(commentId)) {
                        dynamic.getCommentList().remove(comment);
                        notifyDataSetChanged();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean addComment(String dynamicId, DynamicCommentBean comment) {
        for (DynamicSetModel.DataBean.ListBean dynamic : getAllData()) {
            if (dynamic.getSalesProjectID().equals(dynamicId)) {
                for (DynamicCommentBean commentBean : dynamic.getCommentList()) {
                    if (commentBean.getCommentId().equals(comment.commentId)) {
                        return false;
                    }
                }
                dynamic.getCommentList().add(comment);
                notifyDataSetChanged();
            }
        }
        return false;
    }
}
