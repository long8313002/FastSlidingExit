package com.focus.slidingexit;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzheng on 2017/3/28.
 */

class OnRootViewChangeListener implements HoldArrayList.OnRootViewChange, IRootViewInfo {

    private List<View> rootViews;
    private List<SwipeFrameLayout> swipeFrameLayouts;
    private ICacheSwipeLayout cacheSwipeManager;
    private ISwipeFrameManager swipeFrameManager;

    public OnRootViewChangeListener() {
        this.swipeFrameLayouts = new ArrayList<>();
        cacheSwipeManager = CacheSwipeLayoutManager.getCacheManagerImp();
        swipeFrameManager =  SwipeFrameManager.getInstance();
    }

    @Override
    public void onChange(List<View> rootViews) {
        this.rootViews = rootViews;
        swipeFrameLayouts.clear();
        for (View view : rootViews) {
            if (!(view instanceof FrameLayout)) {
                continue;
            }
            FrameLayout dectorView = (FrameLayout) view;
            SwipeFrameLayout injectSwipe = swipeFrameManager.getInjectSwipe(dectorView);
            if (injectSwipe != null) {
                swipeFrameLayouts.add(injectSwipe);
                continue;
            }
            injectSwipe = swipeFrameManager.injectToDectorView((FrameLayout) view, this);
            swipeFrameLayouts.add(injectSwipe);
        }
        cacheSwipeBitmap();
    }

    private void cacheSwipeBitmap() {
        SwipeFrameLayout preSwipLayout = getPreSwipeFrameLayout();
        if (preSwipLayout != null) {
            cacheSwipeManager.cacheLayout(preSwipLayout);
        }
    }

    @Override
    public boolean canSwip() {
        return rootViews != null && rootViews.size() > 1;
    }

    @Override
    public List<View> getRootViews() {
        return rootViews;
    }

    @Override
    public SwipeFrameLayout getPreSwipeFrameLayout() {
        if (swipeFrameLayouts.size() > 1) {
            return swipeFrameLayouts.get(swipeFrameLayouts.size() - 2);
        }
        return null;
    }

    @Override
    public Bitmap getCacheBitmap() {
        SwipeFrameLayout preSwipLayout = getPreSwipeFrameLayout();
        return cacheSwipeManager.getCacheBitmap(preSwipLayout);
    }
}
