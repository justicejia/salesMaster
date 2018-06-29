package com.sohu.focus.salesmaster.dynamics.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseFragment;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.comment.AddCommentEvent;
import com.sohu.focus.salesmaster.comment.UpdateComment;
import com.sohu.focus.salesmaster.comment.DelCommentEvent;
import com.sohu.focus.salesmaster.dynamics.model.DynamicCommentBean;
import com.sohu.focus.salesmaster.dynamics.model.DynamicDetailModel;
import com.sohu.focus.salesmaster.dynamics.model.DynamicSetModel;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.http.api.GetDynamicDetailApi;
import com.sohu.focus.salesmaster.http.model.GetDynamicDetail;
import com.sohu.focus.salesmaster.kernal.bus.RxBus;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestListener;
import com.sohu.focus.salesmaster.main.adapter.DynamicHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

/**
 * Created by luckyzhangx on 14/03/2018.
 */

public class DynamicDetailFragment extends BaseFragment implements UpdateComment {

    private String dynamicId = "0";

    private DynamicSetModel.DataBean.ListBean mData;

    @BindView(R.id.dynamic_container)
    ViewGroup dynamicContainer;
    DynamicHolder holder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dynamicId = getArguments().getString(SalesConstants.EXTRA_DYNAMIC_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dynamicdetail, container, false);
        ButterKnife.bind(this, view);

        initView((ViewGroup) view);
        registerRxbus();

        return view;
    }

    private void initView(ViewGroup view) {
        holder = new DynamicHolder(view);
        dynamicContainer.addView(holder.itemView);
        getDynamic();
    }

    private void getDynamic() {
        GetDynamicDetail model = new GetDynamicDetail();
        model.setDynamicId(dynamicId);
        GetDynamicDetailApi api = new GetDynamicDetailApi(model);

        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<DynamicDetailModel>() {
            @Override
            public void onSuccess(DynamicDetailModel result, String method) {
                holder.setData(result.getData());
                mData = result.getData();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onFailed(DynamicDetailModel result, String method) {

            }
        });
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
        if (mData != null && mData.getSalesProjectID().equals(dynamicId)) {
            for (DynamicCommentBean comment : mData.getCommentList()) {
                if (comment.getCommentId().equals(commentId)) {
                    mData.getCommentList().remove(comment);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean addComment(String dynamicId, DynamicCommentBean comment) {
        if (mData != null && mData.getSalesProjectID().equals(dynamicId)) {
            for (DynamicCommentBean commentBean : mData.getCommentList()) {
                if (commentBean.getCommentId().equals(comment.getCommentId())) {
                    return false;
                }
            }
            mData.getCommentList().add(comment);
            return true;
        }
        return false;
    }

    @OnClick(R.id.back)
    public void back() {
        getActivity().finish();
    }
}
