package com.sohu.focus.salesmaster.comment.view;

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
import com.sohu.focus.salesmaster.comment.adapter.UnreadCommentHolder;
import com.sohu.focus.salesmaster.comment.model.UnreadCommentSetModel;
import com.sohu.focus.salesmaster.dynamics.model.DynamicCommentBean;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.http.api.GetUnreadCommentApi;
import com.sohu.focus.salesmaster.http.model.GetUnreadComment;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestListener;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.EasyRecyclerView;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.decoration.DividerDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by luckyzhangx on 14/03/2018.
 */

public class UnreadCommentsFragment extends BaseFragment implements RecyclerArrayAdapter.OnMoreListener,
        SwipeRefreshLayout.OnRefreshListener {

    private int page = 1;

    @BindView(R.id.unreadComments)
    EasyRecyclerView mComments;
    private RecyclerArrayAdapter<DynamicCommentBean> mAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unreadcoments, container, false);
        ButterKnife.bind(this, view);
        initView();
        onRefresh();
        return view;
    }

    private void initView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mComments.setLayoutManager(layoutManager);
        mComments.addItemDecoration(new DividerDecoration(ContextCompat.getColor(getContext(), R.color
                .standard_line_light), 2));
        mComments.setRefreshListener(this);
        mComments.setEmptyView(R.layout.empty_dynamics);
        initAdapter();
        mComments.setAdapterWithProgress(mAdapter);
    }

    private void initAdapter() {
        mAdapter = new RecyclerArrayAdapter<DynamicCommentBean>(getContext()) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {

                UnreadCommentHolder holder = new UnreadCommentHolder(parent);
                return holder;
            }
        };
        mAdapter.setMore(R.layout.recycer_view_more_comment, this);
        mAdapter.setNoMore(R.layout.recycler_view_nomore_comment, new RecyclerArrayAdapter
                .OnNoMoreListener() {
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

    }

    @Override
    public void onRefresh() {
        getComments();
    }

    @Override
    public void onMoreShow() {
//        getMoreComments();
    }

    @Override
    public void onMoreClick() {
        getMoreComments();
    }

    private void getComments() {
        page = 1;
        getMoreComments();
    }

    private void getMoreComments() {
        GetUnreadComment model = new GetUnreadComment();
        model.setPageSize(20);
        if (mAdapter.getCount() == 0) {
            model.setIncludeHistory(0);
        } else {
            model.setIncludeHistory(1);
        }
        model.setOffset(mAdapter.getCount());

        GetUnreadCommentApi api = new GetUnreadCommentApi(model);
        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<UnreadCommentSetModel>() {
            @Override
            public void onSuccess(UnreadCommentSetModel result, String method) {
                if (result == null || result.getData() == null) return;
                if (page == 1)
                    onGetDynamics(result);
                else
                    onGetMoreDynamics(result);
                if (!CommonUtils.isEmpty(result.getData().getComments()))
                    page++;
            }

            @Override
            public void onError(Throwable e) {
                mAdapter.addAll(new ArrayList<DynamicCommentBean>());
            }

            @Override
            public void onFailed(UnreadCommentSetModel result, String method) {
                if (result != null) {
                    ToastUtil.toast(result.getMsg());
                }
            }
        });
    }

    private void onGetDynamics(UnreadCommentSetModel data) {
        mAdapter.clear();

        onGetMoreDynamics(data);
    }

    private void onGetMoreDynamics(UnreadCommentSetModel data) {
        if (!CommonUtils.isEmpty(data.getData().getComments()))
            mAdapter.addAll(data.getData().getComments());
        else
            mAdapter.addAll(new ArrayList<DynamicCommentBean>());
    }

    @OnClick(R.id.back)
    public void back() {
        getActivity().finish();
    }
}

