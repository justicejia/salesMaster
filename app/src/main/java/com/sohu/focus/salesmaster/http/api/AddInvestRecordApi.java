package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;
import com.sohu.focus.salesmaster.http.model.PostAddInvestModel;

import rx.Observable;

/**
 * Created by jiayuanmin on 2018/4/24
 * description:
 */
public class AddInvestRecordApi extends FocusBaseApi {
    private String estateId;
    private String money;
    private String month;
    private int type;

    public AddInvestRecordApi(String pid) {
        estateId = pid;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        PostAddInvestModel model = new PostAddInvestModel();
        model.setEstateId(estateId);
        model.setMoney(money);
        model.setMonth(month);
        model.setType(type);
        return methods.addInvestRecord(model);
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setType(int type) {
        this.type = type;
    }
}
