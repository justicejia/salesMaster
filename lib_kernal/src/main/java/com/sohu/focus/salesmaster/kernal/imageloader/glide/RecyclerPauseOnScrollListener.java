package com.sohu.focus.salesmaster.kernal.imageloader.glide;

import android.support.v7.widget.RecyclerView;
import android.widget.AbsListView;

import com.bumptech.glide.RequestManager;

/**
 * Created by qiangzhao on 2016/11/28.
 */

public class RecyclerPauseOnScrollListener extends RecyclerView.OnScrollListener {

    private RequestManager manager;

    private final boolean pauseOnScroll;
    private final boolean pauseOnFling;
    private final RecyclerView.OnScrollListener externalListener;

    /**
     * Constructor
     *
     * @param requestManager   {@linkplain RequestManager} instance for controlling
     * @param pauseOnScroll Whether {@linkplain RequestManager#pauseRequests()}  pause ImageLoader} during touch scrolling
     * @param pauseOnFling  Whether {@linkplain RequestManager#pauseRequests()}  pause ImageLoader} during fling
     */
    public RecyclerPauseOnScrollListener(RequestManager requestManager, boolean pauseOnScroll, boolean pauseOnFling) {
        this(requestManager, pauseOnScroll, pauseOnFling, null);
    }

    /**
     * Constructor
     *
     * @param requestManager   {@linkplain RequestManager} instance for controlling
     * @param pauseOnScroll Whether {@linkplain RequestManager#pauseRequests()}  pause ImageLoader} during touch scrolling
     * @param pauseOnFling  Whether {@linkplain RequestManager#pauseRequests()}  pause ImageLoader} during fling
     * @param customListener Your custom {@link RecyclerView.OnScrollListener} for {@linkplain RecyclerView list view} which also
     *                       will be get scroll events
     */
    public RecyclerPauseOnScrollListener(RequestManager requestManager, boolean pauseOnScroll, boolean pauseOnFling,
                                         RecyclerView.OnScrollListener customListener) {
        this.manager = requestManager;
        this.pauseOnScroll = pauseOnScroll;
        this.pauseOnFling = pauseOnFling;
        externalListener = customListener;
    }

    @Override
    public void onScrollStateChanged(RecyclerView view, int scrollState) {
        if(manager != null) {
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
    public void onScrolled(RecyclerView view, int dx, int dy) {
        if (externalListener != null) {
            externalListener.onScrolled(view, dx, dy);
        }
    }
}
