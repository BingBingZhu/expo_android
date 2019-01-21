package com.zolad.gl;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;

import com.zolad.gl.base.GLTextureView;
import com.zolad.gl.base.MultiSampleEGLConfigChooser;
import com.zolad.gl.interfaces.SurfaceListener;
import com.zolad.gl.renders.NeedGLRenderer;

public class NeedTextureView extends GLTextureView {

    private NeedGLRenderer needGLRenderer;
    private SurfaceTextureListener mSurfaceTextureListener;

    public NeedTextureView(Context context) {
        this( context, null );
    }

    public NeedTextureView(Context context, AttributeSet attrs) {
        this( context, attrs, 0 );
    }


    public NeedTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr );
        setConfigAndRenderer();
    }

    private void setConfigAndRenderer() {
        setEGLContextClientVersion( 2 );
        MultiSampleEGLConfigChooser chooser = new MultiSampleEGLConfigChooser();
        setEGLConfigChooser( chooser );
        needGLRenderer = new NeedGLRenderer( this );
        needGLRenderer.setUsesCoverageAa( chooser.usesCoverageAa() );
        setSurfaceListener( surfaceListener );
        setRenderer( needGLRenderer );
        setRenderMode( RENDERMODE_WHEN_DIRTY );
        setOpaque( false );
    }

    private SurfaceListener surfaceListener = new SurfaceListener() {
        @Override
        public void onSurfaceCreated(SurfaceTexture surface) {
            if (mSurfaceTextureListener != null) {
                mSurfaceTextureListener.onSurfaceTextureAvailable( surface, getWidth(), getHeight() );
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceTexture surface, int width, int height) {
            if (mSurfaceTextureListener != null) {
                mSurfaceTextureListener.onSurfaceTextureSizeChanged( surface, width, height );
            }
        }

        @Override
        public void onSurfaceUpdate(SurfaceTexture surface) {
            if (mSurfaceTextureListener != null) {
                mSurfaceTextureListener.onSurfaceTextureUpdated( surface );
            }
        }
    };

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if (mSurfaceTextureListener != null) {
            mSurfaceTextureListener.onSurfaceTextureDestroyed( surface );
        }
        return super.onSurfaceTextureDestroyed( surface );
    }

    public void setCornerRadius(float radius) {
        needGLRenderer.setCornerRadiusAndArrow( radius );
    }

    public void setSurfaceListener(SurfaceListener provider) {
        needGLRenderer.setSurfaceListener( provider );
    }

    public SurfaceTexture getRealSurfaceTexture() {
        return needGLRenderer.getSurfaceTexture();
    }

    @Override
    public void setSurfaceTextureListener(SurfaceTextureListener listener) {
        mSurfaceTextureListener = listener;
    }
}
