package com.sohu.focus.salesmaster.filter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sohu.focus.salesmaster.filter.base.FilterVO;
import com.sohu.focus.salesmaster.kernal.http.BaseMappingModel;

/**
 * Created by luckyzhangx on 15/01/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseFilterDTO extends BaseMappingModel<FilterVO> {

    @Override
    public FilterVO transform() {
        FilterVO vo = new FilterVO(desc, fieldName, fieldValue);
        return vo;
    }

    public String fieldName = "";
    @JsonProperty("label")
    public String desc;
    @JsonProperty("option")
    public String fieldValue;
}
