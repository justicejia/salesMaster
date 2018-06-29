package com.sohu.focus.salesmaster.goal.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseFragment;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.http.api.GetUserInvestApi;
import com.sohu.focus.salesmaster.invest.adapter.UserInvestHistoryHolder;
import com.sohu.focus.salesmaster.invest.model.UserInvestInfoModel;
import com.sohu.focus.salesmaster.invest.view.UserInvestMonthlyActivity;
import com.sohu.focus.salesmaster.invest.view.UserInvestYearlyActivity;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestListener;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.EasyRecyclerView;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.decoration.DividerDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 用户目标-投放目标页面
 */
public class UserGoalInvestFragment extends BaseFragment {

    private static final String TAG = "UserGoalInvestFragment";

    @BindView(R.id.invest_goal_target)
    TextView target;
    @BindView(R.id.invest_goal_complete)
    TextView complete;
    @BindView(R.id.invest_goal_rate)
    TextView rate;
    @BindView(R.id.invest_goal_accumulate)
    TextView accumulate;
    @BindView(R.id.invest_goal_year)
    TextView year;
    @BindView(R.id.invest_goal_list)
    EasyRecyclerView recyclerView;

    @OnClick(R.id.invest_goal_top)
    void showDetail() {
        UserInvestYearlyActivity.naviToUserInvestYear(getContext(), mUserId, mCurYear, mUserName);
    }

    private String mUserId;
    private String mUserName;
    private String mCurYear;
    private RecyclerArrayAdapter<UserInvestInfoModel.DataBean.HistoryBean> mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserId = getArguments().getString(SalesConstants.EXTRA_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goal_invest, container, false);
        ButterKnife.bind(this, view);
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
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerDecoration(ContextCompat.getColor(getContext(), R.color.standard_line),
                getContext().getResources().getDimensionPixelOffset(R.dimen.margin_half)));
        mAdapter = new RecyclerArrayAdapter<UserInvestInfoModel.DataBean.HistoryBean>(getContext()) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new UserInvestHistoryHolder(parent);
            }
        };
        mAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                UserInvestMonthlyActivity.naviToUserInvestMonth(getContext(), mUserId,
                        mAdapter.getAllData().get(position).getMonth(), mUserName
                );
            }
        });
        recyclerView.setAdapter(mAdapter);

    }

    void getData() {
        GetUserInvestApi api = new GetUserInvestApi(mUserId);
        api.setCache(true);
        api.setTag(TAG);
        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<UserInvestInfoModel>() {
            @Override
            public void onSuccess(UserInvestInfoModel result, String method) {
                if (result != null && result.getData() != null) {
                    loadData(result.getData());
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onFailed(UserInvestInfoModel result, String method) {
                if (result != null) {
                    ToastUtil.toast(result.getMsg());
                }
            }
        });
    }

    void loadData(UserInvestInfoModel.DataBean dataBean) {
        mUserName = dataBean.getPersonName();
        mCurYear = dataBean.getYear();
        year.setText(mCurYear + "年投放目标");
        setText(target, dataBean.getTargetMoney(), "万");
        setText(complete, dataBean.getAdMoney(), "万");
        setText(rate, dataBean.getFinishRate(), "%");
        if (CommonUtils.notEmpty(dataBean.getTotalAdMoney())) {
            accumulate.setText("累计投放：" + dataBean.getTotalAdMoney());
        } else {
            accumulate.setText("累计投放：0万");
        }
        mAdapter.clear();
        mAdapter.addAll(dataBean.getHistory());
    }

    private void setText(TextView textView, String content, String unit) {
        if (content.contains(unit)) {
            content = getShowString(content, unit);
            String spaceText = content.substring(0, content.indexOf(unit)) + " " + unit;
            SpannableString spannableString = new SpannableString(spaceText);
            spannableString.setSpan(new RelativeSizeSpan(0.6f), spaceText.indexOf(" "),
                    spaceText.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            textView.setText(spannableString);
        } else {
            textView.setText(content);
        }
    }

    private String getShowString(String money, String unit) {
        String result;
        if (money.length() < 6) {
            return money;
        } else {
            money = money.substring(0, money.indexOf(unit));
            if (money.contains(".")) {
                result = money.substring(0, money.indexOf("."));
                int resultInt = Integer.parseInt(result);
                int dotFirstInt = Integer.parseInt(money.substring(money.indexOf(".") + 1, money.indexOf(".") + 2));
                if (dotFirstInt >= 5) {
                    resultInt++;
                }
                result = resultInt + "";
            } else {
                result = money;
            }
            if (result.length() > 4) {
                result = result.substring(0, 3) + "..";
            }
            return result + unit;
        }
    }


}
