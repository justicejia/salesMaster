package com.sohu.focus.salesmaster.base;

import java.lang.ref.WeakReference;

/**
 * Created by jiayuanmin on 2018/5/18
 * description:
 */

public abstract class BasePresenter<V> {

    protected WeakReference<V> view;

    public void attach(V v) {
        view = new WeakReference<>(v);
    }

    public void detach() {
        if (view != null)
            view.clear();
    }

    public boolean isAttached() {
        return view != null && view.get() != null;
    }

    public V getView() {
        return view != null ? view.get() : null;
    }

    public abstract void release();
}
