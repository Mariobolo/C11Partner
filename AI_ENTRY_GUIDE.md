# C11Partner AI项目引导文档 v2.1

> 🚨 **强制阅读顺序**：每次接手项目时，必须按以下顺序读取：
> 1. **PROJECT_STATUS.md** - 项目当前状态总览（最新进度、已完成、待办）
> 2. **本文件 (AI_ENTRY_GUIDE.md)** - 项目结构、代码导航、开发规范
> 3. **README.md** - 项目说明、功能列表、路线图
> 4. **PROJECT_PLAN.md** - 详细开发计划和里程碑
> 5. 相关技术文档（根据任务读取对应docs/下的文档）

> 🎯 **标准入口**：本文件是AI接手本项目的**唯一标准入口**。所有AI任务布置和工作安排，**必须先阅读本文档**，以快速了解项目、定位代码、高效展开工作。
>
> ⚡ **快速上手**：如果你只有30秒，直接看「🔥 极速导航」和「📊 关键数据速查表」。

---

## 🔥 极速导航（30秒上手）

### 我要改某个功能 → 直接去哪个文件？

| 我要做什么 | 核心文件 | 路径 |
|-----------|---------|------|
| **车辆状态/360全景** | LogcatMonitorService.java | `app/src/main/java/com/c11partner/desktop/LogcatMonitorService.java` |
| **空调控制** | WebViewBridge.java | `app/src/main/java/com/c11partner/desktop/bridge/WebViewBridge.java` |
| **前端页面UI** | index.html | `app/src/main/assets/index.html` |
| **前端JS逻辑** | index.js | `app/src/main/assets/js/index.js` |
| **前后端通信** | WebViewBridge.java + android_interface.js | bridge目录 + assets/js目录 |
| **车控功能** | CarControlManager.java | `app/src/main/java/com/c11partner/desktop/utils/CarControlManager.java` |
| **自动化场景** | AutomationEngine.java | `app/src/main/java/com/c11partner/desktop/utils/AutomationEngine.java` |
| **副屏功能** | SecondaryScreenManager.java + CarStatusPresentation.java | utils目录 + 根目录 |
| **ADB功能/权限** | AdbManager.java + PermissionGrantHelper.java | adb目录 + bridge目录 |
| **主Activity入口** | MainActivity.java | `app/src/main/java/com/c11partner/desktop/MainActivity.java` |
| **应用清单/权限** | AndroidManifest.xml | `app/src/main/AndroidManifest.xml` |
| **构建配置** | build.gradle | `app/build.gradle` |

### 最重要的3件事

1. **包名**：`com.c11partner.desktop`（所有地方都要一致！）
2. **目标平台**：Android 9 (API 28)，零跑C11车机
3. **架构**：Android Java + HTML/CSS/JS + WebView双向通信

---

## 📋 项目基本信息

| 项目 | 说明 |
|------|------|
| **项目名称** | C11Partner（零跑C11专属车载桌面） |
| **基于项目** | DiPartner 开源车载桌面（已深度定制） |
| **当前版本** | v1.2.0（开发中，约98%完成） |
| **项目状态** | 活跃开发中 |
| **包名** | `com.c11partner.desktop` |
| **应用名称** | C11伙伴 |
| **代码规范** | 100% AndroidX 标准，无旧support库 |
| **架构模式** | Android原生Java + HTML/CSS/JS前端 + WebView双向通信 |
| **minSdk** | 25 (Android 7.1) |
| **targetSdk** | 30 (Android 11) |
| **目标车机** | 零跑C11 Android 9 (API 28) |
| **分辨率** | 1920×1080 |
| **GitHub仓库** | https://github.com/Mariobolo/C11Partner |
| **开发者** | Mariobolo |

---

## 📊 关键数据速查表（AI必背）

### 🚗 CAN信号 EventId

