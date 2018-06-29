package com.sohu.focus.salesmaster.http.model;

import java.io.Serializable;

/**
 * Created by yuanminjia on 2018/2/6.
 */

public class DeleteFile implements Serializable {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
