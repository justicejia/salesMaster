package com.sohu.focus.salesmaster.kernal.router.interceptor;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sohu.focus.salesmaster.kernal.BaseApplication;
import com.sohu.focus.salesmaster.kernal.router.GlobalService;
import com.sohu.focus.salesmaster.kernal.router.RouterAction;
import com.sohu.focus.salesmaster.kernal.router.service.LoginService;

/**
 * Created by zhaoqiang on 2017/8/24.
 */
@Interceptor(priority = 7)
public class LoginInterceptor implements IInterceptor {

    Context mContext;
    LoginService loginService;

    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        if(postcard.getExtra() == RouterAction.ACTION_LOGIN) {
            if(loginService.isLogin()) {
                callback.onContinue(postcard);
            } else {
                callback.onInterrupt(null);
                loginService.startLogin(BaseApplication.getApplication());
            }
        } else {
            callback.onContinue(postcard);
        }
    }

    @Override
    public void init(Context context) {
        mContext = context;
        loginService = (LoginService) ARouter.getInstance().build(GlobalService.LIVE_GROUP.LOGIN).navigation();
    }
}
