package com.sohu.focus.salesmaster.kernal.network.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.sohu.focus.salesmaster.kernal.log.FocusLog;
import com.sohu.focus.salesmaster.kernal.network.BusWrapper;
import com.sohu.focus.salesmaster.kernal.network.ConnectionStatus;
import com.sohu.focus.salesmaster.kernal.network.NetworkState;
import com.sohu.focus.salesmaster.kernal.network.event.ConnectionChanged;

/**
 * Created by zhaoqiang on 2017/9/26.
 */

public abstract class BaseBroadcastReceiver extends BroadcastReceiver {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final BusWrapper busWrapper;
    private final Context context;
    protected final FocusLog logger;

    public BaseBroadcastReceiver(BusWrapper busWrapper, FocusLog logger, Context context) {
        this.busWrapper = busWrapper;
        this.logger = logger;
        this.context = context;
    }

    @Override public abstract void onReceive(Context context, Intent intent);

    protected boolean statusNotChanged(ConnectionStatus connectivityStatus) {
        return NetworkState.status == connectivityStatus;
    }

    protected void postConnectivityChanged(ConnectionStatus connectivityStatus) {
        NetworkState.status = connectivityStatus;
        postFromAnyThread(new ConnectionChanged(connectivityStatus, logger, context));
    }

    protected void postFromAnyThread(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            busWrapper.post(event);
        } else {
            handler.post(new Runnable() {
                @Override public void run() {
                    busWrapper.post(event);
                }
            });
        }
    }
}