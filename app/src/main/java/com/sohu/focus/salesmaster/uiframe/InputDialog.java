package com.sohu.focus.salesmaster.uiframe;

import android.app.Dialog;
import android.content.Context;

import com.sohu.focus.salesmaster.R;

/**
 * Created by luckyzhangx on 12/03/2018.
 */

// 紧挨着键盘的输入窗口

public class InputDialog extends Dialog {
    public InputDialog(Context context) {
        super(context, R.style.InputDialog);
//        View view = LayoutInflater.from(context).inflate(R.layout.layout_input,null);

        setContentView(R.layout.layout_input);
    }
}
