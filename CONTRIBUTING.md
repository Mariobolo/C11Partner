# 贡献指南

感谢您对 DiPartner 项目的关注！我们欢迎各种形式的贡献。

## 如何贡献

### 报告问题

如果您发现了 bug 或有功能建议，请通过 Issue 告诉我们：

1. 检查是否已有相关 Issue
2. 如果没有，创建新的 Issue
3. 详细描述问题或建议
4. 如果是 bug，请提供复现步骤

### 提交代码

1. **Fork 仓库**
   ```bash
   git clone https://gitee.com/hex_code/DiPartner.git
   cd DiPartner
   ```

2. **创建分支**
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **提交更改**
   ```bash
   git add .
   git commit -m "描述你的更改"
   ```

4. **推送到远程**
   ```bash
   git push origin feature/your-feature-name
   ```

5. **创建 Pull Request**

## 代码规范

### Java 代码

- 遵循 Google Java Style Guide
- 类名使用大驼峰命名法
- 方法名和变量名使用小驼峰命名法
- 常量使用全大写和下划线

### JavaScript 代码

- 使用 ES6+ 语法
- 变量使用 `const` 或 `let`，避免 `var`
- 函数使用箭头函数
- 添加适当的注释

### CSS 代码

- 使用小写和连字符命名
- 避免使用 `!important`
- 保持选择器简洁

## 提交信息规范

提交信息应该清晰描述更改内容：

```
类型: 简短描述

详细描述（可选）
```

类型包括：
- `feat`: 新功能
- `fix`: 修复 bug
- `docs`: 文档更新
- `style`: 代码格式调整
- `refactor`: 重构
- `test`: 测试相关
- `chore`: 构建/工具相关

示例：
```
feat: 添加天气图标动画效果

- 实现晴天、雨天、雪天三种动画
- 优化性能，减少内存占用
```

## 开发环境

### 必需工具

- Android Studio 4.2+
- JDK 11
- Android SDK 30

### 推荐插件

- Android ButterKnife Zelezny
- GsonFormat
- Rainbow Brackets

## 测试

提交前请确保：

1. 代码可以正常编译
2. 没有明显的崩溃问题
3. 在目标设备上测试通过

## 代码审查

Pull Request 需要经过审查才能合并。审查者会检查：

- 代码质量
- 功能正确性
- 是否符合项目规范

## 行为准则

- 尊重所有贡献者
- 接受建设性批评
- 专注于技术讨论
- 保持友好和耐心

## 联系方式

如有疑问，欢迎通过 Issue 联系我们。

再次感谢您的贡献！
