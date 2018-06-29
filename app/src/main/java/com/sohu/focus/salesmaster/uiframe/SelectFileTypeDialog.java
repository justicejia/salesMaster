package com.sohu.focus.salesmaster.uiframe;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.utils.FileSelectUtil;

/**
 * 选择上传文件类型dialog
 * Created by yuanminjia on 2018/1/31.
 */

public class SelectFileTypeDialog extends BaseDialogFragment implements View.OnClickListener {

    private TextView uploadImg, uploadFile, cancel;
    private static final int REQUEST_FOR_FILE = 1;
    private static final int REQUEST_FOR_IMG = 2;

    private OnDialogListener mListener;


    @Override
    protected void initDefaultData() {

    }

    @Override
    protected boolean getDialogFragmentCancelable() {
        return true;
    }

    @Override
    protected int getDialogLayoutId() {
        return R.layout.dialog_select_type;
    }

    @Override
    protected void findView(View contentView) {
        uploadFile = (TextView) contentView.findViewById(R.id.upload_file);
        uploadImg = (TextView) contentView.findViewById(R.id.upload_img);
        cancel = (TextView) contentView.findViewById(R.id.upload_cancel);
    }

    @Override
    protected void initView() {
        cancel.setOnClickListener(this);
        uploadFile.setOnClickListener(this);
        uploadImg.setOnClickListener(this);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected boolean getDialogOverBound() {
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload_cancel:
                dismiss();
                break;
            case R.id.upload_file:
                Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                intent1.setType("*/*");
                intent1.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent1, REQUEST_FOR_FILE);
                break;
            case R.id.upload_img:
                Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
                intent2.setType("image/*");
                intent2.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent2, REQUEST_FOR_IMG);
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        Uri uri = data.getData();
        String filePath = FileSelectUtil.INSTANCE.getFilePathByUri(uri, getContext());
        if (mListener != null) {
            mListener.onGetFilePath(filePath);
        }
        dismiss();
    }

    public interface OnDialogListener {
        void onGetFilePath(String filePath);
    }

    public void setDialogListener(OnDialogListener listener) {
        mListener = listener;
    }

}
