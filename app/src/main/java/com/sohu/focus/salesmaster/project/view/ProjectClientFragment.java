package com.sohu.focus.salesmaster.project.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseFragment;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.client.adapter.ClientHolder;
import com.sohu.focus.salesmaster.client.model.ClientModel;
import com.sohu.focus.salesmaster.client.model.ClientsModel;
import com.sohu.focus.salesmaster.client.view.AddClientActivity;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.http.api.GetClientsApi;
import com.sohu.focus.salesmaster.http.api.RemoveClientApi;
import com.sohu.focus.salesmaster.http.model.RemoveClient;
import com.sohu.focus.salesmaster.kernal.http.BaseModel;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestListener;
import com.sohu.focus.salesmaster.kernal.log.Logger;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;
import com.sohu.focus.salesmaster.http.model.GetProjectModel;
import com.sohu.focus.salesmaster.uiframe.CommonDialog;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.EasyRecyclerView;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by luckyzhangx on 03/01/2018.
 */

public class ProjectClientFragment extends BaseFragment {

    private static final String TAG = "SelectClientsFragment";

    //    是否可以多选客户
    public static final String SELECTABLE = "selectable";
    private boolean selectable = false;

    private String projId;
    ClientAdapter mAdapter;

    @BindView(R.id.client_list)
    EasyRecyclerView mRecyclerView;

    @OnClick(R.id.refresh)
    void errorRefresh() {
        loadData();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_project_client, container, false);
        ButterKnife.bind(this, v);
        if (selectable) {

        }
        Bundle args = getArguments();
        if (args != null) {
            projId = getArguments().getString(SalesConstants.EXTRA_PROJECT_ID);
            selectable = getArguments().getBoolean(SELECTABLE);
        }
        checkProjId();
        init();
        return v;
    }

    /**
     * @return 是否有有效的楼盘项目 ID
     */
    private boolean checkProjId() {
        if (CommonUtils.isEmpty(projId)) {
            Logger.ZX().e("需要提供楼盘项目 id 才能获取客户列表");
            return false;
        }
        return true;
    }

    private void init() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ClientAdapter(getContext());
        mRecyclerView.setAdapterWithProgress(mAdapter);
        loadData();
    }

    public void loadData() {
        checkProjId();
        GetProjectModel model = new GetProjectModel();
        model.setEstateId(projId);
        model.setType("customer");

        GetClientsApi api = new GetClientsApi();
        api.setModel(model);
        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<ClientsModel>() {
            @Override
            public void onSuccess(ClientsModel result, String method) {
                if (result.getData() != null && CommonUtils.notEmpty(result.getData().getClients())) {
                    mAdapter.clear();
                    mAdapter.addAll(result.getData().getClients());
                } else {
                    mAdapter.addAll(new ArrayList<ClientModel>());
                }
            }

            @Override
            public void onError(Throwable e) {
                mRecyclerView.showError();
            }

            @Override
            public void onFailed(ClientsModel result, String method) {
                if (result != null)
                    ToastUtil.toast(result.getMsg());
            }
        });
    }

    @OnClick(R.id.add_client)
    public void addClient() {
        Intent intent = new Intent(getContext(), AddClientActivity.class);
        intent.putExtra(SalesConstants.EXTRA_PROJECT_ID, projId);
        startActivityForResult(intent, SalesConstants.REQUEST_ADD_CLIENT);
    }

    /**
     * ClientAdapter
     */

    public class ClientAdapter extends RecyclerArrayAdapter<ClientModel> {

        private static final String TAG = "ClientAdapter";

        private List<ClientModel> selectedModels = new ArrayList<>();

        private OnItemLongClickListener longClickListener = new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final int position) {
                //            删除
                CommonDialog dialog = new CommonDialog.CommonDialogBuilder(getContext())
                        .content("确定删除客户?")
                        .leftBtnText("取消")
                        .rightBtnText("确定")
                        .rightBtnListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ClientModel client = mAdapter.getItem(position);
                                RemoveClient removeClient = new RemoveClient(projId, client.clientId);
                                RemoveClientApi api = new RemoveClientApi();
                                api.setRemoveClient(removeClient);
                                HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<BaseModel>() {
                                    @Override
                                    public void onSuccess(BaseModel result, String method) {
                                        ToastUtil.toast("客户删除成功");
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        ToastUtil.toast("客户删除失败，请稍后再试");

                                    }

                                    @Override
                                    public void onFailed(BaseModel result, String method) {
                                        ToastUtil.toast("客户删除失败，请稍后再试");
                                    }
                                });
                                mAdapter.remove(position);
                                mAdapter.notifyItemRemoved(position);
                            }
                        }).create();
                dialog.show(getFragmentManager(), TAG);
                return true;
            }
        };

        ClientAdapter(Context context) {
            super(context);
            setOnItemLongClickListener(longClickListener);
        }

        @Override
        public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
            return new ClientHolder(parent);
        }

        @Override
        public void OnBindViewHolder(BaseViewHolder holder, int position) {
            ((ClientHolder) holder).unSelectable();
            super.OnBindViewHolder(holder, position);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            loadData();
            mAdapter.notifyDataSetChanged();
        }
    }
}
