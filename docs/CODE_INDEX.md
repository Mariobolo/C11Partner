# C11Partner 代码索引文档
> 📌 **快速定位代码的神器** - 修改代码前先看本文档，找到对应函数行号再精准读取，避免读取大文件占用上下文
>
> 自动生成时间：2026-07-04 12:00:00
> 生成脚本：`tools/generate_code_index.py`

---

## 📖 使用说明

### 为什么需要这个文档？
- 项目代码量较大（WebViewBridge 4500+行，index.js 3000+行）
- 每次读取整个文件会占用大量上下文，导致篇幅过长
- 用本文档快速定位函数行号，再精准读取小范围代码

### 使用方法
1. 根据功能分类找到对应的文件和函数
2. 记下函数所在行号
3. 用 `sed -n '起始行,结束行p' 文件名` 精准读取代码
4. 或者用 Grep 搜索函数名定位

### 重新生成索引
```bash
python3 tools/generate_code_index.py
```

---

## 📊 索引概览

| 类型 | 文件数 | 函数/方法数 |
|------|--------|------------|
| Java后端 | 8 | 414 |
| JS前端 | 3 | 145 |
| **总计** | **11** | **559** |


---
## 🎨 前端代码审计（CSS）
> 审计时间：2026-06-23
> 审计阶段：第一阶段 - 代码资产盘点

### CSS文件总览
| 文件名 | 行数 | 大小(KB) | 占比 | 职责说明 |
|--------|------|----------|------|----------|
| components.css | 15,896 | 417 | 38.9% | 通用组件样式（按钮、卡片、弹窗、图标等） |
| widgets.css | 10,797 | 285 | 26.4% | 桌面小组件样式（状态栏、widget卡片、Dock栏等） |
| pages.css | 5,748 | 153 | 14.1% | 各页面专属样式（设置页、应用列表页等） |
| base.css | 2,844 | 67 | 7.0% | 基础重置与全局样式 |
| animations.css | 2,435 | 55 | 6.0% | 动画效果与关键帧 |
| responsive.css | 1,712 | 35 | 4.2% | 响应式适配与媒体查询 |
| theme.css | 352 | 14 | 0.9% | CSS变量/设计令牌/主题系统（含少量!important） |
| **总计** | **39,784** | **1,026** | **100%** | - |
> 注：index.css、music.css、main.css 已删除。CSS通过 index.html 中 `<link>` 标签按顺序直接引入7个模块文件。

### CSS文件加载顺序
```
index.html → <link> 直接引入7个CSS模块（按顺序）：
  theme.css → base.css → animations.css → components.css → widgets.css → pages.css → responsive.css
```
> 注意：CSS加载方式已从 main.css 的 @import 改为 index.html 中直接使用 `<link>` 标签按顺序引入7个模块文件。此方式在 WebView 中更可靠，避免了 @import 在部分 WebView 环境下不生效的问题。

### CSS主要问题识别
1. **文件职责重叠**：components.css与widgets.css存在大量样式重叠，边界不清晰
2. **重复定义严重**：同一选择器在多处重复定义，依靠加载顺序维持最终效果
3. **硬编码较多**：大量颜色、间距、阴影是硬编码值，未使用CSS变量
4. **优先级依赖脆弱**：大量样式依赖文件加载顺序，改动容易引发连锁反应

### CSS !important 清理状态（2026-07-04 更新）
> CSS规范化清理已完成，!important 从原来的 **2,247处** 大幅减少到约 **4处**（仅 base.css 中保留少量必要的 !important 声明）。大部分依赖 !important 覆盖的样式已通过调整选择器优先级和清理冗余代码来解决。底部状态栏相关样式已完全删除。

---

## 📊 CSS重复选择器深度统计表（widgets.css）
> 审计时间：2026-06-24 | 审计阶段：第一阶段 - 代码审计与基线建立

### 核心选择器重复统计
| 选择器 | 出现次数 | 主要行号分布 | 问题类型 | 风险等级 |
|--------|---------|-------------|---------|---------|
| `.widget` | **37次** | 297, 340, 357, 375, 384, 387, 407, 424, 434, 464, 468, 471, 9163, 9169, 9185, 9208, 9229, 9246, 9266, 9278, 9312, 9316, 9382, 9396, 9578, 9731 | 完全重复+版本叠加+伪元素嵌套 | ⚠️ 高 |
| `.weather-widget` | **66次** | 899, 920, 941, 958, 977, 987, 997, 1000, 1009, 1013, 1065+ | 伪元素+hover+嵌套选择器 | ⚠️ 高 |
| `.quick-switch-item` | **68次** | 697, 722, 742, 761, 770, 776, 793, 810, 826, 837, 846, 875, 887, 890, 893, 3700, 3703, 4084, 7167+ | 状态+动画+嵌套选择器 | ⚠️ 高 |
| `.top-status-bar` | **9次** | 6, 1257, 1271, 4839, 4864, 5489, 5503, 9685, 10296 | 完全重复定义+版本叠加 | ⚠️ 中 |
| `.music-widget` | 14次 | 2184, 2291, 2470+ | 动画+交互状态 | ⚠️ 中 |
| `.dock-bar` | 15次 | 多处分布 | 响应式+状态 | ⚠️ 中 |
| `.temperature` | 6次 | 多处分布 | 样式重复 | ⚠️ 低 |
| `.quick-apps` | 4次 | 少量分布 | 轻微重复 | ⚠️ 低 |

### 重点选择器详细分析

#### 1. `.widget` 选择器（37次定义）
**行号与作用：**
- 行297-339：基础布局定义（第1版）
- 行384-463：3D透视和阴影增强（第2版优化）
- 行464-470：毛玻璃效果（第3版优化）
- 行9163-9168：重复的基础样式（疑似定时任务追加）
- 行9169-9184：重复的hover效果（完全重复）
- 行9312-9381：光泽效果增强（第4版优化）
- 行9396+：末尾重复追加的!important覆盖样式（垃圾代码）

**问题识别：**
- ✅ 有效定义：约12个（包含基础样式、伪元素、hover、active、focus状态）
- ❌ 完全重复：约15个（多次定时任务重复追加到文件末尾）
- ❌ 被覆盖无效：约10个（被后面的!important完全覆盖）

