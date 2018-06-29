package com.sohu.focus.salesmaster.kernal.network.receiver;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import com.sohu.focus.salesmaster.kernal.log.FocusLog;
import com.sohu.focus.salesmaster.kernal.network.BusWrapper;
import com.sohu.focus.salesmaster.kernal.network.event.WifiSignalStrengthChanged;

/**
 * Created by zhaoqiang on 2017/9/26.
 */

public final class WifiSignalStrengthChangeReceiver extends BaseBroadcastReceiver {
    private Context context;

    public WifiSignalStrengthChangeReceiver(BusWrapper busWrapper, FocusLog logger, Context context) {
        super(busWrapper, logger, context);
        this.context = context;
    }

    @Override public void onReceive(Context context, Intent intent) {
        // We need to start WiFi scan after receiving an Intent
        // in order to get update with fresh data as soon as possible
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();
        onPostReceive();
    }

    public void onPostReceive() {
        postFromAnyThread(new WifiSignalStrengthChanged(logger, context));
    }
}
