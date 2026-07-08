# C11Partner 代码索引

> 📑 函数级快速定位索引，按文件和功能分类
>
> 最后更新：2026-07-09

---

## 一、前端 JS 函数索引

> 文件按 index.html 加载顺序排列

### 1. utils.js (446行) — 通用工具

| 函数 | 行号 | 说明 |
|------|------|------|
| `showSwipeHint()` | L39 | 显示滑动提示 |
| `showToast(msg, dur)` | L93 | 显示 Toast 提示 |
| `showSuccessMessage(msg)` | L148 | 显示成功消息 |
| `showErrorMessage(msg)` | L157 | 显示错误消息 |
| `blurAfterClick(selector)` | L211 | 触屏点击后恢复失焦 |
| `clearActiveState()` | L229 | 清除 :active 状态 |
| `addClickEffect(el)` | L244 | 添加点击反馈 |
| `formatTime(sec)` | L294 | 格式化时间 MM:SS |
| `debounce(fn, wait)` | L310 | 防抖函数 |
| `throttle(fn, wait)` | L329 | 节流函数 |
| `deepClone(obj)` | L346 | 深拷贝 |
| `initTouchBlur()` | L371 | 全局触屏失焦初始化 |

### 2. storage.js (184行) — 本地存储

| 函数 | 行号 | 说明 |
|------|------|------|
| `Storage.get(key, def)` | L31 | 获取值（支持过期） |
| `Storage.set(key, val, ttl)` | L72 | 设置值（可带过期） |
| `Storage.remove(key)` | L103 | 删除值 |
| `Storage.clear()` | L118 | 清空所有 |
| `Storage.has(key)` | L134 | 检查键存在 |
| `Storage.keys()` | L148 | 获取所有键名 |
| `Storage.size()` | L166 | 获取存储大小 |

### 3. bridge.js (208行) — Android 接口封装

| 函数 | 行号 | 说明 |
|------|------|------|
| `AsyncCallbackManager.generateId()` | L42 | 生成回调ID |
| `AsyncCallbackManager.register(fn, ttl)` | L53 | 注册回调 |
| `AsyncCallbackManager.execute(id, args)` | L73 | 执行回调 |
| `Bridge.isAvailable()` | L95 | 检测 Android 环境 |
| `Bridge.hasMethod(name)` | L105 | 检测方法存在 |
| `Bridge.call(method, ...args)` | L116 | 同步调用 |
| `Bridge.callAsync(method, ...args)` | L136 | 异步调用（带回调） |
| `Bridge.invoke(method, ...args)` | L160 | 自动检测同步/异步 |
| `Bridge.safeCall(method, def, ...args)` | L187 | 安全调用（带默认值） |

### 4. bootstrap.js (91行) — 初始化队列

| 函数 | 行号 | 说明 |
|------|------|------|
| `debounce(fn, wait)` | L10 | 防抖 |
| `throttle(fn, wait)` | L20 | 节流 |
| `addDebouncedClick(el, fn)` | L31 | 防抖点击事件 |
| `safeInit(name, fn)` | L37 | 安全初始化（try-catch） |
| `registerInit(fn)` | L47 | 注册初始化任务 |
| `runInit()` | L51 | 执行初始化队列 |

### 5. panel-controller.js (106行) — 面板控制

| 函数 | 行号 | 说明 |
|------|------|------|
| `showSettingsModal()` | L2 | 显示设置弹窗 |
| `hideSettingsModal()` | L11 | 隐藏设置弹窗 |
| `showAppsModal()` | L20 | 显示应用弹窗（触发加载） |
| `hideAppsModal()` | L42 | 隐藏应用弹窗 |
| `initWallpaperDoubleClick()` | L51 | 壁纸双击切换 |
| `handleWallpaperLongPress()` | L65 | 壁纸长按设置 |

### 6. settings-sync.js (196行) — 设置同步

