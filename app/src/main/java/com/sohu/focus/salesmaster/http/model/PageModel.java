package com.sohu.focus.salesmaster.http.model;

import java.io.Serializable;

/**
 * Created by luckyzhangx on 01/12/2017.
 */

/**
 * 带页数参数的 post model
 */
public class PageModel implements Serializable {
    int page = 1;
    int pageSize = 20;

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
