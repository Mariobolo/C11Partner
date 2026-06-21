package com.c11partner.desktop.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.c11partner.desktop.MainActivity;

/**
 * 开机启动广播接收器
 * 监听系统启动完成事件，自动启动主应用
 */
public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootReceiver";
    
    /**
     * 接收广播事件
     * 当系统启动完成时，启动主应用Activity
     *
     * @param context 上下文
     * @param intent  广播意图
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d(TAG, "收到系统启动完成广播");
            
            // 创建启动Intent
            Intent launchIntent = new Intent(context, MainActivity.class);
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | 
                                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            
            try {
                // 延迟启动以确保系统完全初始化
                Thread.sleep(5000); // 等待5秒
                context.startActivity(launchIntent);
                Log.d(TAG, "已发送启动MainActivity的Intent");
            } catch (Exception e) {
                Log.e(TAG, "启动MainActivity时出错", e);
            }
        }
    }
}