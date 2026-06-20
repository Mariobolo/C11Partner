# C11Partner 开发指南

> 🛠️ 本文档描述如何搭建开发环境、调试和构建 C11Partner
>
> 最后更新：2026-06-20

---

## 一、环境搭建

### 1.1 开发环境要求

| 工具 | 版本要求 | 说明 |
|------|---------|------|
| **JDK** | 11+ | Android 开发必需 |
| **Android Studio** | Arctic Fox+ | 推荐 IDE |
| **Android SDK** | API 28+ | 零跑车机是 Android 9 (API 28) |
| **Gradle** | 7.0+ | 构建工具 |
| **ADB** | 最新版 | 调试必需 |

### 1.2 克隆项目

```bash
git clone https://github.com/Mariobolo/C11Partner.git
cd C11Partner
```

### 1.3 导入项目

1. 打开 Android Studio
2. 选择 "Open an Existing Project"
3. 选择 C11Partner 目录
4. 等待 Gradle 同步完成

### 1.4 签名配置

项目使用开发测试签名，正式发布请替换为自己的签名文件。

**当前签名信息**：
- 签名文件：`app/dipartner.jks`
- 密钥库密码：`dipartner123`
- 密钥别名：`dipartner`
- 密钥密码：`dipartner123`

**替换签名**：
1. 把自己的签名文件放到 `app/` 目录
2. 修改 `app/build.gradle` 中的签名配置
3. 或者在 `local.properties` 中配置

---

## 二、开发流程

### 2.1 代码索引（重要！）

修改代码前**务必先查代码索引**，节省上下文：

```bash
# 查看索引文档
docs/CODE_INDEX.md

# 重新生成索引（代码变动后）
python3 tools/generate_code_index.py
```

### 2.2 常用修改场景

#### 添加新的车控功能

**文件顺序**：
1. `CarControlManager.java` - 核心实现
2. `WebViewBridge.java` - JS 接口
3. `index.js` - 前端 UI (QuickSwitchManager)

**详细步骤**：
1. 在 `CarControlManager.java` 中添加控制方法
2. 在 `WebViewBridge.java` 中添加 `@JavascriptInterface` 方法
3. 在前端 `QuickSwitchManager` 中添加开关
4. 运行 `python3 tools/generate_code_index.py` 更新索引

#### 修改车辆状态显示

**文件顺序**：
1. `LogcatMonitorService.java` - 日志解析
2. `LeapMotorCarState.java` - 状态数据
3. `MainActivity.java` - 状态推送
4. `index.js` - 前端显示 (CarStateManager)

#### 添加自动化场景

**文件顺序**：
1. `AutomationEngine.java` - 核心逻辑
2. `WebViewBridge.java` - 配置接口
3. `index.js` - 配置 UI (AutomationManager)

### 2.3 前端开发

前端代码在 `app/src/main/assets/` 目录：

```
assets/
├── index.html          # 主页面
├── css/
│   ├── index.css       # 主样式
│   └── music.css       # 音乐模块样式
├── js/
│   ├── index.js        # 主 JS（3000+行，含多个管理器）
│   ├── weather.js      # 天气模块
│   ├── music.js        # 音乐模块
│   └── ...
└── images/             # 图片资源
```

**调试技巧**：
- 可以在浏览器中直接打开 `index.html` 预览 UI（部分功能需要 Android 环境）
- 使用 Chrome DevTools 调试 WebView

---

## 三、调试方法

### 3.1 ADB 连接

#### USB 连接
```bash
# 车机开启开发者选项和USB调试
# USB线连接车机
adb devices
```

#### 无线 ADB
```bash
# 先USB连接，然后切换到无线
adb tcpip 5555
adb connect <车机IP>:5555
```

### 3.2 三大核心权限授予

**必须通过 ADB 授予，普通安装无法获取：**

```bash
# 1. 读取系统日志（车辆状态监控必需）
adb shell pm grant com.c11partner.desktop android.permission.READ_LOGS

# 2. DUMP权限（获取系统状态）
adb shell pm grant com.c11partner.desktop android.permission.DUMP

# 3. 写入安全设置（车控功能必需）
adb shell pm grant com.c11partner.desktop android.permission.WRITE_SECURE_SETTINGS
```

**一键授权脚本**：
```bash
adb shell pm grant com.c11partner.desktop android.permission.READ_LOGS && \
adb shell pm grant com.c11partner.desktop android.permission.DUMP && \
adb shell pm grant com.c11partner.desktop android.permission.WRITE_SECURE_SETTINGS
```

### 3.3 日志查看

#### 查看应用日志
```bash
# Windows (旧版ADB不支持--package)
adb logcat -v time | findstr /i com.c11partner.desktop

# Linux/Mac
adb logcat -v time --pid=$(adb shell pidof com.c11partner.desktop)
```

#### 查看车机系统日志
```bash
# 查看CAN信号
adb logcat -v time | findstr /i C11CarSomeIp

# 查看转向灯
adb logcat -v time | findstr /i AroundService

# 查看胎压
adb logcat -v time | findstr /i "zza TPMS"

# 查看空调页面
adb logcat -v time | findstr /i LPSysUI
```

#### 清空日志
```bash
adb logcat -c
```

### 3.4 Crash 调试

```bash
# 查看崩溃日志
adb logcat -v time | findstr /i AndroidRuntime

# 查看ANR
adb logcat -v time | findstr /i "ANR in"
```

### 3.5 通知监听权限

