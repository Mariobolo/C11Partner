# C11Partner 车控接口速查手册

> 🚗 零跑 C11 车控接口完整列表，开发时快速查阅
>
> 最后更新：2026-06-20

---

## 一、三层控制模型总览

```
┌─────────────────────────────────────────────────────────┐
│                    应用层 (C11Partner)                   │
├─────────────────────────────────────────────────────────┤
│  ① Intent主动控制  │  ② Logcat被动监控  │  ③ Settings读写 │
├─────────────────────────────────────────────────────────┤
│                    零跑车机系统层                         │
└─────────────────────────────────────────────────────────┘
```

| 控制方式 | 权限 | 用途 | 数量 |
|---------|------|------|------|
| **Intent 主动控制** | 普通权限 | 启动页面、发送广播 | ~10个 |
| **Logcat 被动监控** | READ_LOGS | 读取车辆状态 | ~20个 |
| **Settings.Global** | WRITE_SECURE_SETTINGS | 读写系统属性 | ~25个 |

---

## 二、Intent 主动控制接口

### 2.1 Activity 启动

| 功能 | Action | 类型 | 状态 | CarControlManager 方法 |
|------|--------|------|------|----------------------|
| **360全景启动** | `com.leapmotor.camera_around` | Activity | ✅ 已实现 | `startCamera360()` |
| **空调控制页面** | `com.leapmotor.action.AIR_CONTROL` | Activity | ✅ 已实现 | - |

### 2.2 广播控制

| 功能 | Action | Extra | 状态 | CarControlManager 方法 |
|------|--------|-------|------|----------------------|
| **方控按键-上一曲** | `com.leapmotor.action.METER.CTRL` | - | ✅ 已实现 | `sendPrevTrack()` |
| **方控按键-下一曲** | `com.leapmotor.action.METER.CTRL` | - | ✅ 已实现 | `sendNextTrack()` |
| **语音车控接口** | `com.iflytek.autofly.handMessage` | - | 🔍 研究中 | `sendVoiceCommand()` |

### 2.3 语音控制（降级方案）

通过讯飞语音接口发送语音指令，作为直接控制的备选方案：

| 功能 | 指令示例 | 说明 |
|------|---------|------|
| 打开空调 | `"打开空调"` | setAcEnabled 失败时降级使用 |
| 关闭空调 | `"关闭空调"` | - |
| 空调风量调到3档 | `"空调风量调到3档"` | setWindLevel 失败时降级使用 |
| 打开除霜 | `"打开除霜"` | setDefrost 使用 |
| 关闭除霜 | `"关闭除霜"` | - |

### 2.4 车控广播（speech.tocarcontrol）

Action: `com.leapmotor.speech.tocarcontrol`

| 功能 | 参数 | 状态 | CarControlManager 方法 |
|------|------|------|----------------------|
| **近光灯** | `CARLIGHT_NEAR_OPEN` / `CARLIGHT_NEAR_CLOSE` | ✅ 已实现 | `setLowBeamLight()` |
| **后雾灯** | `CARLIGHT_REARFOG_OPEN` / `CARLIGHT_REARFOG_CLOSE` | ✅ 已实现 | `setRearFogLight()` |
| **示廓灯** | `CARLIGHT_WIDTH_OPEN` / `CARLIGHT_WIDTH_CLOSE` | ✅ 已实现 | `setPositionLight()` |
| **行人警示** | `CARLIGHT_PEDESTRIAN_OPEN` / `CARLIGHT_PEDESTRIAN_CLOSE` | ✅ 已实现 | `setPedestrianAlert()` |
| **驾驶模式** | `MMI_DRIVER_MODE_SET` + mode值 | ✅ 已实现 | `setDriveMode()` |
| **守护模式** | `GUARD_MODE` | ✅ 已实现 | `setGuardMode()` |
| **小憩模式** | `REST_MODE` | ✅ 已实现 | `setRestMode()` |
| **露营模式** | `CAMPING_MODE` | ✅ 已实现 | `setCampingMode()` |
| **省电模式** | `POWER_SAVE_MODE` | ✅ 已实现 | `setPowerSaveMode()` |
| **哨兵模式** | `SENTINEL_MODE` | ✅ 已实现 | `setSentinelMode()` |

**驾驶模式值**：
| 值 | 模式 |
|----|------|
| 0 | 舒适模式 |
| 1 | 运动模式 |
| 2 | 自定义模式 |
| 3 | 极致模式 |
| 4 | 经济模式 |
| 5 | 零跑模式 |

