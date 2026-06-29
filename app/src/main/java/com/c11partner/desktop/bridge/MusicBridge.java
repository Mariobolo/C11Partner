package com.c11partner.desktop.bridge;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import com.c11partner.desktop.MainActivity;
import com.c11partner.desktop.service.MediaSessionService;
import com.c11partner.desktop.service.MusicNotificationListenerService;
import com.c11partner.desktop.utils.CarControlManager;
import com.c11partner.desktop.utils.MusicUtils;
import org.json.JSONObject;
/**
 * 音乐功能Bridge类
 * 处理所有音乐相关的JavaScript交互接口
 * 
 * 功能说明：
 * 1. 音乐播放控制（播放、暂停、上一首、下一首）
 * 2. 音乐状态查询（播放状态、当前歌曲、进度信息）
 * 3. 音乐可视化控制
 * 4. 系统音乐信息获取
 */
public class MusicBridge extends BaseBridge {
    private static final String TAG = "MusicBridge";
    private MainActivity mActivity;
    private MusicUtils musicUtils;
    private MediaSessionService mediaSessionService;
    private boolean isMediaSessionServiceBound = false;
    /**
     * 构造函数
     *
     * @param context  应用上下文
     * @param activity MainActivity实例
     */
    public MusicBridge(Context context, MainActivity activity) {
        super(context, activity);
        this.mActivity = activity;
        this.musicUtils = new MusicUtils(context);
    }
    /**
     * 设置媒体会话服务
     *
     * @param service 媒体会话服务实例
     * @param isBound 是否已绑定
     */
    public void setMediaSessionService(MediaSessionService service, boolean isBound) {
        this.mediaSessionService = service;
        this.isMediaSessionServiceBound = isBound;
    }
    // ==================== 音乐可视化相关方法 ====================
    /**
     * 开始音乐可视化
     */
    @JavascriptInterface
    public void startMusicVisualizer() {
        logD(TAG, "收到启动音乐可视化的JavaScript调用");
        if (!isActivityValid()) {
            logD(TAG, "Activity无效，无法启动音乐可视化");
            return;
        }
        mActivity.runOnUiThread(() -> {
            if (mActivity.getMusicVisualizer() != null) {
                try {
                    logD(TAG, "正在启动音乐可视化");
                    mActivity.getMusicVisualizer().startVisualizer();
                    logD(TAG, "音乐可视化启动完成");
                } catch (Exception e) {
                    logE(TAG, "启动音乐可视化时出错", e);
                }
            } else {
                logD(TAG, "musicVisualizer对象为空");
            }
        });
    }
    /**
     * 停止音乐可视化
     */
    @JavascriptInterface
    public void stopMusicVisualizer() {
        logD(TAG, "收到停止音乐可视化的JavaScript调用");
        if (!isActivityValid()) {
            logD(TAG, "Activity无效，无法停止音乐可视化");
            return;
        }
        mActivity.runOnUiThread(() -> {
            if (mActivity.getMusicVisualizer() != null) {
                try {
                    mActivity.getMusicVisualizer().stopVisualizer();
                    logD(TAG, "音乐可视化已停止");
                } catch (Exception e) {
                    logE(TAG, "停止音乐可视化时出错", e);
                }
            }
        });
    }
    // ==================== 音乐状态查询方法 ====================
    /**
     * 检查音乐是否正在播放
     *
     * @return 音乐播放状态
     */
    @JavascriptInterface
    public boolean isMusicPlaying() {
        return mActivity.isMusicPlaying();
    }
    /**
     * 获取当前音乐名称
     *
     * @return 当前音乐名称
     */
    @JavascriptInterface
    public String getCurrentMusicName() {
        if (isMediaSessionServiceBound && mediaSessionService != null) {
            return mediaSessionService.getCurrentMusicName();
        }
        return "此刻无声，佳音已备候君启...";
    }

