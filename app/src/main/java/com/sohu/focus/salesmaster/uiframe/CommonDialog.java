package com.sohu.focus.salesmaster.uiframe;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.SpannableString;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.kernal.log.Logger;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;

import java.util.HashSet;

/**
 * CommonDialog for custom
 */

public class CommonDialog extends BaseDialogFragment {

    private static HashSet<String> showTags = new HashSet<>();

    TextView title;

    TextView content;

    TextView cancel;

    TextView confirm;

    View portLineView;

    LinearLayout mRootLayout;

    private DialogParams dialogParams;

    private DismissListener listener;

    @Override
    protected void initDefaultData() {
        mScreenType = "1";
    }

    @Override
    protected boolean getDialogFragmentCancelable() {
        return true;
    }

    @Override
    protected int getDialogLayoutId() {
        return R.layout.ui_framework_dialog_common;
    }

    public void setDialogParams(DialogParams params) {
        this.dialogParams = params;
    }

    public TextView getCancelTextView() {
        return cancel;
    }

    public TextView getConfirmTextView() {
        return confirm;
    }

    public LinearLayout getRootLayout() {
        return mRootLayout;
    }

    protected void apply(final DialogParams params) {
        if (CommonUtils.notEmpty(params.mTitle)) {
            title.setVisibility(View.VISIBLE);
            title.setText(params.mTitle);
        } else {
            title.setVisibility(View.GONE);
        }
        if (params.mIsContentTextNeedSpan) {
            if (params.mSpanMessage != null && params.mSpanMessage.length() > 0) {
                content.setText(params.mSpanMessage, TextView.BufferType.SPANNABLE);
            }
        } else {
            if (CommonUtils.notEmpty(params.mHtmlContent)) {
                content.setText(Html.fromHtml(params.mHtmlContent));
            } else {
                if (CommonUtils.notEmpty(params.mMessage)) {
                    content.setText(params.mMessage);
                }
            }

        }
        if (params.mLineSpace != 0f) {
            content.setLineSpacing(0, params.mLineSpace);
        }
        if (CommonUtils.notEmpty(params.mCancelStr)) {
            if (params.mCancelTextColor != -1) {
                cancel.setTextColor(params.mCancelTextColor);
            }
            cancel.setText(params.mCancelStr);
        }
        if (CommonUtils.notEmpty(params.mConfirmStr)) {
            if (params.mConfirmTextColor != -1) {
                confirm.setTextColor(params.mConfirmTextColor);
            }
            confirm.setText(params.mConfirmStr);
        }
        if (params.mCancelListener != null) {
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    params.mCancelListener.onClick(v);
                    dismiss();
                }
            });
        } else {
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        if (params.mConfirmListener != null) {
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    params.mConfirmListener.onClick(v);
                    dismiss();
                }
            });
        } else {
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        if (params.mIsNeedCancelButton) {
            cancel.setVisibility(View.VISIBLE);
            portLineView.setVisibility(View.VISIBLE);
        } else {
            cancel.setVisibility(View.GONE);
            portLineView.setVisibility(View.GONE);
        }
        if (params.dismissListener != null) {
            listener = params.dismissListener;
        }
        if (params.mBackgroundResId != -1) {
            mRootLayout.setBackgroundResource(params.mBackgroundResId);
        }
        setCancelable(params.mCancelable);
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void findView(View contentView) {
        title = (TextView) contentView.findViewById(R.id.title);
        content = (TextView) contentView.findViewById(R.id.content_msg);
        cancel = (TextView) contentView.findViewById(R.id.cancel);
        confirm = (TextView) contentView.findViewById(R.id.confirm);
        portLineView = contentView.findViewById(R.id.common_dialog_port_line);
        mRootLayout = (LinearLayout) contentView.findViewById(R.id.common_dialog_root_layout);
        if (dialogParams != null) {
            apply(dialogParams);
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (dialogParams != null && dialogParams.showTagOnlyOne && CommonUtils.notEmpty(dialogParams.tag)) {
            if (showTags.contains(dialogParams.tag)) {
                Logger.ZQ().v("common dialog has already show " + tag + " dialog");
            } else {
                showTags.add(dialogParams.tag);
                super.show(manager, tag);
            }
        } else {
            super.show(manager, tag);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null)
            listener.onDismiss();
        if (dialogParams != null && dialogParams.showTagOnlyOne && CommonUtils.notEmpty(dialogParams.tag)) {
            showTags.remove(dialogParams.tag);
        }
    }

    @Override
    protected void loadData() {

    }

    public boolean isShowingTheOne(String tag) {
        return showTags.contains(tag);
    }

    @Override
    protected boolean getDialogOverBound() {
        return false;
    }

    public interface DismissListener {
        void onDismiss();
    }

    public static class CommonDialogBuilder {

        DialogParams dialogParams;

        public CommonDialogBuilder(Context context) {
            dialogParams = new DialogParams(context);
        }

        public CommonDialogBuilder title(String str) {
            dialogParams.mTitle = str;
            return this;
        }

        public CommonDialogBuilder content(String str) {
            dialogParams.mMessage = str;
            return this;
        }

        public CommonDialogBuilder htmlContent(String html) {
            dialogParams.mHtmlContent = html;
            return this;
        }

        public CommonDialogBuilder content(SpannableString ss) {
            dialogParams.mSpanMessage = ss;
            return this;
        }

        public CommonDialogBuilder leftBtnText(String str) {
            dialogParams.mCancelStr = str;
            return this;
        }

        public CommonDialogBuilder leftBtnText(int strId) {
            dialogParams.mCancelStr = dialogParams.mContext.getString(strId);
            return this;
        }

        public CommonDialogBuilder rightBtnText(String str) {
            dialogParams.mConfirmStr = str;
            return this;
        }

        public CommonDialogBuilder rightBtnText(int strId) {
            dialogParams.mConfirmStr = dialogParams.mContext.getString(strId);
            return this;
        }

        public CommonDialogBuilder leftBtnListener(View.OnClickListener listener) {
            dialogParams.mCancelListener = listener;
            return this;
        }

        public CommonDialogBuilder rightBtnListener(View.OnClickListener listener) {
            dialogParams.mConfirmListener = listener;
            return this;
        }

        public CommonDialogBuilder cancelable(boolean cancelable) {
            dialogParams.mCancelable = cancelable;
            return this;
        }

        public CommonDialogBuilder width(int width) {
            dialogParams.mScreenWidth = width;
            return this;
        }

        public CommonDialogBuilder isNeedCancelButton(boolean needCancelButton) {
            dialogParams.mIsNeedCancelButton = needCancelButton;
            return this;
        }

        public CommonDialogBuilder rightTextColor(int confirmTextColor) {
            dialogParams.mConfirmTextColor = confirmTextColor;
            return this;
        }

        public CommonDialogBuilder leftTextColor(int cancelTextColor) {
            dialogParams.mCancelTextColor = cancelTextColor;
            return this;
        }

        public CommonDialogBuilder isContentTextNeedSpan(boolean isNeedSpan) {
            dialogParams.mIsContentTextNeedSpan = isNeedSpan;
            return this;
        }

        public CommonDialogBuilder backgroundResource(int resId) {
            dialogParams.mBackgroundResId = resId;
            return this;
        }

        public CommonDialogBuilder dismiss(DismissListener listener) {
            dialogParams.dismissListener = listener;
            return this;
        }

        public CommonDialogBuilder lineSpaceing(float rate) {
            dialogParams.mLineSpace = rate;
            return this;
        }

        public CommonDialogBuilder onlyOne(boolean flag) {
            dialogParams.showTagOnlyOne = flag;
            return this;
        }

        public CommonDialogBuilder tag(String tag) {
            dialogParams.tag = tag;
            return this;
        }

        public CommonDialog create() {
            final CommonDialog dialog = new CommonDialog();
            dialog.setDialogParams(dialogParams);
            return dialog;
        }
    }

    public static class DialogParams {
        public final Context mContext;

        public String mTitle;
        public String mMessage;
        public String mCancelStr;
        public View.OnClickListener mCancelListener;
        public String mConfirmStr;
        public View.OnClickListener mConfirmListener;
        public boolean mCancelable = false;
        public int mScreenWidth;
        public boolean mIsNeedCancelButton = true;
        public DismissListener dismissListener;
        public int mConfirmTextColor = -1;
        public int mCancelTextColor = -1;
        public boolean mIsContentTextNeedSpan = false;
        public SpannableString mSpanMessage;
        public int mBackgroundResId = -1;
        public boolean showTagOnlyOne = false;
        public String tag = "";
        public String mHtmlContent;
        public float mLineSpace = 0f;

        public DialogParams(Context mContext) {
            this.mContext = mContext;
        }
    }
}
