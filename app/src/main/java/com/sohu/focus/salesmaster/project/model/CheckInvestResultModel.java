package com.sohu.focus.salesmaster.project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sohu.focus.salesmaster.kernal.http.BaseModel;

import java.io.Serializable;

/**
 * Created by jiayuanmin on 2018/4/24
 * description:
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckInvestResultModel extends BaseModel {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataBean implements Serializable {
        private String estateId;
        private String estateName;
        private String adMoney;

        public String getEstateId() {
            return estateId;
        }

        public void setEstateId(String estateId) {
            this.estateId = estateId;
        }

        public String getEstateName() {
            return estateName;
        }

        public void setEstateName(String estateName) {
            this.estateName = estateName;
        }

        public String getAdMoney() {
            return adMoney;
        }

        public void setAdMoney(String adMoney) {
            this.adMoney = adMoney;
        }
    }

}
