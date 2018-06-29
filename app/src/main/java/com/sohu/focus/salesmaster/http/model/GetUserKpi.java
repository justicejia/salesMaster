package com.sohu.focus.salesmaster.http.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by luckyzhangx on 02/02/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetUserKpi implements Serializable {

    public GetUserKpi(String userId) {
        this.userId = userId;
    }

    @JsonProperty("personId")
    public String userId;
}
