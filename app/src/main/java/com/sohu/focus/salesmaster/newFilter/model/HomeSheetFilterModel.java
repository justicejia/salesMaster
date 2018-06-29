package com.sohu.focus.salesmaster.newFilter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sohu.focus.salesmaster.kernal.http.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jiayuanmin on 2018/6/8
 * description:
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HomeSheetFilterModel extends BaseModel {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataBean implements Serializable {
        private List<CommonFilterItemModel> salesRoleList;
        private List<CommonFilterItemModel> cityList;

        public List<CommonFilterItemModel> getSalesRoleList() {
            return salesRoleList;
        }

        public void setSalesRoleList(List<CommonFilterItemModel> salesRoleList) {
            this.salesRoleList = salesRoleList;
        }

        public List<CommonFilterItemModel> getCityList() {
            return cityList;
        }

        public void setCityList(List<CommonFilterItemModel> cityList) {
            this.cityList = cityList;
        }

    }
}