| 功能 | EventId | 值定义 | 日志TAG |
|------|---------|--------|---------|
| **档位** | `1110` | 1=R, 2=N, 3=D, 4=P | `D/C11CarSomeIp` |
| **左前门** | `9123` | 0=关, 1=开 | `D/C11CarSomeIp` |
| **右前门** | `9124` | 0=关, 1=开 | `D/C11CarSomeIp` |
| **左后门** | `9125` | 0=关, 1=开 | `D/C11CarSomeIp` |
| **右后门** | `9126` | 0=关, 1=开 | `D/C11CarSomeIp` |
| **后备箱** | `9127` | 0=关, 1=开 | `D/C11CarSomeIp` |
| **前机盖** | `9128` | 0=关, 1=开 | `D/C11CarSomeIp` |
| **天窗** | `21201` | 0=关, 1=开 | `D/C11CarSomeIp` |
| **遮阳帘** | `21207` | 3=开, 4=关 | `D/BleControlService` |
| **锁车** | `1200` | 0=解锁, 1=上锁 | `D/C11CarSomeIp` |

### 💡 转向灯信号

| 功能 | 日志格式 | TAG |
|------|---------|-----|
| **左转向灯** | `dealTurnLeftLight mLeftLightSts 1/0` | `I/AroundService` |
| **右转向灯** | `dealTurnRightLight mRightLightSts 1/0` | `I/AroundService` |

### 📡 已确认的 Intent

| 功能 | Action | 类型 |
|------|--------|------|
| **360全景启动** | `com.leapmotor.camera_around` | Activity |
| **空调控制页面** | `com.leapmotor.action.AIR_CONTROL` | Activity |
| **方控按键模拟** | `com.leapmotor.action.METER.CTRL` | Broadcast |
| **语音车控接口** | `com.iflytek.autofly.handMessage` | Broadcast |

### 🔑 三大核心权限（必须ADB授权）

```bash
adb shell pm grant com.c11partner.desktop android.permission.READ_LOGS
adb shell pm grant com.c11partner.desktop android.permission.DUMP
adb shell pm grant com.c11partner.desktop android.permission.WRITE_SECURE_SETTINGS
```

> ⚠️ **重要**：这三个权限**无法普通申请**，必须通过ADB授权。没有权限时功能要优雅降级，不能崩溃。

---

## 🗂️ 项目结构完整导航

```
C11Partner/
├── app/
│   ├── src/main/
│   │   ├── assets/                    # 🔵 Web前端资源（重点）
│   │   │   ├── index.html             # 主页面（所有组件都在这里）
│   │   │   ├── css/                   # 样式文件
│   │   │   ├── js/                    # JavaScript文件
│   │   │   │   ├── index.js           # 主逻辑脚本
│   │   │   │   ├── android_interface.js  # Android接口封装
│   │   │   │   ├── weather.js         # 天气模块
│   │   │   │   ├── music.js           # 音乐模块
│   │   │   │   ├── wallpaper.js       # 壁纸模块
│   │   │   │   ├── map.js             # 地图导航
│   │   │   │   ├── datetime.js        # 日期时间
│   │   │   │   └── one-click-permission-patch.js  # 一键授权
│   │   │   ├── images/                # 图片/图标资源
│   │   │   ├── audio/                 # 音频资源
│   │   │   └── music/                 # 音乐资源
│   │   │
│   │   ├── java/com/c11partner/desktop/   # 🟢 Android Java代码（重点）
│   │   │   ├── MainActivity.java          # 主Activity（入口）
│   │   │   ├── LeapMotorCarState.java     # 车辆状态数据类
│   │   │   ├── CarStateListener.java      # 状态监听接口
│   │   │   ├── LogcatMonitorService.java  # 日志监控服务（核心）
│   │   │   ├── LeapMotorCamera360.java    # 360全景工具类
│   │   │   ├── adb/                       # ADB相关模块
│   │   │   │   ├── AdbManager.java
│   │   │   │   ├── AdbCommandProcessor.java
│   │   │   │   ├── AdbIntentForwarder.java
│   │   │   │   └── UsbDebugConnection.java
│   │   │   ├── bridge/                    # WebView通信桥
│   │   │   │   ├── WebViewBridge.java     # JS接口（核心！）
│   │   │   │   ├── AdbCommandExecutor.java
│   │   │   │   ├── AdbCommandHelper.java
│   │   │   │   └── PermissionGrantHelper.java
│   │   │   ├── database/                  # 数据库模块
│   │   │   ├── receiver/                  # 广播接收器
│   │   │   ├── service/                   # 后台服务
│   │   │   └── utils/                     # 工具类
│   │   │
│   │   ├── res/                       # Android资源文件
│   │   └── AndroidManifest.xml        # 应用清单文件
│   │
│   ├── build.gradle                   # 模块构建配置
│   └── dipartner.jks                  # 签名文件（测试用）
│
├── .github/workflows/                 # GitHub Actions CI配置
│   └── android-ci.yml                 # CI构建脚本
│
├── docs/                              # 📚 项目文档
│   ├── LEAPMOTOR_LOG_ANALYSIS.md      # 实车日志分析报告
│   ├── C11_CAR_CONTROL_CAPABILITIES.md  # 车机控制能力分析
│   └── ICON_RESOURCES.md              # 图标素材推荐
│
├── logs_docs/                         # 实车日志文件（参考用）
├── PROJECT_PLAN.md                    # 详细开发计划
├── README.md                          # 项目主文档
└── AI_ENTRY_GUIDE.md                  # ⭐ 本文档（AI入口）
```

