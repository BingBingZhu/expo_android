// 转弯点判定距离
var NAV_TURN_RANGE = 20; 
// 终点距离判定
var NAV_END_RANGE = 8;
var UPDATE_TIME = 3;

var ARROW_INIT_ANGLE = 2.35;
var COMPASS_INIT_ANGLE = 135;
// var COMPASS_INIT_ANGLE = -45;
// var COMPASS_INIT_ANGLE = 315;


var remindCount = 0;
var nextPosIndex = 0;

// 总共路程，用时
var totalDist, totalTime; 

// 用户当前位置
var userPos; 
// 存储所有拐点和终点
var keyPosList; 
// 路线下一个拐点
var nextKeyPos; 

// 动作gif关联表
var actionGifLists = {};

// 动作声音关联表
var actionAudioLists = {};

// 动作声音关联表
var actionRelList = {}; 

// 动画模型与声音路径
var actionBasePath = 'http://47.95.215.6/res/3d/';
var audioBasePath = 'http://47.95.215.6/res/';

var circle, arrow, mainScene, mixer;
 
// 终点poi数据结构
var destPoi = {
    id: '1111',
    tagType: 'div',
    poiName: '',
    distance: '',
    typeNum: '',
    location: {
        longitude: 116.332959,
        latitude: 39.980905
    },
    config: {
        'data-scenetype': '',
        'data-modelname': 'house'
    }
};
 

var emitter = document.getElementById('eventEmitter'); 
var navAction = document.getElementById('navAction');

// 添加点击事件监听器
var pathInfoTag = document.getElementById('pathInfo'); 
var backWarn = document.querySelector('#backWarn');
var cancelBack = document.querySelector('.cancelBack');
var cancelBtn = document.querySelector('.cancelBtn');
var okBtn = document.querySelector('.okBtn');
var pathBack = document.querySelector('.cancelShowInfo');

// 退出事件处理
pathBack.addEventListener('click', exitNav, false);
cancelBack.addEventListener('click', exitNav, false);
cancelBtn.addEventListener('click', cancelExitNav, false);
okBtn.addEventListener('click', confirmExitNav, false);

var images = [];
var gifRelList = [
    'action_01/action_01_1.gif',
    'action_06/action_06_1.gif',
    'action_06/action_06_2.gif',
    'action_07/action_07_1.gif',
    'action_08/action_08_1.gif',
    'action_08/action_08_2.gif'
];

var interactiveList = [
    ['action_08', 2300],
    ['action_08_1', 4600],
    ['action_08_2', 2500]
];


var isBackStatus = false;  // 是否确定后退期间
var isStartStatus = false; // 是否开始动画期间
var isNavOver = false; // 是否到达终点

// GPS信号强弱变量
var weakGPS;
// var strongGPS;
var interactiveStatus = false;// 是否处于交互状态

/**
 * 异步加载辅助动作gif文件
 * 
 * @param {Array} gifRelList 
 */
function preloadRelGifAssets(gifRelList) {
    for(var i=0; i<gifRelList.length; i++) {
        images[i] = new Image();
        images[i].src = actionBasePath + gifRelList[i];
    }
}
preloadRelGifAssets(gifRelList);


/**
 * 返回当前动画元素的actionName
 * 
 * @returns {string}
 */
function getGifAnimaShortSrc() {
    return navAction.getAttribute('src').slice(-13, -4);
}


/* --------------终端与实景界面数据交互接口------------- */


/**
* 对传递过来的sensorData进行处理与应用，实时更新标签位置与罗盘旋转角度
* 
* @param {number} azimuth 
* @param {number} pitch 
* @param {number} roll 
*/

function sendSensorData(azimuth, pitch, roll) {
    // 更新终点标签显示位置
    // testInfo({azimuth: azimuth, type: typeof azimuth});
    azimuth = Number(azimuth);
    pitch = Number(pitch);
    var tag =  document.getElementById(destPoi.id);
    if(destPoi.poiName && tag && userPos) {
        var poiAngle = getHeadingDirectionDegrees(userPos, destPoi.location);   //  终点与当前位置方位角
        updateTagPosition(azimuth, pitch, roll, poiAngle, destPoi.id);    
    }

    // 更新罗盘旋转角度
    if(circle) {
        updateCompassOrientation(azimuth);
    }

}

