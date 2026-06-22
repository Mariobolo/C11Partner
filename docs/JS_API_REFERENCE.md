# WebViewBridge JS 接口文档

> 📋 本文档由工具自动生成，请勿手动修改
> 
> 生成时间：2026-06-22 23:23:13
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
| `getNaviVolume()` | - | `int` | - |
| `setCallVolume()` | - | `boolean` | `volume`: int |
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
| `playSongAtIndex()` | - | `void` | `index`: int |
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
| `getAppVersionInfo()` | - | `String` | - |
| `getConfigApp()` | - | `String` | `buttonId`: String |
| `getQuickAppList()` | - | `String` | - |
| `getSystemApps()` | - | `String` | - |
| `getUserApps()` | - | `String` | - |
| `isAppInstalled()` | - | `boolean` | `packageName`: String |
| `isComponentEnabled()` | - | `boolean` | `componentName`: String |
| `isQuickApp()` | - | `boolean` | `packageName`: String |
| `launchApp()` | - | `void` | `packageName`: String |
| `openAppInfo()` | - | `void` | `packageName`: String |
| `openAppSettings()` | - | `void` | - |
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
| `openSystemSettings()` | - | `void` | - |
| `saveBootGreetingSetting()` | - | `boolean` | `enabled`: boolean |
| `saveRandomModeSetting()` | - | `boolean` | `enabled`: boolean |
| `saveSpecifiedModeSetting()` | - | `boolean` | `enabled`: boolean |
| `setBluetoothEnabled()` | - | `boolean` | `enabled`: boolean |
| `setNightMode()` | - | `boolean` | `on`: boolean |
| `setWifiEnabled()` | - | `boolean` | `enabled`: boolean |

## 状态获取

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `getBatteryInfo()` | - | `String` | - |
| `getEnabledCategories()` | - | `String` | - |
| `getMemoryInfo()` | - | `String` | - |
| `getPlaylist()` | - | `String` | - |
| `getRepeatMode()` | - | `int` | - |
| `getScreenBrightness()` | - | `int` | - |
| `getSystemVersionInfo()` | - | `String` | - |
| `isAutoBrightnessEnabled()` | - | `boolean` | - |
| `isCameraOverspeedLimitEnabled()` | - | `boolean` | - |
| `isScreenOn()` | - | `boolean` | - |
| `isShuffleEnabled()` | - | `boolean` | - |
| `isVehicleLocked()` | - | `boolean` | - |
| `isVideoWhileDrivingEnabled()` | - | `boolean` | - |

## 保存/设置

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `setAutoBrightness()` | - | `boolean` | `enabled`: boolean |
| `setCameraOverspeedLimit()` | - | `boolean` | `enabled`: boolean |
| `setDriverSeatHeating()` | - | `boolean` | `level`: int |
| `setDriverSeatVentilation()` | - | `boolean` | `level`: int |
| `setMirrorHeating()` | - | `boolean` | `on`: boolean |
| `setPassengerSeatHeating()` | - | `boolean` | `level`: int |
| `setPassengerSeatVentilation()` | - | `boolean` | `level`: int |
| `setPlaybackSpeed()` | - | `void` | `speed`: float |
| `setRepeatMode()` | - | `void` | `mode`: int |
| `setScreenBrightness()` | - | `boolean` | `brightness`: int |
| `setShuffleMode()` | - | `void` | `enabled`: boolean |
| `setSteeringWheelHeating()` | - | `boolean` | `on`: boolean |
| `setVideoWhileDriving()` | - | `boolean` | `enabled`: boolean |

## 其他

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `foldMirrors()` | - | `boolean` | - |
| `openRecentTasks()` | - | `void` | - |
| `openRecents()` | - | `void` | - |
| `seekTo()` | - | `void` | `position`: long |
| `sendNextTrack()` | - | `boolean` | - |
| `sendPrevTrack()` | - | `boolean` | - |
| `unfoldMirrors()` | - | `boolean` | - |

---

## 📝 详细说明

### 360全景

#### `startCamera360()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 79 行

### 灯光控制

#### `getAmbientLightColor()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 227 行

#### `isAmbientLightEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 217 行

#### `setAmbientLightColor()`

- **返回值**：`boolean`
- **参数**：
  - `color`: `int`
- **行号**：第 222 行

#### `setAmbientLightEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 212 行

#### `setLowBeamLight()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 93 行

#### `setPedestrianAlert()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 108 行

#### `setPositionLight()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 103 行

#### `setRearFogLight()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 98 行

### 驾驶/场景模式

#### `setCampingMode()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 129 行

#### `setDriveMode()`

- **返回值**：`boolean`
- **参数**：
  - `mode`: `int`
- **行号**：第 114 行

#### `setGuardMode()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 119 行

#### `setPowerSaveMode()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 134 行

#### `setRestMode()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 124 行

#### `setSentinelMode()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 139 行

### 空调控制

#### `getDriverTemp()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 175 行

#### `getPassengerTemp()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 185 行

#### `getWindLevel()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 165 行

