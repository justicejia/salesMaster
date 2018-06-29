package com.sohu.focus.salesmaster.comment.model;

import com.sohu.focus.salesmaster.dynamics.model.DynamicCommentBean;
import com.sohu.focus.salesmaster.kernal.http.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by luckyzhangx on 14/03/2018.
 */

public class UnreadCommentSetModel extends BaseModel {

    public DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        public int count;
        public List<DynamicCommentBean> comments;

        public int getCount() {
            return count;
        }

        public List<DynamicCommentBean> getComments() {
            return comments;
        }
    }
}
