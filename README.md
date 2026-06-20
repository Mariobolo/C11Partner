# C11Partner 零跑C11专属车载桌面系统

一个基于 Android 的开源车载桌面启动器，专为零跑C11车机系统深度定制。

---

## 📢 **项目最新动态**

### ✅ **v1.2.0 - 车控功能增强（开发中，约98%完成）**
- ✅ **CarControlManager车控管理类** - 整合Intent主动控制 + Settings.Global读写（17项属性）
- ✅ **灯光控制** - 近光灯/后雾灯/示廓灯/行人警示音（Intent广播）
- ✅ **驾驶模式控制** - 舒适/运动/自定义/极致/经济/零跑模式（6种模式）
- ✅ **场景模式控制** - 守护/小憩/露营/省电/哨兵模式（5种场景）
- ✅ **系统设置控制** - 夜间模式/WiFi/蓝牙开关
- ✅ **空调控制增强** - 最大制冷模式 + 主副驾温度调节
- ✅ **方控按键模拟** - 上一曲/下一曲
- ✅ **Settings.Global读写** - 360超速限制/行驶视频解禁/音量/空调温度/氛围灯/副屏/语音播报（17项）
- ✅ **Logcat监控增强** - 近光灯/空调页面/蓝牙/屏幕/胎压胎温状态解析
- ✅ **前端状态栏车辆状态** - 档位/车门/转向灯/锁车/车速实时显示
- ✅ **前端快捷开关面板** - 14个快捷开关 + 6种驾驶模式 + 5种场景模式（长按360按钮呼出）
- ✅ **前端自动化场景配置** - 12个常用自动化场景可配置
- ✅ **后端自动化任务执行引擎** - TTS语音播报/倒车降音量/12个自动化场景
- ✅ **副屏投屏功能** - SecondaryScreenManager/CarStatusPresentation/副屏实时状态显示/胎压监测
- ✅ **桌面快捷开关模块** - 4个常用开关+全部按钮，状态与面板同步
- ✅ **实车测试6个问题修复** - 天气空白/快捷应用不显示/胎压数据/状态栏美化/时钟延迟

### ✅ **v1.1.0 - 核心功能已完成 (2026-06-18)**
- ✅ **零跑C11日志监控系统** - 实时解析CAN信号（档位、转向灯、车门）
- ✅ **360全景自动触发** - 转向灯/R挡自动启动（实车验证）
- ✅ **前台Service保活** - 不被系统杀死，常驻运行
- ✅ **WebViewBridge JS接口** - 前端可获取车辆状态
- ✅ **AndroidX 规范** - 全面清理比亚迪专用代码，100% AndroidX 标准
- ✅ **零跑C11空调控制** - 点击打开原生空调页面
- ✅ **比亚迪代码100%清理** - 前端/后端全部替换为零跑实现
- ✅ **车机控制能力分析** - 三层控制模型文档完成
- ✅ **图标素材规划** - Material Design Icons 推荐方案

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
- 🎨 **三层控制模型** - Intent主动控制 + Logcat被动监控 + Settings.Global读写

---

## 🎯 **车机控制三层模型**

### 控制逻辑架构

```
┌─────────────────────────────────────────────────────────┐
│                    应用层 (C11Partner)                   │
├─────────────────────────────────────────────────────────┤
│  ① Intent主动控制  │  ② Logcat被动监控  │  ③ Settings读写 │
├─────────────────────────────────────────────────────────┤
│                    零跑车机系统层                         │
└─────────────────────────────────────────────────────────┘
```

### 控制方式说明

| 控制方式 | 说明 | 权限要求 | 实时性 |
|---------|------|---------|--------|
| **Intent主动控制** | 发送系统广播/启动Activity | 普通权限 | 即时 |
| **Logcat被动监控** | 读取系统日志解析状态 | READ_LOGS | 秒级延迟 |
| **Settings.Global** | 读写系统全局属性 | WRITE_SECURE_SETTINGS | 即时 |

---

## 🚀 **已实现功能**

### 核心功能
- 🎵 **音乐控制组件** - 支持主流音乐播放器的控制和显示
- 🌤️ **天气显示** - 实时天气信息展示（默认商丘地区）
- 🗺️ **地图导航快捷入口**
- ❄️ **空调控制面板** - 点击打开零跑C11原生空调页面
- 🖼️ **壁纸切换系统**
- 📱 **快速启动应用**
- 🛞 **胎压监测显示**
- 🔧 **ADB一键权限授权**

