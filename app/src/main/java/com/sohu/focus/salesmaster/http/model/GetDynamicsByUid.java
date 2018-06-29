package com.sohu.focus.salesmaster.http.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by luckyzhangx on 2017/11/3.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetDynamicsByUid extends PageModel {
    @JsonProperty("personId")
    public String uid = "";
    public boolean includeSubordinate;

    @JsonProperty("areaCodeList")
    public List<String> city;
    @JsonProperty("salesRoleIdList")
    public List<String> salesRole;
    @JsonProperty("salesProcessIdList")
    public List<String> progress;

    public void setParams(String personId, boolean includeSubordinate, int page, int pageSize) {
        this.uid = personId;
        this.includeSubordinate = includeSubordinate;
        this.page = page;
        this.pageSize = pageSize;
    }
}