    /**
     * 获取当前音乐艺术家
     *
     * @return 当前音乐艺术家名称
     */
    @JavascriptInterface
    public String getCurrentMusicArtist() {
        if (isMediaSessionServiceBound && mediaSessionService != null) {
            return mediaSessionService.getCurrentMusicArtist();
        }
        return "未知艺术家";
    }
    /**
     * 获取音乐播放进度信息
     *
     * @return 音乐进度信息JSON字符串
     */
    @JavascriptInterface
    public String getMusicProgressInfo() {
        try {
            if (isMediaSessionServiceBound && mediaSessionService != null) {
                JSONObject progressInfo = new JSONObject();
                progressInfo.put("isPlaying", mediaSessionService.isPlaying());
                progressInfo.put("currentPosition", mediaSessionService.getCurrentPosition());
                progressInfo.put("duration", mediaSessionService.getDuration());
                return progressInfo.toString();
            }
        } catch (Exception e) {
            logE(TAG, "获取音乐进度信息失败", e);
        }
        try {
            JSONObject defaultInfo = new JSONObject();
            defaultInfo.put("isPlaying", false);
            defaultInfo.put("currentPosition", 0);
            defaultInfo.put("duration", 0);
            return defaultInfo.toString();
        } catch (Exception e) {
            return "{\"isPlaying\":false,\"currentPosition\":0,\"duration\":0}";
        }
    }
    /**
     * 获取系统音乐信息（从通知监听服务）
     *
     * @return 音乐信息JSON字符串
     */
    @JavascriptInterface
    public String getSystemMusicInfo() {
        try {
            JSONObject musicInfo = new JSONObject();
            
            // 从通知监听服务获取音乐信息
            String title = MusicNotificationListenerService.getCurrentTitle();
            String artist = MusicNotificationListenerService.getCurrentArtist();
            boolean isPlaying = MusicNotificationListenerService.isPlaying();
            
            musicInfo.put("title", title);
            musicInfo.put("artist", artist);
            musicInfo.put("isPlaying", isPlaying);
            musicInfo.put("hasData", title != null && !title.isEmpty());
            
            return musicInfo.toString();
        } catch (Exception e) {
            logE(TAG, "获取系统音乐信息失败", e);
            try {
                JSONObject defaultInfo = new JSONObject();
                defaultInfo.put("title", "");
                defaultInfo.put("artist", "");
                defaultInfo.put("isPlaying", false);
                defaultInfo.put("hasData", false);
                return defaultInfo.toString();
            } catch (Exception ex) {
                return "{}";
            }
        }
    }
    // ==================== 音乐播放控制方法 ====================
    /**
     * 播放/暂停音乐
     */
    @JavascriptInterface
    public void playPauseMusic() {
        try {
            // 使用 CarControlManager 发送方控按键
            CarControlManager carControl = CarControlManager.getInstance(mContext);
            // 发送播放/暂停按键（通过方控）
            // 注意：零跑C11方控可能没有播放暂停键，尝试使用媒体按钮广播
            sendMediaButton(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
        } catch (Exception e) {
            logE(TAG, "播放/暂停音乐失败", e);
        }
    }
    /**
     * 下一首
     */
    @JavascriptInterface
    public void nextMusic() {
        try {
            CarControlManager carControl = CarControlManager.getInstance(mContext);
            carControl.sendNextTrack();
        } catch (Exception e) {
            logE(TAG, "下一首失败", e);
            // 降级方案：发送媒体按钮广播
            sendMediaButton(KeyEvent.KEYCODE_MEDIA_NEXT);
        }
    }
    /**
     * 上一首
     */
    @JavascriptInterface
    public void prevMusic() {
        try {
            CarControlManager carControl = CarControlManager.getInstance(mContext);
            carControl.sendPrevTrack();
        } catch (Exception e) {
            logE(TAG, "上一首失败", e);
            // 降级方案：发送媒体按钮广播
            sendMediaButton(KeyEvent.KEYCODE_MEDIA_PREVIOUS);
        }
    }
    /**
     * 播放/暂停音乐（兼容旧接口 - 已废弃，请使用playPauseMusic）
     * @deprecated 建议使用 playPauseMusic() 替代
     */
    @Deprecated
    @JavascriptInterface
    public void playPause() {
        logD(TAG, "playPause()已废弃，建议使用playPauseMusic()");
        playPauseMusic();
    }
    /**
     * 下一首（兼容旧接口 - 已废弃，请使用nextMusic）
     * @deprecated 建议使用 nextMusic() 替代
     */
    @Deprecated
    @JavascriptInterface
    public void playNext() {
        logD(TAG, "playNext()已废弃，建议使用nextMusic()");
        nextMusic();
    }
    /**
     * 上一首（兼容旧接口 - 已废弃，请使用prevMusic）
     * @deprecated 建议使用 prevMusic() 替代
     */
    @Deprecated
    @JavascriptInterface
    public void playPrevious() {
        logD(TAG, "playPrevious()已废弃，建议使用prevMusic()");
        prevMusic();
    }
    // ==================== 辅助方法 ====================
    
    /**
     * 安全执行媒体会话操作（统一错误处理）
     *
     * @param action 要执行的操作
     * @param actionName 操作名称（用于日志）
     */
    private void safeMediaSessionAction(Runnable action, String actionName) {
        try {
            if (isMediaSessionServiceBound && mediaSessionService != null) {
                action.run();
                logD(TAG, actionName + " 执行成功");
            } else {
                logD(TAG, actionName + " 跳过：媒体会话服务未绑定");
            }
        } catch (Exception e) {
            logE(TAG, actionName + " 失败", e);
        }
    }
    
    /**
     * 安全获取媒体会话数据（统一错误处理）
     *
     * @param supplier 数据提供者
     * @param defaultValue 默认值
     * @param actionName 操作名称（用于日志）
     * @param <T> 返回值类型
     * @return 获取到的数据或默认值
     */
    private <T> T safeMediaSessionGet(java.util.function.Supplier<T> supplier, T defaultValue, String actionName) {
        try {
            if (isMediaSessionServiceBound && mediaSessionService != null) {
                return supplier.get();
            }
        } catch (Exception e) {
            logE(TAG, actionName + " 失败", e);
        }
        return defaultValue;
    }
    
    /**
     * 安全执行UI线程操作
     *
     * @param action 要执行的操作
     * @param actionName 操作名称（用于日志）
     */
    private void safeUiThreadAction(Runnable action, String actionName) {
        if (!isActivityValid()) {
            logD(TAG, actionName + " 跳过：Activity无效");
            return;
        }
        try {
            mActivity.runOnUiThread(action);
        } catch (Exception e) {
            logE(TAG, actionName + " 失败", e);
        }
    }
    
    /**
     * 安全启动Activity（统一错误处理）
     *
     * @param intent 启动Intent
     * @param activityName Activity名称（用于日志）
     */
    private void safeStartActivity(Intent intent, String activityName) {
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            logD(TAG, "启动" + activityName + "成功");
        } catch (Exception e) {
            logE(TAG, "启动" + activityName + "失败", e);
        }
    }
    
