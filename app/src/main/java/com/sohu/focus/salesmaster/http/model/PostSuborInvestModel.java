package com.sohu.focus.salesmaster.http.model;

import java.io.Serializable;

/**
 * Created by jiayuanmin on 2018/4/26
 * description:
 */
public class PostSuborInvestModel implements Serializable {
    private String personId;
    private String yearOrMonth;
    private String timeType;

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getYearOrMonth() {
        return yearOrMonth;
    }

    public void setYearOrMonth(String yearOrMonth) {
        this.yearOrMonth = yearOrMonth;
    }


    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }
}
