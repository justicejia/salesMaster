package com.sohu.focus.salesmaster.kernal.network.event;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.sohu.focus.salesmaster.kernal.log.FocusLog;

import java.util.List;

import static com.sohu.focus.salesmaster.kernal.network.NetworkHelper.NET_TAG;

/**
 * Event pushed to Otto Event Bus when Wifi Signal strength was changed
 * and list of WiFi Access Points was refreshed
 * <p>
 * Created by zhaoqiang on 2017/9/26.
 */

public class WifiSignalStrengthChanged {

    private static final String MESSAGE = "WifiSignalStrengthChanged";
    private Context context;

    public WifiSignalStrengthChanged(FocusLog logger, Context context) {
        this.context = context;
        logger.i(NET_TAG, MESSAGE);
    }

    public List<ScanResult> getWifiScanResults() {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifiManager.getScanResults();
    }
}
