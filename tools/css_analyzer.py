#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
CSS重复选择器分析工具
用于统计widgets.css中的重复选择器和代码块
"""

import re
from collections import defaultdict
from pathlib import Path

def analyze_css_file(css_path):
    """分析CSS文件，统计选择器出现次数和位置"""
    
    with open(css_path, 'r', encoding='utf-8', errors='ignore') as f:
        lines = f.readlines()
    
    # 选择器匹配模式
    selector_pattern = re.compile(r'^\s*([.#][a-zA-Z][\w\-]*)(?:\s|,|\{)')
    
    # 统计结果
    selector_stats = defaultdict(lambda: {'count': 0, 'lines': [], 'contexts': []})
    
    # 重点关注的选择器
    target_selectors = [
        '.widget',
        '.top-status-bar',
        '.weather-widget',
        '.music-widget',
        '.quick-switch-item'
    ]
    
    for line_num, line in enumerate(lines, 1):
        match = selector_pattern.match(line)
        if match:
            selector = match.group(1)
            
            # 获取上下文（前后5行）
            start = max(0, line_num - 3)
            end = min(len(lines), line_num + 3)
            context = ''.join(lines[start:end]).strip()[:200]
            
            selector_stats[selector]['count'] += 1
            selector_stats[selector]['lines'].append(line_num)
            selector_stats[selector]['contexts'].append(context)
    
    # 生成报告
    report = []
    report.append('# CSS重复选择器统计表\n')
    report.append(f'**分析文件**: {css_path.name}')
    report.append(f'**总行数**: {len(lines)}')
    report.append(f'**分析时间**: 自动生成\n')
    
    report.append('## 重点选择器统计\n')
    report.append('| 选择器 | 出现次数 | 行号分布 |')
    report.append('|--------|----------|----------|')
    
    for selector in target_selectors:
        if selector in selector_stats:
            stat = selector_stats[selector]
            lines_str = ', '.join(map(str, stat['lines'][:10]))
            if len(stat['lines']) > 10:
                lines_str += f' ... (+{len(stat["lines"]) - 10} more)'
            report.append(f'| {selector} | {stat["count"]} | {lines_str} |')
    
    report.append('\n## 重复次数TOP 20选择器\n')
    report.append('| 排名 | 选择器 | 出现次数 |')
    report.append('|------|--------|----------|')
    
    sorted_selectors = sorted(
        [(s, d['count']) for s, d in selector_stats.items() if d['count'] > 1],
        key=lambda x: x[1],
        reverse=True
    )
    
    for i, (selector, count) in enumerate(sorted_selectors[:20], 1):
        report.append(f'| {i} | {selector} | {count} |')
    
    report.append('\n## 重复代码块分析\n')
    
    # 分析完全重复的代码块
    report.append('### 疑似重复代码区域\n')
    report.append('基于选择器重复出现模式，以下区域可能存在重复代码：\n')
    
    for selector in target_selectors:
        if selector in selector_stats and selector_stats[selector]['count'] > 3:
            stat = selector_stats[selector]
            report.append(f'#### {selector} ({stat["count"]}次出现)\n')
            report.append(f'- 首次出现: 第{stat["lines"][0]}行')
            report.append(f'- 重复峰值: 第{stat["lines"][len(stat["lines"])//2]}行')
            report.append(f'- 末次出现: 第{stat["lines"][-1]}行')
            
            # 检查是否有版本叠加特征
            if len(stat['lines']) > 5:
                report.append('- ⚠️ **高风险**: 存在多次版本叠加痕迹，建议合并优化')
            report.append('')
    
    # 疑似无用代码检测
    report.append('### 疑似无用代码检测\n')
    report.append('以下选择器出现次数异常，可能存在无用代码：\n')
    
    suspicious = [(s, d) for s, d in selector_stats.items() 
                  if d['count'] >= 5 and not s.startswith('.quick-switch')]
    for selector, stat in suspicious[:10]:
        report.append(f'- {selector}: {stat["count"]}次定义')
    
    return '\n'.join(report)

if __name__ == '__main__':
    css_path = Path('/home/user/.super_doubao/super-doubao-runtime/workspace/C11Partner/app/src/main/assets/css/widgets.css')
    report = analyze_css_file(css_path)
    
    output_path = Path('/home/user/.super_doubao/super-doubao-runtime/workspace/C11Partner/docs/CSS_DUPLICATE_ANALYSIS.md')
    with open(output_path, 'w', encoding='utf-8') as f:
        f.write(report)
    
    print(f'分析完成，报告已生成: {output_path}')