| 函数 | 行号 | 说明 |
|------|------|------|
| `loadWallpaperSettings()` | L4 | 加载壁纸设置 |
| `applyWallpaperSettings(settings)` | L33 | 应用壁纸设置到UI |
| `initEffectLevel()` | L63 | 初始化特效等级 |
| `applyEffectLevel(level)` | L93 | 应用特效等级 |
| `initThemeMode()` | L99 | 初始化主题模式 |
| `applyThemeMode(mode)` | L129 | 应用主题模式 |
| `updateThemeToggleIcon()` | L136 | 更新主题图标 |
| `initThemeToggleIcon()` | L148 | 初始化主题切换点击 |
| `toggleWallpaperCarousel()` | L173 | 切换壁纸轮播 |
| `restoreDefaultWallpaper()` | L179 | 恢复默认壁纸 |

### 7. ui-initializer.js (473行) — UI 初始化

| 函数 | 行号 | 说明 |
|------|------|------|
| `loadQuickApps()` | L2 | 加载快捷应用 |
| `createShowAllAppsButton()` | L96 | 创建"全部"按钮 |
| `loadQuickSwitches()` | L119 | 加载快捷开关 |
| `updateACControlStatus()` | L204 | 更新空调状态 |
| `checkWifiStatus()` | L214 | 检查 WiFi |
| `checkBluetoothStatus()` | L234 | 检查蓝牙 |
| `checkLocationStatus()` | L254 | 检查定位 |
| `updateNetworkAndBluetoothStatus()` | L274 | 更新状态栏 |
| `initHorizontalScroll()` | L280 | 横向滚动 |
| `updateMusicProgress()` | L313 | 更新音乐进度 |
| `initAcTemperature()` | L327 | 初始化空调温度 |
| `updateAcTemperature()` | L338 | 更新空调温度 |
| `updateAcState()` | L345 | 更新空调状态 |
| `updateWindLevel()` | L358 | 更新风量 |
| `initMusicControls()` | L365 | 初始化音乐控制 |
| `addTimeDisplayClickEvent()` | L395 | 时钟点击事件 |
| `initNavigationButtons()` | L428 | 导航按钮 |

### 8. app-list-manager.js (448行) — 应用列表

| 函数 | 行号 | 说明 |
|------|------|------|
| `generateAppData()` | L7 | 生成模拟数据 |
| `escapeHtml(text)` | L101 | HTML转义 |
| `normalizeAppIcon(icon)` | L108 | 规范化图标 |
| `initAlphabetNav()` | L115 | 字母导航 |
| `renderAppsList(apps)` | L137 | 渲染应用列表 |
| `loadAppList()` | L196 | 加载应用列表（带缓存） |
| `initAppsModal()` | L230 | 初始化弹窗事件 |
| `showAddToQuickAppsDialog(app)` | L333 | 添加到快捷应用 |
| `showRemoveFromQuickAppsDialog(app)` | L384 | 移除快捷应用 |

### 9. theme.js (261行) — 主题切换

| 函数 | 行号 | 说明 |
|------|------|------|
| `Theme.init()` | L52 | 初始化主题 |
| `Theme.setTheme(mode)` | L82 | 设置主题 |
| `Theme.getTheme()` | L116 | 获取当前主题 |
| `Theme.toggleTheme()` | L125 | 切换主题 |
| `Theme.applyTheme(mode)` | L136 | 应用主题到DOM |
| `Theme.isSystemDarkMode()` | L160 | 检测系统深色模式 |
| `Theme.loadTheme()` | L172 | 从存储加载 |
| `Theme.saveTheme(mode)` | L190 | 保存到存储 |
| `Theme.onChange(cb)` | L208 | 添加监听器 |
| `Theme.getAvailableThemes()` | L244 | 可用主题列表 |

### 10. widgets.js (651行) — Widget 管理

| 函数 | 行号 | 说明 |
|------|------|------|
| `Widgets.register(id, config)` | L51 | 注册组件 |
| `Widgets.unregister(id)` | L88 | 注销组件 |
| `Widgets.get(id)` | L118 | 获取组件 |
| `Widgets.update(id, data)` | L140 | 更新数据 |
| `Widgets.render(id)` | L175 | 渲染组件 |
| `Widgets.show(id)` / `hide(id)` / `toggle(id)` | L221/L242/L263 | 显示/隐藏/切换 |
| `Widgets.batchUpdate(updates)` | L308 | 批量更新 |
| `Parallax3D.init()` | L468 | 3D视差初始化 |
| `Parallax3D.destroy()` | L628 | 销毁3D视差 |

