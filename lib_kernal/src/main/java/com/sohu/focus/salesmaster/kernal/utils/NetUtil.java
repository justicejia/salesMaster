package com.sohu.focus.salesmaster.kernal.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;

import com.sohu.focus.salesmaster.kernal.BaseApplication;
import com.sohu.focus.salesmaster.kernal.http.BaseApi;
import com.sohu.focus.salesmaster.kernal.http.cookie.CookieJarImpl;
import com.sohu.focus.salesmaster.kernal.http.cookie.CookieStore;
import com.sohu.focus.salesmaster.kernal.http.cookie.PersistentCookieStore;
import com.sohu.focus.salesmaster.kernal.http.download.DownInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Map;

import okhttp3.Cache;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

/**
 * Created by qiangzhao on 2016/11/14.
 */

public class NetUtil {

    private static final int SIZE_OF_CACHE = 10 * 1024 * 1024;

    private static final int DAY = 24 * 60 * 60;

    private static final int HOUR = 60 * 60;

    private static final int MINUTES = 60;

    /*无网络的情况下本地缓存时间默认30天*/
    public static final int CACHE_NO_NETWORK_TIME = 30 * DAY;
    /*超时时间-默认20秒*/
    public static final int TIMEOUT_TIME = 20;
    /*有网情况下的本地缓存时间默认1秒*/
    public static final int CACHE_HAS_NETWORK_TIME = 1;

    public static final String SALES_COOKIE = "FOCUS_A_UDIG";

    public static final String SALES_USER_AGENT = "SalesMasterApp_Android";

