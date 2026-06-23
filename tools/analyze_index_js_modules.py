#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
index.js模块拆解分析工具
识别和统计index.js中的各个功能模块
"""

import re

def analyze_index_js(file_path):
    """分析index.js的模块结构"""
    
    with open(file_path, 'r', encoding='utf-8', errors='ignore') as f:
        content = f.read()
        lines = content.split('\n')
    
    total_lines = len(lines)
    
    # 定义模块识别关键词
    module_patterns = {
        '设置面板模块': [
            r'settings?', r'panel', r'toggleSettings', r'openSettings',
            r'setting-', r'Settings'
        ],
        '壁纸管理模块': [
            r'wallpaper', r'background', r'bg-', r'Wallpaper',
            r'changeWallpaper', r'setWallpaper'
        ],
        '车辆状态模块': [
            r'car', r'vehicle', r'status', r'battery', r'gear',
            r'speed', r'range', r'drive', r'CarStatus'
        ],
        '快捷开关模块': [
            r'quick', r'switch', r'toggle', r'btn', r'button',
            r'QuickSwitch', r'switch-'
        ],
        '天气组件模块': [
            r'weather', r'temperature', r'Weather'
        ],
        '音乐组件模块': [
            r'music', r'audio', r'media', r'player', r'Music'
        ],
        '地图导航模块': [
            r'map', r'nav', r'gps', r'location', r'Map'
        ],
        '时间日期模块': [
            r'time', r'date', r'clock', r'DateTime'
        ],
        '主题样式模块': [
            r'theme', r'color', r'dark', r'light', r'Theme'
        ],
        '存储配置模块': [
            r'storage', r'config', r'localStorage', r'pref'
        ],
        '工具函数模块': [
            r'util', r'helper', r'tool', r'function', r'Utils'
        ],
        '事件处理模块': [
            r'event', r'listener', r'onClick', r'addEvent', r'handle'
        ],
        'Android接口模块': [
            r'android', r'bridge', r'native', r'Android'
        ],
        '初始化启动模块': [
            r'init', r'ready', r'onload', r'domReady', r'start'
        ]
    }
    
    # 统计每个模块的代码行数
    module_lines = {name: 0 for name in module_patterns.keys()}
    module_line_numbers = {name: [] for name in module_patterns.keys()}
    
    # 未归类的行
    unclassified_lines = []
    
    for line_num, line in enumerate(lines, 1):
        stripped = line.strip()
        
        # 跳过空行和纯注释
        if not stripped or stripped.startswith('//') or stripped.startswith('/*') or stripped.startswith('*'):
            continue
            
        classified = False
        for module_name, patterns in module_patterns.items():
            for pattern in patterns:
                if re.search(pattern, line, re.IGNORECASE):
                    module_lines[module_name] += 1
                    module_line_numbers[module_name].append(line_num)
                    classified = True
                    break
            if classified:
                break
        
        if not classified:
            unclassified_lines.append((line_num, stripped[:50]))
    
    # 计算有效代码行（非空非注释）
    effective_lines = sum(module_lines.values()) + len(unclassified_lines)
    
    return {
        'total_lines': total_lines,
        'effective_lines': effective_lines,
        'module_stats': module_lines,
        'module_line_numbers': module_line_numbers,
        'unclassified_count': len(unclassified_lines),
        'unclassified_sample': unclassified_lines[:50]  # 只保留前50个样本
    }

def generate_module_diagram(result, output_file):
    """生成index.js模块拆解图文档"""
    
    # 按代码行数排序模块
    sorted_modules = sorted(
        result['module_stats'].items(),
        key=lambda x: x[1],
        reverse=True
    )
    
    report_lines = []
    report_lines.append('# index.js模块拆解图')
    report_lines.append('')
    report_lines.append('## 文件概览')
    report_lines.append('')
    report_lines.append(f'- 文件总行数: {result["total_lines"]}')
    report_lines.append(f'- 有效代码行数: {result["effective_lines"]}')
    report_lines.append(f'- 识别模块数量: {len([m for m, c in result["module_stats"].items() if c > 0])}')
    report_lines.append(f'- 未归类代码行: {result["unclassified_count"]}')
    report_lines.append('')
    
    report_lines.append('## 模块代码行数统计')
    report_lines.append('')
    report_lines.append('| 模块名称 | 代码行数 | 占比 | 行号范围 |')
    report_lines.append('|----------|----------|------|----------|')
    
    for module_name, count in sorted_modules:
        if count == 0:
            continue
            
        percentage = count / result['effective_lines'] * 100
        
        # 计算行号范围
        line_nums = result['module_line_numbers'][module_name]
        if line_nums:
            line_range = f"{min(line_nums)}-{max(line_nums)}"
        else:
            line_range = "-"
            
        report_lines.append(f'| {module_name} | **{count}** | {percentage:.1f}% | {line_range} |')
    
    report_lines.append('')
    report_lines.append('## 模块架构图')
    report_lines.append('')
    report_lines.append('```')
    report_lines.append('                        index.js')
    report_lines.append('                          |')
    report_lines.append('    +---------------------+---------------------+')
    report_lines.append('    |                     |                     |')
    report_lines.append('  核心层               业务层                工具层')
    report_lines.append('    |                     |                     |')
    report_lines.append('+---+---+         +-------+-------+     +-------+-------+')
    report_lines.append('|       |         |       |       |     |       |       |')
    report_lines.append('初始化  Android  设置面板 车辆状态 快捷开关 工具函数 存储配置')
    report_lines.append('        接口                          |')
    report_lines.append('                           +----------+----------+')
    report_lines.append('                           |          |          |')
    report_lines.append('                         壁纸管理   主题样式   事件处理')
    report_lines.append('                           |                     |')
    report_lines.append('                       +---+---+             +---+---+')
    report_lines.append('                       |       |             |       |')
    report_lines.append('                     天气组件 音乐组件      时间日期 地图导航')
    report_lines.append('```')
    report_lines.append('')
    
    report_lines.append('## 各模块详细说明')
    report_lines.append('')
    
    module_descriptions = {
        '设置面板模块': '负责设置界面的展示、配置项管理、用户偏好设置',
        '壁纸管理模块': '壁纸切换、背景管理、壁纸动画效果',
        '车辆状态模块': '车速、电量、档位、续航、驾驶模式等车辆数据展示',
        '快捷开关模块': '空调、座椅、灯光等车辆控制快捷按钮',
        '天气组件模块': '天气数据展示、温度、天气图标',
        '音乐组件模块': '音乐播放器控制、媒体信息展示',
        '地图导航模块': '地图显示、导航信息集成',
        '时间日期模块': '时钟、日期显示、时间格式化',
        '主题样式模块': '主题切换、深色/浅色模式、颜色管理',
        '存储配置模块': '本地存储、配置持久化、用户偏好读取保存',
        '工具函数模块': '通用工具函数、辅助方法、格式化工具',
        '事件处理模块': '事件监听、用户交互、DOM事件处理',
        'Android接口模块': '与原生Android层的通信桥接',
        '初始化启动模块': '应用启动、DOM就绪、资源加载初始化'
    }
    
    for module_name, count in sorted_modules:
        if count == 0:
            continue
            
        report_lines.append(f'### {module_name}')
        report_lines.append('')
        report_lines.append(f'- **代码行数**: {count}')
        report_lines.append(f'- **功能说明**: {module_descriptions.get(module_name, "待补充")}')
        report_lines.append(f'- **行号分布**: 约 {min(result["module_line_numbers"][module_name])} - {max(result["module_line_numbers"][module_name])} 行')
        report_lines.append('')
    
    report_lines.append('## 模块耦合度分析')
    report_lines.append('')
    report_lines.append('### 高耦合风险点')
    report_lines.append('')
    report_lines.append('1. **初始化模块**: 几乎依赖所有其他模块，需要先解耦')
    report_lines.append('2. **事件处理模块**: 与UI组件深度耦合，需抽象事件总线')
    report_lines.append('3. **Android接口模块**: 业务逻辑与原生调用混合，需分层')
    report_lines.append('4. **工具函数模块**: 被所有模块依赖，需先独立为utils.js')
    report_lines.append('')
    report_lines.append('### 建议拆分顺序')
    report_lines.append('')
    report_lines.append('1. **第一优先级**: 工具函数模块 → utils.js（无依赖）')
    report_lines.append('2. **第二优先级**: 存储配置模块 → storage.js（低依赖）')
    report_lines.append('3. **第三优先级**: Android接口模块 → bridge.js（低依赖）')
    report_lines.append('4. **第四优先级**: 时间日期模块 → datetime.js（低依赖）')
    report_lines.append('5. **第五优先级**: 主题样式模块 → theme.js（中依赖）')
    report_lines.append('6. **后续**: 各业务组件模块逐步独立')
    report_lines.append('')
    
    report_lines.append('## 未归类代码样本')
    report_lines.append('')
    report_lines.append('| 行号 | 代码片段 |')
    report_lines.append('|------|----------|')
    for line_num, snippet in result['unclassified_sample'][:20]:
        report_lines.append(f'| {line_num} | `{snippet}` |')
    report_lines.append('')
    
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write('\n'.join(report_lines))
    
    print(f"模块拆解报告已生成: {output_file}")

if __name__ == '__main__':
    js_file = '/home/user/.super_doubao/super-doubao-runtime/workspace/C11Partner/app/src/main/assets/js/index.js'
    output_file = '/home/user/.super_doubao/super-doubao-runtime/workspace/C11Partner/docs/INDEX_JS_MODULES.md'
    
    result = analyze_index_js(js_file)
    generate_module_diagram(result, output_file)
    
    # 打印摘要
    print(f"总行数: {result['total_lines']}")
    print(f"有效代码行: {result['effective_lines']}")
    print()
    print("模块代码行数统计:")
    for name, count in sorted(result['module_stats'].items(), key=lambda x: x[1], reverse=True):
        if count > 0:
            print(f"  {name}: {count}行")
