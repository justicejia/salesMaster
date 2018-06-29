package com.sohu.focus.salesmaster.goal.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseFragment;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.http.api.GetUserKpiHistoryApi2;
import com.sohu.focus.salesmaster.http.model.GetUsrKpiHistory2;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestListener;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.goal.model.ProjKpiHistory;
import com.sohu.focus.salesmaster.goal.model.SubKpiHistory;
import com.sohu.focus.salesmaster.goal.model.UsrKpiHistoryModel;
import com.sohu.focus.salesmaster.goal.adapter.ProjKpiHolder;
import com.sohu.focus.salesmaster.goal.adapter.UserKpiHolder;
import com.sohu.focus.salesmaster.goal.model.UsrKpiHistoryModel2;
import com.sohu.focus.salesmaster.project.view.ProjectActivity;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.EasyRecyclerView;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.decoration.DividerDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by luckyzhangx on 05/02/2018.
 */

/**
 * 通过具体时间（2017年12月） 获取用户 400 kpi 列表
 */

public class UsrKpiHistoryFragment2 extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String POST_MODEL = "get_user_kpi_history";

    private GetUsrKpiHistory2 postModel;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    View back;

    @BindView(R.id.kpi_history_list)
    EasyRecyclerView recyclerView;
    KpiHistoryAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        postModel = getArguments().getParcelable(POST_MODEL);
        View v = inflater.inflate(R.layout.fragment_user_kpi_history, container, false);
        ButterKnife.bind(this, v);

        initRecyclerView();
        initAdapter();
        loadData();

        return v;
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerDecoration(ContextCompat.getColor(getContext(), R.color.standard_line_light), 2));
        recyclerView.setRefreshListener(this);
    }

    private void initAdapter() {
        mAdapter = new KpiHistoryAdapter(getContext());
        mAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mAdapter.kpiType == UsrKpiHistoryModel.TYPE_SUB) {
                    GetUsrKpiHistory2 model = postModel.copy();
                    model.userId = ((SubKpiHistory) mAdapter.getItem(position))
                            .getPersonId();
                    UsrKpiHistoryActivity2.show(getContext(), model);
                } else if (mAdapter.kpiType == UsrKpiHistoryModel.TYPE_PROJ) {
                    ProjectActivity.showDynamics(getContext(), ((ProjKpiHistory) mAdapter.getItem
                            (position)).getProjId(), ((ProjKpiHistory) mAdapter.getItem
                            (position)).getProjName());
                }
            }
        });

        mAdapter.setError(R.layout.recycler_view_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {
            }

            @Override
            public void onErrorClick() {
                mAdapter.resumeMore();
            }
        });
        recyclerView.setAdapterWithProgress(mAdapter);
    }

    private void loadData() {
        GetUserKpiHistoryApi2 api = new GetUserKpiHistoryApi2();
        api.setModel(postModel);

        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<UsrKpiHistoryModel2>() {
            @Override
            public void onSuccess(UsrKpiHistoryModel2 result, String method) {
                if (result == null || result.data == null) return;

                title.setText(result.data.getPersonName());

                mAdapter.clear();
                if (CommonUtils.notEmpty(result.data.subordinateList)) {
                    mAdapter.kpiType = UsrKpiHistoryModel.TYPE_SUB;
                    mAdapter.addAll(result.data.subordinateList);
                } else if (CommonUtils.notEmpty(result.data.estateList)) {
                    mAdapter.kpiType = UsrKpiHistoryModel.TYPE_PROJ;
                    mAdapter.addAll(result.data.estateList);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onFailed(UsrKpiHistoryModel2 result, String method) {

            }
        });
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @OnClick(R.id.back)
    public void finish() {
        getActivity().finish();
    }

    //    adapter

    private static class KpiHistoryAdapter extends RecyclerArrayAdapter {

        public int kpiType = UsrKpiHistoryModel.TYPE_SUB;

        public KpiHistoryAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
            switch (kpiType) {
                case UsrKpiHistoryModel.TYPE_SUB:
                    return new UserKpiHolder(parent);
                case UsrKpiHistoryModel.TYPE_PROJ:
                    return new ProjKpiHolder(parent);
            }
            return new ProjKpiHolder(parent);
        }
    }

}
