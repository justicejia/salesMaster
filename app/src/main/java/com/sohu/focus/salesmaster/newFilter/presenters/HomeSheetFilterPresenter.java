package com.sohu.focus.salesmaster.newFilter.presenters;

import com.sohu.focus.salesmaster.base.BasePresenter;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestListener;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;
import com.sohu.focus.salesmaster.newFilter.FilterType;
import com.sohu.focus.salesmaster.newFilter.api.HomeSheetFilterApi;
import com.sohu.focus.salesmaster.newFilter.interview.IFilterSheet;
import com.sohu.focus.salesmaster.newFilter.model.CommonFilterItemModel;
import com.sohu.focus.salesmaster.newFilter.model.HomeSheetFilterModel;


/**
 * Created by jiayuanmin on 2018/6/8
 * description:
 */
public class HomeSheetFilterPresenter extends BasePresenter<IFilterSheet> {

    private static final String TAG = HomeSheetFilterPresenter.class.getSimpleName();
    private HomeSheetFilterApi api;

    public HomeSheetFilterPresenter(String uid) {
        api = new HomeSheetFilterApi(uid);
        api.setTag(TAG);
    }

    public void getSheetFilterData() {
        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<HomeSheetFilterModel>() {
            @Override
            public void onSuccess(HomeSheetFilterModel result, String method) {
                if (isAttached() && result.getData() != null) {
                    if (CommonUtils.notEmpty(result.getData().getCityList())) {
                        for (CommonFilterItemModel item : result.getData().getCityList()) {
                            item.setType(FilterType.CITY);
                        }
                    }
                    if (CommonUtils.notEmpty(result.getData().getSalesRoleList())) {
                        for (CommonFilterItemModel item : result.getData().getSalesRoleList()) {
                            item.setType(FilterType.ROLE);
                        }
                    }
                    getView().onGetSheetFilter(result.getData().getSalesRoleList(), result.getData().getCityList());
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onFailed(HomeSheetFilterModel result, String method) {
                if (result != null) {
                    ToastUtil.toast(result.getMsg());
                }
            }
        });
    }


    @Override
    public void release() {
        detach();
        HttpEngine.getInstance().cancel(TAG);
    }
}