### 11. music.js (358行) — 音乐可视化

| 函数 | 行号 | 说明 |
|------|------|------|
| `updateMusicVisualization(data)` | L46 | 更新可视化（Android调用） |
| `updateMusicStatus(data)` | L98 | 更新音乐状态（Android调用） |
| `updateMusicUI(data)` | L124 | 更新音乐UI |
| `updateMusicProgressBar(pos, dur)` | L155 | 更新进度条 |
| `updateMusicPlayState(playing)` | L194 | 更新播放状态 |
| `formatMusicTime(ms)` | L213 | 格式化音乐时间 |
| `hasNotificationAccess()` | L224 | 检查通知权限 |
| `drawFloats(data)` | L267 | 绘制跳动方块 |
| `drawBars(data)` | L311 | 绘制柱状图 |

### 12-25. 其他模块

| 文件 | 行数 | 关键函数 |
|------|------|----------|
| settings.js | 437 | `Settings.init()`, `Settings.load()`, `Settings.save()`, `Settings.get()`, `Settings.set()` |
| app.js | 427 | `App.init()`, `App.ready()`, `App.on()`, `App.emit()` |
| toast.js | 152 | `showToast()`, `showSuccessMessage()`, `showErrorMessage()` |
| datetime.js | 150 | `updateTime()`, `updateDate()`, `updateLunarDate()`, `getLunarDate()` |
| wallpaper-manager.js | 916 | `init()`, `applyWallpaper()`, `nextWallpaper()`, `startCarousel()`, `WallpaperSwipeManager.*` |
| weather.js | 188 | `initWeatherDisplay()`, `fetchWeatherData()`, `updateWeatherDisplay()` |
| map.js | 195 | `showMapAppSelectionDialog()`, `loadMapAppListForSelection()` |
| async-callback-manager.js | 63 | `generateId()`, `register()`, `execute()` |
| system-music-manager.js | 193 | `SystemMusicManager.init()`, `updateMusicInfo()`, `checkPermission()` |
| car-state-manager.js | 362 | `updateState()`, `updateGearIndicator()`, `updateDoorIndicator()`, `updateTurnIndicators()`, `window.updateCarState()` |
| gear-bridge.js | 186 | `onGearChanged()`, `setupGearListener()`, `init()` |
| automation-manager.js | 399 | `init()`, `loadSettings()`, `toggleScenario()`, `showPanel()`, `trigger()` |
| quick-switch-manager.js | 439 | `showPanel()`, `hidePanel()`, `toggleSwitch()`, `setDriveMode()`, `refreshSwitchStates()` |
| index.js | 604 | `safeInit()`, `initSettingsModal()`, `loadSystemSettings()`, `toggleAC()` 等适配函数 |

---

## 二、后端 Java 方法索引

### WebViewBridge.java (1013行) — JS 统一入口

> 所有 `@JavascriptInterface` 方法，按功能分组

