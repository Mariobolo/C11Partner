# 零跑C11 实车日志分析报告

**基于实车抓取日志的完整车控信号分析**

---

## 📋 核心发现总结

### ✅ **已确认的CAN信号EventId**

| 功能 | EventId | 值 | 说明 |
|------|---------|-----|------|
| **档位** | `1110` | `1`=R挡, `2`=N挡, `3`=D挡, `4`=P挡 | 档位切换信号 |
| **左前门** | `9123` | `0`=关, `1`=开 | 车门状态 |
| **右前门** | `9124` | `0`=关, `1`=开 | 车门状态 |
| **左后门** | `9125` | `0`=关, `1`=开 | 车门状态 |
| **右后门** | `9126` | `0`=关, `1`=开 | 车门状态 |
| **后备箱** | `9127` | `0`=关, `1`=开 | 尾门状态 |
| **前机盖** | `9128` | `0`=关, `1`=开 | 前备箱状态 |
| **天窗** | `21201` | `0`=关, `1`=开 | 天窗状态 |
| **遮阳帘** | `21207` | `3`=开, `4`=关 | 遮阳帘状态 |
| **锁车** | `1200` | `0`=解锁, `1`=上锁 | 车辆锁状态 |

---

### ✅ **已确认的日志TAG**

| TAG | 用途 |
|-----|------|
| `D/C11CarSomeIp` | **核心CAN信号** - 车门、档位、天窗、锁车 |
| `I/AroundService` | **转向灯信号** - 左/右转向灯状态 |
| `D/C11CarXml` | **灯光/空调** - 近光灯、空调开关 |
| `D/LPSysUI.AppStatisticsUtil` | **页面操作** - 空调页面、座椅页面 |
| `D/LPSysUI.LeapMotorTopTaskHelper` | **页面状态** - 空调页面开关 |
| `D/BleControlService` | **遮阳帘** - 遮阳帘控制 |
| `I/TripService` | **途记/行车记录** - 录像启动 |

---

## 🔴 **转向灯信号（360全景触发关键）**

### 左转向灯
```
I/AroundService: dealTurnLeftLight mLeftLightSts 1   // 左转向灯开
I/AroundService: dealTurnLeftLight mLeftLightSts 0   // 左转向灯关
```

### 右转向灯
```
I/AroundService: dealTurnRightLight mRightLightSts 1  // 右转向灯开
I/AroundService: dealTurnRightLight mRightLightSts 0  // 右转向灯关
```

> 💡 **关键**：这是触发360全景的核心信号！

---

## 🅿️ **档位信号（360全景触发关键）**

**日志格式**：
```
D/C11CarSomeIp: onMessage  eventId: 1110 value: X
```

| 值 | 档位 | 触发动作 |
|----|------|---------|
| `1` | R挡（倒车） | ✅ **自动触发360全景** |
| `2` | N挡（空挡） | - |
| `3` | D挡（前进） | - |
| `4` | P挡（驻车） | - |

> 💡 **关键**：R挡时自动触发360全景！

---

## 🚪 **车门信号**

**日志格式**：
```
D/C11CarSomeIp: onMessage  eventId: XXXX value: 0/1
```

| EventId | 车门 | value=0 | value=1 |
|---------|------|---------|---------|
| `9123` | 左前门 | 关 ✅ | 开 ⚠️ |
| `9124` | 右前门 | 关 ✅ | 开 ⚠️ |
| `9125` | 左后门 | 关 ✅ | 开 ⚠️ |
| `9126` | 右后门 | 关 ✅ | 开 ⚠️ |
| `9127` | 后备箱 | 关 ✅ | 开 ⚠️ |
| `9128` | 前机盖 | 关 ✅ | 开 ⚠️ |

---

## 🔒 **锁车信号**

```
D/C11CarSomeIp: eventid: 1200 msg:0  // 解锁
D/C11CarSomeIp: eventid: 1200 msg:1  // 上锁
```

---

## 🌤️ **天窗与遮阳帘**

