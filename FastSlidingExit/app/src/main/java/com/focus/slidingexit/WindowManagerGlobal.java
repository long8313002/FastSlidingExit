package com.focus.slidingexit;

import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by zhangzheng on 2017/3/30.
 */

 class WindowManagerGlobal {

    private Object windowGlobal;
    private Field mViews;
    private static WindowManagerGlobal windowManagerGlobal;

    public static WindowManagerGlobal getInstance() {
        if(windowManagerGlobal == null){
            windowManagerGlobal = new WindowManagerGlobal();
        }
        return windowManagerGlobal;
    }

    private  WindowManagerGlobal() {
        try {
            Class<?> windowManagerGlobal = Class.forName("android.view.WindowManagerGlobal");
            Method getInstance = windowManagerGlobal.getDeclaredMethod("getInstance");
            getInstance.setAccessible(true);
            windowGlobal = getInstance.invoke(null);
            mViews = windowManagerGlobal.getDeclaredField("mViews");
            mViews.setAccessible(true);
        } catch (Exception e) {
        }
    }

    public boolean rootViewIsArray(){
        return mViews.getType() == View[].class;
    }

    public boolean rootViewIsList(){
        return mViews.getType() == ArrayList.class;
    }

    public Object getRootViews(){
        try {
           return mViews.get(windowGlobal);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
