package com.c11partner.desktop.adb;

import android.content.Context;
import android.util.Log;

public class AdbIntentForwarder {
    private static final String TAG = "AdbForwarder";
    
    private static Context mContext;
    private static final String ADB_LOCAL = "127.0.0.1";
    private static final int ADB_PORT = 5555;

    public static void setContext(Context context) {
        mContext = context;
    }

    public static boolean initAdbConnection() {
        try {
            AdbManager.setAppContext(mContext);
            AdbManager.initCrypto();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "ADB连接初始化失败：", e);
            return false;
        }
    }

    public static String executeAcAdbCmd(String cmd) {
        if (!initAdbConnection()) {
            return "ADB连接失败";
        }

        try {
            String fullCmd = cmd;
            boolean success = AdbManager.connectAndExecute(ADB_LOCAL, ADB_PORT, fullCmd);
            
            if (success) {
                Log.i(TAG, "命令执行成功：" + cmd);
                return "执行成功";
            } else {
                Log.e(TAG, "命令执行失败：" + cmd);
                return "执行失败";
            }
        } catch (Exception e) {
            Log.e(TAG, "命令执行异常：", e);
            return "执行异常：" + e.getMessage();
        }
    }

    public static String getAcStartState() {
        String cmd = "dumpsys bydauto_ac";
        return executeAcAdbCmd(cmd);
    }

    public static String toggleAirConditioning(boolean isOn) {
        String cmd = "am start -a android.intent.action.MAIN -c android.intent.category.HOME --ez ac_power " + (isOn ? "true" : "false");
        return executeAcAdbCmd(cmd);
    }

    public static String setAcTemperature(int temperature) {
        String cmd = "am start -a android.intent.action.MAIN -c android.intent.category.HOME --ei ac_temperature " + temperature;
        return executeAcAdbCmd(cmd);
    }
}
