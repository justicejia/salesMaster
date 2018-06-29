package com.sohu.focus.salesmaster.comment.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.dynamics.model.DynamicCommentBean;
import com.sohu.focus.salesmaster.dynamics.view.DynamicDetailActivity;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.kernal.utils.ParseUtil;
import com.sohu.focus.salesmaster.login.AccountManager;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;
import com.sohu.focus.salesmaster.utils.DateUtils;

/**
 * Created by luckyzhangx on 14/03/2018.
 */

public class UnreadCommentHolder extends BaseViewHolder<DynamicCommentBean> {

    TextView personAction, comment, date, proj;

    public UnreadCommentHolder(ViewGroup parent) {
        super(parent, R.layout.holder_comment);

        personAction = $(R.id.person_action);
        comment = $(R.id.content);
        date = $(R.id.date);

        proj = $(R.id.project);
    }

    @Override
    public void setData(final DynamicCommentBean data) {
        String action;
        if (CommonUtils.isEmpty(data.getReplyToUserName())) {
            action = "评论了你";
        } else if (data.getReplyToUserId().equals(AccountManager.INSTANCE.getUserId())) {
            action = "回复了你";
        } else {
            action = "回复了" + data.getReplyToUserName();
        }

        personAction.setText(data.getSalesUserName() + " " + action);
        comment.setText(data.getContent());
        date.setText(
                DateUtils.getSimpleTime(ParseUtil.parseLong(data.getTimeStamp(), -1),
                        getContext()));

        proj.setText(data.getProjName() + "：" + data.getProjStage());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DynamicDetailActivity.showDynamicDetail(getContext(), data.getDynamicId());
            }
        });
    }

}
