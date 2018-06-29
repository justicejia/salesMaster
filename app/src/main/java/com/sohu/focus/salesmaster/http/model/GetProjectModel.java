package com.sohu.focus.salesmaster.http.model;

import java.io.Serializable;

/**
 * Created by yuanminjia on 2018/1/10.
 */

public class GetProjectModel implements Serializable {
    private String estateId;
    private String type;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEstateId() {
        return estateId;
    }

    public void setEstateId(String estateId) {
        this.estateId = estateId;
    }
}