#### 2. `.top-status-bar` 选择器（9次定义）
**行号与作用：**
- 行6-1256：原始基础定义（第1版）
- 行1257-1270：毛玻璃增强（第2版）
- 行4839-4863：车辆状态指示器优化（第3版）
- 行5489-5502：靠左显示优化（第4版）
- 行9685、10296：文件末尾重复追加（垃圾代码）

**问题识别：**
- ✅ 有效定义：4个（渐进式优化版本）
- ❌ 完全重复：5个（文件末尾重复追加）

#### 3. 其他选择器概况
- `.weather-widget`：大量嵌套选择器和hover状态，66次中约40%为有效嵌套，60%为重复
- `.quick-switch-item`：包含active、focus、hover等多种状态，68次定义结构复杂
- `.music-widget`：黑胶唱片动画相关定义较多，14次基本有效

### 代码质量总结
| 指标 | 数值 | 说明 |
|------|------|------|
| 文件总行数 | 10,797行 | 异常庞大 |
| 估计有效代码 | ~5,800行 | 约54% |
| 估计重复/垃圾代码 | ~5,000行 | 约46% |
| 主要污染源 | 定时任务重复追加 | 文件末尾大量!important覆盖样式 |

---

## 🗂️ index.js 模块拆解图
> 审计时间：2026-06-24 | 文件总行数：4,821行

### 模块架构总览
```
index.js (4,821行)
├── 📦 基础架构层 (137行, 2.8%)
│   ├── AsyncCallbackManager - 异步回调管理器
│   └── 全局变量与工具函数
│
├── 🎨 UI交互层 (3,050行, 63.3%)
│   ├── 设置面板模块 - 690行
│   ├── 应用列表模块 - 605行
│   ├── 快捷开关模块 - 191行
│   ├── 快捷应用管理 - 258行
│   ├── 组件可见性配置 - 168行
│   ├── 壁纸交互模块 - 251行
│   ├── 音乐控制模块 - 314行
│   └── 按钮点击效果 - 66行
│
├── 🚗 车控功能层 (753行, 15.6%)
│   ├── 空调控制模块 - 461行
│   ├── 空调控制(重复) - 131行
│   └── 网络状态模块 - 161行
│
├── ⚙️ 系统服务层 (1,203行, 25.0%)
│   ├── 时间更新监听 - 825行
│   └── 自动化场景配置 - 378行
│
└── 🔧 开发工具层 (251行, 5.2%)
    ├── 开发者工具/测试 - 227行
    └── 修复函数 - 24行
```

### 各模块详细统计

| 模块名称 | 行号范围 | 代码行数 | 占比 | 主要功能 |
|---------|---------|---------|------|---------|
| **AsyncCallbackManager + 全局变量** | 1-71 | 71 | 1.5% | 异步回调管理、全局状态标志 |
| **按钮点击效果** | 72-137 | 66 | 1.4% | addClickEffect() 按钮交互反馈 |
| **设置面板模块** | 138-827 | 690 | 14.3% | initSettingsModal、壁纸/系统/组件设置 |
| **组件可见性配置** | 828-995 | 168 | 3.5% | 组件显示/隐藏配置功能 |
| **应用列表模块** | 996-1600 | 605 | 12.5% | 应用渲染、搜索、字母导航 |
| **快捷应用管理** | 1601-1858 | 258 | 5.4% | 快捷应用增删管理 |
| **快捷开关模块** | 1859-2049 | 191 | 4.0% | 快捷开关状态管理 |
| **空调控制模块** | 2050-2510 | 461 | 9.6% | 温度、风量、AC开关控制 |
| **网络状态模块** | 2511-2671 | 161 | 3.3% | WiFi、蓝牙状态检测 |
| **壁纸交互模块** | 2672-2922 | 251 | 5.2% | 壁纸切换、长按删除、双击交互 |
| **音乐控制模块** | 2923-3236 | 314 | 6.5% | 音乐播放、进度、控制按钮 |
| **时间更新监听** | 3237-4061 | 825 | 17.1% | 车辆状态实时推送、时间同步 |
| **自动化场景配置** | 4062-4439 | 378 | 7.8% | 12个自动化场景开关配置 |
| **空调控制(重复)** | 4440-4570 | 131 | 2.7% | updateAcTemperature等重复函数 |
| **开发者工具/测试** | 4571-4797 | 227 | 4.7% | mock数据、调试函数 |
| **修复函数** | 4798-4821 | 24 | 0.5% | fixAppsModalPosition等修复 |
| **总计** | - | **4,821** | **100%** | - |

### 模块问题识别
1. **入口文件臃肿**：index.js承载了几乎所有前端业务逻辑，单一文件过大
2. **代码重复**：空调控制函数出现两次定义（行2050和行4440）
3. **职责不清**：时间更新监听模块（825行）混杂了过多车辆状态处理逻辑
4. **缺乏模块化**：没有清晰的模块边界，函数之间依赖关系复杂

---

---
## ⚡ 前端代码审计（JS）
> 审计时间：2026-06-23
> 审计阶段：第一阶段 - 代码资产盘点

### JS文件总览
| 文件名 | 行数 | 大小(KB) | 占比 | 职责说明 |
|--------|------|----------|------|----------|
| index.js | 4,821 | 174 | 47.2% | 主入口文件（包含大量业务逻辑） |
| music.js | 710 | 23 | 7.0% | 音乐播放器逻辑 |
| wallpaper-manager.js | 673 | 19 | 6.6% | 壁纸管理器 |
| widgets.js | 651 | 21 | 6.4% | 小组件管理 |
| settings.js | 436 | 13 | 4.3% | 设置功能逻辑 |
| app.js | 427 | 12 | 4.2% | 应用管理 |
| utils.js | 329 | 10 | 3.2% | 工具函数库 |
| theme.js | 261 | 8 | 2.6% | 主题管理 |
| wallpaper-swipe-bootstrap.js | 243 | 9 | 2.4% | 壁纸滑动引导 |
| wallpaper.js | 291 | 14 | 2.9% | 壁纸功能 |
| bridge.js | 194 | 7 | 1.9% | Android通信桥接 |
| map.js | 194 | 8 | 1.9% | 地图功能 |
| weather.js | 187 | 6 | 1.8% | 天气功能 |
| android_interface.js | 187 | 6 | 1.8% | Android接口定义 |
| storage.js | 184 | 6 | 1.8% | 存储管理 |
| toast.js | 151 | 4 | 1.5% | 提示消息组件 |
| datetime.js | 149 | 7 | 1.5% | 日期时间功能 |
| 其他patch文件 | ~200 | ~7 | ~2% | 各类补丁文件 |
| **总计** | **10,209** | **360** | **100%** | - |

