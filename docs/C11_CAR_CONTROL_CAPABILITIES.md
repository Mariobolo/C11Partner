# 零跑C11车机控制能力分析文档

## 概述

本文档详细分析零跑C11车机系统的控制能力，包括：
- 可通过Intent主动控制的功能
- 可通过Logcat被动监控的状态
- 可通过Settings.Global读写的系统属性

---

## 一、控制逻辑架构

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

## 二、Intent主动控制功能

### 2.1 已确认可用的Intent

| 功能 | Action | 类型 | 说明 | 状态 |
|------|--------|------|------|------|
| **360全景启动** | `com.leapmotor.camera_around` | Activity | 启动360全景影像 | ✅ 已实现 |
| **空调控制页面** | `com.leapmotor.action.AIR_CONTROL` | Activity | 打开原生空调控制页面 | ✅ 已实现 |
| **方控按键模拟** | `com.leapmotor.action.METER.CTRL` | Broadcast | 模拟方向盘按键 | 🔍 待验证 |
| **语音车控** | `com.leapmotor.speech.tocarcontrol` | Broadcast | 通过语音接口控制车辆 | 🔍 待验证 |
| **讯飞语音控制** | `com.iflytek.autofly.handMessage` | Broadcast | 讯飞语音控制接口 | 🔍 待验证 |

### 2.2 Intent详细说明

#### 360全景启动
```java
Intent intent = new Intent("com.leapmotor.camera_around");
intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
startActivity(intent);
```
- **包名**: `com.leapmotor.camera_around`
- **验证状态**: ✅ 实车验证通过
- **触发条件**: 转向灯、R挡、低速

#### 空调控制页面
```java
Intent intent = new Intent("com.leapmotor.action.AIR_CONTROL");
intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
startActivity(intent);
```
- **验证状态**: ✅ 已实现（备用方案打开设置）
- **说明**: 打开零跑原生空调控制界面

#### 方控按键模拟
```java
Intent intent = new Intent("com.leapmotor.action.METER.CTRL");
intent.putExtra("value", 1);  // 1=上一曲, 2=下一曲
sendBroadcast(intent);
```
- **value值**: 1=上一曲, 2=下一曲
- **验证状态**: 🔍 待验证

#### 语音车控接口
```java
// 儿童锁控制示例
Intent intent = new Intent("com.iflytek.autofly.handMessage");
intent.setPackage("com.leapmotor.leapmotoriflyspeechservice");
intent.putExtra("value", "{\"semantic\":{\"name\":\"儿童锁\",\"operation\":\"CLOSE\",\"service\":\"CAR_CONTROL\"},...}");
sendBroadcast(intent);
```
- **验证状态**: 🔍 待验证
- **可控制项**: 儿童锁、空调、车窗等

---

## 三、Logcat被动监控功能

### 3.1 已确认的CAN信号（C11CarSomeIp）

| 功能 | EventId | 值定义 | TAG | 状态 |
|------|---------|--------|-----|------|
| **档位** | `1110` | 1=R, 2=N, 3=D, 4=P | `D/C11CarSomeIp` | ✅ 已实现 |
| **左前门** | `9123` | 0=关, 1=开 | `D/C11CarSomeIp` | ✅ 已实现 |
| **右前门** | `9124` | 0=关, 1=开 | `D/C11CarSomeIp` | ✅ 已实现 |
| **左后门** | `9125` | 0=关, 1=开 | `D/C11CarSomeIp` | ✅ 已实现 |
| **右后门** | `9126` | 0=关, 1=开 | `D/C11CarSomeIp` | ✅ 已实现 |
| **后备箱** | `9127` | 0=关, 1=开 | `D/C11CarSomeIp` | ✅ 已实现 |
| **前机盖** | `9128` | 0=关, 1=开 | `D/C11CarSomeIp` | ✅ 已实现 |
| **天窗** | `21201` | 0=关, 1=开 | `D/C11CarSomeIp` | ✅ 已实现 |
| **遮阳帘** | `21207` | 3=开, 4=关 | `D/BleControlService` | 🔍 待集成 |
| **锁车状态** | `1200` | 0=解锁, 1=上锁 | `D/C11CarSomeIp` | ✅ 已实现 |

