package com.c11partner.desktop.bridge;
import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import com.c11partner.desktop.MainActivity;
import com.c11partner.desktop.database.AppDatabaseHelper;
import com.c11partner.desktop.database.ComponentConfigDatabaseHelper;
import com.c11partner.desktop.database.ConfigAppDatabaseHelper;
import com.c11partner.desktop.database.QuickAppDatabaseHelper;
import com.c11partner.desktop.database.WallpaperCategoryDatabaseHelper;
import com.c11partner.desktop.database.WallpaperSettingsDatabaseHelper;
import com.c11partner.desktop.service.MediaSessionService;
import org.json.JSONObject;
import java.util.Map;
/**
 * WebView与原生代码交互的桥梁类 - 主入口
 * 
 * 设计原则：
 * 1. 单一职责：仅作为Bridge的调度入口，不实现具体业务逻辑
 * 2. 模块化：所有具体功能委托给专门的Bridge类处理
 * 3. 可维护：代码精简，便于扩展和维护
 * 
 * 功能模块：
 * - AppBridge: 应用管理相关
 * - WallpaperBridge: 壁纸管理相关
 * - CarControlBridge: 车控功能相关
 * - MusicBridge: 音乐功能相关
 * - SystemBridge: 系统设置相关
 * - AdbBridge: ADB授权相关
 */
