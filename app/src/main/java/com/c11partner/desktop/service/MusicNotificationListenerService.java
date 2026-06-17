package com.c11partner.desktop.service;

import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class MusicNotificationListenerService extends NotificationListenerService {
    private static final String TAG = "MusicNotificationListener";
    private static MusicNotificationListenerService instance;

    public static MusicNotificationListenerService getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Log.d(TAG, "MusicNotificationListenerService created");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
        Log.d(TAG, "MusicNotificationListenerService destroyed");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        // 可以在这里处理通知，但主要功能是通过MediaSessionManager获取媒体会话
        Log.d(TAG, "Notification posted: " + sbn.getPackageName());
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.d(TAG, "Notification removed: " + sbn.getPackageName());
    }
}