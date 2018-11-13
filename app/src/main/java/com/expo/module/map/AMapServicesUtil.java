package com.expo.module.map;

/**
 *
 */

import android.graphics.Bitmap;

import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.model.NaviLatLng;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AMapServicesUtil {
    public static int BUFFER_SIZE = 2048;

    public static byte[] inputStreamToByte(InputStream in) throws IOException {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[]                data      = new byte[BUFFER_SIZE];
        int                   count     = -1;
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);

        data = null;
        return outStream.toByteArray();
    }

//    public static LatLonPoint convertToLatLonPoint(LatLng latlon) {
//        return new LatLonPoint(latlon.latitude, latlon.longitude);
//    }

//    public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
//        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
//    }

    public static LatLng convertToLatLng(NaviLatLng latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

//    public static ArrayList<LatLng> convertArrList(List<LatLonPoint> shapes) {
//        ArrayList<LatLng> lineShapes = new ArrayList<Point>();
//        for (LatLonPoint point : shapes) {
//            LatLng latLngTemp = AMapServicesUtil.convertToLatLng(point);
//            lineShapes.add(latLngTemp);
//        }
//        return lineShapes;
//    }

    public static ArrayList<LatLng> convertArrList2(List<NaviLatLng> shapes) {
        ArrayList<LatLng> lineShapes = new ArrayList<LatLng>();
        for (NaviLatLng point : shapes) {
            LatLng latLngTemp = AMapServicesUtil.convertToLatLng(point);
            lineShapes.add(latLngTemp);
        }
        return lineShapes;
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, float res) {
        if (bitmap == null) {
            return null;
        }
        int width, height;
        width = (int) (bitmap.getWidth() * res);
        height = (int) (bitmap.getHeight() * res);
        Bitmap newbmp = Bitmap.createScaledBitmap(bitmap, width, height, true);
        return newbmp;
    }
    /**
     * 计算两定位点间的方位角
     * @param point1
     * @param point2
     * @return 角度
     */
    public static float calculateAzimuth(LatLng point1, LatLng point2) {
        double latDistance = Math.abs(point1.latitude - point2.latitude) * 100000;
        double lngDistance = Math.abs(point1.longitude - point2.longitude) * 100000;
        double angle = Math.toDegrees(Math.atan(lngDistance / latDistance));
        if (point2.longitude > point1.longitude) {
            if (point2.latitude < point1.latitude) {
                angle = 180 - angle;
            }
        } else {
            if (point2.latitude > point1.latitude) {
                angle = 360 - angle;
            } else {
                angle = 180 + angle;
            }
        }
        return (float) angle;
    }
}
