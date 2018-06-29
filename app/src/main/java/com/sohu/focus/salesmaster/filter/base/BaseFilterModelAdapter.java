package com.sohu.focus.salesmaster.filter.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;

import java.util.List;

/**
 * Created by luckyzhangx on 2017/9/27.
 */

public abstract class BaseFilterModelAdapter<T> extends BaseAdapter {

    //    注意 sel 与 selModel 的同步
    protected int sel = -1;
    protected T selModel;

    protected List<T> models;


    public int getSel() {
        return sel;
    }

    public void clearSel() {
        sel = -1;
        selModel = null;
        notifyDataSetChanged();
    }

    public void setSel(int sel) {
        this.sel = sel;
        if (CommonUtils.isEmpty(models)) return;
        if (sel < 0 || sel >= models.size()) return;
        selModel = models.get(sel);
        notifyDataSetChanged();
    }

    //    会调用的 setSel 方法。若子类希望设置选择项后有额外处理，建议覆盖 setSel
    public void setSelModel(T model) {
        sel = -1;
        selModel = null;

        if (CommonUtils.notEmpty(models) && model != null) {
            for (int i = 0; i < models.size(); i++) {
                if (model.equals(models.get(i))) {
                    setSel(i);
                    break;
                }
            }
        }
    }

    public T getSelModel() {
        if (sel > 0 && selModel != null) return selModel;
        if (CommonUtils.isEmpty(models)) return null;
        if (sel < 0 || sel >= models.size()) return null;
        return models.get(sel);
    }

    public void setModels(List<T> models) {
        this.models = models;
        if (CommonUtils.isEmpty(models)) {
            clearSel();
        } else if (sel >= 0 && sel < models.size()) {
            if (!models.get(sel).equals(selModel))
                clearSel();
        } else {
            clearSel();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (models != null)
            return models.size();
        return 0;
    }

    @Override
    public T getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = getConvertView(position, convertView, parent);
        deSelModelView(convertView, position);
        if (position == sel || getItem(position).equals(getSelModel())) {
            selModelView(convertView, position);
        }
        return convertView;
    }

    public void clear() {
        setModels(null);
    }

    protected abstract View getConvertView(int position, View convertView, ViewGroup parent);

    protected abstract void selModelView(View item, int position);

    protected abstract void deSelModelView(View item, int position);
}
