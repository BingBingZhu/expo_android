/**
 * 对应POI点上生成对应与主题相关的标签
 *
 * @param {string} themeId
 * @param {string} tagType
 * @param {string} src
 * @param {object} config
 */
function createTag(themeId, tagType, innerTag, config) {
    // 新加入建筑物则添加对应标签
    // 建筑数据无变化，对应标签保留不做处理
    var building = document.getElementById(themeId);
    var wrapEle = document.getElementById("wrap");

    if (!building) {
        var buildingTag = document.createElement(tagType);
        buildingTag.setAttribute("class", "buildingTag");
        buildingTag.setAttribute("id", themeId);

        if (config) {
            // 对标签进行配置
            for (attr in config) {
                buildingTag.setAttribute("" + attr, config[attr]);
            }
        }

        if (innerTag) {
            buildingTag.innerHTML = innerTag;
        }

        wrapEle.appendChild(buildingTag);
    }

    // 设置标签内容固定宽度(解决标签右移出现压缩问题)
    var tag = document.getElementById(themeId);
    var fixedWidth = getComputedStyle(tag).width;
    tag.style.width = fixedWidth;

}

/**
 * 获得标签当前左上角坐标位置
 *
 * @param {object} tag
 * @returns
 */
function getTagPosition(tag) {
    // var tag = document.getElementById(tagId)

    return {
        left: tag.offsetLeft,
        top: tag.offsetTop
    };
}


/**
 * 获取屏幕宽高
 *
 * @returns {object}
 */
function getViewportSize() {
    return {
        width: window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth,
        height: window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight
    };
}


// 只需调用一次缓存
var viewportSize = getViewportSize();


/**
 * 角度转弧度
 *
 * @param {number} degree
 * @returns
 */
function toRadians(degree) {
    return degree * Math.PI / 180;
}


/**
 * 弧度转角度
 *
 * @param {number} radians
 * @returns
 */
function toDegrees(radians) {
    return (radians * 180) / Math.PI;
}

/**
 * 根据两点经纬度计算距离
 *
 * @param {Object} myDot: {longitude: number, latitude: number}
 * @param {Object} targetDot: {longitude: number, latitude: number}
 * @returns {number} (返回两点之间的距离)
 */
function distance(myDot, targetDot) {
    // R 是地球半径（M）
    var R = 6371393.0;
    var latitude1 = myDot.latitude;
    var longitude1 = myDot.longitude;

    var latitude2 = targetDot.latitude;
    var longitude2 = targetDot.longitude;

    var deltaLatitude = toRadians(latitude2 - latitude1);
    var deltaLongitude = toRadians(longitude2 - longitude1);
    latitude1 = toRadians(latitude1);
    latitude2 = toRadians(latitude2);

    var a = Math.sin(deltaLatitude / 2) *
        Math.sin(deltaLatitude / 2) +
        Math.cos(latitude1) *
        Math.cos(latitude2) *
        Math.sin(deltaLongitude / 2) *
        Math.sin(deltaLongitude / 2);

    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    var d = R * c;
    return parseFloat((d).toFixed(2));
}

/**
 * 计算两点连线的方位角，范围0~360 degree
 *
 * @param {object} fromLoc
 * @param {object} toLoc
 * @returns {number} (方位角)
 */
function getHeadingDirectionDegrees(fromLoc, toLoc) {
    var fLat = toRadians(fromLoc.latitude);
    var fLng = toRadians(fromLoc.longitude);
    var tLat = toRadians(toLoc.latitude);
    var tLng = toRadians(toLoc.longitude);

    var degree = toDegrees(Math.atan2(Math.sin(tLng - fLng) * Math.cos(tLat),
        Math.cos(fLat) * Math.sin(tLat) - Math.sin(fLat) * Math.cos(tLat) * Math.cos(tLng - fLng)));

    if (degree >= 0) {
        return Number(degree.toFixed(2));
    } else {
        return Number((360 + degree).toFixed(2));
    }
}


/**
 * 当手机方位角变化后更新单个标签位置
 *
 * @param {number} azimuth
 * @param {number} pitch
 * @param {number} roll
 * @param {number} poiDireAngle 取值0~360
 * @param {string} tagId
 */

