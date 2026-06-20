# C11Partner 常见问题 FAQ

> ❓ 常见问题解答，遇到问题先看这里
>
> 最后更新：2026-06-20

---

## 一、安装和权限问题

### Q1: 安装后车控功能用不了？

**A**: 大概率是权限问题。C11Partner 需要三个核心权限，必须通过 ADB 授予：

```bash
adb shell pm grant com.c11partner.desktop android.permission.READ_LOGS
adb shell pm grant com.c11partner.desktop android.permission.DUMP
adb shell pm grant com.c11partner.desktop android.permission.WRITE_SECURE_SETTINGS
```

**验证权限**：
```bash
adb shell dumpsys package com.c11partner.desktop | findstr /i permission
```

---

### Q2: 音乐模块不显示歌名？

**A**: 需要开启通知监听权限：

1. 打开 C11Partner
2. 点击音乐模块的"授权通知监听"提示
3. 在设置中找到 C11Partner，开启通知使用权
4. 重启应用

**验证**：播放音乐后，看通知栏有没有音乐通知，有通知就能被监听到。

---

### Q3: 副屏功能用不了？

**A**: 首先确认你的车型有副驾娱乐屏。C11 部分配置没有副屏。

如果有副屏但用不了：
1. 检查是否授予了 WRITE_SECURE_SETTINGS 权限
2. 在快捷开关中打开"副屏投屏"开关
3. 看副屏是否显示车辆状态

---

## 二、编译和构建问题

### Q4: GitHub Actions 构建失败？

**A**: 常见原因和解决方法：

**原因1：语法错误（缺少括号等）**
- 症状：报错提示多个方法解析失败
- 解决：检查报错位置附近的代码，补上缺失的大括号

**原因2：包名不一致**
- 症状：找不到类的错误
- 解决：全局搜索旧包名 `com.dipartner.desktop`，替换为 `com.c11partner.desktop`

**原因3：依赖问题**
- 症状：找不到某个类或方法
- 解决：检查 build.gradle 中的依赖配置

**查看构建日志**：
1. 打开 GitHub Actions 页面
2. 点击失败的构建
3. 查看详细错误信息

---

### Q5: 本地编译报错？

**A**: 按以下步骤排查：

1. **检查 JDK 版本**：需要 JDK 11+
2. **检查 Android SDK**：需要 API 28+
3. **Gradle 同步**：在 Android Studio 中点击 Sync Project
4. **清理重建**：Build → Clean Project → Rebuild Project

如果还是不行，试试：
```bash
./gradlew clean
./gradlew assembleDebug --stacktrace
```

---

## 三、车控功能问题

### Q6: 某个车控功能点击没反应？

**A**: 可能的原因：

1. **权限没授予**：先确认三大核心权限都授予了
2. **功能是推测的**：部分功能是根据命名规律推测的，可能不对
3. **需要特定条件**：某些功能可能需要车辆启动、挂P挡等条件才能用

**排查方法**：
1. 打开 logcat 日志
2. 点击功能按钮
3. 看有没有报错日志
4. 看车机系统有没有反应

如果确认功能不对，可以通过日志分析找正确的接口。

---

### Q7: 360全景不会自动弹出？

**A**: 检查以下几点：

1. **自动化场景是否开启**：在设置中确认"转向灯自动开360"和"R档自动开360"已开启
2. **权限是否授予**：需要 READ_LOGS 权限才能监听转向灯和档位
3. **冷却时间**：360全景有3秒冷却，避免频繁触发

**手动测试**：
- 手动点击360全景按钮，看能不能正常启动
- 如果手动能启动，说明自动触发的监听有问题

---

### Q8: 空调控制不准？

**A**: 空调控制有两种方式：

1. **Settings.Global 直接控制**（推荐）：直接写入系统属性
2. **语音控制降级方案**：通过讯飞语音接口发指令

如果直接控制不准，可以：
1. 检查 strCar100006 等属性是否正确
2. 用语音控制作为备选方案
3. 实车测试验证具体的属性值

---

## 四、日志和调试问题

### Q9: 怎么抓取车机日志？

**A**: 用 ADB logcat：

```bash
# 清空日志
adb logcat -c

# 开始抓取（保存到文件）
adb logcat -v time > log.txt

# 操作车机功能...

# Ctrl+C 停止抓取
```

**过滤特定标签**：
```bash
# Windows
adb logcat -v time | findstr /i C11CarSomeIp

# Linux/Mac
adb logcat -v time | grep -i C11CarSomeIp
```

---

### Q10: 怎么分析日志找车控接口？

**A**: 按以下步骤：

1. **清空日志**：`adb logcat -c`
2. **操作功能**：在车机上操作你想研究的功能
3. **抓取日志**：`adb logcat -v time > log.txt`
4. **分析日志**：
   - 搜索关键词（如功能名、car、light等）
   - 找到相关的 TAG
   - 找到 EventId 或参数值
   - 多次测试验证

**常用搜索关键词**：
- 灯光：light、lamp、carlight
- 空调：air、ac、hvac
- 车门：door
- 档位：gear、eventId 1110
- 360：camera、around、360

---

### Q11: 怎么验证某个 Settings 属性？

**A**: 用 adb shell 命令：

