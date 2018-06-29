package com.sohu.focus.salesmaster.filter.model;

import com.sohu.focus.salesmaster.filter.base.FilterVO;

import java.util.List;

/**
 * Created by luckyzhangx on 15/01/2018.
 */

public class FiltersVO {

    private static FiltersVO INSTANCE;

    public static FiltersVO getINSTANCE() {
        return INSTANCE;
    }

    public static void setINSTANCE(FiltersVO INSTANCE) {
        FiltersVO.INSTANCE = INSTANCE;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    String userId;
    //    城市
    List<FilterVO> city;
    //    角色
    List<FilterVO> salesRole;
    //    状态
    List<FilterVO> projOrder;
    //    类型（其实就是状态）
    List<FilterVO> progress;

    public List<FilterVO> getCity() {
        return city;
    }

    public List<FilterVO> getSalesRole() {
        return salesRole;
    }

    public List<FilterVO> getProjOrder() {
        return projOrder;
    }

    public List<FilterVO> getProgress() {
        return progress;
    }
}
