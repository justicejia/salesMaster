package com.sohu.focus.salesmaster.http.model;

import java.io.Serializable;

/**
 * Created by yuanminjia on 2018/2/6.
 */

public class GetFiles implements Serializable {
    private String estateId;
    private int page;
    private int pageSize;

    public String getEstateId() {
        return estateId;
    }

    public void setEstateId(String estateId) {
        this.estateId = estateId;
    }


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
