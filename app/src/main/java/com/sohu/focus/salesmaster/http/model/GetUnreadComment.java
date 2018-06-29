package com.sohu.focus.salesmaster.http.model;

import java.io.Serializable;

/**
 * Created by luckyzhangx on 14/03/2018.
 */

public class GetUnreadComment implements Serializable {
    public String personId;
    public int pageSize, offset;
    //    0 不包括历史消息，1 包括历史消息
    public int includeHistory;


    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setIncludeHistory(int includeHistory) {
        this.includeHistory = includeHistory;
    }
}
