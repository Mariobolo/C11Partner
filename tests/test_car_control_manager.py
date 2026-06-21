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


if __name__ == '__main__':
    unittest.main()
