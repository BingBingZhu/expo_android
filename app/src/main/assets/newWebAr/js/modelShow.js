
// 模型预览初始化
var viewScene, viewCamera, viewRenderer, orbitControls, viewClock;
var info = document.getElementById("showDetailInfo");
var cancelIcon = document.getElementById('cancelShow');
var mask = document.getElementById("mask");

// var tags = document.getElementsByClassName("buildingTag")
var tag = document.querySelector('.buildingTag');
var wrap = document.getElementById('wrap');

function initViewEnv() {
    viewScene = new THREE.Scene();

    viewCamera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, 0.1, 1000);

    // 构造渲染器
    viewRenderer = new THREE.WebGLRenderer({alpha: true, antialias: true});
    viewRenderer.setClearColor(new THREE.Color(0x000000), 0);
    viewRenderer.setPixelRatio(window.devicePixelRatio);
    viewRenderer.setSize(window.innerWidth, window.innerHeight);

    viewCamera.position.x = 20;
    viewCamera.position.y = 30;
    viewCamera.position.z = 40;
    viewCamera.lookAt(viewScene.position);
    orbitControls = new THREE.OrbitControls(viewCamera, viewRenderer.domElement);
    orbitControls.enableDamping = true;
    orbitControls.dampingFactor = 0.25;
    orbitControls.enableZoom = false;
    orbitControls.maxPolarAngle = Math.PI/2;
    viewClock = new THREE.Clock();
    
    var ambiLight = new THREE.AmbientLight(0xaaaaaa);
    viewScene.add(ambiLight);
    var spotLight = new THREE.DirectionalLight(0xffffff);
    spotLight.position.set(30, 30, 40);
    spotLight.intensity = 1.5;
    viewScene.add(spotLight);

    info.appendChild(viewRenderer.domElement);

}

function showModel(modelName) {
    // 删除已有模型
    // 添加模型
    // 加载模型材质
    var textureLoader = new THREE.TextureLoader();
    textureLoader.setPath('assets/models/');
    var modelTexture =  textureLoader.load(modelName + '.png');
    modelTexture.minFilter = THREE.LinearFilter;
    
    var mtlLoader = new THREE.MTLLoader();
    mtlLoader.setPath('assets/models/');
    // mtlLoader.load(modelName + '.mtl', function (mtl) {

    // 加载模型
    var objLoader = new THREE.OBJLoader();
    // objLoader.setMaterials(mtl)
    objLoader.setPath('assets/models/');

    objLoader.load(modelName+'.obj', function (loadedObj) {

        var showModel = loadedObj.children[0];
        showModel.material.map = modelTexture;
        showModel.position.set(0,0,0);
        showModel.scale.set(0.00025, 0.00025, 0.00025);
        viewScene.add(loadedObj);
        
        // console.log(showModel)
        // var axesHelper = new THREE.AxesHelper(15)
        // showModel.add(axesHelper)
    });
} 

function viewRender() {
    var delta = viewClock.getDelta();
    orbitControls.update(delta);
    // orbitControls.update()
    requestAnimationFrame(viewRender);
    viewRenderer.render(viewScene, viewCamera);
}


function previewModel(modelName) {
    mask.style.display = 'block';
    mask.style.zIndex = '111';
    info.style.display = 'block';
    info.style.zIndex = '112';
    cancelIcon.style.display = 'block';
    showModel(modelName);
}

// 根据点击标签不同更换不同模型
// Array.from(tags).forEach(function(tag) {
tag.addEventListener('click', function(e) {

    if(destPoi.typeNum == 'ar_mission') {
        e.preventDefault();
        return; 
    }
  
    var infoNode = e.currentTarget;

    // var sceneType = infoNode.getAttribute('data-scenetype');
    var modelName = infoNode.getAttribute('data-modelname');
  
    if(modelName !== ''){
        previewModel(modelName);

        // 隐藏可点击标签
        wrap.style.zIndex = '1';
    }
});
// })

// 点击取消预览
cancelIcon.addEventListener('click', function(e) {
    info.style.display = 'none';
    mask.style.display = 'none';
    cancelIcon.style.display = 'none';
    wrap.style.zIndex = '2';
  
});


window.addEventListener('load', function (e) {
    initViewEnv(); // 模型预览环境初始化
    viewRender();
});
