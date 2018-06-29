package com.sohu.focus.salesmaster.goal.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseFragment;
import com.sohu.focus.salesmaster.base.SalesConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserGoalFragment extends BaseFragment {

    @BindView(R.id.tv_400_goal)
    TextView goal400;
    @BindView(R.id.tv_invest_goal)
    TextView goalInvest;
    @BindView(R.id.user_goal_fragment_container)
    FrameLayout container;

    @OnClick(R.id.tv_400_goal)
    void show400() {
        goal400.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        goal400.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_blue_bg));
        goalInvest.setTextColor(ContextCompat.getColor(getContext(), R.color.standard_text_gray));
        goalInvest.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_grey_bg));
        mFragmentManger.beginTransaction().show(fragments.get(1)).hide(fragments.get(0)).commit();
    }

    @OnClick(R.id.tv_invest_goal)
    void showInvest() {
        goalInvest.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        goalInvest.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_blue_bg));
        goal400.setTextColor(ContextCompat.getColor(getContext(), R.color.standard_text_gray));
        goal400.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_grey_bg));

        mFragmentManger.beginTransaction().show(fragments.get(0)).hide(fragments.get(1)).commit();
    }

    private List<BaseFragment> fragments = new ArrayList<>();
    private String mUserId;
    private FragmentManager mFragmentManger;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_goal, container, false);
        ButterKnife.bind(this, view);
        mUserId = getArguments().getString(SalesConstants.EXTRA_ID);
        initView();
        return view;
    }

    void initView() {
        mFragmentManger = getChildFragmentManager();

        BaseFragment goalFragment = new UserGoalInvestFragment();
        BaseFragment kpiFragment = new UserGoal400Fragment();

        Bundle bundle = new Bundle();
        bundle.putString(SalesConstants.EXTRA_ID, mUserId);
        goalFragment.setArguments(bundle);
        kpiFragment.setArguments(bundle);
        fragments.add(goalFragment);
        fragments.add(kpiFragment);

        mFragmentManger.beginTransaction().add(R.id.user_goal_fragment_container, fragments.get(0))
                .add(R.id.user_goal_fragment_container, fragments.get(1)).
                hide(fragments.get(1)).show(fragments.get(0)).commit();

    }

}
