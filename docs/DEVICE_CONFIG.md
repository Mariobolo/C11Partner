# 零跑C11 初代车机设备配置文档

> 2023款零跑C11 初代车机：Leapmotor OS 1.0（底层 Android 9）精准开发向配置

---

## 一、底层系统

| 项目 | 说明 |
|------|------|
| 系统版本 | Leapmotor OS 1.0 |
| Linux 底层 | Android 9 (API 28) |
| 系统分区 | A/B OTA 分区（整车 OTA 升级） |
| 运行环境 | 定制 Android Automotive 精简车机系统 |
| 权限体系 | 安卓 9 老权限模型，不支持分区存储/作用域存储 |
| 调试支持 | adb 无线调试（需打开开发者选项）、logcat、crash 日志、ANR 日志 |

---

## 二、座舱硬件（23款 C11 初代 8155 平台）

### 核心算力硬件

| 组件 | 型号/规格 |
|------|----------|
| SOC | 高通骁龙 SA8155P（8155 车规版）6nm |
| CPU | 8 核（4xA76 大核 + 4xA55 小核） |
| GPU | Adreno 640 |
| 内存（RAM） | 12GB LPDDR4X |
| 内置存储（ROM） | 128GB UFS 2.1 |
| 网络 | 车载 4G 全网通 + 车载 WiFi 热点 |

### 三联屏硬件（UI 适配目标尺寸）

| 屏幕 | 尺寸 | 分辨率 | 方向 |
|------|------|--------|------|
| 仪表屏（主驾） | 10.25 英寸 | 1920x720 | 横屏 |
| **中控主屏（开发主要目标）** | **12.8 英寸** | **1920x1080** | **1080P** |
| 副驾娱乐屏 | 10.25 英寸 | 1920x720 | 横屏 |

- 屏幕像素密度：~180~220dpi
- 渲染形式：SurfaceFlinger 三屏独立图层渲染

### 图形 & 渲染特性

| 特性 | 说明 |
|------|------|
| 图形系统 | Android SurfaceView / TextureView |
| WebView 内核 | Android 9 自带 Chromium WebView（版本偏老，Chrome 74~78 区间） |
| WebGL 支持 | WebGL 1.0 支持良好，WebGL 2.0 支持有限 |
| CSS 硬件加速 | GPU 图层渲染，多图层叠加多了会掉帧 |
| 系统动画层级 | 系统桌面动画 + App 页面动画两层叠加 |

---

## 三、WebView / H5 环境约束

### 支持能力

- ES6 基础语法
- Flex、Grid 基础布局
- CSS3 动画、transform
- localStorage / sessionStorage
- 硬件加速渲染

### 不完美支持

- 部分 CSS 新属性（如 `gap` 在 Grid 以外的支持有限）
- 现代部分 JS 语法
- ES Module 完整版
- 部分 Fetch 特性

### 存储特性

- localStorage / sessionStorage 可用
- 无安全目录沙盒强限制（Android 9 优势）

### 渲染性能

- 硬件加速开启，但图层过多、全局通配符动画会明显掉帧
- 大量全局 CSS 动画、通配符选择器、多层 GPU 图层会导致卡顿

---

## 四、开发调试链路

```
真机车机 → 无线 adb(5555端口) → scrcpy 投屏 → logcat 实时抓日志 → 本地分析前端报错 & 渲染性能
```

### 调试命令

```bash
# 连接车机 adb
adb connect <车机IP>:5555

# scrcpy 投屏
scrcpy -s <车机IP>:5555

# 抓取应用日志
adb -s <车机IP>:5555 logcat -v time | grep -E "C11Partner|WebView|chromium"

# GPU 帧率日志
adb -s <车机IP>:5555 logcat | grep -E "SurfaceFlinger|GPU"

# 安装调试 APK
adb -s <车机IP>:5555 install -r app-debug.apk
```

---

## 五、开发者能力

| 能力 | 支持情况 |
|------|----------|
| USB 调试 | 支持 |
| 无线 adb 调试 | 支持（5555 端口） |
| 第三方 APK 安装 | 支持（adb install） |
| 分屏双应用 | 中控屏支持左右分屏 |
| 多账户 | 车主 ID 账户，桌面布局/设置跟随账户保存 |
| OTA 整车座舱升级 | A/B 分区升级（座舱域不跟智驾域一起更） |
| 语音控制 | "你好小零" 本地+云端双唤醒、可见即可说 |

---

## 六、项目适配参数速查

| 参数 | 值 |
|------|-----|
| 目标系统 | Android 9 API 28 |
| WebView 内核 | Chrome 74~78 区间 |
| 主屏基准尺寸 | 1920x1080 横屏 |
| 像素密度 | ~180~220dpi |
| JS 语法兼容 | ES6 基础，避免前沿新语法 |
| CSS 兼容 | CSS3 动画/transform 可用，新属性需前缀/降级 |
| 性能优化方向 | 三级动画特效分级、图层精简、减少全局重绘 |
