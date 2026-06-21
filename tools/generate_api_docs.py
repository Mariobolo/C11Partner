#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
C11Partner API 文档自动生成工具

功能：
1. 扫描 Java 文件中的 @JavascriptInterface 方法
2. 提取方法名、参数、返回值、注释
3. 按功能分类
4. 生成 Markdown 格式的 API 文档

使用方法：
    python3 tools/generate_api_docs.py
"""

import os
import datetime
import re
from typing import List, Dict, Optional


class MethodInfo:
    """方法信息"""
    def __init__(self):
        self.name = ""
        self.return_type = ""
        self.params = []  # [(type, name)]
        self.description = ""
        self.line_num = 0
        self.category = "其他"


def extract_javadoc(lines: List[str], start_line: int) -> str:
    """提取 Javadoc 注释"""
    doc_lines = []
    i = start_line - 1
    
    # 往上找 Javadoc 注释
    while i >= 0:
        line = lines[i].strip()
        if line.startswith('/**'):
            # 找到注释开始
            break
        elif line.startswith('*') or line.startswith('*/'):
            # 继续往上找
            i -= 1
        elif line == '' or line.startswith('@') or line.startswith('public'):
            # 空行或注解或方法定义，说明没有Javadoc
            return ""
        else:
            i -= 1
    
    if i < 0:
        return ""
    
    # 从注释开始往下读
    j = i
    while j < len(lines):
        line = lines[j].strip()
        if line.startswith('/**'):
            # 去掉 /**
            text = line[3:].strip()
            if text:
                doc_lines.append(text)
        elif line.startswith('*'):
            # 去掉 *
            text = line[1:].strip()
            if text and not text.startswith('@'):
                doc_lines.append(text)
        elif line.startswith('*/'):
            # 注释结束
            break
        j += 1
    
    return ' '.join(doc_lines).strip()


def parse_method_signature(line: str) -> Optional[MethodInfo]:
    """解析方法签名"""
    # 匹配 public 返回值 方法名(参数)
    pattern = r'public\s+(\w+[\w<>\[\]]*)\s+(\w+)\s*\((.*?)\)'
    match = re.search(pattern, line)
    
    if not match:
        return None
    
    method = MethodInfo()
    method.return_type = match.group(1)
    method.name = match.group(2)
    
    # 解析参数
    params_str = match.group(3)
    if params_str:
        params = [p.strip() for p in params_str.split(',')]
        for param in params:
            parts = param.split()
            if len(parts) >= 2:
                param_type = ' '.join(parts[:-1])
                param_name = parts[-1]
                method.params.append((param_type, param_name))
    
    return method


def categorize_method(method: MethodInfo) -> str:
    """对方法进行分类"""
    name = method.name.lower()
    
    # 分类规则（按优先级）
    rules = [
        ('360全景', ['camera360', 'camera_overspeed', '360']),
        ('灯光控制', ['lowbeam', 'rearfog', 'positionlight', 'pedestrian', 'ambientlight', 'light']),
        ('驾驶/场景模式', ['drivemode', 'guardmode', 'restmode', 'campingmode', 'powersave', 'sentinelmode']),
        ('空调控制', ['acenabled', 'windlevel', 'drivertemp', 'passengertemp', 'defrost', 'maxcooling', 'aircondition', 'acinfo', 'air']),
        ('音量控制', ['callvolume', 'navivolume', 'musicvolume', 'volume']),
        ('副屏相关', ['presentation', 'secondarydisplay', 'secondaryscreen', 'subscreen']),
        ('语音控制', ['voicecommand', 'speech', 'voice']),
        ('自动化', ['scenario', 'automation']),
        ('音乐相关', ['music', 'song', 'audio', 'playpause', 'nextmusic', 'prevmusic', 'visualizer', 'mediabutton', 'notificationlistener']),
        ('应用管理', ['applist', 'quickapp', 'launch', 'component', 'configapp', 'app']),
        ('壁纸相关', ['wallpaper', 'bing', 'carousel', 'fillmode']),
        ('ADB相关', ['adb', 'usbdebug', 'wireless', 'authorization']),
        ('权限相关', ['permission', 'hasdump', 'hasread', 'haswrite', 'hasnotification', 'securesettings']),
        ('导航相关', ['navigate', 'amap', 'company', 'home']),
        ('时间日期', ['time', 'date', 'weekday', 'lunar', 'calendar']),
        ('图片处理', ['bitmap', 'drawable', 'base64', 'scale', 'encodeimage', 'firstletter']),
        ('系统设置', ['setting', 'global', 'secure', 'nightmode', 'wifi', 'bluetooth', 'systemlauncher']),
        ('状态获取', ['get', 'is', 'has']),
        ('保存/设置', ['save', 'set', 'update']),
    ]
    
    for cat_name, keywords in rules:
        for kw in keywords:
            if kw.lower() in name:
                return cat_name
    
    return '其他'


def scan_java_file(filepath: str) -> List[MethodInfo]:
    """扫描 Java 文件，提取所有 @JavascriptInterface 方法"""
    methods = []
    
    with open(filepath, 'r', encoding='utf-8') as f:
        lines = f.readlines()
    
    for i, line in enumerate(lines):
        if '@JavascriptInterface' in line:
            # 找到 @JavascriptInterface 注解，下一行应该是方法定义
            # 但可能中间有空行或其他注解
            j = i + 1
            while j < len(lines):
                next_line = lines[j].strip()
                if next_line.startswith('public') and '(' in next_line:
                    # 找到方法定义
                    method = parse_method_signature(next_line)
                    if method:
                        method.line_num = j + 1  # 行号从1开始
                        method.description = extract_javadoc(lines, j + 1)
                        method.category = categorize_method(method)
                        methods.append(method)
                    break
                elif next_line == '' or next_line.startswith('@'):
                    # 空行或其他注解，继续往下找
                    j += 1
                else:
                    break
    
    return methods


def generate_markdown(methods: List[MethodInfo], title: str) -> str:
    """生成 Markdown 文档"""
    # 按分类分组
    categories = {}
    for method in methods:
        cat = method.category
        if cat not in categories:
            categories[cat] = []
        categories[cat].append(method)
    
    # 分类排序
    cat_order = [
        '360全景',
        '灯光控制',
        '驾驶/场景模式',
        '空调控制',
        '音量控制',
        '副屏相关',
        '语音控制',
        '自动化',
        '音乐相关',
        '应用管理',
        '壁纸相关',
        'ADB相关',
        '权限相关',
        '导航相关',
        '时间日期',
        '图片处理',
        '系统设置',
        '状态获取',
        '保存/设置',
        '其他',
    ]
    
    sorted_cats = []
    for cat in cat_order:
        if cat in categories:
            sorted_cats.append((cat, categories[cat]))
    # 添加不在排序中的分类
    for cat, methods in categories.items():
        if cat not in cat_order:
            sorted_cats.append((cat, methods))
    
    # 生成文档
    md = []
    md.append(f'# {title}')
    md.append('')
    md.append('> 📋 本文档由工具自动生成，请勿手动修改')
    md.append('> ')
    md.append(f'> 生成时间：{datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")}')
    md.append(f'> ')
    md.append(f'> 接口总数：{len(methods)} 个')
    md.append('')
    md.append('---')
    md.append('')
    
    # 目录
    md.append('## 📑 目录')
    md.append('')
    for cat, _ in sorted_cats:
        md.append(f'- [{cat}](#{cat.lower().replace("/", "").replace(" ", "-")})')
    md.append('')
    md.append('---')
    md.append('')
    
    # 每个分类的方法列表
    for cat, cat_methods in sorted_cats:
        md.append(f'## {cat}')
        md.append('')
        md.append('| 方法名 | 说明 | 返回值 | 参数 |')
        md.append('|--------|------|--------|------|')
        
        for method in sorted(cat_methods, key=lambda m: m.name):
            # 方法名
            name_str = f'`{method.name}()`'
            
            # 说明
            desc = method.description if method.description else '-'
            if len(desc) > 50:
                desc = desc[:50] + '...'
            
            # 返回值
            ret_str = f'`{method.return_type}`'
            
            # 参数
            if method.params:
                param_str = ', '.join([f'`{p[1]}`: {p[0]}' for p in method.params])
            else:
                param_str = '-'
            
            md.append(f'| {name_str} | {desc} | {ret_str} | {param_str} |')
        
        md.append('')
    
    # 详细说明部分
    md.append('---')
    md.append('')
    md.append('## 📝 详细说明')
    md.append('')
    
    for cat, cat_methods in sorted_cats:
        md.append(f'### {cat}')
        md.append('')
        
        for method in sorted(cat_methods, key=lambda m: m.name):
            md.append(f'#### `{method.name}()`')
            md.append('')
            
            if method.description:
                md.append(f'**说明**：{method.description}')
                md.append('')
            
            md.append(f'- **返回值**：`{method.return_type}`')
            
            if method.params:
                md.append('- **参数**：')
                for param_type, param_name in method.params:
                    md.append(f'  - `{param_name}`: `{param_type}`')
            else:
                md.append('- **参数**：无')
            
            md.append(f'- **行号**：第 {method.line_num} 行')
            md.append('')
    
    md.append('---')
    md.append('')
    md.append('## 🔄 重新生成')
    md.append('')
    md.append('```bash')
    md.append('python3 tools/generate_api_docs.py')
    md.append('```')
    md.append('')
    
    return '\n'.join(md)


def main():
    """主函数"""
    print('🚀 开始生成 API 文档...')
    print()
    
    # 项目根目录
    project_root = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
    
    # 要扫描的文件
    files_to_scan = [
        'app/src/main/java/com/c11partner/desktop/bridge/WebViewBridge.java',
    ]
    
    all_methods = []
    
    for filepath in files_to_scan:
        full_path = os.path.join(project_root, filepath)
        if not os.path.exists(full_path):
            print(f'⚠️  文件不存在: {filepath}')
            continue
        
        print(f'📄 扫描文件: {filepath}')
        methods = scan_java_file(full_path)
        print(f'   找到 {len(methods)} 个 @JavascriptInterface 方法')
        all_methods.extend(methods)
    
    print()
    
    if not all_methods:
        print('❌ 未找到任何方法')
        return
    
    # 生成文档
    md_content = generate_markdown(all_methods, 'WebViewBridge JS 接口文档')
    
    # 输出文件
    output_path = os.path.join(project_root, 'docs/JS_API_REFERENCE.md')
    with open(output_path, 'w', encoding='utf-8') as f:
        f.write(md_content)
    
    print(f'✅ API 文档已生成')
    print(f'   输出文件: {output_path}')
    print(f'   文档行数: {len(md_content.splitlines())} 行')
    print()
    
    # 统计信息
    categories = {}
    for method in all_methods:
        cat = method.category
        categories[cat] = categories.get(cat, 0) + 1
    
    print('📊 分类统计:')
    for cat, count in sorted(categories.items(), key=lambda x: -x[1]):
        print(f'   {cat}: {count} 个')
    
    print()
    print('🎉 完成！')


if __name__ == '__main__':
    main()
