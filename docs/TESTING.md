# C11Partner 测试指南

> 🧪 本文档描述项目的测试框架和测试方法
>
> 最后更新：2026-06-20

---

## 一、测试概览

### 1.1 测试类型

| 类型 | 说明 | 位置 | 状态 |
|------|------|------|------|
| **Python 工具测试** | 开发工具的单元测试 | `tests/` | ✅ 已实现 |
| **Android 单元测试** | Java 代码的单元测试 | `app/src/test/` | 📝 示例 |
| **Instrumented 测试** | 需要设备的测试 | `app/src/androidTest/` | 📝 示例 |
| **实车测试** | 在真实车机上测试 | - | ⚠️ 手动 |

### 1.2 测试原则

- 工具脚本必须有单元测试
- 核心业务逻辑建议有单元测试
- 新功能上线前必须经过实车测试
- 提交代码前确保所有测试通过

---

## 二、Python 工具测试

### 2.1 运行测试

```bash
# 运行所有测试
python3 -m pytest tests/ -v

# 或者直接运行测试文件
python3 tests/test_check_commit_msg.py
```

### 2.2 测试文件列表

| 测试文件 | 测试对象 |
|----------|---------|
| `test_check_commit_msg.py` | 提交信息检查工具 |

### 2.3 添加新测试

1. 在 `tests/` 目录创建 `test_xxx.py` 文件
2. 编写测试类和测试方法
3. 测试方法以 `test_` 开头
4. 使用 assert 断言验证结果

**示例**：
```python
def test_something():
    result = some_function()
    assert result == expected_value
```

---

## 三、Android 单元测试

### 3.1 环境要求

- Android Studio
- JUnit 4
- Mockito（可选，用于 mock）
- Robolectric（可选，用于模拟 Android 环境）

### 3.2 测试目录结构

```
app/src/
├── test/                    # 本地单元测试（不需要设备）
│   └── java/com/c11partner/desktop/
│       ├── ExampleUnitTest.java
│       └── utils/
│           └── CarControlManagerTest.java
└── androidTest/             # Instrumented 测试（需要设备）
    └── java/com/c11partner/desktop/
        └── ExampleInstrumentedTest.java
```

### 3.3 运行单元测试

```bash
# 运行所有单元测试
./gradlew testDebugUnitTest

# 运行特定测试类
./gradlew testDebugUnitTest --tests "com.c11partner.desktop.ExampleUnitTest"
```

### 3.4 单元测试示例

```java
package com.c11partner.desktop;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
}
```

### 3.5 推荐测试的类

| 类名 | 测试优先级 | 说明 |
|------|-----------|------|
| `CarControlManager` | ⭐⭐⭐⭐⭐ | 车控核心逻辑 |
| `AutomationEngine` | ⭐⭐⭐⭐ | 自动化引擎 |
| `LeapMotorCarState` | ⭐⭐⭐ | 车辆状态数据类 |
| `AppUtils` | ⭐⭐⭐ | 应用工具类 |
| `LunarCalendarUtils` | ⭐⭐ | 农历工具类 |

---

## 四、实车测试

### 4.1 测试前准备

1. 授予三大核心权限
2. 开启通知监听权限（如果测试音乐模块）
3. 车辆处于安全状态（驻车、熄火或安全驾驶）

### 4.2 测试清单

#### 基础功能
- [ ] 应用正常启动
- [ ] 主页面正常显示
- [ ] 壁纸正常加载
- [ ] 应用列表正常显示
- [ ] 快捷应用正常启动

#### 车控功能
- [ ] 360全景手动启动正常
- [ ] 近光灯开关正常
- [ ] 驾驶模式切换正常
- [ ] 空调控制正常
- [ ] 音量调节正常
- [ ] 氛围灯控制正常

#### 状态监控
- [ ] 档位显示正确
- [ ] 车门状态显示正确
- [ ] 转向灯显示正确
- [ ] 车速显示正确
- [ ] 胎压显示正确

#### 自动化
- [ ] 转向灯自动开360正常
- [ ] R档自动开360正常
- [ ] 倒车自动降音量正常
- [ ] 语音提示正常

#### 音乐模块
- [ ] 音乐信息正常显示
- [ ] 播放/暂停控制正常
- [ ] 上一首/下一首控制正常

#### 副屏功能
- [ ] 副屏正常显示
- [ ] 车速/档位同步正常
- [ ] 胎压显示正常

### 4.3 Bug 报告模板

```
**Bug 标题**：简要描述问题

**环境**：
- 车型：零跑 C11 2023款纯电舒享版
- 系统版本：xxx
- 应用版本：v1.x.x

**复现步骤**：
1. 
2. 
3. 

**预期结果**：
应该发生什么

**实际结果**：
实际发生了什么

**日志**：
（如果有，请附上相关日志）

**截图/录屏**：
（如果有，请附上）
```

---

## 五、持续集成

### 5.1 GitHub Actions

项目已配置 GitHub Actions 自动构建，未来可以添加自动测试：

- [ ] 单元测试自动运行
- [ ] 代码覆盖率统计
- [ ] 代码质量检查
- [ ] 安全漏洞扫描

---

## 六、测试最佳实践

### 6.1 单元测试原则

1. **测试要快**：单元测试应该能快速运行
2. **测试要独立**：每个测试不依赖其他测试
3. **测试要可重复**：每次运行结果都应该一样
4. **测试要清晰**：测试代码要易读易懂
5. **测试要全面**：覆盖正常情况、边界情况、异常情况

### 6.2 测试命名

```
test_<功能>_<场景>_<预期结果>
```

**示例**：
- `test_check_commit_message_valid_feat_commit`
- `test_check_commit_message_invalid_type`
- `test_check_commit_message_empty_subject`

### 6.3 测试覆盖率

- 工具脚本：建议覆盖率 > 80%
- 核心业务逻辑：建议覆盖率 > 60%
- UI 相关代码：可以适当降低

---

## 七、相关文档

- [提交规范](COMMIT_CONVENTION.md) - 代码提交规范
- [开发指南](DEVELOPMENT_GUIDE.md) - 开发环境和流程
- [架构设计](ARCHITECTURE.md) - 系统架构

---

**文档版本**：v1.0  
**最后更新**：2026-06-20


---

## 六、最新测试数据（2026-06-20）

### 6.1 测试覆盖率

| 工具 | 测试数 | 覆盖率 |
|------|--------|--------|
| check_commit_msg.py | 12 个 | 47% |
| generate_api_docs.py | 8 个 | 57% |
| generate_code_index.py | 11 个 | 85% |
| **总计** | **31 个** | **68%** |

### 6.2 运行测试

```bash
# 运行所有测试
python3 tests/test_check_commit_msg.py
python3 tests/test_generate_code_index.py
python3 tests/test_generate_api_docs.py

# 带覆盖率运行
coverage run --source=tools tests/test_check_commit_msg.py
coverage run --append --source=tools tests/test_generate_code_index.py
coverage run --append --source=tools tests/test_generate_api_docs.py
coverage report -m

# 生成 HTML 报告
coverage html
```

### 6.3 Android 单元测试

CarControlManager 单元测试骨架已创建：
- 位置：`app/src/test/java/com/c11partner/desktop/utils/CarControlManagerTest.java`
- 测试方法：24 个占位
- 框架：JUnit + Mockito + Robolectric

运行方法：
```bash
./gradlew testDebugUnitTest
```

---

*文档版本：v1.1*
*最后更新：2026-06-20*
