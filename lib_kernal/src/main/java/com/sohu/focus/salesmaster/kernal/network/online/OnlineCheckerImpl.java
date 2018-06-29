package com.sohu.focus.salesmaster.kernal.network.online;

import android.content.Context;
import android.content.Intent;

import com.sohu.focus.salesmaster.kernal.network.receiver.InternetConnectionChangeReceiver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 检测是否能连接到焦点主站
 * <p>
 * Created by zhaoqiang on 2017/9/26.
 */

public final class OnlineCheckerImpl implements OnlineChecker {
    private static final String DEFAULT_PING_HOST = "house.focus.cn";
    private static final int DEFAULT_PING_PORT = 80;
    private static final int DEFAULT_PING_TIMEOUT_IN_MS = 2000;

    private final Context context;
    private String pingHost;
    private int pingPort;
    private int pingTimeout;

    public OnlineCheckerImpl(Context context) {
        this.context = context;
        this.pingHost = DEFAULT_PING_HOST;
        this.pingPort = DEFAULT_PING_PORT;
        this.pingTimeout = DEFAULT_PING_TIMEOUT_IN_MS;
    }

    @Override
    public void check() {
        Observable.create(new Observable.OnSubscribe<Boolean>() {

            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                boolean isOnline = false;
                try {
                    Socket socket = new Socket();
                    socket.connect(new InetSocketAddress(pingHost, pingPort), pingTimeout);
                    isOnline = socket.isConnected();
                } catch (IOException e) {
                    isOnline = false;
                } finally {
                    subscriber.onNext(isOnline);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        sendBroadcast(aBoolean);
                    }
                });
    }

    @Override
    public void setPingParameters(String host, int port, int timeoutInMs) {
        this.pingHost = host;
        this.pingPort = port;
        this.pingTimeout = timeoutInMs;
    }

    private void sendBroadcast(boolean isOnline) {
        Intent intent = new Intent(InternetConnectionChangeReceiver.INTENT);
        intent.putExtra(InternetConnectionChangeReceiver.INTENT_EXTRA, isOnline);
        context.sendBroadcast(intent);
    }
}