---

## 🎯 按功能模块定位代码

### 1️⃣ 车辆状态监控 & 360全景自动触发

**核心文件**：
- `LogcatMonitorService.java` - 日志监控主服务（前台Service）
- `LeapMotorCarState.java` - 车辆状态数据类
- `CarStateListener.java` - 状态变化监听接口
- `LeapMotorCamera360.java` - 360全景启动工具类
- `MainActivity.java` - 集成服务和状态回调

**工作原理**：
1. 前台Service启动logcat进程，实时读取系统日志
2. 过滤关键TAG（C11CarSomeIp、AroundService等）
3. 解析CAN信号EventId，更新车辆状态
4. 状态变化时触发回调（如R挡/转向灯 → 启动360全景）
5. 3秒冷却防抖，避免频繁触发

**关键技术点**：
- 360全景启动Intent：`com.leapmotor.camera_around`
- 权限：`READ_LOGS`（需ADB授权）
- 前台Service保活，不被系统杀死

---

### 2️⃣ WebView 前后端通信

**核心文件**：
- `WebViewBridge.java` - Java侧JS接口（bridge目录）
- `android_interface.js` - JS侧Android接口（assets/js/）
- `MainActivity.java` - WebView初始化和配置

**Java → JS 接口清单**：

| 分类 | 方法 | 说明 |
|------|------|------|
| **车辆状态** | `getCarState()` | 获取车辆状态JSON |
| | `startCamera360()` | 手动触发360全景 |
| | `isLogServiceRunning()` | 检查日志服务状态 |
| **权限检查** | `hasReadLogsPermission()` | 检查READ_LOGS权限 |
| | `hasWriteSecureSettingsPermission()` | 检查WRITE_SECURE_SETTINGS |
| | `hasDumpPermission()` | 检查DUMP权限 |
| **空调控制** | `toggleAirConditioning()` | 打开零跑原生空调页面 |
| | `getAcInfo()` | 获取空调支持信息 |
| | `adjustTemperature()` | 温度调节 |
| | `adjustWindLevel()` | 风量调节 |
| | `toggleDefrost()` | 除霜切换 |
| | `initializeAcStatus()` | 空调初始化 |

**Java → JS 主动推送**：
```javascript
// 车辆状态变更时自动调用
window.updateCarState(JSON车辆数据)
```

> 💡 **注意**：后台主动推送，不需要前端轮询。前端不实现也不影响核心功能。

**JS → Java 接口清单（前端调用原生）**：

> 以下方法均通过 `Android.xxx()` 调用，WebViewBridge.java 中实现

