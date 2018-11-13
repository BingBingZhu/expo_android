
var topRightPoint = [116.330649,39.977525]; // 电子围栏东北角
var bottomLeftPoint = [116.334704,39.981734]; // 电子围栏西南角
var mapCenterPoint = [116.333058,39.979113]; // 地图中心点

// var topRightPoint = [115.053723,24.771006]; // 电子围栏东北角
// var bottomLeftPoint = [115.060694,24.760576]; // 电子围栏西南角
// var mapCenterPoint = [(topRightPoint[0]+bottomLeftPoint[0])/2, (topRightPoint[1]+bottomLeftPoint[1])/2]; // 地图中心点


var markers = [];
var poiData; 
var navData; // 导航数据
var destData; // 需导航到达终点数据
var cusCount = 1;

var wwwRoot = '';

var iconRoot = wwwRoot + 'assets/icons/';
var customMapPathRoot = 'http://103.83.44.143:80/res/';
var userIconPath = wwwRoot + 'assets/icons/user_pos.png';
var customMapInfo  = {};
var customMapPath = '';

var infoBanner = document.getElementById('infoContainer');
var goIcon = document.getElementById('goIcon');

// 当前点自定义样式配置
var customStyleConfig = {
    wrapEle: {
        backgroundImage: 'url('+ wwwRoot + 'assets/icons/compass_2d.png)',
        backgroundPosition: 'center',
        backgroundSize: '100% 100%',
        backgroundRepeat: 'no-repeat',
        borderRadius: '50%',
        width: '100px',
        height: '100px',
        transform: 'translate(-50%, -50%)'
      
    }, 
    innerImg: {
        width: '50px',
        height: '50px',
        top: '50%',
        left: '50%',
        transform: 'translate(-50%, -50%) rotate(0deg)'
    }
};

/* -------------------终端与地图页面数据交互接口--------------------- */


/* 终端与map页面接口 */


/**
 * JS可用性检查
 * 
 */
function checkJsUsable() {
    window.Terminal_Interface.canCallJS(true);
}


/**
 * 获取单个poi的id和类型，此数据终端传递给当前页面
 * 
 * @returns {Object} eg: {id: '23', type: 'venues'}
 */
function getSinglePoiInfo() {
    var search = window.location.search; 
    if(search) {
        var query = search.split('&');

        var poiId = query[0].split('=')[1];
        var poiType = query[1].split('=')[1];
  
        return {
            id: poiId,
            type: poiType
        };
    }else{
        return {
            id: '',
            type: ''
        };
    }
}

var singlePoiInfo = getSinglePoiInfo();


/**
 * 从终端获取当前园区的所有POI数据
 * 
 * @returns {JSON}
 */
function getAllPOIData() {
    return window.Terminal_Interface.loadPois();
}

/**
 * 返回当前园区的信息
 * 
 * @returns {JSON}
 */
function getCurrParkInfo() {
    return window.Terminal_Interface.loadCurrParkInfo();
}

var parkInfo = JSON.parse(getCurrParkInfo());

// 获取当前园区信息
customMapInfo = {
    parkName: parkInfo.caption,
    loc: {
        topRightPoint: '',
        bottomLeftPoint: ''
    },
    mapUrl: parkInfo.mapurl
};

customMapPath = customMapPathRoot + customMapInfo.mapUrl;

// 地图范围限制
var mapBounds = new AMap.Bounds(topRightPoint, bottomLeftPoint);

// 添加自定义地图
var imageLayer = new AMap.ImageLayer({
    url: customMapPath,
    bounds: mapBounds,
    zooms: [15, 18],
});

// 创建地图
var map = new AMap.Map('container', {
    resizeEnable: true,
    center: mapCenterPoint,
    zoom: 18,
    layers: [
        new AMap.TileLayer(),
        imageLayer
    ]
});

map.setLimitBounds(mapBounds);

// 地图路线规划
var walking = new AMap.Walking({
    map: map,
    hideMarkers: true,
    autoFitView: true
});


/**
 * 根据parkName和原始数据获取所需数据
 * 
 * @param {{JSON}} data 原始数据
 * @param {string} parkName 
 */
function parsePOIData(data, parkName) {
    var originalPoiData = JSON.parse(data);
    var poiData = [];

    // 添加场馆
    originalPoiData.venues.forEach(function(item) {
        var park = {};
        if(parkName === item.parkname) {
            park = {
                id: item.id,
                name: item.caption,
                type: 'venue',
                longitude: item.lon,
                latitude: item.lat
            };

            poiData.push(park);
        }else{
            return;
        }
    });

    // 添加AR景观
    originalPoiData.mission.forEach(function(item){
        var park = {};
        if(parkName === item.parkname) {
            park = {
                id: item.id,
                name: item.caption,
                type: 'ar_mission',
                longitude: item.lon,
                latitude: item.lat
            };

            poiData.push(park);
        }else{
            return;
        }
    });

    return poiData;
}


