package com.focus.slidingexit;

import android.widget.FrameLayout;

/**
 * Created by zhangzheng on 2017/3/28.
 */

 interface ISwipeFrameManager {

    public SwipeFrameLayout injectToDectorView(FrameLayout decorView, IRootViewInfo rootViewInfo);

    public SwipeFrameLayout getInjectSwipe(FrameLayout decorView);
}