/**
 * 获取用户当前位置
 *  
 * @param {number} longitude 
 * @param {number} latitude 
 */
function sendUserPosition(longitude, latitude, gpsStatus) {
    userPos = {"longitude": longitude, "latitude": latitude};

    // 根据GPS信号强弱展示动作
    if(gpsStatus == 0) {
        replaceActionGif('action_07');
        weakGPS = true;
    }else if(gpsStatus == 1){
        if(weakGPS) {
            replaceActionGif('action_07_1');
        }
        weakGPS = false;
    }else{
        return;
    }
  
}



/**
 * 根据终端提供数据修改目的地poi数据域
 */
function modifyPoiField(dest) {
    destPoi.poiName = dest.name;
    destPoi.typeNum = dest.type; // ar_mission 不带模型预览，venues带模型预览
    destPoi.location.longitude = dest.longitude;
    destPoi.location.latitude  = dest.latitude;
    destPoi.config['data-scenetype'] = dest.type;
}


/**
 * 创建目的地兴趣点标签，待重构
 * 
 * @param {Object} poi 
 */
function genPoiTag(poi) {
    // 初始化
    var clickableEle = '';

    if(poi.typeNum == 'ar_mission') {
        clickableEle = '<i></i>';
    }

    var innerTag = `
  <p>${poi.poiName}</p>
  <p id="destination"><span>前方${poi.distance}<span>${clickableEle}</p>
  <div><div> 
  `;

    createTag(poi.id, poi.tagType, innerTag, poi.config); 
}

/**
 * 处理原始路线数据并提取出关键点坐标
 * 
 * @param {Object} paths  原始路线规划数据
 * @returns {Array} keyPosList: [{
 *    action: string, 
 *    orientation: string, 
 *    keyPos: {longitude: number, latitude: number}
 *  },
 * ...]提取出的关键点数据列表
 */
function genKeyPointsList(paths) {
    // 路段数据
    var path = paths.route.paths[0];
  
    var steps = path.steps;

    // 给restDist和restTime初始值
    totalDist = path.distance;
    totalTime = path.duration;

    // 提取所有路段关键位置坐标
    var keyPosList = steps.map(function(step, index) {
        var orientation = step.orientation;
        var polyline = step.polyline;

        var action   = step.action;
        var lastPosList  = polyline.split(';').pop().split(',');

        var lastPos = {longitude: parseFloat(lastPosList[0]), latitude: parseFloat(lastPosList[1])};

        return {
            action: action, 
            orientation: orientation,
            keyPos: lastPos,
            distance: step.distance,
            duration: step.duration, 
            instruction: step.instruction
        };
    });

    return keyPosList;
}


/* -----------------加载动画声音及其调配---------------- */

/**
 * 网络请求动作模型声音关联数据
 * 
 */
function getTouristActionListObj() {
    axios({
        method: 'post',
        url: 'http://47.95.215.6:8080/Api/Tourist/GetTouristActionList',
        data: {
            Uid: '',
            Ukey: ''
        }
    }).then(function(response) {
        var ObjList = response.data.ObjList;
        console.log(ObjList);

        ObjList.forEach(function(ele) {
            actionRelList[ele.caption] = {
                action: ele.command, 
                picUrl: ele.picurl,
                soundUrl: ele.soundurl
            };

        }, this);

        // 加载gif动画和声音
        loadActionGifLists(actionRelList);
        loadActionAudioLists(actionRelList);
 
    }).catch(function (error) {
        console.log(error);
    });
}


/**
 * 根据动作与资源的关联信息缓存所有的Gif动画
 * 
 * @param {Object} actionRelList 
 */
function loadActionGifLists(actionRelList) {
    for(var relObj in actionRelList) {
        loadActionGif(actionRelList[relObj].action, '.gif');
    }
}


/**
 * 根据动作与资源的关联信息缓存所有的声音文件
 * 
 * @param {Object} actionRelList 
 */
