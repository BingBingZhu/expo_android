# 光团实景导航功能与实现方式

1. 光团动作展示： 使用Three.js引擎渲染光团模型，使用引擎动画系统控制光团动画播放
2. 底部罗盘与方向箭头指示：使用程序绘制罗盘与箭头，利用手机方位传感器和重力传感器指示方向
3. 上部标签展示：利用方位传感器和重力传感器在屏幕正确位置显示标签
4. 标签展开介绍：介绍中模型的旋转和缩放同样使用Three.js引擎控制
5. 实景导航路线：使用程序绘制并根据手机传感器数据变化做相应旋转操作
6. 光团在实景中：根据传感器数据控制光团实景空间位置
7. 底部导航信息提示：从服务器获取数据并展示
