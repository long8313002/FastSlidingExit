package com.focus.slidingexit;

import android.graphics.Bitmap;

/**
 * Created by zhangzheng on 2017/3/28.
 */

 interface ICacheSwipeLayout {

    public void cacheLayout(SwipeFrameLayout layout);

    public Bitmap getCacheBitmap(SwipeFrameLayout layout);
}
