package com.c11partner.desktop.bridge;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.c11partner.desktop.MainActivity;
import com.c11partner.desktop.LeapMotorCamera360;
import com.c11partner.desktop.utils.CarControlManager;

/**
 * 车控功能 Bridge
 * 
 * 负责所有车辆控制相关的 JS 接口实现
 * 包括：360全景、灯光、驾驶模式、空调、音量、氛围灯等
 * 
 * @author C11Partner
 * @version 1.0
 */
public class CarControlBridge extends BaseBridge {
    
    private static final String TAG = "CarControlBridge";
    
    private CarControlManager mCarControlManager;
    
    /**
     * 构造函数
     * 
     * @param context 上下文
     * @param activity MainActivity 实例
     */
    public CarControlBridge(Context context, MainActivity activity) {
        super(context, activity);
    }
    
    /**
     * 获取 CarControlManager 实例
     * 
     * @return CarControlManager 实例
     */
    private CarControlManager getCarControlManager() {
        if (mCarControlManager == null && mContext != null) {
            mCarControlManager = CarControlManager.getInstance(mContext);
        }
        return mCarControlManager;
    }
    
    // ==================== 360全景相关 ====================
    
    /**
     * 手动启动 360 全景
     * 
     * @return 是否启动成功
     */
    @JavascriptInterface
    public boolean startCamera360() {
        try {
            LeapMotorCamera360.startCamera360(mContext, "手动触发");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "启动360全景失败", e);
            showToastOnUiThread("启动360全景失败");
            return false;
        }
    }
    
    /**
     * 设置 360 全景超速限制
     * 
     * @param enabled 是否启用
     * @return 是否设置成功
     */
    @JavascriptInterface
    public boolean setCameraOverspeedLimit(boolean enabled) {
        try {
            return getCarControlManager().setCameraOverspeedLimit(enabled);
        } catch (Exception e) {
            Log.e(TAG, "设置360全景超速限制失败", e);
            return false;
        }
    }
    
    /**
     * 检查 360 全景超速限制是否启用
     * 
     * @return 是否启用
     */
    @JavascriptInterface
    public boolean isCameraOverspeedLimitEnabled() {
        try {
            return getCarControlManager().isCameraOverspeedLimitEnabled();
        } catch (Exception e) {
            Log.e(TAG, "获取360全景超速限制状态失败", e);
            return false;
        }
    }
    
    // ==================== 灯光控制 ====================
    
    /**
     * 设置近光灯
     * 
     * @param on 是否开启
     * @return 是否设置成功
     */
    @JavascriptInterface
    public boolean setLowBeamLight(boolean on) {
        try {
            return getCarControlManager().setLowBeamLight(on);
        } catch (Exception e) {
            Log.e(TAG, "设置近光灯失败", e);
            return false;
        }
    }
    
    /**
     * 设置后雾灯
     * 
     * @param on 是否开启
     * @return 是否设置成功
     */
    @JavascriptInterface
    public boolean setRearFogLight(boolean on) {
        try {
            return getCarControlManager().setRearFogLight(on);
        } catch (Exception e) {
            Log.e(TAG, "设置后雾灯失败", e);
            return false;
        }
    }
    
    /**
     * 设置示廓灯
     * 
     * @param on 是否开启
     * @return 是否设置成功
     */
    @JavascriptInterface
    public boolean setPositionLight(boolean on) {
        try {
            return getCarControlManager().setPositionLight(on);
        } catch (Exception e) {
            Log.e(TAG, "设置示廓灯失败", e);
            return false;
        }
    }
    
    /**
     * 设置行人警示音
     * 
     * @param on 是否开启
     * @return 是否设置成功
     */
    @JavascriptInterface
    public boolean setPedestrianAlert(boolean on) {
        try {
            return getCarControlManager().setPedestrianAlert(on);
        } catch (Exception e) {
            Log.e(TAG, "设置行人警示音失败", e);
            return false;
        }
    }
    
    // ==================== 驾驶/场景模式 ====================
    
    /**
     * 设置驾驶模式
     * 
     * @param mode 模式 0=舒适,1=运动,2=自定义,3=极致,4=经济,5=零跑模式
     * @return 是否设置成功
     */
    @JavascriptInterface
    public boolean setDriveMode(int mode) {
        try {
            return getCarControlManager().setDriveMode(mode);
        } catch (Exception e) {
            Log.e(TAG, "设置驾驶模式失败", e);
            return false;
        }
    }
    
    /**
     * 设置守护模式
     * 
     * @param on 是否开启
     * @return 是否设置成功
     */
    @JavascriptInterface
    public boolean setGuardMode(boolean on) {
        try {
            return getCarControlManager().setGuardMode(on);
        } catch (Exception e) {
            Log.e(TAG, "设置守护模式失败", e);
            return false;
        }
    }
    
    /**
     * 设置小憩模式
     * 
     * @param on 是否开启
     * @return 是否设置成功
     */
    @JavascriptInterface
    public boolean setRestMode(boolean on) {
        try {
            return getCarControlManager().setRestMode(on);
        } catch (Exception e) {
            Log.e(TAG, "设置小憩模式失败", e);
            return false;
        }
    }
    
    /**
     * 设置露营模式
     * 
     * @param on 是否开启
     * @return 是否设置成功
     */
    @JavascriptInterface
    public boolean setCampingMode(boolean on) {
        try {
            return getCarControlManager().setCampingMode(on);
        } catch (Exception e) {
            Log.e(TAG, "设置露营模式失败", e);
            return false;
        }
    }
    
    /**
     * 设置省电模式
     * 
     * @param on 是否开启
     * @return 是否设置成功
     */
    @JavascriptInterface
    public boolean setPowerSaveMode(boolean on) {
        try {
            return getCarControlManager().setPowerSaveMode(on);
        } catch (Exception e) {
            Log.e(TAG, "设置省电模式失败", e);
            return false;
        }
    }
    
    /**
     * 设置哨兵模式
     * 
     * @param on 是否开启
     * @return 是否设置成功
     */
    @JavascriptInterface
    public boolean setSentinelMode(boolean on) {
        try {
            return getCarControlManager().setSentinelMode(on);
        } catch (Exception e) {
            Log.e(TAG, "设置哨兵模式失败", e);
            return false;
        }
    }
    
    // ==================== 空调控制 ====================
    
    /**
     * 设置极速制冷
     * 
     * @param on 是否开启
     * @return 是否设置成功
     */
    @JavascriptInterface
    public boolean setMaxCooling(boolean on) {
        try {
            return getCarControlManager().setMaxCooling(on);
        } catch (Exception e) {
            Log.e(TAG, "设置极速制冷失败", e);
            return false;
        }
    }
    
    /**
     * 设置空调开关
     * 
     * @param enabled 是否开启
     * @return 是否设置成功
     */
    @JavascriptInterface
    public boolean setAcEnabled(boolean enabled) {
        try {
            return getCarControlManager().setAcEnabled(enabled);
        } catch (Exception e) {
            Log.e(TAG, "设置空调开关失败", e);
            return false;
        }
    }
    
    /**
     * 检查空调是否开启
     * 
     * @return 是否开启
     */
    @JavascriptInterface
    public boolean isAcEnabled() {
        try {
            return getCarControlManager().isAcEnabled();
        } catch (Exception e) {
            Log.e(TAG, "获取空调状态失败", e);
            return false;
        }
    }
    
    /**
     * 设置空调风量
     * 
     * @param level 风量等级
     * @return 是否设置成功
     */
    @JavascriptInterface
    public boolean setWindLevel(int level) {
        try {
            return getCarControlManager().setWindLevel(level);
        } catch (Exception e) {
            Log.e(TAG, "设置空调风量失败", e);
            return false;
        }
    }
    
    /**
     * 获取空调风量
     * 
     * @return 风量等级
     */
    @JavascriptInterface
    public int getWindLevel() {
        try {
            return getCarControlManager().getWindLevel();
        } catch (Exception e) {
            Log.e(TAG, "获取空调风量失败", e);
            return 0;
        }
    }
    
    /**
     * 设置主驾空调温度
     * 
     * @param temp 温度
     * @return 是否设置成功
     */
    @JavascriptInterface
    public boolean setDriverTemp(int temp) {
        try {
            return getCarControlManager().setDriverTemp(temp);
        } catch (Exception e) {
            Log.e(TAG, "设置主驾温度失败", e);
            return false;
        }
    }
    
    /**
     * 获取主驾空调温度
     * 
     * @return 温度
     */
    @JavascriptInterface
    public int getDriverTemp() {
        try {
            return getCarControlManager().getDriverTemp();
        } catch (Exception e) {
            Log.e(TAG, "获取主驾温度失败", e);
            return 25;
        }
    }
    
    /**
     * 设置副驾空调温度
     * 
     * @param temp 温度
     * @return 是否设置成功
     */
    @JavascriptInterface
    public boolean setPassengerTemp(int temp) {
        try {
            return getCarControlManager().setPassengerTemp(temp);
        } catch (Exception e) {
            Log.e(TAG, "设置副驾温度失败", e);
            return false;
        }
    }
    
    /**
     * 获取副驾空调温度
     * 
     * @return 温度
     */
    @JavascriptInterface
    public int getPassengerTemp() {
        try {
            return getCarControlManager().getPassengerTemp();
        } catch (Exception e) {
            Log.e(TAG, "获取副驾温度失败", e);
            return 25;
        }
    }
    
    // ==================== 音量控制 ====================
    
    /**
     * 设置蓝牙电话音量
     * 
     * @param volume 音量值
     * @return 是否设置成功
     */
    @JavascriptInterface
    public boolean setCallVolume(int volume) {
        try {
            return getCarControlManager().setCallVolume(volume);
        } catch (Exception e) {
            Log.e(TAG, "设置电话音量失败", e);
            return false;
        }
    }
    
    /**
     * 获取蓝牙电话音量
     * 
     * @return 音量值
     */
    @JavascriptInterface
    public int getCallVolume() {
        try {
            return getCarControlManager().getCallVolume();
        } catch (Exception e) {
            Log.e(TAG, "获取电话音量失败", e);
            return 0;
        }
    }
    
    /**
     * 设置导航语音音量
     * 
     * @param volume 音量值
     * @return 是否设置成功
     */
    @JavascriptInterface
    public boolean setNaviVolume(int volume) {
        try {
            return getCarControlManager().setNaviVolume(volume);
        } catch (Exception e) {
            Log.e(TAG, "设置导航音量失败", e);
            return false;
        }
    }
    
    /**
     * 获取导航语音音量
     * 
     * @return 音量值
     */
    @JavascriptInterface
    public int getNaviVolume() {
        try {
            return getCarControlManager().getNaviVolume();
        } catch (Exception e) {
            Log.e(TAG, "获取导航音量失败", e);
            return 0;
        }
    }
    
    /**
     * 设置媒体音量
     * 
     * @param volume 音量值
     * @return 是否设置成功
     */
    @JavascriptInterface
    public boolean setMusicVolume(int volume) {
        try {
            return getCarControlManager().setMusicVolume(volume);
        } catch (Exception e) {
            Log.e(TAG, "设置媒体音量失败", e);
            return false;
        }
    }
    
    /**
     * 获取媒体音量
     * 
     * @return 音量值
     */
    @JavascriptInterface
    public int getMusicVolume() {
        try {
            return getCarControlManager().getMusicVolume();
        } catch (Exception e) {
            Log.e(TAG, "获取媒体音量失败", e);
            return 0;
        }
    }
    
    // ==================== 氛围灯控制 ====================
    
    /**
     * 设置氛围灯开关
     * 
     * @param enabled 是否开启
     * @return 是否设置成功
     */
    @JavascriptInterface
    public boolean setAmbientLightEnabled(boolean enabled) {
        try {
            return getCarControlManager().setAmbientLightEnabled(enabled);
        } catch (Exception e) {
            Log.e(TAG, "设置氛围灯开关失败", e);
            return false;
        }
    }
    
    /**
     * 检查氛围灯是否开启
     * 
     * @return 是否开启
     */
    @JavascriptInterface
    public boolean isAmbientLightEnabled() {
        try {
            return getCarControlManager().isAmbientLightEnabled();
        } catch (Exception e) {
            Log.e(TAG, "获取氛围灯状态失败", e);
            return false;
        }
    }
    
    /**
     * 设置氛围灯颜色
     * 
     * @param color 颜色值
     * @return 是否设置成功
     */
    @JavascriptInterface
    public boolean setAmbientLightColor(int color) {
        try {
            return getCarControlManager().setAmbientLightColor(color);
        } catch (Exception e) {
            Log.e(TAG, "设置氛围灯颜色失败", e);
            return false;
        }
    }
    
    /**
     * 获取氛围灯颜色
     * 
     * @return 颜色值
     */
    @JavascriptInterface
    public int getAmbientLightColor() {
        try {
            return getCarControlManager().getAmbientLightColor();
        } catch (Exception e) {
            Log.e(TAG, "获取氛围灯颜色失败", e);
            return 0;
        }
    }
    
    // ==================== 系统设置 ====================
    
    /**
     * 设置夜间模式
     * 
     * @param on 是否开启
     * @return 是否设置成功
     */
    @JavascriptInterface
    public boolean setNightMode(boolean on) {
        try {
            return getCarControlManager().setNightMode(on);
        } catch (Exception e) {
            Log.e(TAG, "设置夜间模式失败", e);
            return false;
        }
    }
    
    /**
     * 设置 WiFi 开关
     * 
     * @param enabled 是否开启
     * @return 是否设置成功
     */
    @JavascriptInterface
    public boolean setWifiEnabled(boolean enabled) {
        try {
            return getCarControlManager().setWifiEnabled(enabled);
        } catch (Exception e) {
            Log.e(TAG, "设置WiFi失败", e);
            return false;
        }
    }
    
    /**
     * 设置蓝牙开关
     * 
     * @param enabled 是否开启
     * @return 是否设置成功
     */
    @JavascriptInterface
    public boolean setBluetoothEnabled(boolean enabled) {
        try {
            return getCarControlManager().setBluetoothEnabled(enabled);
        } catch (Exception e) {
            Log.e(TAG, "设置蓝牙失败", e);
            return false;
        }
    }
    
    /**
     * 设置行驶中视频播放
     * 
     * @param enabled 是否开启
     * @return 是否设置成功
     */
    @JavascriptInterface
    public boolean setVideoWhileDriving(boolean enabled) {
        try {
            return getCarControlManager().setVideoWhileDriving(enabled);
        } catch (Exception e) {
            Log.e(TAG, "设置行驶视频失败", e);
            return false;
        }
    }
    
    /**
     * 检查行驶中视频播放是否开启
     * 
     * @return 是否开启
     */
    @JavascriptInterface
    public boolean isVideoWhileDrivingEnabled() {
        try {
            return getCarControlManager().isVideoWhileDrivingEnabled();
        } catch (Exception e) {
            Log.e(TAG, "获取行驶视频状态失败", e);
            return false;
        }
    }
    
    // ==================== 副屏控制 ====================
    
    /**
     * 设置副屏开关
     * 
     * @param enabled 是否开启
     * @return 是否设置成功
     */
    @JavascriptInterface
    public boolean setSecondaryScreenEnabled(boolean enabled) {
        try {
            return getCarControlManager().setSecondaryScreenEnabled(enabled);
        } catch (Exception e) {
            Log.e(TAG, "设置副屏失败", e);
            return false;
        }
    }
    
    /**
     * 检查副屏是否开启
     * 
     * @return 是否开启
     */
    @JavascriptInterface
    public boolean isSecondaryScreenEnabled() {
        try {
            return getCarControlManager().isSecondaryScreenEnabled();
        } catch (Exception e) {
            Log.e(TAG, "获取副屏状态失败", e);
            return false;
        }
    }
    
    // ==================== 语音控制 ====================
    
    /**
     * 设置语音播报开关
     * 
     * @param enabled 是否开启
     * @return 是否设置成功
     */
    @JavascriptInterface
    public boolean setSpeechEnabled(boolean enabled) {
        try {
            return getCarControlManager().setSpeechEnabled(enabled);
        } catch (Exception e) {
            Log.e(TAG, "设置语音播报失败", e);
            return false;
        }
    }
    
    /**
     * 检查语音播报是否开启
     * 
     * @return 是否开启
     */
    @JavascriptInterface
    public boolean isSpeechEnabled() {
        try {
            return getCarControlManager().isSpeechEnabled();
        } catch (Exception e) {
            Log.e(TAG, "获取语音播报状态失败", e);
            return false;
        }
    }
    
    /**
     * 发送语音控制指令
     * 
     * @param command 指令内容
     * @return 是否发送成功
     */
    @JavascriptInterface
    public boolean sendVoiceCommand(String command) {
        try {
            return getCarControlManager().sendVoiceCommand(command);
        } catch (Exception e) {
            Log.e(TAG, "发送语音指令失败", e);
            return false;
        }
    }
    
    // ==================== 方控按键 ====================
    
    /**
     * 发送上一首按键
     * 
     * @return 是否发送成功
     */
    @JavascriptInterface
    public boolean sendPrevTrack() {
        try {
            return getCarControlManager().sendPrevTrack();
        } catch (Exception e) {
            Log.e(TAG, "发送上一首失败", e);
            return false;
        }
    }
    
    /**
     * 发送下一首按键
     * 
     * @return 是否发送成功
     */
    @JavascriptInterface
    public boolean sendNextTrack() {
        try {
            return getCarControlManager().sendNextTrack();
        } catch (Exception e) {
            Log.e(TAG, "发送下一首失败", e);
            return false;
        }
    }
    
    // ==================== 状态读取 ====================
    
    /**
     * 检查车辆是否上锁
     * 
     * @return 是否上锁
     */
    @JavascriptInterface
    public boolean isVehicleLocked() {
        try {
            return getCarControlManager().isVehicleLocked();
        } catch (Exception e) {
            Log.e(TAG, "获取车辆锁状态失败", e);
            return false;
        }
    }
    
    /**
     * 检查屏幕是否开启
     * 
     * @return 是否开启
     */
    @JavascriptInterface
    public boolean isScreenOn() {
        try {
            return getCarControlManager().isScreenOn();
        } catch (Exception e) {
            Log.e(TAG, "获取屏幕状态失败", e);
            return false;
        }
    }
}
