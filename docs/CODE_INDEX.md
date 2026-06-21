# C11Partner 代码索引文档
> 📌 **快速定位代码的神器** - 修改代码前先看本文档，找到对应函数行号再精准读取，避免读取大文件占用上下文
>
> 自动生成时间：2026-06-21 19:18:38
> 生成脚本：`tools/generate_code_index.py`

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

### 重新生成索引
```bash
python3 tools/generate_code_index.py
```

---

## 📊 索引概览

| 类型 | 文件数 | 函数/方法数 |
|------|--------|------------|
| Java后端 | 8 | 480 |
| JS前端 | 3 | 145 |
| **总计** | **11** | **625** |

---

## 🔧 后端核心文件索引（Java）

### MainActivity.java
**路径**：`app/src/main/java/com/c11partner/desktop/MainActivity.java`  
**行数**：约 2107 行  
**职责**：主Activity，应用入口，状态推送，生命周期管理

#### 匿名内部类

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `run()` | 171 | public |
| `run()` | 1258 | public |
| `createTestFile()` | 1339 | private |

#### 状态获取

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getLogcatMonitorService()` | 211 | public |
| `isLogServiceBound()` | 218 | public |
| `onServiceDisconnected()` | 234 | public |
| `onPageFinished()` | 598 | public |
| `isTtsEngineAvailable()` | 694 | private |
| `setupEnglishLanguage()` | 748 | private |
| `generateVisualizationData()` | 838 | private |
| `sendVisualizationDataToFrontend()` | 884 | private |
| `unregisterReceivers()` | 1511 | private |
| `getSystemProperty()` | 1576 | private |
| `onServiceDisconnected()` | 1624 | public |

#### 其他

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `onServiceConnected()` | 224 | public |
| `onBackPressed()` | 495 | public |
| `onActivityResult()` | 503 | protected |
| `checkTTSEngines()` | 632 | private |
| `configureTTSParams()` | 763 | private |
| `generateFrequencyData()` | 850 | private |
| `generateWaveformData()` | 867 | private |
| `prepareDeletedDirectory()` | 1110 | private |
| `showManageStorageDialog()` | 1250 | private |
| `onClick()` | 1264 | public |
| `onClick()` | 1287 | public |
| `speakText()` | 1363 | private |
| `playRandomGreeting()` | 1391 | public |
| `onQuoteReceived()` | 1395 | public |
| `unbindServices()` | 1503 | private |
| `onServiceConnected()` | 1614 | public |
| `onReceive()` | 1634 | public |
| `onReceive()` | 1643 | public |
| `onReceive()` | 1654 | public |
| `onReceive()` | 1665 | public |
| `cleanExpiredCache()` | 1710 | private |
| `clearAppIconCache()` | 1722 | private |

#### 状态监听/回调

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `onGearChanged()` | 242 | public |
| `onTurnLightChanged()` | 248 | public |
| `onDoorChanged()` | 254 | public |
| `onSpeedChanged()` | 260 | public |
| `onLockStateChanged()` | 265 | public |

#### 初始化

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `onNeedStart360()` | 271 | public |
| `startLogcatMonitorService()` | 338 | private |
| `initDatabaseHelpers()` | 545 | private |
| `initWallpaperSettingsReceiver()` | 558 | public |
| `initRestartAppReceiver()` | 568 | public |
| `initWebView()` | 578 | private |
| `initTextToSpeech()` | 616 | public |
| `createTTSInstance()` | 647 | private |
| `onInit()` | 656 | public |
| `createDefaultTtsInstance()` | 676 | private |
| `onInit()` | 679 | public |
| `handleTTSInitFailure()` | 772 | private |
| `initMusicVisualizer()` | 785 | public |
| `createMusicVisualizer()` | 799 | private |
| `startMusicPlaybackCheck()` | 919 | public |
| `startWallpaperCarousel()` | 1012 | public |
| `startTimeUpdate()` | 1148 | public |
| `initSettings()` | 1156 | private |
| `scheduleDelayedStartupTasks()` | 1217 | public |
| `createDirectoriesInRoot()` | 1298 | private |
| `restartWallpaperCarousel()` | 1479 | public |
| `cancelDelayedStartupTasks()` | 1495 | private |
| `rescheduleDelayedStartupTasks()` | 1533 | public |
| `initAppListToDatabase()` | 1587 | private |
| `getRandomWallpaperFromFstartExcept00()` | 1765 | private |
| `getRandomWallpaperFromFstart00()` | 1814 | private |

#### 生命周期

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `onCreate()` | 287 | protected |
| `onStart()` | 417 | protected |
| `onResume()` | 429 | protected |
| `onPause()` | 442 | protected |
| `onStop()` | 454 | protected |
| `onDestroy()` | 466 | protected |

#### 时间日期

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `updateCarStateToFrontend()` | 357 | private |
| `updateVisualizationData()` | 830 | private |
| `stopTimeUpdate()` | 1463 | private |
| `getWeekDay()` | 1562 | private |

#### 权限相关

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `onRequestPermissionsResult()` | 521 | public |
| `requestStoragePermission()` | 1228 | public |

#### 保存/设置

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `setupTTSLanguage()` | 711 | private |
| `setFullscreenMode()` | 1450 | private |

#### 图片处理

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `setupSimplifiedChineseLanguage()` | 731 | private |
| `getCachedAppIconBase64()` | 1691 | private |
| `cacheAppIconBase64()` | 1705 | private |
| `encodeImageToBase64()` | 1932 | private |
| `drawableToBase64()` | 1999 | private |
| `drawableToBitmap()` | 2013 | private |
| `scaleBitmap()` | 2038 | private |
| `bitmapToBase64()` | 2050 | private |
| `getFirstLetter()` | 2064 | private |
| `isChineseChar()` | 2079 | private |
| `getChineseFirstLetter()` | 2085 | private |

#### 音乐相关

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `setupVisualizerListener()` | 807 | private |
| `onVisualizerUpdate()` | 813 | public |
| `registerMusicPlaybackReceiver()` | 894 | private |
| `checkMusicPlaybackState()` | 926 | public |
| `updateMusicPlaybackState()` | 941 | private |
| `updateMusicStatus()` | 983 | public |
| `requestAudioPermission()` | 1221 | public |
| `stopMusicCheck()` | 1487 | private |

#### 壁纸相关

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `pauseWallpaperCarousel()` | 1032 | public |
| `resumeWallpaperCarousel()` | 1040 | public |
| `deleteCurrentWallpaper()` | 1053 | public |
| `canDeleteCurrentWallpaper()` | 1077 | private |
| `performWallpaperDeletion()` | 1124 | private |
| `stopWallpaperCarousel()` | 1471 | public |
| `sendWallpaperSettingsChangedBroadcast()` | 1546 | public |
| `getRandomLocalWallpaper()` | 1727 | private |
| `getRandomOnlineWallpaper()` | 1847 | private |
| `getRandomOnlineWallpaperBase64()` | 1888 | private |
| `getRandomWallpaperUrl()` | 1958 | private |

#### 应用管理

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `preloadAppList()` | 1172 | private |
| `launchSystemHome()` | 1412 | public |

#### ADB相关

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `triggerUsbDebugAuthorization()` | 1600 | public |

---

### WebViewBridge.java ⭐
**路径**：`app/src/main/java/com/c11partner/desktop/bridge/WebViewBridge.java`  
**行数**：约 3924 行  
**职责**：JS桥接层，提供50+个JS接口给前端调用

#### 保存/设置

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `setMediaSessionService()` | 152 | public |
| `setDefaultDesktop()` | 2657 | public |

#### 应用管理

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getAppList()` | 166 | public |
| `getAppListAsync()` | 305 | public |
| `launchApp()` | 1021 | public |
| `getQuickAppList()` | 1041 | public |
| `addQuickApp()` | 1053 | public |
| `removeQuickApp()` | 1063 | public |
| `isQuickApp()` | 1074 | public |
| `saveConfigApp()` | 1087 | public |
| `getConfigApp()` | 1098 | public |
| `saveComponentConfig()` | 1503 | public |
| `isComponentEnabled()` | 1529 | public |
| `getAllComponentConfigs()` | 1546 | public |
| `getQuickAppList()` | 1699 | public |
| `addQuickApp()` | 1711 | public |
| `removeQuickApp()` | 1721 | public |
| `isQuickApp()` | 1732 | public |
| `saveSystemLauncherSetting()` | 2147 | public |
| `saveSystemLauncherSettingAsync()` | 2230 | public |

