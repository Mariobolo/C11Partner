# C11Partner JS 接口文档

> 🔌 前后端接口契约文档，所有 JS 可调用的 Android 方法
>
> 最后更新：2026-07-09
>
> 调用方式：`Android.methodName(参数)`（通过 WebViewBridge 的 `@JavascriptInterface`）

---

## 一、车控接口

> 委托链：WebViewBridge → CarControlBridge → CarControlManager

### 1.1 灯光控制

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `setLowBeamLight(on)` | `boolean` | `boolean` | 近光灯开关 |
| `isLowBeamLightOn()` | — | `boolean` | 查询近光灯 |
| `setRearFogLight(on)` | `boolean` | `boolean` | 后雾灯开关 |
| `isRearFogLightOn()` | — | `boolean` | 查询后雾灯 |
| `setPositionLight(on)` | `boolean` | `boolean` | 示廓灯开关 |
| `isPositionLightOn()` | — | `boolean` | 查询示廓灯 |
| `setPedestrianAlert(on)` | `boolean` | `boolean` | 行人警示音开关 |
| `isPedestrianAlertOn()` | — | `boolean` | 查询行人警示 |

### 1.2 驾驶模式

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `setDriveMode(mode)` | `int(0-5)` | `boolean` | 0=经济 1=舒适 2=运动 3=雪地 4=泥地 5=沙地 |
| `setGuardMode(on)` | `boolean` | `boolean` | 守护模式 |
| `setRestMode(on)` | `boolean` | `boolean` | 小憩模式 |
| `setCampingMode(on)` | `boolean` | `boolean` | 露营模式 |
| `setPowerSaveMode(on)` | `boolean` | `boolean` | 省电模式 |
| `setSentinelMode(on)` | `boolean` | `boolean` | 哨兵模式 |

### 1.3 空调控制

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `setAcEnabled(on)` | `boolean` | `boolean` | 空调开关 |
| `isAcEnabled()` | — | `boolean` | 查询空调状态 |
| `setWindLevel(level)` | `int(1-7)` | `boolean` | 风量等级 |
| `getWindLevel()` | — | `int` | 获取风量 |
| `setDriverTemp(temp)` | `int(16-30)` | `boolean` | 主驾温度（摄氏度） |
| `getDriverTemp()` | — | `int` | 获取主驾温度 |
| `setPassengerTemp(temp)` | `int(16-30)` | `boolean` | 副驾温度 |
| `getPassengerTemp()` | — | `int` | 获取副驾温度 |
| `toggleAirConditioning()` | — | `boolean` | 切换空调 |
| `toggleDefrost()` | — | `boolean` | 切换除霜 |
| `toggleMute()` | — | `boolean` | 切换静音 |
| `increaseTemperature()` | — | `boolean` | 温度+1 |
| `decreaseTemperature()` | — | `boolean` | 温度-1 |
| `increaseWindSpeed()` | — | `boolean` | 风量+1 |
| `decreaseWindSpeed()` | — | `boolean` | 风量-1 |
| `getAcInfo()` | — | `String(JSON)` | 空调信息（风量/温度/开关等） |
| `setMaxCooling(on)` | `boolean` | `boolean` | 极速制冷 |
| `isMaxCoolingOn()` | — | `boolean` | 查询极速制冷 |
| `toggleAC()` | — | `boolean` | 切换空调（toggleAirConditioning别名） |

### 1.4 座椅控制

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `setDriverSeatHeating(level)` | `int(0-3)` | `boolean` | 0=关 1-3=加热等级 |
| `setPassengerSeatHeating(level)` | `int(0-3)` | `boolean` | 副驾座椅加热 |
| `setDriverSeatVentilation(level)` | `int(0-3)` | `boolean` | 主驾座椅通风 |
| `setPassengerSeatVentilation(level)` | `int(0-3)` | `boolean` | 副驾座椅通风 |
| `setSteeringWheelHeating(on)` | `boolean` | `boolean` | 方向盘加热 |

