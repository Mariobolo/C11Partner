# C11Partner 代码索引文档
> 📌 **快速定位代码的神器** - 修改代码前先看本文档，找到对应函数行号再精准读取，避免读取大文件占用上下文
>
> 最后更新：2026-06-20
> 版本：v1.0

---

## 📖 使用说明

### 为什么需要这个文档？
- 项目代码量较大（WebViewBridge 4500+行，index.js 3000+行）
- 每次读取整个文件会占用大量上下文，导致篇幅过长
- 用本文档快速定位函数行号，再精准读取小范围代码

### 使用方法
1. 根据功能分类找到对应的文件和函数
2. 记下函数所在行号
3. 用 `sed -n '起始行,结束行p' 文件名` 精准读取代码
4. 或者用 Grep 搜索函数名定位

---

## 🔧 后端核心文件索引（Java）

### 1. MainActivity.java
**路径**：`app/src/main/java/com/c11partner/desktop/MainActivity.java`  
**行数**：约 1500 行  
**职责**：主Activity，应用入口，状态推送，生命周期管理

| 分类 | 函数名 | 行号 | 说明 |
|------|--------|------|------|
| **生命周期** | `onCreate()` | 287 | Activity创建，初始化 |
| | `onDestroy()` | 466 | Activity销毁，资源释放 |
| **时间更新** | `startTimeUpdate()` | 1148 | 启动时间更新定时器 |
| | `stopTimeUpdate()` | 1463 | 停止时间更新 |
| **服务绑定** | `getLogcatMonitorService()` | 211 | 获取日志监控服务 |
| | `isLogServiceBound()` | 218 | 检查服务是否已绑定 |
| **状态回调** | `onGearChanged()` | ~242 | 档位变化回调 |
| | `onTurnLightChanged()` | ~248 | 转向灯变化回调 |
| | `onDoorChanged()` | ~254 | 车门变化回调 |
| | `onSpeedChanged()` | ~260 | 车速变化回调 |
| | `onLockStateChanged()` | ~265 | 锁车状态变化回调 |
| **工具方法** | `getWeekDay()` | ~? | 获取星期几 |
| | `getLunarDate()` | ~? | 获取农历日期 |

---

### 2. WebViewBridge.java ⭐ 最常用
**路径**：`app/src/main/java/com/c11partner/desktop/bridge/WebViewBridge.java`  
**行数**：约 4500 行  
**职责**：JS桥接层，提供 50+ 个 JS 接口给前端调用

> 💡 **提示**：这是修改最频繁的文件，用函数名快速定位行号

#### 🎮 车控相关接口
| 函数名 | 行号 | 说明 |
|--------|------|------|
| `startCamera360()` | ~? | 启动360全景 |
| `setLowBeamLight()` | ~? | 设置近光灯 |
| `setDriveMode()` | ~? | 设置驾驶模式 |
| `setAcEnabled()` | 307 | 设置空调开关 |
| `setWindLevel()` | 331 | 设置空调风量 |
| `setDriverTemp()` | 570 | 设置主驾温度 |
| `setAmbientLightEnabled()` | 600 | 设置氛围灯开关 |
| `setAmbientLightColor()` | ~? | 设置氛围灯颜色 |
| `setCameraOverspeedLimit()` | 492 | 设置360超速限制 |
| `setVideoWhileDriving()` | 509 | 设置行驶中视频播放 |
| `setMusicVolume()` | 553 | 设置音乐音量 |
| `setSecondaryScreenEnabled()` | ~? | 设置副屏开关 |
| `sendVoiceCommand()` | 271 | 发送语音控制指令 |

#### 🎵 音乐相关接口
| 函数名 | 行号 | 说明 |
|--------|------|------|
| `getSystemMusicInfo()` | 2390 | 获取系统音乐信息 |
| `playPauseMusic()` | 2424 | 播放/暂停音乐 |
| `nextMusic()` | 2441 | 下一首 |
| `prevMusic()` | 2457 | 上一首 |
| `isNotificationListenerEnabled()` | 2499 | 检查通知监听权限 |
| `openNotificationListenerSettings()` | 2519 | 打开通知监听设置 |

