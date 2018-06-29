package com.sohu.focus.salesmaster.sheets.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.sheets.model.SubscriptionModel;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 * Created by jiayuanmin on 2018/5/29
 * description:
 */
public class SubscriptionAdapter extends RecyclerArrayAdapter<SubscriptionModel.DataBean> {

    private static final int ITEM_DIVIDER = 1;
    private static final int ITEM_NORMAL = 2;

    //状态，0已订阅 1申请中 2 可订阅
    public static final int STATUS_SUBSCRIBED = 0;
    public static final int STATUS_APPLICATION = 1;
    public static final int STATUS_SUBSCRIABLE = 2;
    public static final int STATUS_DIVIDER = -1;

    public SubscriptionAdapter(Context context) {
        super(context);
    }

    @Override
    public int getViewType(int position) {
        if (getItem(position).getStatus() == STATUS_DIVIDER) {
            return ITEM_DIVIDER;
        } else {
            return ITEM_NORMAL;
        }
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_DIVIDER) {
            return new DividerHolder(parent);
        } else {
            return new SubscribeHolder(parent);
        }
    }


    static class DividerHolder extends BaseViewHolder<SubscriptionModel.DataBean> {

        private TextView title;

        DividerHolder(ViewGroup parent) {
            super(parent, R.layout.holder_subscribe_divider);
            title = $(R.id.title);
        }

        @Override
        public void setData(SubscriptionModel.DataBean data) {
            title.setText(data.getTitle());
        }
    }

    static class SubscribeHolder extends BaseViewHolder<SubscriptionModel.DataBean> {

        private TextView name;
        private ImageView icon;

        SubscribeHolder(ViewGroup parent) {
            super(parent, R.layout.holder_subscribe);
            name = $(R.id.subscribe_item_name);
            icon = $(R.id.subscribe_item_icon);
        }

        @Override
        public void setData(SubscriptionModel.DataBean data) {
            name.setText(data.getTitle());
            icon.setVisibility(View.GONE);
//            if (data.getStatus() == STATUS_SUBSCRIBED) {
//                icon.setVisibility(View.VISIBLE);
//                icon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ico_unsubscribe));
//            } else if (data.getStatus() == STATUS_SUBSCRIABLE) {
//                icon.setVisibility(View.VISIBLE);
//                icon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ico_add_subscribe));
//            } else {
//                icon.setVisibility(View.GONE);
//            }
        }
    }
}
