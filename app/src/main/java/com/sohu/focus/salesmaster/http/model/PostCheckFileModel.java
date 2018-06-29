package com.sohu.focus.salesmaster.http.model;

import java.io.Serializable;

/**
 * Created by yuanminjia on 2018/2/6.
 */

public class PostCheckFileModel implements Serializable {
    private String estateId;
    private String fileName;

    public String getEstateId() {
        return estateId;
    }

    public void setEstateId(String estateId) {
        this.estateId = estateId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
