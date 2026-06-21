package com.c11partner.desktop.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

/**
 * 无障碍服务类，用于执行系统级操作如打开多任务页面
 */
public class MyAccessibilityService extends AccessibilityService {
    private static final String TAG = "MyAccessibilityService";
    public static final String ACTION_OPEN_RECENTS = "com.c11partner.desktop.action.OPEN_RECENTS";
    
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // 不需要处理AccessibilityEvent
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "无障碍服务被中断");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && ACTION_OPEN_RECENTS.equals(intent.getAction())) {
            // 执行打开多任务页面操作
            performGlobalAction(GLOBAL_ACTION_RECENTS);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d(TAG, "无障碍服务已连接");
    }
}