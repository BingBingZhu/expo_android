//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.google.vr.sdk.widgets.common;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.expo.R;
import com.google.vr.cardboard.AndroidNCompat;
import com.google.vr.cardboard.UiLayer;
import com.google.vr.cardboard.VrParamsProvider;
import com.google.vr.cardboard.VrParamsProviderFactory;
import com.google.vr.sdk.proto.nano.CardboardDevice.DeviceParams;
import com.google.vr.sdk.widgets.common.TouchTracker.TouchEnabledVrView;
import com.google.vr.sdk.widgets.common.VrWidgetRenderer.GLThreadScheduler;
import com.google.vr.widgets.common.R.id;
import com.google.vr.widgets.common.R.layout;
import com.google.vrtoolkit.cardboard.ScreenOnFlagHelper;

public abstract class VrWidgetView extends FrameLayout {
    private static final String TAG = VrWidgetView.class.getSimpleName();
    private static final boolean DEBUG = false;
    private static final int DEFAULT_DISPLAY_MODE = 1;
    private static final Uri INFO_BUTTON_URL = Uri.parse("https://g.co/vr/view");
    private static final String STATE_KEY_SUPER_CLASS = "superClassState";
    private static final String STATE_KEY_ORIENTATION_HELPER = "orientationHelperState";
    private static final String STATE_KEY_WIDGET_RENDERER = "widgetRendererState";
    private static final String STATE_KEY_DISPLAY_MODE = "displayMode";
    private static final float METERS_PER_INCH = 0.0254F;
    private VrWidgetRenderer renderer;
    private VrEventListener eventListener = new VrEventListener();
    private DisplayMetrics displayMetrics;
    private Activity activity;
    private boolean isPaused;
    private VrParamsProvider viewerParamsProvider;
    private ViewGroup innerWidgetView;
    private GLSurfaceView renderingView;
    private View uiView;
    private ImageButton enterStereoModeButton;
    private ImageButton enterFullscreenButton;
    private ImageButton fullscreenBackButton;
    private ImageButton infoButton;
    private boolean isStereoModeButtonEnabled;
    private boolean isFullscreenButtonEnabled;
    private boolean isTransitionViewEnabled;
    private boolean isTouchTrackingEnabled;
    private boolean isInfoButtonEnabled;
    public FullScreenDialog fullScreenDialog;
    private TrackingSensorsHelper sensorsHelper;
    private TouchTracker touchTracker;
    private ScreenOnFlagHelper screenOnFlagHelper;
    private OrientationHelper orientationHelper;
    private ViewRotator viewRotator;
    private int displayMode;
    UiLayer vrUiLayer;