#### 车控接口（委托 CarControlBridge）

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `startCamera360()` | — | void | 启动360全景 |
| `setLowBeamLight(on)` | boolean | void | 近光灯 |
| `isLowBeamLightOn()` | — | boolean | 近光灯状态 |
| `setRearFogLight(on)` | boolean | void | 后雾灯 |
| `setPositionLight(on)` | boolean | void | 示廓灯 |
| `setPedestrianAlert(on)` | boolean | void | 行人警示音 |
| `setDriveMode(mode)` | int(0-5) | void | 驾驶模式 |
| `setGuardMode(on)` / `setRestMode(on)` / `setCampingMode(on)` / `setPowerSaveMode(on)` / `setSentinelMode(on)` | boolean | void | 各种模式 |
| `setMaxCooling(on)` / `isMaxCoolingOn()` | boolean | void/boolean | 极速制冷 |
| `setAcEnabled(on)` / `isAcEnabled()` | boolean | void/boolean | 空调开关 |
| `setWindLevel(level)` / `getWindLevel()` | int | void/int | 风量 |
| `setDriverTemp(temp)` / `getDriverTemp()` | int | void/int | 主驾温度 |
| `setPassengerTemp(temp)` / `getPassengerTemp()` | int | void/int | 副驾温度 |
| `toggleAirConditioning()` / `toggleDefrost()` / `toggleMute()` | — | void | 切换 |
| `increaseTemperature()` / `decreaseTemperature()` | — | void | 温度± |
| `increaseWindSpeed()` / `decreaseWindSpeed()` | — | void | 风量± |
| `setCallVolume(v)` / `getCallVolume()` | int | void/int | 电话音量 |
| `setNaviVolume(v)` / `getNaviVolume()` | int | void/int | 导航音量 |
| `setAmbientLightEnabled(on)` / `isAmbientLightEnabled()` | boolean | void/boolean | 氛围灯 |
| `setAmbientLightColor(color)` / `getAmbientLightColor()` | int | void/int | 氛围灯颜色 |
| `setNightMode(on)` / `isNightModeOn()` | boolean | void/boolean | 夜间模式 |
| `setDriverSeatHeating(level)` / `setPassengerSeatHeating(level)` | int(0-3) | void | 座椅加热 |
| `setDriverSeatVentilation(level)` / `setPassengerSeatVentilation(level)` | int(0-3) | void | 座椅通风 |
| `setSteeringWheelHeating(on)` | boolean | void | 方向盘加热 |
| `foldMirrors()` / `unfoldMirrors()` / `setMirrorHeating(on)` | — | void/boolean | 后视镜 |
| `isVehicleLocked()` / `isScreenOn()` | — | boolean | 状态查询 |

#### 应用接口（委托 AppBridge）

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `getAppList()` | — | String(JSON) | 应用列表（字母分组） |
| `getAllApps()` / `getUserApps()` / `getSystemApps()` | — | String(JSON) | 应用列表 |
| `getAppInfo(pkg)` / `getAppIcon(pkg)` | String | String | 应用信息/图标 |
| `launchApp(pkg)` / `openAppInfo(pkg)` | String | void | 启动/信息页 |
| `getQuickAppList()` | — | String(JSON) | 快捷应用 |
| `addQuickApp(pkg)` / `removeQuickApp(pkg)` / `isQuickApp(pkg)` | String | void/boolean | 快捷应用管理 |
| `getAppListAsync()` | — | void | 异步获取（回调） |

#### 壁纸接口（委托 WallpaperBridge）

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `getWallpaperSettings()` / `getWallpaperSettingsAsync()` | — | String/void | 壁纸设置 |
| `setWallpaperType(type)` / `setWallpaperPath(path)` | int/String | void | 壁纸类型/路径 |
| `getRandomWallpaper()` / `getRandomWallpaperAsync()` | — | String/void | 随机壁纸 |
| `getRandomWallpaperBase64()` / `getRandomWallpaperBase64Async()` | — | String/void | Base64壁纸 |
| `getNextWallpaperUrl()` / `getCurrentWallpaperUrl()` | — | String | 壁纸URL |
| `setWallpaperCarouselEnabled(on)` / `setWallpaperCarouselInterval(sec)` | boolean/int | void | 轮播设置 |
| `refreshBingWallpaper()` / `deleteCurrentWallpaper()` | — | void | 刷新/删除 |
| `updateWallpaperCategories()` / `updateCategoryEnabled(name,on)` / `getEnabledCategories()` | — | void/boolean/String | 分类管理 |