### JS文件加载顺序
```
utils.js → storage.js → bridge.js → theme.js → widgets.js → music.js → settings.js → app.js → toast.js → datetime.js → wallpaper.js → wallpaper-manager.js → wallpaper-swipe-bootstrap.js → weather.js → map.js → index.js
```

### JS主要问题识别
1. **入口文件臃肿**：index.js占了近一半代码量，承载了过多业务逻辑
2. **全局变量较多**：约82个顶层声明，存在命名冲突风险
3. **模块化不彻底**：虽然拆分了文件，但缺乏统一的模块加载和依赖管理
4. **状态分散**：车辆状态、UI状态、设置项等散落在各处

---

## 🔧 后端核心文件索引（Java）

### MainActivity.java
**路径**：`app/src/main/java/com/c11partner/desktop/MainActivity.java`  
**行数**：约 2107 行  
**职责**：主Activity，应用入口，状态推送，生命周期管理

#### 匿名内部类

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `run()` | 171 | public |
| `run()` | 1258 | public |
| `createTestFile()` | 1339 | private |

#### 状态获取

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getLogcatMonitorService()` | 211 | public |
| `isLogServiceBound()` | 218 | public |
| `onServiceDisconnected()` | 234 | public |
| `onPageFinished()` | 598 | public |
| `isTtsEngineAvailable()` | 694 | private |
| `setupEnglishLanguage()` | 748 | private |
| `generateVisualizationData()` | 838 | private |
| `sendVisualizationDataToFrontend()` | 884 | private |
| `unregisterReceivers()` | 1511 | private |
| `getSystemProperty()` | 1576 | private |
| `onServiceDisconnected()` | 1624 | public |

#### 其他

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `onServiceConnected()` | 224 | public |
| `onBackPressed()` | 495 | public |
| `onActivityResult()` | 503 | protected |
| `checkTTSEngines()` | 632 | private |
| `configureTTSParams()` | 763 | private |
| `generateFrequencyData()` | 850 | private |
| `generateWaveformData()` | 867 | private |
| `prepareDeletedDirectory()` | 1110 | private |
| `showManageStorageDialog()` | 1250 | private |
| `onClick()` | 1264 | public |
| `onClick()` | 1287 | public |
| `speakText()` | 1363 | private |
| `playRandomGreeting()` | 1391 | public |
| `onQuoteReceived()` | 1395 | public |
| `unbindServices()` | 1503 | private |
| `onServiceConnected()` | 1614 | public |
| `onReceive()` | 1634 | public |
| `onReceive()` | 1643 | public |
| `onReceive()` | 1654 | public |
| `onReceive()` | 1665 | public |
| `cleanExpiredCache()` | 1710 | private |
| `clearAppIconCache()` | 1722 | private |

#### 状态监听/回调

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `onGearChanged()` | 242 | public |
| `onTurnLightChanged()` | 248 | public |
| `onDoorChanged()` | 254 | public |
| `onSpeedChanged()` | 260 | public |
| `onLockStateChanged()` | 265 | public |

#### 初始化

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `onNeedStart360()` | 271 | public |
| `startLogcatMonitorService()` | 338 | private |
| `initDatabaseHelpers()` | 545 | private |
| `initWallpaperSettingsReceiver()` | 558 | public |
| `initRestartAppReceiver()` | 568 | public |
| `initWebView()` | 578 | private |
| `initTextToSpeech()` | 616 | public |
| `createTTSInstance()` | 647 | private |
| `onInit()` | 656 | public |
| `createDefaultTtsInstance()` | 676 | private |
| `onInit()` | 679 | public |
| `handleTTSInitFailure()` | 772 | private |
| `initMusicVisualizer()` | 785 | public |
| `createMusicVisualizer()` | 799 | private |
| `startMusicPlaybackCheck()` | 919 | public |
| `startWallpaperCarousel()` | 1012 | public |
| `startTimeUpdate()` | 1148 | public |
| `initSettings()` | 1156 | private |
| `scheduleDelayedStartupTasks()` | 1217 | public |
| `createDirectoriesInRoot()` | 1298 | private |
| `restartWallpaperCarousel()` | 1479 | public |
| `cancelDelayedStartupTasks()` | 1495 | private |
| `rescheduleDelayedStartupTasks()` | 1533 | public |
| `initAppListToDatabase()` | 1587 | private |
| `getRandomWallpaperFromFstartExcept00()` | 1765 | private |
| `getRandomWallpaperFromFstart00()` | 1814 | private |

#### 生命周期

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `onCreate()` | 287 | protected |
| `onStart()` | 417 | protected |
| `onResume()` | 429 | protected |
| `onPause()` | 442 | protected |
| `onStop()` | 454 | protected |
| `onDestroy()` | 466 | protected |

#### 时间日期

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `updateCarStateToFrontend()` | 357 | private |
| `updateVisualizationData()` | 830 | private |
| `stopTimeUpdate()` | 1463 | private |
| `getWeekDay()` | 1562 | private |

#### 权限相关

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `onRequestPermissionsResult()` | 521 | public |
| `requestStoragePermission()` | 1228 | public |

#### 保存/设置

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `setupTTSLanguage()` | 711 | private |
| `setFullscreenMode()` | 1450 | private |

#### 图片处理

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `setupSimplifiedChineseLanguage()` | 731 | private |
| `getCachedAppIconBase64()` | 1691 | private |
| `cacheAppIconBase64()` | 1705 | private |
| `encodeImageToBase64()` | 1932 | private |
| `drawableToBase64()` | 1999 | private |
| `drawableToBitmap()` | 2013 | private |
| `scaleBitmap()` | 2038 | private |
| `bitmapToBase64()` | 2050 | private |
| `getFirstLetter()` | 2064 | private |
| `isChineseChar()` | 2079 | private |
| `getChineseFirstLetter()` | 2085 | private |

#### 音乐相关

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `setupVisualizerListener()` | 807 | private |
| `onVisualizerUpdate()` | 813 | public |
| `registerMusicPlaybackReceiver()` | 894 | private |
| `checkMusicPlaybackState()` | 926 | public |
| `updateMusicPlaybackState()` | 941 | private |
| `updateMusicStatus()` | 983 | public |
| `requestAudioPermission()` | 1221 | public |
| `stopMusicCheck()` | 1487 | private |

#### 壁纸相关

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `pauseWallpaperCarousel()` | 1032 | public |
| `resumeWallpaperCarousel()` | 1040 | public |
| `deleteCurrentWallpaper()` | 1053 | public |
| `canDeleteCurrentWallpaper()` | 1077 | private |
| `performWallpaperDeletion()` | 1124 | private |
| `stopWallpaperCarousel()` | 1471 | public |
| `sendWallpaperSettingsChangedBroadcast()` | 1546 | public |
| `getRandomLocalWallpaper()` | 1727 | private |
| `getRandomOnlineWallpaper()` | 1847 | private |
| `getRandomOnlineWallpaperBase64()` | 1888 | private |
| `getRandomWallpaperUrl()` | 1958 | private |

#### 应用管理

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `preloadAppList()` | 1172 | private |
| `launchSystemHome()` | 1412 | public |

#### ADB相关

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `triggerUsbDebugAuthorization()` | 1600 | public |

---

### WebViewBridge.java ⭐
**路径**：`app/src/main/java/com/c11partner/desktop/bridge/WebViewBridge.java`  
**行数**：约 739 行  
**职责**：JS桥接层，提供50+个JS接口给前端调用

#### 保存/设置

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `setMediaSessionService()` | 62 | public |
| `setDriverSeatHeating()` | 316 | public |
| `setPassengerSeatHeating()` | 321 | public |
| `setDriverSeatVentilation()` | 326 | public |
| `setPassengerSeatVentilation()` | 331 | public |
| `setSteeringWheelHeating()` | 337 | public |
| `setMirrorHeating()` | 353 | public |
| `setPlaybackSpeed()` | 533 | public |
| `setRepeatMode()` | 553 | public |
| `setShuffleMode()` | 559 | public |
| `setScreenBrightness()` | 601 | public |
| `setAutoBrightness()` | 607 | public |

#### 壁纸相关

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `notifyWallpaperUpdate()` | 71 | public |
| `saveWallpaperCarouselSetting()` | 409 | public |
| `saveWallpaperCarouselSettingAsync()` | 414 | public |
| `saveWallpaperSwitchInterval()` | 419 | public |
| `saveWallpaperSwitchIntervalAsync()` | 424 | public |
| `sendWallpaperSettingsChangedBroadcast()` | 429 | public |
| `getRandomWallpaper()` | 434 | public |
| `getRandomWallpaperAsync()` | 437 | public |
| `getRandomWallpaperBase64()` | 442 | public |
| `getRandomWallpaperBase64Async()` | 447 | public |
| `updateWallpaperCategories()` | 452 | public |
| `getWallpaperSettings()` | 468 | public |
| `getWallpaperSettingsAsync()` | 471 | public |
| `pauseWallpaperCarousel()` | 476 | public |
| `resumeWallpaperCarousel()` | 479 | public |
| `deleteCurrentWallpaper()` | 482 | public |

#### 初始化

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `startCamera360()` | 79 | public |
| `startMusicVisualizer()` | 486 | public |
| `restartApp()` | 669 | public |
| `initializeAcStatus()` | 736 | public |

#### 车控功能

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `setCameraOverspeedLimit()` | 82 | public |
| `setLowBeamLight()` | 93 | public |
| `setRearFogLight()` | 98 | public |
| `setPositionLight()` | 103 | public |
| `setPedestrianAlert()` | 108 | public |
| `setDriveMode()` | 114 | public |
| `setGuardMode()` | 119 | public |
| `setRestMode()` | 124 | public |
| `setCampingMode()` | 129 | public |
| `setPowerSaveMode()` | 134 | public |
| `setSentinelMode()` | 139 | public |
| `setMaxCooling()` | 145 | public |
| `setAcEnabled()` | 150 | public |
| `setWindLevel()` | 160 | public |
| `setDriverTemp()` | 170 | public |
| `setPassengerTemp()` | 180 | public |
| `setCallVolume()` | 191 | public |
| `setNaviVolume()` | 201 | public |
| `setAmbientLightEnabled()` | 212 | public |
| `setAmbientLightColor()` | 222 | public |
| `setNightMode()` | 233 | public |
| `setWifiEnabled()` | 238 | public |
| `setBluetoothEnabled()` | 243 | public |
| `setVideoWhileDriving()` | 248 | public |
| `setSecondaryScreenEnabled()` | 259 | public |
| `sendVoiceCommand()` | 280 | public |
| `sendPrevTrack()` | 290 | public |
| `sendNextTrack()` | 299 | public |
| `setMusicVolume()` | 517 | public |

#### 状态获取

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `isCameraOverspeedLimitEnabled()` | 87 | public |
| `isAcEnabled()` | 155 | public |
| `isVideoWhileDrivingEnabled()` | 253 | public |
| `isVehicleLocked()` | 305 | public |
| `isScreenOn()` | 310 | public |
| `getAllApps()` | 362 | public |
| `getUserApps()` | 365 | public |
| `getSystemApps()` | 368 | public |
| `getAppInfo()` | 371 | public |
| `getAppIcon()` | 374 | public |
| `isAppInstalled()` | 377 | public |
| `getEnabledCategories()` | 460 | public |
| `isNotificationListenerEnabled()` | 536 | public |
| `getPlaylist()` | 547 | public |
| `getRepeatMode()` | 556 | public |
| `isShuffleEnabled()` | 562 | public |
| `isWifiConnected()` | 566 | public |
| `isBluetoothConnected()` | 569 | public |
| `getScreenBrightness()` | 604 | public |
| `isAutoBrightnessEnabled()` | 610 | public |
| `getSystemVersionInfo()` | 656 | public |
| `getAppVersionInfo()` | 659 | public |
| `getMemoryInfo()` | 662 | public |
| `getBatteryInfo()` | 665 | public |

#### 空调控制

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getWindLevel()` | 165 | public |
| `getDriverTemp()` | 175 | public |
| `getPassengerTemp()` | 185 | public |

