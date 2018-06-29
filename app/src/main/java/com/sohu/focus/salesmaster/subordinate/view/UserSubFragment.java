package com.sohu.focus.salesmaster.subordinate.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseFragment;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.http.model.PostUserId;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestListener;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;
import com.sohu.focus.salesmaster.main.adapter.SubHolder;
import com.sohu.focus.salesmaster.http.api.GetSubApi;
import com.sohu.focus.salesmaster.subordinate.model.SuborModel;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.EasyRecyclerView;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.decoration.DividerDecoration;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 用户下属fragment
 * Created by yuanminjia on 2017/10/31.
 */

public class UserSubFragment extends BaseFragment implements RecyclerArrayAdapter.OnMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.user_sub_list)
    EasyRecyclerView recyclerView;
    RecyclerArrayAdapter<SuborModel.DataBean.ListBean> mAdapter;
    private int mCurrentPage = 1;
    private String mUserId;

    @OnClick(R.id.refresh)
    void errorRefresh() {
        loadData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_sub, container, false);
        ButterKnife.bind(this, view);
        mUserId = getArguments().getString(SalesConstants.EXTRA_ID);
        initView();
        loadData();
        return view;
    }

    public void initView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerDecoration(ContextCompat.getColor(getContext(), R.color.standard_line_light), 2));
        recyclerView.setRefreshListener(this);
        initAdapter();
    }

    public void loadData() {
        GetSubApi api = new GetSubApi();
        PostUserId postUserId = new PostUserId();
        postUserId.setUid(mUserId);
        postUserId.setPage(mCurrentPage++);
        api.setParam(postUserId);
        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<SuborModel>() {
            @Override
            public void onSuccess(SuborModel result, String method) {
                if (result != null && result.getData() != null) {
                    mAdapter.addAll(result.getData().getList());
                }
            }

            @Override
            public void onError(Throwable e) {
                recyclerView.showError();
            }

            @Override
            public void onFailed(SuborModel result, String method) {
                if (result != null) {
                    ToastUtil.toast(result.getMsg());
                }
            }
        });

    }

    public void initAdapter() {
        mAdapter = new RecyclerArrayAdapter<SuborModel.DataBean.ListBean>(getContext()) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new SubHolder(parent);
            }
        };
        mAdapter.setMore(R.layout.recycer_view_more2, this);
        mAdapter.setNoMore(R.layout.recycler_view_nomore2, new RecyclerArrayAdapter.OnNoMoreListener() {
            @Override
            public void onNoMoreShow() {
            }

            @Override
            public void onNoMoreClick() {
                mAdapter.resumeMore();
            }
        });
        mAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent();
                intent.putExtra(SalesConstants.EXTRA_ID, mAdapter.getAllData().get(position).getPersonId());
                intent.putExtra(SalesConstants.EXTRA_NAME, mAdapter.getAllData().get(position).getPersonName());
                intent.putExtra(SalesConstants.EXTRA_ROLE, mAdapter.getItem(position).getSalesRole());
                intent.putExtra(SalesConstants.EXTRA_AREA, mAdapter.getItem(position).getAreaCode());
                intent.setClass(getContext(), SuborActivity.class);
                startActivity(intent);
            }
        });
        recyclerView.setAdapterWithProgress(mAdapter);
    }


    @Override
    public void onRefresh() {
        if (mAdapter != null) {
            mAdapter.clear();
            mCurrentPage = 1;
            loadData();
        }
    }

    @Override
    public void onMoreShow() {
        loadData();
    }

    @Override
    public void onMoreClick() {

    }
}