### 零跑C11专属功能 (v1.1.0新增)
- 🚗 **实时车辆状态监控** - 基于logcat CAN信号解析
  - 档位状态（P/R/N/D）
  - 车门状态（6门）
  - 转向灯状态（左/右/双闪）
  - 天窗状态
  - 锁车状态
  - 车速显示
- 📸 **360全景自动触发** - 转向灯/R挡自动启动
- 🔔 **前台服务保活** - 后台常驻不被杀死
- 🌐 **JS接口开放** - 前端可获取车辆状态
- ❄️ **空调控制** - 打开原生空调页面

### 零跑C11车控功能 (v1.2.0新增)
- 💡 **灯光控制** - 近光灯/后雾灯/示廓灯/行人警示音
- 🏎️ **驾驶模式** - 舒适/运动/自定义/极致/经济/零跑模式（6种）
- 🎬 **场景模式** - 守护/小憩/露营/省电/哨兵模式（5种）
- 🔊 **音量控制** - 电话/导航/媒体音量独立调节
- 🌈 **氛围灯控制** - 开关+7种颜色
- 📺 **副屏控制** - 副屏显示开关
- 🎥 **行驶视频解禁** - 行驶中可播放视频
- 📸 **360限速控制** - 360全景超速限制开关
- 🌙 **夜间模式** - 系统夜间/白天模式切换
- 📶 **WiFi/蓝牙** - 快捷开关
- ❄️ **空调温度** - 主副驾温度独立调节
- 🔊 **语音播报** - 系统语音播报开关
- 📊 **状态栏车辆状态** - 档位/车门/转向灯/锁车实时显示
- ⚡ **快捷开关面板** - 14个快捷开关 + 驾驶模式 + 场景模式
- 🤖 **自动化场景配置** - 12个常用自动化场景
- 📺 **副屏投屏** - 副驾屏显示车速/档位/胎压/车辆状态
- ⚡ **桌面快捷开关** - 桌面常驻4个常用开关，一键直达

---

## 🎯 **开发路线图 (Roadmap)**

### 📋 **v1.2.0 - 🚧 车控功能增强（开发中，约95%完成）**

#### 🔴 **高优先级（已完成）**
- ✅ **CarControlManager车控管理类**
  - ✅ Intent主动控制：灯光、驾驶模式、场景模式、空调、WiFi、蓝牙、方控
  - ✅ Settings.Global读写：360限速、行驶视频、音量、空调温度、氛围灯、副屏、语音播报
- ✅ **Logcat监控增强**
  - ✅ 近光灯状态解析
  - ✅ 蓝牙连接状态解析
  - ✅ 屏幕状态解析
  - ✅ 空调页面状态解析
  - ✅ 胎压胎温解析（简化版）
- ✅ **前端状态栏车辆状态**
  - ✅ 档位指示器（P/R/N/D，不同颜色）
  - ✅ 车门指示器（开门数量，红色警告）
  - ✅ 转向灯指示器（闪烁动画）
  - ✅ 锁车指示器（🔒/🔓）
  - ✅ 360全景指示器
- ✅ **快捷开关面板**
  - ✅ 14个快捷开关（含副屏投屏）
  - ✅ 6种驾驶模式
  - ✅ 5种场景模式
  - ✅ Settings.Global类开关显示"开/关"状态
- ✅ **自动化场景配置界面**
  - ✅ 12个常用自动化场景
  - ✅ 按分类分组
  - ✅ 可独立开关
  - ✅ 前后端配置双向同步
- ✅ **后端自动化任务执行引擎**
  - ✅ 12个自动化场景触发执行
  - ✅ TTS中文语音播报
  - ✅ 倒车自动降音量
  - ✅ 防抖冷却机制
- ✅ **副屏投屏功能**
  - ✅ SecondaryScreenManager副屏管理类
  - ✅ CarStatusPresentation副屏显示
  - ✅ 车速/档位/胎压/车门/转向灯实时显示
  - ✅ 状态指示器（灯光/蓝牙/锁车）

#### 🚧 **待完成**
- [ ] **应用列表分类显示**（后端已完成，前端待实现）
- [ ] **副屏时间更新优化**（低优先级）
- [ ] **UI美化和图标替换**
- [ ] **更多实车测试和bug修复**
- [ ] **v1.2.0正式发布**

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

#### 🟡 **中优先级（三层控制模型）**
- ✅ **车机功能控制增强**
  - ✅ 空调控制（打开原生页面）
  - ✅ 车辆状态显示增强（天窗/近光灯/车速/蓝牙/屏幕）
  - ✅ Settings.Global功能（360超速限制/视频解禁/锁状态/音量/氛围灯/副屏）
  - [ ] 语音车控接口研究（儿童锁/车窗/座椅加热）

