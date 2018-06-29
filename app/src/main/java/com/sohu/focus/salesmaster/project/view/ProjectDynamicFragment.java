package com.sohu.focus.salesmaster.project.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseFragment;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.comment.AddCommentEvent;
import com.sohu.focus.salesmaster.comment.UpdateComment;
import com.sohu.focus.salesmaster.comment.DelCommentEvent;
import com.sohu.focus.salesmaster.dynamics.model.DynamicCommentBean;
import com.sohu.focus.salesmaster.dynamics.model.DynamicSetModel;
import com.sohu.focus.salesmaster.filter.FilterableView;
import com.sohu.focus.salesmaster.filter.SalesFilterRecorder;
import com.sohu.focus.salesmaster.filter.SalesFilterViewHelper;
import com.sohu.focus.salesmaster.filter.base.FilterRecorder;
import com.sohu.focus.salesmaster.filter.base.FilterViewHelper;
import com.sohu.focus.salesmaster.filter.model.FieldNameManager;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.http.api.GetDynamicsByProjIdApi;
import com.sohu.focus.salesmaster.http.model.GetDynamicsByProjId;
import com.sohu.focus.salesmaster.kernal.bus.RxBus;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestListener;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.kernal.utils.ParseUtil;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;
import com.sohu.focus.salesmaster.main.adapter.DynamicAdapter;
import com.sohu.focus.salesmaster.main.adapter.DynamicHolder;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.EasyRecyclerView;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.decoration.DividerDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;


/**
 * Created by yuanminjia on 2018/1/2.
 */

public class ProjectDynamicFragment extends BaseFragment implements RecyclerArrayAdapter.OnMoreListener, SwipeRefreshLayout.OnRefreshListener,
        DynamicHolder.DelCallBack, DynamicHolder.ShowProjectDetailCallBack, FilterableView, UpdateComment {

    public static final String TAG = "ProjectDynamicFragment";

    @BindView(R.id.project_dynamic_list)
    EasyRecyclerView recyclerView;

    @OnClick(R.id.refresh)
    void errorRefresh() {
        getDynamics();
    }

    //    filter
    @BindView(R.id.filter_line)
    View filterLine;

    @BindView(R.id.filter_2_wrapper)
    View filterRoleWrapper;
    @BindView(R.id.filter_2)
    TextView filterRole;

    @BindView(R.id.filter_3_wrapper)
    View filterProgressWrapper;
    @BindView(R.id.filter_3)
    TextView filterProgress;

    SalesFilterViewHelper filterHelper;
    SalesFilterRecorder recorder;

    private DynamicAdapter mAdapter;
    private int page;
    private String mProjectId;
    private boolean deleted = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_dynamic, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        mProjectId = bundle.getString(SalesConstants.EXTRA_PROJECT_ID);
        initView();
        registerRxbus();
        return view;
    }

    void initView() {
        initFilter();
        initRecyclerVIew();
        initAdapter();
        getDynamics();
    }

    public void initRecyclerVIew() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setRefreshListener(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerDecoration(ContextCompat.getColor(getContext(), R.color.standard_line_light), 2));

    }

    private void initAdapter() {
        mAdapter = new DynamicAdapter(getContext());
        mAdapter.setDelCallBack(this);
        mAdapter.setShowProjectDetailCallBack(this);
        recyclerView.setAdapterWithProgress(mAdapter);
    }

    private void getDynamics() {
        page = 1;
        getMoreDynamics();
    }

    private void getMoreDynamics() {
        GetDynamicsByProjId projectId = new GetDynamicsByProjId();
        projectId.setProjId(ParseUtil.parseInt(this.mProjectId, 0));
        projectId.setPage(page);
        projectId.setPageSize(20);
        //        filter
        if (recorder != null) {
            projectId.salesRole = recorder.getStringList(FieldNameManager.SALE_ROLE);
            projectId.progress = recorder.getStringList(FieldNameManager.PROGRESS);
        }

        GetDynamicsByProjIdApi api = new GetDynamicsByProjIdApi();
        api.setProjectId(projectId);
        api.setTag(TAG);
        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<DynamicSetModel>() {
            @Override
            public void onSuccess(DynamicSetModel result, String method) {
                if (result == null || result.getData() == null) {
                    return;
                }
                if (page == 1)
                    onGetDynamics(result);
                else
                    onGetMoreDynamics(result);
                if (!CommonUtils.isEmpty(result.getData().getList()))
                    page++;
            }

            @Override
            public void onError(Throwable e) {
                recyclerView.showError();
            }

            @Override
            public void onFailed(DynamicSetModel result, String method) {
                if (result != null)
                    ToastUtil.toast(result.getMsg());
            }
        });
    }

    private void onGetDynamics(DynamicSetModel data) {
        mAdapter.clear();
        onGetMoreDynamics(data);
    }

    private void onGetMoreDynamics(DynamicSetModel data) {
        mAdapter.addAll(data.getData().getList());
    }

    @Override
    public void del(int position) {
        if (mAdapter != null && mAdapter.getItemCount() > position) {
            mAdapter.remove(position - 1);
            deleted = true;
        }

    }

    @Override
    public void onRefresh() {
        getDynamics();
    }

    @Override
    public void onMoreShow() {
        getMoreDynamics();
    }

    @Override
    public void onMoreClick() {
        getMoreDynamics();
    }

    //    filter
    private void initFilter() {
        filterHelper = new SalesFilterViewHelper(getContext(), filterLine);
        filterHelper.attach(this);

        filterRoleWrapper.setVisibility(View.VISIBLE);
        filterRole.setText("全部角色");
        filterProgressWrapper.setVisibility(View.VISIBLE);
        filterProgress.setText("全部类型");

    }

    //implement filterable
    @Override
    public void filter(FilterRecorder recorder) {
        try {
            this.recorder = (SalesFilterRecorder) recorder;
        } catch (Exception e) {
            e.printStackTrace();
        }
        getDynamics();
    }

    //    click

    @OnClick(R.id.filter_2_wrapper)
    public void filterRole() {
        if (filterHelper == null) return;
        filterHelper.showFilterView(FilterViewHelper.FILTER_SALES_ROLE, filterRole);
    }

    @OnClick(R.id.filter_3_wrapper)
    public void filterProgress() {
        if (filterHelper == null) return;
        filterHelper.showFilterView(FilterViewHelper.FILTER_PROGRESS, filterProgress);
    }

    @Override
    public void showProjectDetail(Intent intent) {
        startActivityForResult(intent, SalesConstants.REQUEST_FOR_PROJECT);
    }

    private void registerRxbus() {
        Subscription subscription1 = RxBus.get().subscribe(AddCommentEvent.class, new RxBus
                .OnHandleEvent<AddCommentEvent>() {
            @Override
            public void onEvent(AddCommentEvent addCommentEvent) {
                addComment(addCommentEvent.getDynamicId(), addCommentEvent.getComment());
            }
        });
        Subscription subscription2 = RxBus.get().subscribe(DelCommentEvent.class, new RxBus
                .OnHandleEvent<DelCommentEvent>() {
            @Override
            public void onEvent(DelCommentEvent delCommentEvent) {
                delComment(delCommentEvent.getDynamicId(), delCommentEvent.getCommentId());
            }
        });

        subscriptionList.add(subscription1);
        subscriptionList.add(subscription2);
    }

    @Override
    public boolean delComment(String dynamicId, String commentId) {
        return mAdapter.delComment(dynamicId, commentId);
    }

    @Override
    public boolean addComment(String dynamicId, DynamicCommentBean comment) {
        return mAdapter.addComment(dynamicId, comment);
    }
}