#### 匿名内部类

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `compare()` | 208 | public |
| `run()` | 943 | public |
| `run()` | 2233 | public |
| `run()` | 2239 | public |
| `run()` | 2253 | public |
| `run()` | 2277 | public |
| `run()` | 2283 | public |
| `run()` | 2297 | public |
| `run()` | 2321 | public |
| `run()` | 2327 | public |
| `run()` | 2341 | public |
| `run()` | 2365 | public |
| `run()` | 2371 | public |
| `run()` | 2385 | public |
| `run()` | 2422 | public |
| `run()` | 2450 | public |
| `run()` | 2471 | public |
| `run()` | 2492 | public |
| `run()` | 2541 | public |
| `run()` | 2900 | public |
| `run()` | 2996 | public |
| `isLogServiceRunning()` | 3131 | public |
| `run()` | 3567 | public |
| `run()` | 3632 | public |
| `run()` | 3668 | public |
| `run()` | 3698 | public |

#### 壁纸相关

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `saveWallpaperCarouselSettingAsync()` | 316 | public |
| `saveWallpaperCarouselSetting()` | 327 | public |
| `saveWallpaperSwitchIntervalAsync()` | 338 | public |
| `getWallpaperSettingsAsync()` | 370 | public |
| `getRandomWallpaperAsync()` | 380 | public |
| `getRandomWallpaperBase64Async()` | 390 | public |
| `saveWallpaperSwitchInterval()` | 401 | public |
| `sendWallpaperSettingsChangedBroadcast()` | 409 | public |
| `getRandomWallpaper()` | 419 | public |
| `getRandomLocalWallpaper()` | 428 | private |
| `getRandomOnlineWallpaper()` | 591 | private |
| `getRandomOnlineWallpaperBase64()` | 648 | private |
| `getRandomWallpaperUrl()` | 700 | private |
| `notifyWallpaperUpdate()` | 940 | public |
| `pauseWallpaperCarousel()` | 1106 | public |
| `resumeWallpaperCarousel()` | 1114 | public |
| `deleteCurrentWallpaper()` | 1123 | public |
| `updateWallpaperCategories()` | 1568 | public |
| `old_updateWallpaperCategories()` | 1577 | private |
| `getWallpaperSettings()` | 1647 | public |
| `getWallpaperSettingsAsync()` | 1657 | public |
| `getRandomWallpaperAsync()` | 1667 | public |
| `getRandomWallpaperBase64()` | 1677 | public |
| `getRandomWallpaperBase64Async()` | 1687 | public |
| `getWallpaperManager()` | 3826 | private |
| `getWallpaperSettingsV2()` | 3834 | public |
| `setWallpaperType()` | 3843 | public |
| `setWallpaperPath()` | 3851 | public |
| `getCurrentWallpaperUrl()` | 3859 | public |
| `getNextWallpaperUrl()` | 3867 | public |
| `setWallpaperCarouselEnabled()` | 3875 | public |
| `setWallpaperCarouselInterval()` | 3884 | public |
| `setWallpaperFillMode()` | 3893 | public |
| `refreshBingWallpaper()` | 3901 | public |
| `pickLocalWallpaperFile()` | 3910 | public |
| `pickLocalWallpaperFolder()` | 3919 | public |

