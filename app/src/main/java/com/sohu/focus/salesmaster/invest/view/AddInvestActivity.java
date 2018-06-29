package com.sohu.focus.salesmaster.invest.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseActivity;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.http.api.AddInvestRecordApi;
import com.sohu.focus.salesmaster.http.api.CheckInvestExistApi;
import com.sohu.focus.salesmaster.kernal.http.BaseModel;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestListener;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.kernal.utils.ScreenUtil;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;
import com.sohu.focus.salesmaster.project.model.CheckInvestResultModel;
import com.sohu.focus.salesmaster.uiframe.CommonDialog;
import com.sohu.focus.salesmaster.uiframe.statusbar.StatusBarHelper;
import com.sohu.focus.salesmaster.uiframe.statusbar.SystemStatusManager;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sohu.focus.salesmaster.uiframe.statusbar.SystemStatusManager.STATUS_STRATEGY_ABOVE_KITKAT;

/**
 * 新增投放
 */
public class AddInvestActivity extends BaseActivity {

    private String mProjectId;
    private String mMonth;
    private String mInvestNumMoney;

    @BindView(R.id.add_invest_confirm)
    TextView confirm;
    @BindView(R.id.add_invest_choose)
    TextView choose;
    @BindView(R.id.add_invest_edit)
    EditText edit;
    @BindView(R.id.add_invest_top)
    RelativeLayout top;

    @OnClick(R.id.add_invest_back)
    void onClickBack() {
        finish();
    }

    @OnClick(R.id.add_invest_choose)
    void onClickChooseTime() {
        ChooseTimeDialog dialog = new ChooseTimeDialog();
        dialog.setOnTimeSelectedListener(new ChooseTimeDialog.OnTimeSelectedListener() {
            @Override
            public void onTimeSelected(int year, int month) {
                if (month < 10) {
                    mMonth = year + "年0" + month + "月";
                } else {
                    mMonth = year + "年" + month + "月";
                }
                choose.setText(mMonth);
                choose.setTextColor(ContextCompat.getColor(AddInvestActivity.this, R.color.standard_text_black));
                refreshConfirmStatus();

            }
        });
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    @OnClick(R.id.add_invest_confirm)
    void onClickConfirm() {
        if (CommonUtils.isEmpty(edit.getText().toString()) || CommonUtils.isEmpty(mMonth)) {
            ToastUtil.toast("请填写完整信息");
            return;
        }
        if (!isNumeric(mInvestNumMoney)) {
            ToastUtil.toast("前填正确的数额");
            return;
        }
        checkIfInvestExist();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invest);
        ButterKnife.bind(this);
        if (StatusBarHelper.statusBarLightMode(this) != StatusBarHelper.OTHER) {
            if (new SystemStatusManager(this).setTranslucentStatus(R.color.white) == STATUS_STRATEGY_ABOVE_KITKAT) {
                int statusHeight = ScreenUtil.getStatusBarHeight(this);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) top.getLayoutParams();
                layoutParams.topMargin = statusHeight;
                top.setLayoutParams(layoutParams);
            }
        }
        mProjectId = getIntent().getStringExtra(SalesConstants.EXTRA_PROJECT_ID);
        initView();
    }

    void initView() {
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mInvestNumMoney = s.toString();
                refreshConfirmStatus();
            }
        });
    }

    private boolean isNumeric(String moneyStr) {
        Pattern pattern = Pattern.compile("\\d*\\.?\\d*");
        return pattern.matcher(moneyStr).matches();
    }

    //添加记录，update：是否是覆盖，true，覆盖 false 累加
    private void updateInvest(boolean update) {
        showProgress();
        AddInvestRecordApi api = new AddInvestRecordApi(mProjectId);
        api.setMoney(mInvestNumMoney);
        api.setMonth(mMonth);
        api.setType(update ? 0 : 1);
        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<BaseModel>() {
            @Override
            public void onSuccess(BaseModel result, String method) {
                dismissProgress();
                CommonDialog dialog = new CommonDialog.CommonDialogBuilder(AddInvestActivity.this)
                        .content("本条投放已更新至项目投放记录")
                        .rightBtnText("我知道了")
                        .isNeedCancelButton(false)
                        .rightTextColor(ContextCompat.getColor(AddInvestActivity.this, R.color.home_icon_selected))
                        .rightBtnListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                goBack();
                            }
                        }).cancelable(false).create();
                dialog.show(getSupportFragmentManager(), "confirm");
            }

            @Override
            public void onError(Throwable e) {
                dismissProgress();
            }

            @Override
            public void onFailed(BaseModel result, String method) {
                if (result != null) {
                    ToastUtil.toast(result.getMsg());
                }
                dismissProgress();
            }
        });
    }


    private void refreshConfirmStatus() {
        if (CommonUtils.notEmpty(mInvestNumMoney) && mInvestNumMoney.length() > 0 && CommonUtils.notEmpty(mMonth)) {
            confirm.setTextColor(ContextCompat.getColor(AddInvestActivity.this, R.color.home_icon_selected));
        } else {
            confirm.setTextColor(ContextCompat.getColor(AddInvestActivity.this, R.color.standard_text_light_gray));
        }
    }

    private void goBack() {
        Intent intent = new Intent();
        intent.putExtra(SalesConstants.EXTRA_INVEST_MONEY, mInvestNumMoney);
        intent.putExtra(SalesConstants.EXTRA_INVEST_TIME, mMonth);
        setResult(RESULT_OK, intent);
        finish();
    }

    //检查是否存在记录
    private void checkIfInvestExist() {
        CheckInvestExistApi api = new CheckInvestExistApi(mProjectId, mMonth);
        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<CheckInvestResultModel>() {
            @Override
            public void onSuccess(CheckInvestResultModel result, String method) {
                if (result != null) {
                    if (result.getData() == null) {
                        updateInvest(true);
                    } else if (CommonUtils.isEmpty(result.getData().getEstateId())) {
                        updateInvest(true);
                    } else {
                        CommonDialog dialog = new CommonDialog.CommonDialogBuilder(AddInvestActivity.this)
                                .htmlContent("该月投放记录已存在<br>" +
                                        "<font color=#666666> 是否替换已有记录或累加本次金额至该月投放记录？</font>")
                                .leftBtnText("替换")
                                .rightBtnText("累加")
                                .leftTextColor(ContextCompat.getColor(AddInvestActivity.this, R.color.home_icon_selected))
                                .rightTextColor(ContextCompat.getColor(AddInvestActivity.this, R.color.home_icon_selected))
                                .leftBtnListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        updateInvest(true);
                                    }
                                })
                                .lineSpaceing(1.3f)
                                .rightBtnListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        updateInvest(false);
                                    }
                                }).create();
                        dialog.show(getSupportFragmentManager(), "Invest");
                    }
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onFailed(CheckInvestResultModel result, String method) {
                if (result != null) {
                    ToastUtil.toast(result.getMsg());
                }
            }
        });
    }

}
