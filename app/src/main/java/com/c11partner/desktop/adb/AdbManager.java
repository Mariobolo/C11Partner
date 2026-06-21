package com.c11partner.desktop.adb;

import android.content.Context;
import android.util.Log;

import com.tananaev.adblib.AdbBase64;
import com.tananaev.adblib.AdbConnection;
import com.tananaev.adblib.AdbCrypto;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * ADB管理器 - 管理ADB连接和认证
 */
public class AdbManager {
    private static final String TAG = "AdbManager";
    private static Context appContext;
    private static AdbCrypto crypto;

    /**
     * 设置应用上下文
     */
    public static void setAppContext(Context context) {
        appContext = context.getApplicationContext();
    }

    /**
     * 初始化ADB加密密钥
     */
    public static void initCrypto() {
        try {
            if (crypto == null) {
                // 尝试从存储中加载现有的密钥对
                try {
                    crypto = AdbCrypto.loadAdbKeyPair(
                        new MyAdbBase64(),
                        new java.io.File(appContext.getFilesDir(), "adb_key"),
                        new java.io.File(appContext.getFilesDir(), "adb_key.pub")
                    );
                } catch (Exception e) {
                    Log.d(TAG, "未找到现有密钥对，生成新的ADB密钥对: " + e.getMessage());
                }

                if (crypto == null) {
                    Log.d(TAG, "生成新的ADB密钥对");
                    crypto = AdbCrypto.generateAdbKeyPair(new MyAdbBase64());
                    // 保存新生成的密钥对
                    try {
                        crypto.saveAdbKeyPair(
                            new java.io.File(appContext.getFilesDir(), "adb_key"),
                            new java.io.File(appContext.getFilesDir(), "adb_key.pub")
                        );
                    } catch (Exception e) {
                        Log.e(TAG, "保存ADB密钥对失败", e);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "初始化ADB加密密钥失败", e);
        }
    }

    /**
     * 连接到ADB服务器
     */
    public static boolean connect(String host, int port) {
        AdbConnection connection = null;
        Socket socket = null;
        try {
            Log.d(TAG, "尝试连接到ADB服务器: " + host + ":" + port);
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), 3000); // 3秒超时
            connection = AdbConnection.create(socket, crypto);
            connection.connect();

            Log.d(TAG, "成功连接到ADB服务器");
            if (connection != null) {
                connection.close(); // 立即关闭连接，目的是触发授权
            }
            if (socket != null) {
                socket.close();
            }
            return true;
        } catch (Exception e) {
            Log.e(TAG, "连接ADB服务器失败: " + e.getMessage(), e);
            if (connection != null) {
                try {
                    connection.close();
                } catch (IOException ioException) {
                    Log.e(TAG, "关闭连接时出错", ioException);
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(TAG, "关闭Socket时出错", ioException);
                }
            }
            return false;
        }
    }

    /**
     * 连接到ADB服务器并执行命令
     */
    public static boolean connectAndExecute(String host, int port, String command) {
        AdbConnection connection = null;
        Socket socket = null;
        try {
            Log.d(TAG, "尝试连接到ADB服务器: " + host + ":" + port);
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), 3000); // 3秒超时
            connection = AdbConnection.create(socket, crypto);
            connection.connect();

            Log.d(TAG, "成功连接到ADB服务器");
            
            // 执行命令
            if (command != null && !command.isEmpty()) {
                Log.d(TAG, "执行ADB命令: " + command);
                Object stream = connection.open("shell:");
                
                // 使用反射来调用write方法，避免类型转换问题
                try {
                    java.lang.reflect.Method writeMethod = stream.getClass().getMethod("write", byte[].class);
                    writeMethod.invoke(stream, command.getBytes());
                    
                    java.lang.reflect.Method writeMethod2 = stream.getClass().getMethod("write", byte[].class);
                    writeMethod2.invoke(stream, "\n".getBytes());
                    
                    // 读取输出 - AdbStream可能没有read方法，尝试其他方式
                    try {
                        byte[] buffer = new byte[4096];
                        java.lang.reflect.Method readMethod = stream.getClass().getMethod("read", byte[].class);
                        int bytesRead = (int) readMethod.invoke(stream, buffer);
                        if (bytesRead > 0) {
                            String output = new String(buffer, 0, bytesRead);
                            Log.d(TAG, "命令输出: " + output);
                        }
                    } catch (Exception readEx) {
                        Log.d(TAG, "无法读取输出，可能AdbStream没有read方法: " + readEx.getMessage());
                    }
                    
                    java.lang.reflect.Method closeMethod = stream.getClass().getMethod("close");
                    closeMethod.invoke(stream);
                    
                    Log.d(TAG, "ADB命令执行完成，返回true");
                } catch (Exception e) {
                    Log.e(TAG, "执行ADB命令时出错", e);
                }
            }
            
            if (connection != null) {
                connection.close();
            }
            if (socket != null) {
                socket.close();
            }
            return true;
        } catch (Exception e) {
            Log.e(TAG, "连接ADB服务器失败: " + e.getMessage(), e);
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception closeEx) {
                    Log.e(TAG, "关闭连接时出错", closeEx);
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception closeEx) {
                    Log.e(TAG, "关闭Socket时出错", closeEx);
                }
            }
            return false;
        }
    }

    /**
     * ADB Base64编码实现
     */
    private static class MyAdbBase64 implements AdbBase64 {
        public String encodeToString(byte[] data) {
            return android.util.Base64.encodeToString(data, android.util.Base64.NO_WRAP);
        }

        public byte[] decodeString(String str) {
            return android.util.Base64.decode(str, android.util.Base64.NO_WRAP);
        }
    }
}