### 2.5 系统设置广播（speech.tosettings）

Action: `com.leapmotor.speech.tosettings`

| 功能 | 参数 | 状态 | CarControlManager 方法 |
|------|------|------|----------------------|
| **夜间模式** | `mode` | ✅ 已实现 | `setNightMode()` |
| **WiFi** | `wifi` | ✅ 已实现 | `setWifiEnabled()` |
| **蓝牙** | `bluetooth` | ✅ 已实现 | `setBluetoothEnabled()` |

### 2.6 空调广播（speech.toairconditioner）

Action: `com.leapmotor.speech.toairconditioner`

| 功能 | 参数 | 状态 | CarControlManager 方法 |
|------|------|------|----------------------|
| **最大制冷** | `HVACACMAXREQ` | ✅ 已实现 | `setMaxCooling()` |

---

## 三、Logcat 被动监控信号

### 3.1 CAN 信号（C11CarSomeIp）

**TAG**: `D/C11CarSomeIp`  
**格式**: `onMessage  eventId: XXXX value: X`

| 功能 | EventId | 值说明 | 状态 |
|------|---------|--------|------|
| **档位** | 1110 | 1=R挡, 2=N挡, 3=D挡, 4=P挡 | ✅ 已实现 |
| **左前门** | 9123 | 0=关, 1=开 | ✅ 已实现 |
| **右前门** | 9124 | 0=关, 1=开 | ✅ 已实现 |
| **左后门** | 9125 | 0=关, 1=开 | ✅ 已实现 |
| **右后门** | 9126 | 0=关, 1=开 | ✅ 已实现 |
| **后备箱** | 9127 | 0=关, 1=开 | ✅ 已实现 |
| **前机盖** | 9128 | 0=关, 1=开 | ✅ 已实现 |
| **天窗** | 21201 | 0=关, 1=开 | ✅ 已实现 |
| **遮阳帘** | 21207 | 3=开, 4=关 | 🔲 待集成 |
| **锁车状态** | 1200 | 0=解锁, 1=上锁 | ✅ 已实现 |

### 3.2 转向灯信号（AroundService）

**TAG**: `I/AroundService`

| 功能 | 日志格式 | 值说明 | 状态 |
|------|---------|--------|------|
| **左转向灯** | `dealTurnLeftLight mLeftLightSts X` | 1=开, 0=关 | ✅ 已实现 |
| **右转向灯** | `dealTurnRightLight mRightLightSts X` | 1=开, 0=关 | ✅ 已实现 |

### 3.3 灯光/车速信号（C11CarXml）

**TAG**: `D/C11CarXml`

| 功能 | 关键字 | 状态 |
|------|--------|------|
| **近光灯** | `nearLight` | ✅ 已实现 |
| **车速** | `speed` | ✅ 已实现 |
| **空调开关** | - | 🔍 待确认 |

### 3.4 胎压胎温（zza）

**TAG**: `D/zza`  
**关键字**: `TPMSBean`

| 功能 | 说明 | 状态 |
|------|------|------|
| **四轮胎压** | 前左、前右、后左、后右 | ✅ 已实现（简化版） |
| **四轮胎温** | 前左、前右、后左、后右 | 🔲 待集成 |

### 3.5 其他信号

| 功能 | TAG | 关键字 | 状态 |
|------|-----|--------|------|
| **空调页面状态** | `D/LPSysUI.LeapMotorTopTaskHelper` | - | ✅ 已实现 |
| **蓝牙连接** | `I/BtMusicManager` | - | ✅ 已实现 |
| **屏幕状态** | `I/MediaTlog-CtrlService` | - | ✅ 已实现 |
| **GPS位置** | `I/LeapSystemAppService` | - | 🔲 待集成 |
| **多媒体控制** | `I/MediaTlog-CtrlService` | - | 🔍 待集成 |
| **360全景状态** | `D/C11CarSomeIp` | `hideAnimView` | 🔲 待集成 |

---

## 四、Settings.Global 系统属性

### 4.1 360全景相关

| 属性名 | 说明 | 权限 | 状态 | CarControlManager 方法 |
|--------|------|------|------|----------------------|
| `camera_overspeed` | 360全景超速限制 | WRITE_SECURE_SETTINGS | ✅ 已实现 | `setCameraOverspeedLimit()` |

### 4.2 行驶限制

| 属性名 | 说明 | 权限 | 状态 | CarControlManager 方法 |
|--------|------|------|------|----------------------|
| `C11_VIDEO_ENABLE` | 行驶中视频播放 | WRITE_SECURE_SETTINGS | ✅ 已实现 | `setVideoWhileDriving()` |

