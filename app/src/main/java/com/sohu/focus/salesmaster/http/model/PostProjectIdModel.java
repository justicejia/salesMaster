package com.sohu.focus.salesmaster.http.model;

import java.io.Serializable;

/**
 * Created by jiayuanmin on 2018/4/23
 * description:
 */
public class PostProjectIdModel implements Serializable {
    private String estateId;

    public String getEstateId() {
        return estateId;
    }

    public void setEstateId(String estateId) {
        this.estateId = estateId;
    }
}