```bash
# 读取属性
adb shell settings get global strCar1409

# 写入属性（需要权限）
adb shell settings put global strCar1409 25

# 列出所有包含 car 的属性
adb shell settings list global | findstr /i strCar
```

---

## 五、前端开发问题

### Q12: 怎么调试前端页面？

**A**: 有几种方法：

**方法1：Chrome DevTools 调试 WebView**
1. 车机连接电脑
2. 打开 Chrome，输入 `chrome://inspect`
3. 找到 WebView，点击 inspect
4. 就可以像调试网页一样调试了

**方法2：浏览器直接打开**
- 把 `app/src/main/assets/index.html` 直接在浏览器打开
- 可以预览 UI，但 JS 接口不会工作（需要 Android 环境）

---

### Q13: JS 调用 Java 接口没反应？

**A**: 检查以下几点：

1. **方法名是否一致**：JS 调用的方法名要和 Java 完全一致
2. **有没有 @JavascriptInterface 注解**：Java 方法必须有这个注解
3. **参数类型是否匹配**：JS 传的参数类型要和 Java 一致
4. **是否在主线程调用**：WebViewBridge 的方法会在主线程执行

**调试技巧**：
- 在 Java 方法开头加 Log 打印
- 看 logcat 有没有调用日志
- 看有没有异常报错

---

### Q14: 前端修改后不生效？

**A**: 可能是缓存问题：

1. **重新安装 APK**：修改前端代码后需要重新构建安装
2. **清除 WebView 缓存**：可以在应用设置中清除缓存
3. **强制刷新**：如果是热更新，需要刷新页面

---

## 六、性能和稳定性问题

### Q15: 应用卡顿？

**A**: 可能的原因和优化：

1. **壁纸太大**：使用分辨率合适的壁纸
2. **动画太多**：减少不必要的动画效果
3. **日志太频繁**：logcat 监控不要太频繁解析
4. **WebView 硬件加速**：确保开启了硬件加速

---

### Q16: 后台服务被杀死？

**A**: Android 系统可能会杀死后台服务，解决方法：

1. **前台服务**：LogcatMonitorService 已经是前台服务，有常驻通知
2. **加入电池优化白名单**：在系统设置中把 C11Partner 加入白名单
3. **自启动权限**：开启应用的自启动权限

---

## 七、其他问题

### Q17: 怎么更新应用？

**A**: 有几种方式：

**方式1：ADB 安装**
```bash
adb install -r app-debug.apk
```

**方式2：GitHub Actions 下载**
1. 打开 GitHub Actions 页面
2. 下载最新构建的 APK
3. 用 ADB 安装到车机

**方式3：U盘安装**
1. 把 APK 拷到 U 盘
2. 车机上安装文件管理器
3. 从 U 盘安装 APK

---

### Q18: 怎么备份配置？

**A**: 配置数据存在以下地方：

1. **SharedPreferences**：应用内部存储
2. **SQLite 数据库**：快捷应用、壁纸配置等
3. **系统属性**：车控相关的设置

**备份方法**：
- 用 ADB 备份应用数据
- 或者手动记录配置项

---

### Q19: 支持哪些车型？

**A**: 目前主要针对 **零跑 C11** 开发：

- ✅ 零跑 C11（2023款纯电舒享版等）
- 🔍 零跑 C01（可能兼容，待验证）
- 🔍 零跑其他车型（待验证）

不同车型的 CAN 信号和系统属性可能不一样，需要实车测试调整。

---

### Q20: 怎么贡献代码？

**A**: 欢迎贡献！

1. Fork 项目
2. 创建功能分支
3. 提交代码
4. 发起 Pull Request

**提交规范**：
- feat: 新功能
- fix: 修复bug
- docs: 文档更新
- refactor: 重构

---

## 八、开发效率问题

### Q21: 修改代码总是要读整个大文件，上下文不够用？

**A**: 使用代码索引文档！

```bash
# 查看代码索引
docs/CODE_INDEX.md

# 找到函数行号后，精准读取小范围
sed -n '起始行,结束行p' 文件名
```

**或者重新生成索引**：
```bash
python3 tools/generate_code_index.py
```

这样可以减少 90%+ 的上下文占用。

---

### Q22: 忘记某个车控接口怎么用了？

**A**: 查车控接口速查手册：

```
docs/CAR_CONTROL_API.md
```

里面包含了所有车控接口的详细说明，包括：
- Intent 广播接口
- Logcat 监控信号
- Settings.Global 属性
- CarControlManager 方法
- WebViewBridge JS 接口

---

## 九、遇到问题怎么办？

### 排查步骤

1. **先看本文档**：80% 的问题在这里能找到答案
2. **查看日志**：用 logcat 看报错信息
3. **检查权限**：三大核心权限是否都授予了
4. **搜索代码**：用 Grep 搜索相关代码
5. **查文档**：看架构文档和开发指南
6. **提 Issue**：如果是 bug，在 GitHub 提 Issue

---

## 十、相关文档

- [架构设计](ARCHITECTURE.md) - 整体架构和模块划分
- [开发指南](DEVELOPMENT_GUIDE.md) - 环境搭建和开发流程
- [车控接口文档](CAR_CONTROL_API.md) - 详细的车控接口列表
- [代码索引](CODE_INDEX.md) - 函数级快速定位

---

**文档版本**：v1.0  
**最后更新**：2026-06-20

有新问题欢迎补充到本文档！