    /**
     * 检查媒体会话服务是否可用
     *
     * @return 媒体会话服务是否可用
     */
    private boolean isMediaSessionAvailable() {
        return isMediaSessionServiceBound && mediaSessionService != null;
    }
    
    /**
     * 发送媒体按钮广播
     *
     * @param keyCode 按键代码
     */
    private void sendMediaButton(int keyCode) {
        try {
            Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
            Bundle extras = new Bundle();
            extras.putParcelable(Intent.EXTRA_KEY_EVENT, 
                new KeyEvent(KeyEvent.ACTION_DOWN, keyCode));
            intent.putExtras(extras);
            mContext.sendBroadcast(intent);
            
            // 再发送一个ACTION_UP
            Intent intentUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
            Bundle extrasUp = new Bundle();
            extrasUp.putParcelable(Intent.EXTRA_KEY_EVENT, 
                new KeyEvent(KeyEvent.ACTION_UP, keyCode));
            intentUp.putExtras(extrasUp);
            mContext.sendBroadcast(intentUp);
            
            logD(TAG, "发送媒体按钮: " + keyCode);
        } catch (Exception e) {
            logE(TAG, "发送媒体按钮失败", e);
        }
    }
    // ==================== 音乐音量控制方法 ====================
    /**
     * 设置音乐音量
     *
     * @param volume 音量值（0-15）
     */
    @JavascriptInterface
    public void setMusicVolume(int volume) {
        try {
            musicUtils.setMusicVolume(volume);
            logD(TAG, "设置音乐音量: " + volume);
        } catch (Exception e) {
            logE(TAG, "设置音乐音量失败", e);
        }
    }
    /**
     * 获取当前音乐音量
     *
     * @return 当前音量值
     */
    @JavascriptInterface
    public int getMusicVolume() {
        try {
            return musicUtils.getMusicVolume();
        } catch (Exception e) {
            logE(TAG, "获取音乐音量失败", e);
            return 7; // 默认中间音量
        }
    }
    /**
     * 音乐音量增加
     */
    @JavascriptInterface
    public void volumeUp() {
        try {
            int current = getMusicVolume();
            setMusicVolume(Math.min(current + 1, 15));
        } catch (Exception e) {
            logE(TAG, "音量增加失败", e);
        }
    }
    /**
     * 音乐音量减少
     */
    @JavascriptInterface
    public void volumeDown() {
        try {
            int current = getMusicVolume();
            setMusicVolume(Math.max(current - 1, 0));
        } catch (Exception e) {
            logE(TAG, "音量减少失败", e);
        }
    }
    // ==================== 播放进度控制方法 ====================
    /**
     * 跳转到指定播放位置
     *
     * @param position 播放位置（毫秒）
     */
    @JavascriptInterface
    public void seekTo(final long position) {
        safeMediaSessionAction(() -> {
            mediaSessionService.seekTo(position);
            logD(TAG, "跳转到播放位置: " + position);
        }, "跳转播放位置");
    }
    /**
     * 设置播放速度
     *
     * @param speed 播放速度（0.5-2.0）
     */
    @JavascriptInterface
    public void setPlaybackSpeed(final float speed) {
        safeMediaSessionAction(() -> {
            mediaSessionService.setPlaybackSpeed(speed);
            logD(TAG, "设置播放速度: " + speed);
        }, "设置播放速度");
    }
    // ==================== 通知监听权限相关方法 ====================
    /**
     * 检查通知监听权限是否开启
     *
     * @return 是否已开启通知监听权限
     */
    @JavascriptInterface
    public boolean isNotificationListenerEnabled() {
        try {
            String packageName = mContext.getPackageName();
            String flat = android.provider.Settings.Secure.getString(
                mContext.getContentResolver(),
                "enabled_notification_listeners");
            if (flat != null) {
                return flat.contains(packageName);
            }
            return false;
        } catch (Exception e) {
            logE(TAG, "检查通知监听权限失败", e);
            return false;
        }
    }
    /**
     * 打开通知监听设置页面
     */
    @JavascriptInterface
    public void openNotificationListenerSettings() {
        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        safeStartActivity(intent, "通知监听设置");
    }
    