function loadActionAudioLists(actionRelList) {
    for(var relObj in actionRelList) {
        if (actionRelList[relObj].soundUrl == "") {
            actionAudioLists[actionRelList[relObj].action] = "";
        }else{
            var audioMedia = new Audio(audioBasePath + actionRelList[relObj].soundUrl); 
            actionAudioLists[actionRelList[relObj].action] = audioMedia;   
        }
    }

}

/**
 * 加载gif图
 * 
 * @param {string} actionName 
 * @param {string} imgExt 
 */
function loadActionGif(actionName, imgExt) {
    var gifUrl = actionBasePath + actionName + '/' + actionName + imgExt;

    actionGifLists[actionName] = new Image();
    actionGifLists[actionName].src = gifUrl;

    actionGifLists[actionName].onload = function(e) {
    // 当action_01加载成功，调用开始动画
        if(actionName === 'action_01') {
            emitter.dispatchEvent(enterNav);
        }
    };

}


/**
 * 替换gif动画
 * 
 * @param {string} gifSrc
 * @param {Object} config
 */
function activeGifAnimation(actionName) {
    var actionGifSrc = actionBasePath + actionName + '/' + actionName + '.gif';
    navAction.setAttribute('src', actionGifSrc);
}


/**
 * 替换navAction元素的img源
 * 
 * @param {string} shortSrc 
 */
function replaceActionGif(shortSrc) {
    var mainSrc = shortSrc.slice(0, -2);
    var src = actionBasePath + mainSrc + '/' + shortSrc + '.gif';

    if(shortSrc.length == 9) {
        src = actionBasePath + shortSrc + '/' + shortSrc + '.gif';
    }

    navAction.setAttribute('src', src);  
}



/**
 * 激活动作对应的音频, 默认单次播放
 * 
 * @param {string} relAction 和音频关联的动作名
 * @param {Object} audioConfig {loop: false, autoplay: false}对音频播放的配置选项
 */
function activeAudio(relAction, audioConfig) {
    var audioMedia = actionAudioLists[relAction];

    for(var item in actionAudioLists) {
        if(actionAudioLists[item] == audioMedia) {
            if(audioMedia.paused) {
                audioMedia.play();      
            }
        }else if(actionAudioLists[item] != ""){
            actionAudioLists[item].pause();
        }
    }
}


/**
 * 判断当前用户位置是否在指定点范围内
 * 
 * @param {dist} 当前点到nextKeyPos距离
 * @returns 
 */
function isWithin(dist, range) {
    if(dist > range) {
        return false;
    }else{
        return true;
    }
}

 

/**
 * 定时判断距离范围
 * 导航数据更新，导航核心逻辑
 * 
 */
function updateRangeJudge() {
    // 计算当前点与下一个转弯点或终点距离
    var dist = distance(userPos, nextKeyPos);

    // 更新路线信息提示
    var tip = calcPathInfo(dist, nextKeyPos);
    updatePathInfoTip(tip.instruction, tip.restDist, tip.restTime);
    updateTargetPoiInfo(tip.restDist);

    // 判断当前是否在转弯点或终点范围内
    var isRange = isWithin(dist, NAV_TURN_RANGE);

    var isEnd = isWithin(dist, NAV_END_RANGE);

    if (isRange) {
        remindCount++;

        // 查找对应nextKeyPos的action
        var updateOk = false;
        keyPosList.forEach(function(item, index) {

            // 此条件只能判定为true有且仅有一次，会判断keyPosList.length - 1次
            if(item.keyPos == nextKeyPos && !updateOk) {
                // 获取对应action, 此action是高德路段对应action，然后根据action去查找调用对应动画模型
                var mapAction = item.action;

                // 终点处理, 循环播放结束动画
                if(mapAction.length === 0 && !isNavOver && isEnd) {
                    activeGifAnimation(actionRelList['终点'].action);
                    activeAudio(actionRelList['终点'].action); 
                    isNavOver = true;               
                    return;
                } 

                // 未到终点，转弯点处理
                if(!isNavOver && mapAction.length != 0) { 

                    // 获取对应动作名
                    var gifAction = actionRelList[mapAction].action;
                    // 激活对应动画模型
                    var gifAnimaShortName = getGifAnimaShortSrc();

                    // 箭头指向下一点
                    if(index < keyPosList.length - 1) {
                        updateArrowOrientation(userPos, keyPosList[index+1].keyPos);            
                    }
          
                    // 避免动画未播完又重新播放
                    if(gifAnimaShortName != gifAction) {
                        activeGifAnimation(gifAction);
                        activeAudio(gifAction);   
                    }
                }

                // 何时更新下一个keyPos?
                if(remindCount > UPDATE_TIME) {
                    // 终点处理
                    if(index == keyPosList.length - 1)  return;

                    // 更新下一个点
                    nextKeyPos = keyPosList[index+1].keyPos;
                    updateOk = true;
                }
            }else{
                return; 
            }
        }, this);

    }else{
    // 直行
        remindCount = 0;

        updateArrowOrientation(userPos, nextKeyPos);
    
        if(!isBackStatus && !isStartStatus && !weakGPS && !interactiveStatus) {
            activeGifAnimation('action_02');
        }
    // 只需播放一次音频

    }
}


