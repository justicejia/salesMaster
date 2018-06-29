package com.sohu.focus.salesmaster.http.model;

import java.io.Serializable;

/**
 * Created by jiayuanmin on 2018/4/24
 * description:
 */
public class PostAddInvestModel implements Serializable {
    private String estateId;
    private String money;
    private String month;
    private int type;

    public String getEstateId() {
        return estateId;
    }

    public void setEstateId(String estateId) {
        this.estateId = estateId;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
