package com.sohu.focus.salesmaster.uiframe.easyrecyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorRes;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.kernal.log.Logger;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.RecyclerArrayAdapter;


import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.swipe.SwipeRefreshLayout;


public class EasyRecyclerView extends FrameLayout {
    public static final String TAG = "EasyRecyclerView";
    public static final boolean DEBUG = false;
    protected FixedRecyclerView mRecycler;
    protected ViewGroup mProgressView;
    protected ViewGroup mEmptyView;
    protected ViewGroup mErrorView;
    protected boolean mClipToPadding;
    protected int mPadding;
    protected int mPaddingTop;
    protected int mPaddingBottom;
    protected int mPaddingLeft;
    protected int mPaddingRight;
    protected int mScrollbarStyle;
    protected int mOverSrollMode;
    protected int mScrollbar;
    protected RecyclerView.OnScrollListener mInternalOnScrollListener;
    protected RecyclerView.OnScrollListener mExternalOnScrollListener;
    protected SwipeRefreshLayout mPtrLayout;
    protected android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener mRefreshListener;
    protected onShowEmptyListener mShowEmptyListener;
    private int mProgressId;
    private int mEmptyId;
    private int mErrorId;
    private boolean isDisableAnimation = false;
    private View mTopView;

    public EasyRecyclerView(Context context) {
        super(context);
        initView();
    }

