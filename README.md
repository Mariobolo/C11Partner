# C11Partner 零跑C11专属车载桌面系统

一个基于 Android 的开源车载桌面启动器，专为零跑C11车机系统深度定制。

## ✨ 项目简介

C11Partner 是基于 DiPartner 开源项目定制的零跑C11专属车载桌面，针对零跑C11 Android 9 车机系统进行了深度优化和功能适配。

## 🚀 功能特性

- 🎵 **音乐控制组件** - 支持主流音乐播放器的控制和显示
- 🌤️ **天气显示** - 实时天气信息展示（默认商丘地区）
- 🗺️ **地图导航快捷入口**
- 🚗 **零跑C11专属车控功能**
- ❄️ **空调控制面板**
- 🖼️ **壁纸切换系统**
- 📱 **快速启动应用**
- 🛞 **胎压监测显示**
- 🎬 **360环视PIP触发**（转向灯联动）
- 🔧 **ADB一键权限授权**

## 🛠️ 技术栈

- **前端**: HTML5 + CSS3 + JavaScript
- **后端**: Android Java (API 25+)
- **通信**: WebView Bridge 双向通信
- **数据库**: SQLite
- **目标平台**: 零跑C11 Android 9 (API 28)

## 📋 系统要求

- Android 7.1+ (API 25)
- 零跑C11车机推荐：Android 9 (API 28)

## 🔧 快速开始

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

## 📁 项目结构

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
│   │   │   │   └── MainActivity.java
│   │   └── res/             # Android 资源
│   └── build.gradle         # 模块构建配置
├── .github/workflows/       # GitHub Actions CI
├── build.gradle             # 项目构建配置
└── README.md
```

## 🔑 核心权限说明

应用需要以下ADB授权权限以实现完整功能：

```bash
# 读取系统日志
adb shell pm grant com.c11partner.desktop android.permission.READ_LOGS
# DUMP权限
adb shell pm grant com.c11partner.desktop android.permission.DUMP
# 写入系统设置
adb shell pm grant com.c11partner.desktop android.permission.WRITE_SECURE_SETTINGS
# 空调控制权限
adb shell pm grant com.c11partner.desktop android.permission.BYDAUTO_AC_COMMON
```

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📄 开源协议

本项目采用 [MIT](LICENSE) 协议开源。

## 🙏 致谢

- 感谢 DiPartner 原作者的开源项目
- 感谢所有为这个项目做出贡献的开发者

## 📞 联系方式

如有问题或建议，欢迎提交 Issue。
