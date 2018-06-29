package com.sohu.focus.salesmaster.project.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseFragment;
import com.sohu.focus.salesmaster.base.BaseWebViewActivity;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.http.api.GetProjectInfoApi;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestListener;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;
import com.sohu.focus.salesmaster.project.model.ProjectInfoModel;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 项目详情-楼盘信息页
 * Created by yuanminjia on 2018/1/2.
 */

public class ProjectInfoFragment extends BaseFragment {

    public static final String TAG = "ProjectInfoFragment";

    @BindView(R.id.project_detail_sales)
    TextView sales;
    @BindView(R.id.project_detail_operating)
    TextView operating;
    @BindView(R.id.project_detail_rating)
    TextView rating;
    @BindView(R.id.project_detail_status)
    TextView status;
    @BindView(R.id.project_detail_type)
    TextView type;
    @BindView(R.id.project_detail_open)
    TextView open;
    @BindView(R.id.project_detail_area)
    TextView area;
    @BindView(R.id.project_detail_avg_price)
    TextView avgPrice;
    @BindView(R.id.project_detail_sales_price)
    TextView saledPrice;
    @BindView(R.id.project_detail_sales_count)
    TextView saledCount;
    @BindView(R.id.project_detail_sales_area)
    TextView saledArea;
    @BindView(R.id.project_detail_not_sale_count)
    TextView unSaledCount;
    @BindView(R.id.project_detail_not_sale_area)
    TextView unSaledArea;

    private String mEstateId;
    private String mEstateUrl;

    @OnClick(R.id.project_detail_view_web)
    void goToWeb() {
        if (CommonUtils.notEmpty(mEstateUrl)) {
            BaseWebViewActivity.naviToWeb(mEstateUrl, getContext(), "楼盘详情");
        } else {
            ToastUtil.toast("网页不存在");
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_info, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        mEstateId = bundle.getString(SalesConstants.EXTRA_PROJECT_ID);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HttpEngine.getInstance().cancel(TAG);
    }


    public void getData() {
        GetProjectInfoApi api = new GetProjectInfoApi(mEstateId);
        api.setTag(TAG);
        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<ProjectInfoModel>() {
            @Override
            public void onSuccess(ProjectInfoModel result, String method) {
                if (result == null || result.getData() == null)
                    return;
                loadData(result);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onFailed(ProjectInfoModel result, String method) {
                if (result != null) ToastUtil.toast(result.getMsg());
            }
        });
    }

    private void loadData(ProjectInfoModel data) {
        //负责人信息
        mEstateUrl = data.getData().getLink();
        if (data.getData().getRoleChainMap() == null) return;
        if (data.getData().getRoleChainMap().get_$1() != null && data.getData().getRoleChainMap().get_$1().size() > 0) {
            StringBuilder chain1 = new StringBuilder();
            for (ProjectInfoModel.DataBean.RoleChainMapBean._$1Bean item : data.getData().getRoleChainMap().get_$1()) {
                chain1.append(item.getPersonName()).append(" > ");
            }
            chain1.replace(chain1.length() - 3, chain1.length(), "");
            sales.setText(chain1.toString().trim());
        } else {
            sales.setText("暂未绑定");
        }
        if (data.getData().getRoleChainMap().get_$2() != null && data.getData().getRoleChainMap().get_$2().size() > 0) {
            StringBuilder chain2 = new StringBuilder();
            for (ProjectInfoModel.DataBean.RoleChainMapBean._$2Bean item : data.getData().getRoleChainMap().get_$2()) {
                chain2.append(item.getPersonName()).append(" > ");
            }
            chain2.replace(chain2.length() - 3, chain2.length(), "");
            operating.setText(chain2.toString().trim());
        } else {
            operating.setText("暂未绑定");
        }

        //楼盘基本信息
        if (data.getData().getBasic() != null) {
            rating.setText(data.getData().getBasic().getScore() + "分");
            status.setText(data.getData().getBasic().getSaleStatus());
            type.setText(data.getData().getBasic().getPropertyTypes());
            open.setText(data.getData().getBasic().getOpeningDate());
            area.setText(data.getData().getBasic().getBuildingArea() + "m²");
        } else {
            rating.setText("暂无");
            status.setText("暂无");
            type.setText("暂无");
            open.setText("暂无");
            area.setText("暂无");
        }
        if (data.getData().getDeal() != null) {
            avgPrice.setText(data.getData().getDeal().getAveragePrice());
            saledPrice.setText(data.getData().getDeal().getSoldMoney());
            saledCount.setText(data.getData().getDeal().getSoldNum());
            saledArea.setText(data.getData().getDeal().getSoldArea());
            unSaledArea.setText(data.getData().getDeal().getUnSoldArea());
            unSaledCount.setText(data.getData().getDeal().getSoldNum());
        } else {
            avgPrice.setText("暂无");
            saledPrice.setText("暂无");
            saledCount.setText("暂无");
            saledArea.setText("暂无");
            unSaledArea.setText("暂无");
            unSaledCount.setText("暂无");
        }
    }

}
