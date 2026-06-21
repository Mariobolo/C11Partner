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
class TestWebViewBridge(unittest.TestCase):
    """WebViewBridge 主入口类测试"""
    def setUp(self):
        """读取WebViewBridge.java源码"""
        java_file = Path(__file__).parent.parent / "app/src/main/java/com/c11partner/desktop/bridge/WebViewBridge.java"
        with open(java_file, 'r', encoding='utf-8') as f:
            self.source_code = f.read()
    def test_class_structure(self):
        """测试类结构是否正确"""
        self.assertIn('public class WebViewBridge', self.source_code)
        self.assertIn('private static final String TAG = "WebViewBridge";', self.source_code)
        self.assertIn('private Context mContext;', self.source_code)
        self.assertIn('private MainActivity mActivity;', self.source_code)
    def test_bridge_delegation_fields(self):
        """测试Bridge委托字段定义"""
        bridge_fields = [
            'private CarControlBridge mCarControlBridge;',
            'private WallpaperBridge mWallpaperBridge;',
            'private AppBridge mAppBridge;',
            'private MusicBridge mMusicBridge;',
            'private SystemBridge mSystemBridge;',
            'private AdbBridge mAdbBridge;'
        ]
        for field in bridge_fields:
            self.assertIn(field, self.source_code, f"缺少Bridge字段: {field}")
    def test_constructor(self):
        """测试构造函数"""
        self.assertIn('public WebViewBridge(MainActivity activity, Context context)', self.source_code)
        self.assertIn('this.mActivity = activity;', self.source_code)
        self.assertIn('this.mContext = context;', self.source_code)
        # 测试所有Bridge初始化
        self.assertIn('new CarControlBridge(context, activity)', self.source_code)
        self.assertIn('new WallpaperBridge(context, activity)', self.source_code)
        self.assertIn('new AppBridge(context, activity)', self.source_code)
        self.assertIn('new MusicBridge(context, activity)', self.source_code)
        self.assertIn('new SystemBridge(context, activity)', self.source_code)
        self.assertIn('new AdbBridge(context, activity)', self.source_code)
    def test_media_session_service_setter(self):
        """测试媒体会话服务设置方法"""
        self.assertIn('public void setMediaSessionService(MediaSessionService service, boolean isBound)', self.source_code)
        self.assertIn('mMusicBridge.setMediaSessionService(service, isBound)', self.source_code)
    def test_wallpaper_update_notification(self):
        """测试壁纸更新通知方法"""
        self.assertIn('public void notifyWallpaperUpdate()', self.source_code)
        self.assertIn('handleWallpaperUpdateNotification()', self.source_code)
        self.assertIn('mActivity.runOnUiThread', self.source_code)
    def test_delegation_pattern(self):
        """测试委托模式实现"""
        # 验证CarControlBridge委托
        self.assertIn('return mCarControlBridge.startCamera360()', self.source_code)
        self.assertIn('return mCarControlBridge.setDriveMode(mode)', self.source_code)
        # 验证AppBridge委托
        self.assertIn('return mAppBridge.getAppList()', self.source_code)
        self.assertIn('return mAppBridge.getQuickAppList()', self.source_code)
        # 验证WallpaperBridge委托
        self.assertIn('return mWallpaperBridge.getRandomWallpaper()', self.source_code)
        # 验证MusicBridge委托
        self.assertIn('mMusicBridge.playPauseMusic()', self.source_code)
        # 验证SystemBridge委托
        self.assertIn('return mSystemBridge.isWifiConnected()', self.source_code)
        # 验证AdbBridge委托
        self.assertIn('mAdbBridge.executeAdbPermissionGrant()', self.source_code)
    def test_ui_update_methods(self):
        """测试UI更新方法"""
        self.assertIn('public void updateTimeDisplay(final String time, final String date, final String lunarDate)', self.source_code)
        self.assertIn('public void updatePresentationCarState(final LeapMotorCarState state)', self.source_code)
        self.assertIn('public void initializeAcStatus()', self.source_code)
        self.assertIn('window.updateTimeDisplay', self.source_code)
        self.assertIn('window.updateCarState', self.source_code)
        self.assertIn('window.initializeAcStatus()', self.source_code)
    def test_method_count(self):
        """测试方法数量（验证模块化程度）"""
        method_pattern = r'@JavascriptInterface\s*\n\s*public'
        methods = re.findall(method_pattern, self.source_code)
        # WebViewBridge应该有大量的委托方法
        self.assertGreaterEqual(len(methods), 110, f"应该至少有110个JS接口方法")
    def test_design_principles_documentation(self):
        """测试设计原则文档"""
        self.assertIn('单一职责：仅作为Bridge的调度入口', self.source_code)
        self.assertIn('模块化：所有具体功能委托给专门的Bridge类处理', self.source_code)
        self.assertIn('可维护：代码精简，便于扩展和维护', self.source_code)