    private CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore());

    private NetUtil() {
    }

    public static NetUtil getInstance() {
        return NetUtilHolder.holder;
    }


    public static Cache getOkHttpCache() {
        File cacheDirectory = new File(StorageUtil.getCacheDir(), StorageUtil.DEFAULT_NETWORK_CACHE_DIR);
        return new Cache(cacheDirectory, NetUtil.SIZE_OF_CACHE);
    }

    public static String getDownloadPath() {
        File cacheDirectory = new File(StorageUtil.getCacheDir(), StorageUtil.DEFAULT_DOWNLOAD_DIR);
        return cacheDirectory.getAbsolutePath();
    }

    /**
     * 描述：判断网络是否有效.
     *
     * @return true, if is network available
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 当前网络是连接的
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        // 当前所连接的网络可用
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 读取baseurl
     *
     * @param url
     * @return
     */
    public static String getBaseUrl(String url) {
        String head = "";
        int index = url.indexOf("://");
        if (index != -1) {
            head = url.substring(0, index + 3);
            url = url.substring(index + 3);
        }
        index = url.indexOf("/");
        if (index != -1) {
            url = url.substring(0, index + 1);
        }
        return head + url;
    }

    /**
     * 写入文件
     *
     * @param file
     * @param info
     * @throws IOException
     */
    public static void writeCache(ResponseBody responseBody, File file, DownInfo info) throws IOException {
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        long allLength;
        if (info.getCountLength() == 0) {
            allLength = responseBody.contentLength();
        } else {
            allLength = info.getCountLength();
        }
        FileChannel channelOut = null;
        RandomAccessFile randomAccessFile = null;
        randomAccessFile = new RandomAccessFile(file, "rwd");
        channelOut = randomAccessFile.getChannel();
        MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE,
                info.getReadLength(), allLength - info.getReadLength());
        byte[] buffer = new byte[1024 * 8];
        int len;
        InputStream is = responseBody.byteStream();
        while ((len = is.read(buffer)) != -1) {
            mappedBuffer.put(buffer, 0, len);
        }
        is.close();
        if (channelOut != null) {
            channelOut.close();
        }
        if (randomAccessFile != null) {
            randomAccessFile.close();
        }
    }

    /**
     * 保存
     *
     * @param response
     * @return
     * @throws IOException
     */
    public static void saveFile(ResponseBody response, File file) throws IOException {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (file.exists()) {
            file.delete();
        }
        BufferedSink sink = Okio.buffer(Okio.sink(file));
        Buffer buffer = sink.buffer();

        int bufferSize = 8 * 1024;

        BufferedSource source = response.source();
        while (source.read(buffer, bufferSize) != -1) {
            sink.emit();
        }
        source.close();
        sink.close();
    }


    /*获取系统默认的User-Agent*/
    private static String getDefaultUserAgent() {
        String userAgent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = WebSettings.getDefaultUserAgent(BaseApplication.getApplication());
            } catch (Exception e) {
                userAgent = System.getProperty("http.agent");
            }
        } else {
            userAgent = System.getProperty("http.agent");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String getSalesUserAgent(boolean userDefault) {
        return userDefault ? getDefaultUserAgent() + SALES_USER_AGENT : SALES_USER_AGENT;
    }


    /**
     * 获取全局的cookie实例
     */
    public CookieJarImpl getCookieJar() {
        return cookieJar;
    }

    public void addCookie(Map<String, String> cookieMap) {
        if (cookieMap == null || cookieMap.isEmpty())
            return;
        addCookie(cookieMap, true);
    }

    /**
     * 添加Cookie
     */
    public void addCookie(Map<String, String> cookieMap, boolean isNeedShareSave) {
        if (cookieMap == null || cookieMap.isEmpty())
            return;
        HttpUrl httpUrl = HttpUrl.parse(BaseApi.BASE_URL);
        Cookie.Builder builder = new Cookie.Builder();
        CookieStore cookieStore = getCookieJar().getCookieStore();
        for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
            builder.name(entry.getKey()).value(entry.getValue());
            Cookie cookie = builder.domain(httpUrl.host()).build();
            cookieStore.saveCookie(httpUrl, cookie, isNeedShareSave);
        }
    }


    public void cleanCookie() {
        //清理本地cookie
        CookieStore cookieStore = getCookieJar().getCookieStore();
        cookieStore.removeAllCookie();
        //清理webview cookie
        CookieSyncManager.createInstance(BaseApplication.getApplication());
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance().removeAllCookie();
    }

    private static class NetUtilHolder {

        private static NetUtil holder = new NetUtil();
    }

    public static void parseParameters(Map map, String data, String encoding) throws UnsupportedEncodingException {
        if (data != null && data.length() > 0) {
            byte[] bytes = null;

            try {
                if (encoding == null) {
                    bytes = data.getBytes();
                } else {
                    bytes = data.getBytes(encoding);
                }
            } catch (UnsupportedEncodingException var5) {
                ;
            }

            parseParameters(map, bytes, encoding);
        }
    }

    public static void parseParameters(Map map, byte[] data, String encoding) throws UnsupportedEncodingException {
        if (data != null && data.length > 0) {
            int ix = 0;
            int ox = 0;
            String key = null;
            String value = null;

            while(ix < data.length) {
                byte c = data[ix++];
                switch((char)c) {
                    case '%':
                        data[ox++] = (byte)((convertHexDigit(data[ix++]) << 4) + convertHexDigit(data[ix++]));
                        break;
                    case '&':
                        value = new String(data, 0, ox, encoding);
                        if (key != null) {
                            putMapEntry(map, key, value);
                            key = null;
                        }

                        ox = 0;
                        break;
                    case '+':
                        data[ox++] = 32;
                        break;
                    case '=':
                        if (key == null) {
                            key = new String(data, 0, ox, encoding);
                            ox = 0;
                        } else {
                            data[ox++] = c;
                        }
                        break;
                    default:
                        data[ox++] = c;
                }
            }

            if (key != null) {
                value = new String(data, 0, ox, encoding);
                putMapEntry(map, key, value);
            }
        }

    }

    private static void putMapEntry(Map map, String name, String value) {
        String[] newValues = null;
        String[] oldValues = (String[])((String[])((String[])map.get(name)));
        if (oldValues == null) {
            newValues = new String[]{value};
        } else {
            newValues = new String[oldValues.length + 1];
            System.arraycopy(oldValues, 0, newValues, 0, oldValues.length);
            newValues[oldValues.length] = value;
        }

        map.put(name, newValues[0]);
    }

    private static byte convertHexDigit(byte b) {
        if (b >= 48 && b <= 57) {
            return (byte)(b - 48);
        } else if (b >= 97 && b <= 102) {
            return (byte)(b - 97 + 10);
        } else {
            return b >= 65 && b <= 70 ? (byte)(b - 65 + 10) : 0;
        }
    }

}