/**
 * 初始化主场景WebGL环境
 * 
 */
function initMainScene() { 
    // 创建场景
    var scene = new THREE.Scene();
    mainScene = scene;
    // 构造透视相机
    var camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, 0.1, 1000);
    // 相机位置
    camera.position.x = 30;
    camera.position.y = 30;
    camera.position.z = 30;
    camera.lookAt(new THREE.Vector3(0, 0, 0));

    // 构造渲染器
    var webGLRenderer = new THREE.WebGLRenderer({alpha: true, antialias: true});
    webGLRenderer.setPixelRatio(window.devicePixelRatio);
    webGLRenderer.setClearColor(0xffffff, 0);
    
    webGLRenderer.setSize(window.innerWidth, window.innerHeight);

    // 添加太阳光源
    var spotLight = new THREE.DirectionalLight(0xffffff);
    spotLight.position.set(10, 40, 10);
    spotLight.intensity = 1;
    scene.add(spotLight);

    // 添加环境光
    var ambient = new THREE.AmbientLight(0x404040);
    scene.add(ambient);

    document.getElementById("WebGL-output").appendChild(webGLRenderer.domElement);

    // var step = 0.03;

    // 模型配置数据
    var controls = new function () {
        this.circlePosX = 15;
        this.circlePosY = 6;
        this.circlePosZ = 15;

        // 使用CylinderGeometry时罗盘配置
        this.radiusTop = 4;
        this.radiusBottom = 4;
        this.height = 0.0001;
        this.radialSegments = 500;
        this.heightSegments = 30;


        this.arrowPosX = 0;
        this.arrowPosY = 1;
        this.arrowPosZ = 0;
        this.arrowRatationRadian = 0;
        // this.INIT_ANGLE = 2.35;

    };


    // 生成罗盘
    function genCompass() {
    // 加载罗盘纹理
        var compassTexture = new THREE.TextureLoader().load('./assets/img/compass.png');
        compassTexture.minFilter = THREE.LinearFilter;

        var circleGeo = new THREE.CylinderGeometry(controls.radiusTop, controls.radiusBottom, controls.height, controls.radialSegments, controls.heightSegments);
        var circleMaterial = new THREE.MeshBasicMaterial();
        circle = new THREE.Mesh(circleGeo, circleMaterial);
        circle.material.map = compassTexture;
        circle.material.transparent = true;
        circle.material.side = THREE.DoubleSize;

        scene.add(circle);
        circle.position.set(controls.circlePosX, controls.circlePosY, controls.circlePosZ);
    }  

    function genArrow() {
    // 加载箭头材质
        var mtlLoader = new THREE.MTLLoader();

        mtlLoader.setPath('assets/models/');
        mtlLoader.load('arrow.mtl', function (mtl) {

            // 加载箭头模型
            var objLoader = new THREE.OBJLoader();

            objLoader.setMaterials(mtl);
      
            objLoader.setPath('assets/models/');
            objLoader.load('arrow.obj', function (loadedObj) {

                arrow = loadedObj.children[0];
                arrow.position.set(controls.arrowPosX, controls.arrowPosY, controls.arrowPosZ);

                scene.add(loadedObj);
        
                // 对象组合
                circle.add(arrow);
            });

        });
    }

    genCompass();
    genArrow();

    // 循环渲染
    function render() {
        requestAnimationFrame(render);

        // 更新动画播放
        webGLRenderer.render(scene, camera);
    }
    render();

    // function initStats() {

    //     var stats = new Stats();
    //     stats.setMode(0); // 0: fps, 1: ms

    //     // Align top-left
    //     stats.domElement.style.position = 'absolute';
    //     stats.domElement.style.left = '0px';
    //     stats.domElement.style.top = '0px';

    //     document.getElementById("Stats-output").appendChild(stats.domElement);

    //     return stats;
    // }

} 
  

