# 车机图标素材推荐清单

## 概述

本文档整理了适用于C11Partner车机桌面项目的开源图标素材库，供选择使用。

---

## 🏆 推荐图标库（按推荐度排序）

### 1. Material Design Icons (MDI) ⭐⭐⭐⭐⭐

| 项目 | 详情 |
|------|------|
| **图标数量** | 7,000+ |
| **协议** | Apache 2.0（完全开源免费，可商用） |
| **格式** | SVG, PNG, 字体图标 |
| **官网** | https://pictogrammers.com/library/mdi/ |
| **GitHub** | https://github.com/Templarian/MaterialDesign |

**优点：**
- ✅ 图标数量最多，涵盖车机所需的所有图标
- ✅ Apache 2.0 协议，完全免费商用
- ✅ 有专门的汽车/交通分类
- ✅ 风格统一，设计规范
- ✅ 支持多种格式，易于集成

**车机相关图标示例：**
- 车门：car-door, car-door-locked
- 空调：air-conditioner, fan, temperature-celsius
- 灯光：car-light-high, car-light-low, turn-signal-left, turn-signal-right
- 档位：gear-p, gear-r, gear-n, gear-d
- 胎压：tire, tire-alert
- 音乐：music, play, pause, skip-next, skip-previous
- 导航：navigation, map-marker, home, office
- 设置：cog, settings
- 蓝牙/网络：bluetooth, wifi, signal

---

### 2. Google Material Icons ⭐⭐⭐⭐

| 项目 | 详情 |
|------|------|
| **图标数量** | 2,500+ |
| **协议** | Apache 2.0 |
| **格式** | SVG, PNG, 字体图标 |
| **官网** | https://fonts.google.com/icons |
| **GitHub** | https://github.com/google/material-design-icons |

**优点：**
- ✅ Google官方出品，质量有保证
- ✅ 5种风格可选（填充、轮廓、圆角、锐利、双色调）
- ✅ 完全开源免费
- ✅ Android原生支持

**缺点：**
- ⚠️ 车机专用图标相对较少

---

### 3. Remix Icon ⭐⭐⭐⭐

| 项目 | 详情 |
|------|------|
| **图标数量** | 2,700+ |
| **协议** | Apache 2.0 |
| **格式** | SVG, PNG, 字体图标 |
| **官网** | https://remixicon.com/ |
| **GitHub** | https://github.com/Remix-Design/RemixIcon |

**优点：**
- ✅ 风格简洁现代
- ✅ 完全开源免费
- ✅ 中文友好

---

### 4. Feather Icons ⭐⭐⭐

| 项目 | 详情 |
|------|------|
| **图标数量** | 280+ |
| **协议** | MIT |
| **格式** | SVG |
| **官网** | https://feathericons.com/ |
| **GitHub** | https://github.com/feathericons/feather |

**优点：**
- ✅ 轻量级
- ✅ 风格简洁
- ✅ MIT协议

**缺点：**
- ⚠️ 图标数量较少，车机专用图标不足

---

### 5. Font Awesome (免费版) ⭐⭐⭐

| 项目 | 详情 |
|------|------|
| **图标数量** | 1,600+（免费版） |
| **协议** | SIL OFL 1.1（免费版可商用） |
| **格式** | SVG, 字体图标 |
| **官网** | https://fontawesome.com/ |
| **GitHub** | https://github.com/FortAwesome/Font-Awesome |

**优点：**
- ✅ 最流行的图标库
- ✅ 社区活跃

**缺点：**
- ⚠️ 免费版图标有限
- ⚠️ 部分车机图标需要Pro版

---

## 🚗 车机专用图标分类

### 车辆状态类
| 功能 | MDI图标名 | 说明 |
|------|-----------|------|
| 车门 | `car-door` | 车门图标 |
| 车门锁定 | `car-door-locked` | 锁车状态 |
| 档位P | `parking` | P挡 |
| 档位R | `backup-restore` 或自定义 | R挡 |
| 档位N | 自定义 | N挡 |
| 档位D | `drive-eta` 或自定义 | D挡 |
| 转向灯左 | `arrow-left-bold` 或自定义 | 左转向灯 |
| 转向灯右 | `arrow-right-bold` 或自定义 | 右转向灯 |
| 锁车 | `lock` | 上锁 |
| 解锁 | `lock-open` | 解锁 |
| 天窗 | `window-maximize` 或自定义 | 天窗 |

