package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;
import com.sohu.focus.salesmaster.http.model.PostCheckInvestExistModel;

import rx.Observable;

/**
 * Created by jiayuanmin on 2018/4/24
 * description:
 */
public class CheckInvestExistApi extends FocusBaseApi {

    private String mPid;
    private String mMonth;

    public CheckInvestExistApi(String pid, String month) {
        mPid = pid;
        mMonth = month;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        PostCheckInvestExistModel model = new PostCheckInvestExistModel();
        model.setEstateId(mPid);
        model.setMonth(mMonth);
        return methods.checkInvestExist(model);
    }
}
