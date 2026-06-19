package com.c11partner.desktop.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.c11partner.desktop.LeapMotorCarState;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 自动化场景引擎
 * 管理自动化场景的配置和执行
 *
 * 触发条件 -> 检查配置 -> 执行动作
 *
 * @author C11Partner Team
 * @version 1.0.0
 */
public class AutomationEngine {

    private static final String TAG = "AutomationEngine";
    private static final String PREFS_NAME = "automation_settings";

    private Context mContext;
    private SharedPreferences mPrefs;
    private CarControlManager mCarControlManager;

    // 上次执行时间，用于防抖
    private long mLast360TriggerTime = 0;
    private long mLastVoiceTime = 0;
    private float mLastSpeed = 0;

    // 360全景触发冷却时间（毫秒）
    private static final long CAMERA_360_COOLDOWN = 3000;
    // 语音播报冷却时间（毫秒）
    private static final long VOICE_COOLDOWN = 5000;

    /**
     * 构造函数
     *
     * @param context 上下文
     */
    public AutomationEngine(Context context) {
        mContext = context;
        mPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        mCarControlManager = new CarControlManager(context);
    }

    /**
     * 检查自动化场景是否启用
     *
     * @param scenarioId 场景ID
     * @return 是否启用
     */
    public boolean isScenarioEnabled(String scenarioId) {
        return mPrefs.getBoolean(scenarioId, getDefaultEnabled(scenarioId));
    }

    /**
     * 获取场景默认启用状态
     *
     * @param scenarioId 场景ID
     * @return 默认是否启用
     */
    private boolean getDefaultEnabled(String scenarioId) {
        switch (scenarioId) {
            case "turnLight360":
            case "reverse360":
            case "doorOpenWarning":
                return true;
            default:
                return false;
        }
    }

    /**
     * 设置场景启用状态
     *
     * @param scenarioId 场景ID
     * @param enabled 是否启用
     */
    public void setScenarioEnabled(String scenarioId, boolean enabled) {
        mPrefs.edit().putBoolean(scenarioId, enabled).apply();
        Log.d(TAG, "场景 " + scenarioId + " 状态: " + (enabled ? "启用" : "禁用"));
    }

    /**
     * 获取所有场景配置JSON
     *
     * @return JSON字符串
     */
    public String getAllScenariosJson() {
        try {
            JSONObject json = new JSONObject();
            String[] scenarioIds = {
                "turnLight360", "reverse360", "lowSpeed360",
                "dGearVoice", "rGearVoice",
                "doorOpenLight", "reverseLowerVolume",
                "nightAutoLight", "lockAutoCloseWindow",
                "speedLimitWarning", "doorOpenWarning", "seatbeltReminder"
            };
            for (String id : scenarioIds) {
                json.put(id, isScenarioEnabled(id));
            }
            return json.toString();
        } catch (JSONException e) {
            Log.e(TAG, "生成场景配置JSON失败", e);
            return "{}";
        }
    }

    /**
     * 从JSON加载所有场景配置
     *
     * @param jsonStr JSON字符串
     */
    public void loadScenariosFromJson(String jsonStr) {
        try {
            JSONObject json = new JSONObject(jsonStr);
            JSONArray keys = json.names();
            if (keys == null) return;
            for (int i = 0; i < keys.length(); i++) {
                String key = keys.getString(i);
                boolean enabled = json.getBoolean(key);
                setScenarioEnabled(key, enabled);
            }
            Log.d(TAG, "场景配置加载完成");
        } catch (JSONException e) {
            Log.e(TAG, "加载场景配置失败", e);
        }
    }

    // ==================== 触发处理方法 ====================

    /**
     * 档位变化时触发
     *
     * @param oldGear 旧档位
     * @param newGear 新档位
     * @param carState 当前车辆状态
     */
    public void onGearChanged(int oldGear, int newGear, LeapMotorCarState carState) {
        Log.d(TAG, "档位变化: " + oldGear + " -> " + newGear);

        // R档自动开360
        if (isScenarioEnabled("reverse360") && newGear == LeapMotorCarState.GEAR_R) {
            triggerCamera360("R档自动触发");
        }

        // R档语音提示
        if (isScenarioEnabled("rGearVoice") && newGear == LeapMotorCarState.GEAR_R) {
            triggerVoicePrompt("倒车请注意");
        }

        // D档语音提示
        if (isScenarioEnabled("dGearVoice") && newGear == LeapMotorCarState.GEAR_D) {
            triggerVoicePrompt("前进档");
        }

        // 倒车自动降音量
        if (isScenarioEnabled("reverseLowerVolume")) {
            if (newGear == LeapMotorCarState.GEAR_R) {
                // 进入R档，降低音量（暂时记录，后续实现音量保存恢复）
                Log.d(TAG, "R档自动降音量（待实现）");
            } else if (oldGear == LeapMotorCarState.GEAR_R) {
                // 离开R档，恢复音量
                Log.d(TAG, "离开R档，恢复音量（待实现）");
            }
        }
    }