### 1.5 后视镜控制

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `foldMirrors()` | — | `boolean` | 折叠后视镜 |
| `unfoldMirrors()` | — | `boolean` | 展开后视镜 |
| `setMirrorHeating(on)` | `boolean` | `boolean` | 后视镜加热 |

### 1.6 音量控制

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `setCallVolume(v)` | `int(0-15)` | `boolean` | 蓝牙电话音量 |
| `getCallVolume()` | — | `int` | 获取电话音量 |
| `setNaviVolume(v)` | `int(0-15)` | `boolean` | 导航音量 |
| `getNaviVolume()` | — | `int` | 获取导航音量 |
| `setMusicVolume(v)` | `int(0-15)` | void | 媒体音量 |
| `getMusicVolume()` | — | `int` | 获取媒体音量 |
| `volumeUp()` | — | void | 音量+1 |
| `volumeDown()` | — | void | 音量-1 |

### 1.7 氛围灯/夜间模式

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `setAmbientLightEnabled(on)` | `boolean` | `boolean` | 氛围灯开关 |
| `isAmbientLightEnabled()` | — | `boolean` | 查询氛围灯 |
| `setAmbientLightColor(color)` | `int(0-16)` | `boolean` | 氛围灯颜色 |
| `getAmbientLightColor()` | — | `int` | 获取氛围灯颜色 |
| `setNightMode(on)` | `boolean` | `boolean` | 夜间模式开关 |
| `isNightModeOn()` | — | `boolean` | 查询夜间模式 |

### 1.8 360全景

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `startCamera360()` | — | `boolean` | 启动360全景 |
| `setCameraOverspeedLimit(on)` | `boolean` | `boolean` | 360超速限制 |
| `isCameraOverspeedLimitEnabled()` | — | `boolean` | 查询超速限制 |

### 1.9 网络与连接

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `setWifiEnabled(on)` | `boolean` | `boolean` | WiFi开关 |
| `isWifiEnabled()` | — | `boolean` | 查询WiFi |
| `setBluetoothEnabled(on)` | `boolean` | `boolean` | 蓝牙开关 |
| `isBluetoothEnabled()` | — | `boolean` | 查询蓝牙 |

### 1.10 其他车控

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `setVideoWhileDriving(on)` | `boolean` | `boolean` | 行驶中视频 |
| `isVideoWhileDrivingEnabled()` | — | `boolean` | 查询行驶视频 |
| `setSecondaryScreenEnabled(on)` | `boolean` | `boolean` | 副屏开关 |
| `isSecondaryScreenEnabled()` | — | `boolean` | 查询副屏 |
| `setSpeechEnabled(on)` | `boolean` | `boolean` | 语音播报开关 |
| `isSpeechEnabled()` | — | `boolean` | 查询语音播报 |
| `sendVoiceCommand(cmd)` | `String` | `boolean` | 发送语音指令 |
| `isVehicleLocked()` | — | `boolean` | 查询锁车状态 |
| `isScreenOn()` | — | `boolean` | 查询屏幕状态 |
| `getCarState()` | — | `String(JSON)` | 获取车辆状态（档位/车速/胎压等） |

---

## 二、应用接口

> 委托链：WebViewBridge → AppBridge → AppUtils

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `getAppList()` | — | `String(JSON)` | 应用列表（按字母分组，含图标Base64） |
| `getAllApps()` | — | `String(JSON)` | 所有已安装应用 |
| `getUserApps()` | — | `String(JSON)` | 用户安装应用 |
| `getSystemApps()` | — | `String(JSON)` | 系统应用 |
| `getAppInfo(pkg)` | `String` | `String(JSON)` | 应用信息 |
| `getAppIcon(pkg)` | `String` | `String(Base64)` | 应用图标 |
| `isAppInstalled(pkg)` | `String` | `boolean` | 是否已安装 |
| `launchApp(pkg)` | `String` | void | 启动应用 |
| `openAppInfo(pkg)` | `String` | void | 打开应用信息页 |
| `getQuickAppList()` | — | `String(JSON)` | 快捷应用列表 |
| `addQuickApp(name, pkg, iconBase64)` | `String, String, String` | void | 添加快捷应用 |
| `removeQuickApp(pkg)` | `String` | void | 移除快捷应用 |
| `isQuickApp(pkg)` | `String` | `boolean` | 是否快捷应用 |
| `saveConfigApp(buttonId, appName, pkg, icon)` | `String, String, String, String` | void | 保存配置应用 |
| `getConfigApp(key)` | `String` | `String` | 获取配置应用 |
| `getAppListAsync(callbackId)` | `String` | void | 异步获取（通过回调） |

