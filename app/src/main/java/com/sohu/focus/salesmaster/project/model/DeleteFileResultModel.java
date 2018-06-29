package com.sohu.focus.salesmaster.project.model;

import com.sohu.focus.salesmaster.kernal.http.BaseModel;

import java.io.Serializable;

/**
 * Created by yuanminjia on 2018/2/6.
 */

public class DeleteFileResultModel extends BaseModel {
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        private int resultCode;
        private String resultMes;

        public int getResultCode() {
            return resultCode;
        }

        public void setResultCode(int resultCode) {
            this.resultCode = resultCode;
        }

        public String getResultMes() {
            return resultMes;
        }

        public void setResultMes(String resultMes) {
            this.resultMes = resultMes;
        }
    }
}