#### 时间日期

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `updateCategoryEnabledAsync()` | 350 | public |
| `updateCategoryEnabled()` | 1614 | public |
| `updateCategoryEnabledAsync()` | 1635 | public |
| `getLunarCalendar()` | 2197 | public |
| `updateTimeDisplay()` | 2408 | public |
| `updateTimeDisplayAsync()` | 2447 | public |
| `getWeekDay()` | 2512 | private |

#### 状态获取

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getEnabledCategoriesAsync()` | 360 | public |
| `getAllApps()` | 958 | public |
| `getUserApps()` | 968 | public |
| `getSystemApps()` | 978 | public |
| `getAppInfo()` | 989 | public |
| `getAppIcon()` | 1000 | public |
| `isAppInstalled()` | 1011 | public |
| `getDeviceIpAddress()` | 1218 | private |
| `getEnabledCategories()` | 1623 | public |
| `isNotificationListenerEnabled()` | 1943 | public |
| `isWifiConnected()` | 1977 | public |
| `isBluetoothConnected()` | 2007 | public |
| `getStaticIntValue()` | 3066 | private |
| `getCarState()` | 3084 | public |
| `isCameraOverspeedLimitEnabled()` | 3351 | public |
| `isVideoWhileDrivingEnabled()` | 3368 | public |
| `isVehicleLocked()` | 3732 | public |
| `isScreenOn()` | 3741 | public |

#### 初始化

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getRandomWallpaperFromFstartExcept00()` | 482 | private |
| `getRandomWallpaperFromFstart00()` | 545 | private |
| `startMusicVisualizer()` | 1742 | public |
| `initializeAcStatus()` | 2530 | public |
| `restartApp()` | 2560 | public |
| `startCamera360()` | 3121 | public |

#### 图片处理

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `encodeImageToBase64()` | 715 | private |
| `drawableToBase64()` | 730 | private |
| `drawableToBitmap()` | 753 | private |
| `scaleBitmap()` | 796 | private |
| `bitmapToBase64()` | 816 | private |
| `getFirstLetter()` | 838 | private |
| `isChineseChar()` | 865 | private |
| `getChineseFirstLetter()` | 880 | private |
| `cacheAppIconBase64()` | 918 | private |
| `getCachedAppIconBase64()` | 929 | private |

#### 其他

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `clearAppIconCache()` | 900 | private |
| `openAppInfo()` | 1031 | public |
| `showToastOnUiThread()` | 1272 | private |
| `openRecentTasks()` | 1389 | public |
| `openRecents()` | 1415 | public |

