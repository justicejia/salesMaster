package com.sohu.focus.salesmaster.http.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by luckyzhangx on 01/12/2017.
 */
public class PostUserId extends PageModel {
    @JsonProperty("personId")
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
