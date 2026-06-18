package com.c11partner.desktop.utils;

import android.os.Handler;
import android.util.Log;

import com.c11partner.desktop.MainActivity;

/**
 * 任务管理器
 * 负责管理应用的各种定时任务和后台任务
 */
public class TaskManager {
    private MainActivity activity;
    private Handler handler;

    public TaskManager(MainActivity activity) {
        this.activity = activity;
        this.handler = new Handler();
    }

    /**
     * 安排延迟启动任务
     */
    public void scheduleDelayedStartupTasks() {
        try {

            if (activity.bootGreetingEnabled) {
                activity.bootGreetingRunnable = () -> activity.playRandomGreeting();
                handler.postDelayed(activity.bootGreetingRunnable, 30000);
                Log.d("TaskManager", "已安排30秒后播放问候语");
            }
        } catch (Exception e) {
            Log.e("TaskManager", "安排延迟启动任务时出错", e);
        }
    }

    /**
     * 取消所有延迟启动任务
     */
    public void cancelDelayedStartupTasks() {
        }
        if (activity.bootGreetingRunnable != null) {
            handler.removeCallbacks(activity.bootGreetingRunnable);
        }
    }

    /**
     * 启动音乐播放状态检查
     */
    public void startMusicPlaybackCheck() {
        activity.musicCheckRunnable = new Runnable() {
            @Override
            public void run() {
                activity.checkMusicPlaybackState();
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(activity.musicCheckRunnable);
    }

    /**
     * 停止音乐播放状态检查
     */
    public void stopMusicPlaybackCheck() {
        if (activity.musicCheckRunnable != null) {
            handler.removeCallbacks(activity.musicCheckRunnable);
        }
    }

    /**
     * 启动壁纸轮播
     */
    public void startWallpaperCarousel() {
        activity.stopWallpaperCarousel();

        try {
            // 从数据库读取壁纸设置
            boolean isWallpaperCarouselEnabled = activity.isWallpaperCarouselEnabled;
            int wallpaperSwitchInterval = activity.wallpaperSwitchInterval;

            // 只有在启用壁纸轮播且应用在前台时才启动定时器
            if (isWallpaperCarouselEnabled && activity.isAppInForeground) {
                activity.wallpaperRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // 通知前端更新壁纸
                        if (activity.webViewBridge != null) {
                            activity.webViewBridge.notifyWallpaperUpdate();
                        }
                        // 继续下一次轮播
                        handler.postDelayed(this, wallpaperSwitchInterval);
                    }
                };

                handler.postDelayed(activity.wallpaperRunnable, wallpaperSwitchInterval);
                Log.d("TaskManager", "壁纸轮播定时器已启动，间隔: " + wallpaperSwitchInterval + "ms");
            } else {
                Log.d("TaskManager", "壁纸轮播未启用或应用在后台");
            }
        } catch (Exception e) {
            Log.e("TaskManager", "启动壁纸轮播时出错", e);
        }
    }

    /**
     * 暂停壁纸轮播
     */
    public void pauseWallpaperCarousel() {
        if (activity.wallpaperRunnable != null) {
            handler.removeCallbacks(activity.wallpaperRunnable);
            Log.d("TaskManager", "壁纸轮播已暂停（应用进入后台）");
        }
    }

    /**
     * 停止壁纸轮播
     */
    public void stopWallpaperCarousel() {
        if (activity.wallpaperRunnable != null) {
            handler.removeCallbacks(activity.wallpaperRunnable);
            activity.wallpaperRunnable = null;
            Log.d("TaskManager", "壁纸轮播定时器已停止");
        }
    }
}