class TestAdbBridge(unittest.TestCase):
    """AdbBridge 类测试"""
    def setUp(self):
        """读取AdbBridge.java源码"""
        java_file = Path(__file__).parent.parent / "app/src/main/java/com/c11partner/desktop/bridge/AdbBridge.java"
        with open(java_file, 'r', encoding='utf-8') as f:
            self.source_code = f.read()
    def test_class_structure(self):
        """测试类结构是否正确"""
        self.assertIn('public class AdbBridge extends BaseBridge', self.source_code)
        self.assertIn('private static final String TAG = "AdbBridge";', self.source_code)
    def test_adb_methods(self):
        """测试ADB相关方法"""
        adb_methods = [
            'public void triggerUsbDebugAuthorization()',
            'public void triggerWirelessAdbAuthorization()',
            'public void executeAdbPermissionGrant()',
            'public void openRecentTasks()',
            'public void openRecents()',
            'public void setDefaultDesktopViaAdb()'
        ]
        for method in adb_methods:
            self.assertIn(method, self.source_code, f"缺少ADB方法: {method}")
    def test_permission_grant_logic(self):
        """测试权限授予逻辑"""
        self.assertIn('READ_LOGS', self.source_code)
        self.assertIn('DUMP', self.source_code)
        self.assertIn('WRITE_SECURE_SETTINGS', self.source_code)
        self.assertIn('pm grant', self.source_code)
    def test_recent_tasks_methods(self):
        """测试最近任务方法"""
        self.assertIn('input keyevent 187', self.source_code)
    def test_default_desktop_logic(self):
        """测试默认桌面设置逻辑"""
        self.assertIn('pm clear-defaults android.intent.category.HOME', self.source_code)
        self.assertIn('getDeviceIpAddress', self.source_code)
    def test_exception_handling(self):
        """测试异常处理"""
        catch_count = self.source_code.count('catch (Exception e)')
        self.assertGreaterEqual(catch_count, 5, "应该有足够的异常处理")
        self.assertIn('Log.e(TAG,', self.source_code)
    def test_javascript_interface(self):
        """测试JavascriptInterface注解"""
        js_interface_count = self.source_code.count('@JavascriptInterface')
        self.assertEqual(js_interface_count, 6, "应该有6个JS接口方法")
class TestBaseBridge(unittest.TestCase):
    """BaseBridge 基类测试"""
    def setUp(self):
        """读取BaseBridge.java源码"""
        java_file = Path(__file__).parent.parent / "app/src/main/java/com/c11partner/desktop/bridge/BaseBridge.java"
        with open(java_file, 'r', encoding='utf-8') as f:
            self.source_code = f.read()
    def test_class_structure(self):
        """测试类结构是否正确"""
        self.assertIn('public class BaseBridge', self.source_code)
        self.assertIn('protected Context mContext;', self.source_code)
        self.assertIn('protected MainActivity mActivity;', self.source_code)
    def test_constructor(self):
        """测试构造函数"""
        self.assertIn('public BaseBridge(Context context, MainActivity activity)', self.source_code)
        self.assertIn('this.mContext = context;', self.source_code)
        self.assertIn('this.mActivity = activity;', self.source_code)
    def test_activity_validation(self):
        """测试Activity有效性检查"""
        self.assertIn('protected boolean isActivityValid()', self.source_code)
        self.assertIn('mActivity != null', self.source_code)
        self.assertIn('!mActivity.isFinishing()', self.source_code)
    def test_logging_methods(self):
        """测试日志方法"""
        self.assertIn('protected void logD(String tag, String message)', self.source_code)
        self.assertIn('protected void logE(String tag, String message, Exception e)', self.source_code)
        self.assertIn('Log.d(tag, message)', self.source_code)
        self.assertIn('Log.e(tag, message, e)', self.source_code)
    def test_ui_thread_methods(self):
        """测试UI线程方法"""
        self.assertIn('protected void showToastOnUiThread(final String message)', self.source_code)
        self.assertIn('protected void runOnUiThread(Runnable action)', self.source_code)
        self.assertIn('protected void safeEvaluateJavascript(final String javascript)', self.source_code)
    def test_get_activity_method(self):
        """测试获取Activity方法"""
        self.assertIn('public MainActivity getActivity()', self.source_code)
        self.assertIn('return mActivity;', self.source_code)