### 4.3 音量控制

| 属性名 | 说明 | 权限 | 状态 | CarControlManager 方法 |
|--------|------|------|------|----------------------|
| `C11_CALL` | 蓝牙电话音量 | WRITE_SECURE_SETTINGS | ✅ 已实现 | `setCallVolume()` |
| `C11_NAVI` | 导航语音音量 | WRITE_SECURE_SETTINGS | ✅ 已实现 | `setNaviVolume()` |
| `C11_MUSIC` | 媒体/音乐音量 | WRITE_SECURE_SETTINGS | ✅ 已实现 | `setMusicVolume()` |

### 4.4 空调控制

| 属性名 | 说明 | 权限 | 状态 | CarControlManager 方法 |
|--------|------|------|------|----------------------|
| `strCar1409` | 主驾空调温度 | WRITE_SECURE_SETTINGS | ✅ 已实现 | `setDriverTemp()` |
| `strCar1410` | 副驾空调温度 | WRITE_SECURE_SETTINGS | ✅ 已实现 | `setPassengerTemp()` |
| `strCar100006` | 空调界面开关 | WRITE_SECURE_SETTINGS | ✅ 已实现 | `setAcEnabled()` |
| `strCar1411` | 空调风量（推测） | WRITE_SECURE_SETTINGS | ⚠️ 待验证 | `setWindLevel()` |

### 4.5 氛围灯

| 属性名 | 说明 | 权限 | 状态 | CarControlManager 方法 |
|--------|------|------|------|----------------------|
| `strCar1800` | 氛围灯总开关 | WRITE_SECURE_SETTINGS | ✅ 已实现 | `setAmbientLightEnabled()` |
| `strCar8867` | 氛围灯颜色 | WRITE_SECURE_SETTINGS | ✅ 已实现 | `setAmbientLightColor()` |

### 4.6 副屏控制

| 属性名 | 说明 | 权限 | 状态 | CarControlManager 方法 |
|--------|------|------|------|----------------------|
| `display_1_state` | 副屏显示状态 | WRITE_SECURE_SETTINGS | ✅ 已实现 | `setSecondaryScreenEnabled()` |

### 4.7 语音控制

| 属性名 | 说明 | 权限 | 状态 | CarControlManager 方法 |
|--------|------|------|------|----------------------|
| `SPEECH_SPEAK` | 语音播报总开关 | WRITE_SECURE_SETTINGS | ✅ 已实现 | `setSpeechEnabled()` |

### 4.8 状态读取

| 属性名 | 说明 | 权限 | 状态 | CarControlManager 方法 |
|--------|------|------|------|----------------------|
| `strCarVehicleLock` | 车辆锁状态 | READ_LOGS | ✅ 已实现 | `isVehicleLocked()` |
| `leap_screen_state` | 屏幕状态 | READ_LOGS | ✅ 已实现 | `isScreenOn()` |

### 4.9 其他（待验证）

| 属性名 | 说明 | 状态 |
|--------|------|------|
| `HOME_XIAOLING_FLOAT` | 小灵动画开关 | 🔍 待验证 |

---

## 五、CarControlManager 方法速查

### 5.1 360全景

| 方法 | 说明 | 返回值 |
|------|------|--------|
| `startCamera360()` | 启动360全景 | boolean |

### 5.2 灯光控制

| 方法 | 说明 | 参数 |
|------|------|------|
| `setLowBeamLight(boolean on)` | 设置近光灯 | true=开, false=关 |
| `setRearFogLight(boolean on)` | 设置后雾灯 | true=开, false=关 |
| `setPositionLight(boolean on)` | 设置示廓灯 | true=开, false=关 |
| `setPedestrianAlert(boolean on)` | 设置行人警示 | true=开, false=关 |

### 5.3 驾驶/场景模式

| 方法 | 说明 | 参数 |
|------|------|------|
| `setDriveMode(int mode)` | 设置驾驶模式 | 0-5（见上表） |
| `setGuardMode()` | 守护模式 | - |
| `setRestMode()` | 小憩模式 | - |
| `setCampingMode()` | 露营模式 | - |
| `setPowerSaveMode()` | 省电模式 | - |
| `setSentinelMode()` | 哨兵模式 | - |

### 5.4 空调控制

