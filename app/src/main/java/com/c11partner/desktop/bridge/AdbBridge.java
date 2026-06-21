package com.c11partner.desktop.bridge;
import android.content.Context;
import android.webkit.JavascriptInterface;
import com.c11partner.desktop.MainActivity;
import com.c11partner.desktop.adb.AdbCommandProcessor;
/**
 * ADB功能Bridge类
 * 处理所有ADB相关的JavaScript交互接口
 * 
 * 功能说明：
 * 1. USB调试授权触发
 * 2. 无线ADB授权
 * 3. ADB权限授予（三大核心权限）
 */
public class AdbBridge extends BaseBridge {
    private static final String TAG = "AdbBridge";
    private com.c11partner.desktop.adb.UsbDebugConnection usbDebugConnection;
    /**
     * 构造函数
     *
     * @param context  应用上下文
     * @param activity MainActivity实例
     */
    public AdbBridge(Context context, MainActivity activity) {
        super(context, activity);
        this.usbDebugConnection = new com.c11partner.desktop.adb.UsbDebugConnection(context);
    }
    // ==================== ADB授权相关方法 ====================
    /**
     * 触发USB调试授权
     * 当用户点击"一键ADB授权"按钮时调用此方法，会在设备上弹出允许USB调试的授权弹窗
     */
    @JavascriptInterface
    public void triggerUsbDebugAuthorization() {
        new Thread(() -> {
            try {
                if (usbDebugConnection != null) {
                    // 检查是否有USB设备连接
                    if (usbDebugConnection.hasUsbDevice()) {
                        // 获取USB设备信息用于日志记录
                        String usbDeviceInfo = usbDebugConnection.getUsbDeviceInfo();
                        logD(TAG, "检测到USB设备:\n" + usbDeviceInfo);
                    } else {
                        logD(TAG, "未检测到USB设备，尝试本地ADB连接");
                    }
                    
                    // 触发ADB调试授权（支持USB和本地连接）
                    boolean result = usbDebugConnection.triggerUsbDebugAuthorization();
                    
                    if (result) {
                        logD(TAG, "成功触发ADB调试授权弹窗");
                        showToastOnUiThread("正在请求ADB调试授权，请在设备上确认");
                    } else {
                        logE(TAG, "触发ADB调试授权失败", null);
                        showToastOnUiThread("触发ADB调试授权失败");
                    }
                } else {
                    logE(TAG, "USB调试连接未初始化", null);
                    showToastOnUiThread("ADB调试连接未初始化");
                }
            } catch (Exception e) {
                logE(TAG, "触发ADB调试授权时发生异常", e);
                showToastOnUiThread("触发ADB调试授权时发生错误: " + e.getMessage());
            }
        }).start();
    }
    
    /**
     * 触发本地ADB授权
     * 为本地ADB连接设计的授权方法
     */
    @JavascriptInterface
    public void triggerWirelessAdbAuthorization() {
        new Thread(() -> {
            try {
                logD(TAG, "开始触发本地ADB授权");
                
                // 初始化ADB加密密钥
                com.c11partner.desktop.adb.AdbManager.setAppContext(mContext);
                com.c11partner.desktop.adb.AdbManager.initCrypto();
                
                // 尝试连接到本地ADB服务
                int[] commonPorts = {5555, 5554, 5556, 5557, 5558, 5559};
                boolean connected = false;
                int connectedPort = -1;
                
                for (int port : commonPorts) {
                    try {
                        logD(TAG, "尝试连接到本地ADB端口: " + port);
                        boolean result = com.c11partner.desktop.adb.AdbManager.connect("127.0.0.1", port);
                        if (result) {
                            logD(TAG, "本地ADB连接成功，端口: " + port);
                            connected = true;
                            connectedPort = port;
                            break;
                        }
                    } catch (Exception e) {
                        logD(TAG, "连接端口 " + port + " 失败: " + e.getMessage());
                    }
                }
                
                if (connected) {
                    // 连接成功后执行授权命令
                    executeWirelessAdbAuthorization(connectedPort);
                    showToastOnUiThread("本地ADB连接成功，正在请求授权...");
                } else {
                    logE(TAG, "所有本地ADB端口都无法连接", null);
                    showToastOnUiThread("无法连接本地ADB，请检查:\n1. ADB调试是否已启用\n2. 设备是否已授权");
                }
            } catch (Exception e) {
                logE(TAG, "触发本地ADB授权时出错", e);
                showToastOnUiThread("本地ADB授权出错: " + e.getMessage());
            }
        }).start();
    }
    
