package com.sohu.focus.salesmaster.kernal.network.event;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.sohu.focus.salesmaster.kernal.log.FocusLog;
import com.sohu.focus.salesmaster.kernal.network.ConnectionStatus;
import com.sohu.focus.salesmaster.kernal.network.MobileNetworkType;

import static com.sohu.focus.salesmaster.kernal.network.NetworkHelper.NET_TAG;

/**
 * Event pushed to Otto Event Bus when ConnectivityStatus changes;
 * E.g. when WiFi is turned on or off or when mobile network is turned on or off
 * it also occurs when WiFi is on, but there is no Internet connection or when there is Internet
 * connection
 * or when device goes off-line
 * <p>
 * Created by zhaoqiang on 2017/9/26.
 */

public final class ConnectionChanged {
    private static final String MESSAGE_FORMAT = "ConnectivityChanged: %s";
    private final ConnectionStatus connectivityStatus;
    private final Context context;

    public ConnectionChanged(ConnectionStatus connectivityStatus, FocusLog logger,
                             Context context) {
        this.connectivityStatus = connectivityStatus;
        this.context = context;
        String message = String.format(MESSAGE_FORMAT, connectivityStatus.toString());
        logger.i(NET_TAG, message);
    }

    public ConnectionStatus getConnectivityStatus() {
        return connectivityStatus;
    }

    public MobileNetworkType getMobileNetworkType() {

        if (connectivityStatus != ConnectionStatus.MOBILE_CONNECTED) {
            return MobileNetworkType.UNKNOWN;
        }

        TelephonyManager telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        switch (telephonyManager.getNetworkType()) {
            case (TelephonyManager.NETWORK_TYPE_LTE):
                return MobileNetworkType.LTE;
            case (TelephonyManager.NETWORK_TYPE_HSPAP):
                return MobileNetworkType.HSPAP;
            case (TelephonyManager.NETWORK_TYPE_EDGE):
                return MobileNetworkType.EDGE;
            case (TelephonyManager.NETWORK_TYPE_GPRS):
                return MobileNetworkType.GPRS;
            default:
                return MobileNetworkType.UNKNOWN;
        }
    }
}
