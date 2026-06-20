#!/usr/bin/env python3
"""
C11Partner 代码索引自动生成脚本
用法: python3 tools/generate_code_index.py
自动扫描核心文件，生成 docs/CODE_INDEX.md
"""

import re
import os
from datetime import datetime

# 项目根目录
ROOT_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

# 要扫描的文件配置
JAVA_FILES = [
    {
        'path': 'app/src/main/java/com/c11partner/desktop/MainActivity.java',
        'name': 'MainActivity.java',
        'desc': '主Activity，应用入口，状态推送，生命周期管理',
        'category': '核心入口'
    },
    {
        'path': 'app/src/main/java/com/c11partner/desktop/bridge/WebViewBridge.java',
        'name': 'WebViewBridge.java',
        'desc': 'JS桥接层，提供50+个JS接口给前端调用',
        'category': 'JS桥接层',
        'star': True
    },
    {
        'path': 'app/src/main/java/com/c11partner/desktop/utils/CarControlManager.java',
        'name': 'CarControlManager.java',
        'desc': '车控功能管理类，三层控制模型的核心实现',
        'category': '车控核心',
        'star': True
    },
    {
        'path': 'app/src/main/java/com/c11partner/desktop/LogcatMonitorService.java',
        'name': 'LogcatMonitorService.java',
        'desc': '日志监控后台服务，解析CAN信号，推送车辆状态',
        'category': '日志监控',
        'star': True
    },
    {
        'path': 'app/src/main/java/com/c11partner/desktop/utils/AutomationEngine.java',
        'name': 'AutomationEngine.java',
        'desc': '自动化场景引擎，管理12个预设自动化场景',
        'category': '自动化'
    },
    {
        'path': 'app/src/main/java/com/c11partner/desktop/CarStatusPresentation.java',
        'name': 'CarStatusPresentation.java',
        'desc': '副屏车辆状态显示Presentation',
        'category': '副屏显示'
    },
    {
        'path': 'app/src/main/java/com/c11partner/desktop/service/MusicNotificationListenerService.java',
        'name': 'MusicNotificationListenerService.java',
        'desc': '音乐通知监听服务，从通知中提取音乐信息',
        'category': '音乐模块'
    },
    {
        'path': 'app/src/main/java/com/c11partner/desktop/utils/SecondaryScreenManager.java',
        'name': 'SecondaryScreenManager.java',
        'desc': '副屏管理类，检测和控制副屏',
        'category': '副屏管理'
    },
]

JS_FILES = [
    {
        'path': 'app/src/main/assets/js/index.js',
        'name': 'index.js',
        'desc': '主JS文件，包含大部分前端逻辑',
        'category': '前端核心',
        'star': True
    },
    {
        'path': 'app/src/main/assets/js/weather.js',
        'name': 'weather.js',
        'desc': '天气模块',
        'category': '天气模块'
    },
    {
        'path': 'app/src/main/assets/js/music.js',
        'name': 'music.js',
        'desc': '音乐可视化和播放控制',
        'category': '音乐模块'
    },
]


def extract_java_methods(filepath):
    """提取Java文件中的公共方法"""
    methods = []
    if not os.path.exists(filepath):
        return methods
    
    with open(filepath, 'r', encoding='utf-8', errors='ignore') as f:
        lines = f.readlines()
    
    for i, line in enumerate(lines):
        # 匹配公共/受保护/私有方法
        match = re.match(
            r'\s*(public|protected|private)\s+'  # 访问修饰符
            r'(?:static\s+)?'  # 可选static
            r'(?:[\w<>\[\],\s]+?\s+)'  # 返回类型
            r'(\w+)\s*\(',  # 方法名
            line
        )
        if match:
            access = match.group(1)
            method_name = match.group(2)
            
            # 跳过构造函数
            class_name = os.path.basename(filepath).replace('.java', '')
            if method_name == class_name:
                continue
            
            # 跳过匿名内部类的 run 方法（太多了）
            if method_name == 'run' and i > 0:
                prev_line = lines[i-1].strip()
                if 'new Runnable()' in prev_line or 'new ' in prev_line:
                    continue
            
            # 提取方法签名（简化版）
            signature = line.strip()
            # 去掉多余空格
            signature = re.sub(r'\s+', ' ', signature)
            
            methods.append({
                'name': method_name,
                'line': i + 1,
                'access': access,
                'signature': signature
            })
    
    return methods


