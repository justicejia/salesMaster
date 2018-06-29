package com.sohu.focus.salesmaster.dynamics.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sohu.focus.salesmaster.kernal.http.BaseModel;

import java.util.List;

/**
 * 动态
 * Created by yuanminjia on 2017/10/27.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class DynamicSetModel extends BaseModel {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataBean {
        private List<Integer> userAvailableRoleList;
        private List<CityListBean> cityList;
        private List<ListBean> list;

        public List<Integer> getUserAvailableRoleList() {
            return userAvailableRoleList;
        }

        public void setUserAvailableRoleList(List<Integer> userAvailableRoleList) {
            this.userAvailableRoleList = userAvailableRoleList;
        }

        public List<CityListBean> getCityList() {
            return cityList;
        }

        public void setCityList(List<CityListBean> cityList) {
            this.cityList = cityList;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class CityListBean {
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
        public static class ListBean {
            private String projectPersonID;
            private long stampTime;
            private String salesProjectID;
            private int salesRole;
            private String adMoney;
            private String remark;
            private String personName;
            private int areaCode;
            private String projectStage;
            private String cityName;
            private String personID;
            private String time;
            private String projectName;
            private String projectID;
            private String projectPersonName;
            private List<ImagesBean> images;
            private List<OptimalImagesBean> optimalImages;
            private List<CustomerListBean> customerList;
            private List<DynamicCommentBean> commentList;

            public String getProjectPersonID() {
                return projectPersonID;
            }

            public void setProjectPersonID(String projectPersonID) {
                this.projectPersonID = projectPersonID;
            }

            public long getStampTime() {
                return stampTime;
            }

            public void setStampTime(long stampTime) {
                this.stampTime = stampTime;
            }

            public String getSalesProjectID() {
                return salesProjectID;
            }

            public void setSalesProjectID(String salesProjectID) {
                this.salesProjectID = salesProjectID;
            }

            public int getSalesRole() {
                return salesRole;
            }

            public void setSalesRole(int salesRole) {
                this.salesRole = salesRole;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getPersonName() {
                return personName;
            }

            public void setPersonName(String personName) {
                this.personName = personName;
            }

            public int getAreaCode() {
                return areaCode;
            }

            public void setAreaCode(int areaCode) {
                this.areaCode = areaCode;
            }

            public String getProjectStage() {
                return projectStage;
            }

            public void setProjectStage(String projectStage) {
                this.projectStage = projectStage;
            }

            public String getCityName() {
                return cityName;
            }

            public void setCityName(String cityName) {
                this.cityName = cityName;
            }

            public String getPersonID() {
                return personID;
            }

            public void setPersonID(String personID) {
                this.personID = personID;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getProjectName() {
                return projectName;
            }

            public void setProjectName(String projectName) {
                this.projectName = projectName;
            }

            public String getProjectID() {
                return projectID;
            }

            public void setProjectID(String projectID) {
                this.projectID = projectID;
            }

            public String getProjectPersonName() {
                return projectPersonName;
            }

            public void setProjectPersonName(String projectPersonName) {
                this.projectPersonName = projectPersonName;
            }

            public List<ImagesBean> getImages() {
                return images;
            }

            public void setImages(List<ImagesBean> images) {
                this.images = images;
            }

            public List<OptimalImagesBean> getOptimalImages() {
                return optimalImages;
            }

            public void setOptimalImages(List<OptimalImagesBean> optimalImages) {
                this.optimalImages = optimalImages;
            }

            public List<CustomerListBean> getCustomerList() {
                return customerList;
            }

            public void setCustomerList(List<CustomerListBean> customerList) {
                this.customerList = customerList;
            }

            public List<DynamicCommentBean> getCommentList() {
                return commentList;
            }

            public void setCommentList(List<DynamicCommentBean> commentList) {
                this.commentList = commentList;
            }

            public String getAdMoney() {
                return adMoney;
            }

            public void setAdMoney(String adMoney) {
                this.adMoney = adMoney;
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class ImagesBean {
                private int width;
                private String url;
                private int height;

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class OptimalImagesBean {
                private int width;
                private String url;
                private int height;

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class CustomerListBean {
                private int customerId;
                private String name;
                private String title;
                private String phone;
                private int status;

                public int getCustomerId() {
                    return customerId;
                }

                public void setCustomerId(int customerId) {
                    this.customerId = customerId;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getPhone() {
                    return phone;
                }

                public void setPhone(String phone) {
                    this.phone = phone;
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
}
