package com.sohu.focus.salesmaster.dynamics.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sohu.focus.salesmaster.kernal.http.BaseModel;

/**
 * Created by luckyzhangx on 14/03/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class DynamicDetailModel extends BaseModel {
    public DynamicSetModel.DataBean.ListBean data;

    public DynamicSetModel.DataBean.ListBean getData() {
        return data;
    }

    public void setData(DynamicSetModel.DataBean.ListBean data) {
        this.data = data;
    }
}