    public VrWidgetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!this.isInEditMode()) {
            this.checkContextIsActivity(context);
            this.init();
        }
    }

    public VrWidgetView(Context context) {
        super(context);
        this.checkContextIsActivity(context);
        this.init();
    }

    protected void onDetachedFromWindow() {
        this.renderer.onViewDetach();
        super.onDetachedFromWindow();
    }

    private void checkContextIsActivity(Context context) {
        if (!(context instanceof Activity)) {
            throw new RuntimeException("Context must be an instance of activity");
        } else {
            this.activity = (Activity) context;
        }
    }

    private void init() {
        this.displayMode = 1;
        this.viewerParamsProvider = VrParamsProviderFactory.create(this.getContext());
        this.sensorsHelper = new TrackingSensorsHelper(this.getContext().getPackageManager());
        this.isStereoModeButtonEnabled = (this.sensorsHelper.areTrackingSensorsAvailable() || this.sensorsHelper.showStereoModeButtonForTesting()) ? true : DEBUG;
        this.isFullscreenButtonEnabled = true;
        this.isInfoButtonEnabled = true;
        this.isTouchTrackingEnabled = true;
        this.isTransitionViewEnabled = true;
        this.screenOnFlagHelper = new ScreenOnFlagHelper(this.activity);
        WindowManager windowManager = (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        this.displayMetrics = new DisplayMetrics();
        display.getRealMetrics(this.displayMetrics);
        this.initializeRenderingView(display.getRotation());
        this.innerWidgetView = new FrameLayout(this.getContext());
        this.innerWidgetView.setId(id.vrwidget_inner_view);
        this.innerWidgetView.addView(this.renderingView);
        this.setPadding(0, 0, 0, 0);
        this.addView(this.innerWidgetView);
        this.orientationHelper = new OrientationHelper(this.activity);
        this.fullScreenDialog = new FullScreenDialog(this.getContext(), this.innerWidgetView, this.renderer);
        this.fullScreenDialog.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                VrWidgetView.this.setDisplayMode(1);
            }
        });
        this.uiView = inflate(this.getContext(), layout.ui_view_embed, (ViewGroup) null);
        this.viewRotator = new ViewRotator(this.getContext(), this.uiView, this.getScreenRotationInDegrees(display.getRotation()), this.sensorsHelper.areTrackingSensorsAvailable());
        this.innerWidgetView.addView(this.uiView);
        this.innerWidgetView.addView(new View(this.getContext()));
        this.vrUiLayer = new UiLayer(this.getContext());
        this.vrUiLayer.setPortraitSupportEnabled(true);
        this.vrUiLayer.setEnabled(true);
        this.innerWidgetView.addView(this.vrUiLayer.getView());
        this.updateTouchTracker();
        this.initializeUiButtons();
    }

    private boolean isFullScreen() {
        return this.displayMode == 2 || this.displayMode == 3;
    }

    private void updateTouchTracker() {
        if (this.touchTracker == null) {
            this.touchTracker = new TouchTracker(this.getContext(), new TouchEnabledVrView() {
                public void onPanningEvent(float deltaPixelX, float deltaPixelY) {
                    VrWidgetView.this.renderer.onPanningEvent(deltaPixelX, deltaPixelY);
                }

                public VrEventListener getEventListener() {
                    return VrWidgetView.this.eventListener;
                }
            });
            this.setOnTouchListener(this.touchTracker);
        }

        this.touchTracker.setTouchTrackingEnabled(this.isTouchTrackingEnabled && this.displayMode != 3);
        this.touchTracker.setVerticalTrackingEnabled(this.isFullScreen());
    }

    private void initializeRenderingView(int rotation) {
        this.renderingView = new GLSurfaceView(this.getContext());
        this.renderingView.setEGLContextClientVersion(2);
        this.renderingView.setEGLConfigChooser(8, 8, 8, 8, 16, 8);
        this.renderingView.setPreserveEGLContextOnPause(true);
        float xMetersPerPixel = 0.0254F / this.displayMetrics.xdpi;
        float yMetersPerPixel = 0.0254F / this.displayMetrics.ydpi;
        GLThreadScheduler scheduler = new GLThreadScheduler() {
            public void queueGlThreadEvent(Runnable runnable) {
                VrWidgetView.this.renderingView.queueEvent(runnable);
            }
        };
        this.renderer = this.createRenderer(this.getContext(), scheduler, xMetersPerPixel, yMetersPerPixel, this.getScreenRotationInDegrees(rotation));
        this.renderingView.setRenderer(this.renderer);
    }

    protected abstract VrWidgetRenderer createRenderer(Context var1, GLThreadScheduler var2, float var3, float var4, int var5);

    private void initializeUiButtons() {
        this.enterFullscreenButton = (ImageButton) this.uiView.findViewById(id.fullscreen_button);
        this.enterFullscreenButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                VrWidgetView.this.setDisplayMode(2);
            }
        });
        this.enterStereoModeButton = (ImageButton) this.uiView.findViewById(id.vr_mode_button);
        this.enterStereoModeButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                VrWidgetView.this.setDisplayMode(3);
            }
        });
        this.fullscreenBackButton = (ImageButton) this.uiView.findViewById(id.fullscreen_back_button);
        this.fullscreenBackButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                VrWidgetView.this.setDisplayMode(1);
            }
        });
        this.infoButton = (ImageButton) this.uiView.findViewById(id.info_button);
        this.infoButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                VrWidgetView.this.activity.startActivity(VrWidgetView.getInfoButtonIntent());
            }
        });
        this.updateButtonVisibility();
    }

    public void getHeadRotation(float[] yawAndPitch) {
        if (yawAndPitch.length < 2) {
            throw new IllegalArgumentException("Array should have at least 2 elements.");
        } else {
            this.renderer.getHeadRotation(yawAndPitch);
        }
    }

    public void setDisplayMode(int newDisplayMode) {
        if (newDisplayMode != this.displayMode) {
            this.renderer.updateCurrentYaw();
            if (newDisplayMode <= 0 || newDisplayMode >= 4) {
                Log.e(TAG, (new StringBuilder(38)).append("Invalid DisplayMode value: ").append(newDisplayMode).toString());
                newDisplayMode = 1;
            }

            this.displayMode = newDisplayMode;
            this.updateStereoMode();
            if (this.isFullScreen()) {
                this.orientationHelper.lockOrientation();
                this.fullScreenDialog.show(newDisplayMode == 2);
            } else {
                this.fullScreenDialog.dismiss();
                this.orientationHelper.restoreOriginalOrientation();
            }

            this.updateControlsLayout();
            this.updateTouchTracker();
            this.eventListener.onDisplayModeChanged(this.displayMode);
        }
    }

    public int getDisplayMode() {
        return this.displayMode;
    }

    private void updateControlsLayout() {
        LinearLayout controlLayout = (LinearLayout) this.innerWidgetView.findViewById(id.control_layout);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) controlLayout.getLayoutParams();
        if (this.displayMode == 3 && this.orientationHelper.isInPortraitOrientation()) {
            layoutParams.addRule(9);
            layoutParams.addRule(11, 0);
        } else {
            layoutParams.addRule(9, 0);
            layoutParams.addRule(11);
        }

        controlLayout.setLayoutParams(layoutParams);
        if (this.displayMode == 2) {
            this.viewRotator.enable();
        } else {
            this.viewRotator.disable();
        }

    }

    private void updateStereoMode() {
        this.renderer.setStereoMode(this.displayMode == 3);
        AndroidNCompat.setVrModeEnabled(this.activity, this.displayMode == 3, 0);
        if (this.displayMode == 3) {
            this.screenOnFlagHelper.start();
        } else {
            this.screenOnFlagHelper.stop();
        }

        this.updateButtonVisibility();
        this.updateViewerName();
    }

    private void updateButtonVisibility() {
        if (this.isFullscreenButtonEnabled && this.displayMode != 2) {
            this.enterFullscreenButton.setVisibility(View.VISIBLE);
        } else {
            this.enterFullscreenButton.setVisibility(View.GONE);
        }

        if (this.isStereoModeButtonEnabled && this.displayMode != 3) {
            this.enterStereoModeButton.setVisibility(View.VISIBLE);
        } else {
            this.enterStereoModeButton.setVisibility(View.GONE);
        }

//        this.vrUiLayer.setSettingsButtonEnabled(this.displayMode == 3);
        this.vrUiLayer.setSettingsButtonEnabled(false);
        this.vrUiLayer.setAlignmentMarkerEnabled(this.displayMode == 3);
        this.vrUiLayer.setTransitionViewEnabled(this.displayMode == 3 && this.isTransitionViewEnabled);
        if (!this.isFullScreen()) {
            this.fullscreenBackButton.setVisibility(View.GONE);
            this.vrUiLayer.setBackButtonListener((Runnable) null);
        } else if (this.displayMode == 3) {
            this.fullscreenBackButton.setVisibility(View.GONE);
            this.vrUiLayer.setBackButtonListener(new Runnable() {
                public void run() {
                    VrWidgetView.this.setDisplayMode(1);
                }
            });
        } else {
            this.fullscreenBackButton.setVisibility(View.GONE);
            this.vrUiLayer.setBackButtonListener((Runnable) null);
        }

        this.infoButton.setVisibility(this.isInfoButtonEnabled && this.displayMode != 3 ? View.VISIBLE : View.GONE);
    }

    private int getScreenRotationInDegrees(int rotation) {
        switch (rotation) {
            case 0:
            default:
                return 0;
            case 1:
                return 90;
            case 2:
                return 180;
            case 3:
                return 270;
        }
    }

    protected void setEventListener(VrEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void pauseRendering() {
        this.renderingView.onPause();
        this.renderer.onPause();
        this.screenOnFlagHelper.stop();
        this.isPaused = true;
        this.viewRotator.disable();
    }

    public void resumeRendering() {
        this.renderingView.onResume();
        this.renderer.onResume();
        this.updateStereoMode();
        if (this.isFullScreen()) {
            this.fullScreenDialog.show(displayMode == 2);
        }

        this.updateButtonVisibility();
        this.updateControlsLayout();
        this.isPaused = false;
    }

    public void shutdown() {
        if (!this.isPaused) {
            throw new IllegalStateException("pauseRendering() must be called before calling shutdown().");
        } else {
            this.viewerParamsProvider.close();
            this.renderer.shutdown();
        }
    }

    public void setStereoModeButtonEnabled(boolean enabled) {
        boolean sensorsAvailable = this.sensorsHelper.areTrackingSensorsAvailable();
        if (enabled && !sensorsAvailable) {
            Log.w(TAG, "This phone doesn't have the necessary sensors for head tracking, stereo Mode button will be disabled.");
        }

        this.isStereoModeButtonEnabled = enabled && sensorsAvailable;
        this.updateButtonVisibility();
    }

    public void setFullscreenButtonEnabled(boolean enabled) {
        this.isFullscreenButtonEnabled = enabled;
        this.updateButtonVisibility();
    }

    public void setTouchTrackingEnabled(boolean enabled) {
        if (this.isTouchTrackingEnabled != enabled) {
            this.isTouchTrackingEnabled = enabled;
            this.updateTouchTracker();
        }
    }

    public void setTransitionViewEnabled(boolean enabled) {
        this.isTransitionViewEnabled = enabled;
        this.updateButtonVisibility();
    }

    public void setInfoButtonEnabled(boolean enabled) {
        this.isInfoButtonEnabled = enabled;
        this.updateButtonVisibility();
    }

    public void setOnTouchListener(OnTouchListener touchListener) {
        this.innerWidgetView.setOnTouchListener(touchListener);
    }

    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superClassState", super.onSaveInstanceState());
        bundle.putBundle("orientationHelperState", this.orientationHelper.onSaveInstanceState());
        bundle.putBundle("widgetRendererState", this.renderer.onSaveInstanceState());
        bundle.putInt("displayMode", this.displayMode);
        return bundle;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            this.orientationHelper.onRestoreInstanceState(bundle.getBundle("orientationHelperState"));
            this.renderer.onRestoreInstanceState(bundle.getBundle("widgetRendererState"));
            this.displayMode = bundle.getInt("displayMode");
            state = bundle.getParcelable("superClassState");
        }

        super.onRestoreInstanceState(state);
    }

    static Intent getInfoButtonIntent() {
        return new Intent("android.intent.action.VIEW", INFO_BUTTON_URL);
    }

    private void updateViewerName() {
        DeviceParams deviceProto = this.viewerParamsProvider.readDeviceParams();
        this.vrUiLayer.setViewerName(deviceProto == null ? null : deviceProto.getModel());
    }

    public abstract static class DisplayMode {
        private static final int START_MARKER = 0;
        public static final int EMBEDDED = 1;
        public static final int FULLSCREEN_MONO = 2;
        public static final int FULLSCREEN_STEREO = 3;
        private static final int END_MARKER = 4;

        public DisplayMode() {
        }
    }
}
