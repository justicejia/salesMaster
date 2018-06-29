package com.sohu.focus.salesmaster.filter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by luckyzhangx on 15/01/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjOrderFilterDTO extends BaseFilterDTO {
    {
        fieldName = FieldNameManager.ESTATE_ORDER;
    }
}