#### 音量控制

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getCallVolume()` | 196 | public |
| `getNaviVolume()` | 206 | public |
| `getMusicVolume()` | 520 | public |

#### 灯光控制

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `isAmbientLightEnabled()` | 217 | public |
| `getAmbientLightColor()` | 227 | public |

#### 副屏相关

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `isSecondaryScreenEnabled()` | 264 | public |
| `updatePresentationCarState()` | 701 | public |

#### 语音控制

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `setSpeechEnabled()` | 270 | public |
| `isSpeechEnabled()` | 275 | public |

#### 其他

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `foldMirrors()` | 343 | public |
| `unfoldMirrors()` | 348 | public |
| `openAppInfo()` | 383 | public |
| `volumeUp()` | 523 | public |
| `volumeDown()` | 526 | public |
| `seekTo()` | 530 | public |
| `openRecentTasks()` | 630 | public |
| `openRecents()` | 633 | public |

#### 应用管理

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getAppList()` | 359 | public |
| `launchApp()` | 380 | public |
| `getQuickAppList()` | 386 | public |
| `addQuickApp()` | 389 | public |
| `removeQuickApp()` | 394 | public |
| `isQuickApp()` | 397 | public |
| `saveConfigApp()` | 400 | public |
| `getConfigApp()` | 405 | public |
| `saveSystemLauncherSetting()` | 572 | public |
| `saveSystemLauncherSettingAsync()` | 577 | public |
| `saveComponentConfig()` | 640 | public |
| `isComponentEnabled()` | 645 | public |
| `getAllComponentConfigs()` | 650 | public |

