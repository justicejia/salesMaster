package com.sohu.focus.salesmaster.goal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;

import java.io.Serializable;

/**
 * Created by luckyzhangx on 08/02/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ProjKpiHistory implements Serializable {
    @JsonProperty("estateId")
    public String projId;
    @JsonProperty("estateName")
    public String projName;
    public String kpiTarget;
    public String kpiFinishRate;
    public String kpiFinish;

    public String getProjId() {
        return CommonUtils.getDataNotNull(projId);
    }

    public String getProjName() {
        return CommonUtils.getDataNotNull(projName);
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
