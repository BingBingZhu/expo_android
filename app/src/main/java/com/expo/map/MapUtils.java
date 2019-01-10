package com.expo.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.DPoint;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.TileOverlayOptions;
import com.amap.api.maps.model.UrlTileProvider;
import com.blankj.utilcode.util.StringUtils;
import com.expo.R;
import com.expo.base.BaseApplication;
import com.expo.base.ExpoApp;
import com.expo.base.utils.PrefsHelper;
import com.expo.entity.Park;
import com.expo.network.Http;
import com.expo.utils.BitmapUtils;
import com.expo.utils.Constants;
import com.expo.widget.ImageViewPlus;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapUtils {

    private AMap map;

    public MapUtils(AMap map) {
        this.map = map;
    }

    public boolean ptInPolygon(LatLng latLng, List<double[]> points){
        return ptInPolygon(latLng.latitude, latLng.longitude, points);
    }

    /**
     * 判断指定坐标在指定范围内
     *
     * @param points
     * @return
     */
    public static boolean ptInPolygon(double lat, double lng, List<double[]> points) {
        int nCross = 0;
        double[] p = null;
        for (int i = 0; i < points.size(); i++) {
            p = points.get( i );
            LatLng p1 = new LatLng( p[1], p[0] );
            p = points.get( (i + 1) % points.size() );
            LatLng p2 = new LatLng( p[1], p[0] );
            // 求解 y=p.y 与 p1p2 的交点
            if (p1.longitude == p2.longitude) {                                                     // p1p2 与 y=p0.y平行
                continue;
            }
            if (lng < Math.min( p1.longitude, p2.longitude )) {                           // 交点在p1p2延长线上
                continue;
            }
            if (lng >= Math.max( p1.longitude, p2.longitude )) {                          // 交点在p1p2延长线上
                continue;
            }
            // 求交点的 X 坐标 --------------------------------------------------------------
            double x = (lng - p1.longitude) * (p2.latitude - p1.latitude) / (p2.longitude - p1.longitude) + p1.latitude;
            if (x > lat) {
                nCross++; // 只统计单边交点
            }
        }
        // 单边交点为偶数，点在多边形之外 ---
        return (nCross % 2 == 1);
    }

    /**
     * 设置地图属性
     */
    public void settingMap(AMap.OnMapTouchListener onMapTouchListener, AMap.OnMarkerClickListener onMarkerClickListener) {
//        map.setMaxZoomLevel( 12F );
//        map.setMinZoomLevel( 10.5F );
        map.getUiSettings().setRotateGesturesEnabled( false );
        map.getUiSettings().setTiltGesturesEnabled( false );
        map.getUiSettings().setZoomControlsEnabled( false );
        map.setMyLocationEnabled( true );
        map.setOnMapTouchListener( onMapTouchListener );
        map.setOnMarkerClickListener( onMarkerClickListener );
        map.setMapType( (int) PrefsHelper.getLong( Constants.Prefs.KEY_MAP_PATTERN, 1 ) );
        setNotFollow();
    }

//    /**
//     * 设置地图可视区域及缩放级别
//     *
//     * @param scenicSpot
//     */
//    public void setMapCamera(Venue scenicSpot) {
//        map.moveCamera( CameraUpdateFactory.newLatLngBounds( getBoundsBuilder( scenicSpot ), 120 ) );
//    }

    /**
     * 限制地图矩形操作区域（园区区域）
     *
     * @param park 园区
     */
    public void setLimits(Park park) {
        map.setMapStatusLimits( getBoundsBuilder( park ) );
    }

    private LatLngBounds getBoundsBuilder(Park park) {
        ArrayList<double[]> electronicFenceList = park.getElectronicFenceList();
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();//存放所有点的经纬度
        for (double[] lngLat : electronicFenceList) {
            boundsBuilder.include( new LatLng( lngLat[1], lngLat[0] ) );
        }
        return boundsBuilder.build();
    }

    public List<DPoint> getGeoFencePoints(Park park) {
        List<DPoint> dPoints = new ArrayList<>();
        ArrayList<double[]> electronicFenceList = park.getElectronicFenceList();
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();//存放所有点的经纬度
        for (double[] lngLat : electronicFenceList) {
            dPoints.add( new DPoint( lngLat[1], lngLat[0] ) );
        }
        return dPoints;
    }

    public float setMapMinZoom() {
        float zoom = map.getCameraPosition().zoom;
        map.setMinZoomLevel( zoom );
        return zoom;
    }

    /**
     * 地图跟随定位点移动
     *
     * @param aMapLocation
     */
    public void setFollow(AMapLocation aMapLocation) {
        mapGoto( aMapLocation );
        settingLocationStylePattern( MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE );
    }

    public void setFollow(LatLng latLng) {
        mapGoto( latLng );
        settingLocationStylePattern( MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE );
    }

    /**
     * 地图不跟随定位点移动
     */
    public void setNotFollow() {
        settingLocationStylePattern( MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER );
    }


    /**
     * 设置定位点模式
     *
     * @param locationStyle MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER  不跟随定位点移动
     *                      MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE            跟随定位点移动
     */
    public void settingLocationStylePattern(int locationStyle) {
        getMyLocationStyle();
        myLocationStyle.myLocationType( locationStyle );
        map.setMyLocationStyle( myLocationStyle );//设置定位蓝点的Style
    }

    MyLocationStyle myLocationStyle = null;

    public void getMyLocationStyle() {
        if (null == myLocationStyle) {
            myLocationStyle = new MyLocationStyle();
            myLocationStyle.interval( 2000 );
            myLocationStyle.strokeColor( BaseApplication.getApplication().getResources().getColor( R.color.color_local_stork ) );//设置定位蓝点精度圆圈的边框颜色的方法。
            myLocationStyle.radiusFillColor( BaseApplication.getApplication().getResources().getColor( R.color.color_local ) );//设置定位蓝点精度圆圈的填充颜色的方法。
            BitmapDescriptor bitmapDescriptor;
            View view = LayoutInflater.from( BaseApplication.getApplication() ).inflate( R.layout.layout_local_icon, null );
            if (null != ExpoApp.getApplication().getUser() && !ExpoApp.getApplication().getUser().getPhotoUrl().isEmpty()) {
                Http.loadBitmap( ExpoApp.getApplication().getUser().getPhotoUrl(), (url, bitmap, obj) -> {
                    settingLocalImage( bitmap );
                    ExpoApp.getApplication().setUserHandBitmap( bitmap );
                }, ExpoApp.getApplication() );
            }
            bitmapDescriptor = BitmapDescriptorFactory.fromView( view );
            myLocationStyle.myLocationIcon( bitmapDescriptor );
        }
    }

    private void settingLocalImage(Bitmap bitmap) {
        View view = LayoutInflater.from( ExpoApp.getApplication() ).inflate( R.layout.layout_local_icon, null );
        ImageViewPlus img = view.findViewById( R.id.local_icon_img );
        img.setImageBitmap( bitmap );
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView( view );
        myLocationStyle.myLocationIcon( bitmapDescriptor );
        map.setMyLocationStyle( myLocationStyle );//设置定位蓝点的Style
    }

    /**
     * 地图可视区域转变到指定中心点
     *
     * @param latLng
     */
    public void mapGoto(LatLng latLng) {
        if (null == latLng || latLng.latitude == 0)
            return;
        CameraUpdate cameraUpdate = CameraUpdateFactory.changeLatLng( latLng );
        map.animateCamera( cameraUpdate );
    }

    public void mapGoto(double lat, double lng) {
        LatLng latLng = new LatLng( lat, lng );
        mapGoto( latLng );
    }

    public void mapGoto(AMapLocation aMapLocation) {
        mapGoto( aMapLocation.getLatitude(), aMapLocation.getLongitude() );
    }

    /**
     * 根据多个marker确定zoom及可视区域
     *
     * @param markers
     */
    public void setCameraZoom(List<Marker> markers) {
        if (markers.isEmpty())
            return;
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();//存放所有点的经纬度
        for (int i = 0; i < markers.size(); i++) {
            boundsBuilder.include( markers.get( i ).getPosition() );
        }
        map.animateCamera( CameraUpdateFactory.newLatLngBounds( boundsBuilder.build(), 260 ) );
    }

    public BitmapDescriptor setMarkerIconDrawable(Context context, Bitmap bitmap, String text) {
        return setMarkerIconDrawable( context, bitmap, text, null, false );
    }

    public BitmapDescriptor setSelectedMarkerIcon(Context context, Bitmap bitmap, String text) {
        return setMarkerIconDrawable( context, bitmap, text, null, true );
    }

    public BitmapDescriptor setMarkerIconDrawable(Context context, Bitmap bitmap, String text, String centerText, boolean isSelected) {
        View view;
        if (isSelected) {
            view = LayoutInflater.from( context ).inflate( R.layout.layout_as_selcted_icon, null );
            if (null == bitmap)
                bitmap = BitmapFactory.decodeResource( context.getResources(), R.mipmap.test_1 );
            bitmap = BitmapUtils.convertToBlackWhite( bitmap, Color.WHITE );  // 渲染图片非透明区域为白色
        } else
            view = LayoutInflater.from( context ).inflate( R.layout.layout_as_icon, null );
        TextView tv = view.findViewById( R.id.as_ico_text );
        ImageView img = view.findViewById( R.id.icon_center_img );
        TextView ctv = view.findViewById( R.id.icon_center_text );
        tv.setText( text );
        if (StringUtils.isEmpty( centerText )) {
            img.setImageBitmap( bitmap );
        } else {
            if (!isSelected) {
                img.setImageResource( 0 );
                tv.setVisibility( View.GONE );
                ctv.setVisibility( View.VISIBLE );
                ctv.setText( centerText );
            }
        }
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView( view );
        return bitmapDescriptor;
    }


    /**
     * 加载离线和在线瓦片数据
     *
     * @param context *** @param baseUrl   瓦片图路径   mapUtils.useOfflineTile( "file:///storage/emulated/0/ThePalaceMuseum" );
     */
    public TileOverlayOptions getTileOverlayOptions(Context context) {
        String cacheDir = Environment.getExternalStorageDirectory() + File.separator + "Android/data/" + context.getPackageName() + "/cache/tiles/";
        return new TileOverlayOptions()
                .diskCacheEnabled( true )
                .diskCacheDir( cacheDir )
                .diskCacheSize( 10000 * 256 * 256 )
                .memoryCacheEnabled( true )
                .memCacheSize( 2000 * 256 * 256 )
                .zIndex( 0 )
                .tileProvider( new UrlTileProvider( 256, 256 ) {
                    @Override
                    public URL getTileUrl(int x, int y, int zoom) {
                        try {
                            return new URL( String.format( Locale.getDefault(), Constants.URL.MAP_TILES_BASE_URL, zoom, x, y ) );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                } );
    }

    public float getDistance(LatLng start, LatLng end) {
        double lon1 = (Math.PI / 180) * start.longitude;
        double lon2 = (Math.PI / 180) * end.longitude;
        double lat1 = (Math.PI / 180) * start.latitude;
        double lat2 = (Math.PI / 180) * end.latitude;
        // 地球半径
        double R = 6371.393;
        // 两点间距离 km，如果想要米的话，结果*1000就可以了
        double d = Math.acos( Math.sin( lat1 ) * Math.sin( lat2 ) + Math.cos( lat1 ) * Math.cos( lat2 ) * Math.cos( lon2 - lon1 ) ) * R;
        return (float) (d * 1000);
    }

    /**
     * 计算两点连线和正北方的角度
     *
     * @return
     */
    public double getAngle(MyLatLng A, MyLatLng B) {
        double dx = (B.m_RadLo - A.m_RadLo) * A.Ed;
        double dy = (B.m_RadLa - A.m_RadLa) * A.Ec;
        double angle = 0.0;
        double dLo = B.m_Longitude - A.m_Longitude;
        double dLa = B.m_Latitude - A.m_Latitude;
        if (dy == 0) {
            angle = Math.atan(Math.abs(dx / dy)) * 180. / Math.PI;
        } else {
            if (dLa < 0) {
                return 90;
            } else if (dLa > 0) {
                return 270;
            } else {
                return 0;
            }
        }
        if (dLo > 0 && dLa <= 0) {
            angle = (90. - angle) + 90;
        } else if (dLo <= 0 && dLa < 0) {
            angle = angle + 180.;
        } else if (dLo < 0 && dLa >= 0) {
            angle = (90. - angle) + 270;
        }
        return angle;
    }

    public static class MyLatLng {
        final static double Rc = 6378137;
        final static double Rj = 6356725;
        double m_LoDeg, m_LoMin, m_LoSec;
        double m_LaDeg, m_LaMin, m_LaSec;
        double m_Longitude, m_Latitude;
        double m_RadLo, m_RadLa;
        double Ec;
        double Ed;

        public MyLatLng(double longitude, double latitude) {
            m_LoDeg = (int) longitude;
            m_LoMin = (int) ((longitude - m_LoDeg) * 60);
            m_LoSec = (longitude - m_LoDeg - m_LoMin / 60.) * 3600;

            m_LaDeg = (int) latitude;
            m_LaMin = (int) ((latitude - m_LaDeg) * 60);
            m_LaSec = (latitude - m_LaDeg - m_LaMin / 60.) * 3600;

            m_Longitude = longitude;
            m_Latitude = latitude;
            m_RadLo = longitude * Math.PI / 180.;
            m_RadLa = latitude * Math.PI / 180.;
            Ec = Rj + (Rc - Rj) * (90. - m_Latitude) / 90.;
            Ed = Ec * Math.cos( m_RadLa );
        }
    }

    public static void main(String args[]) {
        MapUtils mu = new MapUtils( null );
        System.out.println( mu.getAngle
                ( new MyLatLng( 116.331017, 39.980113 ),
                        new MyLatLng( 115.955469, 40.437795 ) ) );
    }

}