#### 时间日期

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `updateCategoryEnabled()` | 455 | public |
| `updateCategoryEnabledAsync()` | 463 | public |
| `getLunarCalendar()` | 597 | public |
| `setScreenTimeout()` | 614 | public |
| `getScreenTimeout()` | 617 | public |
| `updateTimeDisplay()` | 686 | public |

#### 音乐相关

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `stopMusicVisualizer()` | 489 | public |
| `isMusicPlaying()` | 492 | public |
| `getCurrentMusicName()` | 495 | public |
| `getCurrentMusicArtist()` | 498 | public |
| `getMusicProgressInfo()` | 501 | public |
| `getSystemMusicInfo()` | 504 | public |
| `playPauseMusic()` | 507 | public |
| `nextMusic()` | 510 | public |
| `prevMusic()` | 513 | public |
| `playSongAtIndex()` | 550 | public |

#### 系统设置

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `openNotificationListenerSettings()` | 541 | public |
| `saveBootGreetingSetting()` | 582 | public |
| `saveRandomModeSetting()` | 587 | public |
| `saveSpecifiedModeSetting()` | 592 | public |
| `openSystemSettings()` | 672 | public |
| `openAppSettings()` | 675 | public |

#### ADB相关

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `triggerUsbDebugAuthorization()` | 621 | public |
| `triggerWirelessAdbAuthorization()` | 624 | public |
| `executeAdbPermissionGrant()` | 627 | public |
| `setDefaultDesktopViaAdb()` | 636 | public |

---

### CarControlManager.java ⭐
**路径**：`app/src/main/java/com/c11partner/desktop/utils/CarControlManager.java`  
**行数**：约 832 行  
**职责**：车控功能管理类，三层控制模型的核心实现

#### 状态获取

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getInstance()` | 70 | public |
| `isAcEnabled()` | 327 | public |
| `isCameraOverspeedLimitEnabled()` | 504 | public |
| `isVideoWhileDrivingEnabled()` | 521 | public |
| `isVehicleLocked()` | 674 | public |
| `isScreenOn()` | 685 | public |

#### 初始化

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `startCamera360()` | 82 | public |

#### 车控功能

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `setLowBeamLight()` | 101 | public |
| `setRearFogLight()` | 117 | public |
| `setPositionLight()` | 133 | public |
| `setPedestrianAlert()` | 149 | public |
| `setDriveMode()` | 168 | public |
| `setGuardMode()` | 191 | public |
| `setRestMode()` | 207 | public |
| `setCampingMode()` | 223 | public |
| `setPowerSaveMode()` | 239 | public |
| `setSentinelMode()` | 255 | public |
| `sendVoiceCommand()` | 276 | public |
| `setMaxCooling()` | 294 | public |
| `setAcEnabled()` | 312 | public |
| `setWindLevel()` | 336 | public |
| `setDefrost()` | 360 | public |
| `setNightMode()` | 372 | public |
| `setWifiEnabled()` | 388 | public |
| `setBluetoothEnabled()` | 404 | public |
| `sendPrevTrack()` | 422 | public |
| `sendNextTrack()` | 438 | public |
| `setCameraOverspeedLimit()` | 497 | public |
| `setVideoWhileDriving()` | 514 | public |
| `setCallVolume()` | 530 | public |
| `setNaviVolume()` | 544 | public |
| `setMusicVolume()` | 558 | public |
| `setDriverTemp()` | 575 | public |
| `setPassengerTemp()` | 589 | public |
| `setAmbientLightEnabled()` | 605 | public |
| `setAmbientLightColor()` | 620 | public |
| `setSecondaryScreenEnabled()` | 641 | public |

#### 空调控制

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getWindLevel()` | 351 | public |
| `getDriverTemp()` | 582 | public |
| `getPassengerTemp()` | 596 | public |

#### 系统设置

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getGlobalInt()` | 456 | public |
| `getGlobalString()` | 468 | public |
| `setGlobalInt()` | 480 | public |

#### 音量控制

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getCallVolume()` | 537 | public |
| `getNaviVolume()` | 551 | public |
| `getMusicVolume()` | 565 | public |

