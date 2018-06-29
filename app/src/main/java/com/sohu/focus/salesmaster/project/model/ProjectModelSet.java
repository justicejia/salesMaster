package com.sohu.focus.salesmaster.project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sohu.focus.salesmaster.kernal.http.BaseModel;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yuanminjia on 2017/10/30.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectModelSet extends BaseModel {

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
            private String estateId;
            private String projectName;
            private String score;
            private List<UserListBean> userList;
            private int currentShowStatus;  //用来确定筛选后显示评分还是投放，400  0:评分 1：广告投放 2:400电话量
            private String isCalledDistinct;
            private String adMoney;
            private int customerNum;

            public String getEstateId() {
                return CommonUtils.getDataNotNull(estateId);
            }

            public void setEstateId(String estateId) {
                this.estateId = estateId;
            }

            public String getProjectName() {
                return CommonUtils.getDataNotNull(projectName);
            }

            public void setProjectName(String projectName) {
                this.projectName = projectName;
            }

            public List<UserListBean> getUserList() {
                return userList;
            }

            public void setUserList(List<UserListBean> userList) {
                this.userList = userList;
            }

            public String getScore() {
                return CommonUtils.getDataNotNull(score);
            }

            public void setScore(String score) {
                this.score = score;
            }

            public int getCurrentShowStatus() {
                return currentShowStatus;
            }

            public void setCurrentShowStatus(int currentShowStatus) {
                this.currentShowStatus = currentShowStatus;
            }

            public String getIsCalledDistinct() {
                return isCalledDistinct;
            }

            public void setIsCalledDistinct(String isCalledDistinct) {
                this.isCalledDistinct = isCalledDistinct;
            }

            public String getAdMoney() {
                return CommonUtils.getDataNotNull(adMoney);
            }

            public void setAdMoney(String adMoney) {
                this.adMoney = adMoney;
            }

            public int getCustomerNum() {
                return customerNum;
            }

            public void setCustomerNum(int customerNum) {
                this.customerNum = customerNum;
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class UserListBean implements Serializable {
                private String personName;
                private int salesUserRole;
                private String personId;

                public String getPersonName() {
                    return personName;
                }

                public void setPersonName(String personName) {
                    this.personName = personName;
                }

                public int getSalesUserRole() {
                    return salesUserRole;
                }

                public void setSalesUserRole(int salesUserRole) {
                    this.salesUserRole = salesUserRole;
                }

                public String getPersonId() {
                    return personId;
                }

                public void setPersonId(String personId) {
                    this.personId = personId;
                }
            }
        }
    }
}