#### ADB相关

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `triggerUsbDebugAuthorization()` | 1132 | public |
| `triggerWirelessAdbAuthorization()` | 1171 | public |
| `executeWirelessAdbAuthorization()` | 1282 | private |
| `executeAdbPermissionGrant()` | 1343 | public |
| `setDefaultDesktopViaAdb()` | 1439 | public |
| `autoConnectWirelessAdb()` | 2689 | public |
| `executeAdbAuthorization()` | 2754 | private |
| `isUsbDebuggingEnabled()` | 2785 | private |
| `executeAdbCommands()` | 2802 | private |
| `executeAdbCommandsAfterConnection()` | 2855 | private |

#### 音乐相关

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `stopMusicVisualizer()` | 1763 | public |
| `isMusicPlaying()` | 1783 | public |
| `getCurrentMusicName()` | 1794 | public |
| `getMusicProgressInfo()` | 1805 | public |
| `getSystemMusicInfo()` | 1834 | public |
| `playPauseMusic()` | 1868 | public |
| `nextMusic()` | 1885 | public |
| `prevMusic()` | 1901 | public |
| `sendMediaButton()` | 1916 | private |
| `playPause()` | 2095 | public |
| `playNext()` | 2113 | public |
| `playPrevious()` | 2125 | public |

#### 系统设置

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `openNotificationListenerSettings()` | 1963 | public |
| `saveBootGreetingSetting()` | 2164 | public |
| `saveRandomModeSetting()` | 2181 | public |
| `saveSpecifiedModeSetting()` | 2213 | public |
| `saveBootGreetingSettingAsync()` | 2274 | public |
| `saveRandomModeSettingAsync()` | 2318 | public |
| `saveSpecifiedModeSettingAsync()` | 2362 | public |

#### 导航相关

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `isAmapInstalled()` | 2572 | private |
| `navigateToHome()` | 2586 | public |
| `navigateToCompany()` | 2622 | public |

#### 车控功能

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `toggleAirConditioning()` | 2888 | public |
| `openAirConditioningPage()` | 2918 | private |
| `getAcInfo()` | 2949 | public |
| `adjustTemperature()` | 2959 | public |
| `adjustWindLevel()` | 2984 | public |
| `toggleDefrost()` | 3016 | public |
| `sendVoiceCommand()` | 3036 | public |
| `setLowBeamLight()` | 3200 | public |
| `setRearFogLight()` | 3208 | public |
| `setPositionLight()` | 3216 | public |
| `setPedestrianAlert()` | 3224 | public |
| `setDriveMode()` | 3235 | public |
| `setGuardMode()` | 3245 | public |
| `setRestMode()` | 3253 | public |
| `setCampingMode()` | 3261 | public |
| `setPowerSaveMode()` | 3269 | public |
| `setSentinelMode()` | 3277 | public |
| `setMaxCooling()` | 3287 | public |
| `setNightMode()` | 3298 | public |
| `setWifiEnabled()` | 3306 | public |
| `setBluetoothEnabled()` | 3314 | public |
| `sendPrevTrack()` | 3324 | public |
| `sendNextTrack()` | 3332 | public |
| `setCameraOverspeedLimit()` | 3343 | public |
| `setVideoWhileDriving()` | 3360 | public |
| `setCallVolume()` | 3378 | public |
| `setNaviVolume()` | 3394 | public |
| `setMusicVolume()` | 3410 | public |
| `setDriverTemp()` | 3429 | public |
| `setPassengerTemp()` | 3445 | public |
| `setAmbientLightEnabled()` | 3463 | public |
| `setAmbientLightColor()` | 3480 | public |
| `setSecondaryScreenEnabled()` | 3512 | public |

#### 权限相关

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `hasNotificationAccess()` | 3046 | public |
| `hasReadLogsPermission()` | 3141 | public |
| `hasWriteSecureSettingsPermission()` | 3157 | public |
| `hasDumpPermission()` | 3173 | public |

#### 音量控制

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getCallVolume()` | 3386 | public |
| `getNaviVolume()` | 3402 | public |
| `getMusicVolume()` | 3418 | public |

#### 空调控制

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getDriverTemp()` | 3437 | public |
| `getPassengerTemp()` | 3453 | public |

#### 灯光控制

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `isAmbientLightEnabled()` | 3471 | public |
| `getAmbientLightColor()` | 3488 | public |

#### 副屏相关

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `isSecondaryScreenEnabled()` | 3520 | public |
| `hasSecondaryDisplay()` | 3528 | public |
| `getSecondaryDisplayInfo()` | 3541 | public |
| `showCarStatusPresentation()` | 3554 | public |
| `hidePresentation()` | 3624 | public |
| `isPresentationShowing()` | 3652 | public |
| `updatePresentationCarState()` | 3664 | public |
| `updatePresentationTime()` | 3694 | public |

