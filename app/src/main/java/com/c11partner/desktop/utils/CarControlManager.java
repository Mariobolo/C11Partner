package com.c11partner.desktop.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.bluetooth.BluetoothAdapter;
import android.net.wifi.WifiManager;
import android.app.UiModeManager;
import android.provider.Settings;
import android.util.Log;

/**
 * 零跑C11车控功能管理类
 * 整合Intent主动控制 + Settings.Global读写
 * 
 * 三层控制模型：
 * 1. Intent主动控制 - 发送广播/启动Activity
 * 2. Logcat被动监控 - 读取日志解析状态
 * 3. Settings.Global - 读写系统属性
 */
public class CarControlManager {
    
    private static final String TAG = "CarControlManager";
    private static CarControlManager instance;
    private Context context;
    
    // 广播Action常量
    public static final String ACTION_TO_CAR_CONTROL = "com.leapmotor.speech.tocarcontrol";
    public static final String ACTION_TO_AIR_CONDITIONER = "com.leapmotor.speech.toairconditioner";
    public static final String ACTION_TO_SETTINGS = "com.leapmotor.speech.tosettings";
    public static final String ACTION_METER_CTRL = "com.leapmotor.action.METER.CTRL";
    public static final String ACTION_CAMERA_AROUND = "com.leapmotor.camera_around";
    public static final String ACTION_VOICE_HAND_MESSAGE = "com.iflytek.autofly.handMessage";
    
    // Settings.Global属性常量
    public static final String KEY_CAMERA_OVERSPEED = "camera_overspeed";
    public static final String KEY_C11_VIDEO_ENABLE = "C11_VIDEO_ENABLE";
    public static final String KEY_STR_CAR_VEHICLE_LOCK = "strCarVehicleLock";
    public static final String KEY_LEAP_SCREEN_STATE = "leap_screen_state";
    public static final String KEY_DISPLAY_1_STATE = "display_1_state";
    public static final String KEY_SPEECH_SPEAK = "SPEECH_SPEAK";
    public static final String KEY_C11_CALL = "C11_CALL";
    public static final String KEY_C11_NAVI = "C11_NAVI";
    public static final String KEY_C11_MUSIC = "C11_MUSIC";
    public static final String KEY_STR_CAR_1409 = "strCar1409";
    public static final String KEY_STR_CAR_1410 = "strCar1410";
    public static final String KEY_STR_CAR_1411 = "strCar1411";
    public static final String KEY_STR_CAR_100006 = "strCar100006";
    public static final String KEY_STR_CAR_1800 = "strCar1800";
    public static final String KEY_STR_CAR_8867 = "strCar8867";
    
    // 驾驶模式
    public static final int DRIVE_MODE_COMFORT = 0;
    public static final int DRIVE_MODE_SPORT = 1;
    public static final int DRIVE_MODE_CUSTOM = 2;
    public static final int DRIVE_MODE_EXTREME = 3;
    public static final int DRIVE_MODE_ECO = 4;
    public static final int DRIVE_MODE_LEAPMOTOR = 5;
    
    // 氛围灯颜色
    public static final int AMBIENT_RED = 0;
    public static final int AMBIENT_ORANGE = 1;
    public static final int AMBIENT_YELLOW = 3;
    public static final int AMBIENT_GREEN = 7;
    public static final int AMBIENT_CYAN = 10;
    public static final int AMBIENT_BLUE = 14;
    public static final int AMBIENT_PURPLE = 16;
    
    // 本地状态缓存（用于无Logcat监控的开关）
    // 发送Intent指令时同步更新，重启后重置为默认值
    private Boolean lowBeamLightState = null;      // null=未知，true=开，false=关
    private Boolean rearFogLightState = null;
    private Boolean positionLightState = null;
    private Boolean pedestrianAlertState = null;
    private Boolean maxCoolingState = null;
    private Boolean nightModeState = null;
    private Boolean wifiEnabledState = null;
    private Boolean bluetoothEnabledState = null;

    private CarControlManager(Context context) {
        this.context = context.getApplicationContext();
    }
    
    public static synchronized CarControlManager getInstance(Context context) {
        if (instance == null) {
            instance = new CarControlManager(context);
        }
        return instance;
    }
    
