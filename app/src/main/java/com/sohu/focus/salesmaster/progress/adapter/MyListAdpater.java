package com.sohu.focus.salesmaster.progress.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sohu.focus.salesmaster.newFilter.model.FilterModel;
import com.sohu.focus.salesmaster.R;

import java.util.List;

/**
 * Created by yuanminjia on 2017/11/3.
 */

public class MyListAdpater extends BaseAdapter {

    private Context mContext;
    private List<FilterModel.DataBean.StateBean> data;

    public MyListAdpater(Context context, List<FilterModel.DataBean.StateBean> list) {
        mContext = context;
        data = list;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.single_text, parent, false);
            holder.name = convertView.findViewById(R.id.progress_item_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(data.get(position).getLabel());
        return convertView;
    }

    private static class ViewHolder {
        TextView name;
    }
}
