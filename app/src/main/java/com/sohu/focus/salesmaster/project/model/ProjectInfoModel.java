package com.sohu.focus.salesmaster.project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sohu.focus.salesmaster.kernal.http.BaseModel;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by yuanminjia on 2017/12/7.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectInfoModel extends BaseModel {
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataBean implements Serializable {
        private DealBean deal;
        private String link;
        private RoleChainMapBean roleChainMap;
        private BasicBean basic;

        public DealBean getDeal() {
            return deal;
        }

        public void setDeal(DealBean deal) {
            this.deal = deal;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public RoleChainMapBean getRoleChainMap() {
            return roleChainMap;
        }

        public void setRoleChainMap(RoleChainMapBean roleChainMap) {
            this.roleChainMap = roleChainMap;
        }

        public BasicBean getBasic() {
            return basic;
        }

        public void setBasic(BasicBean basic) {
            this.basic = basic;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class DealBean implements Serializable {
            private String soldNum;
            private String unSoldArea;
            private String unSoldNum;
            private String soldArea;
            private String averagePrice;
            private String soldMoney;
            private List<Map<String,String>> competitor;

            public String getSoldNum() {
                return soldNum;
            }

            public void setSoldNum(String soldNum) {
                this.soldNum = soldNum;
            }

            public String getUnSoldArea() {
                return CommonUtils.getDataNotNull(unSoldArea,"暂无");
            }

            public void setUnSoldArea(String unSoldArea) {
                this.unSoldArea = unSoldArea;
            }

            public String getUnSoldNum() {
                return unSoldNum;
            }

            public void setUnSoldNum(String unSoldNum) {
                this.unSoldNum = unSoldNum;
            }

            public String getSoldArea() {
                return soldArea;
            }

            public void setSoldArea(String soldArea) {
                this.soldArea = soldArea;
            }

            public String getAveragePrice() {
                return CommonUtils.getDataNotNull(averagePrice,"暂无");
            }

            public void setAveragePrice(String averagePrice) {
                this.averagePrice = averagePrice;
            }

            public String getSoldMoney() {
                return soldMoney;
            }

            public void setSoldMoney(String soldMoney) {
                this.soldMoney = soldMoney;
            }

            public List<Map<String, String>> getCompetitor() {
                return competitor;
            }

            public void setCompetitor(List<Map<String, String>> competitor) {
                this.competitor = competitor;
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class RoleChainMapBean implements Serializable {
            @JsonProperty("1")
            private List<_$1Bean> _$1;
            @JsonProperty("2")
            private List<_$2Bean> _$2;

            public List<_$1Bean> get_$1() {
                return _$1;
            }

            public void set_$1(List<_$1Bean> _$1) {
                this._$1 = _$1;
            }

            public List<_$2Bean> get_$2() {
                return _$2;
            }

            public void set_$2(List<_$2Bean> _$2) {
                this._$2 = _$2;
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class _$1Bean implements Serializable {
                private String personName;
                private int personId;

                public String getPersonName() {
                    return personName;
                }

                public void setPersonName(String personName) {
                    this.personName = personName;
                }

                public int getPersonId() {
                    return personId;
                }

                public void setPersonId(int personId) {
                    this.personId = personId;
                }
            }


            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class _$2Bean implements Serializable {
                private String personName;
                private int personId;

                public String getPersonName() {
                    return personName;
                }

                public void setPersonName(String personName) {
                    this.personName = personName;
                }

                public int getPersonId() {
                    return personId;
                }

                public void setPersonId(int personId) {
                    this.personId = personId;
                }
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class BasicBean implements Serializable {
            private String propertyTypes;
            private String score;
            private String buildingArea;
            private String saleStatus;
            private String openingDate;
            private String adMoney;

            public String getPropertyTypes() {
                return propertyTypes;
            }

            public void setPropertyTypes(String propertyTypes) {
                this.propertyTypes = propertyTypes;
            }

            public String getScore() {
                return score;
            }

            public void setScore(String score) {
                this.score = score;
            }

            public String getBuildingArea() {
                return buildingArea;
            }

            public void setBuildingArea(String buildingArea) {
                this.buildingArea = buildingArea;
            }

            public String getSaleStatus() {
                return saleStatus;
            }

            public void setSaleStatus(String saleStatus) {
                this.saleStatus = saleStatus;
            }

            public String getOpeningDate() {
                return openingDate;
            }

            public void setOpeningDate(String openingDate) {
                this.openingDate = openingDate;
            }

            public String getAdMoney() {
                return CommonUtils.getDataNotNull(adMoney);
            }

            public void setAdMoney(String adMoney) {
                this.adMoney = adMoney;
            }
        }

    }


}

