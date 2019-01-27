//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.google.vr.sdk.widgets.common;

import android.content.Context;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

class ViewRotator {
    private static final int ORIENTATION_DELTA_THRESHOLD_DEGREES = 70;
    private final View view;
    private int currentViewOrientation90Inc;
    private final int initialRotationDegrees;
    private int originalViewWidth;
    private int originalViewHeight;
    private OrientationEventListener orientationEventListener;

    public ViewRotator(Context context, View view, int initialRotationDegrees, final boolean trackingSensorsAvailable) {
        if (!isViewProperlyConfigured(view)) {
            throw new IllegalArgumentException("View should have MATCH_PARENT layout and no translation.");
        } else {
            if (initialRotationDegrees < 180) {
                this.initialRotationDegrees = initialRotationDegrees;
            } else {
                this.initialRotationDegrees = initialRotationDegrees - 180;
            }

            this.view = view;
            this.orientationEventListener = new OrientationEventListener(context) {
                public void onOrientationChanged(int orientation) {
                    if (trackingSensorsAvailable) {
                        if (orientation != -1) {
                            orientation += ViewRotator.this.initialRotationDegrees;
                            if (orientation > 180) {
                                orientation -= 360;
                            }

                            int orientationDelta = orientation - ViewRotator.this.currentViewOrientation90Inc;
                            if (orientationDelta > 180) {
                                orientationDelta = 360 - orientationDelta;
                            }

                            if (orientationDelta < -180) {
                                orientationDelta += 360;
                            }

                            if (Math.abs(orientationDelta) > 70) {
                                ViewRotator.this.rotateView(orientation);
                            }

                        }
                    }
                }
            };
        }
    }

    public void enable() {
        this.orientationEventListener.enable();
    }

    public void disable() {
        this.orientationEventListener.disable();
        LayoutParams layoutParams = this.view.getLayoutParams();
        layoutParams.height = -1;
        layoutParams.width = -1;
        this.view.setTranslationY(0.0F);
        this.view.setTranslationX(0.0F);
        this.view.setRotation(0.0F);
        this.originalViewWidth = 0;
        this.originalViewHeight = 0;
    }

    private void rotateView(int newPhoneOrientation) {
        if (this.view.getParent() != null) {
            if (this.originalViewWidth == 0 || this.originalViewHeight == 0) {
                this.originalViewWidth = this.view.getWidth();
                this.originalViewHeight = this.view.getHeight();
                if (this.originalViewWidth == 0 || this.originalViewHeight == 0) {
                    return;
                }
            }

            this.currentViewOrientation90Inc = getNearestOrientationWith90Inc(newPhoneOrientation);
            this.view.setRotation((float)(-this.currentViewOrientation90Inc));
            LayoutParams layoutParams = this.view.getLayoutParams();
            if (this.currentViewOrientation90Inc % 180 != 0) {
                layoutParams.height = this.originalViewWidth;
                layoutParams.width = this.originalViewHeight;
                this.view.setTranslationX((float)((this.originalViewWidth - this.originalViewHeight) / 2));
                this.view.setTranslationY((float)((this.originalViewHeight - this.originalViewWidth) / 2));
            } else {
                layoutParams.height = this.originalViewHeight;
                layoutParams.width = this.originalViewWidth;
                this.view.setTranslationY(0.0F);
                this.view.setTranslationX(0.0F);
            }

            this.view.requestLayout();
        }
    }

    static int getNearestOrientationWith90Inc(int orientation) {
        double orientationSign = (double)Math.signum((float)orientation);
        return (int)(orientationSign * (double)Math.round((double)Math.abs(orientation) / 90.0D) * 90.0D);
    }

    private static boolean isViewProperlyConfigured(View view) {
        LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null && (layoutParams.height != -1 || layoutParams.width != -1)) {
            return false;
        } else {
            return view.getTranslationX() == 0.0F && view.getTranslationY() == 0.0F;
        }
    }
}
