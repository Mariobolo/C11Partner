# C11Partner 代码索引文档
> 📌 **快速定位代码的神器** - 修改代码前先看本文档，找到对应函数行号再精准读取，避免读取大文件占用上下文
>
> 自动生成时间：2026-06-20 21:59:50
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
| Java后端 | 8 | 488 |
| JS前端 | 3 | 145 |
| **总计** | **11** | **633** |

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
**行数**：约 4757 行  
**职责**：JS桥接层，提供50+个JS接口给前端调用

#### 保存/设置

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `setMediaSessionService()` | 140 | public |
| `setDefaultDesktop()` | 3214 | public |

#### 应用管理

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getAppList()` | 154 | public |
| `getAppListAsync()` | 293 | public |
| `launchApp()` | 1084 | public |
| `saveConfigApp()` | 1519 | public |
| `getConfigApp()` | 1539 | public |
| `saveComponentConfig()` | 1566 | public |
| `isComponentEnabled()` | 1592 | public |
| `getAllComponentConfigs()` | 1609 | public |
| `getQuickAppList()` | 2207 | public |
| `addQuickApp()` | 2235 | public |
| `removeQuickApp()` | 2261 | public |
| `isQuickApp()` | 2284 | public |
| `saveSystemLauncherSetting()` | 2704 | public |
| `saveSystemLauncherSettingAsync()` | 2787 | public |

#### 匿名内部类

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `compare()` | 196 | public |
| `run()` | 296 | public |
| `run()` | 303 | public |
| `run()` | 327 | public |
| `run()` | 338 | public |
| `run()` | 352 | public |
| `run()` | 398 | public |
| `run()` | 409 | public |
| `run()` | 423 | public |
| `run()` | 1069 | public |
| `run()` | 1687 | public |
| `run()` | 1693 | public |
| `run()` | 1707 | public |
| `run()` | 1754 | public |
| `run()` | 1770 | public |
| `run()` | 1785 | public |
| `run()` | 1834 | public |
| `run()` | 1852 | public |
| `run()` | 1868 | public |
| `run()` | 1891 | public |
| `run()` | 1958 | public |
| `run()` | 1973 | public |
| `run()` | 2086 | public |
| `run()` | 2170 | public |
| `run()` | 2185 | public |
| `run()` | 2790 | public |
| `run()` | 2796 | public |
| `run()` | 2810 | public |
| `run()` | 2834 | public |
| `run()` | 2840 | public |
| `run()` | 2854 | public |
| `run()` | 2878 | public |
| `run()` | 2884 | public |
| `run()` | 2898 | public |
| `run()` | 2922 | public |
| `run()` | 2928 | public |
| `run()` | 2942 | public |
| `run()` | 2979 | public |
| `run()` | 3007 | public |
| `run()` | 3028 | public |
| `run()` | 3049 | public |
| `run()` | 3098 | public |
| `run()` | 3457 | public |
| `run()` | 3553 | public |
| `isLogServiceRunning()` | 3703 | public |
| `run()` | 4319 | public |
| `run()` | 4384 | public |
| `run()` | 4420 | public |
| `run()` | 4450 | public |
| `run()` | 4725 | public |

#### 壁纸相关

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `saveWallpaperCarouselSettingAsync()` | 324 | public |
| `saveWallpaperCarouselSetting()` | 373 | public |
| `saveWallpaperSwitchIntervalAsync()` | 395 | public |
| `saveWallpaperSwitchInterval()` | 444 | public |
| `sendWallpaperSettingsChangedBroadcast()` | 463 | public |
| `getRandomWallpaper()` | 479 | public |
| `getRandomLocalWallpaper()` | 554 | private |
| `getRandomOnlineWallpaper()` | 717 | private |
| `getRandomOnlineWallpaperBase64()` | 774 | private |
| `getRandomWallpaperUrl()` | 826 | private |
| `notifyWallpaperUpdate()` | 1066 | public |
| `pauseWallpaperCarousel()` | 1102 | public |
| `resumeWallpaperCarousel()` | 1117 | public |
| `deleteCurrentWallpaper()` | 1133 | public |
| `updateWallpaperCategories()` | 1631 | public |
| `getWallpaperSettings()` | 1805 | public |
| `getWallpaperSettingsAsync()` | 1831 | public |
| `getRandomWallpaperAsync()` | 1888 | public |
| `getRandomWallpaperBase64()` | 1993 | public |
| `getRandomWallpaperBase64Async()` | 2083 | public |
| `getWallpaperManager()` | 4598 | private |
| `getWallpaperSettingsV2()` | 4606 | public |
| `setWallpaperType()` | 4620 | public |
| `setWallpaperPath()` | 4633 | public |
| `getCurrentWallpaperUrl()` | 4646 | public |
| `getNextWallpaperUrl()` | 4659 | public |
| `setWallpaperCarouselEnabled()` | 4672 | public |
| `setWallpaperCarouselInterval()` | 4690 | public |
| `setWallpaperFillMode()` | 4708 | public |
| `refreshBingWallpaper()` | 4721 | public |
| `pickLocalWallpaperFile()` | 4741 | public |
| `pickLocalWallpaperFolder()` | 4751 | public |

#### 初始化

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getRandomWallpaperFromFstartExcept00()` | 608 | private |
| `getRandomWallpaperFromFstart00()` | 671 | private |
| `startMusicVisualizer()` | 2299 | public |
| `initializeAcStatus()` | 3087 | public |
| `restartApp()` | 3117 | public |
| `startCamera360()` | 3687 | public |