| 分类 | 方法 | 说明 |
|------|------|------|
| **音乐控制** | `playPauseMusic()` | 播放/暂停（通用API） |
| | `nextMusic()` / `prevMusic()` | 上下一首（通用API） |
| | `playPause()` / `playNext()` / `playPrevious()` | 旧API兼容 |
| | `getSystemMusicInfo()` | 获取音乐信息JSON |
| | `getMusicProgressInfo()` | 获取播放进度JSON |
| | `seekTo(ms)` | 跳转播放位置 |
| **车控** | `toggleAC()` / `toggleAirConditioning()` | 空调开关 |
| | `toggleDefrost()` | 除霜 |
| | `increaseTemperature()` / `decreaseTemperature()` | 温度±1 |
| | `increaseWindSpeed()` / `decreaseWindSpeed()` | 风量±1 |
| | `navigateToHome()` / `navigateToCompany()` | 导航回家/公司 |
| | `setDriveMode(mode)` | 驾驶模式(0-5) |
| **快捷开关** | `setLowBeamLight(on)` | 近光灯 |
| | `setWifiEnabled(on)` / `setBluetoothEnabled(on)` | WiFi/蓝牙 |
| | `setVideoWhileDriving(on)` | 行驶视频 |
| | `setAmbientLightEnabled(on)` | 氛围灯 |
| **应用管理** | `launchApp(packageName)` | 启动应用 |
| | `getAppListAsync(callbackId)` | 异步获取应用列表 |
| | `getQuickAppList()` | 快捷应用列表 |
| | `addQuickApp()` / `removeQuickApp()` | 添加/移除快捷应用 |
| **壁纸** | `getWallpaperSettingsAsync(cb)` | 异步壁纸设置 |
| | `getRandomWallpaperBase64Async(cb)` | 异步随机壁纸 |
| **系统** | `showToast(msg)` | 显示Toast |
| | `restartApp()` | 重启应用 |
| | `saveComponentConfig(name, on)` | 组件配置 |
| **ADB** | `triggerWirelessAdbAuthorization()` | 无线ADB授权 |
| | `executeAdbPermissionGrant()` | 执行权限授予 |

> 📄 完整接口参考：`docs/JS_API_REFERENCE.md`

---

### 3️⃣ 空调控制

**核心文件**：
- `WebViewBridge.java` - 空调控制JS接口
- `AdbIntentForwarder.java` - ADB空调命令转发

**实现方式**：
- 点击空调按钮 → 启动零跑C11原生空调Activity
- ComponentName：`com.leapmotor.carcontrol/.presentation.ui.aircontrol.AirControlActivity`
- 备用方案：打开系统设置

**状态确认**：
- 通过Logcat监听 `LPSysUI` TAG确认空调页面开关
- 通过 `C11CarXml` TAG确认空调开关状态

---

### 4️⃣ ADB功能 & 权限授权

**核心文件**：
- `AdbManager.java` - ADB连接管理（adb目录）
- `UsbDebugConnection.java` - USB调试连接
- `AdbCommandProcessor.java` - ADB命令处理器
- `AdbIntentForwarder.java` - ADB Intent转发
- `PermissionGrantHelper.java` - 权限授权帮助（bridge目录）
- `one-click-permission-patch.js` - 一键授权前端

**设计原则**：
- ✅ 核心功能**完全不依赖ADB**连接
- ✅ ADB功能仅作为**高级增强**
- ✅ 普通用户无需ADB即可使用90%功能
- ✅ ADB功能隐藏在设置二级菜单

---

### 5️⃣ 前端页面（HTML/CSS/JS）

**核心文件**：
- `index.html` - 主页面（所有组件都在这里）
- `css/` - 样式文件目录
- `js/index.js` - 主逻辑脚本
- `js/android_interface.js` - Android接口封装

**前端组件清单**：

| 组件 | 说明 | 状态 |
|------|------|------|
| 音乐组件 | 黑胶唱片风格音乐播放器 | ✅ 已实现 |
| 天气组件 | 天气图标+温度+状况 | ✅ 已实现 |
| 导航组件 | 回家/公司快捷导航 | ✅ 已实现 |
| 空调组件 | 温度/风量/模式控制 | ✅ 已实现（打开原生页面） |
| 胎压组件 | 四轮胎压显示 | ✅ 已实现（静态） |
| 快捷启动 | 应用快捷方式 | ✅ 已实现 |
| 状态栏 | 蓝牙/WiFi/应用列表/任务列表/车辆状态文字显示 | ✅ 已实现 |
| 360全景按钮 | 手动触发360 | ✅ 已实现 |

