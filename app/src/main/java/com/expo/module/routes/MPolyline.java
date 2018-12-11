package com.expo.module.routes;

import com.amap.api.maps.model.Polyline;
import com.autonavi.amap.mapcore.interfaces.IPolyline;

public class MPolyline extends Polyline {

    private Object obj;

    public MPolyline(IPolyline iPolyline) {
        super(iPolyline);
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
