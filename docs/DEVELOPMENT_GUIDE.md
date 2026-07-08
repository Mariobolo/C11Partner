# C11Partner 开发指南

> 🛠️ 本文档是开发者从零开始搭建环境到发布版本的完整流程
>
> 最后更新：2026-07-09

---

## 一、环境搭建

### 1.1 必需软件

| 软件 | 版本要求 | 用途 |
|------|---------|------|
| JDK | 17+ | Java 编译 |
| Android SDK | API 28+ (target 36) | Android 编译 |
| Android Studio | Hedgehog+ | IDE（可选） |
| Gradle | 8.9 | 构建工具（项目自带 gradlew） |
| ADB | 最新版 | 设备调试 |
| PowerShell | 5.0+ | 构建脚本 |

### 1.2 环境变量

```powershell
# 系统环境变量
JAVA_HOME=C:\Program Files\Java\jdk-17
ANDROID_HOME=C:\Users\{用户名}\AppData\Local\Android\Sdk
ANDROID_SDK_ROOT=%ANDROID_HOME%

# PATH 中添加
%JAVA_HOME%\bin
%ANDROID_HOME%\platform-tools
%ANDROID_HOME%\tools
```

### 1.3 项目配置

| 文件 | 说明 |
|------|------|
| `local.properties` | SDK 路径（不提交到 Git） |
| `gradle.properties` | Gradle 配置（JVM 内存等） |
| `app/build.gradle` | 应用模块配置 |

**local.properties 示例：**
```properties
sdk.dir=C:\\Users\\{用户名}\\AppData\\Local\\Android\\Sdk
```

### 1.4 验证环境

```powershell
# 验证 JDK
java -version

# 验证 SDK
adb version

# 验证项目配置
cd d:\Stu\Android\C11Partner
.\gradlew tasks
```

---

## 二、项目结构

```
C11Partner/
├── app/
│   ├── build.gradle                    # 应用模块配置
│   └── src/main/
│       ├── assets/                     # 前端资源
│       │   ├── index.html              # HTML 入口
│       │   ├── css/                    # 样式文件（7个）
│       │   ├── js/                     # JS 文件（28个）
│       │   └── images/                 # 图片资源
│       ├── java/com/c11partner/desktop/ # Java 后端
│       │   ├── MainActivity.java       # 主 Activity
│       │   ├── bridge/                 # 桥接层
│       │   ├── utils/                  # 工具类
│       │   ├── service/                # 后台服务
│       │   ├── database/               # 数据库
│       │   └── ...
│       ├── res/                        # Android 资源
│       └── AndroidManifest.xml        # 清单文件
├── docs/                               # 项目文档
├── build.gradle                        # 项目级配置
├── settings.gradle                     # 模块配置
└── gradle/                             # Gradle Wrapper
```

---

## 三、开发流程

### 3.1 日常开发循环

```
修改代码 → 编译 → 安装 → 验证 → 提交
```

### 3.2 编译安装

```powershell
# Debug 编译
.\gradlew assembleDebug

# 安装到已连接设备
adb install -r app\build\outputs\apk\debug\app-debug.apk

# 一条命令编译+安装
.\gradlew assembleDebug; adb install -r app\build\outputs\apk\debug\app-debug.apk
```

### 3.3 前端开发

前端文件在 `app/src/main/assets/` 目录下，修改后需要重新编译安装。

**快速预览（不需要编译）：**
1. 修改 assets 下的 JS/CSS/HTML
2. `adb install -r app\build\outputs\apk\debug\app-debug.apk`
3. 重新打开应用

**前端文件加载顺序（见 ARCHITECTURE.md §3.2）：**
- 修改 JS：注意依赖顺序，被依赖的文件先加载
- 修改 CSS：注意优先级，后加载的文件覆盖先加载的
- 修改 HTML：JS 在 `<head>` 中加载（注意 DOM 未就绪问题）

### 3.4 后端开发

Java 文件在 `app/src/main/java/com/c11partner/desktop/` 目录下。

**添加新的 JS 接口：**
1. 在对应 Bridge 类中添加 `@JavascriptInterface` 方法
2. 在 `WebViewBridge.java` 中添加委托方法
3. 更新 `JS_API_REFERENCE.md`
4. 前端通过 `Android.methodName()` 调用

### 3.5 验证清单

每次修改后，按以下清单验证：

