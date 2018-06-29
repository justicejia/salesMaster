package com.sohu.focus.salesmaster.project.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BaseActivity;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.http.HttpEngine;
import com.sohu.focus.salesmaster.http.api.UpdateCompetitorApi;
import com.sohu.focus.salesmaster.http.model.UpdateCompetitorModel;
import com.sohu.focus.salesmaster.kernal.http.BaseModel;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestListener;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 添加竞品
 * Created by yuanminjia on 2018/1/12.
 */

public class AddCompetitorActivity extends BaseActivity {

    public static final String TAG = AddCompetitorActivity.class.getSimpleName();

    @BindView(R.id.add_competitor_sure)
    TextView confirm;

    @OnClick(R.id.add_competitor_back)
    void back() {
        finish();
    }

    @OnClick(R.id.add_competitor_sure)
    void confirm() {
        if (price.getText().length() > 0 && company.getText().length() > 0) {
            updateData();
        }
    }

    @BindView(R.id.add_competitor_company)
    EditText company;

    @BindView(R.id.add_competitor_price)
    EditText price;

    private UpdateCompetitorModel mData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_competitor);
        ButterKnife.bind(this);
        mData = (UpdateCompetitorModel) getIntent().getSerializableExtra(SalesConstants.EXTRA_COMPETITORS);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpEngine.getInstance().cancel(TAG);
    }

    public void updateData() {
        HashMap<String, String> map = new HashMap<>();
        map.put(company.getText().toString(), price.getText().toString());
        mData.getCompetitorMoney().add(map);
        UpdateCompetitorApi api = new UpdateCompetitorApi(mData);
        api.setTag(TAG);
        HttpEngine.getInstance().doHttpRequest(api, new HttpRequestListener<BaseModel>() {
            @Override
            public void onSuccess(BaseModel result, String method) {
                if (result != null) {
                    Intent intent = new Intent();
                    intent.putExtra(SalesConstants.EXTRA_COMPETITORS, mData);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }

            @Override
            public void onError(Throwable e) {
                mData.getCompetitorMoney().remove(mData.getCompetitorMoney().size() - 1);

            }

            @Override
            public void onFailed(BaseModel result, String method) {
                ToastUtil.toast("添加失败");
                mData.getCompetitorMoney().remove(mData.getCompetitorMoney().size() - 1);

            }
        });
    }


    public void initView() {
        company.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (price.getText().length() > 0 && s.length() > 0) {
                    confirm.setTextColor(ContextCompat.getColor(AddCompetitorActivity.this, R.color.home_icon_selected));
                } else {
                    confirm.setTextColor(ContextCompat.getColor(AddCompetitorActivity.this, R.color.standard_text_light_gray));
                }
            }
        });

        price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (company.getText().length() > 0 && s.length() > 0) {
                    confirm.setTextColor(ContextCompat.getColor(AddCompetitorActivity.this, R.color.home_icon_selected));
                } else {
                    confirm.setTextColor(ContextCompat.getColor(AddCompetitorActivity.this, R.color.standard_text_light_gray));
                }
            }
        });
    }


}
