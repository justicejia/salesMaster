package com.sohu.focus.salesmaster.project.model;

import java.io.Serializable;

/**
 * Created by yuanminjia on 2018/1/12.
 */

public class CompetitorItemModel implements Serializable {
    private String company;
    private String price;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