音乐模块需要通知监听权限：

1. 安装应用后，打开应用
2. 点击音乐模块的"授权通知监听"
3. 跳转到设置页面，开启 C11Partner 的通知使用权

**验证权限**：
```bash
adb shell dumpsys notificationListeners
```

---

## 四、构建和发布

### 4.1 调试构建

```bash
# Debug 构建
./gradlew assembleDebug

# 输出位置
app/build/outputs/apk/debug/app-debug.apk
```

### 4.2 Release 构建

```bash
# Release 构建
./gradlew assembleRelease

# 输出位置
app/build/outputs/apk/release/app-release.apk
```

### 4.3 安装到车机

```bash
# 安装 APK
adb install -r app/build/outputs/apk/debug/app-debug.apk

# 启动应用
adb shell am start -n com.c11partner.desktop/.MainActivity
```

### 4.4 GitHub Actions CI/CD

项目已配置 GitHub Actions 自动构建：

- 触发条件：push 到 main 分支
- 构建产物：APK 文件
- 支持架构：arm64-v8a, x86_64

**查看构建状态**：
```
https://github.com/Mariobolo/C11Partner/actions
```

---

## 五、车机逆向分析技巧

### 5.1 查找系统属性

```bash
# 查看所有系统属性
adb shell getprop

# 搜索车控相关
adb shell getprop | findstr /i car
adb shell getprop | findstr /i strCar

# 查看 Settings.Global
adb shell settings list global
adb shell settings list global | findstr /i strCar
```

### 5.2 查找 Intent Action

```bash
# 查看已安装应用的 Activity
adb shell dumpsys package com.leapmotor.camera

# 查看系统广播
adb logcat -v time | findstr /i "Broadcast"

# 查看正在运行的 Activity
adb shell dumpsys activity activities
```

### 5.3 日志分析流程

1. **清空日志**：`adb logcat -c`
2. **执行操作**：在车机上操作某个功能（如开空调、打转向灯）
3. **抓取日志**：`adb logcat -v time > log.txt`
4. **分析日志**：搜索关键词，找到相关的 TAG 和 EventId

### 5.4 常用日志 TAG

| TAG | 用途 |
|-----|------|
| `C11CarSomeIp` | CAN信号（车门、档位、天窗、锁车） |
| `AroundService` | 转向灯信号 |
| `C11CarXml` | 灯光、空调、车速 |
| `LPSysUI` | 页面操作（空调页面、座椅页面） |
| `BleControlService` | 遮阳帘控制 |
| `BtMusicManager` | 蓝牙音乐 |
| `MediaTlog-CtrlService` | 多媒体控制 |
| `zza` | 胎压胎温（TPMSBean） |
| `LeapSystemAppService` | GPS位置 |

---

## 六、常见开发问题

### 6.1 编译错误

**WebViewBridge 缺少闭合括号**
- 症状：编译报错，提示多个方法解析失败
- 原因：某个方法缺少闭合大括号
- 解决：检查报错位置附近的方法，补上缺失的括号

**包名不一致**
- 症状：运行时找不到类
- 原因：部分文件还在用旧包名 `com.dipartner.desktop`
- 解决：全局搜索替换为 `com.c11partner.desktop`

### 6.2 运行时错误

**权限拒绝**
- 症状：调用车控功能失败，日志显示 SecurityException
- 原因：没有授予 WRITE_SECURE_SETTINGS 权限
- 解决：用 ADB 授予三大核心权限

**车控功能无效**
- 症状：调用接口返回成功，但车机没反应
- 原因：可能是推测的接口不对，或者需要其他条件
- 解决：实车测试验证，查看系统日志找正确的接口

### 6.3 前端问题

**JS 接口不生效**
- 症状：前端调用 JS 接口没反应
- 原因：WebViewBridge 方法名不匹配，或者缺少 @JavascriptInterface
- 解决：检查方法名和注解，确保一致

**页面加载失败**
- 症状：白屏或者显示错误
- 原因：assets 路径不对，或者 JS 有语法错误
- 解决：用 Chrome DevTools 调试 WebView

---

## 七、代码规范

### 7.1 Java 代码规范

- 使用 AndroidX，不要用旧的 support 库
- 方法名清晰易懂，见名知意
- 关键方法添加 Javadoc 注释
- 车控相关方法统一放在 `CarControlManager` 中

### 7.2 JS 代码规范

- 使用 ES6 语法
- 管理器对象用大驼峰命名
- 功能模块化，不要都堆在 index.js 里
- 关键逻辑添加注释

### 7.3 提交规范

```
<type>: <subject>

<body>
```

**type 类型**：
- `feat`：新功能
- `fix`：修复bug
- `docs`：文档更新
- `style`：代码格式调整
- `refactor`：重构
- `test`：测试相关
- `chore`：构建/工具相关

**示例**：
```
feat: 添加氛围灯控制功能
- CarControlManager 添加 setAmbientLightColor 方法
- WebViewBridge 添加对应 JS 接口
- 前端快捷开关添加氛围灯颜色选择
```

---

## 八、相关文档

- [架构设计](ARCHITECTURE.md) - 整体架构和模块划分
- [车控接口文档](CAR_CONTROL_API.md) - 详细的车控接口列表
- [代码索引](CODE_INDEX.md) - 函数级快速定位
- [常见问题](FAQ.md) - 常见问题解答

---

**文档版本**：v1.0  
**最后更新**：2026-06-20
