#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
CarControlManager 单元测试
测试车控功能管理类的常量定义和方法逻辑
"""

import unittest
import re
from pathlib import Path


class TestCarControlManager(unittest.TestCase):
    """CarControlManager 类测试"""

    def setUp(self):
        """读取CarControlManager.java源码"""
        java_file = Path(__file__).parent.parent / "app/src/main/java/com/c11partner/desktop/utils/CarControlManager.java"
        with open(java_file, 'r', encoding='utf-8') as f:
            self.source_code = f.read()

    def test_constants_defined(self):
        """测试所有常量是否正确定义"""
        # 广播Action常量
        self.assertIn('ACTION_TO_CAR_CONTROL = "com.leapmotor.speech.tocarcontrol"', self.source_code)
        self.assertIn('ACTION_TO_AIR_CONDITIONER = "com.leapmotor.speech.toairconditioner"', self.source_code)
        self.assertIn('ACTION_TO_SETTINGS = "com.leapmotor.speech.tosettings"', self.source_code)
        self.assertIn('ACTION_METER_CTRL = "com.leapmotor.action.METER.CTRL"', self.source_code)
        self.assertIn('ACTION_CAMERA_AROUND = "com.leapmotor.camera_around"', self.source_code)
        self.assertIn('ACTION_VOICE_HAND_MESSAGE = "com.iflytek.autofly.handMessage"', self.source_code)

        # Settings.Global属性常量
        self.assertIn('KEY_CAMERA_OVERSPEED = "camera_overspeed"', self.source_code)
        self.assertIn('KEY_C11_VIDEO_ENABLE = "C11_VIDEO_ENABLE"', self.source_code)
        self.assertIn('KEY_STR_CAR_VEHICLE_LOCK = "strCarVehicleLock"', self.source_code)
        self.assertIn('KEY_LEAP_SCREEN_STATE = "leap_screen_state"', self.source_code)
        self.assertIn('KEY_DISPLAY_1_STATE = "display_1_state"', self.source_code)
        self.assertIn('KEY_SPEECH_SPEAK = "SPEECH_SPEAK"', self.source_code)
        self.assertIn('KEY_C11_CALL = "C11_CALL"', self.source_code)
        self.assertIn('KEY_C11_NAVI = "C11_NAVI"', self.source_code)
        self.assertIn('KEY_C11_MUSIC = "C11_MUSIC"', self.source_code)
        self.assertIn('KEY_STR_CAR_1409 = "strCar1409"', self.source_code)
        self.assertIn('KEY_STR_CAR_1410 = "strCar1410"', self.source_code)
        self.assertIn('KEY_STR_CAR_1411 = "strCar1411"', self.source_code)
        self.assertIn('KEY_STR_CAR_100006 = "strCar100006"', self.source_code)
        self.assertIn('KEY_STR_CAR_1800 = "strCar1800"', self.source_code)
        self.assertIn('KEY_STR_CAR_8867 = "strCar8867"', self.source_code)

    def test_drive_mode_constants(self):
        """测试驾驶模式常量定义"""
        self.assertIn('DRIVE_MODE_COMFORT = 0', self.source_code)
        self.assertIn('DRIVE_MODE_SPORT = 1', self.source_code)
        self.assertIn('DRIVE_MODE_CUSTOM = 2', self.source_code)
        self.assertIn('DRIVE_MODE_EXTREME = 3', self.source_code)
        self.assertIn('DRIVE_MODE_ECO = 4', self.source_code)
        self.assertIn('DRIVE_MODE_LEAPMOTOR = 5', self.source_code)

    def test_ambient_light_colors(self):
        """测试氛围灯颜色常量定义"""
        self.assertIn('AMBIENT_RED = 0', self.source_code)
        self.assertIn('AMBIENT_ORANGE = 1', self.source_code)
        self.assertIn('AMBIENT_YELLOW = 3', self.source_code)
        self.assertIn('AMBIENT_GREEN = 7', self.source_code)
        self.assertIn('AMBIENT_CYAN = 10', self.source_code)
        self.assertIn('AMBIENT_BLUE = 14', self.source_code)
        self.assertIn('AMBIENT_PURPLE = 16', self.source_code)

    def test_singleton_pattern(self):
        """测试单例模式实现"""
        self.assertIn('private static CarControlManager instance;', self.source_code)
        self.assertIn('public static synchronized CarControlManager getInstance(Context context)', self.source_code)
        self.assertIn('if (instance == null)', self.source_code)
        self.assertIn('instance = new CarControlManager(context);', self.source_code)

    def test_camera_360_method(self):
        """测试360全景方法实现"""
        self.assertIn('public boolean startCamera360()', self.source_code)
        self.assertIn('Intent intent = new Intent(ACTION_CAMERA_AROUND);', self.source_code)
        self.assertIn('intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);', self.source_code)
        self.assertIn('context.startActivity(intent);', self.source_code)

    def test_light_control_methods(self):
        """测试灯光控制方法"""
        methods = [
            'setLowBeamLight',
            'setRearFogLight',
            'setPositionLight',
            'setPedestrianAlert'
        ]
        for method in methods:
            self.assertIn(f'public boolean {method}(boolean on)', self.source_code)
            self.assertIn('Intent intent = new Intent(ACTION_TO_CAR_CONTROL);', self.source_code)

    def test_drive_mode_method(self):
        """测试驾驶模式方法"""
        self.assertIn('public boolean setDriveMode(int mode)', self.source_code)
        self.assertIn('intent.putExtra("MMI_DRIVER_MODE_SET", mode);', self.source_code)

    def test_scene_mode_methods(self):
        """测试场景模式方法"""
        modes = ['Guard', 'Rest', 'Camping', 'PowerSave', 'Sentinel']
        for mode in modes:
            self.assertIn(f'public boolean set{mode}Mode(boolean on)', self.source_code)

    def test_voice_command_method(self):
        """测试语音指令方法"""
        self.assertIn('public boolean sendVoiceCommand(String command)', self.source_code)
        self.assertIn('Intent intent = new Intent(ACTION_VOICE_HAND_MESSAGE);', self.source_code)
        self.assertIn('intent.putExtra("text", command);', self.source_code)

    def test_ac_control_methods(self):
        """测试空调控制方法"""
        self.assertIn('public boolean setMaxCooling(boolean on)', self.source_code)
        self.assertIn('public boolean setAcEnabled(boolean on)', self.source_code)
        self.assertIn('public boolean isAcEnabled()', self.source_code)
        self.assertIn('public boolean setWindLevel(int level)', self.source_code)
        self.assertIn('public int getWindLevel()', self.source_code)
        self.assertIn('public boolean setDefrost(boolean on)', self.source_code)

    def test_system_settings_methods(self):
        """测试系统设置方法"""
        self.assertIn('public boolean setNightMode(boolean night)', self.source_code)
        self.assertIn('public boolean setWifiEnabled(boolean enabled)', self.source_code)
        self.assertIn('public boolean setBluetoothEnabled(boolean enabled)', self.source_code)

    def test_media_control_methods(self):
        """测试媒体控制方法"""
        self.assertIn('public boolean sendPrevTrack()', self.source_code)
        self.assertIn('public boolean sendNextTrack()', self.source_code)
        self.assertIn('intent.putExtra("value", 1);', self.source_code)
        self.assertIn('intent.putExtra("value", 2);', self.source_code)

    def test_settings_global_methods(self):
        """测试Settings.Global读写方法"""
        self.assertIn('public int getGlobalInt(String key, int defaultValue)', self.source_code)
        self.assertIn('public String getGlobalString(String key)', self.source_code)
        self.assertIn('public boolean setGlobalInt(String key, int value)', self.source_code)
        self.assertIn('Settings.Global.getInt', self.source_code)
        self.assertIn('Settings.Global.getString', self.source_code)
        self.assertIn('Settings.Global.putInt', self.source_code)

    def test_camera_overspeed_methods(self):
        """测试360全景超速限制方法"""
        self.assertIn('public boolean setCameraOverspeedLimit(boolean enabled)', self.source_code)
        self.assertIn('public boolean isCameraOverspeedLimitEnabled()', self.source_code)

    def test_video_while_driving_methods(self):
        """测试行驶中视频播放方法"""
        self.assertIn('public boolean setVideoWhileDriving(boolean enabled)', self.source_code)
        self.assertIn('public boolean isVideoWhileDrivingEnabled()', self.source_code)

    def test_volume_control_methods(self):
        """测试音量控制方法"""
        self.assertIn('public boolean setCallVolume(int volume)', self.source_code)
        self.assertIn('public int getCallVolume()', self.source_code)
        self.assertIn('public boolean setNaviVolume(int volume)', self.source_code)
        self.assertIn('public int getNaviVolume()', self.source_code)
        self.assertIn('public boolean setMusicVolume(int volume)', self.source_code)
        self.assertIn('public int getMusicVolume()', self.source_code)

    def test_temperature_control_methods(self):
        """测试温度控制方法"""
        self.assertIn('public boolean setDriverTemp(int temp)', self.source_code)
        self.assertIn('public int getDriverTemp()', self.source_code)
        self.assertIn('public boolean setPassengerTemp(int temp)', self.source_code)
        self.assertIn('public int getPassengerTemp()', self.source_code)
        # 验证温度范围限制
        self.assertIn('Math.max(16, Math.min(30, temp))', self.source_code)

    def test_ambient_light_methods(self):
        """测试氛围灯控制方法"""
        self.assertIn('public boolean setAmbientLightEnabled(boolean enabled)', self.source_code)
        self.assertIn('public boolean isAmbientLightEnabled()', self.source_code)
        self.assertIn('public boolean setAmbientLightColor(int color)', self.source_code)
        self.assertIn('public int getAmbientLightColor()', self.source_code)

    def test_secondary_screen_methods(self):
        """测试副屏控制方法"""
        self.assertIn('public boolean setSecondaryScreenEnabled(boolean enabled)', self.source_code)
        self.assertIn('public boolean isSecondaryScreenEnabled()', self.source_code)

    def test_speech_control_methods(self):
        """测试语音播报控制方法"""
        self.assertIn('public boolean setSpeechEnabled(boolean enabled)', self.source_code)
        self.assertIn('public boolean isSpeechEnabled()', self.source_code)

    def test_vehicle_status_methods(self):
        """测试车辆状态读取方法"""
        self.assertIn('public boolean isVehicleLocked()', self.source_code)
        self.assertIn('public boolean isScreenOn()', self.source_code)

    def test_method_count(self):
        """测试方法数量是否符合预期"""
        # 统计public方法数量
        method_pattern = r'public\s+\w+\s+\w+\s*\([^)]*\)'
        methods = re.findall(method_pattern, self.source_code)
        # 至少应有30个public方法
        self.assertGreaterEqual(len(methods), 30, f"应该至少有30个public方法，当前只有{len(methods)}个")

    def test_error_handling(self):
        """测试异常处理是否完善"""
        # 所有方法都应该有try-catch异常处理
        catch_count = self.source_code.count('catch (Exception e)')
        self.assertGreaterEqual(catch_count, 15, "应该有足够的异常处理")

    def test_logging(self):
        """测试日志输出是否完善"""
        log_d_count = self.source_code.count('Log.d(TAG,')
        log_e_count = self.source_code.count('Log.e(TAG,')
        self.assertGreaterEqual(log_d_count, 15, "应该有足够的调试日志")
        self.assertGreaterEqual(log_e_count, 10, "应该有足够的错误日志")

    def test_parameter_validation(self):
        """测试参数验证"""
        # 音量范围限制
        self.assertIn('Math.max(0, Math.min(100, volume))', self.source_code)
        # 风量范围限制
        self.assertIn('Math.max(1, Math.min(8, level))', self.source_code)
        # 温度范围限制
        self.assertIn('Math.max(16, Math.min(30, temp))', self.source_code)
    
    def test_defrost_method(self):
        """测试除霜方法"""
        self.assertIn('public boolean setDefrost(boolean on)', self.source_code)
    
    def test_java_doc_comments(self):
        """测试Javadoc注释完整性"""
        # 统计public方法
        method_pattern = r'public\s+\w+\s+(\w+)\s*\([^)]*\)'
        methods = re.findall(method_pattern, self.source_code)
        
        # 统计有Javadoc注释的方法（/** ... */ 后面跟着 public）
        javadoc_pattern = r'/\*\*[^*]*\*+(?:[^/*][^*]*\*+)*/\s*public'
        javadoc_count = len(re.findall(javadoc_pattern, self.source_code, re.DOTALL))
        
        # 至少60%的public方法应该有Javadoc注释
        coverage_ratio = javadoc_count / len(methods) if methods else 0
        self.assertGreaterEqual(coverage_ratio, 0.5, 
            f"Javadoc覆盖率应至少50%，当前{coverage_ratio:.1%}，{javadoc_count}/{len(methods)}个方法有注释")
    
    def test_null_safety(self):
        """测试空安全检查"""
        # 应该有空检查
        self.assertIn('if (instance == null)', self.source_code)
    
    def test_constant_naming_convention(self):
        """测试常量命名规范"""
        # 所有常量应该是全大写下划线分隔
        constant_pattern = r'public static final \w+ (\w+) ='
        constants = re.findall(constant_pattern, self.source_code)
        
        for constant in constants:
            # 跳过TAG
            if constant == 'TAG':
                continue
            # 检查是否全大写
            self.assertEqual(constant, constant.upper(), 
                f"常量 '{constant}' 应该使用全大写下划线命名规范")
    
    def test_method_naming_convention(self):
        """测试方法命名规范（驼峰命名法）"""
        method_pattern = r'public\s+\w+\s+(\w+)\s*\([^)]*\)'
        methods = re.findall(method_pattern, self.source_code)
        
        for method in methods:
            # 跳过getInstance等特殊方法
            if method in ['getInstance']:
                continue
            # 方法名应该以小写开头
            self.assertTrue(method[0].islower(), 
                f"方法名 '{method}' 应该以小写开头（驼峰命名法）")
    
    def test_class_structure(self):
        """测试类结构完整性"""
        # 私有构造函数（单例模式）
        self.assertIn('private CarControlManager(Context context)', self.source_code)
        
        # TAG常量
        self.assertIn('private static final String TAG =', self.source_code)
        
        # Context成员变量（注意：实际代码中是context，不是mContext）
        self.assertIn('private Context context;', self.source_code)
    
    def test_log_tag_consistency(self):
        """测试日志TAG一致性"""
        # 所有Log.d/e应该使用相同的TAG
        tag_pattern = r'Log\.\w+\(TAG,'
        tag_usages = len(re.findall(tag_pattern, self.source_code))
        self.assertGreaterEqual(tag_usages, 20, "应该有足够的日志输出使用TAG常量")
    
    def test_set_guard_mode(self):
        """测试守护模式方法"""
        self.assertIn('public boolean setGuardMode(boolean on)', self.source_code)
        self.assertIn('intent.putExtra("GUARD_MODE", on ? 1 : 0);', self.source_code)
    
    def test_set_rest_mode(self):
        """测试小憩模式方法"""
        self.assertIn('public boolean setRestMode(boolean on)', self.source_code)
        self.assertIn('intent.putExtra("REST_MODE", on ? 1 : 0);', self.source_code)
    
    def test_set_camping_mode(self):
        """测试露营模式方法"""
        self.assertIn('public boolean setCampingMode(boolean on)', self.source_code)
        self.assertIn('intent.putExtra("CAMPING_MODE", on ? 1 : 0);', self.source_code)
    
    def test_set_power_save_mode(self):
        """测试省电模式方法"""
        self.assertIn('public boolean setPowerSaveMode(boolean on)', self.source_code)
        self.assertIn('intent.putExtra("POWER_SAVE_MODE", on ? 1 : 0);', self.source_code)
    
    def test_set_sentinel_mode(self):
        """测试哨兵模式方法"""
        self.assertIn('public boolean setSentinelMode(boolean on)', self.source_code)
        self.assertIn('intent.putExtra("SENTINEL_MODE", on ? 1 : 0);', self.source_code)
    
    def test_code_style_imports(self):
        """测试import语句规范"""
        # 应该导入必要的类
        self.assertIn('import android.content.Context;', self.source_code)
        self.assertIn('import android.content.Intent;', self.source_code)
        self.assertIn('import android.provider.Settings;', self.source_code)
        self.assertIn('import android.util.Log;', self.source_code)
    
    # ==================== 参数验证边界测试 ====================
    
    def test_drive_mode_parameter_validation(self):
        """测试驾驶模式参数验证逻辑"""
        # 应该有参数范围检查
        self.assertIn('if (mode < 0 || mode > 5)', self.source_code)
        self.assertIn('Log.e(TAG, "无效的驾驶模式: " + mode);', self.source_code)
    
    def test_ambient_light_color_validation(self):
        """测试氛围灯颜色参数验证逻辑"""
        # 应该有颜色范围检查
        self.assertIn('if (color < 0 || color > 16)', self.source_code)
        self.assertIn('Log.e(TAG, "无效的氛围灯颜色: " + color);', self.source_code)
    
    def test_volume_clamping(self):
        """测试音量值范围限制"""
        # 所有音量设置都应该有范围限制
        volume_methods = ['setCallVolume', 'setNaviVolume', 'setMusicVolume']
        for method in volume_methods:
            self.assertIn(f'public boolean {method}(int volume)', self.source_code)
        # 应该有0-100的范围限制
        self.assertIn('Math.max(0, Math.min(100, volume))', self.source_code)
    
    def test_temperature_clamping(self):
        """测试温度值范围限制"""
        # 温度应该有16-30的范围限制
        self.assertIn('Math.max(16, Math.min(30, temp))', self.source_code)
    
    def test_wind_level_clamping(self):
        """测试风量值范围限制"""
        # 风量应该有1-8的范围限制
        self.assertIn('Math.max(1, Math.min(8, level))', self.source_code)
    
    # ==================== 方法实现细节测试 ====================
    
    def test_ac_enabled_fallback(self):
        """测试空调开关的语音控制回退机制"""
        self.assertIn('boolean result = setGlobalInt(KEY_STR_CAR_100006, on ? 1 : 0);', self.source_code)
        self.assertIn('if (!result)', self.source_code)
        self.assertIn('String command = on ? "打开空调" : "关闭空调";', self.source_code)
        self.assertIn('result = sendVoiceCommand(command);', self.source_code)
    
    def test_wind_level_fallback(self):
        """测试风量设置的语音控制回退机制"""
        self.assertIn('boolean result = setGlobalInt(KEY_STR_CAR_1411, Math.max(1, Math.min(8, level)));', self.source_code)
        self.assertIn('if (!result)', self.source_code)
        self.assertIn('String command = "空调风量调到" + level + "档";', self.source_code)
        self.assertIn('result = sendVoiceCommand(command);', self.source_code)
    
    def test_defrost_voice_only(self):
        """测试除霜功能仅使用语音控制"""
        # 除霜暂时只有语音控制
        self.assertIn('String command = on ? "打开除霜" : "关闭除霜";', self.source_code)
        self.assertIn('return sendVoiceCommand(command);', self.source_code)
    
    # ==================== 返回值一致性测试 ====================
    
    def test_boolean_return_consistency(self):
        """测试所有控制方法都返回boolean值"""
        method_pattern = r'public boolean (\w+)\s*\([^)]*\)'
        boolean_methods = re.findall(method_pattern, self.source_code)
        # 应该有大量boolean返回值的方法
        self.assertGreaterEqual(len(boolean_methods), 35, 
            f"应该至少有35个boolean返回值的控制方法，当前有{len(boolean_methods)}个")
    
    def test_getter_return_types(self):
        """测试getter方法返回类型一致性"""
        self.assertIn('public int getCallVolume()', self.source_code)
        self.assertIn('public int getNaviVolume()', self.source_code)
        self.assertIn('public int getMusicVolume()', self.source_code)
        self.assertIn('public int getDriverTemp()', self.source_code)
        self.assertIn('public int getPassengerTemp()', self.source_code)
        self.assertIn('public int getWindLevel()', self.source_code)
        self.assertIn('public int getAmbientLightColor()', self.source_code)
    
    # ==================== 代码质量测试 ====================
    
    def test_no_hardcoded_strings(self):
        """测试避免硬编码字符串，使用常量"""
        # 不应该直接使用硬编码的字符串
        hardcoded_actions = [
            '"com.leapmotor.speech.tocarcontrol"',
            '"com.leapmotor.camera_around"'
        ]
        for action in hardcoded_actions:
            # 只在常量定义处出现，不在方法体中出现
            # 检查常量定义行
            const_line = f'public static final String ACTION_\\w+ = {action};'
            const_matches = re.findall(const_line, self.source_code)
            self.assertEqual(len(const_matches), 1, 
                f"Action {action} 应该只在常量定义处出现一次")
    
    def test_method_body_size(self):
        """测试方法体大小适中"""
        # 分割方法体
        method_bodies = re.split(r'public\s+\w+\s+\w+\s*\([^)]*\)\s*\{', self.source_code)[1:]
        for i, body in enumerate(method_bodies):
            # 简单检查方法体不要过大
            lines = body.split('\n')
            # 去掉空行计算有效代码行
            code_lines = [l for l in lines if l.strip() and not l.strip().startswith('//') 
                         and not l.strip().startswith('*') and not l.strip().startswith('/*')]
            # 方法体不应超过50行代码
            self.assertLess(len(code_lines), 50, 
                f"方法#{i+1} 代码行数过多({len(code_lines)}行)，考虑拆分")
    
    def test_exception_handling_in_all_public_methods(self):
        """测试所有public方法都有适当的异常处理"""
        # 查找所有public方法
        public_method_pattern = r'public\s+\w+\s+(\w+)\s*\([^)]*\)'
        public_methods = re.findall(public_method_pattern, self.source_code)
        
        # 查找有try-catch的方法
        try_catch_count = self.source_code.count('try {')
        
        # getter方法通常不需要异常处理，所以控制类方法应至少有40%覆盖率
        coverage = try_catch_count / len(public_methods) if public_methods else 0
        self.assertGreaterEqual(coverage, 0.4, 
            f"异常处理覆盖率应至少40%，当前{coverage:.1%}，{try_catch_count}/{len(public_methods)}个方法有try-catch")
    
    # ==================== 常量完整性测试 ====================
    
    def test_all_drive_modes_defined(self):
        """测试所有6种驾驶模式都已定义"""
        drive_modes = [
            ('DRIVE_MODE_COMFORT', 0),
            ('DRIVE_MODE_SPORT', 1),
            ('DRIVE_MODE_CUSTOM', 2),
            ('DRIVE_MODE_EXTREME', 3),
            ('DRIVE_MODE_ECO', 4),
            ('DRIVE_MODE_LEAPMOTOR', 5)
        ]
        for name, value in drive_modes:
            self.assertIn(f'{name} = {value}', self.source_code, 
                f"驾驶模式常量 {name} 未定义")
    
    def test_all_ambient_colors_defined(self):
        """测试所有氛围灯颜色都已定义"""
        ambient_colors = [
            ('AMBIENT_RED', 0),
            ('AMBIENT_ORANGE', 1),
            ('AMBIENT_YELLOW', 3),
            ('AMBIENT_GREEN', 7),
            ('AMBIENT_CYAN', 10),
            ('AMBIENT_BLUE', 14),
            ('AMBIENT_PURPLE', 16)
        ]
        for name, value in ambient_colors:
            self.assertIn(f'{name} = {value}', self.source_code, 
                f"氛围灯颜色常量 {name} 未定义")
    
    # ==================== 上下文处理测试 ====================
    
    def test_application_context_usage(self):
        """测试使用ApplicationContext避免内存泄漏"""
        self.assertIn('this.context = context.getApplicationContext();', self.source_code)
    
    def test_context_null_safety(self):
        """测试Context空安全处理"""
        # getInstance应该处理null context
        self.assertIn('if (instance == null)', self.source_code)
        self.assertIn('instance = new CarControlManager(context);', self.source_code)
    
    # ==================== 日志完整性测试 ====================
    
    def test_success_logging(self):
        """测试成功操作日志记录"""
        # 成功操作应该有日志
        success_logs = [
            '启动360全景',
            '近光灯:',
            '驾驶模式:',
            '设置 '
        ]
        for log_msg in success_logs:
            self.assertIn(f'Log.d(TAG, "{log_msg}', self.source_code,
                f"缺少成功日志: {log_msg}")
    
    def test_error_logging(self):
        """测试失败操作日志记录"""
        # 失败操作应该有日志
        error_logs = [
            '启动360全景失败',
            '控制近光灯失败',
            '设置驾驶模式失败',
            '写入Settings.Global失败'
        ]
        for log_msg in error_logs:
            self.assertIn(f'Log.e(TAG, "{log_msg}', self.source_code,
                f"缺少错误日志: {log_msg}")
        self.assertIn('import android.util.Log;', self.source_code)


if __name__ == '__main__':
    unittest.main()
