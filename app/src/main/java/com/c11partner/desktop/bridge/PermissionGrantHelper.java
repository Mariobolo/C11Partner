package com.c11partner.desktop.bridge;

import android.webkit.JavascriptInterface;
import android.util.Log;
import android.widget.Toast;
import com.c11partner.desktop.adb.AdbCommandProcessor;

/**
 * 权限授予助手类
 * 提供一键授予权限的功能
 */
public class PermissionGrantHelper {
    private static final String TAG = "PermissionGrantHelper";
    private android.content.Context mContext;
    private android.app.Activity mActivity;
    
    public PermissionGrantHelper(android.app.Activity activity) {
        this.mActivity = activity;
        this.mContext = activity.getApplicationContext();
    }
    
    /**
     * 一键授予权限
     * 授予应用所需的READ_LOGS和DUMP权限
     */
    @JavascriptInterface
    public void grantPermissions() {
        new Thread(() -> {
            try {
                Log.d(TAG, "开始授予必要权限");
                
                // 使用ADB命令处理器执行权限授权
                AdbCommandProcessor processor = new AdbCommandProcessor(mContext);
                
                // 授予READ_LOGS权限
                String command1 = "pm grant com.c11partner.desktop.android.permission.READ_LOGS";
                boolean result1 = processor.executeCommand(command1);
                Log.d(TAG, "READ_LOGS权限授予结果: " + result1);
                
                // 授予DUMP权限
                String command2 = "pm grant com.c11partner.desktop.android.permission.DUMP";
                boolean result2 = processor.executeCommand(command2);
                Log.d(TAG, "DUMP权限授予结果: " + result2);
                
                mActivity.runOnUiThread(() -> {
                    if (result1 && result2) {
                        Toast.makeText(mContext, "权限授予成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "部分权限授予失败", Toast.LENGTH_LONG).show();
                    }
                });
                
            } catch (Exception e) {
                Log.e(TAG, "授予权限时出错", e);
                mActivity.runOnUiThread(() -> {
                    Toast.makeText(mContext, "权限授予失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }
}