poiData = parsePOIData(getAllPOIData(), customMapInfo.parkName);

// testInfo(poiData);
// 根据POI数据生成marker数据
function genPoiMarkers(poiData) {
    var poiMarkers = [];
    poiData.forEach(function (poi) {
        var marker = {};
  
        // 如果poi类型非AR类型则初始化时不显示icon图标
        if (poi.type === 'venue') {
            marker = {
                icon: iconRoot + 'icon_real.png',
                position: [poi.longitude, poi.latitude],
                isAR: false,
                poiType: poi.type,
                name: poi.name,
                markId: poi.id
            };
            poiMarkers.push(marker);
        } else if (poi.type === 'ar_mission') {
            marker = {
                icon: iconRoot + 'icon_ar.png',
                position: [poi.longitude, poi.latitude],
                isAR: true,
                poiType: poi.type,
                name: poi.name,
                markId: poi.id        
            };
            poiMarkers.push(marker);
        }
    });
  
    return poiMarkers;
}

markers = genPoiMarkers(poiData);
// testInfo(markers);

// 实时获取当前位置
var userPos; 
var startMarker; 
function sendUserPosition(longitude, latitude) {
    userPos = {lng: longitude, lat: latitude};

    // 改变用户位置
    if(startMarker) {
    
        if(cusCount == 1) {

            customStartMarker(startMarker, customStyleConfig);
            addStartMarkerLabel();

            cusCount++;
        }
        
        var userPoint = new AMap.LngLat(userPos.lng, userPos.lat); 
        startMarker.setPosition(userPoint);    
    }else{     
    // 生成当前点marker标识
        startMarker = new AMap.Marker({
            map: map,
            icon: new AMap.Icon({
                size: new AMap.Size(40, 50),
                image: userIconPath,
                imageOffset: new AMap.Pixel(0, 0)
            }),
            position: [userPos.lng, userPos.lat],
            offset: new AMap.Pixel(-10, -14),
            clickable: false,
            extData:{ isUser: true}    
        });

    }
}
 

/**
 * 给当前用户点添加唯一标识
 * 
 */
function addStartMarkerLabel() {
    var markDom = startMarker.getContentDom();
    markDom.setAttribute('data-isUser', true);
} 


/**
 * 获取方位角数据
 * 
 * @param {number} azimuth 手机方位角
 * @param {number} pitch 
 * @param {number} roll 
 */
function sendSensorData(azimuth, pitch, roll){
    // testInfo({azimuth:azimuth, pitch: pitch, roll: roll})

    // 根据azimuth旋转当前位置图标
    if(startMarker && startMarker.getContentDom()) {
        var startEle = startMarker.getContentDom();
        var innerImg = startEle.querySelector('img');
  
        // 旋转innerImg元素
 
        innerImg.style.transform = 'translate(-50%, -50%) rotate(' + azimuth + 'deg)';
    
    }else {
        return;
    }

}


/**
 * 自定义当前marker点的样式
 * 
 * @param {object} marker 当前点marker对象
 * @param {object} styleConfig 对当前点marker对象的配置
 * eg: {
 *    wrapEle: {
 *      background: 'url(./) no-repeat center center 100% 100%',
 *      borderRadius: '50%',
 *      width: '100px',
 *      height: '100px',
 *      transform: 'translate(-50%, -50%)'
 * 
 *    }, 
 *    innerImg: {
 *      width: '50px',
 *      height: '50px',
 *      top: '50%',
 *      left: '50%',
 *      transform: 'translate(-50%, -50%) rotate(0deg)' 
 *    }
 * }
 */

function customStartMarker(marker, styleConfig) {
    var markEle = marker.getContentDom();

    // 获取marker图标容器
    var markImg = markEle.querySelector('img');

    // 配置marker包裹元素样式
    for(var prop in styleConfig.wrapEle) {
        markEle.style[prop] = styleConfig.wrapEle[prop];
    }

    // 配置内部Img样式
    for(var attr in styleConfig.innerImg) {
        markImg.style[attr] = styleConfig.innerImg[attr];
    }
}


/**
 * 填充底部路线信息
 * 
 * @param {number} dist 剩余距离
 * @param {number} time 到达时间
 */
function paddingNavContent(dist, time) {
    var destEle = document.querySelector('#restDist > span');
    var timeEle = document.querySelector('#restTime > span');

    destEle.innerHTML = '剩余' + dist + 'KM';
    timeEle.innerHTML = '约' + time + '分钟';
}

function showInfoBanner() {
    infoBanner.style.display = 'block';
}

function hiddenInfoBanner() {
    infoBanner.style.display = 'none';
}

 
/**
 * 点击当前AR marker点，当前点变大，其他AR点变小
 * 
 * @param {object} marker 当前点击marker点
 */
