package com.expo.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.expo.base.ExpoApp;
import com.expo.base.utils.ToastHelper;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by LS on 2017/9/25.
 * 传感器方位处理管理
 */

public class DeviceRotateManager implements SensorEventListener {

    private static DeviceRotateManager manager;
    private static List<OnOrientationChangedListener> listeners;
    private float[] values, r, gravity, geomagnetic;

    /**
     * 对方位监听计数
     */
    private AtomicInteger listenerCount;
    /**
     * 传感器速率
     */
    protected int mDelayRate = SensorManager.SENSOR_DELAY_UI;

    /**
     * 传感器管理器
     */
    protected SensorManager mSensorMag;

    public synchronized static DeviceRotateManager getInstance() {
        if (manager == null) {
            manager = new DeviceRotateManager();
        }
        return manager;
    }

    private DeviceRotateManager() {
        listeners = new LinkedList<>();
        mSensorMag = (SensorManager) ExpoApp.getApplication().getSystemService( Context.SENSOR_SERVICE );
        listenerCount = new AtomicInteger( 0 );
    }

    /**
     * 启动传感器检测
     */
    private void start() {
        Sensor orientationSensor = mSensorMag.getDefaultSensor( Sensor.TYPE_ORIENTATION );
        if (orientationSensor != null) {
            // 注册传感器监听
            mSensorMag.registerListener( this, orientationSensor, mDelayRate );
        } else {
            ToastHelper.showLong( "缺少陀螺仪组件，部分功能无法使用" );
//            Sensor magneticSensor      = mSensorMag.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//            Sensor accelerometerSensor = mSensorMag.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//            //初始化数组
//            values = new float[3];//用来保存最终的结果
//            gravity = new float[3];//用来保存加速度传感器的值
//            r = new float[9];//
//            geomagnetic = new float[3];//用来保存地磁传感器的值
//            // 注册传感器监听
//            mSensorMag.registerListener(this, magneticSensor, mDelayRate);
//            mSensorMag.registerListener(this, accelerometerSensor, mDelayRate);
        }
    }

    /**
     * 停止传感器检测
     */
    private void stop() {
        mSensorMag.unregisterListener( this );
    }

    /**
     * 添加设备方位监听
     *
     * @param listener
     */
    public synchronized void registerOrientationChangedListener(OnOrientationChangedListener listener) {
        if (!listeners.contains( listener )) {
            listeners.add( listener );
            int num = listenerCount.incrementAndGet();
            if (num > 0) {
                start();
            }
        }
    }

    /**
     * 取消设备方位监听
     *
     * @param listener
     */
    public synchronized void unregisterOrientationChangedListener(OnOrientationChangedListener listener) {
        if (listeners.remove( listener )) {
            int num = listenerCount.decrementAndGet();
            if (num <= 0) {
                stop();
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float[] values = event.values.clone();
            if (!listeners.isEmpty()) {
                for (OnOrientationChangedListener listener : listeners) {
                    listener.onChanged( values[0], values[1], values[2] );
                }
            }
        } else {                                                                                    //此方式获得数值抖动比较厉害
//            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
//                geomagnetic = event.values.clone();
//            }
//            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//                gravity = event.values.clone();
//                getValue();
//            }
        }
    }

    /*
     * 计算角度
     */
    private void getValue() {
        // r从这里返回
        SensorManager.getRotationMatrix( r, null, gravity, geomagnetic );
        //values从这里返回
        SensorManager.getOrientation( r, values );
        //提取数据
        float azimuth = (float) Math.toDegrees( values[0] );
        if (azimuth < 0) {
            azimuth = azimuth + 360;
        }
        float pitch = (float) Math.toDegrees( values[1] );
        float roll = (float) Math.toDegrees( values[2] );
        if (!listeners.isEmpty()) {
            for (OnOrientationChangedListener listener : listeners) {
                listener.onChanged( azimuth, pitch, roll );
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


    public interface OnOrientationChangedListener {
        void onChanged(float azimuth, float pitch, float roll);
    }
}
