package com.c11partner.desktop.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.c11partner.desktop.MainActivity;
import com.c11partner.desktop.service.MediaSessionService;

/**
 * 服务管理器
 * 负责管理应用的服务绑定和解除绑定
 */
public class ServiceManager {
    private MainActivity activity;
    private Context context;

    public ServiceManager(MainActivity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }

    /**
     * 绑定媒体会话服务
     */
    public void bindMediaSessionService() {
        Intent mediaSessionIntent = new Intent(context, MediaSessionService.class);
        activity.bindService(mediaSessionIntent, mediaSessionServiceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 解绑所有服务
     */
    public void unbindAllServices() {
        if (activity.isMediaSessionServiceBound) {
            activity.unbindService(mediaSessionServiceConnection);
            activity.isMediaSessionServiceBound = false;
        }
    }

    /**
     * 媒体会话服务连接
     */
    private ServiceConnection mediaSessionServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                MediaSessionService.LocalBinder binder = (MediaSessionService.LocalBinder) service;
                activity.mediaSessionService = binder.getService();
                activity.isMediaSessionServiceBound = true;
                Log.d("ServiceManager", "媒体会话服务绑定成功");
            } catch (Exception e) {
                Log.e("ServiceManager", "媒体会话服务绑定失败", e);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            activity.mediaSessionService = null;
            activity.isMediaSessionServiceBound = false;
            Log.d("ServiceManager", "媒体会话服务解绑");
        }
    };
}
