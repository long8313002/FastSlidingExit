package com.focus.slidingexit;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
/**
 * Created by zhangzheng on 2017/3/30.
 */

public class CacheSwipeLayoutImp implements ICacheSwipeLayout {

    private SparseArray<Bitmap> cacheBitmap = new SparseArray<>();


    @Override
    public void cacheLayout(final SwipeFrameLayout layout) {
        if (!layout.isUserCacheBitmap()) {
            return;
        }
        if (cacheBitmap.size() > 0) {
            cacheBitmap(cacheBitmap.valueAt(0), layout);
            return;
        }

        new Thread(new Runnable() {
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
                }
                if (bitmap == null) {
                    bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                }
                cacheBitmap(bitmap, layout);
            }
        }).start();
    }

    private void cacheBitmap(final Bitmap bitmap, final SwipeFrameLayout layout) {
        if(Looper.getMainLooper()!=Looper.myLooper()){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    cacheBitmap(bitmap,layout);
                }
            });
            return;
        }
        Canvas canvas = new CacheCanvas(bitmap);
        canvas.clipRect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        canvas.drawColor(Color.WHITE);
        cacheBitmap.clear();
        cacheBitmap.put(layout.hashCode(), bitmap);
        layout.dispatchDraw(canvas);
    }

    @Override
    public Bitmap getCacheBitmap(SwipeFrameLayout layout) {
        if (layout == null || !layout.isUserCacheBitmap()) {
            return null;
        }
        Bitmap bitmap = cacheBitmap.get(layout.hashCode());
        if (bitmap == null) {
            if(cacheBitmap.size()>0){
                 cacheBitmap(cacheBitmap.valueAt(0),layout);
            }
            return cacheBitmap.get(layout.hashCode());
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
