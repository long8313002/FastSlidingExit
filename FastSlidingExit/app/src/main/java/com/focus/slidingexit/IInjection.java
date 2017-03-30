package com.focus.slidingexit;

import android.view.View;

import java.util.List;

/**
 * Created by zhangzheng on 2017/3/30.
 */

public interface IInjection {

    public interface OnRootViewsChangeLis{
        void onChange(List<View> rootViews);
    }

    void injection(OnRootViewsChangeLis onRootViewsChangeLis);
}
