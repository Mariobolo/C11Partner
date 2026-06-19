package com.c11partner.desktop.utils;

import android.content.Context;
import android.content.Intent;
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
            Log.d(TAG, "近光灯: " + (on ? "开" : "关"));
            return true;
        } catch (Exception e) {
            Log.e(TAG, "控制近光灯失败", e);
            return false;
        }
    }
    
    /**
     * 控制后雾灯
     */
    public boolean setRearFogLight(boolean on) {
        try {
            Intent intent = new Intent(ACTION_TO_CAR_CONTROL);
            intent.putExtra("CARLIGHT_REARFOGCTL", on ? 1 : 0);
            context.sendBroadcast(intent);
            Log.d(TAG, "后雾灯: " + (on ? "开" : "关"));
            return true;
        } catch (Exception e) {
            Log.e(TAG, "控制后雾灯失败", e);
            return false;
        }
    }
    
    /**
     * 控制示廓灯
     */
    public boolean setPositionLight(boolean on) {
        try {
            Intent intent = new Intent(ACTION_TO_CAR_CONTROL);
            intent.putExtra("CARLIGHT_SHEKUODENG", on ? 1 : 0);
            context.sendBroadcast(intent);
            Log.d(TAG, "示廓灯: " + (on ? "开" : "关"));
            return true;
        } catch (Exception e) {
            Log.e(TAG, "控制示廓灯失败", e);
            return false;
        }
    }
    
    /**
     * 控制行人警示音
     */
    public boolean setPedestrianAlert(boolean on) {
        try {
            Intent intent = new Intent(ACTION_TO_CAR_CONTROL);
            intent.putExtra("PEDESTRIANS_ALERT", on ? 1 : 0);
            context.sendBroadcast(intent);
            Log.d(TAG, "行人警示音: " + (on ? "开" : "关"));
            return true;
        } catch (Exception e) {
            Log.e(TAG, "控制行人警示音失败", e);
            return false;
        }
    }
    
    // ==================== 驾驶模式控制 ====================
    
    /**
     * 设置驾驶模式
     * @param mode 0=舒适, 1=运动, 2=自定义, 3=极致, 4=经济, 5=零跑模式
     */
    public boolean setDriveMode(int mode) {
        try {
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
    
    // ==================== 空调控制 ====================
    
    /**
     * 设置最大制冷模式
     */
    public boolean setMaxCooling(boolean on) {
        try {
            Intent intent = new Intent(ACTION_TO_AIR_CONDITIONER);
            intent.putExtra("HVACACMAXREQ", on ? 1 : 0);
            context.sendBroadcast(intent);
            Log.d(TAG, "最大制冷: " + (on ? "开" : "关"));
            return true;
        } catch (Exception e) {
            Log.e(TAG, "设置最大制冷失败", e);
            return false;
        }
    }

    /**
     * 设置空调开关
     * @param on true=开, false=关
     * @return 是否成功
     */
    public boolean setAcEnabled(boolean on) {
        return setGlobalInt(KEY_STR_CAR_100006, on ? 1 : 0);
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
        return setGlobalInt(KEY_STR_CAR_1411, Math.max(1, Math.min(8, level)));
    }

    /**
     * 获取空调风量
     * @return 风量等级
     */
    public int getWindLevel() {
        return getGlobalInt(KEY_STR_CAR_1411, 3);
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
            Log.d(TAG, "模式: " + (night ? "夜间" : "白天"));
            return true;
        } catch (Exception e) {
            Log.e(TAG, "设置夜间模式失败", e);
            return false;
        }
    }
    
    /**
     * 控制WiFi开关
     */
    public boolean setWifiEnabled(boolean enabled) {
        try {
            Intent intent = new Intent(ACTION_TO_SETTINGS);
            intent.putExtra("wifi", enabled ? 1 : 0);
            context.sendBroadcast(intent);
            Log.d(TAG, "WiFi: " + (enabled ? "开" : "关"));
            return true;
        } catch (Exception e) {
            Log.e(TAG, "控制WiFi失败", e);
            return false;
        }
    }
    
    /**
     * 控制蓝牙开关
     */
    public boolean setBluetoothEnabled(boolean enabled) {
        try {
            Intent intent = new Intent(ACTION_TO_SETTINGS);
            intent.putExtra("bluetooth", enabled ? 1 : 0);
            context.sendBroadcast(intent);
            Log.d(TAG, "蓝牙: " + (enabled ? "开" : "关"));
            return true;
        } catch (Exception e) {
            Log.e(TAG, "控制蓝牙失败", e);
            return false;
        }
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
}
