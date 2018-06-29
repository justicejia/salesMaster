package com.sohu.focus.salesmaster.uiframe;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;

/**
 * Created by yuanminjia on 2017/11/6.
 */

public class LoadingDialog extends Dialog {
    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.loadingDialogStyle);
    }

    TextView tip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        tip = (TextView) findViewById(R.id.progress_dialog_tip);
        tip.setText("发布中..");
    }
}
