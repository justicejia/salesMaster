package com.sohu.focus.salesmaster.http.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sohu.focus.salesmaster.kernal.utils.ParseUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created by luckyzhangx on 11/6/2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetDynamicsByProjId extends PageModel {
    @JsonProperty("estateId")
    long projId = 0;

//    @JsonProperty("areaCodeList")
//    public List<String> city;
    @JsonProperty("salesRoleIdList")
    public List<String> salesRole;
    @JsonProperty("salesProcessIdList")
    public List<String> progress;

    public void setProjId(long projId) {
        this.projId = projId;
    }

    public long getProjId() {
        return projId;
    }
}