#### 图片处理

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `encodeImageToBase64()` | 841 | private |
| `drawableToBase64()` | 856 | private |
| `drawableToBitmap()` | 879 | private |
| `scaleBitmap()` | 922 | private |
| `bitmapToBase64()` | 942 | private |
| `getFirstLetter()` | 964 | private |
| `isChineseChar()` | 991 | private |
| `getChineseFirstLetter()` | 1006 | private |
| `cacheAppIconBase64()` | 1044 | private |
| `getCachedAppIconBase64()` | 1055 | private |

#### 其他

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `clearAppIconCache()` | 1026 | private |
| `showToastOnUiThread()` | 1289 | private |
| `openRecentTasks()` | 1406 | public |
| `openRecents()` | 1432 | public |

#### ADB相关

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `triggerUsbDebugAuthorization()` | 1149 | public |
| `triggerWirelessAdbAuthorization()` | 1188 | public |
| `executeWirelessAdbAuthorization()` | 1299 | private |
| `executeAdbPermissionGrant()` | 1360 | public |
| `setDefaultDesktopViaAdb()` | 1456 | public |
| `autoConnectWirelessAdb()` | 3246 | public |
| `executeAdbAuthorization()` | 3311 | private |
| `isUsbDebuggingEnabled()` | 3342 | private |
| `executeAdbCommands()` | 3359 | private |
| `executeAdbCommandsAfterConnection()` | 3412 | private |