#### 灯光控制

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `isAmbientLightEnabled()` | 612 | public |
| `getAmbientLightColor()` | 632 | public |

#### 副屏相关

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `isSecondaryScreenEnabled()` | 648 | public |

#### 语音控制

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `setSpeechEnabled()` | 657 | public |
| `isSpeechEnabled()` | 664 | public |

#### 保存/设置

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `setDriverSeatHeating()` | 695 | public |
| `setPassengerSeatHeating()` | 713 | public |
| `setDriverSeatVentilation()` | 731 | public |
| `setPassengerSeatVentilation()` | 749 | public |
| `setSteeringWheelHeating()` | 769 | public |
| `setMirrorHeating()` | 820 | public |

#### 其他

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `foldMirrors()` | 787 | public |
| `unfoldMirrors()` | 803 | public |

---

### LogcatMonitorService.java ⭐
**路径**：`app/src/main/java/com/c11partner/desktop/LogcatMonitorService.java`  
**行数**：约 621 行  
**职责**：日志监控后台服务，解析CAN信号，推送车辆状态

#### 状态获取

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getService()` | 86 | public |
| `addListener()` | 525 | public |
| `removeListener()` | 531 | public |
| `getCurrentState()` | 535 | public |

#### 生命周期

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `onCreate()` | 92 | public |
| `onStartCommand()` | 140 | public |
| `onBind()` | 146 | public |
| `onDestroy()` | 151 | public |

#### 初始化

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `createNotificationChannel()` | 104 | private |
| `createNotification()` | 122 | private |
| `startLogcatMonitoring()` | 163 | private |
| `notifyNeedStart360()` | 611 | private |

#### 日志监控

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `stopLogcatMonitoring()` | 178 | private |
| `processCanSignal()` | 313 | private |
| `parseTurnLightSignal()` | 370 | private |
| `parseTirePressureSignal()` | 476 | private |

#### 匿名内部类

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `run()` | 191 | public |
| `run()` | 544 | public |
| `run()` | 558 | public |
| `run()` | 572 | public |
| `run()` | 586 | public |
| `run()` | 600 | public |
| `run()` | 614 | public |

#### 其他

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `parseLogLine()` | 231 | private |
| `parseCanSignal()` | 289 | private |
| `parseSpeedSignal()` | 392 | private |
| `parseAcPageState()` | 426 | private |
| `parseBluetoothState()` | 442 | private |
| `parseScreenState()` | 459 | private |
| `trigger360IfNeeded()` | 492 | private |
| `extractInt()` | 503 | private |
| `extractIntAfter()` | 515 | private |
| `notifyGearChanged()` | 541 | private |
| `notifyTurnLightChanged()` | 555 | private |
| `notifyDoorChanged()` | 569 | private |
| `notifySpeedChanged()` | 583 | private |
| `notifyLockStateChanged()` | 597 | private |

#### 灯光控制

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `parseLowBeamLightSignal()` | 409 | private |

---

### AutomationEngine.java
**路径**：`app/src/main/java/com/c11partner/desktop/utils/AutomationEngine.java`  
**行数**：约 358 行  
**职责**：自动化场景引擎，管理12个预设自动化场景

#### 初始化

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `initTts()` | 62 | private |
| `onInit()` | 66 | public |

#### 自动化

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `isScenarioEnabled()` | 94 | public |
| `setScenarioEnabled()` | 115 | public |
| `getAllScenariosJson()` | 125 | public |
| `loadScenariosFromJson()` | 150 | public |

#### 状态获取

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getDefaultEnabled()` | 104 | private |

#### 状态监听/回调

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `onGearChanged()` | 175 | public |
| `onTurnLightChanged()` | 229 | public |
| `onDoorChanged()` | 243 | public |
| `onSpeedChanged()` | 265 | public |
| `onLockStateChanged()` | 289 | public |

#### 360全景

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `triggerCamera360()` | 303 | private |

#### 其他

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `triggerVoicePrompt()` | 319 | private |
| `destroy()` | 341 | public |

#### 保存/设置

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `resetCooldown()` | 354 | public |

---

### CarStatusPresentation.java
**路径**：`app/src/main/java/com/c11partner/desktop/CarStatusPresentation.java`  
**行数**：约 582 行  
**职责**：副屏车辆状态显示Presentation

#### 生命周期

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `onCreate()` | 72 | protected |
| `onStop()` | 90 | protected |

#### 初始化

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `startTimeUpdate()` | 98 | private |
| `createContentView()` | 146 | private |
| `createTireTextView()` | 330 | private |

#### 匿名内部类

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `run()` | 102 | public |
| `updateStatusIndicators()` | 534 | private |

#### 时间日期

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `stopTimeUpdate()` | 127 | private |
| `updateCurrentTime()` | 136 | private |
| `updateSpeed()` | 398 | public |
| `updateGear()` | 408 | public |
| `updateDoorStatus()` | 441 | public |
| `updateTurnLights()` | 458 | public |
| `updateTirePressure()` | 473 | public |
| `updateTireTextView()` | 490 | private |
| `updateBluetoothState()` | 518 | public |
| `updateLockState()` | 526 | public |
| `updateTime()` | 562 | public |
| `updateDisplay()` | 571 | private |

#### 灯光控制

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `updateLowBeamLight()` | 510 | public |

---

### MusicNotificationListenerService.java
**路径**：`app/src/main/java/com/c11partner/desktop/service/MusicNotificationListenerService.java`  
**行数**：约 228 行  
**职责**：音乐通知监听服务，从通知中提取音乐信息

#### 音乐相关

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `setMusicInfoListener()` | 51 | public |
| `isMusicPackage()` | 120 | private |
| `extractMusicInfo()` | 138 | private |
| `getCurrentMusicInfo()` | 198 | public |

#### 其他

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `onNotificationPosted()` | 56 | public |
| `onNotificationRemoved()` | 81 | public |

#### 状态获取

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `notifyListener()` | 187 | private |
| `getCurrentTitle()` | 211 | public |
| `getCurrentArtist()` | 218 | public |
| `isPlaying()` | 225 | public |

---