#### 🤖 自动化相关接口
| 函数名 | 行号 | 说明 |
|--------|------|------|
| `getAllAutomationScenarios()` | ~? | 获取所有自动化场景 |
| `setAutomationScenarioEnabled()` | 4582 | 设置自动化场景开关 |

#### 📱 应用列表相关
| 函数名 | 行号 | 说明 |
|--------|------|------|
| `getAppList()` | 154 | 获取应用列表（带缓存） |
| `launchApp()` | ~? | 启动应用 |
| `getQuickApps()` | ~? | 获取快捷应用列表 |

#### 🖥️ 副屏相关接口
| 函数名 | 行号 | 说明 |
|--------|------|------|
| `showCarStatusPresentation()` | 4305 | 显示副屏车辆状态 |
| `hidePresentation()` | 4375 | 隐藏副屏显示 |
| `isPresentationShowing()` | 4403 | 副屏是否正在显示 |
| `updatePresentationTime()` | 4445 | 更新副屏时间 |

#### ⏰ 时间显示相关
| 函数名 | 行号 | 说明 |
|--------|------|------|
| `updateTimeDisplay()` | 2964 | 更新前端时间显示 |

---

### 3. CarControlManager.java ⭐ 核心车控
**路径**：`app/src/main/java/com/c11partner/desktop/utils/CarControlManager.java`  
**行数**：约 800 行  
**职责**：车控功能管理类，三层控制模型的核心实现

> 💡 **单例模式**：用 `CarControlManager.getInstance(context)` 获取实例

| 分类 | 函数名 | 行号 | 说明 |
|------|--------|------|------|
| **实例获取** | `getInstance()` | 70 | 获取单例实例 |
| **360全景** | `startCamera360()` | 82 | 启动360全景 |
| **灯光控制** | `setLowBeamLight()` | 101 | 设置近光灯 |
| | `setRearFogLight()` | ~117 | 设置后雾灯 |
| | `setPositionLight()` | ~133 | 设置示廓灯 |
| | `setPedestrianAlert()` | ~149 | 设置行人警示 |
| **驾驶模式** | `setDriveMode()` | 168 | 设置驾驶模式（0-5） |
| **场景模式** | `setGuardMode()` | ~186 | 守护模式 |
| | `setRestMode()` | ~202 | 小憩模式 |
| | `setCampingMode()` | ~218 | 露营模式 |
| | `setPowerSaveMode()` | ~234 | 省电模式 |
| | `setSentinelMode()` | ~250 | 哨兵模式 |
| **空调控制** | `setMaxCooling()` | ~? | 最大制冷 |
| | `setAcEnabled()` | 307 | 设置空调开关 |
| | `setWindLevel()` | 331 | 设置风量 |
| | `setDriverTemp()` | 570 | 主驾温度 |
| | `setPassengerTemp()` | ~? | 副驾温度 |
| **系统设置** | `setNightMode()` | ~? | 夜间模式 |
| | `setWifiEnabled()` | ~? | WiFi开关 |
| | `setBluetoothEnabled()` | ~? | 蓝牙开关 |
| **方控按键** | `sendPrevTrack()` | ~? | 上一曲 |
| | `sendNextTrack()` | ~? | 下一曲 |
| **音量控制** | `setCallVolume()` | ~? | 通话音量 |
| | `setNaviVolume()` | ~? | 导航音量 |
| | `setMusicVolume()` | 553 | 音乐音量 |
| **氛围灯** | `setAmbientLightEnabled()` | 600 | 氛围灯开关 |
| | `setAmbientLightColor()` | ~? | 氛围灯颜色 |
| **语音控制** | `sendVoiceCommand()` | 271 | 发送语音指令 |
| **Settings读写** | `getGlobalInt()` | 451 | 读取全局整型属性 |
| | `setGlobalInt()` | 309 | 设置全局整型属性 |
| | `getGlobalString()` | ~? | 读取全局字符串属性 |