/* 第一个角度：Azimuth (degrees of rotation around the z axis).

表示手机自身的y轴与地磁场北极方向的角度，即手机顶部朝向与正北方向的角度。

（This is the angle between magnetic north and the device's y axis. ）

当手机绕着自身的z轴旋转时，该角度值将发生改变。

例如该角度值为0时，表示手机顶部指向正北；该角度为90度时，代表手机顶部指向正东；
该角度为180度时，代表手机顶部指向正南；该角度为270度时，代表手机顶部指向正西。

第二个角度：Pitch (degrees of rotation around the x axis).

表示手机顶部或尾部翘起的角度。
当手机绕着自身的x轴旋转，该角度会发生变化，值的范围是-180到180度。
当z轴正向朝着y轴正向旋转时，该角度是正值；当z轴正向朝着y轴负向旋转时，该角度是负值。

假设将手机屏幕朝上水平放在桌子上，如果桌子是完全水平的，该角度应该是0。    
假如从手机顶部抬起，直到将手机沿x轴旋转180度（屏幕向下水平放在桌面上），这个过程中，该角度值会从0变化到-180。
如果从手机底部开始抬起，直到将手机沿x轴旋转180度（屏幕向下水平放在桌面上），该角度的值会从0变化到180。       

第三个角度：Roll (degrees of rotation around the y axis).

表示手机左侧或右侧翘起的角度。

当手机绕着自身y轴旋转时，该角度值将会发生变化，取值范围是-90到90度。
当z轴正向朝着x轴正向旋转时，该角度是正值；
当z轴正向朝着x轴负向旋转时，该角度是负值。 
  */

function updateTagPosition(azimuth, pitch, roll, poiDireAngle, tagId, heightDiff) {
    // 预设各个旋转方向的相机视角
    var AZIMUTH_CAMERA_VIEW = 80;
    // var ROLL_CAMERA_VIEW = 90;
    var PITCH_CAMERA_VIEW = 90;
    // var BASELINE_DISTANCE = 50;

    // 俯仰最大最小角度值
    var MIN_PITCH = -125;
    var MAX_PITCH = -35;
    var DIFF_PITCH = 20;


    // 翻滚最大最小角度值
    // var MIN_ROLL  = 0;
    // var MAX_ROLL  = 180;

    // 旋转一周角度
    var CIRCLE_ANGLE = 360;

    var tag = document.getElementById(tagId);
    var AZIMUTH_DIFF = (viewportSize.width - tag.clientWidth) * 90 / viewportSize.width;

    // 对传入传感器参数取整
    azimuth = Math.round(azimuth);
    pitch = Math.round(pitch);
    roll = Math.round(roll);
    poiDireAngle = Math.round(poiDireAngle);

    // 手机方位角azimuth(0~360)     
    // 调整azimuth
    azimuth = azimuth - AZIMUTH_DIFF;

    if (azimuth < 0) {
        azimuth = CIRCLE_ANGLE + azimuth;
    }

    /**
     * 根据旋转角度实时改变标签在屏幕中位置
     *
     * @param {number} azimuthDiff
     * @param {number} pitchDiff
     */
    function fixedTagPosition(azimuthDiff, pitchDiff) {
        // 获取屏幕宽高
        var Sw = viewportSize.width;
        var Sh = viewportSize.height;

        // 获取标签宽高度
        // var tagWidth = tag.clientWidth;
        // var tagHeight = tag.clientHeight;

        var moveWidthRatio = azimuthDiff / AZIMUTH_CAMERA_VIEW;
        var moveHeightRatio = pitchDiff / PITCH_CAMERA_VIEW;

        // 取整数宽高
        var moveWidth = Math.round(moveWidthRatio * Sw);
        var moveHeight = Math.round(moveHeightRatio * Sh);

        heightDiff = heightDiff || "";

        tag.style.top = moveHeight + heightDiff + "px";
        tag.style.left = moveWidth + "px";

    }

    // 手机方位角线的摄像头始终顺时针指向
    var azimuthDiff = 0;
    var pitchDiff = 0;
    // 求POI方位角与相机开始角cameraStartViewAngle之间的角度azimuthDiff


    var cameraStartViewAngle = azimuth + (180 - AZIMUTH_CAMERA_VIEW) / 2;

    if (pitch > MIN_PITCH - DIFF_PITCH && pitch < MAX_PITCH + DIFF_PITCH) {
        // 坐标轴翻转后对pitch的处理
        pitchDiff = MAX_PITCH - pitch;

        if (cameraStartViewAngle > CIRCLE_ANGLE - 90 && poiDireAngle < 90) {

            azimuthDiff = poiDireAngle - cameraStartViewAngle + 360;
            fixedTagPosition(azimuthDiff, pitchDiff);
        } else {
            // fourth quadrant
            azimuthDiff = poiDireAngle - cameraStartViewAngle;
            fixedTagPosition(azimuthDiff, pitchDiff);
        }
    } else {
        return;
    }
}


// /**
//  * 转换二维坐标系X,Y轴到三维坐标系X,Y轴
//  * 
//  * @param {number} x 
//  * @param {number} y 
//  * @returns 
//  */
// function transformCoord(x, y) {
//     var pos = new THREE.Vector3();
//     var vecXZ = x - window.innerWidth/2;

//     // 如果 
//     pos.x = vecXZ*Math.cos((1/4)*Math.PI);
//     pos.y = window.innerHeight/2 - y;
//     pos.z = vecXZ*Math.cos((1/4)*Math.PI);
//     return pos;
// }
