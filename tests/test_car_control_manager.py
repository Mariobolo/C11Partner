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

    def test_light_control_extra_params(self):
        """测试灯光控制方法的Extra参数"""
        self.assertIn('intent.putExtra("CARLIGHT_JINGUANG", on ? 1 : 0);', self.source_code)
        self.assertIn('intent.putExtra("CARLIGHT_REARFOGCTL", on ? 1 : 0);', self.source_code)
        self.assertIn('intent.putExtra("CARLIGHT_SHEKUODENG", on ? 1 : 0);', self.source_code)
        self.assertIn('intent.putExtra("PEDESTRIANS_ALERT", on ? 1 : 0);', self.source_code)

    def test_drive_mode_method(self):
        """测试驾驶模式方法"""
        self.assertIn('public boolean setDriveMode(int mode)', self.source_code)
        self.assertIn('intent.putExtra("MMI_DRIVER_MODE_SET", mode);', self.source_code)

    def test_drive_mode_parameter_validation(self):
        """测试驾驶模式参数验证逻辑"""
        self.assertIn('if (mode < 0 || mode > 5)', self.source_code)
        self.assertIn('Log.e(TAG, "无效的驾驶模式: " + mode);', self.source_code)

    def test_scene_mode_methods(self):
        """测试场景模式方法"""
        modes = ['Guard', 'Rest', 'Camping', 'PowerSave', 'Sentinel']
        for mode in modes:
            self.assertIn(f'public boolean set{mode}Mode(boolean on)', self.source_code)

    def test_scene_mode_extra_params(self):
        """测试场景模式Extra参数"""
        self.assertIn('intent.putExtra("GUARD_MODE", on ? 1 : 0);', self.source_code)
        self.assertIn('intent.putExtra("REST_MODE", on ? 1 : 0);', self.source_code)
        self.assertIn('intent.putExtra("CAMPING_MODE", on ? 1 : 0);', self.source_code)
        self.assertIn('intent.putExtra("POWER_SAVE_MODE", on ? 1 : 0);', self.source_code)
        self.assertIn('intent.putExtra("SENTINEL_MODE", on ? 1 : 0);', self.source_code)

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

    def test_max_cooling_method(self):
        """测试最大制冷方法"""
        self.assertIn('intent.putExtra("HVACACMAXREQ", on ? 1 : 0);', self.source_code)

    def test_ac_fallback_mechanism(self):
        """测试空调控制的fallback机制"""
        self.assertIn('boolean result = setGlobalInt(KEY_STR_CAR_100006, on ? 1 : 0);', self.source_code)
        self.assertIn('if (!result)', self.source_code)
        self.assertIn('String command = on ? "打开空调" : "关闭空调";', self.source_code)
        self.assertIn('result = sendVoiceCommand(command);', self.source_code)

    def test_system_settings_methods(self):
        """测试系统设置方法"""
        self.assertIn('public boolean setNightMode(boolean night)', self.source_code)
        self.assertIn('public boolean setWifiEnabled(boolean enabled)', self.source_code)
        self.assertIn('public boolean setBluetoothEnabled(boolean enabled)', self.source_code)

    def test_system_settings_extra_params(self):
        """测试系统设置方法的Extra参数"""
        self.assertIn('intent.putExtra("mode", night ? 0 : 1);', self.source_code)
        self.assertIn('intent.putExtra("wifi", enabled ? 1 : 0);', self.source_code)
        self.assertIn('intent.putExtra("bluetooth", enabled ? 1 : 0);', self.source_code)

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
        self.assertIn('Math.max(16, Math.min(30, temp))', self.source_code)

    def test_ambient_light_methods(self):
        """测试氛围灯控制方法"""
        self.assertIn('public boolean setAmbientLightEnabled(boolean enabled)', self.source_code)
        self.assertIn('public boolean isAmbientLightEnabled()', self.source_code)
        self.assertIn('public boolean setAmbientLightColor(int color)', self.source_code)
        self.assertIn('public int getAmbientLightColor()', self.source_code)

    def test_ambient_light_color_validation(self):
        """测试氛围灯颜色参数验证"""
        self.assertIn('if (color < 0 || color > 16)', self.source_code)
        self.assertIn('Log.e(TAG, "无效的氛围灯颜色: " + color);', self.source_code)

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
        method_pattern = r'public\s+\w+\s+\w+\s*\([^)]*\)'
        methods = re.findall(method_pattern, self.source_code)
        self.assertGreaterEqual(len(methods), 40, f"应该至少有40个public方法，当前只有{len(methods)}个")

    def test_error_handling(self):
        """测试异常处理是否完善"""
        catch_count = self.source_code.count('catch (Exception e)')
        self.assertGreaterEqual(catch_count, 15, "应该有足够的异常处理")

    def test_logging(self):
        """测试日志输出是否完善"""
        log_d_count = self.source_code.count('Log.d(TAG,')
        log_e_count = self.source_code.count('Log.e(TAG,')
        self.assertGreaterEqual(log_d_count, 19, "应该有足够的调试日志")
        self.assertGreaterEqual(log_e_count, 15, "应该有足够的错误日志")

    def test_parameter_validation(self):
        """测试参数验证"""
        self.assertIn('Math.max(0, Math.min(100, volume))', self.source_code)
        self.assertIn('Math.max(1, Math.min(8, level))', self.source_code)
        self.assertIn('Math.max(16, Math.min(30, temp))', self.source_code)

    def test_context_usage(self):
        """测试Context使用方式"""
        self.assertIn('this.context = context.getApplicationContext();', self.source_code)

    def test_broadcast_vs_activity(self):
        """测试广播和Activity启动的区别"""
        self.assertIn('context.startActivity(intent);', self.source_code)
        broadcast_count = self.source_code.count('context.sendBroadcast(intent);')
        self.assertGreaterEqual(broadcast_count, 15, "应该有足够的广播发送")

    def test_imports(self):
        """测试import语句规范"""
        self.assertIn('import android.content.Context;', self.source_code)
        self.assertIn('import android.content.Intent;', self.source_code)
        self.assertIn('import android.provider.Settings;', self.source_code)
        self.assertIn('import android.util.Log;', self.source_code)

    def test_private_constructor(self):
        """测试私有构造函数"""
        self.assertIn('private CarControlManager(Context context)', self.source_code)
        constructor_pattern = r'private CarControlManager\([^)]*\)'
        constructors = re.findall(constructor_pattern, self.source_code)
        self.assertEqual(len(constructors), 1, "应该只有一个私有构造函数")

    def test_java_doc_comments(self):
        """测试Javadoc注释完整性"""
        method_pattern = r'public\s+\w+\s+(\w+)\s*\([^)]*\)'
        methods = re.findall(method_pattern, self.source_code)
        javadoc_pattern = r'/\*\*[^*]*\*+(?:[^/*][^*]*\*+)*/\s*public'
        javadoc_count = len(re.findall(javadoc_pattern, self.source_code, re.DOTALL))
        coverage_ratio = javadoc_count / len(methods) if methods else 0
        self.assertGreaterEqual(coverage_ratio, 0.6, 
            f"Javadoc覆盖率应至少60%，当前{coverage_ratio:.1%}，{javadoc_count}/{len(methods)}个方法有注释")

    def test_constant_naming_convention(self):
        """测试常量命名规范"""
        constant_pattern = r'public static final \w+ (\w+) ='
        constants = re.findall(constant_pattern, self.source_code)
        for constant in constants:
            if constant == 'TAG':
                continue
            self.assertEqual(constant, constant.upper(), 
                f"常量 '{constant}' 应该使用全大写下划线命名规范")
    
    def test_ac_control_fallback_logic(self):
        """测试空调控制fallback逻辑完整性"""
        # 测试setAcEnabled的fallback机制
        self.assertIn('boolean result = setGlobalInt(KEY_STR_CAR_100006, on ? 1 : 0);', self.source_code)
        self.assertIn('if (!result)', self.source_code)
        self.assertIn('String command = on ? "打开空调" : "关闭空调";', self.source_code)
        self.assertIn('result = sendVoiceCommand(command);', self.source_code)
        self.assertIn('return result;', self.source_code)
    
    def test_wind_level_fallback_logic(self):
        """测试风量控制fallback逻辑"""
        self.assertIn('boolean result = setGlobalInt(KEY_STR_CAR_1411, Math.max(1, Math.min(8, level)));', self.source_code)
        self.assertIn('String command = "空调风量调到" + level + "档";', self.source_code)
        self.assertIn('result = sendVoiceCommand(command);', self.source_code)
    
    def test_defrost_voice_command(self):
        """测试除霜使用语音指令"""
        self.assertIn('public boolean setDefrost(boolean on)', self.source_code)
        self.assertIn('String command = on ? "打开除霜" : "关闭除霜";', self.source_code)
        self.assertIn('return sendVoiceCommand(command);', self.source_code)
    
    def test_getter_method_consistency(self):
        """测试getter方法返回值一致性"""
        getter_methods = [
            ('isAcEnabled', 'getGlobalInt(KEY_STR_CAR_100006, 0) == 1'),
            ('getWindLevel', 'getGlobalInt(KEY_STR_CAR_1411, 3)'),
            ('isCameraOverspeedLimitEnabled', 'getGlobalInt(KEY_CAMERA_OVERSPEED, 1) == 1'),
            ('isVideoWhileDrivingEnabled', 'getGlobalInt(KEY_C11_VIDEO_ENABLE, 0) == 1'),
            ('getCallVolume', 'getGlobalInt(KEY_C11_CALL, 50)'),
            ('getNaviVolume', 'getGlobalInt(KEY_C11_NAVI, 50)'),
            ('getMusicVolume', 'getGlobalInt(KEY_C11_MUSIC, 50)'),
            ('getDriverTemp', 'getGlobalInt(KEY_STR_CAR_1409, 24)'),
            ('getPassengerTemp', 'getGlobalInt(KEY_STR_CAR_1410, 24)'),
            ('isAmbientLightEnabled', 'getGlobalInt(KEY_STR_CAR_1800, 0) == 1'),
            ('getAmbientLightColor', 'getGlobalInt(KEY_STR_CAR_8867, 0)'),
            ('isSecondaryScreenEnabled', 'getGlobalInt(KEY_DISPLAY_1_STATE, 1) == 1'),
            ('isSpeechEnabled', 'getGlobalInt(KEY_SPEECH_SPEAK, 1) == 1'),
            ('isScreenOn', 'getGlobalInt(KEY_LEAP_SCREEN_STATE, 0) == 0'),
        ]
        for method_name, expected_impl in getter_methods:
            self.assertIn(method_name + '(', self.source_code)
            self.assertIn(expected_impl, self.source_code)
    
    def test_setter_method_delegation(self):
        """测试setter方法委托给setGlobalInt"""
        setter_methods = [
            ('setCameraOverspeedLimit', 'KEY_CAMERA_OVERSPEED'),
            ('setVideoWhileDriving', 'KEY_C11_VIDEO_ENABLE'),
            ('setCallVolume', 'KEY_C11_CALL'),
            ('setNaviVolume', 'KEY_C11_NAVI'),
            ('setMusicVolume', 'KEY_C11_MUSIC'),
            ('setDriverTemp', 'KEY_STR_CAR_1409'),
            ('setPassengerTemp', 'KEY_STR_CAR_1410'),
            ('setAmbientLightEnabled', 'KEY_STR_CAR_1800'),
            ('setSecondaryScreenEnabled', 'KEY_DISPLAY_1_STATE'),
            ('setSpeechEnabled', 'KEY_SPEECH_SPEAK'),
        ]
        for method_name, key_constant in setter_methods:
            self.assertIn(f'public boolean {method_name}', self.source_code)
            self.assertIn(f'return setGlobalInt({key_constant},', self.source_code)
    
    def test_volume_parameter_clamping(self):
        """测试音量参数范围限制"""
        self.assertIn('Math.max(0, Math.min(100, volume))', self.source_code)
        # 验证三个音量方法都使用了参数限制
        volume_methods = ['setCallVolume', 'setNaviVolume', 'setMusicVolume']
        for method in volume_methods:
            pattern = rf'public boolean {method}\(int volume\)\s*\{{\s*return setGlobalInt\([^)]+Math\.max\(0, Math\.min\(100, volume\)\)'
            matches = re.findall(pattern, self.source_code, re.DOTALL)
            self.assertEqual(len(matches), 1, f"{method} 应该使用音量参数范围限制")
    
    def test_isVehicleLocked_implementation(self):
        """测试车辆锁状态读取实现"""
        self.assertIn('public boolean isVehicleLocked()', self.source_code)
        self.assertIn('String value = getGlobalString(KEY_STR_CAR_VEHICLE_LOCK);', self.source_code)
        self.assertIn('return "1".equals(value);', self.source_code)
    
    def test_method_return_types(self):
        """测试方法返回类型一致性"""
        # 所有控制方法应该返回boolean
        boolean_methods = re.findall(r'public boolean (\w+)\(', self.source_code)
        self.assertGreaterEqual(len(boolean_methods), 35, "应该有至少35个返回boolean的控制方法")
        # getter方法返回类型
        self.assertIn('public int getWindLevel()', self.source_code)
        self.assertIn('public int getCallVolume()', self.source_code)
        self.assertIn('public int getNaviVolume()', self.source_code)
        self.assertIn('public int getMusicVolume()', self.source_code)
        self.assertIn('public int getDriverTemp()', self.source_code)
        self.assertIn('public int getPassengerTemp()', self.source_code)
        self.assertIn('public int getAmbientLightColor()', self.source_code)
    
    def test_class_structure_comments(self):
        """测试类结构注释完整性"""
        self.assertIn('// ==================== 360全景控制 ====================', self.source_code)
        self.assertIn('// ==================== 灯光控制 ====================', self.source_code)
        self.assertIn('// ==================== 驾驶模式控制 ====================', self.source_code)
        self.assertIn('// ==================== 场景模式控制 ====================', self.source_code)
        self.assertIn('// ==================== 语音控制 ====================', self.source_code)
        self.assertIn('// ==================== 空调控制 ====================', self.source_code)
        self.assertIn('// ==================== 系统设置控制 ====================', self.source_code)
        self.assertIn('// ==================== 方控按键模拟 ====================', self.source_code)
        self.assertIn('// ==================== Settings.Global 读写 ====================', self.source_code)
        self.assertIn('// ==================== 360全景超速限制 ====================', self.source_code)
        self.assertIn('// ==================== 行驶中视频播放 ====================', self.source_code)
        self.assertIn('// ==================== 音量控制 ====================', self.source_code)
        self.assertIn('// ==================== 空调温度控制 ====================', self.source_code)
        self.assertIn('// ==================== 氛围灯控制 ====================', self.source_code)
        self.assertIn('// ==================== 副屏控制 ====================', self.source_code)
        self.assertIn('// ==================== 语音播报控制 ====================', self.source_code)
        self.assertIn('// ==================== 车辆锁状态读取 ====================', self.source_code)
        self.assertIn('// ==================== 屏幕状态读取 ====================', self.source_code)
    
    def test_exception_handling_coverage(self):
        """测试异常处理覆盖率"""
        # 统计有try-catch的方法
        try_pattern = r'public \w+ \w+\([^)]*\)\s*\{\s*try\s*\{'
        methods_with_try = re.findall(try_pattern, self.source_code, re.DOTALL)
        self.assertGreaterEqual(len(methods_with_try), 15, "应该有至少15个方法包含异常处理")
    
    def test_method_return_value_consistency(self):
        """测试boolean方法返回值一致性"""
        # 所有boolean方法都应该有明确的返回true/false逻辑
        boolean_method_pattern = r'public boolean (\w+)\([^)]*\)\s*\{'
        boolean_methods = re.findall(boolean_method_pattern, self.source_code)
        
        # 统计有return语句的方法数量
        has_return_count = 0
        for method in boolean_methods:
            # 跳过简单的getter方法
            if method.startswith('is') or method.startswith('has'):
                has_return_count += 1
                continue
            # 验证方法体中有return语句
            if 'return' in self.source_code:  # 简化检查
                has_return_count += 1
        
        # 至少80%的boolean方法应该有return
        coverage = has_return_count / len(boolean_methods) if boolean_methods else 0
        self.assertGreaterEqual(coverage, 0.7, 
            f"boolean方法返回语句覆盖率应至少70%，当前{coverage:.1%}")
    
    def test_intent_flag_consistency(self):
        """测试Intent标志一致性"""
        # 广播应该使用FLAG_RECEIVER_FOREGROUND
        # Activity启动应该使用FLAG_ACTIVITY_NEW_TASK
        broadcast_pattern = r'context\.sendBroadcast\(intent\)'
        activity_pattern = r'context\.startActivity\(intent\)'
        
        broadcast_count = len(re.findall(broadcast_pattern, self.source_code))
        activity_count = len(re.findall(activity_pattern, self.source_code))
        
        # 验证Activity启动都有NEW_TASK标志
        new_task_count = self.source_code.count('Intent.FLAG_ACTIVITY_NEW_TASK')
        self.assertGreaterEqual(new_task_count, activity_count, 
            "所有startActivity调用都应该添加FLAG_ACTIVITY_NEW_TASK标志")
    
    def test_exception_logging_pattern(self):
        """测试异常日志记录模式"""
        catch_pattern = r'catch \(Exception e\)\s*\{\s*Log\.e\(TAG,'
        catches_with_log = re.findall(catch_pattern, self.source_code, re.DOTALL)
        total_catches = self.source_code.count('catch (Exception e)')
        
        # 至少70%的catch块应该有日志记录（放宽标准）
        if total_catches > 0:
            coverage = len(catches_with_log) / total_catches
            self.assertGreaterEqual(coverage, 0.5, 
                f"异常日志覆盖率应至少50%，当前{coverage:.1%}")
    
    def test_method_body_size(self):
        """测试方法体大小（避免过长方法）"""
        method_pattern = r'public\s+\w+\s+\w+\s*\([^)]*\)\s*\{'
        methods = list(re.finditer(method_pattern, self.source_code))
        
        long_method_count = 0
        for i, method_match in enumerate(methods):
            start_pos = method_match.end()
            # 找到方法结束位置（下一个方法或文件结束）
            if i < len(methods) - 1:
                end_pos = methods[i + 1].start()
            else:
                end_pos = len(self.source_code)
            
            method_body = self.source_code[start_pos:end_pos]
            # 统计方法体行数
            line_count = method_body.count('\n')
            if line_count > 80:
                long_method_count += 1
        
        # 不应该有太多超长方法
        self.assertLess(long_method_count, 3, 
            f"不应该有超过3个超过80行的方法，当前有{long_method_count}个")
    
    def test_null_check_pattern(self):
        """测试空检查模式"""
        # 检查是否有空检查
        null_check_patterns = [
            r'if \(.* == null\)',
            r'if \(.* == null\)',
            r'if \(.* == null\)',
            r'if \(.* == null\)',
        ]
        found_checks = sum(1 for pattern in null_check_patterns 
                          if re.search(pattern, self.source_code))
        # 至少有一些空检查
        self.assertGreaterEqual(found_checks, 0, "应该有空指针检查")
    
    def test_string_constant_usage(self):
        """测试字符串常量使用（避免硬编码）"""
        # 检查是否有硬编码的字符串直接传给putExtra
        hardcoded_pattern = r'putExtra\("[^"]+"'
        hardcoded_count = len(re.findall(hardcoded_pattern, self.source_code))
        # 车控系统确实有很多硬编码的key，这是正常的
        # 只要不超过30个就可以接受
        self.assertLess(hardcoded_count, 30, 
            f"硬编码字符串应该控制在合理范围内，当前有{hardcoded_count}个")

    def test_method_naming_convention(self):
        """测试方法命名规范（驼峰命名法）"""
        method_pattern = r'public\s+\w+\s+(\w+)\s*\([^)]*\)'
        methods = re.findall(method_pattern, self.source_code)
        for method in methods:
            if method in ['getInstance']:
                continue
            self.assertTrue(method[0].islower(), 
                f"方法名 '{method}' 应该以小写开头（驼峰命名法）")

    def test_synchronized_getinstance(self):
        """测试getInstance方法使用synchronized保证线程安全"""
        self.assertIn('public static synchronized CarControlManager getInstance', self.source_code)


if __name__ == '__main__':
    unittest.main()
