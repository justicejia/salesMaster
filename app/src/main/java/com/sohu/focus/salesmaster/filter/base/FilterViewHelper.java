package com.sohu.focus.salesmaster.filter.base;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;

/**
 * Created by luckyzhangx on 17-11-7.
 */

public abstract class FilterViewHelper implements PopupWindow.OnDismissListener {

    private static final String TAG = "FilterViewHelper";

    public static final int FILTER_CITY = 1;
    public static final int FILTER_SALES_ROLE = 2;
    public static final int FILTER_PROGRESS = 3;
    public static final int FILTER_PROJ_ORDER = 4;

    protected PopupWindow mPopupWindow;

    protected Context mContext;
    protected View mAnchorView, triggerView;
    protected ViewGroup mFilterView;

    protected ListView mainList, subList, terList;
    protected BaseFilterModelAdapter<FilterVO> mainAdapter, subAdapter, terAdapter;

    protected FilterRecorder recorder;

    protected boolean needRefresh = false;

    public FilterViewHelper(Context context, View anchorView) {
        mContext = context;
        mAnchorView = anchorView;
        initViews();
    }

    protected void initViews() {

        mPopupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setAnimationStyle(0);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOnDismissListener(this);

        mFilterView = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.layout_filter, null);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
            }
        };

        View.OnClickListener dissmissListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        };

        mFilterView.setOnClickListener(dissmissListener);

        mFilterView.getChildAt(0).setOnClickListener(listener);


        mainList = mFilterView.findViewById(R.id.main_listview);

        mainAdapter = new BaseFilterModelAdapter<FilterVO>() {

            @Override
            public View getConvertView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_filter_main_item, null);
                }
                String name = getItem(position).desc;
                TextView textView = (TextView) convertView.findViewById(R.id.tv_filter);
                textView.setText(name);
                convertView.setBackground(new ColorDrawable(0xffffffff));
                return convertView;
            }

            @Override
            protected void selModelView(View item, int position) {
                ((TextView) ((ViewGroup) item).getChildAt(0)).setTextColor(
                        ContextCompat.getColor(mContext, R.color.standard_text_highlight));
            }

            @Override
            protected void deSelModelView(View item, int position) {
                ((TextView) ((ViewGroup) item).getChildAt(0)).setTextColor(
                        ContextCompat.getColor(mContext, R.color.standard_text_light_black)
                );
            }
        };
        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mainAdapter.setSel(position);
                adjustListViewHeight();
                record(mainAdapter.getSelModel());
                dismissAndRefreshWithTag(mainAdapter.getSelModel().getDesc());
            }
        });

        mainList.setAdapter(mainAdapter);
    }

    public void setTriggerView(View triggerView) {
        this.triggerView = triggerView;
    }


    protected void slideFromTop() {
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(300);
        TranslateAnimation anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, -1,
                Animation.RELATIVE_TO_SELF, 0);
        anim.setDuration(300);
        mPopupWindow.getContentView().startAnimation(alpha);
        ((ViewGroup) (mPopupWindow.getContentView())).getChildAt(0).startAnimation(anim);
    }

    protected void dismissAndRefreshWithTag(String tag) {
        if (!CommonUtils.isEmpty(tag))
            ((TextView) triggerView).setText(tag);
        ((TextView) triggerView).setTextColor(ContextCompat.getColor(mContext, R.color.standard_text_highlight));
        dismissAndRefresh();
    }

    protected void dismissAndRefresh() {
        needRefresh = true;
        mPopupWindow.dismiss();
    }

    public void showFilterView(int filterType, View triggerView) {

        setDropDownHeight();
        setTriggerView(triggerView);
        ((TextView) triggerView).setTextColor(ContextCompat.getColor(mContext, R.color.standard_text_highlight));
        ((TextView) triggerView).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable
                        .filter_arrow_up,
                0);
        mPopupWindow.setContentView(mFilterView);
    }

    protected void showPopupWindow() {
        adjustListViewHeight();
        mPopupWindow.showAsDropDown(mAnchorView);
        slideFromTop();
    }


    protected void showListView() {
        mPopupWindow.setContentView(mFilterView);
        mainList.setVisibility(View.VISIBLE);
        mainAdapter.clear();
    }

    protected void setDropDownHeight() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int location[] = new int[2];
        mAnchorView.getLocationOnScreen(location);
        int height = size.y - location[1] - mAnchorView.getHeight();
        mPopupWindow.setHeight(height);
    }

    protected void adjustListViewHeight() {
        wrapListView(mainList);
        wrapListView(subList);
        wrapListView(terList);

    }

    private void wrapListView(@Nullable ListView list) {
        if (list == null || list.getAdapter() == null) return;
        ViewGroup.LayoutParams lp = list.getLayoutParams();
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        list.setLayoutParams(lp);
    }

    protected void clearAdapter(Boolean clearMain, boolean clearSub, boolean clearTer) {
        if (mainAdapter != null && clearMain)
            mainAdapter.setModels(null);
        if (subAdapter != null && clearSub)
            subAdapter.setModels(null);
        if (terAdapter != null && clearTer)
            terAdapter.setModels(null);
    }

    public void setRecorder(FilterRecorder recorder) {
        this.recorder = recorder;
    }

    protected FilterVO getRecord(String fieldName) {
        if (recorder == null) return null;
        FilterRecord record = recorder.getRecord(fieldName);
        if (record != null) return (FilterVO) record.getFilter();
        return null;
    }

    protected void record(FilterVO filter) {
        if (recorder == null) return;
        FilterRecord record = new FilterRecord(filter.fieldName, filter.fieldValue, filter.parentFieldName);
        record.setFilter(filter);
        recorder.insert(record);
    }

    protected void removeRecord(String fieldName) {
        if (recorder != null) {
            recorder.remove(fieldName);
        }
    }

    public void release() {
//        清空数据引用
        clearAdapter(true, true, true);
        if (recorder != null) {
            recorder.clearAll();
        }
    }

    @Override
    public void onDismiss() {
        if (triggerView == null) return;
        ((TextView) triggerView).setTextColor(ContextCompat.getColor(mContext, R.color
                .standard_text_gray));
        ((TextView) triggerView).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable
                        .filter_arrow_down,
                0);
    }
}
