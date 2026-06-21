# WebViewBridge JS 接口文档

> 📋 本文档由工具自动生成，请勿手动修改
> 
> 生成时间：2026-06-22 00:37:16
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
| `setMusicVolume()` | - | `boolean` | `volume`: int |
| `setNaviVolume()` | - | `boolean` | `volume`: int |

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
| `playPause()` | - | `void` | - |
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
| `isCameraOverspeedLimitEnabled()` | - | `boolean` | - |
| `isScreenOn()` | - | `boolean` | - |
| `isVehicleLocked()` | - | `boolean` | - |
| `isVideoWhileDrivingEnabled()` | - | `boolean` | - |

## 保存/设置

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
| `setCameraOverspeedLimit()` | - | `boolean` | `enabled`: boolean |
| `setVideoWhileDriving()` | - | `boolean` | `enabled`: boolean |

## 其他

| 方法名 | 说明 | 返回值 | 参数 |
|--------|------|--------|------|
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
- **行号**：第 97 行

### 灯光控制

#### `getAmbientLightColor()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 255 行

#### `isAmbientLightEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 245 行

#### `setAmbientLightColor()`

- **返回值**：`boolean`
- **参数**：
  - `color`: `int`
- **行号**：第 250 行

#### `setAmbientLightEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 240 行

#### `setLowBeamLight()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 111 行

#### `setPedestrianAlert()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 126 行

#### `setPositionLight()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 121 行

#### `setRearFogLight()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 116 行

### 驾驶/场景模式

#### `setCampingMode()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 147 行

#### `setDriveMode()`

- **返回值**：`boolean`
- **参数**：
  - `mode`: `int`
- **行号**：第 132 行

#### `setGuardMode()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 137 行

#### `setPowerSaveMode()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 152 行

#### `setRestMode()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 142 行

#### `setSentinelMode()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 157 行

### 空调控制

#### `getDriverTemp()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 193 行

#### `getPassengerTemp()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 203 行

#### `getWindLevel()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 183 行

#### `isAcEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 173 行

#### `setAcEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 168 行

#### `setDriverTemp()`

- **返回值**：`boolean`
- **参数**：
  - `temp`: `int`
- **行号**：第 188 行

#### `setMaxCooling()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 163 行

#### `setPassengerTemp()`

- **返回值**：`boolean`
- **参数**：
  - `temp`: `int`
- **行号**：第 198 行

#### `setWindLevel()`

- **返回值**：`boolean`
- **参数**：
  - `level`: `int`
- **行号**：第 178 行

### 音量控制

#### `getCallVolume()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 214 行

#### `getMusicVolume()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 234 行

#### `getNaviVolume()`

- **返回值**：`int`
- **参数**：无
- **行号**：第 224 行

#### `setCallVolume()`

- **返回值**：`boolean`
- **参数**：
  - `volume`: `int`
- **行号**：第 209 行

#### `setMusicVolume()`

- **返回值**：`boolean`
- **参数**：
  - `volume`: `int`
- **行号**：第 229 行

#### `setNaviVolume()`

- **返回值**：`boolean`
- **参数**：
  - `volume`: `int`
- **行号**：第 219 行

### 副屏相关

#### `isSecondaryScreenEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 292 行

#### `setSecondaryScreenEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 287 行

### 语音控制

#### `isSpeechEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 303 行

#### `sendVoiceCommand()`

- **返回值**：`boolean`
- **参数**：
  - `command`: `String`
- **行号**：第 308 行

#### `setSpeechEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 298 行

### 音乐相关

#### `getCurrentMusicArtist()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 475 行

#### `getCurrentMusicName()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 472 行

#### `getMusicProgressInfo()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 478 行

#### `getSystemMusicInfo()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 481 行

#### `isMusicPlaying()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 469 行

#### `isNotificationListenerEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 502 行

#### `nextMusic()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 487 行

#### `openNotificationListenerSettings()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 507 行

#### `playPause()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 493 行

#### `playPauseMusic()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 484 行

#### `prevMusic()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 490 行

#### `startMusicVisualizer()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 463 行

#### `stopMusicVisualizer()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 466 行

### 应用管理

#### `addQuickApp()`

- **返回值**：`void`
- **参数**：
  - `name`: `String`
  - `packageName`: `String`
  - `iconBase64`: `String`
- **行号**：第 366 行

#### `getAllApps()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 339 行

#### `getAllComponentConfigs()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 577 行

#### `getAppIcon()`

- **返回值**：`String`
- **参数**：
  - `packageName`: `String`
- **行号**：第 351 行

#### `getAppInfo()`

- **返回值**：`String`
- **参数**：
  - `packageName`: `String`
- **行号**：第 348 行

#### `getAppList()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 336 行

#### `getConfigApp()`

- **返回值**：`String`
- **参数**：
  - `buttonId`: `String`
- **行号**：第 382 行

#### `getQuickAppList()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 363 行

#### `getSystemApps()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 345 行

#### `getUserApps()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 342 行

#### `isAppInstalled()`

- **返回值**：`boolean`
- **参数**：
  - `packageName`: `String`
- **行号**：第 354 行

