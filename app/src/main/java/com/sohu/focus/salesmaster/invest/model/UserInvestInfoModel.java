package com.sohu.focus.salesmaster.invest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sohu.focus.salesmaster.kernal.http.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jiayuanmin on 2018/4/25
 * description:
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInvestInfoModel extends BaseModel {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataBean implements Serializable {
        private String personName;
        private String finishRate;
        private String year;
        private int personRole;
        private String personId;
        private String totalAdMoney;
        private String adMoney;
        private String targetMoney;
        private List<HistoryBean> history;

        public String getPersonName() {
            return personName;
        }

        public void setPersonName(String personName) {
            this.personName = personName;
        }

        public String getFinishRate() {
            return finishRate;
        }

        public void setFinishRate(String finishRate) {
            this.finishRate = finishRate;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public int getPersonRole() {
            return personRole;
        }

        public void setPersonRole(int personRole) {
            this.personRole = personRole;
        }

        public String getPersonId() {
            return personId;
        }

        public void setPersonId(String personId) {
            this.personId = personId;
        }

        public String getTotalAdMoney() {
            return totalAdMoney;
        }

        public void setTotalAdMoney(String totalAdMoney) {
            this.totalAdMoney = totalAdMoney;
        }

        public String getAdMoney() {
            return adMoney;
        }

        public void setAdMoney(String adMoney) {
            this.adMoney = adMoney;
        }

        public String getTargetMoney() {
            return targetMoney;
        }

        public void setTargetMoney(String targetMoney) {
            this.targetMoney = targetMoney;
        }

        public List<HistoryBean> getHistory() {
            return history;
        }

        public void setHistory(List<HistoryBean> history) {
            this.history = history;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class HistoryBean implements Serializable {
            private String month;
            private String adMoney;

            public String getMonth() {
                return month;
            }

            public void setMonth(String month) {
                this.month = month;
            }

            public String getAdMoney() {
                return adMoney;
            }

            public void setAdMoney(String adMoney) {
                this.adMoney = adMoney;
            }
        }
    }
}
