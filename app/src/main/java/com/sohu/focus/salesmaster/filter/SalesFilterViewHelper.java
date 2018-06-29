package com.sohu.focus.salesmaster.filter;

import android.content.Context;
import android.view.View;

import com.sohu.focus.salesmaster.filter.base.FilterRecord;
import com.sohu.focus.salesmaster.filter.base.FilterVO;
import com.sohu.focus.salesmaster.filter.base.FilterViewHelper;
import com.sohu.focus.salesmaster.filter.model.FieldNameManager;
import com.sohu.focus.salesmaster.filter.model.FiltersVO;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;
import com.sohu.focus.salesmaster.login.AccountManager;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by luckyzhangx on 15/01/2018.
 */

public class SalesFilterViewHelper extends FilterViewHelper {

    WeakReference<FilterableView> view;

    public SalesFilterViewHelper(Context context, View anchorView) {
        super(context, anchorView);
        recorder = new SalesFilterRecorder();
    }

    public void attach(FilterableView v) {
        view = new WeakReference(v);
    }

    @Override
    public void showFilterView(int filterType, View triggerView) {

        if (FiltersVO.getINSTANCE() == null ||
                !FiltersVO.getINSTANCE().getUserId().equals(AccountManager.INSTANCE.getUserId())) {
            ToastUtil.toast("暂无数据, 请稍后再试");
            FilterPresenter.updateFilterData();
            return;
        }
        super.showFilterView(filterType, triggerView);

        switch (filterType) {
            case FILTER_CITY:
                showFilterView(getFilterData().getCity(), recorder.getRecord(FieldNameManager
                        .CITY));
                break;
            case FILTER_SALES_ROLE:
                showFilterView(getFilterData().getSalesRole(), recorder.getRecord(FieldNameManager
                        .SALE_ROLE));
                break;
            case FILTER_PROGRESS:
                showFilterView(getFilterData().getProgress(), recorder.getRecord(FieldNameManager
                        .PROGRESS));
                break;
            case FILTER_PROJ_ORDER:
                showFilterView(getFilterData().getProjOrder(), recorder.getRecord(FieldNameManager
                        .ESTATE_ORDER));
                break;
        }
        showPopupWindow();
    }

    private void showFilterView(List<FilterVO> filterVOS, FilterRecord record) {
        mainAdapter.setModels(filterVOS);
        if (record != null) mainAdapter.setSelModel(record.getFilter());
    }

    @Override
    public void onDismiss() {
        super.onDismiss();
        if (!isAttached() || !needRefresh) return;
        view.get().filter(recorder);
    }

    private boolean isAttached() {
        return view != null && view.get() != null;
    }

    private FiltersVO getFilterData() {
        return FiltersVO.getINSTANCE();
    }
}