#### 语音控制

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `setSpeechEnabled()` | 3713 | public |
| `isSpeechEnabled()` | 3721 | public |

#### 自动化

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getAutomationSettings()` | 3767 | public |
| `setAutomationSettings()` | 3781 | public |
| `isAutomationScenarioEnabled()` | 3796 | public |
| `setAutomationScenarioEnabled()` | 3811 | public |

---

### CarControlManager.java ⭐
**路径**：`app/src/main/java/com/c11partner/desktop/utils/CarControlManager.java`  
**行数**：约 678 行  
**职责**：车控功能管理类，三层控制模型的核心实现

#### 状态获取

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getInstance()` | 70 | public |
| `isAcEnabled()` | 322 | public |
| `isCameraOverspeedLimitEnabled()` | 499 | public |
| `isVideoWhileDrivingEnabled()` | 516 | public |
| `isVehicleLocked()` | 664 | public |
| `isScreenOn()` | 675 | public |

#### 初始化

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `startCamera360()` | 82 | public |

#### 车控功能

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `setLowBeamLight()` | 101 | public |
| `setRearFogLight()` | 117 | public |
| `setPositionLight()` | 133 | public |
| `setPedestrianAlert()` | 149 | public |
| `setDriveMode()` | 168 | public |
| `setGuardMode()` | 186 | public |
| `setRestMode()` | 202 | public |
| `setCampingMode()` | 218 | public |
| `setPowerSaveMode()` | 234 | public |
| `setSentinelMode()` | 250 | public |
| `sendVoiceCommand()` | 271 | public |
| `setMaxCooling()` | 289 | public |
| `setAcEnabled()` | 307 | public |
| `setWindLevel()` | 331 | public |
| `setDefrost()` | 355 | public |
| `setNightMode()` | 367 | public |
| `setWifiEnabled()` | 383 | public |
| `setBluetoothEnabled()` | 399 | public |
| `sendPrevTrack()` | 417 | public |
| `sendNextTrack()` | 433 | public |
| `setCameraOverspeedLimit()` | 492 | public |
| `setVideoWhileDriving()` | 509 | public |
| `setCallVolume()` | 525 | public |
| `setNaviVolume()` | 539 | public |
| `setMusicVolume()` | 553 | public |
| `setDriverTemp()` | 570 | public |
| `setPassengerTemp()` | 584 | public |
| `setAmbientLightEnabled()` | 600 | public |
| `setAmbientLightColor()` | 615 | public |
| `setSecondaryScreenEnabled()` | 631 | public |

#### 空调控制

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getWindLevel()` | 346 | public |
| `getDriverTemp()` | 577 | public |
| `getPassengerTemp()` | 591 | public |

#### 系统设置

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getGlobalInt()` | 451 | public |
| `getGlobalString()` | 463 | public |
| `setGlobalInt()` | 475 | public |

#### 音量控制

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getCallVolume()` | 532 | public |
| `getNaviVolume()` | 546 | public |
| `getMusicVolume()` | 560 | public |

#### 灯光控制

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `isAmbientLightEnabled()` | 607 | public |
| `getAmbientLightColor()` | 622 | public |

#### 副屏相关

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `isSecondaryScreenEnabled()` | 638 | public |

#### 语音控制

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `setSpeechEnabled()` | 647 | public |
| `isSpeechEnabled()` | 654 | public |

---

### LogcatMonitorService.java ⭐
**路径**：`app/src/main/java/com/c11partner/desktop/LogcatMonitorService.java`  
**行数**：约 621 行  
**职责**：日志监控后台服务，解析CAN信号，推送车辆状态

#### 状态获取

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getService()` | 86 | public |
| `addListener()` | 525 | public |
| `removeListener()` | 531 | public |
| `getCurrentState()` | 535 | public |

#### 生命周期

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `onCreate()` | 92 | public |
| `onStartCommand()` | 140 | public |
| `onBind()` | 146 | public |
| `onDestroy()` | 151 | public |

#### 初始化

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `createNotificationChannel()` | 104 | private |
| `createNotification()` | 122 | private |
| `startLogcatMonitoring()` | 163 | private |
| `notifyNeedStart360()` | 611 | private |

#### 日志监控

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `stopLogcatMonitoring()` | 178 | private |
| `processCanSignal()` | 313 | private |
| `parseTurnLightSignal()` | 370 | private |
| `parseTirePressureSignal()` | 476 | private |

#### 匿名内部类

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `run()` | 191 | public |
| `run()` | 544 | public |
| `run()` | 558 | public |
| `run()` | 572 | public |
| `run()` | 586 | public |
| `run()` | 600 | public |
| `run()` | 614 | public |

