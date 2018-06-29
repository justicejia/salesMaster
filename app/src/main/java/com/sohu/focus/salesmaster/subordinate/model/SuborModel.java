package com.sohu.focus.salesmaster.subordinate.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sohu.focus.salesmaster.kernal.http.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yuanminjia on 2017/10/30.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SuborModel extends BaseModel {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataBean implements Serializable {
        private int count;
        private List<ListBean> list;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ListBean implements Serializable {
            private String personName;
            private String personId;
            private String cityName;
            private int salesRole;
            private int areaCode;

            public String getPersonName() {
                return personName;
            }

            public void setPersonName(String personName) {
                this.personName = personName;
            }

            public String getPersonId() {
                return personId;
            }

            public void setPersonId(String personId) {
                this.personId = personId;
            }

            public int getSalesRole() {
                return salesRole;
            }

            public void setSalesRole(int salesRole) {
                this.salesRole = salesRole;
            }

            public String getCityName() {
                return cityName;
            }

            public void setCityName(String cityName) {
                this.cityName = cityName;
            }

            public int getAreaCode() {
                return areaCode;
            }

            public void setAreaCode(int areaCode) {
                this.areaCode = areaCode;
            }
        }
    }
}
