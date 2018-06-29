package com.sohu.focus.salesmaster.goal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sohu.focus.salesmaster.kernal.http.BaseModel;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by luckyzhangx on 05/02/2018.
 */

/**
 * 通过类型（周月）和相对时间（本上下/周月) 获取用户 400 kpi 列表
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class UsrKpiHistoryModel extends BaseModel {

    public static final int TYPE_SUB = 1;
    public static final int TYPE_PROJ = 2;

    public DataBean data;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataBean implements Serializable {
        public int type;

        public String personName = "";

        public List<SubKpiHistory> subordinateList;
        public List<ProjKpiHistory> estateList;

        public String getPersonName() {
            return CommonUtils.getDataNotNull(personName);
        }
    }
}
