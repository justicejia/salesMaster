package com.sohu.focus.salesmaster.comment;

import com.sohu.focus.salesmaster.kernal.bus.RxEvent;

/**
 * Created by luckyzhangx on 16/03/2018.
 */

public class DelCommentEvent extends RxEvent {
    private String dynamicId;
    private String commentId;

    public String getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(String dynamicId) {
        this.dynamicId = dynamicId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }
}
