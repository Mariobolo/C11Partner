# C11Partner 架构设计文档

> 📐 本文档是 C11Partner 的唯一架构权威文档，前后端统一描述
>
> 最后更新：2026-07-09
> 文档原则：**与代码保持一致，代码变更时同步更新**

---

## 一、项目概述

### 1.1 项目定位

C11Partner 是零跑 C11 车机的第三方桌面应用，通过 WebView 技术栈实现 UI，通过 Java 后端实现车控功能。

### 1.2 运行环境

| 项目 | 值 |
|------|-----|
| 车机系统 | Android 9 (API 28) |
| 硬件平台 | 高通 8155 |
| 屏幕分辨率 | 1920×1080（横屏） |
| WebView 引擎 | 腾讯 TBS X5 |
| 开发语言 | 前端 HTML/CSS/JS，后端 Java |

### 1.3 技术选型

| 层面 | 选型 | 理由 |
|------|------|------|
| UI 渲染 | WebView + HTML/CSS/JS | 开发效率高、迭代快、定制性强 |
| JS 桥接 | `@JavascriptInterface` | Android 原生方案，稳定可靠 |
| 车控方式 | Intent + Settings.Global + Logcat | 三层互补，覆盖面广 |
| 音乐信息 | 通知监听 (NotificationListenerService) | 兼容所有音乐播放器 |
| 模块封装 | IIFE + window 挂载 | 无构建工具依赖，简单可靠 |

---

## 二、整体架构

### 2.1 架构总览

```
┌─────────────────────────────────────────────────────────────────┐
│                        前端 WebView 层                           │
│  (负责 UI 展示和用户交互，不处理业务逻辑)                          │
│                                                                 │
│  index.html → CSS(7个文件) + JS(按依赖顺序加载)                  │
│                                                                 │
│  Android.xxx()  ────── 调用 ──────►  Java 后端                   │
│  window.updateXxx() ◄──── 回调 ────   Java 后端                 │
└─────────────────────────────────────────────────────────────────┘
                            ↕
┌─────────────────────────────────────────────────────────────────┐
│                        后端 Java 层                              │
│  (负责功能实现和安全调配，不关心 UI 细节)                          │
│                                                                 │
│  MainActivity.java (WebView 容器，生命周期管理)                   │
│       │                                                         │
│       ├── WebViewBridge.java (JS 统一入口，委托转发)              │
│       │     ├── CarControlBridge  → CarControlManager           │
│       │     ├── AppBridge          → AppUtils                   │
│       │     ├── WallpaperBridge    → WallpaperManager           │
│       │     ├── MusicBridge         → MusicNotificationListener  │
│       │     ├── SystemBridge        → Settings/SystemAPI         │
│       │     └── AdbBridge           → AdbManager                │
│       │                                                         │
│       ├── LogcatMonitorService (CAN 信号解析 → 车辆状态)          │
│       ├── AutomationEngine (自动化场景触发)                      │
│       └── LeapMotorCarState (车辆状态数据类)                     │
└─────────────────────────────────────────────────────────────────┘
                            ↕
┌─────────────────────────────────────────────────────────────────┐
│                    零跑 C11 车机系统层                           │
│         (Android 9 / API 28 / 高通 8155 / 1920×1080)             │
│                                                                 │
│  ① Intent 主动控制  │  ② Logcat 被动监控  │  ③ Settings 读写     │
└─────────────────────────────────────────────────────────────────┘
```

### 2.2 三层车控模型

零跑车机没有公开 API，通过逆向分析实现三种控制方式：

| 控制方式 | 说明 | 权限要求 | 实时性 | 典型用途 |
|---------|------|---------|--------|---------|
| **Intent 主动控制** | 发送系统广播/启动 Activity | 普通权限 | 即时 | 启动 360、切换驾驶模式 |
| **Logcat 被动监控** | 读取系统日志解析状态 | `READ_LOGS` | 秒级延迟 | 档位、车门、转向灯、胎压 |
| **Settings.Global** | 读写系统全局属性 | `WRITE_SECURE_SETTINGS` | 即时 | 音量、温度、氛围灯 |

---

## 三、前端架构

### 3.1 设计原则

