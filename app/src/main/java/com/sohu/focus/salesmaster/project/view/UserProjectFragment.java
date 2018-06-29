package com.sohu.focus.salesmaster.project.view;

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
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.filter.FilterableView;
import com.sohu.focus.salesmaster.filter.SalesFilterRecorder;
import com.sohu.focus.salesmaster.filter.SalesFilterViewHelper;
import com.sohu.focus.salesmaster.filter.SortFilerHelper;
import com.sohu.focus.salesmaster.filter.base.FilterRecorder;
import com.sohu.focus.salesmaster.filter.base.FilterViewHelper;
import com.sohu.focus.salesmaster.filter.model.FieldNameManager;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.http.api.GetProjsByUidApi;
import com.sohu.focus.salesmaster.http.model.GetProjsByUid;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestListener;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;
import com.sohu.focus.salesmaster.login.AccountManager;
import com.sohu.focus.salesmaster.main.adapter.HomeProjectHolder;
import com.sohu.focus.salesmaster.project.model.ProjectModelSet;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.EasyRecyclerView;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.decoration.DividerDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 用户我的项目列表
 * Created by yuanminjia on 2017/10/30.
 */

public class UserProjectFragment extends BaseFragment implements RecyclerArrayAdapter
        .OnMoreListener, SwipeRefreshLayout.OnRefreshListener, FilterableView {

    private static final String TAG = "UserProjectFragment";

    @BindView(R.id.my_project_list)
    EasyRecyclerView recyclerView;
    private RecyclerArrayAdapter<ProjectModelSet.DataBean.ListBean> mAdapter;
    private int page = 1;
    private final int pageSize = 20;
    private String mUserId;

    //    filter
    @BindView(R.id.filter_header)
    View filterHeader;

    @BindView(R.id.filter_line)
    View filterLine;

    @BindView(R.id.filter_1_wrapper)
    View filterCityWrapper;
    @BindView(R.id.filter_1)
    TextView filterCity;

    @BindView(R.id.filter_2_wrapper)
    View filterOrderWrapper;
    @BindView(R.id.filter_2)
    TextView filterOrder;

    @OnClick(R.id.refresh)
    void errorRefresh() {
        getProjs();
    }

    SalesFilterViewHelper filterHelper;
    SalesFilterRecorder recorder;

    private int mCurrentShowStatus = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_project, container, false);
        ButterKnife.bind(this, view);
        mUserId = getArguments().getString(SalesConstants.EXTRA_ID);
        initView();
        getProjs();
        return view;
    }

    public void initView() {
        initFilter();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerDecoration(ContextCompat.getColor(getContext(), R.color.standard_line_light), 2));
        recyclerView.setRefreshListener(this);
        initAdapter();
        recyclerView.setAdapterWithProgress(mAdapter);
    }

    public void initAdapter() {
        mAdapter = new RecyclerArrayAdapter<ProjectModelSet.DataBean.ListBean>(getContext()) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new HomeProjectHolder(parent);
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
                ProjectActivity.showDynamics(getContext(), mAdapter.getItem(position).getEstateId()
                        , mAdapter.getItem(position).getProjectName());
            }
        });
    }

    void getProjs() {
        page = 1;
        getMoreProjs();
    }

    void getMoreProjs() {
        GetProjsByUid param = new GetProjsByUid();
        param.setParams(page, pageSize, mUserId,
                true, "");
        //        filter
        if (recorder != null) {
            param.citys = recorder.getStringList(FieldNameManager.CITY);
            if (recorder.getRecord(FieldNameManager.ESTATE_ORDER) != null)
                param.projOrder = recorder.getRecord(FieldNameManager.ESTATE_ORDER).getFieldValue();
        }

        if (recorder == null || recorder.getRecord(FieldNameManager.ESTATE_ORDER) == null) {
            mCurrentShowStatus = SalesConstants.SORT_SCORE;
        } else {
            mCurrentShowStatus = SortFilerHelper.getType(recorder.getRecord(FieldNameManager.ESTATE_ORDER).getFieldValue());
        }

        GetProjsByUidApi api = new GetProjsByUidApi();
        api.setParam(param);
        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<ProjectModelSet>() {
            @Override
            public void onSuccess(ProjectModelSet result, String method) {
                if (result == null || result.getData() == null) return;
                if (CommonUtils.notEmpty(result.getData().getList())) {
                    for (ProjectModelSet.DataBean.ListBean i : result.getData().getList()) {
                        i.setCurrentShowStatus(mCurrentShowStatus);
                    }
                }
                if (page == 1)
                    onGetProjs(result);
                else
                    onGetMoreProjs(result);
                if (!CommonUtils.isEmpty(result.getData().getList()))
                    page++;
            }

            @Override
            public void onError(Throwable e) {
                recyclerView.showError();
            }

            @Override
            public void onFailed(ProjectModelSet result, String method) {
                if (result != null)
                    ToastUtil.toast(result.getMsg());
            }
        });
    }

    private void onGetProjs(ProjectModelSet projs) {
        mAdapter.clear();
        onGetMoreProjs(projs);
    }

    private void onGetMoreProjs(ProjectModelSet projs) {
        mAdapter.addAll(projs.getData().getList());
    }

    @Override
    public void onRefresh() {
        getProjs();
    }

    @Override
    public void onMoreShow() {
        getMoreProjs();
    }

    @Override
    public void onMoreClick() {

    }

    //    filter
    private void initFilter() {
        filterHelper = new SalesFilterViewHelper(getContext(), filterLine);
        filterHelper.attach(this);
        filterCity.setText("全部城市");
        filterOrder.setText("按评分排序");

        filterHelper = new SalesFilterViewHelper(getContext(), filterLine);
        filterHelper.attach(this);

        if (AccountManager.INSTANCE.getUserId().equals(mUserId)) {
            filterHeader.setVisibility(View.GONE);
            return;
        }

        filterCityWrapper.setVisibility(View.VISIBLE);
        filterOrderWrapper.setVisibility(View.VISIBLE);
    }

    //    implement filterableView
    @Override
    public void filter(FilterRecorder recorder) {
        try {
            this.recorder = (SalesFilterRecorder) recorder;
        } catch (Exception e) {
            e.printStackTrace();
        }
        getProjs();
    }

    //    click
    @OnClick(R.id.filter_1_wrapper)
    public void filterCity() {
        if (filterHelper == null) return;
        filterHelper.showFilterView(FilterViewHelper.FILTER_CITY, filterCity);
    }

    @OnClick(R.id.filter_2_wrapper)
    public void filterOrder() {
        if (filterHelper == null) return;
        filterHelper.showFilterView(FilterViewHelper.FILTER_PROJ_ORDER, filterOrder);
    }
}
