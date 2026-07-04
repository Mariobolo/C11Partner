# AGP & compileSdk 升级计划

> C11Partner 项目：从 AGP 4.2.2 / compileSdk 30 升级到 AGP 8.5.2 / compileSdk 34

---

## 当前状态

| 组件 | 当前版本 | 目标版本 |
|------|----------|----------|
| AGP | 4.2.2 | 8.5.2 |
| Gradle | 7.2 | 8.7 |
| compileSdk | 30 | 34 |
| buildToolsVersion | 30.0.3 | 省略（AGP 8+ 自动推断） |
| targetSdk | 30 | 30（保持不变） |
| minSdk | 25 | 25（保持不变） |
| JDK | 1.8 | 11（CI 已用 JDK 17，兼容） |

---

## 为什么分两步

从 4.2.2 到 8.5.2 跨越了 4 个大版本，涉及多项破坏性变更一次性处理风险太高。分步走的好处：

- 每步变更量可控，出问题容易定位
- Step 1 解决最核心的 namespace 和 Java 版本问题
- Step 2 处理 AGP 8.x 独有的构建 API 变更

---

## Step 1：AGP 4.2.2 → 7.4.2 + Gradle 8.0

这是过渡步骤，解决基础设施问题。

### 1.1 版本对应关系

| 组件 | 版本 |
|------|------|
| AGP | 7.4.2 |
| Gradle | 8.0 |
| JDK | 11（最低要求） |
| compileSdk | 33（先升到 33，Step 2 再到 34） |

### 1.2 需要修改的文件

**gradle-wrapper.properties**
```properties
distributionUrl=https\://services.gradle.org/distributions/gradle-8.0-bin.zip
```

**app/build.gradle — 完整改写后的关键片段**
```groovy
buildscript {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://maven.aliyun.com/repository/google' }
        maven { url 'https://maven.aliyun.com/repository/central' }
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:7.4.2'
    }
}

android {
    namespace 'com.c11partner.desktop'  // AGP 7.3+ 必填
    compileSdk 33
    // 删除 buildToolsVersion，AGP 7+ 自动推断

    defaultConfig {
        applicationId "com.c11partner.desktop"
        minSdk 25
        targetSdk 30
        // ...
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_11
        targetCompatibility JavaVersion.VERSION_11
    }
}
```

**app/src/main/AndroidManifest.xml**
```xml
<!-- 删除 package 属性，已移至 build.gradle 的 namespace -->
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:usesCleartextTraffic="true">
```

**gradle.properties — 新增**
```properties
android.suppressUnsupportedCompileSdk=33
```

**.github/workflows/android-ci.yml — SDK 包安装**
```yaml
packages: 'platform-tools platforms;android-33'
```

### 1.3 Step 1 变更清单

| 序号 | 文件 | 变更 |
|------|------|------|
| 1 | gradle-wrapper.properties | Gradle 7.2 → 8.0 |
| 2 | app/build.gradle | AGP 4.2.2 → 7.4.2，添加 namespace，删除 buildToolsVersion，Java 1.8 → 11 |
| 3 | AndroidManifest.xml | 删除 package="com.c11partner.desktop" |
| 4 | gradle.properties | suppressUnsupportedCompileSdk 更新为 33 |
| 5 | android-ci.yml | SDK 安装改为 platforms;android-33 |

### 1.4 Step 1 验证标准

- `./gradlew assembleDebug` 成功
- `./gradlew assembleRelease` 成功
- APK 可正常安装到 Android 9 设备
- CI 流水线通过

---

## Step 2：AGP 7.4.2 → 8.5.2 + Gradle 8.7

这是最终目标版本。

### 2.1 版本对应关系

| 组件 | 版本 |
|------|------|
| AGP | 8.5.2 |
| Gradle | 8.7（最低 8.4） |
| JDK | 17（最低要求） |
| compileSdk | 34 |

### 2.2 Step 2 独有变更

**buildscript → plugins 迁移（可选但推荐）**

AGP 8.x 推荐使用 `plugins {}` DSL 替代 `buildscript {}`。但 Groovy 语法下 `buildscript` 仍然可用，项目结构简单可以不改。

**app/build.gradle — AGP 8.x 新增要求**
```groovy
android {
    namespace 'com.c11partner.desktop'
    compileSdk 34

    // AGP 8.0+ 默认禁用 buildConfig，如需使用：
    buildFeatures {
        buildConfig = true
    }
}
```

**gradle.properties — AGP 8.x 配置**
```properties
android.suppressUnsupportedCompileSdk=34
# AGP 8.x 新增：非传递性 R 类（减少资源冲突）
android.nonTransitiveRClass=true
```

**AndroidManifest.xml — AGP 8.x 要求**

`package` 属性在 Step 1 已经删除，无需额外改动。但需确认 `android:exported` 属性——AGP 8.x 对 intent-filter 的 Activity/Service 强制要求声明 `android:exported`。

**.github/workflows/android-ci.yml**
```yaml
packages: 'platform-tools platforms;android-34'
```

### 2.3 Step 2 变更清单

| 序号 | 文件 | 变更 |
|------|------|------|
| 1 | gradle-wrapper.properties | Gradle 8.0 → 8.7 |
| 2 | app/build.gradle | AGP 7.4.2 → 8.5.2，compileSdk 33 → 34，添加 buildConfig 特性开关 |
| 3 | gradle.properties | suppressUnsupportedCompileSdk → 34，添加 nonTransitiveRClass |
| 4 | AndroidManifest.xml | 检查并补充所有 intent-filter 组件的 android:exported 属性 |
| 5 | android-ci.yml | SDK 改为 platforms;android-34 |

### 2.4 Step 2 验证标准

- `./gradlew assembleDebug` 成功
- `./gradlew assembleRelease` 成功
- APK 可正常安装到 Android 9 设备（车机）
- CI 流水线通过
- 无新增 lint 错误

---

## 不变项

| 参数 | 保持原因 |
|------|----------|
| targetSdk 30 | 车机 Android 9，提升无实际收益，避免触发权限变更 |
| minSdk 25 | 兼容性底线不变 |
| Groovy 语法 | 项目体量小，无需迁移 Kotlin DSL |
| 应用签名 | keystore 和签名配置不变 |

---

## 风险与回退

| 风险 | 影响 | 回退方案 |
|------|------|----------|
| 依赖库不兼容新 AGP | 编译失败 | 检查 `implementation` 列表，更新到兼容版本 |
| Java 11 语法不兼容 | 编译失败 | 项目代码未用 Java 新特性，基本无风险 |
| CI JDK 版本 | 构建失败 | CI 已用 JDK 17，向下兼容 JDK 11 |
| namespace 迁移遗漏 | 编译失败 | 确认 AndroidManifest.xml 删除 package 属性 |
| 车机安装运行异常 | 功能异常 | minSdk/targetSdk 未变，APK 向下兼容 Android 9，风险极低 |

每一步完成后单独 commit，出问题可随时 `git revert` 回退到上一步。

---

## 执行顺序

```
Step 1 修改文件 → 本地构建验证 → commit → push → 等 CI 通过
                    ↓ 通过
Step 2 修改文件 → 本地构建验证 → commit → push → 等 CI 通过
                    ↓ 通过
升级完成
```