**getAppList() 返回 JSON 格式：**
```json
{
  "apps": [
    {
      "packageName": "com.amap.android",
      "appName": "高德地图",
      "icon": "data:image/png;base64,..."
    }
  ]
}
```

---

## 三、壁纸接口

> 委托链：WebViewBridge → WallpaperBridge → WallpaperManager

### 3.1 壁纸设置

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `getWallpaperSettings()` | — | `String(JSON)` | 获取壁纸设置 |
| `getWallpaperSettingsAsync(callbackId)` | `String` | void | 异步获取 |
| `getWallpaperSettingsV2()` | — | `String(JSON)` | V2版设置 |
| `setWallpaperType(type)` | `int` | void | 1=必应 2=本地图片 3=本地视频 5=iframe |
| `setWallpaperPath(path)` | `String` | void | 壁纸路径 |
| `setWallpaperCarouselEnabled(on)` | `boolean` | void | 轮播开关 |
| `setWallpaperCarouselInterval(sec)` | `int` | void | 轮播间隔（秒） |
| `setWallpaperFillMode(mode)` | `int` | void | 填充模式 |
| `getCurrentWallpaperUrl()` | — | `String` | 当前壁纸URL |
| `getNextWallpaperUrl()` | — | `String` | 下一张URL |
| `refreshBingWallpaper()` | — | void | 刷新必应壁纸 |
| `deleteCurrentWallpaper()` | — | `boolean` | 删除当前壁纸 |
| `pauseWallpaperCarousel()` | — | void | 暂停轮播 |
| `resumeWallpaperCarousel()` | — | void | 恢复轮播 |

### 3.2 随机壁纸

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `getRandomWallpaper()` | — | `String` | 随机壁纸URL |
| `getRandomWallpaperAsync(callbackId)` | `String` | void | 异步获取URL |
| `getRandomWallpaperBase64()` | — | `String` | 随机壁纸Base64 |
| `getRandomWallpaperBase64Async(callbackId)` | `String` | void | 异步获取Base64 |

### 3.3 壁纸分类

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `updateWallpaperCategories()` | — | void | 更新分类（从网络） |
| `updateCategoryEnabled(name, on)` | `String, boolean` | void | 启用/禁用分类 |
| `updateCategoryEnabledAsync(name, on, callbackId)` | `String, boolean, String` | void | 异步更新 |
| `getEnabledCategories()` | — | `String(JSON)` | 已启用分类 |
| `getEnabledCategoriesAsync(callbackId)` | `String` | void | 异步获取 |

### 3.4 壁纸设置同步

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `saveWallpaperCarouselSetting(on)` | `boolean` | `boolean` | 保存轮播设置 |
| `saveWallpaperCarouselSettingAsync(on, callbackId)` | `boolean, String` | void | 异步保存 |
| `saveWallpaperSwitchInterval(sec)` | `int` | `boolean` | 保存切换间隔 |
| `saveWallpaperSwitchIntervalAsync(sec, callbackId)` | `int, String` | void | 异步保存 |
| `sendWallpaperSettingsChangedBroadcast()` | — | void | 发送设置变更广播 |

---

## 四、音乐接口

> 委托链：WebViewBridge → MusicBridge → MusicNotificationListenerService

### 4.1 播放控制

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `playPauseMusic()` | — | void | 播放/暂停 |
| `nextMusic()` | — | void | 下一首 |
| `prevMusic()` | — | void | 上一首 |
| `seekTo(pos)` | `long(ms)` | void | 跳转到位置 |
| `setPlaybackSpeed(speed)` | `float` | void | 播放速度 |

