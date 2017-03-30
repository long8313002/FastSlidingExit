package com.focus.slidingexit;

import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by zhangzheng on 2017/3/30.
 */

public class OtherPhoneInject implements IInjection {

    @Override
    public void injection(final OnRootViewsChangeLis onRootViewsChangeLis) {
        try {
            Class<?> windowManagerGlobal = Class.forName("android.view.WindowManagerGlobal");
            Method getInstance = windowManagerGlobal.getDeclaredMethod("getInstance");
            getInstance.setAccessible(true);
            Object invoke = getInstance.invoke(null);

            Field mViews = windowManagerGlobal.getDeclaredField("mViews");
            mViews.setAccessible(true);
            mViews.set(invoke, new HoldArrayList(new HoldArrayList.OnRootViewChange() {
                @Override
                public void onChange(List<View> rootViews) {
                    onRootViewsChangeLis.onChange(rootViews);
                }
            }));
        } catch (Exception e) {
            Log.e(SlidingExit.TAG, e.toString());
        }
    }
}
