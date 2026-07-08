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

### 4. bootstrap.js (52行) — 初始化队列

| 函数 | 行号 | 说明 |
|------|------|------|
| `safeInit(name, fn)` | L10 | 安全初始化（try-catch） |
| `registerInit(fn)` | L20 | 注册初始化任务 |
| `runInit()` | L24 | 执行初始化队列 |

### 5. settings-sync.js (174行) — 设置同步

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

### 6. app-list-manager.js (389行) — 应用列表

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

### 7. widgets.js (603行) — Widget 管理

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

### 8. music.js (337行) — 音乐可视化

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

### 9. 其他模块

| 文件 | 行数 | 关键函数 |
|------|------|----------|
| settings.js | 402 | `Settings.init()`, `Settings.load()`, `Settings.save()`, `Settings.get()`, `Settings.set()` |
| app.js | 366 | `App.init()`, `App.ready()`, `App.on()`, `App.emit()` |
| toast.js | 144 | `showToast()`, `showSuccessMessage()`, `showErrorMessage()` |
| datetime.js | 143 | `updateTime()`, `updateDate()`, `updateLunarDate()`, `getLunarDate()` |
| wallpaper-manager.js | 812 | `init()`, `applyWallpaper()`, `nextWallpaper()`, `startCarousel()`, `WallpaperSwipeManager.*` |
| weather.js | 166 | `initWeatherDisplay()`, `fetchWeatherData()`, `updateWeatherDisplay()` |
| map.js | 169 | `showMapAppSelectionDialog()`, `loadMapAppListForSelection()` |
| async-callback-manager.js | 59 | `generateId()`, `register()`, `execute()` |
| system-music-manager.js | 179 | `SystemMusicManager.init()`, `updateMusicInfo()`, `checkPermission()` |
| car-state-manager.js | 355 | `updateState()`, `updateGearIndicator()`, `updateDoorIndicator()`, `updateTurnIndicators()`, `window.updateCarState()` |
| gear-bridge.js | 184 | `onGearChanged()`, `setupGearListener()`, `init()` |
| automation-manager.js | 396 | `init()`, `loadSettings()`, `toggleScenario()`, `showPanel()`, `trigger()` |
| quick-switch-manager.js | 422 | `showPanel()`, `hidePanel()`, `toggleSwitch()`, `setDriveMode()`, `refreshSwitchStates()` |
| index.js | 1003 | 主入口：面板控制(showSettingsModal/hideSettingsModal/showAppsModal/hideAppsModal)、UI初始化(loadQuickApps/loadQuickSwitches/updateNetworkAndBluetoothStatus等)、统一初始化队列 |

---

## 二、后端 Java 方法索引

### WebViewBridge.java (1013行) — JS 统一入口

> 所有 `@JavascriptInterface` 方法，按功能分组

