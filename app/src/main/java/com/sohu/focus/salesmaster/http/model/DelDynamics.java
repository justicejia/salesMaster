package com.sohu.focus.salesmaster.http.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by luckyzhangx on 11/6/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DelDynamics implements Serializable {
    @JsonProperty("salesProjectIdList")
    String[] dynamicIds;

    public String[] getDynamicIds() {
        return dynamicIds;
    }

    public void setDynamicId(String dynamicId) {
        dynamicIds = new String[1];
        dynamicIds[0] = dynamicId + "";
    }

    public void setDynamicIds(String[] dynamicIds) {
        this.dynamicIds = dynamicIds;
    }
}