---

### 6️⃣ 数据库（database目录）

- `AppDatabaseHelper.java` - 应用数据库
- `ComponentConfigDatabaseHelper.java` - 组件配置
- `ConfigAppDatabaseHelper.java` - 配置应用
- `QuickAppDatabaseHelper.java` - 快捷应用
- `WallpaperCategoryDatabaseHelper.java` - 壁纸分类
- `WallpaperSettingsDatabaseHelper.java` - 壁纸设置

---

### 7️⃣ 后台服务（service目录）

- `MediaSessionService.java` - 媒体会话服务
- `MusicNotificationListenerService.java` - 音乐通知监听
- `MusicService.java` - 音乐播放服务
- `MyAccessibilityService.java` - 无障碍服务
- `RecentsAccessibilityService.java` - 最近任务无障碍

**其他服务**：
- `LogcatMonitorService.java` - 日志监控服务（根目录，v1.1.0新增）

---

### 8️⃣ 工具类（utils目录）

- `AppUtils.java` - 应用工具类
- `InitManager.java` - 初始化管理
- `LunarCalendarUtils.java` - 农历日历
- `MusicInfoExtractor.java` - 音乐信息提取
- `MusicUtils.java` - 音乐工具
- `MusicVisualizer.java` - 音乐可视化
- `QuoteApiUtils.java` - 名言API
- `ServiceManager.java` - 服务管理
- `TaskManager.java` - 任务管理
- `WallpaperCategoryApiUtils.java` - 壁纸分类API
- `WallpaperDownloadUtils.java` - 壁纸下载

---

## 🔧 开发环境与构建

### 环境要求
- Android Studio 4.2+
- JDK 17
- Android SDK 30
- Gradle 4.2.2

### 构建命令
```bash
# Debug构建
./gradlew assembleDebug

# Release构建
./gradlew assembleRelease

# 安装到设备
adb install app/build/outputs/apk/debug/app-debug.apk
```

### CI/CD
- **配置文件**：`.github/workflows/android-ci.yml`
- **触发条件**：push到main/master分支、PR、手动触发
- **构建产物**：Debug APK + Release APK
- **超时时间**：20分钟

### 签名信息（测试用）
- **签名文件**：`app/dipartner.jks`
- **密钥库密码**：`dipartner123`
- **密钥别名**：`dipartner`
- **密钥密码**：`dipartner123`

> ⚠️ 正式发布请替换为自己的签名文件！

---

## 📐 代码规范与约定

### Java 代码规范
- ✅ 驼峰命名法（camelCase）
- ✅ 类名大驼峰（PascalCase）
- ✅ 常量全大写下划线分隔
- ✅ 每个方法必须添加Javadoc注释
- ✅ 缩进4个空格
- ✅ 最大行宽120字符
- ✅ **100% AndroidX标准**，无旧support库依赖

### JavaScript 代码规范
- ✅ ES6+语法
- ✅ 使用const/let替代var
- ✅ 统一使用单引号
- ✅ 语句末尾加分号
- ✅ 缩进2个空格

### Git 提交规范
```
feat: 新功能
fix: 修复bug
docs: 文档更新
style: 代码格式调整
refactor: 重构
test: 测试相关
chore: 构建/工具链变动
```

### 🔴 硬性约定（绝对不能违反）

1. **零跑C11车机是Android 9 (API 28)**，不要使用API 28以上的新特性
2. **READ_LOGS和WRITE_SECURE_SETTINGS必须通过ADB授权**，无法普通申请
3. **所有车控信号均来自实车抓取**，不得臆造
4. **核心功能不依赖ADB**，没有ADB也要能正常运行
5. **比亚迪专用代码已100%清理**，不得新增比亚迪相关代码
6. **包名必须是 `com.c11partner.desktop`**，所有地方保持一致
7. **PIP画中画有签名限制**，用系统广播触发原生360，不要自己做PIP

---

## 📝 常见任务标准流程

### 任务1：添加新的车辆状态监控

