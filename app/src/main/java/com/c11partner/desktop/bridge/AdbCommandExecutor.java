package com.c11partner.desktop.bridge;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.c11partner.desktop.adb.AdbCommandProcessor;

/**
 * ADB命令执行助手类
 * 提供通过ADB命令执行系统功能的方法
 */
public class AdbCommandExecutor {
    private static final String TAG = "AdbCommandExecutor";
    private Context mContext;
    
    public AdbCommandExecutor(Context context) {
        this.mContext = context;
    }
    
    /**
     * 执行ADB shell命令
     * @param command 要执行的命令
     */
    public void executeAdbCommand(String command) {
        new Thread(() -> {
            try {
                Log.d(TAG, "执行ADB命令: " + command);
                
                // 使用ADB命令处理器执行命令
                AdbCommandProcessor processor = new AdbCommandProcessor(mContext);
                boolean success = processor.executeCommand(command);
                
                if (success) {
                    showToast("ADB命令执行成功: " + command);
                } else {
                    showToast("ADB命令执行失败，无法连接ADB");
                }
                
            } catch (Exception e) {
                Log.e(TAG, "执行ADB命令时出错: " + command, e);
                showToast("执行ADB命令时出错: " + e.getMessage());
            }
        }).start();
    }
    
    /**
     * 打开系统多任务页面
     */
    public void openRecentsViaAdb() {
        executeAdbCommand("input keyevent 187");
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