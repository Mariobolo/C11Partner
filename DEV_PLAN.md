# C11Partner 开发计划文档

> 📋 本文档记录待办任务列表、状态和交接信息，用于跨会话状态延续
>
> 最后更新：2026-07-09

## 任务状态说明

- `[ ]` 待执行
- `[~]` 进行中
- `[x]` 已完成
- `[!]` 已中断（需阅读交接信息）

---

## 阶段总览

| 阶段 | 名称 | 状态 | 说明 |
|------|------|------|------|
| 阶段1 | 文档整理 | ✅ 已完成 | 重写6个核心文档，删除12个过期文档 |
| 阶段2 | 前端清理 | 🚧 进行中 | 删除冗余中间层，合并到 index.js |
| 阶段3 | 接口对齐 | ⏳ 待执行 | 前后端接口文档一致验证 |
| 阶段4 | CSS 清理 | ⏳ 待执行 | 去除重复样式 |
| 阶段5 | 功能验证 | ⏳ 待执行 | 编译安装，逐一测试所有功能 |

---

## 阶段1：文档整理 ✅ 已完成

- [x] 重写 ARCHITECTURE.md（前后端统一架构）
- [x] 重写 PROJECT_STATUS.md（代码统计和待修复问题）
- [x] 重写 DEVELOPMENT_GUIDE.md（开发指南）
- [x] 重写 CODE_INDEX.md（函数级索引）
- [x] 重写 JS_API_REFERENCE.md（接口契约文档）
- [x] 更新 docs/README.md（文档中心入口）
- [x] 删除12个过期文档（REFACTORING_PLAN.md 等）

---

## 阶段2：前端清理 🚧 进行中

**目标**：移除冗余中间层（panel-controller.js, ui-initializer.js），简化初始化流程，减少代码量约35%

### 步骤2.1：删除 panel-controller.js，合并到 index.js ✅ 已完成
- **状态**：`[x]` 已完成（2026-07-09）
- **文件**：`app/src/main/assets/js/panel-controller.js`（106行，已删除）
- **操作**：
  1. ✅ 读取 panel-controller.js 内容
  2. ✅ 将6个函数（showSettingsModal/hideSettingsModal/showAppsModal/hideAppsModal/initWallpaperDoubleClick/handleWallpaperLongPress）合并到 index.js
  3. ✅ 从 index.html 移除 panel-controller.js 引入
  4. ✅ 删除 panel-controller.js 文件
  5. ✅ 编译验证通过（BUILD SUCCESSFUL）
- **提交记录**：cb35a92 refactor: 前端bug修复并合并panel-controller.js到index.js

### 步骤2.2：删除 ui-initializer.js，合并到 index.js
- **状态**：`[ ]` 待执行
- **文件**：`app/src/main/assets/js/ui-initializer.js`（427行）
- **操作**：
  1. 读取 ui-initializer.js 内容
  2. 将核心方法（loadQuickApps/loadQuickSwitches/updateNetworkAndBluetoothStatus 等）合并到 index.js
  3. 从 index.html 移除 ui-initializer.js 引入
  4. 删除 ui-initializer.js 文件
  5. 验证快捷应用、快捷开关、状态栏正常

### 步骤2.3：简化 bootstrap.js
- **状态**：`[ ]` 待执行
- **文件**：`app/src/main/assets/js/bootstrap.js`（76行）
- **操作**：只保留 safeInit 和 runInit，移除冗余功能

### 步骤2.4：清理 index.js 冗余适配函数
- **状态**：`[ ]` 待执行
- **操作**：移除 index.js 中只是简单转发到中间层的适配函数，直接调用模块方法

### 步骤2.5：编译安装验证
- **状态**：`[ ]` 待执行
- **操作**：编译 APK 并安装到设备，验证所有功能正常

---

## 阶段3：接口对齐 ⏳ 待执行

- [ ] 验证 JS_API_REFERENCE.md 与后端 @JavascriptInterface 方法一致
- [ ] 验证 CODE_INDEX.md 与实际代码一致
- [ ] 更新 PROJECT_STATUS.md（移除已删除文件标记）

---

## 阶段4：CSS 清理 ⏳ 待执行

- [ ] 删除未引入的 main.css（24行）
- [ ] 确认 music.css（747行）是否需要引入
- [ ] 去除重复样式选择器
- [ ] 验证 !important 仅在 base.css 中使用

---

## 阶段5：功能验证 ⏳ 待执行

- [ ] 编译安装 APK
- [ ] 测试面板开关（设置/应用/开关面板）
- [ ] 测试时钟显示（秒数不闪烁）
- [ ] 测试应用列表加载
- [ ] 测试快捷开关功能
- [ ] 测试壁纸切换
- [ ] 测试车控功能

---

## 其他待办（低优先级）

- [ ] 删除废弃 patch 文件（one-click-permission-patch.js, tasks-btn-adb-patch.js）
- [ ] 确认 android_interface.js（291行）是否使用，未使用则删除
- [ ] 更新 PROJECT_STATUS.md 反映阶段2完成后的代码统计

---

## 交接信息

### 当前交接（2026-07-09）

**正在执行**：阶段2步骤2.2 - 删除 ui-initializer.js 并合并到 index.js

**已完成**：
- 阶段1文档整理全部完成
- 创建 DEV_PLAN.md 和 CONTEXT.md 两个记忆文件
- ✅ 步骤2.1：删除 panel-controller.js（106行），6个函数已合并到 index.js

**下一步具体操作**：
1. 读取 `app/src/main/assets/js/ui-initializer.js` 全部内容（427行）
2. 读取 `app/src/main/assets/js/index.js` 中的 UiInitializer 适配层（第144-176行附近）
3. 将 ui-initializer.js 的核心方法（loadQuickApps/loadQuickSwitches/updateNetworkAndBluetoothStatus 等）合并到 index.js
4. 注意：ui-initializer.js 中有些方法可能已被其他模块直接调用（window.UiInitializer.xxx）
5. 从 index.html 移除 `<script src="js/ui-initializer.js"></script>`
6. 删除 ui-initializer.js 文件
7. 编译验证

**注意事项**：
- ui-initializer.js 是较大的文件（427行），合并时注意保持功能完整
- 合并后 index.js 行数会增加，但总体减少了中间层
- 面板控制规范已在 index.js 中（步骤2.1合并），不要重复
- 注意检查是否有其他文件直接调用 `window.UiInitializer` 或 `UiInitializer.xxx`

**已知的 index.js 适配层**（需替换为实际实现或直接删除）：
- loadQuickApps, loadQuickSwitches, updateACControlStatus
- checkWifiStatus, checkBluetoothStatus, checkLocationStatus
- updateNetworkAndBluetoothStatus, initHorizontalScroll
- updateMusicProgress, initAcTemperature, updateAcTemperature
- updateAcState, updateWindLevel, initMusicControls
- addTimeDisplayClickEvent, initNavigationButtons

---

**文档版本**：v1.0
**维护规则**：每次任务状态变化时同步更新本文档
