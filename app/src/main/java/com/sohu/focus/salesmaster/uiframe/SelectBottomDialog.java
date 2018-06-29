package com.sohu.focus.salesmaster.uiframe;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;

/**
 * Created by yuanminjia on 2017/11/6.
 */

public class SelectBottomDialog extends Dialog {

    TextView photo, album;
    Button cancel;
    Context mContext;
    private OnSelectImgListener mListenr;

    public SelectBottomDialog(@NonNull Context context) {
        super(context, R.style.BottomDialogStyle);
        mContext = context;
    }

    public void setImgListener(OnSelectImgListener listener) {
        mListenr = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.getDecorView().setPadding(40,0,40,0);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.BOTTOM;
        dialogWindow.setAttributes(lp);
        setContentView(R.layout.select_img_dialog);
        cancel = (Button) findViewById(R.id.select_img_cancel);
        photo = (TextView) findViewById(R.id.select_img_photo);
        album = (TextView) findViewById(R.id.select_img_album);
        setListener();
    }


    public void setListener() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListenr != null) {
                    mListenr.onClickPhoto();
                }
                dismiss();
            }
        });
        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListenr != null) {
                    mListenr.onClickAlbum();
                }
                dismiss();
            }
        });
    }

    public interface OnSelectImgListener {
        void onClickPhoto();

        void onClickAlbum();
    }
}
