package com.sohu.focus.salesmaster.http.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sohu.focus.salesmaster.client.model.ClientModel;

import java.io.Serializable;

/**
 * Created by luckyzhangx on 10/01/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddClient extends ClientModel implements Serializable {

    @JsonProperty("estateId")
    public String projId;
}
