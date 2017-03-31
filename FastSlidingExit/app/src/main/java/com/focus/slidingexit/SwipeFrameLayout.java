package com.focus.slidingexit;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.FrameLayout;


/**
 * Created by zhangzheng on 2017/3/28.
 */

class SwipeFrameLayout extends FrameLayout {

    private final int SCREEN_WIDTH = getScreenWidth(getContext());
    private float offset;
    private VelocityTracker velocity;
    private boolean isClose;
    private Drawable mShadowDrawable;
    private boolean isSwipe = true;
    private Rect rect = new Rect();
    private boolean isUserCacheBitmap = true;
    private IRootViewInfo rootViewInfo;

    public void setSwipe(boolean swipe) {
        isSwipe = swipe;
    }

    public boolean isUserCacheBitmap() {
        return isUserCacheBitmap;
    }

    public void setUserCacheBitmap(boolean userCacheBitmap) {
        isUserCacheBitmap = userCacheBitmap;
    }

    public SwipeFrameLayout(Context context, IRootViewInfo rootViewInfo) {
        super(context);
        this.rootViewInfo = rootViewInfo;
        setClickable(true);
        mShadowDrawable = ShadeDrawable.getShadeDrawable();
    }

    @Override
    protected void onDetachedFromWindow() {
        setOffset(SCREEN_WIDTH);
        super.onDetachedFromWindow();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        drawPrePagerBitmap(canvas);
        int save = canvas.save();
        canvas.translate(offset, 0);
        drawShadow(canvas);
        super.dispatchDraw(canvas);
        canvas.restoreToCount(save);
    }

    private void drawShadow(Canvas canvas) {
        mShadowDrawable.setBounds(-mShadowDrawable.getIntrinsicWidth(), 0, 0, getBottom());
        mShadowDrawable.draw(canvas);
    }

    private void drawPrePagerBitmap(Canvas canvas) {
        Bitmap cacheBitmap ;
        if(offset ==SCREEN_WIDTH){
            cacheBitmap = rootViewInfo.getCacheBitmap(rootViewInfo.getPreSwipeFrameLayout());
        }else{
            cacheBitmap = rootViewInfo.getCacheBitmap(rootViewInfo.getFilterPreSwipeFrameLayout());
        }
        if (cacheBitmap == null) {
            return;
        }
        int preoffset = (int) (-SCREEN_WIDTH / 2 + offset / 2);
        if (preoffset <= 0) {
            canvas.drawBitmap(cacheBitmap, preoffset, 0, null);
        }
    }

    private float downX;
    private float downY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isSwipe) {
            return false;
        }
        if (rootViewInfo != null && !rootViewInfo.canSwip()) {
            return false;
        }
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downX = ev.getRawX();
            downY = ev.getRawY();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            float ofsetX = ev.getRawX() - downX;
            float ofsetY = Math.abs(ev.getRawY() - downY);

            int criticalValue = ev.getRawX() < 50 ? 8 : 20;

            if (ofsetX > criticalValue && ofsetY < 50) {
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isSwipe) {
            return false;
        }
        if (rootViewInfo != null && !rootViewInfo.canSwip()) {
            return false;
        }
        if (velocity == null) {
            velocity = VelocityTracker.obtain();
        }
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            velocity.addMovement(ev);
            velocity.computeCurrentVelocity(1000);
            float offset = ev.getX() - downX;
            if (offset < 0) {
                offset = 0;
            }
            if (offset > SCREEN_WIDTH) {
                offset = SCREEN_WIDTH;
            }
            setOffset(offset);
        } else if (ev.getAction() == MotionEvent.ACTION_UP
                || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            cancleAnim(velocity.getXVelocity(), velocity.getYVelocity());
            velocity.recycle();
            velocity = null;
        }
        return super.onTouchEvent(ev);
    }

    private void cancleAnim(float XVelocity, float YVelocity) {
        final float endOffset = (offset > SCREEN_WIDTH / 2 || (XVelocity > 2000 && XVelocity > YVelocity * 1.5)) ? SCREEN_WIDTH : 0;
        isClose = endOffset == SCREEN_WIDTH;
        ObjectAnimator anim = ObjectAnimator.ofFloat(this, "Offset", offset, endOffset);
        anim.setDuration((long) (Math.abs(endOffset - offset) / 3));
        anim.start();
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isClose) {
                    getActivity().finish();
                    getActivity().overridePendingTransition(0, 0);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void setOffset(float offset) {
        if (offset == this.offset) {
            return;
        }
        this.offset = offset;
        invalidate();
    }

    private Activity getActivity() {
        return (Activity) getContext();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rect.set(0, 0, w, h);
    }

    private static int getScreenWidth(Context context) {
        Resources res = context.getResources();
        DisplayMetrics metrics = res.getDisplayMetrics();
        return metrics.widthPixels;
    }

}
