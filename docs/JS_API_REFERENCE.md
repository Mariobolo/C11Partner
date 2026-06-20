# WebViewBridge JS 接口文档

> 📋 本文档由工具自动生成，请勿手动修改
> 
> 生成时间：2026-06-20 21:59:50
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
- **行号**：第 3687 行

### 灯光控制

#### `getAmbientLightColor()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 4225 行

#### `isAmbientLightEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 4198 行

#### `setAmbientLightColor()`

- **返回值**：`boolean`
- **参数**：
  - `color`: `int`
- **行号**：第 4212 行

#### `setAmbientLightEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 4185 行

#### `setLowBeamLight()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 3772 行

#### `setPedestrianAlert()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 3811 行

#### `setPositionLight()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 3798 行

#### `setRearFogLight()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 3785 行

### 驾驶/场景模式

#### `setCampingMode()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 3868 行

#### `setDriveMode()`

- **返回值**：`boolean`
- **参数**：
  - `mode`: `int`
- **行号**：第 3827 行

#### `setGuardMode()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 3842 行

#### `setPowerSaveMode()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 3881 行

#### `setRestMode()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 3855 行

#### `setSentinelMode()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 3894 行

### 空调控制

#### `adjustWindLevel()`

- **返回值**：`void`
- **参数**：
  - `delta`: `int`
- **行号**：第 3541 行

#### `getAcInfo()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 3506 行

#### `getDriverTemp()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 4144 行

#### `getPassengerTemp()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 4170 行

#### `setDriverTemp()`

- **返回值**：`boolean`
- **参数**：
  - `temp`: `int`
- **行号**：第 4131 行

#### `setMaxCooling()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 3909 行

#### `setPassengerTemp()`

- **返回值**：`boolean`
- **参数**：
  - `temp`: `int`
- **行号**：第 4157 行

#### `toggleAirConditioning()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 3445 行

#### `toggleDefrost()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 3573 行

### 音量控制

#### `getCallVolume()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 4063 行

#### `getMusicVolume()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 4115 行

#### `getNaviVolume()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 4089 行

#### `setCallVolume()`

- **返回值**：`boolean`
- **参数**：
  - `volume`: `int`
- **行号**：第 4050 行

#### `setMusicVolume()`

- **返回值**：`boolean`
- **参数**：
  - `volume`: `int`
- **行号**：第 4102 行

#### `setNaviVolume()`

- **返回值**：`boolean`
- **参数**：
  - `volume`: `int`
- **行号**：第 4076 行

### 副屏相关

#### `getSecondaryDisplayInfo()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 4293 行

#### `hasSecondaryDisplay()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 4280 行

#### `hidePresentation()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 4376 行

#### `isPresentationShowing()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 4404 行

#### `isSecondaryScreenEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 4267 行

#### `setSecondaryScreenEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 4254 行

#### `showCarStatusPresentation()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 4306 行

### 语音控制

#### `isSpeechEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 4478 行

#### `sendVoiceCommand()`

- **返回值**：`boolean`
- **参数**：
  - `command`: `String`
- **行号**：第 3593 行

#### `setSpeechEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 4465 行

### 自动化

#### `getAutomationSettings()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 4539 行

#### `isAutomationScenarioEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `scenarioId`: `String`
- **行号**：第 4568 行

#### `setAutomationScenarioEnabled()`

- **返回值**：`void`
- **参数**：
  - `scenarioId`: `String`
  - `enabled`: `boolean`
- **行号**：第 4583 行

#### `setAutomationSettings()`

- **返回值**：`void`
- **参数**：
  - `jsonStr`: `String`
- **行号**：第 4553 行

### 音乐相关

#### `getCurrentMusicName()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 2351 行

#### `getMusicProgressInfo()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 2362 行

#### `getSystemMusicInfo()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 2391 行

#### `isMusicPlaying()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 2340 行

#### `isNotificationListenerEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 2500 行

#### `nextMusic()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 2442 行

#### `openNotificationListenerSettings()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 2520 行

#### `playPause()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 2652 行

#### `playPauseMusic()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 2425 行

#### `prevMusic()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 2458 行

#### `startMusicVisualizer()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 2299 行