#### 车控接口（委托 CarControlBridge）

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `startCamera360()` | — | boolean | 启动360全景 |
| `setCameraOverspeedLimit(on)` | boolean | boolean | 360超速限制 |
| `isCameraOverspeedLimitEnabled()` | — | boolean | 查询超速限制 |
| `setLowBeamLight(on)` | boolean | boolean | 近光灯 |
| `isLowBeamLightOn()` | — | boolean | 近光灯状态 |
| `setRearFogLight(on)` | boolean | boolean | 后雾灯 |
| `isRearFogLightOn()` | — | boolean | 查询后雾灯 |
| `setPositionLight(on)` | boolean | boolean | 示廓灯 |
| `isPositionLightOn()` | — | boolean | 查询示廓灯 |
| `setPedestrianAlert(on)` | boolean | boolean | 行人警示音 |
| `isPedestrianAlertOn()` | — | boolean | 查询行人警示 |
| `setDriveMode(mode)` | int(0-5) | boolean | 驾驶模式 |
| `setGuardMode(on)` | boolean | boolean | 守护模式 |
| `setRestMode(on)` | boolean | boolean | 小憩模式 |
| `setCampingMode(on)` | boolean | boolean | 露营模式 |
| `setPowerSaveMode(on)` | boolean | boolean | 省电模式 |
| `setSentinelMode(on)` | boolean | boolean | 哨兵模式 |
| `setMaxCooling(on)` | boolean | boolean | 极速制冷 |
| `isMaxCoolingOn()` | — | boolean | 查询极速制冷 |
| `setAcEnabled(on)` | boolean | boolean | 空调开关 |
| `isAcEnabled()` | — | boolean | 查询空调状态 |
| `setWindLevel(level)` | int | boolean | 风量等级 |
| `getWindLevel()` | — | int | 获取风量 |
| `setDriverTemp(temp)` | int | boolean | 主驾温度 |
| `getDriverTemp()` | — | int | 获取主驾温度 |
| `setPassengerTemp(temp)` | int | boolean | 副驾温度 |
| `getPassengerTemp()` | — | int | 获取副驾温度 |
| `toggleAirConditioning()` | — | boolean | 切换空调 |
| `toggleDefrost()` | — | boolean | 切换除霜 |
| `toggleMute()` | — | boolean | 切换静音 |
| `toggleAC()` | — | boolean | 切换空调（别名） |
| `increaseTemperature()` | — | boolean | 温度+1 |
| `decreaseTemperature()` | — | boolean | 温度-1 |
| `increaseWindSpeed()` | — | boolean | 风量+1 |
| `decreaseWindSpeed()` | — | boolean | 风量-1 |
| `getAcInfo()` | — | String(JSON) | 空调信息 |
| `setCallVolume(v)` | int | boolean | 电话音量 |
| `getCallVolume()` | — | int | 获取电话音量 |
| `setNaviVolume(v)` | int | boolean | 导航音量 |
| `getNaviVolume()` | — | int | 获取导航音量 |
| `setAmbientLightEnabled(on)` | boolean | boolean | 氛围灯 |
| `isAmbientLightEnabled()` | — | boolean | 查询氛围灯 |
| `setAmbientLightColor(color)` | int | boolean | 氛围灯颜色 |
| `getAmbientLightColor()` | — | int | 获取氛围灯颜色 |
| `setNightMode(on)` | boolean | boolean | 夜间模式 |
| `isNightModeOn()` | — | boolean | 查询夜间模式 |
| `setDriverSeatHeating(level)` | int(0-3) | boolean | 座椅加热 |
| `setPassengerSeatHeating(level)` | int(0-3) | boolean | 副驾座椅加热 |
| `setDriverSeatVentilation(level)` | int(0-3) | boolean | 座椅通风 |
| `setPassengerSeatVentilation(level)` | int(0-3) | boolean | 副驾座椅通风 |
| `setSteeringWheelHeating(on)` | boolean | boolean | 方向盘加热 |
| `foldMirrors()` | — | boolean | 折叠后视镜 |
| `unfoldMirrors()` | — | boolean | 展开后视镜 |
| `setMirrorHeating(on)` | boolean | boolean | 后视镜加热 |
| `isVehicleLocked()` | — | boolean | 查询锁车状态 |
| `isScreenOn()` | — | boolean | 查询屏幕状态 |
| `setWifiEnabled(on)` | boolean | boolean | WiFi开关 |
| `isWifiEnabled()` | — | boolean | 查询WiFi |
| `setBluetoothEnabled(on)` | boolean | boolean | 蓝牙开关 |
| `isBluetoothEnabled()` | — | boolean | 查询蓝牙 |
| `setVideoWhileDriving(on)` | boolean | boolean | 行驶中视频 |
| `isVideoWhileDrivingEnabled()` | — | boolean | 查询行驶视频 |
| `setSecondaryScreenEnabled(on)` | boolean | boolean | 副屏开关 |
| `isSecondaryScreenEnabled()` | — | boolean | 查询副屏 |
| `setSpeechEnabled(on)` | boolean | boolean | 语音播报 |
| `isSpeechEnabled()` | — | boolean | 查询语音播报 |
| `sendVoiceCommand(cmd)` | String | boolean | 发送语音指令 |
| `getCarState()` | — | String(JSON) | 获取车辆状态 |

#### 应用接口（委托 AppBridge）

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `getAppList()` | — | String(JSON) | 应用列表（字母分组） |
| `getAllApps()` | — | String(JSON) | 所有已安装应用 |
| `getUserApps()` | — | String(JSON) | 用户安装应用 |
| `getSystemApps()` | — | String(JSON) | 系统应用 |
| `getAppInfo(pkg)` | String | String(JSON) | 应用信息 |
| `getAppIcon(pkg)` | String | String(Base64) | 应用图标 |
| `isAppInstalled(pkg)` | String | boolean | 是否已安装 |
| `launchApp(pkg)` | String | void | 启动应用 |
| `openAppInfo(pkg)` | String | void | 打开应用信息页 |
| `getQuickAppList()` | — | String(JSON) | 快捷应用列表 |
| `addQuickApp(name, pkg, iconBase64)` | String, String, String | void | 添加快捷应用 |
| `removeQuickApp(pkg)` | String | void | 移除快捷应用 |
| `isQuickApp(pkg)` | String | boolean | 是否快捷应用 |
| `saveConfigApp(buttonId, appName, pkg, icon)` | String, String, String, String | void | 保存配置应用 |
| `getConfigApp(key)` | String | String | 获取配置应用 |
| `getAppListAsync(callbackId)` | String | void | 异步获取（回调） |

