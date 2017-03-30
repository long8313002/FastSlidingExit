package com.focus.slidingexit;

import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzheng on 2017/3/28.
 */

class SwipeFrameManager implements ISwipeFrameManager{

    private static ISwipeFrameManager manager = new SwipeFrameManager();

    public static ISwipeFrameManager getInstance(){
        return manager;
    }

    private SwipeFrameManager(){}

    @Override
    public SwipeFrameLayout injectToDectorView(FrameLayout decorView, IRootViewInfo rootViewInfo) {
        List<View> childViews = new ArrayList<>();
        SwipeFrameLayout swipeFrameLayout = new SwipeFrameLayout(decorView.getContext(), rootViewInfo);
        for (int index = 0; index < decorView.getChildCount(); index++) {
            childViews.add(decorView.getChildAt(index));
        }
        for (View child : childViews) {
            decorView.removeView(child);
            swipeFrameLayout.addView(child);
        }
        decorView.addView(swipeFrameLayout);
        return swipeFrameLayout;
    }

    @Override
    public SwipeFrameLayout getInjectSwipe(FrameLayout decorView) {
        if (decorView.getChildCount() != 1) {
            return null;
        }
        View childView = decorView.getChildAt(0);
        if(childView instanceof SwipeFrameLayout){
            return (SwipeFrameLayout) childView;
        }
        return null;
    }
}