1. **UI 只负责展示和交互**：不处理业务逻辑，业务逻辑通过 `Android.xxx()` 委托给后端
2. **扁平化结构**：模块间直接调用，无中间层
3. **IIFE 封装**：每个模块用 IIFE 封装，通过 `window.XxxManager` 暴露公开接口
4. **统一初始化**：所有初始化在 `window.addEventListener('load')` 中完成

### 3.2 JS 文件加载顺序

```
index.html <script> 标签按以下顺序加载：

1. utils.js              ← 基础工具（无依赖）
2. storage.js            ← 本地存储（无依赖）
3. bridge.js             ← Android 接口封装（无依赖）

4. bootstrap.js          ← 初始化队列（AppBootstrap）
5. settings-sync.js      ← 设置同步（SettingsSync）
6. app-list-manager.js  ← 应用列表管理（AppListManager）

7. theme.js             ← 主题切换
8. widgets.js           ← Widget 管理

9. music.js             ← 音乐可视化
10. settings.js          ← 设置面板逻辑
11. app.js               ← 应用管理逻辑
12. toast.js             ← 提示组件
13. datetime.js          ← 时间日期
14. wallpaper-manager.js ← 壁纸管理（WallpaperManager + WallpaperSwipeManager）
15. weather.js           ← 天气模块
16. map.js               ← 地图模块
17. async-callback-manager.js ← 异步回调管理
18. system-music-manager.js   ← 系统音乐信息
19. car-state-manager.js      ← 车辆状态管理
20. gear-bridge.js             ← 档位桥接
21. automation-manager.js     ← 自动化场景
22. quick-switch-manager.js   ← 快捷开关
23. index.js                  ← 主入口（统一初始化 + 面板控制 + UI初始化）
```

### 3.3 模块职责和公开接口

| 模块 | 文件 | 职责 | window 挂载 | 公开方法 |
|------|------|------|------------|----------|
| 工具 | `utils.js` | 通用工具函数 | — | `escapeHtml()`, `normalizeAppIcon()`, `blurAfterClick()`, `debounce()`, `throttle()` |
| 存储 | `storage.js` | localStorage 封装 | — | `get()`, `set()`, `remove()` |
| 桥接 | `bridge.js` | Android 接口代理 | — | `Android` 对象 |
| 引导 | `bootstrap.js` | 初始化队列 | `AppBootstrap` | `registerInit()`, `safeInit()`, `runInit()` |
| 设置同步 | `settings-sync.js` | 设置项读写 | `SettingsSync` | `loadWallpaperSettings()`, `initEffectLevel()`, `applyEffectLevel()`, `initThemeMode()`, `applyThemeMode()`, `initThemeToggleIcon()`, `toggleWallpaperCarousel()`, `restoreDefaultWallpaper()` |
| 应用列表 | `app-list-manager.js` | 应用面板管理 | `AppListManager` | `initAppsModal()`, `loadAppList()`, `renderAppsList()`, `initAlphabetNav()`, `showAddToQuickAppsDialog()`, `showRemoveFromQuickAppsDialog()` |
| 主题 | `theme.js` | 日夜模式切换 | — | `toggleTheme()`, `setTheme()` |
| 音乐 | `music.js` | 频谱可视化 | — | `updateMusicVisualization()`, `updateMusicStatus()` |
| 壁纸 | `wallpaper-manager.js` | 壁纸切换/轮播 | `WallpaperManager` + `WallpaperSwipeManager` | `init()`, `nextWallpaper()`, `toggleCarousel()` |
| 天气 | `weather.js` | 天气数据获取 | — | `initWeatherDisplay()`, `fetchWeatherData()` |
| 车辆状态 | `car-state-manager.js` | 状态栏更新 | `updateCarState()` | `init()`, `updateState()`, `updateGearIndicator()` |
| 快捷开关 | `quick-switch-manager.js` | 开关面板 | `QuickSwitchManager` | `showPanel()`, `hidePanel()`, `toggleSwitch()` |
| 自动化 | `automation-manager.js` | 场景配置 | `AutomationManager` | `init()`, `showPanel()`, `toggleScenario()` |
| 系统音乐 | `system-music-manager.js` | 音乐信息显示 | — | `init()`, `updateMusicInfo()` |
| 档位桥接 | `gear-bridge.js` | 档位前后端桥接 | — | `init()` |
| 异步回调 | `async-callback-manager.js` | 回调统一管理 | — | `register()`, `execute()` |
| 入口 | `index.js` | 统一初始化 + 面板控制 + UI初始化 | — | `safeInit()`, `showSettingsModal()`, `hideSettingsModal()`, `showAppsModal()`, `hideAppsModal()`, `initSettingsModal()`, `loadQuickApps()`, `loadQuickSwitches()`, `updateNetworkAndBluetoothStatus()` |

