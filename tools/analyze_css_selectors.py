#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
CSS选择器重复统计工具
用于分析widgets.css中重复出现的选择器
"""

import re
from collections import defaultdict

def analyze_css_selectors(file_path):
    """分析CSS文件中的选择器"""
    
    # 读取文件
    with open(file_path, 'r', encoding='utf-8', errors='ignore') as f:
        lines = f.readlines()
    
    # 匹配CSS选择器的正则
    # 匹配 .class, #id, element 等选择器开头的行
    selector_pattern = re.compile(r'^\s*([\.#][a-zA-Z0-9_-]+(?:\s*[,\s>\+~]\s*[\.#a-zA-Z0-9_-]+)*)\s*\{?')
    
    selectors_info = defaultdict(list)
    all_selectors = []
    
    for line_num, line in enumerate(lines, 1):
        # 跳过空行和注释
        stripped = line.strip()
        if not stripped or stripped.startswith('/*') or stripped.startswith('*'):
            continue
            
        match = selector_pattern.match(line)
        if match:
            selector = match.group(1).strip()
            # 清理选择器，去除多余空格
            selector = re.sub(r'\s+', ' ', selector)
            
            # 获取该行的上下文（前5个非空字符）
            context = stripped[:50]
            
            selectors_info[selector].append({
                'line': line_num,
                'context': context
            })
            all_selectors.append((line_num, selector))
    
    # 筛选出重复出现的选择器（出现次数 >= 2）
    duplicate_selectors = {
        sel: info for sel, info in selectors_info.items()
        if len(info) >= 2
    }
    
    # 按出现次数排序
    sorted_duplicates = sorted(
        duplicate_selectors.items(),
        key=lambda x: len(x[1]),
        reverse=True
    )
    
    return {
        'total_lines': len(lines),
        'total_selectors_found': len(all_selectors),
        'unique_selectors': len(selectors_info),
        'duplicate_selectors_count': len(duplicate_selectors),
        'duplicates': sorted_duplicates,
        'all_selectors': all_selectors
    }

def generate_report(result, output_file):
    """生成CSS重复选择器统计报告"""
    
    report_lines = []
    report_lines.append('# CSS重复选择器统计表')
    report_lines.append('')
    report_lines.append('## 统计概览')
    report_lines.append('')
    report_lines.append(f'- 文件总行数: {result["total_lines"]}')
    report_lines.append(f'- 发现选择器总数: {result["total_selectors_found"]}')
    report_lines.append(f'- 唯一选择器数量: {result["unique_selectors"]}')
    report_lines.append(f'- 重复选择器数量: {result["duplicate_selectors_count"]}')
    report_lines.append(f'- 重复率: {result["duplicate_selectors_count"]/result["unique_selectors"]*100:.1f}%')
    report_lines.append('')
    
    report_lines.append('## 重复选择器详细列表')
    report_lines.append('')
    report_lines.append('| 选择器 | 出现次数 | 行号分布 | 备注 |')
    report_lines.append('|--------|----------|----------|------|')
    
    for selector, occurrences in result['duplicates']:
        lines = ', '.join([str(o['line']) for o in occurrences])
        count = len(occurrences)
        
        # 标记重点关注的选择器
        note = ''
        key_selectors = ['.widget', '.top-status-bar', '.weather-widget', '.music-widget', '.quick-switch-item']
        for ks in key_selectors:
            if ks in selector:
                note = '⭐ 重点关注'
                break
        
        report_lines.append(f'| `{selector}` | {count} | {lines} | {note} |')
    
    report_lines.append('')
    report_lines.append('## 重点关注选择器详情')
    report_lines.append('')
    
    key_selectors = ['.widget', '.top-status-bar', '.weather-widget', '.music-widget', '.quick-switch-item']
    
    for key_sel in key_selectors:
        report_lines.append(f'### {key_sel}')
        report_lines.append('')
        
        found = False
        for selector, occurrences in result['duplicates']:
            if key_sel in selector:
                found = True
                report_lines.append(f'**{selector}** (出现 {len(occurrences)} 次)')
                report_lines.append('')
                report_lines.append('| 行号 | 上下文 |')
                report_lines.append('|------|--------|')
                for occ in occurrences:
                    ctx = occ['context'].replace('|', '\\|')
                    report_lines.append(f'| {occ["line"]} | `{ctx}` |')
                report_lines.append('')
        
        if not found:
            report_lines.append('无重复定义')
            report_lines.append('')
    
    report_lines.append('## 代码质量分析')
    report_lines.append('')
    report_lines.append('### 问题识别')
    report_lines.append('')
    report_lines.append('1. **完全重复的代码块**: 同一选择器在多处定义相同样式')
    report_lines.append('2. **版本叠加的代码**: 同一选择器多次追加样式，形成版本历史叠加')
    report_lines.append('3. **疑似无用代码**: 选择器定义但未被使用（需配合JS分析）')
    report_lines.append('4. **选择器优先级冲突**: 同一选择器在不同位置定义，可能产生样式覆盖问题')
    report_lines.append('')
    
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write('\n'.join(report_lines))
    
    print(f"报告已生成: {output_file}")

if __name__ == '__main__':
    css_file = '/home/user/.super_doubao/super-doubao-runtime/workspace/C11Partner/app/src/main/assets/css/widgets.css'
    output_file = '/home/user/.super_doubao/super-doubao-runtime/workspace/C11Partner/docs/CSS_DUPLICATE_SELECTORS.md'
    
    result = analyze_css_selectors(css_file)
    generate_report(result, output_file)
    
    # 打印摘要
    print(f"总行数: {result['total_lines']}")
    print(f"选择器总数: {result['total_selectors_found']}")
    print(f"唯一选择器: {result['unique_selectors']}")
    print(f"重复选择器: {result['duplicate_selectors_count']}")
    print()
    print("Top 10 重复最多的选择器:")
    for i, (sel, occ) in enumerate(result['duplicates'][:10], 1):
        print(f"{i}. {sel}: {len(occ)}次")
