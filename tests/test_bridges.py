#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Bridge 模块单元测试
测试 MusicBridge 和 SystemBridge 的代码质量和结构
"""
import unittest
import re
from pathlib import Path
class TestMusicBridge(unittest.TestCase):
    """MusicBridge 类测试"""
    def setUp(self):
        """读取MusicBridge.java源码"""
        java_file = Path(__file__).parent.parent / "app/src/main/java/com/c11partner/desktop/bridge/MusicBridge.java"
        with open(java_file, 'r', encoding='utf-8') as f:
            self.source_code = f.read()
    def test_class_structure(self):
        """测试类结构是否正确"""
        self.assertIn('public class MusicBridge extends BaseBridge', self.source_code)
        self.assertIn('private static final String TAG = "MusicBridge";', self.source_code)
        self.assertIn('private MainActivity mActivity;', self.source_code)
        self.assertIn('private MusicUtils musicUtils;', self.source_code)
    def test_constructor(self):
        """测试构造函数"""
        self.assertIn('public MusicBridge(Context context, MainActivity activity)', self.source_code)
        self.assertIn('super(context, activity);', self.source_code)
        self.assertIn('this.mActivity = activity;', self.source_code)
        self.assertIn('this.musicUtils = new MusicUtils(context);', self.source_code)
    def test_media_session_service_methods(self):
        """测试媒体会话服务方法"""
        self.assertIn('public void setMediaSessionService(MediaSessionService service, boolean isBound)', self.source_code)
        self.assertIn('this.mediaSessionService = service;', self.source_code)
        self.assertIn('this.isMediaSessionServiceBound = isBound;', self.source_code)
    def test_music_visualizer_methods(self):
        """测试音乐可视化方法"""
        self.assertIn('public void startMusicVisualizer()', self.source_code)
        self.assertIn('public void stopMusicVisualizer()', self.source_code)
        self.assertIn('mActivity.musicVisualizer.startVisualizer()', self.source_code)
        self.assertIn('mActivity.musicVisualizer.stopVisualizer()', self.source_code)
    def test_music_status_methods(self):
        """测试音乐状态查询方法"""
        self.assertIn('public boolean isMusicPlaying()', self.source_code)
        self.assertIn('public String getCurrentMusicName()', self.source_code)
        self.assertIn('public String getCurrentMusicArtist()', self.source_code)
        self.assertIn('public String getMusicProgressInfo()', self.source_code)
        self.assertIn('public String getSystemMusicInfo()', self.source_code)
    def test_music_control_methods(self):
        """测试音乐控制方法"""
        self.assertIn('public void playPauseMusic()', self.source_code)
        self.assertIn('public void nextMusic()', self.source_code)
        self.assertIn('public void prevMusic()', self.source_code)
        self.assertIn('CarControlManager.getInstance(mContext)', self.source_code)
        self.assertIn('sendMediaButton(', self.source_code)
    def test_deprecated_methods(self):
        """测试已废弃的兼容方法"""
        self.assertIn('@Deprecated', self.source_code)
        self.assertIn('public void playPause()', self.source_code)
        self.assertIn('public void playNext()', self.source_code)
        self.assertIn('public void playPrevious()', self.source_code)
        self.assertIn('已废弃，建议使用', self.source_code)
    def test_notification_listener_methods(self):
        """测试通知监听权限方法"""
        self.assertIn('public boolean isNotificationListenerEnabled()', self.source_code)
        self.assertIn('public void openNotificationListenerSettings()', self.source_code)
        self.assertIn('enabled_notification_listeners', self.source_code)
        self.assertIn('ACTION_NOTIFICATION_LISTENER_SETTINGS', self.source_code)
    def test_send_media_button_method(self):
        """测试发送媒体按钮方法"""
        self.assertIn('private void sendMediaButton(int keyCode)', self.source_code)
        self.assertIn('Intent.ACTION_MEDIA_BUTTON', self.source_code)
        self.assertIn('KeyEvent.ACTION_DOWN', self.source_code)
        self.assertIn('KeyEvent.ACTION_UP', self.source_code)
    def test_exception_handling(self):
        """测试异常处理"""
        catch_count = self.source_code.count('catch (Exception e)')
        self.assertGreaterEqual(catch_count, 8, "应该有足够的异常处理")
        self.assertIn('logE(TAG,', self.source_code)
    def test_logging(self):
        """测试日志记录"""
        self.assertIn('logD(TAG,', self.source_code)
        self.assertIn('logE(TAG,', self.source_code)
    def test_javascript_interface(self):
        """测试JavascriptInterface注解"""
        js_interface_count = self.source_code.count('@JavascriptInterface')
        self.assertGreaterEqual(js_interface_count, 15, "应该有至少15个JS接口方法")
    def test_imports(self):
        """测试导入语句"""
        required_imports = [
            'import android.content.Context;',
            'import android.content.Intent;',
            'import android.os.Bundle;',
            'import android.view.KeyEvent;',
            'import android.webkit.JavascriptInterface;'
        ]
        for imp in required_imports:
            self.assertIn(imp, self.source_code, f"缺少导入: {imp}")
    def test_method_count(self):
        """测试方法数量"""
        method_pattern = r'@JavascriptInterface\s*\n\s*public'
        methods = re.findall(method_pattern, self.source_code)
        self.assertGreaterEqual(len(methods), 15, f"应该至少有15个JS接口方法")
    def test_null_safety(self):
        """测试空安全检查"""
        self.assertIn('if (!isActivityValid())', self.source_code)
        self.assertIn('if (isMediaSessionServiceBound && mediaSessionService != null)', self.source_code)
        self.assertIn('if (mActivity.musicVisualizer != null)', self.source_code)
    def test_ui_thread_execution(self):
        """测试UI线程执行"""
        self.assertIn('mActivity.runOnUiThread(() -> {', self.source_code)
    def test_json_handling(self):
        """测试JSON处理"""
        self.assertIn('JSONObject progressInfo = new JSONObject();', self.source_code)
        self.assertIn('JSONObject musicInfo = new JSONObject();', self.source_code)
        self.assertIn('progressInfo.put("isPlaying",', self.source_code)
class TestSystemBridge(unittest.TestCase):
    """SystemBridge 类测试"""
    def setUp(self):
        """读取SystemBridge.java源码"""
        java_file = Path(__file__).parent.parent / "app/src/main/java/com/c11partner/desktop/bridge/SystemBridge.java"
        with open(java_file, 'r', encoding='utf-8') as f:
            self.source_code = f.read()
    def test_class_structure(self):
        """测试类结构是否正确"""
        self.assertIn('public class SystemBridge extends BaseBridge', self.source_code)
        self.assertIn('private static final String TAG = "SystemBridge";', self.source_code)
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
    def test_network_methods(self):
        """测试网络状态检测方法"""
        self.assertIn('public boolean isWifiConnected()', self.source_code)
        self.assertIn('public boolean isBluetoothConnected()', self.source_code)
        self.assertIn('ConnectivityManager', self.source_code)
        self.assertIn('BluetoothManager', self.source_code)
    def test_wifi_detection_logic(self):
        """测试WiFi检测逻辑"""
        self.assertIn('Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q', self.source_code)
        self.assertIn('NetworkCapabilities.TRANSPORT_WIFI', self.source_code)
        self.assertIn('NetworkCapabilities.NET_CAPABILITY_INTERNET', self.source_code)
        self.assertIn('connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)', self.source_code)
    def test_bluetooth_detection_logic(self):
        """测试蓝牙检测逻辑"""
        self.assertIn('BluetoothProfile.A2DP', self.source_code)
        self.assertIn('BluetoothProfile.HEADSET', self.source_code)
        self.assertIn('BluetoothProfile.GATT', self.source_code)
        self.assertIn('bluetoothAdapter.getBondedDevices()', self.source_code)
        self.assertIn('Method isConnectedMethod', self.source_code)
    def test_wallpaper_settings_methods(self):
        """测试壁纸设置保存方法"""
        self.assertIn('public boolean saveSystemLauncherSetting(boolean enabled)', self.source_code)
        self.assertIn('public void saveSystemLauncherSettingAsync(final boolean enabled, final String callbackId)', self.source_code)
        self.assertIn('public boolean saveBootGreetingSetting(boolean enabled)', self.source_code)
        self.assertIn('public boolean saveRandomModeSetting(boolean enabled)', self.source_code)
        self.assertIn('public boolean saveSpecifiedModeSetting(boolean enabled)', self.source_code)
    def test_async_callback_mechanism(self):
        """测试异步回调机制"""
        self.assertIn('new Thread(() -> {', self.source_code)
        self.assertIn('mActivity.runOnUiThread(() -> {', self.source_code)
        self.assertIn('handleSaveSystemLauncherSettingCallback', self.source_code)
        self.assertIn('mActivity.webView.loadUrl(javascript)', self.source_code)
    def test_lunar_calendar_method(self):
        """测试农历日期方法"""
        self.assertIn('public String getLunarCalendar()', self.source_code)
        self.assertIn('LunarCalendarUtils.lunarCalendar()', self.source_code)
    def test_component_config_methods(self):
        """测试组件配置方法"""
        self.assertIn('public boolean saveComponentConfig(String componentName, boolean isEnabled)', self.source_code)
        self.assertIn('public boolean isComponentEnabled(String componentName)', self.source_code)
        self.assertIn('public String getAllComponentConfigs()', self.source_code)
        self.assertIn('componentConfigDbHelper.saveOrUpdateComponentConfig', self.source_code)
        self.assertIn('componentConfigDbHelper.isComponentEnabled', self.source_code)
        self.assertIn('componentConfigDbHelper.getAllComponentConfigs()', self.source_code)
    def test_exception_handling(self):
        """测试异常处理"""
        catch_count = self.source_code.count('catch (Exception e)')
        self.assertGreaterEqual(catch_count, 10, "应该有足够的异常处理")
        self.assertIn('logE(TAG,', self.source_code)
    def test_logging(self):
        """测试日志记录"""
        self.assertIn('logD(TAG,', self.source_code)
        self.assertIn('logE(TAG,', self.source_code)
    def test_javascript_interface(self):
        """测试JavascriptInterface注解"""
        js_interface_count = self.source_code.count('@JavascriptInterface')
        self.assertGreaterEqual(js_interface_count, 11, "应该有至少11个JS接口方法")
    def test_imports(self):
        """测试导入语句"""
        required_imports = [
            'import android.content.Context;',
            'import android.net.ConnectivityManager;',
            'import android.net.Network;',
            'import android.net.NetworkCapabilities;',
            'import android.os.Build;',
            'import android.webkit.JavascriptInterface;'
        ]
        for imp in required_imports:
            self.assertIn(imp, self.source_code, f"缺少导入: {imp}")
    def test_method_count(self):
        """测试方法数量"""
        method_pattern = r'@JavascriptInterface\s*\n\s*public'
        methods = re.findall(method_pattern, self.source_code)
        self.assertGreaterEqual(len(methods), 11, f"应该至少有11个JS接口方法")
    def test_null_safety(self):
        """测试空安全检查"""
        self.assertIn('if (connectivityManager == null)', self.source_code)
        self.assertIn('if (network == null)', self.source_code)
        self.assertIn('capabilities != null', self.source_code)
        self.assertIn('if (bluetoothManager == null)', self.source_code)
        self.assertIn('if (componentConfigDbHelper != null)', self.source_code)
    def test_json_handling(self):
        """测试JSON处理"""
        self.assertIn('JSONObject configObj = new JSONObject();', self.source_code)
        self.assertIn('configObj.put(entry.getKey(), entry.getValue())', self.source_code)
    def test_reflection_usage(self):
        """测试反射使用"""
        self.assertIn('Method isConnectedMethod = android.bluetooth.BluetoothDevice.class.getMethod("isConnected")', self.source_code)
        self.assertIn('boolean isConnected = (boolean) isConnectedMethod.invoke(device)', self.source_code)
    def test_database_helper_singleton(self):
        """测试数据库Helper单例模式"""
        self.assertIn('WallpaperSettingsDatabaseHelper.getInstance(context)', self.source_code)
        self.assertIn('ComponentConfigDatabaseHelper.getInstance(context)', self.source_code)
if __name__ == '__main__':
    unittest.main()