**标准步骤**：
1. 在 `LeapMotorCarState.java` 添加状态字段和getter/setter
2. 在 `CarStateListener.java` 添加回调方法
3. 在 `LogcatMonitorService.java` 添加日志解析逻辑
4. 在 `MainActivity.java` 实现回调处理
5. 在 `WebViewBridge.java` 添加JS接口（如需前端获取）
6. 更新前端JS接收数据（如需前端显示）

**检查清单**：
- [ ] 字段类型正确吗？
- [ ] 默认值合理吗？
- [ ] 日志解析正则对吗？
- [ ] 有防抖/冷却机制吗？
- [ ] 没有权限时会崩溃吗？

---

### 任务2：添加新的车控功能

**标准步骤**：
1. 确认控制方式（Intent/Settings/ADB）
2. 在对应工具类或Bridge中添加控制方法
3. 添加JS接口（如需前端调用）
4. 添加状态反馈（通过Logcat被动确认）
5. 更新前端UI和交互

**检查清单**：
- [ ] 控制方式是最优的吗？
- [ ] 失败时有降级方案吗？
- [ ] 有异常捕获吗？
- [ ] 状态反馈准确吗？

---

### 任务3：前端UI修改

**标准步骤**：
1. 修改 `index.html` 中的组件结构
2. 修改 `css/` 中的样式文件
3. 修改 `js/` 中的对应逻辑脚本
4. 如需调用Android功能，通过 `android_interface.js`
5. 测试在车机上的显示效果（1920×1080）

**检查清单**：
- [ ] 布局在1920×1080下正常吗？
- [ ] 图标大小合适吗？
- [ ] 点击区域够大吗？（车机触控要大）
- [ ] 颜色对比度够吗？
- [ ] 有动画效果吗？会不会卡？

---

### 任务4：添加新的设置项

**标准步骤**：
1. 在 `index.html` 添加设置UI
2. 在对应JS文件中添加保存/读取逻辑
3. 如需持久化，使用SharedPreferences或数据库
4. 在 `WebViewBridge.java` 添加Java侧接口（如需）

**检查清单**：
- [ ] 默认值合理吗？
- [ ] 保存后重启还在吗？
- [ ] 有范围限制吗？

---

### 任务5：排查编译错误

**标准步骤**：
1. 查看错误信息，定位具体文件
2. 检查语法错误（花括号、分号、引号等）
3. 检查import语句是否正确
4. 检查包名是否正确（com.c11partner.desktop）
5. 检查AndroidX依赖是否正确
6. 本地构建验证：`./gradlew assembleDebug`

**常见错误速查**：

| 错误现象 | 可能原因 | 解决方案 |
|---------|---------|---------|
| 找不到符号 | import错误或包名不对 | 检查包名和import |
| 语法错误 | 花括号/引号不匹配 | 检查最近修改的代码 |
| 方法不存在 | 方法被删了或改名了 | 检查方法名和参数 |
| 权限拒绝 | 缺少权限声明 | 检查AndroidManifest.xml |
| 类找不到 | 包名重命名漏了 | 全局搜索旧包名 |

---

## ⚠️ 踩坑记录 & 注意事项

### 1. 包名问题（最常见！）
- 所有Java文件的package必须是 `com.c11partner.desktop`
- 所有import语句必须对应新包名
- AndroidManifest.xml的package必须正确
- build.gradle的applicationId必须正确
- JS中的包名引用也要更新
- **检查方法**：全局搜索 `dipartner` 看有没有残留

### 2. 权限问题
- READ_LOGS、DUMP、WRITE_SECURE_SETTINGS 必须通过ADB授权
- 普通申请方式无效，不要在AndroidManifest里申请
- 应用启动时**不要**强制检查这些权限
- 功能降级：没有权限时优雅降级，提示用户但不崩溃

### 3. Android 9 兼容性
- minSdk 25，targetSdk 30
- 不要使用API 28以上的新特性
- AndroidX完全兼容Android 9，放心用
- 前台Service需要NotificationChannel（Android 8+）

### 4. WebView 性能
- 车机WebView性能有限，比手机差很多
- 避免过多DOM操作和复杂动画
- 图片资源要优化大小，不要太大
- 使用硬件加速
- 避免频繁重绘