### 3.4 CSS 文件组织

CSS 通过 `index.html` 中的 `<link>` 标签按固定顺序直接引入（不使用 `@import`，因 WebView 支持不完整）。

| 顺序 | 文件 | 职责 | 行数(约) |
|------|------|------|----------|
| 1 | `theme.css` | CSS 变量、主题色、日夜模式 | 350 |
| 2 | `base.css` | 基础重置、全局默认值、触摸优化、特效等级 | 2,800 |
| 3 | `animations.css` | 动画关键帧、过渡效果 | 2,400 |
| 4 | `components.css` | 通用组件（卡片、按钮、弹窗、设置面板、应用列表） | 15,900 |
| 5 | `widgets.css` | Widget 专属（状态栏、天气、音乐、快捷开关、Dock） | 10,800 |
| 6 | `pages.css` | 页面级布局（设置面板、应用列表页） | 5,700 |
| 7 | `responsive.css` | 响应式适配、媒体查询 | 1,700 |

> 加载顺序不可调换，后续文件依赖前面文件定义的变量和基础样式。
> `!important` 仅允许在 `base.css` 中使用（特效等级切换），其他文件禁止使用。

### 3.5 初始化流程

```
window.addEventListener('load', function() {
    // AppBootstrap.runInit() 按队列执行：
    
    1. 设置默认壁纸背景
    2. 依次执行 registerInit() 注册的初始化函数：
       - initSettingsModal()      ← 设置面板
       - loadQuickApps()          ← 快捷应用
       - loadQuickSwitches()      ← 快捷开关
       - updateNetworkAndBluetoothStatus() ← 状态栏
       - initHorizontalScroll()   ← 横向滚动
       - initMusicControls()      ← 音乐控制
       - addTimeDisplayClickEvent() ← 时钟点击
       - initNavigationButtons()  ← 导航按钮
       - initWallpaperDoubleClick() ← 壁纸双击
       - handleWallpaperLongPress() ← 壁纸长按
       - ...其他初始化
    3. 启动状态栏定时器（5秒间隔更新 WiFi/蓝牙/定位）
    4. 启动音乐进度定时器
});
```

---

## 四、后端架构

### 4.1 设计原则

1. **稳重可靠**：所有操作有 try-catch 保护，失败有降级处理
2. **委托模式**：`WebViewBridge` 作为统一入口，委托给子 Bridge 处理
3. **职责单一**：每个类只负责一个功能领域
4. **线程安全**：UI 操作通过 `runOnUiThread()`，耗时操作用线程池

### 4.2 包结构

