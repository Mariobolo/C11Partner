# 零跑C11车机控制接口完整文档 v2.0

> **适用车型**：零跑C11（2023款及以后，高通8155芯片，安卓9系统）
> **文档用途**：用于开发车机自动化脚本、自定义语音助手、状态监控工具、场景联动功能
> **文档版本**：v2.0（最全面版）
> **最后更新**：2026-06-19（v1.2.0 同步更新）

---

## 目录

1. [控制逻辑架构](#1-控制逻辑架构)
2. [日志事件映射（被动监控）](#2-日志事件映射被动监控)
3. [官方控制接口（主动控制）](#3-官方控制接口主动控制)
4. [语音TTS与语音控制](#4-语音tts与语音控制)
5. [自动化场景实现](#5-自动化场景实现)
6. [权限要求](#6-权限要求)
7. [功能实现优先级](#7-功能实现优先级)
8. [开发注意事项](#8-开发注意事项)

---

## 1. 控制逻辑架构

### 1.1 三层控制模型

```
┌─────────────────────────────────────────────────────────┐
│                    应用层 (C11Partner)                   │
├─────────────────────────────────────────────────────────┤
│  ① Intent主动控制  │  ② Logcat被动监控  │  ③ Settings读写 │
├─────────────────────────────────────────────────────────┤
│                    零跑车机系统层                         │
└─────────────────────────────────────────────────────────┘
```

### 1.2 控制方式说明

| 控制方式 | 说明 | 权限要求 | 实时性 |
|---------|------|---------|--------|
| **Intent主动控制** | 发送系统广播/启动Activity | 普通权限 | 即时 |
| **Logcat被动监控** | 读取系统日志解析状态 | READ_LOGS | 秒级延迟 |
| **Settings.Global** | 读写系统全局属性 | WRITE_SECURE_SETTINGS | 即时 |

---

## 2. 日志事件映射（被动监控）

### 2.1 日志监听基础配置

| 配置项 | 值 | 说明 |
|--------|-----|------|
| **必须监听的日志TAG** | `D/C11CarSomeIp`, `D/LPSysUI.LeapMotorTopTaskHelper`, `D/LPSysUI.AppStatisticsUtil`, `D/C11CarXml`, `D/BleControlService`, `I/TripService`, `I/BtMusicManager`, `I/MediaTlog-CtrlService`, `D/zza`, `I/AroundService` | 所有车辆事件均会从这些TAG输出 |
| 最小日志匹配长度 | 14 | 过滤掉长度小于14的无效日志 |
| 日志匹配方式 | 精确字符串包含匹配 | 不区分大小写，但建议严格匹配空格和符号 |

---

### 2.2 核心车辆状态事件

#### 2.2.1 车门与舱盖状态

| 事件名称 | 日志TAG | 精确匹配字符串 | 事件值 | 含义 | 状态 |
|----------|---------|----------------|--------|------|------|
| 左前门状态变化 | `D/C11CarSomeIp` | `onMessage eventId: 9123 value:` | 1/0 | 打开/关闭 | ✅ 已实现 |
| 右前门状态变化 | `D/C11CarSomeIp` | `onMessage eventId: 9124 value:` | 1/0 | 打开/关闭 | ✅ 已实现 |
| 左后门状态变化 | `D/C11CarSomeIp` | `onMessage eventId: 9125 value:` | 1/0 | 打开/关闭 | ✅ 已实现 |
| 右后门状态变化 | `D/C11CarSomeIp` | `onMessage eventId: 9126 value:` | 1/0 | 打开/关闭 | ✅ 已实现 |
| 后备箱状态变化 | `D/C11CarSomeIp` | `onMessage eventId: 9127 value:` | 1/0 | 打开/关闭 | ✅ 已实现 |
| 前机盖状态变化 | `D/C11CarSomeIp` | `onMessage eventId: 9128 value:` | 1/0 | 打开/关闭 | ✅ 已实现 |

#### 2.2.2 挡位状态

| 事件名称 | 日志TAG | 精确匹配字符串 | 事件值 | 含义 | 状态 |
|----------|---------|----------------|--------|------|------|
| 挡位切换 | `D/C11CarSomeIp` | `onMessage eventId: 1110 value:` | 1/2/3/4 | R挡/N挡/D挡/P挡 | ✅ 已实现 |

#### 2.2.3 车辆锁止状态

| 事件名称 | 日志TAG | 精确匹配字符串 | 事件值 | 含义 | 状态 |
|----------|---------|----------------|--------|------|------|
| 车辆锁止状态变化 | `D/C11CarSomeIp` | `eventid: 1200 msg:` | 0/1 | 解锁/上锁 | ✅ 已实现 |

#### 2.2.4 灯光系统状态

| 事件名称 | 日志TAG | 精确匹配字符串 | 事件值 | 含义 | 备注 | 状态 |
|----------|---------|----------------|--------|------|------|------|
| 近光灯状态 | `D/C11CarXml` | `node_name : Close setTextContent:` | 0/1 | 开启/关闭 | ⚠️ 反向逻辑（0=开，1=关） | ✅ 已实现 |
| 左转灯状态 | `D/C11CarSomeIp` | `onMessage eventId: 9106 value:` | 1/0 | 开启/关闭 | | 🔍 待集成 |
| 右转灯状态 | `D/C11CarSomeIp` | `onMessage eventId: 9107 value:` | 1/0 | 开启/关闭 | | 🔍 待集成 |
| 左转向灯（AroundService） | `I/AroundService` | `dealTurnLeftLight mLeftLightSts` | 1/0 | 开启/关闭 | | ✅ 已实现 |
| 右转向灯（AroundService） | `I/AroundService` | `dealTurnRightLight mRightLightSts` | 1/0 | 开启/关闭 | | ✅ 已实现 |

#### 2.2.5 天窗与遮阳帘状态

| 事件名称 | 日志TAG | 精确匹配字符串 | 事件值 | 含义 | 状态 |
|----------|---------|----------------|--------|------|------|
| 遮阳帘状态 | `D/BleControlService` | `onMessage eventId: 21207 value:` | 3/4 | 完全打开/完全关闭 | 🔍 待集成 |
| 天窗状态 | `D/C11CarSomeIp` | `onMessage eventId: 21201 value:` | 3/4 | 完全打开/完全关闭 | ✅ 已实现 |

#### 2.2.6 多媒体控制事件

| 事件名称 | 日志TAG | 精确匹配字符串 | 含义 | 状态 |
|----------|---------|----------------|------|------|
| 音乐暂停/播放 | `I/MediaTlog-CtrlService` | `Recive wheelService mute: 1` | 音乐暂停/播放 | 🔍 待集成 |
| 上一首 | `I/BtMusicManager` | `Recive wheelService music: 1` | 蓝牙连接时触发 | 🔍 待集成 |
| 下一首 | `I/BtMusicManager` | `Recive wheelService music: 2` | 蓝牙连接时触发 | 🔍 待集成 |
| 途记开始录制 | `I/TripService` | `startMp4Record` | 行车记录仪开始录制 | 🔍 待集成 |

---

### 2.3 扩展车辆状态事件

#### 2.3.1 车辆硬件状态

| 事件名称 | 日志TAG | 精确匹配字符串 | 数据格式 | 含义 | 状态 |
|----------|---------|----------------|----------|------|------|
| 轮胎压力温度更新 | `D/zza` | `TPMSBean{pos=` | pos=X, singleTirePress=XXX, singleTireTemp=XX | 四轮胎压胎温 | ✅ 已实现 |
| GPS位置更新 | `I/LeapSystemAppService` | `### gpsLocationMsg` | [时间戳],1,[纬度],[经度],[海拔] | GPS定位信息 | 🔍 待集成 |
| 车速 | `D/C11CarXml` | `node_name : speed setTextContent:` | 数值（km/h） | 当前车速 | ✅ 已实现 |

#### 2.3.2 360环视状态

| 事件名称 | 日志TAG | 精确匹配字符串 | 含义 | 状态 |
|----------|---------|----------------|------|------|
| 360环视隐藏 | `D/C11CarSomeIp` | `hideAnimView` | 360环视界面关闭 | 🔍 待集成 |
| 360方向盘按键 | `D/C11CarSomeIp` | `onMessage WHEEL_360_ID value: 48` | 方向盘360按键 | 🔍 待集成 |

---

### 2.4 系统与应用状态事件

| 事件名称 | 日志TAG | 精确匹配字符串 | 含义 | 状态 |
|----------|---------|----------------|------|------|
| 屏幕点亮 | `I/MediaTlog-CtrlService` | `Recive the screen on` | 屏幕点亮 | ✅ 已实现 |
| 蓝牙连接状态变化 | `I/BtMusicManager` | `isA2dpConneted:` | true=已连接,false=未连接 | ✅ 已实现 |
| 多媒体服务连接 | `D/SmartDockService` | `onServiceConnected: com.leapmotor.mediac11.MediaService` | 多媒体服务连接成功 | 🔍 待集成 |
| 当前播放音乐信息 | `D/SmartDockAdapter` | `showMusicItem: defaultMusicName =` | 当前播放歌曲名和歌手 | 🔍 待集成 |
| 空调页面状态 | `D/LPSysUI.LeapMotorTopTaskHelper` | `空调页面 airState = 1` | 空调页面开关 | ✅ 已实现 |
| 座椅控制页面 | `D/LPSysUI.AppStatisticsUtil` | `pageId = BottomBar event = 座椅控制页面` | 座椅页面开关 | 🔍 待集成 |

---

## 3. 官方控制接口（主动控制）

### 3.1 Settings.Global 全局属性控制

#### 3.1.1 通用系统属性

| 属性名 | 数据类型 | 取值范围 | 功能说明 | 状态 |
|--------|----------|----------|----------|------|
| `SPEECH_SPEAK` | int | 0/1 | 语音播报总开关 | ✅ 已实现 |
| `display_1_state` | int | 0/1 | 副屏显示状态 | ✅ 已实现 |
| `HOME_XIAOLING_FLOAT` | int | 0/1 | 小灵动画开关 | ✅ 已实现 |
| `leap_screen_state` | int | 0/1 | 屏幕状态（0=点亮，1=熄灭） | ✅ 已实现 |
| `strCarVehicleLock` | String | 0/1 | 车辆锁状态（0=解锁，1=上锁） | ✅ 已实现 |
| `camera_overspeed` | int | 0/1 | 360全景超速限制（0=关闭限制） | ✅ 已实现 |
| `C11_VIDEO_ENABLE` | int | 0/1 | 行驶中视频播放限制（1=允许） | ✅ 已实现 |

#### 3.1.2 音量控制属性

| 属性名 | 数据类型 | 取值范围 | 功能说明 | 状态 |
|--------|----------|----------|----------|------|
| `C11_CALL` | int | 0-100 | 蓝牙电话音量 | ✅ 已实现 |
| `C11_NAVI` | int | 0-100 | 导航语音音量 | ✅ 已实现 |
| `C11_MUSIC` | int | 0-100 | 媒体/音乐音量 | ✅ 已实现 |

#### 3.1.3 空调控制属性

| 属性名 | 数据类型 | 取值范围 | 功能说明 | 状态 |
|--------|----------|----------|----------|------|
| `strCar1409` | int | 16-30 | 主驾空调温度（℃） | ✅ 已实现 |
| `strCar1410` | int | 16-30 | 副驾空调温度（℃） | ✅ 已实现 |
| `strCar100006` | int | 0/1 | 空调界面开关 | ✅ 已实现 |

#### 3.1.4 氛围灯控制属性

| 属性名 | 数据类型 | 取值范围 | 功能说明 | 状态 |
|--------|----------|----------|----------|------|
| `strCar1800` | int | 0/1 | 氛围灯总开关 | ✅ 已实现 |
| `strCar8867` | int | 0-16 | 氛围灯颜色 | ✅ 已实现 |

**氛围灯颜色对照表：**

| 值 | 颜色 |
|----|------|
| 0 | 红色 |
| 1 | 橙色 |
| 3 | 黄色 |
| 7 | 绿色 |
| 10 | 青色 |
| 14 | 蓝色 |
| 16 | 紫色 |

**Java调用示例：**
```java
// 写入属性
Settings.Global.putInt(context.getContentResolver(), "属性名", 数值);
// 读取属性
int value = Settings.Global.getInt(context.getContentResolver(), "属性名", 默认值);
```

**ADB命令示例：**
```bash
adb shell settings put global 属性名 数值
adb shell settings get global 属性名
```

---

### 3.2 IVI 系统广播控制

#### 3.2.1 空调控制广播

| 广播Action | Extra参数 | 取值 | 功能说明 | 状态 |
|------------|-----------|------|----------|------|
| `com.leapmotor.speech.toairconditioner` | `HVACACMAXREQ` | 1 | 开启最大制冷模式 | ✅ 已实现 |
| `com.leapmotor.speech.toairconditioner` | `HVACACMAXREQ` | 0 | 关闭最大制冷模式 | ✅ 已实现 |

#### 3.2.2 系统设置控制广播

| 广播Action | Extra参数 | 取值 | 功能说明 | 状态 |
|------------|-----------|------|----------|------|
| `com.leapmotor.speech.tosettings` | `mode` | 0 | 切换为夜间模式 | ✅ 已实现 |
| `com.leapmotor.speech.tosettings` | `mode` | 1 | 切换为白天模式 | ✅ 已实现 |
| `com.leapmotor.speech.tosettings` | `setting` | 1 | 打开系统设置页面 | ✅ 已实现 |
| `com.leapmotor.speech.tosettings` | `wifi` | 1 | 打开WiFi开关 | ✅ 已实现 |
| `com.leapmotor.speech.tosettings` | `wifi` | 0 | 关闭WiFi开关 | ✅ 已实现 |
| `com.leapmotor.speech.tosettings` | `bluetooth` | 1 | 打开蓝牙开关 | ✅ 已实现 |
| `com.leapmotor.speech.tosettings` | `bluetooth` | 0 | 关闭蓝牙开关 | ✅ 已实现 |

#### 3.2.3 车辆灯光控制广播

| 广播Action | Extra参数 | 取值 | 功能说明 | 状态 |
|------------|-----------|------|----------|------|
| `com.leapmotor.speech.tocarcontrol` | `CARLIGHT_JINGUANG` | 1/0 | 打开/关闭近光灯 | ✅ 已实现 |
| `com.leapmotor.speech.tocarcontrol` | `CARLIGHT_REARFOGCTL` | 1/0 | 打开/关闭后雾灯 | ✅ 已实现 |
| `com.leapmotor.speech.tocarcontrol` | `CARLIGHT_SHEKUODENG` | 1/0 | 打开/关闭示廓灯 | ✅ 已实现 |
| `com.leapmotor.speech.tocarcontrol` | `PEDESTRIANS_ALERT` | 1/0 | 打开/关闭行人警示音（低速警示音） | ✅ 已实现 |

#### 3.2.4 驾驶模式控制广播

| 广播Action | Extra参数 | 取值 | 功能说明 | 状态 |
|------------|-----------|------|----------|------|
| `com.leapmotor.speech.tocarcontrol` | `MMI_DRIVER_MODE_SET` | 0 | 舒适模式 | ✅ 已实现 |
| `com.leapmotor.speech.tocarcontrol` | `MMI_DRIVER_MODE_SET` | 1 | 运动模式 | ✅ 已实现 |
| `com.leapmotor.speech.tocarcontrol` | `MMI_DRIVER_MODE_SET` | 2 | 自定义模式 | ✅ 已实现 |
| `com.leapmotor.speech.tocarcontrol` | `MMI_DRIVER_MODE_SET` | 3 | 极致模式（推测） | ✅ 已实现 |
| `com.leapmotor.speech.tocarcontrol` | `MMI_DRIVER_MODE_SET` | 4 | 经济模式 | ✅ 已实现 |
| `com.leapmotor.speech.tocarcontrol` | `MMI_DRIVER_MODE_SET` | 5 | 零跑模式（推测） | ✅ 已实现 |

#### 3.2.5 场景模式控制广播

| 广播Action | Extra参数 | 取值 | 功能说明 | 状态 |
|------------|-----------|------|----------|------|
| `com.leapmotor.speech.tocarcontrol` | `GUARD_MODE` | 1/0 | 开启/关闭守护模式 | ✅ 已实现 |
| `com.leapmotor.speech.tocarcontrol` | `REST_MODE` | 1/0 | 开启/关闭小憩模式 | ✅ 已实现 |
| `com.leapmotor.speech.tocarcontrol` | `EXPERIENCE_MODE` | 1/0 | 开启/关闭体验模式 | ✅ 已实现 |
| `com.leapmotor.speech.tocarcontrol` | `CAMPING_MODE` | 1/0 | 开启/关闭露营模式 | ✅ 已实现 |
| `com.leapmotor.speech.tocarcontrol` | `POWER_SAVE_MODE` | 1/0 | 开启/关闭省电模式 | ✅ 已实现 |
| `com.leapmotor.speech.tocarcontrol` | `SENTINEL_MODE` | 1/0 | 开启/关闭哨兵模式 | ✅ 已实现 |

#### 3.2.6 其他控制广播

| 广播Action | Extra参数 | 取值 | 功能说明 | 状态 |
|------------|-----------|------|----------|------|
| `com.leapmotor.speech.tocarcontrol` | `LAUNCH` | 1/0 | 启动/设置（待确认） | ✅ 已实现 |
| `com.leapmotor.speech.tojourney` | `journey` | 1 | 当前能耗（待确认） | ✅ 已实现 |
| `com.leapmotor.speech.tojourney` | `healthy` | 1 | 当前状态（待确认） | ✅ 已实现 |

**Java调用示例：**
```java
Intent intent = new Intent("广播Action");
intent.putExtra("Extra参数名", 数值);
context.sendBroadcast(intent);
```

**ADB命令示例：**
```bash
adb shell am broadcast -a 广播Action --ei Extra参数名 数值
```

---

### 3.3 DC 广播常量定义对照表

| 常量名 | 完整Action字符串值 | 功能分类 |
|--------|-------------------|----------|
| `SEND_SETTING_ACTION` | `com.leapmotor.speech.setting` | 语音设置 |
| `SEND_TO_AIR_CONDITIONER_ACTION` | `com.leapmotor.speech.toairconditioner` | 空调控制 |
| `SEND_TO_AUTONAVI_ACTION` | `com.leapmotor.speech.toautonavi` | 导航控制 |
| `SEND_TO_BACK_TO_HOME_ACTION` | `com.leapmotor.speech.backtohome` | 返回桌面 |
| `SEND_TO_CAR_CONTROL_ACTION` | `com.leapmotor.speech.tocarcontrol` | 整车控制 |
| `SEND_TO_DRIVERECORD_ACTION` | `com.leapmotor.speech.todriverecord` | 行车记录仪 |
| `SEND_TO_IQIYI_ACTION` | `com.leapmotor.speech.toiqiyi` | 爱奇艺 |
| `SEND_TO_JOURNEY_ACTION` | `com.leapmotor.speech.tojourney` | 行程信息 |
| `SEND_TO_LECHENG_ACTION` | `com.leapmotor.speech.tolecheng` | 乐橙 |
| `SEND_TO_MEDIA_ACTION` | `com.leapmotor.speech.tomedia` | 媒体控制 |
| `SEND_TO_PHONE_ACTION` | `com.leapmotor.speech.tophone` | 电话 |
| `SEND_TO_SETTINGS_ACTION` | `com.leapmotor.speech.tosettings` | 系统设置 |
| `SEND_TO_SPEECH_ACTION` | `com.iflytek.autofly.sendToSpeech.message` | 讯飞语音交互 |
| `SEND_VOICE_INTERACTION_ACTION` | `com.iflytek.aufofly.warmtip` | 语音互动 |

---

### 3.4 Activity 启动控制

| 功能 | Action / ComponentName | 类型 | 说明 | 状态 |
|------|------------------------|------|------|------|
| **360全景启动** | `com.leapmotor.camera_around` | Activity | 启动360全景影像 | ✅ 已实现 |
| **空调控制页面** | `com.leapmotor.action.AIR_CONTROL` | Activity | 打开原生空调控制页面 | ✅ 已实现 |
| **空调精确页面** | `com.leapmotor.carcontrol/.presentation.ui.aircontrol.AirControlActivity` | Activity | 精确空调页面 | ✅ 已实现 |
| **方控按键模拟** | `com.leapmotor.action.METER.CTRL` | Broadcast | 模拟方向盘按键 | ✅ 已实现 |

**360全景启动示例：**
```java
Intent intent = new Intent("com.leapmotor.camera_around");
intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
startActivity(intent);
```

---

## 4. 语音TTS与语音控制

### 4.1 自由语音文本输出（TTS）

**Java调用示例：**
```java
Intent intent = new Intent("com.iflytek.autofly.TtsService");
intent.setClassName(
    "com.iflytek.cutefly.speechclient.hmi",
    "com.iflytek.autofly.voicecoreservice.tts.TtsService"
);
intent.putExtra("operation", "PLAY");
intent.putExtra("text", "自定义语音内容");
intent.putExtra("package", "leap");
intent.putExtra("priority", "high");
intent.putExtra("streamType", "3");
intent.putExtra("audioFocusDurationHint", "forever");
context.startService(intent);
```

### 4.2 讯飞语音车控接口

**儿童锁控制示例：**
```java
Intent intent = new Intent("com.iflytek.autofly.handMessage");
intent.setPackage("com.leapmotor.leapmotoriflyspeechservice");
intent.putExtra("value", "{\"semantic\":{\"name\":\"儿童锁\",\"operation\":\"OPEN\",\"service\":\"CAR_CONTROL\"},\"focus\":\"carControl\",\"messageType\":\"REQUEST\",\"needResponse\":\"YES\",\"operationApp\":\"speech\",\"protocolId\":0,\"requestCode\":\"10039\",\"statusCode\":0,\"version\":\"v1.0\"}");
context.sendBroadcast(intent);
```

**可控制项（待验证）：**
- 儿童锁（OPEN/CLOSE）
- 空调控制
- 车窗控制
- 座椅加热

---

## 5. 自动化场景实现

### 5.1 Logcat 事件监听与控制

| 场景名称 | 触发条件 | 执行动作 | 功能说明 | 状态 |
|----------|----------|----------|----------|------|
| D档语音提示 | Logcat: `eventId: 1110 value: 3` | 发送TTS广播："起飞！" | 挂入D挡时语音提示 | 💡 可实现 |
| R档语音提示 | Logcat: `eventId: 1110 value: 1` | 发送TTS广播："倒车请注意" | 挂入R挡时语音提示 | 💡 可实现 |
| 转向灯自动开360 | Logcat: 转向灯开启 | 启动360环视 → 延迟600ms → 点击对应方向摄像头 | 打转向灯自动打开360环视 | ✅ 已实现 |
| 转向灯自动关360 | Logcat: 转向灯关闭 | 如果360在前台 → 发送返回键 | 转向灯关闭后自动关闭360 | 💡 可实现 |
| 降速自动360 | Logcat: `speed < 10km/h` | 启动360环视 → 点击前视摄像头 | 车速低于10km/h自动打开360 | 💡 可实现 |
| 360方向盘按键 | Logcat: `WHEEL_360_ID value: 48` | 启动360环视 → 点击前视摄像头 | 解除时速限制 | 💡 可实现 |

### 5.2 安全与提示功能

| 场景名称 | 触发条件 | 执行动作 | 功能说明 | 状态 |
|----------|----------|----------|----------|------|
| 童锁联动开 | 系统属性 `strCarVehicleLock=1` | 发送广播：儿童锁开启 | 车辆上锁后自动开启童锁 | 💡 可实现 |
| 童锁联动关 | 系统属性 `strCarVehicleLock=0` | 发送广播：儿童锁关闭 | 车辆解锁后自动关闭童锁 | 💡 可实现 |
| 锁车音效 | 系统属性 `leap_screen_state=1` | 停止音频 → 播放自定义音效 → 延迟5秒 → 恢复音量 | 自定义锁车提示音 | 💡 可实现 |

### 5.3 实用联动功能

| 场景名称 | 触发条件 | 执行动作 | 功能说明 | 状态 |
|----------|----------|----------|----------|------|
| 悬浮按钮-返回键 | 点击悬浮按钮 | 发送返回键命令 | 虚拟返回键，适配副屏操作 | 💡 可实现 |
| 悬浮按钮-副屏主页键 | 点击悬浮按钮 | 执行Shell命令启动副屏桌面 | 一键启动副屏桌面 | 💡 可实现 |
| 定时清理日志 | 每2分钟 | 清除日志 → `adb shell logcat -c -b all` | 定期清理日志 | 💡 可实现 |

---

## 6. 权限要求

### 6.1 核心权限（必须）

| 权限 | 用途 | 授权方式 |
|------|------|---------|
| `READ_LOGS` | 读取系统日志，监控车辆状态 | ADB授权 |
| `WRITE_SECURE_SETTINGS` | 读写系统全局属性 | ADB授权 |
| `DUMP` | 获取系统状态、内存、CPU、Activity信息 | ADB授权 |
| `SYSTEM_ALERT_WINDOW` | 悬浮窗显示 | 用户授权 |

### 6.2 ADB 授权命令

```bash
# 1. 读取系统日志（用于车机状态监控、转向灯/档位监听、广播捕获）
adb shell pm grant com.c11partner.desktop android.permission.READ_LOGS

# 2. DUMP权限（获取系统状态、内存、CPU、Activity信息）
adb shell pm grant com.c11partner.desktop android.permission.DUMP

# 3. 写入系统设置（车控功能、Settings.Global属性、系统Intent发送）
adb shell pm grant com.c11partner.desktop android.permission.WRITE_SECURE_SETTINGS
```

> ⚠️ **重要**：这三个权限**无法普通申请**，必须通过ADB授权。没有权限时功能要优雅降级，不能崩溃。

---

## 7. 功能实现优先级

### 7.1 高优先级（v1.1.0）

| 功能 | 控制方式 | 优先级 | 状态 |
|------|---------|--------|------|
| 360全景自动触发 | Logcat监控 + Intent | 🔴 最高 | ✅ 已完成 |
| 档位状态显示 | Logcat监控 | 🔴 最高 | ✅ 已完成 |
| 车门状态显示 | Logcat监控 | 🔴 高 | ✅ 已完成 |
| 转向灯状态显示 | Logcat监控 | 🔴 高 | ✅ 已完成 |
| 空调控制（打开页面） | Intent | 🔴 高 | ✅ 已完成 |
| 锁车状态显示 | Logcat监控 | 🟡 中 | ✅ 已完成 |
| 天窗状态显示 | Logcat监控 | 🟡 中 | ✅ 已完成 |

### 7.2 中优先级（v1.2.0）

| 功能 | 控制方式 | 优先级 | 状态 |
|------|---------|--------|------|
| 遮阳帘状态显示 | Logcat监控 | 🟡 中 | 🔲 待开发 |
| 近光灯状态显示 | Logcat监控 | 🟡 中 | ✅ 已完成 |
| 车速显示 | Logcat监控 | 🟡 中 | ✅ 已完成 |
| 胎压胎温显示 | Logcat监控 | 🟡 中 | ✅ 已完成 |
| 行驶中视频解禁 | Settings.Global | 🟡 中 | ✅ 已完成 |
| 360全景超速限制解除 | Settings.Global | 🟡 中 | ✅ 已完成 |
| 音量控制 | Settings.Global | 🟡 中 | 🔲 待开发 |
| 氛围灯控制 | Settings.Global | 🟡 中 | 🔲 待开发 |

### 7.3 低优先级（v1.3.0+）

| 功能 | 控制方式 | 优先级 | 状态 |
|------|---------|--------|------|
| 儿童锁控制 | Intent语音接口 | 🟢 低 | 🔲 待研究 |
| 车窗控制 | Intent语音接口 | 🟢 低 | 🔲 待研究 |
| 座椅加热控制 | Intent语音接口 | 🟢 低 | 🔲 待研究 |
| 驾驶模式切换 | 系统广播 | 🟢 低 | ✅ 已完成 |
| 场景模式控制 | 系统广播 | 🟢 低 | 🔲 待研究 |
| 灯光控制 | 系统广播 | 🟢 低 | 🔲 待研究 |
| 自定义TTS语音 | 讯飞TTS服务 | 🟢 低 | ✅ 已完成 |
| 副屏控制 | Settings.Global | 🟢 低 | ✅ 已完成 |

---

## 8. 开发注意事项

### 8.1 日志匹配精度
- 所有事件必须使用精确字符串包含匹配，注意空格和大小写
- 同一状态变化可能会从多个TAG输出，建议做防抖处理（300ms内只处理一次）

### 8.2 反向逻辑
- 近光灯状态为反向逻辑（0=开，1=关），开发时必须注意转换

### 8.3 重复事件处理
- 同一状态变化可能会从多个TAG输出
- 建议做防抖处理（300ms内只处理一次）
- 避免重复触发同一动作

### 8.4 权限要求
- 监听Logcat需要 `android.permission.READ_LOGS` 权限
- 发送系统广播需要零跑专属广播权限
- 读写Settings.Global需要 `WRITE_SECURE_SETTINGS` 权限
- 没有权限时功能要优雅降级，提示用户但不崩溃

### 8.5 兼容性
- 不同车机系统版本的日志格式可能略有差异，建议增加容错处理
- minSdk 25，targetSdk 30，不要使用API 28以上的新特性
- AndroidX完全兼容Android 9，放心使用

### 8.6 WebView性能
- 车机WebView性能有限，比手机差很多
- 避免过多DOM操作和复杂动画
- 图片资源要优化大小，不要太大
- 使用硬件加速
- 避免频繁重绘

### 8.7 稳定性
- 长时间运行需注意内存泄漏和进程保活
- 前台Service保活，不被系统杀死
- 3秒冷却防抖，避免频繁触发360全景

### 8.8 安全性
- 车控功能需谨慎实现，避免误操作
- 所有车控信号必须来自实车抓取，不得臆造
- 不确定的功能标记为「待验证」

---

## 9. 参考资料

- 实车日志文件（logs_docs/目录）
- MacroDroid配置文件
- 豆包C11接口车控文档
- 豆包C11日志车控文档
- 零跑车控提取2.0.xlsx

---

> **文档说明**：本文档整合了多个来源的车机控制接口信息，是目前最全面的版本。
>
> **状态标记说明**：
> - ✅ 已实现：已在C11Partner项目中实现
> - 🔍 待验证：接口已确认，但未在实车验证
> - 🔲 待开发：规划中，尚未实现
> - 💡 可实现：技术上可行，可作为场景功能实现
