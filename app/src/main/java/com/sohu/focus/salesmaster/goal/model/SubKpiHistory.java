package com.sohu.focus.salesmaster.goal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;

import java.io.Serializable;

/**
 * Created by luckyzhangx on 08/02/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SubKpiHistory implements Serializable {
    public String personName;
    public String personRole;
    public String personId;
    public String kpiTarget;
    public String kpiFinishRate;
    public String kpiFinish;

    public String getPersonName() {
        return CommonUtils.getDataNotNull(personName);
    }

    public String getPersonRole() {
        return CommonUtils.getDataNotNull(personRole);
    }

    public String getPersonId() {
        return CommonUtils.getDataNotNull(personId);
    }

    public String getKpiTarget() {
        return CommonUtils.getDataNotNull(kpiTarget);
    }

    public String getKpiFinishRate() {
        return CommonUtils.getDataNotNull(kpiFinishRate);
    }

    public String getKpiFinish() {
        return CommonUtils.getDataNotNull(kpiFinish);
    }
}
