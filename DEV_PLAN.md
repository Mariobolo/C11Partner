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
| 阶段2 | 前端清理 | ✅ 已完成 | 删除冗余中间层，合并到 index.js，简化初始化流程 |
| 阶段3 | 接口对齐 | ✅ 已完成 | 前后端接口文档一致验证 |
| 阶段4 | CSS 清理 | ✅ 已完成 | 去除重复样式，修复伪元素冲突 |
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

## 阶段2：前端清理 ✅ 已完成

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

### 步骤2.2：删除 ui-initializer.js，合并到 index.js ✅ 已完成
- **状态**：`[x]` 已完成（2026-07-09）
- **文件**：`app/src/main/assets/js/ui-initializer.js`（472行，已删除）
- **操作**：
  1. ✅ 读取 ui-initializer.js 内容
  2. ✅ 将17个核心方法合并到 index.js（替换原适配层）
  3. ✅ 修改 bootstrap.js 中的 UiInitializer 引用为直接调用全局函数
  4. ✅ 从 index.html 移除 ui-initializer.js 引入
  5. ✅ 删除 ui-initializer.js 文件
  6. ✅ 编译验证通过（BUILD SUCCESSFUL）
- **提交记录**：f7c7742 refactor: 合并ui-initializer.js到index.js，移除中间层

### 步骤2.3：简化 bootstrap.js ✅ 已完成
- **状态**：`[x]` 已完成（2026-07-09）
- **文件**：`app/src/main/assets/js/bootstrap.js`（91行→52行）
- **操作**：
  1. ✅ 移除 state 对象（与 index.js indexState 重复，无外部引用）
  2. ✅ 移除 debounce/throttle/addDebouncedClick（utils.js 已有同功能，无外部引用 AppBootstrap.debounce）
  3. ✅ index.js 中 AppBootstrap.debounce 引用改为 window.debounce
  4. ✅ 只保留 safeInit/registerInit/runInit 核心功能
  5. ✅ 编译验证通过（BUILD SUCCESSFUL）
- **提交记录**：51369ad refactor: 简化bootstrap.js，移除冗余state/debounce/throttle/addDebouncedClick

### 步骤2.4：清理 index.js 冗余适配函数 ✅ 已完成
- **状态**：`[x]` 已完成（2026-07-09）
- **操作**：
  1. ✅ 删除 SettingsSync 适配层8个函数（loadWallpaperSettings/initEffectLevel/applyEffectLevel/initThemeMode/applyThemeMode/initThemeToggleIcon/toggleWallpaperCarousel/restoreDefaultWallpaper）
  2. ✅ 删除 AppListManager 适配层7个函数（initAppsModal/renderAppsList/escapeHtml/normalizeAppIcon/initAlphabetNav/showAddToQuickAppsDialog/showRemoveFromQuickAppsDialog）
  3. ✅ 更新 index.js 内部调用为直接调用模块方法（SettingsSync.xxx / AppListManager.xxx）
  4. ✅ 编译验证通过（BUILD SUCCESSFUL）
- **提交记录**：8352887 refactor: 清理index.js冗余适配层，直接调用模块方法

### 步骤2.5：编译安装验证 ✅ 已完成
- **状态**：`[x]` 已完成（2026-07-09）
- **操作**：编译验证通过，静态检查无残留引用

---

## 阶段3：接口对齐 ✅ 已完成

- [x] 验证 JS_API_REFERENCE.md 与后端 @JavascriptInterface 方法一致 ✅ 已完成（v2.1）
- [x] 验证 CODE_INDEX.md 与实际代码一致 ✅ 已完成（v2.1，移除已删除文件）
- [x] 更新 PROJECT_STATUS.md（移除已删除文件标记，更新代码统计）✅ 已完成（v2.1）
- **提交记录**：bfc0551 docs: 阶段3接口对齐，更新CODE_INDEX.md和PROJECT_STATUS.md

---

## 阶段4：CSS 清理 ✅ 已完成

- [x] 删除未引入的 main.css（24行）✅ 已完成（2026-07-09）
- [x] 确认 music.css（768行）— 未引入且无引用，已删除 ✅ 已完成（2026-07-09）
- [x] 去除重复样式选择器（widgets.css 重复定义清理，修复伪元素冲突）✅ 已完成（2026-07-09）
- [x] 验证 !important 仅在 base.css 中使用 ✅ 已完成（2026-07-09，35处均在base.css）

