# C11Partner 零跑C11专属车载桌面系统

一个基于 Android 的开源车载桌面启动器，专为零跑C11车机系统深度定制。

---

## 📢 **项目最新动态**

### ✅ **v1.1.0 - 核心功能已完成 (2026-06-17)**
- ✅ **零跑C11日志监控系统** - 实时解析CAN信号（档位、转向灯、车门）
- ✅ **360全景自动触发** - 转向灯/R挡自动启动（实车验证）
- ✅ **前台Service保活** - 不被系统杀死，常驻运行
- ✅ **WebViewBridge JS接口** - 前端可获取车辆状态
- ✅ **AndroidX 规范** - 全面清理比亚迪专用代码，100% AndroidX 标准

### ✅ **v1.0.0 - 已完成 (2026-06-17)**
- ✅ 从 DiPartner 开源项目 fork 并定制化
- ✅ **全局包名重命名**：`com.dipartner.desktop` → `com.c11partner.desktop`
- ✅ 应用名称改为「C11伙伴」
- ✅ 天气模块默认经纬度改为河南商丘 (34.44, 115.65)
- ✅ GitHub Actions CI 自动构建配置完成
- ✅ 代码语法错误修复完成
- ✅ 项目已上传至 GitHub：https://github.com/Mariobolo/C11Partner

---

## ✨ **项目简介**

C11Partner 是基于 DiPartner 开源项目定制的零跑C11专属车载桌面，针对零跑C11 Android 9 车机系统进行了深度优化和功能适配。

### ✅ **代码规范确认**
- 🎯 **100% AndroidX 标准** - 无旧 support 库依赖
- 🧹 **全面清理比亚迪专用代码** - 所有字段已重命名为通用命名
- 🔧 **零跑C11专属适配** - 基于实车日志的CAN信号解析

---

## 🚀 **已实现功能**

### 核心功能
- 🎵 **音乐控制组件** - 支持主流音乐播放器的控制和显示
- 🌤️ **天气显示** - 实时天气信息展示（默认商丘地区）
- 🗺️ **地图导航快捷入口**
- ❄️ **空调控制面板**
- 🖼️ **壁纸切换系统**
- 📱 **快速启动应用**
- 🛞 **胎压监测显示**
- 🔧 **ADB一键权限授权**

### 零跑C11专属功能 (v1.1.0新增)
- 🚗 **实时车辆状态监控** - 基于logcat CAN信号解析
- 📸 **360全景自动触发** - 转向灯/R挡自动启动
- 🔔 **前台服务保活** - 后台常驻不被杀死
- 🌐 **JS接口开放** - 前端可获取车辆状态

---

## 🎯 **开发路线图 (Roadmap)**

### 📋 **v1.1.0 - ✅ 核心功能已完成**

#### 🔴 **高优先级**
- ✅ **零跑C11日志状态监控系统**
  - ✅ Logcat实时日志抓取与解析
  - ✅ 车辆CAN信号解析（档位、转向灯、车门、天窗、锁车）
  - ✅ 转向灯/双闪状态监听
  - [ ] 车机系统状态监控（CPU、内存、温度）
  - [ ] 故障码读取与告警

- ✅ **360环视自动触发功能**
  - ✅ 打转向灯自动触发360全景
  - ✅ 倒车（R挡）自动触发360全景
  - [ ] 低速（<15km/h）自动触发
  - [ ] PIP画中画模式优化

- ✅ **ADB功能精简与重构（零跑专用）**
  - ✅ 核心功能完全不依赖ADB连接
  - ✅ 一键自我权限授权（三大核心权限）
  - [ ] ADB功能移至设置二级菜单
  - [ ] WiFi ADB自动检测与连接（5555端口）
  - [ ] 常用ADB命令快捷工具集

#### 🟡 **中优先级**
- [ ] **车机功能控制增强**
  - [ ] 空调温度/风量精准控制
  - [ ] 座椅加热/通风控制
  - [ ] 车窗/天窗控制
  - [ ] 氛围灯颜色调节
  - [ ] 驾驶模式切换

#### 🟢 **低优先级**
- [ ] **UI/UX整体美化**
  - [ ] 零跑C11专属主题配色
  - [ ] 动态壁纸支持
  - [ ] 卡片式布局优化
  - [ ] 动画效果增强
  - [ ] 暗黑模式适配

### 📋 **v1.2.0 - 远期规划**
- [ ] **跨品牌自动化任务执行引擎**
  - [ ] 可视化规则编辑器
  - [ ] 5类触发条件：车辆状态/时间/位置/应用状态/系统事件
  - [ ] 5类执行动作：车控/应用/系统/媒体/通知
  - [ ] 跨品牌兼容层：零跑C11、比亚迪、通用Android

- [ ] **副屏与仪表盘投屏系统**
  - [ ] 副屏应用管理：快速启动、悬浮窗、自定义显示、主屏副屏联动
  - [ ] 屏幕投屏功能：任意应用投屏、镜像投屏、分辨率自适应、帧率调节
  - [ ] 仪表盘专属投屏：导航投屏、音乐歌词、车速转速电量叠加、转向灯同步
  - [ ] 最高层透明遮罩系统：自定义透明图片遮罩、形状透明度可调、点击穿透、多图层叠加、动态遮罩效果

