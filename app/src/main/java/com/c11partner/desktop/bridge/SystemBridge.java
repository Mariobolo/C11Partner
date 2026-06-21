package com.c11partner.desktop.bridge;
import android.content.Context;
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
                logD("Android版本过低，无法准确检测蓝牙连接状态");
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
                        logD("检测到已连接的蓝牙设备，配置文件: " + profile);
                        return true;
                    }
                } catch (Exception e) {
                    logD("检查配置文件 " + profile + " 时出错: " + e.getMessage());
                }
            }
            
            // 检查GATT配置文件
            try {
                List<android.bluetooth.BluetoothDevice> gattDevices = bluetoothManager.getConnectedDevices(android.bluetooth.BluetoothProfile.GATT);
                if (gattDevices != null && !gattDevices.isEmpty()) {
                    logD("检测到已连接的GATT蓝牙设备");
                    return true;
                }
            } catch (Exception e) {
                logD("检查GATT配置文件时出错: " + e.getMessage());
            }
            
            // 通过反射检查已配对设备的连接状态
            Set<android.bluetooth.BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
            if (bondedDevices != null) {
                for (android.bluetooth.BluetoothDevice device : bondedDevices) {
                    try {
                        Method isConnectedMethod = android.bluetooth.BluetoothDevice.class.getMethod("isConnected");
                        boolean isConnected = (boolean) isConnectedMethod.invoke(device);
                        if (isConnected) {
                            logD("检测到已连接的配对设备: " + device.getName());
                            return true;
                        }
                    } catch (Exception e) {
                        // 反射失败，继续检查下一个设备
                    }
                }
            }
            
            logD("没有检测到已连接的蓝牙设备");
            return false;
        } catch (Exception e) {
            logE("检查蓝牙连接状态时出错", e);
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
        try {
            wallpaperSettingsDbHelper.updateSystemLauncher(enabled);
            return true;
        } catch (Exception e) {
            logE("保存原桌面自启设置时出错", e);
            return false;
        }
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
                logE("保存原桌面自启设置时出错", e);
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
        try {
            wallpaperSettingsDbHelper.updateBootGreeting(enabled);
            return true;
        } catch (Exception e) {
            logE("保存开机问候语设置时出错", e);
            return false;
        }
    }
    /**
     * 保存随机模式设置
     *
     * @param enabled 是否启用
     * @return 是否保存成功
     */
    @JavascriptInterface
    public boolean saveRandomModeSetting(boolean enabled) {
        try {
            wallpaperSettingsDbHelper.updateRandomMode(enabled);
            return true;
        } catch (Exception e) {
            logE("保存随机模式设置时出错", e);
            return false;
        }
    }
    /**
     * 保存指定模式设置
     *
     * @param enabled 是否启用
     * @return 是否保存成功
     */
    @JavascriptInterface
    public boolean saveSpecifiedModeSetting(boolean enabled) {
        try {
            wallpaperSettingsDbHelper.updateSpecifiedMode(enabled);
            return true;
        } catch (Exception e) {
            logE("保存指定模式设置时出错", e);
            return false;
        }
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
            logE("获取农历日期时出错", e);
            return "农历日期获取失败";
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
            logE("保存组件配置时出错", e);
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
            logE("获取组件配置时出错", e);
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
            logE("获取所有组件配置时出错", e);
        }
        return "{}";
    }
}
