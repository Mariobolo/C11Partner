package com.c11partner.desktop.bridge;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;

import android.webkit.JavascriptInterface;
import com.c11partner.desktop.MainActivity;
import com.c11partner.desktop.database.ComponentConfigDatabaseHelper;
import com.c11partner.desktop.database.WallpaperSettingsDatabaseHelper;
import com.c11partner.desktop.utils.LunarCalendarUtils;
import org.json.JSONObject;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * 系统设置Bridge类
 * 处理所有系统设置相关的JavaScript交互接口
 * 
 * 功能说明：
 * 1. 网络状态检测（WiFi、蓝牙）
 * 2. 系统设置保存（原桌面自启、开机问候语等）
 * 3. 农历日期获取
 * 4. 组件配置管理（桌面组件启用/禁用）
 */
public class SystemBridge extends BaseBridge {
    private static final String TAG = "SystemBridge";
    private MainActivity mActivity;
    private WallpaperSettingsDatabaseHelper wallpaperSettingsDbHelper;
    private ComponentConfigDatabaseHelper componentConfigDbHelper;
    /**
     * 构造函数
     *
     * @param context  应用上下文
     * @param activity MainActivity实例
     */
    public SystemBridge(Context context, MainActivity activity) {
        super(context, activity);
        this.mActivity = activity;
        this.wallpaperSettingsDbHelper = WallpaperSettingsDatabaseHelper.getInstance(context);
        this.componentConfigDbHelper = ComponentConfigDatabaseHelper.getInstance(context);
    }
    // ==================== 辅助方法 ====================
    
    /**
     * 安全执行数据库操作（统一错误处理）
     *
     * @param operation 数据库操作
     * @param operationName 操作名称（用于日志）
     * @return 操作是否成功
     */
    private boolean safeDbOperation(java.util.function.Supplier<Boolean> operation, String operationName) {
        try {
            boolean result = operation.get();
            logD(TAG, operationName + " 成功");
            return result;
        } catch (Exception e) {
            logE(TAG, operationName + " 失败", e);
            return false;
        }
    }
    
    /**
     * 安全执行数据库写操作（无返回值）
     *
     * @param operation 数据库操作
     * @param operationName 操作名称（用于日志）
     */
    private void safeDbWrite(Runnable operation, String operationName) {
        try {
            operation.run();
            logD(TAG, operationName + " 成功");
        } catch (Exception e) {
            logE(TAG, operationName + " 失败", e);
        }
    }
    
    /**
     * 安全获取数据（统一错误处理）
     *
     * @param supplier 数据提供者
     * @param defaultValue 默认值
     * @param operationName 操作名称（用于日志）
     * @param <T> 返回值类型
     * @return 获取到的数据或默认值
     */
    private <T> T safeGet(java.util.function.Supplier<T> supplier, T defaultValue, String operationName) {
        try {
            return supplier.get();
        } catch (Exception e) {
            logE(TAG, operationName + " 失败", e);
            return defaultValue;
        }
    }
    