#### 其他

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `parseLogLine()` | 231 | private |
| `parseCanSignal()` | 289 | private |
| `parseSpeedSignal()` | 392 | private |
| `parseAcPageState()` | 426 | private |
| `parseBluetoothState()` | 442 | private |
| `parseScreenState()` | 459 | private |
| `trigger360IfNeeded()` | 492 | private |
| `extractInt()` | 503 | private |
| `extractIntAfter()` | 515 | private |
| `notifyGearChanged()` | 541 | private |
| `notifyTurnLightChanged()` | 555 | private |
| `notifyDoorChanged()` | 569 | private |
| `notifySpeedChanged()` | 583 | private |
| `notifyLockStateChanged()` | 597 | private |

#### 灯光控制

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `parseLowBeamLightSignal()` | 409 | private |

---

### AutomationEngine.java
**路径**：`app/src/main/java/com/c11partner/desktop/utils/AutomationEngine.java`  
**行数**：约 358 行  
**职责**：自动化场景引擎，管理12个预设自动化场景

#### 初始化

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `initTts()` | 62 | private |
| `onInit()` | 66 | public |

#### 自动化

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `isScenarioEnabled()` | 94 | public |
| `setScenarioEnabled()` | 115 | public |
| `getAllScenariosJson()` | 125 | public |
| `loadScenariosFromJson()` | 150 | public |

#### 状态获取

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getDefaultEnabled()` | 104 | private |

#### 状态监听/回调

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `onGearChanged()` | 175 | public |
| `onTurnLightChanged()` | 229 | public |
| `onDoorChanged()` | 243 | public |
| `onSpeedChanged()` | 265 | public |
| `onLockStateChanged()` | 289 | public |

#### 360全景

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `triggerCamera360()` | 303 | private |

#### 其他

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `triggerVoicePrompt()` | 319 | private |
| `destroy()` | 341 | public |

#### 保存/设置

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `resetCooldown()` | 354 | public |

---

### CarStatusPresentation.java
**路径**：`app/src/main/java/com/c11partner/desktop/CarStatusPresentation.java`  
**行数**：约 582 行  
**职责**：副屏车辆状态显示Presentation

#### 生命周期

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `onCreate()` | 72 | protected |
| `onStop()` | 90 | protected |

#### 初始化

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `startTimeUpdate()` | 98 | private |
| `createContentView()` | 146 | private |
| `createTireTextView()` | 330 | private |

#### 匿名内部类

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `run()` | 102 | public |
| `updateStatusIndicators()` | 534 | private |

#### 时间日期

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `stopTimeUpdate()` | 127 | private |
| `updateCurrentTime()` | 136 | private |
| `updateSpeed()` | 398 | public |
| `updateGear()` | 408 | public |
| `updateDoorStatus()` | 441 | public |
| `updateTurnLights()` | 458 | public |
| `updateTirePressure()` | 473 | public |
| `updateTireTextView()` | 490 | private |
| `updateBluetoothState()` | 518 | public |
| `updateLockState()` | 526 | public |
| `updateTime()` | 562 | public |
| `updateDisplay()` | 571 | private |

#### 灯光控制

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `updateLowBeamLight()` | 510 | public |

---

### MusicNotificationListenerService.java
**路径**：`app/src/main/java/com/c11partner/desktop/service/MusicNotificationListenerService.java`  
**行数**：约 228 行  
**职责**：音乐通知监听服务，从通知中提取音乐信息

#### 音乐相关

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `setMusicInfoListener()` | 51 | public |
| `isMusicPackage()` | 120 | private |
| `extractMusicInfo()` | 138 | private |
| `getCurrentMusicInfo()` | 198 | public |

#### 其他

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `onNotificationPosted()` | 56 | public |
| `onNotificationRemoved()` | 81 | public |

#### 状态获取

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `notifyListener()` | 187 | private |
| `getCurrentTitle()` | 211 | public |
| `getCurrentArtist()` | 218 | public |
| `isPlaying()` | 225 | public |

---

### SecondaryScreenManager.java
**路径**：`app/src/main/java/com/c11partner/desktop/utils/SecondaryScreenManager.java`  
**行数**：约 227 行  
**职责**：副屏管理类，检测和控制副屏

#### 状态获取

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getDisplays()` | 45 | public |
| `getDisplayStateName()` | 99 | private |
| `isShowing()` | 162 | public |