#### `isAcEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 155 行

#### `setAcEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 150 行

#### `setDriverTemp()`

- **返回值**：`boolean`
- **参数**：
  - `temp`: `int`
- **行号**：第 170 行

#### `setMaxCooling()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 145 行

#### `setPassengerTemp()`

- **返回值**：`boolean`
- **参数**：
  - `temp`: `int`
- **行号**：第 180 行

#### `setWindLevel()`

- **返回值**：`boolean`
- **参数**：
  - `level`: `int`
- **行号**：第 160 行

### 音量控制

#### `getCallVolume()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 196 行

#### `getMusicVolume()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 520 行

#### `getNaviVolume()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 206 行

#### `setCallVolume()`

- **返回值**：`boolean`
- **参数**：
  - `volume`: `int`
- **行号**：第 191 行

#### `setMusicVolume()`

- **返回值**：`void`
- **参数**：
  - `volume`: `int`
- **行号**：第 517 行

#### `setNaviVolume()`

- **返回值**：`boolean`
- **参数**：
  - `volume`: `int`
- **行号**：第 201 行

#### `volumeDown()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 526 行

#### `volumeUp()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 523 行

### 副屏相关

#### `isSecondaryScreenEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 264 行

#### `setSecondaryScreenEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 259 行

### 语音控制

#### `isSpeechEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 275 行

#### `sendVoiceCommand()`

- **返回值**：`boolean`
- **参数**：
  - `command`: `String`
- **行号**：第 280 行

#### `setSpeechEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 270 行

### 音乐相关

#### `getCurrentMusicArtist()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 498 行

#### `getCurrentMusicName()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 495 行

#### `getMusicProgressInfo()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 501 行

#### `getSystemMusicInfo()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 504 行

#### `isMusicPlaying()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 492 行

#### `isNotificationListenerEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 536 行

#### `nextMusic()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 510 行

#### `openNotificationListenerSettings()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 541 行

#### `playPauseMusic()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 507 行

#### `playSongAtIndex()`

- **返回值**：`void`
- **参数**：
  - `index`: `int`
- **行号**：第 550 行

#### `prevMusic()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 513 行

#### `startMusicVisualizer()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 486 行

#### `stopMusicVisualizer()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 489 行

### 应用管理

#### `addQuickApp()`

- **返回值**：`void`
- **参数**：
  - `name`: `String`
  - `packageName`: `String`
  - `iconBase64`: `String`
- **行号**：第 389 行

#### `getAllApps()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 362 行

#### `getAllComponentConfigs()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 650 行

#### `getAppIcon()`

- **返回值**：`String`
- **参数**：
  - `packageName`: `String`
- **行号**：第 374 行

#### `getAppInfo()`

- **返回值**：`String`
- **参数**：
  - `packageName`: `String`
- **行号**：第 371 行

#### `getAppList()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 359 行

#### `getAppVersionInfo()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 659 行

#### `getConfigApp()`

- **返回值**：`String`
- **参数**：
  - `buttonId`: `String`
- **行号**：第 405 行

#### `getQuickAppList()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 386 行

#### `getSystemApps()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 368 行

#### `getUserApps()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 365 行

#### `isAppInstalled()`

- **返回值**：`boolean`
- **参数**：
  - `packageName`: `String`
- **行号**：第 377 行

#### `isComponentEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `componentName`: `String`
- **行号**：第 645 行

#### `isQuickApp()`

- **返回值**：`boolean`
- **参数**：
  - `packageName`: `String`
- **行号**：第 397 行

#### `launchApp()`

- **返回值**：`void`
- **参数**：
  - `packageName`: `String`
- **行号**：第 380 行

#### `openAppInfo()`

- **返回值**：`void`
- **参数**：
  - `packageName`: `String`
- **行号**：第 383 行

#### `openAppSettings()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 675 行

#### `removeQuickApp()`

- **返回值**：`void`
- **参数**：
  - `packageName`: `String`
- **行号**：第 394 行

#### `restartApp()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 669 行

#### `saveComponentConfig()`

- **返回值**：`boolean`
- **参数**：
  - `componentName`: `String`
  - `isEnabled`: `boolean`
- **行号**：第 640 行

#### `saveConfigApp()`

- **返回值**：`void`
- **参数**：
  - `buttonId`: `String`
  - `appName`: `String`
  - `packageName`: `String`
  - `appIcon`: `String`
- **行号**：第 400 行

#### `saveSystemLauncherSetting()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 572 行

#### `saveSystemLauncherSettingAsync()`

- **返回值**：`void`
- **参数**：
  - `enabled`: `final boolean`
  - `callbackId`: `final String`
- **行号**：第 577 行

### 壁纸相关

#### `deleteCurrentWallpaper()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 482 行

#### `getRandomWallpaper()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 434 行

#### `getRandomWallpaperAsync()`

- **返回值**：`void`
- **参数**：
  - `callbackId`: `final String`
- **行号**：第 437 行

#### `getRandomWallpaperBase64()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 442 行

