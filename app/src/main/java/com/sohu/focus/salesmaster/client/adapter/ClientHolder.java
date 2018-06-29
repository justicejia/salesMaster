package com.sohu.focus.salesmaster.client.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.client.model.ClientModel;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by yuanminjia on 2018/1/3.
 */

public class ClientHolder extends BaseViewHolder<ClientModel> {

    TextView name;
    TextView detail;
    ImageView select;

    boolean selected;

    public ClientHolder(ViewGroup parent) {
        super(parent, R.layout.holder_client);
        name = $(R.id.client_name);
        detail = $(R.id.client_detail);
        select = $(R.id.client_select);

    }

    @Override
    public void setData(ClientModel client) {
        name.setText(client.name);
        detail.setText("职位：" + client.job + " " + "手机号：" + client.tel);

    }

    public void unSelectable() {
        select.setVisibility(View.GONE);
    }

    public void select() {
        selected = true;
        select.setImageResource(R.drawable.client_select);
    }

    public void unSelect() {
        selected = false;
        select.setImageResource(R.drawable.client_unselect);

    }

    private void del() {

    }
}