def extract_js_functions(filepath):
    """提取JS文件中的函数和对象"""
    functions = []
    if not os.path.exists(filepath):
        return functions
    
    with open(filepath, 'r', encoding='utf-8', errors='ignore') as f:
        lines = f.readlines()
    
    for i, line in enumerate(lines):
        # 匹配 function 定义
        match = re.match(r'\s*function\s+(\w+)\s*\(', line)
        if match:
            functions.append({
                'name': match.group(1),
                'line': i + 1,
                'type': 'function'
            })
            continue
        
        # 匹配对象方法
        match = re.match(r'\s+(\w+)\s*:\s*function\s*\(', line)
        if match:
            functions.append({
                'name': match.group(1),
                'line': i + 1,
                'type': 'method'
            })
            continue
        
        # 匹配 const 对象定义（管理器）
        match = re.match(r'const\s+(\w+)\s*=\s*\{', line)
        if match:
            functions.append({
                'name': match.group(1),
                'line': i + 1,
                'type': 'object'
            })
            continue
    
    return functions


def categorize_java_methods(methods, file_name):
    """对Java方法按功能分类"""
    categories = {}
    
    # 简单的关键词分类规则（按优先级排序，先匹配的优先）
    rules = [
        ('匿名内部类', ['run', 'compare', 'accept', 'test']),  # 优先匹配，避免混入其他分类
        ('生命周期', ['onCreate', 'onDestroy', 'onStart', 'onStop', 'onPause', 'onResume', 'onStartCommand', 'onBind']),
        ('初始化', ['init', 'initialize', 'create', 'start']),
        ('车控功能', ['setLowBeam', 'setRearFog', 'setPositionLight', 'setPedestrian', 'setDriveMode', 
                   'setGuardMode', 'setRestMode', 'setCampingMode', 'setPowerSave', 'setSentinelMode',
                   'setMaxCooling', 'setAcEnabled', 'setWindLevel', 'setDriverTemp', 'setPassengerTemp',
                   'setDefrost', 'setNightMode', 'setWifiEnabled', 'setBluetoothEnabled', 'startCamera360',
                   'sendPrevTrack', 'sendNextTrack', 'sendVoiceCommand', 'setAmbientLight',
                   'setCameraOverspeed', 'setVideoWhile', 'setCallVolume', 'setNaviVolume',
                   'setMusicVolume', 'setSecondaryScreen', 'toggleAir', 'toggleDefrost',
                   'openAirConditioning', 'adjustTemperature', 'adjustWindLevel', 'getAcInfo',
                   'initializeAcStatus']),
        ('360全景', ['Camera360', 'camera360', 'camera_overspeed']),
        ('空调控制', ['AirCondition', 'airCondition', 'AcInfo', 'acInfo', 'WindLevel', 'windLevel',
                   'DriverTemp', 'driverTemp', 'PassengerTemp', 'passengerTemp', 'Defrost', 'defrost',
                   'MaxCool', 'maxCool']),
        ('灯光控制', ['LowBeam', 'lowBeam', 'RearFog', 'rearFog', 'PositionLight', 'positionLight',
                   'AmbientLight', 'ambientLight', 'PedestrianAlert', 'pedestrianAlert']),
        ('驾驶/场景模式', ['DriveMode', 'driveMode', 'GuardMode', 'guardMode', 'RestMode', 'restMode',
                       'CampingMode', 'campingMode', 'PowerSave', 'powerSave', 'SentinelMode', 'sentinelMode']),
        ('音量控制', ['CallVolume', 'callVolume', 'NaviVolume', 'naviVolume', 'MusicVolume', 'musicVolume']),
        ('副屏相关', ['Presentation', 'presentation', 'SecondaryDisplay', 'secondaryDisplay',
                   'SecondaryScreen', 'secondaryScreen']),
        ('语音控制', ['VoiceCommand', 'voiceCommand', 'Speech', 'speech']),
        ('自动化', ['Scenario', 'scenario', 'Automation', 'automation']),
        ('音乐相关', ['Music', 'music', 'Song', 'song', 'Audio', 'audio', 'playPause', 'PlayNext',
                   'playNext', 'PlayPrevious', 'playPrevious', 'nextMusic', 'prevMusic', 'visualizer',
                   'Visualizer', 'mediaButton', 'MediaButton']),
        ('应用管理', ['AppList', 'appList', 'QuickApp', 'quickApp', 'Launch', 'launch', 'Component',
                   'component', 'ConfigApp', 'configApp']),
        ('壁纸相关', ['Wallpaper', 'wallpaper', 'Bing', 'bing', 'carousel', 'Carousel', 'fillMode',
                   'FillMode']),
        ('ADB相关', ['Adb', 'adb', 'UsbDebug', 'usbDebug', 'Wireless', 'wireless', 'Authorization',
                   'authorization']),
        ('权限相关', ['Permission', 'permission', 'hasDump', 'hasRead', 'hasWrite', 'hasNotification',
                   'notificationAccess', 'secureSettings']),
        ('导航相关', ['Navigate', 'navigate', 'Amap', 'amap', 'Company', 'company', 'Home']),
        ('时间日期', ['Time', 'time', 'Date', 'date', 'WeekDay', 'weekDay', 'Lunar', 'lunar',
                   'Calendar', 'calendar']),
        ('图片处理', ['Bitmap', 'bitmap', 'Drawable', 'drawable', 'Base64', 'base64', 'scale',
                   'Scale', 'encodeImage', 'firstLetter', 'FirstLetter', 'chinese', 'Chinese']),
        ('系统设置', ['Setting', 'setting', 'Global', 'global', 'Secure', 'secure', 'SystemLauncher',
                   'systemLauncher', 'bootGreeting', 'BootGreeting', 'randomMode', 'RandomMode',
                   'specifiedMode', 'SpecifiedMode']),
        ('状态获取', ['get', 'is', 'has']),
        ('状态监听/回调', ['onGear', 'onTurn', 'onDoor', 'onSpeed', 'onLock', 'register', 'unregister',
                       'Listener', 'listener', 'Callback', 'callback']),
        ('日志监控', ['startLog', 'stopLog', 'processCan', 'parseTurn', 'parseTire']),
        ('保存/设置', ['save', 'Save', 'set', 'update', 'Update']),
    ]
    
    for method in methods:
        name = method['name']
        categorized = False
        
        # 按优先级顺序匹配（列表顺序就是优先级）
        for cat_name, keywords in rules:
            for kw in keywords:
                if kw.lower() in name.lower():
                    if cat_name not in categories:
                        categories[cat_name] = []
                    categories[cat_name].append(method)
                    categorized = True
                    break
            if categorized:
                break
        
        if not categorized:
            if '其他' not in categories:
                categories['其他'] = []
            categories['其他'].append(method)
    
    return categories


