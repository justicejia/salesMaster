package com.sohu.focus.salesmaster.dynamics.view;

import android.app.Activity;
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
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseFragment;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.comment.AddCommentEvent;
import com.sohu.focus.salesmaster.comment.UpdateComment;
import com.sohu.focus.salesmaster.comment.DelCommentEvent;
import com.sohu.focus.salesmaster.dynamics.model.DynamicCommentBean;
import com.sohu.focus.salesmaster.filter.FilterableView;
import com.sohu.focus.salesmaster.filter.SalesFilterRecorder;
import com.sohu.focus.salesmaster.filter.SalesFilterViewHelper;
import com.sohu.focus.salesmaster.filter.base.FilterRecorder;
import com.sohu.focus.salesmaster.filter.base.FilterViewHelper;
import com.sohu.focus.salesmaster.filter.model.FieldNameManager;
import com.sohu.focus.salesmaster.http.api.GetDynamicsByUidApi;
import com.sohu.focus.salesmaster.http.model.GetDynamicsByUid;
import com.sohu.focus.salesmaster.dynamics.model.DynamicSetModel;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.kernal.bus.RxBus;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestListener;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;
import com.sohu.focus.salesmaster.login.AccountManager;
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
 * 个人中心我的动态页
 * Created by yuanminjia on 2017/10/30.
 */

public class UserDynamicsFragment extends BaseFragment implements RecyclerArrayAdapter.OnMoreListener,
        SwipeRefreshLayout.OnRefreshListener,
        DynamicHolder.DelCallBack,
        DynamicHolder.ShowProjectDetailCallBack,
        FilterableView,
        UpdateComment {

    private static final String TAG = "UserDynamicsFragment";
    private DynamicAdapter mAdapter;
    @BindView(R.id.filter_header)
    View filterHeader;
    @BindView(R.id.me_dynamic_list)
    EasyRecyclerView recyclerView;
    //    filter
    @BindView(R.id.filter_line)
    View filterLine;
    @BindView(R.id.filter_1_wrapper)
    View filterCityWrapper;
    @BindView(R.id.filter_1)
    TextView filterCity;
    @BindView(R.id.filter_2_wrapper)
    View filterRoleWrapper;
    @BindView(R.id.filter_2)
    TextView filterRole;
    @BindView(R.id.filter_3_wrapper)
    View filterProgressWrapper;
    @BindView(R.id.filter_3)
    TextView filterProgress;

    @OnClick(R.id.refresh)
    void errorRefresh() {
        getDynamics();
    }

    SalesFilterViewHelper filterHelper;
    SalesFilterRecorder recorder;

    int page;
    private final int pageSize = 20;
    private String mUserId;
    private boolean isSub = false;  //是否是下属的动态


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_dynamics, container, false);
        ButterKnife.bind(this, view);
        mUserId = getArguments().getString(SalesConstants.EXTRA_ID, AccountManager.INSTANCE.getUserId());
        isSub = getArguments().getBoolean(SalesConstants.EXTRA_IS_SUB, false);
        initView();
        getDynamics();
        registerRxbus();
        return view;
    }

    private void getDynamics() {
        page = 1;
        getMoreDynamics();
    }

    private void getMoreDynamics() {
        GetDynamicsByUid params = new GetDynamicsByUid();
        params.setParams(mUserId, isSub, page, pageSize);
        //        filter
        if (recorder != null) {
            params.city = recorder.getStringList(FieldNameManager.CITY);
            params.salesRole = recorder.getStringList(FieldNameManager.SALE_ROLE);
            params.progress = recorder.getStringList(FieldNameManager.PROGRESS);
        }

        GetDynamicsByUidApi api = new GetDynamicsByUidApi();
        api.setParam(params);
        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<DynamicSetModel>() {
            @Override
            public void onSuccess(DynamicSetModel result, String method) {
                if (result == null || result.getData() == null) return;
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
                if (result != null) {
                    ToastUtil.toast(result.getMsg());
                }
            }
        });
    }

    private void onGetDynamics(DynamicSetModel data) {
        mAdapter.clear();
        for (DynamicSetModel.DataBean.CityListBean bean : data.getData().getCityList()) {
            if (bean.getLabel().equals("全国")) {
                filterCityWrapper.setVisibility(View.VISIBLE);
                break;
            }
        }
        if (data.getData().getUserAvailableRoleList().size() >= 2)
            filterRoleWrapper.setVisibility(View.VISIBLE);
        else
            filterRoleWrapper.setVisibility(View.GONE);

        onGetMoreDynamics(data);
    }

    private void onGetMoreDynamics(DynamicSetModel data) {
        if (!CommonUtils.isEmpty(data.getData().getList()))
            mAdapter.addAll(data.getData().getList());
        else
            mAdapter.addAll(new ArrayList<DynamicSetModel.DataBean.ListBean>());
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
        mAdapter = new DynamicAdapter(getContext());
        mAdapter.setDelCallBack(this);
        mAdapter.setShowProjectDetailCallBack(this);
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

    }

    @Override
    public void del(int position) {
        if (mAdapter != null && mAdapter.getItemCount() > position)
            mAdapter.remove(position);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            getDynamics();
        }
    }

    @Override
    public void showProjectDetail(Intent intent) {
        startActivityForResult(intent, SalesConstants.REQUEST_FOR_PROJECT);
    }

    //    filter
    private void initFilter() {
        filterCity.setText("全部城市");
        filterRole.setText("全部角色");
        filterProgress.setText("全部类型");

        filterHelper = new SalesFilterViewHelper(getContext(), filterLine);
        filterHelper.attach(this);

        if (!isSub) {
            filterHeader.setVisibility(View.GONE);
            return;
        }

//        filterCityWrapper.setVisibility(View.VISIBLE);
//        filterRoleWrapper.setVisibility(View.VISIBLE);
        filterProgressWrapper.setVisibility(View.VISIBLE);

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
    @OnClick(R.id.filter_1_wrapper)
    public void filterCity() {
        if (filterHelper == null) return;
        filterHelper.showFilterView(FilterViewHelper.FILTER_CITY, filterCity);
    }

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
