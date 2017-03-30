package com.focus.slidingexit;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzheng on 2017/3/30.
 */

public class SamsungPhoneInject implements IInjection {

    private List<View> rootViewList = new ArrayList<>();
    private String preRootViewSnapshotInfo;

    @Override
    public void injection(final OnRootViewsChangeLis onRootViewsChangeLis) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    SystemClock.sleep(500);
                    View[] rootViews = (View[]) WindowManagerGlobal.getInstance().getRootViews();
                    rootViewList.clear();
                    for (View view : rootViews) {
                        rootViewList.add(view);
                    }
                    filterAndNotify(onRootViewsChangeLis, rootViewList);
                }
            }
        }).start();
    }

    private void filterAndNotify(final OnRootViewsChangeLis onRootViewsChangeLis, final List<View> rootViewList) {
        String snapshotInfo = getSnapshotInfo(rootViewList);
        if (snapshotInfo.equals(preRootViewSnapshotInfo)) {
            return;
        }
        this.preRootViewSnapshotInfo = snapshotInfo;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                onRootViewsChangeLis.onChange(rootViewList);
            }
        });
    }

    private String getSnapshotInfo(List<View> rootViewList) {
        if (rootViewList == null) {
            return "";
        }
        StringBuffer info = new StringBuffer();
        for (View view : rootViewList) {
            info.append(view.hashCode() + "");
        }
        return info.toString();
    }
}
