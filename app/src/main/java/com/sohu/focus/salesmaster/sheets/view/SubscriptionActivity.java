package com.sohu.focus.salesmaster.sheets.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseActivity;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.http.api.GetUserSubscribeListApi;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestListener;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;
import com.sohu.focus.salesmaster.login.AccountManager;
import com.sohu.focus.salesmaster.sheets.adapter.SubscriptionAdapter;
import com.sohu.focus.salesmaster.sheets.model.SubscriptionModel;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.EasyRecyclerView;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.decoration.DividerDecoration;
import com.sohu.focus.salesmaster.uiframe.statusbar.StatusBarHelper;
import com.sohu.focus.salesmaster.uiframe.statusbar.SystemStatusManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sohu.focus.salesmaster.sheets.adapter.SubscriptionAdapter.STATUS_APPLICATION;
import static com.sohu.focus.salesmaster.sheets.adapter.SubscriptionAdapter.STATUS_DIVIDER;
import static com.sohu.focus.salesmaster.sheets.adapter.SubscriptionAdapter.STATUS_SUBSCRIABLE;
import static com.sohu.focus.salesmaster.sheets.adapter.SubscriptionAdapter.STATUS_SUBSCRIBED;

/**
 * Created by jiayuanmin on 2018/5/22
 * description:
 */
public class SubscriptionActivity extends BaseActivity {

    private static final String TAG = SubscriptionActivity.class.getSimpleName();

//    private SubscriptionAdapter mAdapter;

//    @BindView(R.id.subscribed_list)
//    EasyRecyclerView recyclerView;

    @OnClick(R.id.subscribe_back)
    void goBack() {
        this.finish();
    }

    @OnClick(R.id.back_center)
    void goback2() {
        this.finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        ButterKnife.bind(this);
        if (StatusBarHelper.statusBarLightMode(this) != StatusBarHelper.OTHER) {
            new SystemStatusManager(this).setTranslucentStatus(R.color.white);
        }
    }

//    void initView() {
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.addItemDecoration(new DividerDecoration(this));
//        mAdapter = new SubscriptionAdapter(this);
//        recyclerView.setAdapterWithProgress(mAdapter);
//    }
//
//    void getData() {
//        GetUserSubscribeListApi api = new GetUserSubscribeListApi(AccountManager.INSTANCE.getUserId());
//        api.setTag(TAG);
//        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<SubscriptionModel>() {
//            @Override
//            public void onSuccess(SubscriptionModel result, String method) {
//                if (result != null) {
//                    mAdapter.addAll(process(result.getData()));
//                }
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onFailed(SubscriptionModel result, String method) {
//                if (result != null) {
//                    ToastUtil.toast(result.getMsg());
//                }
//            }
//        });
//    }
//
//    List<SubscriptionModel.DataBean> process(List<SubscriptionModel.DataBean> list) {
//        List<SubscriptionModel.DataBean> result = new ArrayList<>();
//        List<SubscriptionModel.DataBean> result1 = new ArrayList<>();
//        List<SubscriptionModel.DataBean> result2 = new ArrayList<>();
//        List<SubscriptionModel.DataBean> result3 = new ArrayList<>();
//        for (SubscriptionModel.DataBean item : list) {
//            if (item.getStatus() == STATUS_SUBSCRIBED) {
//                result1.add(item);
//            } else if (item.getStatus() == STATUS_SUBSCRIABLE) {
//                result2.add(item);
//            } else if (item.getStatus() == STATUS_APPLICATION) {
//                result3.add(item);
//            }
//        }
//
//        if (result1.size() > 0) {
//            SubscriptionModel.DataBean head = new SubscriptionModel.DataBean();
//            head.setStatus(STATUS_DIVIDER);
//            head.setTitle("已订阅");
//            result.add(head);
//            result.addAll(result1);
//        }
//        if (result2.size() > 0) {
//            SubscriptionModel.DataBean head = new SubscriptionModel.DataBean();
//            head.setStatus(STATUS_DIVIDER);
//            head.setTitle("更多订阅");
//            result.add(head);
//            result.addAll(result2);
//        }
//        if (result3.size() > 0) {
//            SubscriptionModel.DataBean head = new SubscriptionModel.DataBean();
//            head.setStatus(STATUS_DIVIDER);
//            head.setTitle("申请中");
//            result.add(head);
//            result.addAll(result3);
//        }
//        return result;
//    }
}