#### 🟢 **低优先级**
- [ ] **UI/UX整体美化 + 图标规范化**
  - [ ] 图标素材选型（推荐Material Design Icons）
  - [ ] 零跑C11专属主题配色
  - [ ] 动态壁纸支持
  - [ ] 卡片式布局优化
  - [ ] 动画效果增强
  - [ ] 暗黑模式适配

### 📋 **v1.3.0 - 远期规划**
- [ ] **OTA在线更新**
- [ ] **用户配置云同步**
- [ ] **第三方插件系统**
- [ ] **更多车控功能（车窗、座椅加热、儿童锁等）**

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
│   │   │   │   ├── index.js # 主逻辑（含CarStateManager、QuickSwitchManager、AutomationManager）
│   │   │   ├── images/      # 图片资源
│   │   │   └── index.html   # 主页面
│   │   ├── java/            # Android Java 代码
│   │   │   ├── com.c11partner.desktop/
│   │   │   │   ├── adb/     # ADB 相关模块
│   │   │   │   ├── bridge/  # WebView 通信桥
│   │   │   │   │   └── WebViewBridge.java  # JS接口（50+方法）
│   │   │   │   ├── database/# 数据库模块
│   │   │   │   ├── service/ # 后台服务
│   │   │   │   ├── utils/   # 工具类
│   │   │   │   │   ├── CarControlManager.java  # 车控管理类
│   │   │   │   │   ├── AutomationEngine.java   # 自动化场景引擎
│   │   │   │   │   └── SecondaryScreenManager.java  # 副屏管理类
│   │   │   │   ├── MainActivity.java
│   │   │   │   ├── LeapMotorCarState.java     # 车辆状态数据类
│   │   │   │   ├── CarStateListener.java      # 状态监听接口
│   │   │   │   ├── LogcatMonitorService.java  # 日志监控服务（核心）
│   │   │   │   ├── LeapMotorCamera360.java    # 360全景工具类
│   │   │   │   └── CarStatusPresentation.java # 副屏车辆状态显示
│   │   └── res/             # Android 资源
│   ├── dipartner.jks        # 内置签名文件
│   └── build.gradle         # 模块构建配置
├── .github/workflows/       # GitHub Actions CI
├── docs/                    # 文档目录
│   ├── LEAPMOTOR_LOG_ANALYSIS.md  # 实车日志分析报告
│   ├── C11_CAR_CONTROL_CAPABILITIES.md  # 车机控制能力分析
│   └── ICON_RESOURCES.md    # 图标素材推荐清单
├── logs_docs/               # 实车日志文件
├── PROJECT_STATUS.md        # ⭐ 项目状态总览（AI必读）
├── PROJECT_PLAN.md          # 详细项目开发计划
├── AI_ENTRY_GUIDE.md        # ⭐ AI项目引导文档（标准入口）
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

## 📚 **相关文档**

- 📄 [PROJECT_STATUS.md](./PROJECT_STATUS.md) - ⭐ **项目状态总览（AI必读）**
- 📄 [AI_ENTRY_GUIDE.md](./AI_ENTRY_GUIDE.md) - ⭐ **AI项目引导文档（标准入口）**
- 📄 [PROJECT_PLAN.md](./PROJECT_PLAN.md) - 详细项目开发计划
- 📄 [docs/LEAPMOTOR_LOG_ANALYSIS.md](./docs/LEAPMOTOR_LOG_ANALYSIS.md) - 实车日志分析报告
- 📄 [docs/C11_CAR_CONTROL_CAPABILITIES.md](./docs/C11_CAR_CONTROL_CAPABILITIES.md) - 车机控制能力分析
- 📄 [docs/ICON_RESOURCES.md](./docs/ICON_RESOURCES.md) - 图标素材推荐清单

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


---

## 📊 项目状态（2026-06-20）

| 指标 | 状态 |
|------|------|
| **版本** | v1.2.0（开发中，~99.9% 完成） |
| **测试覆盖率** | 68%（31 个测试全部通过） |
| **文档数量** | 17 份 |
| **代码索引** | 633 个函数/方法 |
| **模块化拆分** | 框架就绪，车控模块 91% 完成 |
| **CI/CD** | ✅ GitHub Actions 自动构建 |

### 最新进展
- ✅ 模块化拆分框架（BaseBridge + 3 个子 Bridge）
- ✅ 测试覆盖率大幅提升（8% → 68%）
- ✅ 完整文档体系（17 份文档）
- ✅ 代码索引自动生成工具
- ✅ API 文档自动生成工具
- ✅ 提交规范检查工具

---

*最后更新：2026-06-20*
