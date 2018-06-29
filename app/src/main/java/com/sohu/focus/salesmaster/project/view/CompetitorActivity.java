package com.sohu.focus.salesmaster.project.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseActivity;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.http.api.UpdateCompetitorApi;
import com.sohu.focus.salesmaster.http.model.UpdateCompetitorModel;
import com.sohu.focus.salesmaster.kernal.http.BaseModel;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestListener;
import com.sohu.focus.salesmaster.kernal.utils.ScreenUtil;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;
import com.sohu.focus.salesmaster.project.adapter.CompetitorHolder;
import com.sohu.focus.salesmaster.project.model.CompetitorItemModel;
import com.sohu.focus.salesmaster.uiframe.CommonDialog;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.EasyRecyclerView;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.decoration.DividerDecoration;
import com.sohu.focus.salesmaster.uiframe.statusbar.StatusBarHelper;
import com.sohu.focus.salesmaster.uiframe.statusbar.SystemStatusManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sohu.focus.salesmaster.uiframe.statusbar.SystemStatusManager.STATUS_STRATEGY_ABOVE_KITKAT;

/**
 * 竞品投放页
 * Created by yuanminjia on 2018/1/12.
 */

public class CompetitorActivity extends BaseActivity {

    public static final String TAG = CompetitorActivity.class.getSimpleName();
    @BindView(R.id.competitor_list)
    EasyRecyclerView recyclerView;
    private RecyclerArrayAdapter<CompetitorItemModel> mAdapter;
    private UpdateCompetitorModel mData;
    List<CompetitorItemModel> mListData = new ArrayList<>();


    @OnClick(R.id.competitor_back)
    void back() {
        finish();
    }

    @OnClick(R.id.competitor_add)
    void add() {
        Intent intent = new Intent();
        intent.setClass(this, AddCompetitorActivity.class);
        intent.putExtra(SalesConstants.EXTRA_COMPETITORS, mData);
        startActivityForResult(intent, SalesConstants.REQUEST_ADD_COMPETITOR);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competitor);
        ButterKnife.bind(this);

        if (StatusBarHelper.statusBarLightMode(this) != StatusBarHelper.OTHER) {
            if (new SystemStatusManager(this).setTranslucentStatus(R.color.white) == STATUS_STRATEGY_ABOVE_KITKAT) {
                int statusHeight = ScreenUtil.getStatusBarHeight(this);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) recyclerView.getLayoutParams();
                layoutParams.topMargin = statusHeight;
                recyclerView.setLayoutParams(layoutParams);
            }
        }

        mData = (UpdateCompetitorModel) getIntent().getSerializableExtra(SalesConstants.EXTRA_COMPETITORS);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpEngine.getInstance().cancel(TAG);
    }

    void initView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerDecoration(ContextCompat.getColor(this, R.color.standard_line_light), 1));
        mAdapter = new RecyclerArrayAdapter<CompetitorItemModel>(this) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new CompetitorHolder(parent);
            }
        };
        recyclerView.setAdapterWithProgress(mAdapter);
        if (mData.getCompetitorMoney() != null) {
            for (Map<String, String> item : mData.getCompetitorMoney()) {
                CompetitorItemModel model = new CompetitorItemModel();
                String k = "";
                for (String entry : item.keySet()) {
                    k = entry;
                }
                model.setCompany(k);
                if (item.containsKey(k)) {
                    model.setPrice(item.get(k));
                }
                mListData.add(model);
            }
        }
        mAdapter.addAll(mListData);
        mAdapter.setOnItemLongClickListener(new RecyclerArrayAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final int position) {
                final UpdateCompetitorModel save = new UpdateCompetitorModel();
                save.setEstateId(mData.getEstateId());
                List<Map<String, String>> temp = new ArrayList<>();
                temp.addAll(mData.getCompetitorMoney());
                save.setCompetitorMoney(temp);
                CommonDialog dialog = new CommonDialog.CommonDialogBuilder(CompetitorActivity.this)
                        .content("确定删除该条数据?")
                        .leftBtnText("取消")
                        .rightBtnText("确定")
                        .rightBtnListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mData.getCompetitorMoney().remove(position);
                                UpdateCompetitorApi api = new UpdateCompetitorApi(mData);
                                api.setTag(TAG);
                                HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<BaseModel>() {
                                    @Override
                                    public void onSuccess(BaseModel result, String method) {
                                        if (result != null) {
                                            mAdapter.remove(position);
                                            ToastUtil.toast("删除成功");
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        mData = save;
                                    }

                                    @Override
                                    public void onFailed(BaseModel result, String method) {
                                        ToastUtil.toast("删除失败");
                                        mData = save;

                                    }
                                });
                            }
                        }).create();
                dialog.show(getSupportFragmentManager(), TAG);
                return true;
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mData = (UpdateCompetitorModel) data.getSerializableExtra(SalesConstants.EXTRA_COMPETITORS);
            List<CompetitorItemModel> list = new ArrayList<>();
            for (Map<String, String> item : mData.getCompetitorMoney()) {
                CompetitorItemModel model = new CompetitorItemModel();
                String k = "";
                for (String entry : item.keySet()) {
                    k = entry;
                }
                model.setCompany(k);
                if (item.containsKey(k)) {
                    model.setPrice(item.get(k));
                }
                list.add(model);
            }
            mAdapter.clear();
            mAdapter.addAll(list);
        }
    }
}
