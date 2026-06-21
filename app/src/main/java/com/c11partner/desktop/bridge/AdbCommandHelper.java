package com.c11partner.desktop.bridge;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.c11partner.desktop.adb.AdbCommandProcessor;

/**
 * ADB命令执行助手类
 * 提供通过ADB命令执行系统功能的方法
 */
public class AdbCommandHelper {
    private static final String TAG = "AdbCommandHelper";
    private Context mContext;
    
    public AdbCommandHelper(Context context) {
        this.mContext = context;
    }
    
    /**
     * 通过ADB命令打开系统多任务页面
     */
    public void openRecentsViaAdb() {
        new Thread(() -> {
            try {
                Log.d(TAG, "通过ADB命令打开多任务页面");
                
                // 使用ADB命令处理器执行命令
                AdbCommandProcessor processor = new AdbCommandProcessor(mContext);
                
                // 使用input keyevent命令发送最近任务按键(187)
                String command = "input keyevent 187";
                boolean success = processor.executeCommand(command);
                
                if (success) {
                    Log.d(TAG, "input keyevent 187 执行成功");
                    showToast("已通过ADB命令打开多任务页面");
                } else {
                    showToast("无法通过ADB命令打开多任务页面");
                }
                
            } catch (Exception e) {
                Log.e(TAG, "通过ADB命令打开多任务页面时出错", e);
                showToast("执行ADB命令时出错: " + e.getMessage());
            }
        }).start();
    }
    
    /**
     * 在主线程显示Toast消息
     */
    private void showToast(String message) {
        if (mContext != null) {
            ((android.app.Activity) mContext).runOnUiThread(() -> {
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            });
        }
    }
}