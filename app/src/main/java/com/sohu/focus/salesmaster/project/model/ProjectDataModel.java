package com.sohu.focus.salesmaster.project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sohu.focus.salesmaster.kernal.http.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yuanminjia on 2018/1/10.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectDataModel extends BaseModel {

    public DataBean data;
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataBean implements Serializable {
        public UvBean uv;
        public CountLiveBrokerBean countLiveBroker;
        public IsCalledDistinctBean isCalledDistinct;
        public CountLiveBean countLive;
        public PvBean pv;
        public IsAnsweredRateBean isAnsweredRate;
        public YesterdayDataMapBean yesterdayDataMap;


        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class UvBean implements Serializable {
            public List<Long> y;
            public List<Long> yLabel;
            public List<String> xLabel;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class CountLiveBrokerBean implements Serializable {
            public List<Long> y;
            public List<Long> yLabel;
            public List<String> xLabel;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class IsCalledDistinctBean implements Serializable {
            public List<Long> y;
            public List<Long> yLabel;
            public List<String> xLabel;
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class CountLiveBean implements Serializable {
            public List<Long> y;
            public List<Long> yLabel;
            public List<String> xLabel;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class PvBean implements Serializable {
            public List<Long> y;
            public List<Long> yLabel;
            public List<String> xLabel;

        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class IsAnsweredRateBean implements Serializable {
            public List<Float> y;
            public List<Float> yLabel;
            public List<String> xLabel;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class YesterdayDataMapBean implements Serializable {
            public String uv;
            public String countLiveBroker;
            public String isCalledDistinct;
            public String countLive;
            public String pv;
            public String isAnsweredRate;
    }

}
