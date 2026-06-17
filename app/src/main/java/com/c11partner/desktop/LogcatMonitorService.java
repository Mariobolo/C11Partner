package com.c11partner.desktop;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 零跑C11日志监控服务
 * 实时抓取logcat并解析车辆CAN信号
 * 
 * 基于实车日志分析的关键TAG:
 * - C11CarSomeIp: CAN信号（档位、车门、天窗、锁车）
 * - AroundService: 转向灯信号
 * - C11CarXml: 灯光、空调、车速
 */
public class LogcatMonitorService extends Service {
    
    private static final String TAG = "LogcatMonitorService";
    
    // 日志过滤关键词
    private static final String[] FILTER_TAGS = {
        "C11CarSomeIp",
        "AroundService",
        "C11CarXml"
    };
    
    // CAN信号EventId定义（来自实车日志分析）
    private static final int EVENT_GEAR = 1110;           // 档位
    private static final int EVENT_FRONT_LEFT_DOOR = 9123;  // 左前门
    private static final int EVENT_FRONT_RIGHT_DOOR = 9124; // 右前门
    private static final int EVENT_REAR_LEFT_DOOR = 9125;   // 左后门
    private static final int EVENT_REAR_RIGHT_DOOR = 9126;  // 右后门
    private static final int EVENT_TRUNK = 9127;           // 后备箱
    private static final int EVENT_HOOD = 9128;            // 前机盖
    private static final int EVENT_SUNROOF = 21201;        // 天窗
    private static final int EVENT_SUNSHADE = 21207;       // 遮阳帘
    private static final int EVENT_LOCK = 1200;            // 锁车
    
    // Binder
    private final IBinder binder = new LogcatMonitorBinder();
    
    // 车辆状态
    private LeapMotorCarState carState = new LeapMotorCarState();
    
    // 监听器列表
    private List<CarStateListener> listeners = new ArrayList<>();
    
    // 日志读取线程
    private Thread logcatThread;
    private volatile boolean isRunning = false;
    
    // 360全景触发冷却时间（防止频繁触发）
    private static final long COOLDOWN_360_MS = 3000; // 3秒冷却
    private long last360TriggerTime = 0;
    
    // Handler用于主线程回调
    private Handler mainHandler;
    
    public class LogcatMonitorBinder extends Binder {
        public LogcatMonitorService getService() {
            return LogcatMonitorService.this;
        }
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        mainHandler = new Handler(Looper.getMainLooper());
        Log.i(TAG, "日志监控服务创建");
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startLogcatMonitoring();
        return START_STICKY;
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLogcatMonitoring();
        Log.i(TAG, "日志监控服务停止");
    }
    
    /**
     * 启动日志监控
     */
    private void startLogcatMonitoring() {
        if (isRunning) {
            Log.w(TAG, "日志监控已在运行");
            return;
        }
        
        isRunning = true;
        logcatThread = new Thread(new LogcatReaderRunnable());
        logcatThread.start();
        Log.i(TAG, "日志监控已启动");
    }
    
    /**
     * 停止日志监控
     */
    private void stopLogcatMonitoring() {
        isRunning = false;
        if (logcatThread != null) {
            logcatThread.interrupt();
            logcatThread = null;
        }
    }
    
