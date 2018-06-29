package com.sohu.focus.salesmaster.project.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseFragment;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.http.api.GetProjectDataApi;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestListener;
import com.sohu.focus.salesmaster.project.adapter.ChartTopHolder;
import com.sohu.focus.salesmaster.project.model.ProjectDataModel;
import com.sohu.focus.salesmaster.uiframe.LineChart;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.EasyRecyclerView;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 楼盘详情-数据
 * Created by yuanminjia on 2018/1/2.
 */

public class ProjectDataFragment extends BaseFragment {

    public static final String TAG = "ProjectDataFragment";
    @BindView(R.id.project_chart)
    LineChart chart;
    @BindView(R.id.project_pv)
    TextView pv;
    @BindView(R.id.project_uv)
    TextView uv;
    @BindView(R.id.project_400)
    TextView calls;
    @BindView(R.id.project_400_rate)
    TextView callRate;
    @BindView(R.id.project_brokers)
    TextView brokers;
    @BindView(R.id.project_new_lives)
    TextView liveCount;
    @BindView(R.id.project_chart_top)
    EasyRecyclerView recyclerView;

    private String mEstateId;
    private ProjectDataModel.DataBean mData;
    private RecyclerArrayAdapter<ItemModel> mAdapter;

    private List<ItemModel> mListData = new ArrayList<>();

    public static final int DATA_TYPE_PV = 101;
    public static final int DATA_TYPE_UV = 102;
    public static final int DATA_TYPE_400 = 103;
    public static final int DATA_TYPE_400_RATE = 104;
    public static final int DATA_TYPE_BROKER = 105;
    public static final int DATA_TYPE_LIVE_COUNT = 106;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_data, container, false);
        ButterKnife.bind(this, view);
        mEstateId = getArguments().getString(SalesConstants.EXTRA_PROJECT_ID);
        initView();
        getData();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HttpEngine.getInstance().cancel(TAG);
    }

    void initView() {
        final RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(manager);
        mAdapter = new RecyclerArrayAdapter<ItemModel>(getContext()) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new ChartTopHolder(parent);
            }
        };
        ItemModel model1 = new ItemModel();
        model1.setContent("PV");
        model1.setSelected(true);

        ItemModel model2 = new ItemModel();
        model2.setContent("UV");

        ItemModel model3 = new ItemModel();
        model3.setContent("400");

        ItemModel model4 = new ItemModel();
        model4.setContent("400接通率");

        ItemModel model5 = new ItemModel();
        model5.setContent("活跃经纪人");

        ItemModel model6 = new ItemModel();
        model6.setContent("新开直播间");

        mListData.clear();
        mListData.add(model1);
        mListData.add(model2);
        mListData.add(model3);
        mListData.add(model4);
        mListData.add(model5);
        mListData.add(model6);

        mAdapter.addAll(mListData);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                for (ItemModel i : mListData) {
                    i.setSelected(false);
                }
                mListData.get(position).setSelected(true);
                mAdapter.notifyDataSetChanged();
                switch (position) {
                    case 0:
                        switchToType(DATA_TYPE_PV);
                        break;
                    case 1:
                        switchToType(DATA_TYPE_UV);
                        break;
                    case 2:
                        switchToType(DATA_TYPE_400);
                        break;
                    case 3:
                        switchToType(DATA_TYPE_400_RATE);
                        break;
                    case 4:
                        switchToType(DATA_TYPE_BROKER);
                        break;
                    case 5:
                        switchToType(DATA_TYPE_LIVE_COUNT);
                        break;

                }
            }
        });

    }


    void getData() {
        GetProjectDataApi api = new GetProjectDataApi(mEstateId);
        api.setTag(TAG);
        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<ProjectDataModel>() {
            @Override
            public void onSuccess(ProjectDataModel result, String method) {
                if (result != null && result.data != null) {
                    mData = result.data;
                    loadData();
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onFailed(ProjectDataModel result, String method) {

            }
        });
    }

    void loadData() {
        if (mData != null && mData.yesterdayDataMap != null) {
            pv.setText(mData.yesterdayDataMap.pv);
            uv.setText(mData.yesterdayDataMap.uv);
            calls.setText(mData.yesterdayDataMap.isCalledDistinct);
            String callRateString = mData.yesterdayDataMap.isAnsweredRate;
            float t;
            try {
                t = Float.parseFloat(callRateString);
            } catch (Exception e) {
                t = 0.0f;
            }
            int n = (int)(100 * t);
            callRate.setText(n + "%");
            brokers.setText(mData.yesterdayDataMap.countLiveBroker);
            liveCount.setText(mData.yesterdayDataMap.countLive);
        }
        switchToType(DATA_TYPE_PV);
    }

    void switchToType(int type) {
        switch (type) {
            case DATA_TYPE_PV:
                if (mData != null && mData.pv != null) {
                    chart.setLongData(mData.pv.xLabel, mData.pv.yLabel, mData.pv.y);
                }
                break;
            case DATA_TYPE_UV:
                if (mData != null && mData.uv != null) {
                    chart.setLongData(mData.uv.xLabel, mData.uv.yLabel, mData.uv.y);
                }
                break;
            case DATA_TYPE_400:
                if (mData != null && mData.isCalledDistinct != null) {
                    chart.setLongData(mData.isCalledDistinct.xLabel, mData.isCalledDistinct.yLabel, mData.isCalledDistinct.y);
                }
                break;
            case DATA_TYPE_400_RATE:
                if (mData != null && mData.isAnsweredRate != null) {
                    chart.setFloatData(mData.isAnsweredRate.xLabel, mData.isAnsweredRate.yLabel, mData.isAnsweredRate.y);
                }
                break;
            case DATA_TYPE_BROKER:
                if (mData != null && mData.countLiveBroker != null) {
                    chart.setLongData(mData.countLiveBroker.xLabel, mData.countLiveBroker.yLabel, mData.countLiveBroker.y);
                }
                break;
            case DATA_TYPE_LIVE_COUNT:
                if (mData != null && mData.countLive != null) {
                    chart.setLongData(mData.countLive.xLabel, mData.countLive.yLabel, mData.countLive.y);
                }
                break;
        }

    }

    public static class ItemModel {
        private String content;
        private boolean isSelected;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }

}
