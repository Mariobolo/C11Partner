# C11Partner 项目上下文文档

> 📖 本文档记录项目架构概要、关键文件索引、已完成工作摘要，帮助快速恢复对项目的理解
>
> 最后更新：2026-07-09

---

## 一、项目概述

**C11Partner** 是零跑 C11 车机的第三方桌面应用，通过 WebView 技术栈实现 UI，通过 Java 后端实现车控功能。

### 运行环境

| 项目 | 值 |
|------|-----|
| 车机系统 | Android 9 (API 28) |
| 硬件平台 | 高通 8155 |
| 屏幕分辨率 | 1920×1080（横屏） |
| WebView 引擎 | 腾讯 TBS X5 |
| 开发语言 | 前端 HTML/CSS/JS，后端 Java |

### 技术选型

- **UI 渲染**：WebView + HTML/CSS/JS
- **JS 桥接**：`@JavascriptInterface`（Android 原生方案）
- **车控方式**：Intent + Settings.Global + Logcat（三层互补）
- **音乐信息**：NotificationListenerService（通知监听）
- **模块封装**：IIFE + window 挂载（无构建工具依赖）

---

## 二、整体架构

### 2.1 架构总览

```
┌─────────────────────────────────────────────────────────────────┐
│                        前端 WebView 层                           │
│  index.html → CSS(7个文件) + JS(按依赖顺序加载)                  │
│  Android.xxx()  ────── 调用 ──────►  Java 后端                   │
│  window.updateXxx() ◄──── 回调 ────   Java 后端                 │
└─────────────────────────────────────────────────────────────────┘
                            ↕
┌─────────────────────────────────────────────────────────────────┐
│                        后端 Java 层                              │
│  MainActivity.java (WebView 容器)                                │
│       ├── WebViewBridge.java (JS 统一入口，委托转发)              │
│       │     ├── CarControlBridge  → CarControlManager           │
│       │     ├── AppBridge          → AppUtils                   │
│       │     ├── WallpaperBridge    → WallpaperManager           │
│       │     ├── MusicBridge         → MusicNotificationListener  │
│       │     ├── SystemBridge        → Settings/SystemAPI         │
│       │     └── AdbBridge           → AdbManager                │
│       ├── LogcatMonitorService (CAN 信号解析 → 车辆状态)          │
│       ├── AutomationEngine (自动化场景触发)                      │
│       └── LeapMotorCarState (车辆状态数据类)                     │
└─────────────────────────────────────────────────────────────────┘
                            ↕
┌─────────────────────────────────────────────────────────────────┐
│                    零跑 C11 车机系统层                           │
│  ① Intent 主动控制  │  ② Logcat 被动监控  │  ③ Settings 读写     │
└─────────────────────────────────────────────────────────────────┘
```

### 2.2 三层车控模型

| 控制方式 | 说明 | 权限要求 | 实时性 |
|---------|------|---------|--------|
| **Intent 主动控制** | 发送系统广播/启动 Activity | 普通权限 | 即时 |
| **Logcat 被动监控** | 读取系统日志解析状态 | `READ_LOGS` | 秒级延迟 |
| **Settings.Global** | 读写系统全局属性 | `WRITE_SECURE_SETTINGS` | 即时 |

---

## 三、关键文件索引

### 3.1 前端文件（app/src/main/assets/）

#### JS 加载顺序（index.html）

```
基础依赖层：utils.js → storage.js → bridge.js
核心框架层：bootstrap.js → settings-sync.js → app-list-manager.js
核心功能层：theme.js → widgets.js
业务逻辑层：music.js → settings.js → app.js → toast.js → datetime.js → wallpaper-manager.js
           → weather.js → map.js → async-callback-manager.js → system-music-manager.js
           → car-state-manager.js → gear-bridge.js → automation-manager.js
           → quick-switch-manager.js → index.js
```

#### 关键 JS 文件