    /**
     * 转向灯变化时触发
     *
     * @param isLeft 是否左转向灯
     * @param state 状态（0=关，1=开）
     * @param carState 当前车辆状态
     */
    public void onTurnLightChanged(boolean isLeft, int state, LeapMotorCarState carState) {
        // 转向灯自动开360
        if (isScenarioEnabled("turnLight360") && state == 1) {
            triggerCamera360((isLeft ? "左" : "右") + "转向灯自动触发");
        }
    }

    /**
     * 车门变化时触发
     *
     * @param doorName 车门名称
     * @param state 状态（0=关，1=开）
     * @param carState 当前车辆状态
     */
    public void onDoorChanged(String doorName, int state, LeapMotorCarState carState) {
        // 开门自动亮灯
        if (isScenarioEnabled("doorOpenLight") && state == 1) {
            Log.d(TAG, "开门自动亮灯: " + doorName);
            mCarControlManager.setLowBeamLight(true);
        }

        // 开门预警
        if (isScenarioEnabled("doorOpenWarning") && state == 1) {
            // D档行驶中开门才预警
            if (carState.isDriveGear() && carState.getSpeed() > 5) {
                triggerVoicePrompt(doorName + "未关好");
            }
        }
    }

    /**
     * 车速变化时触发
     *
     * @param speed 当前车速
     * @param carState 当前车辆状态
     */
    public void onSpeedChanged(float speed, LeapMotorCarState carState) {
        // 降速自动开360
        if (isScenarioEnabled("lowSpeed360")) {
            if (speed < 10 && mLastSpeed >= 10) {
                triggerCamera360("低速自动触发");
            }
        }

        // 超速提醒
        if (isScenarioEnabled("speedLimitWarning")) {
            if (speed > 120 && mLastSpeed <= 120) {
                triggerVoicePrompt("您已超速，当前车速" + (int)speed + "公里");
            }
        }

        mLastSpeed = speed;
    }

    /**
     * 锁车状态变化时触发
     *
     * @param isLocked 是否锁车
     * @param carState 当前车辆状态
     */
    public void onLockStateChanged(boolean isLocked, LeapMotorCarState carState) {
        // 锁车自动关窗（待实现，需要车窗控制接口）
        if (isScenarioEnabled("lockAutoCloseWindow") && isLocked) {
            Log.d(TAG, "锁车自动关窗（待实现，需要车窗控制接口）");
        }
    }

    // ==================== 动作执行方法 ====================

    /**
     * 触发360全景
     *
     * @param reason 触发原因
     */
    private void triggerCamera360(String reason) {
        long now = System.currentTimeMillis();
        if (now - mLast360TriggerTime < CAMERA_360_COOLDOWN) {
            Log.d(TAG, "360全景冷却中，跳过: " + reason);
            return;
        }
        mLast360TriggerTime = now;
        Log.d(TAG, "触发360全景: " + reason);
        mCarControlManager.startCamera360();
    }

    /**
     * 触发语音播报
     *
     * @param text 播报文本
     */
    private void triggerVoicePrompt(String text) {
        long now = System.currentTimeMillis();
        if (now - mLastVoiceTime < VOICE_COOLDOWN) {
            Log.d(TAG, "语音播报冷却中，跳过: " + text);
            return;
        }
        mLastVoiceTime = now;
        Log.d(TAG, "语音播报: " + text);
        // TODO: 实现语音播报功能
        // 可以通过系统语音引擎或者零跑语音接口实现
    }

    /**
     * 重置冷却时间（用于测试）
     */
    public void resetCooldown() {
        mLast360TriggerTime = 0;
        mLastVoiceTime = 0;
    }
}
