# C11Partner 文档中心

> 📚 所有文档的总入口，快速找到你需要的文档
>
> 最后更新：2026-06-20

---

## 📋 文档清单

### 入门必读

| 文档 | 说明 | 适合人群 |
|------|------|---------|
| [README.md](../README.md) | 项目介绍和快速开始 | 所有人 |
| [架构设计](ARCHITECTURE.md) | 整体架构、模块划分、关键设计决策 | 开发者 |
| [开发指南](DEVELOPMENT_GUIDE.md) | 环境搭建、开发流程、调试方法 | 开发者 |

### 开发参考

| 文档 | 说明 | 推荐度 |
|------|------|--------|
| [车控接口速查](CAR_CONTROL_API.md) | 所有车控接口的完整列表 ⭐最常用 | ⭐⭐⭐⭐⭐ |
| [代码索引](CODE_INDEX.md) | 函数级快速定位，节省上下文 ⭐最常用 | ⭐⭐⭐⭐⭐ |
| [常见问题 FAQ](FAQ.md) | 常见问题解答，遇到问题先看这里 | ⭐⭐⭐⭐ |

### 其他文档

| 文档 | 说明 |
|------|------|
| [日志分析报告](LEAPMOTOR_LOG_ANALYSIS.md) | 零跑车机实车日志分析结果 |
| [图标素材推荐](ICON_RESOURCES.md) | 图标素材推荐清单 |
| [项目计划](../PROJECT_PLAN.md) | 详细开发计划和进度 |
| [项目状态总览](../PROJECT_STATUS.md) | 项目状态总览 |

---

## 🚀 快速开始

### 我是用户，想安装使用

1. 看 [README.md](../README.md) 了解项目
2. 下载最新的 APK 安装包
3. 按说明授予三大核心权限
4. 开始使用

### 我是开发者，想参与开发

1. **先看架构**：[架构设计](ARCHITECTURE.md) - 了解整体结构
2. **搭环境**：[开发指南](DEVELOPMENT_GUIDE.md) - 环境搭建
3. **查接口**：[车控接口速查](CAR_CONTROL_API.md) - 车控接口参考
4. **找代码**：[代码索引](CODE_INDEX.md) - 快速定位函数
5. **遇问题**：[常见问题 FAQ](FAQ.md) - 常见问题解答

---

## 💡 文档使用技巧

### 1. 代码索引用法（最常用）

修改代码前先查索引，找到行号再精准读取：

```bash
# 1. 查索引文档，找到函数名和行号
docs/CODE_INDEX.md

# 2. 精准读取小范围代码（示例）
sed -n '3770,3785p' app/src/main/java/com/c11partner/desktop/bridge/WebViewBridge.java
```

**好处**：减少 90%+ 的上下文占用，避免篇幅过长。

### 2. 车控接口速查用法

添加车控功能前，先查接口文档：

- 确认功能是否已有接口
- 了解接口的参数和返回值
- 知道用的是 Intent / Settings / Logcat 哪种方式

### 3. FAQ 用法

遇到问题先查 FAQ：

- 80% 的常见问题都能找到答案
- 找不到再看日志和代码

---

## 📝 文档维护

### 更新文档

代码变动后，记得更新相关文档：

1. **代码变动** → 运行 `python3 tools/generate_code_index.py` 更新代码索引
2. **新增车控功能** → 更新 [车控接口速查](CAR_CONTROL_API.md)
3. **架构调整** → 更新 [架构设计](ARCHITECTURE.md)
4. **遇到新问题** → 补充到 [FAQ](FAQ.md)

### 文档规范

- 文件名：大驼峰 + 下划线，如 `CAR_CONTROL_API.md`
- 标题层级：# 一级、## 二级、### 三级
- 表格对齐：尽量对齐，美观易读
- 状态标记：✅ 已完成、⚠️ 待验证、🔍 研究中、🔲 待集成

---

## 🔗 相关链接

- **项目仓库**：https://github.com/Mariobolo/C11Partner
- **Issues**：https://github.com/Mariobolo/C11Partner/issues
- **Actions**：https://github.com/Mariobolo/C11Partner/actions

---

**文档版本**：v1.0  
**最后更新**：2026-06-20
