package com.sohu.focus.salesmaster.comment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sohu.focus.salesmaster.comment.DynamicCommentManager;
import com.sohu.focus.salesmaster.dynamics.model.DynamicCommentBean;
import com.sohu.focus.salesmaster.dynamics.model.DynamicSetModel;
import com.sohu.focus.salesmaster.kernal.http.BaseModel;

/**
 * Created by luckyzhangx on 14/03/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostCommentResultModel extends BaseModel {

    public DynamicCommentBean data;

    public DynamicCommentBean getData() {
        return data;
    }

    public void setData(DynamicCommentBean data) {
        this.data = data;
    }

}