---

### 4. LogcatMonitorService.java ⭐ 日志监控
**路径**：`app/src/main/java/com/c11partner/desktop/LogcatMonitorService.java`  
**行数**：约 600 行  
**职责**：日志监控后台服务，解析CAN信号，推送车辆状态

| 分类 | 函数名 | 行号 | 说明 |
|------|--------|------|------|
| **生命周期** | `onCreate()` | 92 | 服务创建 |
| | `onStartCommand()` | 140 | 服务启动 |
| | `onDestroy()` | 151 | 服务销毁 |
| **日志监控** | `startLogcatMonitoring()` | 163 | 开始日志监控 |
| | `stopLogcatMonitoring()` | 178 | 停止日志监控 |
| **信号解析** | `processCanSignal()` | 313 | 处理CAN信号 |
| | `parseTurnLightSignal()` | ~? | 解析转向灯信号 |
| | `parseTirePressureData()` | ~? | 解析胎压数据 |
| **状态管理** | `getCarState()` | ~? | 获取当前车辆状态 |
| | `registerListener()` | ~? | 注册状态监听器 |
| | `unregisterListener()` | ~? | 注销状态监听器 |
| **通知** | `createNotificationChannel()` | ~104 | 创建通知渠道 |
| | `createNotification()` | ~122 | 创建前台通知 |

---

### 5. AutomationEngine.java
**路径**：`app/src/main/java/com/c11partner/desktop/utils/AutomationEngine.java`  
**行数**：约 350 行  
**职责**：自动化场景引擎，管理 12 个预设自动化场景

| 分类 | 函数名 | 行号 | 说明 |
|------|--------|------|------|
| **初始化** | `initTts()` | 62 | 初始化TTS语音 |
| **场景管理** | `isScenarioEnabled()` | 94 | 检查场景是否启用 |
| | `setScenarioEnabled()` | 115 | 设置场景开关 |
| | `getAllScenariosJson()` | 125 | 获取所有场景JSON |
| **状态触发** | `onGearChanged()` | 175 | 档位变化触发 |
| | `onTurnLightChanged()` | 229 | 转向灯变化触发 |
| | `onDoorChanged()` | 243 | 车门变化触发 |
| | `onSpeedChanged()` | 265 | 车速变化触发 |
| **语音播报** | `speak()` | ~? | TTS语音播报 |
| **音量控制** | `lowerVolumeForReverse()` | ~? | 倒车降音量 |
| | `restoreVolume()` | ~? | 恢复音量 |

---

### 6. CarStatusPresentation.java
**路径**：`app/src/main/java/com/c11partner/desktop/CarStatusPresentation.java`  
**行数**：约 550 行  
**职责**：副屏车辆状态显示 Presentation

| 分类 | 函数名 | 行号 | 说明 |
|------|--------|------|------|
| **生命周期** | `onCreate()` | 72 | 创建显示 |
| | `onDestroy()` | 90 | 销毁显示 |
| **时间更新** | `startTimeUpdate()` | 98 | 启动时间更新（每分钟） |
| | `stopTimeUpdate()` | 127 | 停止时间更新 |
| | `updateCurrentTime()` | 136 | 更新当前时间显示 |
| **布局创建** | `createContentView()` | 146 | 创建内容视图 |
| | `createTireTextView()` | ~330 | 创建胎压文本视图 |
| **状态更新** | `updateSpeed()` | 398 | 更新车速 |
| | `updateGear()` | 408 | 更新档位 |
| | `updateDoorStatus()` | 441 | 更新车门状态 |
| | `updateTurnLights()` | 458 | 更新转向灯 |
| | `updateTirePressure()` | 473 | 更新胎压 |
| | `updateLowBeamLight()` | ~? | 更新近光灯状态 |
| | `updateBluetoothState()` | ~? | 更新蓝牙状态 |
| | `updateLockState()` | ~? | 更新锁车状态 |
| | `updateTime()` | 562 | 更新时间（外部接口） |
| | `updateDisplay()` | 571 | 更新所有显示 |

