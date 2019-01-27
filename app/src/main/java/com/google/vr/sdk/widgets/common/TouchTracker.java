//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.google.vr.sdk.widgets.common;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;

public class TouchTracker implements OnTouchListener {
    private final TouchEnabledVrView target;
    private PointF lastTouchPointPx = new PointF();
    private PointF startTouchPointPx = new PointF();
    private boolean isYawing;
    private final float scrollSlopPx;
    private boolean touchTrackingEnabled = true;
    private boolean verticalTrackingEnabled = false;

    public TouchTracker(Context context, TouchEnabledVrView target) {
        this.target = target;
        this.scrollSlopPx = (float)ViewConfiguration.get(context).getScaledTouchSlop();
    }

    TouchTracker(TouchEnabledVrView target, float scrollSlopPx) {
        this.target = target;
        this.scrollSlopPx = scrollSlopPx;
    }

    void setVerticalTrackingEnabled(boolean enabled) {
        this.verticalTrackingEnabled = enabled;
    }

    void setTouchTrackingEnabled(boolean enabled) {
        this.touchTrackingEnabled = enabled;
    }

    public boolean onTouch(View view, MotionEvent event) {
        switch(event.getAction()) {
            case 0:
                this.startTouchPointPx.set(event.getX(), event.getY());
                this.lastTouchPointPx.set(event.getX(), event.getY());
                view.getParent().requestDisallowInterceptTouchEvent(true);
                this.isYawing = false;
                return true;
            case 1:
                if (!this.touchTrackingEnabled || Math.abs(event.getX() - this.startTouchPointPx.x) < this.scrollSlopPx && Math.abs(event.getY() - this.startTouchPointPx.y) < this.scrollSlopPx) {
                    this.target.getEventListener().onClick();
                }

                view.getParent().requestDisallowInterceptTouchEvent(false);
                return true;
            case 2:
                if (!this.touchTrackingEnabled) {
                    view.getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                } else {
                    if (!this.isYawing) {
                        if (!this.verticalTrackingEnabled && Math.abs(event.getY() - this.startTouchPointPx.y) > this.scrollSlopPx) {
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                            return false;
                        }

                        if (Math.abs(event.getX() - this.startTouchPointPx.x) > this.scrollSlopPx) {
                            this.isYawing = true;
                        }
                    }

                    this.target.onPanningEvent(event.getX() - this.lastTouchPointPx.x, this.verticalTrackingEnabled ? event.getY() - this.lastTouchPointPx.y : 0.0F);
                    this.lastTouchPointPx.set(event.getX(), event.getY());
                    return true;
                }
            default:
                return false;
        }
    }

    interface TouchEnabledVrView {
        void onPanningEvent(float var1, float var2);

        VrEventListener getEventListener();
    }
}