| 文件 | 行数(约) | 职责 | window 挂载 |
|------|----------|------|------------|
| utils.js | 414 | 通用工具函数 | — |
| storage.js | 174 | localStorage 封装 | — |
| bridge.js | 194 | Android 接口代理 | `Android` 对象 |
| bootstrap.js | 52 | 初始化队列（已简化） | `AppBootstrap` |
| settings-sync.js | 174 | 设置项读写 | `SettingsSync` |
| app-list-manager.js | 389 | 应用面板管理 | `AppListManager` |
| theme.js | 233 | 日夜模式切换 | — |
| widgets.js | 603 | Widget 管理 | — |
| music.js | 337 | 频谱可视化 | — |
| settings.js | 402 | 设置面板逻辑 | — |
| app.js | 366 | 应用管理逻辑 | — |
| toast.js | 144 | 提示组件 | — |
| datetime.js | 143 | 时间日期 | — |
| wallpaper-manager.js | 812 | 壁纸切换/轮播 | `WallpaperManager` + `WallpaperSwipeManager` |
| weather.js | 166 | 天气数据获取 | — |
| map.js | 169 | 地图模块 | — |
| async-callback-manager.js | 59 | 回调统一管理 | — |
| system-music-manager.js | 179 | 音乐信息显示 | — |
| car-state-manager.js | 355 | 状态栏更新 | `updateCarState()` |
| gear-bridge.js | 184 | 档位前后端桥接 | — |
| automation-manager.js | 396 | 场景配置 | `AutomationManager` |
| quick-switch-manager.js | 422 | 开关面板 | `QuickSwitchManager` |
| index.js | 1003 | 主入口，统一初始化（含面板控制+UI初始化） | — |

#### CSS 文件（按加载顺序）

| 顺序 | 文件 | 行数(约) | 职责 |
|------|------|----------|------|
| 1 | theme.css | 489 | CSS 变量、主题色、日夜模式 |
| 2 | base.css | 579 | 基础重置、全局默认值、触摸优化、特效等级 |
| 3 | animations.css | 1,961 | 动画关键帧、过渡效果 |
| 4 | components.css | 12,277 | 通用组件（卡片、按钮、弹窗、设置面板、应用列表） |
| 5 | widgets.css | 6,601 | Widget 专属（状态栏、天气、音乐、快捷开关、Dock） |
| 6 | pages.css | 5,244 | 页面级布局（设置面板、应用列表页） |
| 7 | responsive.css | 1,417 | 响应式适配、媒体查询 |

> `!important` 仅允许在 base.css 中使用（特效等级切换）

### 3.2 后端文件（app/src/main/java/com/c11partner/desktop/）

| 包 | 关键文件 | 职责 |
|----|----------|------|
| desktop (根) | MainActivity.java | 主 Activity，WebView 容器 |
| desktop (根) | LogcatMonitorService.java | 日志监控后台服务 |
| desktop (根) | LeapMotorCarState.java | 车辆状态数据类 |
| desktop (根) | LeapMotorCamera360.java | 360 全景管理 |
| desktop (根) | CarStatusPresentation.java | 副屏车辆状态显示 |
| bridge | WebViewBridge.java | JS 统一入口（委托转发） |
| bridge | CarControlBridge.java | 车控功能（50+ 方法） |
| bridge | AppBridge.java | 应用管理（15 方法） |
| bridge | WallpaperBridge.java | 壁纸功能（26 方法） |
| bridge | MusicBridge.java | 音乐功能（12 方法） |
| bridge | SystemBridge.java | 系统设置（11 方法） |
| bridge | AdbBridge.java | ADB 授权（4 方法） |
| utils | CarControlManager.java | 车控实现 |
| utils | AutomationEngine.java | 自动化场景引擎 |
| utils | AppUtils.java | 应用信息获取 |
| utils | WallpaperManager.java | 壁纸管理 |
| utils | SecondaryScreenManager.java | 副屏管理 |
| service | MusicNotificationListenerService.java | 音乐通知监听 |
| service | MyAccessibilityService.java | 无障碍服务 |
| database | AppDatabaseHelper.java | 应用列表缓存 |

### 3.3 文档文件（docs/）

