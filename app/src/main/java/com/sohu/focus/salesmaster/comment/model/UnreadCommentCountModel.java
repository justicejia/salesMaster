package com.sohu.focus.salesmaster.comment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sohu.focus.salesmaster.kernal.http.BaseModel;

import java.io.Serializable;

/**
 * Created by luckyzhangx on 14/03/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class UnreadCommentCountModel extends BaseModel {

    public DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataBean implements Serializable {
        public String commentator;
        public int count;

        public String getCommentator() {
            return commentator;
        }

        public int getCount() {
            return count;
        }
    }
}