#### `stopMusicVisualizer()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 2320 行

### 应用管理

#### `addQuickApp()`

- **返回值**：`void`
- **参数**：
  - `name`: `String`
  - `packageName`: `String`
  - `iconBase64`: `String`
- **行号**：第 2235 行

#### `getAllComponentConfigs()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 1609 行

#### `getAppList()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 154 行

#### `getAppListAsync()`

- **返回值**：`void`
- **参数**：
  - `callbackId`: `final String`
- **行号**：第 293 行

#### `getConfigApp()`

- **返回值**：`String`
- **参数**：
  - `buttonId`: `String`
- **行号**：第 1539 行

#### `getQuickAppList()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 2207 行

#### `isComponentEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `componentName`: `String`
- **行号**：第 1592 行

#### `isQuickApp()`

- **返回值**：`boolean`
- **参数**：
  - `packageName`: `String`
- **行号**：第 2284 行

#### `launchApp()`

- **返回值**：`void`
- **参数**：
  - `packageName`: `String`
- **行号**：第 1084 行

#### `removeQuickApp()`

- **返回值**：`void`
- **参数**：
  - `packageName`: `String`
- **行号**：第 2261 行

#### `restartApp()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 3117 行

#### `saveComponentConfig()`

- **返回值**：`boolean`
- **参数**：
  - `componentName`: `String`
  - `isEnabled`: `boolean`
- **行号**：第 1566 行

#### `saveConfigApp()`

- **返回值**：`void`
- **参数**：
  - `buttonId`: `String`
  - `appName`: `String`
  - `packageName`: `String`
  - `appIcon`: `String`
- **行号**：第 1519 行

#### `saveSystemLauncherSetting()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 2704 行

#### `saveSystemLauncherSettingAsync()`

- **返回值**：`void`
- **参数**：
  - `enabled`: `final boolean`
  - `callbackId`: `final String`
- **行号**：第 2787 行

### 壁纸相关

#### `deleteCurrentWallpaper()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 1133 行

#### `getCurrentWallpaperUrl()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 4646 行

#### `getNextWallpaperUrl()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 4659 行

#### `getRandomWallpaper()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 479 行

#### `getRandomWallpaperAsync()`

- **返回值**：`void`
- **参数**：
  - `callbackId`: `final String`
- **行号**：第 1888 行

#### `getRandomWallpaperBase64()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 1993 行

#### `getRandomWallpaperBase64Async()`

- **返回值**：`void`
- **参数**：
  - `callbackId`: `final String`
- **行号**：第 2083 行

#### `getWallpaperSettings()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 1805 行

#### `getWallpaperSettingsAsync()`

- **返回值**：`void`
- **参数**：
  - `callbackId`: `final String`
- **行号**：第 1831 行

#### `getWallpaperSettingsV2()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 4606 行

#### `pauseWallpaperCarousel()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 1102 行

#### `pickLocalWallpaperFile()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 4741 行

#### `pickLocalWallpaperFolder()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 4751 行

#### `refreshBingWallpaper()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 4721 行

#### `resumeWallpaperCarousel()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 1117 行

#### `saveWallpaperCarouselSetting()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 373 行

#### `saveWallpaperCarouselSettingAsync()`

- **返回值**：`void`
- **参数**：
  - `enabled`: `final boolean`
  - `callbackId`: `final String`
- **行号**：第 324 行

#### `saveWallpaperSwitchInterval()`

- **返回值**：`boolean`
- **参数**：
  - `interval`: `int`
- **行号**：第 444 行

#### `saveWallpaperSwitchIntervalAsync()`

- **返回值**：`void`
- **参数**：
  - `interval`: `final int`
  - `callbackId`: `final String`
- **行号**：第 395 行

#### `sendWallpaperSettingsChangedBroadcast()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 463 行

#### `setWallpaperCarouselEnabled()`

- **返回值**：`void`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 4672 行

#### `setWallpaperCarouselInterval()`

- **返回值**：`void`
- **参数**：
  - `intervalMs`: `int`
- **行号**：第 4690 行

#### `setWallpaperFillMode()`

- **返回值**：`void`
- **参数**：
  - `mode`: `int`
