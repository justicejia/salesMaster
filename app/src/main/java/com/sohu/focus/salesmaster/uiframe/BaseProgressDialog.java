package com.sohu.focus.salesmaster.uiframe;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.Window;

import com.sohu.focus.salesmaster.R;

/**
 * Created by jiayuanmin on 2018/4/27
 * description:
 */
public class BaseProgressDialog extends Dialog {

    private Context mContext;

    public BaseProgressDialog(@NonNull Context context) {
        super(context);
        mContext = context;
        initView();
    }

    private void initView() {
        Window window = getWindow();
        if (window != null) {
            window.requestFeature(Window.FEATURE_NO_TITLE);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        setContentView(R.layout.dialog_progress);
    }

}
