package com.c11partner.desktop.adb;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

import java.util.Map;

/**
 * USB调试连接类 - 处理USB调试连接和授权
 */
public class UsbDebugConnection {
    private static final String TAG = "UsbDebugConnection";
    private Context context;
    private UsbManager usbManager;

    public UsbDebugConnection(Context context) {
        this.context = context;
        this.usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
    }

    /**
     * 检查是否有USB设备连接
     */
    public boolean hasUsbDevice() {
        Map<String, UsbDevice> deviceList = usbManager.getDeviceList();
        return deviceList != null && !deviceList.isEmpty();
    }

    /**
     * 获取USB设备信息
     */
    public String getUsbDeviceInfo() {
        StringBuilder sb = new StringBuilder();
        Map<String, UsbDevice> deviceList = usbManager.getDeviceList();
        
        if (deviceList != null && !deviceList.isEmpty()) {
            for (Map.Entry<String, UsbDevice> entry : deviceList.entrySet()) {
                UsbDevice device = entry.getValue();
                sb.append("设备名: ").append(device.getDeviceName()).append("\n");
                sb.append("产品ID: ").append(device.getProductId()).append("\n");
                sb.append("厂商ID: ").append(device.getVendorId()).append("\n");
                sb.append("设备协议: ").append(device.getDeviceProtocol()).append("\n");
                sb.append("设备类: ").append(device.getDeviceClass()).append("\n");
                sb.append("设备子类: ").append(device.getDeviceSubclass()).append("\n");
                sb.append("序列号: ").append(device.getSerialNumber()).append("\n");
                sb.append("---\n");
            }
        } else {
            sb.append("未检测到USB设备");
        }
        
        return sb.toString();
    }

    /**
     * 触发USB调试授权弹窗
     * 通过尝试连接本地ADB服务来触发系统弹出授权弹窗
     */
    public boolean triggerUsbDebugAuthorization() {
        try {
            // 初始化ADB加密
            AdbManager.setAppContext(context);
            AdbManager.initCrypto();

            // 尝试连接到本地ADB服务（通常是localhost:5555）
            // 对于本机ADB，我们使用127.0.0.1或localhost
            boolean connected = false;
            int[] portsToTry = {5555, 5554, 5556, 5557, 5558, 5559};

            for (int port : portsToTry) {
                if (AdbManager.connect("127.0.0.1", port)) {
                    connected = true;
                    Log.d(TAG, "成功连接到本地ADB服务，端口: " + port);
                    break;
                } else {
                    Log.d(TAG, "连接本地ADB服务失败，端口: " + port);
                }
            }

            if (!connected) {
                Log.e(TAG, "无法连接到本地ADB服务，尝试执行shell命令触发授权弹窗");
                // 如果无法直接连接ADB，尝试通过其他方式触发授权
                return executeShellCommandToTriggerAuthorization();
            }

            return true;
        } catch (Exception e) {
            Log.e(TAG, "触发USB调试授权时出错", e);
            return false;
        }
    }

    /**
     * 通过执行shell命令触发授权弹窗（备用方法）
     */
    private boolean executeShellCommandToTriggerAuthorization() {
        try {
            // 尝试使用Runtime执行adb命令，这可能会触发系统授权弹窗
            String packageName = context.getPackageName();
            Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", 
                "adb shell pm grant " + packageName + " android.permission.DUMP"});
            
            int exitCode = process.waitFor();
            Log.d(TAG, "执行shell命令退出码: " + exitCode);
            return exitCode == 0;
        } catch (Exception e) {
            Log.e(TAG, "执行shell命令触发授权时出错", e);
            return false;
        }
    }

    /**
     * 获取当前连接的USB设备数量
     */
    public int getConnectedUsbDeviceCount() {
        Map<String, UsbDevice> deviceList = usbManager.getDeviceList();
        return deviceList != null ? deviceList.size() : 0;
    }

    /**
     * 检查USB调试是否已启用
     * 通过尝试连接本地ADB服务来间接判断USB调试是否已启用
     */
    public boolean isUsbDebuggingEnabled() {
        try {
            // 初始化ADB加密
            AdbManager.setAppContext(context);
            AdbManager.initCrypto();

            // 尝试连接到本地ADB服务
            return AdbManager.connect("127.0.0.1", 5555);
        } catch (Exception e) {
            Log.e(TAG, "检查USB调试状态时出错", e);
            return false;
        }
    }
}