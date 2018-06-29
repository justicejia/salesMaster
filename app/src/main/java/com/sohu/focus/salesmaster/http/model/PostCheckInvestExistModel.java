package com.sohu.focus.salesmaster.http.model;

import java.io.Serializable;

/**
 * Created by jiayuanmin on 2018/4/24
 * description:
 */
public class PostCheckInvestExistModel implements Serializable {
    private String estateId;
    private String month;

    public String getEstateId() {
        return estateId;
    }

    public void setEstateId(String estateId) {
        this.estateId = estateId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