    /**
     * 执行ADB权限授权命令
     * 零跑C11专用：授予READ_LOGS、DUMP、WRITE_SECURE_SETTINGS三大核心权限
     */
    @JavascriptInterface
    public void executeAdbPermissionGrant() {
        new Thread(() -> {
            try {
                String packageName = mContext.getPackageName();
                logD(TAG, "开始执行ADB权限授权，包名: " + packageName);
                
                // 使用ADB命令处理器执行权限授权
                AdbCommandProcessor processor = new AdbCommandProcessor(mContext);
                
                // 1. 授予READ_LOGS权限（日志监控）
                String command1 = "pm grant " + packageName + " android.permission.READ_LOGS";
                logD(TAG, "执行命令: " + command1);
                boolean result1 = processor.executeCommand(command1);
                
                // 2. 授予DUMP权限（系统状态获取）
                String command2 = "pm grant " + packageName + " android.permission.DUMP";
                logD(TAG, "执行命令: " + command2);
                boolean result2 = processor.executeCommand(command2);
                
                // 3. 授予WRITE_SECURE_SETTINGS权限（车控功能）
                String command3 = "pm grant " + packageName + " android.permission.WRITE_SECURE_SETTINGS";
                logD(TAG, "执行命令: " + command3);
                boolean result3 = processor.executeCommand(command3);
                
                // 显示结果
                final boolean allSuccess = result1 && result2 && result3;
                final String resultMsg;
                if (allSuccess) {
                    resultMsg = "三大核心权限授权成功！\nREAD_LOGS + DUMP + WRITE_SECURE_SETTINGS";
                    logD(TAG, "三大核心权限授权成功");
                } else {
                    resultMsg = "ADB权限授权结果：\n" +
                            "READ_LOGS: " + (result1 ? "✅" : "❌") + " " +
                            "DUMP: " + (result2 ? "✅" : "❌") + " " +
                            "WRITE_SECURE_SETTINGS: " + (result3 ? "✅" : "❌");
                    logE(TAG, "部分权限授权失败", null);
                }
                
                runOnUiThread(() -> {
                    showToastOnUiThread(resultMsg);
                });
                
            } catch (Exception e) {
                logE(TAG, "执行ADB权限授权时出错", e);
                showToastOnUiThread("执行ADB权限授权时出错: " + e.getMessage());
            }
        }).start();
    }
    
    // ==================== 系统操作方法 ====================
    /**
     * 打开最近任务（多任务视图）
     */
    @JavascriptInterface
    public void openRecentTasks() {
        new Thread(() -> {
            try {
                AdbCommandProcessor processor = new AdbCommandProcessor(mContext);
                processor.executeCommand("input keyevent 187");
            } catch (Exception e) {
                logE(TAG, "打开多任务时出错", e);
            }
        }).start();
    }
    
    /**
     * 打开最近任务（别名方法）
     */
    @JavascriptInterface
    public void openRecents() {
        openRecentTasks();
    }
    
