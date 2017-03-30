package com.focus.slidingexit;

/**
 * Created by zhangzheng on 2017/3/28.
 */

public class SlidingExit {

    public static final String TAG = "SlidingExit";

    private static SlidingExit slidingExit = new SlidingExit();


    private IInjection injection;
    private IInjection.OnRootViewsChangeLis rootViewChangeManager;

    private SlidingExit(){
        rootViewChangeManager = new OnRootViewChangeManager();
        WindowManagerGlobal managerGlobal = WindowManagerGlobal.getInstance();
        if(managerGlobal.rootViewIsList()){
            injection = new OtherPhoneInject();
        }else if(managerGlobal.rootViewIsArray()){
            injection = new SamsungPhoneInject();
        }
    }

    private void injectionImp(){
        if(injection !=null){
            injection.injection(rootViewChangeManager);
        }
    }


    public static void injection() {
        slidingExit.injectionImp();
    }
}