### 5. 比亚迪代码清理
- 项目已100%清理比亚迪专用代码
- 不要新增任何比亚迪相关代码
- 如发现残留，立即清理并替换为零跑实现
- **检查方法**：全局搜索 `byd`、`BYD`、`比亚迪`

### 6. 实车数据问题
- 所有车控信号**必须**来自实车抓取
- 不得臆造、猜测信号
- 不确定的功能标记为「待验证」
- 实车日志在 `logs_docs/` 目录，可参考

---

## 🧪 代码修改验证清单

每次修改代码后，必须检查：

- [ ] 能正常编译通过吗？
- [ ] 没有语法错误吗？
- [ ] 包名都对吗？（全局搜索dipartner确认无残留）
- [ ] 比亚迪代码都清理了吗？（全局搜索byd确认）
- [ ] 没有权限时会崩溃吗？
- [ ] 异常都捕获了吗？
- [ ] 注释写了吗？
- [ ] 符合代码规范吗？
- [ ] 相关文档更新了吗？

---

## 📚 相关文档索引

| 文档 | 路径 | 说明 | 何时看 |
|------|------|------|--------|
| **项目主文档** | `README.md` | 项目介绍、功能、路线图 | 第一次接手时 |
| **详细开发计划** | `PROJECT_PLAN.md` | 完整开发roadmap、进度跟踪 | 规划任务时 |
| **实车日志分析** | `docs/LEAPMOTOR_LOG_ANALYSIS.md` | CAN信号、TAG、Intent详细分析 | 做车控功能时 |
| **车机控制能力** | `docs/C11_CAR_CONTROL_CAPABILITIES.md` | 三层控制模型、功能清单 | 规划车控功能时 |
| **图标素材推荐** | `docs/ICON_RESOURCES.md` | 开源图标库推荐、车机图标分类 | 做UI美化时 |
| **CI配置** | `.github/workflows/android-ci.yml` | GitHub Actions自动构建 | CI出问题时 |

---

## 🎯 AI工作标准流程

### 接手新任务时

1. **先读本文档**（5分钟）- 了解项目概览和结构
2. **定位相关文件** - 根据功能分类找到对应代码
3. **阅读相关文档** - 查看docs/下的详细文档
4. **理解现有实现** - 先看懂已有代码再修改
5. **遵循代码规范** - 保持风格一致
6. **测试验证** - 确保修改后能正常编译运行

### 修改代码时

1. 先确认Git状态，确保在正确的分支上
2. 小步提交，每个提交一个功能点
3. 遵循Git提交规范（feat/fix/docs等）
4. 修改后检查是否有编译错误
5. 更新相关文档（如有必要）
6. 验证清单全部通过

### 遇到问题时

1. 先查看本文档的「踩坑记录」
2. 查看实车日志文件（logs_docs/）参考
3. 查看相关文档的详细说明
4. 参考已有类似功能的实现方式
5. 如不确定，先做最小验证，不要大改
6. 实在解决不了，说明清楚卡点

---

## 🔗 快速链接

- **GitHub仓库**：https://github.com/Mariobolo/C11Partner
- **CI构建状态**：https://github.com/Mariobolo/C11Partner/actions
- **Issues**：https://github.com/Mariobolo/C11Partner/issues
- **原项目**：https://gitee.com/hex_code/DiPartner

---

## 📝 文档维护说明

> 💡 **本文档是活文档**，需要随着项目发展持续更新。
>
> **更新时机**：
> - 新增功能模块时
> - 发现新的坑点时
> - 有新的实车数据时
> - 代码结构大调整时
>
> **更新原则**：
> - 保持简洁，不要太啰嗦
> - 重点突出，AI能快速找到
> - 实用为主，不要形式主义
> - 过时信息及时删除或标记

---

**文档版本**：v2.0  
**创建日期**：2026-06-18  
**最后更新**：2026-06-19  
**维护者**：AI协作团队

---

> ⭐ **记住**：本文档是AI项目引导的标准入口。每次接手新任务前，请先阅读本文档。如有更新，请同步更新本文档，保持信息准确。
