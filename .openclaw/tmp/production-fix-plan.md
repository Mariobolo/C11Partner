# 生产环境修复计划

> 创建时间：2026-07-01
> 目标：解决前端审查发现的所有问题，达到可发布状态

## P0：补齐 WebViewBridge 缺失方法（32个）

### 车控核心（10个）
- [ ] getCarState - 获取车辆状态JSON
- [ ] navigateToHome - 导航回家
- [ ] navigateToCompany - 导航去公司
- [ ] toggleAC - 空调开关
- [ ] toggleAirConditioning - 空调开关（别名）
- [ ] toggleDefrost - 除霜开关
- [ ] increaseTemperature / decreaseTemperature - 温度调节
- [ ] increaseWindSpeed / decreaseWindSpeed - 风量调节
- [ ] getAcInfo - 获取空调状态

### 音乐控制旧API（5个）
- [ ] playPause / playNext / playPrevious - 旧API兼容
- [ ] playMusic / pauseMusic - 直接控制

### 应用管理（5个）
- [ ] getAppListAsync - 异步应用列表
- [ ] getEnabledCategoriesAsync - 异步壁纸分类
- [ ] setDefaultDesktop - 设置默认桌面
- [ ] showToast - Toast提示
- [ ] hasNotificationAccess - 通知权限检查

### 自动化/展示/ADB（12个）
- [ ] getAutomationSettings / setAutomationSettings / setAutomationScenarioEnabled
- [ ] hidePresentation / isPresentationShowing / showCarStatusPresentation
- [ ] enableAdbDebugging / executeAdbCommand / openRecentsViaAdb
- [ ] adjustTemperature / adjustWindLevel

## P1：完善 android_interface.js mock 接口
- [ ] 为所有32个缺失方法添加mock实现
- [ ] 为已有方法补充返回值模拟

## P2：清理代码
- [ ] 全局变量封装（16个）
- [ ] 死代码清理
- [ ] 冗余CSS清理

## 文档更新
- [ ] AI_ENTRY_GUIDE.md - 更新Bridge方法清单
- [ ] CHANGELOG_OPTIMIZATION.md - 记录本次修复
- [ ] PROJECT_STATUS.md - 更新进度
- [ ] docs/JS_API_REFERENCE.md - 更新API文档
