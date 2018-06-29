package com.sohu.focus.salesmaster.comment;

import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.http.api.PostCommentApi;
import com.sohu.focus.salesmaster.http.model.PostComment;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestListener;
import com.sohu.focus.salesmaster.comment.model.PostCommentResultModel;

/**
 * Created by luckyzhangx on 12/03/2018.
 */

public class DynamicCommentManager {

    private static final DynamicCommentManager INSTANCE = new DynamicCommentManager();

    public static DynamicCommentManager getINSTANCE() {
        return INSTANCE;
    }

    public void postDynamicComment(PostComment comment,
                                   HttpRequestListener<PostCommentResultModel> listener) {
        PostCommentApi api = new PostCommentApi(comment);
        HttpEngine.getInstance().doHttpRequest(api, listener);
//        post 之后注意清掉缓存
    }

    public void cacheDynamicComment(PostComment review) {

    }

    public PostComment getCacheDynamicReview(String dynamicId) {
        PostComment comment = new PostComment();
        comment.dynamicId = dynamicId;
        return comment;
    }

    public PostComment getCacheDynamicReview(String dynamicId, String replyToUserId) {
        PostComment comment = new PostComment();
        comment.dynamicId = dynamicId;
        comment.replyToUserId = replyToUserId;
        return comment;
    }

    public void removeCacheDynamicReview(String dynamicId) {

    }
}