def generate_markdown():
    """生成Markdown格式的索引文档"""
    
    md = []
    
    # 标题
    md.append('# C11Partner 代码索引文档')
    md.append('> 📌 **快速定位代码的神器** - 修改代码前先看本文档，找到对应函数行号再精准读取，避免读取大文件占用上下文')
    md.append('>')
    md.append(f'> 自动生成时间：{datetime.now().strftime("%Y-%m-%d %H:%M:%S")}')
    md.append(f'> 生成脚本：`tools/generate_code_index.py`')
    md.append('')
    md.append('---')
    md.append('')
    
    # 使用说明
    md.append('## 📖 使用说明')
    md.append('')
    md.append('### 为什么需要这个文档？')
    md.append('- 项目代码量较大（WebViewBridge 4500+行，index.js 3000+行）')
    md.append('- 每次读取整个文件会占用大量上下文，导致篇幅过长')
    md.append('- 用本文档快速定位函数行号，再精准读取小范围代码')
    md.append('')
    md.append('### 使用方法')
    md.append('1. 根据功能分类找到对应的文件和函数')
    md.append('2. 记下函数所在行号')
    md.append('3. 用 `sed -n \'起始行,结束行p\' 文件名` 精准读取代码')
    md.append('4. 或者用 Grep 搜索函数名定位')
    md.append('')
    md.append('### 重新生成索引')
    md.append('```bash')
    md.append('python3 tools/generate_code_index.py')
    md.append('```')
    md.append('')
    md.append('---')
    md.append('')
    
    # 统计信息
    total_java_methods = 0
    total_js_functions = 0
    for f in JAVA_FILES:
        methods = extract_java_methods(os.path.join(ROOT_DIR, f['path']))
        total_java_methods += len(methods)
    for f in JS_FILES:
        funcs = extract_js_functions(os.path.join(ROOT_DIR, f['path']))
        total_js_functions += len(funcs)
    
    md.append('## 📊 索引概览')
    md.append('')
    md.append('| 类型 | 文件数 | 函数/方法数 |')
    md.append('|------|--------|------------|')
    md.append(f'| Java后端 | {len(JAVA_FILES)} | {total_java_methods} |')
    md.append(f'| JS前端 | {len(JS_FILES)} | {total_js_functions} |')
    md.append(f'| **总计** | **{len(JAVA_FILES) + len(JS_FILES)}** | **{total_java_methods + total_js_functions}** |')
    md.append('')
    md.append('---')
    md.append('')
    
    # Java文件索引
    md.append('## 🔧 后端核心文件索引（Java）')
    md.append('')
    
    for f in JAVA_FILES:
        filepath = os.path.join(ROOT_DIR, f['path'])
        methods = extract_java_methods(filepath)
        
        star_mark = ' ⭐' if f.get('star') else ''
        
        md.append(f'### {f["name"]}{star_mark}')
        md.append(f'**路径**：`{f["path"]}`  ')
        md.append(f'**行数**：约 {sum(1 for _ in open(filepath, "r", encoding="utf-8", errors="ignore"))} 行  ')
        md.append(f'**职责**：{f["desc"]}')
        md.append('')
        
        if len(methods) == 0:
            md.append('*暂无方法*')
            md.append('')
            continue
        
        # 分类显示
        categories = categorize_java_methods(methods, f['name'])
        
        for cat_name, cat_methods in categories.items():
            if len(cat_methods) == 0:
                continue
            
            md.append(f'#### {cat_name}')
            md.append('')
            md.append('| 函数名 | 行号 | 访问修饰符 |')
            md.append('|--------|------|-----------|')
            
            for m in sorted(cat_methods, key=lambda x: x['line']):
                md.append(f'| `{m["name"]}()` | {m["line"]} | {m["access"]} |')
            
            md.append('')
        
        md.append('---')
        md.append('')
    
    # JS文件索引
    md.append('## 🎨 前端核心文件索引（JS）')
    md.append('')
    
    for f in JS_FILES:
        filepath = os.path.join(ROOT_DIR, f['path'])
        funcs = extract_js_functions(filepath)
        
        star_mark = ' ⭐' if f.get('star') else ''
        
        md.append(f'### {f["name"]}{star_mark}')
        md.append(f'**路径**：`{f["path"]}`  ')
        md.append(f'**行数**：约 {sum(1 for _ in open(filepath, "r", encoding="utf-8", errors="ignore"))} 行  ')
        md.append(f'**职责**：{f["desc"]}')
        md.append('')
        
        if len(funcs) == 0:
            md.append('*暂无函数*')
            md.append('')
            continue
        
        md.append('| 名称 | 行号 | 类型 |')
        md.append('|------|------|------|')
        
        for func in sorted(funcs, key=lambda x: x['line']):
            type_mark = {
                'function': '函数',
                'method': '方法',
                'object': '对象/管理器'
            }.get(func['type'], func['type'])
            
            name_display = f'`{func["name"]}`'
            if func['type'] == 'function':
                name_display = f'`{func["name"]}()`'
            
            md.append(f'| {name_display} | {func["line"]} | {type_mark} |')
        
        md.append('')
        md.append('---')
        md.append('')
    
    # 快速查找指南
    md.append('## 🚀 快速查找指南')
    md.append('')
    md.append('### 按功能找代码')
    md.append('')
    md.append('#### 我想修改车控功能')
    md.append('1. 先看 `CarControlManager.java`（核心实现）')
    md.append('2. 再看 `WebViewBridge.java`（JS接口）')
    md.append('3. 最后看前端 `QuickSwitchManager`（UI交互）')
    md.append('')
    md.append('#### 我想修改车辆状态显示')
    md.append('1. 先看 `LogcatMonitorService.java`（日志解析）')
    md.append('2. 再看 `LeapMotorCarState.java`（状态数据）')
    md.append('3. 最后看前端 `CarStateManager`（UI显示）')
    md.append('')
    md.append('#### 我想修改自动化场景')
    md.append('1. 先看 `AutomationEngine.java`（核心引擎）')
    md.append('2. 再看 `WebViewBridge.java`（配置接口）')
    md.append('3. 最后看前端 `AutomationManager`（配置UI）')
    md.append('')
    md.append('#### 我想修改音乐模块')
    md.append('1. 先看 `MusicNotificationListenerService.java`（通知监听）')
    md.append('2. 再看 `WebViewBridge.java`（JS接口）')
    md.append('3. 最后看前端 `SystemMusicManager`（UI显示）')
    md.append('')
    md.append('#### 我想修改副屏显示')
    md.append('1. 先看 `CarStatusPresentation.java`（副屏UI）')
    md.append('2. 再看 `SecondaryScreenManager.java`（副屏管理）')
    md.append('3. 最后看 `WebViewBridge.java`（控制接口）')
    md.append('')
    md.append('---')
    md.append('')
    
    # 常用修改场景速查
    md.append('### 常用修改场景速查')
    md.append('')
    md.append('| 场景 | 首选文件 | 关键函数/对象 |')
    md.append('|------|----------|--------------|')
    md.append('| 添加新的车控功能 | CarControlManager.java | setXXX() 方法 |')
    md.append('| 添加新的JS接口 | WebViewBridge.java | @JavascriptInterface 方法 |')
    md.append('| 修改快捷开关 | index.js | QuickSwitchManager |')
    md.append('| 修改状态栏车辆状态 | index.js | CarStateManager |')
    md.append('| 添加自动化场景 | AutomationEngine.java | onXXXChanged() 方法 |')
    md.append('| 修改应用列表 | index.js | renderAppsList(), filterAppList() |')
    md.append('| 修改天气模块 | weather.js | fetchWeatherData(), updateWeatherDisplay() |')
    md.append('| 修改副屏显示内容 | CarStatusPresentation.java | updateXXX() 方法 |')
    md.append('| 修改音乐信息获取 | MusicNotificationListenerService.java | extractMusicInfo() |')
    md.append('')
    md.append('---')
    md.append('')
    
    # 页脚
    md.append('**💡 小贴士**：代码变动后记得重新运行脚本更新索引！')
    md.append('')
    md.append('```bash')
    md.append('python3 tools/generate_code_index.py')
    md.append('```')
    
    return '\n'.join(md)


