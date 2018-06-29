package com.sohu.focus.salesmaster.kernal.network;

import com.sohu.focus.salesmaster.kernal.bus.RxBus;
import com.sohu.focus.salesmaster.kernal.network.event.ConnectionChanged;

import rx.Subscription;

/**
 * Created by zhaoqiang on 2017/9/26.
 */

public class NetBus implements BusWrapper {

    Subscription subscription;

    OnNetChangedListener listener;

    boolean isPause = false;

    ConnectionStatus cache;

    public NetBus(OnNetChangedListener listener) {
        this.listener = listener;
    }

    @Override
    public void register() {
        subscription = RxBus.get().subscribe(ConnectionChanged.class, new RxBus.OnHandleEvent<ConnectionChanged>() {
            @Override
            public void onEvent(ConnectionChanged connectionChanged) {
                if (listener != null) {
                    if (isPause) {
                        cache = connectionChanged.getConnectivityStatus();
                    } else {
                        listener.onNetChanged(connectionChanged.getConnectivityStatus());
                    }
                }
            }
        });
    }

    public void resume() {
        isPause = false;
        if (cache != null && listener != null) {
            listener.onNetChanged(cache);
            cache = null;
        }
    }

    public void pause() {
        isPause = true;
    }

    @Override
    public void unregister() {
        if (subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
        listener = null;
        cache = null;
    }

    @Override
    public void post(Object event) {
        RxBus.get().post(event);
    }

    public interface OnNetChangedListener {
        void onNetChanged(ConnectionStatus status);
    }
}