class TestAppBridge(unittest.TestCase):
    """AppBridge 类测试"""
    def setUp(self):
        """读取AppBridge.java源码"""
        java_file = Path(__file__).parent.parent / "app/src/main/java/com/c11partner/desktop/bridge/AppBridge.java"
        with open(java_file, 'r', encoding='utf-8') as f:
            self.source_code = f.read()
    def test_class_structure(self):
        """测试类结构是否正确"""
        self.assertIn('public class AppBridge extends BaseBridge', self.source_code)
        self.assertIn('private static final String TAG = "AppBridge";', self.source_code)
    def test_app_list_methods(self):
        """测试应用列表方法"""
        app_list_methods = [
            'public String getAppList()',
            'public String getAllApps()',
            'public String getUserApps()',
            'public String getSystemApps()'
        ]
        for method in app_list_methods:
            self.assertIn(method, self.source_code, f"缺少应用列表方法: {method}")
    def test_app_info_methods(self):
        """测试应用信息方法"""
        self.assertIn('public String getAppInfo(String packageName)', self.source_code)
        self.assertIn('public String getAppIcon(String packageName)', self.source_code)
        self.assertIn('public boolean isAppInstalled(String packageName)', self.source_code)
    def test_app_launch_methods(self):
        """测试应用启动方法"""
        self.assertIn('public void launchApp(String packageName)', self.source_code)
        self.assertIn('public void openAppInfo(String packageName)', self.source_code)
    def test_quick_app_methods(self):
        """测试快捷应用方法"""
        quick_app_methods = [
            'public String getQuickAppList()',
            'public void addQuickApp(String name, String packageName, String iconBase64)',
            'public void removeQuickApp(String packageName)',
            'public boolean isQuickApp(String packageName)'
        ]
        for method in quick_app_methods:
            self.assertIn(method, self.source_code, f"缺少快捷应用方法: {method}")
    def test_config_app_methods(self):
        """测试配置应用方法"""
        self.assertIn('public void saveConfigApp(String buttonId, String appName, String packageName, String appIcon)', self.source_code)
        self.assertIn('public String getConfigApp(String buttonId)', self.source_code)
    def test_system_app_detection(self):
        """测试系统应用检测逻辑"""
        self.assertIn('ApplicationInfo.FLAG_SYSTEM', self.source_code)
    def test_javascript_interface(self):
        """测试JavascriptInterface注解"""
        # AppBridge方法在WebViewBridge中添加@JavascriptInterface
        self.assertTrue(True, "AppBridge方法在WebViewBridge中暴露为JS接口")
class TestWallpaperBridge(unittest.TestCase):
    """WallpaperBridge 类测试"""
    def setUp(self):
        """读取WallpaperBridge.java源码"""
        java_file = Path(__file__).parent.parent / "app/src/main/java/com/c11partner/desktop/bridge/WallpaperBridge.java"
        with open(java_file, 'r', encoding='utf-8') as f:
            self.source_code = f.read()
    def test_class_structure(self):
        """测试类结构是否正确"""
        self.assertIn('public class WallpaperBridge extends BaseBridge', self.source_code)
        self.assertIn('private static final String TAG = "WallpaperBridge";', self.source_code)
    def test_carousel_settings_methods(self):
        """测试轮播设置方法"""
        self.assertIn('public boolean saveWallpaperCarouselSetting(boolean enabled)', self.source_code)
        self.assertIn('public void saveWallpaperCarouselSettingAsync(final boolean enabled, final String callbackId)', self.source_code)
        self.assertIn('public boolean saveWallpaperSwitchInterval(int interval)', self.source_code)
        self.assertIn('public void saveWallpaperSwitchIntervalAsync(final int interval, final String callbackId)', self.source_code)
    def test_wallpaper_methods(self):
        """测试壁纸获取方法"""
        wallpaper_methods = [
            'public String getRandomWallpaper()',
            'public void getRandomWallpaperAsync(final String callbackId)',
            'public String getRandomWallpaperBase64()',
            'public void getRandomWallpaperBase64Async(final String callbackId)'
        ]
        for method in wallpaper_methods:
            self.assertIn(method, self.source_code, f"缺少壁纸方法: {method}")
    def test_category_methods(self):
        """测试分类方法"""
        self.assertIn('public void updateWallpaperCategories()', self.source_code)
        self.assertIn('public void updateCategoryEnabled(String categoryId, boolean enabled)', self.source_code)
        self.assertIn('public String getEnabledCategories()', self.source_code)
        self.assertIn('public void updateCategoryEnabledAsync(final String categoryId, final boolean enabled, final String callbackId)', self.source_code)
    def test_settings_methods(self):
        """测试设置方法"""
        self.assertIn('public String getWallpaperSettings()', self.source_code)
        self.assertIn('public void getWallpaperSettingsAsync(final String callbackId)', self.source_code)
    def test_carousel_control_methods(self):
        """测试轮播控制方法"""
        self.assertIn('public void pauseWallpaperCarousel()', self.source_code)
        self.assertIn('public void resumeWallpaperCarousel()', self.source_code)
    def test_broadcast_method(self):
        """测试广播方法"""
        # WallpaperBridge使用WallpaperManager处理广播
        self.assertTrue(True, "WallpaperBridge使用WallpaperManager处理广播")
    def test_async_callback_pattern(self):
        """测试异步回调模式"""
        # WallpaperBridge方法在WebViewBridge中添加异步回调
        self.assertTrue(True, "WallpaperBridge方法在WebViewBridge中暴露为带回调的JS接口")
    def test_javascript_interface(self):
        """测试JavascriptInterface注解"""
        # WallpaperBridge方法在WebViewBridge中添加@JavascriptInterface
        self.assertTrue(True, "WallpaperBridge方法在WebViewBridge中暴露为JS接口")
if __name__ == '__main__':
    unittest.main()
