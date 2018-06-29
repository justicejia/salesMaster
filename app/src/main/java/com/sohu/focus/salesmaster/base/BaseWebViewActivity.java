package com.sohu.focus.salesmaster.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.http.api.GetFilterApi;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestListener;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.kernal.utils.EnvironmentManager;
import com.sohu.focus.salesmaster.kernal.utils.NetUtil;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;
import com.sohu.focus.salesmaster.kernal.utils.VersionCompat;
import com.sohu.focus.salesmaster.login.AccountManager;
import com.sohu.focus.salesmaster.main.view.MainActivity;
import com.sohu.focus.salesmaster.newFilter.model.FilterModel;
import com.sohu.focus.salesmaster.subordinate.view.SuborActivity;
import com.sohu.focus.salesmaster.uiframe.statusbar.StatusBarHelper;
import com.sohu.focus.salesmaster.uiframe.statusbar.SystemStatusManager;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Web
 * Created by yuanminjia on 2017/11/1.
 */

public class BaseWebViewActivity extends BaseActivity implements View.OnKeyListener {
    private static final String TAG = "BaseWebViewActivity";

    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.webview_progress)
    ProgressBar progressBar;
    @BindView(R.id.webview_top)
    RelativeLayout top;
    @BindView(R.id.webview_title)
    TextView title;
    @BindView(R.id.webview_close)
    ImageView close;
    @BindView(R.id.webview_trend)
    TextView trend;
    @BindView(R.id.error_with_back)
    LinearLayout errorWithBack;


    protected WebSettings mSettings;
    private String mMoreUrl;
    private String mUrl;

    @OnClick({R.id.web_back, R.id.back})
    void back() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

    @OnClick(R.id.refresh)
    void refresh() {
        errorWithBack.setVisibility(View.GONE);
        webView.loadUrl(mUrl);
    }

    @OnClick(R.id.webview_trend)
    void showTrend() {
        BaseWebViewActivity.naviToWebWithCookie(mMoreUrl, null, this, "趋势分析");
    }

    @OnClick(R.id.webview_close)
    void close() {
        finish();
    }

    /**
     * 楼盘详情页
     */
    public static void naviToWeb(String url, Context context, String title) {
        Intent intent = new Intent();
        intent.setClass(context, BaseWebViewActivity.class);
        intent.putExtra(SalesConstants.EXTRA_WEB_URL, url);
        intent.putExtra(SalesConstants.EXTRA_WEB_TITLE, title);
        context.startActivity(intent);
    }


    /**
     * 登陆页
     */
    public static void naviToWebWithFlag(String url, Context context) {
        Intent intent = new Intent();
        intent.setClass(context, BaseWebViewActivity.class);
        intent.putExtra(SalesConstants.EXTRA_WEB_URL, url);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 用于用户报表页
     */
    public static void naviToWebWithCookie(@Nullable String url, @Nullable String moreUrl, Context context, String title) {
        Intent intent = new Intent();
        intent.setClass(context, BaseWebViewActivity.class);
        intent.putExtra(SalesConstants.EXTRA_WEB_URL, url);
        intent.putExtra(SalesConstants.EXTRA_WEB_MORE_URL, moreUrl);
        intent.putExtra(SalesConstants.EXTRA_WEB_TITLE, title);
        intent.putExtra(SalesConstants.EXTRA_NEED_COOKIE, true);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_web);
        ButterKnife.bind(this);
        if (StatusBarHelper.statusBarLightMode(this) != StatusBarHelper.OTHER) {
            new SystemStatusManager(this).setTranslucentStatus(R.color.white);
        }
        initWebView();
        initData();
    }

    public void initData() {
        mUrl = getIntent().getStringExtra(SalesConstants.EXTRA_WEB_URL);
        String titleStr = getIntent().getStringExtra(SalesConstants.EXTRA_WEB_TITLE);
        mMoreUrl = getIntent().getStringExtra(SalesConstants.EXTRA_WEB_MORE_URL);
        boolean needSynCookie = getIntent().getBooleanExtra(SalesConstants.EXTRA_NEED_COOKIE, false);
        if (CommonUtils.notEmpty(titleStr)) {
            top.setVisibility(View.VISIBLE);
            title.setText(titleStr);
        } else {
            top.setVisibility(View.GONE);
        }
        if (CommonUtils.notEmpty(mMoreUrl)) {
            trend.setVisibility(View.VISIBLE);
            close.setVisibility(View.GONE);
        } else {
            close.setVisibility(View.VISIBLE);
            trend.setVisibility(View.GONE);
        }
        if (needSynCookie) {
            syncCookie(mUrl);
        }
        webView.loadUrl(mUrl);
    }

    public void syncCookie(String url) {
        if (CommonUtils.notEmpty(url) && webView != null) {
            if (VersionCompat.isBelowLOLLIPOP()) {
                CookieSyncManager.createInstance(this);
            }
            CookieManager cookieManager = CookieManager.getInstance();
            if (VersionCompat.isAtLeastLOLLIPOP())
                cookieManager.setAcceptThirdPartyCookies(webView, true);
            cookieManager.setAcceptCookie(true);
            HttpUrl target = HttpUrl.parse(url);
            if (target == null)
                return;
            if (AccountManager.INSTANCE.isLogin()) {
                HttpUrl focusDomain = HttpUrl.parse(FocusBaseApi.BASE_URL);
                if (focusDomain == null)
                    return;
                StringBuilder sb = new StringBuilder();
                if (CommonUtils.notEmpty(NetUtil.getInstance().getCookieJar().getCookieStore().getCookie(focusDomain))) {
                    for (Cookie cookie : NetUtil.getInstance().getCookieJar().getCookieStore().getCookie(focusDomain)) {
                        sb.setLength(0);
                        sb.append(cookie.name());
                        sb.append("=");
                        sb.append(cookie.value());
                        sb.append("; path=").append("/");
                        sb.append("; domain=").append(".").append(focusDomain.topPrivateDomain());
                        cookieManager.setCookie(focusDomain.topPrivateDomain(), sb.toString());
                    }
                }
            }
            if (VersionCompat.isBelowLOLLIPOP()) {
                CookieSyncManager.getInstance().sync();
            }

        }
    }

    public void initWebView() {
        mSettings = webView.getSettings();
        mSettings.setUseWideViewPort(true);
        mSettings.setSupportZoom(false);
        mSettings.setBuiltInZoomControls(false);
        mSettings.setBlockNetworkImage(false);
        mSettings.setJavaScriptEnabled(true);
        mSettings.setDomStorageEnabled(true);
        mSettings.setLoadWithOverviewMode(true);
        //加载https
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mSettings.setUserAgentString(webView.getSettings().getUserAgentString() + " " + NetUtil.getSalesUserAgent(false));
        mSettings.setDomStorageEnabled(true);
        mSettings.setDatabaseEnabled(true);
        mSettings.setAppCacheEnabled(true);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        mSettings.setAppCachePath(appCachePath);
        webView.setWebChromeClient(new ChromeClient(this));
        webView.setWebViewClient(new MyWebViewClient());
        webView.setOnKeyListener(this);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (webView != null) {
                webView.stopLoading();
                if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                    webView.goBack();// 返回前一个页面
                    return true;
                } else {
                    finish();
                }
            }
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            webView.setOnKeyListener(null);
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.destroy();
            webView = null;
            HttpEngine.getInstance().cancel(TAG);
        }
        super.onDestroy();
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("http://productivity.focus-test.cn/draw?")) {
                String[] split = url.split("\\?");
                if (split.length == 2) {
                    Map<String, String> paramsMap = new HashMap<>();
                    try {
                        NetUtil.parseParameters(paramsMap, split[1], "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (paramsMap.size() == 1 && paramsMap.containsKey("personId")) {
                        if (mIsVisible) {
                            Intent intent = new Intent();
                            intent.putExtra(SalesConstants.EXTRA_ID, paramsMap.get("personId"));
                            intent.putExtra(SalesConstants.EXTRA_LOAD_INFO, true);
                            intent.setClass(BaseWebViewActivity.this, SuborActivity.class);
                            startActivity(intent);
                        }
                        return true;
                    }
                }
            }
            if (url.startsWith("http://shengtai-audit")) {
                String cookie = CookieManager.getInstance().getCookie(url);
                String saveCookie = "";
                if (cookie.split("=").length > 1) {
                    String first = cookie.split("=")[0];
                    saveCookie = cookie.substring(first.length() + 1, cookie.length());
                }
                HashMap<String, String> cookieMap = new HashMap<>();
                if (EnvironmentManager.curEnvironment == EnvironmentManager.OFFICIAL_ENVIRONMENT) {
                    cookieMap.put(NetUtil.SALES_COOKIE, saveCookie);
                } else {
                    cookieMap.put(NetUtil.SALES_COOKIE, saveCookie);
                }
                NetUtil.getInstance().addCookie(cookieMap);
                GetFilterApi api = new GetFilterApi();
                api.setTag(TAG);
                HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<FilterModel>() {
                    @Override
                    public void onSuccess(FilterModel result, String method) {
                        if (result == null || result.getData() == null) return;
                        if (result.getData().getUserAvailableRoleList().size() > 1) {
                            AccountManager.INSTANCE.saveUserViewRoles(true);
                        } else {
                            AccountManager.INSTANCE.saveUserViewRoles(false);
                        }
                        boolean isWholeCountry = false;
                        if (CommonUtils.notEmpty(result.getData().getCityList())) {
                            for (FilterModel.DataBean.CityBean item : result.getData().getCityList()) {
                                if (item.getLabel().equals("全国")) isWholeCountry = true;
                            }
                        }
                        AccountManager.INSTANCE.saveUserIsWholeCountry(isWholeCountry);
                        AccountManager.INSTANCE.saveUserId(result.getData().getUserId());
                        AccountManager.INSTANCE.saveUserRole(result.getData().getSalesRole());
                        ToastUtil.toast("登录成功");
                        Intent intent = new Intent();
                        intent.setClass(BaseWebViewActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onFailed(FilterModel result, String method) {
                        if (result != null)
                            ToastUtil.toast(result.getMsg());
                        NetUtil.getInstance().cleanCookie();
                        webView.loadUrl(EnvironmentManager.getLoginUrl());
                    }
                });
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);

        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            errorWithBack.setVisibility(View.VISIBLE);
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            errorWithBack.setVisibility(View.VISIBLE);
        }
    }

    private static class ChromeClient extends WebChromeClient {

        private WeakReference<BaseWebViewActivity> mActivity;

        ChromeClient(BaseWebViewActivity fragment) {
            mActivity = new WeakReference<>(fragment);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            BaseWebViewActivity activity = mActivity.get();
            if (activity != null) {
                if (newProgress == 100) {
                    activity.progressBar.setVisibility(View.INVISIBLE);
                } else {
                    if (View.INVISIBLE == activity.progressBar.getVisibility()) {
                        activity.progressBar.setVisibility(View.VISIBLE);
                    }
                    activity.progressBar.setProgress(newProgress);
                }
            }

            super.onProgressChanged(view, newProgress);
        }

        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }

        @Override
        public void onGeolocationPermissionsHidePrompt() {
            super.onGeolocationPermissionsHidePrompt();
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);//注意个函数，第二个参数就是是否同意定位权限，第三个是是否希望内核记住
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }


    }

    private boolean mIsVisible;

    @Override
    protected void onPause() {
        super.onPause();
        mIsVisible = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsVisible = true;
    }
}
