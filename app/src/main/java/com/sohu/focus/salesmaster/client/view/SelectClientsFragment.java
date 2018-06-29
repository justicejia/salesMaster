package com.sohu.focus.salesmaster.client.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseFragment;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.client.adapter.ClientHolder;
import com.sohu.focus.salesmaster.client.model.ClientModel;
import com.sohu.focus.salesmaster.client.model.ClientsModel;
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

public class SelectClientsFragment extends BaseFragment {

    private static final String TAG = "SelectClientsFragment";

    public static SelectClientsFragment newInstance(String projId, ArrayList<String>
            selectClients) {

        Bundle args = new Bundle();
        args.putString(SalesConstants.EXTRA_PROJECT_ID, projId);
        args.putStringArrayList(SalesConstants.EXTRA_SELECT_CLIENT_IDS, selectClients);
        SelectClientsFragment fragment = new SelectClientsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private String projId;

    @BindView(R.id.client_list)
    EasyRecyclerView mRecyclerView;
    ClientAdapter mAdapter;

    @BindView(R.id.client_count)
    TextView clientsCount;
    @BindView(R.id.finish)
    TextView finish;
    @BindView(R.id.select_client_top)
    RelativeLayout topLayout;

    List<String> selectClients;
    private List<ClientModel> selectedModels = new ArrayList<>();
    private int selectedCount = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_selectclients, container, false);
        ButterKnife.bind(this, v);

        Bundle args = getArguments();
        if (args != null) {
            projId = getArguments().getString(SalesConstants.EXTRA_PROJECT_ID);
            selectClients = getArguments().getStringArrayList(SalesConstants.EXTRA_SELECT_CLIENT_IDS);
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
        loadData();
        mRecyclerView.setAdapterWithProgress(mAdapter);
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

                    clientsCount.setVisibility(View.VISIBLE);
                    for (ClientModel client : result.getData().getClients()) {
                        if (selectClients.contains(client.clientId)) {
                            selectedModels.add(client);
                            selectedCount++;
                            selectClients.remove(client.clientId);
                        }
                    }
                    onRefreshSelectedCount(selectedModels.size());
                } else if (result.getData() != null && CommonUtils.isEmpty(result.getData().getClients())) {
                    clientsCount.setVisibility(View.GONE);
                    mAdapter.clear();
                    mAdapter.addAll(new ArrayList<ClientModel>());
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: ");
            }

            @Override
            public void onFailed(ClientsModel result, String method) {
                Log.d(TAG, "onFailed: ");
            }
        });
    }

    @OnClick(R.id.add_client)
    public void addClient() {
        Intent intent = new Intent(getContext(), AddClientActivity.class);
        intent.putExtra(SalesConstants.EXTRA_PROJECT_ID, projId);
        startActivityForResult(intent, SalesConstants.REQUEST_ADD_CLIENT);
    }

    @OnClick(R.id.finish)
    public void finishSelect() {
        if (mAdapter.getSelectedModels() == null)
            return;
        List<ClientModel> clients = mAdapter.getSelectedModels();
        ArrayList<String> selectIds = new ArrayList<>();
        ArrayList<String> selectNames = new ArrayList<>();
        if (CommonUtils.notEmpty(clients)) {
            for (ClientModel client : clients) {
                selectIds.add(client.clientId);
                selectNames.add(client.getName());
            }
        }
//         返回已选 id
        Intent intent = new Intent();
        intent.putStringArrayListExtra(SalesConstants.EXTRA_SELECT_CLIENT_IDS, selectIds);
        intent.putStringArrayListExtra(SalesConstants.EXTRA_SELECT_CLIENT_NAMES, selectNames);
        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();
    }

    @OnClick(R.id.select_client_back)
    void back() {
        getActivity().finish();
    }


    public void onRefreshSelectedCount(int i) {
        String stirng = "已选择 " + "<font color='#799ff3'>" + i + "</font> 位客户";
        clientsCount.setText(Html.fromHtml(stirng));
        finish.setTextColor(ContextCompat.getColor(getContext(), R.color.standard_text_highlight));
    }

    /**
     * ClientAdapter
     */

    public class ClientAdapter extends RecyclerArrayAdapter<ClientModel> {

        private static final String TAG = "ClientAdapter";

        private OnItemClickListener clickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (selectedModels.contains(getItem(position))) {
                    selectedModels.remove(getItem(position));
                    selectedCount--;
                } else {
                    selectedModels.add(getItem(position));
                    selectedCount++;
                }
                onRefreshSelectedCount(selectedCount);
                notifyItemChanged(position);
            }

        };

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
                                if (selectedModels.contains(client)) {
                                    selectedModels.remove(client);
                                    selectedCount--;
                                    onRefreshSelectedCount(selectedCount);
                                }
                            }
                        }).create();
                dialog.show(getFragmentManager(), TAG);
                return true;
            }
        };

        ClientAdapter(Context context) {
            super(context);
            setOnItemClickListener(clickListener);
            setOnItemLongClickListener(longClickListener);
        }

        @Override
        public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
            return new ClientHolder(parent);
        }

        @Override
        public void OnBindViewHolder(BaseViewHolder holder, int position) {
            if (selectedModels.contains(getItem(position)))
                ((ClientHolder) holder).select();
            else
                ((ClientHolder) holder).unSelect();
            super.OnBindViewHolder(holder, position);
        }

        public List<ClientModel> getSelectedModels() {
            return selectedModels;
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