    // ==================== 音乐收藏与播放列表方法 ====================
    /**
     * 获取当前播放列表
     *
     * @return 播放列表JSON字符串
     */
    @JavascriptInterface
    public String getPlaylist() {
        try {
            if (isMediaSessionServiceBound && mediaSessionService != null) {
                return mediaSessionService.getPlaylist();
            }
        } catch (Exception e) {
            logE(TAG, "获取播放列表失败", e);
        }
        return "[]";
    }
    
    /**
     * 播放指定索引的歌曲
     *
     * @param index 歌曲索引
     */
    @JavascriptInterface
    public void playSongAtIndex(final int index) {
        safeMediaSessionAction(() -> {
            mediaSessionService.playSongAtIndex(index);
            logD(TAG, "播放第 " + index + " 首歌曲");
        }, "播放指定歌曲");
    }
    
    /**
     * 切换循环模式
     *
     * @param mode 循环模式：0-不循环，1-单曲循环，2-列表循环
     */
    @JavascriptInterface
    public void setRepeatMode(final int mode) {
        safeMediaSessionAction(() -> {
            mediaSessionService.setRepeatMode(mode);
            logD(TAG, "设置循环模式: " + mode);
        }, "设置循环模式");
    }
    
    /**
     * 获取当前循环模式
     *
     * @return 循环模式
     */
    @JavascriptInterface
    public int getRepeatMode() {
        return safeMediaSessionGet(
            () -> mediaSessionService.getRepeatMode(),
            0,
            "获取循环模式"
        );
    }
    
    /**
     * 切换随机播放模式
     *
     * @param enabled 是否启用随机播放
     */
    @JavascriptInterface
    public void setShuffleMode(final boolean enabled) {
        safeMediaSessionAction(() -> {
            mediaSessionService.setShuffleMode(enabled);
            logD(TAG, "设置随机播放: " + enabled);
        }, "设置随机播放");
    }
    
    /**
     * 获取随机播放状态
     *
     * @return 是否启用随机播放
     */
    @JavascriptInterface
    public boolean isShuffleEnabled() {
        return safeMediaSessionGet(
            () -> mediaSessionService.isShuffleEnabled(),
            false,
            "获取随机播放状态"
        );
    }
}
