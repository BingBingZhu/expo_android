## 原生与地图页面WebView 接口

----------
iOS与JS统一参数和返回值为Object字面量和基本类型

1. 获取POI数据 JS调原生

返回数据格式JSON
形式如下
```
@return {JSON} {
  venues: [{},{}...],
  mission: [{},{}...]
}
```

**loadPois()**

2. 获取当前位置 原生调JS

sendUserPosition(lng: number, lat: number)

3. 跳转3D视图 JS调原生


@param {Dict} 字段 paths, destInfo 该参数字段终端存储,实景导航界面需要使用

**startArNavigation({paths: {...}, destInfo: {...}})**

4. 获取传感器数据 原生调JS

传感器数值变化实时调用JS

sendSensorData(azimuth: number, pitch: number, roll: number)

5. 通知终端调用JS的时机， JS调原生,JS可用性检查, 该接口被调用后终端才能向调用
JS函数sendSensorData和sendUserPosition

@param {bool} true/false

**canCallJS(true)**

6. 加载当前园区数据
@return {JSON}
**loadCurrParkInfo()**

7. 详情页面与终端接口 JS调原生
场馆详情页面点击-到这去-调用接口

**goMapPage()**

该方法内部加载跳转2d地图
URL格式'http://47.95.215.6:80/res/static/map.html?poiId=poiId&type=type&'

搜索跳转'http://47.95.215.6:80/res/static/map.html?poiId=poiId&type=type&'

8. 加载场馆详情和百科详情

场馆详情
http://47.95.215.6:80/res/static/wikiDetails.html?id=35&

百科详情
http://47.95.215.6:80/res/static/venuesDetails.html?id=35&

type=['venue', 'ar_mission']
