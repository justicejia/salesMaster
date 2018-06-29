package com.sohu.focus.salesmaster.kernal.imageloader.glide;

import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListView;

import com.bumptech.glide.RequestManager;

/**
 * Listener-helper for {@linkplain AbsListView list views} ({@link ListView}, {@link GridView}) which can
 * {@linkplain RequestManager#pauseRequests()}  pause ImageLoader's tasks} while list view is scrolling (touch scrolling and/or
 * fling). It prevents redundant loadings.<br />
 * Set it to your list view's {@link AbsListView#setOnScrollListener(AbsListView.OnScrollListener) setOnScrollListener(...)}.<br />
 * This listener can wrap your custom {@linkplain AbsListView.OnScrollListener listener}.
 * <p>
 * Created by qiangzhao on 2016/11/29.
 */

public class ListPauseOnScrollListener implements AbsListView.OnScrollListener {

    private final boolean pauseOnScroll;
    private final boolean pauseOnFling;
    private final AbsListView.OnScrollListener externalListener;
    private RequestManager manager;

    /**
     * Constructor
     *
     * @param requestManager {@linkplain RequestManager} instance for controlling
     * @param pauseOnScroll  Whether {@linkplain RequestManager#pauseRequests()}  pause ImageLoader} during touch scrolling
     * @param pauseOnFling   Whether {@linkplain RequestManager#pauseRequests()}  pause ImageLoader} during fling
     */
    public ListPauseOnScrollListener(RequestManager requestManager, boolean pauseOnScroll, boolean pauseOnFling) {
        this(requestManager, pauseOnScroll, pauseOnFling, null);
    }

    /**
     * Constructor
     *
     * @param requestManager {@linkplain RequestManager} instance for controlling
     * @param pauseOnScroll  Whether {@linkplain RequestManager#pauseRequests()}  pause ImageLoader} during touch scrolling
     * @param pauseOnFling   Whether {@linkplain RequestManager#pauseRequests()}  pause ImageLoader} during fling
     * @param customListener Your custom {@link AbsListView.OnScrollListener} for {@linkplain AbsListView list view} which also
     *                       will be get scroll events
     */
    public ListPauseOnScrollListener(RequestManager requestManager, boolean pauseOnScroll, boolean pauseOnFling,
                                     AbsListView.OnScrollListener customListener) {
        this.manager = requestManager;
        this.pauseOnScroll = pauseOnScroll;
        this.pauseOnFling = pauseOnFling;
        externalListener = customListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (manager != null) {
            switch (scrollState) {
                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                    manager.resumeRequests();
                    break;
                case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    if (pauseOnScroll) {
                        manager.pauseRequests();
                    } else {
                        manager.resumeRequests();
                    }
                    break;
                case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                    if (pauseOnFling) {
                        manager.pauseRequests();
                    }
                    break;
            }
        }
        if (externalListener != null) {
            externalListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (externalListener != null) {
            externalListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }
}
