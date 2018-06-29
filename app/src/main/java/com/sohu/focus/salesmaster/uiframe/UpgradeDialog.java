package com.sohu.focus.salesmaster.uiframe;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.main.model.UpdateModel;
import com.sohu.focus.salesmaster.utils.UpgradeUtil;

/**
 * 自更新dialog
 * Created by yuanminjia on 2017/12/14.
 */

public class UpgradeDialog extends BaseDialogFragment {
    private UpdateModel mData;
    TextView download;
    TextView content;
    TextView version;

    @Override
    protected void initDefaultData() {
        Bundle bundle = getArguments();
        mData = (UpdateModel) bundle.getSerializable(SalesConstants.EXTRA_UPGRADE_DATA);
    }

    @Override
    protected boolean getDialogFragmentCancelable() {
        return !mData.getData().isIsForcedUpdate();
    }

    @Override
    protected int getDialogLayoutId() {
        return R.layout.dialog_upgrade;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void loadWhenOverBound() {
        Dialog dialog = getDialog();
        dialog.getWindow().setLayout(CommonUtils.dp2pxConvertInt(getContext(), 275), -2);
    }

    @Override
    protected void findView(View contentView) {
        download = (TextView) contentView.findViewById(R.id.upgrade_download);
        content = (TextView) contentView.findViewById(R.id.upgrade_content);
        version = (TextView) contentView.findViewById(R.id.upgrade_version);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpgradeUtil.INSTANCE.startDownloadApk(mData.getData().getUpdateURL());
                dismiss();
            }
        });
        content.setText(mData.getData().getUpdateDescription());
        float v = Float.parseFloat(mData.getData().getVersion());
        float result = v / 100;
        version.setText(result + "");
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected boolean getDialogOverBound() {
        return false;
    }

}