### 4.2 状态查询

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `isMusicPlaying()` | — | `boolean` | 播放状态 |
| `getCurrentMusicName()` | — | `String` | 歌曲名 |
| `getCurrentMusicArtist()` | — | `String` | 歌手名 |
| `getMusicProgressInfo()` | — | `String(JSON)` | 进度信息 |
| `getSystemMusicInfo()` | — | `String(JSON)` | 系统音乐信息 |

### 4.3 可视化

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `startMusicVisualizer()` | — | void | 启动频谱可视化 |
| `stopMusicVisualizer()` | — | void | 停止可视化 |

### 4.4 播放列表

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `getPlaylist()` | — | `String(JSON)` | 播放列表 |
| `playSongAtIndex(i)` | `int` | void | 播放指定歌曲 |
| `setRepeatMode(m)` | `int` | void | 0=不循环 1=单曲 2=列表 |
| `getRepeatMode()` | — | `int` | 获取循环模式 |
| `setShuffleMode(on)` | `boolean` | void | 随机播放 |
| `isShuffleEnabled()` | — | `boolean` | 查询随机 |

### 4.5 通知监听权限

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `isNotificationListenerEnabled()` | — | `boolean` | 检查权限 |
| `openNotificationListenerSettings()` | — | void | 打开设置页 |
| `hasNotificationAccess()` | — | `boolean` | 查询权限 |

---

## 五、系统接口

> 委托链：WebViewBridge → SystemBridge

### 5.1 网络状态

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `isWifiConnected()` | — | `boolean` | WiFi是否连接 |
| `isBluetoothConnected()` | — | `boolean` | 蓝牙是否连接 |
| `isLocationEnabled()` | — | `boolean` | 定位是否开启 |

### 5.2 屏幕设置

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `setScreenBrightness(v)` | `int(0-255)` | `boolean` | 屏幕亮度 |
| `getScreenBrightness()` | — | `int` | 获取亮度 |
| `setAutoBrightness(on)` | `boolean` | `boolean` | 自动亮度 |
| `isAutoBrightnessEnabled()` | — | `boolean` | 查询自动亮度 |
| `setScreenTimeout(sec)` | `int` | `boolean` | 屏幕超时 |
| `getScreenTimeout()` | — | `int` | 获取超时 |

### 5.3 系统信息

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `getSystemVersionInfo()` | — | `String(JSON)` | 系统版本 |
| `getAppVersionInfo()` | — | `String(JSON)` | 应用版本 |
| `getMemoryInfo()` | — | `String(JSON)` | 内存信息 |
| `getBatteryInfo()` | — | `String(JSON)` | 电池信息 |
| `getLunarCalendar()` | — | `String(JSON)` | 农历日期 |

### 5.4 系统操作

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `restartApp()` | — | void | 重启应用 |
| `openSystemSettings()` | — | void | 打开系统设置 |
| `openAppSettings()` | — | void | 打开应用设置 |
| `showToast(msg)` | `String` | void | 显示Toast |

### 5.5 组件配置

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `saveComponentConfig(id, enabled)` | `String, boolean` | `boolean` | 保存组件配置 |
| `isComponentEnabled(id)` | `String` | `boolean` | 查询组件 |
| `getAllComponentConfigs()` | — | `String(JSON)` | 所有配置 |

### 5.6 其他设置

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `saveSystemLauncherSetting(on)` | `boolean` | `boolean` | 原桌面自启 |
| `saveSystemLauncherSettingAsync(on, callbackId)` | `boolean, String` | void | 异步保存 |
| `saveBootGreetingSetting(on)` | `boolean` | `boolean` | 开机问候语 |
| `saveRandomModeSetting(on)` | `boolean` | `boolean` | 随机模式 |
| `saveSpecifiedModeSetting(enabled)` | `boolean` | `boolean` | 指定模式 |

---

## 六、ADB 接口