    /**
     * 通过ADB设置默认桌面
     */
    @JavascriptInterface
    public void setDefaultDesktopViaAdb() {
        logD(TAG, "setDefaultDesktopViaAdb方法被调用");
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
                logE(TAG, "设置默认桌面时出错", e);
            }
        }).start();
    }
    
    // ==================== 私有辅助方法 ====================
    /**
     * 获取设备的WiFi IP地址
     */
    private String getDeviceIpAddress() {
        try {
            logD(TAG, "开始获取设备IP地址");
            
            // 方法1：尝试直接获取wlan0接口
            java.net.NetworkInterface networkInterface = java.net.NetworkInterface.getByName("wlan0");
            if (networkInterface == null) {
                logD(TAG, "wlan0接口不存在，尝试eth0");
                networkInterface = java.net.NetworkInterface.getByName("eth0");
            }
            
            if (networkInterface != null && networkInterface.isUp()) {
                logD(TAG, "找到网络接口: " + networkInterface.getName());
                java.util.Enumeration<java.net.InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    java.net.InetAddress address = addresses.nextElement();
                    if (!address.isLoopbackAddress() && address instanceof java.net.Inet4Address) {
                        String ip = address.getHostAddress();
                        logD(TAG, "获取到IP地址: " + ip);
                        return ip;
                    }
                }
            }
            
            logD(TAG, "方法1失败，尝试方法2");
            
            // 方法2：使用ConnectivityManager获取WiFi IP
            android.net.ConnectivityManager cm = (android.net.ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                android.net.Network activeNetwork = cm.getActiveNetwork();
                if (activeNetwork != null) {
                    android.net.NetworkCapabilities nc = cm.getNetworkCapabilities(activeNetwork);
                    if (nc != null && nc.hasTransport(android.net.NetworkCapabilities.TRANSPORT_WIFI)) {
                        java.net.InetAddress ip = cm.getLinkProperties(activeNetwork).getLinkAddresses().get(0).getAddress();
                        if (ip != null && !ip.isLoopbackAddress() && ip instanceof java.net.Inet4Address) {
                            String ipAddress = ip.getHostAddress();
                            logD(TAG, "通过ConnectivityManager获取到IP: " + ipAddress);
                            return ipAddress;
                        }
                    }
                }
            }
            
            logD(TAG, "方法2失败，返回null");
            
        } catch (Exception e) {
            logE(TAG, "获取设备IP地址失败", e);
        }
        return null;
    }
    

    
    /**
     * 执行本地ADB授权命令
     * 零跑C11专用：授予READ_LOGS、DUMP、WRITE_SECURE_SETTINGS三大核心权限
     */
    private void executeWirelessAdbAuthorization(int port) {
        logD(TAG, "executeWirelessAdbAuthorization 被调用，端口: " + port);
        
        new Thread(() -> {
            try {
                logD(TAG, "开始执行本地ADB授权，端口: " + port);
                
                String packageName = mContext.getPackageName();
                boolean allSuccess = true;
                
                // 1. 授予READ_LOGS权限（日志监控）
                String command1 = "pm grant " + packageName + " android.permission.READ_LOGS";
                logD(TAG, "尝试通过ADB授予权限: " + command1);
                boolean result1 = com.c11partner.desktop.adb.AdbManager.connectAndExecute("127.0.0.1", port, command1);
                if (!result1) {
                    logE(TAG, "READ_LOGS权限授权失败");
                    allSuccess = false;
                }
                
                // 2. 授予DUMP权限（系统状态获取）
                String command2 = "pm grant " + packageName + " android.permission.DUMP";
                logD(TAG, "尝试通过ADB授予权限: " + command2);
                boolean result2 = com.c11partner.desktop.adb.AdbManager.connectAndExecute("127.0.0.1", port, command2);
                if (!result2) {
                    logE(TAG, "DUMP权限授权失败");
                    allSuccess = false;
                }
                
                // 3. 授予WRITE_SECURE_SETTINGS权限（车控功能）
                String command3 = "pm grant " + packageName + " android.permission.WRITE_SECURE_SETTINGS";
                logD(TAG, "尝试通过ADB授予权限: " + command3);
                boolean result3 = com.c11partner.desktop.adb.AdbManager.connectAndExecute("127.0.0.1", port, command3);
                if (!result3) {
                    logE(TAG, "WRITE_SECURE_SETTINGS权限授权失败");
                    allSuccess = false;
                }
                
                if (allSuccess) {
                    logD(TAG, "三大核心权限授权成功");
                    showToastOnUiThread("三大核心权限授权成功！\nREAD_LOGS + DUMP + WRITE_SECURE_SETTINGS");
                } else {
                    logE(TAG, "部分权限授权失败");
                    String msg = "ADB权限授权结果：\n";
                    msg += "READ_LOGS: " + (result1 ? "✅" : "❌") + " ";
                    msg += "DUMP: " + (result2 ? "✅" : "❌") + " ";
                    msg += "WRITE_SECURE_SETTINGS: " + (result3 ? "✅" : "❌");
                    showToastOnUiThread(msg);
                }
                
            } catch (Exception e) {
                logE(TAG, "执行本地ADB授权命令时出错", e);
                showToastOnUiThread("执行授权命令时出错: " + e.getMessage());
            }
        }).start();
    }
}