### 天窗
```
D/C11CarSomeIp: onMessage  eventId: 21201 value: 0  // 天窗关
D/C11CarSomeIp: onMessage  eventId: 21201 value: 1  // 天窗开
```

### 遮阳帘
```
D/BleControlService: onMessage  eventId: 21207 value: 3  // 遮阳帘开
D/BleControlService: onMessage  eventId: 21207 value: 4  // 遮阳帘关
```

---

## 💨 **空调控制**

### 空调开关
```
D/C11CarXml: onPageAction: pageId = BottomBar event = 空调开关 value = 1  // 开
D/LPSysUI.AppStatisticsUtil: onPageAction: pageId = BottomBar event = 空调开关 value = 0  // 关
```

### 空调页面状态
```
D/LPSysUI.LeapMotorTopTaskHelper: 空调页面 airState = 1  CAR_CONTROL = 0  // 空调页面打开
D/LPSysUI.LeapMotorTopTaskHelper: 空调页面 airState = 0  CAR_CONTROL = 0  // 空调页面关闭
```

---

## 💡 **灯光控制**

### 近光灯
```
D/C11CarXml: node_name : Close  setTextContent: 0  // 近光灯开
D/C11CarXml: node_name : Close  setTextContent: 1  // 近光灯关
```

---

## 🎯 **360全景触发条件（已验证）**

### 自动触发360全景的场景：

| 触发条件 | 日志信号 | 优先级 |
|---------|---------|--------|
| 1️⃣ **打左转向灯** | `I/AroundService: dealTurnLeftLight mLeftLightSts 1` | 🔴 最高 |
| 2️⃣ **打右转向灯** | `I/AroundService: dealTurnRightLight mRightLightSts 1` | 🔴 最高 |
| 3️⃣ **挂R挡（倒车）** | `D/C11CarSomeIp: eventId: 1110 value: 1` | 🔴 最高 |
| 4️⃣ **车速 < 15km/h** | *待确认* | 🟡 中 |

---

## 📊 **日志监控系统设计方案**

### 监控TAG列表（需监听）
```java
String[] MONITOR_TAGS = {
    "C11CarSomeIp",      // CAN信号核心
    "AroundService",     // 转向灯
    "C11CarXml",         // 灯光/空调
    "LPSysUI",           // 页面状态
    "BleControlService"  // 遮阳帘
};
```

### 核心监控逻辑
```java
// 1. 转向灯监控 → 触发360全景
if (log.contains("dealTurnLeftLight mLeftLightSts 1") || 
    log.contains("dealTurnRightLight mRightLightSts 1")) {
    startAroundView();  // 启动360全景
}

// 2. 倒车档监控 → 触发360全景
if (log.contains("eventId: 1110 value: 1")) {
    startAroundView();  // 启动360全景
}

// 3. 车门状态监控 → 语音提示
if (log.contains("eventId: 9123 value: 1")) {
    playAudio("左前门已打开");
}
```

---

## 🚀 **下一步开发计划**

### Phase 1: 日志监控核心服务
- [ ] 创建 `LogcatMonitorService` 后台服务
- [ ] 实现实时logcat抓取
- [ ] 实现TAG过滤和关键词匹配
- [ ] 转向灯/档位状态解析

### Phase 2: 360全景自动触发
- [ ] 找到360全景启动Intent
- [ ] 转向灯触发360逻辑
- [ ] 倒车档触发360逻辑
- [ ] 触发防抖和冷却机制

### Phase 3: 状态显示与语音
- [ ] 车门状态UI显示
- [ ] 档位状态UI显示
- [ ] 锁车/解锁语音提示
- [ ] 车门未关提醒

---

## 📝 **重要说明**

1. **所有信号均来自实车抓取**，100%准确可用
2. **READ_LOGS权限**是必须的，需通过ADB授权
3. **360全景启动方式**还需要进一步确认Intent
4. **车速信号**在现有日志中未找到，需要补充抓取

---

**日志分析完成！可以开始开发零跑C11专属功能了！** 🎉
