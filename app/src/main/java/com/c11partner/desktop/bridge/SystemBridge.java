package com.c11partner.desktop.bridge;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.util.Log;
import android.webkit.JavascriptInterface;
import com.c11partner.desktop.MainActivity;
import com.c11partner.desktop.database.WallpaperSettingsDatabaseHelper;
import com.c11partner.desktop.utils.LunarCalendarUtils;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
/**
 * 系统设置Bridge类
 * 处理所有系统设置相关的JavaScript交互接口
 * 
 * 功能说明：
 * 1. 网络状态检测（WiFi、蓝牙）
 * 2. 系统设置保存（原桌面自启、开机问候语等）
 * 3. 农历日期获取
 */
public class SystemBridge extends BaseBridge {
    private static final String TAG = "SystemBridge";
    private MainActivity mActivity;
    private WallpaperSettingsDatabaseHelper wallpaperSettingsDbHelper;
    /**
     * 构造函数
     *
     * @param context  应用上下文
     * @param activity MainActivity实例
     */
    public SystemBridge(Context context, MainActivity activity) {
        super(context);
        this.mActivity = activity;
        this.wallpaperSettingsDbHelper = WallpaperSettingsDatabaseHelper.getInstance(context);
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                android.bluetooth.BluetoothManager bluetoothManager =
                        (android.bluetooth.BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
                if (bluetoothManager != null) {
                    android.bluetooth.BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
                    // 检查蓝牙是否启用
                    if (bluetoothAdapter != null) {
                        if (bluetoothAdapter.isEnabled()) {
                            // 检查所有可能的蓝牙配置文件连接
                            int[] profiles = { 
                                android.bluetooth.BluetoothProfile.GATT,
                                android.bluetooth.BluetoothProfile.GATT_SERVER,
                                android.bluetooth.BluetoothProfile.A2DP,
                                android.bluetooth.BluetoothProfile.HEADSET,
                                android.bluetooth.BluetoothProfile.HEALTH
                            };
                            
                            for (int profile : profiles) {
                                try {
                                    List<android.bluetooth.BluetoothDevice> connectedDevices = 
                                            bluetoothManager.getConnectedDevices(profile);
                                    if (connectedDevices != null && !connectedDevices.isEmpty()) {
                                        Log.d(TAG, "检测到已连接的设备，配置文件: " + profile + ", 数量: " + connectedDevices.size());
                                        for (android.bluetooth.BluetoothDevice device : connectedDevices) {
                                            Log.d(TAG, "已连接设备: " + device.getName() + " (" + device.getAddress() + ")");
                                        }
                                        return true;
                                    } else {
                                        Log.d(TAG, "配置文件 " + profile + " 没有已连接的设备");
                                    }
                                } catch (Exception e) {
                                    Log.d(TAG, "检查配置文件 " + profile + " 时出错: " + e.getMessage());
                                }
                            }
                            
                            // 尝试使用传统方法检查已配对设备的连接状态
                            try {
                                Set<android.bluetooth.BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
                                Log.d(TAG, "已配对设备数量: " + (bondedDevices != null ? bondedDevices.size() : 0));
                                
                                if (bondedDevices != null) {
                                    for (android.bluetooth.BluetoothDevice device : bondedDevices) {
                                        Log.d(TAG, "已配对设备: " + device.getName() + " (" + device.getAddress() + "), 状态: " + device.getBondState());
                                        
                                        // 尝试检查设备是否处于连接状态
                                        try {
                                            Method isConnectedMethod = android.bluetooth.BluetoothDevice.class.getMethod("isConnected");
                                            boolean isConnected = (boolean) isConnectedMethod.invoke(device);
                                            Log.d(TAG, "设备 " + device.getName() + " 连接状态: " + isConnected);
                                            if (isConnected) {
                                                return true;
                                            }
                                        } catch (Exception e) {
                                            Log.d(TAG, "无法检查设备连接状态: " + e.getMessage());
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                Log.d(TAG, "检查已配对设备时出错: " + e.getMessage());
                            }
                            
                            // 如果没有检测到任何连接的设备，返回false
                            Log.d(TAG, "没有检测到已连接的蓝牙设备");
                            return false;
                        }
                    }
                }
            } else {
                // 对于较老的Android版本，返回false，因为无法准确检测连接状态
                Log.d(TAG, "Android版本过低，无法准确检测蓝牙连接状态");
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "检查蓝牙连接状态时出错", e);
        }
        Log.d(TAG, "蓝牙连接状态检查完成，返回false");
        return false;
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
            Log.e(TAG, "保存原桌面自启设置时出错", e);
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    wallpaperSettingsDbHelper.updateSystemLauncher(enabled);
                    // 在UI线程中执行JavaScript回调
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mActivity.webView != null) {
                                String javascript = String.format(
                                        "javascript:window.handleSaveSystemLauncherSettingCallback('%s', %s)",
                                        callbackId, "true");
                                mActivity.webView.loadUrl(javascript);
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "保存原桌面自启设置时出错", e);
                    // 在UI线程中执行JavaScript回调
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mActivity.webView != null) {
                                String javascript = String.format(
                                        "javascript:window.handleSaveSystemLauncherSettingCallback('%s', %s)",
                                        callbackId, "false");
                                mActivity.webView.loadUrl(javascript);
                            }
                        }
                    });
                }
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
            Log.e(TAG, "保存开机问候语设置时出错", e);
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
            Log.e(TAG, "保存随机模式设置时出错", e);
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
            Log.e(TAG, "保存指定模式设置时出错", e);
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
            Log.e(TAG, "获取农历日期时出错", e);
            return "农历日期获取失败";
        }
    }
}