```
com.c11partner.desktop
├── MainActivity.java              # 主 Activity，WebView 容器
├── App.java                       # Application 类
├── CarStateListener.java          # 车辆状态监听接口
├── CarStatusPresentation.java     # 副屏车辆状态显示
├── LeapMotorCamera360.java       # 360 全景管理
├── LeapMotorCarState.java        # 车辆状态数据类
├── LogcatMonitorService.java     # 日志监控后台服务
│
├── bridge/                        # WebView 桥接层
│   ├── BaseBridge.java            # 桥接基类
│   ├── WebViewBridge.java        # JS 统一入口（委托转发）
│   ├── CarControlBridge.java      # 车控功能
│   ├── AppBridge.java             # 应用管理
│   ├── WallpaperBridge.java       # 壁纸功能
│   ├── MusicBridge.java           # 音乐功能
│   ├── SystemBridge.java          # 系统设置
│   └── AdbBridge.java             # ADB 授权
│
├── adb/                           # ADB 相关
│   ├── AdbManager.java            # ADB 连接管理
│   ├── AdbCommandProcessor.java  # ADB 命令处理
│   ├── AdbIntentForwarder.java   # ADB Intent 转发
│   └── UsbDebugConnection.java    # USB 调试连接
│
├── database/                      # 数据库
│   ├── AppDatabaseHelper.java     # 应用列表缓存
│   ├── ComponentConfigDatabaseHelper.java  # 组件配置
│   ├── ConfigAppDatabaseHelper.java        # 配置应用
│   ├── QuickAppDatabaseHelper.java         # 快捷应用
│   ├── WallpaperCategoryDatabaseHelper.java # 壁纸分类
│   └── WallpaperSettingsDatabaseHelper.java # 壁纸设置
│
├── receiver/                      # 广播接收器
│   └── BootReceiver.java          # 开机启动
│
├── service/                       # 服务
│   ├── MediaSessionService.java   # 媒体会话
│   ├── MusicNotificationListenerService.java # 音乐通知监听
│   ├── MusicService.java          # 音乐播放服务
│   ├── MyAccessibilityService.java # 无障碍服务
│   └── RecentsAccessibilityService.java # 最近任务无障碍
│
└── utils/                         # 工具类
    ├── AppUtils.java              # 应用信息获取
    ├── AutomationEngine.java      # 自动化引擎
    ├── CarControlManager.java     # 车控实现
    ├── InitManager.java           # 初始化管理
    ├── LunarCalendarUtils.java    # 农历日历
    ├── MusicInfoExtractor.java    # 音乐信息提取
    ├── MusicUtils.java            # 音乐工具
    ├── MusicVisualizer.java       # 音乐可视化
    ├── QuoteApiUtils.java         # 名言 API
    ├── SecondaryScreenManager.java # 副屏管理
    ├── ServiceManager.java        # 服务管理
    ├── TaskManager.java           # 任务管理
    ├── WallpaperCategoryApiUtils.java # 壁纸分类 API
    ├── WallpaperDownloadUtils.java    # 壁纸下载
    └── WallpaperManager.java      # 壁纸管理
```

### 4.3 桥接层结构

```
JS 调用: Android.methodName(参数)
           │
           ▼
    WebViewBridge.java (@JavascriptInterface)
           │
           ├── 车控 → CarControlBridge → CarControlManager
           │       (50+ 方法: 灯光、空调、模式、音量等)
           │
           ├── 应用 → AppBridge → AppUtils
           │       (15 方法: 应用列表、快捷应用、启动等)
           │
           ├── 壁纸 → WallpaperBridge → WallpaperManager
           │       (26 方法: 切换、轮播、分类、删除等)
           │
           ├── 音乐 → MusicBridge → MusicNotificationListenerService
           │       (12 方法: 播放控制、信息获取等)
           │
           ├── 系统 → SystemBridge → Settings API
           │       (11 方法: 亮度、屏幕超时、系统设置等)
           │
           └── ADB → AdbBridge → AdbManager
                   (4 方法: ADB 授权、权限授予等)
```

### 4.4 车辆状态更新流程

```
LogcatMonitorService (后台服务，持续读取日志)
        │
        ▼
  解析 CAN 信号 (档位/车门/转向灯/胎压/车速等)
        │
        ▼
  LeapMotorCarState (状态数据类)
        │
        ├──► CarStateListener 回调
        │       │
        │       ▼
        │   MainActivity.updateCarStateToFrontend()
        │       │
        │       ▼
        │   webView.evaluateJavascript("window.updateCarState(json)")
        │       │
        │       ▼
        │   CarStateManager.updateState() → 更新状态栏 UI
        │
        └──► AutomationEngine.onXxxChanged()
                │
                ▼
            检查触发条件 → 执行自动化动作
            (开360/语音提示/降音量等)
```

### 4.5 车控操作流程

```
用户点击快捷开关
        │
        ▼
  QuickSwitchManager (前端)
        │
        ▼
  Android.setLowBeamLight(true)  ← JS 调用
        │
        ▼
  WebViewBridge.setLowBeamLight()  ← @JavascriptInterface
        │
        ▼
  CarControlBridge.setLowBeamLight()  ← 委托
        │
        ▼
  CarControlManager.setLowBeamLight()  ← 实现
        │
        ├──► 方式1: 发送 Intent 广播
        ├──► 方式2: 写入 Settings.Global
        └──► 方式3: 语音控制（降级方案）
```