function zoomInCurrClickedMarker(marker) {
    var markers = document.querySelectorAll('.amap-icon');
    // var markersImg = document.querySelectorAll('.amap-icon > img');

    var markerImg = marker.querySelector('img');

    // 获取当前点击marker的宽高
    var markerW = 40;
    var markerH = 45;

    // 放大当前点击marker
    markerImg.style.width = markerW * 1.5 + 'px';
    markerImg.style.height = markerH * 1.5 + 'px';

    marker.style.width = markerW * 1.5 + 'px';
    marker.style.height = markerH * 1.5 + 'px';
  
    // 其他marker尺寸大小恢复正常
    Array.from(markers).forEach(function(markEle,index) {
    // var isUserMark = markEle.getExtData().isUser
        var isUserLabel = markEle.getAttribute('data-isUser');

        // 用户marker不做处理
        if (isUserLabel) return; 

        if(markEle !== marker ) {
            // console.log(index)
            var markEleImg = markEle.querySelector('img');

            markEle.style.width = markerW + 'px';
            markEle.style.height = markerH + 'px';
      
            markEleImg.style.width = markerW + 'px';
            markEleImg.style.height = markerH + 'px';
      
        }
    });
}


// 添加多个marker点, 并给每个点添加点击事件处理

markers.forEach(function (marker) {
    var markIcon = new AMap.Marker({
        map: map,
        // icon: marker.icon,
        icon: new AMap.Icon({
            size: new AMap.Size(40, 50),
            image: marker.icon,
            imageOffset: new AMap.Pixel(0, 0)
        }),
        position: marker.position,
        offset: new AMap.Pixel(-14, -55),
        animation: marker.animation,
        extData:{ 
            markId: marker.markId,
            isAR: marker.isAR,
            name: marker.name,
            poiType: marker.poiType
        }
    });

    // 点击marker点获取点坐标，同时请求路线规划接口，填充数据到底部banner
    function clickHandle(e) {
    // 变换marker大小
        var marker = markIcon.getContentDom();
        markIcon.setAnimation('AMAP_ANIMATION_DROP');
    
        zoomInCurrClickedMarker(marker);
        markIcon.setOffset(new AMap.Pixel(-20, -63));
        // 获取终点坐标及其他相关信息
        var destPos = markIcon.getPosition();
        var targetExtData = markIcon.getExtData();
        destData = {
            name: targetExtData.name,
            type: targetExtData.poiType,
            longitude: destPos.lng,
            latitude: destPos.lat
        };
 
        // 请求路线规划接口
        // testInfo(userPos);
        if(userPos) {
            axios({
                method: 'post',
                url: 'http://103.83.44.143:8080/Api/Terminal/GetPathPlanningstr',
                data: {
                    Uid: '',
                    Ukey: '',
                    originlon: userPos.lng,
                    originlat: userPos.lat,
                    destlon: destPos.lng,
                    destlat: destPos.lat
                }
            }).then(function (response) {
                // console.log(response);
                // test, 添加路线展示
                walking.search([userPos.lng,userPos.lat], [destPos.lng, destPos.lat]);
  
  
                // 获取路线规划数据
                navData = response.data;
                var paths = navData.route.paths;
                var dist = (parseInt(paths[0].distance)/1000).toFixed(2);
                var time = (parseInt(paths[0].duration)/60).toFixed(1);
  
                paddingNavContent(dist, time);
                showInfoBanner();
            }).catch(function (error) {
                // console.log(error);
            });
  
        }
    }

    markIcon.on('click', clickHandle);


    // 唯一确定一个选中POI点
    var isSelected = singlePoiInfo.id == markIcon.getExtData().markId && 
                   singlePoiInfo.type == markIcon.getExtData().poiType;
  
    // 只有唯一一个poi被选中
    if(isSelected) {
        // testInfo(isSelected);
        var loopTimer = setInterval(function() {
            if(markIcon.getContentDom() && userPos){
                clickHandle();
                clearInterval(loopTimer);
            }
        }, 1000);
    }

});

// 点击到这去按钮跳转3D视图
goIcon.addEventListener('click', function (e) {
    var navDataStr = JSON.stringify(navData);
    var destDataStr = JSON.stringify(destData);
    goto3DView(navDataStr, destDataStr);
});

// 点击地图上其他地方隐藏底部banner
map.on('click', function(e) {
    hiddenInfoBanner();
});


// 退出
var returnPrev = document.getElementById('returnPrev');
returnPrev.addEventListener('click', function() {
    window.Terminal_Interface.finish();
});


/**
 * 跳转3D视图
 * 
 * @param {string} paths 传递给3D页面的路线规划json数据  
 */
function goto3DView(paths, destInfo) {
    window.Terminal_Interface.startArNavigation(paths, destInfo);
}


window.onload = function() {
    if(sendUserPosition && sendSensorData) {
        checkJsUsable();
    }
};