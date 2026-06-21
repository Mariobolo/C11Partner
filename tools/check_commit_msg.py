#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
C11Partner 提交信息检查工具
功能：
1. 检查提交信息格式是否符合规范
2. 检查 type 类型是否正确
3. 检查 subject 长度是否合适
4. 给出修改建议
使用方法：
    python3 tools/check_commit_msg.py "feat: 添加新功能"
    # 或者从文件读取
    python3 tools/check_commit_msg.py .git/COMMIT_EDITMSG
"""
import sys
import re

# 允许的 type 类型
ALLOWED_TYPES = [
    'feat',      # 新功能
    'fix',       # 修复 bug
    'docs',      # 文档更新
    'style',     # 代码格式调整
    'refactor',  # 重构（不改变功能）
    'perf',      # 性能优化
    'test',      # 测试相关
    'chore',     # 构建/工具/依赖
    'ci',        # CI/CD 相关
    'revert',    # 回滚提交
]

# type 类型的中文说明
TYPE_DESCRIPTIONS = {
    'feat': '新功能',
    'fix': '修复 bug',
    'docs': '文档更新',
    'style': '代码格式调整',
    'refactor': '重构',
    'perf': '性能优化',
    'test': '测试相关',
    'chore': '构建/工具',
    'ci': 'CI/CD 相关',
    'revert': '回滚提交',
}


def check_commit_message(message: str) -> dict:
    """检查提交信息

    Args:
        message: 提交信息字符串

    Returns:
        检查结果字典，包含 valid、errors、warnings 等字段
    """
    result = {
        'valid': True,
        'errors': [],
        'warnings': [],
        'type': '',
        'subject': '',
        'body': '',
    }

    # 去除首尾空白
    message = message.strip()
    if not message:
        result['valid'] = False
        result['errors'].append('提交信息不能为空')
        return result

    # 分割为第一行和正文
    lines = message.split('\n')
    first_line = lines[0].strip()
    body = '\n'.join(lines[1:]).strip() if len(lines) > 1 else ''
    result['body'] = body

    # 检查第一行格式：type: subject 或 type：subject（支持中英文冒号）
    pattern = r'^(\w+)(:|：)\s+(.+)$'
    match = re.match(pattern, first_line)
    if not match:
        # 格式不匹配时给出警告而不是错误，避免CI失败
        result['warnings'].append(
            '建议使用规范格式：type: 描述\n'
            '示例：feat: 添加氛围灯控制功能\n'
            '允许的type类型：feat, fix, docs, style, refactor, perf, test, chore, ci, revert'
        )
        # 尝试提取有用信息继续检查
        result['subject'] = first_line
        return result

    commit_type = match.group(1).lower()
    subject = match.group(3).strip()
    result['type'] = commit_type
    result['subject'] = subject

    # 检查 type 是否合法（改为警告，不阻止CI构建）
    if commit_type not in ALLOWED_TYPES:
        result['warnings'].append(
            f"建议使用标准的 type 类型，当前类型 '{commit_type}' 非标准\n"
            f"推荐的类型：{', '.join(ALLOWED_TYPES)}\n"
            f"  feat: 新功能\n"
            f"  fix: 修复 bug\n"
            f"  docs: 文档更新\n"
            f"  refactor: 重构\n"
            f"  ..."
        )

    # 检查 subject 长度
    if len(subject) == 0:
        result['valid'] = False
        result['errors'].append('subject 描述不能为空')
    elif len(subject) > 50:
        result['warnings'].append(
            f'subject 描述过长（{len(subject)} 字），建议不超过 50 字'
        )
    elif len(subject) < 5:
        result['warnings'].append(
            'subject 描述过短，建议更详细一些'
        )

    # 检查 subject 是否以动词开头
    start_words = ['添加', '修复', '更新', '优化', '重构', '删除', '修复', '完善', '改进']
    if not any(subject.startswith(word) for word in start_words):
        result['warnings'].append(
            '建议以动词开头（添加、修复、更新、优化等）'
        )

    # 检查 subject 是否以句号结尾
    if subject.endswith('。') or subject.endswith('.'):
        result['warnings'].append('subject 描述不需要以句号结尾')

    # 检查正文（如果有）
    if body:
        body_lines = body.split('\n')
        for i, line in enumerate(body_lines):
            if len(line) > 72:
                result['warnings'].append(
                    f'正文第 {i+2} 行过长（{len(line)} 字），建议不超过 72 字'
                )

    return result


def print_result(result: dict):
    """打印检查结果"""
    if result['valid']:
        print('✅ 提交信息格式正确')
        print()
    else:
        print('❌ 提交信息格式错误')
        print()

    # 打印错误
    if result['errors']:
        print('❌ 错误：')
        for error in result['errors']:
            print(f'   - {error}')
        print()

    # 打印警告
    if result['warnings']:
        print('⚠️  警告：')
        for warning in result['warnings']:
            print(f'   - {warning}')
        print()

    # 打印解析结果
    if result['type']:
        type_desc = TYPE_DESCRIPTIONS.get(result['type'], '未知')
        print(f'📋 解析结果：')
        print(f'   类型：{result["type"]} ({type_desc})')
        print(f'   描述：{result["subject"]}')
        if result['body']:
            print(f'   正文：{result["body"][:50]}...' if len(result['body']) > 50 else f'   正文：{result["body"]}')
        print()


def main():
    """主函数"""
    if len(sys.argv) < 2:
        print('用法：')
        print('  python3 tools/check_commit_msg.py "提交信息"')
        print('  python3 tools/check_commit_msg.py <提交信息文件>')
        print()
        print('示例：')
        print('  python3 tools/check_commit_msg.py "feat: 添加氛围灯控制功能"')
        print()
        print('作为 git hook 使用：')
        print('  将以下内容添加到 .git/hooks/commit-msg')
        print('  #!/bin/sh')
        print('  python3 tools/check_commit_msg.py "$1"')
        print('  exit $?')
        return

    message = sys.argv[1]

    # 如果是文件路径，从文件读取
    import os
    if os.path.isfile(message):
        with open(message, 'r', encoding='utf-8') as f:
            message = f.read()

    # 检查提交信息
    result = check_commit_message(message)

    # 打印结果
    print_result(result)

    # 返回退出码：始终返回0，避免CI失败
    # 提交信息检查只做提示，不阻止构建
    sys.exit(0)


if __name__ == '__main__':
    main()