### SecondaryScreenManager.java
**路径**：`app/src/main/java/com/c11partner/desktop/utils/SecondaryScreenManager.java`  
**行数**：约 227 行  
**职责**：副屏管理类，检测和控制副屏

#### 状态获取

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getDisplays()` | 45 | public |
| `getDisplayStateName()` | 99 | private |
| `isShowing()` | 162 | public |

#### 副屏相关

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `getSecondaryDisplay()` | 55 | public |
| `hasSecondaryDisplay()` | 73 | public |
| `getSecondaryDisplayInfo()` | 80 | public |
| `showPresentation()` | 121 | public |
| `hidePresentation()` | 147 | public |
| `getCurrentPresentation()` | 169 | public |
| `isSecondaryScreenEnabled()` | 191 | public |
| `launchAppOnSecondaryScreen()` | 206 | public |

#### 车控功能

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `setSecondaryScreenEnabled()` | 178 | public |

#### 其他

| 函数名 | 行号 | 访问修饰符 |
|--------|------|-----------|
| `destroy()` | 222 | public |

---

## 🎨 前端核心文件索引（JS）

### index.js ⭐
**路径**：`app/src/main/assets/js/index.js`  
**行数**：约 4354 行  
**职责**：主JS文件，包含大部分前端逻辑

| 名称 | 行号 | 类型 |
|------|------|------|
| `AsyncCallbackManager` | 13 | 对象/管理器 |
| `generateId` | 22 | 方法 |
| `register` | 32 | 方法 |
| `execute` | 45 | 方法 |
| `addClickEffect()` | 72 | 函数 |
| `initSettingsModal()` | 138 | 函数 |
| `loadWallpaperSettings()` | 287 | 函数 |
| `loadSystemSettings()` | 388 | 函数 |
| `loadEnabledCategories()` | 403 | 函数 |
| `initCategoryCheckboxEvents()` | 445 | 函数 |
| `initRestartAppButton()` | 482 | 函数 |
| `initADBButton()` | 576 | 函数 |
| `showSettingsModal()` | 660 | 函数 |
| `hideSettingsModal()` | 707 | 函数 |
| `hideSettingsModal()` | 818 | 函数 |
| `initComponentVisibility()` | 825 | 函数 |
| `loadComponentConfigs()` | 847 | 函数 |
| `addComponentConfigEventListeners()` | 873 | 函数 |
| `updateComponentVisibility()` | 956 | 函数 |
| `generateAppData()` | 993 | 函数 |
| `initAlphabetNav()` | 1060 | 函数 |
| `initAlphabetNavFromData()` | 1085 | 函数 |
| `renderAppsList()` | 1110 | 函数 |
| `initAppsModal()` | 1185 | 函数 |
| `setAppListCache()` | 1203 | 函数 |
| `filterAppList()` | 1271 | 函数 |
| `updateAlphabetNavVisibility()` | 1303 | 函数 |
| `loadAppList()` | 1321 | 函数 |
| `renderAppsList()` | 1390 | 函数 |
| `initAlphabetNavFromData()` | 1472 | 函数 |
| `showAppsModal()` | 1511 | 函数 |
| `hideAppsModal()` | 1538 | 函数 |
| `resetAutoCloseTimer()` | 1562 | 函数 |
| `clearAutoCloseTimer()` | 1595 | 函数 |
| `showAddToQuickAppsDialog()` | 1613 | 函数 |
| `closeDialogFunc()` | 1660 | 函数 |
| `showRemoveFromQuickAppsDialog()` | 1699 | 函数 |
| `closeDialogFunc()` | 1734 | 函数 |
| `loadQuickApps()` | 1773 | 函数 |
| `loadQuickSwitches()` | 1856 | 函数 |
| `updateQuickSwitchStatus()` | 1913 | 函数 |
| `refreshQuickSwitchesStatus()` | 1927 | 函数 |
| `checkForOverlappingElements()` | 1939 | 函数 |
| `getEventListeners()` | 1992 | 函数 |
| `addTouchSwipeListener()` | 2010 | 函数 |
| `initConfigurableButtons()` | 2019 | 函数 |
| `getButtonDisplayName()` | 2240 | 函数 |
| `decreaseWindLevel()` | 2253 | 函数 |
| `increaseWindLevel()` | 2268 | 函数 |
| `setWindLevel()` | 2282 | 函数 |
| `getCurrentWindLevel()` | 2292 | 函数 |
| `decreaseTemperature()` | 2305 | 函数 |
| `increaseTemperature()` | 2329 | 函数 |
| `setWindLevel()` | 2353 | 函数 |
| `toggleAirConditioning()` | 2361 | 函数 |
| `toggleDefrost()` | 2390 | 函数 |
| `checkWifiStatus()` | 2448 | 函数 |
| `checkBluetoothStatus()` | 2475 | 函数 |
| `updateNetworkAndBluetoothStatus()` | 2502 | 函数 |
| `isElementOverBackground()` | 2509 | 函数 |
| `getElementsAtTouchPoint()` | 2522 | 函数 |
| `initHorizontalScroll()` | 2561 | 函数 |
| `initWallpaperDoubleClick()` | 2694 | 函数 |
| `handleWallpaperLongPress()` | 2770 | 函数 |
| `addTimeDisplayClickEvent()` | 2808 | 函数 |
| `toggleWallpaperCarousel()` | 2828 | 函数 |
| `restoreDefaultWallpaper()` | 2864 | 函数 |
| `updateMusicProgress()` | 2882 | 函数 |
| `updateMusicPlayPauseIcon()` | 2930 | 函数 |
| `toggleProgressLoop()` | 2960 | 函数 |
| `SystemMusicManager` | 2973 | 对象/管理器 |
| `init` | 2977 | 方法 |
| `bindControls` | 2984 | 方法 |
| `startUpdate` | 3019 | 方法 |
| `stopUpdate` | 3027 | 方法 |
| `updateMusicInfo` | 3035 | 方法 |
| `checkPermission` | 3087 | 方法 |
| `initMusicControls()` | 3112 | 函数 |
| `registerTimeUpdateListener()` | 3122 | 函数 |
| `updateState` | 3193 | 方法 |
| `updateGearIndicator` | 3210 | 方法 |
| `updateDoorIndicator` | 3232 | 方法 |
| `updateTurnIndicators` | 3256 | 方法 |
| `updateLockIndicator` | 3288 | 方法 |
| `updateSpeedDisplay` | 3304 | 方法 |
| `updateTirePressure` | 3312 | 方法 |
| `updateCamera360Indicator` | 3366 | 方法 |
| `init` | 3381 | 方法 |
| `addBlinkAnimation` | 3403 | 方法 |
| `togglePanel` | 3483 | 方法 |
| `showPanel` | 3495 | 方法 |
| `hidePanel` | 3508 | 方法 |
| `createPanel` | 3519 | 方法 |
| `toggleSwitch` | 3611 | 方法 |
| `setDriveMode` | 3687 | 方法 |
| `toggleSceneMode` | 3697 | 方法 |
| `refreshSwitchStates` | 3728 | 方法 |
| `AutomationManager` | 3844 | 对象/管理器 |
| `init` | 3951 | 方法 |
| `loadSettings` | 3959 | 方法 |
| `saveSettings` | 3997 | 方法 |
| `isEnabled` | 4017 | 方法 |
| `toggleScenario` | 4024 | 方法 |
| `getScenariosByCategory` | 4043 | 方法 |
| `showPanel` | 4060 | 方法 |
| `hidePanel` | 4073 | 方法 |
| `togglePanel` | 4083 | 方法 |
| `createPanel` | 4095 | 方法 |
| `refreshPanel` | 4169 | 方法 |
| `trigger` | 4192 | 方法 |
| `updateAcTemperature()` | 4228 | 函数 |
| `updateAcState()` | 4236 | 函数 |
| `updateWindLevel()` | 4242 | 函数 |
| `initAcTemperature()` | 4248 | 函数 |
| `VoiceTestManager` | 4276 | 对象/管理器 |
| `init` | 4277 | 方法 |
| `sendVoiceCommand` | 4319 | 方法 |
| `showResult` | 4339 | 方法 |

---

### weather.js
**路径**：`app/src/main/assets/js/weather.js`  
**行数**：约 188 行  
**职责**：天气模块

| 名称 | 行号 | 类型 |
|------|------|------|
| `initWeatherDisplay()` | 10 | 函数 |
| `updateWeatherInfo()` | 30 | 函数 |
| `fetchWeatherData()` | 38 | 函数 |
| `fetchWeatherDataWithXHR()` | 65 | 函数 |
| `updateWeatherDisplay()` | 90 | 函数 |
| `getWeatherDescription()` | 135 | 函数 |
| `getWeatherIcon()` | 171 | 函数 |

---

### music.js
**路径**：`app/src/main/assets/js/music.js`  
**行数**：约 710 行  
**职责**：音乐可视化和播放控制

| 名称 | 行号 | 类型 |
|------|------|------|
| `updateMusicVisualization()` | 46 | 函数 |
| `updateMusicStatus()` | 98 | 函数 |
| `updateMusicUI()` | 124 | 函数 |
| `updateMusicProgressBar()` | 155 | 函数 |
| `updateMusicPlayState()` | 193 | 函数 |
| `formatMusicTime()` | 227 | 函数 |
| `hasNotificationAccess()` | 238 | 函数 |
| `isMusicTimeSupported()` | 246 | 函数 |
| `clearFloats()` | 265 | 函数 |
| `clearCanvas()` | 273 | 函数 |
| `drawFloats()` | 293 | 函数 |
| `drawBars()` | 337 | 函数 |
| `drawEachFrame()` | 364 | 函数 |
| `visualize()` | 386 | 函数 |
| `initCustomAudioPlayer()` | 417 | 函数 |
| `updateProgressBar()` | 435 | 函数 |
| `updateCurrentSongName()` | 445 | 函数 |
| `updatePlayingState()` | 460 | 函数 |
| `toggleProgressLoop()` | 688 | 函数 |
| `updateMusicName()` | 701 | 函数 |

---

## 🚀 快速查找指南

### 按功能找代码

#### 我想修改车控功能
1. 先看 `CarControlManager.java`（核心实现）
2. 再看 `WebViewBridge.java`（JS接口）
3. 最后看前端 `QuickSwitchManager`（UI交互）

#### 我想修改车辆状态显示
1. 先看 `LogcatMonitorService.java`（日志解析）
2. 再看 `LeapMotorCarState.java`（状态数据）
3. 最后看前端 `CarStateManager`（UI显示）

#### 我想修改自动化场景
1. 先看 `AutomationEngine.java`（核心引擎）
2. 再看 `WebViewBridge.java`（配置接口）
3. 最后看前端 `AutomationManager`（配置UI）

#### 我想修改音乐模块
1. 先看 `MusicNotificationListenerService.java`（通知监听）
2. 再看 `WebViewBridge.java`（JS接口）
3. 最后看前端 `SystemMusicManager`（UI显示）

#### 我想修改副屏显示
1. 先看 `CarStatusPresentation.java`（副屏UI）
2. 再看 `SecondaryScreenManager.java`（副屏管理）
3. 最后看 `WebViewBridge.java`（控制接口）

---

### 常用修改场景速查

| 场景 | 首选文件 | 关键函数/对象 |
|------|----------|--------------|
| 添加新的车控功能 | CarControlManager.java | setXXX() 方法 |
| 添加新的JS接口 | WebViewBridge.java | @JavascriptInterface 方法 |
| 修改快捷开关 | index.js | QuickSwitchManager |
| 修改状态栏车辆状态 | index.js | CarStateManager |
| 添加自动化场景 | AutomationEngine.java | onXXXChanged() 方法 |
| 修改应用列表 | index.js | renderAppsList(), filterAppList() |
| 修改天气模块 | weather.js | fetchWeatherData(), updateWeatherDisplay() |
| 修改副屏显示内容 | CarStatusPresentation.java | updateXXX() 方法 |
| 修改音乐信息获取 | MusicNotificationListenerService.java | extractMusicInfo() |

---

**💡 小贴士**：代码变动后记得重新运行脚本更新索引！

```bash
python3 tools/generate_code_index.py
```