| 方法 | 说明 | 参数 |
|------|------|------|
| `setMaxCooling()` | 最大制冷 | - |
| `setAcEnabled(boolean on)` | 设置空调开关 | true=开, false=关 |
| `isAcEnabled()` | 获取空调状态 | - |
| `setWindLevel(int level)` | 设置空调风量 | 1-?档 |
| `getWindLevel()` | 获取空调风量 | - |
| `setDefrost(boolean on)` | 设置除霜 | true=开, false=关 |
| `setDriverTemp(int temp)` | 设置主驾温度 | 温度值 |
| `getDriverTemp()` | 获取主驾温度 | - |
| `setPassengerTemp(int temp)` | 设置副驾温度 | 温度值 |
| `getPassengerTemp()` | 获取副驾温度 | - |

### 5.5 系统设置

| 方法 | 说明 | 参数 |
|------|------|------|
| `setNightMode(boolean on)` | 设置夜间模式 | true=开, false=关 |
| `setWifiEnabled(boolean on)` | 设置WiFi | true=开, false=关 |
| `setBluetoothEnabled(boolean on)` | 设置蓝牙 | true=开, false=关 |

### 5.6 方控按键

| 方法 | 说明 |
|------|------|
| `sendPrevTrack()` | 上一曲 |
| `sendNextTrack()` | 下一曲 |

### 5.7 音量控制

| 方法 | 说明 | 参数 |
|------|------|------|
| `setCallVolume(int volume)` | 设置通话音量 | 音量值 |
| `getCallVolume()` | 获取通话音量 | - |
| `setNaviVolume(int volume)` | 设置导航音量 | 音量值 |
| `getNaviVolume()` | 获取导航音量 | - |
| `setMusicVolume(int volume)` | 设置音乐音量 | 音量值 |
| `getMusicVolume()` | 获取音乐音量 | - |

### 5.8 氛围灯

| 方法 | 说明 | 参数 |
|------|------|------|
| `setAmbientLightEnabled(boolean on)` | 氛围灯开关 | true=开, false=关 |
| `isAmbientLightEnabled()` | 获取氛围灯状态 | - |
| `setAmbientLightColor(int color)` | 设置氛围灯颜色 | 颜色值 |
| `getAmbientLightColor()` | 获取氛围灯颜色 | - |

### 5.9 360全景/行驶限制

| 方法 | 说明 | 参数 |
|------|------|------|
| `setCameraOverspeedLimit(boolean on)` | 360超速限制 | true=开启限制, false=关闭限制 |
| `isCameraOverspeedLimitEnabled()` | 获取360超速限制状态 | - |
| `setVideoWhileDriving(boolean on)` | 行驶中视频播放 | true=允许, false=禁止 |
| `isVideoWhileDrivingEnabled()` | 获取行驶视频状态 | - |

### 5.10 副屏控制

| 方法 | 说明 | 参数 |
|------|------|------|
| `setSecondaryScreenEnabled(boolean on)` | 副屏开关 | true=开, false=关 |
| `isSecondaryScreenEnabled()` | 获取副屏状态 | - |

### 5.11 语音控制

| 方法 | 说明 | 参数 |
|------|------|------|
| `setSpeechEnabled(boolean on)` | 语音播报开关 | true=开, false=关 |
| `isSpeechEnabled()` | 获取语音状态 | - |
| `sendVoiceCommand(String command)` | 发送语音指令 | 指令文本 |

### 5.12 状态读取

| 方法 | 说明 | 返回值 |
|------|------|--------|
| `isVehicleLocked()` | 车辆是否上锁 | boolean |
| `isScreenOn()` | 屏幕是否亮起 | boolean |

### 5.13 Settings 底层方法

| 方法 | 说明 |
|------|------|
| `getGlobalInt(String key, int def)` | 读取全局整型属性 |
| `getGlobalString(String key)` | 读取全局字符串属性 |
| `setGlobalInt(String key, int value)` | 设置全局整型属性 |
| `setGlobalString(String key, String value)` | 设置全局字符串属性 |

---

## 六、WebViewBridge JS 接口速查（车控相关）

### 6.1 360全景

| JS 方法 | 说明 |
|---------|------|
| `startCamera360()` | 启动360全景 |

### 6.2 快捷开关

