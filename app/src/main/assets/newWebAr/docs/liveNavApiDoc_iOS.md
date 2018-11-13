
AR 实景导航界面与终端接口说明

1. 获取当前位置 原生调JS

sendUserPosition(lng: number, lat: number)

2. 获取传感器数据 原生调JS

sendSensorData(azimuth: number, pitch: number, roll: number)

3. 获取目的地数据 JS调原生

@return json 
该接口返回数据为二维地图接口

**startArNavigation({paths: {...}, destInfo: {...}})**
获得的destInfo数据

function getDestInfo(destInfo) {}

4. 获取路线规划数据 JS调原生
@return json 

该接口返回数据为二维地图接口

**startArNavigation({paths: {...}, destInfo: {...}})**获得的paths数据


function getPaths(paths) {}



5. 通知终端调用JS的时机，JS可用性检查 JS调原生
@param {bool} true/false

**canCallJS(true)**

6. 退出处理，点击back键，弹出确认框，点击*确认*直接退出，点击*取消*则阻止退出事件默认行为，留在实景导航页面

IOS提供后退接口

**goBack()**

7. 