| 验证项 | 方法 | 预期结果 |
|--------|------|---------|
| 编译通过 | `.\gradlew assembleDebug` | BUILD SUCCESSFUL |
| 应用启动 | 打开应用 | 无崩溃，桌面正常显示 |
| 时钟显示 | 观察桌面时钟 | 时间正确，秒数不闪烁 |
| 快捷应用 | 点击应用图标 | 正常启动 |
| 快捷开关 | 点击开关 | 状态切换正确 |
| 设置面板 | 点击设置按钮 | 面板正常显示，点击空白关闭 |
| 应用面板 | 点击"全部"按钮 | 应用列表正常加载 |
| 壁纸切换 | 双击桌面 | 壁纸切换 |
| 车辆状态 | 观察状态栏 | 档位/车门等状态正确 |

---

## 四、调试技巧

### 4.1 前端调试

**查看 JS 日志：**
```powershell
# 查看所有日志
adb logcat -s "chromium" "Console" "System.err"

# 过滤 WebView 控制台
adb logcat | Select-String "chromium"
```

**在 JS 中输出日志：**
```javascript
console.log('调试信息:', variable);
```

### 4.2 后端调试

**查看 Java 日志：**
```powershell
# 查看应用日志
adb logcat --pid=$(adb shell pidof com.c11partner.desktop)

# 过滤特定标签
adb logcat -s "C11Partner" "CarControl" "LogcatMonitor"
```

### 4.3 ADB 常用命令

```powershell
# 查看已连接设备
adb devices

# 安装应用
adb install -r app\build\outputs\apk\debug\app-debug.apk

# 授予权限
adb shell pm grant com.c11partner.desktop android.permission.READ_LOGS
adb shell pm grant com.c11partner.desktop android.permission.DUMP
adb shell pm grant com.c11partner.desktop android.permission.WRITE_SECURE_SETTINGS

# 启动应用
adb shell am start -n com.c11partner.desktop/.MainActivity

# 查看应用日志
adb logcat --pid=$(adb shell pidof com.c11partner.desktop)

# 模拟车辆信号（开发用）
adb shell am broadcast -a com.c11partner.CAR_STATE --es gear "D"
```

---

## 五、权限配置

### 5.1 首次安装

安装后需通过 ADB 授予三大核心权限：

```powershell
adb shell pm grant com.c11partner.desktop android.permission.READ_LOGS
adb shell pm grant com.c11partner.desktop android.permission.DUMP
adb shell pm grant com.c11partner.desktop android.permission.WRITE_SECURE_SETTINGS
```

### 5.2 用户授权

以下权限需用户手动授予：

| 权限 | 路径 | 说明 |
|------|------|------|
| 通知监听 | 设置 → 应用 → C11伙伴 → 通知使用权 | 读取音乐信息 |
| 悬浮窗 | 设置 → 应用 → C11伙伴 → 悬浮窗权限 | 悬浮窗显示 |
| 无障碍 | 设置 → 无障碍 → C11伙伴 | 模拟点击 |

---

## 六、Git 工作流

### 6.1 分支管理

| 分支 | 用途 |
|------|------|
| `main` | 生产发布分支 |
| `dev` | 开发分支 |
| `feature/*` | 功能分支 |
| `fix/*` | 修复分支 |

### 6.2 提交规范

见 [COMMIT_CONVENTION.md](COMMIT_CONVENTION.md)

**格式：**
```
<type>(<scope>): <subject>

<body>
```

**类型：**
- `feat`: 新功能
- `fix`: 修复
- `refactor`: 重构
- `docs`: 文档
- `style`: 格式
- `test`: 测试
- `chore`: 构建

### 6.3 提交流程

```powershell
# 1. 查看变更
git status

# 2. 暂存
git add <文件路径>

# 3. 提交
git commit -m "feat(quick-switch): 添加空调控制开关"

# 4. 推送
git push origin dev
```

---

## 七、常见问题

见 [FAQ.md](FAQ.md)

---

## 八、相关文档

| 文档 | 职责 |
|------|------|
| [架构文档](ARCHITECTURE.md) | 整体架构设计 |
| [代码索引](CODE_INDEX.md) | 函数级快速定位 |
| [JS 接口文档](JS_API_REFERENCE.md) | 前后端接口契约 |
| [项目状态](PROJECT_STATUS.md) | 当前项目真实状态 |
| [常见问题](FAQ.md) | 常见问题解答 |
| [提交规范](COMMIT_CONVENTION.md) | Git 提交规范 |

---

**文档版本**：v2.0
**最后更新**：2026-07-09
