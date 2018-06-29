package com.sohu.focus.salesmaster.progress.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
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
import com.sohu.focus.salesmaster.kernal.utils.ParseUtil;
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

import static android.app.Activity.RESULT_OK;

/**
 * 首页楼盘项目页
 * Created by yuanminjia on 2017/10/27.
 */

public class SelectProjectFragment extends BaseFragment
        implements RecyclerArrayAdapter.OnMoreListener, SwipeRefreshLayout.OnRefreshListener,
        FilterableView {

    private static final String TAG = "SelectProjectFragment";

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.project_list)
    EasyRecyclerView recyclerView;
    String keyword = "";

    //    filter
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

    SalesFilterViewHelper filterHelper;
    SalesFilterRecorder recorder;

    private RecyclerArrayAdapter<ProjectModelSet.DataBean.ListBean> mAdapter;

    private int mPage = 0;
    private int mCurrentShowStatus;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (getView() != null) return getView();

        View view = inflater.inflate(R.layout.fragment_main_project, container, false);
        ButterKnife.bind(this, view);


        initView();
        initAdapter();
        getProjs();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HttpEngine.getInstance().cancel(TAG);
    }

    private void initView() {
        title.setText(getContext().getString(R.string.select_project));

        initFilter();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerDecoration(ContextCompat.getColor(getContext(), R.color.standard_line_light), 2));
        recyclerView.setRefreshListener(this);
    }

    private void initAdapter() {
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

        mAdapter.setError(R.layout.recycler_view_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {
            }

            @Override
            public void onErrorClick() {
                mAdapter.resumeMore();
            }
        });

        mAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent();
                String name = (mAdapter.getItem(position)).getProjectName();
                int num = (mAdapter.getItem(position)).getCustomerNum();
                int projectId = ParseUtil.parseInt((mAdapter.getItem(position)).getEstateId(), -1);
                intent.putExtra(SalesConstants.EXTRA_PROJECT, name);
                intent.putExtra(SalesConstants.EXTRA_PROJECT_ID, projectId);
                intent.putExtra(SalesConstants.EXTRA_PROJECT_CLIENT_NUM,num);
                getActivity().setResult(RESULT_OK, intent);
                getActivity().finish();
            }
        });

        recyclerView.setAdapterWithProgress(mAdapter);
    }

    private void getProjs() {
        mPage = 1;
        HttpEngine.getInstance().cancel(TAG);
        getMoreProjs();
    }

    private void getMoreProjs() {
        GetProjsByUid param = new GetProjsByUid();
        param.setParams(mPage, 20, AccountManager.INSTANCE.getUserId(),
                true, keyword);
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
        api.setTag(TAG);
        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<ProjectModelSet>() {
            @Override
            public void onSuccess(ProjectModelSet result, String method) {
                if (result == null || result.getData() == null)
                    return;
                if (CommonUtils.notEmpty(result.getData().getList())) {
                    for (ProjectModelSet.DataBean.ListBean i : result.getData().getList()) {
                        i.setCurrentShowStatus(mCurrentShowStatus);
                    }
                }
                if (mPage == 1) onGetProjs(result);
                else onGetMoreProjs(result);
                if (!CommonUtils.isEmpty(result.getData().getList()))
                    mPage++;
            }

            @Override
            public void onError(Throwable e) {
                mAdapter.addAll(new ArrayList<ProjectModelSet.DataBean.ListBean>());
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
        if (!CommonUtils.isEmpty(projs.getData().getList())) {
            mAdapter.addAll(projs.getData().getList());
        } else
            mAdapter.addAll(new ArrayList<ProjectModelSet.DataBean.ListBean>());
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
        getMoreProjs();
    }

    //    filter
    private void initFilter() {
        filterCityWrapper.setVisibility(View.VISIBLE);
        filterCity.setText("全部城市");
        filterHelper = new SalesFilterViewHelper(getContext(), filterLine);
        filterHelper.attach(this);
        filterOrderWrapper.setVisibility(View.VISIBLE);
        filterOrder.setText("按评分排序");
        filterHelper = new SalesFilterViewHelper(getContext(), filterLine);
        filterHelper.attach(this);
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

    @OnClick(R.id.search_proj)
    public void search() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, new SearchSelectProjFragment());

        transaction.addToBackStack(null);
        transaction.commit();
    }
}