    // ==================== 网络状态检测方法 ====================
    /**
     * 检查WiFi是否已连接
     *
     * @return WiFi连接状态
     */
    @JavascriptInterface
    public boolean isWifiConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false; // 无连接管理器，视为未连接
        }
        // 适配 Android 10 及以上版本（API 29+）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Network network = connectivityManager.getActiveNetwork();
            if (network == null) {
                return false; // 无活动网络
            }
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            return capabilities != null
                    && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) // 是 WiFi 网络
                    && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET); // 有互联网访问能力
        } else {
            // 适配 Android 10 以下版本（API < 29）
            android.net.NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return wifiInfo != null && wifiInfo.isConnected(); // 已连接到 WiFi 且网络可用
        }
    }
    /**
     * 检查蓝牙是否已连接
     *
     * @return 蓝牙连接状态
     */
    @JavascriptInterface
    public boolean isBluetoothConnected() {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
                logD(TAG, "Android版本过低，无法准确检测蓝牙连接状态");
                return false;
            }
            
            android.bluetooth.BluetoothManager bluetoothManager =
                    (android.bluetooth.BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
            if (bluetoothManager == null) {
                return false;
            }
            
            android.bluetooth.BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
            if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                return false;
            }
            
            // 优先检查A2DP和HEADSET配置文件（最常用的音频设备）
            int[] priorityProfiles = {
                android.bluetooth.BluetoothProfile.A2DP,
                android.bluetooth.BluetoothProfile.HEADSET
            };
            
            for (int profile : priorityProfiles) {
                try {
                    List<android.bluetooth.BluetoothDevice> connectedDevices = bluetoothManager.getConnectedDevices(profile);
                    if (connectedDevices != null && !connectedDevices.isEmpty()) {
                        logD(TAG, "检测到已连接的蓝牙设备，配置文件: " + profile);
                        return true;
                    }
                } catch (Exception e) {
                    logD(TAG, "检查配置文件 " + profile + " 时出错: " + e.getMessage());
                }
            }
            
            // 检查GATT配置文件
            try {
                List<android.bluetooth.BluetoothDevice> gattDevices = bluetoothManager.getConnectedDevices(android.bluetooth.BluetoothProfile.GATT);
                if (gattDevices != null && !gattDevices.isEmpty()) {
                    logD(TAG, "检测到已连接的GATT蓝牙设备");
                    return true;
                }
            } catch (Exception e) {
                logD(TAG, "检查GATT配置文件时出错: " + e.getMessage());
            }
            
            // 通过反射检查已配对设备的连接状态
            Set<android.bluetooth.BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
            if (bondedDevices != null) {
                for (android.bluetooth.BluetoothDevice device : bondedDevices) {
                    try {
                        Method isConnectedMethod = android.bluetooth.BluetoothDevice.class.getMethod("isConnected");
                        boolean isConnected = (boolean) isConnectedMethod.invoke(device);
                        if (isConnected) {
                            logD(TAG, "检测到已连接的配对设备: " + device.getName());
                            return true;
                        }
                    } catch (Exception e) {
                        // 反射失败，继续检查下一个设备
                    }
                }
            }
            
            logD(TAG, "没有检测到已连接的蓝牙设备");
            return false;
        } catch (Exception e) {
            logE(TAG, "检查蓝牙连接状态时出错", e);
            return false;
        }
    }
    // ==================== 系统设置保存方法 ====================
    /**
     * 保存原桌面自启设置
     *
     * @param enabled 是否启用
     * @return 是否保存成功
     */
    @JavascriptInterface
    public boolean saveSystemLauncherSetting(boolean enabled) {
        return safeDbOperation(() -> {
            wallpaperSettingsDbHelper.updateSystemLauncher(enabled);
            return true;
        }, "保存原桌面自启设置");
    }
    /**
     * 异步保存原桌面自启设置
     *
     * @param enabled    是否启用
     * @param callbackId 回调ID，用于JavaScript端识别回调
     */
    @JavascriptInterface
    public void saveSystemLauncherSettingAsync(final boolean enabled, final String callbackId) {
        new Thread(() -> {
            try {
                wallpaperSettingsDbHelper.updateSystemLauncher(enabled);
                // 在UI线程中执行JavaScript回调
                mActivity.runOnUiThread(() -> {
                    if (mActivity.webView != null) {
                        String javascript = String.format(
                                "javascript:window.handleSaveSystemLauncherSettingCallback('%s', %s)",
                                callbackId, "true");
                        mActivity.webView.loadUrl(javascript);
                    }
                });
            } catch (Exception e) {
                logE(TAG, "保存原桌面自启设置时出错", e);
                // 在UI线程中执行JavaScript回调
                mActivity.runOnUiThread(() -> {
                    if (mActivity.webView != null) {
                        String javascript = String.format(
                                "javascript:window.handleSaveSystemLauncherSettingCallback('%s', %s)",
                                callbackId, "false");
                        mActivity.webView.loadUrl(javascript);
                    }
                });
            }
        }).start();
    }
    /**
     * 保存开机问候语设置
     *
     * @param enabled 是否启用
     * @return 是否保存成功
     */
    @JavascriptInterface
    public boolean saveBootGreetingSetting(boolean enabled) {
        return safeDbOperation(() -> {
            wallpaperSettingsDbHelper.updateBootGreeting(enabled);
            return true;
        }, "保存开机问候语设置");
    }
    /**
     * 保存随机模式设置
     *
     * @param enabled 是否启用
     * @return 是否保存成功
     */
    @JavascriptInterface
    public boolean saveRandomModeSetting(boolean enabled) {
        return safeDbOperation(() -> {
            wallpaperSettingsDbHelper.updateRandomMode(enabled);
            return true;
        }, "保存随机模式设置");
    }
    /**
     * 保存指定模式设置
     *
     * @param enabled 是否启用
     * @return 是否保存成功
     */
    @JavascriptInterface
    public boolean saveSpecifiedModeSetting(boolean enabled) {
        return safeDbOperation(() -> {
            wallpaperSettingsDbHelper.updateSpecifiedMode(enabled);
            return true;
        }, "保存指定模式设置");
    }
    // ==================== 农历日期获取方法 ====================
    /**
     * 获取当前农历日期
     *
     * @return 格式化的农历日期字符串
     */
    @JavascriptInterface
    public String getLunarCalendar() {
        try {
            return LunarCalendarUtils.lunarCalendar();
        } catch (Exception e) {
            logE(TAG, "获取农历日期时出错", e);
            return "农历日期获取失败";
        }
    }
    // ==================== 屏幕亮度控制方法 ====================
    /**
     * 设置屏幕亮度
     *
     * @param brightness 亮度值（0-255）
     * @return 是否设置成功
     */
    @JavascriptInterface
    public boolean setScreenBrightness(int brightness) {
        try {
            // 限制亮度范围
            int safeBrightness = Math.max(0, Math.min(255, brightness));
            
            // 设置系统亮度
            android.provider.Settings.System.putInt(
                mContext.getContentResolver(),
                android.provider.Settings.System.SCREEN_BRIGHTNESS,
                safeBrightness
            );
            
            logD(TAG, "设置屏幕亮度: " + safeBrightness);
            return true;
        } catch (Exception e) {
            logE(TAG, "设置屏幕亮度失败", e);
            return false;
        }
    }
    /**
     * 获取当前屏幕亮度
     *
     * @return 亮度值（0-255）
     */
    @JavascriptInterface
    public int getScreenBrightness() {
        try {
            return android.provider.Settings.System.getInt(
                mContext.getContentResolver(),
                android.provider.Settings.System.SCREEN_BRIGHTNESS
            );
        } catch (Exception e) {
            logE(TAG, "获取屏幕亮度失败", e);
            return 128; // 默认中间亮度
        }
    }
    /**
     * 设置自动亮度调节
     *
     * @param enabled 是否启用自动亮度
     * @return 是否设置成功
     */
    @JavascriptInterface
    public boolean setAutoBrightness(boolean enabled) {
        try {
            android.provider.Settings.System.putInt(
                mContext.getContentResolver(),
                android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
                enabled ? 1 : 0
            );
            logD(TAG, "设置自动亮度: " + enabled);
            return true;
        } catch (Exception e) {
            logE(TAG, "设置自动亮度失败", e);
            return false;
        }
    }
    /**
     * 检查是否启用自动亮度
     *
     * @return 是否启用自动亮度
     */
    @JavascriptInterface
    public boolean isAutoBrightnessEnabled() {
        try {
            int mode = android.provider.Settings.System.getInt(
                mContext.getContentResolver(),
                android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE
            );
            return mode == 1;
        } catch (Exception e) {
            logE(TAG, "检查自动亮度状态失败", e);
            return false;
        }
    }
    // ==================== 屏幕超时设置方法 ====================
    /**
     * 设置屏幕超时时间
     *
     * @param seconds 超时时间（秒）
     * @return 是否设置成功
     */
    @JavascriptInterface
    public boolean setScreenTimeout(int seconds) {
        try {
            android.provider.Settings.System.putInt(
                mContext.getContentResolver(),
                android.provider.Settings.System.SCREEN_OFF_TIMEOUT,
                seconds * 1000 // 转换为毫秒
            );
            logD(TAG, "设置屏幕超时: " + seconds + "秒");
            return true;
        } catch (Exception e) {
            logE(TAG, "设置屏幕超时失败", e);
            return false;
        }
    }
    /**
     * 获取屏幕超时时间
     *
     * @return 超时时间（秒）
     */
    @JavascriptInterface
    public int getScreenTimeout() {
        try {
            int timeoutMs = android.provider.Settings.System.getInt(
                mContext.getContentResolver(),
                android.provider.Settings.System.SCREEN_OFF_TIMEOUT
            );
            return timeoutMs / 1000; // 转换为秒
        } catch (Exception e) {
            logE(TAG, "获取屏幕超时失败", e);
            return 60; // 默认60秒
        }
    }

    // ==================== 组件配置方法 ====================
    /**
     * 保存组件配置
     *
     * @param componentName 组件名称
     * @param isEnabled     是否启用
     * @return 是否保存成功
     */
    @JavascriptInterface
    public boolean saveComponentConfig(String componentName, boolean isEnabled) {
        try {
            if (componentConfigDbHelper != null) {
                long result = componentConfigDbHelper.saveOrUpdateComponentConfig(componentName, isEnabled);
                return result != -1;
            }
        } catch (Exception e) {
            logE(TAG, "保存组件配置时出错", e);
        }
        return false;
    }

    /**
     * 检查组件是否启用
     *
     * @param componentName 组件名称
     * @return 是否启用
     */
    @JavascriptInterface
    public boolean isComponentEnabled(String componentName) {
        try {
            if (componentConfigDbHelper != null) {
                return componentConfigDbHelper.isComponentEnabled(componentName);
            }
        } catch (Exception e) {
            logE(TAG, "获取组件配置时出错", e);
        }
        return true;
    }

    /**
     * 获取所有组件配置
     *
     * @return JSON格式的所有组件配置
     */
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
            logE(TAG, "获取所有组件配置时出错", e);
        }
        return "{}";
    }
    
    // ==================== 系统信息获取方法 ====================
    /**
     * 获取系统版本信息
     *
     * @return 系统版本信息JSON字符串
     */
    @JavascriptInterface
    public String getSystemVersionInfo() {
        try {
            JSONObject info = new JSONObject();
            info.put("androidVersion", Build.VERSION.RELEASE);
            info.put("sdkVersion", Build.VERSION.SDK_INT);
            info.put("deviceModel", Build.MODEL);
            info.put("deviceManufacturer", Build.MANUFACTURER);
            info.put("deviceBrand", Build.BRAND);
            info.put("deviceProduct", Build.PRODUCT);
            info.put("buildNumber", Build.DISPLAY);
            return info.toString();
        } catch (Exception e) {
            logE(TAG, "获取系统版本信息失败", e);
            return "{}";
        }
    }
    
    /**
     * 获取应用版本信息
     *
     * @return 应用版本信息JSON字符串
     */
    @JavascriptInterface
    public String getAppVersionInfo() {
        try {
            android.content.pm.PackageInfo pInfo = mContext.getPackageManager()
                .getPackageInfo(mContext.getPackageName(), 0);
            JSONObject info = new JSONObject();
            info.put("versionName", pInfo.versionName);
            info.put("versionCode", pInfo.versionCode);
            info.put("packageName", mContext.getPackageName());
            return info.toString();
        } catch (Exception e) {
            logE(TAG, "获取应用版本信息失败", e);
            return "{}";
        }
    }
    
    /**
     * 获取可用内存信息
     *
     * @return 内存信息JSON字符串
     */
    @JavascriptInterface
    public String getMemoryInfo() {
        try {
            android.app.ActivityManager activityManager = 
                (android.app.ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
            android.app.ActivityManager.MemoryInfo memoryInfo = 
                new android.app.ActivityManager.MemoryInfo();
            activityManager.getMemoryInfo(memoryInfo);
            
            JSONObject info = new JSONObject();
            info.put("availableMem", memoryInfo.availMem);
            info.put("totalMem", memoryInfo.totalMem);
            info.put("lowMemory", memoryInfo.lowMemory);
            info.put("threshold", memoryInfo.threshold);
            return info.toString();
        } catch (Exception e) {
            logE(TAG, "获取内存信息失败", e);
            return "{}";
        }
    }
    
    /**
     * 获取电池信息
     *
     * @return 电池信息JSON字符串
     */
    @JavascriptInterface
    public String getBatteryInfo() {
        try {
            Intent batteryIntent = mContext.registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            
            if (batteryIntent != null) {
                int level = batteryIntent.getIntExtra("level", -1);
                int scale = batteryIntent.getIntExtra("scale", -1);
                int status = batteryIntent.getIntExtra("status", -1);
                int plugged = batteryIntent.getIntExtra("plugged", -1);
                float batteryPct = level * 100 / (float) scale;
                
                JSONObject info = new JSONObject();
                info.put("level", batteryPct);
                info.put("isCharging", plugged > 0);
                info.put("status", status);
                info.put("plugged", plugged);
                return info.toString();
            }
        } catch (Exception e) {
            logE(TAG, "获取电池信息失败", e);
        }
        return "{}";
    }
    
    // ==================== 系统操作方法 ====================
    /**
     * 重启应用
     */
    @JavascriptInterface
    public void restartApp() {
        try {
            android.content.pm.PackageManager pm = mContext.getPackageManager();
            Intent intent = pm.getLaunchIntentForPackage(mContext.getPackageName());
            if (intent != null) {
                android.app.PendingIntent pendingIntent = android.app.PendingIntent.getActivity(
                    mContext, 0, intent,
                    android.app.PendingIntent.FLAG_CANCEL_CURRENT | 
                    android.app.PendingIntent.FLAG_IMMUTABLE);
                android.app.AlarmManager alarmManager = 
                    (android.app.AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(android.app.AlarmManager.RTC, 
                    System.currentTimeMillis() + 100, pendingIntent);
                System.exit(0);
            }
        } catch (Exception e) {
            logE(TAG, "重启应用失败", e);
        }
    }
    
    /**
     * 打开系统设置
     */
    @JavascriptInterface
    public void openSystemSettings() {
        try {
            Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } catch (Exception e) {
            logE(TAG, "打开系统设置失败", e);
        }
    }
    
    /**
     * 打开应用设置
     */
    @JavascriptInterface
    public void openAppSettings() {
        try {
            Intent intent = new Intent(
                android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(android.net.Uri.parse("package:" + mContext.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } catch (Exception e) {
            logE(TAG, "打开应用设置失败", e);
        }
    }
}