### 3.2 转向灯信号（AroundService）

| 功能 | 日志格式 | TAG | 状态 |
|------|---------|-----|------|
| **左转向灯** | `dealTurnLeftLight mLeftLightSts 1` | `I/AroundService` | ✅ 已实现 |
| **右转向灯** | `dealTurnRightLight mRightLightSts 1` | `I/AroundService` | ✅ 已实现 |

### 3.3 灯光与空调信号（C11CarXml）

| 功能 | 日志格式 | TAG | 状态 |
|------|---------|-----|------|
| **近光灯** | `node_name : Close  setTextContent: 0/1` | `D/C11CarXml` | 🔍 待集成 |
| **空调开关** | `onPageAction: pageId = BottomBar event = 空调开关 value = 0/1` | `D/LPSysUI.AppStatisticsUtil` | 🔍 待集成 |
| **车速** | `node_name : speed  setTextContent: XX` | `D/C11CarXml` | 🔍 待集成 |

### 3.4 页面状态监控（LeapMotorTopTaskHelper）

| 功能 | 日志格式 | TAG | 状态 |
|------|---------|-----|------|
| **空调页面状态** | `空调页面 airState = 1  CAR_CONTROL = 0` | `D/LPSysUI.LeapMotorTopTaskHelper` | 🔍 待集成 |
| **座椅控制页面** | `onPageAction: pageId = BottomBar event = 座椅控制页面 value = 0/1` | `D/LPSysUI.AppStatisticsUtil` | 🔍 待集成 |

---

## 四、Settings.Global 系统属性

### 4.1 可读写的系统属性

| 属性名 | 说明 | 值定义 | 权限 | 状态 |
|--------|------|--------|------|------|
| `camera_overspeed` | 360全景超速限制 | 0=关闭限制, 1=开启限制 | WRITE_SECURE_SETTINGS | 🔍 待验证 |
| `strCarVehicleLock` | 车辆锁状态 | 0=解锁, 1=上锁 | READ_LOGS | 🔍 待验证 |
| `C11_VIDEO_ENABLE` | 行驶中视频播放 | 0=禁止, 1=允许 | WRITE_SECURE_SETTINGS | 🔍 待验证 |

### 4.2 属性详细说明

#### camera_overspeed
- **说明**: 控制360全景是否受速度限制
- **用途**: 任何时速均可开启360全景
- **设置方式**:
```java
Settings.Global.putInt(getContentResolver(), "camera_overspeed", 0);
```

#### strCarVehicleLock
- **说明**: 车辆门锁状态
- **用途**: 监控车辆锁车/解锁状态
- **读取方式**:
```java
String lockState = Settings.Global.getString(getContentResolver(), "strCarVehicleLock");
```

#### C11_VIDEO_ENABLE
- **说明**: 行驶中是否允许视频播放
- **用途**: 解除行驶中视频播放限制
- **设置方式**:
```java
Settings.Global.putInt(getContentResolver(), "C11_VIDEO_ENABLE", 1);
```

---

## 五、功能实现优先级

### 5.1 高优先级（v1.1.0）

| 功能 | 控制方式 | 优先级 | 状态 |
|------|---------|--------|------|
| 360全景自动触发 | Logcat监控 + Intent | 🔴 最高 | ✅ 已完成 |
| 档位状态显示 | Logcat监控 | 🔴 最高 | ✅ 已完成 |
| 车门状态显示 | Logcat监控 | 🔴 高 | ✅ 已完成 |
| 转向灯状态显示 | Logcat监控 | 🔴 高 | ✅ 已完成 |
| 空调控制（打开页面） | Intent | 🔴 高 | ✅ 已完成 |
| 锁车状态显示 | Logcat监控 | 🟡 中 | ✅ 已完成 |

