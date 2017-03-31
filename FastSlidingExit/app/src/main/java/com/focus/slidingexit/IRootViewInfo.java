package com.focus.slidingexit;

import android.graphics.Bitmap;
import android.view.View;

import java.util.List;

/**
 * Created by zhangzheng on 2017/3/28.
 */

 interface IRootViewInfo {

    boolean canSwip();

    List<View> getRootViews();

    SwipeFrameLayout getFilterPreSwipeFrameLayout();

    SwipeFrameLayout getPreSwipeFrameLayout();

    Bitmap getCacheBitmap(SwipeFrameLayout preSwipe);
}
