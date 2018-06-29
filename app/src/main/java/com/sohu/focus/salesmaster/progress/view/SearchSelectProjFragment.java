package com.sohu.focus.salesmaster.progress.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseFragment;
import com.sohu.focus.salesmaster.base.SalesConstants;
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
import com.sohu.focus.salesmaster.project.view.ProjectActivity;
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
 * Created by luckyzhangx on 16/01/2018.
 */

public class SearchSelectProjFragment extends BaseFragment
        implements RecyclerArrayAdapter.OnMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "SearchProjFragment";

    @BindView(R.id.project_list)
    EasyRecyclerView recyclerView;
    @BindView(R.id.project_search)
    EditText search;
    String keyword = "";

    private RecyclerArrayAdapter<ProjectModelSet.DataBean.ListBean> mAdapter;

    private int mPage = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_project, container, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ButterKnife.bind(this, view);
        initView();
        initAdapter();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HttpEngine.getInstance().cancel(TAG);
    }

    private void initView() {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerDecoration(ContextCompat.getColor(getContext(), R.color.standard_line_light), 2));
        recyclerView.setRefreshListener(this);

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    keyword = search.getText().toString();
                    onRefresh();
                    return true;
                }
                return false;
            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                keyword = search.getText().toString();
                onRefresh();
                if (recyclerView != null) {
                    if (s.length() == 0)
                        recyclerView.setEmptyView(R.layout.project_please_search);
                    else
                        recyclerView.setEmptyView(R.layout.empty_search_project);
                }
            }
        });
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
                int projectId = ParseUtil.parseInt((mAdapter.getItem(position)).getEstateId(), -1);
                intent.putExtra(SalesConstants.EXTRA_PROJECT, name);
                intent.putExtra(SalesConstants.EXTRA_PROJECT_ID, projectId);
                intent.putExtra(SalesConstants.EXTRA_PROJECT_CLIENT_NUM, (mAdapter.getItem(position).getCustomerNum()));
                getActivity().setResult(RESULT_OK, intent);
                getActivity().finish();
            }
        });

        recyclerView.setAdapter(mAdapter);
    }

    private void getProjs() {
        mPage = 1;
        HttpEngine.getInstance().cancel(TAG);
        getMoreProjs();
    }

    private void getMoreProjs() {
        GetProjsByUid param = new GetProjsByUid();
        if (CommonUtils.isEmpty(keyword)) {
            mAdapter.clear();
            mAdapter.addAll(new ArrayList<ProjectModelSet.DataBean.ListBean>());
            return;
        }
        param.setParams(mPage, 20, AccountManager.INSTANCE.getUserId(),
                true, keyword);

        GetProjsByUidApi api = new GetProjsByUidApi();
        api.setParam(param);
        api.setTag(TAG);
        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<ProjectModelSet>() {
            @Override
            public void onSuccess(ProjectModelSet result, String method) {
                if (result == null || result.getData() == null)
                    return;
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

    @OnClick(R.id.cancel)
    public void cancel() {
        getFragmentManager().popBackStack();
    }

}