#### `isComponentEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `componentName`: `String`
- **行号**：第 572 行

#### `isQuickApp()`

- **返回值**：`boolean`
- **参数**：
  - `packageName`: `String`
- **行号**：第 374 行

#### `launchApp()`

- **返回值**：`void`
- **参数**：
  - `packageName`: `String`
- **行号**：第 357 行

#### `openAppInfo()`

- **返回值**：`void`
- **参数**：
  - `packageName`: `String`
- **行号**：第 360 行

#### `removeQuickApp()`

- **返回值**：`void`
- **参数**：
  - `packageName`: `String`
- **行号**：第 371 行

#### `saveComponentConfig()`

- **返回值**：`boolean`
- **参数**：
  - `componentName`: `String`
  - `isEnabled`: `boolean`
- **行号**：第 567 行

#### `saveConfigApp()`

- **返回值**：`void`
- **参数**：
  - `buttonId`: `String`
  - `appName`: `String`
  - `packageName`: `String`
  - `appIcon`: `String`
- **行号**：第 377 行

#### `saveSystemLauncherSetting()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 519 行

#### `saveSystemLauncherSettingAsync()`

- **返回值**：`void`
- **参数**：
  - `enabled`: `final boolean`
  - `callbackId`: `final String`
- **行号**：第 524 行

### 壁纸相关

#### `deleteCurrentWallpaper()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 459 行

#### `getRandomWallpaper()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 411 行

#### `getRandomWallpaperAsync()`

- **返回值**：`void`
- **参数**：
  - `callbackId`: `final String`
- **行号**：第 414 行

#### `getRandomWallpaperBase64()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 419 行

#### `getRandomWallpaperBase64Async()`

- **返回值**：`void`
- **参数**：
  - `callbackId`: `final String`
- **行号**：第 424 行

#### `getWallpaperSettings()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 445 行

#### `getWallpaperSettingsAsync()`

- **返回值**：`void`
- **参数**：
  - `callbackId`: `final String`
- **行号**：第 448 行

#### `pauseWallpaperCarousel()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 453 行

#### `resumeWallpaperCarousel()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 456 行

#### `saveWallpaperCarouselSetting()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 386 行

#### `saveWallpaperCarouselSettingAsync()`

- **返回值**：`void`
- **参数**：
  - `enabled`: `final boolean`
  - `callbackId`: `final String`
- **行号**：第 391 行

#### `saveWallpaperSwitchInterval()`

- **返回值**：`boolean`
- **参数**：
  - `interval`: `int`
- **行号**：第 396 行

#### `saveWallpaperSwitchIntervalAsync()`

- **返回值**：`void`
- **参数**：
  - `interval`: `final int`
  - `callbackId`: `final String`
- **行号**：第 401 行

#### `sendWallpaperSettingsChangedBroadcast()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 406 行

#### `updateWallpaperCategories()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 429 行

### ADB相关

#### `executeAdbPermissionGrant()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 554 行

#### `setDefaultDesktopViaAdb()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 563 行

#### `triggerUsbDebugAuthorization()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 548 行

#### `triggerWirelessAdbAuthorization()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 551 行

### 时间日期

#### `getLunarCalendar()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 544 行

#### `updateCategoryEnabled()`

- **返回值**：`void`
- **参数**：
  - `categoryId`: `String`
  - `enabled`: `boolean`
- **行号**：第 432 行

#### `updateCategoryEnabledAsync()`

- **返回值**：`void`
- **参数**：
  - `categoryId`: `final String`
  - `enabled`: `final boolean`
  - `callbackId`: `final String`
- **行号**：第 440 行

### 系统设置

#### `isBluetoothConnected()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 516 行

#### `isWifiConnected()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 513 行

#### `saveBootGreetingSetting()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 529 行

#### `saveRandomModeSetting()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 534 行

#### `saveSpecifiedModeSetting()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 539 行

#### `setBluetoothEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 271 行

#### `setNightMode()`

- **返回值**：`boolean`
- **参数**：
  - `on`: `boolean`
- **行号**：第 261 行

#### `setWifiEnabled()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 266 行

### 状态获取

#### `getEnabledCategories()`

- **返回值**：`String`
- **参数**：无
- **行号**：第 437 行

#### `isCameraOverspeedLimitEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 105 行

#### `isScreenOn()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 330 行

#### `isVehicleLocked()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 325 行

#### `isVideoWhileDrivingEnabled()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 281 行

### 保存/设置

#### `setCameraOverspeedLimit()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 100 行

#### `setVideoWhileDriving()`

- **返回值**：`boolean`
- **参数**：
  - `enabled`: `boolean`
- **行号**：第 276 行

### 其他

#### `openRecentTasks()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 557 行

#### `openRecents()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 560 行

#### `playNext()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 496 行

#### `playPrevious()`

- **返回值**：`void`
- **参数**：无
- **行号**：第 499 行

#### `sendNextTrack()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 319 行

#### `sendPrevTrack()`

- **返回值**：`boolean`
- **参数**：无
- **行号**：第 314 行

---

## 🔄 重新生成

```bash
python3 tools/generate_api_docs.py
```
