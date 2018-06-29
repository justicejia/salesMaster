package com.sohu.focus.salesmaster.http.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by luckyzhangx on 14/03/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetUnreadCommentCount implements Serializable {
    public String personId;

    public void setPersonId(String personId) {
        this.personId = personId;
    }
}
