package com.sohu.focus.salesmaster.filter;

import com.sohu.focus.salesmaster.filter.model.FiltersDTO;
import com.sohu.focus.salesmaster.filter.model.FiltersVO;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.http.api.GetFilterDataApi;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestMappingListener;
import com.sohu.focus.salesmaster.kernal.log.Logger;
import com.sohu.focus.salesmaster.login.AccountManager;

/**
 * Created by luckyzhangx on 15/01/2018.
 */

public class FilterPresenter {
    public static void updateFilterData() {
        if (!AccountManager.INSTANCE.isLogin()) return;
        if (FiltersVO.getINSTANCE() != null &&
                FiltersVO.getINSTANCE().getUserId().equals(AccountManager.INSTANCE.getUserId())) {
            return;
        }
        forceUpdateFilterData();
    }

    public static void forceUpdateFilterData() {
        GetFilterDataApi api = new GetFilterDataApi();
        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestMappingListener<FiltersDTO,
                FiltersVO>() {
            @Override
            public void onSuccess(FiltersVO result) {
                if (result != null) {
                    result.setUserId(AccountManager.INSTANCE.getUserId());
                    FiltersVO.setINSTANCE(result);
                }
            }

            @Override
            public void onError(Throwable e) {
                Logger.ZX().e("获取筛选参数错误");
            }

            @Override
            public void onFailed(FiltersDTO result, String method) {
                Logger.ZX().e("获取筛选参数失败");
            }
        });
    }
}