### 5.2 中优先级（v1.2.0）

| 功能 | 控制方式 | 优先级 | 状态 |
|------|---------|--------|------|
| 天窗状态显示 | Logcat监控 | 🟡 中 | 🔲 待开发 |
| 遮阳帘状态显示 | Logcat监控 | 🟡 中 | 🔲 待开发 |
| 近光灯状态显示 | Logcat监控 | 🟡 中 | 🔲 待开发 |
| 车速显示 | Logcat监控 | 🟡 中 | 🔲 待开发 |
| 行驶中视频解禁 | Settings.Global | 🟡 中 | 🔲 待开发 |
| 360全景超速限制解除 | Settings.Global | 🟡 中 | 🔲 待开发 |

### 5.3 低优先级（v1.3.0+）

| 功能 | 控制方式 | 优先级 | 状态 |
|------|---------|--------|------|
| 儿童锁控制 | Intent语音接口 | 🟢 低 | 🔲 待研究 |
| 车窗控制 | Intent语音接口 | 🟢 低 | 🔲 待研究 |
| 座椅加热控制 | Intent语音接口 | 🟢 低 | 🔲 待研究 |
| 氛围灯控制 | 待研究 | 🟢 低 | 🔲 待研究 |
| 驾驶模式切换 | 待研究 | 🟢 低 | 🔲 待研究 |

---

## 六、权限要求

### 6.1 核心权限（必须）

| 权限 | 用途 | 授权方式 |
|------|------|---------|
| `READ_LOGS` | 读取系统日志，监控车辆状态 | ADB授权 |
| `WRITE_SECURE_SETTINGS` | 读写系统全局属性 | ADB授权 |
| `SYSTEM_ALERT_WINDOW` | 悬浮窗显示 | 用户授权 |

### 6.2 ADB授权命令

```bash
# 读取系统日志
adb shell pm grant com.c11partner.desktop android.permission.READ_LOGS

# 写入安全设置
adb shell pm grant com.c11partner.desktop android.permission.WRITE_SECURE_SETTINGS

# DUMP权限
adb shell pm grant com.c11partner.desktop android.permission.DUMP
```

---

## 七、技术实现要点

### 7.1 Logcat监控实现

```java
// 启动logcat进程
Process process = Runtime.getRuntime().exec("logcat -v time");
BufferedReader reader = new BufferedReader(
    new InputStreamReader(process.getInputStream()));

String line;
while ((line = reader.readLine()) != null) {
    // 解析CAN信号
    if (line.contains("C11CarSomeIp") && line.contains("eventId: 1110")) {
        // 解析档位信号
        int gear = parseGearValue(line);
        onGearChanged(gear);
    }
}
```

### 7.2 Settings.Global读写实现

```java
// 读取
int value = Settings.Global.getInt(getContentResolver(), "camera_overspeed", 1);

// 写入
Settings.Global.putInt(getContentResolver(), "camera_overspeed", 0);
```

### 7.3 Intent控制实现

```java
// 启动Activity
Intent intent = new Intent("com.leapmotor.camera_around");
intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
startActivity(intent);

// 发送广播
Intent broadcast = new Intent("com.leapmotor.action.METER.CTRL");
broadcast.putExtra("value", 1);
sendBroadcast(broadcast);
```

---

## 八、注意事项

1. **权限要求**: READ_LOGS和WRITE_SECURE_SETTINGS必须通过ADB授权
2. **实时性**: Logcat监控有秒级延迟，不适合用于高实时性控制
3. **兼容性**: 不同车机版本的日志格式可能有差异
4. **稳定性**: 长时间运行需注意内存泄漏和进程保活
5. **安全性**: 车控功能需谨慎实现，避免误操作

---

## 九、参考资料

- C11日志方案.txt
- MacroDroid配置文件
- 零跑C11实车日志
- 豆包C11接口车控文档.docx