/**
 * 更新箭头指向，指示箭头指向下一个拐点而非终点
 * 
 * @param {Object} userPos {longitude: number, latitude: number}
 * @param {Object} nextKeyPos {longitude: number, latitude: number}
 */
function updateArrowOrientation(userPos, nextKeyPos) {
    // 获取目的地与当前位置的方位角
    var nextPosAngel = getHeadingDirectionDegrees(userPos, nextKeyPos);
    var arrowAzimuth = toRadians(nextPosAngel);

    arrow.rotation.y = ARROW_INIT_ANGLE - arrowAzimuth;  
}


/**
 * 更新罗盘旋转方位，使其校准方位
 * 
 * @param {number} azimuth 
 */
function updateCompassOrientation(azimuth) {
    var rad = toRadians(azimuth + COMPASS_INIT_ANGLE);
    circle.rotation.y = rad;
}

/**
 * 更新目的距离
 * 
 * @param {number} dist 
 */
function updateTargetPoiInfo(dist) {
    var destination = document.querySelector('#destination > span');
    destination.innerHTML = '前方' + dist + 'M';
}


/**
 * 计算剩余距离及时间
 * 
 * @returns {
     instruction: instruction,
     restDist: restDist,
     restTime: restDist/1.2
  }
 */
function calcPathInfo(distance, nextKeyPos) {
    var walkedDist = 0; // 走过的整段step路长
    var totalWalkedDist = 0; // 总共走过的路长
    var stepWalkDist = 0;  // 每段路程已走过的路长
    var instruction;

    keyPosList.forEach(function(item, index) {
        if(item.keyPos == nextKeyPos) {
            // 获取instruction
            instruction = item.instruction;

            // 加上前面step的distance
            var stepIndex = index; 
            while(stepIndex > 0) {
                walkedDist += parseInt(keyPosList[stepIndex-1].distance);
                stepIndex--;
            }

            stepWalkDist  = parseInt(item.distance) - parseInt(distance);

            // 计算走过路程
            totalWalkedDist = walkedDist + stepWalkDist; 
        }else{
            return;
        }
    });
 
    var restDist = parseInt(totalDist) - totalWalkedDist;
    // 替换字符串中的数字
    instruction = instruction.replace(/[0-9]+/g, parseInt(distance));

    return {
        instruction: instruction,
        restDist: restDist,
        restTime: (restDist/1.2/60).toFixed(2)
    };
}


var pathInstruction = document.querySelector('#instruction');
var distanceEle = document.querySelector('#restDist');
var timeEle = document.querySelector('#restTime');


/**
 * 行走过程中更新路线信息展示
 * 
 * @param {string} instruction 路线指示信息
 * @param {number} dist 目的地距离，当大于1000M时，单位采用KM，当小于1000M单位M，默认单位M
 * @param {number} time 步行时间，单位分钟
 */
function updatePathInfoTip(instruction, dist, time) {
    pathInstruction.innerHTML = instruction;
    // 
    distanceEle.innerHTML = '全程剩余: ' + dist + 'M';
    timeEle.innerHTML = '约' + time + '分钟';
}


/**
 * 进入动画
 * 
 * @param {string} enterAction 
 */
var enterNav = new Event('enterNav');
emitter.addEventListener('enterNav', function(e) {
    navWelcome();
});


/**
 * 开始进入导航
 * 
 * @param {string} enterAction 
 */
