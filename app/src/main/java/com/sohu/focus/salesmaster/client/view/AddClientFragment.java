package com.sohu.focus.salesmaster.client.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseFragment;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.client.model.ClientModel;
import com.sohu.focus.salesmaster.client.model.ClientsModel;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.http.api.AddClientApi;
import com.sohu.focus.salesmaster.http.api.GetClientsApi;
import com.sohu.focus.salesmaster.http.model.AddClient;
import com.sohu.focus.salesmaster.kernal.http.BaseModel;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestListener;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;
import com.sohu.focus.salesmaster.http.model.GetProjectModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by luckyzhangx on 04/01/2018.
 */

public class AddClientFragment extends BaseFragment {

    @BindView(R.id.post)
    TextView add;
    @BindView(R.id.add_client_name)
    EditText name;
    @BindView(R.id.add_client_job)
    EditText job;
    @BindView(R.id.add_client_tel)
    EditText tel;

    private String clientId = "";
    private String projId = "";
    private static final String TAG = "AddClientFragment";

    public static AddClientFragment newInstance(String projId) {
        return newInstance("", projId);
    }

    public static AddClientFragment newInstance(String clientId, String projId) {

        Bundle args = new Bundle();
        args.putString(SalesConstants.EXTRA_ID, clientId);
        args.putString(SalesConstants.EXTRA_PROJECT_ID, projId);

        AddClientFragment fragment = new AddClientFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            isContentFill();
        }
    };

    private boolean full = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_addclient, container, false);
        ButterKnife.bind(this, v);

        Bundle arguments = getArguments();

        clientId = arguments.getString(SalesConstants.EXTRA_ID);
        projId = arguments.getString(SalesConstants.EXTRA_PROJECT_ID);

        name.addTextChangedListener(textWatcher);
        job.addTextChangedListener(textWatcher);
        tel.addTextChangedListener(textWatcher);


        fillContent();

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HttpEngine.getInstance().cancel(TAG);
    }

    private void fillContent() {

//        如果有 clientId，说明是更新客户，需要先显示客户信息

        if (CommonUtils.isEmpty(clientId) || CommonUtils.isEmpty(projId)) return;

        GetProjectModel model = new GetProjectModel();
        model.setEstateId(projId);
        model.setType("customer");

        GetClientsApi api = new GetClientsApi();
        api.setTag(TAG);
        api.setModel(model);
        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<ClientsModel>() {
            @Override
            public void onSuccess(ClientsModel result, String method) {
                if (result.getData() != null && CommonUtils.notEmpty(result.getData().getClients())) {
                    for (ClientModel model : result.getData().getClients()) {
                        if (model.getClientId().equals(clientId)) {
                            name.setText(model.getName());
                            name.setSelection(name.getText().length());
                            job.setText(model.getJob());
                            tel.setText(model.getTel());
                            break;
                        }
                    }
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

    @OnClick(R.id.post)
    public void postAddClient() {
        if (isContentFill()) {
//            调用接口添加客户
            AddClientApi api = new AddClientApi();
            AddClient body = new AddClient();
            body.clientId = clientId;
            body.projId = projId;
            body.job = job.getText().toString();
            body.name = name.getText().toString();
            body.tel = tel.getText().toString();
            api.setClientModel(body);
            HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<BaseModel>() {
                @Override
                public void onSuccess(BaseModel result, String method) {
//                    成功，应该有提示，并跳转到前一页
                    ToastUtil.toast("客户添加成功");
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onFailed(BaseModel result, String method) {
                    ToastUtil.toast(result.getMsg());
                }
            });
        }

    }

    @OnClick(R.id.back)
    void back() {
        getActivity().finish();
    }

    //    检查是否每一项都有输入文字
    private boolean isContentFill() {
        if (!CommonUtils.isEmpty(projId)) full = true;
        full &=
                name.getText().length() > 0 &&
                        job.getText().length() > 0 &&
                        tel.getText().length() > 0;
        if (full)
            add.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
        else
            add.setTextColor(getContext().getResources().getColor(R.color
                    .standard_text_light_gray));
        return full;
    }

}
