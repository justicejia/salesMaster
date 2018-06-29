package com.sohu.focus.salesmaster.main.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseFragment;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.comment.AddCommentEvent;
import com.sohu.focus.salesmaster.comment.UpdateComment;
import com.sohu.focus.salesmaster.comment.model.UnreadCommentCountModel;
import com.sohu.focus.salesmaster.comment.DelCommentEvent;
import com.sohu.focus.salesmaster.comment.view.UnreadCommentsActivity;
import com.sohu.focus.salesmaster.dynamics.model.DynamicCommentBean;
import com.sohu.focus.salesmaster.dynamics.model.DynamicSetModel;
import com.sohu.focus.salesmaster.filter.FilterableView;
import com.sohu.focus.salesmaster.filter.SalesFilterRecorder;
import com.sohu.focus.salesmaster.filter.SalesFilterViewHelper;
import com.sohu.focus.salesmaster.filter.base.FilterRecorder;
import com.sohu.focus.salesmaster.filter.base.FilterViewHelper;
import com.sohu.focus.salesmaster.filter.model.FieldNameManager;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.http.api.GetDynamicsByUidApi;
import com.sohu.focus.salesmaster.http.api.GetUnreadCommentCountApi;
import com.sohu.focus.salesmaster.http.model.GetDynamicsByUid;
import com.sohu.focus.salesmaster.http.model.GetUnreadCommentCount;
import com.sohu.focus.salesmaster.kernal.bus.RxBus;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestListener;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;
import com.sohu.focus.salesmaster.login.AccountManager;
import com.sohu.focus.salesmaster.main.adapter.DynamicAdapter;
import com.sohu.focus.salesmaster.main.adapter.DynamicHolder;
import com.sohu.focus.salesmaster.me.view.MeActivity;
import com.sohu.focus.salesmaster.progress.view.AddProgressActivity;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.EasyRecyclerView;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.decoration.DividerDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

/**
 * 首页我的动态页
 * Created by yuanminjia on 2017/10/26.
 */

public class HomeDynamicsFragment extends BaseFragment implements RecyclerArrayAdapter.OnMoreListener,
        SwipeRefreshLayout.OnRefreshListener,
        DynamicHolder.DelCallBack,
        DynamicHolder.ShowProjectDetailCallBack,
        FilterableView,
        UpdateComment {

    private static final String TAG = "HomePageFragment";

    @BindView(R.id.toComments)
    View toComment;
    @BindView(R.id.unreadComment)
    TextView unreadComment;


    @BindView(R.id.home_dynamic_list)
    EasyRecyclerView recyclerView;
    DynamicAdapter mAdapter;

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
    void refresh() {
        getDynamics();
    }

    SalesFilterViewHelper filterHelper;
    SalesFilterRecorder recorder;

    private int page = 1;
    private final int pageSize = 20;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_home, container, false);
        ButterKnife.bind(this, view);
        initView();
        registerRxbus();
        getDynamics();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getUnreadCommentsCount();
    }

    @OnClick(R.id.home_user)
    void jumpToUser() {
        Intent intent = new Intent();
        intent.setClass(getContext(), MeActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.home_plus)
    void addProgress() {
        Intent intent = new Intent();
        intent.setClass(getContext(), AddProgressActivity.class);
        startActivityForResult(intent, SalesConstants.REQUEST_ADD_DYNAMICS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            getDynamics();
        }
    }

    public void initView() {
        initFilter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerDecoration(ContextCompat.getColor(getContext(), R.color.standard_line_light), 2));
        recyclerView.setRefreshListener(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        initAdapter();
    }

    private void getDynamics() {
        page = 1;
        getMoreDynamics();
    }

    private void getMoreDynamics() {
        GetDynamicsByUid params = new GetDynamicsByUid();
        params.setParams(AccountManager.INSTANCE.getUserId(), true, page, pageSize);

        if (recorder != null) {
            params.city = recorder.getStringList(FieldNameManager.CITY);
            params.salesRole = recorder.getStringList(FieldNameManager.SALE_ROLE);
            params.progress = recorder.getStringList(FieldNameManager.PROGRESS);
        }

        GetDynamicsByUidApi api = new GetDynamicsByUidApi();
        api.setTag(TAG);
        api.setParam(params);
        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<DynamicSetModel>() {
            @Override
            public void onSuccess(DynamicSetModel result, String method) {
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
        if (mAdapter != null)
            mAdapter.clear();
        onGetMoreDynamics(data);
    }

    private void onGetMoreDynamics(DynamicSetModel data) {
        if (mAdapter == null)
            return;
        if (!CommonUtils.isEmpty(data.getData().getList()))
            mAdapter.addAll(data.getData().getList());
        else
            mAdapter.addAll(new ArrayList<DynamicSetModel.DataBean.ListBean>());
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

        recyclerView.setAdapterWithProgress(mAdapter);

    }

    @Override
    public void onRefresh() {
        getDynamics();
        getUnreadCommentsCount();
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
    public void showProjectDetail(Intent intent) {
        startActivityForResult(intent, SalesConstants.REQUEST_FOR_PROJECT);
    }

    //    filter
    private void initFilter() {
        filterHelper = new SalesFilterViewHelper(getContext(), filterLine);
        filterHelper.attach(this);

        filterCityWrapper.setVisibility(View.VISIBLE);
        filterCity.setText("全部城市");
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

    // 评论

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

    private void getUnreadCommentsCount() {
        GetUnreadCommentCount model = new GetUnreadCommentCount();
//        model.setPersonId();
        GetUnreadCommentCountApi api = new GetUnreadCommentCountApi(model);

        HttpEngine.getInstance().doHttpRequest(api, new
                HttpRequestListener<UnreadCommentCountModel>() {
                    @Override
                    public void onSuccess(UnreadCommentCountModel result, String method) {
                        if (result == null || result.getData() == null)
                            return;
                        unreadComment.setText(result.getData().getCommentator() + " "
                                + result.getData().getCount() + "条新评论");
                        if (result.getData().getCount() > 0)
                            showCommentsCount();
                        else
                            hideCommentsCount();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: ");

                    }

                    @Override
                    public void onFailed(UnreadCommentCountModel result, String method) {
                        Log.d(TAG, "onFailed: ");
                    }
                });
    }

    private void showCommentsCount() {
        if (Build.VERSION.SDK_INT > 19)
            TransitionManager.beginDelayedTransition((ViewGroup) getView());
        toComment.setVisibility(View.VISIBLE);
    }

    private void hideCommentsCount() {
        if (Build.VERSION.SDK_INT > 19)
            TransitionManager.beginDelayedTransition((ViewGroup) getView());
        toComment.setVisibility(View.GONE);
    }

    @OnClick(R.id.toComments)
    public void showUnreadComment() {
        toComment.setVisibility(View.GONE);
        UnreadCommentsActivity.showUnreadComments(getContext());
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