    /**
     * 日志读取线程
     */
    private class LogcatReaderRunnable implements Runnable {
        @Override
        public void run() {
            Process process = null;
            BufferedReader reader = null;
            
            try {
                // 构建logcat命令，过滤关键TAG
                StringBuilder cmd = new StringBuilder("logcat -v time ");
                for (String tag : FILTER_TAGS) {
                    cmd.append(tag).append(":D ");
                }
                cmd.append("*:S"); // 静默其他日志
                
                Log.d(TAG, "执行命令: " + cmd.toString());
                process = Runtime.getRuntime().exec(cmd.toString());
                
                reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
                );
                
                String line;
                while (isRunning && (line = reader.readLine()) != null) {
                    parseLogLine(line);
                }
                
            } catch (Exception e) {
                Log.e(TAG, "日志读取错误: " + e.getMessage(), e);
            } finally {
                try {
                    if (reader != null) reader.close();
                    if (process != null) process.destroy();
                } catch (Exception e) {
                    Log.e(TAG, "清理资源错误: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * 解析单行日志
     */
    private void parseLogLine(String line) {
        try {
            // 解析CAN信号日志
            // 格式: D/C11CarSomeIp: onMessage  eventId: 1110 value: 3
            if (line.contains("C11CarSomeIp") && line.contains("onMessage")) {
                parseCanSignal(line);
            }
            
            // 解析转向灯信号
            // 格式: I/AroundService: dealTurnLeftLight mLeftLightSts 1
            else if (line.contains("AroundService")) {
                parseTurnLightSignal(line);
            }
            
            // 解析车速信号
            // 格式: D/C11CarXml: node_name : speed  setTextContent: 10
            else if (line.contains("C11CarXml") && line.contains("speed")) {
                parseSpeedSignal(line);
            }
            
        } catch (Exception e) {
            Log.e(TAG, "解析日志行错误: " + e.getMessage());
        }
    }
    
    /**
     * 解析CAN信号日志
     */
    private void parseCanSignal(String line) {
        try {
            // 提取eventId
            int eventIdPos = line.indexOf("eventId:");
            int valuePos = line.indexOf("value:");
            
            if (eventIdPos > 0 && valuePos > 0) {
                String eventIdStr = line.substring(eventIdPos + 8, valuePos).trim();
                String valueStr = line.substring(valuePos + 6).trim();
                
                // 提取数字
                int eventId = extractInt(eventIdStr);
                int value = extractInt(valueStr);
                
                processCanSignal(eventId, value);
            }
        } catch (Exception e) {
            Log.e(TAG, "解析CAN信号错误: " + e.getMessage());
        }
    }
    
    /**
     * 处理CAN信号
     */
    private void processCanSignal(int eventId, int value) {
        int oldGear = carState.getGear();
        
        switch (eventId) {
            case EVENT_GEAR:
                carState.setGear(value);
                notifyGearChanged(oldGear, value);
                // R挡触发360全景
                if (value == LeapMotorCarState.GEAR_R) {
                    trigger360IfNeeded("挂入R挡");
                }
                break;
                
            case EVENT_FRONT_LEFT_DOOR:
                carState.setFrontLeftDoor(value);
                notifyDoorChanged("左前门", value);
                break;
                
            case EVENT_FRONT_RIGHT_DOOR:
                carState.setFrontRightDoor(value);
                notifyDoorChanged("右前门", value);
                break;
                
            case EVENT_REAR_LEFT_DOOR:
                carState.setRearLeftDoor(value);
                notifyDoorChanged("左后门", value);
                break;
                
            case EVENT_REAR_RIGHT_DOOR:
                carState.setRearRightDoor(value);
                notifyDoorChanged("右后门", value);
                break;
                
            case EVENT_TRUNK:
                carState.setTrunkDoor(value);
                notifyDoorChanged("后备箱", value);
                break;
                
            case EVENT_HOOD:
                carState.setHoodDoor(value);
                notifyDoorChanged("前机盖", value);
                break;
                
            case EVENT_SUNROOF:
                carState.setSunroof(value);
                break;
                
            case EVENT_SUNSHADE:
                carState.setSunshade(value);
                break;
                
            case EVENT_LOCK:
                carState.setLockState(value);
                notifyLockStateChanged(value == 1);
                break;
        }
    }
    
    /**
     * 解析转向灯信号
     */
    private void parseTurnLightSignal(String line) {
        try {
            // 左转向灯
            if (line.contains("dealTurnLeftLight")) {
                int state = extractIntAfter(line, "mLeftLightSts");
                carState.setLeftTurnLight(state);
                notifyTurnLightChanged(true, state);
                if (state == 1) {
                    trigger360IfNeeded("左转向灯开启");
                }
            }
            // 右转向灯
            else if (line.contains("dealTurnRightLight")) {
                int state = extractIntAfter(line, "mRightLightSts");
                carState.setRightTurnLight(state);
                notifyTurnLightChanged(false, state);
                if (state == 1) {
                    trigger360IfNeeded("右转向灯开启");
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "解析转向灯信号错误: " + e.getMessage());
        }
    }
    
    /**
     * 解析车速信号
     */
    private void parseSpeedSignal(String line) {
        try {
            if (line.contains("setTextContent:")) {
                int pos = line.indexOf("setTextContent:");
                String speedStr = line.substring(pos + 15).trim();
                float speed = Float.parseFloat(speedStr);
                carState.setSpeed(speed);
                notifySpeedChanged(speed);
            }
        } catch (Exception e) {
            Log.e(TAG, "解析车速信号错误: " + e.getMessage());
        }
    }
    
    /**
     * 触发360全景（带冷却）
     */
    private void trigger360IfNeeded(String reason) {
        long now = System.currentTimeMillis();
        if (now - last360TriggerTime > COOLDOWN_360_MS) {
            last360TriggerTime = now;
            notifyNeedStart360(reason);
            Log.i(TAG, "触发360全景: " + reason);
        }
    }
    
    // ========== 工具方法 ==========
    
    private int extractInt(String str) {
        StringBuilder num = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                num.append(c);
            } else if (num.length() > 0) {
                break;
            }
        }
        return num.length() > 0 ? Integer.parseInt(num.toString()) : 0;
    }
    
    private int extractIntAfter(String line, String keyword) {
        int pos = line.indexOf(keyword);
        if (pos > 0) {
            return extractInt(line.substring(pos + keyword.length()));
        }
        return 0;
    }
    
    // ========== 监听器管理 ==========
    
    public void addListener(CarStateListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    public void removeListener(CarStateListener listener) {
        listeners.remove(listener);
    }
    
    public LeapMotorCarState getCurrentState() {
        return carState;
    }
    
    // ========== 监听器通知 ==========
    
    private void notifyGearChanged(final int oldGear, final int newGear) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                for (CarStateListener listener : listeners) {
                    listener.onGearChanged(oldGear, newGear);
                }
            }
        });
    }
    
    private void notifyTurnLightChanged(final boolean isLeft, final int state) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                for (CarStateListener listener : listeners) {
                    listener.onTurnLightChanged(isLeft, state);
                }
            }
        });
    }
    
    private void notifyDoorChanged(final String doorName, final int state) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                for (CarStateListener listener : listeners) {
                    listener.onDoorChanged(doorName, state);
                }
            }
        });
    }
    
    private void notifySpeedChanged(final float speed) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                for (CarStateListener listener : listeners) {
                    listener.onSpeedChanged(speed);
                }
            }
        });
    }
    
    private void notifyLockStateChanged(final boolean isLocked) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                for (CarStateListener listener : listeners) {
                    listener.onLockStateChanged(isLocked);
                }
            }
        });
    }
    
    private void notifyNeedStart360(final String reason) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                for (CarStateListener listener : listeners) {
                    listener.onNeedStart360(reason);
                }
            }
        });
    }
}
