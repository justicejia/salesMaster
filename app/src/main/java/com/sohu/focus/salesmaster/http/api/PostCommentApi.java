package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;
import com.sohu.focus.salesmaster.http.model.PostComment;

import rx.Observable;

/**
 * Created by luckyzhangx on 14/03/2018.
 */

public class PostCommentApi extends FocusBaseApi {

    private PostComment postModel;

    public PostCommentApi(PostComment postModel) {
        this.postModel = postModel;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        return methods.postComment(postModel);
    }
}
