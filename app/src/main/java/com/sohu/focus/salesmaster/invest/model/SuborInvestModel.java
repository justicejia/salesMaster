package com.sohu.focus.salesmaster.invest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sohu.focus.salesmaster.kernal.http.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jiayuanmin on 2018/4/26
 * description:
 */
public class SuborInvestModel extends BaseModel {

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
        private String personId;
        private int personRole;
        private int type;

        private List<UserInvestCommonVO> subordinateList;
        private List<UserInvestCommonVO> estateList;


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

        public int getPersonRole() {
            return personRole;
        }

        public void setPersonRole(int personRole) {
            this.personRole = personRole;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }


        public List<UserInvestCommonVO> getSubordinateList() {
            return subordinateList;
        }

        public void setSubordinateList(List<UserInvestCommonVO> subordinateList) {
            this.subordinateList = subordinateList;
        }

        public List<UserInvestCommonVO> getEstateList() {
            return estateList;
        }

        public void setEstateList(List<UserInvestCommonVO> estateList) {
            this.estateList = estateList;
        }
    }
}