#### 音乐接口（委托 MusicBridge）

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `startMusicVisualizer()` / `stopMusicVisualizer()` | — | void | 可视化 |
| `isMusicPlaying()` | — | boolean | 播放状态 |
| `getCurrentMusicName()` / `getCurrentMusicArtist()` | — | String | 歌名/歌手 |
| `getMusicProgressInfo()` / `getSystemMusicInfo()` | — | String(JSON) | 进度/系统信息 |
| `playPauseMusic()` / `nextMusic()` / `prevMusic()` | — | void | 播放控制 |
| `setMusicVolume(v)` / `getMusicVolume()` | int | void/int | 音量 |
| `volumeUp()` / `volumeDown()` | — | void | 音量± |
| `seekTo(pos)` / `setPlaybackSpeed(speed)` | int/float | void | 跳转/速度 |
| `getPlaylist()` / `playSongAtIndex(i)` | —/int | String/void | 播放列表 |
| `setRepeatMode(m)` / `getRepeatMode()` / `setShuffleMode(on)` / `isShuffleEnabled()` | int/boolean | void/int/boolean | 循环/随机 |

#### 系统接口（委托 SystemBridge）

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `isWifiConnected()` / `isBluetoothConnected()` / `isLocationEnabled()` | — | boolean | 网络状态 |
| `setScreenBrightness(v)` / `getScreenBrightness()` | int | void/int | 亮度 |
| `setAutoBrightness(on)` / `isAutoBrightnessEnabled()` | boolean | void/boolean | 自动亮度 |
| `setScreenTimeout(sec)` / `getScreenTimeout()` | int | void/int | 屏幕超时 |
| `getSystemVersionInfo()` / `getAppVersionInfo()` | — | String | 版本信息 |
| `getMemoryInfo()` / `getBatteryInfo()` | — | String | 内存/电池 |
| `restartApp()` / `openSystemSettings()` / `openAppSettings()` | — | void | 系统 |
| `getLunarCalendar()` | — | String | 农历 |
| `saveComponentConfig(id,enabled)` / `isComponentEnabled(id)` / `getAllComponentConfigs()` | String/boolean | void/boolean/String | 组件配置 |

#### ADB 接口（委托 AdbBridge）

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `triggerUsbDebugAuthorization()` | — | void | USB调试授权 |
| `triggerWirelessAdbAuthorization()` | — | void | 本地ADB授权 |
| `executeAdbPermissionGrant()` | — | void | 权限授权 |
| `setDefaultDesktopViaAdb()` | — | void | 设置默认桌面 |

#### 自动化接口

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `getAutomationSettings()` | — | String(JSON) | 获取配置 |
| `setAutomationSettings(json)` | String | void | 保存配置 |
| `setAutomationScenarioEnabled(id,enabled)` | String/boolean | void | 启用/禁用场景 |

#### 导航接口

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `navigateToHome()` | — | void | 导航回家 |
| `navigateToCompany()` | — | void | 导航去公司 |

---

### 核心后端类

| 类 | 文件 | 行数 | 关键方法 |
|----|------|------|----------|
| `MainActivity` | MainActivity.java | 2193 | `onCreate()`, `onResume()`, `onDestroy()`, `updateMusicStatus()`, `startTimeUpdate()` |
| `LogcatMonitorService` | LogcatMonitorService.java | 714 | `onCreate()`, `onStartCommand()`, `addListener()`, `getCurrentState()` |
| `CarControlManager` | CarControlManager.java | 921 | `getInstance()`, `setLowBeamLight()`, `setDriveMode()`, `setAcEnabled()` 等66个方法 |
| `AutomationEngine` | AutomationEngine.java | 358 | `onGearChanged()`, `onTurnLightChanged()`, `onDoorChanged()`, `onSpeedChanged()` |
| `LeapMotorCarState` | LeapMotorCarState.java | 218 | 所有车辆状态的 getter/setter（档位/车门/灯光/胎压等） |

---

## 三、Java 回调前端接口

Java 通过 `webView.evaluateJavascript()` 调用前端 `window` 上的全局函数：

| 前端函数 | 调用方 | 参数 | 说明 |
|---------|--------|------|------|
| `window.updateCarState(json)` | MainActivity | JSON字符串 | 车辆状态更新 |
| `window.updateMusicStatus(json)` | MainActivity | JSON字符串 | 音乐状态更新 |
| `window.updateMusicVisualization(data)` | MusicVisualizer | float数组 | 音乐频谱数据 |
| `window.AutomationManager.trigger(id)` | AutomationEngine | 场景ID | 触发自动化场景 |

---

**文档版本**：v2.0
**最后更新**：2026-07-09
