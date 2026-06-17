package com.c11partner.desktop;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * 零跑C11 360全景控制工具类
 * 基于实车日志分析确认的启动方式
 */
public class LeapMotorCamera360 {
    
    private static final String TAG = "LeapMotorCamera360";
    
    // 360全景启动Action（已通过实车验证）
    private static final String ACTION_CAMERA_AROUND = "com.leapmotor.camera_around";
    
    /**
     * 启动360全景
     * @param context 上下文
     * @param reason 触发原因（用于日志和提示）
     */
    public static void startCamera360(Context context, String reason) {
        try {
            Intent intent = new Intent(ACTION_CAMERA_AROUND);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            
            Log.i(TAG, "启动360全景成功: " + reason);
            Toast.makeText(context, "360全景已启动: " + reason, Toast.LENGTH_SHORT).show();
            
        } catch (Exception e) {
            Log.e(TAG, "启动360全景失败: " + e.getMessage(), e);
            Toast.makeText(context, "启动360全景失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * 检查是否支持360全景
     * @param context 上下文
     * @return 是否支持
     */
    public static boolean isSupported(Context context) {
        try {
            Intent intent = new Intent(ACTION_CAMERA_AROUND);
            return context.getPackageManager().resolveActivity(intent, 0) != null;
        } catch (Exception e) {
            Log.e(TAG, "检查360全景支持失败: " + e.getMessage());
            return false;
        }
    }
}
