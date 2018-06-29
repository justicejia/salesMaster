package com.sohu.focus.salesmaster.invest.view;

import android.view.View;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.uiframe.BaseDialogFragment;
import com.sohu.focus.salesmaster.uiframe.SimpleWheelView;

import java.util.ArrayList;
import java.util.Calendar;

public class ChooseTimeDialog extends BaseDialogFragment {

    private static int mCurYear;
    private static int mCurMonth;
    private static final int DEFAULT_YEAR_MIN = 2010;
    private static final int DEFAULT_YEAR_MAX = 2030;

    static {
        mCurYear = Calendar.getInstance().get(Calendar.YEAR);
        mCurMonth = Calendar.getInstance().get(Calendar.MONTH);
    }

    private TextView cancel, confirm;
    private SimpleWheelView wheelViewYear, wheelViewMonth;
    private ArrayList<String> listYear = new ArrayList<>();
    private ArrayList<String> listMonth = new ArrayList<>();
    private int mSelectedYear;
    private int mSelectedMonth;
    private OnTimeSelectedListener mListener;

    public void setOnTimeSelectedListener(OnTimeSelectedListener listener) {
        mListener = listener;
    }

    @Override
    protected void initDefaultData() {
        for (int i = DEFAULT_YEAR_MIN; i < DEFAULT_YEAR_MAX; i++) {
            listYear.add("    " + i + "年");
        }
        for (int i = 1; i < 13; i++) {
            listMonth.add(i + "月    ");
        }
    }

    @Override
    protected boolean getDialogFragmentCancelable() {
        return true;
    }

    @Override
    protected int getDialogLayoutId() {
        return R.layout.dialog_choose_time;
    }

    @Override
    protected void initView() {
        wheelViewYear.setData(listYear);
        wheelViewYear.setDefault(mCurYear - DEFAULT_YEAR_MIN);
        wheelViewMonth.setData(listMonth);
        wheelViewMonth.setDefault(mCurMonth);
        wheelViewMonth.setCyclic(false);
        wheelViewYear.setCyclic(false);

        mSelectedMonth = mCurMonth + 1;
        mSelectedYear = mCurYear;

        wheelViewYear.setOnSelectListener(new SimpleWheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                mSelectedYear = DEFAULT_YEAR_MIN + id;
            }

            @Override
            public void selecting(int id, String text) {
            }
        });
        wheelViewMonth.setOnSelectListener(new SimpleWheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                mSelectedMonth = 1 + id;
            }

            @Override
            public void selecting(int id, String text) {
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onTimeSelected(mSelectedYear, mSelectedMonth);
                }
                dismiss();
            }
        });
    }


    @Override
    protected void findView(View contentView) {
        cancel = contentView.findViewById(R.id.choose_time_cancel);
        confirm = contentView.findViewById(R.id.choose_time_confirm);
        wheelViewYear = contentView.findViewById(R.id.wheelView_year);
        wheelViewMonth = contentView.findViewById(R.id.wheelView_month);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected boolean getDialogOverBound() {
        return true;
    }

    public interface OnTimeSelectedListener {
        void onTimeSelected(int year, int month);
    }
}
