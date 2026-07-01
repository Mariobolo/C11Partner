package com.c11partner.desktop.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.c11partner.desktop.MainActivity;
import com.c11partner.desktop.adb.UsbDebugConnection;
import com.c11partner.desktop.bridge.WebViewBridge;
import com.c11partner.desktop.database.AppDatabaseHelper;
import com.c11partner.desktop.database.ComponentConfigDatabaseHelper;
import com.c11partner.desktop.database.ConfigAppDatabaseHelper;
import com.c11partner.desktop.database.QuickAppDatabaseHelper;
import com.c11partner.desktop.database.WallpaperCategoryDatabaseHelper;
import com.c11partner.desktop.database.WallpaperSettingsDatabaseHelper;
import com.c11partner.desktop.service.MediaSessionService;
import com.c11partner.desktop.service.MusicService;

/**
 * 初始化管理器
 * 负责管理应用的所有初始化工作
 */
public class InitManager {
    private MainActivity activity;
    private Context context;

    public InitManager(MainActivity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }

    /**
     * 初始化核心组件
     */
    public void initCoreComponents() {
        // 初始化WebView桥接器
        activity.webViewBridge = new WebViewBridge(activity, activity);
        // 初始化USB调试连接
        activity.usbDebugConnection = new UsbDebugConnection(activity);
        // 初始化Quote API工具
        activity.quoteApiUtils = new QuoteApiUtils();
        // 初始化音频管理器
        activity.audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    /**
     * 初始化数据库辅助类
     */
    public void initDatabaseHelpers() {
        activity.dbHelper = AppDatabaseHelper.getInstance(context);
        activity.quickAppDbHelper = QuickAppDatabaseHelper.getInstance(context);
        activity.wallpaperDbHelper = WallpaperCategoryDatabaseHelper.getInstance(context);
        activity.wallpaperSettingsDbHelper = WallpaperSettingsDatabaseHelper.getInstance(context);
        activity.configAppDbHelper = ConfigAppDatabaseHelper.getInstance(context);
        activity.componentConfigDbHelper = ComponentConfigDatabaseHelper.getInstance(context);
    }

    /**
     * 初始化广播接收器
     */
    public void initReceivers() {
        // 初始化壁纸设置更改广播接收器
        activity.initWallpaperSettingsReceiver();
        // 初始化重启应用广播接收器
        activity.initRestartAppReceiver();
    }

    /**
     * 初始化服务
     */
    public void initServices() {
        // 绑定媒体会话服务
        Intent mediaSessionIntent = new Intent(context, MediaSessionService.class);
        activity.bindService(mediaSessionIntent, activity.mediaSessionServiceConnection, Context.BIND_AUTO_CREATE);

        // 启动音乐服务
        Intent musicServiceIntent = new Intent(context, MusicService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(musicServiceIntent);
        } else {
            context.startService(musicServiceIntent);
        }

        // 启动媒体会话服务
        Intent mediaSessionServiceIntent = new Intent(context, MediaSessionService.class);
        context.startService(mediaSessionServiceIntent);
    }

    /**
     * 初始化权限
     */
    public void initPermissions() {
        // 录音权限已移除（应用不需要麦克风）
        activity.requestStoragePermission();
        requestNotificationListenerPermission();
    }

    /**
     * 请求通知监听权限
     * 注：已移除权限申请弹窗，改为静默处理
     */
    private void requestNotificationListenerPermission() {
        //移除权限申请弹窗，让系统静默处理
        Log.d("InitManager", "音乐权限检查已跳过，使用dumpsys方式获取音乐信息");
    }

    /**
     * 初始化组件
     */
    public void initComponents() {
        activity.initMusicVisualizer();
        activity.initTextToSpeech();
    }

    /**
     * 启动各项任务
     */
    public void startTasks() {
        activity.startMusicPlaybackCheck();
        activity.startTimeUpdate();
        activity.startWallpaperCarousel();
        activity.scheduleDelayedStartupTasks();
    }
}
