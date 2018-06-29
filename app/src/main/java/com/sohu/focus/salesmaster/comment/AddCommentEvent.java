package com.sohu.focus.salesmaster.comment;

import com.sohu.focus.salesmaster.dynamics.model.DynamicCommentBean;
import com.sohu.focus.salesmaster.kernal.bus.RxEvent;

/**
 * Created by luckyzhangx on 16/03/2018.
 */

public class AddCommentEvent extends RxEvent {
    private String dynamicId;
    private DynamicCommentBean comment;

    public String getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(String dynamicId) {
        this.dynamicId = dynamicId;
    }

    public DynamicCommentBean getComment() {
        return comment;
    }

    public void setComment(DynamicCommentBean comment) {
        this.comment = comment;
    }
}