#### `getRandomWallpaperBase64Async()`

- **返回值**：`void`
- **参数**：
  - `callbackId`: `final String`
- **行号**：第 447 行

#### `getWallpaperSettings()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 468 行

#### `getWallpaperSettingsAsync()`

- **返回值**：`void`
- **参数**：
  - `callbackId`: `final String`
- **行号**：第 471 行

#### `pauseWallpaperCarousel()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 476 行

#### `resumeWallpaperCarousel()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 479 行

#### `saveWallpaperCarouselSetting()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 409 行

#### `saveWallpaperCarouselSettingAsync()`

- **返回值**：`void`
- **参数**：
  - `enabled`: `final boolean`
  - `callbackId`: `final String`
- **行号**：第 414 行

#### `saveWallpaperSwitchInterval()`

- **返回值**：`boolean`
- **参数**：
  - `interval`: `int`
- **行号**：第 419 行

#### `saveWallpaperSwitchIntervalAsync()`

- **返回值**：`void`
- **参数**：
  - `interval`: `final int`
  - `callbackId`: `final String`
- **行号**：第 424 行

#### `sendWallpaperSettingsChangedBroadcast()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 429 行

#### `updateWallpaperCategories()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 452 行

### ADB相关

#### `executeAdbPermissionGrant()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 627 行

#### `setDefaultDesktopViaAdb()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 636 行

#### `triggerUsbDebugAuthorization()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 621 行

#### `triggerWirelessAdbAuthorization()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 624 行

### 时间日期

#### `getLunarCalendar()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 597 行

#### `getScreenTimeout()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 617 行

#### `setScreenTimeout()`

- **返回值**：`boolean`
- **参数**：
  - `seconds`: `int`
- **行号**：第 614 行

#### `updateCategoryEnabled()`

- **返回值**：`void`
- **参数**：
  - `categoryId`: `String`
  - `enabled`: `boolean`
- **行号**：第 455 行

#### `updateCategoryEnabledAsync()`

- **返回值**：`void`
- **参数**：
  - `categoryId`: `final String`
  - `enabled`: `final boolean`
  - `callbackId`: `final String`
- **行号**：第 463 行

### 系统设置

#### `isBluetoothConnected()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 569 行

#### `isWifiConnected()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 566 行

#### `openSystemSettings()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 672 行

#### `saveBootGreetingSetting()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 582 行

#### `saveRandomModeSetting()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 587 行

#### `saveSpecifiedModeSetting()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 592 行

#### `setBluetoothEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 243 行

#### `setNightMode()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 233 行

#### `setWifiEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 238 行

### 状态获取

#### `getBatteryInfo()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 665 行

#### `getEnabledCategories()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 460 行

#### `getMemoryInfo()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 662 行

#### `getPlaylist()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 547 行

#### `getRepeatMode()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 556 行

#### `getScreenBrightness()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 604 行

#### `getSystemVersionInfo()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 656 行

#### `isAutoBrightnessEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 610 行

#### `isCameraOverspeedLimitEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 87 行

#### `isScreenOn()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 310 行

#### `isShuffleEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 562 行

#### `isVehicleLocked()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 305 行

#### `isVideoWhileDrivingEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 253 行

### 保存/设置

#### `setAutoBrightness()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 607 行

#### `setCameraOverspeedLimit()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 82 行

#### `setDriverSeatHeating()`

- **返回值**：`boolean`
- **参数**：
  - `level`: `int`
- **行号**：第 316 行

#### `setDriverSeatVentilation()`

- **返回值**：`boolean`
- **参数**：
  - `level`: `int`
- **行号**：第 326 行

#### `setMirrorHeating()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 353 行

#### `setPassengerSeatHeating()`

- **返回值**：`boolean`
- **参数**：
  - `level`: `int`
- **行号**：第 321 行

#### `setPassengerSeatVentilation()`

- **返回值**：`boolean`
- **参数**：
  - `level`: `int`
- **行号**：第 331 行

#### `setPlaybackSpeed()`

- **返回值**：`void`
- **参数**：
  - `speed`: `float`
- **行号**：第 533 行

#### `setRepeatMode()`

- **返回值**：`void`
- **参数**：
  - `mode`: `int`
- **行号**：第 553 行

#### `setScreenBrightness()`

- **返回值**：`boolean`
- **参数**：
  - `brightness`: `int`
- **行号**：第 601 行

#### `setShuffleMode()`

- **返回值**：`void`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 559 行

#### `setSteeringWheelHeating()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 337 行

#### `setVideoWhileDriving()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 248 行

### 其他

#### `foldMirrors()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 343 行

#### `openRecentTasks()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 630 行

#### `openRecents()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 633 行

#### `seekTo()`

- **返回值**：`void`
- **参数**：
  - `position`: `long`
- **行号**：第 530 行

#### `sendNextTrack()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 299 行

#### `sendPrevTrack()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 290 行

#### `unfoldMirrors()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 348 行

---

## 🔄 重新生成

```bash
python3 tools/generate_api_docs.py
```