| 文档 | 职责 |
|------|------|
| ARCHITECTURE.md | ⭐ 架构权威文档（前后端统一） |
| PROJECT_STATUS.md | 项目当前真实状态（代码统计） |
| DEVELOPMENT_GUIDE.md | 开发指南（环境搭建、编译安装） |
| CODE_INDEX.md | 函数级快速定位索引 |
| JS_API_REFERENCE.md | 前后端接口契约（167个接口） |
| FAQ.md | 常见问题解答 |
| COMMIT_CONVENTION.md | Git 提交规范 |
| DEVICE_CONFIG.md | 设备配置信息 |
| TESTING.md | 测试方法 |
| ICON_RESOURCES.md | 图标素材推荐 |
| LEAPMOTOR_LOG_ANALYSIS.md | 实车日志分析 |

---

## 四、核心流程

### 4.1 前端初始化流程

```
window.addEventListener('load', function() {
    AppBootstrap.runInit() 按队列执行：
    1. 设置默认壁纸背景
    2. initSettingsModal()      ← 设置面板
    3. loadQuickApps()          ← 快捷应用
    4. loadQuickSwitches()      ← 快捷开关
    5. updateNetworkAndBluetoothStatus() ← 状态栏
    6. initHorizontalScroll()   ← 横向滚动
    7. initMusicControls()      ← 音乐控制
    8. addTimeDisplayClickEvent() ← 时钟点击
    9. initNavigationButtons()  ← 导航按钮
    ...其他初始化
    N. 启动状态栏定时器（5秒间隔）
});
```

### 4.2 车辆状态更新流程

```
LogcatMonitorService (后台服务，持续读取日志)
    → 解析 CAN 信号 (档位/车门/转向灯/胎压/车速等)
    → LeapMotorCarState (状态数据类)
    → CarStateListener 回调
    → MainActivity.updateCarStateToFrontend()
    → webView.evaluateJavascript("window.updateCarState(json)")
    → CarStateManager.updateState() → 更新状态栏 UI
```

### 4.3 车控操作流程

```
用户点击快捷开关
    → QuickSwitchManager (前端)
    → Android.setLowBeamLight(true)  ← JS 调用
    → WebViewBridge.setLowBeamLight()  ← @JavascriptInterface
    → CarControlBridge.setLowBeamLight()  ← 委托
    → CarControlManager.setLowBeamLight()  ← 实现
    → 发送 Intent 广播 / 写入 Settings.Global
```

---

## 五、技术约束和规范

### 5.1 硬约束（必须遵守）

- Panel 关闭必须点击面板边缘或空白处触发
- 所有面板（设置、开关、应用列表）必须全屏
- 触摸元素点击/触摸结束后必须 blur 恢复正常样式
- 触摸设备上禁用所有 focus 样式（outline, box-shadow, transform）
- 三种特效等级：无特效(none)、最低特效(low)、全特效(high)，默认无特效
- WebView 不使用强制硬件层（LAYER_TYPE_HARDWARE）
- Target SDK 必须为 28（匹配零跑C11 23款 Android 9）
- ABI filters 限制为 armeabi-v7a 和 arm64-v8a（8155芯片）
- 壁纸设置仅4种：Bing(默认)、本地图片、视频、3D车模
- JS 模块必须显式挂载到 window 对象
- 面板显示用 `active` 类 + `display` 属性组合控制

### 5.2 面板显示规范

- **面板打开**：先 `display: flex`，再添加 `active` 类触发动画
- **面板关闭**：先移除 `active` 类，等动画结束再 `display: none`
- **面板可见性判断**：用 `classList.contains('active')`
- **点击关闭**：检查 `event.target === modal`（只有点击背景层才关闭）
- **设置面板**：`wallpaper-tab` 必须有 `active` 类（防止空白）
- **应用面板**：打开时必须触发 `loadAppList()`

### 5.3 CSS 规范

- 通过 `<link>` 标签引入，不使用 `@import`
- `!important` 仅允许在 base.css 中使用
- 新增样式禁止使用 `!important`，通过提高选择器优先级解决
- 颜色使用 CSS 变量（`var(--xxx)`），不硬编码