#### 壁纸接口（委托 WallpaperBridge）

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `getWallpaperSettings()` | — | String(JSON) | 获取壁纸设置 |
| `getWallpaperSettingsAsync(callbackId)` | String | void | 异步获取 |
| `getWallpaperSettingsV2()` | — | String(JSON) | V2版设置 |
| `setWallpaperType(type)` | int | void | 壁纸类型 |
| `setWallpaperPath(path)` | String | void | 壁纸路径 |
| `setWallpaperCarouselEnabled(on)` | boolean | void | 轮播开关 |
| `setWallpaperCarouselInterval(sec)` | int | void | 轮播间隔 |
| `setWallpaperFillMode(mode)` | int | void | 填充模式 |
| `getCurrentWallpaperUrl()` | — | String | 当前壁纸URL |
| `getNextWallpaperUrl()` | — | String | 下一张URL |
| `refreshBingWallpaper()` | — | void | 刷新必应壁纸 |
| `deleteCurrentWallpaper()` | — | boolean | 删除当前壁纸 |
| `pauseWallpaperCarousel()` | — | void | 暂停轮播 |
| `resumeWallpaperCarousel()` | — | void | 恢复轮播 |
| `getRandomWallpaper()` | — | String | 随机壁纸URL |
| `getRandomWallpaperAsync(callbackId)` | String | void | 异步获取 |
| `getRandomWallpaperBase64()` | — | String | 随机壁纸Base64 |
| `getRandomWallpaperBase64Async(callbackId)` | String | void | 异步获取 |
| `updateWallpaperCategories()` | — | void | 更新分类 |
| `updateCategoryEnabled(name, on)` | String, boolean | void | 启用/禁用分类 |
| `updateCategoryEnabledAsync(name, on, callbackId)` | String, boolean, String | void | 异步更新 |
| `getEnabledCategories()` | — | String(JSON) | 已启用分类 |
| `getEnabledCategoriesAsync(callbackId)` | String | void | 异步获取 |
| `saveWallpaperCarouselSetting(on)` | boolean | boolean | 保存轮播设置 |
| `saveWallpaperCarouselSettingAsync(on, callbackId)` | boolean, String | void | 异步保存 |
| `saveWallpaperSwitchInterval(sec)` | int | boolean | 保存切换间隔 |
| `saveWallpaperSwitchIntervalAsync(sec, callbackId)` | int, String | void | 异步保存 |
| `sendWallpaperSettingsChangedBroadcast()` | — | void | 发送设置变更广播 |

#### 音乐接口（委托 MusicBridge）

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `startMusicVisualizer()` | — | void | 启动频谱可视化 |
| `stopMusicVisualizer()` | — | void | 停止可视化 |
| `isMusicPlaying()` | — | boolean | 播放状态 |
| `getCurrentMusicName()` | — | String | 歌曲名 |
| `getCurrentMusicArtist()` | — | String | 歌手名 |
| `getMusicProgressInfo()` | — | String(JSON) | 进度信息 |
| `getSystemMusicInfo()` | — | String(JSON) | 系统音乐信息 |
| `playPauseMusic()` | — | void | 播放/暂停 |
| `nextMusic()` | — | void | 下一首 |
| `prevMusic()` | — | void | 上一首 |
| `setMusicVolume(v)` | int | void | 媒体音量 |
| `getMusicVolume()` | — | int | 获取媒体音量 |
| `volumeUp()` | — | void | 音量+1 |
| `volumeDown()` | — | void | 音量-1 |
| `seekTo(pos)` | long(ms) | void | 跳转到位置 |
| `setPlaybackSpeed(speed)` | float | void | 播放速度 |
| `isNotificationListenerEnabled()` | — | boolean | 检查通知权限 |
| `openNotificationListenerSettings()` | — | void | 打开设置页 |
| `hasNotificationAccess()` | — | boolean | 查询权限 |
| `getPlaylist()` | — | String(JSON) | 播放列表 |
| `playSongAtIndex(i)` | int | void | 播放指定歌曲 |
| `setRepeatMode(m)` | int | void | 循环模式 |
| `getRepeatMode()` | — | int | 获取循环模式 |
| `setShuffleMode(on)` | boolean | void | 随机播放 |
| `isShuffleEnabled()` | — | boolean | 查询随机 |