---

## 阶段5：功能验证 ⏳ 进行中

- [x] 编译安装 APK
  - ✅ 编译验证通过（BUILD SUCCESSFUL，2026-07-09 16:03）
  - ✅ 安装到模拟器成功（emulator-5556，2026-07-09 16:05）
  - ✅ 修复 CSS 图片路径（widgets.css 中 nav_car.png/nav_map_go_home.png）
  - ✅ 重新安装验证通过（BUILD SUCCESSFUL，adb install -r，2026-07-09 16:08）
- [x] 前端静态检查（2026-07-13）
  - ✅ JS 引入与实际文件完全一致（24个文件）
  - ✅ 无已删除文件残留引用
  - ✅ !important 仅 base.css 中使用（35处），widgets.css 仅注释
  - ✅ 面板控制规范正确（active+display配合）
  - ✅ 模块 window 挂载正常
- [x] 修复日间模式样式不完整（2026-07-13，commit 2b6c1c3）
  - ✅ 添加 modal/tab-navigation/tab-button/setting-item 面板覆盖
  - ✅ 添加 apps-modal-header/apps-search-bar/search-input 覆盖
  - ✅ 添加 quick-switch-panel/switch-item 覆盖
  - ✅ 添加 wallpaper-type-item/app-tab 分类标签覆盖
- [ ] 测试面板开关（设置/应用/开关面板）
- [ ] 测试时钟显示（秒数不闪烁）
- [ ] 测试应用列表加载
- [ ] 测试快捷开关功能
- [ ] 测试壁纸切换
- [ ] 测试车控功能
- [ ] 测试日间模式显示效果

---

## 其他待办（低优先级）

- [x] 删除废弃 patch 文件（one-click-permissions-patch.js, tasks-btn-adb-patch.js）✅ 已完成（2026-07-09）
- [x] 确认 android_interface.js（291行）是否使用，未使用则删除 ✅ 已完成（2026-07-09，未被 index.html 引入）
- [x] 更新 PROJECT_STATUS.md 反映阶段2-4完成后的代码统计 ✅ 已完成（2026-07-09，v2.2）

---

## 交接信息

### 当前交接（2026-07-14）

**正在执行**：阶段5 - 功能验证（阻塞：无设备连接）

**已完成**：
- ✅ 阶段1文档整理全部完成
- ✅ 阶段2前端清理全部完成
- ✅ 阶段3接口对齐全部完成
- ✅ 阶段4 CSS清理全部完成
- ✅ 阶段5 编译验证（BUILD SUCCESSFUL，2026-07-14）
- ✅ 阶段5 前端静态检查（6项全部通过，2026-07-14）
- ✅ 阶段5 修复日间模式样式不完整（theme.css +163行，commit 2b6c1c3）
- ✅ 代码规范检查（!important 仅在 base.css 中使用，符合规范）
- ✅ Git 推送成功（4个本地提交已推送）

**阻塞原因**：
- AVD 模拟器配置损坏（`C:\Users\Mario\.android\avd\C11Test.ini` 文件为空）
- 无法写入 AVD 配置文件（工作目录外，权限限制）
- 无真机或其他模拟器连接

**下一步具体操作**：
1. **修复 AVD 配置**（推荐）：通过 Android Studio 打开 AVD Manager 删除并重建 C11Test，或手动写入 `C11Test.ini`：
   ```
   avd.ini.encoding=UTF-8
   path=C:\Users\Mario\.android\avd\C11Test.avd
   path.rel=avd\C11Test.avd
   target=android-28
   ```
2. **或连接真机/其他模拟器**：adb devices 确认连接后继续
3. 安装 APK 后测试功能：
   - 面板开关（设置/应用/开关面板）
   - 时钟显示（秒数不闪烁）
   - 应用列表加载
   - 快捷开关功能
   - 壁纸切换
   - 车控功能
   - 日间模式显示效果

---

**文档版本**：v1.1
**维护规则**：每次任务状态变化时同步更新本文档
