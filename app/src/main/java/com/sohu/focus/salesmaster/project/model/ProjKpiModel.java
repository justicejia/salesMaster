package com.sohu.focus.salesmaster.project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sohu.focus.salesmaster.kernal.http.BaseModel;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by luckyzhangx on 02/02/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjKpiModel extends BaseModel {

    public DataBean data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataBean implements Serializable {
        public Week week;
        public Month month;
        public List<KpiHistory> history;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Week implements Serializable {
        public String lastWeekFinishRate;
        public String lastWeekFinish;
        public String lastWeekTarget;

        public String thisWeekFinishRate;
        public String thisWeekFinish;
        public String thisWeekTarget;

        public String nextWeekTarget;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Month implements Serializable {
        public String lastMonthFinishRate;
        public String lastMonthFinish;
        public String lastMonthTarget;

        public String thisMonthFinishRate;
        public String thisMonthFinish;
        public String thisMonthTarget;

        public String nextMonthTarget;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KpiHistory implements Serializable {
        public String month;
        public String kpiTarget;
        public String kpiFinishRate;
        public String kpiFinish;

        public String getMonth() {
            return CommonUtils.getDataNotNull(month);
        }

        public String getKpiTarget() {
            return CommonUtils.getDataNotNull(kpiTarget);
        }

        public String getKpiFinishRate() {
            return CommonUtils.getDataNotNull(kpiFinishRate);
        }

        public String getKpiFinish() {
            return CommonUtils.getDataNotNull(kpiFinish);
        }
    }

}
