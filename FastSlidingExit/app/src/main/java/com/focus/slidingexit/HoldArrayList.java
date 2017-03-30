package com.focus.slidingexit;

import android.app.Activity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzheng on 2017/3/28.
 */

 class HoldArrayList extends ArrayList<View> {

    public interface OnRootViewChange {
        void onChange(List<View> rootViews);
    }

    private OnRootViewChange onRootViewChange;
    private List<View> activityRootViews;

    public HoldArrayList(OnRootViewChange onRootViewChange) {
        this.onRootViewChange = onRootViewChange;
        this.activityRootViews = new ArrayList<>();
    }

    @Override
    public boolean add(View t) {
        boolean add = super.add(t);
        onChange();
        return add;
    }

    @Override
    public boolean remove(Object o) {
        boolean remove = super.remove(o);
        onChange();
        return remove;
    }

    @Override
    public View remove(int index) {
        View remove = super.remove(index);
        onChange();
        return remove;
    }

    private void onChange() {
        activityRootViews.clear();
        for (View view : this) {
            if (view.getContext() instanceof Activity) {
                activityRootViews.add(view);
            }
        }
        if (onRootViewChange != null) {
            onRootViewChange.onChange(activityRootViews);
        }
    }
}
