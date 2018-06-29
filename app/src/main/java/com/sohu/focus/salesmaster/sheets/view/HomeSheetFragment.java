package com.sohu.focus.salesmaster.sheets.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseFragment;
import com.sohu.focus.salesmaster.base.BaseWebViewActivity;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.http.api.GetUserSheetsApi;
import com.sohu.focus.salesmaster.kernal.http.BaseApi;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestListener;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;
import com.sohu.focus.salesmaster.login.AccountManager;
import com.sohu.focus.salesmaster.me.view.MeActivity;
import com.sohu.focus.salesmaster.newFilter.FilterHelper;
import com.sohu.focus.salesmaster.newFilter.FilterType;
import com.sohu.focus.salesmaster.newFilter.interview.IFilterInterface;
import com.sohu.focus.salesmaster.newFilter.interview.IFilterSheet;
import com.sohu.focus.salesmaster.newFilter.model.CommonFilterItemModel;
import com.sohu.focus.salesmaster.newFilter.presenters.HomeSheetFilterPresenter;
import com.sohu.focus.salesmaster.sheets.adapter.SheetHolder;
import com.sohu.focus.salesmaster.sheets.model.SheetModel;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.EasyRecyclerView;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jiayuanmin on 2018/5/18
 * description:
 */
public class HomeSheetFragment extends BaseFragment implements IFilterInterface, IFilterSheet {

    private static final String TAG = HomeSheetFragment.class.getSimpleName();

    private FilterHelper mFilterHelper;
    private HomeSheetFilterPresenter homeSheetFilterPresenter;
    private RecyclerArrayAdapter<SheetModel.DataBean> mAdapter;
    private int mCurArea = -1;
    private int mCurRole = AccountManager.INSTANCE.getUserRole();

    @BindView(R.id.sheet_filter_city)
    TextView filterCity;
    @BindView(R.id.sheet_filter_role)
    TextView filterRole;
    @BindView(R.id.sheet_filter_city_arrow)
    ImageView cityArrow;
    @BindView(R.id.sheet_filter_role_arrow)
    ImageView roleArrow;
    @BindView(R.id.sheet_list)
    EasyRecyclerView recyclerView;
    @BindView(R.id.sheet_top_divider)
    View divider;

    @BindView(R.id.filter_layout)
    LinearLayout filterLayout;
    @BindView(R.id.sheet_role_layout)
    LinearLayout roleLayout;
    @BindView(R.id.sheet_city_layout)
    LinearLayout cityLayout;

    @OnClick(R.id.sheet_city_layout)
    void selectCity() {
        mFilterHelper.showOrHidePopup(FilterType.CITY, divider, filterCity, cityArrow);
    }

    @OnClick(R.id.sheet_role_layout)
    void selectRole() {
        mFilterHelper.showOrHidePopup(FilterType.ROLE, divider, filterRole, roleArrow);
    }

    @OnClick(R.id.sheet_head)
    void showProfile() {
        Intent intent = new Intent(getContext(), MeActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.sheet_subscribe)
    void subscribe() {
        Intent intent = new Intent(getContext(), SubscriptionActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.refresh)
    void errorClick() {
        getListData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_sheet, container, false);
        ButterKnife.bind(this, view);
        initRecyclerView();
        initData();
        return view;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HttpEngine.getInstance().cancel(TAG);
        mFilterHelper.release();
        homeSheetFilterPresenter.release();
    }

    private void initData() {
        mFilterHelper = new FilterHelper(getContext());
        homeSheetFilterPresenter = new HomeSheetFilterPresenter(AccountManager.INSTANCE.getUserId());
        homeSheetFilterPresenter.attach(this);
        mFilterHelper.attach(this);

        homeSheetFilterPresenter.getSheetFilterData();
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new RecyclerArrayAdapter<SheetModel.DataBean>(getContext()) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new SheetHolder(parent);
            }
        };
        mAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String suffix = "?personId=" + AccountManager.INSTANCE.getUserId() + "&areaCode=" + mCurArea + "&salesRole=" + mCurRole;
                String url = BaseApi.BASE_URL.substring(0, BaseApi.BASE_URL.length() - 1)
                        + mAdapter.getItem(position).getApi() + suffix;
                String moreUrl = BaseApi.BASE_URL.substring(0, BaseApi.BASE_URL.length() - 1)
                        + mAdapter.getItem(position).getApi() + "/trend" + suffix;
                BaseWebViewActivity.naviToWebWithCookie(url, moreUrl, getContext(), mAdapter.getItem(position).getTitle());
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    private void getListData() {
        recyclerView.showProgress();
        GetUserSheetsApi api = new GetUserSheetsApi(AccountManager.INSTANCE.getUserId(), mCurArea, mCurRole);
        api.setTag(TAG);
        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<SheetModel>() {
            @Override
            public void onSuccess(SheetModel result, String method) {
                recyclerView.showRecycler();
                if (result != null) {
                    mAdapter.clear();
                    mAdapter.addAll(result.getData());
                }
            }

            @Override
            public void onError(Throwable e) {
                recyclerView.showError();
            }

            @Override
            public void onFailed(SheetModel result, String method) {
                if (result != null) {
                    ToastUtil.toast(result.getMsg());
                }
                recyclerView.showRecycler();

            }
        });
    }

    @Override
    public void onSelectFilterItem(CommonFilterItemModel item) {
        if (item.getType() == FilterType.CITY) {
            filterCity.setText(item.getLabel());
            mCurArea = item.getOption();
        } else if (item.getType() == FilterType.ROLE) {
            filterRole.setText(item.getLabel());
            mCurRole = item.getOption();
        }
        getListData();
    }

    @Override
    public void onGetSheetFilter(List<CommonFilterItemModel> roleList, List<CommonFilterItemModel> cityList) {
        boolean shouldFilterRole = CommonUtils.notEmpty(roleList) && roleList.size() > 1;
        boolean shouldFilterCity = CommonUtils.notEmpty(cityList) && cityList.size() > 1;
        if (shouldFilterRole) {
            mFilterHelper.setData(FilterType.ROLE, roleList);
        }
        mCurRole = roleList.get(0).getOption();
        filterRole.setText(roleList.get(0).getLabel());
        if (shouldFilterCity) {
            mFilterHelper.setData(FilterType.CITY, cityList);
        }
        CommonFilterItemModel aDefault = CommonFilterItemModel.getDefault();
        if (cityList.contains(aDefault)) {
            mCurArea = aDefault.getOption();
            filterCity.setText(aDefault.getLabel());
        } else {
            mCurArea = cityList.get(0).getOption();
            filterCity.setText(cityList.get(0).getLabel());
        }
        if (!shouldFilterCity && !shouldFilterRole) {
            filterLayout.setVisibility(View.GONE);
        } else {
            filterLayout.setVisibility(View.VISIBLE);
            if (shouldFilterRole) {
                roleLayout.setVisibility(View.VISIBLE);
            } else {
                roleLayout.setVisibility(View.GONE);
            }
            if (shouldFilterCity) {
                cityLayout.setVisibility(View.VISIBLE);
            } else {
                cityLayout.setVisibility(View.GONE);
            }
        }
        getListData();

    }

}
