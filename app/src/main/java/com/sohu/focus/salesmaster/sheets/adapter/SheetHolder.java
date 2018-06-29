package com.sohu.focus.salesmaster.sheets.adapter;

import android.view.ViewGroup;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.sheets.model.SheetModel;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by jiayuanmin on 2018/5/22
 * description:
 */
public class SheetHolder extends BaseViewHolder<SheetModel.DataBean> {

    private TextView title, titleMid, titleBottom;
    private TextView subtitle, subtitleBottom;

    public SheetHolder(ViewGroup parent) {
        super(parent, R.layout.holder_sheet);
        title = $(R.id.sheet_item_title);
        titleMid = $(R.id.sheet_item_mid_title);
        titleBottom = $(R.id.sheet_item_bottom_title);
        subtitle = $(R.id.sheet_item_subtitle);
        subtitleBottom = $(R.id.sheet_item_bottom_subtitle);
    }

    @Override
    public void setData(SheetModel.DataBean data) {
        title.setText(data.getTitle());
        if (CommonUtils.notEmpty(data.getDataList())) {
            titleMid.setText(data.getDataList().get(0).get(0) + "：" + data.getDataList().get(0).get(1));
            titleBottom.setText(data.getDataList().get(1).get(0) + "：" + data.getDataList().get(1).get(1));
            subtitle.setText(data.getDataList().get(2).get(0));
            subtitleBottom.setText(data.getDataList().get(2).get(1));
        }
    }
}
