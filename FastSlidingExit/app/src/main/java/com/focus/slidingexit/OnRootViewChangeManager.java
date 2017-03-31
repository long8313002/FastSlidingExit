package com.focus.slidingexit;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzheng on 2017/3/28.
 */

class OnRootViewChangeManager implements IRootViewInfo, IInjection.OnRootViewsChangeLis {

    private List<View> rootViews;
    private List<SwipeFrameLayout> swipeFrameLayouts;
    private ICacheSwipeLayout cacheSwipeManager;
    private ISwipeFrameManager swipeFrameManager;

    public OnRootViewChangeManager() {
        this.swipeFrameLayouts = new ArrayList<>();
        cacheSwipeManager = CacheSwipeLayoutManager.getCacheManagerImp();
        swipeFrameManager = SwipeFrameManager.getInstance();
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
        Object allRootViews = WindowManagerGlobal.getInstance().getRootViews();
        boolean canSwip = this.rootViews != null && this.rootViews.size() > 1;
        if (allRootViews != null) {
            if (allRootViews instanceof View[] && ((View[]) allRootViews).length <= 1) {
                return false;
            }

            if (allRootViews instanceof ArrayList && ((ArrayList) allRootViews).size() <= 1) {
                return false;
            }
        }
        return canSwip;
    }

    @Override
    public List<View> getRootViews() {
        return rootViews;
    }

    @Override
    public SwipeFrameLayout getPreSwipeFrameLayout() {
        List<SwipeFrameLayout> remove = new ArrayList<>();
        for (SwipeFrameLayout swipeFrameLayout:swipeFrameLayouts){
            Activity activity = (Activity) swipeFrameLayout.getContext();
            if(activity.isFinishing()){
                remove.add(swipeFrameLayout);
            }
        }
        swipeFrameLayouts.removeAll(remove);
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