#### 状态获取

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getDeviceIpAddress()` | 1235 | private |
| `getEnabledCategories()` | 1727 | public |
| `getEnabledCategoriesAsync()` | 1751 | public |
| `isNotificationListenerEnabled()` | 2500 | public |
| `isWifiConnected()` | 2534 | public |
| `isBluetoothConnected()` | 2564 | public |
| `getStaticIntValue()` | 3632 | private |
| `getCarState()` | 3650 | public |
| `isCameraOverspeedLimitEnabled()` | 4008 | public |
| `isVideoWhileDrivingEnabled()` | 4035 | public |
| `isVehicleLocked()` | 4494 | public |
| `isScreenOn()` | 4508 | public |

#### 时间日期

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `updateCategoryEnabled()` | 1668 | public |
| `updateCategoryEnabledAsync()` | 1684 | public |
| `getLunarCalendar()` | 2754 | public |
| `updateTimeDisplay()` | 2965 | public |
| `updateTimeDisplayAsync()` | 3004 | public |
| `getWeekDay()` | 3069 | private |

#### 音乐相关

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `stopMusicVisualizer()` | 2320 | public |
| `isMusicPlaying()` | 2340 | public |
| `getCurrentMusicName()` | 2351 | public |
| `getMusicProgressInfo()` | 2362 | public |
| `getSystemMusicInfo()` | 2391 | public |
| `playPauseMusic()` | 2425 | public |
| `nextMusic()` | 2442 | public |
| `prevMusic()` | 2458 | public |
| `sendMediaButton()` | 2473 | private |
| `playPause()` | 2652 | public |
| `playNext()` | 2670 | public |
| `playPrevious()` | 2682 | public |

#### 系统设置

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `openNotificationListenerSettings()` | 2520 | public |
| `saveBootGreetingSetting()` | 2721 | public |
| `saveRandomModeSetting()` | 2738 | public |
| `saveSpecifiedModeSetting()` | 2770 | public |
| `saveBootGreetingSettingAsync()` | 2831 | public |
| `saveRandomModeSettingAsync()` | 2875 | public |
| `saveSpecifiedModeSettingAsync()` | 2919 | public |

#### 导航相关

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `isAmapInstalled()` | 3129 | private |
| `navigateToHome()` | 3143 | public |
| `navigateToCompany()` | 3179 | public |

#### 车控功能

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `toggleAirConditioning()` | 3445 | public |
| `openAirConditioningPage()` | 3475 | private |
| `getAcInfo()` | 3506 | public |
| `adjustTemperature()` | 3516 | public |
| `adjustWindLevel()` | 3541 | public |
| `toggleDefrost()` | 3573 | public |
| `sendVoiceCommand()` | 3593 | public |
| `setLowBeamLight()` | 3772 | public |
| `setRearFogLight()` | 3785 | public |
| `setPositionLight()` | 3798 | public |
| `setPedestrianAlert()` | 3811 | public |
| `setDriveMode()` | 3827 | public |
| `setGuardMode()` | 3842 | public |
| `setRestMode()` | 3855 | public |
| `setCampingMode()` | 3868 | public |
| `setPowerSaveMode()` | 3881 | public |
| `setSentinelMode()` | 3894 | public |
| `setMaxCooling()` | 3909 | public |
| `setNightMode()` | 3925 | public |
| `setWifiEnabled()` | 3938 | public |
| `setBluetoothEnabled()` | 3951 | public |
| `sendPrevTrack()` | 3966 | public |
| `sendNextTrack()` | 3979 | public |
| `setCameraOverspeedLimit()` | 3995 | public |
| `setVideoWhileDriving()` | 4022 | public |
| `setCallVolume()` | 4050 | public |
| `setNaviVolume()` | 4076 | public |
| `setMusicVolume()` | 4102 | public |
| `setDriverTemp()` | 4131 | public |
| `setPassengerTemp()` | 4157 | public |
| `setAmbientLightEnabled()` | 4185 | public |
| `setAmbientLightColor()` | 4212 | public |
| `setSecondaryScreenEnabled()` | 4254 | public |

#### 权限相关

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `hasNotificationAccess()` | 3612 | public |
| `hasReadLogsPermission()` | 3713 | public |
| `hasWriteSecureSettingsPermission()` | 3729 | public |
| `hasDumpPermission()` | 3745 | public |

#### 音量控制

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getCallVolume()` | 4063 | public |
| `getNaviVolume()` | 4089 | public |
| `getMusicVolume()` | 4115 | public |

#### 空调控制

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getDriverTemp()` | 4144 | public |
| `getPassengerTemp()` | 4170 | public |

#### 灯光控制

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `isAmbientLightEnabled()` | 4198 | public |
| `getAmbientLightColor()` | 4225 | public |

#### 副屏相关

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `isSecondaryScreenEnabled()` | 4267 | public |
| `hasSecondaryDisplay()` | 4280 | public |
| `getSecondaryDisplayInfo()` | 4293 | public |
| `showCarStatusPresentation()` | 4306 | public |
| `hidePresentation()` | 4376 | public |
| `isPresentationShowing()` | 4404 | public |
| `updatePresentationCarState()` | 4416 | public |
| `updatePresentationTime()` | 4446 | public |

#### 语音控制

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `setSpeechEnabled()` | 4465 | public |
| `isSpeechEnabled()` | 4478 | public |

#### 自动化

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getAutomationSettings()` | 4539 | public |
| `setAutomationSettings()` | 4553 | public |
| `isAutomationScenarioEnabled()` | 4568 | public |
| `setAutomationScenarioEnabled()` | 4583 | public |

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
| `onDestroy()` | 90 | public |

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