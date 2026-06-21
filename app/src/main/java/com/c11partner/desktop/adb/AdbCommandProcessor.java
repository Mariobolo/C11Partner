package com.c11partner.desktop.adb;

import android.content.Context;
import android.util.Log;

/**
 * ADB命令处理器 - 统一处理ADB命令执行逻辑
 * 
 * 功能说明：
 * 1. 封装ADB初始化、连接与命令执行流程
 * 2. 实现端口缓存和加密初始化缓存优化执行速度
 * 3. 提供多端口尝试机制，提高连接成功率
 * 4. 支持单命令和批量命令执行
 * 
 * 主要特性：
 * - 端口缓存：记录上次成功的端口，优先使用
 * - 加密初始化缓存：避免重复初始化ADB加密
 * - 多端口尝试：依次尝试多个ADB端口，直到成功
 * - 本地回环地址：使用127.0.0.1本地连接，无需WiFi
 */
public class AdbCommandProcessor {
    private static final String TAG = "AdbCommandProcessor";
    private Context context;
    
    // ADB端口列表，按优先级排序
    private int[] portsToTry = {5555, 5554, 5556, 5557, 5558, 5559};
    
    // 缓存上次成功的端口，提高下次连接速度
    private static Integer lastSuccessfulPort = null;
    
    // 缓存加密初始化状态，避免重复初始化
    private static boolean cryptoInitialized = false;
    
    /**
     * 构造函数
     * 
     * @param context 应用上下文
     */
    public AdbCommandProcessor(Context context) {
        this.context = context;
    }
    
    /**
     * 执行ADB命令
     * 
     * 执行流程：
     * 1. 初始化ADB加密密钥（如果尚未初始化）
     * 2. 优先使用上次成功的端口尝试连接
     * 3. 如果失败，依次尝试其他端口
     * 4. 记录成功的端口供下次使用
     * 
     * @param command 要执行的ADB命令
     * @return 是否执行成功
     */
    public boolean executeCommand(String command) {
        try {
            // 初始化ADB加密密钥（只执行一次）
            initAdbCrypto();
            
            // 尝试连接到本地ADB服务并执行命令
            // 优先使用上次成功的端口
            if (lastSuccessfulPort != null) {
                try {
                    Log.d(TAG, "优先使用上次成功的端口: " + lastSuccessfulPort);
                    boolean result = AdbManager.connectAndExecute("127.0.0.1", lastSuccessfulPort, command);
                    if (result) {
                        Log.d(TAG, "ADB命令执行成功，端口: " + lastSuccessfulPort);
                        return true;
                    }
                } catch (Exception e) {
                    Log.d(TAG, "使用上次成功端口失败: " + e.getMessage());
                    lastSuccessfulPort = null; // 清除缓存的端口
                }
            }
            
            // 尝试其他端口
            for (int port : portsToTry) {
                try {
                    Log.d(TAG, "尝试连接到本地ADB端口: " + port);
                    boolean result = AdbManager.connectAndExecute("127.0.0.1", port, command);
                    if (result) {
                        Log.d(TAG, "ADB命令执行成功，端口: " + port);
                        lastSuccessfulPort = port; // 缓存成功的端口
                        return true;
                    }
                } catch (Exception e) {
                    Log.d(TAG, "连接端口 " + port + " 失败: " + e.getMessage());
                }
            }
            
            Log.e(TAG, "所有ADB端口都无法连接，命令执行失败: " + command);
            return false;
            
        } catch (Exception e) {
            Log.e(TAG, "执行ADB命令时出错: " + command, e);
            return false;
        }
    }
    
    /**
     * 执行多个ADB命令
     * 
     * 依次执行多个命令，只有所有命令都成功才返回true
     * 
     * @param commands 要执行的ADB命令数组
     * @return 所有命令是否都执行成功
     */
    public boolean executeCommands(String[] commands) {
        boolean allSuccess = true;
        
        for (String command : commands) {
            boolean result = executeCommand(command);
            if (!result) {
                allSuccess = false;
            }
        }
        
        return allSuccess;
    }
    
    /**
     * 初始化ADB加密密钥（只执行一次）
     * 
     * 使用静态变量缓存初始化状态，避免重复初始化
     * 提高ADB命令执行性能
     */
    private void initAdbCrypto() {
        if (!cryptoInitialized) {
            try {
                AdbManager.setAppContext(context);
                AdbManager.initCrypto();
                Log.d(TAG, "ADB加密密钥初始化成功");
                cryptoInitialized = true;
            } catch (Exception e) {
                Log.e(TAG, "ADB加密密钥初始化失败", e);
            }
        }
    }
    
    /**
     * 测试ADB连接
     * @return 是否能连接到ADB服务
     */
    public boolean testAdbConnection() {
        try {
            // 初始化ADB加密密钥
            initAdbCrypto();
            
            // 优先使用上次成功的端口
            if (lastSuccessfulPort != null) {
                try {
                    Log.d(TAG, "测试上次成功的端口: " + lastSuccessfulPort);
                    boolean connected = AdbManager.connect("127.0.0.1", lastSuccessfulPort);
                    if (connected) {
                        Log.d(TAG, "ADB连接测试成功，端口: " + lastSuccessfulPort);
                        return true;
                    }
                } catch (Exception e) {
                    Log.d(TAG, "测试上次成功端口失败: " + e.getMessage());
                    lastSuccessfulPort = null;
                }
            }
            
            // 尝试其他端口
            for (int port : portsToTry) {
                try {
                    Log.d(TAG, "测试连接到本地ADB端口: " + port);
                    boolean connected = AdbManager.connect("127.0.0.1", port);
                    if (connected) {
                        Log.d(TAG, "ADB连接测试成功，端口: " + port);
                        lastSuccessfulPort = port; // 缓存成功的端口
                        return true;
                    }
                } catch (Exception e) {
                    Log.d(TAG, "测试连接端口 " + port + " 失败: " + e.getMessage());
                }
            }
            
            Log.e(TAG, "ADB连接测试失败");
            return false;
            
        } catch (Exception e) {
            Log.e(TAG, "ADB连接测试时出错", e);
            return false;
        }
    }
    
    /**
     * 清除缓存的连接信息
     */
    public void clearCache() {
        lastSuccessfulPort = null;
        cryptoInitialized = false;
        Log.d(TAG, "已清除ADB连接缓存");
    }
}