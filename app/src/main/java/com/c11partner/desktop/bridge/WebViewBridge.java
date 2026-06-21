package com.c11partner.desktop.bridge;

import android.content.Context;
import android.webkit.JavascriptInterface;
import com.c11partner.desktop.MainActivity;

import com.c11partner.desktop.LeapMotorCarState;
import com.c11partner.desktop.service.MediaSessionService;

import org.json.JSONObject;

/**
 * WebView与原生代码交互的桥梁类 - 主入口
 * 
 * 设计原则：
 * 1. 单一职责：仅作为Bridge的调度入口，不实现具体业务逻辑
 * 2. 模块化：所有具体功能委托给专门的Bridge类处理
 * 3. 可维护：代码精简，便于扩展和维护
 * 
 * 功能模块：
 * - CarControlBridge: 车控功能相关（50+方法）
 * - AppBridge: 应用管理相关
 * - WallpaperBridge: 壁纸管理相关
 * - MusicBridge: 音乐功能相关
 * - SystemBridge: 系统设置相关
 * - AdbBridge: ADB授权相关
 */
public class WebViewBridge extends BaseBridge {
    private static final String TAG = "WebViewBridge";

    // 模块化Bridge - 所有具体功能委托给这些Bridge处理
    // 设计原则：WebViewBridge仅作为调度入口，不持有具体业务数据
    // 各子Bridge内部自行管理所需的数据库和资源依赖
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
        super(context, activity);
        
        // 初始化所有功能Bridge模块
        // 各子Bridge内部自行管理数据库依赖（单例模式）
        this.mCarControlBridge = new CarControlBridge(context, activity);
        this.mWallpaperBridge = new WallpaperBridge(context, activity);
        this.mAppBridge = new AppBridge(context, activity);
        this.mMusicBridge = new MusicBridge(context, activity);
        this.mSystemBridge = new SystemBridge(context, activity);
        this.mAdbBridge = new AdbBridge(context, activity);
        
