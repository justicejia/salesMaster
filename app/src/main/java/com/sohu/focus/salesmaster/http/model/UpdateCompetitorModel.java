package com.sohu.focus.salesmaster.http.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yuanminjia on 2018/1/12.
 */

public class UpdateCompetitorModel implements Serializable {
    private String estateId;
    private List<Map<String, String>> competitorMoney = new ArrayList<>();

    public String getEstateId() {
        return estateId;
    }

    public void setEstateId(String estateId) {
        this.estateId = estateId;
    }

    public List<Map<String, String>> getCompetitorMoney() {
        return competitorMoney;
    }

    public void setCompetitorMoney(List<Map<String, String>> competitorMoney) {
        this.competitorMoney = competitorMoney;
    }
}
