package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;
import com.sohu.focus.salesmaster.http.model.PostSuborInvestModel;

import rx.Observable;

/**
 * Created by jiayuanmin on 2018/4/26
 * description:
 */
public class GetSuborInvestApi extends FocusBaseApi {

    public static final String INVEST_TYPE_MONTH = "month";
    public static final String INVEST_TYPE_YEAR = "year";

    private String personId;
    private String timeType;
    private String yearOrMonth;

    public GetSuborInvestApi(String uid) {
        personId = uid;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        PostSuborInvestModel model = new PostSuborInvestModel();
        model.setPersonId(personId);
        model.setTimeType(timeType);
        model.setYearOrMonth(yearOrMonth);

        return methods.getSuborInvest(model);
    }


    public void setYearOrMonth(String yearOrMonth) {
        this.yearOrMonth = yearOrMonth;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }
}