### 空调控制类
| 功能 | MDI图标名 | 说明 |
|------|-----------|------|
| AC开关 | `air-conditioner` | 空调 |
| 温度加 | `temperature-celsius` | 温度 |
| 温度减 | `thermometer-minus` | 降温 |
| 风量加 | `fan` | 风扇 |
| 风量减 | `fan-off` | 风扇关闭 |
| 除霜 | `car-defrost-front` | 前除霜 |
| 座椅加热 | `seat-heater` | 座椅加热 |

### 灯光控制类
| 功能 | MDI图标名 | 说明 |
|------|-----------|------|
| 近光灯 | `car-light-low` | 近光 |
| 远光灯 | `car-light-high` | 远光 |
| 示宽灯 | `lightbulb` | 小灯 |
| 自动大灯 | `auto-fix` | 自动 |

### 导航与位置类
| 功能 | MDI图标名 | 说明 |
|------|-----------|------|
| 导航 | `navigation` | 导航 |
| 回家 | `home` | 家 |
| 去公司 | `office-building` | 公司 |
| 地图 | `map-marker` | 位置 |

### 音乐控制类
| 功能 | MDI图标名 | 说明 |
|------|-----------|------|
| 播放 | `play` | 播放 |
| 暂停 | `pause` | 暂停 |
| 上一曲 | `skip-previous` | 上一首 |
| 下一曲 | `skip-next` | 下一首 |
| 音乐 | `music` | 音乐 |

### 系统功能类
| 功能 | MDI图标名 | 说明 |
|------|-----------|------|
| 蓝牙 | `bluetooth` | 蓝牙 |
| WiFi | `wifi` | 无线网络 |
| 设置 | `cog` | 设置 |
| 应用列表 | `apps` | 应用 |
| 任务列表 | `view-list` | 任务 |
| 360全景 | `camera` 或 `surround-sound` | 360影像 |
| 亮度 | `brightness-6` | 屏幕亮度 |
| 音量 | `volume-high` | 音量 |

---

## 🎯 推荐方案

### 方案一：Material Design Icons (MDI) - 推荐 ⭐⭐⭐⭐⭐

**理由：**
1. 图标数量最多（7000+），车机相关图标最丰富
2. Apache 2.0 协议，完全开源免费，可商用
3. 风格统一，设计规范
4. 社区活跃，持续更新
5. 支持SVG、PNG、字体等多种格式

**集成方式：**
- 方式1：下载SVG图标，放入项目assets目录
- 方式2：使用图标字体（减小体积）
- 方式3：按需下载单个图标

---

### 方案二：Google Material Icons + 自定义补充

**理由：**
1. Google官方出品，Android原生支持
2. 5种风格可选
3. 基础图标足够使用
4. 车机专用图标需要自己设计补充

---

### 方案三：Remix Icon

**理由：**
1. 风格简洁现代
2. 中文友好
3. 完全开源免费

---

## 📦 集成建议

### 推荐使用 MDI 图标库，集成方式：

1. **图标格式选择**：使用PNG格式（512x512或256x256）
   - 优点：兼容性好，无需额外处理
   - 缺点：体积稍大

2. **图标大小规范**：
   - 状态栏图标：24x24dp
   - 功能按钮图标：32x32dp
   - 大按钮图标：48x48dp

3. **图标颜色规范**：
   - 默认：白色 (#FFFFFF)
   - 激活状态：主题色（如蓝色 #2196F3）
   - 警告状态：红色 (#F44336)
   - 禁用状态：灰色 (#9E9E9E)

4. **目录结构**：
```
app/src/main/assets/images/
├── status/          # 状态栏图标
│   ├── bluetooth.png
│   ├── wifi.png
│   └── ...
├── control/         # 控制按钮图标
│   ├── ac.png
│   ├── fan.png
│   └── ...
├── nav/             # 导航图标
│   ├── home.png
│   ├── office.png
│   └── ...
└── music/           # 音乐图标
    ├── play.png
    ├── pause.png
    └── ...
```

---

## 🔗 相关链接

- Material Design Icons: https://pictogrammers.com/library/mdi/
- Google Material Icons: https://fonts.google.com/icons
- Remix Icon: https://remixicon.com/
- Feather Icons: https://feathericons.com/
- Font Awesome: https://fontawesome.com/
