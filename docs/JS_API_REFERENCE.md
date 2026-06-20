# WebViewBridge JS 接口文档

> 📋 本文档由工具自动生成，请勿手动修改
> 
> 生成时间：2026-06-20 23:46:45
> 
> 接口总数：4 个

---

## 📑 目录

- [360全景](#360全景)
- [灯光控制](#灯光控制)
- [驾驶/场景模式](#驾驶场景模式)
- [空调控制](#空调控制)
- [音量控制](#音量控制)
- [副屏相关](#副屏相关)
- [语音控制](#语音控制)
- [自动化](#自动化)
- [音乐相关](#音乐相关)
- [应用管理](#应用管理)
- [壁纸相关](#壁纸相关)
- [ADB相关](#adb相关)
- [权限相关](#权限相关)
- [导航相关](#导航相关)
- [时间日期](#时间日期)
- [系统设置](#系统设置)
- [状态获取](#状态获取)
- [保存/设置](#保存设置)
- [其他](#其他)

---

## 360全景

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `startCamera360()` | - | `boolean` | - |

## 灯光控制

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `getAmbientLightColor()` | - | `int` | - |
| `isAmbientLightEnabled()` | - | `boolean` | - |
| `setAmbientLightColor()` | - | `boolean` | `color`: int |
| `setAmbientLightEnabled()` | - | `boolean` | `enabled`: boolean |
| `setLowBeamLight()` | - | `boolean` | `on`: boolean |
| `setPedestrianAlert()` | - | `boolean` | `on`: boolean |
| `setPositionLight()` | - | `boolean` | `on`: boolean |
| `setRearFogLight()` | - | `boolean` | `on`: boolean |

## 驾驶/场景模式

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `setCampingMode()` | - | `boolean` | `on`: boolean |
| `setDriveMode()` | - | `boolean` | `mode`: int |
| `setGuardMode()` | - | `boolean` | `on`: boolean |
| `setPowerSaveMode()` | - | `boolean` | `on`: boolean |
| `setRestMode()` | - | `boolean` | `on`: boolean |
| `setSentinelMode()` | - | `boolean` | `on`: boolean |

## 空调控制

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `adjustWindLevel()` | - | `void` | `delta`: int |
| `getAcInfo()` | - | `String` | - |
| `getDriverTemp()` | - | `int` | - |
| `getPassengerTemp()` | - | `int` | - |
| `setDriverTemp()` | - | `boolean` | `temp`: int |
| `setMaxCooling()` | - | `boolean` | `on`: boolean |
| `setPassengerTemp()` | - | `boolean` | `temp`: int |
| `toggleAirConditioning()` | - | `void` | - |
| `toggleDefrost()` | - | `void` | - |

## 音量控制

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `getCallVolume()` | - | `int` | - |
| `getMusicVolume()` | - | `int` | - |
| `getNaviVolume()` | - | `int` | - |
| `setCallVolume()` | - | `boolean` | `volume`: int |
| `setMusicVolume()` | - | `boolean` | `volume`: int |
| `setNaviVolume()` | - | `boolean` | `volume`: int |

## 副屏相关

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `getSecondaryDisplayInfo()` | - | `String` | - |
| `hasSecondaryDisplay()` | - | `boolean` | - |
| `hidePresentation()` | - | `boolean` | - |
| `isPresentationShowing()` | - | `boolean` | - |
| `isSecondaryScreenEnabled()` | - | `boolean` | - |
| `setSecondaryScreenEnabled()` | - | `boolean` | `enabled`: boolean |
| `showCarStatusPresentation()` | - | `boolean` | - |

## 语音控制

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `isSpeechEnabled()` | - | `boolean` | - |
| `sendVoiceCommand()` | - | `boolean` | `command`: String |
| `setSpeechEnabled()` | - | `boolean` | `enabled`: boolean |

## 自动化

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `getAutomationSettings()` | - | `String` | - |
| `isAutomationScenarioEnabled()` | - | `boolean` | `scenarioId`: String |
| `setAutomationScenarioEnabled()` | - | `void` | `scenarioId`: String, `enabled`: boolean |
| `setAutomationSettings()` | - | `void` | `jsonStr`: String |

## 音乐相关

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `getCurrentMusicName()` | - | `String` | - |
| `getMusicProgressInfo()` | - | `String` | - |
| `getSystemMusicInfo()` | - | `String` | - |
| `isMusicPlaying()` | - | `boolean` | - |
| `isNotificationListenerEnabled()` | - | `boolean` | - |
| `nextMusic()` | - | `void` | - |
| `openNotificationListenerSettings()` | - | `void` | - |
| `playPause()` | - | `void` | - |
| `playPauseMusic()` | - | `void` | - |
| `prevMusic()` | - | `void` | - |
| `startMusicVisualizer()` | - | `void` | - |
| `stopMusicVisualizer()` | - | `void` | - |

## 应用管理

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `addQuickApp()` | - | `void` | `name`: String, `packageName`: String, `iconBase64`: String |
| `getAllComponentConfigs()` | - | `String` | - |
| `getAppList()` | - | `String` | - |
| `getAppListAsync()` | - | `void` | `callbackId`: final String |
| `getConfigApp()` | - | `String` | `buttonId`: String |
| `getQuickAppList()` | - | `String` | - |
| `isComponentEnabled()` | - | `boolean` | `componentName`: String |
| `isQuickApp()` | - | `boolean` | `packageName`: String |
| `launchApp()` | - | `void` | `packageName`: String |
| `removeQuickApp()` | - | `void` | `packageName`: String |
| `restartApp()` | - | `void` | - |
| `saveComponentConfig()` | - | `boolean` | `componentName`: String, `isEnabled`: boolean |
| `saveConfigApp()` | - | `void` | `buttonId`: String, `appName`: String, `packageName`: String, `appIcon`: String |
| `saveSystemLauncherSetting()` | - | `boolean` | `enabled`: boolean |
| `saveSystemLauncherSettingAsync()` | - | `void` | `enabled`: final boolean, `callbackId`: final String |

## 壁纸相关

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `deleteCurrentWallpaper()` | - | `boolean` | - |
| `getCurrentWallpaperUrl()` | - | `String` | - |
| `getNextWallpaperUrl()` | - | `String` | - |
| `getRandomWallpaper()` | - | `String` | - |
| `getRandomWallpaperAsync()` | - | `void` | `callbackId`: final String |
| `getRandomWallpaperBase64()` | - | `String` | - |
| `getRandomWallpaperBase64Async()` | - | `void` | `callbackId`: final String |
| `getWallpaperSettings()` | - | `String` | - |
| `getWallpaperSettingsAsync()` | - | `void` | `callbackId`: final String |
| `getWallpaperSettingsV2()` | - | `String` | - |
| `pauseWallpaperCarousel()` | - | `void` | - |
| `pickLocalWallpaperFile()` | - | `void` | - |
| `pickLocalWallpaperFolder()` | - | `void` | - |
| `refreshBingWallpaper()` | - | `void` | - |
| `resumeWallpaperCarousel()` | - | `void` | - |
| `saveWallpaperCarouselSetting()` | - | `boolean` | `enabled`: boolean |
| `saveWallpaperCarouselSettingAsync()` | - | `void` | `enabled`: final boolean, `callbackId`: final String |
| `saveWallpaperSwitchInterval()` | - | `boolean` | `interval`: int |
| `saveWallpaperSwitchIntervalAsync()` | - | `void` | `interval`: final int, `callbackId`: final String |
| `sendWallpaperSettingsChangedBroadcast()` | - | `void` | - |
| `setWallpaperCarouselEnabled()` | - | `void` | `enabled`: boolean |
| `setWallpaperCarouselInterval()` | - | `void` | `intervalMs`: int |
| `setWallpaperFillMode()` | - | `void` | `mode`: int |
| `setWallpaperPath()` | - | `void` | `path`: String |
| `setWallpaperType()` | - | `void` | `type`: int |
| `updateWallpaperCategories()` | - | `void` | - |

## ADB相关

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `executeAdbPermissionGrant()` | - | `void` | - |
| `setDefaultDesktopViaAdb()` | - | `void` | - |
| `triggerUsbDebugAuthorization()` | - | `void` | - |
| `triggerWirelessAdbAuthorization()` | - | `void` | - |

## 权限相关

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `hasDumpPermission()` | - | `boolean` | - |
| `hasNotificationAccess()` | - | `boolean` | - |
| `hasReadLogsPermission()` | - | `boolean` | - |
| `hasWriteSecureSettingsPermission()` | - | `boolean` | - |

## 导航相关

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `navigateToCompany()` | - | `void` | - |
| `navigateToHome()` | - | `void` | - |

## 时间日期

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `getLunarCalendar()` | - | `String` | - |
| `updateCategoryEnabled()` | - | `void` | `categoryId`: String, `enabled`: boolean |
| `updateCategoryEnabledAsync()` | - | `void` | `categoryId`: final String, `enabled`: final boolean, `callbackId`: final String |
| `updateTimeDisplay()` | - | `void` | `time`: String, `date`: String, `lunarDate`: String |
| `updateTimeDisplayAsync()` | - | `void` | `callbackId`: final String |

## 系统设置

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `isBluetoothConnected()` | - | `boolean` | - |
| `isWifiConnected()` | - | `boolean` | - |
| `saveBootGreetingSetting()` | - | `boolean` | `enabled`: boolean |
| `saveBootGreetingSettingAsync()` | - | `void` | `enabled`: final boolean, `callbackId`: final String |
| `saveRandomModeSetting()` | - | `boolean` | `enabled`: boolean |
| `saveRandomModeSettingAsync()` | - | `void` | `enabled`: final boolean, `callbackId`: final String |
| `saveSpecifiedModeSetting()` | - | `boolean` | `enabled`: boolean |
| `saveSpecifiedModeSettingAsync()` | - | `void` | `enabled`: final boolean, `callbackId`: final String |
| `setBluetoothEnabled()` | - | `boolean` | `enabled`: boolean |
| `setNightMode()` | - | `boolean` | `night`: boolean |
| `setWifiEnabled()` | - | `boolean` | `enabled`: boolean |

## 状态获取

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `getCarState()` | - | `String` | - |
| `getEnabledCategories()` | - | `String` | - |
| `getEnabledCategoriesAsync()` | - | `void` | `callbackId`: final String |
| `isCameraOverspeedLimitEnabled()` | - | `boolean` | - |
| `isLogServiceRunning()` | - | `boolean` | - |
| `isScreenOn()` | - | `boolean` | - |
| `isVehicleLocked()` | - | `boolean` | - |
| `isVideoWhileDrivingEnabled()` | - | `boolean` | - |

## 保存/设置

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `setCameraOverspeedLimit()` | - | `boolean` | `enabled`: boolean |
| `setDefaultDesktop()` | - | `void` | - |
| `setVideoWhileDriving()` | - | `boolean` | `enabled`: boolean |

## 其他

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `adjustTemperature()` | - | `void` | `delta`: int |
| `initializeAcStatus()` | - | `void` | - |
| `openRecentTasks()` | - | `void` | - |
| `openRecents()` | - | `void` | - |
| `playNext()` | - | `void` | - |
| `playPrevious()` | - | `void` | - |
| `sendNextTrack()` | - | `boolean` | - |
| `sendPrevTrack()` | - | `boolean` | - |

---

## 📝 详细说明

### 360全景

#### `startCamera360()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 3682 行

### 灯光控制

#### `getAmbientLightColor()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 4049 行

#### `isAmbientLightEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 4032 行

#### `setAmbientLightColor()`

- **返回值**：`boolean`
- **参数**：
  - `color`: `int`
- **行号**：第 4041 行

#### `setAmbientLightEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 4024 行

#### `setLowBeamLight()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 3761 行

#### `setPedestrianAlert()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 3785 行

#### `setPositionLight()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 3777 行

#### `setRearFogLight()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 3769 行

### 驾驶/场景模式

#### `setCampingMode()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 3822 行

#### `setDriveMode()`

- **返回值**：`boolean`
- **参数**：
  - `mode`: `int`
- **行号**：第 3796 行

#### `setGuardMode()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 3806 行

#### `setPowerSaveMode()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 3830 行

#### `setRestMode()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 3814 行

#### `setSentinelMode()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 3838 行

### 空调控制

#### `adjustWindLevel()`

- **返回值**：`void`
- **参数**：
  - `delta`: `int`
- **行号**：第 3545 行

#### `getAcInfo()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 3510 行

#### `getDriverTemp()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 3998 行

#### `getPassengerTemp()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 4014 行

#### `setDriverTemp()`

- **返回值**：`boolean`
- **参数**：
  - `temp`: `int`
- **行号**：第 3990 行

#### `setMaxCooling()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 3848 行

#### `setPassengerTemp()`

- **返回值**：`boolean`
- **参数**：
  - `temp`: `int`
- **行号**：第 4006 行

#### `toggleAirConditioning()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 3449 行

#### `toggleDefrost()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 3577 行

### 音量控制

#### `getCallVolume()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 3947 行

#### `getMusicVolume()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 3979 行

#### `getNaviVolume()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 3963 行

#### `setCallVolume()`

- **返回值**：`boolean`
- **参数**：
  - `volume`: `int`
- **行号**：第 3939 行

#### `setMusicVolume()`

- **返回值**：`boolean`
- **参数**：
  - `volume`: `int`
- **行号**：第 3971 行

#### `setNaviVolume()`

- **返回值**：`boolean`
- **参数**：
  - `volume`: `int`
- **行号**：第 3955 行

### 副屏相关

#### `getSecondaryDisplayInfo()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 4102 行

#### `hasSecondaryDisplay()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 4089 行

#### `hidePresentation()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 4185 行

#### `isPresentationShowing()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 4213 行

#### `isSecondaryScreenEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 4081 行

#### `setSecondaryScreenEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 4073 行

#### `showCarStatusPresentation()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 4115 行

### 语音控制

#### `isSpeechEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 4282 行

#### `sendVoiceCommand()`

- **返回值**：`boolean`
- **参数**：
  - `command`: `String`
- **行号**：第 3597 行

#### `setSpeechEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 4274 行

### 自动化

#### `getAutomationSettings()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 4328 行

#### `isAutomationScenarioEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `scenarioId`: `String`
- **行号**：第 4357 行

#### `setAutomationScenarioEnabled()`

- **返回值**：`void`
- **参数**：
  - `scenarioId`: `String`
  - `enabled`: `boolean`
- **行号**：第 4372 行

#### `setAutomationSettings()`

- **返回值**：`void`
- **参数**：
  - `jsonStr`: `String`
- **行号**：第 4342 行

### 音乐相关

#### `getCurrentMusicName()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 2355 行

#### `getMusicProgressInfo()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 2366 行

#### `getSystemMusicInfo()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 2395 行

#### `isMusicPlaying()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 2344 行

#### `isNotificationListenerEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 2504 行

#### `nextMusic()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 2446 行

#### `openNotificationListenerSettings()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 2524 行

#### `playPause()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 2656 行

#### `playPauseMusic()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 2429 行

#### `prevMusic()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 2462 行

#### `startMusicVisualizer()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 2303 行

#### `stopMusicVisualizer()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 2324 行

### 应用管理

#### `addQuickApp()`

- **返回值**：`void`
- **参数**：
  - `name`: `String`
  - `packageName`: `String`
  - `iconBase64`: `String`
- **行号**：第 2239 行

#### `getAllComponentConfigs()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 1613 行

#### `getAppList()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 158 行

#### `getAppListAsync()`

- **返回值**：`void`
- **参数**：
  - `callbackId`: `final String`
- **行号**：第 297 行

#### `getConfigApp()`

- **返回值**：`String`
- **参数**：
  - `buttonId`: `String`
- **行号**：第 1543 行

#### `getQuickAppList()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 2211 行

#### `isComponentEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `componentName`: `String`
- **行号**：第 1596 行

#### `isQuickApp()`

- **返回值**：`boolean`
- **参数**：
  - `packageName`: `String`
- **行号**：第 2288 行

#### `launchApp()`

- **返回值**：`void`
- **参数**：
  - `packageName`: `String`
- **行号**：第 1088 行

#### `removeQuickApp()`

- **返回值**：`void`
- **参数**：
  - `packageName`: `String`
- **行号**：第 2265 行

#### `restartApp()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 3121 行

#### `saveComponentConfig()`

- **返回值**：`boolean`
- **参数**：
  - `componentName`: `String`
  - `isEnabled`: `boolean`
- **行号**：第 1570 行

#### `saveConfigApp()`

- **返回值**：`void`
- **参数**：
  - `buttonId`: `String`
  - `appName`: `String`
  - `packageName`: `String`
  - `appIcon`: `String`
- **行号**：第 1523 行

#### `saveSystemLauncherSetting()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 2708 行

#### `saveSystemLauncherSettingAsync()`

- **返回值**：`void`
- **参数**：
  - `enabled`: `final boolean`
  - `callbackId`: `final String`
- **行号**：第 2791 行

### 壁纸相关

#### `deleteCurrentWallpaper()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 1137 行

#### `getCurrentWallpaperUrl()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 4435 行

#### `getNextWallpaperUrl()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 4448 行

#### `getRandomWallpaper()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 483 行

#### `getRandomWallpaperAsync()`

- **返回值**：`void`
- **参数**：
  - `callbackId`: `final String`
- **行号**：第 1892 行

#### `getRandomWallpaperBase64()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 1997 行

#### `getRandomWallpaperBase64Async()`

- **返回值**：`void`
- **参数**：
  - `callbackId`: `final String`
- **行号**：第 2087 行

#### `getWallpaperSettings()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 1809 行

#### `getWallpaperSettingsAsync()`

- **返回值**：`void`
- **参数**：
  - `callbackId`: `final String`
- **行号**：第 1835 行

#### `getWallpaperSettingsV2()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 4395 行

#### `pauseWallpaperCarousel()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 1106 行

#### `pickLocalWallpaperFile()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 4530 行

#### `pickLocalWallpaperFolder()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 4540 行

#### `refreshBingWallpaper()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 4510 行

#### `resumeWallpaperCarousel()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 1121 行

#### `saveWallpaperCarouselSetting()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 377 行

#### `saveWallpaperCarouselSettingAsync()`

- **返回值**：`void`
- **参数**：
  - `enabled`: `final boolean`
  - `callbackId`: `final String`
- **行号**：第 328 行

#### `saveWallpaperSwitchInterval()`

- **返回值**：`boolean`
- **参数**：
  - `interval`: `int`
- **行号**：第 448 行

#### `saveWallpaperSwitchIntervalAsync()`

- **返回值**：`void`
- **参数**：
  - `interval`: `final int`
  - `callbackId`: `final String`
- **行号**：第 399 行

#### `sendWallpaperSettingsChangedBroadcast()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 467 行

#### `setWallpaperCarouselEnabled()`

- **返回值**：`void`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 4461 行

#### `setWallpaperCarouselInterval()`

- **返回值**：`void`
- **参数**：
  - `intervalMs`: `int`
- **行号**：第 4479 行

#### `setWallpaperFillMode()`

- **返回值**：`void`
- **参数**：
  - `mode`: `int`
- **行号**：第 4497 行

#### `setWallpaperPath()`

- **返回值**：`void`
- **参数**：
  - `path`: `String`
- **行号**：第 4422 行

#### `setWallpaperType()`

- **返回值**：`void`
- **参数**：
  - `type`: `int`
- **行号**：第 4409 行

#### `updateWallpaperCategories()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 1635 行

### ADB相关

#### `executeAdbPermissionGrant()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 1364 行

#### `setDefaultDesktopViaAdb()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 1460 行

#### `triggerUsbDebugAuthorization()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 1153 行

#### `triggerWirelessAdbAuthorization()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 1192 行

### 权限相关

#### `hasDumpPermission()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 3734 行

#### `hasNotificationAccess()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 3607 行

#### `hasReadLogsPermission()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 3702 行

#### `hasWriteSecureSettingsPermission()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 3718 行

### 导航相关

#### `navigateToCompany()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 3183 行

#### `navigateToHome()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 3147 行

### 时间日期

#### `getLunarCalendar()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 2758 行

#### `updateCategoryEnabled()`

- **返回值**：`void`
- **参数**：
  - `categoryId`: `String`
  - `enabled`: `boolean`
- **行号**：第 1672 行

#### `updateCategoryEnabledAsync()`

- **返回值**：`void`
- **参数**：
  - `categoryId`: `final String`
  - `enabled`: `final boolean`
  - `callbackId`: `final String`
- **行号**：第 1688 行

#### `updateTimeDisplay()`

- **返回值**：`void`
- **参数**：
  - `time`: `String`
  - `date`: `String`
  - `lunarDate`: `String`
- **行号**：第 2969 行

#### `updateTimeDisplayAsync()`

- **返回值**：`void`
- **参数**：
  - `callbackId`: `final String`
- **行号**：第 3008 行

### 系统设置

#### `isBluetoothConnected()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 2568 行

#### `isWifiConnected()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 2538 行

#### `saveBootGreetingSetting()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 2725 行

#### `saveBootGreetingSettingAsync()`

- **返回值**：`void`
- **参数**：
  - `enabled`: `final boolean`
  - `callbackId`: `final String`
- **行号**：第 2835 行

#### `saveRandomModeSetting()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 2742 行

#### `saveRandomModeSettingAsync()`

- **返回值**：`void`
- **参数**：
  - `enabled`: `final boolean`
  - `callbackId`: `final String`
- **行号**：第 2879 行

#### `saveSpecifiedModeSetting()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 2774 行

#### `saveSpecifiedModeSettingAsync()`

- **返回值**：`void`
- **参数**：
  - `enabled`: `final boolean`
  - `callbackId`: `final String`
- **行号**：第 2923 行

#### `setBluetoothEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 3875 行

#### `setNightMode()`

- **返回值**：`boolean`
- **参数**：
  - `night`: `boolean`
- **行号**：第 3859 行

#### `setWifiEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 3867 行

### 状态获取

#### `getCarState()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 3645 行

#### `getEnabledCategories()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 1731 行

#### `getEnabledCategoriesAsync()`

- **返回值**：`void`
- **参数**：
  - `callbackId`: `final String`
- **行号**：第 1755 行

#### `isCameraOverspeedLimitEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 3912 行

#### `isLogServiceRunning()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 3692 行

#### `isScreenOn()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 4302 行

#### `isVehicleLocked()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 4293 行

#### `isVideoWhileDrivingEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 3929 行

### 保存/设置

#### `setCameraOverspeedLimit()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 3904 行

#### `setDefaultDesktop()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 3218 行

#### `setVideoWhileDriving()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 3921 行

### 其他

#### `adjustTemperature()`

- **返回值**：`void`
- **参数**：
  - `delta`: `int`
- **行号**：第 3520 行

#### `initializeAcStatus()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 3091 行

#### `openRecentTasks()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 1410 行

#### `openRecents()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 1436 行

#### `playNext()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 2674 行

#### `playPrevious()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 2686 行

#### `sendNextTrack()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 3893 行

#### `sendPrevTrack()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 3885 行

---

## 🔄 重新生成

```bash
python3 tools/generate_api_docs.py
```
