package com.sohu.focus.salesmaster.kernal.network;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.sohu.focus.salesmaster.kernal.log.FocusLog;
import com.sohu.focus.salesmaster.kernal.log.Logger;
import com.sohu.focus.salesmaster.kernal.network.online.OnlineChecker;
import com.sohu.focus.salesmaster.kernal.network.online.OnlineCheckerImpl;
import com.sohu.focus.salesmaster.kernal.network.receiver.InternetConnectionChangeReceiver;
import com.sohu.focus.salesmaster.kernal.network.receiver.NetworkConnectionChangeReceiver;
import com.sohu.focus.salesmaster.kernal.network.receiver.WifiSignalStrengthChangeReceiver;

/**
 * Created by zhaoqiang on 2017/9/26.
 */

public class NetworkHelper {

    public static final String NET_TAG = "net_status";

    private boolean wifiAccessPointsScanEnabled = false;
    private final Context context;
    private final NetworkConnectionChangeReceiver networkConnectionChangeReceiver;
    private final InternetConnectionChangeReceiver internetConnectionChangeReceiver;
    private final WifiSignalStrengthChangeReceiver wifiSignalStrengthChangeReceiver;
    private final OnlineChecker onlineChecker;

    /**
     * Initializes NetworkEvents object
     * with NetworkEventsLogger as default logger.
     *
     * @param context Android context
     * @param busWrapper Wrapper for event bus
     */
    public NetworkHelper(Context context, BusWrapper busWrapper) {
        this(context, busWrapper, Logger.ZQ());
    }

    /**
     * Initializes NetworkEvents object.
     *
     * @param context Android context
     * @param busWrapper Wrapper fo event bus
     * @param logger message logger (NetworkEventsLogger logs messages to LogCat)
     */
    public NetworkHelper(Context context, BusWrapper busWrapper, FocusLog logger) {
        checkNotNull(context, "context == null");
        checkNotNull(busWrapper, "busWrapper == null");
        checkNotNull(logger, "logger == null");
        this.context = context;
        this.onlineChecker = new OnlineCheckerImpl(context);
        this.networkConnectionChangeReceiver =
                new NetworkConnectionChangeReceiver(busWrapper, logger, context, onlineChecker);
        this.internetConnectionChangeReceiver =
                new InternetConnectionChangeReceiver(busWrapper, logger, context);
        this.wifiSignalStrengthChangeReceiver =
                new WifiSignalStrengthChangeReceiver(busWrapper, logger, context);
    }

    /**
     * Enables wifi access points scan.
     * When it's not called, WifiSignalStrengthChanged event will never occur.
     *
     * @return NetworkEvents object
     */
    public NetworkHelper enableWifiScan() {
        this.wifiAccessPointsScanEnabled = true;
        return this;
    }

    /**
     * Enables internet connection check.
     * when it's not called, WIFI_CONNECTED_HAS_INTERNET
     * and WIFI_CONNECTED_HAS_NO_INTERNET ConnectivityStatus will never be set
     * Please, be careful! Internet connection check may contain bugs
     * that's why it's disabled by default.
     *
     * @return NetworkEvents object
     */
    public NetworkHelper enableInternetCheck() {
        networkConnectionChangeReceiver.enableInternetCheck();
        return this;
    }

    /**
     * Sets ping parameters of the host used to check Internet connection.
     * If it's not set, library will use default ping parameters.
     *
     * @param host host to be pinged
     * @param port port of the host
     * @param timeoutInMs timeout in milliseconds
     * @return NetworkEvents object
     */
    public NetworkHelper setPingParameters(String host, int port, int timeoutInMs) {
        onlineChecker.setPingParameters(host, port, timeoutInMs);
        return this;
    }

    /**
     * Registers NetworkEvents.
     * It should be executed in onCreate() method in activity
     * or during creating instance of class extending Application.
     */
    public void register() {
        registerNetworkConnectionChangeReceiver();
        registerInternetConnectionChangeReceiver();

        if (wifiAccessPointsScanEnabled) {
            registerWifiSignalStrengthChangeReceiver();
            // start WiFi scan in order to refresh access point list
            // if this won't be called WifiSignalStrengthChanged may never occur
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            wifiManager.startScan();
        }
    }

    /**
     * Unregisters NetworkEvents.
     * It should be executed in onDestroy() method in activity
     * or during destroying instance of class extending Application.
     */
    public void unregister() {
        try {
            context.unregisterReceiver(networkConnectionChangeReceiver);
            context.unregisterReceiver(internetConnectionChangeReceiver);
            if (wifiAccessPointsScanEnabled) {
                context.unregisterReceiver(wifiSignalStrengthChangeReceiver);
            }
        } catch (Exception ignored) {

        }
    }

    private void registerNetworkConnectionChangeReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        context.registerReceiver(networkConnectionChangeReceiver, filter);
    }

    private void registerInternetConnectionChangeReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(InternetConnectionChangeReceiver.INTENT);
        context.registerReceiver(internetConnectionChangeReceiver, filter);
    }

    private void registerWifiSignalStrengthChangeReceiver() {
        IntentFilter filter = new IntentFilter(WifiManager.RSSI_CHANGED_ACTION);
        context.registerReceiver(wifiSignalStrengthChangeReceiver, filter);
    }

    private void checkNotNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Helper method, which checks if device is connected to WiFi or mobile network.
     *
     * @param context Activity or application context
     * @return boolean true if is connected to mobile or WiFi network.
     */
    public static boolean isConnectedToWiFiOrMobileNetwork(Context context) {
        final String service = Context.CONNECTIVITY_SERVICE;
        final ConnectivityManager manager = (ConnectivityManager) context.getSystemService(service);
        final NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo == null) {
            return false;
        }

        final boolean isWifiNetwork = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
        final boolean isMobileNetwork = networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;

        if (isWifiNetwork || isMobileNetwork) {
            return true;
        }

        return false;
    }

    public static ConnectionStatus getConnectionStatus(Context context) {
        ConnectionStatus status = ConnectionStatus.OFFLINE;
        final String service = Context.CONNECTIVITY_SERVICE;
        final ConnectivityManager manager = (ConnectivityManager) context.getSystemService(service);
        final NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo == null) {
            return status;
        }

        final boolean isWifiNetwork = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
        final boolean isMobileNetwork = networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;

        if (isWifiNetwork) {
            status = ConnectionStatus.WIFI_CONNECTED;
        }

        if(isMobileNetwork) {
            status = ConnectionStatus.MOBILE_CONNECTED;
        }

        return status;
    }
}
