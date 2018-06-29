package com.sohu.focus.salesmaster.project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sohu.focus.salesmaster.kernal.http.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jiayuanmin on 2018/4/23
 * description:
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectInvestModel extends BaseModel{

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataBean implements Serializable {
        private String finishRate;
        private int estateId;
        private String estateName;
        private String year;
        private String adMoneyTotal;
        private String adMoney;
        private String targetMoney;
        private List<HistoryBean> history;

        public String getFinishRate() {
            return finishRate;
        }

        public void setFinishRate(String finishRate) {
            this.finishRate = finishRate;
        }

        public int getEstateId() {
            return estateId;
        }

        public void setEstateId(int estateId) {
            this.estateId = estateId;
        }

        public String getEstateName() {
            return estateName;
        }

        public void setEstateName(String estateName) {
            this.estateName = estateName;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getAdMoneyTotal() {
            return adMoneyTotal;
        }

        public void setAdMoneyTotal(String adMoneyTotal) {
            this.adMoneyTotal = adMoneyTotal;
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