> 委托链：WebViewBridge → AdbBridge → AdbManager

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `triggerUsbDebugAuthorization()` | — | void | USB调试授权 |
| `triggerWirelessAdbAuthorization()` | — | void | 本地ADB授权 |
| `executeAdbPermissionGrant()` | — | void | 授权三大权限 |
| `setDefaultDesktopViaAdb()` | — | void | 设置默认桌面 |
| `openRecentTasks()` | — | void | 打开最近任务 |
| `openRecents()` | — | void | 同上（别名） |
| `enableAdbDebugging()` | — | void | 启用ADB调试 |
| `executeAdbCommand(cmd)` | `String` | void | 执行ADB命令 |
| `setDefaultDesktop()` | — | void | 设置默认桌面（别名） |
| `openRecentsViaAdb()` | — | void | 通过ADB打开最近任务 |

---

## 七、自动化接口

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `getAutomationSettings()` | — | `String(JSON)` | 获取所有自动化场景配置 |
| `setAutomationSettings(json)` | `String(JSON)` | `boolean` | 保存配置 |
| `setAutomationScenarioEnabled(id, enabled)` | `String, boolean` | `boolean` | 启用/禁用场景 |

**getAutomationSettings() 返回 JSON 格式：**
```json
{
  "scenarios": [
    {
      "id": "reverse_360",
      "name": "倒车自动360",
      "category": "safety",
      "enabled": true
    }
  ]
}
```

---

## 八、导航接口

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `navigateToHome()` | — | void | 导航回家（打开地图APP） |
| `navigateToCompany()` | — | void | 导航去公司 |

---

## 九、副屏接口（Stub）

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `isPresentationShowing()` | — | `boolean` | 是否显示副屏 |
| `hidePresentation()` | — | void | 隐藏副屏 |
| `showCarStatusPresentation()` | — | `boolean` | 显示车辆状态副屏 |

---

## 十、Java → JS 回调

> Java 通过 `webView.evaluateJavascript()` 调用前端全局函数

| 前端函数 | 参数 | 调用时机 |
|---------|------|---------|
| `window.updateCarState(json)` | JSON字符串 | 车辆状态变化时（档位/车门/灯光等） |
| `window.updateMusicStatus(json)` | JSON字符串 | 音乐状态变化时 |
| `window.updateMusicVisualization(data)` | float数组 | 音乐频谱数据（每帧） |
| `window.AutomationManager.trigger(id)` | 场景ID | 自动化场景触发时 |
| `window.updateTimeDisplay(time, date, lunarDate)` | String, String, String | 时间更新 |
| `window.initializeAcStatus(json)` | JSON字符串 | 空调状态初始化 |
| `window.handleWallpaperUpdateNotification()` | — | 壁纸更新通知 |

**updateCarState() 参数 JSON 格式：**
```json
{
  "speed": 35,
  "gear": "D",
  "gearText": "D",
  "leftTurnLight": 0,
  "rightTurnLight": 1,
  "lowBeamLight": 0,
  "lockState": 1,
  "isLocked": true,
  "openDoorCount": 0,
  "isAnyDoorOpen": false,
  "sunroof": 0,
  "bluetoothConnected": true,
  "acPageOpen": false,
  "camera360Visible": false,
  "frontLeftTirePressure": 2.4,
  "frontRightTirePressure": 2.4,
  "rearLeftTirePressure": 2.4,
  "rearRightTirePressure": 2.4
}
```

---

## 十一、废弃接口

| 方法 | 替代方法 | 说明 |
|------|---------|------|
| `sendPrevTrack()` | `prevMusic()` | 已废弃，保留兼容 |
| `sendNextTrack()` | `nextMusic()` | 已废弃，保留兼容 |
| `playPause()` | `playPauseMusic()` | 已废弃，保留兼容 |
| `playNext()` | `nextMusic()` | 已废弃，保留兼容 |
| `playPrevious()` | `prevMusic()` | 已废弃，保留兼容 |
| `playMusic()` | `playPauseMusic()` | 已废弃，保留兼容 |
| `pauseMusic()` | `playPauseMusic()` | 已废弃，保留兼容 |

---

**文档版本**：v2.1
**最后更新**：2026-07-09