#### 系统接口（委托 SystemBridge）

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `isWifiConnected()` | — | boolean | WiFi是否连接 |
| `isBluetoothConnected()` | — | boolean | 蓝牙是否连接 |
| `isLocationEnabled()` | — | boolean | 定位是否开启 |
| `setScreenBrightness(v)` | int(0-255) | boolean | 屏幕亮度 |
| `getScreenBrightness()` | — | int | 获取亮度 |
| `setAutoBrightness(on)` | boolean | boolean | 自动亮度 |
| `isAutoBrightnessEnabled()` | — | boolean | 查询自动亮度 |
| `setScreenTimeout(sec)` | int | boolean | 屏幕超时 |
| `getScreenTimeout()` | — | int | 获取超时 |
| `getSystemVersionInfo()` | — | String(JSON) | 系统版本 |
| `getAppVersionInfo()` | — | String(JSON) | 应用版本 |
| `getMemoryInfo()` | — | String(JSON) | 内存信息 |
| `getBatteryInfo()` | — | String(JSON) | 电池信息 |
| `getLunarCalendar()` | — | String(JSON) | 农历日期 |
| `restartApp()` | — | void | 重启应用 |
| `openSystemSettings()` | — | void | 打开系统设置 |
| `openAppSettings()` | — | void | 打开应用设置 |
| `showToast(msg)` | String | void | 显示Toast |
| `saveComponentConfig(id, enabled)` | String, boolean | boolean | 保存组件配置 |
| `isComponentEnabled(id)` | String | boolean | 查询组件 |
| `getAllComponentConfigs()` | — | String(JSON) | 所有配置 |
| `saveSystemLauncherSetting(on)` | boolean | boolean | 原桌面自启 |
| `saveSystemLauncherSettingAsync(on, callbackId)` | boolean, String | void | 异步保存 |
| `saveBootGreetingSetting(on)` | boolean | boolean | 开机问候语 |
| `saveRandomModeSetting(on)` | boolean | boolean | 随机模式 |
| `saveSpecifiedModeSetting(enabled)` | boolean | boolean | 指定模式 |

#### ADB 接口（委托 AdbBridge）

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `triggerUsbDebugAuthorization()` | — | void | USB调试授权 |
| `triggerWirelessAdbAuthorization()` | — | void | 本地ADB授权 |
| `executeAdbPermissionGrant()` | — | void | 授权三大权限 |
| `setDefaultDesktopViaAdb()` | — | void | 设置默认桌面 |
| `openRecentTasks()` | — | void | 打开最近任务 |
| `openRecents()` | — | void | 同上（别名） |
| `enableAdbDebugging()` | — | void | 启用ADB调试 |
| `executeAdbCommand(cmd)` | String | void | 执行ADB命令 |
| `setDefaultDesktop()` | — | void | 设置默认桌面（别名） |
| `openRecentsViaAdb()` | — | void | 通过ADB打开最近任务 |

#### 副屏接口（Stub）

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `isPresentationShowing()` | — | boolean | 是否显示副屏 |
| `hidePresentation()` | — | void | 隐藏副屏 |
| `showCarStatusPresentation()` | — | boolean | 显示车辆状态副屏 |

#### 自动化接口

| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `getAutomationSettings()` | — | String(JSON) | 获取配置 |
| `setAutomationSettings(json)` | String(JSON) | boolean | 保存配置 |
| `setAutomationScenarioEnabled(id, enabled)` | String, boolean | boolean | 启用/禁用场景 |

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
| `window.updateTimeDisplay(time, date, lunarDate)` | MainActivity | String, String, String | 时间更新 |
| `window.initializeAcStatus(json)` | MainActivity | JSON字符串 | 空调状态初始化 |
| `window.handleWallpaperUpdateNotification()` | WebViewBridge | — | 壁纸更新通知 |

---

**文档版本**：v2.1
**最后更新**：2026-07-09
