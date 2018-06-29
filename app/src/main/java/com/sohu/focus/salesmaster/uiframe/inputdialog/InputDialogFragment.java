package com.sohu.focus.salesmaster.uiframe.inputdialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by luckyzhangx on 12/03/2018.
 */

public class InputDialogFragment extends AppCompatDialogFragment {

//    listener

    //    发送
    public interface SendListener {
        void send(String content);
    }

    //    内容没有发送，外部可以选择缓存
    public interface CacheListener {
        void cache(String content);
    }

    String hint = "";

    private SendListener mSendListener;
    private CacheListener mCacheListener;

    public void setSendListener(SendListener mSendListener) {
        this.mSendListener = mSendListener;
    }

    public void setCacheListener(CacheListener mCacheListener) {
        this.mCacheListener = mCacheListener;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    //    View

    @BindView(R.id.contentEt)
    EditText contentEt;

    @OnClick(R.id.sendBtn)
    public void send() {
        if (CommonUtils.isEmpty(contentEt.getText().toString())) {
            ToastUtil.toast("请输入内容");
        } else if (mSendListener != null) {
            mSendListener.send(contentEt.getText().toString());
            dismiss();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_input, container, false);

        ButterKnife.bind(this, v);

        contentEt.setHint(hint);
        contentEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    send();
                    handled = true;
                }
                return handled;
            }
        });

        return v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        initWindow(dialog.getWindow());
        return dialog;
    }

    private void initWindow(Window window) {
        window.setGravity(Gravity.BOTTOM);
        window.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable
                .dialog_answer_inset));
    }

    @Override
    public void onResume() {
        super.onResume();

//        去除奇怪的 Window 默认设置的 padding。
        Window window = getDialog().getWindow();
        Point point = new Point();
        window.getWindowManager().getDefaultDisplay().getSize(point);
        window.setLayout(point.x, window.getAttributes().height);

        window.setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mSendListener = null;
        mCacheListener = null;
    }

    protected static class Params {
        SendListener mSendListener;
        CacheListener mCacheListener;

        String hint;
    }


    public static class Builder {

        private Params params = new Params();

        public Builder() {
        }

        public Builder setSendListener(SendListener sendListener) {
            params.mSendListener = sendListener;
            return this;
        }

        public Builder setCacheListener(CacheListener cacheListener) {
            params.mCacheListener = cacheListener;
            return this;
        }

        public Builder setHint(String hint) {
            params.hint = hint;
            return this;
        }

        public InputDialogFragment show(FragmentManager fm, String tag) {
            InputDialogFragment inputDialogFragment = new InputDialogFragment();
            inputDialogFragment.setSendListener(params.mSendListener);
            inputDialogFragment.setCacheListener(params.mCacheListener);
            inputDialogFragment.setHint(params.hint);
            inputDialogFragment.show(fm, tag);

            return inputDialogFragment;
        }
    }
}
