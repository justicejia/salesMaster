package com.sohu.focus.salesmaster.newFilter.interview;

import com.sohu.focus.salesmaster.newFilter.model.CommonFilterItemModel;

import java.util.List;

/**
 * Created by jiayuanmin on 2018/6/8
 * description:
 */
public interface IFilterSheet {
    void onGetSheetFilter(List<CommonFilterItemModel> roleList, List<CommonFilterItemModel> cityList);
}
