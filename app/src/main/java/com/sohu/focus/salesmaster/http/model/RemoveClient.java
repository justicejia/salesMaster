package com.sohu.focus.salesmaster.http.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;

import java.io.Serializable;

/**
 * Created by luckyzhangx on 10/01/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class RemoveClient implements Serializable {

    public RemoveClient(String projId, String clientId) {
        this.projId = projId;
        this.clientId = clientId;
    }

    @JsonProperty("customerId")
    public String clientId;

    @JsonProperty("estateId")
    public String projId;
}