    public EasyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        initView();
    }

    public EasyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(attrs);
        initView();
    }

    private static void log(String content) {
        if (DEBUG)
            Logger.ZQ().v(TAG, content);
    }

    public SwipeRefreshLayout getSwipeToRefresh() {
        return mPtrLayout;
    }

    public FixedRecyclerView getRecyclerView() {
        return mRecycler;
    }

    protected void initAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.EasyRecyclerView);
        try {
            mClipToPadding = a.getBoolean(R.styleable.EasyRecyclerView_recyclerClipToPadding, false);
            mPadding = (int) a.getDimension(R.styleable.EasyRecyclerView_recyclerPadding, -1.0f);
            mPaddingTop = (int) a.getDimension(R.styleable.EasyRecyclerView_recyclerPaddingTop, 0.0f);
            mPaddingBottom = (int) a.getDimension(R.styleable.EasyRecyclerView_recyclerPaddingBottom, 0.0f);
            mPaddingLeft = (int) a.getDimension(R.styleable.EasyRecyclerView_recyclerPaddingLeft, 0.0f);
            mPaddingRight = (int) a.getDimension(R.styleable.EasyRecyclerView_recyclerPaddingRight, 0.0f);
            mScrollbarStyle = a.getInteger(R.styleable.EasyRecyclerView_scrollbarStyle, -1);
            mScrollbar = a.getInteger(R.styleable.EasyRecyclerView_scrollbars, -1);
            mOverSrollMode = a.getInteger(R.styleable.EasyRecyclerView_overScrollMode, -1);

            mEmptyId = a.getResourceId(R.styleable.EasyRecyclerView_layout_empty, 0);
            mProgressId = a.getResourceId(R.styleable.EasyRecyclerView_layout_progress, 0);
            mErrorId = a.getResourceId(R.styleable.EasyRecyclerView_layout_error, 0);
        } finally {
            a.recycle();
        }
    }

    private void initView() {
        if (isInEditMode()) {
            return;
        }
        //生成主View
        View v = LayoutInflater.from(getContext()).inflate(R.layout.ui_framework_progress_recyclerview, this);
        mPtrLayout = (SwipeRefreshLayout) v.findViewById(R.id.ptr_layout);
        mPtrLayout.setEnabled(false);

        mProgressView = (ViewGroup) v.findViewById(R.id.progress);
        if (mProgressId != 0) LayoutInflater.from(getContext()).inflate(mProgressId, mProgressView);
        mEmptyView = (ViewGroup) v.findViewById(R.id.empty);
        if (mEmptyId != 0) LayoutInflater.from(getContext()).inflate(mEmptyId, mEmptyView);
        mErrorView = (ViewGroup) v.findViewById(R.id.error);
        if (mErrorId != 0) LayoutInflater.from(getContext()).inflate(mErrorId, mErrorView);
        initRecyclerView(v);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mPtrLayout.dispatchTouchEvent(ev);
    }

    /**
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setRecyclerPadding(int left, int top, int right, int bottom) {
        this.mPaddingLeft = left;
        this.mPaddingTop = top;
        this.mPaddingRight = right;
        this.mPaddingBottom = bottom;
        mRecycler.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
    }

    public void setClipToPadding(boolean isClip) {
        mRecycler.setClipToPadding(isClip);
    }

    public void setEmptyView(View emptyView) {
        mEmptyView.removeAllViews();
        mEmptyView.addView(emptyView);
    }

    public void setProgressView(View progressView) {
        mProgressView.removeAllViews();
        mProgressView.addView(progressView);
    }

    public void setErrorView(View errorView) {
        mErrorView.removeAllViews();
        mErrorView.addView(errorView);
    }

    public void scrollToPosition(int position) {
        getRecyclerView().scrollToPosition(position);
    }

    public void disableAnimation() {
        isDisableAnimation = true;
    }

    /**
     * Implement this method to customize the AbsListView
     */
    protected void initRecyclerView(View view) {
        mRecycler = (FixedRecyclerView) view.findViewById(android.R.id.list);
        if (isDisableAnimation) {
            ((SimpleItemAnimator) mRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
        }
        setItemAnimator(null);
        if (mRecycler != null) {
            mRecycler.setHasFixedSize(true);
            mRecycler.setClipToPadding(mClipToPadding);
            mInternalOnScrollListener = new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (mExternalOnScrollListener != null)
                        mExternalOnScrollListener.onScrolled(recyclerView, dx, dy);

                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (mExternalOnScrollListener != null)
                        mExternalOnScrollListener.onScrollStateChanged(recyclerView, newState);

                }
            };
            mRecycler.addOnScrollListener(mInternalOnScrollListener);

            if (mPadding != -1.0f) {
                mRecycler.setPadding(mPadding, mPadding, mPadding, mPadding);
            } else {
                mRecycler.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
            }
            if (mScrollbarStyle != -1) {
                mRecycler.setScrollBarStyle(mScrollbarStyle);
            }
            if (mOverSrollMode != -1) {
                switch (mOverSrollMode) {
                    case 0:
                        mRecycler.setOverScrollMode(OVER_SCROLL_ALWAYS);
                        break;
                    case 1:
                        mRecycler.setOverScrollMode(OVER_SCROLL_NEVER);
                        break;
                    case 2:
                        mRecycler.setOverScrollMode(OVER_SCROLL_IF_CONTENT_SCROLLS);
                        break;
                }

            }
            switch (mScrollbar) {
                case 0:
                    setVerticalScrollBarEnabled(false);
                    break;
                case 1:
                    setHorizontalScrollBarEnabled(false);
                    break;
                case 2:
                    setVerticalScrollBarEnabled(false);
                    setHorizontalScrollBarEnabled(false);
                    break;
            }
        }
    }

    @Override
    public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled) {
        mRecycler.setVerticalScrollBarEnabled(verticalScrollBarEnabled);
    }

    @Override
    public void setHorizontalScrollBarEnabled(boolean horizontalScrollBarEnabled) {
        mRecycler.setHorizontalScrollBarEnabled(horizontalScrollBarEnabled);
    }

    /**
     * Set the layout manager to the recycler
     *
     * @param manager
     */
    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        mRecycler.setLayoutManager(manager);
    }

    /**
     * 设置适配器，关闭所有副view。展示进度条View
     * 适配器有更新，自动关闭所有副view。根据条数判断是否展示EmptyView
     *
     * @param adapter
     */
    public void setAdapterWithProgress(RecyclerView.Adapter adapter) {
        mRecycler.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new EasyDataObserver(this));
        //只有Adapter为空时才显示ProgressView
        if (adapter instanceof RecyclerArrayAdapter) {
            if (((RecyclerArrayAdapter) adapter).getCount() == 0) {
                showProgress();
            } else {
                showRecycler();
            }
        } else {
            if (adapter.getItemCount() == 0) {
                showProgress();
            } else {
                showRecycler();
            }
        }
    }

    /**
     * Remove the adapter from the recycler
     */
    public void clear() {
        mRecycler.setAdapter(null);
    }

    private void hideAll() {
        mEmptyView.setVisibility(View.GONE);
        mProgressView.setVisibility(View.GONE);
        mErrorView.setVisibility(GONE);
        mPtrLayout.setRefreshing(false);
        mRecycler.setVisibility(View.INVISIBLE);
    }

    public void showError() {
        log("showError");
        if (mErrorView.getChildCount() > 0) {
            hideAll();
            mErrorView.setVisibility(View.VISIBLE);
        } else {
            showRecycler();
        }

    }

    public void showEmpty() {
        log("showEmpty");
        if (mEmptyView.getChildCount() > 0) {
            hideAll();
            mEmptyView.setVisibility(View.VISIBLE);
            if (mShowEmptyListener != null)
                mShowEmptyListener.onShowEmpty();
        } else {
            showRecycler();
        }
    }

    public void showProgress() {
        log("showProgress");
        if (mProgressView.getChildCount() > 0) {
            hideAll();
            mProgressView.setVisibility(View.VISIBLE);
        } else {
            showRecycler();
        }
    }

    public void showRecycler() {
        log("showRecycler");
        hideAll();
        mRecycler.setVisibility(View.VISIBLE);
        if (mShowEmptyListener != null)
            mShowEmptyListener.onShowRecycler();
    }

    /**
     * Set the listener when refresh is triggered and enable the SwipeRefreshLayout
     *
     * @param listener
     */
    public void setRefreshListener(android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener listener) {
        mPtrLayout.setEnabled(true);
        mPtrLayout.setOnRefreshListener(listener);
        this.mRefreshListener = listener;
    }

    public void setRefreshing(final boolean isRefreshing) {
        mPtrLayout.post(new Runnable() {
            @Override
            public void run() {
                mPtrLayout.setRefreshing(isRefreshing);
            }
        });
    }

    public void setRefreshing(final boolean isRefreshing, final boolean isCallbackListener) {
        mPtrLayout.post(new Runnable() {
            @Override
            public void run() {
                mPtrLayout.setRefreshing(isRefreshing);
                if (isRefreshing && isCallbackListener && mRefreshListener != null) {
                    mRefreshListener.onRefresh();
                }
            }
        });
    }

    /**
     * Set the colors for the SwipeRefreshLayout states
     *
     * @param colRes
     */
    public void setRefreshingColorResources(@ColorRes int... colRes) {
        mPtrLayout.setColorSchemeResources(colRes);
    }

    /**
     * Set the colors for the SwipeRefreshLayout states
     *
     * @param col
     */
    public void setRefreshingColor(int... col) {
        mPtrLayout.setColorSchemeColors(col);
    }

    /**
     * Set the scroll listener for the recycler
     *
     * @param listener
     */
    public void setOnScrollListener(RecyclerView.OnScrollListener listener) {
        mExternalOnScrollListener = listener;
    }

    /**
     * Add the onItemTouchListener for the recycler
     *
     * @param listener
     */
    public void addOnItemTouchListener(RecyclerView.OnItemTouchListener listener) {
        mRecycler.addOnItemTouchListener(listener);
    }

    /**
     * Remove the onItemTouchListener for the recycler
     *
     * @param listener
     */
    public void removeOnItemTouchListener(RecyclerView.OnItemTouchListener listener) {
        mRecycler.removeOnItemTouchListener(listener);
    }

    /**
     * @return the recycler adapter
     */
    public RecyclerView.Adapter getAdapter() {
        return mRecycler.getAdapter();
    }

    /**
     * 设置适配器，关闭所有副view。展示recyclerView
     * 适配器有更新，自动关闭所有副view。根据条数判断是否展示EmptyView
     *
     * @param adapter
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecycler.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new EasyDataObserver(this));
        showRecycler();
    }

    public void setOnTouchListener(OnTouchListener listener) {
        mRecycler.setOnTouchListener(listener);
    }

    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        mRecycler.setItemAnimator(animator);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecycler.addItemDecoration(itemDecoration);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration, int index) {
        mRecycler.addItemDecoration(itemDecoration, index);
    }

    public void removeItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecycler.removeItemDecoration(itemDecoration);
    }

    /**
     * @return inflated error view or null
     */
    public View getErrorView() {
        if (mErrorView.getChildCount() > 0) return mErrorView.getChildAt(0);
        return null;
    }

    public void setErrorView(int errorView) {
        mErrorView.removeAllViews();
        LayoutInflater.from(getContext()).inflate(errorView, mErrorView);
    }

    /**
     * @return inflated progress view or null
     */
    public View getProgressView() {
        if (mProgressView.getChildCount() > 0) return mProgressView.getChildAt(0);
        return null;
    }

    public void setProgressView(int progressView) {
        mProgressView.removeAllViews();
        LayoutInflater.from(getContext()).inflate(progressView, mProgressView);
    }

    /**
     * @return inflated empty view or null
     */
    public View getEmptyView() {
        if (mEmptyView.getChildCount() > 0) return mEmptyView.getChildAt(0);
        return null;
    }

    public void setEmptyView(int emptyView) {
        mEmptyView.removeAllViews();
        LayoutInflater.from(getContext()).inflate(emptyView, mEmptyView);
    }

    public void setOnShowEmptyListener(onShowEmptyListener listener) {
        mShowEmptyListener = listener;
    }

    public interface onShowEmptyListener {
        /**
         * use this method when recycler view show empty view
         */
        void onShowEmpty();

        /**
         * use this method when recycler view show recycler view
         */
        void onShowRecycler();
    }

    public static class EasyDataObserver extends AdapterDataObserver {
        private EasyRecyclerView recyclerView;

        public EasyDataObserver(EasyRecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            update();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            update();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            update();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            update();
        }

        @Override
        public void onChanged() {
            super.onChanged();
            update();
        }

        //自动更改Container的样式
        private void update() {
            log("update");
            int count;
            if (recyclerView.getAdapter() instanceof RecyclerArrayAdapter) {
                count = ((RecyclerArrayAdapter) recyclerView.getAdapter()).getCount();
            } else {
                count = recyclerView.getAdapter().getItemCount();
            }
            if (count == 0) {
                log("no data:" + "show empty");
                recyclerView.showEmpty();
            } else {
                log("has data");
                recyclerView.showRecycler();
            }
        }
    }
}