---

### 7. MusicNotificationListenerService.java
**路径**：`app/src/main/java/com/c11partner/desktop/service/MusicNotificationListenerService.java`  
**行数**：约 230 行  
**职责**：音乐通知监听服务，从通知中提取音乐信息

| 函数名 | 行号 | 说明 |
|--------|------|------|
| `onNotificationPosted()` | ~56 | 通知发布时触发 |
| `onNotificationRemoved()` | ~81 | 通知移除时触发 |
| `isMusicPackage()` | ~120 | 判断是否音乐应用包名 |
| `extractMusicInfo()` | ~138 | 从通知中提取音乐信息 |
| `notifyListener()` | ~187 | 通知监听器音乐信息变化 |

---

### 8. SecondaryScreenManager.java
**路径**：`app/src/main/java/com/c11partner/desktop/utils/SecondaryScreenManager.java`  
**行数**：约 200 行  
**职责**：副屏管理类，检测和控制副屏

| 函数名 | 行号 | 说明 |
|--------|------|------|
| `getSecondaryDisplay()` | ~55 | 获取副屏Display对象 |
| `hasSecondaryDisplay()` | ~73 | 是否有副屏 |
| `getSecondaryDisplayInfo()` | ~80 | 获取副屏信息 |
| `showPresentation()` | ~121 | 显示Presentation |
| `hidePresentation()` | ~147 | 隐藏Presentation |
| `isShowing()` | ~162 | 是否正在显示 |
| `setSecondaryScreenEnabled()` | ~178 | 设置副屏开关（系统属性） |
| `isSecondaryScreenEnabled()` | ~191 | 副屏是否启用 |

---

## 🎨 前端核心文件索引（JS）

### 1. index.js ⭐ 最常用
**路径**：`app/src/main/assets/js/index.js`  
**行数**：约 3000 行  
**职责**：主JS文件，包含大部分前端逻辑

#### 📦 核心管理器（对象）
| 管理器 | 行号 | 说明 |
|--------|------|------|
| `AsyncCallbackManager` | ~13 | 异步回调管理器 |
| `CarStateManager` | 3186 | 车辆状态管理器 |
| `QuickSwitchManager` | 3440 | 快捷开关管理器 |
| `AutomationManager` | 3844 | 自动化场景管理器 |
| `SystemMusicManager` | 2973 | 系统音乐管理器 |
| `VoiceTestManager` | ~? | 语音测试管理器 |

#### 🎛️ 初始化函数
| 函数名 | 行号 | 说明 |
|--------|------|------|
| `initSettingsModal()` | 138 | 初始化设置弹窗 |
| `initAppsModal()` | ~1184 | 初始化应用列表弹窗 |
| `addClickEffect()` | ~72 | 添加点击效果 |

#### 🎵 音乐相关
| 函数名 | 行号 | 说明 |
|--------|------|------|
| `SystemMusicManager.init()` | ~? | 系统音乐管理器初始化 |
| `SystemMusicManager.updateMusicInfo()` | ~? | 更新音乐信息 |
| `updateMusicPlayPauseIcon()` | ~? | 更新播放/暂停图标 |

#### 📱 应用列表相关
| 函数名 | 行号 | 说明 |
|--------|------|------|
| `renderAppsList()` | 1110 | 渲染应用列表 |
| `filterAppList()` | 1271 | 过滤应用列表（分类+搜索） |
| `updateAlphabetNavVisibility()` | ~? | 更新字母导航栏可见性 |

#### ⚙️ 设置相关
| 函数名 | 行号 | 说明 |
|--------|------|------|
| `showSettingsModal()` | 660 | 显示设置弹窗 |
| `hideSettingsModal()` | 707 | 隐藏设置弹窗 |
| `loadWallpaperSettings()` | ~287 | 加载壁纸设置 |
| `loadSystemSettings()` | ~388 | 加载系统设置 |

