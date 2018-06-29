package com.sohu.focus.salesmaster.kernal.http;

import android.util.SparseArray;

import com.sohu.focus.salesmaster.kernal.http.convert.JacksonConverterFactory;
import com.sohu.focus.salesmaster.kernal.http.interceptor.FocusCacheInterceptor;
import com.sohu.focus.salesmaster.kernal.http.interceptor.FocusCacheOnlyInterceptor;
import com.sohu.focus.salesmaster.kernal.http.interceptor.FocusHeaderInterceptor;
import com.sohu.focus.salesmaster.kernal.http.interceptor.FocusTagInterceptor;
import com.sohu.focus.salesmaster.kernal.http.ssl.SSLManager;
import com.sohu.focus.salesmaster.kernal.log.FocusLog;
import com.sohu.focus.salesmaster.kernal.log.Logger;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.kernal.utils.NetUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

import static com.sohu.focus.salesmaster.kernal.http.ssl.SSLManager.SSL_TRUST_ALL;


/**
 * retrofit工厂类，保证每种类型的retrofit只创建一次
 * Created by qiangzhao on 2016/12/14.
 */

public class RetrofitFactory {

    private static final String TAG = RetrofitFactory.class.getSimpleName();

    private static SparseArray<Retrofit> retrofitMap = new SparseArray<>(5);
    private static Map<Integer, OkHttpClient> okHttpClientsCache = new HashMap<>(5);

    private static SparseArray<Retrofit> retrofitCacheMap = new SparseArray<>(5);
    private static List<OkHttpClient> okHttpClientsCache2 = new ArrayList<>(5);

    private static SSLManager.SSLParams sslParams;

    private RetrofitFactory() {
        sslParams = SSLManager.getSSLParams(SSL_TRUST_ALL);
    }

    public static Retrofit createRetrofit(BaseApi api) {
        Retrofit retrofit;
        int key = generateRetrofitKey(api);
        retrofit = retrofitMap.get(key);
        if (retrofit == null) {
            retrofit = createRetrofitInternal(api);
            retrofitMap.put(key, retrofit);
        }
        return retrofit;
    }

    public static Retrofit createCacheRetrofit(BaseApi api) {
        Retrofit retrofit;
        int key = generateRetrofitKey(api);
        retrofit = retrofitCacheMap.get(key);
        if (retrofit == null) {
            retrofit = createCacheRetrofitInternal(api);
            retrofitCacheMap.put(key, retrofit);
        }
        return retrofit;
    }

    public static void removeNoCacheRetrofit(BaseApi api) {
        Retrofit retrofit;
        int key = generateRetrofitKey(api);
        retrofit = retrofitCacheMap.get(key);
        if(retrofit != null)
            retrofitCacheMap.remove(key);
    }

    private static Retrofit createRetrofitInternal(BaseApi api) {
        OkHttpClient.Builder okHttpBuilder = getDefaultBuilder(api.getTag());
        if (api.isCache()) {
            okHttpBuilder.addInterceptor(new FocusCacheInterceptor())
                    .addNetworkInterceptor(new FocusCacheInterceptor())
                    .cache(NetUtil.getOkHttpCache());
        }
        if (FocusLog.isDebugging) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpBuilder.addInterceptor(httpLoggingInterceptor);
        }
        OkHttpClient client = okHttpBuilder.build();
        okHttpClientsCache.put(generateRetrofitKey(api),client);
        String baseUrl = CommonUtils.isEmpty(api.getCustomBaseUrl()) ? api.getBaseUrl() : api.getCustomBaseUrl();
        Logger.ZQ().d(TAG, "create retrofit : [baseUrl] : " + baseUrl + "  [cache] : " + api.isCache());
        return new Retrofit.Builder()
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }

    private static Retrofit createCacheRetrofitInternal(BaseApi api) {
        OkHttpClient.Builder okHttpBuilder = okHttpClientsCache.get(generateRetrofitKey(api)) != null?
                okHttpClientsCache.get(generateRetrofitKey(api)).newBuilder() : getDefaultBuilder(api.getTag());
        okHttpBuilder.retryOnConnectionFailure(false);//设置不进行连接失败重试
        okHttpBuilder.addInterceptor(new FocusCacheOnlyInterceptor());
        okHttpBuilder.addNetworkInterceptor(new FocusCacheOnlyInterceptor());
        okHttpBuilder.cache(NetUtil.getOkHttpCache());//这种缓存
        if (FocusLog.isDebugging) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpBuilder.addInterceptor(httpLoggingInterceptor);
        }
        OkHttpClient client = okHttpBuilder.build();
        okHttpClientsCache2.add(client);
        String baseUrl = CommonUtils.isEmpty(api.getCustomBaseUrl()) ? api.getBaseUrl() : api.getCustomBaseUrl();
        Logger.ZQ().d(TAG, "create cache retrofit : [baseUrl] : " + baseUrl + "  [cache] : " + api.isCache());
        return new Retrofit.Builder()
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }

    public static int generateRetrofitKey(BaseApi api) {
        String domainUrl = CommonUtils.isEmpty(api.getCustomBaseUrl()) ? api.getBaseUrl() : api.getCustomBaseUrl();
        return domainUrl.hashCode() + (api.isCache() ? 1 : 0);
    }

    public static int generateCacheKey(BaseApi api) {
        String domainUrl = api.getCompleteUrl();
        return domainUrl.hashCode() + (api.isCache() ? 1 : 0);
    }

    /**
     * 配置okhttp的公共参数
     */
    private static OkHttpClient.Builder getDefaultBuilder(String tag) {
        return new OkHttpClient.Builder()
                .connectTimeout(NetUtil.TIMEOUT_TIME, TimeUnit.SECONDS)
                .readTimeout(NetUtil.TIMEOUT_TIME, TimeUnit.SECONDS)
                .addNetworkInterceptor(new FocusHeaderInterceptor())
                .cookieJar(NetUtil.getInstance().getCookieJar())
                .addInterceptor(new FocusTagInterceptor(tag))
                .addNetworkInterceptor(new FocusTagInterceptor(tag));
//                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
//                .hostnameVerifier(getHostnameVerifier(new String[]{BaseApi.BASE_URL}));
    }

    protected static HostnameVerifier getHostnameVerifier(final String[] hostUrls) {

        HostnameVerifier TRUSTED_VERIFIER = new HostnameVerifier() {

            public boolean verify(String hostname, SSLSession session) {
                boolean ret = false;
                for (String host : hostUrls) {
                    if (host.equalsIgnoreCase(hostname)) {
                        ret = true;
                    }
                }
                return ret;
            }
        };
        return TRUSTED_VERIFIER;
    }

    public static Map<Integer,OkHttpClient> getOkHttpClientCache() {
        return okHttpClientsCache;
    }

    public static List<OkHttpClient> getOkHttpClientCache2() {
        return okHttpClientsCache2;
    }
}
