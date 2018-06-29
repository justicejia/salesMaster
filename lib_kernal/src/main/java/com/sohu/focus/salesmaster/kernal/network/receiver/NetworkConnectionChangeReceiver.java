package com.sohu.focus.salesmaster.kernal.network.receiver;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.sohu.focus.salesmaster.kernal.log.FocusLog;
import com.sohu.focus.salesmaster.kernal.network.BusWrapper;
import com.sohu.focus.salesmaster.kernal.network.ConnectionStatus;
import com.sohu.focus.salesmaster.kernal.network.online.OnlineChecker;

/**
 * Created by zhaoqiang on 2017/9/26.
 */

public final class NetworkConnectionChangeReceiver extends BaseBroadcastReceiver {
    private final OnlineChecker onlineChecker;
    private boolean internetCheckEnabled = false;

    public NetworkConnectionChangeReceiver(BusWrapper busWrapper, FocusLog logger, Context context,
                                           OnlineChecker onlineChecker) {
        super(busWrapper, logger, context);
        this.onlineChecker = onlineChecker;
    }

    public void enableInternetCheck() {
        this.internetCheckEnabled = true;
    }

    @Override public void onReceive(final Context context, Intent intent) {
        onPostReceive(getConnectivityStatus(context));
    }

    public void onPostReceive(final ConnectionStatus connectivityStatus) {
        if (statusNotChanged(connectivityStatus)) {
            return;
        }

        boolean isConnectedToWifi = connectivityStatus == ConnectionStatus.WIFI_CONNECTED;

        if (internetCheckEnabled && isConnectedToWifi) {
            onlineChecker.check();
        } else {
            postConnectivityChanged(connectivityStatus);
        }
    }

    private ConnectionStatus getConnectivityStatus(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return ConnectionStatus.WIFI_CONNECTED;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return ConnectionStatus.MOBILE_CONNECTED;
            }
        }

        return ConnectionStatus.OFFLINE;
    }
}