---

### 2. weather.js
**路径**：`app/src/main/assets/js/weather.js`  
**行数**：约 180 行  
**职责**：天气模块

| 函数名 | 行号 | 说明 |
|--------|------|------|
| `initWeatherDisplay()` | 10 | 初始化天气显示 |
| `updateWeatherInfo()` | ~30 | 更新天气信息 |
| `fetchWeatherData()` | 38 | 获取天气数据 |
| `updateWeatherDisplay()` | 90 | 更新天气显示 |
| `getWeatherDescription()` | 135 | 获取天气描述文字 |
| `getWeatherIcon()` | 171 | 获取天气图标 |

---

### 3. music.js
**路径**：`app/src/main/assets/js/music.js`  
**行数**：约 450 行  
**职责**：音乐可视化和播放控制

| 函数名 | 行号 | 说明 |
|--------|------|------|
| `updateMusicVisualization()` | ~46 | 更新音乐可视化 |
| `updateMusicStatus()` | ~98 | 更新音乐状态 |
| `updateMusicUI()` | ~124 | 更新音乐UI |
| `updateMusicProgressBar()` | ~155 | 更新进度条 |
| `updateMusicPlayState()` | ~193 | 更新播放状态 |
| `formatMusicTime()` | ~227 | 格式化音乐时间 |
| `initCustomAudioPlayer()` | ~417 | 初始化自定义音频播放器 |
| `visualize()` | ~386 | 可视化主函数 |

---

## 🚀 快速查找指南

### 按功能找代码

#### 我想修改车控功能
1. 先看 `CarControlManager.java`（核心实现）
2. 再看 `WebViewBridge.java`（JS接口）
3. 最后看前端 `QuickSwitchManager`（UI交互）

#### 我想修改车辆状态显示
1. 先看 `LogcatMonitorService.java`（日志解析）
2. 再看 `LeapMotorCarState.java`（状态数据）
3. 最后看前端 `CarStateManager`（UI显示）

#### 我想修改自动化场景
1. 先看 `AutomationEngine.java`（核心引擎）
2. 再看 `WebViewBridge.java`（配置接口）
3. 最后看前端 `AutomationManager`（配置UI）

#### 我想修改音乐模块
1. 先看 `MusicNotificationListenerService.java`（通知监听）
2. 再看 `WebViewBridge.java`（JS接口）
3. 最后看前端 `SystemMusicManager`（UI显示）

#### 我想修改副屏显示
1. 先看 `CarStatusPresentation.java`（副屏UI）
2. 再看 `SecondaryScreenManager.java`（副屏管理）
3. 最后看 `WebViewBridge.java`（控制接口）

---

### 常用修改场景速查

| 场景 | 首选文件 | 关键函数/对象 |
|------|----------|--------------|
| 添加新的车控功能 | CarControlManager.java | setXXX() 方法 |
| 添加新的JS接口 | WebViewBridge.java | @JavascriptInterface 方法 |
| 修改快捷开关 | index.js | QuickSwitchManager |
| 修改状态栏车辆状态 | index.js | CarStateManager |
| 添加自动化场景 | AutomationEngine.java | onXXXChanged() 方法 |
| 修改应用列表 | index.js | renderAppsList(), filterAppList() |
| 修改天气模块 | weather.js | fetchWeatherData(), updateWeatherDisplay() |
| 修改副屏显示内容 | CarStatusPresentation.java | updateXXX() 方法 |
| 修改音乐信息获取 | MusicNotificationListenerService.java | extractMusicInfo() |

---

## 📝 更新记录

| 版本 | 日期 | 更新内容 |
|------|------|----------|
| v1.0 | 2026-06-20 | 初始版本，建立核心文件索引 |

---

**💡 小贴士**：修改代码后记得更新本文档的行号，保持索引准确！