public class WebViewBridge {
    private static final String TAG = "WebViewBridge";
    private Context mContext;
    private MainActivity mActivity;
    // 数据库帮助类（仅保留核心引用）
    private AppDatabaseHelper dbHelper;
    private QuickAppDatabaseHelper quickAppDbHelper;
    private WallpaperCategoryDatabaseHelper wallpaperDbHelper;
    private WallpaperSettingsDatabaseHelper wallpaperSettingsDbHelper;
    private ConfigAppDatabaseHelper configAppDbHelper;
    private ComponentConfigDatabaseHelper componentConfigDbHelper;
    // 模块化Bridge - 所有具体功能委托给这些Bridge处理
    private CarControlBridge mCarControlBridge;
    private WallpaperBridge mWallpaperBridge;
    private AppBridge mAppBridge;
    private MusicBridge mMusicBridge;
    private SystemBridge mSystemBridge;
    private AdbBridge mAdbBridge;
    /**
     * 构造函数 - 初始化所有Bridge模块
     *
     * @param activity MainActivity实例
     * @param context  应用上下文
     */
    public WebViewBridge(MainActivity activity, Context context) {
        this.mActivity = activity;
        this.mContext = context;
        
        // 初始化数据库帮助类
        this.dbHelper = AppDatabaseHelper.getInstance(context);
        this.quickAppDbHelper = QuickAppDatabaseHelper.getInstance(context);
        this.wallpaperDbHelper = WallpaperCategoryDatabaseHelper.getInstance(context);
        this.wallpaperSettingsDbHelper = WallpaperSettingsDatabaseHelper.getInstance(context);
        this.configAppDbHelper = ConfigAppDatabaseHelper.getInstance(context);
        this.componentConfigDbHelper = ComponentConfigDatabaseHelper.getInstance(context);
        
        // 初始化所有功能Bridge模块
        this.mCarControlBridge = new CarControlBridge(context, activity);
        this.mWallpaperBridge = new WallpaperBridge(context, activity);
        this.mAppBridge = new AppBridge(context, activity);
        this.mMusicBridge = new MusicBridge(context, activity);
        this.mSystemBridge = new SystemBridge(context, activity);
        this.mAdbBridge = new AdbBridge(context, activity);
        
        Log.d(TAG, "WebViewBridge初始化完成，所有功能模块已加载");
    }
    /**
     * 设置媒体会话服务 - 传递给MusicBridge
     *
     * @param service 媒体会话服务实例
     * @param isBound 是否已绑定
     */
    public void setMediaSessionService(MediaSessionService service, boolean isBound) {
        if (mMusicBridge != null) {
            mMusicBridge.setMediaSessionService(service, isBound);
        }
    }
    /**
     * 通知前端更新壁纸 - 传递给WallpaperBridge
     */
    public void notifyWallpaperUpdate() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mActivity.webView != null) {
                    String javascript = "javascript:window.handleWallpaperUpdateNotification()";
                    mActivity.webView.loadUrl(javascript);
                }
            }
        });
    }
    // ==================== AppBridge 委托方法 ====================
    @JavascriptInterface
    public String getAppList() { return mAppBridge.getAppList(); }
    
    @JavascriptInterface
    public String getAllApps() { return mAppBridge.getAllApps(); }
    
    @JavascriptInterface
    public String getUserApps() { return mAppBridge.getUserApps(); }
    
    @JavascriptInterface
    public String getSystemApps() { return mAppBridge.getSystemApps(); }
    
    @JavascriptInterface
    public String getAppInfo(String packageName) { return mAppBridge.getAppInfo(packageName); }
    
    @JavascriptInterface
    public String getAppIcon(String packageName) { return mAppBridge.getAppIcon(packageName); }
    
    @JavascriptInterface
    public boolean isAppInstalled(String packageName) { return mAppBridge.isAppInstalled(packageName); }
    
    @JavascriptInterface
    public void launchApp(String packageName) { mAppBridge.launchApp(packageName); }
    
    @JavascriptInterface
    public void openAppInfo(String packageName) { mAppBridge.openAppInfo(packageName); }
    
    @JavascriptInterface
    public String getQuickAppList() { return mAppBridge.getQuickAppList(); }
    
    @JavascriptInterface
    public void addQuickApp(String name, String packageName, String iconBase64) { 
        mAppBridge.addQuickApp(name, packageName, iconBase64); 
    }
    
    @JavascriptInterface
    public void removeQuickApp(String packageName) { mAppBridge.removeQuickApp(packageName); }
    
    @JavascriptInterface
    public boolean isQuickApp(String packageName) { return mAppBridge.isQuickApp(packageName); }
    
    @JavascriptInterface
    public void saveConfigApp(String buttonId, String appName, String packageName, String appIcon) {
        mAppBridge.saveConfigApp(buttonId, appName, packageName, appIcon);
    }
    
    @JavascriptInterface
    public String getConfigApp(String buttonId) { return mAppBridge.getConfigApp(buttonId); }
    // ==================== WallpaperBridge 委托方法 ====================
    @JavascriptInterface
    public boolean saveWallpaperCarouselSetting(boolean enabled) {
        return mWallpaperBridge.saveWallpaperCarouselSetting(enabled);
    }
    
    @JavascriptInterface
    public void saveWallpaperCarouselSettingAsync(final boolean enabled, final String callbackId) {
        mWallpaperBridge.saveWallpaperCarouselSettingAsync(enabled, callbackId);
    }
    
    @JavascriptInterface
    public boolean saveWallpaperSwitchInterval(int interval) {
        return mWallpaperBridge.saveWallpaperSwitchInterval(interval);
    }
    
    @JavascriptInterface
    public void saveWallpaperSwitchIntervalAsync(final int interval, final String callbackId) {
        mWallpaperBridge.saveWallpaperSwitchIntervalAsync(interval, callbackId);
    }
    
    @JavascriptInterface
    public void sendWallpaperSettingsChangedBroadcast() {
        mWallpaperBridge.sendWallpaperSettingsChangedBroadcast();
    }
    
    @JavascriptInterface
    public String getRandomWallpaper() { return mWallpaperBridge.getRandomWallpaper(); }
    
    @JavascriptInterface
    public void getRandomWallpaperAsync(final String callbackId) {
        mWallpaperBridge.getRandomWallpaperAsync(callbackId);
    }
    
    @JavascriptInterface
    public String getRandomWallpaperBase64() {
        return mWallpaperBridge.getRandomWallpaperBase64();
    }
    
    @JavascriptInterface
    public void getRandomWallpaperBase64Async(final String callbackId) {
        mWallpaperBridge.getRandomWallpaperBase64Async(callbackId);
    }
    
    @JavascriptInterface
    public void updateWallpaperCategories() { mWallpaperBridge.updateWallpaperCategories(); }
    
    @JavascriptInterface
    public void updateCategoryEnabled(String categoryId, boolean enabled) {
        mWallpaperBridge.updateCategoryEnabled(categoryId, enabled);
    }
    
    @JavascriptInterface
    public String getEnabledCategories() { return mWallpaperBridge.getEnabledCategories(); }
    
    @JavascriptInterface
    public void updateCategoryEnabledAsync(final String categoryId, final boolean enabled, final String callbackId) {
        mWallpaperBridge.updateCategoryEnabledAsync(categoryId, enabled, callbackId);
    }
    
    @JavascriptInterface
    public String getWallpaperSettings() { return mWallpaperBridge.getWallpaperSettings(); }
    
    @JavascriptInterface
    public void getWallpaperSettingsAsync(final String callbackId) {
        mWallpaperBridge.getWallpaperSettingsAsync(callbackId);
    }
    
    @JavascriptInterface
    public void pauseWallpaperCarousel() { mWallpaperBridge.pauseWallpaperCarousel(); }
    
    @JavascriptInterface
    public void resumeWallpaperCarousel() { mWallpaperBridge.resumeWallpaperCarousel(); }
    
    @JavascriptInterface
    public boolean deleteCurrentWallpaper() { return mWallpaperBridge.deleteCurrentWallpaper(); }
    // ==================== MusicBridge 委托方法 ====================
    @JavascriptInterface
    public void startMusicVisualizer() { mMusicBridge.startMusicVisualizer(); }
    
    @JavascriptInterface
    public void stopMusicVisualizer() { mMusicBridge.stopMusicVisualizer(); }
    
    @JavascriptInterface
    public boolean isMusicPlaying() { return mMusicBridge.isMusicPlaying(); }
    
    @JavascriptInterface
    public String getCurrentMusicName() { return mMusicBridge.getCurrentMusicName(); }
    
    @JavascriptInterface
    public String getMusicProgressInfo() { return mMusicBridge.getMusicProgressInfo(); }
    
    @JavascriptInterface
    public String getSystemMusicInfo() { return mMusicBridge.getSystemMusicInfo(); }
    
    @JavascriptInterface
    public void playPauseMusic() { mMusicBridge.playPauseMusic(); }
    
    @JavascriptInterface
    public void nextMusic() { mMusicBridge.nextMusic(); }
    
    @JavascriptInterface
    public void prevMusic() { mMusicBridge.prevMusic(); }
    
    @JavascriptInterface
    public void playPause() { mMusicBridge.playPause(); }
    
    @JavascriptInterface
    public void playNext() { mMusicBridge.playNext(); }
    
    @JavascriptInterface
    public void playPrevious() { mMusicBridge.playPrevious(); }
    
    @JavascriptInterface
    public boolean isNotificationListenerEnabled() {
        return mMusicBridge.isNotificationListenerEnabled();
    }
    
    @JavascriptInterface
    public void openNotificationListenerSettings() {
        mMusicBridge.openNotificationListenerSettings();
    }
    // ==================== SystemBridge 委托方法 ====================
    @JavascriptInterface
    public boolean isWifiConnected() { return mSystemBridge.isWifiConnected(); }
    
    @JavascriptInterface
    public boolean isBluetoothConnected() { return mSystemBridge.isBluetoothConnected(); }
    
    @JavascriptInterface
    public boolean saveSystemLauncherSetting(boolean enabled) {
        return mSystemBridge.saveSystemLauncherSetting(enabled);
    }
    
    @JavascriptInterface
    public void saveSystemLauncherSettingAsync(final boolean enabled, final String callbackId) {
        mSystemBridge.saveSystemLauncherSettingAsync(enabled, callbackId);
    }
    
    @JavascriptInterface
    public boolean saveBootGreetingSetting(boolean enabled) {
        return mSystemBridge.saveBootGreetingSetting(enabled);
    }
    
    @JavascriptInterface
    public boolean saveRandomModeSetting(boolean enabled) {
        return mSystemBridge.saveRandomModeSetting(enabled);
    }
    
    @JavascriptInterface
    public boolean saveSpecifiedModeSetting(boolean enabled) {
        return mSystemBridge.saveSpecifiedModeSetting(enabled);
    }
    
    @JavascriptInterface
    public String getLunarCalendar() { return mSystemBridge.getLunarCalendar(); }
    // ==================== AdbBridge 委托方法 ====================
    @JavascriptInterface
    public void triggerUsbDebugAuthorization() { mAdbBridge.triggerUsbDebugAuthorization(); }
    
    @JavascriptInterface
    public void triggerWirelessAdbAuthorization() { mAdbBridge.triggerWirelessAdbAuthorization(); }
    
    @JavascriptInterface
    public void executeAdbPermissionGrant() { mAdbBridge.executeAdbPermissionGrant(); }
    
    @JavascriptInterface
    public void openRecentTasks() {
        // 多任务功能也委托给AdbBridge
        new Thread(() -> {
            try {
                com.c11partner.desktop.adb.AdbCommandProcessor processor = 
                    new com.c11partner.desktop.adb.AdbCommandProcessor(mContext);
                processor.executeCommand("input keyevent 187");
            } catch (Exception e) {
                Log.e(TAG, "打开多任务时出错", e);
            }
        }).start();
    }
    
    @JavascriptInterface
    public void openRecents() { openRecentTasks(); }
    
    @JavascriptInterface
    public void setDefaultDesktopViaAdb() {
        Log.d(TAG, "setDefaultDesktopViaAdb方法被调用");
        new Thread(() -> {
            try {
                com.c11partner.desktop.adb.AdbManager.setAppContext(mContext);
                com.c11partner.desktop.adb.AdbManager.initCrypto();
                
                String deviceIp = getDeviceIpAddress();
                if (deviceIp == null) return;
                
                int[] commonPorts = {5555, 5554, 5556};
                for (int port : commonPorts) {
                    if (com.c11partner.desktop.adb.AdbManager.connectAndExecute(
                            deviceIp, port, "pm clear-defaults android.intent.category.HOME")) {
                        break;
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "设置默认桌面时出错", e);
            }
        }).start();
    }
    // ==================== 组件配置方法（保留在主Bridge） ====================
    @JavascriptInterface
    public boolean saveComponentConfig(String componentName, boolean isEnabled) {
        try {
            if (componentConfigDbHelper != null) {
                long result = componentConfigDbHelper.saveOrUpdateComponentConfig(componentName, isEnabled);
                return result != -1;
            }
        } catch (Exception e) {
            Log.e(TAG, "保存组件配置时出错", e);
        }
        return false;
    }
    
    @JavascriptInterface
    public boolean isComponentEnabled(String componentName) {
        try {
            if (componentConfigDbHelper != null) {
                return componentConfigDbHelper.isComponentEnabled(componentName);
            }
        } catch (Exception e) {
            Log.e(TAG, "获取组件配置时出错", e);
        }
        return true;
    }
    
    @JavascriptInterface
    public String getAllComponentConfigs() {
        try {
            if (componentConfigDbHelper != null) {
                Map<String, Boolean> configs = componentConfigDbHelper.getAllComponentConfigs();
                JSONObject configObj = new JSONObject();
                for (Map.Entry<String, Boolean> entry : configs.entrySet()) {
                    configObj.put(entry.getKey(), entry.getValue());
                }
                return configObj.toString();
            }
        } catch (Exception e) {
            Log.e(TAG, "获取所有组件配置时出错", e);
        }
        return "{}";
    }
    // ==================== 私有辅助方法 ====================
    // ==================== MainActivity 调用的UI更新方法 ====================
    
    /**
     * 更新时间显示（MainActivity调用）
     * 
     * @param time 时间字符串
     * @param date 日期字符串
     * @param lunarDate 农历日期
     */
    public void updateTimeDisplay(final String time, final String date, final String lunarDate) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mActivity.webView != null) {
                    String javascript = String.format(
                        "javascript:window.updateTimeDisplay(%s, %s, %s)",
                        org.json.JSONObject.quote(time),
                        org.json.JSONObject.quote(date),
                        org.json.JSONObject.quote(lunarDate)
                    );
                    mActivity.webView.loadUrl(javascript);
                }
            }
        });
    }
    
    /**
     * 更新车辆状态显示（MainActivity调用）
     * 
     * @param state 车辆状态对象
     */
    public void updatePresentationCarState(final com.c11partner.desktop.LeapMotorCarState state) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mActivity.webView != null && state != null) {
                    try {
                        org.json.JSONObject stateJson = new org.json.JSONObject();
                        stateJson.put("speed", state.speed);
                        stateJson.put("batteryLevel", state.batteryLevel);
                        stateJson.put("range", state.range);
                        stateJson.put("gear", state.gear);
                        stateJson.put("doorStatus", state.doorStatus);
                        stateJson.put("lightStatus", state.lightStatus);
                        stateJson.put("acStatus", state.acStatus);
                        stateJson.put("temperature", state.temperature);
                        
                        String javascript = "javascript:window.updateCarState(" + stateJson.toString() + ")";
                        mActivity.webView.loadUrl(javascript);
                    } catch (Exception e) {
                        Log.e(TAG, "更新车辆状态失败", e);
                    }
                }
            }
        });
    }
    
    /**
     * 初始化空调状态（MainActivity调用）
     */
    public void initializeAcStatus() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mActivity.webView != null) {
                    // 调用前端初始化空调状态
                    mActivity.webView.loadUrl("javascript:window.initializeAcStatus()");
                }
            }
        });
    }
    
    // ==================== 私有辅助方法 ====================
    
    private String getDeviceIpAddress() {
        try {
            java.net.NetworkInterface networkInterface = java.net.NetworkInterface.getByName("wlan0");
            if (networkInterface == null) networkInterface = java.net.NetworkInterface.getByName("eth0");
            
            if (networkInterface != null && networkInterface.isUp()) {
                java.util.Enumeration<java.net.InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    java.net.InetAddress address = addresses.nextElement();
                    if (!address.isLoopbackAddress() && address instanceof java.net.Inet4Address) {
                        return address.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "获取设备IP地址失败", e);
        }
        return null;
    }
}
