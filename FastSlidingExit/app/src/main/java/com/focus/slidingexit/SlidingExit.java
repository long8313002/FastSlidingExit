package com.focus.slidingexit;

import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by zhangzheng on 2017/3/28.
 */

public class SlidingExit {

    private static final String TAG = "SlidingExit";

    public static void injection() {
        try {
            Class<?> windowManagerGlobal = Class.forName("android.view.WindowManagerGlobal");
            Method getInstance = windowManagerGlobal.getDeclaredMethod("getInstance");
            getInstance.setAccessible(true);
            Object invoke = getInstance.invoke(null);

            Field mViews = windowManagerGlobal.getDeclaredField("mViews");
            mViews.setAccessible(true);
            mViews.set(invoke, new HoldArrayList(new OnRootViewChangeListener()));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
}
