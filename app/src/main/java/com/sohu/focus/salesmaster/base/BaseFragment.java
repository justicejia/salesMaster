package com.sohu.focus.salesmaster.base;

/**
 * Created by yuanminjia on 2017/10/27.
 */


import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * focus base fragment
 * Created by qiangzhao on 2017/1/17.
 */

public class BaseFragment extends Fragment {

    protected List<Subscription> subscriptionList = new ArrayList();

    @Override

    public void onResume() {
        super.onResume();
    }

    public void showProgress() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showProgress();
        }
    }

    public void dismissProgress() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).dismissProgress();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (Subscription subscription : subscriptionList) {
            if (subscription != null && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
        }
    }
}

