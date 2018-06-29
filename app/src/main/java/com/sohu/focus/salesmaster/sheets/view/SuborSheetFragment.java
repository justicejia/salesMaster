package com.sohu.focus.salesmaster.sheets.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sohu.focus.salesmaster.base.BaseWebViewActivity;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.http.api.GetUserSheetsApi;
import com.sohu.focus.salesmaster.kernal.http.BaseApi;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestListener;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;
import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseFragment;
import com.sohu.focus.salesmaster.sheets.adapter.SubSheetHolder;
import com.sohu.focus.salesmaster.sheets.model.SheetModel;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.EasyRecyclerView;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.decoration.DividerDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jiayuanmin on 2018/5/18
 * description:
 */
public class SuborSheetFragment extends BaseFragment {

    private static final String TAG = HomeSheetFragment.class.getSimpleName();

    private RecyclerArrayAdapter<SheetModel.DataBean> mAdapter;
    private int mAreaCode = -1;
    private int mRole;
    private String mUserId;

    @BindView(R.id.sheet_list)
    EasyRecyclerView recyclerView;

    @OnClick(R.id.refresh)
    void errorClick() {
        getData();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub_sheet, container, false);
        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            mUserId = getArguments().getString(SalesConstants.EXTRA_ID);
            mAreaCode = getArguments().getInt(SalesConstants.EXTRA_AREA);
            mRole = getArguments().getInt(SalesConstants.EXTRA_ROLE);
        }
        initView();
        getData();
        return view;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HttpEngine.getInstance().cancel(TAG);
    }

    private void initView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerDecoration(getContext()));
        mAdapter = new RecyclerArrayAdapter<SheetModel.DataBean>(getContext()) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new SubSheetHolder(parent);
            }
        };
        mAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String suffix = "?personId=" + mUserId + "&areaCode=" + mAreaCode + "&salesRole=" + mRole;
                String url = BaseApi.BASE_URL.substring(0, BaseApi.BASE_URL.length() - 1)
                        + mAdapter.getItem(position).getApi() + suffix;
                String moreUrl = BaseApi.BASE_URL.substring(0, BaseApi.BASE_URL.length() - 1)
                        + mAdapter.getItem(position).getApi() + "/trend" + suffix;
                BaseWebViewActivity.naviToWebWithCookie(url, moreUrl, getContext(), mAdapter.getItem(position).getTitle());
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    private void getData() {
        recyclerView.showProgress();
        GetUserSheetsApi api = new GetUserSheetsApi(mUserId, mAreaCode, mRole);
        api.setTag(TAG);
        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<SheetModel>() {
            @Override
            public void onSuccess(SheetModel result, String method) {
                if (result != null) {
                    mAdapter.clear();
                    mAdapter.addAll(result.getData());
                }
                recyclerView.showRecycler();
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

}
