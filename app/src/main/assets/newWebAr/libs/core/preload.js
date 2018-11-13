// 预加载图片资源
function preload() {
  function imgPreloader(imgList) {
    for(var i=0; i<imgList.length; i++) {
      images[i] = new Image()
      images[i].src = imgList[i]
    }
  }
  imgPreloader(imgList)
}
var images = []