        logD(TAG, "WebViewBridge初始化完成，所有功能模块已加载");
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
        safeEvaluateJavascript("javascript:window.handleWallpaperUpdateNotification()");
    }

    // ==================== CarControlBridge 委托方法 ====================
    
    // 360全景相关
    @JavascriptInterface
    public boolean startCamera360() { return mCarControlBridge.startCamera360(); }
    
    @JavascriptInterface
    public boolean setCameraOverspeedLimit(boolean enabled) { 
        return mCarControlBridge.setCameraOverspeedLimit(enabled); 
    }
    
    @JavascriptInterface
    public boolean isCameraOverspeedLimitEnabled() { 
        return mCarControlBridge.isCameraOverspeedLimitEnabled(); 
    }
    
    // 灯光控制
    @JavascriptInterface
    public boolean setLowBeamLight(boolean on) { 
        return mCarControlBridge.setLowBeamLight(on); 
    }
    
    @JavascriptInterface
    public boolean setRearFogLight(boolean on) { 
        return mCarControlBridge.setRearFogLight(on); 
    }
    
    @JavascriptInterface
    public boolean setPositionLight(boolean on) { 
        return mCarControlBridge.setPositionLight(on); 
    }
    
    @JavascriptInterface
    public boolean setPedestrianAlert(boolean on) { 
        return mCarControlBridge.setPedestrianAlert(on); 
    }
    
    // 驾驶/场景模式
    @JavascriptInterface
    public boolean setDriveMode(int mode) { 
        return mCarControlBridge.setDriveMode(mode); 
    }
    
    @JavascriptInterface
    public boolean setGuardMode(boolean on) { 
        return mCarControlBridge.setGuardMode(on); 
    }
    
    @JavascriptInterface
    public boolean setRestMode(boolean on) { 
        return mCarControlBridge.setRestMode(on); 
    }
    
    @JavascriptInterface
    public boolean setCampingMode(boolean on) { 
        return mCarControlBridge.setCampingMode(on); 
    }
    
    @JavascriptInterface
    public boolean setPowerSaveMode(boolean on) { 
        return mCarControlBridge.setPowerSaveMode(on); 
    }
    
    @JavascriptInterface
    public boolean setSentinelMode(boolean on) { 
        return mCarControlBridge.setSentinelMode(on); 
    }
    
    // 空调控制
    @JavascriptInterface
    public boolean setMaxCooling(boolean on) { 
        return mCarControlBridge.setMaxCooling(on); 
    }
    
    @JavascriptInterface
    public boolean setAcEnabled(boolean enabled) { 
        return mCarControlBridge.setAcEnabled(enabled); 
    }
    
    @JavascriptInterface
    public boolean isAcEnabled() { 
        return mCarControlBridge.isAcEnabled(); 
    }
    
    @JavascriptInterface
    public boolean setWindLevel(int level) { 
        return mCarControlBridge.setWindLevel(level); 
    }
    
    @JavascriptInterface
    public int getWindLevel() { 
        return mCarControlBridge.getWindLevel(); 
    }
    
    @JavascriptInterface
    public boolean setDriverTemp(int temp) { 
        return mCarControlBridge.setDriverTemp(temp); 
    }
    
    @JavascriptInterface
    public int getDriverTemp() { 
        return mCarControlBridge.getDriverTemp(); 
    }
    
    @JavascriptInterface
    public boolean setPassengerTemp(int temp) { 
        return mCarControlBridge.setPassengerTemp(temp); 
    }
    
    @JavascriptInterface
    public int getPassengerTemp() { 
        return mCarControlBridge.getPassengerTemp(); 
    }
    
    // 音量控制
    @JavascriptInterface
    public boolean setCallVolume(int volume) { 
        return mCarControlBridge.setCallVolume(volume); 
    }
    
    @JavascriptInterface
    public int getCallVolume() { 
        return mCarControlBridge.getCallVolume(); 
    }
    
    @JavascriptInterface
    public boolean setNaviVolume(int volume) { 
        return mCarControlBridge.setNaviVolume(volume); 
    }
    
    @JavascriptInterface
    public int getNaviVolume() { 
        return mCarControlBridge.getNaviVolume(); 
    }
    
    @JavascriptInterface
    public boolean setMusicVolume(int volume) { 
        return mCarControlBridge.setMusicVolume(volume); 
    }
    
    @JavascriptInterface
    public int getMusicVolume() { 
        return mCarControlBridge.getMusicVolume(); 
    }
    
    // 氛围灯控制
    @JavascriptInterface
    public boolean setAmbientLightEnabled(boolean enabled) { 
        return mCarControlBridge.setAmbientLightEnabled(enabled); 
    }
    
    @JavascriptInterface
    public boolean isAmbientLightEnabled() { 
        return mCarControlBridge.isAmbientLightEnabled(); 
    }
    
    @JavascriptInterface
    public boolean setAmbientLightColor(int color) { 
        return mCarControlBridge.setAmbientLightColor(color); 
    }
    
    @JavascriptInterface
    public int getAmbientLightColor() { 
        return mCarControlBridge.getAmbientLightColor(); 
    }
    
    // 系统设置
    @JavascriptInterface
    public boolean setNightMode(boolean on) { 
        return mCarControlBridge.setNightMode(on); 
    }
    
    @JavascriptInterface
    public boolean setWifiEnabled(boolean enabled) { 
        return mCarControlBridge.setWifiEnabled(enabled); 
    }
    
    @JavascriptInterface
    public boolean setBluetoothEnabled(boolean enabled) { 
        return mCarControlBridge.setBluetoothEnabled(enabled); 
    }
    
    @JavascriptInterface
    public boolean setVideoWhileDriving(boolean enabled) { 
        return mCarControlBridge.setVideoWhileDriving(enabled); 
    }
    
    @JavascriptInterface
    public boolean isVideoWhileDrivingEnabled() { 
        return mCarControlBridge.isVideoWhileDrivingEnabled(); 
    }
    
    // 副屏控制
    @JavascriptInterface
    public boolean setSecondaryScreenEnabled(boolean enabled) { 
        return mCarControlBridge.setSecondaryScreenEnabled(enabled); 
    }
    
    @JavascriptInterface
    public boolean isSecondaryScreenEnabled() { 
        return mCarControlBridge.isSecondaryScreenEnabled(); 
    }
    
    // 语音控制
    @JavascriptInterface
    public boolean setSpeechEnabled(boolean enabled) { 
        return mCarControlBridge.setSpeechEnabled(enabled); 
    }
    
    @JavascriptInterface
    public boolean isSpeechEnabled() { 
        return mCarControlBridge.isSpeechEnabled(); 
    }
    
    @JavascriptInterface
    public boolean sendVoiceCommand(String command) { 
        return mCarControlBridge.sendVoiceCommand(command); 
    }
    
    // 方控按键
    @JavascriptInterface
    public boolean sendPrevTrack() { 
        return mCarControlBridge.sendPrevTrack(); 
    }
    
    @JavascriptInterface
    public boolean sendNextTrack() { 
        return mCarControlBridge.sendNextTrack(); 
    }
    
    // 状态读取
    @JavascriptInterface
    public boolean isVehicleLocked() { 
        return mCarControlBridge.isVehicleLocked(); 
    }
    
    @JavascriptInterface
    public boolean isScreenOn() { 
        return mCarControlBridge.isScreenOn(); 
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
    public String getCurrentMusicArtist() { return mMusicBridge.getCurrentMusicArtist(); }
    
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
    
    // 音乐音量控制
    @JavascriptInterface
    public void setMusicVolume(int volume) { mMusicBridge.setMusicVolume(volume); }
    
    @JavascriptInterface
    public int getMusicVolume() { return mMusicBridge.getMusicVolume(); }
    
    @JavascriptInterface
    public void volumeUp() { mMusicBridge.volumeUp(); }
    
    @JavascriptInterface
    public void volumeDown() { mMusicBridge.volumeDown(); }
    
    // 播放进度控制
    @JavascriptInterface
    public void seekTo(long position) { mMusicBridge.seekTo(position); }
    
    @JavascriptInterface
    public void setPlaybackSpeed(float speed) { mMusicBridge.setPlaybackSpeed(speed); }
    
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
    
    // 屏幕亮度控制
    @JavascriptInterface
    public boolean setScreenBrightness(int brightness) { return mSystemBridge.setScreenBrightness(brightness); }
    
    @JavascriptInterface
    public int getScreenBrightness() { return mSystemBridge.getScreenBrightness(); }
    
    @JavascriptInterface
    public boolean setAutoBrightness(boolean enabled) { return mSystemBridge.setAutoBrightness(enabled); }
    
    @JavascriptInterface
    public boolean isAutoBrightnessEnabled() { return mSystemBridge.isAutoBrightnessEnabled(); }
    
    // 屏幕超时设置
    @JavascriptInterface
    public boolean setScreenTimeout(int seconds) { return mSystemBridge.setScreenTimeout(seconds); }
    
    @JavascriptInterface
    public int getScreenTimeout() { return mSystemBridge.getScreenTimeout(); }

    // ==================== AdbBridge 委托方法 ====================
    @JavascriptInterface
    public void triggerUsbDebugAuthorization() { mAdbBridge.triggerUsbDebugAuthorization(); }
    
    @JavascriptInterface
    public void triggerWirelessAdbAuthorization() { mAdbBridge.triggerWirelessAdbAuthorization(); }
    
    @JavascriptInterface
    public void executeAdbPermissionGrant() { mAdbBridge.executeAdbPermissionGrant(); }
    
    @JavascriptInterface
    public void openRecentTasks() { mAdbBridge.openRecentTasks(); }
    
    @JavascriptInterface
    public void openRecents() { mAdbBridge.openRecents(); }
    
    @JavascriptInterface
    public void setDefaultDesktopViaAdb() { mAdbBridge.setDefaultDesktopViaAdb(); }

    // ==================== SystemBridge 委托方法（组件配置） ====================
    @JavascriptInterface
    public boolean saveComponentConfig(String componentName, boolean isEnabled) {
        return mSystemBridge.saveComponentConfig(componentName, isEnabled);
    }
    
    @JavascriptInterface
    public boolean isComponentEnabled(String componentName) {
        return mSystemBridge.isComponentEnabled(componentName);
    }
    
    @JavascriptInterface
    public String getAllComponentConfigs() {
        return mSystemBridge.getAllComponentConfigs();
    }

    // ==================== MainActivity 调用的UI更新方法 ====================
    
    /**
     * 更新时间显示（MainActivity调用）
     * 
     * @param time 时间字符串
     * @param date 日期字符串
     * @param lunarDate 农历日期
     */
    public void updateTimeDisplay(final String time, final String date, final String lunarDate) {
        String javascript = String.format(
            "javascript:window.updateTimeDisplay(%s, %s, %s)",
            JSONObject.quote(time),
            JSONObject.quote(date),
            JSONObject.quote(lunarDate)
        );
        safeEvaluateJavascript(javascript);
    }
    
    /**
     * 更新车辆状态显示（MainActivity调用）
     * 
     * @param state 车辆状态对象
     */
    public void updatePresentationCarState(final LeapMotorCarState state) {
        if (state == null) {
            return;
        }
        try {
            JSONObject stateJson = new JSONObject();
            stateJson.put("speed", state.getSpeed());
            stateJson.put("gear", state.getGear());
            stateJson.put("gearText", state.getGearText());
            stateJson.put("leftTurnLight", state.getLeftTurnLight());
            stateJson.put("rightTurnLight", state.getRightTurnLight());
            stateJson.put("lowBeamLight", state.getLowBeamLight());
            stateJson.put("lockState", state.getLockState());
            stateJson.put("isLocked", state.isLocked());
            stateJson.put("openDoorCount", state.getOpenDoorCount());
            stateJson.put("isAnyDoorOpen", state.isAnyDoorOpen());
            stateJson.put("sunroof", state.getSunroof());
            stateJson.put("bluetoothConnected", state.isBluetoothConnected());
            stateJson.put("acPageOpen", state.isAcPageOpen());
            stateJson.put("camera360Visible", state.isCamera360Visible());
            stateJson.put("frontLeftTirePressure", state.getFrontLeftTirePressure());
            stateJson.put("frontRightTirePressure", state.getFrontRightTirePressure());
            stateJson.put("rearLeftTirePressure", state.getRearLeftTirePressure());
            stateJson.put("rearRightTirePressure", state.getRearRightTirePressure());
            
            String javascript = "javascript:window.updateCarState(" + stateJson.toString() + ")";
            safeEvaluateJavascript(javascript);
        } catch (Exception e) {
            logE(TAG, "更新车辆状态失败", e);
        }
    }
    
    /**
     * 初始化空调状态（MainActivity调用）
     */
    public void initializeAcStatus() {
        safeEvaluateJavascript("javascript:window.initializeAcStatus()");
    }
}
