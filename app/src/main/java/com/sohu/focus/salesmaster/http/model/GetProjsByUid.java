package com.sohu.focus.salesmaster.http.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by luckyzhangx on 2017/11/2.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetProjsByUid extends PageModel {
    public String personId;
    public boolean includeSubordinate;
    public String keyword = "";
    @JsonProperty("areaCodeList")
    public List<String> citys;
    @JsonProperty("order")
    public String projOrder;

    public void setParams(int page, int pageSize, String personId, boolean includeSubordinate, String keyword) {
        this.page = page;
        this.pageSize = pageSize;
        this.personId = personId;
        this.includeSubordinate = includeSubordinate;
        this.keyword = keyword;
    }


}