    // ==================== 360全景控制 ====================
    
    /**
     * 启动360全景影像
     */
    public boolean startCamera360() {
        try {
            Intent intent = new Intent(ACTION_CAMERA_AROUND);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            Log.d(TAG, "启动360全景");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "启动360全景失败", e);
            return false;
        }
    }
    
    // ==================== 灯光控制 ====================
    
    /**
     * 控制近光灯
     * @param on true=开, false=关
     */
    public boolean setLowBeamLight(boolean on) {
        try {
            Intent intent = new Intent(ACTION_TO_CAR_CONTROL);
            intent.putExtra("CARLIGHT_JINGUANG", on ? 1 : 0);
            context.sendBroadcast(intent);
            lowBeamLightState = on;
            Log.d(TAG, "近光灯: " + (on ? "开" : "关"));
            return true;
        } catch (Exception e) {
            Log.e(TAG, "控制近光灯失败", e);
            return false;
        }
    }
    
    /**
     * 获取近光灯状态
     * 优先从Logcat监控获取，fallback到本地缓存
     */
    public boolean isLowBeamLightOn() {
        // 优先从Logcat监控的CarState获取
        // 由WebViewBridge层调用LogcatMonitorService
        // 这里返回本地缓存
        return lowBeamLightState != null ? lowBeamLightState : false;
    }
    
    /**
     * 由Logcat监控更新近光灯状态
     */
    public void updateLowBeamLightState(boolean on) {
        this.lowBeamLightState = on;
    }
    
    /**
     * 控制后雾灯
     */
    public boolean setRearFogLight(boolean on) {
        try {
            Intent intent = new Intent(ACTION_TO_CAR_CONTROL);
            intent.putExtra("CARLIGHT_REARFOGCTL", on ? 1 : 0);
            context.sendBroadcast(intent);
            rearFogLightState = on;
            Log.d(TAG, "后雾灯: " + (on ? "开" : "关"));
            return true;
        } catch (Exception e) {
            Log.e(TAG, "控制后雾灯失败", e);
            return false;
        }
    }
    public boolean isRearFogLightOn() { return rearFogLightState != null ? rearFogLightState : false; }
    
    /**
     * 控制示廓灯
     */
    public boolean setPositionLight(boolean on) {
        try {
            Intent intent = new Intent(ACTION_TO_CAR_CONTROL);
            intent.putExtra("CARLIGHT_SHEKUODENG", on ? 1 : 0);
            context.sendBroadcast(intent);
            positionLightState = on;
            Log.d(TAG, "示廓灯: " + (on ? "开" : "关"));
            return true;
        } catch (Exception e) {
            Log.e(TAG, "控制示廓灯失败", e);
            return false;
        }
    }
    public boolean isPositionLightOn() { return positionLightState != null ? positionLightState : false; }
    
    /**
     * 控制行人警示音
     */
    public boolean setPedestrianAlert(boolean on) {
        try {
            Intent intent = new Intent(ACTION_TO_CAR_CONTROL);
            intent.putExtra("PEDESTRIANS_ALERT", on ? 1 : 0);
            context.sendBroadcast(intent);
            pedestrianAlertState = on;
            Log.d(TAG, "行人警示音: " + (on ? "开" : "关"));
            return true;
        } catch (Exception e) {
            Log.e(TAG, "控制行人警示音失败", e);
            return false;
        }
    }
    public boolean isPedestrianAlertOn() { return pedestrianAlertState != null ? pedestrianAlertState : false; }
    /**
     * 由Logcat监控更新行人警示状态
     */
    public void updatePedestrianAlertState(boolean on) {
        this.pedestrianAlertState = on;
    }
    
    // ==================== 驾驶模式控制 ====================
    
    /**
     * 设置驾驶模式
     * @param mode 0=舒适, 1=运动, 2=自定义, 3=极致, 4=经济, 5=零跑模式
     */
    public boolean setDriveMode(int mode) {
        try {
            // 参数验证
            if (mode < 0 || mode > 5) {
                Log.e(TAG, "无效的驾驶模式: " + mode);
                return false;
            }
            Intent intent = new Intent(ACTION_TO_CAR_CONTROL);
            intent.putExtra("MMI_DRIVER_MODE_SET", mode);
            context.sendBroadcast(intent);
            Log.d(TAG, "驾驶模式: " + mode);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "设置驾驶模式失败", e);
            return false;
        }
    }
    
    // ==================== 场景模式控制 ====================
    
    /**
     * 控制守护模式
     */
    public boolean setGuardMode(boolean on) {
        try {
            Intent intent = new Intent(ACTION_TO_CAR_CONTROL);
            intent.putExtra("GUARD_MODE", on ? 1 : 0);
            context.sendBroadcast(intent);
            Log.d(TAG, "守护模式: " + (on ? "开" : "关"));
            return true;
        } catch (Exception e) {
            Log.e(TAG, "控制守护模式失败", e);
            return false;
        }
    }
    
    /**
     * 控制小憩模式
     */
    public boolean setRestMode(boolean on) {
        try {
            Intent intent = new Intent(ACTION_TO_CAR_CONTROL);
            intent.putExtra("REST_MODE", on ? 1 : 0);
            context.sendBroadcast(intent);
            Log.d(TAG, "小憩模式: " + (on ? "开" : "关"));
            return true;
        } catch (Exception e) {
            Log.e(TAG, "控制小憩模式失败", e);
            return false;
        }
    }
    
    /**
     * 控制露营模式
     */
    public boolean setCampingMode(boolean on) {
        try {
            Intent intent = new Intent(ACTION_TO_CAR_CONTROL);
            intent.putExtra("CAMPING_MODE", on ? 1 : 0);
            context.sendBroadcast(intent);
            Log.d(TAG, "露营模式: " + (on ? "开" : "关"));
            return true;
        } catch (Exception e) {
            Log.e(TAG, "控制露营模式失败", e);
            return false;
        }
    }
    
    /**
     * 控制省电模式
     */
    public boolean setPowerSaveMode(boolean on) {
        try {
            Intent intent = new Intent(ACTION_TO_CAR_CONTROL);
            intent.putExtra("POWER_SAVE_MODE", on ? 1 : 0);
            context.sendBroadcast(intent);
            Log.d(TAG, "省电模式: " + (on ? "开" : "关"));
            return true;
        } catch (Exception e) {
            Log.e(TAG, "控制省电模式失败", e);
            return false;
        }
    }
    
    /**
     * 控制哨兵模式
     */
    public boolean setSentinelMode(boolean on) {
        try {
            Intent intent = new Intent(ACTION_TO_CAR_CONTROL);
            intent.putExtra("SENTINEL_MODE", on ? 1 : 0);
            context.sendBroadcast(intent);
            Log.d(TAG, "哨兵模式: " + (on ? "开" : "关"));
            return true;
        } catch (Exception e) {
            Log.e(TAG, "控制哨兵模式失败", e);
            return false;
        }
    }
    
    // ==================== 语音控制 ====================

    /**
     * 发送语音指令（通过讯飞语音接口）
     * 暂时用于替代没有直接控制接口的功能
     * @param command 语音指令文本
     * @return 是否成功
     */
    public boolean sendVoiceCommand(String command) {
        try {
            Intent intent = new Intent(ACTION_VOICE_HAND_MESSAGE);
            intent.putExtra("text", command);  // 猜测的参数名，待实车验证
            context.sendBroadcast(intent);
            Log.d(TAG, "发送语音指令: " + command);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "发送语音指令失败: " + command, e);
            return false;
        }
    }

    // ==================== 空调控制 ====================
    
    /**
     * 设置最大制冷模式
     */
    public boolean setMaxCooling(boolean on) {
        try {
            Intent intent = new Intent(ACTION_TO_AIR_CONDITIONER);
            intent.putExtra("HVACACMAXREQ", on ? 1 : 0);
            context.sendBroadcast(intent);
            maxCoolingState = on;
            Log.d(TAG, "最大制冷: " + (on ? "开" : "关"));
            return true;
        } catch (Exception e) {
            Log.e(TAG, "设置最大制冷失败", e);
            return false;
        }
    }
    public boolean isMaxCoolingOn() { return maxCoolingState != null ? maxCoolingState : false; }
    /**
     * 由Logcat监控更新最大制冷状态
     */
    public void updateMaxCoolingState(boolean on) {
        this.maxCoolingState = on;
    }

    /**
     * 设置空调开关
     * @param on true=开, false=关
     * @return 是否成功
     */
    public boolean setAcEnabled(boolean on) {
        // 先尝试直接控制
        boolean result = setGlobalInt(KEY_STR_CAR_100006, on ? 1 : 0);
        if (!result) {
            // 失败则用语音控制作为备选
            String command = on ? "打开空调" : "关闭空调";
            result = sendVoiceCommand(command);
        }
        return result;
    }

    /**
     * 获取空调开关状态
     * @return true=开, false=关
     */
    public boolean isAcEnabled() {
        return getGlobalInt(KEY_STR_CAR_100006, 0) == 1;
    }

    /**
     * 设置空调风量
     * @param level 风量等级（1-7或1-8，待验证）
     * @return 是否成功
     */
    public boolean setWindLevel(int level) {
        // 先尝试直接控制
        boolean result = setGlobalInt(KEY_STR_CAR_1411, Math.max(1, Math.min(8, level)));
        if (!result) {
            // 失败则用语音控制作为备选
            String command = "空调风量调到" + level + "档";
            result = sendVoiceCommand(command);
        }
        return result;
    }

    /**
     * 获取空调风量
     * @return 风量等级
     */
    public int getWindLevel() {
        return getGlobalInt(KEY_STR_CAR_1411, 3);
    }
    /**
     * 设置除霜模式
     * 暂未找到直接控制接口，使用语音控制
     * @param on true=开, false=关
     * @return 是否成功
     */
    public boolean setDefrost(boolean on) {
        String command = on ? "打开除霜" : "关闭除霜";
        return sendVoiceCommand(command);
    }

    
    // ==================== 系统设置控制 ====================
    
    /**
     * 设置夜间/白天模式
     * @param night true=夜间, false=白天
     */
    public boolean setNightMode(boolean night) {
        try {
            Intent intent = new Intent(ACTION_TO_SETTINGS);
            intent.putExtra("mode", night ? 0 : 1);
            context.sendBroadcast(intent);
            nightModeState = night;
            Log.d(TAG, "模式: " + (night ? "夜间" : "白天"));
            return true;
        } catch (Exception e) {
            Log.e(TAG, "设置夜间模式失败", e);
            return false;
        }
    }
    public boolean isNightModeOn() {
        try {
            UiModeManager uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
            if (uiModeManager != null) {
                return uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES;
            }
        } catch (Exception e) {
            Log.e(TAG, "读取夜间模式状态失败", e);
        }
        return nightModeState != null ? nightModeState : false;
    }
    
    /**
     * 控制WiFi开关
     */
    public boolean setWifiEnabled(boolean enabled) {
        try {
            Intent intent = new Intent(ACTION_TO_SETTINGS);
            intent.putExtra("wifi", enabled ? 1 : 0);
            context.sendBroadcast(intent);
            wifiEnabledState = enabled;
            Log.d(TAG, "WiFi: " + (enabled ? "开" : "关"));
            return true;
        } catch (Exception e) {
            Log.e(TAG, "控制WiFi失败", e);
            return false;
        }
    }
    public boolean isWifiEnabled() {
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null) {
                return wifiManager.isWifiEnabled();
            }
        } catch (Exception e) {
            Log.e(TAG, "读取WiFi状态失败", e);
        }
        return wifiEnabledState != null ? wifiEnabledState : false;
    }
    
    /**
     * 控制蓝牙开关
     */
    public boolean setBluetoothEnabled(boolean enabled) {
        try {
            Intent intent = new Intent(ACTION_TO_SETTINGS);
            intent.putExtra("bluetooth", enabled ? 1 : 0);
            context.sendBroadcast(intent);
            bluetoothEnabledState = enabled;
            Log.d(TAG, "蓝牙: " + (enabled ? "开" : "关"));
            return true;
        } catch (Exception e) {
            Log.e(TAG, "控制蓝牙失败", e);
            return false;
        }
    }
    public boolean isBluetoothEnabled() {
        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter != null) {
                return bluetoothAdapter.isEnabled();
            }
        } catch (Exception e) {
            Log.e(TAG, "读取蓝牙状态失败", e);
        }
        return bluetoothEnabledState != null ? bluetoothEnabledState : false;
    }
    
    // ==================== 方控按键模拟 ====================
    
    /**
     * 模拟方控上一曲
     */
    public boolean sendPrevTrack() {
        try {
            Intent intent = new Intent(ACTION_METER_CTRL);
            intent.putExtra("value", 1);
            context.sendBroadcast(intent);
            Log.d(TAG, "方控: 上一曲");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "发送上一曲失败", e);
            return false;
        }
    }
    
    /**
     * 模拟方控下一曲
     */
    public boolean sendNextTrack() {
        try {
            Intent intent = new Intent(ACTION_METER_CTRL);
            intent.putExtra("value", 2);
            context.sendBroadcast(intent);
            Log.d(TAG, "方控: 下一曲");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "发送下一曲失败", e);
            return false;
        }
    }
    
    // ==================== Settings.Global 读写 ====================
    
    /**
     * 读取Settings.Global整数值
     */
    public int getGlobalInt(String key, int defaultValue) {
        try {
            return Settings.Global.getInt(context.getContentResolver(), key, defaultValue);
        } catch (Exception e) {
            Log.e(TAG, "读取Settings.Global失败: " + key, e);
            return defaultValue;
        }
    }
    
    /**
     * 读取Settings.Global字符串值
     */
    public String getGlobalString(String key) {
        try {
            return Settings.Global.getString(context.getContentResolver(), key);
        } catch (Exception e) {
            Log.e(TAG, "读取Settings.Global失败: " + key, e);
            return null;
        }
    }
    
    /**
     * 写入Settings.Global整数值
     */
    public boolean setGlobalInt(String key, int value) {
        try {
            Settings.Global.putInt(context.getContentResolver(), key, value);
            Log.d(TAG, "设置 " + key + " = " + value);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "写入Settings.Global失败: " + key, e);
            return false;
        }
    }
    
    // ==================== 360全景超速限制 ====================
    
    /**
     * 设置360全景超速限制
     * @param enabled true=开启限制, false=关闭限制（任何时速都可开启）
     */
    public boolean setCameraOverspeedLimit(boolean enabled) {
        return setGlobalInt(KEY_CAMERA_OVERSPEED, enabled ? 1 : 0);
    }
    
    /**
     * 获取360全景超速限制状态
     */
    public boolean isCameraOverspeedLimitEnabled() {
        return getGlobalInt(KEY_CAMERA_OVERSPEED, 1) == 1;
    }
    
    // ==================== 行驶中视频播放 ====================
    
    /**
     * 设置行驶中视频播放限制
     * @param enabled true=允许播放, false=禁止播放
     */
    public boolean setVideoWhileDriving(boolean enabled) {
        return setGlobalInt(KEY_C11_VIDEO_ENABLE, enabled ? 1 : 0);
    }
    
    /**
     * 获取行驶中视频播放状态
     */
    public boolean isVideoWhileDrivingEnabled() {
        return getGlobalInt(KEY_C11_VIDEO_ENABLE, 0) == 1;
    }
    
    // ==================== 音量控制 ====================
    
    /**
     * 设置蓝牙电话音量
     */
    public boolean setCallVolume(int volume) {
        return setGlobalInt(KEY_C11_CALL, Math.max(0, Math.min(100, volume)));
    }
    
    /**
     * 获取蓝牙电话音量
     */
    public int getCallVolume() {
        return getGlobalInt(KEY_C11_CALL, 50);
    }
    
    /**
     * 设置导航音量
     */
    public boolean setNaviVolume(int volume) {
        return setGlobalInt(KEY_C11_NAVI, Math.max(0, Math.min(100, volume)));
    }
    
    /**
     * 获取导航音量
     */
    public int getNaviVolume() {
        return getGlobalInt(KEY_C11_NAVI, 50);
    }
    
    /**
     * 设置媒体音量
     */
    public boolean setMusicVolume(int volume) {
        return setGlobalInt(KEY_C11_MUSIC, Math.max(0, Math.min(100, volume)));
    }
    
    /**
     * 获取媒体音量
     */
    public int getMusicVolume() {
        return getGlobalInt(KEY_C11_MUSIC, 50);
    }
    
    // ==================== 空调温度控制 ====================
    
    /**
     * 设置主驾空调温度
     * @param temp 16-30℃
     */
    public boolean setDriverTemp(int temp) {
        return setGlobalInt(KEY_STR_CAR_1409, Math.max(16, Math.min(30, temp)));
    }
    
    /**
     * 获取主驾空调温度
     */
    public int getDriverTemp() {
        return getGlobalInt(KEY_STR_CAR_1409, 24);
    }
    
    /**
     * 设置副驾空调温度
     */
    public boolean setPassengerTemp(int temp) {
        return setGlobalInt(KEY_STR_CAR_1410, Math.max(16, Math.min(30, temp)));
    }
    
    /**
     * 获取副驾空调温度
     */
    public int getPassengerTemp() {
        return getGlobalInt(KEY_STR_CAR_1410, 24);
    }
    
    // ==================== 氛围灯控制 ====================
    
    /**
     * 设置氛围灯开关
     */
    public boolean setAmbientLightEnabled(boolean enabled) {
        return setGlobalInt(KEY_STR_CAR_1800, enabled ? 1 : 0);
    }
    
    /**
     * 获取氛围灯开关状态
     */
    public boolean isAmbientLightEnabled() {
        return getGlobalInt(KEY_STR_CAR_1800, 0) == 1;
    }
    
    /**
     * 设置氛围灯颜色
     * @param color 0=红, 1=橙, 3=黄, 7=绿, 10=青, 14=蓝, 16=紫
     */
    public boolean setAmbientLightColor(int color) {
        // 参数验证
        if (color < 0 || color > 16) {
            Log.e(TAG, "无效的氛围灯颜色: " + color);
            return false;
        }
        return setGlobalInt(KEY_STR_CAR_8867, color);
    }
    
    /**
     * 获取氛围灯颜色
     */
    public int getAmbientLightColor() {
        return getGlobalInt(KEY_STR_CAR_8867, 0);
    }
    
    // ==================== 副屏控制 ====================
    
    /**
     * 设置副屏显示状态
     */
    public boolean setSecondaryScreenEnabled(boolean enabled) {
        return setGlobalInt(KEY_DISPLAY_1_STATE, enabled ? 1 : 0);
    }
    
    /**
     * 获取副屏显示状态
     */
    public boolean isSecondaryScreenEnabled() {
        return getGlobalInt(KEY_DISPLAY_1_STATE, 1) == 1;
    }
    
    // ==================== 语音播报控制 ====================
    
    /**
     * 设置语音播报开关
     */
    public boolean setSpeechEnabled(boolean enabled) {
        return setGlobalInt(KEY_SPEECH_SPEAK, enabled ? 1 : 0);
    }
    
    /**
     * 获取语音播报开关状态
     */
    public boolean isSpeechEnabled() {
        return getGlobalInt(KEY_SPEECH_SPEAK, 1) == 1;
    }
    
    // ==================== 车辆锁状态读取 ====================
    
    /**
     * 获取车辆锁状态
     * @return true=上锁, false=解锁
     */
    public boolean isVehicleLocked() {
        String value = getGlobalString(KEY_STR_CAR_VEHICLE_LOCK);
        return "1".equals(value);
    }
    
    // ==================== 屏幕状态读取 ====================
    
    /**
     * 获取屏幕状态
     * @return true=点亮, false=熄灭
     */
    public boolean isScreenOn() {
        return getGlobalInt(KEY_LEAP_SCREEN_STATE, 0) == 0;
    }
    
    // ==================== 座椅控制 ====================
    
    /**
     * 设置主驾座椅加热
     * @param level 加热等级（0-3，0=关闭）
     */
    public boolean setDriverSeatHeating(int level) {
        try {
            int safeLevel = Math.max(0, Math.min(3, level));
            Intent intent = new Intent(ACTION_TO_CAR_CONTROL);
            intent.putExtra("SEAT_HEAT_LEFT", safeLevel);
            context.sendBroadcast(intent);
            Log.d(TAG, "主驾座椅加热等级: " + safeLevel);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "设置主驾座椅加热失败", e);
            return false;
        }
    }
    
    /**
     * 设置副驾座椅加热
     * @param level 加热等级（0-3，0=关闭）
     */
    public boolean setPassengerSeatHeating(int level) {
        try {
            int safeLevel = Math.max(0, Math.min(3, level));
            Intent intent = new Intent(ACTION_TO_CAR_CONTROL);
            intent.putExtra("SEAT_HEAT_RIGHT", safeLevel);
            context.sendBroadcast(intent);
            Log.d(TAG, "副驾座椅加热等级: " + safeLevel);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "设置副驾座椅加热失败", e);
            return false;
        }
    }
    
    /**
     * 设置主驾座椅通风
     * @param level 通风等级（0-3，0=关闭）
     */
    public boolean setDriverSeatVentilation(int level) {
        try {
            int safeLevel = Math.max(0, Math.min(3, level));
            Intent intent = new Intent(ACTION_TO_CAR_CONTROL);
            intent.putExtra("SEAT_VENT_LEFT", safeLevel);
            context.sendBroadcast(intent);
            Log.d(TAG, "主驾座椅通风等级: " + safeLevel);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "设置主驾座椅通风失败", e);
            return false;
        }
    }
    
    /**
     * 设置副驾座椅通风
     * @param level 通风等级（0-3，0=关闭）
     */
    public boolean setPassengerSeatVentilation(int level) {
        try {
            int safeLevel = Math.max(0, Math.min(3, level));
            Intent intent = new Intent(ACTION_TO_CAR_CONTROL);
            intent.putExtra("SEAT_VENT_RIGHT", safeLevel);
            context.sendBroadcast(intent);
            Log.d(TAG, "副驾座椅通风等级: " + safeLevel);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "设置副驾座椅通风失败", e);
            return false;
        }
    }
    
    // ==================== 方向盘控制 ====================
    
    /**
     * 设置方向盘加热
     * @param on true=开启, false=关闭
     */
    public boolean setSteeringWheelHeating(boolean on) {
        try {
            Intent intent = new Intent(ACTION_TO_CAR_CONTROL);
            intent.putExtra("STEERING_HEAT", on ? 1 : 0);
            context.sendBroadcast(intent);
            Log.d(TAG, "方向盘加热: " + (on ? "开" : "关"));
            return true;
        } catch (Exception e) {
            Log.e(TAG, "设置方向盘加热失败", e);
            return false;
        }
    }
    
    // ==================== 后视镜控制 ====================
    
    /**
     * 折叠后视镜
     */
    public boolean foldMirrors() {
        try {
            Intent intent = new Intent(ACTION_TO_CAR_CONTROL);
            intent.putExtra("MIRROR_FOLD", 1);
            context.sendBroadcast(intent);
            Log.d(TAG, "折叠后视镜");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "折叠后视镜失败", e);
            return false;
        }
    }
    
    /**
     * 展开后视镜
     */
    public boolean unfoldMirrors() {
        try {
            Intent intent = new Intent(ACTION_TO_CAR_CONTROL);
            intent.putExtra("MIRROR_FOLD", 0);
            context.sendBroadcast(intent);
            Log.d(TAG, "展开后视镜");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "展开后视镜失败", e);
            return false;
        }
    }
    
    /**
     * 设置后视镜加热
     * @param on true=开启, false=关闭
     */
    public boolean setMirrorHeating(boolean on) {
        try {
            Intent intent = new Intent(ACTION_TO_CAR_CONTROL);
            intent.putExtra("MIRROR_HEAT", on ? 1 : 0);
            context.sendBroadcast(intent);
            Log.d(TAG, "后视镜加热: " + (on ? "开" : "关"));
            return true;
        } catch (Exception e) {
            Log.e(TAG, "设置后视镜加热失败", e);
            return false;
        }
    }
}
