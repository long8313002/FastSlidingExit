package com.focus.slidingexit;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.SparseArray;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by zhangzheng on 2017/3/30.
 */

public class CacheSwipeLayoutImp implements ICacheSwipeLayout {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private SparseArray<Bitmap> cacheBitmap = new SparseArray<>();

    private Future<?> submit;

    @Override
    public void cacheLayout(final SwipeFrameLayout layout) {
        if (!layout.isUserCacheBitmap()) {
            return;
        }
        if (submit != null && !submit.isCancelled()) {
            submit.cancel(true);
            submit = null;
        }
        if (cacheBitmap.size() > 0) {
            cacheBitmap(cacheBitmap.valueAt(0), layout);
            return;
        }
        submit = executorService.submit(new Runnable() {
            @Override
            public void run() {
                int width = layout.getMeasuredWidth();
                int height = layout.getMeasuredHeight();
                if (width <= 0 || height <= 0) {
                    return;
                }
                Bitmap bitmap = null;
                if (cacheBitmap.size() > 0) {
                    bitmap = cacheBitmap.valueAt(0);
                    if (bitmap.getWidth() <= 0 || bitmap.getHeight() <= 0 || bitmap.isRecycled()) {
                        bitmap = null;
                    }
                    cacheBitmap.clear();
                }
                if (bitmap == null) {
                    bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                }
                cacheBitmap(bitmap, layout);
            }
        });
    }

    private void cacheBitmap(Bitmap bitmap, SwipeFrameLayout layout) {
        Canvas canvas = new CacheCanvas(bitmap);
        canvas.clipRect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        canvas.drawColor(Color.WHITE);
        layout.dispatchDraw(canvas);
        cacheBitmap.put(layout.hashCode(), bitmap);
    }

    @Override
    public Bitmap getCacheBitmap(SwipeFrameLayout layout) {
        if (layout == null || !layout.isUserCacheBitmap()) {
            return null;
        }
        Bitmap bitmap = cacheBitmap.get(layout.hashCode());
        if (bitmap == null) {
            return null;
        }
        if (bitmap.getWidth() <= 0 || bitmap.getHeight() <= 0 || bitmap.isRecycled()) {
            bitmap = null;
        }
        return bitmap;
    }

    private class CacheCanvas extends Canvas {

        private CacheCanvas(Bitmap bitmap) {
            super(bitmap);
        }

        @Override
        public boolean quickReject(float left, float top, float right, float bottom, EdgeType type) {
            return false;
        }

        @Override
        public boolean quickReject(RectF rect, EdgeType type) {
            return false;
        }
    }
}