---

## 五、权限模型

### 5.1 三大核心权限（ADB 授予）

| 权限 | 用途 | 授予方式 |
|------|------|---------|
| `READ_LOGS` | 读取系统日志，获取车辆状态 | `adb shell pm grant com.c11partner.desktop android.permission.READ_LOGS` |
| `DUMP` | 获取系统状态信息 | `adb shell pm grant com.c11partner.desktop android.permission.DUMP` |
| `WRITE_SECURE_SETTINGS` | 写入系统设置，控制车辆功能 | `adb shell pm grant com.c11partner.desktop android.permission.WRITE_SECURE_SETTINGS` |

### 5.2 用户授权权限

| 权限 | 用途 | 授予方式 |
|------|------|---------|
| 通知监听 | 读取音乐播放信息 | 设置页 → 通知使用权 |
| 悬浮窗 | 悬浮窗显示 | 设置页 → 悬浮窗权限 |
| 无障碍 | 模拟点击操作 | 设置页 → 无障碍服务 |

---

## 六、代码规范

### 6.1 命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| Java 类名 | PascalCase | `CarControlManager` |
| Java 方法 | camelCase | `setLowBeamLight` |
| Java 常量 | UPPER_SNAKE | `KEY_STR_CAR_100006` |
| JS 对象 | PascalCase | `QuickSwitchManager` |
| JS 函数 | camelCase | `updateCarState` |
| JS 变量 | camelCase | `isDragging` |
| CSS 类 | kebab-case | `.quick-switch-panel` |
| CSS 变量 | `--kebab-case` | `--primary-color` |

### 6.2 JS 模块规范

- 使用 IIFE 封装：`const XxxManager = (function() { ... return { ... }; })();`
- 显式挂载到 window：`window.XxxManager = XxxManager;`
- 公开方法通过 return 暴露，私有方法不暴露
- 每个模块有 `init()` 方法用于初始化

### 6.3 CSS 规范

- 通过 `<link>` 标签引入，不使用 `@import`
- `!important` 仅允许在 `base.css` 中使用（特效等级切换）
- 新增样式禁止使用 `!important`，通过提高选择器优先级解决冲突
- 颜色使用 CSS 变量（`var(--xxx)`），不硬编码

### 6.4 Java 规范

- 使用 AndroidX，不用旧 support 库
- `@JavascriptInterface` 方法必须有对应文档
- 耗时操作使用线程池，不阻塞主线程
- UI 操作通过 `runOnUiThread()`

---

## 七、扩展开发指南

### 7.1 添加新的车控功能

1. **后端**：`CarControlManager.java` 添加控制方法
2. **桥接**：`CarControlBridge.java` 添加实现，`WebViewBridge.java` 添加 `@JavascriptInterface` 委托
3. **前端**：`quick-switch-manager.js` 添加开关 UI
4. **文档**：更新 `JS_API_REFERENCE.md`

### 7.2 添加新的前端模块

1. 创建 `xxx-manager.js`，IIFE 封装并挂载到 `window`
2. 在 `index.html` 中用 `<script>` 引入（注意依赖顺序）
3. 在 `index.js` 中通过 `safeInit('initXxx', initXxx)` 注册初始化
4. CSS 样式添加到对应模块文件（`widgets.css` 或 `components.css`）

### 7.3 添加新的自动化场景

1. `AutomationEngine.java` 添加触发条件判断和执行动作
2. `WebViewBridge.java` 添加配置接口
3. `automation-manager.js` 添加前端配置开关

---

## 八、相关文档

| 文档 | 职责 |
|------|------|
| [开发指南](DEVELOPMENT_GUIDE.md) | 环境搭建、开发流程、验证清单 |
| [代码索引](CODE_INDEX.md) | 函数级快速定位 |
| [JS 接口文档](JS_API_REFERENCE.md) | 前后端接口契约 |
| [项目状态](PROJECT_STATUS.md) | 当前项目真实状态 |
| [常见问题](FAQ.md) | 常见问题解答 |
| [提交规范](COMMIT_CONVENTION.md) | Git 提交规范 |

---

**文档版本**：v2.0
**最后更新**：2026-07-09
