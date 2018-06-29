package com.sohu.focus.salesmaster.newFilter;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.base.BasePresenter;
import com.sohu.focus.salesmaster.newFilter.interview.IFilterInterface;
import com.sohu.focus.salesmaster.newFilter.model.CommonFilterItemModel;
import com.sohu.focus.salesmaster.sheets.view.HomeSheetFragment;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.EasyRecyclerView;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.decoration.DividerDecoration;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jiayuanmin on 2018/5/18
 * description:
 */
public class FilterHelper extends BasePresenter<IFilterInterface> {

    private Context mContext;
    private PopupWindow mPopWindow;
    private View mContentView;
    private EasyRecyclerView filterList;
    private RecyclerArrayAdapter<CommonFilterItemModel> mAdapter;
    private ImageView arrowView;
    private TextView typeText;
    private AlphaAnimation mAlphaAnimation;
    private TranslateAnimation mTranslateAnimation;
    private List<CommonFilterItemModel> mCityList = new ArrayList<>();
    private List<CommonFilterItemModel> mRoleList = new ArrayList<>();

    public FilterHelper(Context context) {
        mContext = context;
        mContentView = LayoutInflater.from(context).inflate(R.layout.popup_filter, null);
        initRecyclerView();
        initPopUpWindow();
    }

    public void setData(@FilterType int type, List<CommonFilterItemModel> data) {
        switch (type) {
            case FilterType.CITY:
                mCityList = data;
                break;
            case FilterType.ROLE:
                mRoleList = data;
                break;
            case FilterType.ORDER:
            case FilterType.PROGRESS:
                break;

        }
    }

    private void initRecyclerView() {
        filterList = mContentView.findViewById(R.id.filter_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        filterList.setLayoutManager(layoutManager);
        filterList.addItemDecoration(new DividerDecoration(ContextCompat.getColor(mContext, R.color.standard_line), 2));
        mAdapter = new RecyclerArrayAdapter<CommonFilterItemModel>(mContext) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new FilterHolder(parent);
            }
        };
        mAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                for (CommonFilterItemModel itemModel : mAdapter.getAllData()) {
                    itemModel.setSelected(false);
                }
                mAdapter.getAllData().get(position).setSelected(true);
                if (isAttached()) {
                    getView().onSelectFilterItem(mAdapter.getItem(position));
                }
                mPopWindow.dismiss();
            }
        });
        filterList.setAdapter(mAdapter);

    }

    private void initPopUpWindow() {
        mPopWindow = new PopupWindow(mContentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mContentView.findViewById(R.id.filter_pop_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (typeText != null && arrowView != null) {
                    typeText.setTextColor(ContextCompat.getColor(mContext, R.color.standard_text_light_black));
                    arrowView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.filter_arrow_down));
                }
            }
        });
        mAlphaAnimation = new AlphaAnimation(0, 1);
        mAlphaAnimation.setDuration(300);
        mTranslateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, -1,
                Animation.RELATIVE_TO_SELF, 0);
        mTranslateAnimation.setDuration(300);
    }

    /**
     * <p>
     * e.g {@link HomeSheetFragment}
     * <p>
     *
     * @param filterType FilterType
     * @param anchorView 筛选框出现再哪个View下面
     * @param textView   筛选文本
     * @param arrow      筛选文本右侧的箭头
     */
    public void showOrHidePopup(@FilterType int filterType, View anchorView, TextView textView, ImageView arrow) {
        if (mPopWindow.isShowing()) {
            mPopWindow.dismiss();
            return;
        }
        typeText = textView;
        arrowView = arrow;
        typeText.setTextColor(ContextCompat.getColor(mContext, R.color.home_icon_selected));
        arrow.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.filter_arrow_up));
        mAdapter.clear();
        switch (filterType) {
            case FilterType.CITY:
                mAdapter.addAll(mCityList);
                if (Build.VERSION.SDK_INT != 24) {
                    mPopWindow.showAsDropDown(anchorView);
                } else {
                    int[] location = new int[2];  // 获取控件在屏幕的位置
                    anchorView.getLocationOnScreen(location);
                    mPopWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, location[0], location[1] + anchorView.getHeight());
                }
                break;
            case FilterType.ROLE:
                mAdapter.addAll(mRoleList);
                if (Build.VERSION.SDK_INT != 24) {
                    mPopWindow.showAsDropDown(anchorView);
                } else {
                    int[] location = new int[2];  // 获取控件在屏幕的位置
                    anchorView.getLocationOnScreen(location);
                    mPopWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, location[0], location[1] + anchorView.getHeight());
                }
                break;
        }
        slideFromTop();
    }

    private void slideFromTop() {
        mPopWindow.getContentView().startAnimation(mAlphaAnimation);
        filterList.startAnimation(mTranslateAnimation);
    }


    @Override
    public void release() {
        detach();
    }

}
