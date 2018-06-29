package com.sohu.focus.salesmaster.comment;

import com.sohu.focus.salesmaster.dynamics.model.DynamicCommentBean;

/**
 * Created by luckyzhangx on 20/03/2018.
 */

public interface UpdateComment {
    boolean delComment(String dynamicId, String commentId);

    boolean addComment(String dynamicId, DynamicCommentBean comment);
}