#### 副屏相关

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getSecondaryDisplay()` | 55 | public |
| `hasSecondaryDisplay()` | 73 | public |
| `getSecondaryDisplayInfo()` | 80 | public |
| `showPresentation()` | 121 | public |
| `hidePresentation()` | 147 | public |
| `getCurrentPresentation()` | 169 | public |
| `isSecondaryScreenEnabled()` | 191 | public |
| `launchAppOnSecondaryScreen()` | 206 | public |

#### 车控功能

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `setSecondaryScreenEnabled()` | 178 | public |

#### 其他

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `destroy()` | 222 | public |

---

## 🎨 前端核心文件索引（JS）

### index.js ⭐
**路径**：`app/src/main/assets/js/index.js`  
**行数**：约 4354 行  
**职责**：主JS文件，包含大部分前端逻辑

| 名称 | 行号 | 类型 |
|------|------|------|
| `AsyncCallbackManager` | 13 | 对象/管理器 |
| `generateId` | 22 | 方法 |
| `register` | 32 | 方法 |
| `execute` | 45 | 方法 |
| `addClickEffect()` | 72 | 函数 |
| `initSettingsModal()` | 138 | 函数 |
| `loadWallpaperSettings()` | 287 | 函数 |
| `loadSystemSettings()` | 388 | 函数 |
| `loadEnabledCategories()` | 403 | 函数 |
| `initCategoryCheckboxEvents()` | 445 | 函数 |
| `initRestartAppButton()` | 482 | 函数 |
| `initADBButton()` | 576 | 函数 |
| `showSettingsModal()` | 660 | 函数 |
| `hideSettingsModal()` | 707 | 函数 |
| `hideSettingsModal()` | 818 | 函数 |
| `initComponentVisibility()` | 825 | 函数 |
| `loadComponentConfigs()` | 847 | 函数 |
| `addComponentConfigEventListeners()` | 873 | 函数 |
| `updateComponentVisibility()` | 956 | 函数 |
| `generateAppData()` | 993 | 函数 |
| `initAlphabetNav()` | 1060 | 函数 |
| `initAlphabetNavFromData()` | 1085 | 函数 |
| `renderAppsList()` | 1110 | 函数 |
| `initAppsModal()` | 1185 | 函数 |
| `setAppListCache()` | 1203 | 函数 |
| `filterAppList()` | 1271 | 函数 |
| `updateAlphabetNavVisibility()` | 1303 | 函数 |
| `loadAppList()` | 1321 | 函数 |
| `renderAppsList()` | 1390 | 函数 |
| `initAlphabetNavFromData()` | 1472 | 函数 |
| `showAppsModal()` | 1511 | 函数 |
| `hideAppsModal()` | 1538 | 函数 |
| `resetAutoCloseTimer()` | 1562 | 函数 |
| `clearAutoCloseTimer()` | 1595 | 函数 |
| `showAddToQuickAppsDialog()` | 1613 | 函数 |
| `closeDialogFunc()` | 1660 | 函数 |
| `showRemoveFromQuickAppsDialog()` | 1699 | 函数 |
| `closeDialogFunc()` | 1734 | 函数 |
| `loadQuickApps()` | 1773 | 函数 |
| `loadQuickSwitches()` | 1856 | 函数 |
| `updateQuickSwitchStatus()` | 1913 | 函数 |
| `refreshQuickSwitchesStatus()` | 1927 | 函数 |
| `checkForOverlappingElements()` | 1939 | 函数 |
| `getEventListeners()` | 1992 | 函数 |
| `addTouchSwipeListener()` | 2010 | 函数 |
| `initConfigurableButtons()` | 2019 | 函数 |
| `getButtonDisplayName()` | 2240 | 函数 |
| `decreaseWindLevel()` | 2253 | 函数 |
| `increaseWindLevel()` | 2268 | 函数 |
| `setWindLevel()` | 2282 | 函数 |
| `getCurrentWindLevel()` | 2292 | 函数 |
| `decreaseTemperature()` | 2305 | 函数 |
| `increaseTemperature()` | 2329 | 函数 |
| `setWindLevel()` | 2353 | 函数 |
| `toggleAirConditioning()` | 2361 | 函数 |
| `toggleDefrost()` | 2390 | 函数 |
| `checkWifiStatus()` | 2448 | 函数 |
| `checkBluetoothStatus()` | 2475 | 函数 |
| `updateNetworkAndBluetoothStatus()` | 2502 | 函数 |
| `isElementOverBackground()` | 2509 | 函数 |
| `getElementsAtTouchPoint()` | 2522 | 函数 |
| `initHorizontalScroll()` | 2561 | 函数 |
| `initWallpaperDoubleClick()` | 2694 | 函数 |
| `handleWallpaperLongPress()` | 2770 | 函数 |
| `addTimeDisplayClickEvent()` | 2808 | 函数 |
| `toggleWallpaperCarousel()` | 2828 | 函数 |
| `restoreDefaultWallpaper()` | 2864 | 函数 |
| `updateMusicProgress()` | 2882 | 函数 |
| `updateMusicPlayPauseIcon()` | 2930 | 函数 |
| `toggleProgressLoop()` | 2960 | 函数 |
| `SystemMusicManager` | 2973 | 对象/管理器 |
| `init` | 2977 | 方法 |
| `bindControls` | 2984 | 方法 |
| `startUpdate` | 3019 | 方法 |
| `stopUpdate` | 3027 | 方法 |
| `updateMusicInfo` | 3035 | 方法 |
| `checkPermission` | 3087 | 方法 |
| `initMusicControls()` | 3112 | 函数 |
| `registerTimeUpdateListener()` | 3122 | 函数 |
| `updateState` | 3193 | 方法 |
| `updateGearIndicator` | 3210 | 方法 |
| `updateDoorIndicator` | 3232 | 方法 |
| `updateTurnIndicators` | 3256 | 方法 |
| `updateLockIndicator` | 3288 | 方法 |
| `updateSpeedDisplay` | 3304 | 方法 |
| `updateTirePressure` | 3312 | 方法 |
| `updateCamera360Indicator` | 3366 | 方法 |
| `init` | 3381 | 方法 |
| `addBlinkAnimation` | 3403 | 方法 |
| `togglePanel` | 3483 | 方法 |
| `showPanel` | 3495 | 方法 |
| `hidePanel` | 3508 | 方法 |
| `createPanel` | 3519 | 方法 |
| `toggleSwitch` | 3611 | 方法 |
| `setDriveMode` | 3687 | 方法 |
| `toggleSceneMode` | 3697 | 方法 |
| `refreshSwitchStates` | 3728 | 方法 |
| `AutomationManager` | 3844 | 对象/管理器 |
| `init` | 3951 | 方法 |
| `loadSettings` | 3959 | 方法 |
| `saveSettings` | 3997 | 方法 |
| `isEnabled` | 4017 | 方法 |
| `toggleScenario` | 4024 | 方法 |
| `getScenariosByCategory` | 4043 | 方法 |
| `showPanel` | 4060 | 方法 |
| `hidePanel` | 4073 | 方法 |
| `togglePanel` | 4083 | 方法 |
| `createPanel` | 4095 | 方法 |
| `refreshPanel` | 4169 | 方法 |
| `trigger` | 4192 | 方法 |
| `updateAcTemperature()` | 4228 | 函数 |
| `updateAcState()` | 4236 | 函数 |
| `updateWindLevel()` | 4242 | 函数 |
| `initAcTemperature()` | 4248 | 函数 |
| `VoiceTestManager` | 4276 | 对象/管理器 |
| `init` | 4277 | 方法 |
| `sendVoiceCommand` | 4319 | 方法 |
| `showResult` | 4339 | 方法 |

---

### weather.js
**路径**：`app/src/main/assets/js/weather.js`  
**行数**：约 188 行  
**职责**：天气模块

| 名称 | 行号 | 类型 |
|------|------|------|
| `initWeatherDisplay()` | 10 | 函数 |
| `updateWeatherInfo()` | 30 | 函数 |
| `fetchWeatherData()` | 38 | 函数 |
| `fetchWeatherDataWithXHR()` | 65 | 函数 |
| `updateWeatherDisplay()` | 90 | 函数 |
| `getWeatherDescription()` | 135 | 函数 |
| `getWeatherIcon()` | 171 | 函数 |

---

### music.js
**路径**：`app/src/main/assets/js/music.js`  
**行数**：约 710 行  
**职责**：音乐可视化和播放控制

| 名称 | 行号 | 类型 |
|------|------|------|
| `updateMusicVisualization()` | 46 | 函数 |
| `updateMusicStatus()` | 98 | 函数 |
| `updateMusicUI()` | 124 | 函数 |
| `updateMusicProgressBar()` | 155 | 函数 |
| `updateMusicPlayState()` | 193 | 函数 |
| `formatMusicTime()` | 227 | 函数 |
| `hasNotificationAccess()` | 238 | 函数 |
| `isMusicTimeSupported()` | 246 | 函数 |
| `clearFloats()` | 265 | 函数 |
| `clearCanvas()` | 273 | 函数 |
| `drawFloats()` | 293 | 函数 |
| `drawBars()` | 337 | 函数 |
| `drawEachFrame()` | 364 | 函数 |
| `visualize()` | 386 | 函数 |
| `initCustomAudioPlayer()` | 417 | 函数 |
| `updateProgressBar()` | 435 | 函数 |
| `updateCurrentSongName()` | 445 | 函数 |
| `updatePlayingState()` | 460 | 函数 |
| `toggleProgressLoop()` | 688 | 函数 |
| `updateMusicName()` | 701 | 函数 |

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

**💡 小贴士**：代码变动后记得重新运行脚本更新索引！

```bash
python3 tools/generate_code_index.py
```