| JS 方法 | 说明 |
|---------|------|
| `setLowBeamLight(on)` | 近光灯开关 |
| `setRearFogLight(on)` | 后雾灯开关 |
| `setPositionLight(on)` | 示廓灯开关 |
| `setPedestrianAlert(on)` | 行人警示开关 |
| `setMaxCooling()` | 最大制冷 |
| `setNightMode(on)` | 夜间模式开关 |
| `setWifiEnabled(on)` | WiFi开关 |
| `setBluetoothEnabled(on)` | 蓝牙开关 |
| `setVideoWhileDriving(on)` | 行驶视频开关 |
| `setCameraOverspeedLimit(on)` | 360限速开关 |
| `setAmbientLightEnabled(on)` | 氛围灯开关 |
| `setSpeechEnabled(on)` | 语音播报开关 |
| `setSecondaryScreenEnabled(on)` | 副屏开关 |

### 6.3 驾驶/场景模式

| JS 方法 | 说明 |
|---------|------|
| `setDriveMode(mode)` | 设置驾驶模式 |
| `setGuardMode()` | 守护模式 |
| `setRestMode()` | 小憩模式 |
| `setCampingMode()` | 露营模式 |
| `setPowerSaveMode()` | 省电模式 |
| `setSentinelMode()` | 哨兵模式 |

### 6.4 空调控制

| JS 方法 | 说明 |
|---------|------|
| `setAcEnabled(on)` | 空调开关 |
| `isAcEnabled()` | 获取空调状态 |
| `setWindLevel(level)` | 设置风量 |
| `getWindLevel()` | 获取风量 |
| `setDriverTemp(temp)` | 设置主驾温度 |
| `getDriverTemp()` | 获取主驾温度 |
| `setPassengerTemp(temp)` | 设置副驾温度 |
| `getPassengerTemp()` | 获取副驾温度 |

### 6.5 音量控制

| JS 方法 | 说明 |
|---------|------|
| `setCallVolume(volume)` | 设置通话音量 |
| `getCallVolume()` | 获取通话音量 |
| `setNaviVolume(volume)` | 设置导航音量 |
| `getNaviVolume()` | 获取导航音量 |
| `setMusicVolume(volume)` | 设置音乐音量 |
| `getMusicVolume()` | 获取音乐音量 |

### 6.6 副屏控制

| JS 方法 | 说明 |
|---------|------|
| `showCarStatusPresentation()` | 显示副屏车辆状态 |
| `hidePresentation()` | 隐藏副屏 |
| `isPresentationShowing()` | 副屏是否显示 |
| `updatePresentationTime(time)` | 更新副屏时间 |
| `hasSecondaryDisplay()` | 是否有副屏 |
| `getSecondaryDisplayInfo()` | 获取副屏信息 |

### 6.7 音乐控制

| JS 方法 | 说明 |
|---------|------|
| `getSystemMusicInfo()` | 获取系统音乐信息 |
| `playPauseMusic()` | 播放/暂停 |
| `nextMusic()` | 下一首 |
| `prevMusic()` | 上一首 |
| `isNotificationListenerEnabled()` | 通知监听是否启用 |
| `openNotificationListenerSettings()` | 打开通知监听设置 |

### 6.8 自动化

| JS 方法 | 说明 |
|---------|------|
| `getAllAutomationScenarios()` | 获取所有自动化场景 |
| `setAutomationScenarioEnabled(id, enabled)` | 设置场景开关 |
| `isAutomationScenarioEnabled(id)` | 场景是否启用 |

---

## 七、360全景自动触发条件

| 触发条件 | 日志信号 | 优先级 |
|---------|---------|--------|
| 打左转向灯 | `I/AroundService: dealTurnLeftLight mLeftLightSts 1` | 🔴 最高 |
| 打右转向灯 | `I/AroundService: dealTurnRightLight mRightLightSts 1` | 🔴 最高 |
| 挂R挡（倒车） | `D/C11CarSomeIp: eventId: 1110 value: 1` | 🔴 最高 |
| 低速行驶 | 车速 < 阈值 | 🟡 中 |

---

## 八、验证状态说明

| 状态 | 说明 |
|------|------|
| ✅ 已实现 | 功能已实现并验证可用 |
| ⚠️ 待验证 | 功能已实现，但需要实车验证 |
| 🔍 研究中 | 正在研究接口，尚未实现 |
| 🔲 待集成 | 接口已确认，但还没集成到代码中 |

---

## 九、相关文档

- [架构设计](ARCHITECTURE.md) - 整体架构和模块划分
- [开发指南](DEVELOPMENT_GUIDE.md) - 环境搭建和开发流程
- [代码索引](CODE_INDEX.md) - 函数级快速定位
- [常见问题](FAQ.md) - 常见问题解答

---

**文档版本**：v1.0  
**最后更新**：2026-06-20
