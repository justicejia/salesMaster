package com.sohu.focus.salesmaster.kernal.network.receiver;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.sohu.focus.salesmaster.kernal.log.FocusLog;
import com.sohu.focus.salesmaster.kernal.network.BusWrapper;
import com.sohu.focus.salesmaster.kernal.network.ConnectionStatus;

/**
 * Created by zhaoqiang on 2017/9/26.
 */

public final class InternetConnectionChangeReceiver extends BaseBroadcastReceiver {
    public final static String INTENT =
            "networkevents.intent.action.INTERNET_CONNECTION_STATE_CHANGED";
    public final static String INTENT_EXTRA = "networkevents.intent.extra.CONNECTED_TO_INTERNET";

    public InternetConnectionChangeReceiver(BusWrapper busWrapper, FocusLog logger, Context context) {
        super(busWrapper, logger, context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(INTENT)) {
            boolean connectedToInternet = intent.getBooleanExtra(INTENT_EXTRA, false);
            onPostReceive(connectedToInternet, context);
        }
    }

    public void onPostReceive(boolean connectedToInternet, Context context) {
        ConnectionStatus connectivityStatus =
                (connectedToInternet) ? ConnectionStatus.WIFI_CONNECTED_HAS_INTERNET
                        : ConnectionStatus.WIFI_CONNECTED_HAS_NO_INTERNET;

        if (statusNotChanged(connectivityStatus)) {
            return;
        }

        // we are checking if device is connected to WiFi again,
        // because connectivityStatus may change in a short period of time
        // after receiving it

        if (context != null && !isConnectedToWifi(context)) {
            return;
        }

        postConnectivityChanged(connectivityStatus);
    }

    private boolean isConnectedToWifi(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            return networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
        }

        return false;
    }
}