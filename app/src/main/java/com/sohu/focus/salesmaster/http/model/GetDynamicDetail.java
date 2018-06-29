package com.sohu.focus.salesmaster.http.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by luckyzhangx on 14/03/2018.
 */

public class GetDynamicDetail implements Serializable {
    @JsonProperty("projectId")
    public String dynamicId;

    public void setDynamicId(String dynamicId) {
        this.dynamicId = dynamicId;
    }
}