- [ ] **OTA在线更新**
- [ ] **用户配置云同步**
- [ ] **第三方插件系统**

---

## 🛠️ **技术栈**

- **前端**: HTML5 + CSS3 + JavaScript
- **后端**: Android Java (API 25+)
- **通信**: WebView Bridge 双向通信
- **数据库**: SQLite
- **目标平台**: 零跑C11 Android 9 (API 28)
- **CI/CD**: GitHub Actions 自动构建
- **✅ AndroidX**: 完全采用 AndroidX，无旧 support 库依赖

---

## 📋 **系统要求**

- Android 7.1+ (API 25)
- 零跑C11车机推荐：Android 9 (API 28)
- ✅ AndroidX 完全兼容 Android 9

---

## 🔧 **快速开始**

### 1. 克隆项目
```bash
git clone https://github.com/Mariobolo/C11Partner.git
cd C11Partner
```

### 2. 配置环境
- Android Studio 4.2+
- JDK 17
- Android SDK 30

### 3. 构建项目
```bash
chmod +x gradlew
./gradlew assembleDebug
```

### 4. 安装到设备
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## 📁 **项目结构**

```
C11Partner/
├── app/
│   ├── src/main/
│   │   ├── assets/          # Web 前端资源
│   │   │   ├── css/         # 样式文件
│   │   │   ├── js/          # JavaScript 文件
│   │   │   ├── images/      # 图片资源
│   │   │   └── index.html   # 主页面
│   │   ├── java/            # Android Java 代码
│   │   │   ├── com.c11partner.desktop/
│   │   │   │   ├── adb/     # ADB 相关模块
│   │   │   │   ├── bridge/  # WebView 通信桥
│   │   │   │   ├── database/# 数据库模块
│   │   │   │   ├── service/ # 后台服务
│   │   │   │   ├── utils/   # 工具类
│   │   │   │   ├── MainActivity.java
│   │   │   │   ├── LeapMotorCarState.java     # v1.1.0 车辆状态数据类
│   │   │   │   ├── CarStateListener.java      # v1.1.0 状态监听接口
│   │   │   │   ├── LogcatMonitorService.java  # v1.1.0 日志监控服务
│   │   │   │   └── LeapMotorCamera360.java    # v1.1.0 360全景工具类
│   │   └── res/             # Android 资源
│   ├── dipartner.jks        # 内置签名文件
│   └── build.gradle         # 模块构建配置
├── .github/workflows/       # GitHub Actions CI
├── docs/                    # 文档目录
│   └── LEAPMOTOR_LOG_ANALYSIS.md  # 实车日志分析报告
├── logs_docs/               # 实车日志文件
├── PROJECT_PLAN.md          # 详细项目开发计划
├── build.gradle             # 项目构建配置
└── README.md
```

---

## 🔑 **核心权限说明**

应用需要以下ADB授权权限以实现完整功能（零跑C11专用）：

```bash
# 1. 读取系统日志（用于车机状态监控、转向灯/档位监听、广播捕获）
adb shell pm grant com.c11partner.desktop android.permission.READ_LOGS

# 2. DUMP权限（获取系统状态、内存、CPU、Activity信息）
adb shell pm grant com.c11partner.desktop android.permission.DUMP

# 3. 写入系统设置（车控功能、Settings.Global属性、系统Intent发送）
adb shell pm grant com.c11partner.desktop android.permission.WRITE_SECURE_SETTINGS
```

> 💡 **零跑C11车控原理**：通过 `logcat` 捕获系统广播 + `Settings.Global` 读写系统属性 + `sendBroadcast` 发送车控Intent实现，无需比亚迪专用权限。**以上三个权限必须全部授予！**

---

## 🔏 **APK签名说明**

项目已内置签名文件 `app/dipartner.jks`，可直接构建Release版本：

- **签名文件**：`app/dipartner.jks`
- **密钥库密码**：`dipartner123`
- **密钥别名**：`dipartner`
- **密钥密码**：`dipartner123`

> ⚠️ **注意**：此为开发测试签名，正式发布请替换为您自己的签名文件！

---

## 📐 **代码规范**

为保证项目质量，我们遵循以下规范：

### Java 代码规范
- ✅ 使用驼峰命名法（camelCase）
- ✅ 类名使用大驼峰（PascalCase）
- ✅ 常量使用全大写下划线分隔
- ✅ 每个方法必须添加Javadoc注释
- ✅ 缩进使用4个空格
- ✅ 最大行宽120字符
- ✅ 100% AndroidX 标准，无旧 support 库依赖

### JavaScript 代码规范
- ✅ 使用ES6+语法
- ✅ 使用 `const` / `let` 替代 `var`
- ✅ 统一使用单引号
- ✅ 语句末尾加分号
- ✅ 缩进使用2个空格

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

---

## 🤝 **贡献指南**

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'feat: Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

---

## 📄 **开源协议**

本项目采用 [MIT](LICENSE) 协议开源。

---

## 🙏 **致谢**

- 感谢 DiPartner 原作者的开源项目
- 感谢所有为这个项目做出贡献的开发者

---

## 📞 **联系方式**

如有问题或建议，欢迎提交 Issue。

---

**🌟 如果这个项目对你有帮助，欢迎给个 Star 支持！**
