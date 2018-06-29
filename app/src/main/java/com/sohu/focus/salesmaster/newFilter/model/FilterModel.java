package com.sohu.focus.salesmaster.newFilter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sohu.focus.salesmaster.kernal.http.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jiayuanmin on 2018/5/18
 * description:
 */


@JsonIgnoreProperties(ignoreUnknown = true)
public class FilterModel extends BaseModel {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataBean implements Serializable {
        private String cityName;
        private String level;
        private int salesRole;
        private String sohuMail;
        private String userName;
        private String userId;
        private String salesRoleName;
        private List<SalesRoleListBean> salesRoleList;
        private List<Integer> userAvailableRoleList;
        private List<StateBean> state;
        private List<CityBean> cityList;


        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public int getSalesRole() {
            return salesRole;
        }

        public void setSalesRole(int salesRole) {
            this.salesRole = salesRole;
        }

        public String getSohuMail() {
            return sohuMail;
        }

        public void setSohuMail(String sohuMail) {
            this.sohuMail = sohuMail;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getSalesRoleName() {
            return salesRoleName;
        }

        public void setSalesRoleName(String salesRoleName) {
            this.salesRoleName = salesRoleName;
        }

        public List<SalesRoleListBean> getSalesRoleList() {
            return salesRoleList;
        }

        public void setSalesRoleList(List<SalesRoleListBean> salesRoleList) {
            this.salesRoleList = salesRoleList;
        }

        public List<Integer> getUserAvailableRoleList() {
            return userAvailableRoleList;
        }

        public void setUserAvailableRoleList(List<Integer> userAvailableRoleList) {
            this.userAvailableRoleList = userAvailableRoleList;
        }

        public List<StateBean> getState() {
            return state;
        }

        public void setState(List<StateBean> state) {
            this.state = state;
        }

        public List<CityBean> getCityList() {
            return cityList;
        }

        public void setCityList(List<CityBean> cityList) {
            this.cityList = cityList;
        }


        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class SalesRoleListBean implements Serializable {
            private String label;
            private int option;

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public int getOption() {
                return option;
            }

            public void setOption(int option) {
                this.option = option;
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class CityBean implements Serializable {
            private String label;
            private int option;

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public int getOption() {
                return option;
            }

            public void setOption(int option) {
                this.option = option;
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class StateBean implements Serializable {
            private String label;
            private int option;
            private boolean needCustomer;
            private int status;

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public int getOption() {
                return option;
            }

            public void setOption(int option) {
                this.option = option;
            }

            public boolean isNeedCustomer() {
                return needCustomer;
            }

            public void setNeedCustomer(boolean needCustomer) {
                this.needCustomer = needCustomer;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }


}
