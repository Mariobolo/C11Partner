# WebViewBridge JS 接口文档

> 📋 本文档由工具自动生成，请勿手动修改
> 
> 生成时间：2026-06-22 01:33:06
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
- [音乐相关](#音乐相关)
- [应用管理](#应用管理)
- [壁纸相关](#壁纸相关)
- [ADB相关](#adb相关)
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
| `getDriverTemp()` | - | `int` | - |
| `getPassengerTemp()` | - | `int` | - |
| `getWindLevel()` | - | `int` | - |
| `isAcEnabled()` | - | `boolean` | - |
| `setAcEnabled()` | - | `boolean` | `enabled`: boolean |
| `setDriverTemp()` | - | `boolean` | `temp`: int |
| `setMaxCooling()` | - | `boolean` | `on`: boolean |
| `setPassengerTemp()` | - | `boolean` | `temp`: int |
| `setWindLevel()` | - | `boolean` | `level`: int |

## 音量控制

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `getCallVolume()` | - | `int` | - |
| `getMusicVolume()` | - | `int` | - |
| `getMusicVolume()` | - | `int` | - |
| `getNaviVolume()` | - | `int` | - |
| `setCallVolume()` | - | `boolean` | `volume`: int |
| `setMusicVolume()` | - | `boolean` | `volume`: int |
| `setMusicVolume()` | - | `void` | `volume`: int |
| `setNaviVolume()` | - | `boolean` | `volume`: int |
| `volumeDown()` | - | `void` | - |
| `volumeUp()` | - | `void` | - |

## 副屏相关

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `isSecondaryScreenEnabled()` | - | `boolean` | - |
| `setSecondaryScreenEnabled()` | - | `boolean` | `enabled`: boolean |

## 语音控制

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `isSpeechEnabled()` | - | `boolean` | - |
| `sendVoiceCommand()` | - | `boolean` | `command`: String |
| `setSpeechEnabled()` | - | `boolean` | `enabled`: boolean |

## 音乐相关

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `getCurrentMusicArtist()` | - | `String` | - |
| `getCurrentMusicName()` | - | `String` | - |
| `getMusicProgressInfo()` | - | `String` | - |
| `getSystemMusicInfo()` | - | `String` | - |
| `isMusicPlaying()` | - | `boolean` | - |
| `isNotificationListenerEnabled()` | - | `boolean` | - |
| `nextMusic()` | - | `void` | - |
| `openNotificationListenerSettings()` | - | `void` | - |
| `playPauseMusic()` | - | `void` | - |
| `prevMusic()` | - | `void` | - |
| `startMusicVisualizer()` | - | `void` | - |
| `stopMusicVisualizer()` | - | `void` | - |

## 应用管理

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `addQuickApp()` | - | `void` | `name`: String, `packageName`: String, `iconBase64`: String |
| `getAllApps()` | - | `String` | - |
| `getAllComponentConfigs()` | - | `String` | - |
| `getAppIcon()` | - | `String` | `packageName`: String |
| `getAppInfo()` | - | `String` | `packageName`: String |
| `getAppList()` | - | `String` | - |
| `getConfigApp()` | - | `String` | `buttonId`: String |
| `getQuickAppList()` | - | `String` | - |
| `getSystemApps()` | - | `String` | - |
| `getUserApps()` | - | `String` | - |
| `isAppInstalled()` | - | `boolean` | `packageName`: String |
| `isComponentEnabled()` | - | `boolean` | `componentName`: String |
| `isQuickApp()` | - | `boolean` | `packageName`: String |
| `launchApp()` | - | `void` | `packageName`: String |
| `openAppInfo()` | - | `void` | `packageName`: String |
| `removeQuickApp()` | - | `void` | `packageName`: String |
| `saveComponentConfig()` | - | `boolean` | `componentName`: String, `isEnabled`: boolean |
| `saveConfigApp()` | - | `void` | `buttonId`: String, `appName`: String, `packageName`: String, `appIcon`: String |
| `saveSystemLauncherSetting()` | - | `boolean` | `enabled`: boolean |
| `saveSystemLauncherSettingAsync()` | - | `void` | `enabled`: final boolean, `callbackId`: final String |

## 壁纸相关

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `deleteCurrentWallpaper()` | - | `boolean` | - |
| `getRandomWallpaper()` | - | `String` | - |
| `getRandomWallpaperAsync()` | - | `void` | `callbackId`: final String |
| `getRandomWallpaperBase64()` | - | `String` | - |
| `getRandomWallpaperBase64Async()` | - | `void` | `callbackId`: final String |
| `getWallpaperSettings()` | - | `String` | - |
| `getWallpaperSettingsAsync()` | - | `void` | `callbackId`: final String |
| `pauseWallpaperCarousel()` | - | `void` | - |
| `resumeWallpaperCarousel()` | - | `void` | - |
| `saveWallpaperCarouselSetting()` | - | `boolean` | `enabled`: boolean |
| `saveWallpaperCarouselSettingAsync()` | - | `void` | `enabled`: final boolean, `callbackId`: final String |
| `saveWallpaperSwitchInterval()` | - | `boolean` | `interval`: int |
| `saveWallpaperSwitchIntervalAsync()` | - | `void` | `interval`: final int, `callbackId`: final String |
| `sendWallpaperSettingsChangedBroadcast()` | - | `void` | - |
| `updateWallpaperCategories()` | - | `void` | - |

## ADB相关

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `executeAdbPermissionGrant()` | - | `void` | - |
| `setDefaultDesktopViaAdb()` | - | `void` | - |
| `triggerUsbDebugAuthorization()` | - | `void` | - |
| `triggerWirelessAdbAuthorization()` | - | `void` | - |

## 时间日期

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `getLunarCalendar()` | - | `String` | - |
| `getScreenTimeout()` | - | `int` | - |
| `setScreenTimeout()` | - | `boolean` | `seconds`: int |
| `updateCategoryEnabled()` | - | `void` | `categoryId`: String, `enabled`: boolean |
| `updateCategoryEnabledAsync()` | - | `void` | `categoryId`: final String, `enabled`: final boolean, `callbackId`: final String |

## 系统设置

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `isBluetoothConnected()` | - | `boolean` | - |
| `isWifiConnected()` | - | `boolean` | - |
| `saveBootGreetingSetting()` | - | `boolean` | `enabled`: boolean |
| `saveRandomModeSetting()` | - | `boolean` | `enabled`: boolean |
| `saveSpecifiedModeSetting()` | - | `boolean` | `enabled`: boolean |
| `setBluetoothEnabled()` | - | `boolean` | `enabled`: boolean |
| `setNightMode()` | - | `boolean` | `on`: boolean |
| `setWifiEnabled()` | - | `boolean` | `enabled`: boolean |

## 状态获取

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `getEnabledCategories()` | - | `String` | - |
| `getScreenBrightness()` | - | `int` | - |
| `isAutoBrightnessEnabled()` | - | `boolean` | - |
| `isCameraOverspeedLimitEnabled()` | - | `boolean` | - |
| `isScreenOn()` | - | `boolean` | - |
| `isVehicleLocked()` | - | `boolean` | - |
| `isVideoWhileDrivingEnabled()` | - | `boolean` | - |

## 保存/设置

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `setAutoBrightness()` | - | `boolean` | `enabled`: boolean |
| `setCameraOverspeedLimit()` | - | `boolean` | `enabled`: boolean |
| `setPlaybackSpeed()` | - | `void` | `speed`: float |
| `setScreenBrightness()` | - | `boolean` | `brightness`: int |
| `setVideoWhileDriving()` | - | `boolean` | `enabled`: boolean |

## 其他

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `openRecentTasks()` | - | `void` | - |
| `openRecents()` | - | `void` | - |
| `seekTo()` | - | `void` | `position`: long |
| `sendNextTrack()` | - | `boolean` | - |
| `sendPrevTrack()` | - | `boolean` | - |

---

## 📝 详细说明

### 360全景

#### `startCamera360()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 85 行

### 灯光控制

#### `getAmbientLightColor()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 243 行

#### `isAmbientLightEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 233 行

#### `setAmbientLightColor()`

- **返回值**：`boolean`
- **参数**：
  - `color`: `int`
- **行号**：第 238 行

#### `setAmbientLightEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 228 行

#### `setLowBeamLight()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 99 行

#### `setPedestrianAlert()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 114 行

#### `setPositionLight()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 109 行

#### `setRearFogLight()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 104 行

### 驾驶/场景模式

#### `setCampingMode()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 135 行

#### `setDriveMode()`

- **返回值**：`boolean`
- **参数**：
  - `mode`: `int`
- **行号**：第 120 行

#### `setGuardMode()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 125 行

#### `setPowerSaveMode()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 140 行

#### `setRestMode()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 130 行

#### `setSentinelMode()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 145 行

### 空调控制

#### `getDriverTemp()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 181 行

#### `getPassengerTemp()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 191 行

#### `getWindLevel()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 171 行

#### `isAcEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 161 行

#### `setAcEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 156 行

#### `setDriverTemp()`

- **返回值**：`boolean`
- **参数**：
  - `temp`: `int`
- **行号**：第 176 行

#### `setMaxCooling()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 151 行

#### `setPassengerTemp()`

- **返回值**：`boolean`
- **参数**：
  - `temp`: `int`
- **行号**：第 186 行

#### `setWindLevel()`

- **返回值**：`boolean`
- **参数**：
  - `level`: `int`
- **行号**：第 166 行

### 音量控制

#### `getCallVolume()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 202 行

#### `getMusicVolume()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 222 行

#### `getMusicVolume()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 485 行

#### `getNaviVolume()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 212 行

#### `setCallVolume()`

- **返回值**：`boolean`
- **参数**：
  - `volume`: `int`
- **行号**：第 197 行

#### `setMusicVolume()`

- **返回值**：`boolean`
- **参数**：
  - `volume`: `int`
- **行号**：第 217 行

#### `setMusicVolume()`

- **返回值**：`void`
- **参数**：
  - `volume`: `int`
- **行号**：第 482 行

#### `setNaviVolume()`

- **返回值**：`boolean`
- **参数**：
  - `volume`: `int`
- **行号**：第 207 行

#### `volumeDown()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 491 行

#### `volumeUp()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 488 行

### 副屏相关

#### `isSecondaryScreenEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 280 行

#### `setSecondaryScreenEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 275 行

### 语音控制

#### `isSpeechEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 291 行

#### `sendVoiceCommand()`

- **返回值**：`boolean`
- **参数**：
  - `command`: `String`
- **行号**：第 296 行

#### `setSpeechEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 286 行

### 音乐相关

#### `getCurrentMusicArtist()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 463 行

#### `getCurrentMusicName()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 460 行

#### `getMusicProgressInfo()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 466 行

#### `getSystemMusicInfo()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 469 行

#### `isMusicPlaying()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 457 行

#### `isNotificationListenerEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 501 行

#### `nextMusic()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 475 行

#### `openNotificationListenerSettings()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 506 行

#### `playPauseMusic()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 472 行

#### `prevMusic()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 478 行

#### `startMusicVisualizer()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 451 行

#### `stopMusicVisualizer()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 454 行

### 应用管理

#### `addQuickApp()`

- **返回值**：`void`
- **参数**：
  - `name`: `String`
  - `packageName`: `String`
  - `iconBase64`: `String`
- **行号**：第 354 行

#### `getAllApps()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 327 行

#### `getAllComponentConfigs()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 596 行

#### `getAppIcon()`

- **返回值**：`String`
- **参数**：
  - `packageName`: `String`
- **行号**：第 339 行

#### `getAppInfo()`

- **返回值**：`String`
- **参数**：
  - `packageName`: `String`
- **行号**：第 336 行

#### `getAppList()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 324 行

#### `getConfigApp()`

- **返回值**：`String`
- **参数**：
  - `buttonId`: `String`
- **行号**：第 370 行

#### `getQuickAppList()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 351 行

#### `getSystemApps()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 333 行

#### `getUserApps()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 330 行

#### `isAppInstalled()`

- **返回值**：`boolean`
- **参数**：
  - `packageName`: `String`
- **行号**：第 342 行

#### `isComponentEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `componentName`: `String`
- **行号**：第 591 行

#### `isQuickApp()`

- **返回值**：`boolean`
- **参数**：
  - `packageName`: `String`
- **行号**：第 362 行

#### `launchApp()`

- **返回值**：`void`
- **参数**：
  - `packageName`: `String`
- **行号**：第 345 行

#### `openAppInfo()`

- **返回值**：`void`
- **参数**：
  - `packageName`: `String`
- **行号**：第 348 行

#### `removeQuickApp()`

- **返回值**：`void`
- **参数**：
  - `packageName`: `String`
- **行号**：第 359 行

#### `saveComponentConfig()`

- **返回值**：`boolean`
- **参数**：
  - `componentName`: `String`
  - `isEnabled`: `boolean`
- **行号**：第 586 行

#### `saveConfigApp()`

- **返回值**：`void`
- **参数**：
  - `buttonId`: `String`
  - `appName`: `String`
  - `packageName`: `String`
  - `appIcon`: `String`
- **行号**：第 365 行

#### `saveSystemLauncherSetting()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 518 行

#### `saveSystemLauncherSettingAsync()`

- **返回值**：`void`
- **参数**：
  - `enabled`: `final boolean`
  - `callbackId`: `final String`
- **行号**：第 523 行

### 壁纸相关

#### `deleteCurrentWallpaper()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 447 行

#### `getRandomWallpaper()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 399 行

#### `getRandomWallpaperAsync()`

- **返回值**：`void`
- **参数**：
  - `callbackId`: `final String`
- **行号**：第 402 行

#### `getRandomWallpaperBase64()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 407 行

#### `getRandomWallpaperBase64Async()`

- **返回值**：`void`
- **参数**：
  - `callbackId`: `final String`
- **行号**：第 412 行

#### `getWallpaperSettings()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 433 行

#### `getWallpaperSettingsAsync()`

- **返回值**：`void`
- **参数**：
  - `callbackId`: `final String`
- **行号**：第 436 行

#### `pauseWallpaperCarousel()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 441 行

#### `resumeWallpaperCarousel()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 444 行

#### `saveWallpaperCarouselSetting()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 374 行

#### `saveWallpaperCarouselSettingAsync()`

- **返回值**：`void`
- **参数**：
  - `enabled`: `final boolean`
  - `callbackId`: `final String`
- **行号**：第 379 行

#### `saveWallpaperSwitchInterval()`

- **返回值**：`boolean`
- **参数**：
  - `interval`: `int`
- **行号**：第 384 行

#### `saveWallpaperSwitchIntervalAsync()`

- **返回值**：`void`
- **参数**：
  - `interval`: `final int`
  - `callbackId`: `final String`
- **行号**：第 389 行

#### `sendWallpaperSettingsChangedBroadcast()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 394 行

#### `updateWallpaperCategories()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 417 行

### ADB相关

#### `executeAdbPermissionGrant()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 573 行

#### `setDefaultDesktopViaAdb()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 582 行

#### `triggerUsbDebugAuthorization()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 567 行

#### `triggerWirelessAdbAuthorization()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 570 行

### 时间日期

#### `getLunarCalendar()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 543 行

#### `getScreenTimeout()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 563 行

#### `setScreenTimeout()`

- **返回值**：`boolean`
- **参数**：
  - `seconds`: `int`
- **行号**：第 560 行

#### `updateCategoryEnabled()`

- **返回值**：`void`
- **参数**：
  - `categoryId`: `String`
  - `enabled`: `boolean`
- **行号**：第 420 行

#### `updateCategoryEnabledAsync()`

- **返回值**：`void`
- **参数**：
  - `categoryId`: `final String`
  - `enabled`: `final boolean`
  - `callbackId`: `final String`
- **行号**：第 428 行

### 系统设置

#### `isBluetoothConnected()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 515 行

#### `isWifiConnected()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 512 行

#### `saveBootGreetingSetting()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 528 行

#### `saveRandomModeSetting()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 533 行

#### `saveSpecifiedModeSetting()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 538 行

#### `setBluetoothEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 259 行

#### `setNightMode()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 249 行

#### `setWifiEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 254 行

### 状态获取

#### `getEnabledCategories()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 425 行

#### `getScreenBrightness()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 550 行

#### `isAutoBrightnessEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 556 行

#### `isCameraOverspeedLimitEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 93 行

#### `isScreenOn()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 318 行

#### `isVehicleLocked()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 313 行

#### `isVideoWhileDrivingEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 269 行

### 保存/设置

#### `setAutoBrightness()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 553 行

#### `setCameraOverspeedLimit()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 88 行

#### `setPlaybackSpeed()`

- **返回值**：`void`
- **参数**：
  - `speed`: `float`
- **行号**：第 498 行

#### `setScreenBrightness()`

- **返回值**：`boolean`
- **参数**：
  - `brightness`: `int`
- **行号**：第 547 行

#### `setVideoWhileDriving()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 264 行

### 其他

#### `openRecentTasks()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 576 行

#### `openRecents()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 579 行

#### `seekTo()`

- **返回值**：`void`
- **参数**：
  - `position`: `long`
- **行号**：第 495 行

#### `sendNextTrack()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 307 行

#### `sendPrevTrack()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 302 行

---

## 🔄 重新生成

```bash
python3 tools/generate_api_docs.py
```