- **行号**：第 4708 行

#### `setWallpaperPath()`

- **返回值**：`void`
- **参数**：
  - `path`: `String`
- **行号**：第 4633 行

#### `setWallpaperType()`

- **返回值**：`void`
- **参数**：
  - `type`: `int`
- **行号**：第 4620 行

#### `updateWallpaperCategories()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 1631 行

### ADB相关

#### `executeAdbPermissionGrant()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 1360 行

#### `setDefaultDesktopViaAdb()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 1456 行

#### `triggerUsbDebugAuthorization()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 1149 行

#### `triggerWirelessAdbAuthorization()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 1188 行

### 权限相关

#### `hasDumpPermission()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 3745 行

#### `hasNotificationAccess()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 3612 行

#### `hasReadLogsPermission()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 3713 行

#### `hasWriteSecureSettingsPermission()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 3729 行

### 导航相关

#### `navigateToCompany()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 3179 行

#### `navigateToHome()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 3143 行

### 时间日期

#### `getLunarCalendar()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 2754 行

#### `updateCategoryEnabled()`

- **返回值**：`void`
- **参数**：
  - `categoryId`: `String`
  - `enabled`: `boolean`
- **行号**：第 1668 行

#### `updateCategoryEnabledAsync()`

- **返回值**：`void`
- **参数**：
  - `categoryId`: `final String`
  - `enabled`: `final boolean`
  - `callbackId`: `final String`
- **行号**：第 1684 行

#### `updateTimeDisplay()`

- **返回值**：`void`
- **参数**：
  - `time`: `String`
  - `date`: `String`
  - `lunarDate`: `String`
- **行号**：第 2965 行

#### `updateTimeDisplayAsync()`

- **返回值**：`void`
- **参数**：
  - `callbackId`: `final String`
- **行号**：第 3004 行

### 系统设置

#### `isBluetoothConnected()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 2564 行

#### `isWifiConnected()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 2534 行

#### `saveBootGreetingSetting()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 2721 行

#### `saveBootGreetingSettingAsync()`

- **返回值**：`void`
- **参数**：
  - `enabled`: `final boolean`
  - `callbackId`: `final String`
- **行号**：第 2831 行

#### `saveRandomModeSetting()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 2738 行

#### `saveRandomModeSettingAsync()`

- **返回值**：`void`
- **参数**：
  - `enabled`: `final boolean`
  - `callbackId`: `final String`
- **行号**：第 2875 行

#### `saveSpecifiedModeSetting()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 2770 行

#### `saveSpecifiedModeSettingAsync()`

- **返回值**：`void`
- **参数**：
  - `enabled`: `final boolean`
  - `callbackId`: `final String`
- **行号**：第 2919 行

#### `setBluetoothEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 3951 行

#### `setNightMode()`

- **返回值**：`boolean`
- **参数**：
  - `night`: `boolean`
- **行号**：第 3925 行

#### `setWifiEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 3938 行

### 状态获取

#### `getCarState()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 3650 行

#### `getEnabledCategories()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 1727 行

#### `getEnabledCategoriesAsync()`

- **返回值**：`void`
- **参数**：
  - `callbackId`: `final String`
- **行号**：第 1751 行

#### `isCameraOverspeedLimitEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 4008 行

#### `isLogServiceRunning()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 3703 行

#### `isScreenOn()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 4508 行

#### `isVehicleLocked()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 4494 行

#### `isVideoWhileDrivingEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 4035 行

### 保存/设置

#### `setCameraOverspeedLimit()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 3995 行

#### `setDefaultDesktop()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 3214 行

#### `setVideoWhileDriving()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 4022 行

### 其他

#### `adjustTemperature()`

- **返回值**：`void`
- **参数**：
  - `delta`: `int`
- **行号**：第 3516 行

#### `initializeAcStatus()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 3087 行

#### `openRecentTasks()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 1406 行

#### `openRecents()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 1432 行

#### `playNext()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 2670 行

#### `playPrevious()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 2682 行

#### `sendNextTrack()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 3979 行

#### `sendPrevTrack()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 3966 行

---

## 🔄 重新生成

```bash
python3 tools/generate_api_docs.py
```
