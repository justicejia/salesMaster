package com.sohu.focus.salesmaster.invest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;

import java.io.Serializable;

/**
 * Created by jiayuanmin on 2018/4/25
 * description:
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInvestCommonVO implements Serializable {

    //两种模式，一种是有下属，一种无下属
    private boolean hasSub;

    //二者共有的字段
    public String adMoney;
    public String targetMoney;
    public String finishRate;

    //有下属
    public String personName;
    public int personRole;
    public String personId;

    //无下属
    public String estateId;
    public String estateName;


    public boolean isHasSub() {
        return CommonUtils.notEmpty(personId);
    }

    public void setHasSub(boolean hasSub) {
        this.hasSub = hasSub;
    }
}
