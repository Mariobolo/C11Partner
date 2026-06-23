# CSS重复选择器统计表

**分析文件**: widgets.css
**总行数**: 10797
**分析时间**: 自动生成

## 重点选择器统计

| 选择器 | 出现次数 | 行号分布 |
|--------|----------|----------|
| .widget | 34 | 297, 384, 464, 1660, 1710, 1748, 1811, 1890, 1933, 1943 ... (+24 more) |
| .top-status-bar | 16 | 6, 1257, 1722, 1771, 1838, 1917, 1951, 4839, 5489, 5954 ... (+6 more) |
| .weather-widget | 14 | 899, 958, 1009, 5134, 5207, 5267, 6513, 6576, 6642, 6754 ... (+4 more) |
| .music-widget | 3 | 2184, 2240, 2291 |
| .quick-switch-item | 21 | 697, 742, 776, 7167, 7346, 7350, 8286, 8308, 8326, 8327 ... (+11 more) |

## 重复次数TOP 20选择器

| 排名 | 选择器 | 出现次数 |
|------|--------|----------|
| 1 | .widget | 34 |
| 2 | .map-widget | 29 |
| 3 | .quick-switch-item | 21 |
| 4 | .quick-app-item | 20 |
| 5 | .dock-btn | 20 |
| 6 | .card-view | 19 |
| 7 | .layout-left | 17 |
| 8 | .top-status-bar | 16 |
| 9 | .weather-widget | 14 |
| 10 | .control-buttons | 14 |
| 11 | .date-text | 13 |
| 12 | .car-status-indicators | 12 |
| 13 | #ffffff | 12 |
| 14 | .quick-app-icon | 12 |
| 15 | .quick-switch-icon | 12 |
| 16 | .stagger-loading | 12 |
| 17 | .lunar-calendar-text | 11 |
| 18 | .quick-apps-widget | 11 |
| 19 | .gear-indicator | 10 |
| 20 | .quick-switch-name | 10 |

## 重复代码块分析

### 疑似重复代码区域

基于选择器重复出现模式，以下区域可能存在重复代码：

#### .widget (34次出现)

- 首次出现: 第297行
- 重复峰值: 第6043行
- 末次出现: 第9731行
- ⚠️ **高风险**: 存在多次版本叠加痕迹，建议合并优化

#### .top-status-bar (16次出现)

- 首次出现: 第6行
- 重复峰值: 第5489行
- 末次出现: 第10296行
- ⚠️ **高风险**: 存在多次版本叠加痕迹，建议合并优化

#### .weather-widget (14次出现)

- 首次出现: 第899行
- 重复峰值: 第6576行
- 末次出现: 第6840行
- ⚠️ **高风险**: 存在多次版本叠加痕迹，建议合并优化

#### .quick-switch-item (21次出现)

- 首次出现: 第697行
- 重复峰值: 第8343行
- 末次出现: 第10693行
- ⚠️ **高风险**: 存在多次版本叠加痕迹，建议合并优化

### 疑似无用代码检测

以下选择器出现次数异常，可能存在无用代码：

- .top-status-bar: 16次定义
- .top-left-section: 6次定义
- .top-left-time: 8次定义
- .top-right-info: 6次定义
- .car-status-indicators: 12次定义
- .status-indicator: 7次定义
- .gear-indicator: 10次定义
- .network-status-icon: 7次定义
- .bluetooth-status-icon: 7次定义
- .network-5g-icon: 6次定义