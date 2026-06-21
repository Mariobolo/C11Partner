#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
SystemBridge 单元测试
测试系统设置Bridge类的常量定义和方法逻辑
"""
import unittest
import re
from pathlib import Path


class TestSystemBridge(unittest.TestCase):
    """SystemBridge 类测试"""

    def setUp(self):
        """读取SystemBridge.java源码"""
        java_file = Path(__file__).parent.parent / "app/src/main/java/com/c11partner/desktop/bridge/SystemBridge.java"
        with open(java_file, 'r', encoding='utf-8') as f:
            self.source_code = f.read()

    def test_class_definition(self):
        """测试类定义和继承关系"""
        self.assertIn('public class SystemBridge extends BaseBridge', self.source_code)
        self.assertIn('private static final String TAG = "SystemBridge";', self.source_code)

    def test_member_variables(self):
        """测试成员变量定义"""
        self.assertIn('private MainActivity mActivity;', self.source_code)
        self.assertIn('private WallpaperSettingsDatabaseHelper wallpaperSettingsDbHelper;', self.source_code)
        self.assertIn('private ComponentConfigDatabaseHelper componentConfigDbHelper;', self.source_code)

    def test_constructor(self):
        """测试构造函数"""
        self.assertIn('public SystemBridge(Context context, MainActivity activity)', self.source_code)
        self.assertIn('super(context, activity);', self.source_code)
        self.assertIn('this.mActivity = activity;', self.source_code)
        self.assertIn('WallpaperSettingsDatabaseHelper.getInstance(context)', self.source_code)
        self.assertIn('ComponentConfigDatabaseHelper.getInstance(context)', self.source_code)

    def test_helper_methods(self):
        """测试辅助方法"""
        self.assertIn('private boolean safeDbOperation(java.util.function.Supplier<Boolean> operation', self.source_code)
        self.assertIn('private void safeDbWrite(Runnable operation, String operationName)', self.source_code)
        self.assertIn('private <T> T safeGet(java.util.function.Supplier<T> supplier', self.source_code)
        self.assertIn('private void safeStartActivity(Intent intent, String activityName)', self.source_code)
        self.assertIn('private boolean isDatabaseAvailable()', self.source_code)
        self.assertIn('private void safeUiThreadAction(Runnable action, String actionName)', self.source_code)

    def test_network_detection_methods(self):
        """测试网络状态检测方法"""
        self.assertIn('public boolean isWifiConnected()', self.source_code)
        self.assertIn('public boolean isBluetoothConnected()', self.source_code)
        self.assertIn('ConnectivityManager', self.source_code)
        self.assertIn('NetworkCapabilities', self.source_code)
        self.assertIn('TRANSPORT_WIFI', self.source_code)
        self.assertIn('BluetoothManager', self.source_code)
        self.assertIn('BluetoothAdapter', self.source_code)

    def test_wifi_detection_logic(self):
        """测试WiFi检测逻辑"""
        self.assertIn('Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q', self.source_code)
        self.assertIn('getActiveNetwork()', self.source_code)
        self.assertIn('getNetworkCapabilities(network)', self.source_code)
        self.assertIn('NET_CAPABILITY_INTERNET', self.source_code)

    def test_bluetooth_detection_logic(self):
        """测试蓝牙检测逻辑"""
        self.assertIn('BluetoothProfile.A2DP', self.source_code)
        self.assertIn('BluetoothProfile.HEADSET', self.source_code)
        self.assertIn('BluetoothProfile.GATT', self.source_code)
        self.assertIn('getBondedDevices()', self.source_code)
        self.assertIn('Method isConnectedMethod', self.source_code)
        self.assertIn('isConnectedMethod.invoke(device)', self.source_code)

    def test_system_settings_methods(self):
        """测试系统设置保存方法"""
        self.assertIn('public boolean saveSystemLauncherSetting(boolean enabled)', self.source_code)
        self.assertIn('public void saveSystemLauncherSettingAsync(final boolean enabled, final String callbackId)', self.source_code)
        self.assertIn('public boolean saveBootGreetingSetting(boolean enabled)', self.source_code)
        self.assertIn('public boolean saveRandomModeSetting(boolean enabled)', self.source_code)
        self.assertIn('public boolean saveSpecifiedModeSetting(boolean enabled)', self.source_code)
        self.assertIn('wallpaperSettingsDbHelper.updateSystemLauncher(enabled)', self.source_code)
        self.assertIn('wallpaperSettingsDbHelper.updateBootGreeting(enabled)', self.source_code)

    def test_async_callback_pattern(self):
        """测试异步回调模式"""
        self.assertIn('new Thread(() -> {', self.source_code)
        self.assertIn('mActivity.runOnUiThread(() -> {', self.source_code)
        self.assertIn('mActivity.webView.loadUrl(javascript)', self.source_code)
        self.assertIn('handleSaveSystemLauncherSettingCallback', self.source_code)

    def test_lunar_calendar_method(self):
        """测试农历日期方法"""
        self.assertIn('public String getLunarCalendar()', self.source_code)
        self.assertIn('LunarCalendarUtils.lunarCalendar()', self.source_code)

    def test_brightness_control_methods(self):
        """测试屏幕亮度控制方法"""
        self.assertIn('public boolean setScreenBrightness(int brightness)', self.source_code)
        self.assertIn('public int getScreenBrightness()', self.source_code)
        self.assertIn('public boolean setAutoBrightness(boolean enabled)', self.source_code)
        self.assertIn('public boolean isAutoBrightnessEnabled()', self.source_code)
        self.assertIn('Settings.System.SCREEN_BRIGHTNESS', self.source_code)
        self.assertIn('Settings.System.SCREEN_BRIGHTNESS_MODE', self.source_code)
        self.assertIn('Math.max(0, Math.min(255, brightness))', self.source_code)

    def test_screen_timeout_methods(self):
        """测试屏幕超时设置方法"""
        self.assertIn('public boolean setScreenTimeout(int seconds)', self.source_code)
        self.assertIn('public int getScreenTimeout()', self.source_code)
        self.assertIn('Settings.System.SCREEN_OFF_TIMEOUT', self.source_code)
        self.assertIn('seconds * 1000', self.source_code)

    def test_component_config_methods(self):
        """测试组件配置方法"""
        self.assertIn('public boolean saveComponentConfig(String componentName, boolean isEnabled)', self.source_code)
        self.assertIn('public boolean isComponentEnabled(String componentName)', self.source_code)
        self.assertIn('public String getAllComponentConfigs()', self.source_code)
        self.assertIn('componentConfigDbHelper.saveOrUpdateComponentConfig(componentName, isEnabled)', self.source_code)
        self.assertIn('componentConfigDbHelper.isComponentEnabled(componentName)', self.source_code)

    def test_system_info_methods(self):
        """测试系统信息方法"""
        self.assertIn('public String getSystemVersionInfo()', self.source_code)
        self.assertIn('public String getAppVersionInfo()', self.source_code)
        self.assertIn('public String getMemoryInfo()', self.source_code)
        self.assertIn('public String getBatteryInfo()', self.source_code)
        self.assertIn('Build.MODEL', self.source_code)
        self.assertIn('Build.VERSION.RELEASE', self.source_code)
        self.assertIn('Build.VERSION.SDK_INT', self.source_code)

    def test_system_operation_methods(self):
        """测试系统操作方法"""
        self.assertIn('public void restartApp()', self.source_code)
        self.assertIn('public void openSystemSettings()', self.source_code)
        self.assertIn('public void openAppSettings()', self.source_code)

    def test_imports(self):
        """测试import语句"""
        self.assertIn('import android.content.Context;', self.source_code)
        self.assertIn('import android.content.Intent;', self.source_code)
        self.assertIn('import android.net.ConnectivityManager;', self.source_code)
        self.assertIn('import android.net.Network;', self.source_code)
        self.assertIn('import android.net.NetworkCapabilities;', self.source_code)
        self.assertIn('import android.os.Build;', self.source_code)
        self.assertIn('import android.webkit.JavascriptInterface;', self.source_code)
        self.assertIn('import org.json.JSONObject;', self.source_code)

    def test_javascript_interface_annotations(self):
        """测试JavascriptInterface注解数量"""
        annotation_count = self.source_code.count('@JavascriptInterface')
        self.assertGreaterEqual(annotation_count, 12, f"应该至少有12个@JavascriptInterface方法，当前只有{annotation_count}个")

    def test_error_handling(self):
        """测试异常处理"""
        catch_count = self.source_code.count('catch (Exception e)')
        self.assertGreaterEqual(catch_count, 8, f"应该有足够的异常处理，当前只有{catch_count}个")

    def test_logging(self):
        """测试日志输出"""
        log_d_count = self.source_code.count('logD(TAG,')
        log_e_count = self.source_code.count('logE(TAG,')
        self.assertGreaterEqual(log_d_count, 12, f"应该有足够的调试日志，当前只有{log_d_count}个")
        self.assertGreaterEqual(log_e_count, 8, f"应该有足够的错误日志，当前只有{log_e_count}个")

    def test_method_count(self):
        """测试方法数量"""
        method_pattern = r'public\s+\w+\s+\w+\s*\([^)]*\)'
        methods = re.findall(method_pattern, self.source_code)
        self.assertGreaterEqual(len(methods), 12, f"应该至少有12个public方法，当前只有{len(methods)}个")

    def test_null_checks(self):
        """测试空值检查"""
        null_check_count = self.source_code.count('!= null')
        self.assertGreaterEqual(null_check_count, 10, f"应该有足够的空值检查，当前只有{null_check_count}个")

    def test_database_helper_pattern(self):
        """测试数据库Helper单例模式"""
        self.assertIn('.getInstance(context)', self.source_code)

    def test_reflection_usage(self):
        """测试反射使用"""
        self.assertIn('Method isConnectedMethod', self.source_code)
        self.assertIn('.class.getMethod("isConnected")', self.source_code)


if __name__ == '__main__':
    unittest.main()
