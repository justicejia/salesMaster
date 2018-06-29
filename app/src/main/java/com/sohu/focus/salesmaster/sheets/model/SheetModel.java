package com.sohu.focus.salesmaster.sheets.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sohu.focus.salesmaster.kernal.http.BaseModel;

import java.util.List;

/**
 * Created by jiayuanmin on 2018/5/22
 * description:
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SheetModel extends BaseModel {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataBean {
        private String title;
        private String api;
        private List<List<String>> dataList;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<List<String>> getDataList() {
            return dataList;
        }

        public void setDataList(List<List<String>> dataList) {
            this.dataList = dataList;
        }

        public String getApi() {
            return api;
        }

        public void setApi(String api) {
            this.api = api;
        }
    }
}