### 5.4 JS 模块规范

- 使用 IIFE 封装：`const XxxManager = (function() { ... return { ... }; })();`
- 显式挂载到 window：`window.XxxManager = XxxManager;`
- 公开方法通过 return 暴露，私有方法不暴露
- 每个模块有 `init()` 方法用于初始化

---

## 六、已完成工作摘要

### 阶段1：文档整理（2026-07-09 完成）

- 重写6个核心文档：ARCHITECTURE.md, PROJECT_STATUS.md, DEVELOPMENT_GUIDE.md, CODE_INDEX.md, JS_API_REFERENCE.md, docs/README.md
- 删除12个过期文档
- 建立文档维护规范

### 阶段2：前端清理（2026-07-09 完成）

- 删除 panel-controller.js（106行），6个面板控制函数合并到 index.js
- 删除 ui-initializer.js（472行），17个UI初始化函数合并到 index.js
- 简化 bootstrap.js（91行→52行），移除冗余 state/debounce/throttle/addDebouncedClick
- 清理 index.js 冗余适配层（删除15个转发函数，改为直接调用模块方法）
- debounce/throttle 统一使用 utils.js 的全局函数

### 阶段3：接口对齐（2026-07-09 完成）

- JS_API_REFERENCE.md v2.1：修复返回值类型（void→boolean）、补充缺失方法（getCarState/toggleAC/setCameraOverspeedLimit等）、修正参数签名、新增副屏接口章节
- CODE_INDEX.md v2.1：移除已删除的 panel-controller.js 和 ui-initializer.js、更新文件行数、补充缺失接口、修正返回值类型
- PROJECT_STATUS.md v2.1：更新代码统计（JS 26文件→8,559行）、移除过期文档标记、更新待修复问题和技术债务列表

### 阶段4：CSS 清理（2026-07-09 部分完成）

- 删除 main.css（26行）：@import 入口文件，index.html 已直接引入各CSS
- 删除 music.css（768行）：未在 index.html 引入，JS 无特有类名引用，widgets.css 已有完整音乐组件样式
- 验证 !important：仅 base.css 中有 35 处（特效等级+触摸优化），符合规范
- 剩余：widgets.css 中重复样式选择器待清理

### 之前的 Bug 修复

- **时钟秒数闪烁**：删除 ui-initializer.js 中重复的时间更新逻辑，统一使用 datetime.js
- **面板点击任意元素都关闭**：改为检查 `event.target === modal`
- **应用列表不加载**：将 loadAppList 提取为模块级函数，showAppsModal 时调用
- **设置面板空白**：给 wallpaper-tab 添加 active 类
- **面板关闭不触发**：统一用 active 类 + display 配合
- **开关面板透明父div**：新增 quickSwitchPanelWrapper

---

## 七、技术债务清单

### 高优先级
- （已清除）前端冗余中间层已在阶段2完成清理

### 中优先级
- 废弃 patch 文件未删除（one-click-permission-patch.js, tasks-btn-adb-patch.js）
- android_interface.js（291行）可能未使用，待确认
- widgets.css 中存在重复样式选择器，需清理

### 低优先级
- （已完成）PROJECT_STATUS.md 和 CODE_INDEX.md 已在阶段3更新

---

## 八、开发命令

### 编译安装

```bash
# 编译 Debug APK
./gradlew assembleDebug

# 安装到设备
adb install app/build/outputs/apk/debug/app-debug.apk

# 编译并安装
./gradlew installDebug
```

### 权限授予（首次安装后）

```bash
adb shell pm grant com.c11partner.desktop android.permission.READ_LOGS
adb shell pm grant com.c11partner.desktop android.permission.DUMP
adb shell pm grant com.c11partner.desktop android.permission.WRITE_SECURE_SETTINGS
```

### Git 提交规范

```
类型: 简短描述

类型：feat/fix/docs/style/refactor/test/chore
```

---

**文档版本**：v1.0
**维护规则**：架构变化、技术决策、完成重要工作时更新本文档
