package com.focus.slidingexit;

import android.app.Application;

/**
 * Created by zhangzheng on 2017/3/28.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SlidingExit.injection();
    }
}
