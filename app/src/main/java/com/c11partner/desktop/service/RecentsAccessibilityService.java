package com.c11partner.desktop.service;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

/**
 * 最近任务辅助服务
 * 提供打开最近任务页面的功能，通过辅助功能权限实现
 */
public class RecentsAccessibilityService extends AccessibilityService {
    private static final String TAG = "RecentsAccessibilityService";
    
    /**
     * 广播接收器
     * 接收打开最近任务页面的广播命令
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        /**
         * 接收广播事件
         * 当接收到打开最近任务页面的广播时，执行相应操作
         *
         * @param context 上下文
         * @param intent  广播意图
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.c11partner.desktop.OPEN_RECENTS".equals(intent.getAction())) {
                Log.d(TAG, "Received OPEN_RECENTS broadcast");
                openRecents();
            }
        }
    };
    
    /**
     * 服务连接时调用
     * 注册广播接收器
     */
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        // 注册广播接收器
        IntentFilter filter = new IntentFilter("com.c11partner.desktop.OPEN_RECENTS");
        registerReceiver(receiver, filter);
        Log.d(TAG, "RecentsAccessibilityService connected and receiver registered");
    }
    
    // 添加一个标志来防止重复调用
    private boolean isProcessing = false;
    
    /**
     * 处理辅助功能事件
     * 此服务不需要处理辅助功能事件
     *
     * @param event 辅助功能事件
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // 不需要处理事件
        // 但为了防止意外触发，添加一个空实现
    }
    
    /**
     * 处理中断事件
     * 此服务不需要处理中断事件
     */
    @Override
    public void onInterrupt() {
        // 不需要处理中断
    }
    
    /**
     * 打开最近任务页面
     * 使用辅助功能的全局操作打开最近任务页面
     */
    public void openRecents() {
        // 防止重复调用
        if (isProcessing) {
            Log.d(TAG, "Already processing, ignoring duplicate call");
            return;
        }
        
        isProcessing = true;
        Log.d(TAG, "Opening recents screen");
        performGlobalAction(GLOBAL_ACTION_RECENTS);
        
        // 在短时间内重置处理标志
        new android.os.Handler().postDelayed(() -> {
            isProcessing = false;
        }, 500); // 500毫秒后重置
    }
    
    /**
     * 服务销毁时调用
     * 注销广播接收器
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        // 注销广播接收器
        try {
            unregisterReceiver(receiver);
        } catch (IllegalArgumentException e) {
            // 接收器可能未注册
            Log.w(TAG, "Receiver not registered", e);
        }
    }
}