function enterNavAnima(enterAction, enterTime) {
    activeGifAnimation(enterAction); // 时间控制
    navAction.style.display = 'block';
    isStartStatus = true;
    // 播放开始导航语音
 
    setTimeout(function() {
    // 跳到欢迎动作
        replaceActionGif('action_01_1');
        activeAudio(enterAction);

        setTimeout(function() {
            // 直行
            activeGifAnimation('action_02');
            activeAudio('action_02'); 
            isStartStatus = false;
        }, 6120);

    }, enterTime);
}


/**
 * 开始进入导游流程
 * 
 * @param {string} welcomeAction 
 */
function navWelcome() {
    // 调用开始动画
    enterNavAnima('action_01', 2520);


    setTimeout(function() {
        setInterval(updateRangeJudge, 2000);    
    },  3000);
}


function popPathInfoTip() {
    pathInfoTag.style.display = 'block';
}

function hidePathInfoTip() {
    pathInfoTag.style.display = 'none';  
}


function popExitPrompt() {
    backWarn.style.display = 'block';
}

function hideExitPrompt() {
    backWarn.style.display = 'none';
}

// 点击路线back键,中途退出导游处理
function exitNav() {
    // 导航结束，点击back,直接退出
    if(isNavOver) {
        confirmExitNav();
    }

    // 弹出确认框
    activeGifAnimation('action_06');
    activeAudio('action_06');
    isBackStatus = true;
    setTimeout(function() {
        replaceActionGif('action_06_1');
    }, 2880);


    hidePathInfoTip();
    popExitPrompt();
}

// 点击取消
function cancelExitNav() {
    // 隐藏框
    hideExitPrompt();
    popPathInfoTip();

    replaceActionGif('action_06_2');

    setTimeout(function() {
        activeGifAnimation('action_02');
        isBackStatus = false;
    }, 2280);
}

/**
 * 暂停所有音频
 * 
 */
function disabledAllAudio() {
  
    for(var item in actionAudioLists) {
        if(actionAudioLists[item]) {
            actionAudioLists[item].pause();
        }
    }
}


// 点击确定，触发退出事件
function confirmExitNav() {
    // 确定退出时,暂停音频播放
    disabledAllAudio();
    // 手动触发退出

    window.webkit.messageHandlers.goBack.postMessage(null);
}


initMainScene(); // 初始化WebGL环境

// 初始化箭头指向及路线信息

var arrowTimer = setInterval(function() {
    if(userPos && arrow && nextKeyPos) {
        updateArrowOrientation(userPos, nextKeyPos);

        var dist = distance(userPos, nextKeyPos);

        // 更新路线信息提示
        var tip = calcPathInfo(dist, nextKeyPos);
        updatePathInfoTip(tip.instruction, tip.restDist, tip.restTime);
        updateTargetPoiInfo(tip.restDist);
  
        clearInterval(arrowTimer);
    }
}, 1000);


// 直行状态下互动，互动动作随机出现
navAction.addEventListener('click', function() {
    var shortSrc = getGifAnimaShortSrc();
    if(shortSrc == 'action_02') {
    // 随机出现interactiveList中的动作
    // 获取随机数[0,3)
        var randomNum = parseInt(Math.random()*3);

        replaceActionGif(interactiveList[randomNum][0]);
        interactiveStatus = true;

        // 定时替换成直行动作
        setTimeout(function() {
            replaceActionGif('action_02');
            interactiveStatus = false;
        }, interactiveList[randomNum][1]);

    }else{
        return;
    }

});


/**
 * 获取目的地相关数据
 * 
 * @param {JSON} destInfo 
 */
function getDestInfo(destInfo) {
    // testInfo(destInfo)
    modifyPoiField(JSON.parse(destInfo));
    genPoiTag(destPoi);

}

/**
 * 获取路线数据
 * 
 * @param {JSON} paths 
 */
function getPaths(paths) {
    keyPosList = genKeyPointsList(JSON.parse(paths));

    //初始化下一路径点
    nextKeyPos = keyPosList[0].keyPos;
}



// window.addEventListener('load', function(e) {


window.webkit.messageHandlers.canCallJS.postMessage(true);

getTouristActionListObj(); // 加载所需动画声音数据


// })