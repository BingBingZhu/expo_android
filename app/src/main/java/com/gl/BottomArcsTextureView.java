package com.gl;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;

import com.gl.base.GLTextureView;
import com.gl.base.MultiSampleEGLConfigChooser;
import com.gl.interfaces.SurfaceListener;
import com.gl.renders.BottomArcsGLRenderer;

public class BottomArcsTextureView extends GLTextureView {

    private BottomArcsGLRenderer bottomArcsGLRenderer;
    private SurfaceTextureListener mSurfaceTextureListener;

    public BottomArcsTextureView(Context context) {
        this( context, null );
    }

    public BottomArcsTextureView(Context context, AttributeSet attrs) {
        this( context, attrs, 0 );
    }


    public BottomArcsTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr );
        setConfigAndRenderer();
    }

    private void setConfigAndRenderer() {
        setEGLContextClientVersion( 2 );
        MultiSampleEGLConfigChooser chooser = new MultiSampleEGLConfigChooser();
        setEGLConfigChooser( chooser );
        bottomArcsGLRenderer = new BottomArcsGLRenderer( this );
        bottomArcsGLRenderer.setUsesCoverageAa( chooser.usesCoverageAa() );
        setSurfaceListener( surfaceListener );
        setRenderer( bottomArcsGLRenderer );
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
        bottomArcsGLRenderer.setCornerRadiusAndArrow( radius );
    }

    public void setSurfaceListener(SurfaceListener provider) {
        bottomArcsGLRenderer.setSurfaceListener( provider );
    }

    public SurfaceTexture getRealSurfaceTexture() {
        return bottomArcsGLRenderer.getSurfaceTexture();
    }

    @Override
    public void setSurfaceTextureListener(SurfaceTextureListener listener) {
        mSurfaceTextureListener = listener;
    }
}