def main():
    """主函数"""
    print('🚀 开始生成代码索引...')
    print('')
    
    # 生成Markdown
    md_content = generate_markdown()
    
    # 写入文件
    output_path = os.path.join(ROOT_DIR, 'docs', 'CODE_INDEX.md')
    os.makedirs(os.path.dirname(output_path), exist_ok=True)
    
    with open(output_path, 'w', encoding='utf-8') as f:
        f.write(md_content)
    
    # 统计
    total_lines = len(md_content.split('\n'))
    print(f'✅ 代码索引已生成')
    print(f'   输出文件: {output_path}')
    print(f'   文档行数: {total_lines} 行')
    print('')
    print('📊 统计信息:')
    
    for f in JAVA_FILES:
        filepath = os.path.join(ROOT_DIR, f['path'])
        methods = extract_java_methods(filepath)
        print(f'   {f["name"]}: {len(methods)} 个方法')
    
    print('')
    for f in JS_FILES:
        filepath = os.path.join(ROOT_DIR, f['path'])
        funcs = extract_js_functions(filepath)
        print(f'   {f["name"]}: {len(funcs)} 个函数/对象')
    
    total_java = sum(len(extract_java_methods(os.path.join(ROOT_DIR, f['path']))) for f in JAVA_FILES)
    total_js = sum(len(extract_js_functions(os.path.join(ROOT_DIR, f['path']))) for f in JS_FILES)
    print('')
    print(f'   总计: {total_java + total_js} 个函数/方法')


if __name__ == '__main__':
    main()
